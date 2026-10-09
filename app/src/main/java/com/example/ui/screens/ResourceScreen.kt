package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.db.UploadedResourceEntity
import com.example.ui.theme.FlameRed
import kotlinx.coroutines.launch

/**
 * 资源中心（v1.4.0）：
 * 1. 彻底移除原「提示词区 / Skill 技能库」双 Tab 切换；
 * 2. 顶部全面升级为多功能搜索栏，直接对接 GitHub 仓库实时资源；
 * 3. 支持「全部」「图片提示词」「视频提示词」「Skill 技能」快捷筛选；
 * 4. 下方指定区域呈现提示词卡片及高清预览图展示，支持一键复制提示词咒语与大图预览。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceScreen(
    resources: List<UploadedResourceEntity>,
    onRefreshCloud: () -> Unit = {},
    onDelete: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") } // all, prompt_image, prompt_video, skill
    var previewResource by remember { mutableStateOf<UploadedResourceEntity?>(null) }
    var isSyncing by remember { mutableStateOf(false) }

    // 筛选类别列表
    val categories = listOf(
        Triple("all", "全部", resources.size),
        Triple("prompt_image", "图片提示词", resources.count { it.type == "prompt_image" }),
        Triple("prompt_video", "视频提示词", resources.count { it.type == "prompt_video" }),
        Triple("skill", "Skill 技能", resources.count { it.type == "skill" })
    )

    // 实时搜索过滤
    val filteredList = remember(resources, searchQuery, selectedCategory) {
        val query = searchQuery.trim().lowercase()
        resources.filter { item ->
            // 类别筛选
            val matchesCategory = when (selectedCategory) {
                "prompt_image" -> item.type == "prompt_image"
                "prompt_video" -> item.type == "prompt_video"
                "skill" -> item.type == "skill"
                else -> true
            }
            if (!matchesCategory) return@filter false

            // 搜索文本筛选
            if (query.isEmpty()) return@filter true
            item.title.lowercase().contains(query) ||
                item.desc.lowercase().contains(query) ||
                item.prompt.lowercase().contains(query) ||
                item.tags.lowercase().contains(query) ||
                item.author.lowercase().contains(query) ||
                item.badge.lowercase().contains(query)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // ========== 1. 顶栏：GitHub 仓库直连状态与实时同步 ==========
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E1B4B).copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.25f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "GitHub 云端直连 · 资源库",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4338CA)
                        )
                    }
                }
            }

            // 刷新 / 同步按钮
            Surface(
                onClick = {
                    if (!isSyncing) {
                        isSyncing = true
                        onRefreshCloud()
                        Toast.makeText(context, "正在从 GitHub 仓库同步最新资源...", Toast.LENGTH_SHORT).show()
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(1200)
                            isSyncing = false
                            Toast.makeText(context, "GitHub 资源同步完成！", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.85f),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shadowElevation = 1.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(13.dp),
                            color = Color(0xFF6366F1)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "同步 GitHub 资源",
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSyncing) "同步中" else "同步",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )
                }
            }
        }

        // ========== 2. 搜索框（取代原双 Tab）==========
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.95f),
                            Color(0xFFF8FAFC).copy(alpha = 0.90f),
                            Color.White.copy(alpha = 0.95f)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF6366F1).copy(alpha = 0.50f),
                            Color(0xFFA855F7).copy(alpha = 0.40f),
                            Color(0xFFEC4899).copy(alpha = 0.45f),
                            Color(0xFF6366F1).copy(alpha = 0.40f)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                placeholder = {
                    Text(
                        text = "搜索资源、提示词咒语、视频、模型、技能...",
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "搜索",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "清除",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ========== 3. 分类标签胶囊行 ==========
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { (type, label, count) ->
                val isSelected = selectedCategory == type
                Surface(
                    onClick = { selectedCategory = type },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFF1E1B4B) else Color.White.copy(alpha = 0.75f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) Color(0xFF1E1B4B) else Color(0xFFE2E8F0)
                    ),
                    shadowElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF475569)
                        )
                        if (count > 0) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFF1F5F9)
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

        Spacer(modifier = Modifier.height(10.dp))

        // ========== 4. 下方指定区域：搜索与资源卡片（含预览图呈现）==========
        if (filteredList.isEmpty()) {
            // 空状态与推荐词
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Palette,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (searchQuery.isNotBlank()) "未搜索到与「$searchQuery」相关的资源" else "正在连接 GitHub 仓库资源库...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "点击下方推荐关键词一键检索：",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    // 推荐关键词快捷选择
                    val suggestions = listOf("赛博朋克", "Sora视频", "汉服唯美", "机车狂飙", "3D潮玩", "架构Skill")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        suggestions.take(3).forEach { tag ->
                            Surface(
                                onClick = { searchQuery = tag },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF475569),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        suggestions.drop(3).forEach { tag ->
                            Surface(
                                onClick = { searchQuery = tag },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF475569),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    ResourceDisplayCard(
                        item = item,
                        onPreview = { previewResource = item },
                        onCopyPrompt = { text ->
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("Prompt", text))
                            Toast.makeText(context, "✨ 已复制提示词咒语至剪贴板！可以直接使用", Toast.LENGTH_SHORT).show()
                        },
                        onAction = { url ->
                            if (url.isNotBlank()) {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "无法打开链接", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    // 预览弹窗
    previewResource?.let { res ->
        ResourceDetailDialog(
            resource = res,
            onDismiss = { previewResource = null },
            onCopyPrompt = { text ->
                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("Prompt", text))
                Toast.makeText(context, "✨ 已复制提示词咒语！", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

/**
 * 资源展示卡片：突出高清预览图呈现、类型徽标、提示词文本框与一键复制
 */
