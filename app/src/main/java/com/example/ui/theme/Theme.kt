package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.ui.uiverse.ActiveUiverseState

enum class AtmosphereEffect {
    NONE,
    STARS,
    SNOW,
    RAIN,
    FIREFLIES,
    AURORA
}

data class ThemePreset(
    val id: String,
    val name: String,
    val style: String,
    val categoryName: String = "经典",
    val primaryColor: Color,
    val secondaryColor: Color,
    val bgColor: Color,
    val surfaceColor: Color,
    val textColor: Color,
    val atmosphereEffect: AtmosphereEffect = AtmosphereEffect.NONE,
    // v1.2.0：渐变主题支持——有值时背景/卡片使用该渐变（从浅到深 2-3 色）
    val gradientColors: List<Color> = emptyList(),
    // v1.2.2：卡片专属渐变（默认经典皮肤保持浅粉→浅紫→浅蓝，切换主题后跟随主题色）
    val cardGradientColors: List<Color> = emptyList()
)

object ThemePresetsRepository {
    // 经典默认主题：雅致国潮红金（赤红主色 + 暖金辅助 + 纯净米白底 + 雅致深褐字）
    val shengshiTheme = ThemePreset(
        id = "shengshi_huadan",
        name = "雅致红金 (默认)",
        style = "classic",
        categoryName = "经典",
        primaryColor = Color(0xFFDE2910),      // 赤红
        secondaryColor = Color(0xFFE8A200),   // 暖金
        bgColor = Color(0xFFFFF7EC),          // 暖米白
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF3B1F1F),        // 深褐
        atmosphereEffect = AtmosphereEffect.NONE,
        cardGradientColors = listOf(Color(0xFFFFE4EC), Color(0xFFE9E4FF), Color(0xFFDCEBFF))
    )

    val defaultTheme = shengshiTheme

    // 经典皮肤（可选，保留供主题切换）
    val classicTheme = ThemePreset(
        id = "classic_default",
        name = "经典皮肤",
        style = "classic",
        categoryName = "经典",
        primaryColor = Color(0xFF2196F3),
        secondaryColor = Color(0xFF42A5F5),
        bgColor = Color(0xFFFAFAFA),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF212121),
        atmosphereEffect = AtmosphereEffect.NONE,
        cardGradientColors = listOf(Color(0xFFFFE4EC), Color(0xFFE9E4FF), Color(0xFFDCEBFF))
    )

    // 盛世华诞为默认软件主题；经典皮肤保留可选
    // v1.2.0：10 款渐变颜色主题（柔和渐变背景 + 对应主色）
    // v1.2.2：cardGradientColors 跟随各自主题色系，切换后卡片同步变色
    val gradientSunset = ThemePreset(
        id = "grad_sunset", name = "落日橘粉", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFFF7E5F), secondaryColor = Color(0xFFFEB47B),
        bgColor = Color(0xFFFFF3EC), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A2C20),
        gradientColors = listOf(Color(0xFFFFD8C4), Color(0xFFFFF0E8)),
        cardGradientColors = listOf(Color(0xFFFFD8C4), Color(0xFFFFE8D9), Color(0xFFFFF0E8))
    )
    val gradientOcean = ThemePreset(
        id = "grad_ocean", name = "海洋蓝", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF2193B0), secondaryColor = Color(0xFF6DD5ED),
        bgColor = Color(0xFFEAF6FB), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF14343F),
        gradientColors = listOf(Color(0xFFC9E9F7), Color(0xFFE8F6FC)),
        cardGradientColors = listOf(Color(0xFFC9E9F7), Color(0xFFD8F0FA), Color(0xFFE8F6FC))
    )
    val gradientMint = ThemePreset(
        id = "grad_mint", name = "薄荷奶绿", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF11998E), secondaryColor = Color(0xFF38EF7D),
        bgColor = Color(0xFFEAFBF2), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF123B2E),
        gradientColors = listOf(Color(0xFFC8F0DC), Color(0xFFEAFBF2)),
        cardGradientColors = listOf(Color(0xFFC8F0DC), Color(0xFFDCF5E8), Color(0xFFEAFBF2))
    )
    val gradientLavender = ThemePreset(
        id = "grad_lavender", name = "薰衣草紫", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF7F7FD5), secondaryColor = Color(0xFF86A8E7),
        bgColor = Color(0xFFF2EFFB), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF2E2A4A),
        gradientColors = listOf(Color(0xFFDCD6F5), Color(0xFFF2EFFB)),
        cardGradientColors = listOf(Color(0xFFDCD6F5), Color(0xFFE8E2F8), Color(0xFFF2EFFB))
    )
    val gradientCherry = ThemePreset(
        id = "grad_cherry", name = "樱花粉", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFF857A6), secondaryColor = Color(0xFFFF5858),
        bgColor = Color(0xFFFFF0F4), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A2030),
        gradientColors = listOf(Color(0xFFFBD3E0), Color(0xFFFFF0F4)),
        cardGradientColors = listOf(Color(0xFFFBD3E0), Color(0xFFFDE4EC), Color(0xFFFFF0F4))
    )
    val gradientSky = ThemePreset(
        id = "grad_sky", name = "天空蓝", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF4A90D9), secondaryColor = Color(0xFF63B8FF),
        bgColor = Color(0xFFEDF5FF), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF1B3A5C),
        gradientColors = listOf(Color(0xFFCFE8FF), Color(0xFFEDF5FF)),
        cardGradientColors = listOf(Color(0xFFCFE8FF), Color(0xFFDDF0FF), Color(0xFFEDF5FF))
    )
    val gradientPeach = ThemePreset(
        id = "grad_peach", name = "蜜桃甜橙", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFFF9A8B), secondaryColor = Color(0xFFFF6A88),
        bgColor = Color(0xFFFFF5F0), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A2A20),
        gradientColors = listOf(Color(0xFFFFD8CC), Color(0xFFFFF5F0)),
        cardGradientColors = listOf(Color(0xFFFFD8CC), Color(0xFFFFE5DC), Color(0xFFFFF5F0))
    )
    val gradientAurora = ThemePreset(
        id = "grad_aurora", name = "极光绿", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF00B4DB), secondaryColor = Color(0xFF0083B0),
        bgColor = Color(0xFFEAF8FA), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF103A40),
        gradientColors = listOf(Color(0xFFC0EEF4), Color(0xFFEAF8FA)),
        cardGradientColors = listOf(Color(0xFFC0EEF4), Color(0xFFD4F4F8), Color(0xFFEAF8FA))
    )
    val gradientRoseGold = ThemePreset(
        id = "grad_rosegold", name = "玫瑰金", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFB76E79), secondaryColor = Color(0xFFEACDC2),
        bgColor = Color(0xFFFAF3F0), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF3A2028),
        gradientColors = listOf(Color(0xFFF0D9D0), Color(0xFFFAF3F0)),
        cardGradientColors = listOf(Color(0xFFF0D9D0), Color(0xFFF7E6DF), Color(0xFFFAF3F0))
    )
    val gradientLemon = ThemePreset(
        id = "grad_lemon", name = "柠檬黄", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFF7971E), secondaryColor = Color(0xFFFFD200),
        bgColor = Color(0xFFFFFAEB), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A3A10),
        gradientColors = listOf(Color(0xFFFFE9A8), Color(0xFFFFFAEB)),
        cardGradientColors = listOf(Color(0xFFFFE9A8), Color(0xFFFFF2C8), Color(0xFFFFFAEB))
    )
    val gradientNebula = ThemePreset(
        id = "grad_nebula", name = "星云蓝紫", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF5C6BC0), secondaryColor = Color(0xFFAB47BC),
        bgColor = Color(0xFFF1EFFA), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF26204A),
        gradientColors = listOf(Color(0xFFD8D0F5), Color(0xFFF1EFFA)),
        cardGradientColors = listOf(Color(0xFFD8D0F5), Color(0xFFE6E0F8), Color(0xFFF1EFFA))
    )

    // 暗黑风格主题预设（暗黑风格按钮切换）
    val darkObsidian = ThemePreset(
        id = "dark_obsidian", name = "深邃曜石暗黑", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFFA855F7), secondaryColor = Color(0xFF38BDF8),
        bgColor = Color(0xFF0F172A), surfaceColor = Color(0xFF1E293B), textColor = Color(0xFFF1F5F9),
        gradientColors = listOf(Color(0xFF0B0F19), Color(0xFF131B2E)),
        cardGradientColors = listOf(Color(0xFF1E293B), Color(0xFF182234), Color(0xFF141C2B))
    )
    val darkMidnight = ThemePreset(
        id = "dark_midnight", name = "极夜星空暗黑", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFF38BDF8), secondaryColor = Color(0xFF818CF8),
        bgColor = Color(0xFF0B132B), surfaceColor = Color(0xFF1C2541), textColor = Color(0xFFE2E8F0),
        gradientColors = listOf(Color(0xFF070B19), Color(0xFF0E1A38)),
        cardGradientColors = listOf(Color(0xFF1C2541), Color(0xFF172038), Color(0xFF121B2F))
    )
    val darkCyberpunk = ThemePreset(
        id = "dark_cyberpunk", name = "赛博朋克深空", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFF06B6D4), secondaryColor = Color(0xFFEC4899),
        bgColor = Color(0xFF0D0D15), surfaceColor = Color(0xFF181824), textColor = Color(0xFFF8FAFC),
        gradientColors = listOf(Color(0xFF0A0A12), Color(0xFF141422)),
        cardGradientColors = listOf(Color(0xFF1F1F30), Color(0xFF181826), Color(0xFF12121E))
    )
    val darkEmerald = ThemePreset(
        id = "dark_emerald", name = "夜间护眼暗绿", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFF10B981), secondaryColor = Color(0xFF34D399),
        bgColor = Color(0xFF061A14), surfaceColor = Color(0xFF0D2E24), textColor = Color(0xFFECFDF5),
        gradientColors = listOf(Color(0xFF041410), Color(0xFF0B241C)),
        cardGradientColors = listOf(Color(0xFF0D2E24), Color(0xFF0A261E), Color(0xFF071E17))
    )

    // iOS 风格主题（iOS 磨砂轻奢、iOS 纯白灵动、iOS 深空灰）
    val iosFrostGlass = ThemePreset(
        id = "ios_frost_glass", name = "iOS 磨砂轻奢", style = "ios", categoryName = "iOS风格",
        primaryColor = Color(0xFF007AFF), secondaryColor = Color(0xFF5856D6),
        bgColor = Color(0xFFF2F2F7), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF1C1C1E),
        gradientColors = listOf(Color(0xFFE5E5EA), Color(0xFFF2F2F7)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF9F9FB), Color(0xFFF2F2F7))
    )
    val iosDynamicWhite = ThemePreset(
        id = "ios_dynamic_white", name = "iOS 纯净灵动", style = "ios", categoryName = "iOS风格",
        primaryColor = Color(0xFF34C759), secondaryColor = Color(0xFF30B0C7),
        bgColor = Color(0xFFF8F9FA), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF111827),
        gradientColors = listOf(Color(0xFFEDF2F7), Color(0xFFF8F9FA)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC), Color(0xFFF1F5F9))
    )
    val iosSpaceGray = ThemePreset(
        id = "ios_space_gray", name = "iOS 深空夜影", style = "ios", categoryName = "iOS风格",
        primaryColor = Color(0xFF0A84FF), secondaryColor = Color(0xFF5E5CE6),
        bgColor = Color(0xFF1C1C1E), surfaceColor = Color(0xFF2C2C2E), textColor = Color(0xFFF2F2F7),
        gradientColors = listOf(Color(0xFF151517), Color(0xFF1C1C1E)),
        cardGradientColors = listOf(Color(0xFF2C2C2E), Color(0xFF242426), Color(0xFF1C1C1E))
    )

    // 新拟态风格主题（柔和凸起/凹陷浮雕光影）
    val neuSoftCream = ThemePreset(
        id = "neu_soft_cream", name = "新拟态·奶油浮雕", style = "neumorphism", categoryName = "新拟态",
        primaryColor = Color(0xFF6366F1), secondaryColor = Color(0xFFEC4899),
        bgColor = Color(0xFFE0E5EC), surfaceColor = Color(0xFFE0E5EC), textColor = Color(0xFF334155),
        gradientColors = listOf(Color(0xFFD6DCE5), Color(0xFFE8EEF5)),
        cardGradientColors = listOf(Color(0xFFE0E5EC), Color(0xFFE4E9F0), Color(0xFFE8EEF5))
    )
    val neuClaySlate = ThemePreset(
        id = "neu_clay_slate", name = "新拟态·冷灰微浮", style = "neumorphism", categoryName = "新拟态",
        primaryColor = Color(0xFF3B82F6), secondaryColor = Color(0xFF10B981),
        bgColor = Color(0xFFEBECF0), surfaceColor = Color(0xFFEBECF0), textColor = Color(0xFF1E293B),
        gradientColors = listOf(Color(0xFFE2E4E9), Color(0xFFF2F3F7)),
        cardGradientColors = listOf(Color(0xFFEBECF0), Color(0xFFEFF0F4), Color(0xFFF4F5F8))
    )
    val neuDarkRelief = ThemePreset(
        id = "neu_dark_relief", name = "新拟态·暗夜浮雕", style = "neumorphism", categoryName = "新拟态",
        primaryColor = Color(0xFF8B5CF6), secondaryColor = Color(0xFF06B6D4),
        bgColor = Color(0xFF24272C), surfaceColor = Color(0xFF24272C), textColor = Color(0xFFF1F5F9),
        gradientColors = listOf(Color(0xFF1C1E22), Color(0xFF2A2D33)),
        cardGradientColors = listOf(Color(0xFF24272C), Color(0xFF2A2E35), Color(0xFF31363E))
    )

    val iosThemes: List<ThemePreset> = listOf(
        iosFrostGlass, iosDynamicWhite, iosSpaceGray
    )

    val neuThemes: List<ThemePreset> = listOf(
        neuSoftCream, neuClaySlate, neuDarkRelief
    )

    val darkThemes: List<ThemePreset> = listOf(
        darkObsidian, darkMidnight, darkCyberpunk, darkEmerald
    )

    val gradientThemes: List<ThemePreset> = listOf(
        gradientSunset, gradientOcean, gradientMint, gradientLavender, gradientCherry,
        gradientSky, gradientPeach, gradientAurora, gradientRoseGold, gradientLemon, gradientNebula
    )

    val allThemes: List<ThemePreset> = listOf(
        shengshiTheme,
        classicTheme
    ) + iosThemes + neuThemes + darkThemes
}

