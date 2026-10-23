package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** 主题收纳分类（主题外观弹窗按此分组展示） */
enum class ThemeCategory(val title: String) {
    GLASS("玻璃拟态"),
    NEU("新拟态"),
    HAND("手绘涂鸦"),
    RETRO("复古纸感"),
    DARK("暗黑霓虹"),
    CHUNKY("立体Q弹"),
    MINIMAL("极简扁平")
}

/**
 * 2026-10-23：14 套新主题（用户逐套提供设计稿，要求写进「主题外观」切换器，
 * 点击即全局应用，并删除之前的 jicun 旧主题）。
 *
 * 每套主题 = 完整 ThemeUiColors 色板 + 卡片设计风格映射。
 * 全软件所有组件（卡片/底栏/背景/按钮/文字）均从 LocalThemeUiColors 读色，
 * 因此切换主题 id 后整个 App 自动跟随变色，无需逐组件改动。
 */
data class AppTheme(
    val id: String,
    /** 英文标签，如 FROST UI */
    val tag: String,
    /** 中文名，如 冰晶液态 */
    val name: String,
    /** 一句话描述 */
    val desc: String,
    /** 深色主题（决定 Material colorScheme 用深色还是浅色） */
    val isDark: Boolean = false,
    /** 收纳分类 */
    val category: ThemeCategory,
    val colors: ThemeUiColors
)

private fun translucentWhite(alpha: Float) = Color(0xFFFFFFFF).copy(alpha = alpha)

