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
    // 默认主题：国风山水动态（青山黛墨绿 + 苍翠碧玉 + 烟雨玉白底 + 雅墨深字）
    val shanshuiTheme = ThemePreset(
        id = "shanshui_dongtai",
        name = "山水动态 (默认)",
        style = "landscape",
        categoryName = "国风山水",
        primaryColor = Color(0xFF1B4931),      // 黛墨松绿
        secondaryColor = Color(0xFF2D6A4F),    // 苍翠碧玉
        bgColor = Color(0xFFF2F7F4),           // 烟雨玉白
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF132A13),         // 黛墨黑
        atmosphereEffect = AtmosphereEffect.FIREFLIES,
        gradientColors = listOf(Color(0xFFE2EFE7), Color(0xFFF2F7F4)),
        cardGradientColors = listOf(Color(0xFFE2EFE7), Color(0xFFEBF5EE), Color(0xFFF2F7F4))
    )

    val defaultTheme = shanshuiTheme
    val shengshiTheme = shanshuiTheme

    // 经典皮肤（可选，保留供主题切换）
    val classicTheme = ThemePreset(
        id = "classic_default",
        name = "经典皮肤",
        style = "classic",
        categoryName = "经典",
        primaryColor = Color(0xFF2196F3),
        secondaryColor = Color(0xFF42A5F5),
        bgColor = Color(0xFFFAFAFA),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF212121),
        atmosphereEffect = AtmosphereEffect.NONE,
        cardGradientColors = listOf(Color(0xFFFFE4EC), Color(0xFFE9E4FF), Color(0xFFDCEBFF))
    )

    // 盛世华诞为默认软件主题；经典皮肤保留可选
    // v1.2.0：10 款渐变颜色主题（柔和渐变背景 + 对应主色）
    // v1.2.2：cardGradientColors 跟随各自主题色系，切换后卡片同步变色
    val gradientSunset = ThemePreset(
        id = "grad_sunset", name = "落日橘粉", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFFF7E5F), secondaryColor = Color(0xFFFEB47B),
        bgColor = Color(0xFFFFF3EC), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A2C20),
        gradientColors = listOf(Color(0xFFFFD8C4), Color(0xFFFFF0E8)),
        cardGradientColors = listOf(Color(0xFFFFD8C4), Color(0xFFFFE8D9), Color(0xFFFFF0E8))
    )
    val gradientOcean = ThemePreset(
        id = "grad_ocean", name = "海洋蓝", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF2193B0), secondaryColor = Color(0xFF6DD5ED),
        bgColor = Color(0xFFEAF6FB), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF14343F),
        gradientColors = listOf(Color(0xFFC9E9F7), Color(0xFFE8F6FC)),
        cardGradientColors = listOf(Color(0xFFC9E9F7), Color(0xFFD8F0FA), Color(0xFFE8F6FC))
    )
    val gradientMint = ThemePreset(
        id = "grad_mint", name = "薄荷奶绿", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF11998E), secondaryColor = Color(0xFF38EF7D),
        bgColor = Color(0xFFEAFBF2), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF123B2E),
        gradientColors = listOf(Color(0xFFC8F0DC), Color(0xFFEAFBF2)),
        cardGradientColors = listOf(Color(0xFFC8F0DC), Color(0xFFDCF5E8), Color(0xFFEAFBF2))
    )
    val gradientLavender = ThemePreset(
        id = "grad_lavender", name = "薰衣草紫", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF7F7FD5), secondaryColor = Color(0xFF86A8E7),
        bgColor = Color(0xFFF2EFFB), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF2E2A4A),
        gradientColors = listOf(Color(0xFFDCD6F5), Color(0xFFF2EFFB)),
        cardGradientColors = listOf(Color(0xFFDCD6F5), Color(0xFFE8E2F8), Color(0xFFF2EFFB))
    )
    val gradientCherry = ThemePreset(
        id = "grad_cherry", name = "樱花粉", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFF857A6), secondaryColor = Color(0xFFFF5858),
        bgColor = Color(0xFFFFF0F4), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A2030),
        gradientColors = listOf(Color(0xFFFBD3E0), Color(0xFFFFF0F4)),
        cardGradientColors = listOf(Color(0xFFFBD3E0), Color(0xFFFDE4EC), Color(0xFFFFF0F4))
    )
    val gradientSky = ThemePreset(
        id = "grad_sky", name = "天空蓝", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF4A90D9), secondaryColor = Color(0xFF63B8FF),
        bgColor = Color(0xFFEDF5FF), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF1B3A5C),
        gradientColors = listOf(Color(0xFFCFE8FF), Color(0xFFEDF5FF)),
        cardGradientColors = listOf(Color(0xFFCFE8FF), Color(0xFFDDF0FF), Color(0xFFEDF5FF))
    )
    val gradientPeach = ThemePreset(
        id = "grad_peach", name = "蜜桃甜橙", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFFF9A8B), secondaryColor = Color(0xFFFF6A88),
        bgColor = Color(0xFFFFF5F0), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A2A20),
        gradientColors = listOf(Color(0xFFFFD8CC), Color(0xFFFFF5F0)),
        cardGradientColors = listOf(Color(0xFFFFD8CC), Color(0xFFFFE5DC), Color(0xFFFFF5F0))
    )
    val gradientAurora = ThemePreset(
        id = "grad_aurora", name = "极光绿", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF00B4DB), secondaryColor = Color(0xFF0083B0),
        bgColor = Color(0xFFEAF8FA), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF103A40),
        gradientColors = listOf(Color(0xFFC0EEF4), Color(0xFFEAF8FA)),
        cardGradientColors = listOf(Color(0xFFC0EEF4), Color(0xFFD4F4F8), Color(0xFFEAF8FA))
    )
    val gradientRoseGold = ThemePreset(
        id = "grad_rosegold", name = "玫瑰金", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFB76E79), secondaryColor = Color(0xFFEACDC2),
        bgColor = Color(0xFFFAF3F0), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF3A2028),
        gradientColors = listOf(Color(0xFFF0D9D0), Color(0xFFFAF3F0)),
        cardGradientColors = listOf(Color(0xFFF0D9D0), Color(0xFFF7E6DF), Color(0xFFFAF3F0))
    )
    val gradientLemon = ThemePreset(
        id = "grad_lemon", name = "柠檬黄", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFFF7971E), secondaryColor = Color(0xFFFFD200),
        bgColor = Color(0xFFFFFAEB), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF4A3A10),
        gradientColors = listOf(Color(0xFFFFE9A8), Color(0xFFFFFAEB)),
        cardGradientColors = listOf(Color(0xFFFFE9A8), Color(0xFFFFF2C8), Color(0xFFFFFAEB))
    )
    val gradientNebula = ThemePreset(
        id = "grad_nebula", name = "星云蓝紫", style = "gradient", categoryName = "渐变色",
        primaryColor = Color(0xFF5C6BC0), secondaryColor = Color(0xFFAB47BC),
        bgColor = Color(0xFFF1EFFA), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF26204A),
        gradientColors = listOf(Color(0xFFD8D0F5), Color(0xFFF1EFFA)),
        cardGradientColors = listOf(Color(0xFFD8D0F5), Color(0xFFE6E0F8), Color(0xFFF1EFFA))
    )

    // 暗黑风格主题预设（暗黑风格按钮切换）
    val darkObsidian = ThemePreset(
        id = "dark_obsidian", name = "深邃曜石暗黑", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFFA855F7), secondaryColor = Color(0xFF38BDF8),
        bgColor = Color(0xFF0F172A), surfaceColor = Color(0xFF1E293B), textColor = Color(0xFFF1F5F9),
        gradientColors = listOf(Color(0xFF0B0F19), Color(0xFF131B2E)),
        cardGradientColors = listOf(Color(0xFF1E293B), Color(0xFF182234), Color(0xFF141C2B))
    )
    val darkMidnight = ThemePreset(
        id = "dark_midnight", name = "极夜星空暗黑", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFF38BDF8), secondaryColor = Color(0xFF818CF8),
        bgColor = Color(0xFF0B132B), surfaceColor = Color(0xFF1C2541), textColor = Color(0xFFE2E8F0),
        gradientColors = listOf(Color(0xFF070B19), Color(0xFF0E1A38)),
        cardGradientColors = listOf(Color(0xFF1C2541), Color(0xFF172038), Color(0xFF121B2F))
    )
    val darkCyberpunk = ThemePreset(
        id = "dark_cyberpunk", name = "赛博朋克深空", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFF06B6D4), secondaryColor = Color(0xFFEC4899),
        bgColor = Color(0xFF0D0D15), surfaceColor = Color(0xFF181824), textColor = Color(0xFFF8FAFC),
        gradientColors = listOf(Color(0xFF0A0A12), Color(0xFF141422)),
        cardGradientColors = listOf(Color(0xFF1F1F30), Color(0xFF181826), Color(0xFF12121E))
    )
    val darkEmerald = ThemePreset(
        id = "dark_emerald", name = "夜间护眼暗绿", style = "dark", categoryName = "暗黑风格",
        primaryColor = Color(0xFF10B981), secondaryColor = Color(0xFF34D399),
        bgColor = Color(0xFF061A14), surfaceColor = Color(0xFF0D2E24), textColor = Color(0xFFECFDF5),
        gradientColors = listOf(Color(0xFF041410), Color(0xFF0B241C)),
        cardGradientColors = listOf(Color(0xFF0D2E24), Color(0xFF0A261E), Color(0xFF071E17))
    )

    // iOS 风格主题（iOS 磨砂轻奢、iOS 纯白灵动、iOS 深空灰）
    val iosFrostGlass = ThemePreset(
        id = "ios_frost_glass", name = "iOS 磨砂轻奢", style = "ios", categoryName = "iOS风格",
        primaryColor = Color(0xFF007AFF), secondaryColor = Color(0xFF5856D6),
        bgColor = Color(0xFFF2F2F7), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF1C1C1E),
        gradientColors = listOf(Color(0xFFE5E5EA), Color(0xFFF2F2F7)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF9F9FB), Color(0xFFF2F2F7))
    )
    val iosDynamicWhite = ThemePreset(
        id = "ios_dynamic_white", name = "iOS 纯净灵动", style = "ios", categoryName = "iOS风格",
        primaryColor = Color(0xFF34C759), secondaryColor = Color(0xFF30B0C7),
        bgColor = Color(0xFFF8F9FA), surfaceColor = Color(0xFFFFFFFF), textColor = Color(0xFF111827),
        gradientColors = listOf(Color(0xFFEDF2F7), Color(0xFFF8F9FA)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC), Color(0xFFF1F5F9))
    )
    val iosSpaceGray = ThemePreset(
        id = "ios_space_gray", name = "iOS 深空夜影", style = "ios", categoryName = "iOS风格",
        primaryColor = Color(0xFF0A84FF), secondaryColor = Color(0xFF5E5CE6),
        bgColor = Color(0xFF1C1C1E), surfaceColor = Color(0xFF2C2C2E), textColor = Color(0xFFF2F2F7),
        gradientColors = listOf(Color(0xFF151517), Color(0xFF1C1C1E)),
        cardGradientColors = listOf(Color(0xFF2C2C2E), Color(0xFF242426), Color(0xFF1C1C1E))
    )

    // 新拟态风格主题（柔和凸起/凹陷浮雕光影）
    val neuSoftCream = ThemePreset(
        id = "neu_soft_cream", name = "新拟态·奶油浮雕", style = "neumorphism", categoryName = "新拟态",
        primaryColor = Color(0xFF6366F1), secondaryColor = Color(0xFFEC4899),
        bgColor = Color(0xFFE0E5EC), surfaceColor = Color(0xFFE0E5EC), textColor = Color(0xFF334155),
        gradientColors = listOf(Color(0xFFD6DCE5), Color(0xFFE8EEF5)),
        cardGradientColors = listOf(Color(0xFFE0E5EC), Color(0xFFE4E9F0), Color(0xFFE8EEF5))
    )
    val neuClaySlate = ThemePreset(
        id = "neu_clay_slate", name = "新拟态·冷灰微浮", style = "neumorphism", categoryName = "新拟态",
        primaryColor = Color(0xFF3B82F6), secondaryColor = Color(0xFF10B981),
        bgColor = Color(0xFFEBECF0), surfaceColor = Color(0xFFEBECF0), textColor = Color(0xFF1E293B),
        gradientColors = listOf(Color(0xFFE2E4E9), Color(0xFFF2F3F7)),
        cardGradientColors = listOf(Color(0xFFEBECF0), Color(0xFFEFF0F4), Color(0xFFF4F5F8))
    )
    val neuDarkRelief = ThemePreset(
        id = "neu_dark_relief", name = "新拟态·暗夜浮雕", style = "neumorphism", categoryName = "新拟态",
        primaryColor = Color(0xFF8B5CF6), secondaryColor = Color(0xFF06B6D4),
        bgColor = Color(0xFF24272C), surfaceColor = Color(0xFF24272C), textColor = Color(0xFFF1F5F9),
        gradientColors = listOf(Color(0xFF1C1E22), Color(0xFF2A2D33)),
        cardGradientColors = listOf(Color(0xFF24272C), Color(0xFF2A2E35), Color(0xFF31363E))
    )

    // ============================================================
    // 🎨 14大艺术与设计流派主题（涵盖拟物、扁平、Material、新拟态、玻璃拟态、
    // 粘土拟态、液态玻璃、新粗野主义、极简主义、包豪斯、复古未来、粗野主义、
    // 反设计、酸性设计——背景与图标容器全套跟随改变）
    // ============================================================
    val styleSkeuomorphism = ThemePreset(
        id = "style_skeuomorphism",
        name = "1️⃣ 拟物化 (Skeuomorphism)",
        style = "skeuomorphism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF8B4513),
        secondaryColor = Color(0xFFD4AF37),
        bgColor = Color(0xFFEFE9E3),
        surfaceColor = Color(0xFFF9F6F0),
        textColor = Color(0xFF3E2723),
        designStyle = DesignStyle.SKEUOMORPHISM,
        description = "模仿现实物体的质感、纹理和效果，让用户感到熟悉、亲切。",
        gradientColors = listOf(Color(0xFFE4DAD0), Color(0xFFF2ECE6)),
        cardGradientColors = listOf(Color(0xFFF9F6F0), Color(0xFFF0EAE3), Color(0xFFE5DDD5))
    )

    val styleFlatDesign = ThemePreset(
        id = "style_flat_design",
        name = "2️⃣ 扁平化 (Flat Design)",
        style = "flat_design",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF16A085),
        secondaryColor = Color(0xFF2980B9),
        bgColor = Color(0xFFECF0F1),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF2C3E50),
        designStyle = DesignStyle.FLAT_DESIGN,
        description = "作为对拟物化的“反叛”，其特点是简洁、二维、无阴影和纹理，强调内容本身。",
        gradientColors = listOf(Color(0xFFECF0F1), Color(0xFFE0E6ED)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF7F9FA), Color(0xFFEDF2F4))
    )

    val styleMaterialDesign = ThemePreset(
        id = "style_material_design",
        name = "3️⃣ 材质设计 (Material Design)",
        style = "material_design",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF6750A4),
        secondaryColor = Color(0xFF03DAC6),
        bgColor = Color(0xFFFEF7FF),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1D1B20),
        designStyle = DesignStyle.MATERIAL_DESIGN,
        description = "由Google推出，介于扁平与拟物之间，通过层级、阴影和动画模拟纸张和墨水的物理世界，提供清晰的空间感。",
        gradientColors = listOf(Color(0xFFF7EDFF), Color(0xFFFEF7FF)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF7EDFF), Color(0xFFEDE3F7))
    )

    val styleNeumorphism = ThemePreset(
        id = "style_neumorphism",
        name = "4️⃣ 新拟态 (Neumorphism)",
        style = "neumorphism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF4F46E5),
        secondaryColor = Color(0xFF06B6D4),
        bgColor = Color(0xFFE0E5EC),
        surfaceColor = Color(0xFFE0E5EC),
        textColor = Color(0xFF334155),
        designStyle = DesignStyle.NEUMORPHISM,
        description = "用和背景相似的卡片，通过精致的阴影（包括内阴影）来模拟出浮雕或嵌入的立体效果。",
        gradientColors = listOf(Color(0xFFD9DFE8), Color(0xFFE5EAF2)),
        cardGradientColors = listOf(Color(0xFFE0E5EC), Color(0xFFE5EAF0), Color(0xFFEBF0F6))
    )

    val styleGlassmorphism = ThemePreset(
        id = "style_glassmorphism",
        name = "5️⃣ 玻璃拟态 (Glassmorphism)",
        style = "glassmorphism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF818CF8),
        secondaryColor = Color(0xFF38BDF8),
        bgColor = Color(0xFF0F172A),
        surfaceColor = Color(0x331E293B),
        textColor = Color(0xFFF8FAFC),
        designStyle = DesignStyle.GLASSMORPHISM,
        description = "模拟磨砂玻璃的质感，通过透明度和背景模糊创建层次感鲜明的毛玻璃效果。",
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF1E1B4B)),
        cardGradientColors = listOf(Color(0x40334155), Color(0x2D1E293B), Color(0x220F172A))
    )

    val styleClaymorphism = ThemePreset(
        id = "style_claymorphism",
        name = "6️⃣ 粘土拟态 (Claymorphism)",
        style = "claymorphism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFFFF6584),
        secondaryColor = Color(0xFF6C63FF),
        bgColor = Color(0xFFF0F4FD),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF2D3748),
        designStyle = DesignStyle.CLAYMORPHISM,
        description = "相比新拟态，它更像是Q版的3D风格：使用明亮的色彩、圆润的边角和厚实的阴影，如同玩具一般可爱。",
        gradientColors = listOf(Color(0xFFE8EEFC), Color(0xFFF5F8FF)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFF2F6FE), Color(0xFFE8EEFC))
    )

    val styleLiquidGlass = ThemePreset(
        id = "style_liquid_glass",
        name = "7️⃣ 液态玻璃 (Liquid Glass)",
        style = "liquid_glass",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF00C6FF),
        secondaryColor = Color(0xFF0072FF),
        bgColor = Color(0xFFEAF5FA),
        surfaceColor = Color(0xDDFFFFFF),
        textColor = Color(0xFF0A3D62),
        designStyle = DesignStyle.LIQUID_GLASS,
        description = "被称为“动态的玻璃拟态”，增加了动态响应和光线折射效果，是玻璃拟态的进化版。",
        gradientColors = listOf(Color(0xFFD4EFFC), Color(0xFFF0F8FF)),
        cardGradientColors = listOf(Color(0xEEFFFFFF), Color(0xDDEDF8FD), Color(0xCCE0F2FA))
    )

    val styleNeoBrutalism = ThemePreset(
        id = "style_neo_brutalism",
        name = "8️⃣ 新粗野主义 (Neo-Brutalism)",
        style = "neo_brutalism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFFFFDE59),
        secondaryColor = Color(0xFFFF5757),
        bgColor = Color(0xFFFFFDF5),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF000000),
        designStyle = DesignStyle.NEO_BRUTALISM,
        description = "一种“反精致”的风格，使用粗重的边框、强烈的对比色和直接大胆的排版，极具视觉冲击力。",
        gradientColors = listOf(Color(0xFFFFFBE6), Color(0xFFFFFDF5)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFFFEEB), Color(0xFFFFFBE6))
    )

    val styleMinimalism = ThemePreset(
        id = "style_minimalism",
        name = "9️⃣ 极简主义 (Minimalism)",
        style = "minimalism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF18181B),
        secondaryColor = Color(0xFF71717A),
        bgColor = Color(0xFFFAFAFA),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF09090B),
        designStyle = DesignStyle.MINIMALISM,
        description = "“少即是多”。大量留白、无装饰的排版和有限的配色，只保留最核心的元素。",
        gradientColors = listOf(Color(0xFFF4F4F5), Color(0xFFFAFAFA)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFAFAFA), Color(0xFFF4F4F5))
    )

    val styleBauhaus = ThemePreset(
        id = "style_bauhaus",
        name = "🔟 包豪斯 (Bauhaus)",
        style = "bauhaus",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFFD62828),
        secondaryColor = Color(0xFF003049),
        bgColor = Color(0xFFFDFBF7),
        surfaceColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF1B1B1B),
        designStyle = DesignStyle.BAUHAUS,
        description = "强调形式服务于功能，喜欢使用基础的几何形状（圆形、方形、三角形）和红、黄、蓝三原色。",
        gradientColors = listOf(Color(0xFFF7F2E7), Color(0xFFFDFBF7)),
        cardGradientColors = listOf(Color(0xFFFFFFFF), Color(0xFFFDF9F0), Color(0xFFF6EEDC))
    )

    val styleRetrofuturism = ThemePreset(
        id = "style_retrofuturism",
        name = "1️⃣1️⃣ 复古未来主义 (Retrofuturism)",
        style = "retrofuturism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFFFF007F),
        secondaryColor = Color(0xFF00F0FF),
        bgColor = Color(0xFF120826),
        surfaceColor = Color(0xFF1E1035),
        textColor = Color(0xFFFCE7F3),
        designStyle = DesignStyle.RETROFUTURISM,
        description = "它是“过去的未来”，常混合蒸汽波、赛博朋克（霓虹灯、故障艺术）等元素，用复古视角想象未来世界。",
        gradientColors = listOf(Color(0xFF120826), Color(0xFF2A0845)),
        cardGradientColors = listOf(Color(0xFF1E1035), Color(0xFF281347), Color(0xFF190C30))
    )

    val styleBrutalism = ThemePreset(
        id = "style_brutalism",
        name = "1️⃣2️⃣ 粗野主义 (Brutalism)",
        style = "brutalism",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF333333),
        secondaryColor = Color(0xFF777777),
        bgColor = Color(0xFFDCDDDE),
        surfaceColor = Color(0xFFEBECEE),
        textColor = Color(0xFF000000),
        designStyle = DesignStyle.BRUTALISM,
        description = "新粗野主义的前身，它更极致，完全放弃装饰、结构外露，用纯粹功能性的元素直接呈现，显得有些“原始”。",
        gradientColors = listOf(Color(0xFFD3D4D6), Color(0xFFDCDDDE)),
        cardGradientColors = listOf(Color(0xFFEBECEE), Color(0xFFE2E3E6), Color(0xFFD7D8DC))
    )

    val styleAntiDesign = ThemePreset(
        id = "style_anti_design",
        name = "1️⃣3️⃣ 反设计 (Anti-Design)",
        style = "anti_design",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFF00FF66),
        secondaryColor = Color(0xFFFF0055),
        bgColor = Color(0xFF0A0014),
        surfaceColor = Color(0xFF1F002A),
        textColor = Color(0xFF00FF66),
        designStyle = DesignStyle.ANTI_DESIGN,
        description = "以一种混乱、不和谐、朋克风的方式，挑战传统的美学和可用性规则。",
        gradientColors = listOf(Color(0xFF0A0014), Color(0xFF200028)),
        cardGradientColors = listOf(Color(0xFF1F002A), Color(0xFF2B003A), Color(0xFF15001D))
    )

    val styleAcidGraphics = ThemePreset(
        id = "style_acid_graphics",
        name = "1️⃣4️⃣ 酸性设计 (Acid Graphics)",
        style = "acid_graphics",
        categoryName = "14大设计风格",
        primaryColor = Color(0xFFB026FF),
        secondaryColor = Color(0xFFCCFF00),
        bgColor = Color(0xFF0C071E),
        surfaceColor = Color(0xFF1A1238),
        textColor = Color(0xFFE0E7FF),
        designStyle = DesignStyle.ACID_GRAPHICS,
        description = "灵感源自迷幻视觉，常使用高饱和度、循环的几何图形、液态金属感以及混乱的排版，视觉冲击力极强。",
        gradientColors = listOf(Color(0xFF0C071E), Color(0xFF1E0D3A)),
        cardGradientColors = listOf(Color(0xFF1A1238), Color(0xFF25184F), Color(0xFF140B30))
    )

    val designStyleThemes: List<ThemePreset> = listOf(
        styleSkeuomorphism,
        styleFlatDesign,
        styleMaterialDesign,
        styleNeumorphism,
        styleGlassmorphism,
        styleClaymorphism,
        styleLiquidGlass,
        styleNeoBrutalism,
        styleMinimalism,
        styleBauhaus,
        styleRetrofuturism,
        styleBrutalism,
        styleAntiDesign,
        styleAcidGraphics
    )

    val iosThemes: List<ThemePreset> = listOf(
        iosFrostGlass, iosDynamicWhite, iosSpaceGray
    )

    val neuThemes: List<ThemePreset> = listOf(
        neuSoftCream, neuClaySlate, neuDarkRelief
    )

    val darkThemes: List<ThemePreset> = listOf(
        darkObsidian, darkMidnight, darkCyberpunk, darkEmerald
    )

    val gradientThemes: List<ThemePreset> = listOf(
        gradientSunset, gradientOcean, gradientMint, gradientLavender, gradientCherry,
        gradientSky, gradientPeach, gradientAurora, gradientRoseGold, gradientLemon, gradientNebula
    )

    val allThemes: List<ThemePreset> = listOf(
        shanshuiTheme,
        classicTheme
    ) + designStyleThemes + iosThemes + neuThemes + darkThemes + gradientThemes
}

