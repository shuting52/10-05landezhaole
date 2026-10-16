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
    ACID_GRAPHICS("Acid Graphics 酸性设计", "灵感源自迷幻视觉，常使用高饱和度、循环的几何图形、液态金属感以及混乱的排版，视觉冲击力极强。"),
    HAND_DRAWN("Hand-Drawn 手绘风", "圆润抖动的线条、波浪线、小星星小花涂鸦，奶油黄底色，治愈系手绘质感。")
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
    // v1.5.5：主题系统按 shejishi 设计规范重写 —— 14 套风格
    // 规范来源：https://github.com/shuting52/shejishi.git
    // ============================================================

    /** 1. 黏土可爱 —— 奶油底 + 番茄红 + 明黄（默认主题） */
    val clayCute = ThemePreset(
        id = "sheji_clay_cute",
        name = "黏土可爱",
        style = "clay",
        categoryName = "shejishi",
        primaryColor = Color(0xFFFF4D2E),
        secondaryColor = Color(0xFFFFD400),
        bgColor = Color(0xFFFFF8EC),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF23232B),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFF8EC), Color(0xFFFFF0D6)),
        cardGradientColors = listOf(Color(0xFFFFF8EC), Color(0xFFFFEFD0)),
        designStyle = DesignStyle.CLAYMORPHISM,
        description = "奶油底 · 番茄红 · 明黄点缀 · 粗黑描边贴纸感"
    )

    /** 2. 国潮风 —— 宣纸底 + 朱红 + 黛青 + 烫金 */
    val guochao = ThemePreset(
        id = "sheji_guochao",
        name = "国潮风",
        style = "guochao",
        categoryName = "shejishi",
        primaryColor = Color(0xFFC8102E),
        secondaryColor = Color(0xFF2B4C4A),
        bgColor = Color(0xFFF5EFE0),
        surfaceColor = Color(0xFFFFFBF0),
        textColor = Color(0xFF1A1A1A),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFF5EFE0), Color(0xFFEDE3D0)),
        cardGradientColors = listOf(Color(0xFFF5EFE0), Color(0xFFF0E6D2), Color(0xFFC9A86A)),
        designStyle = DesignStyle.DEFAULT,
        description = "宣纸底 · 朱红 · 黛青 · 烫金细线"
    )

    /** 3. 暗黑霓虹 —— 纯黑底 + 荧光渐变 */
    val darkNeon = ThemePreset(
        id = "sheji_dark_neon",
        name = "暗黑霓虹",
        style = "neon",
        categoryName = "shejishi",
        primaryColor = Color(0xFF00F0FF),
        secondaryColor = Color(0xFFB537F2),
        bgColor = Color(0xFF000000),
        surfaceColor = Color(0xFF0A0A0A),
        textColor = Color(0xFFFFFFFF),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFF00F0FF), Color(0xFFB537F2), Color(0xFFFF2E9A)),
        cardGradientColors = listOf(Color(0xFF0A0A0A), Color(0xFF1A0A2E)),
        designStyle = DesignStyle.DEFAULT,
        description = "纯黑底 · 青紫粉荧光渐变 · 发光描边"
    )

    /** 4. 极简奶油 —— 米白底 + 墨 + 单点缀 */
    val minimalCream = ThemePreset(
        id = "sheji_minimal_cream",
        name = "极简奶油",
        style = "minimal",
        categoryName = "shejishi",
        primaryColor = Color(0xFF1A1A1A),
        secondaryColor = Color(0xFFE8B04B),
        bgColor = Color(0xFFFAF7F2),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1A1A1A),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFAF7F2), Color(0xFFF5F0E8)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFAF7F2)),
        designStyle = DesignStyle.MINIMALISM,
        description = "米白底 · 墨即主色 · 单点缀金 · 大量留白"
    )

    /** 5. 复古港风 —— 米黄底 + 港红港绿撞色 */
    val hkRetro = ThemePreset(
        id = "sheji_hk_retro",
        name = "复古港风",
        style = "retro",
        categoryName = "shejishi",
        primaryColor = Color(0xFFD93A2B),
        secondaryColor = Color(0xFF1F7A5C),
        bgColor = Color(0xFFF7EFD8),
        surfaceColor = Color(0xFFFFFBF0),
        textColor = Color(0xFF2B2118),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFF7EFD8), Color(0xFFF0E2C4)),
        cardGradientColors = listOf(Color(0xFFF7EFD8), Color(0xFFEFDDB8)),
        designStyle = DesignStyle.DEFAULT,
        description = "米黄底 · 港红港绿撞色 · 粗黑描边招牌字"
    )

    /** 6. 新拟态 —— 浅灰蓝底 + 珊瑚 */
    val neumorphism = ThemePreset(
        id = "sheji_neumorphism",
        name = "新拟态",
        style = "neu",
        categoryName = "shejishi",
        primaryColor = Color(0xFFFF6B6B),
        secondaryColor = Color(0xFF6D7B8D),
        bgColor = Color(0xFFE0E5EC),
        surfaceColor = Color(0xFFE0E5EC),
        textColor = Color(0xFF3D4A5C),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFE0E5EC), Color(0xFFD5DBE5)),
        cardGradientColors = listOf(Color(0xFFE0E5EC), Color(0xFFD8DEE8)),
        designStyle = DesignStyle.NEUMORPHISM,
        description = "浅灰蓝同色 · 珊瑚点缀 · 双向柔阴影"
    )

    /** 7. 手绘风 —— 奶油黄底 + 鸭黄 + 橙 */
    val handDrawn = ThemePreset(
        id = "sheji_hand_drawn",
        name = "手绘风",
        style = "handdrawn",
        categoryName = "shejishi",
        primaryColor = Color(0xFFFFC93C),
        secondaryColor = Color(0xFFFF9F43),
        bgColor = Color(0xFFFFF6E5),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF4A3B2A),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFF6E5), Color(0xFFFFEDC8)),
        cardGradientColors = listOf(Color(0xFFFFF6E5), Color(0xFFFFF0D0)),
        designStyle = DesignStyle.HAND_DRAWN,
        description = "奶油黄底 · 鸭黄 · 手绘线条 · ◆装饰符"
    )

    /** 8. Bento 格栅风 —— 浅灰底 + 品牌蓝 */
    val bentoGrid = ThemePreset(
        id = "sheji_bento",
        name = "Bento格栅",
        style = "bento",
        categoryName = "shejishi",
        primaryColor = Color(0xFF0071E3),
        secondaryColor = Color(0xFF0071E3),
        bgColor = Color(0xFFF5F5F7),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1D1D1F),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFF5F5F7), Color(0xFFEBEBED)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF5F5F7)),
        designStyle = DesignStyle.DEFAULT,
        description = "浅灰底 · 品牌蓝 · 严格网格 · 数据可视化"
    )

    /** 9. 液态玻璃 —— 自适应背景 + 系统蓝 */
    val liquidGlassNew = ThemePreset(
        id = "sheji_liquid_glass",
        name = "液态玻璃",
        style = "liquid",
        categoryName = "shejishi",
        primaryColor = Color(0xFF0A84FF),
        secondaryColor = Color(0xFF007AFF),
        bgColor = Color(0xFFFFFFFF),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF000000),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F4FF)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF5F8FF)),
        designStyle = DesignStyle.LIQUID_GLASS,
        description = "玻璃拟态 · 系统蓝 · 高光描边 · 悬浮胶囊"
    )

    /** 10. 新丑风 —— 奶油纸底 + 高饱和撞色 + 粗黑边 */
    val neoBrutalism = ThemePreset(
        id = "sheji_neo_brutalism",
        name = "新丑风",
        style = "brutalism",
        categoryName = "shejishi",
        primaryColor = Color(0xFFFF6B6B),
        secondaryColor = Color(0xFFFFDC58),
        bgColor = Color(0xFFFFF4E5),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF000000),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFF4E5), Color(0xFFFFECD0)),
        cardGradientColors = listOf(Color(0xFFFFF4E5), Color(0xFFFFE8C8), Color(0xFF7FBC8C)),
        designStyle = DesignStyle.NEO_BRUTALISM,
        description = "奶油纸底 · 高饱和撞色 · 3px黑边 · 硬阴影"
    )

    /** 11. 千禧复兴风 —— 蓝天渐变底 + 科技蓝 */
    val frutigerAero = ThemePreset(
        id = "sheji_frutiger_aero",
        name = "千禧复兴",
        style = "aero",
        categoryName = "shejishi",
        primaryColor = Color(0xFF00A8E8),
        secondaryColor = Color(0xFF7ED957),
        bgColor = Color(0xFFE8F9FF),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF0A2540),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFF7EC8F7), Color(0xFFE8F9FF)),
        cardGradientColors = listOf(Color(0xFFE8F9FF), Color(0xFFD0F0FF)),
        designStyle = DesignStyle.DEFAULT,
        description = "蓝天渐变 · 科技蓝 · 玻璃高光 · 气泡上浮"
    )

    /** 12. 多巴胺配色风 —— 白底 + 高饱和主色 */
    val dopamine = ThemePreset(
        id = "sheji_dopamine",
        name = "多巴胺",
        style = "dopamine",
        categoryName = "shejishi",
        primaryColor = Color(0xFF58CC02),
        secondaryColor = Color(0xFFA560FF),
        bgColor = Color(0xFFFFFFFF),
        surfaceColor = Color(0xFFFFF9E6),
        textColor = Color(0xFF3C3C3C),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFFF9E6)),
        cardGradientColors = listOf(Color(0xFF58CC02), Color(0xFFFF4B4B), Color(0xFFA560FF)),
        designStyle = DesignStyle.DEFAULT,
        description = "白底 · 多邻国绿 · 高饱和 · 弹性动效"
    )

    /** 13. 瑞士大字报 —— 纸白底 + 黑 + 报纸红 */
    val swissPoster = ThemePreset(
        id = "sheji_swiss_poster",
        name = "瑞士大字报",
        style = "swiss",
        categoryName = "shejishi",
        primaryColor = Color(0xFF111111),
        secondaryColor = Color(0xFFE30613),
        bgColor = Color(0xFFFAFAF8),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF111111),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFAFAF8), Color(0xFFF5F5F3)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFAFAF8)),
        designStyle = DesignStyle.MINIMALISM,
        description = "纸白底 · 黑即主色 · 报纸红强调 · 超大标题"
    )

    /** 14. 扁平插画风 —— 暖纸底 + 柔紫 */
    val flatIllustration = ThemePreset(
        id = "sheji_flat_illustration",
        name = "扁平插画",
        style = "flat",
        categoryName = "shejishi",
        primaryColor = Color(0xFF6C5CE7),
        secondaryColor = Color(0xFF00B894),
        bgColor = Color(0xFFF7F3EF),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF2D3436),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFE8F0FE), Color(0xFFF3E8FF)),
        cardGradientColors = listOf(Color(0xFFF7F3EF), Color(0xFFEFE8FF)),
        designStyle = DesignStyle.FLAT_DESIGN,
        description = "暖纸底 · 柔紫 · 大圆角 · 插画呼吸感"
    )

    // —— 默认主题 ——
    val defaultTheme = clayCute
    val classicTheme = clayCute

    // —— 分类列表（供主题切换弹窗） ——
    val shejishiThemes: List<ThemePreset> = listOf(
        clayCute, guochao, darkNeon, minimalCream, hkRetro, neumorphism, handDrawn,
        bentoGrid, liquidGlassNew, neoBrutalism, frutigerAero, dopamine, swissPoster, flatIllustration
    )

    // 兼容旧字段名
    val clayThemes: List<ThemePreset> = listOf(clayCute)
    val handDrawnThemes: List<ThemePreset> = listOf(handDrawn)
    val freshThemes: List<ThemePreset> = emptyList()
    val glassThemes: List<ThemePreset> = listOf(liquidGlassNew)
    val iosThemes: List<ThemePreset> = emptyList()
    val minimalThemes: List<ThemePreset> = listOf(minimalCream, swissPoster)
    val neuThemes: List<ThemePreset> = listOf(neumorphism)
    val darkThemes: List<ThemePreset> = listOf(darkNeon)
    val designStyleThemes: List<ThemePreset> = emptyList()
    val gradientThemes: List<ThemePreset> = listOf(liquidGlassNew, frutigerAero)

    val allThemes: List<ThemePreset> = shejishiThemes

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
    // v1.5.6：背景渐变（从 ThemePreset.gradientColors 派生，背景不再误用卡片渐变色）
    val backgroundGradient: List<Color> = emptyList(),
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
        backgroundGradient = gradientColors,
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
