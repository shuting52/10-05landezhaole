package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.util.MediaParseResult
import com.example.data.util.VideoLinkParser
import com.example.download.DownloadJobState
import com.example.download.WatermarkDownloadItem
import com.example.download.runWatermarkDownload
import com.example.ui.theme.LocalThemeUiColors
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 去水印（jicun 风格重写，2026-10-23）
 *
 * 流程对标 jicun (dhvbjvvb/jicun)：粘贴分享链接 → 开始解析 → 预览（播放器/图集/文案/选清晰度）
 * → 下载落盘 → 本地历史（最多 200 条）。
 * 解析引擎：本地 VideoLinkParser（直连平台接口，无需第三方密钥）。
 * 主题：jicun 浅色（蓝灰 #CDDCDC + 深蓝 #1257C9）。
 */

// ---------------- 历史记录模型（SharedPreferences + JSON，200 条上限） ----------------

private data class WatermarkHistoryItem(
    val title: String,
    val author: String,
    val platform: String,
    val coverUrl: String?,
    val videoUrl: String?,
    val imageCount: Int,
    val originalLink: String,
    val timestamp: Long
) {
    fun toJson() = JSONObject().apply {
        put("title", title); put("author", author); put("platform", platform)
        put("coverUrl", coverUrl ?: ""); put("videoUrl", videoUrl ?: "")
        put("imageCount", imageCount); put("originalLink", originalLink)
        put("timestamp", timestamp)
    }

    companion object {
        fun fromJson(o: JSONObject) = WatermarkHistoryItem(
            title = o.optString("title"), author = o.optString("author"),
            platform = o.optString("platform"),
            coverUrl = o.optString("coverUrl").ifBlank { null },
            videoUrl = o.optString("videoUrl").ifBlank { null },
            imageCount = o.optInt("imageCount"),
            originalLink = o.optString("originalLink"),
            timestamp = o.optLong("timestamp")
        )
    }
}

private const val HISTORY_PREFS = "jicun_watermark_history"
private const val HISTORY_KEY = "items"
private const val HISTORY_MAX = 200

private fun loadHistory(context: Context): MutableList<WatermarkHistoryItem> {
    val list = mutableListOf<WatermarkHistoryItem>()
    try {
        val raw = context.getSharedPreferences(HISTORY_PREFS, Context.MODE_PRIVATE)
            .getString(HISTORY_KEY, null) ?: return list
        val arr = JSONArray(raw)
        for (i in 0 until arr.length()) list += WatermarkHistoryItem.fromJson(arr.getJSONObject(i))
    } catch (_: Exception) { }
    return list
}

private fun saveHistory(context: Context, list: List<WatermarkHistoryItem>) {
    try {
        val arr = JSONArray()
        list.take(HISTORY_MAX).forEach { arr.put(it.toJson()) }
        context.getSharedPreferences(HISTORY_PREFS, Context.MODE_PRIVATE)
            .edit().putString(HISTORY_KEY, arr.toString()).apply()
    } catch (_: Exception) { }
}

// ---------------- 工具函数 ----------------

private fun extractUrl(input: String): String {
    val regex = "(https?://[a-zA-Z0-9./?=_%&:-]+)".toRegex()
    return regex.find(input)?.value ?: input.trim()
}

private fun detectPlatform(url: String): String {
    val lower = url.lowercase()
    return when {
        "douyin" in lower || "iesdouyin" in lower -> "抖音"
        "tiktok" in lower -> "TikTok"
        "kuaishou" in lower || "kwai" in lower -> "快手"
        "bilibili" in lower || "b23.tv" in lower -> "哔哩哔哩"
        "xiaohongshu" in lower || "xhslink" in lower -> "小红书"
        "weibo" in lower -> "微博"
        "ixigua" in lower -> "西瓜视频"
        "haokan" in lower -> "好看视频"
        "weishi" in lower -> "微视"
        "pipixia" in lower -> "皮皮虾"
        "x.com" in lower || "twitter" in lower -> "X"
        else -> "短视频"
    }
}

private fun formatTime(ts: Long): String =
    SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(ts))

// ---------------- 主界面 ----------------

