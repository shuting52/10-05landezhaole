package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * 象限卡片位置定义：
 * TOP_LEFT: 左上角圆角 50dp，其余 6dp (对应 CSS 90px 5px 5px 5px)
 * TOP_RIGHT: 右上角圆角 50dp，其余 6dp (对应 CSS 5px 90px 5px 5px)
 * BOTTOM_LEFT: 左下角圆角 50dp，其余 6dp (对应 CSS 5px 5px 5px 90px)
 * BOTTOM_RIGHT: 右下角圆角 50dp，其余 6dp (对应 CSS 5px 5px 90px 5px)
 */
enum class QuadrantPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
}

/** 单个象限卡片的数据模型 */
data class QuadrantCardItem(
    val title: String,
    val position: QuadrantPosition,
    val defaultBgColor: Color = Color.White,
    val activeBgColor: Color,
    val defaultIconColor: Color,
    val spotShadowColor: Color,
    val iconDrawer: @Composable (Color) -> Unit,
    val onClick: () -> Unit
)

/**
 * 通用 2x2 四象限异形四叶草卡片矩阵
 */
@Composable
fun QuadrantCardGrid(
    items: List<QuadrantCardItem>,
    modifier: Modifier = Modifier
) {
    if (items.size < 4) return
    val topLeft = items.firstOrNull { it.position == QuadrantPosition.TOP_LEFT } ?: items[0]
    val topRight = items.firstOrNull { it.position == QuadrantPosition.TOP_RIGHT } ?: items[1]
    val bottomLeft = items.firstOrNull { it.position == QuadrantPosition.BOTTOM_LEFT } ?: items[2]
    val bottomRight = items.firstOrNull { it.position == QuadrantPosition.BOTTOM_RIGHT } ?: items[3]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 上半区 .up
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuadrantSingleCard(item = topLeft)
                QuadrantSingleCard(item = topRight)
            }
            // 下半区 .down
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuadrantSingleCard(item = bottomLeft)
                QuadrantSingleCard(item = bottomRight)
            }
        }
    }
}

/**
 * 单个异形象限卡片实现（液体玻璃状 Liquid Glass 材质呈现）
 */
