package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.DesignStyle
import com.example.ui.viewmodel.AppBottomTab
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 底部导航 Tab 数据配置：
 * - 鲜艳色彩独立渲染（每个图标独立配色，告别单调灰白）
 * - 伴生可爱萌宠与专属拟人台词，支持长按跟随手指
 */
data class BottomNavTabItem(
    val tab: AppBottomTab,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val gradient: List<Color>,
    val petKaomoji: String,
    val petVoice: String
)

/**
 * 底部导航栏（液体玻璃状 Liquid Glass + 鲜艳彩色图标 + 长按跟随手指萌趣动态特效）：
 * 1. 液体玻璃底座：多层半透明冰晶折射渐变 + 游走液态光斑 + 顶部曲面高光反射带 + 镜面渐变描边。
 * 2. 液态水滴滑动胶囊：切换 Tab 时选中背景如真实水滴般带弹性拉伸丝滑游移到目标 Tab。
 * 3. 绿色箭头需求：图标采用鲜艳独立色彩 + 独立轻光晕容器呈现（非灰白文本）。
 * 4. 绿色箭头需求：长按任意图标即可触发「萌宠跟随手指」可爱动态特效，手指拖曳时伴生晶莹液态光球、
 *    萌宠颜文字、互动台词气泡、流光拖尾与释放时的彩星爆裂动画！
 */
