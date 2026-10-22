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
import com.example.ui.theme.DesignStyle
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
    // v1.4.13：图4风格重写 —— 白色圆角容器 + 黄色高亮选中 pill（主题跟随）
    val themeColors = com.example.ui.theme.LocalThemeUiColors.current
    // v1.5.8：圆角跟 DesignStyle 走，手绘风用 24dp 大圆角
    // v1.6.2：卡通风格用 28.dp（更新弹窗同款）
    val navCorner = when (themeColors.designStyle) {
        DesignStyle.HAND_DRAWN -> 24.dp
        DesignStyle.CARTOON -> 28.dp
        DesignStyle.LIQUID_GLASS -> 28.dp
        else -> 18.dp
    }
    val navPillCorner = when (themeColors.designStyle) {
        DesignStyle.HAND_DRAWN -> 16.dp
        DesignStyle.CARTOON -> 20.dp
        DesignStyle.LIQUID_GLASS -> 18.dp
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
            tab = AppBottomTab.SKILL,
            title = if (uiText?.tabSkill.isNullOrBlank() || uiText?.tabSkill == "资源" || uiText?.tabSkill == "懒得KunBox" || uiText?.tabSkill == "懒得连了" || uiText?.tabSkill == "AI前言" || uiText?.tabSkill == "DownVid") "去水印" else uiText!!.tabSkill,
            icon = Icons.Filled.PlayArrow,
            color = themeColors.primary,
            gradient = listOf(themeColors.primary, themeColors.secondary),
            petKaomoji = "(◕‿◕)b",
            petVoice = "全网无水印视频一键解析下载喵~ 🎬"
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

    val isLiquidGlass = themeColors.designStyle == DesignStyle.LIQUID_GLASS
    val highlightColor = themeColors.secondary
    val selectedTextColor = themeColors.text
    val unselectedColor = if (isLiquidGlass) themeColors.textMuted.copy(alpha = 0.82f)
        else themeColors.text.copy(alpha = 0.45f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            // v1.4.19：系统导航栏安全边距，避免底部 Tab 与手机导航键重叠
            .navigationBarsPadding()
            // v1.4.20：整体缩小一号
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .shadow(6.dp, RoundedCornerShape(navCorner), ambientColor = Color.Black.copy(alpha = 0.08f))
            .clip(RoundedCornerShape(navCorner))
            .background(
                if (isLiquidGlass) {
                    Brush.linearGradient(
                        listOf(
                            themeColors.surface.copy(alpha = 0.82f),
                            Color(0xB71A1B29),
                            Color(0xC51A1723)
                        )
                    )
                } else {
                    Brush.linearGradient(listOf(themeColors.surface, themeColors.surface))
                }
            )
            .border(
                1.dp,
                if (isLiquidGlass) {
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.62f),
                            themeColors.secondary.copy(alpha = 0.54f),
                            themeColors.accent.copy(alpha = 0.52f),
                            Color.White.copy(alpha = 0.34f)
                        )
                    )
                } else {
                    Brush.linearGradient(listOf(themeColors.primary.copy(alpha = 0.18f), themeColors.primary.copy(alpha = 0.18f)))
                },
                RoundedCornerShape(navCorner)
            )
            .padding(horizontal = 6.dp, vertical = 5.dp)
    ) {
        if (isLiquidGlass) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, Color.White.copy(alpha = 0.58f), themeColors.secondary.copy(alpha = 0.48f), Color.Transparent)
                        )
                    )
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = selectedTab == item.tab
                // 选中态背景色动画
                val pillColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        if (isLiquidGlass) highlightColor.copy(alpha = 0.38f) else highlightColor
                    } else Color.Transparent,
                    animationSpec = tween(250),
                    label = "tabPill"
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(navPillCorner))
                        .background(pillColor)
                        .clickable { onTabSelected(item.tab) }
                        .padding(vertical = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) selectedTextColor else unselectedColor,
                        modifier = Modifier.size(18.dp)
                    )
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
