package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.local.db.UploadedResourceEntity
import com.example.data.util.VideoCache
import com.example.ui.theme.CuteMint
import com.example.ui.theme.CutePink
import com.example.ui.theme.NeonPurple
import kotlinx.coroutines.launch

/**
 * Skill 技能详情弹窗（点击技能卡片后，以独立弹窗形式呈现全部内容）：
 * - 可视化视频预览：mediaUrl 视频缓存本地后播放（失败回退预览图/图标）；无视频时显示预览图
 * - 提示词：完整展示（v1.0.12：取消复制功能）
 * - 操作按钮：严格遵循控制台上传形式——仅「下载」（文件形式下载到本地）/「跳转」（URL 形式）两个按钮
 *   （v1.0.12：取消复制 / 分享 / icon 图标功能）
 */
@Composable
fun SkillDetailDialog(
    res: UploadedResourceEntity,
    onDismiss: () -> Unit,
    onDownload: (url: String, fileName: String?) -> Unit = { _, _ -> },
    onJump: (url: String) -> Unit = {}
) {
    val context = LocalContext.current

    val isVideo = res.mediaUrl.isNotBlank() &&
        Regex("\\.(mp4|webm|mov|m4v|mkv)(\\?.*)?$", RegexOption.IGNORE_CASE).containsMatchIn(res.mediaUrl)
    // v1.0.12：严格遵循控制台上传形式——file 模式 url 存文件直链 →「下载」；url 模式 →「跳转」
    val isFileMode = res.mode != "url"
    val fileUrl = res.fileUrl.ifBlank { res.url }.ifBlank {
        // 兼容旧数据：控制台早期把 zip/md 技能包直链放在 mediaUrl 字段
        if (res.mediaUrl.endsWith(".zip", true) || res.mediaUrl.endsWith(".md", true)) res.mediaUrl else ""
    }
    val jumpUrl = if (isFileMode) "" else res.url.ifBlank { res.fileUrl }
    val canDownload = isFileMode && fileUrl.isNotBlank()
    val canJump = !isFileMode && jumpUrl.isNotBlank()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .fillMaxHeight(0.9f)
                    .clip(RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.White.copy(alpha = 0.74f),
                border = BorderStroke(1.4.dp, Color.White.copy(alpha = 0.92f)),
                tonalElevation = 0.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // ===== 头部 =====
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(NeonPurple, CutePink))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = res.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                if (res.badge.isNotBlank() && res.badge != "用户上传") {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = CutePink.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = res.badge,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CutePink,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = buildString {
                                    append(if (res.author.isNotBlank()) "作者：${res.author}" else "懒得找了 · Skill")
                                    if (res.tags.isNotBlank()) append("  ·  ${res.tags}")
                                },
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Filled.Close, contentDescription = "关闭")
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                    // ===== 内容区 =====
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // --- 可视化视频预览 ---
                        if (isVideo) {
                            VideoPreviewBox(
                                videoUrl = res.mediaUrl,
                                fallbackImageUrl = res.previewUrl,
                                isVideo = true
                            )
                        } else if (res.previewUrl.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(170.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                coil.compose.AsyncImage(
                                    model = res.previewUrl,
                                    contentDescription = res.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Brush.linearGradient(listOf(NeonPurple.copy(alpha = 0.18f), CutePink.copy(alpha = 0.18f)))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Image,
                                    contentDescription = null,
                                    tint = NeonPurple.copy(alpha = 0.6f),
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // --- 描述 ---
                        if (res.desc.isNotBlank()) {
                            Text(
                                text = res.desc,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // --- 提示词（v1.0.12：取消复制按钮，仅完整展示控制台上传的提示词内容）---
                        if (res.prompt.isNotBlank()) {
                            Text(
                                text = "✦ 提示词",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = res.prompt,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "该技能未附带提示词，到官方群反馈获取更多内容～",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // --- 操作按钮：严格遵循控制台上传形式——仅「下载」（文件形式下载到本地）/「跳转」（URL 形式）两个按钮 ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (canDownload) {
                                Button(
                                    onClick = {
                                        val name = res.title + (if (fileUrl.endsWith(".zip", true)) ".zip" else if (fileUrl.endsWith(".apk", true)) ".apk" else if (fileUrl.endsWith(".md", true)) ".md" else "")
                                        onDownload(fileUrl, name)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("下载", fontSize = 12.sp)
                                }
                            }
                            if (canJump) {
                                Button(
                                    onClick = { onJump(jumpUrl) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("跳转", fontSize = 12.sp)
                                }
                            }
                            if (!canDownload && !canJump) {
                                // 控制台未配置文件/URL 时仅展示内容
                                Text(
                                    text = "该技能未配置下载文件或跳转链接",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 视频预览：先用 VideoCache 下载到本地缓存再由 VideoView 播放（无障碍秒开），
 * 未就绪时直接远程直连试播，失败回退预览图 / 占位。
 */
@Composable
private fun VideoPreviewBox(
    videoUrl: String,
    fallbackImageUrl: String,
    isVideo: Boolean
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var localPath by remember(videoUrl) { mutableStateOf<String?>(null) }
    var videoFailed by remember(videoUrl) { mutableStateOf(false) }
    var cacheChecked by remember(videoUrl) { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(videoUrl) {
        try {
            localPath = VideoCache.ensureLocal(context, videoUrl)
        } catch (_: Exception) {}
        if (localPath == null) videoFailed = true
        cacheChecked = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        if (!videoFailed && localPath != null) {
            val localUri = remember(localPath) { android.net.Uri.fromFile(java.io.File(localPath!!)) }
            AndroidView(
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
                        setOnClickListener {
                            if (isPlaying) pause() else start()
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
                modifier = Modifier.fillMaxSize()
            )
        }
        // 加载遮罩：本地视频缓存未完成时显示
        if (!videoFailed && !cacheChecked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.5.dp, modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("视频加载中…", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                }
            }
        }
        // 播放角标
        Surface(
            color = Color.Black.copy(alpha = 0.45f),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "播放预览",
                tint = Color.White,
                modifier = Modifier
                    .padding(10.dp)
                    .size(26.dp)
            )
        }
    }
    // 视频失败回退：预览图
    if (videoFailed) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            if (fallbackImageUrl.isNotBlank()) {
                coil.compose.AsyncImage(
                    model = fallbackImageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("视频预览暂不可用", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}