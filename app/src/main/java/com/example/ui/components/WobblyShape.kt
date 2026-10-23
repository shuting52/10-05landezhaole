package com.example.ui.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * 2026-10-23：手绘抖动圆角（15套主题"原原本本"——手绘风/手绘涂鸦的歪扭描边签名）。
 * 扰动由 seed 确定性生成，同一卡片每次绘制形状一致，不会闪烁。
 */
class WobblyRoundedShape(
    private val seed: Int,
    private val radius: Dp = 24.dp,
    private val wobble: Dp = 4.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val r = with(density) { radius.toPx() }
        val w = with(density) { wobble.toPx() }
        return Outline.Generic(wobblyRoundRectPath(size, r, w, seed))
    }
}

/**
 * 抖动手绘圆角矩形路径：四条边微弓、四角圆弧，控制点带确定性扰动。
 * seed 相同则路径相同。
 */
fun wobblyRoundRectPath(size: Size, radiusPx: Float, wobblePx: Float, seed: Int): Path {
    // 确定性伪随机：i -> [-wobblePx, wobblePx]
    fun j(i: Int): Float {
        var h = seed * 31 + i * 7919
        h = h xor (h shl 13)
        h = h xor (h ushr 17)
        h = h xor (h shl 5)
        val v = (h and 0x7FFF).toFloat() / 0x7FFF.toFloat()
        return (v * 2f - 1f) * wobblePx
    }
    val w = size.width
    val h = size.height
    val r = radiusPx
    return Path().apply {
        // 起点：上边左端
        moveTo(r + j(0), j(1))
        // 上边（微弓）→ 右上
        quadraticBezierTo(w / 2 + j(2), j(3), w - r + j(4), j(5))
        // 右上圆角 → 右边
        quadraticBezierTo(w + j(6), j(7), w + j(8), r + j(9))
        // 右边（微弓）→ 右下
        quadraticBezierTo(w + j(10), h / 2 + j(11), w + j(12), h - r + j(13))
        // 右下圆角 → 下边
        quadraticBezierTo(w + j(14), h + j(15), w - r + j(16), h + j(17))
        // 下边（微弓）→ 左下
        quadraticBezierTo(w / 2 + j(18), h + j(19), r + j(20), h + j(21))
        // 左下圆角 → 左边
        quadraticBezierTo(j(22), h + j(23), j(24), h - r + j(25))
        // 左边（微弓）→ 左上
        quadraticBezierTo(j(26), h / 2 + j(27), j(28), r + j(29))
        // 左上圆角 → 起点
        quadraticBezierTo(j(30), j(31), r + j(32), j(33))
        close()
    }
}
