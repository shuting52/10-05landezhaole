package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ============================================================================
// 国潮主题：纸墨风亮色方案（控制台固定亮色，不随系统深色切换）
// 由 v2.1.0 发布物反编译还原
// ============================================================================

private val GuochaoColorScheme = lightColorScheme(
    primary = Cinnabar,
    onPrimary = Paper,
    primaryContainer = StatCinnabarStart,
    onPrimaryContainer = Cinnabar,
    secondary = Ink,
    onSecondary = Paper,
    secondaryContainer = StatInkStart,
    onSecondaryContainer = Ink,
    tertiary = Gold,
    onTertiary = Color.White,
    tertiaryContainer = StatGoldStart,
    onTertiaryContainer = GoldDark,
    background = Paper,
    onBackground = InkBlack,
    surface = PaperSoft,
    onSurface = InkBlack,
    surfaceVariant = MistSoft,
    onSurfaceVariant = InkBlack.copy(alpha = 0.65f),
    error = Cinnabar,
    onError = Color.White,
    outline = Mist,
    outlineVariant = Mist.copy(alpha = 0.7f),
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    // 国潮纸墨风仅用亮色方案；不使用动态取色，保持品牌一致
    MaterialTheme(
        colorScheme = GuochaoColorScheme,
        typography = Typography,
        content = content,
    )
}
