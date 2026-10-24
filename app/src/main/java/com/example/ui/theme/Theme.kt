package com.example.ui.theme

import android.os.Build
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.uiverse.ActiveUiverseState

/** 主题签名背景装饰 */
enum class BgDecor {
    NONE,   // 纯色/渐变，无装饰
    BLOBS,  // 柔和色块（冰晶/雾面/千禧）
    DOTS    // 圆点阵（星芒）
}

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
    CYBERPUNK("Cyberpunk 赛博朋克", "高饱和度霓虹与深邃暗夜科技感。"),
    JICUN("Jicun 即存", "即存 jicun 原生主题：半透明白卡片 + 20dp 超椭圆圆角，无描边无阴影。"),
    AERO("Aero 千禧复兴", "Frutiger Aero：蓝天白云、玻璃高光、水滴气泡的千禧年数字自然主义。"),
    LEATHER("Leather 皮纹鞣制", "深棕鞣制皮革：噪点皮纹 + 斜纹编织 + 虚线缝线，暖金点缀的复古手工质感。"),
    NEU("Neu 柔影", "NEU UI 柔影拟物：与背景同色的表面，靠左上白阴影 + 右下蓝阴影塑造体积，描边白 33%。"),
    GLOSS("Gloss 柔光", "GLOSS UI 柔光拟物：白到灰的高光渐变 + 1px 灰描边 + 文字 1px 白高光，按下向内压缩。"),
    CLAY("Clay 黏土", "CLAY UI 黏土立体：奶油粉/宝宝蓝/芝士黄，内高光 + 厚实彩色阴影，按下整体塌缩。"),
    LIQUID("Liquid Glass 液态玻璃", "液态玻璃交互预览（暗色流光）：深紫底 + 白 17%→5%→10% 玻璃渐变卡片 + 34dp 大圆角 + 1.2px 亮边 + 蓝紫徽标渐变。")
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
    // 2026-10-23：主色上的文字/图标色（深色主题用深色字保证对比度，如暗黑街头荧光绿底配黑字）
    val onPrimary: Color = Color.White,
    val border: Color = Color(0xFF00A8E8).copy(alpha = 0.35f),
    // 2026-10-23：15套主题签名级装饰（"原原本本"还原各原稿视觉签名）
    val hardShadowColor: Color? = null,  // 硬偏移阴影（新粗野/多巴胺/拨杆签名），null=不用
    val hardShadowDx: Dp = 0.dp,
    val hardShadowDy: Dp = 4.dp,
    val cardBorderWidth: Dp? = null,     // 卡片描边宽，null=按设计风格默认
    val bgDecor: BgDecor = BgDecor.NONE, // 背景签名装饰
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
// 2026-10-23：删除旧主题（用户要求）。jicun 浅色/深色主题与
// 跟随系统/浅色/深色三档切换已移除，由 14 套新主题（AppThemes.kt）取代。
// 主题 id 持久化在 lzdz_appearance 的 app_theme_id，非法 id 回退默认。
// ============================================================

@Composable
fun MyApplicationTheme(
    uiverseState: ActiveUiverseState = ActiveUiverseState(),
    appThemeId: String = DEFAULT_APP_THEME_ID,
    content: @Composable () -> Unit
) {
    val appTheme = appThemeById(appThemeId)
    val uiColors = appTheme.colors
    val isDark = appTheme.isDark
    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = uiColors.primary,
            secondary = uiColors.secondary,
            background = uiColors.background,
            surface = uiColors.surface,
            onPrimary = uiColors.onPrimary,
            onSecondary = uiColors.onPrimary,
            onBackground = uiColors.text,
            onSurface = uiColors.text
        )
    } else {
        lightColorScheme(
            primary = uiColors.primary,
            secondary = uiColors.secondary,
            background = uiColors.background,
            surface = uiColors.surface,
            onPrimary = uiColors.onPrimary,
            onSecondary = uiColors.onPrimary,
            onBackground = uiColors.text,
            onSurface = uiColors.text
        )
    }

    CompositionLocalProvider(
        LocalUiverseState provides uiverseState,
        LocalThemeUiColors provides uiColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