val LocalUiverseState = staticCompositionLocalOf { ActiveUiverseState() }

// ============================================================
// v1.2.2：主题 UI 色板（ThemeUiColors）
// 从当前 ThemePreset 派生一整组 UI 组件颜色，通过 CompositionLocal 全局提供。
// 关键组件（卡片/底栏/按钮/标题/图标）改为从此色板读色，切换主题时 UI 自动跟随变色。
// ============================================================
data class ThemeUiColors(
    val primary: Color = Color(0xFF1B4931),        // 青山黛墨绿
    val secondary: Color = Color(0xFF2D6A4F),      // 苍翠碧玉
    val background: Color = Color(0xFFF2F7F4),     // 烟雨玉白
    val surface: Color = Color(0xFFFFFFFF),
    val text: Color = Color(0xFF132A13),           // 黛墨黑
    val textMuted: Color = Color(0xFF52796F),
    val accent: Color = Color(0xFF1B4931),
    val border: Color = Color(0xFF2D6A4F).copy(alpha = 0.35f),
    // 卡片渐变（国风山水温润水玉渐变）
    val cardGradient: List<Color> = listOf(Color(0xFFE2EFE7), Color(0xFFEBF5EE), Color(0xFFF2F7F4)),
    val designStyle: DesignStyle = DesignStyle.DEFAULT
)

val LocalThemeUiColors = staticCompositionLocalOf { ThemeUiColors() }

fun ThemePreset.toUiColors(): ThemeUiColors {
    val cardGrad = if (cardGradientColors.size >= 2) cardGradientColors
    else if (gradientColors.size >= 2) gradientColors
    else listOf(surfaceColor)
    return ThemeUiColors(
        primary = primaryColor,
        secondary = secondaryColor,
        background = if (gradientColors.size >= 2) gradientColors[0] else bgColor,
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
