package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 2026-10-24：用户要求删除全部 17 套主题，只保留一套极简默认（jicun 浅色色板）。
 * 主题切换功能已彻底移除，本文件仅保留极简默认主题定义。
 */
data class AppTheme(
    val id: String,
    val name: String,
    val isDark: Boolean = false,
    val colors: ThemeUiColors
)

/** 全局唯一主题：极简（jicun 浅色色板） */
val APP_THEMES: List<AppTheme> = listOf(
    AppTheme(
        id = "minimal",
        name = "极简",
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
    )
)

/** 默认主题：极简（唯一） */
const val DEFAULT_APP_THEME_ID = "minimal"

/** 按 id 取主题（现仅有一套，直接返回） */
fun appThemeById(id: String): AppTheme = APP_THEMES.first()