@Composable
fun CustomRadioBottomNav(
    selectedTab: AppBottomTab,
    onTabSelected: (AppBottomTab) -> Unit,
    modifier: Modifier = Modifier,
    uiText: com.example.data.remote.UiTextDto? = null,
    // 底部导航红点徽标：tab -> 徽标文字（如 "3"），空 map 即不显示
    badges: Map<AppBottomTab, String> = emptyMap(),
    // 2026-10-23 jicun 式底栏外观：隐藏文字标识 / 液态玻璃风格（关 = 渐变按钮样式）
    hideLabels: Boolean = false,
    glassStyle: Boolean = true,
    // 2026-10-24：底栏主题：auto=跟随主题 / glass=液态玻璃 / gradient=渐变按钮 / light_dock=浅色毛玻璃程序坞
    bottomBarStyle: String = "auto"
) {
    // v1.4.13：图4风格重写 —— 白色圆角容器 + 黄色高亮选中 pill（主题跟随）
    val themeColors = com.example.ui.theme.LocalThemeUiColors.current
    // v1.5.8：圆角跟 DesignStyle 走，手绘风用 24dp 大圆角
    // v1.6.2：卡通风格用 28.dp（更新弹窗同款）
    val navCorner = when (themeColors.designStyle) {
        DesignStyle.HAND_DRAWN -> 24.dp
        DesignStyle.CARTOON -> 28.dp
        DesignStyle.AURORA -> 28.dp
        // 2026-10-23：新粗野主题底栏 8dp 硬朗圆角（原稿签名）
        DesignStyle.NEO_BRUTALISM -> 8.dp
        // 2026-10-23：jicun 底栏跟随卡片 20dp
        DesignStyle.JICUN -> 20.dp
        // 2026-10-24：Frutiger Aero 底栏 26dp（设计稿 .fa-tabbar）
        DesignStyle.AERO -> 26.dp
        else -> 18.dp
    }
    val navPillCorner = when (themeColors.designStyle) {
        DesignStyle.HAND_DRAWN -> 16.dp
        DesignStyle.CARTOON -> 20.dp
        DesignStyle.AURORA -> 18.dp
        // 2026-10-24：Frutiger Aero 胶囊 20dp（设计稿 .fa-pill）
        DesignStyle.AERO -> 20.dp
        else -> 12.dp
    }
    val navItems = listOf(
        BottomNavTabItem(
            tab = AppBottomTab.HOME,
            title = uiText?.tabHome?.ifBlank { "首页" } ?: "首页",
            icon = Icons.Filled.Home,
            color = themeColors.primary,
            gradient = listOf(themeColors.primary, themeColors.secondary),
            petKaomoji = "(ฅ^•ﻌ•^ฅ)",
            petVoice = "带我回家喵~ ✨"
        ),
        BottomNavTabItem(
            tab = AppBottomTab.SOFTWARE,
            title = uiText?.tabSoftware?.ifBlank { "软件" } ?: "软件",
            icon = Icons.Filled.Widgets,
            color = themeColors.primary,
            gradient = listOf(themeColors.primary, themeColors.secondary),
            petKaomoji = "(≧∇≦)ﾉ",
            petVoice = "发现宝藏软件啦! ⚡"
        ),
        BottomNavTabItem(
            tab = AppBottomTab.TOOLBOX,
            title = uiText?.tabToolbox?.ifBlank { "工具箱" } ?: "工具箱",
            icon = Icons.Filled.Build,
            color = themeColors.primary,
            gradient = listOf(themeColors.primary, themeColors.secondary),
            petKaomoji = "( •̀ ω •́ )✧",
            petVoice = "万能工具变变变! 🍀"
        ),
        BottomNavTabItem(
            tab = AppBottomTab.SETTINGS,
            title = uiText?.tabSettings?.ifBlank { "设置" } ?: "设置",
            icon = Icons.Filled.Settings,
            color = themeColors.primary,
            gradient = listOf(themeColors.primary, themeColors.secondary),
            petKaomoji = "(¬‿¬)",
            petVoice = "设置一下更顺手~ ⚙"
        )
    )

    // 2026-10-24：浅色毛玻璃程序坞（用户 Dock HTML）优先于主题，独立渲染后直接返回
    if (bottomBarStyle == "light_dock") {
        LightDockBottomBar(
            navItems = navItems,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            badges = badges,
            hideLabels = hideLabels,
            modifier = modifier
        )
        return
    }

    // 2026-10-25：粒子拖尾底栏（用户发的粒子拖尾底栏 HTML）：显式样式优先于主题，
    // 与浅色坞同级，拖动鱼眼放大 + 手指处冒上升粒子 + 最近图标名提示
    if (bottomBarStyle == "particle") {
        ParticleDockBar(
            navItems = navItems,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            badges = badges,
            modifier = modifier
        )
        return
    }

    // 2026-10-25：程序坞主题详细重写：坞是主题本身的一部分，
    // 不再被存量玻璃/渐变开关盖住（浅色坞 light_dock 仍优先，它本身就是坞的变体）
    if (themeColors.designStyle == DesignStyle.DOCK) {
        DockBottomBar(
            navItems = navItems,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            badges = badges,
            modifier = modifier
        )
        return
    }

    // 2026-10-24：新主题专属底栏（柔影 / 柔光 / 黏土，各按 HTML tabbar 翻译），仅跟随主题时生效
    if (bottomBarStyle == "auto") {
        when (themeColors.designStyle) {
            DesignStyle.NEU -> {
                SoftThemeBottomBar(
                    navItems = navItems, selectedTab = selectedTab, onTabSelected = onTabSelected,
                    badges = badges, hideLabels = hideLabels, modifier = modifier,
                    barBg = Color(0xFFE0E8EF), barBorderColor = Color.White.copy(alpha = 0.333f),
                    barBorderWidth = 2.dp, barRadius = 32.dp, pillRadius = 22.dp,
                    shadowDp = 12.dp, shadowAmbient = Color(0xFF5F9DE7).copy(alpha = 0.48f),
                    selectedBrush = Brush.linearGradient(
                        listOf(
                            Color(0xFF5F9DE7).copy(alpha = 0.08f),
                            Color(0xFF5F9DE7).copy(alpha = 0.08f)
                        )
                    ),
                    selectedBorderColor = Color.White.copy(alpha = 0.333f),
                    selectedText = Color(0xFF3F7FC4), unselectedText = Color(0xFF7E97B8),
                    selectedIconTint = Color(0xFF3F7FC4), unselectedIconTint = Color(0xFF7E97B8)
                )
                return
            }
            DesignStyle.GLOSS -> {
                SoftThemeBottomBar(
                    navItems = navItems, selectedTab = selectedTab, onTabSelected = onTabSelected,
                    badges = badges, hideLabels = hideLabels, modifier = modifier,
                    barBg = Color.White.copy(alpha = 0.92f), barBorderColor = Color(0xFFB9BABD),
                    barBorderWidth = 1.dp, barRadius = 32.dp, pillRadius = 22.dp,
                    shadowDp = 8.dp, shadowAmbient = Color.Black.copy(alpha = 0.14f),
                    selectedBrush = Brush.linearGradient(
                        listOf(Color(0xFF4DA3FF), Color(0xFF007AFF))
                    ),
                    selectedBorderColor = Color(0xFF0062CC),
                    selectedText = Color.White, unselectedText = Color(0xFF8E8E93),
                    selectedIconTint = Color.White, unselectedIconTint = Color(0xFF8E8E93)
                )
                return
            }
            DesignStyle.CLAY -> {
                SoftThemeBottomBar(
                    navItems = navItems, selectedTab = selectedTab, onTabSelected = onTabSelected,
                    badges = badges, hideLabels = hideLabels, modifier = modifier,
                    barBg = Color(0xFFF7F2FB).copy(alpha = 0.9f), barBorderColor = Color.Transparent,
                    barBorderWidth = 0.dp, barRadius = 28.dp, pillRadius = 22.dp,
                    shadowDp = 10.dp, shadowAmbient = Color(0xFF785AA0).copy(alpha = 0.25f),
                    selectedBrush = Brush.linearGradient(
                        listOf(Color(0xFFFFB3C9), Color(0xFFFF9EBB))
                    ),
                    selectedBorderColor = null,
                    selectedText = Color.White, unselectedText = Color(0xFF9A8FB0),
                    selectedIconTint = Color.White, unselectedIconTint = Color(0xFF9A8FB0)
                )
                return
            }
            // 2026-10-24：液态玻璃专属底栏（预览 .lg 翻译）：暗玻璃条 + 白 30% 亮边，
            // 选中为预览未读徽标的蓝紫渐变 #3B6BFF→#8A3BFF
            DesignStyle.LIQUID -> {
                SoftThemeBottomBar(
                    navItems = navItems, selectedTab = selectedTab, onTabSelected = onTabSelected,
                    badges = badges, hideLabels = hideLabels, modifier = modifier,
                    barBg = Color.White.copy(alpha = 0.10f), barBorderColor = Color.White.copy(alpha = 0.30f),
                    barBorderWidth = 1.dp, barRadius = 34.dp, pillRadius = 22.dp,
                    shadowDp = 12.dp, shadowAmbient = Color.Black.copy(alpha = 0.55f),
                    selectedBrush = Brush.linearGradient(
                        listOf(Color(0xFF3B6BFF), Color(0xFF8A3BFF))
                    ),
                    selectedBorderColor = Color.White.copy(alpha = 0.30f),
                    selectedText = Color.White, unselectedText = Color.White.copy(alpha = 0.72f),
                    selectedIconTint = Color.White, unselectedIconTint = Color.White.copy(alpha = 0.72f)
                )
                return
            }
            // 2026-10-25：拨杆专属底栏（LEVER UI .tabbar）：白药丸条 + 拨杆蓝 pill + 深海硬边近似（柔阴影）
            DesignStyle.LEVER -> {
                SoftThemeBottomBar(
                    navItems = navItems, selectedTab = selectedTab, onTabSelected = onTabSelected,
                    badges = badges, hideLabels = hideLabels, modifier = modifier,
                    barBg = Color.White, barBorderColor = Color.Transparent,
                    barBorderWidth = 0.dp, barRadius = 32.dp, pillRadius = 24.dp,
                    shadowDp = 12.dp, shadowAmbient = Color(0xFF1A4766).copy(alpha = 0.18f),
                    selectedBrush = Brush.linearGradient(
                        listOf(Color(0xFF1269EF), Color(0xFF1269EF))
                    ),
                    selectedBorderColor = null,
                    selectedText = Color.White, unselectedText = Color(0xFF7BA7C4),
                    selectedIconTint = Color.White, unselectedIconTint = Color(0xFF7BA7C4)
                )
                return
            }
            // 2026-10-25：雾面专属底栏（GLASS UI .tabbar）：白 40% 雾面胶囊 + 白 55% 描边 + 白 65% pill
            DesignStyle.GLASS -> {
                SoftThemeBottomBar(
                    navItems = navItems, selectedTab = selectedTab, onTabSelected = onTabSelected,
                    badges = badges, hideLabels = hideLabels, modifier = modifier,
                    barBg = Color.White.copy(alpha = 0.40f), barBorderColor = Color.White.copy(alpha = 0.55f),
                    barBorderWidth = 1.dp, barRadius = 32.dp, pillRadius = 22.dp,
                    shadowDp = 8.dp, shadowAmbient = Color(0xFF1F3A5F).copy(alpha = 0.18f),
                    selectedBrush = Brush.linearGradient(
                        listOf(
                            Color(0xFFFFFFFF).copy(alpha = 0.65f),
                            Color(0xFFFFFFFF).copy(alpha = 0.65f)
                        )
                    ),
                    selectedBorderColor = Color.White.copy(alpha = 0.55f),
                    selectedText = Color(0xFF0653C7), unselectedText = Color(0xFF64748B),
                    selectedIconTint = Color(0xFF0653C7), unselectedIconTint = Color(0xFF64748B)
                )
                return
            }
            else -> { }
        }
    }

    // 2026-10-24：显式底栏主题覆盖旧 glass 开关；auto 时沿用旧逻辑
    val effectiveGlassStyle = when (bottomBarStyle) {
        "glass" -> true
        "gradient" -> false
        else -> glassStyle
    }

    // 2026-10-24：皮纹鞣制底栏（设计稿 g95）—— 边缘到边缘的深棕皮革条，
    // 与玻璃胶囊逻辑完全不同，独立渲染后直接返回；仅跟随主题时生效
    if (bottomBarStyle == "auto" && themeColors.designStyle == DesignStyle.LEATHER) {
        LeatherBottomBar(
            navItems = navItems,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            badges = badges,
            hideLabels = hideLabels,
            modifier = modifier
        )
        return
    }

    // 2026-10-23：选中态文字跟随主题 onPrimary（暗黑街头/星芒深色字保证对比度）
    // 2026-10-23：液态玻璃风格下选中态落在白色玻璃胶囊上，改用深色保证对比
    val selectedTextColor = if (effectiveGlassStyle) Color(0xFF1E293B) else themeColors.onPrimary
    val unselectedColor = themeColors.text.copy(alpha = 0.45f)

    // 2026-10-23：长按放大 + 透明玻璃质感
    var isLongPressed by remember { mutableStateOf(false) }
    val barScale by animateFloatAsState(
        targetValue = if (isLongPressed) 1.12f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "barLongPressScale"
    )
    // 长按时玻璃背景更透明（图标保持锐利）：背景色 alpha 动画
    val glassBgAlpha by animateFloatAsState(
        targetValue = if (isLongPressed) 0.45f else 0.90f,
        animationSpec = tween(250),
        label = "barGlassBgAlpha"
    )

    // 2026-10-23 jicun 式底栏外观：液态玻璃风格 vs 渐变按钮样式
    // 长按放大：pointerInput 检测长按，graphicsLayer 缩放 + 玻璃背景透明度
    val pressScope = rememberCoroutineScope()
    val longPressModifier = Modifier.pointerInput(Unit) {
        awaitEachGesture {
            // requireUnconsumed=false：子元素 clickable 会消费按下事件，父级也要能收到
            awaitFirstDown(requireUnconsumed = false)
            var longPressed = false
            val job = pressScope.launch {
                delay(400)
                longPressed = true
                isLongPressed = true
            }
            try {
                waitForUpOrCancellation()
            } finally {
                job.cancel()
                if (longPressed) isLongPressed = false
            }
        }
    }
    val scaleModifier = Modifier.graphicsLayer(
        scaleX = barScale,
        scaleY = barScale,
        transformOrigin = TransformOrigin.Center
    )

    // 2026-10-23：底栏底色/描边跟随主题（原稿签名；此前写死浅灰+ jicun 蓝描边）
    val barBaseColor = when (themeColors.designStyle) {
        DesignStyle.NEO_BRUTALISM -> themeColors.surface
        DesignStyle.NEUMORPHISM -> themeColors.background
        DesignStyle.GLASSMORPHISM, DesignStyle.AURORA -> Color.White.copy(alpha = 0.60f)
        DesignStyle.HAND_DRAWN -> Color.White.copy(alpha = 0.85f)
        // 2026-10-24：Frutiger Aero 底栏玻璃（设计稿 .fa-tabbar 白→浅蓝渐变基底）
        DesignStyle.AERO -> Color(0xFFE4F4FE)
        else -> themeColors.surface.copy(alpha = 0.92f)
    }
    val isBrutalistBar = themeColors.designStyle == DesignStyle.NEO_BRUTALISM
    val isAeroBar = themeColors.designStyle == DesignStyle.AERO
    val barModifier = if (effectiveGlassStyle) {
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 3.dp)
            .shadow(14.dp, RoundedCornerShape(navCorner), ambientColor = Color.Black.copy(alpha = 0.18f))
            .clip(RoundedCornerShape(navCorner))
            // 长按时背景更透明，玻璃质感增强
            // 2026-10-24：Frutiger Aero 用白→浅蓝垂直渐变（设计稿 .fa-tabbar）
            .then(
                if (isAeroBar) Modifier.background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.88f * glassBgAlpha + 0.12f * (1f - glassBgAlpha)),
                            Color(0xFFA5DCFA).copy(alpha = 0.60f * glassBgAlpha + 0.12f * (1f - glassBgAlpha))
                        )
                    )
                )
                else Modifier.background(barBaseColor.copy(alpha = glassBgAlpha))
            )
            .border(
                if (isBrutalistBar) 2.dp else 1.dp,
                if (isBrutalistBar) themeColors.border
                // 2026-10-24：Frutiger Aero 白色亮边（设计稿 border: 1px solid rgba(255,255,255,.95)）
                else if (isAeroBar) Color.White.copy(alpha = 0.95f)
                else themeColors.primary.copy(alpha = 0.12f),
                RoundedCornerShape(navCorner)
            )
            .padding(horizontal = 6.dp, vertical = 3.dp)
    } else {
        // 渐变按钮样式：透明容器，tab 本体为渐变按钮
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    }

    // 2026-10-23：底栏采用 liquid-radio 液态玻璃风格 —— 选中态为滑动液态玻璃胶囊
    //（翻译 React 版 SVG 湍流位移滤镜 + 多层内阴玻璃高光为 Compose 实现）
    // 2026-10-23 修：胶囊用 drawBehind 直接绘制（不参与布局），避免 fillMaxHeight 撑高底栏
    val selectedIndex = navItems.indexOfFirst { it.tab == selectedTab }.coerceAtLeast(0)
    // 液态滑动：spring 模拟 cubic-bezier(0.16,1,0.3,1) 的弹性
    val pillOffset by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = 380f
        ),
        label = "liquidPillX"
    )
    // 2026-10-24：改成 Apple iOS 26 Liquid Glass 风格 —— 更通透、高光克制
    // 2026-10-24：Frutiger Aero 下用蓝色玻璃胶囊（设计稿 .fa-pill）
    val isAeroPill = themeColors.designStyle == DesignStyle.AERO
    val liquidPillBg = if (effectiveGlassStyle) {
        Modifier.drawBehind {
            val tabW = size.width / navItems.size
            val corner = navPillCorner.toPx()
            val hPad = 3.dp.toPx()
            val vPad = 4.dp.toPx()
            val left = pillOffset * tabW + hPad
            val right = (pillOffset + 1) * tabW - hPad
            val top = vPad
            val bottom = size.height - vPad
            val w = right - left
            val h = bottom - top
            if (w <= 0 || h <= 0) return@drawBehind
            if (isAeroPill) {
                // Frutiger Aero 蓝色玻璃胶囊：蓝渐变 + 白亮边 + 蓝色光晕
                // 1. 蓝色光晕
                drawRoundRect(
                    color = Color(0xFF00A8E8).copy(alpha = 0.22f),
                    topLeft = Offset(left - 6.dp.toPx(), top - 3.dp.toPx()),
                    size = Size(w + 12.dp.toPx(), h + 6.dp.toPx()),
                    cornerRadius = CornerRadius(corner + 6.dp.toPx())
                )
                // 2. 主体：蓝渐变玻璃
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF00A8E8).copy(alpha = 0.32f),
                            Color(0xFF00A8E8).copy(alpha = 0.10f)
                        ),
                        startY = top,
                        endY = bottom
                    ),
                    topLeft = Offset(left, top),
                    size = Size(w, h),
                    cornerRadius = CornerRadius(corner)
                )
                // 3. 顶部高光
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.8f),
                            Color.White.copy(alpha = 0.0f)
                        ),
                        startY = top,
                        endY = top + h * 0.45f
                    ),
                    topLeft = Offset(left, top),
                    size = Size(w, h * 0.45f),
                    cornerRadius = CornerRadius(corner)
                )
                // 4. 白亮边
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.7f),
                    topLeft = Offset(left, top),
                    size = Size(w, h),
                    cornerRadius = CornerRadius(corner),
                    style = Stroke(1.dp.toPx())
                )
                return@drawBehind
            }
            // 1. 底层柔光（液体边缘）
            drawRoundRect(
                color = Color.White.copy(alpha = 0.28f),
                topLeft = Offset(left - 6.dp.toPx(), top - 3.dp.toPx()),
                size = Size(w + 12.dp.toPx(), h + 6.dp.toPx()),
                cornerRadius = CornerRadius(corner + 6.dp.toPx())
            )
            // 2. 主体：高通透玻璃（Apple 风格比霜化玻璃更透）
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.48f),
                        Color.White.copy(alpha = 0.32f),
                        Color.White.copy(alpha = 0.42f)
                    ),
                    startY = top,
                    endY = bottom
                ),
                topLeft = Offset(left, top),
                size = Size(w, h),
                cornerRadius = CornerRadius(corner)
            )
            // 3. 顶部镜面高光（Apple 标志性的一线高光）
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.85f),
                        Color.White.copy(alpha = 0.0f)
                    ),
                    startY = top,
                    endY = top + h * 0.28f
                ),
                topLeft = Offset(left, top),
                size = Size(w, h * 0.28f),
                cornerRadius = CornerRadius(corner)
            )
            // 4. 纤细亮边（0.8dp）
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.9f),
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.55f)
                    ),
                    start = Offset(left, top),
                    end = Offset(right, bottom)
                ),
                topLeft = Offset(left, top),
                size = Size(w, h),
                cornerRadius = CornerRadius(corner),
                style = Stroke(0.8.dp.toPx())
            )
        }
    } else Modifier

    Box(
        modifier = barModifier
            .then(longPressModifier)
            .then(scaleModifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(liquidPillBg),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = selectedTab == item.tab
                val pillShape = RoundedCornerShape(navPillCorner)
                // 渐变按钮样式：选中 = 渐变底白字，未选中 = 浅底（玻璃风格下选中态由滑动胶囊呈现）
                val itemBg: @Composable () -> Modifier = {
                    if (effectiveGlassStyle) {
                        Modifier // 液态玻璃胶囊已在底层滑动，此处不再画各自背景
                    } else if (isSelected) {
                        Modifier.background(
                            Brush.linearGradient(item.gradient),
                            pillShape
                        )
                    } else {
                        Modifier
                            .background(themeColors.surface.copy(alpha = 0.6f), pillShape)
                            .border(1.dp, themeColors.primary.copy(alpha = 0.15f), pillShape)
                    }
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(pillShape)
                            .then(itemBg())
                            .clickable { onTabSelected(item.tab) }
                            .padding(vertical = if (effectiveGlassStyle) 3.dp else 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    // 假 z-index:9999 层：图标与红点徽标绘制在玻璃之上，保持锐利不被折射
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) selectedTextColor else unselectedColor,
                            modifier = Modifier.size(16.dp)
                        )
                        badges[item.tab]?.let { badgeText ->
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-6).dp)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF3B30))
                                    .border(1.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    // 2026-10-23 jicun 式：底栏文字标识隐藏
                    if (!hideLabels) {
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                            color = if (isSelected) selectedTextColor else unselectedColor,
                            maxLines = 1
                        )
                    }
                    } // Column 闭合
                    // 2026-10-24 Frutiger Aero：选中 tab 顶部浮动气泡（绝对定位，不占布局高度）
                    if (isSelected && isAeroBar && effectiveGlassStyle) {
                        com.example.ui.theme.AeroBubble(
                            modifier = Modifier.align(Alignment.TopCenter).offset(y = (-10).dp),
                            size = 12.dp
                        )
                    }
                } // Box 闭合
            }
        }
    }
}

