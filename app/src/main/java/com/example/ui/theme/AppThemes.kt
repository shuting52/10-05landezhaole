package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 2026-10-24：用户要求删除全部 17 套主题，只保留一套极简默认。
 * 2026-10-24：按用户设计稿（s11-aero Frutiger Aero）改为千禧复兴风格。
 * 2026-10-24：用户新设计稿（皮纹鞣制 g95）加入第二套主题，恢复主题切换。
 */
data class AppTheme(
    val id: String,
    val name: String,
    val isDark: Boolean = false,
    val colors: ThemeUiColors
)

/** 全局主题：千禧复兴 Frutiger Aero + 皮纹鞣制 + 柔影 / 柔光 / 黏土（2026-10-24 用户新设计稿） */
val APP_THEMES: List<AppTheme> = listOf(
    AppTheme(
        id = "minimal",
        name = "千禧复兴",
        colors = ThemeUiColors(
            primary = Color(0xFF00A8E8),          // fa-blue 天空蓝
            secondary = Color(0xFF0090C9),        // fa-blue-deep 深海蓝
            background = Color(0xFFE8F9FF),       // 最浅天空
            surface = Color(0x8CFFFFFF),          // 白 55% 玻璃卡片
            text = Color(0xFF0A2540),             // fa-ink 墨蓝
            textMuted = Color(0xFF5B7A90),        // 浅灰蓝
            accent = Color(0xFF00A8E8),
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFF00A8E8).copy(alpha = 0.35f),
            // 卡片：白 75% → 白 55% 玻璃渐变（fa-card）
            cardGradient = listOf(Color(0xBFFFFFFF), Color(0x8CFFFFFF)),
            // 背景：Frutiger Aero 天空（fa-phone）
            backgroundGradient = listOf(
                Color(0xFF7EC8F7),
                Color(0xFFBDE4FB),
                Color(0xFFDFF3FE),
                Color(0xFFE8F9FF)
            ),
            designStyle = DesignStyle.AERO
        )
    ),
    AppTheme(
        id = "leather",
        name = "皮纹鞣制",
        isDark = true,
        colors = ThemeUiColors(
            primary = Color(0xFFD8B98A),          // 鞣制金棕
            secondary = Color(0xFFA08776),        // 哑光皮棕
            background = Color(0xFF14100C),       // 深皮革底
            surface = Color(0x14FFFFFF),          // 白 8% 皮革卡片
            text = Color(0xFFF0D9B8),             // 奶油皮革字
            textMuted = Color(0xFFA08776),        // 哑光皮棕（未选中）
            accent = Color(0xFFF0D9B8),
            onPrimary = Color(0xFF2A2018),        // 深棕字（金棕底上）
            border = Color(0xFFDCB482).copy(alpha = 0.45f),  // 缝线金棕虚线
            // 卡片：深皮革半透明（设计稿 .cardx）
            cardGradient = listOf(Color(0x0DFFFFFF), Color(0x08FFFFFF)),
            // 背景：深棕皮革（设计稿 --bg #14100c + 顶栏暖棕晕）
            backgroundGradient = listOf(
                Color(0xFF3A2C20),
                Color(0xFF2A2018),
                Color(0xFF1E1812),
                Color(0xFF14100C)
            ),
            designStyle = DesignStyle.LEATHER
        )
    ),
    AppTheme(
        id = "neu",
        name = "柔影",
        colors = ThemeUiColors(
            primary = Color(0xFF5F9DE7),          // 柔影蓝
            secondary = Color(0xFF3F7FC4),        // 深柔影蓝
            background = Color(0xFFE0E8EF),       // 雾蓝灰
            surface = Color(0xFFE0E8EF),          // 与背景同色（拟物靠阴影塑形）
            text = Color(0xFF516D91),             // 深灰蓝
            textMuted = Color(0xFF7E97B8),        // 文字灰蓝
            accent = Color(0xFFF0A93C),           // 点缀金
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFFFFFFFF).copy(alpha = 0.333f),  // 描边白 33%
            cardGradient = listOf(Color(0xFFE0E8EF), Color(0xFFE5EDF5)),
            backgroundGradient = listOf(
                Color(0xFFC3CFDB),
                Color(0xFFDDE6EE),
                Color(0xFFE0E8EF)
            ),
            designStyle = DesignStyle.NEU
        )
    ),
    AppTheme(
        id = "gloss",
        name = "柔光",
        colors = ThemeUiColors(
            primary = Color(0xFF007AFF),          // 品牌蓝
            secondary = Color(0xFF0062CC),        // 深蓝
            background = Color(0xFFE9EAEC),       // 背景灰
            surface = Color(0xFFFFFFFF),          // 表面白
            text = Color(0xFF3C3C3C),             // 文字灰
            textMuted = Color(0xFF8E8E93),        // 系统灰
            accent = Color(0xFFF5A623),           // 点缀金
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFFC6C7C9),           // 描边灰
            cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFF6F7F8)),
            backgroundGradient = listOf(
                Color(0xFFE9EAEC),
                Color(0xFFEDEEF0),
                Color(0xFFF6F7F8)
            ),
            designStyle = DesignStyle.GLOSS
        )
    ),
    AppTheme(
        id = "clay",
        name = "黏土",
        colors = ThemeUiColors(
            primary = Color(0xFFFF9EBB),          // 奶油粉
            secondary = Color(0xFF8EC9F5),        // 宝宝蓝
            background = Color(0xFFEFE7F5),       // 淡紫底
            surface = Color(0xFFF7F2FB),          // 奶白面板
            text = Color(0xFF4A3F5C),             // 深紫灰
            textMuted = Color(0xFF9A8FB0),        // 淡紫灰
            accent = Color(0xFFFFD166),           // 芝士黄
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFFFFFFFF).copy(alpha = 0.6f),
            cardGradient = listOf(Color(0xFFFFFFFF), Color(0xFFF4EEFB)),
            backgroundGradient = listOf(
                Color(0xFFD9CFE8),
                Color(0xFFEFE7F5),
                Color(0xFFF7F2FB)
            ),
            designStyle = DesignStyle.CLAY
        )
    )
)

/** 默认主题：千禧复兴 */
const val DEFAULT_APP_THEME_ID = "minimal"

/** 按 id 取主题，非法 id 回退默认 */
fun appThemeById(id: String): AppTheme = APP_THEMES.firstOrNull { it.id == id } ?: APP_THEMES.first()