@Composable
private fun QuadrantSingleCard(item: QuadrantCardItem) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val shape = when (item.position) {
        QuadrantPosition.TOP_LEFT -> RoundedCornerShape(topStart = 50.dp, topEnd = 6.dp, bottomEnd = 6.dp, bottomStart = 6.dp)
        QuadrantPosition.TOP_RIGHT -> RoundedCornerShape(topStart = 6.dp, topEnd = 50.dp, bottomEnd = 6.dp, bottomStart = 6.dp)
        QuadrantPosition.BOTTOM_LEFT -> RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomEnd = 6.dp, bottomStart = 50.dp)
        QuadrantPosition.BOTTOM_RIGHT -> RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomEnd = 50.dp, bottomStart = 6.dp)
    }

    val iconOffset = when (item.position) {
        QuadrantPosition.TOP_LEFT -> Offset(6.dp.value, 4.dp.value)
        QuadrantPosition.TOP_RIGHT -> Offset(-6.dp.value, 4.dp.value)
        QuadrantPosition.BOTTOM_LEFT -> Offset(6.dp.value, -4.dp.value)
        QuadrantPosition.BOTTOM_RIGHT -> Offset(-6.dp.value, -4.dp.value)
    }

    // 0.2s ease-in-out 缩放动效（对应 CSS: transition: .2s ease-in-out; scale: 1.1）
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.10f else 1.0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "quadrantScale"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isPressed) Color.White else item.defaultIconColor,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "quadrantIconColor"
    )

    // 液态玻璃呼吸折射相位
    val infiniteTransition = rememberInfiniteTransition(label = "quadrant_liquid_glass")
    val liquidPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "quadrant_liquid_phase"
    )
    val phaseShift = remember(item.position, item.title) {
        (item.position.ordinal * 1.45f) + (item.title.hashCode() % 7) * 0.3f
    }

    val glassBackgroundBrush = if (isPressed) {
        Brush.linearGradient(
            colors = listOf(
                item.activeBgColor.copy(alpha = 0.90f),
                item.activeBgColor.copy(alpha = 0.78f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.44f),
                Color(0xFFF6F0FF).copy(alpha = 0.26f),
                Color(0xFFFFEDF5).copy(alpha = 0.32f),
                Color.White.copy(alpha = 0.48f)
            ),
            start = Offset(0f, 0f),
            end = Offset(260f, 260f)
        )
    }

    val glassBorderBrush = if (isPressed) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                item.activeBgColor.copy(alpha = 0.85f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                Color.White.copy(alpha = 0.42f),
                item.defaultIconColor.copy(alpha = 0.32f),
                Color.White.copy(alpha = 0.90f)
            ),
            start = Offset(0f, 0f),
            end = Offset(260f, 260f)
        )
    }

    Box(
        modifier = Modifier
            .size(94.dp)
            .scale(scale)
            .clip(shape)
            .background(brush = glassBackgroundBrush)
            .border(
                width = 1.4.dp,
                brush = glassBorderBrush,
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = item.onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // 液态玻璃折射光球与曲面镜面高光层
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height

            // 1. 游走液态光斑（带图标专属色彩微折射）
            val b1x = w * (0.28f + 0.18f * cos(liquidPhase + phaseShift))
            val b1y = h * (0.28f + 0.16f * sin(liquidPhase + phaseShift))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        if (isPressed) Color.White.copy(alpha = 0.28f)
                        else item.defaultIconColor.copy(alpha = 0.18f),
                        Color.Transparent
                    ),
                    center = Offset(b1x, b1y),
                    radius = w * 0.62f
                ),
                center = Offset(b1x, b1y),
                radius = w * 0.62f
            )

            // 2. 第二游走冰晶折射光斑
            val b2x = w * (0.72f + 0.15f * sin(liquidPhase * 0.85f + phaseShift))
            val b2y = h * (0.70f + 0.15f * cos(liquidPhase * 0.85f + phaseShift))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (isPressed) 0.22f else 0.42f),
                        Color.Transparent
                    ),
                    center = Offset(b2x, b2y),
                    radius = w * 0.54f
                ),
                center = Offset(b2x, b2y),
                radius = w * 0.54f
            )

            // 3. 顶部液态曲面高光反射带
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (isPressed) 0.35f else 0.64f),
                        Color.White.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = h * 0.38f
                ),
                topLeft = Offset(4.dp.toPx(), 3.dp.toPx()),
                size = Size(w - 8.dp.toPx(), h * 0.34f),
                cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.offset(x = iconOffset.x.dp, y = iconOffset.y.dp)
        ) {
            Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
                item.iconDrawer(contentColor)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

/* =========================================================================
 * 12 大专属矢量图标（原生 DrawScope 绘制，纯净矢量、无锯齿、响应变白动效）
 * ========================================================================= */

/** 1. 联系作者图标 (Person / Author Badge) */
@Composable
fun DrawContactAuthorIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // 头部
        drawCircle(color = color, radius = w * 0.20f, center = Offset(w * 0.5f, h * 0.32f))
        // 肩部轮廓
        val bodyPath = Path().apply {
            moveTo(w * 0.22f, h * 0.80f)
            cubicTo(w * 0.22f, h * 0.58f, w * 0.36f, h * 0.54f, w * 0.50f, h * 0.54f)
            cubicTo(w * 0.64f, h * 0.54f, w * 0.78f, h * 0.58f, w * 0.78f, h * 0.80f)
            close()
        }
        drawPath(path = bodyPath, color = color, style = Fill)
    }
}

/** 2. 官网群图标 (Globe / Community Network) */
@Composable
fun DrawOfficialWebsiteGroupIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.2.dp.toPx()
        // 外层地球圆环
        drawCircle(color = color, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(strokeW))
        // 赤道纬线
        drawLine(color = color, start = Offset(w * 0.15f, h * 0.5f), end = Offset(w * 0.85f, h * 0.5f), strokeWidth = strokeW)
        // 经线椭圆
        drawOval(color = color, topLeft = Offset(w * 0.30f, h * 0.15f), size = Size(w * 0.40f, h * 0.70f), style = Stroke(strokeW))
    }
}

/** 3. 软件反馈图标 (Feedback Chat Bubble with Exclamation / Wrench) */
@Composable
fun DrawSoftwareFeedbackIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.2.dp.toPx()
        // 气泡框
        val bubble = Path().apply {
            moveTo(w * 0.20f, h * 0.22f)
            lineTo(w * 0.80f, h * 0.22f)
            lineTo(w * 0.80f, h * 0.66f)
            lineTo(w * 0.44f, h * 0.66f)
            lineTo(w * 0.28f, h * 0.82f)
            lineTo(w * 0.28f, h * 0.66f)
            lineTo(w * 0.20f, h * 0.66f)
            close()
        }
        drawPath(bubble, color = color, style = Stroke(strokeW))
        // 气泡内部感叹号
        drawLine(color = color, start = Offset(w * 0.5f, h * 0.32f), end = Offset(w * 0.5f, h * 0.48f), strokeWidth = strokeW)
        drawCircle(color = color, radius = 1.8.dp.toPx(), center = Offset(w * 0.5f, h * 0.56f))
    }
}