/**
 * 2026-10-24：柔影 / 柔光 / 黏土三套主题共用的悬浮胶囊底栏（各按 HTML tabbar 参数传色）：
 * - 外层悬浮胶囊（左右悬空 + 圆角 + 阴影），内层选中项为滑动胶囊的静态版（选中项背景即 pill）
 * - 图标 22dp（HTML .tab svg）、文字 10sp，徽标沿用红点
 * - 注意：HTML 的 inset 内阴影在 Compose 里没有直接等价物，选中凹陷感用描边 + 淡底近似，
 *   不声称与 HTML 的 inset 阴影完全一致
 */
@Composable
private fun SoftThemeBottomBar(
    navItems: List<BottomNavTabItem>,
    selectedTab: AppBottomTab,
    onTabSelected: (AppBottomTab) -> Unit,
    badges: Map<AppBottomTab, String>,
    hideLabels: Boolean,
    modifier: Modifier = Modifier,
    barBg: Color,
    barBorderColor: Color,
    barBorderWidth: androidx.compose.ui.unit.Dp,
    barRadius: androidx.compose.ui.unit.Dp,
    pillRadius: androidx.compose.ui.unit.Dp,
    shadowDp: androidx.compose.ui.unit.Dp,
    shadowAmbient: Color,
    selectedBrush: Brush,
    selectedBorderColor: Color?,
    selectedText: Color,
    unselectedText: Color,
    selectedIconTint: Color,
    unselectedIconTint: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 3.dp)
            .shadow(shadowDp, RoundedCornerShape(barRadius), ambientColor = shadowAmbient)
            .clip(RoundedCornerShape(barRadius))
            .background(barBg)
            .border(barBorderWidth, barBorderColor, RoundedCornerShape(barRadius))
            .padding(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = selectedTab == item.tab
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(pillRadius))
                            .let { m ->
                                if (isSelected) {
                                    m.background(selectedBrush)
                                        .let { mm ->
                                            if (selectedBorderColor != null) mm.border(1.dp, selectedBorderColor, RoundedCornerShape(pillRadius)) else mm
                                        }
                                } else m
                            }
                            .clickable { onTabSelected(item.tab) }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) selectedIconTint else unselectedIconTint,
                                modifier = Modifier.size(22.dp)
                            )
                            badges[item.tab]?.let { badgeText ->
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 8.dp, y = (-6).dp)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF3B30))
                                        .border(1.5.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = badgeText,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                        if (!hideLabels) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) selectedText else unselectedText,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2026-10-24：浅色毛玻璃程序坞底栏（用户 Dock HTML 原样翻译，移动端适配说明）：
 * - 悬浮胶囊：白 55% 底、1dp 白 90% 亮边、26dp 圆角、18dp 阴影、左右 12dp 悬空
 * - 图标：渐变底方块（每 Tab 一色，对标 HTML 的粉/紫/绿/黄）+ 15dp 圆角 + 白 70% 描边
 * - 选中放大：52dp（未选中 44dp，弹簧动画），上浮 4dp，底部运行点；无悬停提示，改用文字标签
 * - 分组分隔：软件 / 工具箱之间 1dp 竖线（对标 HTML 的 .sep）
 * - 注意：Compose 没有 Web 的 backdrop-filter 真实背景模糊，毛玻璃以半透明白近似，
 *   不声称与 HTML 的 blur(22px) saturate(1.6) 完全一致
 */
