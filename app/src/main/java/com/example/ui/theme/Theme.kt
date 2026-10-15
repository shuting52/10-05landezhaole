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
    ACID_GRAPHICS("Acid Graphics 酸性设计", "灵感源自迷幻视觉，常使用高饱和度、循环的几何图形、液态金属感以及混乱的排版，视觉冲击力极强。")
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
    // v1.4.13：主题系统重写 —— 移除全部旧主题（含默认），6 款新主题：
    // 黏土拟态 ×2 / 磨砂玻璃 / 液态玻璃 / iOS 清新 / iOS 暗夜
    // 每款都带 gradientColors，切换时背景必跟随变化。
    // ============================================================

    /** 橙子汽水 —— 3D 黏土拟态（默认主题） */
    val clayOrange = ThemePreset(
        id = "clay_orange",
        name = "橙子汽水",
        style = "clay",
        categoryName = "黏土拟态",
        primaryColor = Color(0xFFE8590C),
        secondaryColor = Color(0xFFFF9A3D),
        bgColor = Color(0xFFFFF3E6),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF4A2C12),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFE0B8), Color(0xFFFFF6EC)),
        cardGradientColors = listOf(Color(0xFFFFD8A8), Color(0xFFFFE8CC), Color(0xFFFFF3E6)),
        designStyle = DesignStyle.CLAYMORPHISM,
        description = "Q版3D黏土 · 暖橙可爱 · 默认"
    )

    /** 巧克力派 —— 3D 黏土拟态（暗） */
    val clayChoco = ThemePreset(
        id = "clay_choco",
        name = "巧克力派",
        style = "clay",
        categoryName = "黏土拟态",
        primaryColor = Color(0xFFFF9A3D),
        secondaryColor = Color(0xFFE8590C),
        bgColor = Color(0xFF2B1D14),
        surfaceColor = Color(0xFF3E2A1D),
        textColor = Color(0xFFFFF3E6),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFF3E2A1D), Color(0xFF241811)),
        cardGradientColors = listOf(Color(0xFF4A3423), Color(0xFF3E2A1D), Color(0xFF2B1D14)),
        designStyle = DesignStyle.CLAYMORPHISM,
        description = "浓郁巧克力 · 暗夜黏土"
    )

    /** 手绘暖阳 —— 手绘风（v1.4.31 新增，参考 shejishi 手绘风主题） */
    val handDrawnSunny = ThemePreset(
        id = "hand_drawn_sunny",
        name = "手绘暖阳",
        style = "handdrawn",
        categoryName = "手绘风",
        primaryColor = Color(0xFFFFC93C),      // 鸭黄
        secondaryColor = Color(0xFFFF9F43),    // 橙
        bgColor = Color(0xFFFFF6E5),           // 奶油黄
        surfaceColor = Color(0xFFFFFFFF),      // 纯白卡片
        textColor = Color(0xFF4A3B2A),         // 深棕
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFF6E5), Color(0xFFFFF0D0)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFFF9EE), Color(0xFFFFF6E5)),
        designStyle = DesignStyle.DEFAULT,
        description = "奶油黄底 · 鸭黄点缀 · 手绘治愈"
    )

    /** 磨砂玻璃 —— 玻璃拟态（浅） */
    val frostGlass = ThemePreset(
        id = "frost_glass",
        name = "磨砂玻璃",
        style = "glass",
        categoryName = "玻璃拟态",
        primaryColor = Color(0xFF2E7CD6),
        secondaryColor = Color(0xFF7BB8F0),
        bgColor = Color(0xFFEEF4FA),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1E3A5C),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFDCEBF8), Color(0xFFF7FBFE)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F7FD), Color(0xFFE8F2FA)),
        designStyle = DesignStyle.GLASSMORPHISM,
        description = "毛玻璃质感 · 通透轻盈"
    )

    /** 液态玻璃 —— 液态玻璃（动态光感） */
    val liquidGlass = ThemePreset(
        id = "liquid_glass",
        name = "液态玻璃",
        style = "glass",
        categoryName = "液态玻璃",
        primaryColor = Color(0xFF7C5CFF),
        secondaryColor = Color(0xFF4AD9E8),
        bgColor = Color(0xFFEDE9FE),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF2D2350),
        atmosphereEffect = AtmosphereEffect.AURORA,
        gradientColors = listOf(Color(0xFFDCD4FF), Color(0xFFEAFBFF)),
        cardGradientColors = listOf(Color(0xFFE0D9FF), Color(0xFFD6F3FA), Color(0xFFF0EFFF)),
        designStyle = DesignStyle.LIQUID_GLASS,
        description = "光线折射 · 梦幻流光"
    )

    /** iOS 清新 —— iOS 风格（浅） */
    val iosFresh = ThemePreset(
        id = "ios_fresh",
        name = "iOS 清新",
        style = "ios",
        categoryName = "iOS风格",
        primaryColor = Color(0xFF007AFF),
        secondaryColor = Color(0xFF5AC8FA),
        bgColor = Color(0xFFF2F2F7),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1C1C1E),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFE9E9EE), Color(0xFFFFFFFF)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF8F8FA), Color(0xFFF2F2F7)),
        designStyle = DesignStyle.FLAT_DESIGN,
        description = "原生 iOS 质感 · 干净利落"
    )

    /** iOS 暗夜 —— iOS 风格（暗） */
    val iosDark = ThemePreset(
        id = "ios_dark",
        name = "iOS 暗夜",
        style = "ios",
        categoryName = "iOS风格",
        primaryColor = Color(0xFF0A84FF),
        secondaryColor = Color(0xFF5AC8FA),
        bgColor = Color(0xFF000000),
        surfaceColor = Color(0xFF1C1C1E),
        textColor = Color(0xFFF2F2F7),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFF1C1C1E), Color(0xFF000000)),
        cardGradientColors = listOf(Color(0xFF2C2C2E), Color(0xFF1C1C1E), Color(0xFF111111)),
        designStyle = DesignStyle.FLAT_DESIGN,
        description = "深邃暗黑 · 护眼省电"
    )

    // ============================================================
    // v1.4.22：新增 30 款主题 —— 极简主义 ×10 / 新拟态 ×10 / 磨砂玻璃 ×10
    // ============================================================

    // ---------------- 极简主义 ×10（大量留白 · 单重点色 · 无装饰） ----------------
    val minimalPaper = ThemePreset(
        id = "minimal_paper", name = "纸白", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF1A1A1A), secondaryColor = Color(0xFF8A8A8A),
        bgColor = Color(0xFFFAFAF8), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF1A1A1A),
        gradientColors = listOf(Color(0xFFF4F4F0), Color(0xFFFFFFFF)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF7F7F4)),
        designStyle = DesignStyle.MINIMALISM, description = "一张白纸 · 黑白分明"
    )
    val minimalInk = ThemePreset(
        id = "minimal_ink", name = "墨黑", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFFF5F5F5), secondaryColor = Color(0xFF9A9A9A),
        bgColor = Color(0xFF0B0B0C), surfaceColor = Color(0xFF161618), textColor = Color(0xFFF5F5F5),
        gradientColors = listOf(Color(0xFF161618), Color(0xFF0B0B0C)),
        cardGradientColors = listOf(Color(0xFF1E1E20), Color(0xFF161618)),
        designStyle = DesignStyle.MINIMALISM, description = "极致黑 · 护眼深邃"
    )
    val minimalOat = ThemePreset(
        id = "minimal_oat", name = "燕麦", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF8A6D4B), secondaryColor = Color(0xFFB89B72),
        bgColor = Color(0xFFF6F1E8), surfaceColor = Color(0xFFFFFDF8), textColor = Color(0xFF4A3A28),
        gradientColors = listOf(Color(0xFFEFE7D8), Color(0xFFFFFDF8)),
        cardGradientColors = listOf(Color(0xFFFFFDF8), Color(0xFFF6F1E8)),
        designStyle = DesignStyle.MINIMALISM, description = "温暖燕麦 · 侘寂留白"
    )
    val minimalMist = ThemePreset(
        id = "minimal_mist", name = "雾蓝", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF4A7BA7), secondaryColor = Color(0xFF8FB4D4),
        bgColor = Color(0xFFF0F4F8), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF243B52),
        gradientColors = listOf(Color(0xFFE4ECF4), Color(0xFFF8FBFE)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F4F8)),
        designStyle = DesignStyle.MINIMALISM, description = "雾蓝呼吸 · 冷静克制"
    )
    val minimalMint = ThemePreset(
        id = "minimal_mint", name = "薄荷", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF3D8B6D), secondaryColor = Color(0xFF7FBC9E),
        bgColor = Color(0xFFEFF8F3), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF1F4436),
        gradientColors = listOf(Color(0xFFE0F0E8), Color(0xFFF7FCF9)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFEFF8F3)),
        designStyle = DesignStyle.MINIMALISM, description = "薄荷清新 · 自然留白"
    )
    val minimalSakura = ThemePreset(
        id = "minimal_sakura", name = "樱粉", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFFC26A8A), secondaryColor = Color(0xFFE0A0B8),
        bgColor = Color(0xFFFBF2F4), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF5C2E3E),
        gradientColors = listOf(Color(0xFFF6E4E9), Color(0xFFFDF7F9)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFBF2F4)),
        designStyle = DesignStyle.MINIMALISM, description = "樱粉淡雅 · 温柔留白"
    )
    val minimalLemon = ThemePreset(
        id = "minimal_lemon", name = "柠檬", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF9A7B00), secondaryColor = Color(0xFFC9A91F),
        bgColor = Color(0xFFFCF9EC), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4D3F0A),
        gradientColors = listOf(Color(0xFFF7F0D4), Color(0xFFFEFCF4)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFCF9EC)),
        designStyle = DesignStyle.MINIMALISM, description = "柠檬微光 · 明亮留白"
    )
    val minimalLavender = ThemePreset(
        id = "minimal_lavender", name = "薰衣草", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF7B6BAE), secondaryColor = Color(0xFFA89BD4),
        bgColor = Color(0xFFF4F2FA), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF3A3358),
        gradientColors = listOf(Color(0xFFE8E4F4), Color(0xFFFAF9FD)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF4F2FA)),
        designStyle = DesignStyle.MINIMALISM, description = "薰衣草紫 · 宁静留白"
    )
    val minimalCeladon = ThemePreset(
        id = "minimal_celadon", name = "青瓷", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF5B8A7E), secondaryColor = Color(0xFF8FB8AC),
        bgColor = Color(0xFFF0F5F3), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF2A443C),
        gradientColors = listOf(Color(0xFFE2EDE9), Color(0xFFF7FAF9)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F5F3)),
        designStyle = DesignStyle.MINIMALISM, description = "青瓷雅致 · 东方留白"
    )
    val minimalStone = ThemePreset(
        id = "minimal_stone", name = "石灰", style = "minimal", categoryName = "极简主义",
        primaryColor = Color(0xFF6E6E6E), secondaryColor = Color(0xFFA8A8A8),
        bgColor = Color(0xFFF4F4F2), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF2E2E2E),
        gradientColors = listOf(Color(0xFFE9E9E6), Color(0xFFFBFBFA)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF4F4F2)),
        designStyle = DesignStyle.MINIMALISM, description = "石灰质感 · 中性留白"
    )

    // ---------------- 新拟态 ×10（同色浮雕 · 精致双阴影） ----------------
    val neuCream = ThemePreset(
        id = "neu_cream", name = "奶油浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFFC67B3C), secondaryColor = Color(0xFFE0A866),
        bgColor = Color(0xFFE8E0D4), surfaceColor = Color(0xFFE8E0D4), textColor = Color(0xFF5C4326),
        gradientColors = listOf(Color(0xFFF0E9DC), Color(0xFFDCD2C2)),
        cardGradientColors = listOf(Color(0xFFF0E9DC), Color(0xFFE8E0D4), Color(0xFFDCD2C2)),
        designStyle = DesignStyle.NEUMORPHISM, description = "奶油浮雕 · 温润立体"
    )
    val neuMist = ThemePreset(
        id = "neu_mist", name = "雾灰浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFF6B7B9A), secondaryColor = Color(0xFF9AABC4),
        bgColor = Color(0xFFE0E5EC), surfaceColor = Color(0xFFE0E5EC), textColor = Color(0xFF3A4456),
        gradientColors = listOf(Color(0xFFEAEEF4), Color(0xFFD2D8E0)),
        cardGradientColors = listOf(Color(0xFFEAEEF4), Color(0xFFE0E5EC), Color(0xFFD2D8E0)),
        designStyle = DesignStyle.NEUMORPHISM, description = "雾灰浮雕 · 经典新拟态"
    )
    val neuPeach = ThemePreset(
        id = "neu_peach", name = "蜜桃浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFFD47B5C), secondaryColor = Color(0xFFE8A184),
        bgColor = Color(0xFFF0DCD2), surfaceColor = Color(0xFFF0DCD2), textColor = Color(0xFF6B3A2A),
        gradientColors = listOf(Color(0xFFF8E6DC), Color(0xFFE4CFC2)),
        cardGradientColors = listOf(Color(0xFFF8E6DC), Color(0xFFF0DCD2), Color(0xFFE4CFC2)),
        designStyle = DesignStyle.NEUMORPHISM, description = "蜜桃浮雕 · 甜美立体"
    )
    val neuSky = ThemePreset(
        id = "neu_sky", name = "天青浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFF5B8FB9), secondaryColor = Color(0xFF8FBCD8),
        bgColor = Color(0xFFDCE9F0), surfaceColor = Color(0xFFDCE9F0), textColor = Color(0xFF2E4A5E),
        gradientColors = listOf(Color(0xFFE6F0F6), Color(0xFFCCDAE6)),
        cardGradientColors = listOf(Color(0xFFE6F0F6), Color(0xFFDCE9F0), Color(0xFFCCDAE6)),
        designStyle = DesignStyle.NEUMORPHISM, description = "天青浮雕 · 通透立体"
    )
    val neuMint = ThemePreset(
        id = "neu_mint", name = "薄荷浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFF4E9B7E), secondaryColor = Color(0xFF7FBEA2),
        bgColor = Color(0xFFD8EDE4), surfaceColor = Color(0xFFD8EDE4), textColor = Color(0xFF26493C),
        gradientColors = listOf(Color(0xFFE2F4EC), Color(0xFFC8DFD4)),
        cardGradientColors = listOf(Color(0xFFE2F4EC), Color(0xFFD8EDE4), Color(0xFFC8DFD4)),
        designStyle = DesignStyle.NEUMORPHISM, description = "薄荷浮雕 · 清新立体"
    )
    val neuLilac = ThemePreset(
        id = "neu_lilac", name = "丁香浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFF7E6BB8), secondaryColor = Color(0xFFA394D4),
        bgColor = Color(0xFFE2DFF0), surfaceColor = Color(0xFFE2DFF0), textColor = Color(0xFF3E3660),
        gradientColors = listOf(Color(0xFFECE9F8), Color(0xFFD4CFE6)),
        cardGradientColors = listOf(Color(0xFFECE9F8), Color(0xFFE2DFF0), Color(0xFFD4CFE6)),
        designStyle = DesignStyle.NEUMORPHISM, description = "丁香浮雕 · 梦幻立体"
    )
    val neuLemon = ThemePreset(
        id = "neu_lemon", name = "柠檬浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFFB89B3C), secondaryColor = Color(0xFFD4BC66),
        bgColor = Color(0xFFF2ECD8), surfaceColor = Color(0xFFF2ECD8), textColor = Color(0xFF5E4E1E),
        gradientColors = listOf(Color(0xFFFAF4E2), Color(0xFFE6DCC2)),
        cardGradientColors = listOf(Color(0xFFFAF4E2), Color(0xFFF2ECD8), Color(0xFFE6DCC2)),
        designStyle = DesignStyle.NEUMORPHISM, description = "柠檬浮雕 · 明亮立体"
    )
    val neuCoral = ThemePreset(
        id = "neu_coral", name = "珊瑚浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFFD4665C), secondaryColor = Color(0xFFE8948A),
        bgColor = Color(0xFFF0D8D0), surfaceColor = Color(0xFFF0D8D0), textColor = Color(0xFF6B322A),
        gradientColors = listOf(Color(0xFFF8E2DA), Color(0xFFE4C8BE)),
        cardGradientColors = listOf(Color(0xFFF8E2DA), Color(0xFFF0D8D0), Color(0xFFE4C8BE)),
        designStyle = DesignStyle.NEUMORPHISM, description = "珊瑚浮雕 · 热情立体"
    )
    val neuMidnight = ThemePreset(
        id = "neu_midnight", name = "午夜浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFF7B8FD4), secondaryColor = Color(0xFFA3B4E8),
        bgColor = Color(0xFF2A2E3A), surfaceColor = Color(0xFF2A2E3A), textColor = Color(0xFFE4E8F2),
        gradientColors = listOf(Color(0xFF343947), Color(0xFF20242E)),
        cardGradientColors = listOf(Color(0xFF343947), Color(0xFF2A2E3A), Color(0xFF20242E)),
        designStyle = DesignStyle.NEUMORPHISM, description = "午夜浮雕 · 暗夜立体"
    )
    val neuForest = ThemePreset(
        id = "neu_forest", name = "森绿浮雕", style = "neu", categoryName = "新拟态",
        primaryColor = Color(0xFF6BAE7B), secondaryColor = Color(0xFF93C8A2),
        bgColor = Color(0xFF24352C), surfaceColor = Color(0xFF24352C), textColor = Color(0xFFE2EFE6),
        gradientColors = listOf(Color(0xFF2E4237), Color(0xFF1C2A21)),
        cardGradientColors = listOf(Color(0xFF2E4237), Color(0xFF24352C), Color(0xFF1C2A21)),
        designStyle = DesignStyle.NEUMORPHISM, description = "森绿浮雕 · 深邃立体"
    )

    // ---------------- 磨砂玻璃 ×10（不同配色风格） ----------------
    val glassOcean = ThemePreset(
        id = "glass_ocean", name = "海洋", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFF0A84FF), secondaryColor = Color(0xFF5AC8FA),
        bgColor = Color(0xFFE8F2FC), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF12395E),
        gradientColors = listOf(Color(0xFFD4E8FA), Color(0xFFF2F9FE)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFE8F2FC), Color(0xFFD4E8FA)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "深海之蓝 · 通透"
    )
    val glassSunset = ThemePreset(
        id = "glass_sunset", name = "落日", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFFFF6B4A), secondaryColor = Color(0xFFFFB84A),
        bgColor = Color(0xFFFDEEE6), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF6B2E1A),
        gradientColors = listOf(Color(0xFFFADFD2), Color(0xFFFFF6F0)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFDEEE6), Color(0xFFFADFD2)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "落日熔金 · 温暖"
    )
    val glassAurora = ThemePreset(
        id = "glass_aurora", name = "极光", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFF7C5CFF), secondaryColor = Color(0xFF4AD9E8),
        bgColor = Color(0xFFEEE9FE), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF352A6B),
        atmosphereEffect = AtmosphereEffect.AURORA,
        gradientColors = listOf(Color(0xFFDDD4FE), Color(0xFFEAFBFF)),
        cardGradientColors = listOf(Color(0xFFE4DBFE), Color(0xFFD9F2FA), Color(0xFFF2EEFE)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "极光流光 · 梦幻"
    )
    val glassSakura = ThemePreset(
        id = "glass_sakura", name = "樱花", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFFF06292), secondaryColor = Color(0xFFF8A8C8),
        bgColor = Color(0xFFFCEEF3), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF6B2A40),
        gradientColors = listOf(Color(0xFFFADDE8), Color(0xFFFFF5F8)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFCEEF3), Color(0xFFFADDE8)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "樱花粉黛 · 浪漫"
    )
    val glassMint = ThemePreset(
        id = "glass_mint", name = "薄荷", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFF00B894), secondaryColor = Color(0xFF55DDBB),
        bgColor = Color(0xFFE8F8F2), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF0F4D3E),
        gradientColors = listOf(Color(0xFFD2F0E4), Color(0xFFF4FCF9)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFE8F8F2), Color(0xFFD2F0E4)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "薄荷冰沙 · 清爽"
    )
    val glassGrape = ThemePreset(
        id = "glass_grape", name = "葡萄", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFF9B51E0), secondaryColor = Color(0xFFC084FC),
        bgColor = Color(0xFFF1EAFB), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF44266B),
        gradientColors = listOf(Color(0xFFE4D6F8), Color(0xFFFAF6FE)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF1EAFB), Color(0xFFE4D6F8)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "葡萄紫雾 · 优雅"
    )
    val glassHoney = ThemePreset(
        id = "glass_honey", name = "蜜橙", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFFFF9A3D), secondaryColor = Color(0xFFFFD93D),
        bgColor = Color(0xFFFFF2E2), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF6B4416),
        gradientColors = listOf(Color(0xFFFFE4C2), Color(0xFFFFF9F0)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFFF2E2), Color(0xFFFFE4C2)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "蜜橙暖阳 · 甜美"
    )
    val glassGlacier = ThemePreset(
        id = "glass_glacier", name = "冰川", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFF22B8CF), secondaryColor = Color(0xFF4AD9E8),
        bgColor = Color(0xFFEAFBFE), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF134E5C),
        gradientColors = listOf(Color(0xFFD4F2F8), Color(0xFFF5FDFF)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFEAFBFE), Color(0xFFD4F2F8)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "冰川水晶 · 纯净"
    )
    val glassNeon = ThemePreset(
        id = "glass_neon", name = "霓虹夜", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFF00F0FF), secondaryColor = Color(0xFFFF2EA6),
        bgColor = Color(0xFF0A0E1A), surfaceColor = Color(0xFF141A2E), textColor = Color(0xFFEAF6FF),
        atmosphereEffect = AtmosphereEffect.STARS,
        gradientColors = listOf(Color(0xFF141A2E), Color(0xFF0A0E1A)),
        cardGradientColors = listOf(Color(0xFF1E2640), Color(0xFF141A2E), Color(0xFF0E1424)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "霓虹暗夜 · 赛博"
    )
    val glassForest = ThemePreset(
        id = "glass_forest", name = "森林", style = "glass", categoryName = "玻璃拟态",
        primaryColor = Color(0xFF2E9E6B), secondaryColor = Color(0xFF7BC8A0),
        bgColor = Color(0xFFEAF5EE), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF1E4A34),
        gradientColors = listOf(Color(0xFFD8EBDE), Color(0xFFF6FBF7)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFEAF5EE), Color(0xFFD8EBDE)),
        designStyle = DesignStyle.GLASSMORPHISM, description = "森林氧气 · 自然"
    )

    /** 奶油团子 —— 启动页同款 pastel 奶油风（v1.4.22 起为默认主题） */
    val creamDango = ThemePreset(
        id = "cream_dango",
        name = "奶油团子",
        style = "clay",
        categoryName = "黏土拟态",
        primaryColor = Color(0xFFE8590C),
        secondaryColor = Color(0xFFFF9A3D),
        bgColor = Color(0xFFFFF8E7),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF4A2C12),
        atmosphereEffect = AtmosphereEffect.NONE,
        gradientColors = listOf(Color(0xFFFFF1DC), Color(0xFFFFF8E7)),
        cardGradientColors = listOf(Color(0xFFFFE8D6), Color(0xFFFFF1DC), Color(0xFFFFF8E7)),
        designStyle = DesignStyle.CLAYMORPHISM,
        description = "启动页同款 · 奶油 pastel · 默认"
    )

    // —— 兼容别名（供旧引用处过渡，实际指向新主题） ——
    // v1.4.22：默认主题改为启动页风格「奶油团子」
    val defaultTheme = creamDango
    val classicTheme = iosFresh
    val shengshiTheme = clayOrange
    val shanshuiTheme = clayOrange

    // —— 分类列表（供主题切换弹窗） ——
    val clayThemes: List<ThemePreset> = listOf(creamDango, clayOrange, clayChoco)
    // v1.4.31：手绘风分类
    val handDrawnThemes: List<ThemePreset> = listOf(handDrawnSunny)
    val glassThemes: List<ThemePreset> = listOf(
        frostGlass, liquidGlass,
        glassOcean, glassSunset, glassAurora, glassSakura, glassMint,
        glassGrape, glassHoney, glassGlacier, glassNeon, glassForest
    )
    val iosThemes: List<ThemePreset> = listOf(iosFresh, iosDark)
    // v1.4.22：新增极简 / 新拟态分类
    val minimalThemes: List<ThemePreset> = listOf(
        minimalPaper, minimalInk, minimalOat, minimalMist, minimalMint,
        minimalSakura, minimalLemon, minimalLavender, minimalCeladon, minimalStone
    )
    val neuThemes: List<ThemePreset> = listOf(
        neuCream, neuMist, neuPeach, neuSky, neuMint,
        neuLilac, neuLemon, neuCoral, neuMidnight, neuForest
    )

    // 兼容旧字段名（UiverseDialog 过渡用，实际不再使用）
    val darkThemes: List<ThemePreset> = listOf(clayChoco, iosDark, minimalInk, neuMidnight, neuForest, glassNeon)
    val designStyleThemes: List<ThemePreset> = emptyList()
    val gradientThemes: List<ThemePreset> = listOf(liquidGlass, frostGlass)

    val allThemes: List<ThemePreset> = listOf(
        creamDango, clayOrange, clayChoco, handDrawnSunny, frostGlass, liquidGlass, iosFresh, iosDark,
        minimalPaper, minimalInk, minimalOat, minimalMist, minimalMint,
        minimalSakura, minimalLemon, minimalLavender, minimalCeladon, minimalStone,
        neuCream, neuMist, neuPeach, neuSky, neuMint,
        neuLilac, neuLemon, neuCoral, neuMidnight, neuForest,
        glassOcean, glassSunset, glassAurora, glassSakura, glassMint,
        glassGrape, glassHoney, glassGlacier, glassNeon, glassForest
    )

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
