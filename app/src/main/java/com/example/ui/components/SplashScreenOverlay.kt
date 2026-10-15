package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.VideoView
import coil.compose.AsyncImage
import com.example.data.remote.SplashDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.withFrameNanos

/**
 * v1.1.18 开屏动画重制版：
 * 「盛世华诞 · 圆形波浪旋转加载器」
 *
 * 视觉呈现（灵感源自经典 CSS 圆形波浪加载器）：
 * 1. 五圈不规则波浪环（红/橙/金/绿/青）各自反向不同速度旋转，外发光呼吸
 * 2. 环绕轨道四颗光点公转，中心金色核心「懒得找了」流光标题
 * 3. 底部「加载中」点点动画 + 盛世华诞红金渐变背景
 * 4. 右上角「跳过」倒计时按钮（默认 5 秒，云端可覆盖），倒计时结束自动进入首页
 *
 * 云端 type=html/media 分支保留：控制台可切换到 WebView 网页开屏 / 视频开屏 / 图片开屏。
 */
@Composable
fun SplashScreenOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    splash: SplashDto? = null,
    splashReady: Boolean = false,
    // v1.2.8：开屏副标题/加载文字可云端覆盖（控制台 UI 文本编辑）
    uiText: com.example.data.remote.UiTextDto? = null
) {
    var countdownSeconds by remember(splash?.durationSeconds) {
        // v1.4.11：默认 5 秒；云端控制台配置了展示时长则严格跟随后台设定
        mutableIntStateOf((splash?.durationSeconds ?: 5).coerceIn(1, 15))
    }

    val entryScale = remember { Animatable(0.7f) }
    val entryAlpha = remember { Animatable(0f) }

    LaunchedEffect(isVisible, splashReady) {
        if (isVisible) {
            launch {
                entryScale.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
            }
            launch {
                entryAlpha.animateTo(1f, tween(450))
            }
            // 等待云端开屏配置就绪（最多等 3.5 秒，避免网络异常时一直卡在开屏）
            var waited = 0
            while (!splashReady && waited < 3500) {
                delay(100)
                waited += 100
            }
            // 严格按设定时长倒计时（每 100ms 校准系统时间，避免 withFrameNanos 高频轮询抢占主线程）
            val totalMillis = countdownSeconds * 1000L
            val startTime = System.currentTimeMillis()
            while (true) {
                val elapsed = System.currentTimeMillis() - startTime
                val remainSec = ((totalMillis - elapsed) / 1000L).coerceAtLeast(0L).toInt()
                if (remainSec != countdownSeconds) {
                    countdownSeconds = remainSec
                }
                if (elapsed >= totalMillis) break
                delay(100L)
            }
            delay(150)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(250)),
        exit = fadeOut(tween(350)) + scaleOut(targetScale = 1.08f, animationSpec = tween(350))
    ) {
        val sp = splash
        if (sp != null && (sp.type == "html" || sp.type == "media")) {
            CloudSplashContent(splash = sp, onDismiss = onDismiss)
        } else {
            ShengshiWaveSplashContent(
                countdownSeconds = countdownSeconds,
                onDismiss = onDismiss,
                entryScale = entryScale,
                entryAlpha = entryAlpha,
                uiText = uiText
            )
        }
    }
}

/* =====================================================================
 * 「盛世华诞 · 圆形波浪旋转加载器」开屏
 * ===================================================================== */

/* =====================================================================
 * v1.4.19 开屏动效重制：「Aurora 极光 × 流光 × 涟漪」
 * 最新 CSS 流行特效的 Compose 还原：
 * 1. Aurora 极光背景 —— 4 团大面积径向渐变色块缓慢漂移（CSS filter:blur 质感）
 * 2. 涟漪扩散 —— 中央 3 圈同心圆持续向外扩散淡出
 * 3. 旋转光环 —— 中央虚线光环缓慢旋转（CSS conic-gradient spinner 质感）
 * 4. 标题流光 shimmer —— "懒得找了" 高光横向扫过（CSS background-clip:text 质感）
 * 5. 上升粒子 —— 26 颗星星/圆点从底部升起
 * 6. 玻璃气泡 —— 半透明高光气泡漂浮
 * 7. 团子 squash 拉伸 + 摇摆 + 眨眼（保留 v1.4.14 可爱风）
 * ===================================================================== */

