package com.example.ui.screens

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.util.VideoLinkParser
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.ThemeUiColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 视频解析结果数据结构
 */
data class VideoParseResult(
    val title: String,
    val author: String,
    val platform: String,
    val coverUrl: String?,
    val durationText: String,
    val videoUrls: List<String>,
    val imageUrls: List<String> = emptyList(),
    val audioUrl: String? = null,
    val originalLink: String,
    val timestamp: Long = System.currentTimeMillis(),
    val qualityOptions: List<Pair<String, String>> = emptyList() // v1.4.32：画质选项
)

/**
 * 支持的短视频与多媒体平台
 */
data class DownVidPlatform(
    val name: String,
    val tag: String,
    val iconColor: Color,
    val tip: String
)

// v1.4.21：解析引擎升级（移植 GitHub 开源项目 parse-video-py / f2 解析逻辑），
// 各平台走专用解析器：无水印直链 + 真实作者/标题，失败自动回退通用提取
private val SupportedPlatforms = listOf(
    DownVidPlatform("抖音", "Douyin", Color(0xFF0F0F12), "无水印直链(playwm→play)·视频/图集·真实作者"),
    DownVidPlatform("快手", "Kuaishou", Color(0xFFFF5000), "INIT_STATE 直取原画·视频/图集·真实作者"),
    DownVidPlatform("哔哩哔哩", "Bilibili", Color(0xFF00A1D6), "官方 API 取 cid/playurl·高清直链"),
    DownVidPlatform("小红书", "RED", Color(0xFFFF2442), "无水印图文原图·视频 masterUrl 直链"),
    DownVidPlatform("微博", "Weibo", Color(0xFFE6162D), "h5/m API·视频秒拍·正文大图"),
    DownVidPlatform("西瓜视频", "Xigua", Color(0xFFFF3B30), "同抖音引擎·无水印中长视频"),
    DownVidPlatform("好看视频", "Haokan", Color(0xFF2932E1), "百度官方 json 接口·原画直链"),
    DownVidPlatform("微视", "Weishi", Color(0xFF07C160), "video_url 直取"),
    DownVidPlatform("YouTube", "YouTube", Color(0xFFFF0000), "通用提取·og:video"),
    DownVidPlatform("TikTok", "TikTok", Color(0xFF00F2FE), "同抖音引擎·无水印原画")
)

