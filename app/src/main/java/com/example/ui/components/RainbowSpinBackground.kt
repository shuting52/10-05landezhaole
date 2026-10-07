package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

/**
 * v1.1.17：彩虹旋转渐变卡片背景（还原用户提供的 conic-gradient 旋转 CSS 效果）：
 * - 12 色全彩 sweepGradient（等价 CSS conic-gradient）
 * - 4 秒线性无限旋转（等价 @keyframes speeen）
 * - 8dp 高斯模糊（等价 .uwu { filter: blur(8px) }）
 * - 尺寸放大 150% 并居中偏移，旋转时无边缘露出（等价 min-width/min-height:150% + translate(-50%,-50%)）
 * 用法：放在卡片内容 Box 的最底层，卡片表面用半透明白色盖住，文字即可呈现「彩虹在文字背后」效果。
 * 仅应用于首页 / 软件版块 / Skill 技能库卡片，其它界面不调用。
 */
@Composable
fun RainbowSpinBackground(
    modifier: Modifier = Modifier,
    blurRadius: androidx.compose.ui.unit.Dp = 8.dp,
    spinMs: Int = 4000
) {
    // 12 色（等价 CSS hsl 序列：红→橙→红紫→黄绿→绿→青→天蓝→靛→紫→品红→粉红）
    val rainbowColors = listOf(
        Color(0xFFFF0000), // hsl(0,100%,50%)
        Color(0xFFFF8000), // hsl(30,100%,50%)
        Color(0xFFC2004A), // hsl(330,100%,38%)
        Color(0xFF80FF00), // hsl(90,100%,50%)
        Color(0xFF00FF00), // hsl(120,100%,50%)
        Color(0xFF00FF80), // hsl(150,100%,50%)
        Color(0xFF00FFFF), // hsl(180,100%,50%)
        Color(0xFF0080FF), // hsl(210,100%,50%)
        Color(0xFF3333FF), // hsl(240,100%,60%)
        Color(0xFF8000FF), // hsl(270,100%,50%)
        Color(0xFFFF00FF), // hsl(300,100%,50%)
        Color(0xFFFF0080)  // hsl(330,100%,50%)
    )
    val transition = rememberInfiniteTransition(label = "rainbow_spin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(spinMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rainbow_angle"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .blur(blurRadius)
    ) {
        // 旋转的 sweepGradient（尺寸 1.5 倍 + 居中偏移，保证旋转无角落露出）
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val side = size.maxDimension * 1.5f
            rotate(degrees = angle) {
                drawRect(
                    brush = Brush.sweepGradient(
                        colors = rainbowColors,
                        center = Offset(size.width / 2f, size.height / 2f)
                    ),
                    topLeft = Offset(
                        (size.width - side) / 2f,
                        (size.height - side) / 2f
                    ),
                    size = androidx.compose.ui.geometry.Size(side, side)
                )
            }
        }
    }
}