/** 上升粒子种子 */
private data class SplashParticle(
    val x: Float,       // 0..1 横向位置
    val speed: Float,   // 上升速度系数
    val sizeDp: Float,  // 大小 dp
    val phase: Float,   // 0..1 相位
    val color: Color,
    val isStar: Boolean
)

@Composable
private fun ShengshiWaveSplashContent(
    countdownSeconds: Int,
    onDismiss: () -> Unit,
    entryScale: Animatable<Float, *>,
    entryAlpha: Animatable<Float, *>,
    uiText: com.example.data.remote.UiTextDto? = null
) {
    val infinite = rememberInfiniteTransition(label = "splash_fx")

    // ---- Aurora 极光漂移 ----
    val aurora1 by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Reverse), label = "a1")
    val aurora2 by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Reverse), label = "a2")
    val aurora3 by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Reverse), label = "a3")

    // ---- 标题流光：-0.3..1.3 循环扫过 ----
    val shimmer by infinite.animateFloat(-0.3f, 1.3f, infiniteRepeatable(tween(2800, easing = LinearEasing), RepeatMode.Restart), label = "shimmer")

    // ---- 涟漪扩散 0..1 / 旋转光环角度 ----
    val ripple by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Restart), label = "ripple")
    val ringAngle by infinite.animateFloat(0f, 360f, infiniteRepeatable(tween(16000, easing = LinearEasing), RepeatMode.Restart), label = "ring")

    // ---- 团子浮动 + squash 拉伸 + 摇摆 ----
    val float1 by infinite.animateFloat(0f, 14f, infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f1")
    val float2 by infinite.animateFloat(0f, -12f, infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f2")
    val float3 by infinite.animateFloat(0f, 10f, infiniteRepeatable(tween(1900, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f3")
    val squash by infinite.animateFloat(0.94f, 1.06f, infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "squash")
    val bubblePop by infinite.animateFloat(0.92f, 1.06f, infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pop")
    val wobble by infinite.animateFloat(-7f, 7f, infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "wobble")
    // 眨眼
    val blink by infinite.animateFloat(1f, 1f, infiniteRepeatable(
        keyframes { durationMillis = 3600; 1f at 0; 1f at 3300; 0.12f at 3400; 1f at 3500 },
        RepeatMode.Restart
    ), label = "blink")
    // 粒子上升进度
    val particleT by infinite.animateFloat(0f, 1f, infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Restart), label = "pt")

    // 粒子种子（固定随机，保证稳定）
    val particles = remember {
        val rnd = kotlin.random.Random(20261015)
        val palette = listOf(Color(0xFFFFD93D), Color(0xFFF8A8C8), Color(0xFF8FD0F0), Color(0xFFFF9A3D), Color.White)
        List(26) {
            SplashParticle(
                x = rnd.nextFloat(),
                speed = 0.5f + rnd.nextFloat() * 0.9f,
                sizeDp = 3f + rnd.nextFloat() * 7f,
                phase = rnd.nextFloat(),
                color = palette[rnd.nextInt(palette.size)],
                isStar = rnd.nextFloat() < 0.35f
            )
        }
    }
    // 玻璃气泡种子：x, y, 半径(dp)
    val bubbles = remember {
        val rnd = kotlin.random.Random(7)
        List(7) { Triple(rnd.nextFloat(), 0.1f + rnd.nextFloat() * 0.75f, 12f + rnd.nextFloat() * 26f) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF8E7), // 奶油白
                        Color(0xFFFFF1DC), // 蜜桃
                        Color(0xFFFFE8D6)  // 柔粉
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() }
    ) {
        // ============ 背景特效 Canvas ============
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width; val h = size.height

            // ---- 1. Aurora 极光团（径向渐变模拟 CSS blur） ----
            drawAuroraBlob(
                center = Offset(w * (0.12f + 0.30f * aurora1), h * (0.18f + 0.10f * aurora2)),
                r = w * 0.55f, color = Color(0xFFF8A8C8)
            )
            drawAuroraBlob(
                center = Offset(w * (0.88f - 0.28f * aurora2), h * (0.30f + 0.12f * aurora3)),
                r = w * 0.50f, color = Color(0xFF8FD0F0)
            )
            drawAuroraBlob(
                center = Offset(w * (0.30f + 0.35f * aurora3), h * (0.85f - 0.10f * aurora1)),
                r = w * 0.60f, color = Color(0xFFFFD93D)
            )
            drawAuroraBlob(
                center = Offset(w * (0.75f - 0.20f * aurora1), h * (0.70f + 0.12f * aurora2)),
                r = w * 0.42f, color = Color(0xFFFF9A3D)
            )

            // ---- 2. 中央涟漪扩散 ----
            val cx = w * 0.5f; val cy = h * 0.42f
            for (i in 0 until 3) {
                val t = (ripple + i / 3f) % 1f
                drawCircle(
                    color = Color(0xFFFF9A3D).copy(alpha = (1f - t) * 0.30f),
                    radius = w * (0.16f + t * 0.42f),
                    center = Offset(cx, cy),
                    style = Stroke(width = (1f + 3f * (1f - t)).dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // ---- 3. 旋转虚线光环 ----
            val ringR = w * 0.30f
            drawArc(
                color = Color(0xFFFFD93D).copy(alpha = 0.55f),
                startAngle = ringAngle,
                sweepAngle = 300f,
                useCenter = false,
                topLeft = Offset(cx - ringR, cy - ringR),
                size = Size(ringR * 2f, ringR * 2f),
                style = Stroke(
                    width = 5.dp.toPx(), cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(22f, 16f), 0f)
                )
            )
            drawArc(
                color = Color(0xFFF8A8C8).copy(alpha = 0.45f),
                startAngle = -ringAngle * 0.7f,
                sweepAngle = 200f,
                useCenter = false,
                topLeft = Offset(cx - ringR * 1.25f, cy - ringR * 1.25f),
                size = Size(ringR * 2.5f, ringR * 2.5f),
                style = Stroke(
                    width = 3.dp.toPx(), cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 18f), 0f)
                )
            )

            // ---- 4. 可爱团子（squash 拉伸 + 摇摆 + 眨眼） ----
            withTransform(
                transformBlock = {
                    val c1 = Offset(w * 0.22f, h * 0.28f + float1 * 2)
                    rotate(wobble * 0.6f, pivot = c1)
                    scale(sx = 2f - squash, sy = squash, pivot = c1)
                }
            ) {
                drawKawaiiBlob(
                    center = Offset(w * 0.22f, h * 0.28f + float1 * 2),
                    r = w * 0.11f, color = Color(0xFFF8A8C8), eyeScaleY = blink
                )
            }
            withTransform(
                transformBlock = {
                    val c2 = Offset(w * 0.55f, h * 0.66f + float2 * 2)
                    rotate(-wobble * 0.5f, pivot = c2)
                    scale(sx = 2f - squash, sy = squash, pivot = c2)
                }
            ) {
                drawKawaiiBlob(
                    center = Offset(w * 0.55f, h * 0.66f + float2 * 2),
                    r = w * 0.17f, color = Color(0xFF8FD0F0), eyeScaleY = blink, roundRect = true
                )
            }
            withTransform(
                transformBlock = {
                    val c3 = Offset(w * 0.80f, h * 0.74f + float3 * 2)
                    rotate(wobble * 0.8f, pivot = c3)
                    scale(sx = 2f - squash, sy = squash, pivot = c3)
                }
            ) {
                drawKawaiiBlob(
                    center = Offset(w * 0.80f, h * 0.74f + float3 * 2),
                    r = w * 0.13f, color = Color(0xFFFFD93D), eyeScaleY = blink
                )
            }
            withTransform(
                transformBlock = {
                    val c4 = Offset(w * 0.86f, h * 0.20f + float2)
                    scale(sx = 2f - squash, sy = squash, pivot = c4)
                }
            ) {
                drawKawaiiBlob(
                    center = Offset(w * 0.86f, h * 0.20f + float2),
                    r = w * 0.075f, color = Color(0xFFFF9A3D), eyeScaleY = blink
                )
            }

            // ---- 5. 玻璃气泡 ----
            bubbles.forEachIndexed { i, (bx, by, br) ->
                val drift = when (i % 3) { 0 -> float1; 1 -> float2; else -> float3 }
                val c = Offset(w * bx, h * by + drift * 1.6f)
                val r = br.dp.toPx()
                drawCircle(Color.White.copy(alpha = 0.16f), r, c)
                drawCircle(Color.White.copy(alpha = 0.30f), r * 0.32f, Offset(c.x - r * 0.30f, c.y - r * 0.34f))
                drawArc(
                    Color.White.copy(alpha = 0.55f), 195f, 110f, false,
                    Offset(c.x - r * 0.85f, c.y - r * 0.85f), Size(r * 1.7f, r * 1.7f),
                    style = Stroke(2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // ---- 6. 上升粒子 ----
            particles.forEach { pt ->
                val t = (particleT * pt.speed + pt.phase) % 1f
                val y = h * (1.06f - t * 1.18f)
                val alpha = (minOf(t * 6f, (1f - t) * 2.5f).coerceIn(0f, 1f)) * 0.85f
                if (alpha > 0.01f) {
                    val c = Offset(w * pt.x, y)
                    val r = pt.sizeDp.dp.toPx()
                    if (pt.isStar) {
                        drawStar(center = c, r = r, color = pt.color.copy(alpha = alpha))
                    } else {
                        drawCircle(color = pt.color.copy(alpha = alpha), radius = r * 0.5f, center = c)
                    }
                }
            }
        }

        // ---- 右上角跳过按钮（奶白 pill） ----
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.85f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8590C).copy(alpha = 0.35f)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 44.dp, end = 20.dp)
                .clickable { onDismiss() }
        ) {
            Text(
                text = "跳过 $countdownSeconds s",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE8590C),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }

        // ---- 中央文案区 ----
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-40).dp)
                .scale(entryScale.value)
                .graphicsLayer { alpha = entryAlpha.value }
                .padding(horizontal = 32.dp)
        ) {
            // Hi~ 气泡
            Box(
                modifier = Modifier
                    .scale(bubblePop)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF2B2B2B))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("Hi~", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
            // 主标题：流光 shimmer
            val shimmerX = shimmer * 760f
            Text(
                text = "懒得找了",
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2B2B2B),
                            Color(0xFF2B2B2B),
                            Color(0xFFFFF3D6),
                            Color(0xFFFFB03D),
                            Color(0xFF2B2B2B),
                            Color(0xFF2B2B2B)
                        ),
                        start = Offset(shimmerX - 170f, 0f),
                        end = Offset(shimmerX + 170f, 0f)
                    ),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            )
            // 波浪装饰线
            Canvas(modifier = Modifier.fillMaxWidth(0.7f).height(12.dp)) {
                val w = size.width; val h = size.height
                val path = Path().apply {
                    moveTo(0f, h * 0.6f)
                    cubicTo(w * 0.25f, h * 0.1f, w * 0.35f, h * 1.1f, w * 0.5f, h * 0.6f)
                    cubicTo(w * 0.65f, h * 0.1f, w * 0.75f, h * 1.1f, w, h * 0.6f)
                }
                drawPath(path, Color(0xFFFF9A3D), style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "全网视频 · 一键直达",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A6A4A),
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            // 求分享气泡
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .border(1.5.dp, Color(0xFF8FD0F0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text("无水印~", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E9BD6))
                }
                Box(
                    modifier = Modifier
                        .scale(bubblePop)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFD93D))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text("YEAH!", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(0xFF2B2B2B))
                }
            }
        }

        // ---- 底部加载点点 ----
        var dotCount by remember { mutableIntStateOf(0) }
        LaunchedEffect(Unit) {
            while (true) {
                dotCount = (dotCount + 1) % 4
                delay(400)
            }
        }
        Text(
            text = (uiText?.splashLoading?.ifBlank { "正在为你准备" } ?: "正在为你准备") + ".".repeat(dotCount),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB08968),
            letterSpacing = 2.sp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp)
        )
    }
}

