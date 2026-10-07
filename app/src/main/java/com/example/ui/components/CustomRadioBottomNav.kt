package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
import com.example.ui.viewmodel.AppBottomTab
import kotlinx.coroutines.launch
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
    uiText: com.example.data.remote.UiTextDto? = null
) {
    val navItems = listOf(
        BottomNavTabItem(
            tab = AppBottomTab.HOME,
            title = uiText?.tabHome?.ifBlank { "首页" } ?: "首页",
            icon = Icons.Filled.Home,
            color = Color(0xFF2563EB), // 晴空蔚蓝
            gradient = listOf(Color(0xFF3B82F6), Color(0xFF06B6D4)),
            petKaomoji = "(ฅ^•ﻌ•^ฅ)",
            petVoice = "带我回家喵~ ✨"
        ),
        BottomNavTabItem(
            tab = AppBottomTab.SOFTWARE,
            title = uiText?.tabSoftware?.ifBlank { "软件" } ?: "软件",
            icon = Icons.Filled.Widgets,
            color = Color(0xFFF97316), // 活力暖橙
            gradient = listOf(Color(0xFFFB923C), Color(0xFFF59E0B)),
            petKaomoji = "(≧∇≦)ﾉ",
            petVoice = "发现宝藏软件啦! ⚡"
        ),
        BottomNavTabItem(
            tab = AppBottomTab.SKILL,
            title = uiText?.tabSkill?.ifBlank { "Skill" } ?: "Skill",
            icon = Icons.Filled.Favorite,
            color = Color(0xFFF43F5E), // 甜心瑰红
            gradient = listOf(Color(0xFFFB7185), Color(0xFFE11D48)),
            petKaomoji = "(˶ᵔ ᵕ ᵔ˶) ♡",
            petVoice = "送你小心心~ 💖"
        ),
        BottomNavTabItem(
            tab = AppBottomTab.TOOLBOX,
            title = uiText?.tabToolbox?.ifBlank { "工具箱" } ?: "工具箱",
            icon = Icons.Filled.Build,
            color = Color(0xFF10B981), // 清新翡翠绿
            gradient = listOf(Color(0xFF34D399), Color(0xFF059669)),
            petKaomoji = "( •̀ ω •́ )✧",
            petVoice = "万能工具变变变! 🍀"
        ),
        BottomNavTabItem(
            tab = AppBottomTab.SETTINGS,
            title = uiText?.tabSettings?.ifBlank { "设置" } ?: "设置",
            icon = Icons.Filled.Settings,
            color = Color(0xFF8B5CF6), // 梦幻丁香紫
            gradient = listOf(Color(0xFFA78BFA), Color(0xFF7C3AED)),
            petKaomoji = "(◕‿◕✿)",
            petVoice = "神秘魔法设置中~ 🔮"
        )
    )

    val themeUi = LocalThemeUiColors.current
    val activeColor = themeUi.primary
    val secondaryColor = themeUi.secondary
    val activeTextColor = if (activeColor.luminance() > 0.62f) Color(0xFF1E293B) else Color.White

    val selectedIndex = navItems.indexOfFirst { it.tab == selectedTab }.coerceAtLeast(0)
    val coroutineScope = rememberCoroutineScope()

    // 1. 液态水滴选中胶囊平滑位置动画（0f..4f）
    val animatedTabPosition by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.64f,
            stiffness = 360f
        ),
        label = "liquid_tab_slide"
    )

    // 2. 点击触发的特效状态
    var burstTabIndex by remember { mutableIntStateOf(selectedIndex) }
    val burstProgress = remember { Animatable(1f) }
    val iconBounceScale = remember { Animatable(1f) }
    val iconWiggleDeg = remember { Animatable(0f) }

    // 3. 持续环境液态玻璃折射流光动画
    val infiniteTransition = rememberInfiniteTransition(label = "bottom_nav_liquid_glass")
    val liquidPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bottom_nav_liquid_phase"
    )

    // 4. 长按跟随手指萌趣交互状态
    var activeHoldingIndex by remember { mutableStateOf<Int?>(null) }
    var holdingFingerOffset by remember { mutableStateOf(Offset.Zero) }
    var isHoldingActive by remember { mutableStateOf(false) }

    // 释放时的彩星爆开粒子特效
    val releaseBurstAnim = remember { Animatable(1f) }
    var lastBurstPos by remember { mutableStateOf(Offset.Zero) }
    var lastBurstColor by remember { mutableStateOf(Color.White) }

    LaunchedEffect(selectedTab) {
        burstTabIndex = selectedIndex
        launch {
            burstProgress.snapTo(0f)
            burstProgress.animateTo(1f, tween(680, easing = FastOutSlowInEasing))
        }
        launch {
            iconBounceScale.snapTo(1f)
            iconBounceScale.animateTo(
                targetValue = 1f,
                animationSpec = keyframes {
                    durationMillis = 520
                    0.82f at 60
                    1.28f at 190
                    0.93f at 320
                    1.08f at 420
                    1.0f at 520
                }
            )
        }
        launch {
            iconWiggleDeg.snapTo(0f)
            iconWiggleDeg.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 480
                    -14f at 95
                    12f at 210
                    -7f at 330
                    4f at 410
                    0f at 480
                }
            )
        }
    }

    val navShape = RoundedCornerShape(22.dp)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("custom_radio_bottom_nav")
    ) {
        val totalWidthPx = constraints.maxWidth.toFloat()
        val slotWidthPx = totalWidthPx / navItems.size.toFloat()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(navShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.52f),
                            Color(0xFFF5F0FF).copy(alpha = 0.38f),
                            Color(0xFFFFEBF3).copy(alpha = 0.40f),
                            Color.White.copy(alpha = 0.56f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 220f)
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.96f),
                            Color.White.copy(alpha = 0.45f),
                            activeColor.copy(alpha = 0.50f),
                            Color.White.copy(alpha = 0.92f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 200f)
                    ),
                    shape = navShape
                )
        ) {
            // 底层 Canvas：绘制液体玻璃折射光斑 + 液态水滴滑动选中胶囊 + 水波纹粒子迸发
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val tabCount = navItems.size
                val slotWidth = w / tabCount

                // A. 环境液态折射光斑
                val orb1X = w * (0.20f + 0.15f * cos(liquidPhase))
                val orb1Y = h * (0.35f + 0.20f * sin(liquidPhase))
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(activeColor.copy(alpha = 0.20f), Color.Transparent),
                        center = Offset(orb1X, orb1Y),
                        radius = w * 0.28f
                    ),
                    center = Offset(orb1X, orb1Y),
                    radius = w * 0.28f
                )

                // B. 液态水滴滑动胶囊
                val pillW = slotWidth * 0.88f
                val pillH = h * 0.78f
                val pillRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
                val pillCenterX = (animatedTabPosition + 0.5f) * slotWidth
                val pillLeft = pillCenterX - pillW / 2f
                val pillTop = (h - pillH) / 2f

                // 选中水滴光晕
                drawRoundRect(
                    brush = Brush.radialGradient(
                        colors = listOf(activeColor.copy(alpha = 0.35f), Color.Transparent),
                        center = Offset(pillCenterX, h / 2f),
                        radius = pillW * 0.75f
                    ),
                    topLeft = Offset(pillLeft - 6.dp.toPx(), pillTop - 4.dp.toPx()),
                    size = Size(pillW + 12.dp.toPx(), pillH + 8.dp.toPx()),
                    cornerRadius = CornerRadius(22.dp.toPx(), 22.dp.toPx())
                )

                // 选中水滴半透玻璃底色
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            activeColor.copy(alpha = 0.88f),
                            secondaryColor.copy(alpha = 0.78f)
                        ),
                        start = Offset(pillLeft, pillTop),
                        end = Offset(pillLeft + pillW, pillTop + pillH)
                    ),
                    topLeft = Offset(pillLeft, pillTop),
                    size = Size(pillW, pillH),
                    cornerRadius = pillRadius
                )

                // 高光高透反光层
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.58f),
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        startY = pillTop,
                        endY = pillTop + pillH * 0.48f
                    ),
                    topLeft = Offset(pillLeft + 4.dp.toPx(), pillTop + 2.dp.toPx()),
                    size = Size((pillW - 8.dp.toPx()).coerceAtLeast(0f), pillH * 0.44f),
                    cornerRadius = CornerRadius(15.dp.toPx(), 15.dp.toPx())
                )

                // 选中高光描边
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.92f),
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.85f)
                        ),
                        start = Offset(pillLeft, pillTop),
                        end = Offset(pillLeft + pillW, pillTop + pillH)
                    ),
                    topLeft = Offset(pillLeft, pillTop),
                    size = Size(pillW, pillH),
                    cornerRadius = pillRadius,
                    style = Stroke(width = 1.2.dp.toPx())
                )

                // C. 点击水波纹与粒子
                val bp = burstProgress.value
                if (bp in 0.001f..0.995f) {
                    val burstCenterX = (burstTabIndex + 0.5f) * slotWidth
                    val burstCenterY = h * 0.48f
                    val fadeAlpha = (1f - bp).coerceIn(0f, 1f)

                    val outerRingRadius = 10.dp.toPx() + bp * (slotWidth * 0.72f)
                    drawCircle(
                        color = Color.White.copy(alpha = fadeAlpha * 0.75f),
                        radius = outerRingRadius,
                        center = Offset(burstCenterX, burstCenterY),
                        style = Stroke(width = (2.6f * (1f - bp * 0.6f)).dp.toPx())
                    )

                    val particleCount = 12
                    for (i in 0 until particleCount) {
                        val angleRad = (i * (360.0 / particleCount) + 15.0) * (Math.PI / 180.0)
                        val speedMultiplier = if (i % 2 == 0) 1.0f else 0.72f
                        val decel = 1f - (1f - bp) * (1f - bp)
                        val dist = (12.dp.toPx() + decel * slotWidth * 0.65f) * speedMultiplier
                        val px = burstCenterX + cos(angleRad).toFloat() * dist
                        val py = burstCenterY + sin(angleRad).toFloat() * dist * 0.78f
                        val pAlpha = (1f - bp * bp).coerceIn(0f, 1f)
                        drawCircle(
                            color = navItems[burstTabIndex.coerceIn(0, 4)].color.copy(alpha = pAlpha * 0.85f),
                            radius = (3.dp.toPx() * (1f - bp * 0.5f)),
                            center = Offset(px, py)
                        )
                    }
                }

                // D. 长按释放时的彩星爆开粒子特效
                val rp = releaseBurstAnim.value
                if (rp in 0.001f..0.99f) {
                    val rx = lastBurstPos.x
                    val ry = lastBurstPos.y
                    val rAlpha = (1f - rp).coerceIn(0f, 1f)
                    for (i in 0 until 14) {
                        val ang = (i * (360.0 / 14) + 12.0) * (Math.PI / 180.0)
                        val dist = (10.dp.toPx() + rp * 45.dp.toPx())
                        val px = rx + cos(ang).toFloat() * dist
                        val py = ry + sin(ang).toFloat() * dist
                        drawCircle(
                            color = lastBurstColor.copy(alpha = rAlpha),
                            radius = (3.5f * (1f - rp * 0.4f)).dp.toPx(),
                            center = Offset(px, py)
                        )
                    }
                }
            }

            // 前景交互层：5 个带鲜明色彩与长按手势监听的 Tab 项
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = selectedTab == item.tab
                    val isBeingHeld = isHoldingActive && activeHoldingIndex == index

                    val pressScale by animateFloatAsState(
                        targetValue = when {
                            isBeingHeld -> 1.15f
                            isSelected -> 1.05f
                            else -> 1.0f
                        },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "tab_scale_$index"
                    )

                    val iconLiftY by animateFloatAsState(
                        targetValue = if (isSelected) -2.dp.value else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "tab_lift_$index"
                    )

                    val itemTextColor by animateColorAsState(
                        targetValue = if (isSelected) activeTextColor else Color(0xFF475569),
                        animationSpec = tween(220),
                        label = "tab_text_color_$index"
                    )

                    val dynamicScale = if (isSelected) pressScale * iconBounceScale.value else pressScale
                    val dynamicRotation = if (isSelected) iconWiggleDeg.value else 0f

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .pointerInput(item.tab) {
                                awaitEachGesture {
                                    val down = awaitFirstDown(requireUnconsumed = false)
                                    val startTime = System.currentTimeMillis()
                                    val startPos = down.position
                                    var isLongPressed = false
                                    var lastPos = startPos
                                    val longPressTimeout = 260L

                                    while (true) {
                                        val event = awaitPointerEvent()
                                        val change = event.changes.firstOrNull { it.id == down.id }
                                        if (change == null || !change.pressed) {
                                            // 松手
                                            if (isLongPressed) {
                                                // 触发释放彩星爆裂动画
                                                val tabCenterX = (index + 0.5f) * slotWidthPx
                                                lastBurstPos = Offset(tabCenterX + holdingFingerOffset.x, 30.dp.toPx() + holdingFingerOffset.y - 65.dp.toPx())
                                                lastBurstColor = item.color
                                                coroutineScope.launch {
                                                    releaseBurstAnim.snapTo(0f)
                                                    releaseBurstAnim.animateTo(1f, tween(480, easing = FastOutSlowInEasing))
                                                }
                                                isHoldingActive = false
                                                activeHoldingIndex = null
                                                holdingFingerOffset = Offset.Zero
                                            } else {
                                                val duration = System.currentTimeMillis() - startTime
                                                val dist = (lastPos - startPos).getDistance()
                                                if (duration < 450L && dist < 35f) {
                                                    burstTabIndex = index
                                                    coroutineScope.launch {
                                                        burstProgress.snapTo(0f)
                                                        burstProgress.animateTo(1f, tween(680, easing = FastOutSlowInEasing))
                                                    }
                                                    coroutineScope.launch {
                                                        iconBounceScale.snapTo(1f)
                                                        iconBounceScale.animateTo(
                                                            targetValue = 1f,
                                                            animationSpec = keyframes {
                                                                durationMillis = 520
                                                                0.82f at 60
                                                                1.28f at 190
                                                                0.93f at 320
                                                                1.08f at 420
                                                                1.0f at 520
                                                            }
                                                        )
                                                    }
                                                    coroutineScope.launch {
                                                        iconWiggleDeg.snapTo(0f)
                                                        iconWiggleDeg.animateTo(
                                                            targetValue = 0f,
                                                            animationSpec = keyframes {
                                                                durationMillis = 480
                                                                -14f at 95
                                                                12f at 210
                                                                -7f at 330
                                                                4f at 410
                                                                0f at 480
                                                            }
                                                        )
                                                    }
                                                    onTabSelected(item.tab)
                                                }
                                            }
                                            break
                                        }

                                        lastPos = change.position
                                        val elapsed = System.currentTimeMillis() - startTime

                                        if (!isLongPressed) {
                                            val dist = (lastPos - startPos).getDistance()
                                            if (elapsed >= longPressTimeout) {
                                                isLongPressed = true
                                                isHoldingActive = true
                                                activeHoldingIndex = index
                                                change.consume()
                                                holdingFingerOffset = lastPos - startPos
                                            } else if (dist > 45f) {
                                                // 快速划过，放弃长按
                                                break
                                            }
                                        } else {
                                            change.consume()
                                            holdingFingerOffset = lastPos - startPos
                                        }
                                    }
                                }
                            }
                            .padding(vertical = 3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 绿色箭头需求：图标采用饱满鲜艳的颜色呈现，配以圆形轻彩晕背景
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .offset(y = iconLiftY.dp)
                                .graphicsLayer {
                                    scaleX = dynamicScale
                                    scaleY = dynamicScale
                                    rotationZ = dynamicRotation
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) {
                                            Brush.radialGradient(
                                                listOf(
                                                    item.color.copy(alpha = 0.32f),
                                                    item.color.copy(alpha = 0.10f)
                                                )
                                            )
                                        } else {
                                            Brush.radialGradient(
                                                listOf(
                                                    item.color.copy(alpha = 0.18f),
                                                    item.color.copy(alpha = 0.05f)
                                                )
                                            )
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 1.4.dp else 0.8.dp,
                                        brush = if (isSelected) {
                                            Brush.linearGradient(item.gradient)
                                        } else {
                                            Brush.linearGradient(
                                                listOf(
                                                    item.color.copy(alpha = 0.45f),
                                                    item.color.copy(alpha = 0.20f)
                                                )
                                            )
                                        },
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) {
                                        if (activeColor.luminance() > 0.65f) item.color else Color.White
                                    } else {
                                        item.color
                                    },
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.title,
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                            color = itemTextColor
                        )

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(2.dp))
                            // 底部彩色微型流光指示胶囊
                            Box(
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(1.5.dp))
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.95f),
                                                item.color,
                                                Color.White.copy(alpha = 0.95f)
                                            )
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }

        // 底部Tab长按跟随手指特效（全屏悬浮 Popup，液体玻璃呈现 + 文字向上浮动动效 + 防遮挡居中优化）
        if (isHoldingActive && activeHoldingIndex != null) {
            val holdingItem = navItems[activeHoldingIndex!!]
            val density = LocalDensity.current

            val tabCenterX = (activeHoldingIndex!! + 0.5f) * slotWidthPx
            val tabCenterY = with(density) { 30.dp.toPx() }

            // 优化：文字向上抬高至 -105.dp，横向居中偏移 -85.dp，确保手指完全不遮挡文字气泡与光球
            val targetPxX = tabCenterX + holdingFingerOffset.x - with(density) { 85.dp.toPx() }
            val targetPxY = tabCenterY + holdingFingerOffset.y - with(density) { 105.dp.toPx() }

            Popup(
                offset = IntOffset(targetPxX.roundToInt(), targetPxY.roundToInt()),
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                CuteFingerFollower(
                    item = holdingItem,
                    fingerOffset = holdingFingerOffset
                )
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
