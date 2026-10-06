package com.example.ui.components

import android.graphics.Matrix
import android.graphics.SweepGradient
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 全局共享的边框流光角度提供器（0°..360°）
 * 通过顶级单一动画驱动，避免每个卡片独立启动 Animator，全应用百张卡片流畅稳定 60/120fps
 */
val LocalStreamingBorderAngle = compositionLocalOf { 0f }

/**
 * 围绕卡片边框循环跑动的流光动效修饰符（Streaming Border Light Beam）：
 *
 * 视觉特征：
 * 1. 【激光流光核心】：由白炽高光核（White Core）与主体霓虹色（Accent Color）构成的强聚集光束，
 *    紧贴卡片圆角轮廓做 360 度周游环跑。
 * 2. 【彗星渐变拖尾】：流光头部锐利炽烈，后方拖出柔和透明的渐变光尾（Tapering Tail）。
 * 3. 【泛光光晕渲染】：外扩双层描边（Double-Pass Stroke），内层精准锐利、外层柔光扩散（Glow Halo）。
 * 4. 【半透基准底边】：底层保留细腻的高级半透边框，流光未经过处仍保证卡片立体边界感。
 */
@Composable
fun Modifier.streamingBorder(
    cornerRadius: Dp = 18.dp,
    strokeWidth: Dp = 1.6.dp,
    glowWidth: Dp = 3.6.dp,
    accentColor: Color = Color(0xFF38BDF8),
    tailColor: Color = accentColor.copy(alpha = 0.45f),
    baseBorderColor: Color = Color.White.copy(alpha = 0.38f),
    phaseOffset: Float = 0f,
    showGlow: Boolean = true,
    rainbow: Boolean = true
): Modifier {
    val masterAngle = LocalStreamingBorderAngle.current
    val angle = (masterAngle + phaseOffset) % 360f

    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val glowWidthPx = with(density) { glowWidth.toPx() }
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }

    val accentArgb = accentColor.toArgb()
    val tailArgb = tailColor.toArgb()

    return this.drawWithContent {
        drawContent()

        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@drawWithContent

        val cx = w / 2f
        val cy = h / 2f
        val maxRadius = minOf(w, h) / 2f
        val r = cornerRadiusPx.coerceIn(0f, maxRadius)

        val halfStroke = strokeWidthPx / 2f
        val rectTopLeft = Offset(halfStroke, halfStroke)
        val rectSize = Size(w - strokeWidthPx, h - strokeWidthPx)
        val rectCorner = CornerRadius(r, r)

        // 1. 底层微光基础描边（保证卡片在流光未扫过区域有高品质轮廓）
        if (baseBorderColor.alpha > 0f) {
            drawRoundRect(
                color = baseBorderColor,
                topLeft = rectTopLeft,
                size = rectSize,
                cornerRadius = rectCorner,
                style = Stroke(width = strokeWidthPx)
            )
        }

        // 2. 围绕边框跑动的流光 Shader
        val (colors, positions) = if (rainbow) {
            // 七彩多巴胺全光谱霓虹流光（红、橙、黄、绿、青、蓝、紫、粉 + 白炽激光头）
            intArrayOf(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.rgb(236, 72, 153),  // 粉红
                android.graphics.Color.rgb(168, 85, 247),  // 紫
                android.graphics.Color.rgb(59, 130, 246),  // 蓝
                android.graphics.Color.rgb(6, 182, 212),   // 青
                android.graphics.Color.rgb(16, 185, 129),  // 绿
                android.graphics.Color.rgb(234, 179, 8),   // 黄
                android.graphics.Color.rgb(249, 115, 22),  // 橙
                android.graphics.Color.rgb(239, 68, 68),   // 红
                android.graphics.Color.WHITE,              // 激光白芯
                android.graphics.Color.TRANSPARENT
            ) to floatArrayOf(
                0.0f, 0.38f, 0.52f, 0.63f, 0.72f, 0.80f, 0.86f, 0.91f, 0.95f, 0.975f, 0.99f, 1.0f
            )
        } else {
            // 荧光单色 / 彗星拖尾流光
            intArrayOf(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
                tailArgb,
                accentArgb,
                android.graphics.Color.WHITE,
                accentArgb,
                android.graphics.Color.TRANSPARENT
            ) to floatArrayOf(
                0.0f, 0.60f, 0.78f, 0.90f, 0.965f, 0.988f, 1.0f
            )
        }

        val sweepShader = SweepGradient(cx, cy, colors, positions)
        val matrix = Matrix()
        matrix.postRotate(angle, cx, cy)
        sweepShader.setLocalMatrix(matrix)

        val sweepBrush = object : ShaderBrush() {
            override fun createShader(size: Size): Shader = sweepShader
        }

        // 3. 柔光光晕层（Double-Pass Stroke 外层光晕，营造空间立体悬浮感）
        if (showGlow) {
            drawRoundRect(
                brush = sweepBrush,
                topLeft = rectTopLeft,
                size = rectSize,
                cornerRadius = rectCorner,
                style = Stroke(width = glowWidthPx),
                alpha = 0.40f
            )
        }

        // 4. 核心锐利流光激光层（Sharp Core Beam）
        drawRoundRect(
            brush = sweepBrush,
            topLeft = rectTopLeft,
            size = rectSize,
            cornerRadius = rectCorner,
            style = Stroke(width = strokeWidthPx)
        )
    }
}

/**
 * 带有边框流光跑动效果的通用卡片容器（StreamingBorderCard）
 */
@Composable
fun StreamingBorderCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    strokeWidth: Dp = 1.6.dp,
    glowWidth: Dp = 3.6.dp,
    accentColor: Color = Color(0xFF38BDF8),
    tailColor: Color = accentColor.copy(alpha = 0.45f),
    baseBorderColor: Color = Color.White.copy(alpha = 0.35f),
    phaseOffset: Float = 0f,
    showGlow: Boolean = true,
    rainbow: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .streamingBorder(
                cornerRadius = cornerRadius,
                strokeWidth = strokeWidth,
                glowWidth = glowWidth,
                accentColor = accentColor,
                tailColor = tailColor,
                baseBorderColor = baseBorderColor,
                phaseOffset = phaseOffset,
                showGlow = showGlow,
                rainbow = rainbow
            ),
        content = content
    )
}