@Composable
private fun LightDockBottomBar(
    navItems: List<BottomNavTabItem>,
    selectedTab: AppBottomTab,
    onTabSelected: (AppBottomTab) -> Unit,
    badges: Map<AppBottomTab, String>,
    hideLabels: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 3.dp)
            .shadow(18.dp, RoundedCornerShape(26.dp), ambientColor = Color.Black.copy(alpha = 0.18f))
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White.copy(alpha = 0.55f))
            .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(26.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            navItems.forEachIndexed { idx, item ->
                if (idx == 2) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color(0xFF0F172A).copy(alpha = 0.15f))
                    )
                }
                val isSelected = selectedTab == item.tab
                val iconSize by animateDpAsState(
                    targetValue = if (isSelected) 52.dp else 44.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "dockIconSize"
                )
                val iconGradient = when (item.tab) {
                    AppBottomTab.HOME -> listOf(Color(0xFFFDA4AF), Color(0xFFE11D48))
                    AppBottomTab.SOFTWARE -> listOf(Color(0xFFC4B5FD), Color(0xFF8B5CF6))
                    AppBottomTab.TOOLBOX -> listOf(Color(0xFF6EE7B7), Color(0xFF059669))
                    AppBottomTab.SETTINGS -> listOf(Color(0xFFFCD34D), Color(0xFFF59E0B))
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.tab) }
                        .padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .offset(y = if (isSelected) (-4).dp else 0.dp)
                                .size(iconSize)
                                .shadow(6.dp, RoundedCornerShape(15.dp))
                                .clip(RoundedCornerShape(15.dp))
                                .background(Brush.linearGradient(iconGradient))
                                .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(15.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        badges[item.tab]?.let { badgeText ->
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-6).dp)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF3B30))
                                    .border(1.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF64748B))
                        )
                    }
                    if (!hideLabels) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFF1F2937) else Color(0xFF64748B),
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * 2026-10-24：程序坞底栏 —— 用户 macOS 风格程序坞 HTML（复刻自 21st.dev）的 .dock 原样复刻，
 * 不再套用通用胶囊条（之前几套主题变味就是因为只换色、形态仍是胶囊条）：
 * - 居中悬浮、内容宽（非满宽条），padding 14dp/16dp，gap 10dp，圆角 26dp
 * - 白 12% 底 + 白 22% 描边 + 18dp 黑 50% 阴影 + 顶部 1px 白 25% 内高光（HTML inset 高光近似）
 * - 图标 52dp 方块、圆角 14dp、白 20% 描边，渐变直接取 HTML 各图标：
 *   首页=访达蓝 #7DB4FF→#2F6FE4，软件=浏览器青 #5EEAD4→#0EA5E9，
 *   工具箱=相机紫 #C4B5FD→#8B5CF6，设置=设置灰 #9CA3AF→#374151
 * - 选中项下方 5dp 白 85% 运行点（HTML .dot-run）；设置前有 1dp 白 25% 分隔线
 * - 鱼眼放大：HTML 原公式 MAX=1.8、SIGMA=62、上浮=(s-1)*34、原点底部居中；
 *   手机无悬停，改为手指在坞上横向拖动时放大（HTML touchmove 同款），松手复位
 * - 不带文字（HTML 坞本身无文字，标签只在悬停提示里，手机上无悬停故省略）；
 *   角标红点保留（功能需要）
 * - 注意：Compose 没有 backdrop-filter，玻璃仍是半透明近似，真模糊做不出来；
 *   放大后的图标不被坞裁剪（背景层与图标层分离，外层不 clip）
 */