val LocalUiverseState = staticCompositionLocalOf { ActiveUiverseState() }

// ============================================================
// v1.2.2：主题 UI 色板（ThemeUiColors）
// 从当前 ThemePreset 派生一整组 UI 组件颜色，通过 CompositionLocal 全局提供。
// 关键组件（卡片/底栏/按钮/标题）改为从此色板读色，切换主题时 UI 自动跟随变色。
// ============================================================
data class ThemeUiColors(
    val primary: Color = Color(0xFFDE2910),
    val secondary: Color = Color(0xFFE8A200),
    val background: Color = Color(0xFFFFF7EC),
    val surface: Color = Color(0xFFFFFFFF),
    val text: Color = Color(0xFF3B1F1F),
    val textMuted: Color = Color(0xFF7A6A60),
    val accent: Color = Color(0xFFDE2910),
    val border: Color = Color(0xFFFFD700).copy(alpha = 0.55f),
    // 卡片渐变（默认经典=浅粉→浅紫→浅蓝；渐变主题=跟随主题色）
    val cardGradient: List<Color> = listOf(Color(0xFFFFE4EC), Color(0xFFE9E4FF), Color(0xFFDCEBFF))
)

val LocalThemeUiColors = staticCompositionLocalOf { ThemeUiColors() }

