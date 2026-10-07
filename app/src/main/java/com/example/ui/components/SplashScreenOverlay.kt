package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
 * 4. 右上角「跳过」倒计时按钮（默认 3 秒，云端可覆盖），倒计时结束自动进入首页
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
        // v1.1.18：默认 3 秒；云端控制台配置了展示时长则严格跟随后台设定
        mutableIntStateOf((splash?.durationSeconds ?: 3).coerceIn(1, 15))
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

/** 盛世华诞主题色 */
private val SsRed = Color(0xFFDE2910)          // 中国红
private val SsGold = Color(0xFFFFD700)         // 烫金
private val SsDeepRed = Color(0xFF450A0A)      // 深朱砂
private val SsDarkRed = Color(0xFF7F1D1D)      // 熟褐红
private val SsBloodRed = Color(0xFF991B1B)     // 中国红渐变段

/** 五圈波浪环颜色（对应经典波浪加载器五色） */
private val RingColors = listOf(
    Color(0xFFFF006E), // 品红
    Color(0xFFFF7B00), // 暖橙
    Color(0xFFFFEA00), // 鎏金
    Color(0xFF00FF88), // 荧光绿
    Color(0xFF00D9FF)  // 冰青
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(SsDarkRed, SsBloodRed, SsDeepRed)
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onDismiss() }
    ) {
        // ---- 两团柔光光晕（左上红晕 / 右下金晕，漂浮呼吸）----
        val floatT by rememberInfiniteTransition(label = "glow_float").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "floatT"
        )
        Box(
            modifier = Modifier
                .size(420.dp)
                .align(Alignment.TopStart)
                .offset(y = (floatT * 40f).dp)
                .graphicsLayer { alpha = 0.22f }
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFF3B6E), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(420.dp)
                .align(Alignment.BottomEnd)
                .offset(y = (-floatT * 40f).dp)
                .graphicsLayer { alpha = 0.18f }
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFFFC24D), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        // ---- 右上角「跳过 3s」按钮 ----
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Black.copy(alpha = 0.35f),
            border = androidx.compose.foundation.BorderStroke(1.dp, SsGold.copy(alpha = 0.65f)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 44.dp, end = 20.dp)
                .clickable { onDismiss() }
        ) {
            Text(
                text = "跳过 $countdownSeconds s",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SsGold,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }

        // ---- 中央加载器 ----
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .scale(entryScale.value)
                .graphicsLayer { alpha = entryAlpha.value }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(280.dp)
            ) {
                // 五圈波浪环（各自旋转，方向交替）
                val rings = remember {
                    listOf(
                        RingSpec(252f, 4000, false, floatArrayOf(42f, 58f, 70f, 30f)),
                        RingSpec(207f, 5000, true, floatArrayOf(60f, 40f, 30f, 70f)),
                        RingSpec(162f, 3500, false, floatArrayOf(30f, 70f, 70f, 30f)),
                        RingSpec(117f, 6000, true, floatArrayOf(70f, 30f, 50f, 50f)),
                        RingSpec(72f, 4500, false, floatArrayOf(50f, 50f, 30f, 70f))
                    )
                }
                rings.forEachIndexed { i, spec ->
                    WaveRing(spec = spec, index = i, color = RingColors[i])
                }

                // 环绕轨道四颗光点（公转）
                val orbitAngle by rememberInfiniteTransition(label = "orbit").animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "orbitAngle"
                )
                OrbitDots(angle = orbitAngle)

                // 中心金色核心：「懒得找了」流光标题
                val corePulse by rememberInfiniteTransition(label = "core_pulse").animateFloat(
                    initialValue = 1f,
                    targetValue = 1.12f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "corePulse"
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(104.dp)
                        .scale(corePulse)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFFFFF4C2), SsGold, Color(0xFFE8A200))
                            )
                        )
                        .shadow(22.dp, CircleShape, ambientColor = SsGold, spotColor = SsGold)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "懒得找了",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF7A2E00),
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // v1.2.4：开屏加载文字改为「数据正在初始化...」
            var dotCount by remember { mutableIntStateOf(0) }
            LaunchedEffect(Unit) {
                while (true) {
                    dotCount = (dotCount + 1) % 4
                    delay(400)
                }
            }
            Text(
                text = (uiText?.splashLoading?.ifBlank { "数据正在初始化" } ?: "数据正在初始化") + ".".repeat(dotCount),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SsGold,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = uiText?.splashSub?.ifBlank { "海量宝藏资源 · 免授权直享" } ?: "海量宝藏资源 · 免授权直享",
                fontSize = 12.sp,
                color = Color(0xFFFFE9A8).copy(alpha = 0.9f),
                letterSpacing = 2.sp
            )
        }
    }
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
