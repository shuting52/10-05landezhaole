package com.example.ui.uiverse

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.ui.components.LiquidGlassDialogShell
import com.example.ui.components.streamingBorder
import com.example.ui.theme.ThemePreset
import com.example.ui.theme.ThemePresetsRepository

/**
 * 主题切换弹窗：
 * - 纯正液态高透玻璃质感背景呈现
 * - 边框周围七彩霓虹慢速流光模糊跑动特效
 * - 国庆文字已全部移除，换为雅致默认红金与极简暗黑/iOS/新拟态三大格调
 * - 增加 iOS 风格主题（iOS 磨砂轻奢、iOS 纯净灵动、iOS 深空夜影）
 * - 增加 新拟态风格主题（奶油浮雕、冷灰微浮、暗夜浮雕）
 * - 保留 暗黑风格矩阵（深邃曜石、极夜星空、赛博朋克深空、夜间护眼暗绿）
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
    var selectedCategoryTab by remember { mutableStateOf("ALL") }

    LiquidGlassDialogShell(
        onDismissRequest = onClose,
        title = "主题切换",
        subtitle = "黏土拟态 · 极简主义 · 新拟态 · 玻璃拟态 · iOS风格",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // v1.4.13：黏土风分类胶囊（v1.4.22 新增：极简 / 新拟态）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFF6EC))
                    .border(1.5.dp, Color(0xFFE8590C).copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                    .padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    "ALL" to "全部",
                    "CLAY" to "黏土",
                    "HANDDRAWN" to "手绘",
                    "MINIMAL" to "极简",
                    "NEU" to "新拟态",
                    "GLASS" to "玻璃",
                    "IOS" to "iOS"
                ).forEach { (tabKey, tabTitle) ->
                    val isTabSelected = selectedCategoryTab == tabKey
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(
                                if (isTabSelected) Color(0xFFE8590C)
                                else Color.Transparent
                            )
                            // 黏土立体感：选中时底部厚阴影
                            .then(
                                if (isTabSelected) Modifier.shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color(0xFFE8590C).copy(alpha = 0.4f))
                                else Modifier
                            )
                            .clickable { selectedCategoryTab = tabKey }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabTitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTabSelected) Color.White else Color(0xFF8A6A4A)
                        )
                    }
                }
            }

            // v1.4.13：黏土风主题卡片网格（2列，3D黏土质感）
            @Composable
            fun clayThemeCard(theme: ThemePreset) {
                val isSelected = activeGradientThemeId == theme.id || (activeGradientThemeId.isEmpty() && theme.id == ThemePresetsRepository.defaultTheme.id)
                // 黏土效果：双层背景模拟厚度 + 选中时内压
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(theme.secondaryColor.copy(alpha = 0.55f)) // 底部厚度层
                        .padding(bottom = if (isSelected) 2.dp else 5.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(theme.surfaceColor, theme.bgColor)
                            )
                        )
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) theme.primaryColor else theme.primaryColor.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            onApplyGradientTheme(theme)
                            Toast.makeText(context, "已切换为「${theme.name}」", Toast.LENGTH_SHORT).show()
                            onClose()
                        }
                        .padding(12.dp)
                ) {
                    Column {
                        // 迷你预览：三色圆点 + 渐变条
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.linearGradient(
                                            listOf(theme.primaryColor, theme.secondaryColor)
                                        )
                                    )
                                    .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape)
                            )
                            Column {
                                Text(
                                    text = theme.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = theme.textColor
                                )
                                Text(
                                    text = theme.categoryName,
                                    fontSize = 10.sp,
                                    color = theme.textColor.copy(alpha = 0.6f)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (isSelected) {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = "已选中",
                                    tint = theme.primaryColor,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // 渐变预览条
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(26.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        if (theme.gradientColors.size >= 2) theme.gradientColors
                                        else listOf(theme.bgColor, theme.surfaceColor)
                                    )
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = theme.description,
                            fontSize = 10.sp,
                            color = theme.textColor.copy(alpha = 0.55f),
                            maxLines = 1
                        )
                    }
                }
            }

            val visibleThemes = when (selectedCategoryTab) {
                "CLAY" -> ThemePresetsRepository.clayThemes
                "HANDDRAWN" -> ThemePresetsRepository.handDrawnThemes
                "MINIMAL" -> ThemePresetsRepository.minimalThemes
                "NEU" -> ThemePresetsRepository.neuThemes
                "GLASS" -> ThemePresetsRepository.glassThemes
                "IOS" -> ThemePresetsRepository.iosThemes
                else -> ThemePresetsRepository.allThemes
            }

            // 2列网格
            visibleThemes.chunked(2).forEach { rowThemes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowThemes.forEach { theme ->
                        Box(modifier = Modifier.weight(1f)) {
                            clayThemeCard(theme)
                        }
                    }
                    // 奇数时补齐
                    if (rowThemes.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // v1.4.22：自定义背景 —— 用户上传图片/视频作为软件背景
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.75f))
                    .border(1.dp, Color(0xFFE8590C).copy(alpha = 0.2f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Image, contentDescription = null, tint = Color(0xFFE8590C), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("自定义背景", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A2C12))
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = when (localBgMediaType) {
                            "image" -> "已设置图片"
                            "video" -> "已设置视频"
                            else -> "未设置"
                        },
                        fontSize = 11.sp,
                        color = if (localBgMediaType == "none") Color(0xFF8A6A4A) else Color(0xFFE8590C),
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "上传本地图片或视频作为全软件背景（自动循环播放），不影响主题配色",
                    fontSize = 10.5.sp, color = Color(0xFF8A6A4A), lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onPickLocalImage,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上传图片", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = onPickLocalVideo,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Videocam, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上传视频", fontSize = 12.sp)
                    }
                    if (localBgMediaType != "none") {
                        OutlinedButton(
                            onClick = onClearLocalBgMedia,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.DeleteOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("清除", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 底部操作栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onResetDefault()
                        onApplyGradientTheme(ThemePresetsRepository.defaultTheme)
                        Toast.makeText(context, "已重置为「奶油团子」默认皮肤", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("重置默认", fontSize = 12.sp)
                }

                Button(
                    onClick = onClose,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier
                        .weight(1f)
                        .streamingBorder(
                            cornerRadius = 14.dp,
                            strokeWidth = 1.4.dp,
                            glowWidth = 3.dp,
                            baseBorderColor = Color(0xFFA855F7).copy(alpha = 0.5f),
                            rainbow = true,
                            showGlow = true
                        )
                ) {
                    Text("完成", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * 液体玻璃专属主题卡片（高透折射微弧曲面反射 + 七彩流光跑动边框）
 */
