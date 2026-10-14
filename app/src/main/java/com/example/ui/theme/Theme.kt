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

enum class DesignStyle(val displayName: String, val desc: String) {
    DEFAULT("默认", "经典自然微质感"),
    SKEUOMORPHISM("Skeuomorphism 拟物化", "模仿现实物体的质感、纹理和效果，让用户感到熟悉、亲切。"),
    FLAT_DESIGN("Flat Design 扁平化", "作为对拟物化的“反叛”，其特点是简洁、二维、无阴影和纹理，强调内容本身。"),
    MATERIAL_DESIGN("Material Design 材质设计", "由Google推出，介于扁平与拟物之间，通过层级、阴影和动画模拟纸张和墨水的物理世界，提供清晰的空间感。"),
    NEUMORPHISM("Neumorphism 新拟态", "用和背景相似的卡片，通过精致的阴影（包括内阴影）来模拟出浮雕或嵌入的立体效果。"),
    GLASSMORPHISM("Glassmorphism 玻璃拟态", "模拟磨砂玻璃的质感，通过透明度和背景模糊创建层次感鲜明的毛玻璃效果。"),
    CLAYMORPHISM("Claymorphism 粘土拟态", "相比新拟态，它更像是Q版的3D风格：使用明亮的色彩、圆润的边角和厚实的阴影，如同玩具一般可爱。"),
    LIQUID_GLASS("Liquid Glass 液态玻璃", "被称为“动态的玻璃拟态”，增加了动态响应和光线折射效果，是玻璃拟态的进化版。"),
    NEO_BRUTALISM("Neo-Brutalism 新粗野主义", "一种“反精致”的风格，使用粗重的边框、强烈的对比色和直接大胆的排版，极具视觉冲击力。"),
    MINIMALISM("Minimalism 极简主义", "“少即是多”。大量留白、无装饰的排版和有限的配色，只保留最核心的元素。"),
    BAUHAUS("Bauhaus 包豪斯风格", "强调形式服务于功能，喜欢使用基础的几何形状（圆形、方形、三角形）和红、黄、蓝三原色。"),
    RETROFUTURISM("Retrofuturism 复古未来主义", "它是“过去的未来”，常混合蒸汽波、赛博朋克（霓虹灯、故障艺术）等元素，用复古视角想象未来世界。"),
    BRUTALISM("Brutalism 粗野主义", "新粗野主义的前身，它更极致，完全放弃装饰、结构外露，用纯粹功能性的元素直接呈现，显得有些“原始”。"),
    ANTI_DESIGN("Anti-Design 反设计", "以一种混乱、不和谐、朋克风的方式，挑战传统的美学和可用性规则。"),
    ACID_GRAPHICS("Acid Graphics 酸性设计", "灵感源自迷幻视觉，常使用高饱和度、循环的几何图形、液态金属感以及混乱的排版，视觉冲击力极强。")
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
    val cardGradientColors: List<Color> = emptyList(),
    val designStyle: DesignStyle = DesignStyle.DEFAULT,
    val description: String = ""
)


object ThemePresetsRepository {
    // ============================================================
    // v1.4.13：主题系统重写 —— 移除全部旧主题（含默认），6 款新主题：
    // 黏土拟态 ×2 / 磨砂玻璃 / 液态玻璃 / iOS 清新 / iOS 暗夜
    // 每款都带 gradientColors，切换时背景必跟随变化。
    // ============================================================

