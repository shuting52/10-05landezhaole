package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.local.db.UploadedResourceEntity
import com.example.data.util.VideoCache
import com.example.ui.components.UiverseAmber
import com.example.ui.components.UiverseInk
import com.example.ui.components.UiverseTextMuted
import com.example.ui.components.neoShadow
import com.example.ui.theme.FlameRed
import com.example.ui.theme.SunsetOrange

/**
 * 提示词区：展示控制台云端同步的提示词（图片提示词 / 视频提示词）。
 * 支持预览图展示、点击查看大图、一键复制提示词。
 */
@Composable
fun PromptHubSubView(
    prompts: List<UploadedResourceEntity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedType by remember { mutableStateOf<String?>(null) } // null=全部, image, video
    var previewing by remember { mutableStateOf<UploadedResourceEntity?>(null) }
    // v1.7.4：全局只有一个视频正在播放（带声音）——避免多个视频同时出声
    var activeVideoId by remember { mutableStateOf<String?>(null) }
    fun stopOtherVideos(activeId: String?) {
        if (activeVideoId != activeId) activeVideoId = activeId
    }

    val imageList = prompts.filter { it.type == "prompt_image" }
    val videoList = prompts.filter { it.type == "prompt_video" }

    // v1.9.0：全部分类列表优化——图片/视频分组，视频按标题排序，视频卡片间加大间距并显示标题分隔条
    val filteredList = remember(selectedType, prompts) {
        when (selectedType) {
            "image" -> imageList
            "video" -> videoList.sortedWith(compareBy<UploadedResourceEntity> { it.title.lowercase() })
            else -> prompts
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // v1.8.7：子分类向右向左滑动切换（LazyRow 横向滚动分类条 + 内容区左右滑手势切换）
        // 分类：全部 / 图片提示词 / 视频提示词
        val typeTabs = listOf(
            Pair(null as String?, "全部 (${prompts.size})"),
            Pair("image", "图片提示词 (${imageList.size})"),
            Pair("video", "视频提示词 (${videoList.size})")
        )
        val typeIndex = typeTabs.indexOfFirst { it.first == selectedType }.coerceAtLeast(0)
        // 提示词子分类选项卡（精美液态玻璃半透底板 + 渐变流光微描边 + 高级胶囊指示器）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.88f),
                            Color(0xFFFFF7ED).copy(alpha = 0.75f),
                            Color(0xFFFFF1F2).copy(alpha = 0.70f),
                            Color.White.copy(alpha = 0.92f)
                        )
                    )
                )
                .border(
                    width = 1.3.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFFFB800).copy(alpha = 0.50f),
                            Color(0xFFFF5E00).copy(alpha = 0.40f),
                            Color(0xFF9333EA).copy(alpha = 0.35f),
                            Color(0xFFFFB800).copy(alpha = 0.45f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                typeTabs.forEach { (type, label) ->
                    val isSelected = selectedType == type
                    val cleanText = if (type == null) "全部" else label.substringBefore(" (")
                    val count = when (type) {
                        "image" -> imageList.size
                        "video" -> videoList.size
                        else -> prompts.size
                    }

                    Surface(
                        onClick = { selectedType = type },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) {
                            Color(0xFFFF9500)
                        } else {
                            Color.Transparent
                        },
                        shadowElevation = if (isSelected) 3.dp else 0.dp,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = cleanText,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                fontSize = 13.5.sp,
                                maxLines = 1
                            )
                            if (count > 0) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) Color.White.copy(alpha = 0.28f) else Color(0xFFF1F5F9)
                                ) {
                                    Text(
                                        text = "$count",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color(0xFF64748B),
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Palette,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
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
        } else {
            // v1.8.7：内容区左右滑动手势切换子分类（左滑→下一个，右滑→上一个）
            var dragAccum by remember { mutableStateOf(0f) }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(selectedType) {
                        detectHorizontalDragGestures(
                            onDragStart = { dragAccum = 0f },
                            onDragEnd = {
                                val cur = typeTabs.indexOfFirst { it.first == selectedType }.coerceAtLeast(0)
                                val switched = when {
                                    dragAccum > 60f -> { // 向右滑→上一个
                                        val prev = (cur - 1).coerceAtLeast(0)
                                        if (prev != cur) selectedType = typeTabs[prev].first
                                        prev != cur
                                    }
                                    dragAccum < -60f -> { // 向左滑→下一个
                                        val next = (cur + 1).coerceAtMost(typeTabs.size - 1)
                                        if (next != cur) selectedType = typeTabs[next].first
                                        next != cur
                                    }
                                    else -> false
                                }
                                dragAccum = 0f
                            },
                            onDragCancel = { dragAccum = 0f }
                        ) { change, dragAmount ->
                            change.consume()
                            dragAccum += dragAmount
                        }
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // v1.9.0：全部分类列表——图片与视频分组展示，视频前显示标题分隔条，间距拉开更直观
                if (selectedType == null) {
                    // 图片组
                    if (imageList.isNotEmpty()) {
                        item(key = "section_image") {
                            SectionDivider(
                                title = "图片提示词",
                                count = imageList.size,
                                color = Color(0xFFDE2910)
                            )
                        }
                        items(imageList, key = { it.id }) { prompt ->
                            CloudPromptCard(
                                prompt = prompt,
                                activeVideoId = activeVideoId,
                                onVideoActivate = { id -> stopOtherVideos(id) },
                                onImageClick = {
                                    previewing = prompt
                                    stopOtherVideos("preview_" + prompt.id)
                                },
                                onCopy = { text ->
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cm.setPrimaryClip(ClipData.newPlainText("Prompt", text))
                                    Toast.makeText(context, "已复制提示词！可以直接在Midjourney/FLUX/Sora中使用", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                    // 视频组（按标题排序，每个视频前显示标题分隔条）
                    val sortedVideos = videoList.sortedWith(compareBy<UploadedResourceEntity> { it.title.lowercase() })
                    if (sortedVideos.isNotEmpty()) {
                        item(key = "section_video") {
                            SectionDivider(
                                title = "视频提示词",
                                count = sortedVideos.size,
                                color = Color(0xFFF59E0B)
                            )
                        }
                        sortedVideos.forEachIndexed { index, prompt ->
                            // 每个视频标题作为该视频的独立分隔条（v1.9.0 需求：视频标题作分割线）
                            item(key = "vt_" + prompt.id) {
                                VideoTitleDivider(title = prompt.title, index = index + 1)
                            }
                            item(key = prompt.id) {
                                CloudPromptCard(
                                    prompt = prompt,
                                    activeVideoId = activeVideoId,
                                    onVideoActivate = { id -> stopOtherVideos(id) },
                                    onImageClick = {
                                        previewing = prompt
                                        stopOtherVideos("preview_" + prompt.id)
                                    },
                                    onCopy = { text ->
                                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        cm.setPrimaryClip(ClipData.newPlainText("Prompt", text))
                                        Toast.makeText(context, "已复制提示词！可以直接在Midjourney/FLUX/Sora中使用", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { prompt ->
                        CloudPromptCard(
                            prompt = prompt,
                            activeVideoId = activeVideoId,
                            onVideoActivate = { id -> stopOtherVideos(id) },
                            onImageClick = {
                                previewing = prompt
                                // 打开全屏预览时停掉列表中的卡片视频，避免同时出声
                                stopOtherVideos("preview_" + prompt.id)
                            },
                            onCopy = { text ->
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cm.setPrimaryClip(ClipData.newPlainText("Prompt", text))
                                Toast.makeText(context, "已复制提示词！可以直接在Midjourney/FLUX/Sora中使用", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

    // 大图预览 Dialog
    previewing?.let { prompt ->
        CloudPromptPreviewDialog(
            prompt = prompt,
            onDismiss = { previewing = null },
            onCopy = { text ->
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("Prompt", text))
                Toast.makeText(context, "已复制提示词！", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

/** v1.9.0：分类分隔条（图片组/视频组分组标题） */
@Composable
private fun SectionDivider(
    title: String,
    count: Int,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(16.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$count 个",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        )
    }
}

/** v1.9.0：视频标题分隔条（每个视频的标题作为分割线，直观分开展示） */
@Composable
private fun VideoTitleDivider(
    title: String,
    index: Int = 0
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF59E0B).copy(alpha = 0.12f),
        border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.35f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = if (index > 0) "? $index" else "?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF59E0B)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title.ifBlank { "视频提示词" },
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CloudPromptCard(
    prompt: UploadedResourceEntity,
    activeVideoId: String? = null,
    onVideoActivate: (String) -> Unit = {},
    onImageClick: () -> Unit,
    onCopy: (String) -> Unit
) {
    val isVideo = prompt.type == "prompt_video"
    // v1.7.4：当前卡片是否为“正在播放（带声音）”的视频
    val isActiveVideo = isVideo && activeVideoId == prompt.id
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.58f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.72f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // 可视化预览：视频提示词优先播放演示视频（失败时回退预览图，避免黑屏），图片提示词展示预览图
            // v1.7.6 修复视频预览延迟：有预览图时默认显示预览图（AsyncImage 即时加载），点击后才打开全屏播放视频（带声音），
            // 列表不再内嵌 VideoView 预加载——彻底解决预览卡顿/多视频同时缓冲问题
            if (isVideo && prompt.mediaUrl.isNotBlank() && prompt.previewUrl.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .clickable { onImageClick() }
                ) {
                    AsyncImage(
                        model = prompt.previewUrl,
                        contentDescription = prompt.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // 底部渐变遮罩 + 播放按钮角标
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
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.55f))
                            .border(1.5.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("▶", color = Color.White, fontSize = 22.sp)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "? 视频 · 点击播放（带声音）",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            } else if (isVideo && prompt.mediaUrl.isNotBlank()) {
                var videoFailed by remember(prompt.mediaUrl) { mutableStateOf(false) }
                val context = androidx.compose.ui.platform.LocalContext.current
                var localPath by remember(prompt.mediaUrl) { mutableStateOf<String?>(null) }
                var cacheChecked by remember(prompt.mediaUrl) { mutableStateOf(false) }

                // 仅当用户点击激活当前视频时才下载并初始化单个 VideoView，杜绝列表同时创建多个解码器导致 Codec2 资源报错
                androidx.compose.runtime.LaunchedEffect(prompt.mediaUrl, isActiveVideo) {
                    if (isActiveVideo && !cacheChecked) {
                        try {
                            localPath = VideoCache.ensureLocal(context, prompt.mediaUrl)
                        } catch (_: Exception) {}
                        cacheChecked = true
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(Color(0xFF14161F))
                ) {
                    if (isActiveVideo && !videoFailed) {
                        val effectiveUri = remember(localPath, cacheChecked, prompt.mediaUrl) {
                            when {
                                localPath != null -> android.net.Uri.fromFile(java.io.File(localPath!!))
                                cacheChecked -> android.net.Uri.parse(
                                    com.example.data.util.VideoCache.normalizeMediaUrl(prompt.mediaUrl)
                                )
                                else -> null
                            }
                        }

                        if (effectiveUri != null) {
                            androidx.compose.ui.viewinterop.AndroidView(
                                factory = { ctx ->
                                    android.widget.VideoView(ctx).apply {
                                        setOnPreparedListener { mp ->
                                            mp.isLooping = true
                                            mp.setVolume(1f, 1f)
                                            mp.start()
                                        }
                                        setOnErrorListener { _, _, _ ->
                                            videoFailed = true
                                            true
                                        }
                                    }
                                },
                                update = { view ->
                                    val targetTag = effectiveUri.toString()
                                    if (view.tag != targetTag) {
                                        view.tag = targetTag
                                        view.setVideoURI(effectiveUri)
                                    }
                                    if (!view.isPlaying) {
                                        view.start()
                                    }
                                },
                                onRelease = { view ->
                                    try {
                                        view.stopPlayback()
                                    } catch (_: Exception) {}
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(170.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    androidx.compose.material3.CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("正在加载视频…", color = Color.White, fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        // 未激活播放时：展示预览图或精致影院级待播封面（不创建后台解码器）
                        if (prompt.previewUrl.isNotBlank()) {
                            AsyncImage(
                                model = prompt.previewUrl,
                                contentDescription = prompt.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                Color(0xFF1E1B4B),
                                                Color(0xFF31102F),
                                                Color(0xFF0F172A)
                                            )
                                        )
                                    )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.55f))
                                    .border(1.5.dp, Color.White.copy(alpha = 0.85f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("▶", color = Color.White, fontSize = 20.sp)
                            }
                        }
                    }

                    // 点击切换播放/暂停
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                if (isActiveVideo) {
                                    onVideoActivate("")
                                } else {
                                    videoFailed = false
                                    onVideoActivate(prompt.id)
                                }
                            }
                    )

                    // 右上角状态角标
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = if (isActiveVideo) 0.55f else 0.45f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (isActiveVideo) "🔊 播放中" else "🎬 点击播放",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            } else if (prompt.previewUrl.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .clickable { onImageClick() }
                ) {
                    AsyncImage(
                        model = prompt.previewUrl,
                        contentDescription = prompt.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.15f),
                                        Color.Black.copy(alpha = 0.45f)
                                    )
                                )
                            )
                    )
                    // 类型角标
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.55f),
                        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(FlameRed)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isVideo) "🎬 视频提示词" else "🖼 图片提示词",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    // 查看大图
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .clickable { onImageClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Filled.ZoomIn, contentDescription = "查看大图", tint = Color.Black, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "预览大图", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 内容区
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = prompt.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (prompt.desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prompt.desc,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (prompt.prompt.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.50f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.60f))
                    ) {
                        Text(
                            text = prompt.prompt,
                            fontSize = 11.5.sp,
                            lineHeight = 17.sp,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (prompt.author.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.60f),
                            border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.80f))
                        ) {
                            Text(
                                text = "作者: ${prompt.author}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Button(
                        onClick = { onCopy(prompt.prompt) },
                        enabled = prompt.prompt.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 5.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("复制提示词", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CloudPromptPreviewDialog(
    prompt: UploadedResourceEntity,
    onDismiss: () -> Unit,
    onCopy: (String) -> Unit
) {
    // 是否进入全屏视频模式（沉浸式播放）
    var fullscreenVideo by remember { mutableStateOf(false) }

    if (fullscreenVideo) {
        val context = androidx.compose.ui.platform.LocalContext.current
        var fsLocalPath by remember(prompt.mediaUrl) { mutableStateOf<String?>(null) }
        var fsCacheChecked by remember(prompt.mediaUrl) { mutableStateOf(false) }
        androidx.compose.runtime.LaunchedEffect(prompt.mediaUrl) {
            try { fsLocalPath = com.example.data.util.VideoCache.ensureLocal(context, prompt.mediaUrl) } catch (_: Exception) {}
            fsCacheChecked = true
        }
        Dialog(
            onDismissRequest = { fullscreenVideo = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { fullscreenVideo = false }
            ) {
                val effectiveFsUri = remember(fsLocalPath, fsCacheChecked, prompt.mediaUrl) {
                    when {
                        fsLocalPath != null -> android.net.Uri.fromFile(java.io.File(fsLocalPath!!))
                        fsCacheChecked -> android.net.Uri.parse(com.example.data.util.VideoCache.normalizeMediaUrl(prompt.mediaUrl))
                        else -> null
                    }
                }
                if (effectiveFsUri != null) {
                    androidx.compose.ui.viewinterop.AndroidView(
                        factory = { ctx ->
                            android.widget.VideoView(ctx).apply {
                                setOnPreparedListener { mp ->
                                    mp.isLooping = true
                                    mp.setVolume(1f, 1f)
                                    mp.start()
                                }
                                setOnErrorListener { _, _, _ -> true }
                                layoutParams = android.view.ViewGroup.LayoutParams(
                                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                    android.view.ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        },
                        update = { view ->
                            val targetTag = effectiveFsUri.toString()
                            if (view.tag != targetTag) {
                                view.tag = targetTag
                                view.setVideoURI(effectiveFsUri)
                            }
                        },
                        onRelease = { view ->
                            try { view.stopPlayback() } catch (_: Exception) {}
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            androidx.compose.material3.CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "正在加载视频…",
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                // 顶部返回按钮
                Surface(
                    onClick = { fullscreenVideo = false },
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "退出全屏",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp).size(20.dp)
                    )
                }
                Text(
                    text = "点击任意处退出全屏",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                )
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable { onDismiss() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E24).copy(alpha = 0.95f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.20f))
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = prompt.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (prompt.type == "prompt_video") "🎬 视频提示词" else "🖼 图片提示词",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.65f)
                            )
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.Close, contentDescription = "关闭", tint = Color.White)
                        }
                    }

                    // 视频提示词：优先内嵌播放演示视频 + 全屏按钮；图片提示词展示预览图
                    if (prompt.type == "prompt_video" && prompt.mediaUrl.isNotBlank()) {
                        var videoFailed by remember(prompt.mediaUrl) { mutableStateOf(false) }
                        val context = androidx.compose.ui.platform.LocalContext.current
                        var localPath by remember(prompt.mediaUrl) { mutableStateOf<String?>(null) }
                        var cacheChecked by remember(prompt.mediaUrl) { mutableStateOf(false) }
                        androidx.compose.runtime.LaunchedEffect(prompt.mediaUrl) {
                            try { localPath = com.example.data.util.VideoCache.ensureLocal(context, prompt.mediaUrl) } catch (_: Exception) {}
                            if (localPath == null) videoFailed = true
                            cacheChecked = true
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .background(Color.Black)
                        ) {
                            if (!fullscreenVideo && !videoFailed && localPath != null) {
                                val localUri = remember(localPath) { android.net.Uri.fromFile(java.io.File(localPath!!)) }
                                androidx.compose.ui.viewinterop.AndroidView(
                                    factory = { ctx ->
                                        android.widget.VideoView(ctx).apply {
                                            setOnPreparedListener { mp ->
                                                mp.isLooping = true
                                                mp.setVolume(1f, 1f)
                                                mp.start()
                                            }
                                            setOnErrorListener { _, _, _ ->
                                                videoFailed = true
                                                true
                                            }
                                        }
                                    },
                                    update = { view ->
                                        val targetTag = localUri.toString()
                                        if (view.tag != targetTag) {
                                            view.tag = targetTag
                                            view.setVideoURI(localUri)
                                        }
                                    },
                                    onRelease = { view ->
                                        try { view.stopPlayback() } catch (_: Exception) {}
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp)
                                )
                            } else if (prompt.previewUrl.isNotBlank()) {
                                AsyncImage(
                                    model = prompt.previewUrl,
                                    contentDescription = prompt.title,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            if (!cacheChecked) {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.material3.CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            // 视频预览：点击视频区域即进入全屏播放（无文字标记，轻触即开）
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { fullscreenVideo = true }
                            )
                        }
                    } else if (prompt.previewUrl.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .background(Color.Black)
                        ) {
                            AsyncImage(
                                model = prompt.previewUrl,
                                contentDescription = prompt.title,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        if (prompt.desc.isNotBlank()) {
                            Text(
                                text = prompt.desc,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 17.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        if (prompt.prompt.isNotBlank()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.08f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "AI PROMPT (可直接粘贴到生图/视频模型):",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD54F)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = prompt.prompt,
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        color = Color.White.copy(alpha = 0.90f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { onCopy(prompt.prompt) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("一键复制提示词")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