@Composable
fun WatermarkScreen(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val theme = LocalThemeUiColors.current

    var inputText by remember { mutableStateOf("") }
    var isParsing by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<MediaParseResult?>(null) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var selectedQuality by remember(result) { mutableStateOf(0) }
    var fullscreenImage by remember { mutableStateOf<String?>(null) }
    // 批量解析
    var isBatchMode by remember { mutableStateOf(false) }
    val batchResults = remember { mutableStateListOf<MediaParseResult>() }
    var batchProgress by remember { mutableStateOf("") }
    // 2026-10-23：jicun 式分段下载任务 + 粘贴自动解析开关
    var downloadJob by remember { mutableStateOf<DownloadJobState?>(null) }
    var autoParseOnPaste by remember { mutableStateOf(true) }

    // 发起 jicun 分段下载
    fun requestDownload(items: List<WatermarkDownloadItem>, title: String) {
        if (items.isEmpty()) return
        val job = DownloadJobState(title, items.size)
        downloadJob = job
        scope.launch {
            runWatermarkDownload(context, items, job)
        }
    }

    val history = remember { mutableStateListOf<WatermarkHistoryItem>() }
    LaunchedEffect(Unit) {
        history.clear()
        history.addAll(loadHistory(context))
    }

    fun doParse(overrideText: String? = null) {
        val target = extractUrl(overrideText ?: inputText)
        if (target.isBlank() || (!target.startsWith("http://") && !target.startsWith("https://"))) {
            errorMsg = "请粘贴合法的分享链接"
            Toast.makeText(context, "请粘贴合法的分享链接", Toast.LENGTH_SHORT).show()
            return
        }
        isParsing = true
        errorMsg = null
        result = null
        scope.launch {
            try {
                val media = VideoLinkParser.resolveMedia(context, target)
                if (media.videoUrls.isEmpty() && media.imageUrls.isEmpty()) {
                    errorMsg = "解析失败：未能获取到无水印媒体，请检查链接是否有效"
                } else {
                    result = media
                    // 写入历史
                    val item = WatermarkHistoryItem(
                        title = media.title?.ifBlank { null } ?: "无标题",
                        author = media.author?.ifBlank { null } ?: "",
                        platform = media.platform?.ifBlank { null } ?: detectPlatform(target),
                        coverUrl = media.coverUrl,
                        videoUrl = media.videoUrls.firstOrNull(),
                        imageCount = media.imageUrls.size,
                        originalLink = target,
                        timestamp = System.currentTimeMillis()
                    )
                    history.removeAll { it.originalLink == target }
                    history.add(0, item)
                    if (history.size > HISTORY_MAX) history.removeRange(HISTORY_MAX, history.size)
                    saveHistory(context, history)
                }
            } catch (e: Exception) {
                errorMsg = "解析出错：${e.message}"
            } finally {
                isParsing = false
            }
        }
    }

    fun pasteFromClipboard() {
        try {
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val text = cm.primaryClip?.getItemAt(0)?.coerceToText(context)?.toString().orEmpty()
            if (text.isBlank()) {
                Toast.makeText(context, "剪贴板是空的", Toast.LENGTH_SHORT).show()
                return
            }
            inputText = text
            // jicun 式：粘贴后自动解析（批量模式不自动；可开关）
            if (!isBatchMode && autoParseOnPaste) doParse(text)
        } catch (e: Exception) {
            Toast.makeText(context, "读取剪贴板失败", Toast.LENGTH_SHORT).show()
        }
    }

    // 批量解析：每行一个链接
    fun doBatchParse() {
        val urls = inputText.lines()
            .map { extractUrl(it.trim()) }
            .filter { it.startsWith("http://") || it.startsWith("https://") }
            .distinct()
        if (urls.isEmpty()) {
            errorMsg = "请每行粘贴一个分享链接"
            Toast.makeText(context, "请每行粘贴一个分享链接", Toast.LENGTH_SHORT).show()
            return
        }
        isParsing = true
        errorMsg = null
        batchResults.clear()
        scope.launch {
            urls.forEachIndexed { idx, url ->
                batchProgress = "正在解析 ${idx + 1}/${urls.size}"
                try {
                    val media = VideoLinkParser.resolveMedia(context, url)
                    if (media.videoUrls.isNotEmpty() || media.imageUrls.isNotEmpty()) {
                        batchResults.add(media)
                        val item = WatermarkHistoryItem(
                            title = media.title?.ifBlank { null } ?: "无标题",
                            author = media.author?.ifBlank { null } ?: "",
                            platform = media.platform?.ifBlank { null } ?: detectPlatform(url),
                            coverUrl = media.coverUrl,
                            videoUrl = media.videoUrls.firstOrNull(),
                            imageCount = media.imageUrls.size,
                            originalLink = url,
                            timestamp = System.currentTimeMillis()
                        )
                        history.removeAll { it.originalLink == url }
                        history.add(0, item)
                    }
                } catch (_: Exception) { }
            }
            if (history.isNotEmpty()) saveHistory(context, history)
            batchProgress = "完成：${batchResults.size}/${urls.size} 成功"
            isParsing = false
        }
    }

    // 全屏看图
    fullscreenImage?.let { url ->
        Dialog(
            onDismissRequest = { fullscreenImage = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.92f))
                    .clickable { fullscreenImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                IconButton(
                    onClick = { fullscreenImage = null },
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "关闭", tint = Color.White)
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ===== 输入卡片 =====
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(Brush.linearGradient(theme.cardGradient))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Link, contentDescription = null, tint = theme.primary, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("粘贴分享链接", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = theme.text)
                            Spacer(Modifier.weight(1f))
                            // 批量模式开关
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { isBatchMode = !isBatchMode }
                            ) {
                                Text("批量", fontSize = 12.5.sp, color = theme.textMuted)
                                Spacer(Modifier.width(4.dp))
                                Switch(
                                    checked = isBatchMode,
                                    onCheckedChange = { isBatchMode = it },
                                    modifier = Modifier.size(width = 40.dp, height = 24.dp)
                                )
                            }
                        }
                        // 2026-10-23：粘贴自动解析开关（jicun 式）
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { autoParseOnPaste = !autoParseOnPaste }
                        ) {
                            Text("粘贴后自动解析", fontSize = 12.5.sp, color = theme.textMuted)
                            Spacer(Modifier.width(4.dp))
                            Switch(
                                checked = autoParseOnPaste,
                                onCheckedChange = { autoParseOnPaste = it },
                                modifier = Modifier.size(width = 40.dp, height = 24.dp)
                            )
                        }
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            modifier = Modifier.fillMaxWidth().heightIn(min = if (isBatchMode) 120.dp else 90.dp),
                            placeholder = {
                                Text(
                                    if (isBatchMode) "每行粘贴一个分享链接，可批量解析…" else "粘贴抖音 / 快手 / 小红书 / 哔哩哔哩等分享链接…",
                                    fontSize = 13.sp, color = theme.textMuted
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            maxLines = if (isBatchMode) 8 else 4
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(
                                onClick = { pasteFromClipboard() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("粘贴")
                            }
                            Button(
                                onClick = { if (isBatchMode) doBatchParse() else doParse() },
                                enabled = !isParsing,
                                modifier = Modifier.weight(2f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = theme.primary)
                            ) {
                                if (isParsing) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                    Spacer(Modifier.width(8.dp))
                                    Text(if (isBatchMode && batchProgress.isNotBlank()) batchProgress else "解析中…")
                                } else {
                                    Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(if (isBatchMode) "批量解析" else "开始解析", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        errorMsg?.let {
                            Text(it, color = Color(0xFFC0392B), fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // ===== 解析结果（2026-10-23：jicun 式按内容拆卡） =====
        result?.let { media ->
            item {
                JicunResultCards(
                    media = media,
                    selectedQuality = selectedQuality,
                    onQualitySelected = { selectedQuality = it },
                    onImageClick = { fullscreenImage = it },
                    onDownloadRequested = { items, title -> requestDownload(items, title) }
                )
            }
        }

        // ===== 批量解析结果 =====
        if (batchResults.isNotEmpty()) {
            item {
                Text(
                    "批量结果（${batchResults.size}）",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = theme.text,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(batchResults.size) { idx ->
                val media = batchResults[idx]
                JicunResultCards(
                    media = media,
                    selectedQuality = 0,
                    onQualitySelected = {},
                    onImageClick = { fullscreenImage = it },
                    onDownloadRequested = { items, title -> requestDownload(items, title) }
                )
            }
        }

        // ===== 历史 =====
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.History, contentDescription = null, tint = theme.textMuted, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("解析历史", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = theme.text)
                    Spacer(Modifier.width(6.dp))
                    Text("${history.size}", fontSize = 12.sp, color = theme.textMuted)
                }
                if (history.isNotEmpty()) {
                    TextButton(onClick = {
                        history.clear()
                        saveHistory(context, history)
                    }) { Text("清空", fontSize = 13.sp, color = theme.textMuted) }
                }
            }
        }
        if (history.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无历史记录", fontSize = 13.sp, color = theme.textMuted)
                }
            }
        } else {
            itemsIndexed(history, key = { _, h -> h.timestamp.toString() + h.originalLink }) { _, h ->
                HistoryRow(
                    item = h,
                    onClick = { doParse(h.originalLink) },
                    onDelete = {
                        history.remove(h)
                        saveHistory(context, history)
                    }
                )
            }
        }
    }

    // 2026-10-23：jicun 式下载进度弹窗
    downloadJob?.let { job ->
        DownloadProgressDialog(job = job, onClose = { downloadJob = null })
    }
}

// ---------------- 历史行 ----------------

@Composable
private fun HistoryRow(
    item: WatermarkHistoryItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val theme = LocalThemeUiColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.linearGradient(theme.cardGradient))
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面缩略图
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(theme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            if (!item.coverUrl.isNullOrBlank()) {
                AsyncImage(
                    model = item.coverUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(item.platform.take(1), fontWeight = FontWeight.Bold, color = theme.primary, fontSize = 20.sp)
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.text, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(2.dp))
            Text(
                "${item.platform} · ${if (item.videoUrl != null) "视频" else "${item.imageCount}图"} · ${formatTime(item.timestamp)}",
                fontSize = 11.sp, color = theme.textMuted, maxLines = 1
            )
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "删除", tint = theme.textMuted, modifier = Modifier.size(18.dp))
        }
    }
}
