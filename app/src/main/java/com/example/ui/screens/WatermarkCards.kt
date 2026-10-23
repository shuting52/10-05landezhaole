package com.example.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.util.MediaParseResult
import com.example.download.DownloadJobState
import com.example.download.WatermarkDownloadItem
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.ThemeUiColors
import kotlinx.coroutines.delay

// ============================================================
// 2026-10-23：jicun 式去水印卡片 —— 按内容拆卡（媒体/图集/音频/文案），
// 有什么显示什么；下载带应用内进度；图集多选；音频独立卡。
// 解析仍用本地 VideoLinkParser 引擎（jicun 的解析走其自家付费接口，不可复用）。
// ============================================================

/** 文件名清洗：去话题/emoji/非法字符，限长（jicun 式命名规则简化版） */
internal fun cleanFileName(raw: String?, fallback: String, ext: String): String {
    var s = raw ?: ""
    s = s.replace(Regex("#[^#\\s]+"), "")
    s = s.replace(Regex("[\\p{So}\\p{Cn}]"), "")
    s = s.replace(Regex("[/\\\\:*?\"<>|]"), "").trim().replace(Regex("\\s+"), " ")
    if (s.isEmpty()) s = fallback
    if (s.length > 40) s = s.take(40).trim()
    if (s.endsWith(".$ext", ignoreCase = true)) s = s.dropLast(ext.length + 1)
    return "$s.$ext"
}


internal fun guessImageExt(url: String): String =
    url.substringBefore('?').substringAfterLast('.', "").lowercase()
        .takeIf { it in setOf("jpg", "jpeg", "png", "webp", "gif") } ?: "jpg"

// ---------------- 可折叠卡片基座 ----------------

@Composable
internal fun CollapsibleCard(
    title: String,
    icon: ImageVector,
    subtitle: String = "",
    defaultExpanded: Boolean = true,
    theme: ThemeUiColors,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(defaultExpanded) }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(theme.cardGradient))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { expanded = !expanded }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = theme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = theme.text)
                if (subtitle.isNotBlank()) {
                    Spacer(Modifier.width(8.dp))
                    Text(subtitle, fontSize = 12.sp, color = theme.textMuted)
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "收起" else "展开",
                    tint = theme.textMuted,
                    modifier = Modifier.size(22.dp)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    content = content
                )
            }
        }
    }
}

// ---------------- 结果分发：有什么显示什么 ----------------