/** 4. 官方群聊图标 (Official Group Chat - Multiple Users Bubbles) */
@Composable
fun DrawOfficialChatGroupIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.0.dp.toPx()
        // 左上主气泡
        drawRoundRect(color = color, topLeft = Offset(w * 0.14f, h * 0.20f), size = Size(w * 0.46f, h * 0.38f), cornerRadius = CornerRadius(6.dp.toPx()), style = Stroke(strokeW))
        // 右下辅助气泡
        drawRoundRect(color = color, topLeft = Offset(w * 0.40f, h * 0.42f), size = Size(w * 0.46f, h * 0.38f), cornerRadius = CornerRadius(6.dp.toPx()), style = Stroke(strokeW))
        // 左气泡点点
        drawCircle(color = color, radius = 2.dp.toPx(), center = Offset(w * 0.28f, h * 0.39f))
        drawCircle(color = color, radius = 2.dp.toPx(), center = Offset(w * 0.37f, h * 0.39f))
        drawCircle(color = color, radius = 2.dp.toPx(), center = Offset(w * 0.46f, h * 0.39f))
        // 右气泡点点
        drawCircle(color = color, radius = 2.dp.toPx(), center = Offset(w * 0.54f, h * 0.61f))
        drawCircle(color = color, radius = 2.dp.toPx(), center = Offset(w * 0.63f, h * 0.61f))
        drawCircle(color = color, radius = 2.dp.toPx(), center = Offset(w * 0.72f, h * 0.61f))
    }
}

/** 5. 应用评分图标 (Star) */
@Composable
fun DrawAppRatingIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val star = Path().apply {
            val cX = w * 0.5f
            val cY = h * 0.5f
            val rOut = w * 0.38f
            val rIn = w * 0.18f
            for (i in 0 until 10) {
                val r = if (i % 2 == 0) rOut else rIn
                val angle = (i * 36 - 90) * (Math.PI / 180.0)
                val x = (cX + r * Math.cos(angle)).toFloat()
                val y = (cY + r * Math.sin(angle)).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }
        drawPath(star, color = color, style = Fill)
    }
}

/** 6. 分享软件图标 (Share Nodes) */
@Composable
fun DrawShareAppIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.4.dp.toPx()
        val p1 = Offset(w * 0.28f, h * 0.50f)
        val p2 = Offset(w * 0.72f, h * 0.28f)
        val p3 = Offset(w * 0.72f, h * 0.72f)
        drawLine(color = color, start = p1, end = p2, strokeWidth = strokeW)
        drawLine(color = color, start = p1, end = p3, strokeWidth = strokeW)
        drawCircle(color = color, radius = w * 0.11f, center = p1)
        drawCircle(color = color, radius = w * 0.11f, center = p2)
        drawCircle(color = color, radius = w * 0.11f, center = p3)
    }
}

/** 7. 检查更新图标 (Rocket) */
@Composable
fun DrawCheckUpdateIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val rocket = Path().apply {
            moveTo(w * 0.50f, h * 0.14f)
            cubicTo(w * 0.65f, h * 0.30f, w * 0.68f, h * 0.55f, w * 0.64f, h * 0.70f)
            lineTo(w * 0.76f, h * 0.78f)
            lineTo(w * 0.60f, h * 0.78f)
            lineTo(w * 0.50f, h * 0.88f)
            lineTo(w * 0.40f, h * 0.78f)
            lineTo(w * 0.24f, h * 0.78f)
            lineTo(w * 0.36f, h * 0.70f)
            cubicTo(w * 0.32f, h * 0.55f, w * 0.35f, h * 0.30f, w * 0.50f, h * 0.14f)
            close()
        }
        drawPath(rocket, color = color, style = Fill)
        drawCircle(color = Color.White, radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.42f))
    }
}