@Composable
private fun ResourceDisplayCard(
    item: UploadedResourceEntity,
    onPreview: () -> Unit,
    onCopyPrompt: (String) -> Unit,
    onAction: (String) -> Unit
) {
    val isImagePrompt = item.type == "prompt_image"
    val isVideoPrompt = item.type == "prompt_video"
    val isPrompt = isImagePrompt || isVideoPrompt || item.prompt.isNotBlank()

    // 默认或后备预览图匹配
    val effectivePreview = remember(item.id, item.previewUrl, item.iconUrl) {
        when {
            item.previewUrl.isNotBlank() -> item.previewUrl
            item.iconUrl.isNotBlank() -> item.iconUrl
            item.title.contains("赛博") || item.desc.contains("赛博") -> "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80"
            item.title.contains("汉服") || item.desc.contains("汉服") -> "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80"
            item.title.contains("金龙") || item.desc.contains("龙") -> "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80"
            item.title.contains("潮玩") || item.desc.contains("盲盒") -> "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80"
            item.title.contains("机车") || item.desc.contains("狂飙") -> "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=800&auto=format&fit=crop&q=80"
            else -> ""
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        border = BorderStroke(1.2.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // ===== 1. 突出预览图呈现区域 =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .clickable { onPreview() }
            ) {
                if (effectivePreview.isNotBlank()) {
                    AsyncImage(
                        model = effectivePreview,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // 无图时炫酷渐变背景与图标
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
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isVideoPrompt) Icons.Filled.Movie else if (isImagePrompt) Icons.Filled.Palette else Icons.Filled.Terminal,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.45f),
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                // 阴影渐变遮罩
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.65f)
                                )
                            )
                        )
                )

                // 左上角：类型徽标
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = when {
                        isImagePrompt -> Color(0xFFF97316).copy(alpha = 0.90f)
                        isVideoPrompt -> Color(0xFF8B5CF6).copy(alpha = 0.90f)
                        else -> Color(0xFF10B981).copy(alpha = 0.90f)
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = when {
                                isImagePrompt -> "🎨 图片提示词"
                                isVideoPrompt -> "🎬 视频提示词"
                                else -> "⚡ Skill 技能"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // 右上角：模型/规格徽标
                if (item.badge.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.60f),
                        border = BorderStroke(0.6.dp, Color.White.copy(alpha = 0.40f)),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = item.badge,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }

                // 右下角：查看大图 / 播放指示
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.90f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = if (isVideoPrompt) Icons.Filled.Movie else Icons.Filled.ZoomIn,
                            contentDescription = "查看详情",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isVideoPrompt) "预览详情" else "预览大图",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                }
            }

            // ===== 2. 卡片主体内容 =====
            Column(modifier = Modifier.padding(14.dp)) {
                // 标题
                Text(
                    text = item.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF0F172A)
                )

                // 描述
                if (item.desc.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.desc,
                        fontSize = 12.5.sp,
                        color = Color(0xFF475569),
                        lineHeight = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // 提示词专区（深色极客风或浅色高对比度容器）
                if (item.prompt.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F172A),
                        border = BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFFFBBF24),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Prompt 咒语指令",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFBBF24)
                                    )
                                }
                                Text(
                                    text = "点击一键复制",
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.prompt,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 16.sp,
                                color = Color(0xFFE2E8F0),
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 底部操作栏与作者信息
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 作者或标签
                    if (item.author.isNotBlank() || item.tags.isNotBlank()) {
                        Text(
                            text = if (item.author.isNotBlank()) "👤 ${item.author}" else "🏷️ ${item.tags}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // 动作按钮
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (item.prompt.isNotBlank()) {
                            Button(
                                onClick = { onCopyPrompt(item.prompt) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF6366F1)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = "复制",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("复制提示词", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        val actionUrl = item.fileUrl.ifBlank { item.url }
                        if (actionUrl.isNotBlank()) {
                            Button(
                                onClick = { onAction(actionUrl) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0F172A)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (item.mode == "file") Icons.Filled.Download else Icons.Filled.OpenInNew,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (item.mode == "file") "下载" else "打开",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
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
 * 资源详细弹窗（支持高清大图全貌与完整提示词）
 */
@Composable
private fun ResourceDetailDialog(
    resource: UploadedResourceEntity,
    onDismiss: () -> Unit,
    onCopyPrompt: (String) -> Unit
) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp)),
            color = Color(0xFF0F172A),
            border = BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // 顶栏关闭按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = resource.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "关闭", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 大图展示
                val imgUrl = resource.previewUrl.ifBlank { resource.iconUrl }
                if (imgUrl.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        AsyncImage(
                            model = imgUrl,
                            contentDescription = resource.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 描述
                if (resource.desc.isNotBlank()) {
                    Text(
                        text = resource.desc,
                        fontSize = 13.sp,
                        color = Color(0xFF94A3B8),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // 完整提示词
                if (resource.prompt.isNotBlank()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, Color(0xFF475569))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "✨ 完整 Prompt 指令：",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFBBF24)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = resource.prompt,
                                fontSize = 11.5.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFFF1F5F9),
                                lineHeight = 17.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // 底部按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (resource.prompt.isNotBlank()) {
                        Button(
                            onClick = { onCopyPrompt(resource.prompt) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                        ) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("复制提示词", fontWeight = FontWeight.Bold)
                        }
                    }

                    val targetUrl = resource.fileUrl.ifBlank { resource.url }
                    if (targetUrl.isNotBlank()) {
                        Button(
                            onClick = {
                                try {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl)))
                                } catch (_: Exception) {}
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text(if (resource.mode == "file") "立即下载" else "访问链接", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