@Composable
internal fun JicunResultCards(
    media: MediaParseResult,
    selectedQuality: Int,
    onQualitySelected: (Int) -> Unit,
    onImageClick: (String) -> Unit,
    onDownloadRequested: (items: List<WatermarkDownloadItem>, title: String) -> Unit
) {
    val theme = LocalThemeUiColors.current
    val context = LocalContext.current

    val hasVideo = media.videoUrls.isNotEmpty()
    val hasImages = media.imageUrls.isNotEmpty()
    // jicun hasStandaloneAudio 规则：独立音轨且与视频地址不同
    val mainVideo = media.videoUrls.firstOrNull()
    val hasAudio = !media.audioUrl.isNullOrBlank() && media.audioUrl != mainVideo
    val descText = buildString {
        if (!media.description.isNullOrBlank()) append(media.description)
        if (media.topics.isNotEmpty()) {
            if (isNotEmpty()) append("\n")
            append(media.topics.joinToString(" "))
        }
    }

    // 头部：平台 + 作者 + 标题
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(theme.primary)
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                media.platform?.ifBlank { null } ?: "短视频",
                color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(8.dp))
        if (!media.author.isNullOrBlank()) {
            Text(media.author!!, fontSize = 13.sp, color = theme.textMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
    if (!media.title.isNullOrBlank()) {
        Text(media.title!!, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = theme.text, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }

    // 媒体卡：视频（+ 混合图集）
    if (hasVideo) {
        MediaPreviewCard(
            media = media,
            selectedQuality = selectedQuality,
            onQualitySelected = onQualitySelected,
            onImageClick = onImageClick,
            onDownloadRequested = onDownloadRequested,
            theme = theme
        )
    } else if (hasImages) {
        // 纯图集卡
        GalleryPreviewCard(
            media = media,
            onImageClick = onImageClick,
            onDownloadRequested = onDownloadRequested,
            theme = theme
        )
    }

    // 音频卡（默认收起）
    if (hasAudio) {
        AudioPreviewCard(
            media = media,
            onDownloadRequested = onDownloadRequested,
            theme = theme
        )
    }

    // 文案卡（默认收起）
    if (descText.isNotBlank()) {
        TextPreviewCard(descText = descText, theme = theme)
    }
}

// ---------------- 媒体卡（视频 / 混合） ----------------

@Composable
private fun MediaPreviewCard(
    media: MediaParseResult,
    selectedQuality: Int,
    onQualitySelected: (Int) -> Unit,
    onImageClick: (String) -> Unit,
    onDownloadRequested: (items: List<WatermarkDownloadItem>, title: String) -> Unit,
    theme: ThemeUiColors
) {
    val context = LocalContext.current
    val currentVideoUrl = when {
        media.qualityOptions.isNotEmpty() -> media.qualityOptions[selectedQuality.coerceIn(media.qualityOptions.indices)].second
        media.videoUrls.isNotEmpty() -> media.videoUrls.first()
        else -> null
    }
    val isMixed = media.imageUrls.isNotEmpty()
    var selectedImages by remember(media) { mutableStateOf(media.imageUrls.indices.toSet()) }

    CollapsibleCard(
        title = if (isMixed) "媒体预览" else "视频预览",
        icon = Icons.Filled.Videocam,
        subtitle = if (isMixed) "视频 + ${media.imageUrls.size} 张图" else "",
        theme = theme
    ) {
        currentVideoUrl?.let { url ->
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoURI(Uri.parse(url))
                        setMediaController(MediaController(ctx))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
            )
        }

        // 清晰度
        if (media.qualityOptions.size > 1) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("清晰度", fontSize = 12.sp, color = theme.textMuted)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(media.qualityOptions) { idx, (label, _) ->
                        val selected = idx == selectedQuality
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(if (selected) theme.primary else theme.primary.copy(alpha = 0.10f))
                                .clickable { onQualitySelected(idx) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                label, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                color = if (selected) Color.White else theme.primary
                            )
                        }
                    }
                }
            }
        }

        // 混合图集（多选）
        if (isMixed) {
            GallerySelector(
                imageUrls = media.imageUrls,
                selected = selectedImages,
                onToggle = { idx ->
                    selectedImages = if (idx in selectedImages) selectedImages - idx else selectedImages + idx
                },
                onSelectAll = {
                    selectedImages = if (selectedImages.size == media.imageUrls.size) emptySet()
                    else media.imageUrls.indices.toSet()
                },
                onImageClick = onImageClick,
                theme = theme
            )
        }

        // 下载按钮（jicun 分段下载器）
        Button(
            onClick = {
                val items = mutableListOf<WatermarkDownloadItem>()
                currentVideoUrl?.let { url ->
                    val name = cleanFileName(media.title, "video_${System.currentTimeMillis() % 100000}", "mp4")
                    items.add(WatermarkDownloadItem(url, name, Environment.DIRECTORY_MOVIES))
                }
                if (isMixed) {
                    val ts = System.currentTimeMillis() % 100000
                    selectedImages.sorted().forEachIndexed { i, idx ->
                        val url = media.imageUrls[idx]
                        val name = cleanFileName("${media.title}_$i", "img_${ts}_$i", guessImageExt(url))
                        items.add(WatermarkDownloadItem(url, name, Environment.DIRECTORY_PICTURES))
                    }
                }
                if (items.isEmpty()) {
                    Toast.makeText(context, "没有可下载的内容", Toast.LENGTH_SHORT).show()
                } else {
                    onDownloadRequested(items, media.title?.take(20) ?: "媒体")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = theme.primary)
        ) {
            Icon(Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                "下载" + if (isMixed && selectedImages.isNotEmpty()) "（视频+${selectedImages.size}张图）" else "",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------------- 图集卡（纯图片，多选） ----------------

@Composable
private fun GalleryPreviewCard(
    media: MediaParseResult,
    onImageClick: (String) -> Unit,
    onDownloadRequested: (items: List<WatermarkDownloadItem>, title: String) -> Unit,
    theme: ThemeUiColors
) {
    val context = LocalContext.current
    var selected by remember(media) { mutableStateOf(media.imageUrls.indices.toSet()) }

    CollapsibleCard(
        title = "图集预览",
        icon = Icons.Filled.Image,
        subtitle = "${media.imageUrls.size} 张 · 点选多选",
        theme = theme
    ) {
        GallerySelector(
            imageUrls = media.imageUrls,
            selected = selected,
            onToggle = { idx -> selected = if (idx in selected) selected - idx else selected + idx },
            onSelectAll = {
                selected = if (selected.size == media.imageUrls.size) emptySet()
                else media.imageUrls.indices.toSet()
            },
            onImageClick = onImageClick,
            theme = theme
        )
        Button(
            onClick = {
                if (selected.isEmpty()) {
                    Toast.makeText(context, "请先点选要下载的图片", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val items = mutableListOf<WatermarkDownloadItem>()
                val ts = System.currentTimeMillis() % 100000
                selected.sorted().forEachIndexed { i, idx ->
                    val url = media.imageUrls[idx]
                    val name = cleanFileName("${media.title}_$i", "img_${ts}_$i", guessImageExt(url))
                    items.add(WatermarkDownloadItem(url, name, Environment.DIRECTORY_PICTURES))
                }
                if (items.isEmpty()) {
                    Toast.makeText(context, "请先点选要下载的图片", Toast.LENGTH_SHORT).show()
                } else {
                    onDownloadRequested(items, "${selected.size} 张图片")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = theme.primary)
        ) {
            Icon(Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("下载选中（${selected.size}）", fontWeight = FontWeight.Bold)
        }
    }
}

// ---------------- 图集多选器（共用） ----------------

@Composable
private fun GallerySelector(
    imageUrls: List<String>,
    selected: Set<Int>,
    onToggle: (Int) -> Unit,
    onSelectAll: () -> Unit,
    onImageClick: (String) -> Unit,
    theme: ThemeUiColors
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("共 ${imageUrls.size} 张 · 已选 ${selected.size}", fontSize = 12.sp, color = theme.textMuted)
            TextButton(onClick = onSelectAll) {
                Text(
                    if (selected.size == imageUrls.size) "取消全选" else "全选",
                    fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = theme.primary
                )
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(imageUrls) { idx, url ->
                val isSel = idx in selected
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            2.5.dp,
                            if (isSel) theme.primary else Color.Transparent,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { onToggle(idx) }
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                    // 选中勾
                    if (isSel) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(4.dp)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(theme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                    // 眼睛看大图
                    IconButton(
                        onClick = { onImageClick(url) },
                        modifier = Modifier.align(Alignment.BottomEnd).size(30.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Visibility, contentDescription = "看大图", tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

// ---------------- 音频卡 ----------------

@Composable
private fun AudioPreviewCard(
    media: MediaParseResult,
    onDownloadRequested: (items: List<WatermarkDownloadItem>, title: String) -> Unit,
    theme: ThemeUiColors
) {
    val context = LocalContext.current
    val audioUrl = media.audioUrl ?: return

    CollapsibleCard(
        title = "音频",
        icon = Icons.Filled.MusicNote,
        subtitle = media.audioTitle?.take(18) ?: "独立音轨",
        defaultExpanded = false,
        theme = theme
    ) {
        if (!media.audioTitle.isNullOrBlank()) {
            Text(media.audioTitle!!, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = theme.text, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        AudioPlayerBar(url = audioUrl, theme = theme)
        Button(
            onClick = {
                val name = cleanFileName(media.audioTitle ?: media.title, "audio_${System.currentTimeMillis() % 100000}", "mp3")
                onDownloadRequested(
                    listOf(WatermarkDownloadItem(audioUrl, name, Environment.DIRECTORY_MUSIC)),
                    media.audioTitle?.take(20) ?: "音频"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = theme.primary)
        ) {
            Icon(Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("下载音频", fontWeight = FontWeight.Bold)
        }
    }
}

// ---------------- 音频迷你播放器 ----------------

@Composable
private fun AudioPlayerBar(url: String, theme: ThemeUiColors) {
    var isPlaying by remember { mutableStateOf(false) }
    var prepared by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf(0) }
    var position by remember { mutableStateOf(0) }
    val playerHolder = remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(url) {
        val mp = MediaPlayer()
        playerHolder.value = mp
        try {
            mp.setDataSource(url)
            mp.setOnPreparedListener {
                prepared = true
                duration = mp.duration.coerceAtLeast(0)
            }
            mp.setOnCompletionListener {
                isPlaying = false
                position = 0
                try { mp.seekTo(0) } catch (_: Exception) { }
            }
            mp.setOnErrorListener { _, _, _ -> true }
            mp.prepareAsync()
        } catch (_: Exception) { }
        onDispose {
            try { mp.release() } catch (_: Exception) { }
            playerHolder.value = null
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(500)
            playerHolder.value?.let {
                try { position = it.currentPosition } catch (_: Exception) { }
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val mp = playerHolder.value ?: return@IconButton
                if (!prepared) return@IconButton
                try {
                    if (isPlaying) { mp.pause(); isPlaying = false }
                    else { mp.start(); isPlaying = true }
                } catch (_: Exception) { }
            },
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(theme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "暂停" else "播放",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Slider(
            value = position.toFloat(),
            onValueChange = {
                position = it.toInt()
                try { playerHolder.value?.seekTo(it.toInt()) } catch (_: Exception) { }
            },
            valueRange = 0f..duration.coerceAtLeast(1).toFloat(),
            enabled = prepared,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = theme.primary,
                activeTrackColor = theme.primary,
                inactiveTrackColor = theme.primary.copy(alpha = 0.2f)
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "${formatMs(position)} / ${formatMs(duration)}",
            fontSize = 11.sp,
            color = theme.textMuted
        )
    }
}

private fun formatMs(ms: Int): String {
    if (ms <= 0) return "0:00"
    val s = ms / 1000
    return "%d:%02d".format(s / 60, s % 60)
}

// ---------------- 文案卡 ----------------

@Composable
private fun TextPreviewCard(descText: String, theme: ThemeUiColors) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    CollapsibleCard(
        title = "文案",
        icon = Icons.Filled.Description,
        subtitle = "点展开查看全文",
        defaultExpanded = false,
        theme = theme
    ) {
        Text(
            descText,
            fontSize = 13.sp,
            color = theme.text,
            lineHeight = 19.sp,
            maxLines = if (expanded) Int.MAX_VALUE else 6,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { expanded = !expanded }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "收起" else "展开全文", fontSize = 12.5.sp, color = theme.primary)
            }
            Button(
                onClick = {
                    try {
                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        cm.setPrimaryClip(android.content.ClipData.newPlainText("watermark", descText))
                        Toast.makeText(context, "文案已复制", Toast.LENGTH_SHORT).show()
                    } catch (_: Exception) { }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = theme.primary.copy(alpha = 0.12f))
            ) {
                Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = theme.primary, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(4.dp))
                Text("复制文案", fontSize = 12.5.sp, color = theme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ---------------- 下载进度弹窗（jicun 式：进度环 + 实时速度 + ETA + 取消） ----------------
@Composable
internal fun DownloadProgressDialog(job: DownloadJobState, onClose: () -> Unit) {
    val theme = LocalThemeUiColors.current
    val progress by job.progress.collectAsState()
    val speedText by job.speedText.collectAsState()
    val etaText by job.etaText.collectAsState()
    val done by job.done.collectAsState()
    val failed by job.failed.collectAsState()
    val errorMsg by job.errorMsg.collectAsState()
    val doneCount by job.doneCount.collectAsState()
    var cancelling by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { if (done || failed) onClose() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.linearGradient(theme.cardGradient))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    when {
                        done -> "下载完成"
                        failed -> "下载失败"
                        else -> "正在下载"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = theme.text,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { if (done || failed) onClose() }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.Close, contentDescription = "关闭", tint = theme.textMuted, modifier = Modifier.size(18.dp))
                }
            }
            Text(
                "${job.title}" + if (job.totalItems > 1 && !done) "（$doneCount/${job.totalItems}）" else "",
                fontSize = 13.sp,
                color = theme.textMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(110.dp),
                    strokeWidth = 9.dp,
                    color = theme.primary,
                    trackColor = theme.primary.copy(alpha = 0.15f)
                )
                Text(
                    "${(progress * 100).toInt()}%",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = theme.text
                )
            }
            if (!done && !failed) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (speedText.isNotBlank()) Text(speedText, fontSize = 13.sp, color = theme.primary, fontWeight = FontWeight.Bold)
                    if (etaText.isNotBlank()) Text(etaText, fontSize = 13.sp, color = theme.textMuted)
                }
            }
            if (failed) {
                Text(errorMsg ?: "下载失败，可重新解析后重试", fontSize = 13.sp, color = Color(0xFFC0392B))
            }
            if (done) {
                Text("已保存到相册 / 文件", fontSize = 13.sp, color = theme.textMuted)
            }
            Button(
                onClick = {
                    if (done || failed) {
                        onClose()
                    } else {
                        cancelling = true
                        job.cancelled.set(true)
                    }
                },
                enabled = !cancelling || done || failed,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (done || failed) theme.primary else Color(0xFFEF4444)
                )
            ) {
                Text(
                    when {
                        done || failed -> "完成"
                        cancelling -> "正在取消…"
                        else -> "取消下载"
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