@Composable
private fun DockBottomBar(
    navItems: List<BottomNavTabItem>,
    selectedTab: AppBottomTab,
    onTabSelected: (AppBottomTab) -> Unit,
    badges: Map<AppBottomTab, String>,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var focusX by remember { mutableStateOf<Float?>(null) }
    // 2026-10-24：本工程 Compose 版本解析不到 positionInRoot/positionInParent，
    // 改用确定性布局计算：坞内子项顺序固定（tile, tile, tile, sep, tile），
    // 尺寸全是定值（图标 52dp、间距 10dp、分隔线 1dp），中心坐标直接算出；
    // 若 Tab 数不是 4 个，centers 为空，放大自动退化为 1（不影响点击）
    val tileCentersPx = remember(density, navItems.size) {
        if (navItems.size != 4) emptyMap()
        else {
            val tile = with(density) { 52.dp.toPx() }
            val gap = with(density) { 10.dp.toPx() }
            val sep = with(density) { 1.dp.toPx() }
            val map = mutableMapOf<AppBottomTab, Float>()
            var x = 0f
            // 子项：tile0, tile1, tile2, sep, tile3；相邻间 gap
            val tabs = listOf(
                navItems[0].tab, navItems[1].tab, navItems[2].tab, null, navItems[3].tab
            )
            val widths = listOf(tile, tile, tile, sep, tile)
            tabs.forEachIndexed { ci, tab ->
                if (ci > 0) x += gap
                if (tab != null) map[tab] = x + widths[ci] / 2f
                x += widths[ci]
            }
            map
        }
    }
    val sigmaPx = with(density) { 62.dp.toPx() }
    val liftPx = with(density) { 34.dp.toPx() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 6.dp, top = 3.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 背景层：圆角玻璃坞（不裁剪图标，放大时图标可伸出坞外，如 HTML）
        Box(
            modifier = Modifier
                .shadow(18.dp, RoundedCornerShape(26.dp), ambientColor = Color.Black.copy(alpha = 0.50f))
                .clip(RoundedCornerShape(26.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(26.dp))
                .drawBehind {
                    // inset 顶部高光近似：坞内顶边 1px 白 25% 线（两端留 26dp 圆角余量）
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f),
                        start = Offset(26.dp.toPx(), 1.dp.toPx()),
                        end = Offset(size.width - 26.dp.toPx(), 1.dp.toPx()),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { focusX = it.x },
                        onHorizontalDrag = { change, _ ->
                            focusX = change.position.x
                            change.consume()
                        },
                        onDragEnd = { focusX = null },
                        onDragCancel = { focusX = null }
                    )
                }
            ) {
                navItems.forEachIndexed { idx, item ->
                    if (idx == 3) {
                        // 设置前的分隔线（HTML 第二组分隔：白 25%、上下各留 6dp）
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Color.White.copy(alpha = 0.25f))
                                .align(Alignment.CenterVertically)
                        )
                    }
                    val isSelected = selectedTab == item.tab
                    val center = tileCentersPx[item.tab]
                    val fx = focusX
                    val scale = if (fx != null && center != null) {
                        val d = kotlin.math.abs(fx - center)
                        1f + 0.8f * kotlin.math.exp(-(d * d) / (2f * sigmaPx * sigmaPx)).toFloat()
                    } else 1f
                    val lift = (scale - 1f) * liftPx
                    val iconGradient = when (item.tab) {
                        AppBottomTab.HOME -> listOf(Color(0xFF7DB4FF), Color(0xFF2F6FE4))
                        AppBottomTab.SOFTWARE -> listOf(Color(0xFF5EEAD4), Color(0xFF0EA5E9))
                        AppBottomTab.TOOLBOX -> listOf(Color(0xFFC4B5FD), Color(0xFF8B5CF6))
                        AppBottomTab.SETTINGS -> listOf(Color(0xFF9CA3AF), Color(0xFF374151))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    translationY = -lift
                                    transformOrigin = TransformOrigin(0.5f, 1f)
                                }
                                .size(52.dp)
                                .shadow(6.dp, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.linearGradient(iconGradient))
                                .drawBehind {
                                    // HTML 图标内阴影：inset 0 1px 0 白 35% 顶部高光
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.35f),
                                        start = Offset(14.dp.toPx(), 1.dp.toPx()),
                                        end = Offset(size.width - 14.dp.toPx(), 1.dp.toPx()),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                                .border(1.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(14.dp))
                                .clickable { onTabSelected(item.tab) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            badges[item.tab]?.let { badgeText ->
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 8.dp, y = (-6).dp)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF3B30))
                                        .border(1.5.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = badgeText,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) Color.White.copy(alpha = 0.85f)
                                    else Color.Transparent
                                )
                        )
                    }
                }
            }
        }
    }
}

