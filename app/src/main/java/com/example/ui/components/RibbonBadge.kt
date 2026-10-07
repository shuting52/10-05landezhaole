package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeType
import com.example.ui.theme.BadgeBlue1
import com.example.ui.theme.BadgeBlue2
import com.example.ui.theme.BadgeEmerald1
import com.example.ui.theme.BadgeEmerald2
import com.example.ui.theme.BadgeGold1
import com.example.ui.theme.BadgeGold2
import com.example.ui.theme.BadgeRose1
import com.example.ui.theme.BadgeRose2

import androidx.compose.ui.text.style.TextOverflow

/**
 * Intelligent sanitizer for badge text:
 * Prevents long badges from breaking card layout by compressing or abbreviating
 * long text while preserving semantic punchiness.
 */
fun sanitizeBadgeText(raw: String): String {
    val trimmed = raw.trim()
    if (trimmed.length <= 3) return trimmed
    return when {
        trimmed.equals("Agentic IDE", ignoreCase = true) -> "Agent"
        trimmed.equals("浏览器容器", ignoreCase = true) -> "容器"
        trimmed.equals("超长上下文", ignoreCase = true) -> "超长"
        trimmed.equals("智能体开发平台", ignoreCase = true) -> "Agent"
        trimmed.equals("智能体平台", ignoreCase = true) -> "Agent"
        trimmed.equals("低代码平台", ignoreCase = true) -> "低代码"
        trimmed.equals("AI开发者", ignoreCase = true) -> "开发"
        trimmed.equals("开源Agent", ignoreCase = true) -> "开源"
        trimmed.equals("DevTools", ignoreCase = true) -> "Dev"
        trimmed.equals("全栈构建", ignoreCase = true) -> "全栈"
        trimmed.equals("讯飞星火", ignoreCase = true) -> "星火"
        trimmed.equals("豆包驱动", ignoreCase = true) -> "豆包"
        trimmed.equals("超大仓库", ignoreCase = true) -> "仓库"
        trimmed.equals("趣味编程", ignoreCase = true) -> "趣味"
        trimmed.equals("解压捏捏", ignoreCase = true) -> "解压"
        trimmed.equals("国内免梯", ignoreCase = true) -> "直连"
        trimmed.equals("学术论文", ignoreCase = true) -> "学术"
        trimmed.length > 4 -> trimmed.take(3)
        else -> trimmed
    }
}

/**
 * Modern CSS-inspired Ribbon Badge with animations:
 * - Dynamic infinite transition mimicking CSS keyframes (@keyframes shimmer & pulse)
 * - Shimmer sweep light bar effect (CSS background-position sweep)
 * - Breath / pulse scale effect for NEW and HOT badges
 * - Glowing radar pulse dot for NEW badges
 * - Strictly width-constrained (max 54dp) and auto-ellipsized to prevent overflow
 */
@Composable
fun RibbonBadge(
    text: String,
    badgeType: BadgeType = BadgeType.ROSE,
    modifier: Modifier = Modifier
) {
    val displayText = sanitizeBadgeText(text).let {
        // v1.1.11：非标准角标（GitHub/OpenAI/腾讯出品 等描述性文字）统一显示为「HOT」，
        // 只有 NEW / HOT 标准角标保留原文，简洁统一呈现
        if (it.equals("NEW", true) || it.equals("HOT", true)) it else "HOT"
    }
    val isNewOrHot = badgeType == BadgeType.NEW || badgeType == BadgeType.ROSE || badgeType == BadgeType.GOLD
    val isNew = badgeType == BadgeType.NEW || text.contains("NEW", ignoreCase = true) || text.contains("新", ignoreCase = true)

    // Infinite transition for CSS-style keyframe animations
    val infiniteTransition = rememberInfiniteTransition(label = "ribbon_badge_anim")

    // CSS Keyframe 1: Subtle breathing pulse scale (1.0f -> 1.05f -> 1.0f)
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isNewOrHot) 1.05f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badge_scale"
    )

    // CSS Keyframe 2: Dynamic angle / shimmer light sweep (CSS linear-gradient sliding effect)
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -1.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isNew) 1800 else 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    // CSS Keyframe 3: Glowing radar dot alpha/pulse for NEW
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    // CSS Keyframe 4: 渐变方向 360° 旋转 (linear-gradient angle 动画)
    val gradAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isNew) 2600 else 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "grad_angle"
    )

    // CSS Keyframe 5: 外层光晕呼吸 (glow pulse)
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = if (isNew) 0.9f else 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    // CSS Keyframe 6: 弹跳摇摆 (bounce wobble)
    val wobble by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobble"
    )

    // 国庆潮流国潮风：角标升级为盛世朱红与鎏金渐变小胶囊
    val baseGradientColors = listOf(Color(0xFFDE2910), Color(0xFFFF3D00), Color(0xFFFF8C00))

    val baseBrush = Brush.linearGradient(baseGradientColors)
    // 全圆角胶囊形状（统一化）
    val badgeShape = RoundedCornerShape(50)

    Box(
        modifier = modifier
            .scale(scale)
            .graphicsLayer { rotationZ = if (isNewOrHot) wobble else 0f }
            .shadow(
                elevation = if (isNew) 3.dp else 1.5.dp,
                shape = badgeShape,
                ambientColor = baseGradientColors.first().copy(alpha = glowAlpha),
                spotColor = baseGradientColors.first().copy(alpha = glowAlpha)
            )
            .clip(badgeShape)
            .border(
                width = 0.9.dp,
                color = Color(0xFFFFD700).copy(alpha = if (isNew) 0.95f else 0.70f),
                shape = badgeShape
            )
            .height(17.dp)
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        // CSS 动态角度渐变背景 + 流光扫描（Canvas 全背景绘制）
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val cX = w / 2f
            val cY = h / 2f
            val len = kotlin.math.hypot(w.toDouble(), h.toDouble()).toFloat() / 2f
            val rad = Math.toRadians(gradAngle.toDouble())
            val cosA = kotlin.math.cos(rad).toFloat()
            val sinA = kotlin.math.sin(rad).toFloat()

            // 1) 动态旋转渐变背景（CSS linear-gradient angle）
            drawRect(
                brush = Brush.linearGradient(
                    colors = baseGradientColors,
                    start = Offset(cX - cosA * len, cY - sinA * len),
                    end = Offset(cX + cosA * len, cY + sinA * len)
                )
            )

            // 2) 流光扫过（shimmer sweep）
            val currentX = w * shimmerOffset
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.30f),
                        Color.White.copy(alpha = 0.65f),
                        Color.White.copy(alpha = 0.30f),
                        Color.Transparent
                    ),
                    start = Offset(currentX - w * 0.5f, 0f),
                    end = Offset(currentX + w * 0.5f, h)
                )
            )

            // 3) 上下边缘高光描边
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (isNew) 0.75f else 0.4f),
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.18f)
                    )
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pulsing live radar indicator dot for NEW sites
            if (isNew) {
                Box(
                    modifier = Modifier
                        .size(3.5.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = dotAlpha))
                )
                Spacer(modifier = Modifier.width(2.5.dp))
            }

            Text(
                text = displayText,
                color = Color.White,
                fontSize = if (displayText.length >= 4) 8.5.sp else 9.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = if (displayText.length >= 4) (-0.3).sp else 0.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
