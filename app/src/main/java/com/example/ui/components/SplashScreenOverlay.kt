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
import com.example.ui.theme.LocalThemeUiColors
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
    var countdownSeconds by remember {
        // v1.7.20：固定 5 秒倒计时（用户需求，不再跟随云端配置）
        mutableIntStateOf(5)
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
            CubeLoaderSplashContent(
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
 * 3D 立方体加载器启动页 (Cube Loader Splash Screen)
 * 基于 CSS .cube-loader preserve-3d / rotateX(-30deg) / 4s rotateY 无限旋转
 * ===================================================================== */

private val CubeSideGradientStops = arrayOf(
    0.000f to Color(0xFF423E40), // hsl(330, 3.13%, 25.1%) 0%
    0.055f to Color(0xFF406462), // hsl(177.27, 21.71%, 32.06%) 5.5%
    0.121f to Color(0xFF3E7E7B), // hsl(176.67, 34.1%, 36.88%) 12.1%
    0.196f to Color(0xFF3C948F), // hsl(176.61, 42.28%, 40.7%) 19.6%
    0.279f to Color(0xFF3AA6A0), // hsl(176.63, 48.32%, 43.88%) 27.9%
    0.366f to Color(0xFF38B6AF), // hsl(176.66, 53.07%, 46.58%) 36.6%
    0.456f to Color(0xFF36C4BC), // hsl(176.7, 56.94%, 48.91%) 45.6%
    0.546f to Color(0xFF34D0C7), // hsl(176.74, 62.39%, 50.91%) 54.6%
    0.634f to Color(0xFF32DBD1), // hsl(176.77, 69.86%, 52.62%) 63.4%
    0.717f to Color(0xFF30E4DA), // hsl(176.8, 76.78%, 54.08%) 71.7%
    0.794f to Color(0xFF2EECE2), // hsl(176.83, 83.02%, 55.29%) 79.4%
    0.862f to Color(0xFF2DF2E8), // hsl(176.85, 88.44%, 56.28%) 86.2%
    0.919f to Color(0xFF2CF7ED), // hsl(176.86, 92.9%, 57.04%) 91.9%
    0.963f to Color(0xFF2BFBF0), // hsl(176.88, 96.24%, 57.59%) 96.3%
    0.990f to Color(0xFF2AFDF2), // hsl(176.88, 98.34%, 57.93%) 99%
    1.000f to Color(0xFF2AFEF3)  // hsl(176.89, 99.07%, 58.04%) 100%
)

@Composable
fun CubeLoader3D(
    modifier: Modifier = Modifier,
    sizeDp: androidx.compose.ui.unit.Dp = 88.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cube_loader_trans")
    val rotYDeg by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cube_rot_y"
    )

    Canvas(
        modifier = modifier.size(sizeDp * 2.3f)
    ) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f - 8.dp.toPx()

        val cubeSide = sizeDp.toPx()
        val half = cubeSide / 2f
        val focalLength = cubeSide * 6.5f

        val rotXRad = Math.toRadians(-30.0).toFloat()
        val cosX = kotlin.math.cos(rotXRad)
        val sinX = kotlin.math.sin(rotXRad)

        val rotYRad = Math.toRadians(rotYDeg.toDouble()).toFloat()
        val cosY = kotlin.math.cos(rotYRad)
        val sinY = kotlin.math.sin(rotYRad)

        fun project(x: Float, y: Float, z: Float): Triple<Float, Float, Float> {
            val x1 = x * cosY + z * sinY
            val y1 = y
            val z1 = -x * sinY + z * cosY
            val x2 = x1
            val y2 = y1 * cosX - z1 * sinX
            val z2 = y1 * sinX + z1 * cosX
            val scale = focalLength / (focalLength + z2)
            val sx = cx + x2 * scale
            val sy = cy + y2 * scale
            return Triple(sx, sy, z2)
        }

        // --- 1. 底面投影发光 (.cube-top::before) ---
        val floorY = half + 42.dp.toPx()
        val shadowCenter = project(0f, floorY, 0f)

        // 柔和外发光光晕
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF2AFEF3).copy(alpha = 0.28f),
                    Color(0xFF3C948F).copy(alpha = 0.16f),
                    Color(0xFF323232).copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = Offset(shadowCenter.first, shadowCenter.second),
                radius = half * 2.4f
            ),
            radius = half * 2.4f,
            center = Offset(shadowCenter.first, shadowCenter.second)
        )

        // 核心青绿高光
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF2AFEF3).copy(alpha = 0.60f),
                    Color(0xFF3C948F).copy(alpha = 0.35f),
                    Color.Transparent
                ),
                center = Offset(shadowCenter.first, shadowCenter.second),
                radius = half * 1.4f
            ),
            radius = half * 1.4f,
            center = Offset(shadowCenter.first, shadowCenter.second)
        )

        // 底板阴影盒
        val shadowCorners = listOf(
            project(-half * 1.05f, floorY, -half * 1.05f),
            project(half * 1.05f, floorY, -half * 1.05f),
            project(half * 1.05f, floorY, half * 1.05f),
            project(-half * 1.05f, floorY, half * 1.05f)
        )
        val shadowPath = Path().apply {
            moveTo(shadowCorners[0].first, shadowCorners[0].second)
            for (k in 1..3) {
                lineTo(shadowCorners[k].first, shadowCorners[k].second)
            }
            close()
        }
        drawPath(
            path = shadowPath,
            color = Color(0xFF3C948F).copy(alpha = 0.40f)
        )

        // --- 2. 侧面 (4面，--i = 0..3) ---
        val sideFaceVertices = listOf(
            listOf(Triple(-half, -half, half), Triple(half, -half, half), Triple(half, half, half), Triple(-half, half, half)),
            listOf(Triple(half, -half, half), Triple(half, -half, -half), Triple(half, half, -half), Triple(half, half, half)),
            listOf(Triple(half, -half, -half), Triple(-half, -half, -half), Triple(-half, half, -half), Triple(half, half, -half)),
            listOf(Triple(-half, -half, -half), Triple(-half, -half, half), Triple(-half, half, half), Triple(-half, half, -half))
        )

        data class RenderFace(
            val path: Path,
            val brush: Brush,
            val avgZ: Float
        )

        val facesToDraw = mutableListOf<RenderFace>()

        for (i in 0..3) {
            val verts = sideFaceVertices[i]
            val p0 = project(verts[0].first, verts[0].second, verts[0].third)
            val p1 = project(verts[1].first, verts[1].second, verts[1].third)
            val p2 = project(verts[2].first, verts[2].second, verts[2].third)
            val p3 = project(verts[3].first, verts[3].second, verts[3].third)

            val cp = (p1.first - p0.first) * (p2.second - p1.second) - (p1.second - p0.second) * (p2.first - p1.first)
            if (cp > 0.05f) {
                val path = Path().apply {
                    moveTo(p0.first, p0.second)
                    lineTo(p1.first, p1.second)
                    lineTo(p2.first, p2.second)
                    lineTo(p3.first, p3.second)
                    close()
                }
                val avgZ = (p0.third + p1.third + p2.third + p3.third) / 4f
                val topCenter = Offset((p0.first + p1.first) / 2f, (p0.second + p1.second) / 2f)
                val botCenter = Offset((p2.first + p3.first) / 2f, (p2.second + p3.second) / 2f)

                val gradient = Brush.linearGradient(
                    colorStops = CubeSideGradientStops,
                    start = topCenter,
                    end = botCenter
                )

                facesToDraw.add(RenderFace(path, gradient, avgZ))
            }
        }

        // --- 3. 顶面 (.cube-top, rotateX(90deg) translateZ(37.5px)) ---
        val topFaceVertices = listOf(
            Triple(-half, -half, -half),
            Triple(half, -half, -half),
            Triple(half, -half, half),
            Triple(-half, -half, half)
        )
        val tp0 = project(topFaceVertices[0].first, topFaceVertices[0].second, topFaceVertices[0].third)
        val tp1 = project(topFaceVertices[1].first, topFaceVertices[1].second, topFaceVertices[1].third)
        val tp2 = project(topFaceVertices[2].first, topFaceVertices[2].second, topFaceVertices[2].third)
        val tp3 = project(topFaceVertices[3].first, topFaceVertices[3].second, topFaceVertices[3].third)

        val topPath = Path().apply {
            moveTo(tp0.first, tp0.second)
            lineTo(tp1.first, tp1.second)
            lineTo(tp2.first, tp2.second)
            lineTo(tp3.first, tp3.second)
            close()
        }
        val topBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF484245),
                Color(0xFF423E40),
                Color(0xFF383436)
            ),
            start = Offset(tp0.first, tp0.second),
            end = Offset(tp2.first, tp2.second)
        )

        // 绘制可见侧面（按深度排序绘制）
        facesToDraw.sortBy { it.avgZ }
        for (face in facesToDraw) {
            drawPath(face.path, face.brush)
            drawPath(
                face.path,
                color = Color(0xFF2AFEF3).copy(alpha = 0.35f),
                style = Stroke(width = 0.8.dp.toPx())
            )
        }

        // 绘制顶面
        drawPath(topPath, topBrush)
        drawPath(
            topPath,
            color = Color(0xFF5E5458).copy(alpha = 0.65f),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@Composable
/* =====================================================================
 * v1.7.20：打字机启动页（用户需求）
 * - 核心文字「懒得找了」打字机效果呈现，无多余文字
 * - 倒计时只显示数字（无「跳过」），固定 5 秒后进入
 * - 背景/文字跟随主题（更新弹窗除外）
 * ===================================================================== */
private fun CubeLoaderSplashContent(
    countdownSeconds: Int,
    onDismiss: () -> Unit,
    entryScale: Animatable<Float, *>,
    entryAlpha: Animatable<Float, *>,
    uiText: com.example.data.remote.UiTextDto? = null
) {
    val theme = LocalThemeUiColors.current
    val fullText = "懒得找了"
    var visibleChars by remember { mutableIntStateOf(0) }
    var cursorVisible by remember { mutableStateOf(true) }

    // 打字机效果：每 350ms 出现一个字
    LaunchedEffect(Unit) {
        while (visibleChars < fullText.length) {
            delay(350)
            visibleChars++
        }
    }
    // 光标闪烁
    LaunchedEffect(Unit) {
        while (true) {
            delay(530)
            cursorVisible = !cursorVisible
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(theme.backgroundGradient))
    ) {
        // 倒计时：只显示数字（右上角，无「跳过」）
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 16.dp, end = 20.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.65f))
                .border(1.dp, theme.primary.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$countdownSeconds",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = theme.primary
            )
        }

        // 中央：打字机「懒得找了」
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .scale(entryScale.value)
                .graphicsLayer { alpha = entryAlpha.value }
                .padding(horizontal = 28.dp)
        ) {
            Text(
                text = fullText.take(visibleChars) + if (cursorVisible && visibleChars < fullText.length) "▌" else "",
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = theme.text,
                letterSpacing = 6.sp
            )
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
