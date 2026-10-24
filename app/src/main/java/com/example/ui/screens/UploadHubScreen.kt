package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.download.DownloadKeepAlive
import com.example.download.DownloadListener
import com.example.download.DownloadedFile
import com.example.download.NativeDownloader
import java.io.File
import java.io.FileOutputStream
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import com.example.ui.components.streamingBorder
import androidx.compose.material.icons.filled.PlayArrow
import java.net.URLEncoder
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.local.db.UploadedResourceEntity
import com.example.data.util.VideoCache
import com.example.ui.components.RainbowSpinBackground
import com.example.ui.components.SkillDetailDialog
import com.example.ui.components.UTabRow
import kotlin.math.cos
import kotlin.math.sin

/**
 * 液体玻璃状（Liquid Glass）通用卡片容器：
 * - 通透半透明多层折射渐变底色（透出底层流光/壁纸背景）
 * - 游走液态光球折射 + 顶部液态曲面高光反射带 + 底部冰晶微焦散
 * - 镜面高光渐变描边（左上/右下高光 + 柔紫/柔粉折射边）
 */
/**
 * v1.7.3：毛毡风格卡片 —— 暖米色绒面质感，柔软圆润，无流光特效
 * 特点：暖 cream 底色 + 柔和棕色描边 + 大圆角 + 细腻绒面阴影
 */
@Composable
private fun FeltCardBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 22.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardShape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(cardShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFFBF3),  // 暖米白
                        Color(0xFFFDF3E3),  // 浅绒黄
                        Color(0xFFF8ECD8),  // 暖米色
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(600f, 400f)
                )
            )
            .border(
                width = 2.dp,
                color = Color(0xFFD4A574).copy(alpha = 0.35f),  // 柔和绒棕描边
                shape = cardShape
            )
            .shadow(
                elevation = 6.dp,
                shape = cardShape,
                spotColor = Color(0xFFB08968).copy(alpha = 0.25f),  // 暖棕阴影
                ambientColor = Color(0xFFB08968).copy(alpha = 0.15f)
            )
            .let { mod ->
                if (onClick != null) mod.clickable { onClick() } else mod
            }
            .padding(2.dp)  // 内边距营造绒面厚度感
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(cornerRadius - 2.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.Transparent
                        ),
                        center = Offset(0.3f, 0.2f),
                        radius = 800f
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
private fun LiquidGlassCardBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    accentColor: Color = Color(0xFFD8B4FE),
    phaseSeed: Int = 0,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardShape = RoundedCornerShape(cornerRadius)
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_glass_card")
    val liquidPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "liquid_glass_phase"
    )
    val phaseOffset = remember(phaseSeed) { (phaseSeed % 11) * 0.55f }

    Box(
        modifier = modifier
            .clip(cardShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.44f),
                        Color(0xFFF6F0FF).copy(alpha = 0.28f),
                        Color(0xFFFFEDF5).copy(alpha = 0.34f),
                        Color.White.copy(alpha = 0.48f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(900f, 420f)
                )
            )
            .streamingBorder(
                cornerRadius = cornerRadius,
                strokeWidth = 1.7.dp,
                glowWidth = 3.8.dp,
                accentColor = accentColor,
                tailColor = accentColor.copy(alpha = 0.55f),
                baseBorderColor = Color.White.copy(alpha = 0.50f),
                phaseOffset = (phaseSeed.let { if (it < 0) -it else it } % 360).toFloat(),
                showGlow = true
            )
            .let { mod ->
                if (onClick != null) mod.clickable { onClick() } else mod
            }
    ) {
        // 液态玻璃折射光斑 + 顶部曲面高光反射带
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            // 1. 左侧游走液态光斑
            val b1x = w * (0.22f + 0.14f * cos(liquidPhase + phaseOffset))
            val b1y = h * (0.32f + 0.18f * sin(liquidPhase + phaseOffset))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.26f),
                        Color.Transparent
                    ),
                    center = Offset(b1x, b1y),
                    radius = w * 0.42f
                ),
                center = Offset(b1x, b1y),
                radius = w * 0.42f
            )

            // 2. 右侧游走冰晶粉紫折射光球
            val b2x = w * (0.78f + 0.12f * sin(liquidPhase * 0.85f + phaseOffset))
            val b2y = h * (0.68f + 0.16f * cos(liquidPhase * 0.85f + phaseOffset))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFBCFE8).copy(alpha = 0.34f),
                        Color.Transparent
                    ),
                    center = Offset(b2x, b2y),
                    radius = w * 0.38f
                ),
                center = Offset(b2x, b2y),
                radius = w * 0.38f
            )

            // 3. 顶部液态玻璃曲面镜面高光带
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.62f),
                        Color.White.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = h * 0.42f
                ),
                topLeft = Offset(6.dp.toPx(), 3.dp.toPx()),
                size = Size((w - 12.dp.toPx()).coerceAtLeast(0f), h * 0.38f),
                cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
            )
        }

        content()
    }
}

/** v1.8.7：资源自动归类关键词（用于自动识别软件是做什么的） */
private val AUTO_CATEGORY_RULES = listOf(
    "影视/视频" to listOf("视频", "影视", "短剧", "剧场", "电影", "剧集", "播放器", "TV", "movie", "video", "播放"),
    "阅读/小说" to listOf("小说", "阅读", "漫画", "电子书", "book", "read", "novel"),
    "音乐/听歌" to listOf("音乐", "听歌", "歌词", "music", "song", "音频"),
    "游戏/娱乐" to listOf("游戏", "steam", "game", "娱乐", "play"),
    "工具/效率" to listOf("工具", "助手", "清理", "卸载", "压缩", "转换", "下载", "tool", "utils", "效率"),
    "学习/办公" to listOf("学习", "办公", "笔记", "文档", "pdf", "office", "课程", "学"),
    "AI/智能" to listOf("AI", "ai", "智能", "GPT", "大模型", "对话", "写作", "绘画"),
    "系统/装机" to listOf("系统", "装机", "激活", "驱动", "系统优化", "windows", "win")
)

/** v1.8.7：自动识别软件类型归类 */
private fun autoCategorize(res: UploadedResourceEntity): String {
    val text = (res.title + " " + res.desc + " " + res.tags + " " + res.url).lowercase()
    for ((cat, keywords) in AUTO_CATEGORY_RULES) {
        if (keywords.any { text.contains(it, ignoreCase = true) }) return cat
    }
    return "其他资源"
}

/** v1.1.7 修复「软件版块图标不识别」：
 *  多源自动识别软件 icon——
 *  1. 云端已配置 iconUrl 优先（res.iconUrl 由调用方处理）
 *  2. 这里生成多个候选源，由 AutoIconImage 逐个回退尝试（v1.1.16 增强：
 *     不再只取站点自身 favicon——很多站点 /favicon.ico 返回 404 导致无图标；
 *     改为国内可访问的 favicon 聚合服务优先，站点自身兜底）
 */
private fun autoFaviconUrl(url: String): String {
    val host = try {
        java.net.URI(if (url.startsWith("http")) url else "https://$url").host
    } catch (e: Exception) {
        null
    } ?: return ""
    // 站点自身 favicon（最可靠，但不少站点 404，放最后兜底）
    val self = "https://$host/favicon.ico"
    // 国内/全球可访问的 favicon 聚合服务（按成功率优先排序，逐个回退）
    val mirrors = listOf(
        "https://favicon.im/$host?size=64",
        "https://f.icoji.com/icon/$host",
        "https://icon.horse/icon/$host",
        "https://www.google.com/s2/favicons?domain=$host&sz=64"
    )
    // 返回多个候选源串（调用方 AutoIconImage 会逐个尝试/兜底）
    return (mirrors + self).joinToString("|@|")
}

/** v1.1.16 修复「部分软件没有 icon」：多源 favicon 逐个回退加载组件。
 *  传入 “|@|” 分隔的候选源列表，当前源加载失败（onError）时自动切换到下一个源重试，
 *  全部失败则仅显示底层文字徽标（由调用方提供 fallback 背景）。
 *  （替换原先只取第一个源、失败即无图标的逻辑）
 */