/** Aurora 极光团：径向渐变模拟 CSS blur 大色块 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawAuroraBlob(
    center: Offset,
    r: Float,
    color: Color
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = 0.42f),
                color.copy(alpha = 0.14f),
                Color.Transparent
            ),
            center = center,
            radius = r
        ),
        radius = r,
        center = center
    )
}
/** 可爱团子绘制（含笑脸，eyeScaleY 控制眨眼） */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawKawaiiBlob(
    center: Offset,
    r: Float,
    color: Color,
    eyeScaleY: Float,
    roundRect: Boolean = false
) {
    // 身体
    if (roundRect) {
        drawRoundRect(
            color = color,
            topLeft = Offset(center.x - r, center.y - r * 1.15f),
            size = Size(r * 2, r * 2.3f),
            cornerRadius = CornerRadius(r * 0.7f, r * 0.7f)
        )
    } else {
        drawCircle(color = color, radius = r, center = center)
    }
    // 高光
    drawCircle(
        color = Color.White.copy(alpha = 0.35f),
        radius = r * 0.28f,
        center = Offset(center.x - r * 0.35f, center.y - r * 0.4f)
    )
    val faceColor = Color(0xFF2B2B2B)
    val eyeY = center.y - r * 0.05f
    val eyeDX = r * 0.32f
    val eyeH = r * 0.16f * eyeScaleY.coerceAtLeast(0.12f)
    // 眼睛（椭圆，眨眼时压扁）
    drawOval(faceColor, topLeft = Offset(center.x - eyeDX - r * 0.09f, eyeY - eyeH), size = Size(r * 0.18f, eyeH * 2))
    drawOval(faceColor, topLeft = Offset(center.x + eyeDX - r * 0.09f, eyeY - eyeH), size = Size(r * 0.18f, eyeH * 2))
    // 微笑嘴
    val mouthPath = Path().apply {
        moveTo(center.x - r * 0.16f, center.y + r * 0.22f)
        quadraticTo(center.x, center.y + r * 0.38f, center.x + r * 0.16f, center.y + r * 0.22f)
    }
    drawPath(mouthPath, faceColor, style = Stroke(width = r * 0.07f, cap = StrokeCap.Round))
    // 腮红
    drawCircle(Color(0xFFFF6B8A).copy(alpha = 0.45f), r * 0.12f, Offset(center.x - r * 0.52f, center.y + r * 0.18f))
    drawCircle(Color(0xFFFF6B8A).copy(alpha = 0.45f), r * 0.12f, Offset(center.x + r * 0.52f, center.y + r * 0.18f))
}

