package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ============================================================
// Liquid Glass · 液态玻璃四步法（用户 2026-10-22 亲授）
// 关键在于模拟真实玻璃的物理特性：折射、高光和厚度感。
//
// 步骤1 基础模糊与增色：半透明白色图层打底（背景本身柔和渐变即产生虚化景深感），
//         再叠一层同色系高饱和色，用 Overlay 混合模式与背后的背景真实混合增色。
// 步骤2 叠加白色提升通透感：纯白 10% 叠加，瞬间提升亮度与通透感。
// 步骤3 内阴影/高光塑造厚度：顶部白色内高光（模拟 Y+2/blurless 4 的白色内阴影）+
//         1px 白色内描边 + 底部内阴影，共同塑出玻璃厚度。
// 步骤4 边缘折射与色散：边缘 sweep 渐变描边模拟折射扭曲带出的彩色；
//         红/蓝两道发丝描边微错位，模拟不易察觉的色散色差。
// ============================================================

/**
 * 液态玻璃卡片修饰符。所有绘制都在同一 drawWithContent 内按“由底到顶”执行，
 * 对应四步法；BlendMode.Overlay 会与卡片背后的背景真实混合，达到增色目的。
 *
 * @param cornerRadius 卡片圆角（与外部 clip 保持一致）
 * @param baseAlpha 步骤1：半透明白底不透明度
 * @param tint 步骤1：增色（同色系饱和色）
 * @param tintAlpha 增色层不透明度
 * @param whiteOverlayAlpha 步骤2：叠白不透明度（默认 10%）
 * @param refractionColors 步骤4：边缘折射色带（sweep 渐变一周）
 * @param dispersionAlpha 步骤4：红蓝色散描边不透明度（保持微妙）
 */
fun Modifier.liquidGlass(
    cornerRadius: Dp = 28.dp,
    baseAlpha: Float = 0.12f,
    tint: Color = Color.White,
    tintAlpha: Float = 0.55f,
    whiteOverlayAlpha: Float = 0.10f,
    refractionColors: List<Color> = listOf(
        Color.White.copy(alpha = 0.85f),
        Color(0xFF7DE8E0).copy(alpha = 0.45f),
        Color.White.copy(alpha = 0.15f),
        Color(0xFFFF7A93).copy(alpha = 0.45f),
        Color(0xFFFFC37E).copy(alpha = 0.35f),
        Color.White.copy(alpha = 0.85f)
    ),
    dispersionAlpha: Float = 0.10f
): Modifier = this.drawWithContent {
    val cr = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
    val w = size.width
    val h = size.height

    // ---- 步骤1：半透明白底（玻璃基底） ----
    drawRoundRect(
        color = Color.White.copy(alpha = baseAlpha),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = cr,
        style = Fill
    )
    // ---- 步骤1：增色 —— 饱和色以 Overlay 混合与背后背景真实混合 ----
    drawRoundRect(
        color = tint.copy(alpha = tintAlpha),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = cr,
        style = Fill,
        blendMode = BlendMode.Overlay
    )
    // ---- 步骤2：叠白 10%（通透感来源） ----
    drawRoundRect(
        color = Color.White.copy(alpha = whiteOverlayAlpha),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = cr,
        style = Fill
    )

    // 卡片内容（图标/文字）在玻璃层之上绘制
    drawContent()

    // ---- 步骤3：顶部内高光（模拟白色内阴影 Y+2 / blur 4 / Overlay）----
    val highlightH = (h * 0.38f).coerceAtLeast(14.dp.toPx())
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.38f), Color.Transparent),
            startY = 0f,
            endY = highlightH
        ),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = cr,
        style = Fill
    )
    // ---- 步骤3：底部内阴影（厚度感） ----
    val shadowH = (h * 0.30f).coerceAtLeast(10.dp.toPx())
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.16f)),
            startY = h - shadowH,
            endY = h
        ),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = cr,
        style = Fill
    )
    // ---- 斜向镜面高光带（液态玻璃标志性的一笔） ----
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.16f),
                Color.Transparent
            ),
            start = Offset(0f, 0f),
            end = Offset(w * 0.55f, h * 0.55f)
        ),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = cr,
        style = Fill
    )

    // ---- 步骤3：1px 白色内描边（精致边缘） ----
    val strokePx = 1.dp.toPx()
    drawRoundRect(
        color = Color.White.copy(alpha = 0.55f),
        topLeft = Offset(strokePx / 2f, strokePx / 2f),
        size = androidx.compose.ui.geometry.Size(w - strokePx, h - strokePx),
        cornerRadius = CornerRadius((cornerRadius.toPx() - strokePx / 2f).coerceAtLeast(0f), (cornerRadius.toPx() - strokePx / 2f).coerceAtLeast(0f)),
        style = Stroke(width = strokePx)
    )

    // ---- 步骤4：边缘折射 —— sweep 彩色描边模拟光线扭曲 ----
    drawRoundRect(
        brush = Brush.sweepGradient(refractionColors),
        topLeft = Offset.Zero,
        size = size,
        cornerRadius = cr,
        style = Stroke(width = 1.4.dp.toPx())
    )
    // ---- 步骤4：色散 —— 红/蓝发丝描边微错位（保持不易察觉） ----
    val dispPx = 0.8.dp.toPx()
    drawRoundRect(
        color = Color(0xFFFF4D5E).copy(alpha = dispersionAlpha),
        topLeft = Offset(-dispPx / 2f, -dispPx / 2f),
        size = androidx.compose.ui.geometry.Size(w + dispPx, h + dispPx),
        cornerRadius = CornerRadius(cr.x + dispPx / 2f, cr.y + dispPx / 2f),
        style = Stroke(width = dispPx)
    )
    drawRoundRect(
        color = Color(0xFF4DA6FF).copy(alpha = dispersionAlpha),
        topLeft = Offset(dispPx / 2f, dispPx / 2f),
        size = androidx.compose.ui.geometry.Size(w - dispPx, h - dispPx),
        cornerRadius = CornerRadius((cr.x - dispPx / 2f).coerceAtLeast(0f), (cr.y - dispPx / 2f).coerceAtLeast(0f)),
        style = Stroke(width = dispPx)
    )
}

/** 液态玻璃主题默认圆角（与卡片形状保持一致） */
val LiquidGlassCardShape = RoundedCornerShape(28.dp)