    /** 橙子汽水 —— 3D 黏土拟态（默认主题） */
    val clayOrange = ThemePreset(
        id = "clay_orange",
        name = "橙子汽水",
        style = "clay",
        categoryName = "黏土拟态",
        primaryColor = Color(0xFFE8590C),
        secondaryColor = Color(0xFFFF9A3D),
        bgColor = Color(0xFFFFF3E6),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF4A2C12),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFE0B8), Color(0xFFFFF6EC)),
        cardGradientColors = listOf(Color(0xFFFFD8A8), Color(0xFFFFE8CC), Color(0xFFFFF3E6)),
        designStyle = DesignStyle.CLAYMORPHISM,
        description = "Q版3D黏土 · 暖橙可爱 · 默认"
    )

    /** 巧克力派 —— 3D 黏土拟态（暗） */
    val clayChoco = ThemePreset(
        id = "clay_choco",
        name = "巧克力派",
        style = "clay",
        categoryName = "黏土拟态",
        primaryColor = Color(0xFFFF9A3D),
        secondaryColor = Color(0xFFE8590C),
        bgColor = Color(0xFF2B1D14),
        surfaceColor = Color(0xFF3E2A1D),
        textColor = Color(0xFFFFF3E6),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFF3E2A1D), Color(0xFF241811)),
        cardGradientColors = listOf(Color(0xFF4A3423), Color(0xFF3E2A1D), Color(0xFF2B1D14)),
        designStyle = DesignStyle.CLAYMORPHISM,
        description = "浓郁巧克力 · 暗夜黏土"
    )

    /** 磨砂玻璃 —— 玻璃拟态（浅） */
    val frostGlass = ThemePreset(
        id = "frost_glass",
        name = "磨砂玻璃",
        style = "glass",
        categoryName = "玻璃拟态",
        primaryColor = Color(0xFF2E7CD6),
        secondaryColor = Color(0xFF7BB8F0),
        bgColor = Color(0xFFEEF4FA),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1E3A5C),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFDCEBF8), Color(0xFFF7FBFE)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F7FD), Color(0xFFE8F2FA)),
        designStyle = DesignStyle.GLASSMORPHISM,
        description = "毛玻璃质感 · 通透轻盈"
    )

    /** 液态玻璃 —— 液态玻璃（动态光感） */
    val liquidGlass = ThemePreset(
        id = "liquid_glass",
        name = "液态玻璃",
        style = "glass",
        categoryName = "液态玻璃",
        primaryColor = Color(0xFF7C5CFF),
        secondaryColor = Color(0xFF4AD9E8),
        bgColor = Color(0xFFEDE9FE),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF2D2350),
        atmosphereEffect = AtmosphereEffect.AURORA,
        gradientColors = listOf(Color(0xFFDCD4FF), Color(0xFFEAFBFF)),
        cardGradientColors = listOf(Color(0xFFE0D9FF), Color(0xFFD6F3FA), Color(0xFFF0EFFF)),
        designStyle = DesignStyle.LIQUID_GLASS,
        description = "光线折射 · 梦幻流光"
    )

    /** iOS 清新 —— iOS 风格（浅） */
    val iosFresh = ThemePreset(
        id = "ios_fresh",
        name = "iOS 清新",
        style = "ios",
        categoryName = "iOS风格",
        primaryColor = Color(0xFF007AFF),
        secondaryColor = Color(0xFF5AC8FA),
        bgColor = Color(0xFFF2F2F7),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1C1C1E),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFE9E9EE), Color(0xFFFFFFFF)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF8F8FA), Color(0xFFF2F2F7)),
        designStyle = DesignStyle.FLAT_DESIGN,
        description = "原生 iOS 质感 · 干净利落"
    )

    /** iOS 暗夜 —— iOS 风格（暗） */
    val iosDark = ThemePreset(
        id = "ios_dark",
        name = "iOS 暗夜",
        style = "ios",
        categoryName = "iOS风格",
        primaryColor = Color(0xFF0A84FF),
        secondaryColor = Color(0xFF5AC8FA),
        bgColor = Color(0xFF000000),
        surfaceColor = Color(0xFF1C1C1E),
        textColor = Color(0xFFF2F2F7),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFF1C1C1E), Color(0xFF000000)),
        cardGradientColors = listOf(Color(0xFF2C2C2E), Color(0xFF1C1C1E), Color(0xFF111111)),
        designStyle = DesignStyle.FLAT_DESIGN,
        description = "深邃暗黑 · 护眼省电"
    )

    // —— 兼容别名（供旧引用处过渡，实际指向新主题） ——
    val defaultTheme = clayOrange
    val classicTheme = iosFresh
    val shengshiTheme = clayOrange
    val shanshuiTheme = clayOrange

    // —— 分类列表（供主题切换弹窗） ——
    val clayThemes: List<ThemePreset> = listOf(clayOrange, clayChoco)
    val glassThemes: List<ThemePreset> = listOf(frostGlass, liquidGlass)
    val iosThemes: List<ThemePreset> = listOf(iosFresh, iosDark)

    // 兼容旧字段名（UiverseDialog 过渡用，实际不再使用）
    val darkThemes: List<ThemePreset> = listOf(clayChoco, iosDark)
    val neuThemes: List<ThemePreset> = emptyList()
    val designStyleThemes: List<ThemePreset> = emptyList()
    val gradientThemes: List<ThemePreset> = listOf(liquidGlass, frostGlass)

    val allThemes: List<ThemePreset> = listOf(
        clayOrange, clayChoco, frostGlass, liquidGlass, iosFresh, iosDark
    )

    fun findById(id: String): ThemePreset? = allThemes.find { it.id == id }
}
val LocalUiverseState = staticCompositionLocalOf { ActiveUiverseState() }

// ============================================================
// v1.2.2：主题 UI 色板（ThemeUiColors）
// 从当前 ThemePreset 派生一整组 UI 组件颜色，通过 CompositionLocal 全局提供。
// 关键组件（卡片/底栏/按钮/标题/图标）改为从此色板读色，切换主题时 UI 自动跟随变色。
// ============================================================
data class ThemeUiColors(
    val primary: Color = Color(0xFFE8590C),        // 橙子汽水
    val secondary: Color = Color(0xFFFF9A3D),
    val background: Color = Color(0xFFFFF3E6),
    val surface: Color = Color(0xFFFFFFFF),
    val text: Color = Color(0xFF4A2C12),           // 巧克力棕
    val textMuted: Color = Color(0xFF8A6A4A),
    val accent: Color = Color(0xFFE8590C),
    val border: Color = Color(0xFFE8590C).copy(alpha = 0.35f),
    // 卡片渐变（暖橙黏土）
    val cardGradient: List<Color> = listOf(Color(0xFFFFD8A8), Color(0xFFFFE8CC), Color(0xFFFFF3E6)),
    val designStyle: DesignStyle = DesignStyle.CLAYMORPHISM
)

val LocalThemeUiColors = staticCompositionLocalOf { ThemeUiColors() }

fun ThemePreset.toUiColors(): ThemeUiColors {
    val cardGrad = if (cardGradientColors.size >= 2) cardGradientColors
    else if (gradientColors.size >= 2) gradientColors
    else listOf(surfaceColor)
    return ThemeUiColors(
        primary = primaryColor,
        secondary = secondaryColor,
        background = bgColor,
        surface = surfaceColor,
        text = textColor,
        textMuted = textColor.copy(alpha = 0.62f),
        accent = primaryColor,
        border = if (cardGrad.size >= 2) primaryColor.copy(alpha = 0.55f) else primaryColor.copy(alpha = 0.35f),
        cardGradient = cardGrad,
        designStyle = this.designStyle
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