/** 14 套主题，按用户提供顺序排列 */
val APP_THEMES: List<AppTheme> = listOf(
    // 1. FROST UI · 冰晶液态：冰蓝 + 霜纹玻璃 + 冷冽通透
    AppTheme(
        id = "frost",
        tag = "FROST UI",
        name = "冰晶液态",
        desc = "冰蓝霜纹玻璃，冷冽通透",
        category = ThemeCategory.GLASS,
        colors = ThemeUiColors(
            bgDecor = BgDecor.BLOBS,
            primary = Color(0xFF4FB2EC),
            secondary = Color(0xFF2B8FD4),
            background = Color(0xFFDCEBF9),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF16324C),
            textMuted = Color(0xFF5F7F9C),
            accent = Color(0xFF4FB2EC),
            border = Color(0xFFFFFFFF).copy(alpha = 0.8f),
            cardGradient = listOf(translucentWhite(0.58f), translucentWhite(0.38f)),
            backgroundGradient = listOf(
                Color(0xFFCFE3F5),
                Color(0xFFA9C8E8),
                Color(0xFF8FB6DD)
            ),
            designStyle = DesignStyle.GLASSMORPHISM
        )
    ),
    // 2. VOUCHER UI · 复古票据：牛皮纸 + 粗描边 + 硬阴影
    AppTheme(
        id = "voucher",
        tag = "VOUCHER UI",
        name = "复古票据",
        desc = "牛皮纸票据风，粗描边硬阴影",
        category = ThemeCategory.RETRO,
        colors = ThemeUiColors(
            hardShadowColor = Color(0xFF4A3525), hardShadowDx = 3.dp, hardShadowDy = 3.dp,
            primary = Color(0xFFD93A2B),
            secondary = Color(0xFF1F7A5C),
            background = Color(0xFFF3E9D2),
            surface = Color(0xFFFBF6E9),
            text = Color(0xFF4A3525),
            textMuted = Color(0xFF6B5340),
            accent = Color(0xFFD93A2B),
            border = Color(0xFF4A3525),
            cardGradient = listOf(Color(0xFFFBF6E9), Color(0xFFF3E9D2)),
            backgroundGradient = listOf(Color(0xFFF3E9D2), Color(0xFFFBF6E9)),
            designStyle = DesignStyle.NEO_BRUTALISM
        )
    ),
    // 3. STREET UI · 暗黑街头：暗黑底 + 荧光绿霓虹（深色）
    AppTheme(
        id = "street",
        tag = "STREET UI",
        name = "暗黑街头",
        desc = "暗黑底荧光绿，深色模式",
        category = ThemeCategory.DARK,
        isDark = true,
        colors = ThemeUiColors(
            primary = Color(0xFFC6FF4A),
            secondary = Color(0xFFFF5A1F),
            background = Color(0xFF0B0B0C),
            surface = Color(0xFF141416),
            text = Color(0xFFFFFFFF),
            textMuted = Color(0xFF9A9AA0),
            accent = Color(0xFFC6FF4A),
            onPrimary = Color(0xFF0B0B0C),
            border = Color(0xFFC6FF4A),
            cardGradient = listOf(Color(0xFF141416), Color(0xFF1B1B1E)),
            backgroundGradient = listOf(Color(0xFF0B0B0C), Color(0xFF141416)),
            designStyle = DesignStyle.NEO_BRUTALISM
        )
    ),
    // 4. Dopamine UI · 多巴胺：高饱和撞色，Q弹立体
    AppTheme(
        id = "dopamine",
        tag = "Dopamine UI",
        name = "多巴胺",
        desc = "高饱和撞色，Q弹立体",
        category = ThemeCategory.CHUNKY,
        colors = ThemeUiColors(
            cardBorderWidth = 3.dp, hardShadowColor = Color(0xFF58A700), hardShadowDy = 4.dp,
            primary = Color(0xFF58CC02),
            secondary = Color(0xFF1CB0F6),
            background = Color(0xFFFFF9E6),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF3C3C3C),
            textMuted = Color(0xFF8A8A8A),
            accent = Color(0xFF58CC02),
            border = Color(0xFF3C3C3C),
            cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFFFF9E6)),
            backgroundGradient = listOf(Color(0xFFFFFFFF), Color(0xFFFFF9E6)),
            designStyle = DesignStyle.CLAYMORPHISM
        )
    ),
    // 5. AERO UI · Frutiger Aero 千禧复兴：天空光泽玻璃
    AppTheme(
        id = "aero",
        tag = "AERO UI",
        name = "千禧复兴",
        desc = "Frutiger Aero 天空光泽玻璃",
        category = ThemeCategory.GLASS,
        colors = ThemeUiColors(
            bgDecor = BgDecor.BLOBS,
            primary = Color(0xFF00A8E8),
            secondary = Color(0xFF0090C9),
            background = Color(0xFFBDE4FB),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF0A2540),
            textMuted = Color(0xFF5B7A90),
            accent = Color(0xFF00A8E8),
            border = Color(0xFFFFFFFF).copy(alpha = 0.65f),
            cardGradient = listOf(translucentWhite(0.55f), translucentWhite(0.35f)),
            backgroundGradient = listOf(
                Color(0xFF7EC8F7),
                Color(0xFFBDE4FB),
                Color(0xFFE8F9FF)
            ),
            designStyle = DesignStyle.AURORA
        )
    ),
    // 6. NEU UI · 新拟态：同色浮雕柔阴影
    AppTheme(
        id = "neu",
        tag = "NEU UI",
        name = "新拟态",
        desc = "同色浮雕，柔和立体阴影",
        category = ThemeCategory.NEU,
        colors = ThemeUiColors(
            primary = Color(0xFFFF6B6B),
            secondary = Color(0xFFE85A5A),
            background = Color(0xFFE0E5EC),
            surface = Color(0xFFE0E5EC),
            text = Color(0xFF3D4A5C),
            textMuted = Color(0xFF6D7B8D),
            accent = Color(0xFFFF6B6B),
            border = Color(0xFFCBD5E1),
            cardGradient = listOf(Color(0xFFE0E5EC), Color(0xFFE0E5EC)),
            backgroundGradient = listOf(Color(0xFFE0E5EC), Color(0xFFD2DCE5)),
            designStyle = DesignStyle.NEUMORPHISM
        )
    ),
    // 7. 手绘风 Doodle：奶油黄 + 抖动描边，治愈系
    AppTheme(
        id = "doodle",
        tag = "DOODLE",
        name = "手绘风",
        desc = "奶油黄手绘，治愈系涂鸦",
        category = ThemeCategory.HAND,
        colors = ThemeUiColors(
            primary = Color(0xFFFF9F43),
            secondary = Color(0xFFFFC93C),
            background = Color(0xFFFFF6E5),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF4A3B2A),
            textMuted = Color(0xFFA08B6D),
            accent = Color(0xFFFF9F43),
            border = Color(0xFF4A3B2A),
            cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFFFF6E5)),
            backgroundGradient = listOf(Color(0xFFFFF6E5), Color(0xFFFBEED3)),
            designStyle = DesignStyle.HAND_DRAWN
        )
    ),
    // 8. 国潮风 Chinese Chic：朱砂 + 黛青，东方典雅
    AppTheme(
        id = "guochao",
        tag = "CHINESE CHIC",
        name = "国潮风",
        desc = "朱砂黛青，东方典雅",
        category = ThemeCategory.RETRO,
        colors = ThemeUiColors(
            primary = Color(0xFFC8102E),
            secondary = Color(0xFF2B4C4A),
            background = Color(0xFFF5EFE0),
            surface = Color(0xFFFBF8EE),
            text = Color(0xFF1A1A1A),
            textMuted = Color(0xFF8A8272),
            accent = Color(0xFFC8102E),
            border = Color(0xFFC9A86A),
            cardGradient = listOf(Color(0xFFFBF8EE), Color(0xFFF5EFE0)),
            backgroundGradient = listOf(Color(0xFFF5EFE0), Color(0xFFECE2CC)),
            designStyle = DesignStyle.GUOCHAO
        )
    ),
    // 9. LEVER UI · 拨杆：玩具质感拨杆按钮
    AppTheme(
        id = "lever",
        tag = "LEVER UI",
        name = "拨杆",
        desc = "玩具质感，拨杆按钮",
        category = ThemeCategory.CHUNKY,
        colors = ThemeUiColors(
            hardShadowColor = Color(0xFF3083BB), hardShadowDy = 4.dp,
            primary = Color(0xFF1269EF),
            secondary = Color(0xFFED487E),
            background = Color(0xFFDCEBF9),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF085991),
            textMuted = Color(0xFF1A4766),
            accent = Color(0xFF1269EF),
            border = Color(0xFFFFFFFF).copy(alpha = 0.8f),
            cardGradient = listOf(Color(0xFFB7DFFB), Color(0xFFA8D4F5)),
            backgroundGradient = listOf(Color(0xFFDCEBF9), Color(0xFFC2DDF4)),
            designStyle = DesignStyle.CLAYMORPHISM
        )
    ),
    // 10. DOODLE UI · 手绘涂鸦：蜡笔抖线 + 歪扭边框
    AppTheme(
        id = "doodle2",
        tag = "DOODLE UI",
        name = "手绘涂鸦",
        desc = "蜡笔抖线，歪扭边框",
        category = ThemeCategory.HAND,
        colors = ThemeUiColors(
            primary = Color(0xFFE2605C),
            secondary = Color(0xFF5B8DD9),
            background = Color(0xFFF6F1E5),
            surface = Color(0xFFFFFDF7),
            text = Color(0xFF444433),
            textMuted = Color(0xFF8A8471),
            accent = Color(0xFFE2605C),
            border = Color(0xFF444433),
            cardGradient = listOf(Color(0xFFFFFDF7), Color(0xFFF6F1E5)),
            backgroundGradient = listOf(Color(0xFFF6F1E5), Color(0xFFEDE4CF)),
            designStyle = DesignStyle.HAND_DRAWN
        )
    ),
    // 11. NEU UI · 柔影：浅蓝灰柔光拟态
    AppTheme(
        id = "neusoft",
        tag = "NEU 柔影",
        name = "柔影",
        desc = "浅蓝灰柔光，优雅拟态",
        category = ThemeCategory.NEU,
        colors = ThemeUiColors(
            primary = Color(0xFF5F9DE7),
            secondary = Color(0xFF3F7FC4),
            background = Color(0xFFE0E8EF),
            surface = Color(0xFFE0E8EF),
            text = Color(0xFF516D91),
            textMuted = Color(0xFF7E97B8),
            accent = Color(0xFF5F9DE7),
            border = Color(0xFFFFFFFF).copy(alpha = 0.5f),
            cardGradient = listOf(Color(0xFFE0E8EF), Color(0xFFE0E8EF)),
            backgroundGradient = listOf(Color(0xFFE0E8EF), Color(0xFFD2DEE9)),
            designStyle = DesignStyle.NEUMORPHISM
        )
    ),
    // 12. GLASS UI · 雾面：雾面毛玻璃 + 彩色光斑
    AppTheme(
        id = "glass",
        tag = "GLASS UI",
        name = "雾面",
        desc = "雾面毛玻璃，彩色光斑",
        category = ThemeCategory.GLASS,
        colors = ThemeUiColors(
            bgDecor = BgDecor.BLOBS,
            primary = Color(0xFF2879F3),
            secondary = Color(0xFFF37E10),
            background = Color(0xFFEDF1F6),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF22303F),
            textMuted = Color(0xFF64748B),
            accent = Color(0xFF2879F3),
            border = Color(0xFFFFFFFF).copy(alpha = 0.55f),
            cardGradient = listOf(translucentWhite(0.32f), translucentWhite(0.45f)),
            backgroundGradient = listOf(
                Color(0xFFD3E3F8),
                Color(0xFFEDF1F6),
                Color(0xFFF8E9D4)
            ),
            designStyle = DesignStyle.GLASSMORPHISM
        )
    ),
    // 13. GLOSS UI · 柔光拟物：柔和高光拟物
    AppTheme(
        id = "gloss",
        tag = "GLOSS UI",
        name = "柔光拟物",
        desc = "柔和高光，经典拟物",
        category = ThemeCategory.CHUNKY,
        colors = ThemeUiColors(
            primary = Color(0xFF007AFF),
            secondary = Color(0xFF0062CC),
            background = Color(0xFFE9EAEC),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF3C3C3C),
            textMuted = Color(0xFF8E8E93),
            accent = Color(0xFF007AFF),
            border = Color(0xFF8F9092),
            cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFF2F2F4)),
            backgroundGradient = listOf(Color(0xFFE9EAEC), Color(0xFFDCDDE0)),
            designStyle = DesignStyle.SKEUOMORPHISM
        )
    ),
    // 14. STAR UI · 星芒：暗黑 + 橙绿撞色（深色）
    AppTheme(
        id = "star",
        tag = "STAR UI",
        name = "星芒",
        desc = "暗黑橙绿撞色，深色模式",
        category = ThemeCategory.DARK,
        isDark = true,
        colors = ThemeUiColors(
            hardShadowColor = Color(0xFFFF9100), hardShadowDx = 4.dp, hardShadowDy = 4.dp, bgDecor = BgDecor.DOTS,
            primary = Color(0xFFFF9100),
            secondary = Color(0xFF91FF00),
            background = Color(0xFF151210),
            surface = Color(0xFF221D18),
            text = Color(0xFFFFF6EA),
            textMuted = Color(0xFFB3A794),
            accent = Color(0xFFFF9100),
            onPrimary = Color(0xFF1A1206),
            border = Color(0xFF000000),
            cardGradient = listOf(Color(0xFF221D18), Color(0xFF2C251E)),
            backgroundGradient = listOf(Color(0xFF151210), Color(0xFF1F1812)),
            designStyle = DesignStyle.NEO_BRUTALISM
        )
    ),
    // 15. SOFT SWITCH UI · 柔光开关：浅色轻拟态，描边圆环开关
    AppTheme(
        id = "softswitch",
        tag = "SOFT SWITCH UI",
        name = "柔光开关",
        desc = "浅色轻拟态，柔光描边",
        category = ThemeCategory.NEU,
        colors = ThemeUiColors(
            primary = Color(0xFF8BA9D4),
            secondary = Color(0xFFA9C4E8),
            background = Color(0xFFF2F6FB),
            surface = Color(0xFFFFFFFF),
            text = Color(0xFF3D4B5E),
            textMuted = Color(0xFF8B98AB),
            accent = Color(0xFF8BA9D4),
            border = Color(0xFFE9EFF6),
            cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFFFFFFF)),
            backgroundGradient = listOf(Color(0xFFF2F6FB), Color(0xFFE9F0F8)),
            designStyle = DesignStyle.NEUMORPHISM
        )
    ),
    // 16. JICUN · 即存浅色：jicun 原生浅色主题（Palette.light 原样搬运）
    // 页底 #CDDCDC / 强调 #1257C9 / 卡片白 55% 无描边 20dp 超椭圆
    AppTheme(
        id = "jicun",
        tag = "JICUN",
        name = "即存·浅色",
        desc = "jicun 原生浅色，青灰底+半透明白卡",
        category = ThemeCategory.MINIMAL,
        colors = ThemeUiColors(
            primary = Color(0xFF1257C9),
            secondary = Color(0xFF1257C9),
            background = Color(0xFFCDDCDC),
            surface = Color(0x8CFFFFFF),
            text = Color(0xFF1B2430),
            textMuted = Color(0xFF6E7887),
            accent = Color(0xFF1257C9),
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0x00000000),
            cardGradient = listOf(Color(0x8CFFFFFF), Color(0x8CFFFFFF)),
            backgroundGradient = listOf(Color(0xFFCDDCDC), Color(0xFFCDDCDC), Color(0xFFCDDCDC)),
            designStyle = DesignStyle.JICUN
        )
    ),
    // 17. JICUN DARK · 即存深色：jicun 原生深色主题（Palette.dark 原样搬运）
    // 页底黑→#434343 渐变 / 强调 #5AA9FF / 卡片白 15% 无描边 20dp
    AppTheme(
        id = "jicun_dark",
        tag = "JICUN DARK",
        name = "即存·深色",
        desc = "jicun 原生深色，黑灰渐变底+淡白卡",
        category = ThemeCategory.MINIMAL,
        isDark = true,
        colors = ThemeUiColors(
            primary = Color(0xFF5AA9FF),
            secondary = Color(0xFF5AA9FF),
            background = Color(0xFF434343),
            surface = Color(0x26FFFFFF),
            text = Color(0xFFF5F7FA),
            textMuted = Color(0xFFADB7C5),
            accent = Color(0xFF5AA9FF),
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0x00000000),
            cardGradient = listOf(Color(0x26FFFFFF), Color(0x26FFFFFF)),
            backgroundGradient = listOf(Color(0xFF000000), Color(0xFF434343), Color(0xFF434343)),
            designStyle = DesignStyle.JICUN
        )
    )
)

/** 默认主题：即存·浅色（jicun 原生主题，用户指定为默认） */
const val DEFAULT_APP_THEME_ID = "jicun"

/** 按 id 取主题，非法 id 回退默认 */
fun appThemeById(id: String): AppTheme =
    APP_THEMES.firstOrNull { it.id == id } ?: APP_THEMES.first { it.id == DEFAULT_APP_THEME_ID }