/**
 * 2026-10-24：皮纹鞣制底栏（设计稿 g95 原样翻译）
 * - 边缘到边缘深棕渐变（#3a2c20 → #2a2018），无圆角无浮动
 * - 顶部 2dp 金棕虚线缝线（dashed border）
 * - 噪点皮纹 + 45°/-45° 斜纹编织（Canvas 绘制，极低透明度）
 * - 未选中 #a08776，选中 #f0d9b8 + 图标圆形金棕描边 + 淡金底
 */
@Composable
private fun LeatherBottomBar(
    navItems: List<BottomNavTabItem>,
    selectedTab: AppBottomTab,
    onTabSelected: (AppBottomTab) -> Unit,
    badges: Map<AppBottomTab, String>,
    hideLabels: Boolean,
    modifier: Modifier = Modifier
) {
    val leatherTop = Color(0xFF3A2C20)
    val leatherBottom = Color(0xFF2A2018)
    val stitch = Color(0xFFDCB482).copy(alpha = 0.45f)
    val unselected = Color(0xFFA08776)
    val selected = Color(0xFFF0D9B8)
    val selectedRing = Color(0xFFD8B98A)
    val selectedBg = Color(0xFFD8B98A).copy(alpha = 0.12f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(
                Brush.verticalGradient(listOf(leatherTop, leatherBottom))
            )
            // 皮纹纹理层：噪点 + 斜纹编织（drawBehind，不占布局）
            .drawBehind {
                // 1. 斜纹编织：45° / -45° 细线，极低透明度
                val step = 8.dp.toPx()
                val lineAlpha = 0.02f
                var x = -size.height
                while (x < size.width + size.height) {
                    drawLine(
                        color = Color.White.copy(alpha = lineAlpha),
                        start = Offset(x, 0f),
                        end = Offset(x + size.height, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                    drawLine(
                        color = Color.Black.copy(alpha = 0.06f),
                        start = Offset(x + step / 2, 0f),
                        end = Offset(x + step / 2 + size.height, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                    x += step * 2
                }
                // 2. 噪点皮纹：伪随机小点（固定种子，渲染稳定）
                val dotStep = 6.dp.toPx()
                var row = 0
                var y = 0f
                while (y < size.height) {
                    var col = 0
                    var px = 0f
                    while (px < size.width) {
                        val hash = (row * 31 + col * 17) % 7
                        if (hash < 3) {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.03f),
                                radius = 1.dp.toPx(),
                                center = Offset(px + (hash * 1.5f).dp.toPx(), y + (hash * 0.8f).dp.toPx())
                            )
                        }
                        col++
                        px += dotStep
                    }
                    row++
                    y += dotStep
                }
                // 3. 顶部虚线缝线（2dp 金棕 dashed）
                drawLine(
                    color = stitch,
                    start = Offset(0f, 1.dp.toPx()),
                    end = Offset(size.width, 1.dp.toPx()),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(), 6.dp.toPx()), 0f)
                )
                // 4. 内阴影：顶部 8dp 黑色渐隐（皮革压边感）
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                        startY = 0f,
                        endY = 8.dp.toPx()
                    ),
                    size = Size(size.width, 8.dp.toPx())
                )
            }
            .padding(horizontal = 6.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = selectedTab == item.tab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.tab) }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // 选中态：圆形金棕描边 + 淡金底（设计稿 .tab.on .ic）
                        val iconModifier = if (isSelected) {
                            Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(selectedBg)
                                .border(2.dp, selectedRing, CircleShape)
                                .padding(4.dp)
                        } else {
                            Modifier.size(26.dp)
                        }
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) selected else unselected,
                            modifier = iconModifier
                        )
                        badges[item.tab]?.let { badgeText ->
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 8.dp, y = (-6).dp)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF3B30))
                                    .border(1.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    if (!hideLabels) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = item.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) selected else unselected,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

