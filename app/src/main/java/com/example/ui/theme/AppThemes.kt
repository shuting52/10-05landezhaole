package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 2026-10-24：用户要求删除全部 17 套主题，只保留一套极简默认。
 * 2026-10-24：按用户设计稿（s11-aero Frutiger Aero）改为千禧复兴风格。
 * 主题切换功能已彻底移除，本文件仅保留千禧复兴默认主题定义。
 */
data class AppTheme(
    val id: String,
    val name: String,
    val isDark: Boolean = false,
    val colors: ThemeUiColors
)

/** 全局唯一主题：千禧复兴 Frutiger Aero（用户设计稿 s11-aero） */
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
    )
)

/** 默认主题：千禧复兴（唯一） */
const val DEFAULT_APP_THEME_ID = "minimal"

/** 按 id 取主题（现仅有一套，直接返回） */
fun appThemeById(id: String): AppTheme = APP_THEMES.first()