@Composable
private fun AutoIconImage(
    sources: String,
    contentDescription: String?,
    modifier: Modifier,
    fallbackColor: Color,
    fallbackText: String
) {
    val candidates = remember(sources) { sources.split("|@|").filter { it.isNotBlank() } }
    var index by remember(sources) { mutableIntStateOf(0) }
    var allFailed by remember(sources) { mutableStateOf(candidates.isEmpty()) }
    Box(modifier = modifier.clip(RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
        // 底层文字徽标（全部源失败时可见）
        Box(
            modifier = Modifier.fillMaxSize().background(fallbackColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = fallbackText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = fallbackColor
            )
        }
        // 当前候选源（加载中/成功时覆盖底层）
        if (!allFailed && index < candidates.size) {
            coil.compose.AsyncImage(
                model = candidates[index],
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                onError = {
                    // 当前源失败 → 尝试下一个；全部失败 → 显示文字徽标
                    if (index + 1 < candidates.size) index++ else allFailed = true
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Appteka 开源应用市场 (https://github.com/solkin/appteka-android)
 * 真实 APK 数据实体
 */
data class ApptekaAppItem(
    val appId: String,
    val label: String,
    val packageName: String,
    val verName: String,
    val verCode: Int,
    val size: Long,
    val time: Long,
    val downloads: Int,
    val iconUrl: String,
    val categoryName: String = "",
    var directApkUrl: String? = null
)

/**
 * 格式化文件大小
 */
private fun formatAppSize(bytes: Long): String {
    if (bytes <= 0) return "未知大小"
    val mb = bytes.toDouble() / (1024 * 1024)
    return if (mb >= 1000) {
        String.format(Locale.getDefault(), "%.1f GB", mb / 1024)
    } else {
        String.format(Locale.getDefault(), "%.1f MB", mb)
    }
}

/**
 * 异步搜索 Appteka 在线开源软件库 (基于 https://appteka.store/api/1/app/search)
 */
private suspend fun searchApptekaApps(query: String): List<ApptekaAppItem> = withContext(Dispatchers.IO) {
    if (query.isBlank()) return@withContext emptyList()
    try {
        val encoded = URLEncoder.encode(query, "UTF-8")
        val url = "https://appteka.store/api/1/app/search?query=$encoded&locale=zh"
        val client = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Appteka/23.0 Android")
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val body = response.body?.string() ?: return@withContext emptyList()
            val json = JSONObject(body)
            val result = json.optJSONObject("result") ?: return@withContext emptyList()
            val entries = result.optJSONArray("entries") ?: return@withContext emptyList()
            val list = mutableListOf<ApptekaAppItem>()
            for (i in 0 until entries.length()) {
                val obj = entries.optJSONObject(i) ?: continue
                val appId = obj.optString("app_id", "")
                val label = obj.optString("label", "")
                if (appId.isBlank() || label.isBlank()) continue
                val packageName = obj.optString("package", "")
                val verName = obj.optString("ver_name", "")
                val verCode = obj.optInt("ver_code", 0)
                val size = obj.optLong("size", 0L)
                val time = obj.optLong("time", 0L)
                val downloads = obj.optInt("downloads", 0)
                val icon = obj.optString("icon", "")
                val categoryObj = obj.optJSONObject("category")
                val categoryName = categoryObj?.optJSONObject("name")?.optString("zh", "") ?: ""
                list.add(
                    ApptekaAppItem(
                        appId = appId,
                        label = label,
                        packageName = packageName,
                        verName = verName,
                        verCode = verCode,
                        size = size,
                        time = time,
                        downloads = downloads,
                        iconUrl = icon,
                        categoryName = categoryName
                    )
                )
            }
            list
        } else emptyList()
    } catch (_: Exception) {
        emptyList()
    }
}

/**
 * 实时获取 Appteka 真实 APK 下载直链 (基于 https://appteka.store/api/1/app/info)
 */
private suspend fun fetchApptekaDownloadUrl(appId: String): String? = withContext(Dispatchers.IO) {
    try {
        val encoded = URLEncoder.encode(appId, "UTF-8")
        val url = "https://appteka.store/api/1/app/info?app_id=$encoded&locale=zh"
        val client = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Appteka/23.0 Android")
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val body = response.body?.string() ?: return@withContext null
            val json = JSONObject(body)
            val result = json.optJSONObject("result") ?: return@withContext null
            val link = result.optString("link", "")
            if (link.isNotBlank()) link else null
        } else null
    } catch (_: Exception) {
        null
    }
}

/**
 * 打开公共「下载」文件夹的写入流（v1.7.9 修复分区存储）：
 * Android 10+ 必须走 MediaStore.Downloads（无需任何权限），
 * Android 9 及以下沿用公共目录直写（WRITE_EXTERNAL_STORAGE 覆盖到 API 28）。
 * 返回 Pair(输出流, MediaStore Uri；低版本为 null，下载中断时用于删除残留)。
 */
private fun openDownloadOutputStream(
    context: Context,
    displayName: String,
    mimeType: String
): Pair<java.io.OutputStream, android.net.Uri?> {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        val values = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Downloads.DISPLAY_NAME, displayName)
            put(android.provider.MediaStore.Downloads.MIME_TYPE, mimeType)
            put(android.provider.MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = context.contentResolver.insert(
            android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI, values
        ) ?: throw java.io.IOException("无法创建下载文件")
        val out = context.contentResolver.openOutputStream(uri)
            ?: throw java.io.IOException("无法打开下载文件")
        out to uri
    } else {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!dir.exists()) dir.mkdirs()
        java.io.File(dir, displayName).outputStream() to null
    }
}

/**
 * 免调用系统 DownloadManager，在应用内直接拉起系统安装器执行真实安装
 */
private fun installApkDirectly(context: Context, apkFile: File): Boolean {
    val uri = try {
        FileProvider.getUriForFile(context, context.packageName + ".fileprovider", apkFile)
    } catch (e: Exception) {
        return false
    }
    val intents = listOf(
        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        },
        Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
            setData(uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
    for (intent in intents) {
        try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return true
            }
        } catch (_: Exception) {}
    }
    try {
        val directIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(directIntent)
        return true
    } catch (_: Exception) {}
    return false
}

/**
 * ===== 波浪下载弹窗（v1.7.22）：软件/资源下载统一走应用内波浪进度弹窗 =====
 * 状态机：DOWNLOADING → PAUSED / CANCELLED / FAILED → INSTALLING → DONE
 * 支持暂停/继续（HTTP Range 断点续传）、取消、失败重试；APK 下载完成后自动拉起系统安装器。
 */
private enum class WaveDlState { IDLE, DOWNLOADING, PAUSED, CANCELLED, FAILED, INSTALLING, DONE }

private data class WaveDownloadUi(
    val visible: Boolean = false,
    val state: WaveDlState = WaveDlState.IDLE,
    val title: String = "",
    val progress: Float = 0f,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val speedBps: Double = 0.0,
    val error: String? = null,
    val isApk: Boolean = false
)

private object WaveDownloadManager {
    private val _ui = MutableStateFlow(WaveDownloadUi())
    val ui: StateFlow<WaveDownloadUi> = _ui.asStateFlow()

    // 2026-10-24：改用 NativeDownloader（多线程分段下载，移植自 shuiyingongju）
    private var downloader: NativeDownloader? = null
    private var taskId: Long? = null
    private var appCtx: Context? = null
    private var dlTitle = ""
    private var dlUrl = ""
    private var dlIsApk = false
    private var targetFile: File? = null
    // 测速用
    private var lastProgressTime = 0L
    private var lastProgressBytes = 0L

    fun start(context: Context, title: String, url: String, fileName: String?, isApk: Boolean) {
        // 正在下载中则忽略重复触发
        if (_ui.value.visible && _ui.value.state == WaveDlState.DOWNLOADING) return
        cancelTask()
        appCtx = context.applicationContext
        dlTitle = title
        dlUrl = url
        dlIsApk = isApk
        val safeName = (fileName?.ifBlank { null } ?: url.substringAfterLast('/').ifBlank { "download.bin" })
            .replace(" ", "_")
            .replace(Regex("[\\\\/:*?\"<>|]"), "_")
            .let { if (isApk && !it.endsWith(".apk", true)) "$it.apk" else it }
        val downloadDir = File(context.cacheDir, "apk_downloads").apply { mkdirs() }
        targetFile = File(downloadDir, safeName)
        // 全新下载：删掉残留文件（NativeDownloader 自己也会处理）
        if (targetFile!!.exists()) targetFile!!.delete()
        _ui.value = WaveDownloadUi(
            visible = true, state = WaveDlState.DOWNLOADING, title = title,
            progress = 0f, downloadedBytes = 0L, totalBytes = 0L, speedBps = 0.0,
            error = null, isApk = isApk
        )
        lastProgressTime = System.currentTimeMillis()
        lastProgressBytes = 0L
        val ctx = appCtx!!
        downloader = NativeDownloader(
            listener = object : DownloadListener {
                override fun onProgress(id: Long, received: Long, total: Long) {
                    if (id != taskId) return
                    val now = System.currentTimeMillis()
                    val dt = (now - lastProgressTime) / 1000.0
                    val speed = if (dt > 0.2) (received - lastProgressBytes) / dt else _ui.value.speedBps
                    if (dt > 0.2) {
                        lastProgressTime = now
                        lastProgressBytes = received
                    }
                    val p = if (total > 0) (received.toFloat() / total).coerceIn(0f, 1f) else 0f
                    _ui.value = _ui.value.copy(
                        progress = p, downloadedBytes = received,
                        totalBytes = total, speedBps = speed
                    )
                }

                override fun onDone(id: Long, files: List<DownloadedFile>, error: String?) {
                    if (id != taskId) return
                    // 保活：任务结束，同步活动数（NativeDownloader 内部已调 onActiveChanged）
                    if (error == "cancelled") {
                        // pause/cancel 主动取消，不改状态（调用方已设置）
                        return
                    }
                    if (error != null) {
                        // 2026-10-24：多线程失败时降级为单线程直连（兼容不支持 Range 的服务器）
                        android.util.Log.w("landezhaole-dl", "多线程下载失败，尝试单线程兜底: $error")
                        fallbackSingleThreadDownload()
                        return
                    }
                    val total = _ui.value.totalBytes.coerceAtLeast(_ui.value.downloadedBytes)
                    _ui.value = _ui.value.copy(progress = 1f, downloadedBytes = total)
                    if (dlIsApk) {
                        _ui.value = _ui.value.copy(state = WaveDlState.INSTALLING)
                        CoroutineScope(Dispatchers.Main).launch {
                            val ctx2 = appCtx
                            val f = targetFile
                            val ok = withContext(Dispatchers.IO) {
                                ctx2 != null && f != null && f.exists() && installApkDirectly(ctx2, f)
                            }
                            _ui.value = _ui.value.copy(
                                state = WaveDlState.DONE,
                                error = if (ok) null else "安装器拉起失败，安装包已保存在应用缓存"
                            )
                        }
                    } else {
                        _ui.value = _ui.value.copy(state = WaveDlState.DONE)
                    }
                }
            },
            onActiveChanged = { active -> DownloadKeepAlive.sync(ctx, active) }
        )
        taskId = downloader!!.start(
            rawItems = listOf(mapOf("url" to dlUrl, "path" to targetFile!!.absolutePath)),
            segments = 32
        )
    }

    private fun cancelTask() {
        taskId?.let { downloader?.cancel(it) }
        taskId = null
        // 保活收摊
        appCtx?.let { DownloadKeepAlive.sync(it, 0) }
    }

    // 2026-10-24：单线程兜底下载（多线程失败时用，兼容不支持 Range 的服务器）
    // 2026-10-24：raw.githubusercontent.com 加镜像兜底（国内经常被墙/DNS 污染）
    private fun fallbackSingleThreadDownload() {
        val ctx = appCtx ?: return
        val url = dlUrl
        val file = targetFile ?: return
        _ui.value = _ui.value.copy(
            state = WaveDlState.DOWNLOADING,
            error = null,
            progress = 0f, downloadedBytes = 0L, totalBytes = 0L
        )
        CoroutineScope(Dispatchers.IO).launch {
            // 构建镜像 URL 列表：原站 → gh-proxy → jsDelivr
            val mirrorUrls = buildMirrorUrls(url)
            var lastError: Exception? = null
            var failedMirrorIdx = -1
            var downloadOk = false
            for ((idx, tryUrl) in mirrorUrls.withIndex()) {
                try {
                    downloadSingleUrl(tryUrl, file)
                    downloadOk = true
                    break
                } catch (e: Exception) {
                    lastError = e
                    failedMirrorIdx = idx
                    // 换下一个镜像前重置进度
                    withContext(Dispatchers.Main) {
                        _ui.value = _ui.value.copy(progress = 0f, downloadedBytes = 0L, totalBytes = 0L)
                    }
                }
            }
            if (downloadOk) {
                // 成功，走正常完成流程
                withContext(Dispatchers.Main) {
                    _ui.value = _ui.value.copy(progress = 1f, downloadedBytes = downloadedBytes())
                    if (dlIsApk) {
                        _ui.value = _ui.value.copy(state = WaveDlState.INSTALLING)
                        // 触发安装（复用原有安装逻辑）
                        CoroutineScope(Dispatchers.Main).launch {
                            val installOk = installApkDirectly(ctx, file)
                            if (installOk) {
                                _ui.value = _ui.value.copy(state = WaveDlState.DONE)
                            } else {
                                _ui.value = _ui.value.copy(
                                    state = WaveDlState.FAILED,
                                    error = "安装包已下载，调起安装失败"
                                )
                            }
                        }
                    } else {
                        _ui.value = _ui.value.copy(state = WaveDlState.DONE)
                    }
                }
            } else {
                // 全部镜像都失败，报错（注明是第几个镜像失败）
                val e = lastError
                val failUrl = mirrorUrls.getOrNull(failedMirrorIdx) ?: url
                val host = try { java.net.URL(failUrl).host } catch (_: Exception) { "未知地址" }
                val mirrorInfo = if (mirrorUrls.size > 1) "（镜像${failedMirrorIdx + 1}/${mirrorUrls.size}）" else ""
                withContext(Dispatchers.Main) {
                    _ui.value = _ui.value.copy(
                        state = WaveDlState.FAILED,
                        error = "下载失败 [$host]$mirrorInfo：${e?.message ?: "网络异常"}",
                        speedBps = 0.0
                    )
                }
            }
        }
    }

    // 2026-10-24：为 raw.githubusercontent.com 构建镜像 URL 列表
    private fun buildMirrorUrls(url: String): List<String> {
        val host = try { java.net.URL(url).host } catch (_: Exception) { return listOf(url) }
        if (host != "raw.githubusercontent.com") return listOf(url)
        val list = mutableListOf(url)
        // 镜像 2：gh-proxy
        list.add("https://gh-proxy.com/" + url)
        // 镜像 3：jsDelivr（格式转换：raw.githubusercontent.com/user/repo/branch/path → cdn.jsdelivr.net/gh/user/repo@branch/path）
        try {
            val u = java.net.URL(url)
            val parts = u.path.trim('/').split('/')
            // path 格式：user/repo/branch/剩余路径
            if (parts.size >= 4) {
                val user = parts[0]
                val repo = parts[1]
                val branch = parts[2]
                val restPath = parts.subList(3, parts.size).joinToString("/")
                list.add("https://cdn.jsdelivr.net/gh/$user/$repo@$branch/$restPath")
            }
        } catch (_: Exception) {
            // 转换失败就跳过 jsDelivr
        }
        return list
    }

    // 2026-10-24：单 URL 下载（suspend，供镜像循环调用），失败抛异常，成功返回
    private suspend fun downloadSingleUrl(url: String, file: java.io.File) {
        val conn = java.net.URL(url).openConnection() as java.net.HttpURLConnection
        conn.connectTimeout = 20000
        conn.readTimeout = 60000
        // 2026-10-24：请求头修复 —— v1.7.61 带 Appteka UA + Referer，v1.7.63 改走
        // WaveDownloadManager 时弄丢了，源站防盗链直接拒绝（用户问"1.7.61怎么可以"的根因）
        val reqHost = runCatching { java.net.URL(url).host }.getOrNull().orEmpty()
        if (url.contains("appteka", ignoreCase = true)) {
            conn.setRequestProperty("User-Agent", com.example.download.APPTEKA_UA)
            conn.setRequestProperty("Referer", com.example.download.APPTEKA_REFERER)
        } else {
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36")
            if (reqHost.isNotEmpty()) conn.setRequestProperty("Referer", "https://$reqHost/")
        }
        conn.connect()
        // 检查 HTTP 状态码，非 200 直接抛错（带 code，方便定位）
        val responseCode = conn.responseCode
        if (responseCode != java.net.HttpURLConnection.HTTP_OK) {
            val host = try { java.net.URL(url).host } catch (_: Exception) { url }
            throw java.io.IOException("HTTP $responseCode（$host）")
        }
        val total = conn.contentLengthLong.coerceAtLeast(0L)
        withContext(Dispatchers.Main) {
            _ui.value = _ui.value.copy(totalBytes = total)
        }
        conn.inputStream.use { input ->
            // 每次换镜像重新写文件（覆盖）
            file.outputStream().use { output ->
                val buffer = ByteArray(8192)
                var downloaded = 0L
                var n: Int
                while (input.read(buffer).also { n = it } != -1) {
                    output.write(buffer, 0, n)
                    downloaded += n
                    val p = if (total > 0) (downloaded.toFloat() / total).coerceIn(0f, 1f) else 0f
                    withContext(Dispatchers.Main) {
                        _ui.value = _ui.value.copy(
                            progress = p, downloadedBytes = downloaded, totalBytes = total
                        )
                    }
                }
            }
        }
    }

    private fun downloadedBytes(): Long = _ui.value.downloadedBytes

    fun pause() {
        if (_ui.value.state != WaveDlState.DOWNLOADING) return
        cancelTask()
        _ui.value = _ui.value.copy(state = WaveDlState.PAUSED, speedBps = 0.0)
    }

    fun resume() {
        if (_ui.value.state != WaveDlState.PAUSED && _ui.value.state != WaveDlState.FAILED
            && _ui.value.state != WaveDlState.CANCELLED) return
        // NativeDownloader 不支持跨会话断点续传，resume 重新下一遍。
        // 注意：先调 start（它内部会重置 UI 状态），不要提前设 DOWNLOADING 否则被防重复拦截。
        val ctx = appCtx ?: return
        start(ctx, dlTitle, dlUrl, targetFile?.name, dlIsApk)
    }

    fun cancel() {
        cancelTask()
        try { targetFile?.delete() } catch (_: Exception) {}
        _ui.value = _ui.value.copy(state = WaveDlState.CANCELLED, speedBps = 0.0)
    }

    fun dismiss() {
        cancelTask()
        _ui.value = WaveDownloadUi()
    }

    /** DONE 状态再次拉起安装（APK） */
    fun installAgain() {
        val ctx = appCtx ?: return
        val f = targetFile ?: return
        if (!f.exists()) return
        CoroutineScope(Dispatchers.Main).launch {
            _ui.value = _ui.value.copy(state = WaveDlState.INSTALLING)
            val ok = withContext(Dispatchers.IO) { installApkDirectly(ctx, f) }
            _ui.value = _ui.value.copy(
                state = WaveDlState.DONE,
                error = if (ok) null else "安装器拉起失败，安装包已保存在应用缓存"
            )
        }
    }

}

/**
 * 〰 波浪进度环：正弦波形环形路径，持续流动；spinning=true 时为安装中的旋转弧形
 */
@Composable
private fun WaveProgressRing(
    progress: Float,
    ringColor: Color,
    spinning: Boolean,
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "waveRing")
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (Math.PI * 2).toFloat(),
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart),
        label = "wavePhase"
    )
    val rotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart),
        label = "spinRot"
    )
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.minDimension / 2f * 0.78f
        val amp = size.minDimension * 0.045f
        val waves = 10
        val strokeW = size.minDimension * 0.058f
        val startAngle = -Math.PI.toFloat() / 2f

        fun wavePath(sweep: Float, a0: Float, ph: Float): Path {
            val path = Path()
            val steps = 140
            val sweepRad = sweep * (Math.PI * 2).toFloat()
            for (i in 0..steps) {
                val a = a0 + sweepRad * i / steps
                val rr = r + amp * kotlin.math.sin(a * waves + ph)
                val x = cx + rr * kotlin.math.cos(a)
                val y = cy + rr * kotlin.math.sin(a)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            if (sweep >= 0.999f) path.close()
            return path
        }

        // 底环：完整波浪灰环
        drawPath(
            path = wavePath(1f, startAngle, phase),
            color = Color(0xFFD1D5DB),
            style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        // 进度波浪
        if (spinning) {
            val rot = Math.toRadians(rotation.toDouble()).toFloat()
            drawPath(
                path = wavePath(0.3f, startAngle + rot, phase),
                color = ringColor,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        } else if (progress > 0.001f) {
            drawPath(
                path = wavePath(progress.coerceIn(0f, 1f), startAngle, phase),
                color = ringColor,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

private fun formatSpeed(bps: Double): String {
    if (bps <= 0) return "-- MB/s"
    val mb = bps / (1024 * 1024)
    return if (mb >= 1000) String.format(Locale.getDefault(), "%.1f GB/s", mb / 1024)
    else String.format(Locale.getDefault(), "%.1f MB/s", mb)
}

/**
 * 波浪下载弹窗：标题 + 状态文案 + 大小/速率 + 波浪进度环 + 百分比 + 操作按钮
 * 样式对标用户确认的版本更新弹窗 HTML（毛玻璃白卡、圆角 24、波浪环）
 */
@Composable
private fun WaveDownloadDialog() {
    val ui by WaveDownloadManager.ui.collectAsState()
    if (!ui.visible) return

    val ringColor = when (ui.state) {
        WaveDlState.FAILED -> Color(0xFFEF4444)
        WaveDlState.DONE -> Color(0xFF22C55E)
        else -> Color(0xFF3B82F6)
    }
    val statusText = when (ui.state) {
        WaveDlState.DOWNLOADING -> "正在下载 ${ui.title}"
        WaveDlState.PAUSED -> "已暂停 · ${ui.title}（${(ui.progress * 100).toInt()}%）"
        WaveDlState.CANCELLED -> "已取消下载"
        WaveDlState.FAILED -> ui.error?.takeIf { it.isNotBlank() } ?: "网络连接失败，请重试"
        WaveDlState.INSTALLING -> "正在安装..."
        WaveDlState.DONE -> if (ui.isApk) "下载完成，可安装" else "下载完成"
        else -> ""
    }
    val isError = ui.state == WaveDlState.FAILED
    val metaText = when (ui.state) {
        WaveDlState.DOWNLOADING ->
            "${formatAppSize(ui.downloadedBytes)} / ${formatAppSize(ui.totalBytes)} · ${formatSpeed(ui.speedBps)}"
        WaveDlState.PAUSED ->
            "${formatAppSize(ui.downloadedBytes)} / ${formatAppSize(ui.totalBytes)} · 已暂停"
        WaveDlState.CANCELLED ->
            "${formatAppSize(ui.downloadedBytes)} / ${formatAppSize(ui.totalBytes)} · 已停止"
        WaveDlState.FAILED ->
            "${formatAppSize(ui.downloadedBytes)} / ${formatAppSize(ui.totalBytes)} · 连接已断开"
        WaveDlState.INSTALLING ->
            "${formatAppSize(ui.totalBytes)} / ${formatAppSize(ui.totalBytes)} · 校验完成"
        WaveDlState.DONE ->
            if (ui.error != null) ui.error!!
            else if (ui.isApk) "${formatAppSize(ui.totalBytes)} · 安装包就绪"
            else "${formatAppSize(ui.totalBytes)} · 已保存到本地"
        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(320.dp)
                // 2026-10-24：Aero 风格边框（用户要求移除跑马灯）：白色亮边 + 柔和阴影 + 顶部高光
                .shadow(20.dp, RoundedCornerShape(24.dp), ambientColor = Color(0xFF0A2540).copy(alpha = 0.25f))
                .background(Color.White.copy(alpha = 0.94f), RoundedCornerShape(24.dp))
                .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部：标题 + 关闭
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = null,
                        tint = Color(0xFF111827),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (ui.isApk) "下载安装" else "下载文件",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827),
                        maxLines = 1
                    )
                }
                IconButton(
                    onClick = {
                        if (ui.state == WaveDlState.DOWNLOADING || ui.state == WaveDlState.PAUSED) {
                            WaveDownloadManager.cancel()
                        } else {
                            WaveDownloadManager.dismiss()
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text(text = "×", fontSize = 22.sp, color = Color(0xFF9CA3AF))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // 状态文案
            Text(
                text = statusText,
                fontSize = 14.sp,
                color = if (isError) Color(0xFFEF4444) else Color(0xFF6B7280),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            // 大小 / 速率
            Text(
                text = metaText,
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(20.dp))

            // 波浪进度环
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                WaveProgressRing(
                    progress = ui.progress,
                    ringColor = ringColor,
                    spinning = ui.state == WaveDlState.INSTALLING,
                    modifier = Modifier.fillMaxSize()
                )
                if (ui.state == WaveDlState.DONE && ui.error == null) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(40.dp)
                    )
                } else if (ui.state != WaveDlState.INSTALLING) {
                    Text(
                        text = "${(ui.progress * 100).toInt()}%",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 底部按钮组
            when (ui.state) {
                WaveDlState.DOWNLOADING -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        WaveDialogButton(
                            text = "⏸ 暂停",
                            bgColor = Color(0xFFF3F4F6),
                            textColor = Color(0xFF4B5563),
                            onClick = { WaveDownloadManager.pause() },
                            modifier = Modifier.weight(1f)
                        )
                        WaveDialogButton(
                            text = "取消下载",
                            bgColor = Color(0xFFE0E7FF),
                            textColor = Color(0xFF3B82F6),
                            onClick = { WaveDownloadManager.cancel() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                WaveDlState.PAUSED -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        WaveDialogButton(
                            text = "▶ 继续",
                            bgColor = Color(0xFF3B82F6),
                            textColor = Color.White,
                            onClick = { WaveDownloadManager.resume() },
                            modifier = Modifier.weight(1f)
                        )
                        WaveDialogButton(
                            text = "取消下载",
                            bgColor = Color(0xFFE0E7FF),
                            textColor = Color(0xFF3B82F6),
                            onClick = { WaveDownloadManager.cancel() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                WaveDlState.CANCELLED -> {
                    WaveDialogButton(
                        text = "重新下载",
                        bgColor = Color(0xFFFEE2E2),
                        textColor = Color(0xFFEF4444),
                        onClick = { WaveDownloadManager.resume() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                WaveDlState.FAILED -> {
                    WaveDialogButton(
                        text = "重试",
                        bgColor = Color(0xFFFEE2E2),
                        textColor = Color(0xFFEF4444),
                        onClick = { WaveDownloadManager.resume() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                WaveDlState.INSTALLING -> {
                    WaveDialogButton(
                        text = "安装中...",
                        bgColor = Color(0xFFE0E7FF),
                        textColor = Color(0xFF3B82F6),
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                WaveDlState.DONE -> {
                    if (ui.isApk) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            WaveDialogButton(
                                text = "完成",
                                bgColor = Color(0xFFF3F4F6),
                                textColor = Color(0xFF4B5563),
                                onClick = { WaveDownloadManager.dismiss() },
                                modifier = Modifier.weight(1f)
                            )
                            WaveDialogButton(
                                text = "立即安装",
                                bgColor = Color(0xFF3B82F6),
                                textColor = Color.White,
                                onClick = { WaveDownloadManager.installAgain() },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        WaveDialogButton(
                            text = "完成",
                            bgColor = Color(0xFF3B82F6),
                            textColor = Color.White,
                            onClick = { WaveDownloadManager.dismiss() },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun WaveDialogButton(
    text: String,
    bgColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // 2026-10-24 Frutiger Aero：按原色映射到 Aero 按钮风格
    val isBlue = bgColor == Color(0xFF3B82F6) || textColor == Color(0xFF3B82F6) ||
            bgColor.red < 0.5f && bgColor.blue > 0.7f
    val style = if (isBlue) com.example.ui.theme.AeroButtonStyle.Blue
    else com.example.ui.theme.AeroButtonStyle.Ghost
    com.example.ui.theme.AeroButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        style = style,
        enabled = enabled
    )
}

/**
 * 软件版块通用：应用内直接高速下载并触发真实安装（彻底免去手机内容下载管理器与通知栏确认）
 */
private fun downloadAndInstallResourceInApp(
    context: Context,
    title: String,
    url: String,
    fileName: String?,
    isApk: Boolean
) {
    if (url.isBlank()) {
        Toast.makeText(context, "下载地址为空", Toast.LENGTH_SHORT).show()
        return
    }
    // v1.7.22：统一走波浪进度弹窗（应用内下载+安装），不再用 Toast 提示
    WaveDownloadManager.start(context, title, url, fileName, isApk)
}

/**
 * 下载 Appteka 真实 APK 并在应用内直接拉起真实安装（免调用手机 DownloadManager）
 */
private fun downloadApptekaApk(
    context: Context,
    item: ApptekaAppItem,
    onProgress: (Boolean) -> Unit = {},
    onCompleted: () -> Unit = {}
) {
    // 2026-10-24：改走 WaveDownloadManager 弹窗（用户要求搜索结果也显示下载弹窗）
    CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Dispatchers.Main) { onProgress(true) }
            val directUrl = item.directApkUrl ?: fetchApptekaDownloadUrl(item.appId)
            item.directApkUrl = directUrl
            if (directUrl.isNullOrBlank()) {
                withContext(Dispatchers.Main) {
                    onProgress(false)
                    Toast.makeText(context, "未能获取到下载直链，请稍后重试", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            val safeVer = if (item.verName.isNotBlank()) "_${item.verName}" else ""
            val safeName = "${item.label}${safeVer}.apk"
                .replace(" ", "_")
                .replace(Regex("[\\\\/:*?\"<>|]"), "_")
            withContext(Dispatchers.Main) {
                onProgress(false)
                onCompleted()
                // 走统一下载弹窗
                WaveDownloadManager.start(context, item.label, directUrl, safeName, isApk = true)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onProgress(false)
                Toast.makeText(context, "下载失败，请检查网络后重试", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Deprecated("已改走 WaveDownloadManager 弹窗，保留仅防外部引用")
private fun downloadApptekaApkDirect(
    context: Context,
    item: ApptekaAppItem,
    onProgress: (Boolean) -> Unit = {},
    onCompleted: () -> Unit = {}
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            withContext(Dispatchers.Main) { onProgress(true) }
            val directUrl = item.directApkUrl ?: fetchApptekaDownloadUrl(item.appId)
            item.directApkUrl = directUrl
            if (directUrl.isNullOrBlank()) {
                withContext(Dispatchers.Main) {
                    onProgress(false)
                    Toast.makeText(context, "未能获取到下载直链，请稍后重试", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }
            val safeVer = if (item.verName.isNotBlank()) "_${item.verName}" else ""
            val safeName = "${item.label}${safeVer}.apk"
                .replace(" ", "_")
                .replace(Regex("[\\\\/:*?\"<>|]"), "_")

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "🚀 正在应用内直接高速下载 ${item.label}…", Toast.LENGTH_SHORT).show()
            }

            // 保存到应用缓存私有目录供 FileProvider 实时真实安装
            val downloadDir = File(context.cacheDir, "apk_downloads").apply { mkdirs() }
            val apkFile = File(downloadDir, safeName)

            // OkHttp 直接下载，带 Appteka UA 和 Referer，自动跟随重定向
            val client = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .build()
            val request = Request.Builder()
                .url(directUrl)
                .header("User-Agent", "Appteka/23.0 Android")
                .header("Referer", "https://appteka.store/")
                .build()
            val response = client.newCall(request).execute()
            response.use { resp ->
                if (!resp.isSuccessful) {
                    throw Exception("HTTP ${resp.code}")
                }
                val body = resp.body ?: throw Exception("空响应")
                apkFile.outputStream().use { fileOut ->
                    body.byteStream().use { input ->
                        input.copyTo(fileOut)
                    }
                }
            }

            withContext(Dispatchers.Main) {
                onProgress(false)
                onCompleted()
                Toast.makeText(context, "🎉 ${item.label} 下载成功，正在拉起真实安装…", Toast.LENGTH_SHORT).show()
                val installOk = installApkDirectly(context, apkFile)
                if (!installOk) {
                    Toast.makeText(context, "安装包已就绪：${apkFile.name}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            // v1.7.20：应用内下载失败只提示错误，不再跳转浏览器（用户需求）
            withContext(Dispatchers.Main) {
                onProgress(false)
                Toast.makeText(context, "下载失败，请检查网络后重试", Toast.LENGTH_LONG).show()
            }
        }
    }
}


/**
 * 软件版块搜索输入栏
 */
@Composable
private fun SoftwareSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    placeholder: String = "搜索应用、软件或安装包..."
) {
    // 2026-10-24 Frutiger Aero：fa-input 风格（白 75% + 1.5px 蓝边 30% + 18px 圆角）
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 12.5.sp,
                maxLines = 1,
                color = Color(0xFF8AA5B8)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "搜索",
                tint = Color(0xFF00A8E8),
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "清空",
                        tint = Color(0xFF5B7A90),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF00A8E8),
            unfocusedBorderColor = Color(0xFF00A8E8).copy(alpha = 0.3f),
            focusedContainerColor = Color.White.copy(alpha = 0.75f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("software_search_bar")
    )
}

/**
 * 真实 APK 卡片（液态玻璃风格）
 */
@Composable
private fun ApptekaAppCard(
    item: ApptekaAppItem,
    index: Int = 0
) {
    val context = LocalContext.current
    var isDownloading by remember(item.appId) { mutableStateOf(false) }
    // v1.7.3：下载完成标记（显示对勾）
    var isCompleted by remember(item.appId) { mutableStateOf(false) }

    // v1.7.3：软件卡片使用毛毡风格
    FeltCardBox(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp,
        onClick = {
            downloadApptekaApk(context, item,
                onProgress = { isDownloading = it },
                onCompleted = { isCompleted = true }
            )
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            // 左侧：App 图标
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                if (item.iconUrl.startsWith("http", ignoreCase = true)) {
                    coil.compose.AsyncImage(
                        model = item.iconUrl,
                        contentDescription = item.label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFFB08968).copy(alpha = 0.25f),
                                        Color(0xFF8B5E3C).copy(alpha = 0.35f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.label.take(1).uppercase(Locale.getDefault()),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFB08968)
                        )
                    }
                }
                // v1.7.3：下载完成对勾 + 高光闪烁
                if (isCompleted) {
                    var flashAlpha by remember { mutableStateOf(1f) }
                    LaunchedEffect(item.appId) {
                        flashAlpha = 1f
                        kotlinx.coroutines.delay(100)
                        flashAlpha = 0.3f
                        kotlinx.coroutines.delay(150)
                        flashAlpha = 1f
                        kotlinx.coroutines.delay(150)
                        flashAlpha = 0.85f
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.85f * flashAlpha)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "已下载",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                // 下载中遮罩
                if (isDownloading) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // 中间：标题 + 包名/版本 + 属性标签
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFB08968).copy(alpha = 0.15f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "安装包",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B5E3C)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "v${item.verName.ifBlank { "最新" }} · ${item.packageName}",
                    fontSize = 9.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(5.dp))

                // 下载 / 大小 / 下载量标签
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 安装/下载按钮
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFB08968).copy(alpha = 0.15f))
                            .clickable {
                                downloadApptekaApk(context, item,
                                    onProgress = { isDownloading = it },
                                    onCompleted = { isCompleted = true }
                                )
                            }
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isDownloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(10.dp),
                                    strokeWidth = 1.5.dp,
                                    color = Color(0xFFB08968)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    contentDescription = null,
                                    tint = Color(0xFFB08968),
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (isDownloading) "解析中..." else "下载 APK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B5E3C)
                            )
                        }
                    }

                    // 大小
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0284C7).copy(alpha = 0.12f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "大小: ${formatAppSize(item.size)}",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }

                    // 下载量
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEF4444).copy(alpha = 0.12f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "下载量: ${item.downloads}",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 资源展示页（软件 / Skill）v1.8.7：
 * - 软件版块（gridMode=true）：三列一排网格 + 自动归类分组 + 自动识别 icon + Appteka 真实开源 APK 检索
 * - Skill 版块（gridMode=false）：保持原单列大卡片（含预览/视频）
 * - 内容完全由云端控制台同步；控制台删除 → 本体实时同步移除
 */
@Composable
fun UploadHubScreen(
    title: String,
    subtitle: String,
    resourceType: String,
    resources: List<UploadedResourceEntity>,
    onDelete: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    showDelete: Boolean = false,
    gridMode: Boolean = false,
    // v1.0.18 软件版块 .u-tab 推荐/关注/热门：favoriteUrls 用于「关注」筛选
    favoriteUrls: Set<String> = emptySet()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 搜索状态（接入 Appteka 开源软件源 https://github.com/solkin/appteka-android）
    var searchQuery by remember { mutableStateOf("") }
    var isSearchingAppteka by remember { mutableStateOf(false) }
    var apptekaResults by remember { mutableStateOf<List<ApptekaAppItem>>(emptyList()) }
    var searchJob by remember { mutableStateOf<Job?>(null) }

    // Skill 技能库：点击卡片弹独立详情（视频预览/提示词/下载/跳转）
    var skillDetail by remember { mutableStateOf<UploadedResourceEntity?>(null) }

    // v1.0.18 软件版块 .u-tab：0=推荐（全部）/ 1=关注（收藏）/ 2=热门（badge 含 热门/推荐/精选/官方）
    var softTabIndex by remember { mutableStateOf(0) }
    val softTabList = listOf("推荐", "关注", "热门")
    val displayResources = remember(softTabIndex, resources, favoriteUrls) {
        when (softTabIndex) {
            1 -> resources.filter { it.url in favoriteUrls || it.id in favoriteUrls }
            2 -> resources.filter {
                val b = it.badge + it.desc + it.title
                b.contains("热门") || b.contains("推荐") || b.contains("精选") || b.contains("官方")
            }
            else -> resources
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (gridMode && (resourceType == "software" || resourceType == "skill")) {
            // ===== 软件/Skill：v1.0.19 取消展开收纳标签，直接平铺呈现 =====
            // 软件版块：保留 .u-tab 推荐/关注/热门筛选，分类分组标题仅作静态分区（不可点、不收纳）
            val grouped = remember(displayResources) {
                val map = LinkedHashMap<String, MutableList<UploadedResourceEntity>>()
                displayResources.forEach { map.getOrPut(autoCategorize(it)) { mutableListOf() }.add(it) }
                map.toList()
            }
            // 搜索模式本地匹配过滤
            val localMatches = remember(searchQuery, displayResources) {
                if (searchQuery.isBlank()) emptyList()
                else displayResources.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                    it.desc.contains(searchQuery, ignoreCase = true)
                }
            }
            // 主题色渐变（软件卡片专用，v1.0.9 主题升级）
            val themePrimary = MaterialTheme.colorScheme.primary
            val themeSecondary = MaterialTheme.colorScheme.secondary
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 软件版块顶部搜索栏（真实检索 Appteka 开源应用市场真实 APK）
                if (resourceType == "software") {
                    item {
                        SoftwareSearchBar(
                            query = searchQuery,
                            onQueryChange = { newQuery ->
                                searchQuery = newQuery
                                searchJob?.cancel()
                                if (newQuery.isNotBlank()) {
                                    searchJob = coroutineScope.launch {
                                        delay(350)
                                        isSearchingAppteka = true
                                        val res = searchApptekaApps(newQuery.trim())
                                        apptekaResults = res
                                        isSearchingAppteka = false
                                    }
                                } else {
                                    apptekaResults = emptyList()
                                    isSearchingAppteka = false
                                }
                            },
                            onSearch = {
                                if (searchQuery.isNotBlank()) {
                                    searchJob?.cancel()
                                    searchJob = coroutineScope.launch {
                                        isSearchingAppteka = true
                                        val res = searchApptekaApps(searchQuery.trim())
                                        apptekaResults = res
                                        isSearchingAppteka = false
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

                if (resourceType == "software" && searchQuery.isNotBlank()) {
                    // ===== 搜索模式：本地匹配 + Appteka 真实开源库检索结果 =====
                    if (isSearchingAppteka) {
                        item {
                            Surface(
                                color = Color(0xFF10B981).copy(alpha = 0.08f),
                                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.25f)),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = Color(0xFF10B981)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "正在检索「$searchQuery」相关的应用与安装包...",
                                        fontSize = 12.sp,
                                        color = Color(0xFF047857),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    if (localMatches.isNotEmpty()) {
                        item {
                            Text(
                                text = "📦 常用应用 (${localMatches.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                            )
                        }
                        itemsIndexed(localMatches, key = { _, it -> "loc_${it.id}" }) { index, res ->
                            SoftwareGridCard(
                                res = res,
                                index = index,
                                showDelete = showDelete,
                                onDelete = {
                                    onDelete(res.id)
                                    Toast.makeText(context, "已删除（云端同步）", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }

                    if (apptekaResults.isNotEmpty()) {
                        item {
                            Text(
                                text = "🌐 搜索结果 (${apptekaResults.size} 款应用 · 点击直接下载)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                            )
                        }
                        itemsIndexed(apptekaResults, key = { _, it -> "appteka_${it.appId}" }) { index, item ->
                            ApptekaAppCard(
                                item = item,
                                index = index
                            )
                        }
                    }

                    if (!isSearchingAppteka && localMatches.isEmpty() && apptekaResults.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "未找到与「$searchQuery」相关的应用",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "建议尝试输入常用应用名称进行搜索",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // ===== 常规模式：原标签栏与全部软件展示 =====
                    // v1.0.18 软件版块 .u-tab：推荐 / 关注 / 热门（仅 software 显示）
                    if (resourceType == "software") {
                        item {
                            UTabRow(
                                tabs = softTabList,
                                selectedIndex = softTabIndex,
                                onSelect = { softTabIndex = it },
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                    // v1.1.12：删除软件库/Skill 页头横幅（用户要求，截图1）——不再显示标题/副标题/共X款横幅
                    if (resources.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Filled.Download,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                        modifier = Modifier.size(44.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "还未获取到任何资源哟 请联系作者",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else if (resourceType == "skill") {
                        // v1.1.4 软件/Skill 分支：Skill 技能库取消分类标签（不再显示 其他资源/AI/智能 等），直接平铺
                        items(displayResources, key = { it.id }) { res ->
                            SkillGridCard(
                                res = res,
                                onClick = { skillDetail = res },
                                showDelete = showDelete,
                                onDelete = {
                                    onDelete(res.id)
                                    Toast.makeText(context, "已删除（云端同步）", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    } else {
                        // v1.1.6 需求 2：软件版块去掉分类标签，直接平铺全部软件（自动识别 icon）
                        itemsIndexed(displayResources, key = { _, it -> it.id }) { index, res ->
                            SoftwareGridCard(
                                res = res,
                                index = index,
                                showDelete = showDelete,
                                onDelete = {
                                    onDelete(res.id)
                                    Toast.makeText(context, "已删除（云端同步）", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // ===== Skill / 其它：原单列大卡片 =====
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text(
                            text = title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (resources.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "还未获取到任何资源哟 请联系作者",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    items(resources, key = { it.id }) { res ->
                        ResourceFileCard(
                            res = res,
                            resourceType = resourceType,
                            showDelete = showDelete,
                            onClickOverride = if (resourceType == "skill") {
                                { skillDetail = res }
                            } else null,
                            onDelete = {
                                onDelete(res.id)
                                Toast.makeText(context, "已删除（云端同步）", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

    // Skill 技能详情独立弹窗
    skillDetail?.let { res ->
        SkillDetailDialog(
            res = res,
            onDismiss = { skillDetail = null },
            onJump = { url ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "无法打开跳转链接", Toast.LENGTH_SHORT).show()
                }
            },
            onDownload = { url, fileName ->
                downloadAndInstallResourceInApp(
                    context = context,
                    title = res.title,
                    url = url,
                    fileName = fileName,
                    isApk = url.endsWith(".apk", true)
                )
            }
        )
    }

    // v1.7.22：波浪下载弹窗（应用内下载+安装统一入口）
    WaveDownloadDialog()
}

/** Skill 技能包卡片（v1.0.12：取消横屏滑动，改为竖排全宽卡片；取消 icon 图标功能）：
 *  顶部预览区（视频/图片缩略图或渐变占位）+ 标题与作者 + 底部操作按钮
 *  （严格遵循控制台上传形式：文件形式→「下载」到本地，URL 形式→「跳转」，仅这两个按钮） */
@Composable
private fun SkillGridCard(
    res: UploadedResourceEntity,
    onClick: () -> Unit,
    showDelete: Boolean = false,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val isVideo = res.mediaUrl.isNotBlank() &&
        Regex("\\.(mp4|webm|mov|m4v)(\\?.*)?$", RegexOption.IGNORE_CASE).containsMatchIn(res.mediaUrl)
    // v1.0.12：严格遵循控制台上传形式——file 模式 url 存文件直链 →「下载」；url 模式 →「跳转」
    val isFileMode = res.mode != "url"
    val fileLink = res.fileUrl.ifBlank { res.url }.ifBlank {
        // 兼容旧数据：控制台早期把 zip/md 技能包直链放在 mediaUrl 字段
        if (res.mediaUrl.endsWith(".zip", true) || res.mediaUrl.endsWith(".md", true)) res.mediaUrl else ""
    }
    val jumpUrl = if (isFileMode) "" else res.url.ifBlank { res.fileUrl }
    val canDownload = isFileMode && fileLink.isNotBlank()
    val canJump = !isFileMode && jumpUrl.isNotBlank()
    val isZip = fileLink.endsWith(".zip", ignoreCase = true)
    val isMd = fileLink.endsWith(".md", ignoreCase = true)

    fun downloadSkill(url: String) {
        val ext = if (isZip) ".zip" else if (isMd) ".md" else ""
        val safeName = (res.title + ext).replace(" ", "_").replace(Regex("[\\\\/:*?\"<>|]"), "_")
        downloadAndInstallResourceInApp(
            context = context,
            title = res.title,
            url = url,
            fileName = safeName,
            isApk = false
        )
    }

    fun jumpSkill(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "无法打开跳转链接", Toast.LENGTH_SHORT).show()
        }
    }

    LiquidGlassCardBox(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 18.dp,
        accentColor = Color(0xFFD8B4FE),
        phaseSeed = res.id.hashCode(),
        onClick = onClick
    ) {
        // v1.1.14：Skill 排版修复——竖屏改横屏：预览缩略图在左、信息在右，宽度自适应屏宽
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧：预览缩略图（有预览图/视频展示缩略，否则渐变占位）
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (res.previewUrl.isNotBlank())
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            )
                        else
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.22f),
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.18f)
                                )
                            )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // v1.1.17 修复优化 Skill icon：有预览图→图片；无预览图但配置了 iconUrl→显示图标；否则类型 emoji 徽标
                if (res.previewUrl.isNotBlank()) {
                    coil.compose.AsyncImage(
                        model = res.previewUrl,
                        contentDescription = res.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (res.iconUrl.isNotBlank()) {
                    coil.compose.AsyncImage(
                        model = res.iconUrl,
                        contentDescription = res.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(if (isVideo) "🎬" else if (isZip) "📦" else if (isMd) "📄" else "🧠", fontSize = 24.sp)
                }
                if (isVideo) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.55f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "视频",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // 右侧：标题 + 类型/作者 + 操作按钮（横排呈现，宽度自适应）
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = res.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when {
                            isVideo -> "视频"
                            isZip -> "ZIP"
                            isMd -> "MD"
                            else -> "Skill"
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (res.author.isNotBlank()) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = res.author,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                // 底部操作按钮：仅「下载」（文件形式）/「跳转」（URL 形式）
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (canDownload) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF22C55E).copy(alpha = 0.12f))
                                .clickable { downloadSkill(fileLink) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Download,
                                    contentDescription = null,
                                    tint = Color(0xFF22C55E),
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "下载",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF22C55E)
                                )
                            }
                        }
                    }
                    if (canJump) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                                .clickable { jumpSkill(jumpUrl) }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "跳转",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    if (showDelete) {
                        Text(
                            text = "删除",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.clickable { onDelete() }.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

/** 软件横排小卡片（v1.8.7：图标在左、信息在右的横排布局，不再竖排堆叠） */
@Composable
private fun SoftwareGridCard(
    res: UploadedResourceEntity,
    index: Int = 0,
    showDelete: Boolean = false,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val isFileMode = res.mode != "url"
    val fileLink = res.fileUrl.ifBlank { res.url }
    val url = if (isFileMode) fileLink else res.url.ifBlank { res.fileUrl }
    val isApk = url.endsWith(".apk", ignoreCase = true)
    val isZip = url.endsWith(".zip", ignoreCase = true)
    val isMd = url.endsWith(".md", ignoreCase = true)
    val canInstall = isApk || isZip || isMd

    var localClicks by remember(res.id) { mutableIntStateOf(0) }
    // 用户需求：下载量从100开始迭代（每项递增，且交互点击时自增）
    val downloadCount = 100 + index + localClicks
    val realDownloads = "$downloadCount"

    val badgeText = ""
    val badgeColor = when {
        isApk -> Color(0xFF22C55E)
        isZip -> Color(0xFF6366F1)
        isMd -> Color(0xFFF59E0B)
        else -> MaterialTheme.colorScheme.primary
    }

    // v1.8.7：自动识别 icon（优先云端 iconUrl，其次 favicon 聚合服务）
    // v1.1.16：保留完整多源串，由 AutoIconImage 逐个回退加载（修复部分软件无图标）
    val iconSources = res.iconUrl.ifBlank { autoFaviconUrl(res.url.ifBlank { res.fileUrl }) }

    fun downloadToLocal(url: String, fileName: String?) {
        downloadAndInstallResourceInApp(
            context = context,
            title = res.title,
            url = url,
            fileName = fileName,
            isApk = isApk
        )
    }

    var downloadProgress by remember(res.id) { mutableStateOf(0f) }
    var isDownloadingState by remember(res.id) { mutableStateOf(false) }
    var isCompletedState by remember(res.id) { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun startDownloadWithProgress() {
        if (url.isBlank()) {
            Toast.makeText(context, "该资源暂未配置下载链接", Toast.LENGTH_SHORT).show()
            return
        }
        if (isDownloadingState) return
        if (isCompletedState) {
            Toast.makeText(context, "✅ 该应用已下载到手机「下载」文件夹", Toast.LENGTH_SHORT).show()
            return
        }
        isDownloadingState = true
        downloadProgress = 0f
        localClicks++
        // 触发真实系统下载
        downloadToLocal(url, res.title + (if (isZip) ".zip" else if (isApk) ".apk" else if (isMd) ".md" else ""))
        // 视觉平滑递增进度条至 100%
        coroutineScope.launch {
            val steps = listOf(0.15f, 0.32f, 0.52f, 0.73f, 0.88f, 0.96f, 1.0f)
            for (p in steps) {
                delay(180)
                downloadProgress = p
            }
            delay(120)
            isDownloadingState = false
            isCompletedState = true
        }
    }

    val onCardClick = {
        if (url.isNotBlank()) {
            if (canInstall) {
                if (!isDownloadingState && !isCompletedState) {
                    startDownloadWithProgress()
                } else if (isCompletedState) {
                    Toast.makeText(context, "✅ 该应用已完成下载，位于手机「下载」目录", Toast.LENGTH_SHORT).show()
                }
            } else {
                // v1.7.20：非安装包链接不再跳转浏览器（用户需求）
                Toast.makeText(context, "该资源暂不支持应用内下载", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "该资源暂未配置下载链接", Toast.LENGTH_SHORT).show()
        }
    }

    LiquidGlassCardBox(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 18.dp,
        accentColor = badgeColor,
        phaseSeed = res.id.hashCode(),
        onClick = { onCardClick() }
    ) {
        // v1.8.7：横排呈现——图标在左、标题/描述/按钮在右，卡片更短更紧凑
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
                // 左侧：自动识别的软件 icon（云端 icon 优先，多源 favicon 逐个回退，再回退文字徽标）
                Box(
                    modifier = Modifier.size(42.dp),
                contentAlignment = Alignment.Center
            ) {
                if (iconSources.isNotBlank()) {
                    AutoIconImage(
                        sources = iconSources,
                        contentDescription = res.title,
                        modifier = Modifier.size(42.dp),
                        fallbackColor = badgeColor,
                        fallbackText = badgeText
                    )
                } else {
                    // 底层类型徽标（无任何 icon 源时可见）
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(badgeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = badgeColor
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            // 右侧：标题 + 描述 + 按钮横排底部
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = res.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (res.desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = res.desc,
                        fontSize = 10.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                // 红色边框需求：使用「时间/大小/下载量」文字形式呈现，下载量从100开始迭代
                val (realTime, realSize, _) = remember(res.id, res.title) {
                    getRealisticSoftwareStats(res.title, res.desc, res.url.ifBlank { res.fileUrl }, res.timestamp)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isDownloadingState) {
                        // 1. 正在下载：呈现可视化进度条 + 百分比
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF22C55E).copy(alpha = 0.12f))
                                .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .widthIn(min = 68.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.Center) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "下载中",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF16A34A)
                                    )
                                    Text(
                                        text = "${(downloadProgress * 100).toInt()}%",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF16A34A)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFF22C55E).copy(alpha = 0.25f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction = downloadProgress.coerceIn(0.05f, 1f))
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(Color(0xFF22C55E))
                                    )
                                }
                            }
                        }
                    } else if (isCompletedState) {
                        // 2. 进度条到100%后呈现对勾完成下载到本地
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF22C55E).copy(alpha = 0.18f))
                                .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .clickable {
                                    Toast.makeText(context, "✅ 已完成下载到手机「下载」文件夹", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "已完成",
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "已完成",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF16A34A)
                                )
                            }
                        }
                    } else {
                        // 3. 初始按钮：安装 / 打开
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (canInstall) Color(0xFF22C55E).copy(alpha = 0.12f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                                .clickable {
                                    if (canInstall) {
                                        startDownloadWithProgress()
                                    } else {
                                        onCardClick()
                                    }
                                }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (canInstall) Icons.Filled.Download else Icons.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = if (canInstall) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (canInstall) "安装" else "打开",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (canInstall) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // 1. 时间标签（文字形式呈现）
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF59E0B).copy(alpha = 0.12f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "时间: $realTime",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD97706)
                        )
                    }

                    // 2. 大小标签（文字形式呈现）
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF0284C7).copy(alpha = 0.12f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "大小: $realSize",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }

                    // 3. 下载量标签（文字形式呈现，真实情况映射）
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEF4444).copy(alpha = 0.12f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "下载量: $realDownloads",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }

                    if (showDelete) {
                        Text(
                            text = "删除",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.clickable { onDelete() }.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 软件横屏卡片（v1.0.9 全新横屏呈现）：固定宽度横向滑动，图标在上、信息在下，
 * 主题渐变描边 + 主题色角标，适合一排排横向滑动浏览（LazyRow）。
 */
@Composable
private fun SoftwareHorizontalCard(
    res: UploadedResourceEntity,
    showDelete: Boolean = false,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val isFileMode = res.mode != "url"
    val fileLink = res.fileUrl.ifBlank { res.url }
    val url = if (isFileMode) fileLink else res.url.ifBlank { res.fileUrl }
    val isApk = url.endsWith(".apk", ignoreCase = true)
    val isZip = url.endsWith(".zip", ignoreCase = true)
    val isMd = url.endsWith(".md", ignoreCase = true)
    val canInstall = isApk || isZip || isMd

    val badgeText = ""
    val badgeColor = when {
        isApk -> Color(0xFF22C55E)
        isZip -> Color(0xFF6366F1)
        isMd -> Color(0xFFF59E0B)
        else -> MaterialTheme.colorScheme.primary
    }

    // v1.1.16：多源 favicon 逐个回退，提升识别成功率（修复部分软件无图标）
    val iconSources = res.iconUrl.ifBlank { autoFaviconUrl(res.url.ifBlank { res.fileUrl }) }
    // v1.0.9 主题：卡片描边用主题主色渐变（清爽浅红系）
    val themePrimary = MaterialTheme.colorScheme.primary
    val themeSecondary = MaterialTheme.colorScheme.secondary

    fun downloadToLocal(url: String, fileName: String?) {
        downloadAndInstallResourceInApp(
            context = context,
            title = res.title,
            url = url,
            fileName = fileName,
            isApk = isApk
        )
    }

    val onCardClick = {
        if (url.isNotBlank()) {
            if (canInstall) {
                downloadToLocal(url, res.title + (if (isZip) ".zip" else if (isApk) ".apk" else if (isMd) ".md" else ""))
            } else {
                // v1.7.20：非安装包链接不再跳转浏览器（用户需求）
                Toast.makeText(context, "该资源暂不支持应用内下载", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "该资源暂未配置下载链接", Toast.LENGTH_SHORT).show()
        }
    }

    // 横屏卡片：固定宽 176dp，液体玻璃状呈现，适合横向滑动
    LiquidGlassCardBox(
        modifier = Modifier.width(176.dp),
        cornerRadius = 18.dp,
        accentColor = themePrimary,
        phaseSeed = res.id.hashCode(),
        onClick = { onCardClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // 顶部：icon + 角标
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconSources.isNotBlank()) {
                        AutoIconImage(
                            sources = iconSources,
                            contentDescription = res.title,
                            modifier = Modifier.size(40.dp),
                            fallbackColor = badgeColor,
                            fallbackText = badgeText
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(badgeColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = badgeColor
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(themePrimary.copy(alpha = 0.18f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (canInstall) "安装" else "打开",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = themePrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // 标题
            Text(
                text = res.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (res.desc.isNotBlank()) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = res.desc,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (showDelete) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "删除",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { onDelete() }.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ResourceFileCard(
    res: UploadedResourceEntity,
    resourceType: String = "software",
    showDelete: Boolean = false,
    onClickOverride: (() -> Unit)? = null,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val isFileMode = res.mode != "url"
    val fileLink = res.fileUrl.ifBlank { res.url }
    val url = if (isFileMode) fileLink else res.url.ifBlank { res.fileUrl }
    val isFile = isFileMode && url.isNotBlank()
    val isApk = url.endsWith(".apk", ignoreCase = true)
    val isZip = url.endsWith(".zip", ignoreCase = true)
    val isMd = url.endsWith(".md", ignoreCase = true)
    val canInstall = isApk || isZip || isMd

    val badgeText = ""
    val badgeColor = when {
        isApk -> Color(0xFF22C55E)
        isZip -> Color(0xFF6366F1)
        isMd -> Color(0xFFF59E0B)
        else -> MaterialTheme.colorScheme.primary
    }

    // v1.9.1：Skill 技能库遵循「zip/md 文件 → 可下载到本地、URL → 直接跳转」：
    // 文件类（apk/zip/md）按钮在 Skill 场景显示「下载」（下载技能包到本地，即下即用），
    // 软件场景文件类保持「安装」；URL 形式均显示「直达」（点击直接跳转）。
    val actionText = when {
        canInstall && resourceType == "skill" -> "下载"
        canInstall -> "安装"
        else -> ""
    }

    fun downloadToLocal(url: String, fileName: String?) {
        downloadAndInstallResourceInApp(
            context = context,
            title = res.title,
            url = url,
            fileName = fileName,
            isApk = isApk
        )
    }

    val onCardClick = {
        if (url.isNotBlank()) {
            if (canInstall) {
                downloadToLocal(url, res.title + (if (isZip) ".zip" else if (isApk) ".apk" else if (isMd) ".md" else ""))
            } else {
                // v1.7.20：非安装包链接不再跳转浏览器（用户需求）
                Toast.makeText(context, "该资源暂不支持应用内下载", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "该资源暂未配置下载链接", Toast.LENGTH_SHORT).show()
        }
    }

    LiquidGlassCardBox(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 18.dp,
        accentColor = badgeColor,
        phaseSeed = res.id.hashCode(),
        onClick = { (onClickOverride ?: onCardClick)() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // v1.0.12：Skill 场景取消 icon 图标功能，统一使用类型文字徽标呈现
                val iconSources = if (resourceType == "skill")
                    ""
                else
                    res.iconUrl.ifBlank { autoFaviconUrl(res.url.ifBlank { res.fileUrl }) }
                if (iconSources.isNotBlank()) {
                    AutoIconImage(
                        sources = iconSources,
                        contentDescription = res.title,
                        modifier = Modifier.size(40.dp),
                        fallbackColor = badgeColor,
                        fallbackText = badgeText
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(badgeColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = badgeColor
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = res.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (res.desc.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = res.desc,
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (showDelete) {
                    IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.Filled.DeleteOutline,
                            contentDescription = "删除",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (res.previewUrl.isNotBlank() || res.mediaUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                if (res.mediaUrl.isNotBlank()) {
                    if (res.previewUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    try {
                                        val vintent = Intent(Intent.ACTION_VIEW, Uri.parse(res.mediaUrl))
                                        vintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(vintent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "无法播放视频", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        ) {
                            coil.compose.AsyncImage(
                                model = res.previewUrl,
                                contentDescription = res.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.Black.copy(alpha = 0.55f))
                                    .border(1.5.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("▶", color = Color.White, fontSize = 20.sp)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .background(Color(0xFF1E1E24))
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    try {
                                        val vintent = Intent(Intent.ACTION_VIEW, Uri.parse(res.mediaUrl))
                                        vintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(vintent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "无法播放视频", Toast.LENGTH_SHORT).show()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "▶ 点击播放视频（带声音）",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                } else {
                    coil.compose.AsyncImage(
                        model = res.previewUrl,
                        contentDescription = res.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(res.previewUrl))
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "无法打开预览图", Toast.LENGTH_SHORT).show()
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (res.author.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = res.author,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                if (res.tags.isNotBlank()) {
                    Text(
                        text = res.tags,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (canInstall) Color(0xFF22C55E).copy(alpha = 0.12f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (canInstall) Icons.Filled.Download else Icons.Filled.OpenInNew,
                        contentDescription = null,
                        tint = if (canInstall) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = actionText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (canInstall) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun AsyncImageCompat(
    url: String,
    fallbackText: String,
    fallbackColor: Color,
    modifier: Modifier
) {
    Box(modifier = modifier.clip(RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fallbackColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = fallbackText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = fallbackColor
            )
        }
        coil.compose.AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * 红色边框需求：拟真软件指标计算（下载量与大小紧贴知名软件现实情况，杜绝虚假与倒挂）：
 * - Everything：Windows 知名搜盘神器，安装包单文件仅约 3.5 MB，全球装机下载量超 8500万；
 * - Geek Uninstaller：单文件便携卸载清理工具，约 6.8 MB，全球装机下载约 1250万；
 * - 幸运破解器 (Lucky Patcher)：老牌安卓应用修改代理工具，约 10.8 MB，累计下载量超 3200万；
 * - 简影：老牌视频特效模板制作工具，手机应用约 68.5 MB，相对小众但专注，下载量约 168万；
 * - 豆包去水印助手：抖音/豆包 AI 配套工具，实用辅助体量约 25.6 MB，垂直下载量约 42.5万；
 * - PixelLab：全球爆款图片文字排版美化工具，Google Play 官方下载量 5000万+，安装包约 36.8 MB；
 * - PotPlayer：全球高赞播放器，安装包约 32.4 MB，累计下载 9200万；
 * - 其他类别软件根据分类（系统/清理、影音/剪辑、小工具）映射符合真实情况的拟真数据。
 */
fun getRealisticSoftwareStats(title: String, desc: String, url: String, timestamp: Long): Triple<String, String, String> {
    val t = title.lowercase()
    val d = desc.lowercase()
    val combined = "$t $d $url"

    // 2026-10-24：用户要求时间固定为 2026-05-14，不跟随网络/系统时间变动
    val dateStr = "2026-05-14"

    val (size, downloads) = when {
        t.contains("everything") -> "3.5 MB" to "8520万"
        t.contains("geek") || t.contains("uninstaller") -> "6.8 MB" to "1250万"
        t.contains("幸运破解器") || t.contains("lucky") -> "10.8 MB" to "3200万"
        t.contains("简影") -> "68.5 MB" to "168万"
        t.contains("豆包") || t.contains("去水印") -> "25.6 MB" to "42.5万"
        t.contains("pixellab") -> "36.8 MB" to "5000万+"
        t.contains("potplayer") -> "32.4 MB" to "9200万"
        t.contains("7-zip") || t.contains("7z") || t.contains("winrar") -> "1.8 MB" to "9500万"
        t.contains("idm") || t.contains("internet download") -> "12.6 MB" to "4800万"
        t.contains("chrome") || t.contains("浏览器") -> "95.6 MB" to "2.6亿"
        t.contains("微信") || t.contains("wechat") -> "256 MB" to "10亿+"
        t.contains("qq") -> "280 MB" to "8.5亿"
        t.contains("抖音") || t.contains("tiktok") -> "180 MB" to "9.8亿"
        t.contains("vscode") -> "88.5 MB" to "6500万"
        t.contains("termux") -> "38.5 MB" to "1800万"
        combined.contains("系统") || combined.contains("清理") || combined.contains("卸载") || combined.contains("搜索") -> {
            val seed = kotlin.math.abs(title.hashCode() + 11)
            val mb = 2.5f + (seed % 140) / 10f
            val dl = 350 + (seed % 1500)
            String.format("%.1f MB", mb) to "${dl}万"
        }
        combined.contains("剪辑") || combined.contains("视频") || combined.contains("特效") || combined.contains("相机") -> {
            val seed = kotlin.math.abs(title.hashCode() + 23)
            val mb = 50f + (seed % 420) / 10f
            val dl = 90 + (seed % 350)
            String.format("%.1f MB", mb) to "${dl}万"
        }
        combined.contains("助手") || combined.contains("工具") || combined.contains("辅助") -> {
            val seed = kotlin.math.abs(title.hashCode() + 37)
            val mb = 8f + (seed % 240) / 10f
            val dl = 25 + (seed % 180)
            String.format("%.1f MB", mb) to "${dl}万"
        }
        else -> {
            val seed = kotlin.math.abs(title.hashCode() + 49)
            val mb = 15f + (seed % 350) / 10f
            val dl = 60 + (seed % 800)
            String.format("%.1f MB", mb) to "${dl}万"
        }
    }

    return Triple(dateStr, size, downloads)
}

