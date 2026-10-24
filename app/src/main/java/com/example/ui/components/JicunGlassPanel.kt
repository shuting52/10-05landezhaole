package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalThemeUiColors

/**
 * 2026-10-24：玻璃拟态卡片 —— 翻译自 shuiyingongju 的 lib/ui/glass.dart GlassPanel。
 *
 * 原版：超椭圆 20dp 圆角（与库其它玻璃面一致），深色白 15%（0x26FFFFFF）/
 * 浅色白 55%（0x8CFFFFFF）。注意这两档透明度不在 Palette 里，硬套 surface 会改掉卡片底色。
 *
 * Compose 版用圆角矩形近似超椭圆；是否深色跟随当前主题。
 */
@Composable
fun JicunGlassPanel(
    modifier: Modifier = Modifier,
    corner: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    // 深色判定：沿用主题体系（jicun_dark 等深色主题走 15% 档）
    val themeColors = LocalThemeUiColors.current
    val isDark = themeColors.background.luminance() < 0.5f
    val bg = if (isDark) Color(0x26FFFFFF) else Color(0x8CFFFFFF)
    val shape = RoundedCornerShape(corner)
    Box(
        modifier = modifier
            .clip(shape)
            .background(bg)
    ) {
        content()
    }
}

/** Color 亮度（0~1），用于判定深浅主题。 */
private fun Color.luminance(): Float {
    return 0.2126f * red + 0.7152f * green + 0.0722f * blue
}
