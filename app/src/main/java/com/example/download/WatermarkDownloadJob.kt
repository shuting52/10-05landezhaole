package com.example.download

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

// ============================================================
// 2026-10-23：去水印下载任务编排（jicun 式）。
// JicunDownloader 收到缓存文件 → 校验 → 登记媒体库 → 清理。
// ============================================================

data class WatermarkDownloadItem(
    val url: String,
    val filename: String,
    val dirType: String // Environment.DIRECTORY_MOVIES / PICTURES / MUSIC
)

/**
 * 下载任务 UI 状态（进度弹窗绑定）。
 * 2026-10-23 修复：下载器 16 条线程并发回调进度，Compose mutableStateOf 非线程安全，
 * 改用 StateFlow（value 写线程安全），UI 层 collectAsState 订阅。
 */
class DownloadJobState(val title: String, val totalItems: Int) {
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()
    private val _speedText = MutableStateFlow("")
    val speedText: StateFlow<String> = _speedText.asStateFlow()
    private val _etaText = MutableStateFlow("")
    val etaText: StateFlow<String> = _etaText.asStateFlow()
    private val _done = MutableStateFlow(false)
    val done: StateFlow<Boolean> = _done.asStateFlow()
    private val _failed = MutableStateFlow(false)
    val failed: StateFlow<Boolean> = _failed.asStateFlow()
    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()
    private val _doneCount = MutableStateFlow(0)
    val doneCount: StateFlow<Int> = _doneCount.asStateFlow()
    val cancelled = AtomicBoolean(false)

    // 线程安全更新（下载器 IO 线程直接调）
    fun setProgress(v: Float) { _progress.value = v.coerceIn(0f, 1f) }
    fun setSpeedText(v: String) { _speedText.value = v }
    fun setEtaText(v: String) { _etaText.value = v }
    fun setDoneCount(v: Int) { _doneCount.value = v }
    fun markDone() { _progress.value = 1f; _speedText.value = ""; _etaText.value = ""; _done.value = true }
    fun markFailed(msg: String) { _errorMsg.value = msg; _failed.value = true }
}

private fun mimeFor(filename: String, dirType: String): String {
    val ext = filename.substringAfterLast('.', "").lowercase()
    return when (ext) {
        "mp4", "mov" -> "video/mp4"
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "webp" -> "image/webp"
        "gif" -> "image/gif"
        "mp3" -> "audio/mpeg"
        "m4a" -> "audio/mp4"
        "wav" -> "audio/wav"
        else -> when (dirType) {
            Environment.DIRECTORY_MOVIES -> "video/mp4"
            Environment.DIRECTORY_MUSIC -> "audio/mpeg"
            else -> "image/jpeg"
        }
    }
}

/**
 * 把收好的文件登记进系统媒体库（jicun MediaPublisher 简化版）。
 * API 29+ 走 MediaStore + RELATIVE_PATH；以下直接写公共目录 + 扫媒体库。
 */
fun publishToGallery(context: Context, src: File, displayName: String, dirType: String): Boolean {
    return try {
        if (Build.VERSION.SDK_INT >= 29) {
            val collection: Uri = when (dirType) {
                Environment.DIRECTORY_MOVIES ->
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                Environment.DIRECTORY_MUSIC ->
                    MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                else ->
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            }
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeFor(displayName, dirType))
                put(MediaStore.MediaColumns.RELATIVE_PATH, "$dirType/懒得找了")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
            val uri = context.contentResolver.insert(collection, values) ?: return false
            try {
                context.contentResolver.openOutputStream(uri)?.use { out ->
                    src.inputStream().use { it.copyTo(out) }
                } ?: throw IllegalStateException("openOutputStream null")
                values.clear()
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                context.contentResolver.update(uri, values, null, null)
                true
            } catch (e: Exception) {
                try { context.contentResolver.delete(uri, null, null) } catch (_: Exception) { }
                false
            }
        } else {
            @Suppress("DEPRECATION")
            val pubDir = Environment.getExternalStoragePublicDirectory(dirType)
            val target = File(pubDir, "懒得找了/$displayName")
            target.parentFile?.mkdirs()
            src.copyTo(target, overwrite = true)
            MediaScannerConnection.scanFile(context, arrayOf(target.absolutePath), null, null)
            true
        }
    } catch (_: Exception) {
        false
    }
}

private fun formatSpeed(bps: Float): String = when {
    bps <= 0 -> ""
    bps >= 1024 * 1024 -> "%.1f MB/s".format(bps / (1024 * 1024))
    bps >= 1024 -> "%.0f KB/s".format(bps / 1024)
    else -> "%.0f B/s".format(bps)
}

/**
 * 执行一批去水印下载：逐项收流 → 入库。进度/速度/ETA 实时写回 job。
 * 在 Dispatchers.IO 调用。
 */
suspend fun runWatermarkDownload(
    context: Context,
    items: List<WatermarkDownloadItem>,
    job: DownloadJobState
) = withContext(Dispatchers.IO) {
    val doneSizes = LongArray(items.size)
    val itemTotals = LongArray(items.size) { -1L }
    var lastReportMs = 0L
    var lastBytes = 0L
    var lastTimeMs = System.currentTimeMillis()
    var lastSpeed = 0f

    try {
        items.forEachIndexed { idx, item ->
            if (job.cancelled.get()) throw JicunDownloader.CancelledException()
            val tmp = File(context.cacheDir, "wm_${System.currentTimeMillis()}_$idx.tmp")
            try {
                JicunDownloader.download(
                    url = item.url,
                    dest = tmp,
                    onProgress = { dl, total ->
                        doneSizes[idx] = dl
                        if (total > 0) itemTotals[idx] = total
                        val nowMs = System.currentTimeMillis()
                        if (nowMs - lastReportMs >= 200) {
                            lastReportMs = nowMs
                            val aggTotal = doneSizes.sum()
                            val dt = (nowMs - lastTimeMs) / 1000f
                            if (dt > 0) {
                                lastSpeed = (aggTotal - lastBytes) / dt
                                job.setSpeedText(formatSpeed(lastSpeed))
                                // ETA：已知总量时估算
                                val estTotal = itemTotals.filter { it > 0 }.sum()
                                job.setEtaText(
                                    if (lastSpeed > 64 * 1024 && estTotal > aggTotal) {
                                        val sec = ((estTotal - aggTotal) / lastSpeed).toLong()
                                        "还剩约 %d:%02d".format(sec / 60, sec % 60)
                                    } else ""
                                )
                                lastBytes = aggTotal
                                lastTimeMs = nowMs
                            }
                        }
                        // 进度：按项平均（每项权重相等，简单可靠）
                        val perItem = 1f / items.size
                        val itemFrac = if (total > 0) (dl.toFloat() / total).coerceIn(0f, 1f) else 0f
                        job.setProgress((idx + itemFrac) * perItem)
                    },
                    isCancelled = { job.cancelled.get() }
                )
                // 完整性校验
                if (!tmp.exists() || tmp.length() == 0L) throw IllegalStateException("文件为空")
                // 入库
                val ok = publishToGallery(context, tmp, item.filename, item.dirType)
                if (!ok) throw IllegalStateException("保存到相册失败")
                doneSizes[idx] = tmp.length().coerceAtLeast(doneSizes[idx])
                job.setDoneCount(idx + 1)
            } finally {
                try { tmp.delete() } catch (_: Exception) { }
            }
        }
        job.markDone()
    } catch (e: JicunDownloader.CancelledException) {
        job.markFailed("已取消")
    } catch (e: Exception) {
        job.markFailed(e.message ?: "下载失败")
    }
}
