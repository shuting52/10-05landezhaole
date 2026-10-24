package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
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
    glassStyle: Boolean = true
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

    // 2026-10-23：选中态文字跟随主题 onPrimary（暗黑街头/星芒深色字保证对比度）
    // 2026-10-23：液态玻璃风格下选中态落在白色玻璃胶囊上，改用深色保证对比
    val selectedTextColor = if (glassStyle) Color(0xFF1E293B) else themeColors.onPrimary
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
    val barModifier = if (glassStyle) {
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 6.dp)
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
            .padding(horizontal = 6.dp, vertical = 5.dp)
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
    val liquidPillBg = if (glassStyle) {
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
                    if (glassStyle) {
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
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(pillShape)
                        .then(itemBg())
                        .clickable { onTabSelected(item.tab) }
                        .padding(vertical = if (glassStyle) 5.dp else 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 假 z-index:9999 层：图标与红点徽标绘制在玻璃之上，保持锐利不被折射
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) selectedTextColor else unselectedColor,
                            modifier = Modifier.size(18.dp)
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