/** 小星星 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStar(
    center: Offset,
    r: Float,
    color: Color
) {
    val path = Path().apply {
        for (i in 0 until 10) {
            val ang = Math.PI / 5 * i - Math.PI / 2
            val rad = if (i % 2 == 0) r else r * 0.45f
            val x = center.x + (rad * kotlin.math.cos(ang)).toFloat()
            val y = center.y + (rad * kotlin.math.sin(ang)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    drawPath(path, color)
}

/* =====================================================================
 * 云端开屏分支（type=html / media，控制台可切换，保留）
 * ===================================================================== */

@Composable
private fun CloudSplashContent(
    splash: SplashDto,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(parseHexColor(splash.bgColor))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() }
    ) {
        when (splash.type) {
            "html" -> {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                            settings.javaScriptEnabled = true
                            setBackgroundColor(0x00000000)
                            webViewClient = object : WebViewClient() {
                                override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                                    return true
                                }
                            }
                            loadDataWithBaseURL(null, splash.customHtml, "text/html", "UTF-8", null)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            "media" -> {
                val url = splash.mediaUrl
                if (url.isNullOrBlank()) return@Box
                if (url.contains(".mp4", ignoreCase = true) || url.contains(".webm", ignoreCase = true) ||
                    url.contains(".mov", ignoreCase = true) || url.contains(".m4v", ignoreCase = true)
                ) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setVideoURI(Uri.parse(url))
                                setOnPreparedListener { mp ->
                                    mp.isLooping = true
                                    mp.setVolume(0f, 0f)
                                    mp.start()
                                }
                                setOnErrorListener { _, _, _ -> true }
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        },
                        onRelease = { view ->
                            try { view.stopPlayback() } catch (_: Exception) {}
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

private fun parseHexColor(hex: String): Color {
    return try {
        val clean = hex.removePrefix("#")
        val v = clean.toLong(16)
        Color(
            red = ((v shr 16) and 0xFF) / 255f,
            green = ((v shr 8) and 0xFF) / 255f,
            blue = (v and 0xFF) / 255f,
            alpha = 1f
        )
    } catch (e: Exception) {
        Color(0xFF450A0A)
    }
}