@Composable
private fun DarkThemeButton(
    title: String,
    subtitle: String,
    accentColor: Color,
    secondaryColor: Color = accentColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    Surface(
        onClick = onClick,
        shape = cardShape,
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.8.dp else 1.1.dp,
            color = if (isSelected) accentColor else Color.White.copy(alpha = 0.85f)
        ),
        modifier = modifier
            .clip(cardShape)
            .background(
                brush = Brush.linearGradient(
                    colors = if (isSelected) {
                        listOf(
                            Color.White.copy(alpha = 0.90f),
                            Color(0xFFFAF5FF).copy(alpha = 0.78f),
                            Color(0xFFF3E8FF).copy(alpha = 0.68f),
                            Color.White.copy(alpha = 0.94f)
                        )
                    } else {
                        listOf(
                            Color.White.copy(alpha = 0.62f),
                            Color(0xFFF8FAFC).copy(alpha = 0.44f),
                            Color(0xFFEDE9FE).copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.70f)
                        )
                    },
                    start = Offset(0f, 0f),
                    end = Offset(800f, 260f)
                )
            )
            .then(
                if (isSelected) {
                    Modifier.streamingBorder(
                        cornerRadius = 16.dp,
                        strokeWidth = 1.6.dp,
                        glowWidth = 3.2.dp,
                        baseBorderColor = accentColor.copy(alpha = 0.6f),
                        rainbow = true,
                        showGlow = true
                    )
                } else Modifier
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // 液体玻璃顶部曲面光学折射高光带
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isSelected) 0.68f else 0.45f),
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = h * 0.42f
                    ),
                    topLeft = Offset(4.dp.toPx(), 2.dp.toPx()),
                    size = Size(w - 8.dp.toPx(), h * 0.38f),
                    cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // 颜色指示光点
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(accentColor, secondaryColor)
                                )
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.9f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) accentColor else Color(0xFF1E293B),
                            maxLines = 1
                        )
                        Text(
                            text = subtitle,
                            fontSize = 10.5.sp,
                            color = if (isSelected) accentColor.copy(alpha = 0.85f) else Color(0xFF64748B),
                            maxLines = 2,
                            lineHeight = 13.5.sp
                        )
                    }
                }

                if (isSelected) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(accentColor.copy(alpha = 0.16f))
                            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "当前使用",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }
            }
        }
    }
}
