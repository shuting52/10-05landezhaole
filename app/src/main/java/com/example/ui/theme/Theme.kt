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
    AURORA("Aurora 极光流体", "微信式液态玻璃：极光丝绸背景上的通透折射卡片。"),
    NEO_BRUTALISM("Neo-Brutalism 新粗野主义", "一种“反精致”的风格，使用粗重的边框、强烈的对比色和直接大胆的排版，极具视觉冲击力。"),
    MINIMALISM("Minimalism 极简主义", "“少即是多”。大量留白、无装饰的排版和有限的配色，只保留最核心的元素。"),
    BAUHAUS("Bauhaus 包豪斯风格", "强调形式服务于功能，喜欢使用基础的几何形状（圆形、方形、三角形）和红、黄、蓝三原色。"),
    RETROFUTURISM("Retrofuturism 复古未来主义", "它是“过去的未来”，常混合蒸汽波、赛博朋克（霓虹灯、故障艺术）等元素，用复古视角想象未来世界。"),
    BRUTALISM("Brutalism 粗野主义", "新粗野主义的前身，它更极致，完全放弃装饰、结构外露，用纯粹功能性的元素直接呈现，显得有些“原始”。"),
    ANTI_DESIGN("Anti-Design 反设计", "以一种混乱、不和谐、朋克风的方式，挑战传统的美学和可用性规则。"),
    ACID_GRAPHICS("Acid Graphics 酸性设计", "灵感源自迷幻视觉，常使用高饱和度、循环的几何图形、液态金属感以及混乱的排版，视觉冲击力极强。"),
    HAND_DRAWN("Hand-Drawn 手绘风", "圆润抖动的线条、波浪线、小星星小花涂鸦，奶油黄底色，治愈系手绘质感。"),
    CARTOON("Cartoon 卡通", "更新弹窗同款：28.dp 大圆角 + 3.dp 深可可描边 + 粉色硬阴影，Q版卡通质感。"),
    FELT_CRAFT("Felt Craft 毛毡风", "温暖质朴的羊毛毡手作质感，缝线虚线边缘，温润燕麦羊毛色系。"),
    GUOCHAO("Guochao 国潮风", "经典中国传统色调与东方典雅韵味。"),
    CYBERPUNK("Cyberpunk 赛博朋克", "高饱和度霓虹与深邃暗夜科技感。")
}

val LocalUiverseState = staticCompositionLocalOf { ActiveUiverseState() }

// ============================================================
// v1.2.2：主题 UI 色板（ThemeUiColors）
// 固定 UI 色板（v1.7.17 主题预设已删除），通过 CompositionLocal 全局提供。
// 关键组件（卡片/底栏/按钮/标题/图标）改为从此色板读色，切换主题时 UI 自动跟随变色。
// ============================================================
data class ThemeUiColors(
    // v1.7.19：Frutiger Aero 千禧复兴皮肤（fa-blue/fa-sky/fa-ink）
    val primary: Color = Color(0xFF00A8E8),        // 天空蓝
    val secondary: Color = Color(0xFF0090C9),      // 深海蓝
    val background: Color = Color(0xFFE8F9FF),     // 最浅天空
    val surface: Color = Color(0xFFFFFFFF),
    val text: Color = Color(0xFF0A2540),           // 墨蓝
    val textMuted: Color = Color(0xFF5B7A90),      // 浅灰蓝
    val accent: Color = Color(0xFF00A8E8),
    val border: Color = Color(0xFF00A8E8).copy(alpha = 0.35f),
    // 卡片渐变（玻璃白→浅天空，高光感）
    val cardGradient: List<Color> = listOf(Color(0xFFFFFFFF), Color(0xFFE8F9FF)),
    // 背景渐变（Frutiger Aero 天空：蓝→浅蓝→极浅）
    val backgroundGradient: List<Color> = listOf(
        Color(0xFF7EC8F7),
        Color(0xFFBDE4FB),
        Color(0xFFDFF3FE),
        Color(0xFFE8F9FF)
    ),
    val designStyle: DesignStyle = DesignStyle.SKEUOMORPHISM
)

val LocalThemeUiColors = staticCompositionLocalOf { ThemeUiColors() }

// ============================================================
// v1.7.27：App 主题（LEVER UI 拨杆玩具风加入）
// ============================================================
enum class AppTheme(val displayName: String, val desc: String) {
    AERO("Liquid Glass", "液态玻璃 · 通透折射与厚度高光"),
    LEVER("LEVER UI", "拨杆玩具风 · 硬阴影按压回弹"),
    AURORA("极光流体", "微信式液态玻璃 · 极光丝绸折射")
}

