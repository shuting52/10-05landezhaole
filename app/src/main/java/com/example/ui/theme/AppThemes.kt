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

/** 全局主题：千禧复兴 Frutiger Aero + 皮纹鞣制 */
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
    )
)

/** 默认主题：千禧复兴 */
const val DEFAULT_APP_THEME_ID = "minimal"

/** 按 id 取主题，非法 id 回退默认 */
fun appThemeById(id: String): AppTheme = APP_THEMES.firstOrNull { it.id == id } ?: APP_THEMES.first()