fun ThemePreset.toUiColors(): ThemeUiColors {
    val cardGrad = if (cardGradientColors.size >= 2) cardGradientColors
    else if (gradientColors.size >= 2) gradientColors
    else listOf(surfaceColor)
    return ThemeUiColors(
        primary = primaryColor,
        secondary = secondaryColor,
        background = if (gradientColors.size >= 2) gradientColors[0] else bgColor,
        surface = surfaceColor,
        text = textColor,
        textMuted = textColor.copy(alpha = 0.62f),
        accent = primaryColor,
        border = if (cardGrad.size >= 2) primaryColor.copy(alpha = 0.55f) else primaryColor.copy(alpha = 0.35f),
        cardGradient = cardGrad
    )
}

@Composable
fun MyApplicationTheme(
    themePreset: ThemePreset = ThemePresetsRepository.defaultTheme,
    uiverseState: ActiveUiverseState = ActiveUiverseState(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = themePreset.primaryColor,
        secondary = themePreset.secondaryColor,
        background = themePreset.bgColor,
        surface = themePreset.surfaceColor,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = themePreset.textColor,
        onSurface = themePreset.textColor
    )

    CompositionLocalProvider(
        LocalUiverseState provides uiverseState,
        // v1.2.2：提供主题 UI 色板，组件切换主题时自动跟随变色
        LocalThemeUiColors provides themePreset.toUiColors()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