/** 8. 主题与外观图标 (Palette) */
@Composable
fun DrawThemeAppearanceIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.2.dp.toPx()
        val palette = Path().apply {
            moveTo(w * 0.50f, h * 0.16f)
            cubicTo(w * 0.80f, h * 0.16f, w * 0.86f, h * 0.45f, w * 0.80f, h * 0.68f)
            cubicTo(w * 0.74f, h * 0.86f, w * 0.55f, h * 0.84f, w * 0.45f, h * 0.74f)
            cubicTo(w * 0.38f, h * 0.66f, w * 0.30f, h * 0.68f, w * 0.24f, h * 0.76f)
            cubicTo(w * 0.14f, h * 0.68f, w * 0.14f, h * 0.42f, w * 0.22f, h * 0.28f)
            cubicTo(w * 0.28f, h * 0.18f, w * 0.38f, h * 0.16f, w * 0.50f, h * 0.16f)
            close()
        }
        drawPath(palette, color = color, style = Stroke(strokeW))
        drawCircle(color = color, radius = 2.2.dp.toPx(), center = Offset(w * 0.38f, h * 0.32f))
        drawCircle(color = color, radius = 2.2.dp.toPx(), center = Offset(w * 0.55f, h * 0.28f))
        drawCircle(color = color, radius = 2.2.dp.toPx(), center = Offset(w * 0.70f, h * 0.38f))
    }
}

/** 9. 关于我们图标 (Info Badge) */
@Composable
fun DrawAboutUsIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.4.dp.toPx()
        drawCircle(color = color, radius = w * 0.36f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(strokeW))
        drawCircle(color = color, radius = 2.2.dp.toPx(), center = Offset(w * 0.5f, h * 0.34f))
        drawLine(color = color, start = Offset(w * 0.5f, h * 0.46f), end = Offset(w * 0.5f, h * 0.68f), strokeWidth = strokeW)
    }
}

/** 10. 用户协议图标 (Scroll Document) */
@Composable
fun DrawUserTermsIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.2.dp.toPx()
        drawRoundRect(color = color, topLeft = Offset(w * 0.24f, h * 0.16f), size = Size(w * 0.52f, h * 0.68f), cornerRadius = CornerRadius(4.dp.toPx()), style = Stroke(strokeW))
        drawLine(color = color, start = Offset(w * 0.34f, h * 0.32f), end = Offset(w * 0.66f, h * 0.32f), strokeWidth = strokeW)
        drawLine(color = color, start = Offset(w * 0.34f, h * 0.46f), end = Offset(w * 0.66f, h * 0.46f), strokeWidth = strokeW)
        drawLine(color = color, start = Offset(w * 0.34f, h * 0.60f), end = Offset(w * 0.54f, h * 0.60f), strokeWidth = strokeW)
    }
}

/** 11. 隐私政策图标 (Security Shield & Lock) */
@Composable
fun DrawPrivacyPolicyIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.2.dp.toPx()
        val shield = Path().apply {
            moveTo(w * 0.50f, h * 0.16f)
            lineTo(w * 0.78f, h * 0.26f)
            lineTo(w * 0.78f, h * 0.52f)
            cubicTo(w * 0.78f, h * 0.72f, w * 0.62f, h * 0.84f, w * 0.50f, h * 0.88f)
            cubicTo(w * 0.38f, h * 0.84f, w * 0.22f, h * 0.72f, w * 0.22f, h * 0.52f)
            lineTo(w * 0.22f, h * 0.26f)
            close()
        }
        drawPath(shield, color = color, style = Stroke(strokeW))
        drawRoundRect(color = color, topLeft = Offset(w * 0.40f, h * 0.48f), size = Size(w * 0.20f, h * 0.18f), cornerRadius = CornerRadius(2.dp.toPx()))
        drawArc(color = color, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(w * 0.43f, h * 0.38f), size = Size(w * 0.14f, h * 0.18f), style = Stroke(1.8.dp.toPx()))
    }
}

/** 12. 儿童隐私政策图标 (Child Face) */
@Composable
fun DrawChildPrivacyIcon(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val strokeW = 2.2.dp.toPx()
        drawCircle(color = color, radius = w * 0.34f, center = Offset(w * 0.5f, h * 0.5f), style = Stroke(strokeW))
        drawArc(color = color, startAngle = 200f, sweepAngle = 140f, useCenter = false, topLeft = Offset(w * 0.32f, h * 0.38f), size = Size(w * 0.12f, h * 0.10f), style = Stroke(strokeW))
        drawArc(color = color, startAngle = 200f, sweepAngle = 140f, useCenter = false, topLeft = Offset(w * 0.56f, h * 0.38f), size = Size(w * 0.12f, h * 0.10f), style = Stroke(strokeW))
        drawArc(color = color, startAngle = 20f, sweepAngle = 140f, useCenter = false, topLeft = Offset(w * 0.36f, h * 0.48f), size = Size(w * 0.28f, h * 0.22f), style = Stroke(strokeW))
    }
}