/** LEVER UI 色板（源自用户提供的 LEVER UI 拨杆组件库 HTML） */
fun leverUiColors() = ThemeUiColors(
    primary = Color(0xFF1269EF),        // 拨杆蓝
    secondary = Color(0xFF0D4FC0),      // 拨杆深蓝
    background = Color(0xFFDCEBF9),     // 纸蓝
    surface = Color(0xFFFFFFFF),
    text = Color(0xFF085991),           // 墨水蓝
    textMuted = Color(0xFF3D7CA8),
    accent = Color(0xFFED487E),         // 拨杆粉
    border = Color(0xFF1A4766).copy(alpha = 0.35f),  // 深海
    cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFB7DFFB)),
    backgroundGradient = listOf(
        Color(0xFFB7DFFB),
        Color(0xFFDCEBF9),
        Color(0xFFDCEBF9)
    ),
    designStyle = DesignStyle.CLAYMORPHISM  // 玩具质感最接近粘土拟态
)

/** Liquid Glass：深海靛蓝底色，珊瑚、冰青与暖金形成克制的彩色折射。
 *  2026-10-22 按用户四步法重写：背景只用靛蓝→紫两种主色柔和渐变，
 *  卡片玻璃层由 Modifier.liquidGlass() 按“模糊增色/叠白/厚度/折射色散”四步绘制。 */
fun aeroUiColors() = ThemeUiColors(
    primary = Color(0xFFFF7A93),        // 折射珊瑚
    secondary = Color(0xFF7DE8E0),      // 折射冰青
    background = Color(0xFF0C1122),     // 深海靛蓝
    surface = Color(0xFF151C33),
    text = Color(0xFFF5F7FC),
    textMuted = Color(0xFFA6AEC7),
    accent = Color(0xFFFFC37E),         // 折射暖金
    border = Color.White.copy(alpha = 0.35f),
    // 卡片兜底渐变（玻璃主体由 liquidGlass 绘制，此处仅作非玻璃场景回退）
    cardGradient = listOf(Color.White.copy(alpha = 0.16f), Color.White.copy(alpha = 0.07f)),
    // 背景渐变：靛蓝 → 紫，两种主色，大块柔和
    backgroundGradient = listOf(
        Color(0xFF111832),
        Color(0xFF1D1A3E)
    ),
    designStyle = DesignStyle.LIQUID_GLASS
)

/** 极光流体：曜石黑底上的橙/青/紫极光丝绸，卡片为通透折射玻璃（对标用户微信主题截图）。 */
fun auroraUiColors() = ThemeUiColors(
    primary = Color(0xFFFF6B4A),        // 极光橙
    secondary = Color(0xFF2DD4BF),      // 极光青
    background = Color(0xFF0A0A12),     // 曜石黑
    surface = Color(0xFF14141F),
    text = Color(0xFFFFFFFF),
    textMuted = Color(0xFFB9B9CC),
    accent = Color(0xFFA78BFA),         // 极光紫
    border = Color.White.copy(alpha = 0.35f),
    cardGradient = listOf(Color.White.copy(alpha = 0.16f), Color.White.copy(alpha = 0.07f)),
    backgroundGradient = listOf(
        Color(0xFF0D0D18),
        Color(0xFF160F2A)
    ),
    designStyle = DesignStyle.AURORA
)

val LocalAppTheme = staticCompositionLocalOf { AppTheme.AERO }

@Composable
fun MyApplicationTheme(
    uiverseState: ActiveUiverseState = ActiveUiverseState(),
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    appTheme: AppTheme = AppTheme.AERO,
    content: @Composable () -> Unit
) {
    val uiColors = when (appTheme) {
        AppTheme.AERO -> aeroUiColors()
        AppTheme.LEVER -> leverUiColors()
        AppTheme.AURORA -> auroraUiColors()
    }
    val colorScheme = when (appTheme) {
        AppTheme.AERO, AppTheme.AURORA -> darkColorScheme(
            primary = uiColors.primary,
            onPrimary = Color(0xFF2B1119),
            primaryContainer = Color(0xFF633346),
            onPrimaryContainer = Color(0xFFFFD9E0),
            secondary = uiColors.secondary,
            onSecondary = Color(0xFF0B2527),
            secondaryContainer = Color(0xFF21464A),
            onSecondaryContainer = Color(0xFFB5F5EF),
            tertiary = uiColors.accent,
            onTertiary = Color(0xFF2A1B0B),
            background = uiColors.background,
            onBackground = uiColors.text,
            surface = uiColors.surface,
            onSurface = uiColors.text,
            surfaceVariant = Color(0xFF252B3C),
            onSurfaceVariant = uiColors.textMuted,
            outline = uiColors.border,
            outlineVariant = Color.White.copy(alpha = 0.16f),
            error = Color(0xFFFF8A8A),
            onError = Color(0xFF3A0909)
        )
        AppTheme.LEVER -> lightColorScheme(
            primary = uiColors.primary,
            secondary = uiColors.secondary,
            background = uiColors.background,
            surface = uiColors.surface,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = uiColors.text,
            onSurface = uiColors.text
        )
    }

    CompositionLocalProvider(
        LocalUiverseState provides uiverseState,
        LocalThemeUiColors provides uiColors,
        LocalAppTheme provides appTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
