package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

/** 全局主题：千禧复兴 Frutiger Aero + 皮纹鞣制 + 柔影 / 柔光 / 黏土 / 液态玻璃 / 程序坞（2026-10-24 用户新设计稿） */
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
    ),
    AppTheme(
        id = "liquid",
        name = "液态玻璃",
        // 2026-10-24：用户发的液态玻璃交互预览（默认暗色流光档）翻译进原生主题；
        // Compose 没有 backdrop-filter，玻璃用半透明渐变 + 亮边近似，不承诺真模糊
        isDark = true,
        colors = ThemeUiColors(
            primary = Color(0xFF3B6BFF),          // 徽标渐变起点蓝
            secondary = Color(0xFF8A3BFF),        // 徽标渐变终点紫
            background = Color(0xFF140A20),       // 预览 --mode-bg 深紫
            surface = Color(0xFF241A38),          // 深紫面板（玻璃白 10% 叠在深紫底上的近似色）
            text = Color(0xFFFFFFFF),             // 预览 --text-1 白
            textMuted = Color(0xFFFFFFFF).copy(alpha = 0.72f),  // 预览 --text-2 白 72%
            accent = Color(0xFF8A3BFF),           // 点缀紫
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFFFFFFFF).copy(alpha = 0.30f),  // 分组标题描边白 30%
            // 预览 .lg 玻璃渐变：白 17% → 5% → 10%（155deg）
            cardGradient = listOf(
                Color(0xFFFFFFFF).copy(alpha = 0.17f),
                Color(0xFFFFFFFF).copy(alpha = 0.05f),
                Color(0xFFFFFFFF).copy(alpha = 0.10f)
            ),
            // 预览壁纸：深紫底 + 四角流光（红/橙/粉/紫）近似为深紫渐变
            backgroundGradient = listOf(
                Color(0xFF2A1245),
                Color(0xFF1A0C2E),
                Color(0xFF140A20)
            ),
            designStyle = DesignStyle.LIQUID
        )
    ),
    AppTheme(
        id = "dock",
        name = "程序坞",
        // 2026-10-24：用户发的 macOS 风格程序坞 HTML（复刻自 21st.dev）翻译进原生主题；
        // Compose 没有 backdrop-filter，玻璃用半透明 + 描边近似，不承诺真模糊
        isDark = true,
        colors = ThemeUiColors(
            primary = Color(0xFF5AA9FF),          // 标题句点蓝
            secondary = Color(0xFF2F6FE4),        // 访达图标渐变深蓝
            background = Color(0xFF0B1220),       // 背景渐变中段深海军蓝
            surface = Color(0xFF1C2A44),          // 背景渐变顶段（面板色）
            text = Color(0xFFE8EEF7),             // 正文浅灰蓝白
            textMuted = Color(0xFF9FB3D1),        // 标签灰蓝
            accent = Color(0xFF5AA9FF),           // 点缀蓝
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFFFFFFFF).copy(alpha = 0.22f),  // 程序坞描边白 22%
            // 程序坞玻璃：白 12%（blur 22px saturate 1.6 的半透明近似）
            cardGradient = listOf(
                Color(0xFFFFFFFF).copy(alpha = 0.12f),
                Color(0xFFFFFFFF).copy(alpha = 0.06f)
            ),
            // body 径向渐变纵向近似：#1c2a44 → #0b1220 → #060a14
            backgroundGradient = listOf(
                Color(0xFF1C2A44),
                Color(0xFF0B1220),
                Color(0xFF060A14)
            ),
            designStyle = DesignStyle.DOCK
        )
    ),
    AppTheme(
        id = "lever",
        name = "拨杆",
        // 2026-10-25：用户发的 LEVER UI 拨杆组件库翻译进原生主题；
        // 卡片硬阴影 0 3px #B9D2E6（原稿 .vcardbox），底栏白药丸 + 拨杆蓝 pill
        isDark = false,
        colors = ThemeUiColors(
            primary = Color(0xFF1269EF),          // 拨杆蓝
            secondary = Color(0xFF0D4FC0),        // 拨杆深蓝（阴影/渐变深端）
            background = Color(0xFFDCEBF9),       // 纸蓝
            surface = Color(0xFFFFFFFF),          // 卡片白
            text = Color(0xFF085991),             // 墨水蓝
            textMuted = Color(0xFF3D7CA8),        // 次要蓝
            accent = Color(0xFFED487E),           // 拨杆粉
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFF1A4766),           // 深海（顶栏/卡片描边）
            hardShadowColor = Color(0xFFB9D2E6), // 卡片硬阴影
            hardShadowDx = 0.dp,
            hardShadowDy = 3.dp,
            cardGradient = listOf(
                Color(0xFFFFFFFF),
                Color(0xFFFFFFFF)
            ),
            backgroundGradient = listOf(
                Color(0xFFDCEBF9),
                Color(0xFFDCEBF9)
            ),
            designStyle = DesignStyle.LEVER
        )
    ),
    AppTheme(
        id = "glass",
        name = "雾面",
        // 2026-10-25：用户发的 GLASS UI 雾面组件库翻译进原生主题；
        // Compose 没有 backdrop-filter，玻璃用半透明 + 描边近似，不承诺真模糊；
        // 背景三色渐变纵向近似（原稿 135deg），光斑模糊不做
        isDark = false,
        colors = ThemeUiColors(
            primary = Color(0xFF2879F3),          // 雾面蓝
            secondary = Color(0xFF0653C7),        // 深蓝
            background = Color(0xFFEDF1F6),       // 渐变中段浅灰蓝
            surface = Color(0xFFFFFFFF).copy(alpha = 0.55f),  // 玻璃白 55%
            text = Color(0xFF22303F),             // 墨 slate
            textMuted = Color(0xFF64748B),        // 次要灰
            accent = Color(0xFFF37E10),           // 雾面橙
            onPrimary = Color(0xFFFFFFFF),
            border = Color(0xFFFFFFFF).copy(alpha = 0.55f),  // 描边白 55%
            cardGradient = listOf(
                Color(0xFFFFFFFF).copy(alpha = 0.45f),
                Color(0xFFFFFFFF).copy(alpha = 0.32f)
            ),
            backgroundGradient = listOf(
                Color(0xFFD3E3F8),
                Color(0xFFEDF1F6),
                Color(0xFFF8E9D4)
            ),
            designStyle = DesignStyle.GLASS
        )
    )
)

/** 默认主题：千禧复兴 */
const val DEFAULT_APP_THEME_ID = "minimal"

/** 按 id 取主题，非法 id 回退默认 */
fun appThemeById(id: String): AppTheme = APP_THEMES.firstOrNull { it.id == id } ?: APP_THEMES.first()
