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
import androidx.compose.ui.graphics.drawscope.Stroke
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

/** 国风山水主题色 */
private val SsJade = Color(0xFF1B4931)          // 青山黛绿
private val SsGold = Color(0xFFFDE047)         // 缃色流金
private val SsDeepCyan = Color(0xFF0A192F)     // 黛墨夜色
private val SsMistGreen = Color(0xFF2D6A4F)    // 远山翠微
private val SsWaterTeal = Color(0xFF132E35)    // 碧水深潭

/** 五圈波浪环颜色（国风山水五色：碧玉、天青、流金、松柏、霁蓝） */
private val RingColors = listOf(
    Color(0xFF10B981), // 碧玉绿
    Color(0xFF06B6D4), // 烟波天青
    Color(0xFFFACC15), // 缃色流金
    Color(0xFF34D399), // 松柏翠绿
    Color(0xFF38BDF8)  // 霁蓝晴空
)

/** 五圈波浪环尺寸（从外到内）与旋转速度 */
private data class RingSpec(val size: Float, val durationMs: Int, val reverse: Boolean, val radius: FloatArray)

@Composable
private fun ShengshiWaveSplashContent(
    countdownSeconds: Int,
    onDismiss: () -> Unit,
    entryScale: Animatable<Float, *>,
    entryAlpha: Animatable<Float, *>,
    uiText: com.example.data.remote.UiTextDto? = null
) {
    // v1.4.14：图2风格重写 —— 清新 pastel 可爱风启动页（动态漂浮团子 + 俏皮文案）
    val infinite = rememberInfiniteTransition(label = "splash_float")
    val float1 by infinite.animateFloat(0f, 14f, infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f1")
    val float2 by infinite.animateFloat(0f, -12f, infiniteRepeatable(tween(2600, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f2")
    val float3 by infinite.animateFloat(0f, 10f, infiniteRepeatable(tween(1900, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f3")
    val bubblePop by infinite.animateFloat(0.92f, 1.06f, infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pop")
    // 眨眼动画
    val blink by infinite.animateFloat(1f, 1f, infiniteRepeatable(
        keyframes { durationMillis = 3600; 1f at 0; 1f at 3300; 0.12f at 3400; 1f at 3500 },
        RepeatMode.Restart
    ), label = "blink")

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
        // ---- 背景漂浮团子 Canvas ----
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width; val h = size.height
            // 粉色圆团子（左上）
            drawKawaiiBlob(
                center = Offset(w * 0.22f, h * 0.30f + float1 * 2),
                r = w * 0.13f,
                color = Color(0xFFF8A8C8),
                eyeScaleY = blink
            )
            // 蓝色圆角团子（中央偏下）
            drawKawaiiBlob(
                center = Offset(w * 0.52f, h * 0.62f + float2 * 2),
                r = w * 0.20f,
                color = Color(0xFF8FD0F0),
                eyeScaleY = blink,
                roundRect = true
            )
            // 黄色团子（右下）
            drawKawaiiBlob(
                center = Offset(w * 0.78f, h * 0.72f + float3 * 2),
                r = w * 0.15f,
                color = Color(0xFFFFD93D),
                eyeScaleY = blink
            )
            // 橙色小团子（右上点缀）
            drawKawaiiBlob(
                center = Offset(w * 0.85f, h * 0.22f + float2),
                r = w * 0.09f,
                color = Color(0xFFFF9A3D),
                eyeScaleY = blink
            )
            // 星星点缀
            val starY = h * 0.18f + float3
            drawStar(center = Offset(w * 0.15f, starY), r = 14.dp.toPx(), color = Color(0xFFFFD93D))
            drawStar(center = Offset(w * 0.68f, h * 0.14f + float1), r = 10.dp.toPx(), color = Color(0xFFF8A8C8))
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
            // 主标题：懒得找了
            Text(
                text = "懒得找了",
                fontSize = 52.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF2B2B2B),
                letterSpacing = 2.sp
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

/** 单圈波浪环（独立组合函数，内部持有自己的旋转/呼吸动画） */
@Composable
private fun WaveRing(spec: RingSpec, index: Int, color: Color) {
    val angle by rememberInfiniteTransition(label = "ring_$index").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(spec.durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringAngle$index"
    )
    val pulse by rememberInfiniteTransition(label = "ring_pulse_$index").animateFloat(
        initialValue = 0.96f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600 + index * 180, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringPulse$index"
    )
    val shape = RoundedCornerShape(
        topStartPercent = spec.radius[0].toInt(),
        topEndPercent = spec.radius[1].toInt(),
        bottomEndPercent = spec.radius[2].toInt(),
        bottomStartPercent = spec.radius[3].toInt()
    )
    Box(
        modifier = Modifier
            .size(spec.size.dp)
            .scale(pulse)
            .rotate(if (spec.reverse) -angle else angle)
            .clip(shape)
            .background(color.copy(alpha = 0.10f))
            .border(3.dp, color, shape)
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = color,
                spotColor = color
            )
    )
}

/** 环绕轨道四颗光点（沿方形轨道公转） */
@Composable
private fun OrbitDots(angle: Float) {
    val dotColors = listOf(
        Color(0xFFFF006E),
        Color(0xFF00D9FF),
        Color(0xFFFFEA00),
        Color(0xFF00FF88)
    )
    Box(modifier = Modifier.size(220.dp)) {
        // 四颗光点分别置于上/下/左/右，整体随 angle 旋转
        val spin = angle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(spin)
        ) {
            listOf(
                Alignment.TopCenter to dotColors[0],
                Alignment.BottomCenter to dotColors[1],
                Alignment.CenterStart to dotColors[2],
                Alignment.CenterEnd to dotColors[3]
            ).forEach { (align, color) ->
                Box(
                    modifier = Modifier
                        .align(align)
                        .size(9.dp)
                        .clip(CircleShape)
                        .background(color)
                        .shadow(8.dp, CircleShape, ambientColor = color, spotColor = color)
                )
            }
        }
    }
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