/**
 * 底部 Tab 长按跟随特效：
 * 优化文字可见度、全液体玻璃质感呈现、文字向上浮动上升动效
 */
@Composable
private fun CuteFingerFollower(
    item: BottomNavTabItem,
    fingerOffset: Offset
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cute_follower_wobble")

    // 文字向上浮动/升腾的连续动态效果
    val textAscendY by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = -7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "text_ascend_y"
    )

    // 液体玻璃流光呼吸
    val glassGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glass_glow"
    )

    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val wobbleAngle by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(850, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobble"
    )

    // 拖动倾斜惯性
    val tiltByDrag = (fingerOffset.x * 0.12f).coerceIn(-15f, 15f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(170.dp)
    ) {
        // 1. 浮动液体玻璃台词气泡（文字向上浮动效果）
        Box(
            modifier = Modifier
                .offset(y = textAscendY.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.88f),
                            Color(0xFFF8FAFC).copy(alpha = 0.75f),
                            Color(0xFFF1F5F9).copy(alpha = 0.80f),
                            Color.White.copy(alpha = 0.92f)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.98f),
                            item.color.copy(alpha = 0.65f * glassGlowAlpha),
                            Color(0xFF38BDF8).copy(alpha = 0.50f),
                            Color.White.copy(alpha = 0.95f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✨",
                    fontSize = 11.sp,
                    modifier = Modifier.offset(y = (-textAscendY * 0.5f).dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.petVoice,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 2. 液体玻璃晶莹萌宠光球
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(62.dp)
                .graphicsLayer {
                    scaleX = breatheScale
                    scaleY = breatheScale
                    rotationZ = wobbleAngle + tiltByDrag
                }
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.90f),
                            item.color.copy(alpha = 0.42f),
                            item.color.copy(alpha = 0.18f)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.sweepGradient(item.gradient),
                    shape = CircleShape
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = item.petKaomoji,
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Black,
                    color = item.color
                )
            }
        }
    }
}

// 2026-10-25：粒子拖尾底栏（用户发的「懒得找了 · 粒子拖尾底栏」HTML 翻译）：
// 渐变玻璃坞 + 斜向流光 + 鱼眼放大 + 拖动粒子拖尾 + 最近图标名提示；
// Compose 没有 backdrop-filter，玻璃用半透明渐变 + 描边近似；
// 图标不用原稿 emoji，沿用软件 4 个 Tab 的矢量图标，渐变取原稿值
private data class TrailParticle(
    val id: Long,
    val xPx: Float,
    val yPx: Float,
    val startMs: Long
)

