package com.example.model

/**
 * 反编译重建 · 枚举
 * title / route：UI 层所需扩展（由 v2.1.0 反编译 UI 还原）
 */
enum class AdminScreen {
    DASHBOARD,
    CARDS,
    CATEGORIES,
    BUTTONS,
    SKILLS,
    TEXTS,
    LOGS,
    SETTINGS,
    APP_MODULES,
    THEME_KIT,
    ENTRIES
}

val AdminScreen.title: String
    get() = when (this) {
        AdminScreen.DASHBOARD -> "工作台"
        AdminScreen.CARDS -> "卡片管理"
        AdminScreen.CATEGORIES -> "分类管理"
        AdminScreen.BUTTONS -> "软件管理"
        AdminScreen.SKILLS -> "Skill 管理"
        AdminScreen.TEXTS -> "文本管理"
        AdminScreen.LOGS -> "操作日志"
        AdminScreen.SETTINGS -> "设置"
        AdminScreen.APP_MODULES -> "本体模块"
        AdminScreen.THEME_KIT -> "主题工具箱"
        AdminScreen.ENTRIES -> "入口管理"
    }

val AdminScreen.route: String
    get() = when (this) {
        AdminScreen.DASHBOARD -> "dashboard"
        AdminScreen.CARDS -> "cards"
        AdminScreen.CATEGORIES -> "categories"
        AdminScreen.BUTTONS -> "buttons"
        AdminScreen.SKILLS -> "skills"
        AdminScreen.TEXTS -> "texts"
        AdminScreen.LOGS -> "logs"
        AdminScreen.SETTINGS -> "settings"
        AdminScreen.APP_MODULES -> "modules"
        AdminScreen.THEME_KIT -> "theme"
        AdminScreen.ENTRIES -> "entries"
    }