/**
 * DownVid 全网无水印视频下载神器
 * 基于开源项目 https://github.com/yxxbc/downvid 深度适配 Android 端；
 * v1.4.21 解析引擎升级，移植以下开源项目解析逻辑（遵循其开源协议）：
 * - https://github.com/wujunwei928/parse-video-py (MIT)
 * - https://github.com/Johnserf-Seed/f2 (Apache-2.0)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DownVidScreen(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // v1.4.13：主题跟随 —— 所有主色从主题色板读取，切换主题时本页全部变色
    val themeColors = LocalThemeUiColors.current

    var inputUrl by remember { mutableStateOf("") }
    var isParsing by remember { mutableStateOf(false) }
    var currentResult by remember { mutableStateOf<VideoParseResult?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: 解析下载, 1: 支持平台, 2: 下载记录, 3: 使用帮助

    val historyList = remember { mutableStateListOf<VideoParseResult>() }

    // v1.4.33：新版三卡片布局状态 —— 图片多选 / 卡片折叠（随解析结果重置）
    var selectedImageIdx by remember(currentResult) { mutableStateOf(setOf<Int>()) }
    var mediaExpanded by remember(currentResult) { mutableStateOf(true) }
    var descExpanded by remember { mutableStateOf(false) }

    // v1.4.34：自动解析 / 自动下载开关（SharedPreferences 持久化，默认全开）
    val autoPrefs = remember { context.getSharedPreferences("lzdz_downvid_auto", Context.MODE_PRIVATE) }
    var autoParseEnabled by remember { mutableStateOf(autoPrefs.getBoolean("auto_parse", true)) }
    var autoDownloadEnabled by remember { mutableStateOf(autoPrefs.getBoolean("auto_download", true)) }


    // 解析逻辑
    // v1.4.34：支持 overrideUrl（粘贴/剪贴板自动触发时直接传，避免 state 未刷新读到旧值）；
    // 解析成功后按开关自动下载全部媒体
    fun triggerImageDownload(urls: List<String>) {
        if (urls.isEmpty()) {
            Toast.makeText(context, "没有可下载的图片", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val ts = System.currentTimeMillis()
            urls.forEachIndexed { index, url ->
                val ext = url.substringBefore('?').substringAfterLast('.', "").lowercase()
                    .takeIf { it in setOf("jpg", "jpeg", "png", "webp") } ?: "jpg"
                val filename = "LDZ_${ts}_${index + 1}.$ext"
                val request = DownloadManager.Request(Uri.parse(url)).apply {
                    setTitle("懒得找了 · 图片 ${index + 1}/${urls.size}")
                    setDescription("正在保存到相册...")
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    // 存入公共 Pictures 目录 → 系统相册自动可见
                    setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "懒得找了/$filename")
                    setAllowedOverMetered(true)
                    setAllowedOverRoaming(true)
                }
                downloadManager.enqueue(request)
            }
            Toast.makeText(context, "🚀 ${urls.size} 张图片已加入下载，完成后自动存入相册", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "下载发起失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 执行系统下载
    fun triggerDownload(url: String, filename: String) {        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url)).apply {
                setTitle("DownVid · $filename")
                setDescription("正在保存到相册/本地存储...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "DownVid/$filename")
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }
            downloadManager.enqueue(request)
            Toast.makeText(context, "🚀 已加入系统下载队列，下载完成后保存在 Download/DownVid 目录", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // 回退到浏览器下载
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                Toast.makeText(context, "正在通过浏览器下载...", Toast.LENGTH_SHORT).show()
            } catch (_: Exception) {
                Toast.makeText(context, "下载发起失败: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // v1.4.33：粘贴辅助（输入卡片「粘贴」按钮共用）
    // v1.4.34：粘贴后自动解析（开关控制）
    fun startParsing(overrideUrl: String? = null) {
        val target = extractUrl(overrideUrl ?: inputUrl).trim()
        if (target.isBlank() || (!target.startsWith("http://") && !target.startsWith("https://"))) {
            errorMessage = "请输入或粘贴合法的视频/图文分享链接"
            Toast.makeText(context, "请输入合法的分享链接", Toast.LENGTH_SHORT).show()
            return
        }

        isParsing = true
        errorMessage = null

        coroutineScope.launch {
            try {
                // v1.4.13：改用 resolveMedia（视频+图片+真实封面+标题）；解析不到真实直链则明确失败，
                // 不再把分享页链接伪装成可下载直链（之前兜底导致下载到 HTML 页面）
                val media = VideoLinkParser.resolveMedia(context, target)
                if (media.videoUrls.isNotEmpty() || media.imageUrls.isNotEmpty()) {
                    // v1.4.21：优先用解析器返回的真实平台名/作者名
                    val platformName = media.platform?.takeIf { it.isNotBlank() } ?: detectPlatform(target)
                    val kind = if (media.videoUrls.isNotEmpty()) "视频" else "图片"
                    val result = VideoParseResult(
                        title = media.title?.ifBlank { null }
                            ?: "${platformName}精选${kind} · ${System.currentTimeMillis() % 10000}",
                        author = media.author?.takeIf { it.isNotBlank() } ?: "原创作者",
                        platform = platformName,
                        coverUrl = media.coverUrl
                            ?: media.imageUrls.firstOrNull()
                            ?: media.videoUrls.firstOrNull(),
                        durationText = if (media.videoUrls.isNotEmpty()) "高清原画" else "${media.imageUrls.size}张原图",
                        videoUrls = media.videoUrls,
                        imageUrls = media.imageUrls,
                        originalLink = target,
                        qualityOptions = media.qualityOptions
                    )
                    currentResult = result
                    if (historyList.none { it.originalLink == target }) {
                        historyList.add(0, result)
                    }
                    // v1.4.34：解析成功后自动下载（开关控制；视频用默认最高画质，附带图片一并下载）
                    if (autoDownloadEnabled) {
                        if (result.videoUrls.isNotEmpty()) {
                            val bestUrl = result.qualityOptions.firstOrNull()?.second
                                ?: result.videoUrls.first()
                            triggerDownload(bestUrl, "DownVid_${System.currentTimeMillis()}.mp4")
                            if (result.imageUrls.isNotEmpty()) {
                                triggerImageDownload(result.imageUrls)
                            }
                        } else if (result.imageUrls.isNotEmpty()) {
                            triggerImageDownload(result.imageUrls)
                        }
                    } else {
                        Toast.makeText(context, "🎉 解析成功，可一键下载！", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 明确失败：不展示虚假下载按钮
                    currentResult = null
                    errorMessage = "未能提取到视频/图片直链，该分享可能需要登录、已失效或平台升级了反爬"
                    Toast.makeText(context, "😕 解析失败：未找到可下载的媒体", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                errorMessage = "解析遇到网络异常: ${e.message}"
            } finally {
                isParsing = false
            }
        }
    }

    // v1.4.13：一键下载图片并自动存入相册（Pictures/懒得找了，相册可见，无需手动操作）
    fun doPaste() {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val text = clip.getItemAt(0).text?.toString() ?: ""
                val extracted = extractUrl(text)
                if (extracted.isNotBlank()) {
                    inputUrl = extracted
                    Toast.makeText(context, "已粘贴链接", Toast.LENGTH_SHORT).show()
                    if (autoParseEnabled) {
                        autoPrefs.edit().putString("last_auto_target", extracted.trim()).apply()
                        startParsing(extracted)
                    }
                } else {
                    inputUrl = text
                }
            } else {
                Toast.makeText(context, "剪贴板为空", Toast.LENGTH_SHORT).show()
            }
        } catch (_: Exception) {}
    }


    // 自动读取剪贴板有效链接
    // v1.4.34：进页自动解析（开关控制；同一链接只自动处理一次，避免重复下载）
    LaunchedEffect(Unit) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val text = clip.getItemAt(0).text?.toString() ?: ""
                val extracted = extractUrl(text)
                if (extracted.isNotBlank() && (extracted.startsWith("http://") || extracted.startsWith("https://"))) {
                    inputUrl = extracted
                    if (autoParseEnabled && autoPrefs.getString("last_auto_target", null) != extracted.trim()) {
                        autoPrefs.edit().putString("last_auto_target", extracted.trim()).apply()
                        startParsing(extracted)
                    }
                }
            }
        } catch (_: Exception) {}
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("downvid_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // ---- 顶部标题（v1.4.33：对标参考图，简洁大标题替代 Hero 横幅） ----
        item {
            Text(
                text = "解析",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = themeColors.text,
                modifier = Modifier.padding(start = 20.dp, top = 14.dp, bottom = 6.dp)
            )
        }

        // ---- 卡片1：粘贴链接（v1.4.33：对标参考图） ----
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(themeColors.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Link,
                                contentDescription = null,
                                tint = themeColors.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "粘贴链接",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = themeColors.text
                            )
                            Text(
                                text = "粘贴平台分享链接",
                                fontSize = 12.sp,
                                color = themeColors.textMuted
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(themeColors.primary.copy(alpha = 0.14f))
                                    .clickable { doPaste() }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.ContentPaste,
                                        contentDescription = null,
                                        tint = themeColors.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("粘贴", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = themeColors.primary)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(LocalThemeUiColors.current.surface)
                                    .clickable { inputUrl = ""; errorMessage = null }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        contentDescription = null,
                                        tint = themeColors.textMuted,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("清空", fontSize = 13.sp, color = themeColors.textMuted)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = inputUrl,
                        onValueChange = {
                            inputUrl = it
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("downvid_input_url"),
                        placeholder = {
                            Text(
                                "粘贴视频/图文分享链接...",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        },
                        maxLines = 3,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LocalThemeUiColors.current.surface,
                            unfocusedContainerColor = LocalThemeUiColors.current.surface,
                            disabledContainerColor = LocalThemeUiColors.current.surface,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { startParsing() })
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { startParsing() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("downvid_btn_parse"),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColors.primary,
                            disabledContainerColor = LocalThemeUiColors.current.border
                        ),
                        enabled = !isParsing
                    ) {
                        if (isParsing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("正在解析...", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                Icons.Filled.RocketLaunch,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("完成解析", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // v1.4.34：自动解析 / 自动下载开关
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AutoToggleChip(
                            label = "粘贴自动解析",
                            checked = autoParseEnabled,
                            onCheckedChange = {
                                autoParseEnabled = it
                                autoPrefs.edit().putBoolean("auto_parse", it).apply()
                            },
                            themeColors = themeColors,
                            modifier = Modifier.weight(1f)
                        )
                        AutoToggleChip(
                            label = "解析自动下载",
                            checked = autoDownloadEnabled,
                            onCheckedChange = {
                                autoDownloadEnabled = it
                                autoPrefs.edit().putBoolean("auto_download", it).apply()
                            },
                            themeColors = themeColors,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            fontSize = 12.sp,
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }

        // ---- 卡片2：媒体预览（v1.4.33：对标参考图；图片多选下载 + 视频画质能力保留） ----
        if (currentResult != null) {
            val res = currentResult!!
            val isImagePost = res.videoUrls.isEmpty() && res.imageUrls.isNotEmpty()
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { mediaExpanded = !mediaExpanded },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(themeColors.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isImagePost) Icons.Filled.Collections else Icons.Filled.Movie,
                                    contentDescription = null,
                                    tint = themeColors.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = themeColors.primary.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = res.platform,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = themeColors.primary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isImagePost) "图集预览" else "视频预览",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.text
                                    )
                                }
                                Text(
                                    text = if (isImagePost) "图片列表与缩略图" else "无水印直链 · 点选画质下载",
                                    fontSize = 12.sp,
                                    color = themeColors.textMuted
                                )
                            }
                            Icon(
                                if (mediaExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (mediaExpanded) "收起" else "展开",
                                tint = themeColors.textMuted
                            )
                        }

                        if (mediaExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))

                            if (isImagePost) {
                                // ---- 图集模式：横向缩略图多选 ----
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(res.imageUrls.size) { idx ->
                                        val url = res.imageUrls[idx]
                                        val selected = selectedImageIdx.contains(idx)
                                        Box(
                                            modifier = Modifier
                                                .size(96.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(LocalThemeUiColors.current.surface)
                                                .then(
                                                    if (selected) Modifier.border(
                                                        2.5.dp,
                                                        themeColors.primary,
                                                        RoundedCornerShape(14.dp)
                                                    ) else Modifier
                                                )
                                                .clickable {
                                                    selectedImageIdx =
                                                        if (selected) selectedImageIdx - idx
                                                        else selectedImageIdx + idx
                                                }
                                        ) {
                                            AsyncImage(
                                                model = url,
                                                contentDescription = "图片 ${idx + 1}",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(6.dp)
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (selected) themeColors.primary
                                                        else Color.Black.copy(alpha = 0.55f)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    if (selected) Icons.Filled.DoneAll else Icons.Filled.Visibility,
                                                    contentDescription = if (selected) "已选中" else "预览",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "共 ${res.imageUrls.size} 张" +
                                        if (selectedImageIdx.isNotEmpty()) " · 已选 ${selectedImageIdx.size} 张" else "",
                                    fontSize = 13.sp,
                                    color = themeColors.textMuted
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    val allSelected = selectedImageIdx.size == res.imageUrls.size
                                    Button(
                                        onClick = {
                                            selectedImageIdx =
                                                if (allSelected) emptySet()
                                                else res.imageUrls.indices.toSet()
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(24.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = themeColors.primary.copy(alpha = 0.14f)
                                        )
                                    ) {
                                        Icon(
                                            Icons.Filled.TouchApp,
                                            contentDescription = null,
                                            tint = themeColors.primary,
                                            modifier = Modifier.size(17.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            if (allSelected) "取消全选" else "全选媒体",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = themeColors.primary
                                        )
                                    }
                                    val hasSelection = selectedImageIdx.isNotEmpty()
                                    Button(
                                        onClick = {
                                            val urls = selectedImageIdx.sorted()
                                                .map { res.imageUrls[it] }
                                            triggerImageDownload(urls)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(24.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (hasSelection) themeColors.primary
                                            else LocalThemeUiColors.current.border,
                                            disabledContainerColor = LocalThemeUiColors.current.border
                                        ),
                                        enabled = hasSelection
                                    ) {
                                        Icon(
                                            Icons.Filled.Download,
                                            contentDescription = null,
                                            modifier = Modifier.size(17.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "下载媒体" + if (hasSelection) "(${selectedImageIdx.size})" else "",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                // ---- 视频模式：封面 + 画质下载（v1.4.32 能力完整保留） ----
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(96.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(LocalThemeUiColors.current.surface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Filled.Image,
                                            contentDescription = null,
                                            tint = themeColors.primary.copy(alpha = 0.3f),
                                            modifier = Modifier.size(36.dp)
                                        )
                                        if (res.coverUrl != null) {
                                            AsyncImage(
                                                model = res.coverUrl,
                                                contentDescription = "视频封面",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .background(
                                                    Color.Black.copy(alpha = 0.6f),
                                                    RoundedCornerShape(topStart = 6.dp)
                                                )
                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                        ) {
                                            Text(res.durationText, fontSize = 9.sp, color = Color.White)
                                        }
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = res.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            color = themeColors.text
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "直链已提取 · 无水印",
                                            fontSize = 11.sp,
                                            color = themeColors.textMuted
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (res.videoUrls.isNotEmpty()) {
                                        val qualities = res.qualityOptions.ifEmpty {
                                            listOf("原画" to res.videoUrls.first())
                                        }
                                        var selectedQuality by remember(res) { mutableStateOf(qualities.first()) }
                                        var showQualityMenu by remember { mutableStateOf(false) }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Button(
                                                onClick = {
                                                    triggerDownload(
                                                        selectedQuality.second,
                                                        "DownVid_${System.currentTimeMillis()}.mp4"
                                                    )
                                                },
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = themeColors.primary
                                                ),
                                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                                modifier = Modifier.height(38.dp)
                                            ) {
                                                Icon(Icons.Filled.Movie, contentDescription = null, modifier = Modifier.size(15.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("下载${selectedQuality.first}视频", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            }
                                            if (qualities.size > 1) {
                                                Box {
                                                    IconButton(onClick = { showQualityMenu = true }) {
                                                        Icon(
                                                            Icons.Filled.ArrowDropDown,
                                                            contentDescription = "选择画质",
                                                            tint = themeColors.primary
                                                        )
                                                    }
                                                    DropdownMenu(
                                                        expanded = showQualityMenu,
                                                        onDismissRequest = { showQualityMenu = false }
                                                    ) {
                                                        qualities.forEach { q ->
                                                            DropdownMenuItem(
                                                                text = { Text(q.first) },
                                                                onClick = {
                                                                    selectedQuality = q
                                                                    showQualityMenu = false
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    // v1.4.33：视频贴文若附带图片，保留旧版一键下载图片能力
                                    if (res.imageUrls.isNotEmpty()) {
                                        Button(
                                            onClick = { triggerImageDownload(res.imageUrls) },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = themeColors.primary.copy(alpha = 0.14f)
                                            ),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            modifier = Modifier.height(38.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.Image,
                                                contentDescription = null,
                                                tint = themeColors.primary,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                "下载图片(${res.imageUrls.size})",
                                                fontSize = 13.sp,
                                                color = themeColors.primary
                                            )
                                        }
                                    }

                                    if (res.videoUrls.isNotEmpty()) {
                                        Button(
                                            onClick = {
                                                val targetUrl = res.audioUrl ?: res.videoUrls.first()
                                                triggerDownload(targetUrl, "DownVid_Audio_${System.currentTimeMillis()}.mp3")
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = themeColors.secondary),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            modifier = Modifier.height(38.dp)
                                        ) {
                                            Icon(Icons.Filled.MusicNote, contentDescription = null, modifier = Modifier.size(15.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("提取音频", fontSize = 13.sp)
                                        }
                                    }

                                    val copyUrl = res.videoUrls.firstOrNull() ?: res.imageUrls.firstOrNull()
                                    if (copyUrl != null) {
                                        Button(
                                            onClick = {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText("媒体直链", copyUrl))
                                                Toast.makeText(context, "已复制直链到剪贴板", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = themeColors.primary.copy(alpha = 0.12f)
                                            ),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                            modifier = Modifier.height(38.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.ContentCopy,
                                                contentDescription = null,
                                                tint = themeColors.primary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("复制直链", color = themeColors.primary, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ---- 卡片3：文案预览（v1.4.33：对标参考图） ----
        if (currentResult != null) {
            val res = currentResult!!
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { descExpanded = !descExpanded },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(themeColors.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Description,
                                    contentDescription = null,
                                    tint = themeColors.text,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "文案预览",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.text
                                )
                                Text(
                                    text = "描述文案",
                                    fontSize = 12.sp,
                                    color = themeColors.textMuted
                                )
                            }
                            Text(
                                text = "已解析",
                                fontSize = 13.sp,
                                color = themeColors.textMuted
                            )
                            Icon(
                                if (descExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (descExpanded) "收起" else "展开",
                                tint = themeColors.textMuted
                            )
                        }

                        if (descExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(LocalThemeUiColors.current.surface)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = res.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = themeColors.text
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "作者：${res.author} · 平台：${res.platform}",
                                        fontSize = 12.sp,
                                        color = themeColors.textMuted
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = res.originalLink,
                                        fontSize = 11.sp,
                                        color = themeColors.textMuted.copy(alpha = 0.7f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(themeColors.primary.copy(alpha = 0.12f))
                                        .clickable {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            clipboard.setPrimaryClip(
                                                ClipData.newPlainText("文案", "${res.title}\n${res.originalLink}")
                                            )
                                            Toast.makeText(context, "文案已复制", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 14.dp, vertical = 7.dp)
                                ) {
                                    Text(
                                        "复制文案",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ---- 分页 Tab 栏 ----
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                containerColor = Color.White.copy(alpha = 0.85f),
                contentColor = themeColors.primary,
                edgePadding = 8.dp
            ) {
                listOf("🔥 支持平台", "📂 解析历史 (${historyList.size})", "💡 使用技巧").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) themeColors.primary else LocalThemeUiColors.current.textMuted
                            )
                        }
                    )
                }
            }
        }

        // ---- Tab 0: 支持平台网格 ----
        if (selectedTab == 0) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "全网多平台无水印下载支持",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColors.primary,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SupportedPlatforms.forEach { p ->
                            PlatformPillCard(platform = p, onClick = {
                                Toast.makeText(context, "${p.name}: ${p.tip}", Toast.LENGTH_LONG).show()
                            })
                        }
                    }
                }
            }
        }

        // ---- Tab 1: 历史记录 ----
        if (selectedTab == 1) {
            if (historyList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.History, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("暂无下载历史，粘贴链接解析吧~", fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
            } else {
                items(historyList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.SmartDisplay, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(item.originalLink, fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            IconButton(onClick = {
                                val url = item.videoUrls.firstOrNull() ?: item.imageUrls.firstOrNull()
                                if (url != null) {
                                    val isImg = item.videoUrls.isEmpty()
                                    if (isImg) triggerImageDownload(listOf(url))
                                    else triggerDownload(url, "DownVid_${System.currentTimeMillis()}.mp4")
                                }
                            }) {
                                Icon(Icons.Filled.Download, contentDescription = "重新下载", tint = themeColors.secondary)
                            }
                            IconButton(onClick = { historyList.remove(item) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "删除记录", tint = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }

        // ---- Tab 2: 使用技巧 ----
        if (selectedTab == 2) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💡 3 步极速下载无水印视频", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = themeColors.primary)
                        Spacer(modifier = Modifier.height(10.dp))
                        TipStepRow("1", "在抖音/快手/小红书/B站点击「分享」按钮")
                        TipStepRow("2", "点击「复制链接」，回到本应用会自动识别粘贴")
                        TipStepRow("3", "点击「一键提取下载」，保存到系统相册与本地")
                    }
                }
            }
        }

    }
}

@Composable
private fun DownVidHeader(onPasteClipboard: () -> Unit) {
    val themeColors = LocalThemeUiColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        themeColors.primary, // 黛墨松绿
                        themeColors.secondary, // 苍翠碧玉
                        Color(0xFF0F303B)  // 远山墨蓝
                    )
                )
            )
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFDE047).copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "DownVid",
                        tint = Color(0xFF0D2818),
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "懒得找了",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Save the videos you like, locally.",
                        fontSize = 11.sp,
                        color = Color(0xFFE2EFE7).copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "全网无水印原画视频/音频提取 · 抖音/快手/B站/YouTube/小红书/微博一网打尽",
                fontSize = 12.sp,
                color = Color(0xFFF2F7F4).copy(alpha = 0.9f),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun PlatformPillCard(platform: DownVidPlatform, onClick: () -> Unit) {
    val themeColors = LocalThemeUiColors.current
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(platform.iconColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(platform.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF132A13))
                Text(platform.tag, fontSize = 9.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun TipStepRow(step: String, text: String) {
    val themeColors = LocalThemeUiColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(themeColors.secondary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(step, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = themeColors.primary)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, fontSize = 12.sp, color = LocalThemeUiColors.current.text)
    }
}

private fun extractUrl(input: String): String {
    val regex = "(https?://[a-zA-Z0-9./?=_%&:-]+)".toRegex()
    return regex.find(input)?.value ?: input
}

private fun detectPlatform(url: String): String {
    val lower = url.lowercase()
    return when {
        "douyin" in lower || "iesdouyin" in lower || "tiktok" in lower -> "抖音/TikTok"
        "kuaishou" in lower || "kwai" in lower -> "快手"
        "bilibili" in lower || "b23.tv" in lower -> "哔哩哔哩"
        "xiaohongshu" in lower || "xhslink" in lower -> "小红书"
        "youtube" in lower || "youtu.be" in lower -> "YouTube"
        "weibo" in lower -> "微博"
        "ixigua" in lower -> "西瓜视频"
        "haokan" in lower -> "好看视频"
        "weishi" in lower -> "微视"
        "pipixia" in lower -> "皮皮虾"
        else -> "短视频"
    }
}

// v1.4.34：自动开关小组件（标签 + Switch，点整行可切换）
@Composable
private fun AutoToggleChip(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    themeColors: ThemeUiColors,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (checked) themeColors.primary.copy(alpha = 0.12f) else LocalThemeUiColors.current.surface)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (checked) themeColors.primary else themeColors.textMuted,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = themeColors.primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = LocalThemeUiColors.current.border
            )
        )
    }
}