@Composable
private fun ParticleDockBar(
    navItems: List<BottomNavTabItem>,
    selectedTab: AppBottomTab,
    onTabSelected: (AppBottomTab) -> Unit,
    badges: Map<AppBottomTab, String>,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var focusX by remember { mutableStateOf<Float?>(null) }
    var particles by remember { mutableStateOf<List<TrailParticle>>(emptyList()) }
    var nowMs by remember { mutableStateOf(0L) }
    var nextId by remember { mutableStateOf(0L) }
    // 粒子时钟：有粒子时才逐帧跑，粒子 950ms 后过期摘掉（墙钟，与生成时同源）
    LaunchedEffect(particles.isNotEmpty()) {
        while (particles.isNotEmpty()) {
            withFrameNanos {
                nowMs = System.currentTimeMillis()
                particles = particles.filter { nowMs - it.startMs < 950 }
            }
        }
    }
    // 与 DockBottomBar 相同的确定性布局：tile, tile, tile, sep, tile
    val tileCentersPx = remember(density, navItems.size) {
        if (navItems.size != 4) emptyMap()
        else {
            val tile = with(density) { 52.dp.toPx() }
            val gap = with(density) { 10.dp.toPx() }
            val sep = with(density) { 1.dp.toPx() }
            val map = mutableMapOf<AppBottomTab, Float>()
            var x = 0f
            val tabs = listOf(navItems[0].tab, navItems[1].tab, navItems[2].tab, null, navItems[3].tab)
            val widths = listOf(tile, tile, tile, sep, tile)
            tabs.forEachIndexed { ci, tab ->
                if (ci > 0) x += gap
                if (tab != null) map[tab] = x + widths[ci] / 2f
                x += widths[ci]
            }
            map
        }
    }
    val sigmaPx = with(density) { 62.dp.toPx() }
    val liftPx = with(density) { 34.dp.toPx() }
    val padHPx = with(density) { 16.dp.toPx() }
    val padTPx = with(density) { 14.dp.toPx() }
    // 拖动时最近的 Tab（提示条用）
    val nearestTab = focusX?.let { fx ->
        tileCentersPx.minByOrNull { kotlin.math.abs(fx - it.value) }?.key
    }
    val nearestTitle = navItems.firstOrNull { it.tab == nearestTab }?.title

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 6.dp, top = 3.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 提示条：拖动时显示最近图标名（原稿 hover tip 的触屏等价）
            if (focusX != null && nearestTitle != null) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF141C2E).copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(text = nearestTitle, fontSize = 12.sp, color = Color(0xFFCFE0F7))
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
            // 坞本体：不用 clip，背景用 shape 画，图标与粒子可伸出坞外（原稿 overflow 行为）
            Box(
                modifier = Modifier
                    .shadow(18.dp, RoundedCornerShape(26.dp), ambientColor = Color.Black.copy(alpha = 0.45f))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFFFFFFF).copy(alpha = 0.18f),
                                Color(0xFFFFFFFF).copy(alpha = 0.10f),
                                Color(0xFFFFFFFF).copy(alpha = 0.16f)
                            )
                        ),
                        RoundedCornerShape(26.dp)
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.30f), RoundedCornerShape(26.dp))
                    .drawBehind {
                        // inset 顶部高光：1px 白 35% 线（两端留 26dp 圆角余量）
                        drawLine(
                            color = Color.White.copy(alpha = 0.35f),
                            start = Offset(26.dp.toPx(), 1.dp.toPx()),
                            end = Offset(size.width - 26.dp.toPx(), 1.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = { offset ->
                                focusX = offset.x
                                val px = offset.x + padHPx
                                val py = offset.y + padTPx
                                particles = (particles + TrailParticle(nextId++, px, py, System.currentTimeMillis())).takeLast(40)
                            },
                            onHorizontalDrag = { change, _ ->
                                focusX = change.position.x
                                val px = change.position.x + padHPx
                                val py = change.position.y + padTPx
                                particles = (particles + TrailParticle(nextId++, px, py, System.currentTimeMillis())).takeLast(40)
                                change.consume()
                            },
                            onDragEnd = { focusX = null },
                            onDragCancel = { focusX = null }
                        )
                    }
            ) {
                // 斜向流光（原稿 .dock:after）：透明 → 白 20% → 透明
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colorStops = arrayOf(
                                    0.30f to Color.Transparent,
                                    0.48f to Color.White.copy(alpha = 0.20f),
                                    0.62f to Color.Transparent
                                )
                            ),
                            RoundedCornerShape(26.dp)
                        )
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    navItems.forEachIndexed { idx, item ->
                        if (idx == 3) {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(40.dp)
                                    .background(Color.White.copy(alpha = 0.25f))
                                    .align(Alignment.CenterVertically)
                            )
                        }
                        val isSelected = selectedTab == item.tab
                        val center = tileCentersPx[item.tab]
                        val fx = focusX
                        val scale = if (fx != null && center != null) {
                            val d = kotlin.math.abs(fx - center)
                            1f + 0.8f * kotlin.math.exp(-(d * d) / (2f * sigmaPx * sigmaPx)).toFloat()
                        } else 1f
                        val lift = (scale - 1f) * liftPx
                        val iconGradient = when (item.tab) {
                            AppBottomTab.HOME -> listOf(Color(0xFF7DD3FC), Color(0xFF0369A1))
                            AppBottomTab.SOFTWARE -> listOf(Color(0xFF38BDF8), Color(0xFF1D4ED8))
                            AppBottomTab.TOOLBOX -> listOf(Color(0xFFA78BFA), Color(0xFF6D28D9))
                            AppBottomTab.SETTINGS -> listOf(Color(0xFF94A3B8), Color(0xFF334155))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        translationY = -lift
                                        transformOrigin = TransformOrigin(0.5f, 1f)
                                    }
                                    .size(52.dp)
                                    .shadow(6.dp, RoundedCornerShape(14.dp))
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Brush.linearGradient(iconGradient))
                                    .drawBehind {
                                        drawLine(
                                            color = Color.White.copy(alpha = 0.35f),
                                            start = Offset(14.dp.toPx(), 1.dp.toPx()),
                                            end = Offset(size.width - 14.dp.toPx(), 1.dp.toPx()),
                                            strokeWidth = 1.dp.toPx()
                                        )
                                    }
                                    .border(1.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(14.dp))
                                    .clickable { onTabSelected(item.tab) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                badges[item.tab]?.let { badgeText ->
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 8.dp, y = (-6).dp)
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFF3B30))
                                            .border(1.5.dp, Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = badgeText,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) Color.White.copy(alpha = 0.85f)
                                        else Color.Transparent
                                    )
                            )
                        }
                    }
                }
                // 粒子层：盖在最上层，从手指处冒出、0.9 秒内上升 46dp 并淡出缩小到 0.3
                Box(modifier = Modifier.fillMaxSize()) {
                    val now = nowMs
                    particles.forEach { p ->
                        val progress = ((now - p.startMs) / 900f).coerceIn(0f, 1f)
                        val risePx = with(density) { 46.dp.toPx() } * progress
                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        (p.xPx - with(density) { 4.dp.toPx() }).toInt(),
                                        (p.yPx - risePx - with(density) { 4.dp.toPx() }).toInt()
                                    )
                                }
                                .graphicsLayer {
                                    alpha = 1f - progress
                                    scaleX = 1f - 0.7f * progress
                                    scaleY = 1f - 0.7f * progress
                                }
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color.White, Color(0xFF7DD3FC))
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}
