package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * v1.6.3 龙腾开屏 —— 致敬 dragon-codex-boot 的启动特效理念：
 * 全屏龙动画播放 → 点击跳过 → 淡出交接主界面。
 * 纯 Compose Canvas 原创绘制（盘龙剪影 + 火星粒子 + 云纹），不使用任何第三方版权素材。
 *
 * @param onFinish 动画结束或用户点击跳过时回调
 * @param durationMs 动画总时长（默认 3200ms）
 */
@Composable
fun DragonBootSplash(
    onFinish: () -> Unit,
    durationMs: Long = 3200L
) {
    var finished by remember { mutableStateOf(false) }
    val finishOnce = remember { { if (!finished) { finished = true; onFinish() } } }

    // 到时自动结束
    LaunchedEffect(Unit) {
        delay(durationMs)
        finishOnce()
    }

    // 龙体呼吸光晕
    val infinite = rememberInfiniteTransition(label = "dragon_boot")
    val glow by infinite.animateFloat(
        initialValue = 0.55f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dragonGlow"
    )
    // 龙体盘旋旋转
    val rotate by infinite.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dragonRotate"
    )
    // 火星粒子上升
    val emberT by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "emberRise"
    )

    // 进度（与总时长同步）
    var progress by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        val steps = 40
        repeat(steps) {
            delay(durationMs / steps)
            progress = (it + 1) / steps.toFloat()
        }
    }

    // 预生成火星粒子
    val embers = remember {
        List(42) {
            Ember(
                xFrac = Random.nextFloat(),
                speed = 0.5f + Random.nextFloat(),
                size = 2f + Random.nextFloat() * 5f,
                phase = Random.nextFloat(),
                drift = (Random.nextFloat() - 0.5f) * 0.2f
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D0208), Color(0xFF2B0A12), Color(0xFF0D0208))
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { finishOnce() },
        contentAlignment = Alignment.Center
    ) {
        // 火星粒子层
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            embers.forEach { e ->
                val t = ((emberT * e.speed + e.phase) % 1f)
                val y = h * (1f - t)
                val x = w * (e.xFrac + e.drift * t * 4f)
                val alpha = (1f - t) * 0.9f
                drawCircle(
                    color = Color(0xFFFF9A3D).copy(alpha = alpha),
                    radius = e.size,
                    center = Offset(x.coerceIn(0f, w), y)
                )
                drawCircle(
                    color = Color(0xFFFFD400).copy(alpha = alpha * 0.7f),
                    radius = e.size * 0.45f,
                    center = Offset(x.coerceIn(0f, w), y)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 盘龙 Canvas
            Canvas(
                modifier = Modifier
                    .width(280.dp)
                    .height(280.dp)
            ) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val baseR = size.minDimension * 0.34f

                // 外圈云纹光环
                withTransform({ rotate(rotate, Offset(cx, cy)) }) {
                    for (i in 0 until 3) {
                        drawArc(
                            color = Color(0xFFFF4D2E).copy(alpha = 0.25f + glow * 0.2f),
                            startAngle = i * 120f,
                            sweepAngle = 80f,
                            useCenter = false,
                            topLeft = Offset(cx - baseR * 1.35f, cy - baseR * 1.35f),
                            size = androidx.compose.ui.geometry.Size(baseR * 2.7f, baseR * 2.7f),
                            style = Stroke(width = 10f, cap = StrokeCap.Round)
                        )
                    }
                }

                // 盘龙身体：螺旋
                val bodyPath = Path().apply {
                    val turns = 2.2f
                    val steps = 120
                    for (i in 0..steps) {
                        val t = i / steps.toFloat()
                        val ang = t * turns * 2f * PI.toFloat() + rotate * PI.toFloat() / 180f * 0.15f
                        val r = baseR * (0.25f + 0.75f * t)
                        val x = cx + r * cos(ang).toFloat()
                        val y = cy + r * sin(ang).toFloat() * 0.9f
                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                }
                // 身体光晕底层
                drawPath(
                    path = bodyPath,
                    color = Color(0xFFFF4D2E).copy(alpha = 0.18f * glow),
                    style = Stroke(width = 34f, cap = StrokeCap.Round)
                )
                // 身体主体
                drawPath(
                    path = bodyPath,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFD400), Color(0xFFFF4D2E), Color(0xFFB71C1C)),
                        start = Offset(cx - baseR, cy - baseR),
                        end = Offset(cx + baseR, cy + baseR)
                    ),
                    style = Stroke(width = 16f, cap = StrokeCap.Round)
                )
                // 背鳍
                val finPath = Path().apply {
                    val turns = 2.2f
                    val steps = 40
                    for (i in 0..steps step 2) {
                        val t = i / 120f
                        val ang = t * turns * 2f * PI.toFloat() + rotate * PI.toFloat() / 180f * 0.15f
                        val r = baseR * (0.25f + 0.75f * t)
                        val bx = cx + r * cos(ang).toFloat()
                        val by = cy + r * sin(ang).toFloat() * 0.9f
                        val fx = cx + (r + 22f) * cos(ang).toFloat()
                        val fy = cy + (r + 22f) * sin(ang).toFloat() * 0.9f
                        moveTo(bx, by); lineTo(fx, fy)
                    }
                }
                drawPath(
                    path = finPath,
                    color = Color(0xFFFFD400).copy(alpha = 0.75f * glow),
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )

                // 龙头（螺旋外端）
                val headT = 1f
                val headAng = headT * 2.2f * 2f * PI.toFloat() + rotate * PI.toFloat() / 180f * 0.15f
                val hr = baseR * (0.25f + 0.75f * headT)
                val hx = cx + hr * cos(headAng).toFloat()
                val hy = cy + hr * sin(headAng).toFloat() * 0.9f
                // 头
                drawCircle(
                    color = Color(0xFFFF6B35),
                    radius = 26f,
                    center = Offset(hx, hy)
                )
                drawCircle(
                    color = Color(0xFFFFD400).copy(alpha = 0.35f * glow),
                    radius = 34f,
                    center = Offset(hx, hy)
                )
                // 犄角
                val hornPath = Path().apply {
                    moveTo(hx - 8f, hy - 20f); lineTo(hx - 22f, hy - 52f); lineTo(hx - 4f, hy - 34f)
                    moveTo(hx + 8f, hy - 20f); lineTo(hx + 22f, hy - 52f); lineTo(hx + 4f, hy - 34f)
                }
                drawPath(hornPath, Color(0xFFFFD400), style = Stroke(width = 7f, cap = StrokeCap.Round))
                // 眼睛（发光）
                val eyeDx = cos(headAng + PI / 2).toFloat() * 10f
                val eyeDy = sin(headAng + PI / 2).toFloat() * 10f
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f * glow),
                    radius = 7f,
                    center = Offset(hx + eyeDx, hy + eyeDy - 4f)
                )
                drawCircle(
                    color = Color(0xFFFF1744),
                    radius = 3.5f,
                    center = Offset(hx + eyeDx, hy + eyeDy - 4f)
                )
                // 龙珠
                val pearlAng = headAng + 0.9f
                val px = hx + 52f * cos(pearlAng).toFloat()
                val py = hy + 52f * sin(pearlAng).toFloat()
                drawCircle(
                    color = Color(0xFFFFD400).copy(alpha = 0.25f * glow),
                    radius = 20f,
                    center = Offset(px, py)
                )
                drawCircle(
                    color = Color(0xFFFFF176),
                    radius = 11f,
                    center = Offset(px, py)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "懒得找了",
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "龙腾开屏 · 点击任意处跳过",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.55f)
            )
            Spacer(modifier = Modifier.height(18.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(180.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFFFF4D2E),
                trackColor = Color.White.copy(alpha = 0.15f)
            )
        }
    }
}

private data class Ember(
    val xFrac: Float,
    val speed: Float,
    val size: Float,
    val phase: Float,
    val drift: Float
)
