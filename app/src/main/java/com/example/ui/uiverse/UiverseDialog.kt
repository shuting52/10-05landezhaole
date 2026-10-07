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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.TouchApp
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

    val allDarkThemes = ThemePresetsRepository.darkThemes
    val iosThemes = ThemePresetsRepository.iosThemes
    val neuThemes = ThemePresetsRepository.neuThemes

    LiquidGlassDialogShell(
        onDismissRequest = onClose,
        title = "主题切换",
        subtitle = "液态高透玻璃质感 · 暗黑 / iOS / 新拟态风格",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 顶栏分类快速切换胶囊（全部 / 暗黑极客 / iOS风格 / 新拟态）- 液体玻璃高透半折射质感
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.60f),
                                Color(0xFFF1F5F9).copy(alpha = 0.38f),
                                Color(0xFFF8FAFC).copy(alpha = 0.48f),
                                Color.White.copy(alpha = 0.68f)
                            )
                        )
                    )
                    .border(
                        width = 1.1.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.95f),
                                Color(0xFFCBD5E1).copy(alpha = 0.50f),
                                Color.White.copy(alpha = 0.90f)
                            )
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    "ALL" to "全部精选",
                    "DARK" to "暗黑极客",
                    "IOS" to "iOS风格",
                    "NEU" to "新拟态"
                ).forEach { (tabKey, tabTitle) ->
                    val isTabSelected = selectedCategoryTab == tabKey
                    val tabModifier = if (isTabSelected) {
                        Modifier
                            .background(
                                brush = Brush.linearGradient(listOf(Color(0xFF8B5CF6), Color(0xFF38BDF8))),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                    } else {
                        Modifier.background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .then(tabModifier)
                            .clickable { selectedCategoryTab = tabKey }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabTitle,
                            fontSize = 11.5.sp,
                            fontWeight = if (isTabSelected) FontWeight.Black else FontWeight.SemiBold,
                            color = if (isTabSelected) Color.White else Color(0xFF475569)
                        )
                    }
                }
            }

            // 1. 经典默认与科技蓝
            if (selectedCategoryTab == "ALL") {
                Text(
                    text = "系统经典预设",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                )

                DarkThemeButton(
                    title = "雅致红金 (默认)",
                    subtitle = "经典赤金 · 雅致纯粹 · 官方默认主题",
                    accentColor = Color(0xFFDE2910),
                    isSelected = activeGradientThemeId == ThemePresetsRepository.shengshiTheme.id || activeGradientThemeId.isEmpty(),
                    onClick = {
                        onResetDefault()
                        onApplyGradientTheme(ThemePresetsRepository.shengshiTheme)
                        Toast.makeText(context, "已切换为「雅致红金」默认皮肤", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DarkThemeButton(
                    title = "经典科技蓝",
                    subtitle = "纯净明亮 · 极简冷冽 · 科技护眼",
                    accentColor = Color(0xFF2196F3),
                    isSelected = activeGradientThemeId == ThemePresetsRepository.classicTheme.id,
                    onClick = {
                        onApplyGradientTheme(ThemePresetsRepository.classicTheme)
                        Toast.makeText(context, "已切换为「经典科技蓝」皮肤", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 2. iOS 风格主题区
            if (selectedCategoryTab == "ALL" || selectedCategoryTab == "IOS") {
                Text(
                    text = "🍏 iOS 磨砂轻奢与纯白灵动",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF007AFF),
                    modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                )

                iosThemes.forEach { iosTheme ->
                    DarkThemeButton(
                        title = iosTheme.name,
                        subtitle = when (iosTheme.id) {
                            "ios_frost_glass" -> "苹果经典毛玻璃质感 · 动态微透冷白"
                            "ios_dynamic_white" -> "清新纯净绿水灵动 · 极简圆润界面"
                            "ios_space_gray" -> "iOS 专属深空夜影 · 旗舰暗色质感"
                            else -> "iOS 顶级工业设计美学"
                        },
                        accentColor = iosTheme.primaryColor,
                        secondaryColor = iosTheme.secondaryColor,
                        isSelected = activeGradientThemeId == iosTheme.id,
                        onClick = {
                            onApplyGradientTheme(iosTheme)
                            Toast.makeText(context, "已开启「${iosTheme.name}」", Toast.LENGTH_SHORT).show()
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 3. 新拟态 Neumorphism 风格主题区
            if (selectedCategoryTab == "ALL" || selectedCategoryTab == "NEU") {
                Text(
                    text = "🔮 新拟态浮雕光影 (Neumorphism)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6366F1),
                    modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                )

                neuThemes.forEach { neuTheme ->
                    DarkThemeButton(
                        title = neuTheme.name,
                        subtitle = when (neuTheme.id) {
                            "neu_soft_cream" -> "奶油微凸柔和浮雕 · 空间凹凸光影"
                            "neu_clay_slate" -> "冷灰粘土拟物微浮 · 平滑微阴影"
                            "neu_dark_relief" -> "暗夜神秘浮雕光影 · 紫青双束泛光"
                            else -> "极简新拟态空间立体感"
                        },
                        accentColor = neuTheme.primaryColor,
                        secondaryColor = neuTheme.secondaryColor,
                        isSelected = activeGradientThemeId == neuTheme.id,
                        onClick = {
                            onApplyGradientTheme(neuTheme)
                            Toast.makeText(context, "已开启「${neuTheme.name}」", Toast.LENGTH_SHORT).show()
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 4. 暗黑极客风格主题区
            if (selectedCategoryTab == "ALL" || selectedCategoryTab == "DARK") {
                Text(
                    text = "🌌 暗黑极客质感矩阵",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA855F7),
                    modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                )

                allDarkThemes.forEach { darkTheme ->
                    DarkThemeButton(
                        title = darkTheme.name,
                        subtitle = when (darkTheme.id) {
                            "dark_obsidian" -> "曜石紫金流光 · 极客深邃黑"
                            "dark_midnight" -> "极夜深蓝星空 · 沉稳冷峻"
                            "dark_cyberpunk" -> "赛博霓虹电光 · 炫彩粉青"
                            "dark_emerald" -> "暗夜翡翠幽绿 · 柔光护眼"
                            else -> "高级暗黑格调质感"
                        },
                        accentColor = darkTheme.primaryColor,
                        secondaryColor = darkTheme.secondaryColor,
                        isSelected = activeGradientThemeId == darkTheme.id,
                        onClick = {
                            onApplyGradientTheme(darkTheme)
                            Toast.makeText(context, "已开启暗黑模式「${darkTheme.name}」", Toast.LENGTH_SHORT).show()
                            onClose()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
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
                        onApplyGradientTheme(ThemePresetsRepository.shengshiTheme)
                        Toast.makeText(context, "已重置为系统默认皮肤", Toast.LENGTH_SHORT).show()
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
                            maxLines = 1
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
