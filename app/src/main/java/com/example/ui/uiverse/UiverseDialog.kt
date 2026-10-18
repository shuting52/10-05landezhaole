package com.example.ui.uiverse

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassDialogShell
import com.example.ui.components.streamingBorder
import com.example.ui.theme.DesignStyle
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.ThemePreset
import com.example.ui.theme.ThemePresetsRepository

/**
 * 主题外观与个性化中心
 * 支持毛毡风格、手绘风格、新拟态风格、经典主题实时切换，全软件界面即刻同步应用。
 * 保留用户本地图片/视频自定义背景设置。
 */
@Composable
fun UiverseDialog(
    isOpen: Boolean,
    onClose: () -> Unit,
    activeState: ActiveUiverseState = ActiveUiverseState(),
    onApplyKit: (UiKitPreset) -> Unit = {},
    onApplyCustomCss: (css: String, html: String) -> Unit = { _, _ -> },
    onResetDefault: () -> Unit = {},
    onApplyItemAsComponent: (UiverseItem) -> Unit = {},
    onApplyComponentTheme: (compId: String, css: String) -> Unit = { _, _ -> },
    componentThemes: Map<String, String> = emptyMap(),
    localBgMediaType: String = "none",
    onPickLocalImage: () -> Unit = {},
    onPickLocalVideo: () -> Unit = {},
    onClearLocalBgMedia: () -> Unit = {},
    onApplyGradientTheme: (ThemePreset) -> Unit = {},
    onApplyDynamicEffect: (DynamicEffectPreset) -> Unit = {},
    activeGradientThemeId: String = "",
    activeDynamicEffect: DynamicEffectPreset = DynamicEffectPreset.NONE
) {
    if (!isOpen) return

    val context = LocalContext.current
    val currentUiColors = LocalThemeUiColors.current

    // 分类 Tab：0: 毛毡风格, 1: 手绘风格, 2: 新拟态, 3: 经典风格, 4: 自定义背景
    var selectedTab by remember { mutableIntStateOf(0) }
    val categories = listOf("🧶 毛毡风格", "✏️ 手绘风格", "☁️ 新拟态", "🎨 经典热门", "🖼️ 自定义背景")

    LiquidGlassDialogShell(
        onDismissRequest = onClose,
        title = "主题外观与个性化",
        subtitle = "点击任意主题，全软件界面实时同步应用",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 分类选择 Tab
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp)),
                containerColor = Color.White.copy(alpha = 0.6f),
                contentColor = currentUiColors.primary
            ) {
                categories.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.5.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) currentUiColors.primary else Color(0xFF64748B)
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    // 毛毡风格专区
                    ThemeSectionHeader(
                        title = "毛毡手作工坊",
                        badge = "温润质朴 · 粗纺质感",
                        badgeColor = Color(0xFFC86446)
                    )
                    ThemePresetsRepository.feltThemes.forEach { preset ->
                        ThemeCard(
                            preset = preset,
                            isActive = preset.id == activeGradientThemeId,
                            onClick = {
                                onApplyGradientTheme(preset)
                                Toast.makeText(context, "✨ 已应用「${preset.name}」毛毡主题，全界面实时生效", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                1 -> {
                    // 手绘风格专区
                    ThemeSectionHeader(
                        title = "治愈系手绘馆",
                        badge = "插画随笔 · 纸本素描",
                        badgeColor = Color(0xFFFF6B81)
                    )
                    ThemePresetsRepository.handDrawnThemes.forEach { preset ->
                        ThemeCard(
                            preset = preset,
                            isActive = preset.id == activeGradientThemeId,
                            onClick = {
                                onApplyGradientTheme(preset)
                                Toast.makeText(context, "✨ 已应用「${preset.name}」手绘主题，全界面实时生效", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                2 -> {
                    // 新拟态风格专区
                    ThemeSectionHeader(
                        title = "极简新拟态空间",
                        badge = "柔和浮雕 · 凹凸雕琢",
                        badgeColor = Color(0xFF4F46E5)
                    )
                    ThemePresetsRepository.neuThemes.forEach { preset ->
                        ThemeCard(
                            preset = preset,
                            isActive = preset.id == activeGradientThemeId,
                            onClick = {
                                onApplyGradientTheme(preset)
                                Toast.makeText(context, "✨ 已应用「${preset.name}」新拟态主题，全界面实时生效", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                3 -> {
                    // 经典与热门专区
                    ThemeSectionHeader(
                        title = "经典与流行系列",
                        badge = "官方甄选 · 百变颜值",
                        badgeColor = Color(0xFFFF4D2E)
                    )
                    ThemePresetsRepository.classicThemes.forEach { preset ->
                        ThemeCard(
                            preset = preset,
                            isActive = preset.id == activeGradientThemeId,
                            onClick = {
                                onApplyGradientTheme(preset)
                                Toast.makeText(context, "✨ 已应用「${preset.name}」主题，全界面实时生效", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                4 -> {
                    // 自定义背景
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.85f))
                            .border(1.dp, currentUiColors.primary.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Image, contentDescription = null, tint = currentUiColors.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("自定义图片/视频背景", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            Spacer(modifier = Modifier.weight(1f))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (localBgMediaType == "none") Color(0xFFE2E8F0) else currentUiColors.primary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = when (localBgMediaType) {
                                        "image" -> "已应用图片背景"
                                        "video" -> "已应用视频背景"
                                        else -> "未设置"
                                    },
                                    fontSize = 11.sp,
                                    color = if (localBgMediaType == "none") Color(0xFF64748B) else currentUiColors.primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "上传手机本地喜欢的壁纸图片或 MP4 动态视频，作为全软件的动态背景底图（自动循环播放并适配深浅）",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onPickLocalImage,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = currentUiColors.primary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("上传图片", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = onPickLocalVideo,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = currentUiColors.secondary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Filled.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("上传视频", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                            if (localBgMediaType != "none") {
                                OutlinedButton(
                                    onClick = onClearLocalBgMedia,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                                    modifier = Modifier.weight(0.8f)
                                ) {
                                    Icon(Icons.Filled.DeleteOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("清除", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 底部关闭按钮
            Button(
                onClick = onClose,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = currentUiColors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text("完成设置", color = Color.White, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ThemeSectionHeader(
    title: String,
    badge: String,
    badgeColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = badgeColor.copy(alpha = 0.12f)
        ) {
            Text(
                text = badge,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun ThemeCard(
    preset: ThemePreset,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val cardBg = if (isActive) Color.White else Color.White.copy(alpha = 0.85f)
    val borderColor = if (isActive) preset.primaryColor else Color(0xFFCBD5E1).copy(alpha = 0.6f)
    val borderWidth = if (isActive) 2.dp else 1.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(borderWidth, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 风格色板预览块
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(preset.bgColor)
                    .border(1.5.dp, preset.primaryColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // 内部小方块展示 primary 与 secondary
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(preset.primaryColor, preset.secondaryColor)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isActive) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = preset.name,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val styleLabel = when (preset.designStyle) {
                        DesignStyle.FELT_CRAFT -> "毛毡手工"
                        DesignStyle.HAND_DRAWN -> "手绘涂鸦"
                        DesignStyle.NEUMORPHISM -> "新拟态"
                        DesignStyle.CARTOON -> "Q版卡通"
                        DesignStyle.GUOCHAO -> "国潮古风"
                        DesignStyle.CYBERPUNK -> "赛博霓虹"
                        DesignStyle.LIQUID_GLASS -> "液态玻璃"
                        DesignStyle.NEO_BRUTALISM -> "新丑撞色"
                        else -> "经典"
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = preset.primaryColor.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = styleLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = preset.primaryColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = preset.description.ifBlank { "全软件界面色调深度适配" },
                    fontSize = 11.5.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                // 色轮预览点
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(preset.primaryColor, preset.secondaryColor, preset.bgColor, preset.surfaceColor).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(0.5.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 状态徽标
            if (isActive) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF10B981).copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "已应用",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669)
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = preset.primaryColor.copy(alpha = 0.1f),
                    modifier = Modifier.clickable { onClick() }
                ) {
                    Text(
                        text = "应用",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = preset.primaryColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
