package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/* ============================================================
 *  动态卡通更新弹窗 —— 「小懒」音乐猫全程 Canvas 手绘动画
 *  状态流转：检测 → 发现新版本 → 下载(进度环) → 安装(火箭) → 完成(彩带)
 * ============================================================ */

// 弹窗专属配色（霓虹卡通风）
private val CuteCyan = Color(0xFF4DE3FF)
private val CutePurple = Color(0xFF8B5CF6)
private val CutePink = Color(0xFFFF5FA2)
private val CuteYellow = Color(0xFFFFD93D)
private val CuteOrange = Color(0xFFFF9F43)
private val CuteGreen = Color(0xFF3EE6A0)
private val InkDark = Color(0xFF3A2E45)
private val CreamTop = Color(0xFFFFE9BE)
private val CreamBottom = Color(0xFFFFCF8A)
private val BodyTop = Color(0xFFFFC97E)
private val BodyBottom = Color(0xFFFFB25E)

private enum class MascotMood { HAPPY, NEUTRAL, SAD }

@Composable
fun CartoonUpdateDialog(
    state: CartoonUpdateState,
    currentVersion: String,
    newVersion: String? = null,
    onStartDownload: () -> Unit,
    onInstall: () -> Unit,
    onOpenInstallSettings: () -> Unit,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    onDone: () -> Unit,
    onRestartApp: (() -> Unit)? = null,
    // v1.7.8：官方群按钮回调（强制更新弹窗内提供“官方群”入口）
    onOpenGroup: (() -> Unit)? = null
) {
    val info = (state as? CartoonUpdateState.Found)?.info
    val isForce = info?.forceUpdate == true

    // 入场动画：弹性缩放 + 淡入
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }
    val scale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.78f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMediumLow),
        label = "dialog_scale"
    )
    val dialogAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(220),
        label = "dialog_alpha"
    )

    val mood = when (state) {
        is CartoonUpdateState.Error -> MascotMood.SAD
        is CartoonUpdateState.Found, CartoonUpdateState.DownloadReady, CartoonUpdateState.Installing, is CartoonUpdateState.Done -> MascotMood.HAPPY
        else -> MascotMood.NEUTRAL
    }

    BackHandler(enabled = true, onBack = { if (!isForce && state !is CartoonUpdateState.Installing) onDismiss() })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.66f))
            .clickable(enabled = !isForce && state !is CartoonUpdateState.Installing) { onDismiss() }
            .testTag("cartoon_update_dialog"),
        contentAlignment = Alignment.Center
    ) {
        // ===== v1.8.9 最新动态 CSS 手绘弹窗（全新设计）=====
        Box(
            modifier = Modifier
                .width(340.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = dialogAlpha
                }
                .clickable(enabled = true, onClick = { /* 消费点击，阻止穿透 */ })
        ) {
            // 1) 动态流动渐变背景（CSS background 流动效果：色块随时间左右漂移）
            val bgFlow = rememberInfiniteTransition(label = "card_bg_flow")
            val bgPhase by bgFlow.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
                label = "card_bg_flow_phase"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1C1B2E), Color(0xFF33205E), Color(0xFF1E2A5A),
                                Color(0xFF4A2A6A), Color(0xFF241A3B), Color(0xFF1C1B2E)
                            ),
                            start = Offset(-320f + bgPhase * 640f, 0f),
                            end = Offset(320f + bgPhase * 640f, 520f)
                        )
                    )
            )
            // 2) 手绘描边（Canvas 抖动线条，涂鸦风，双层笔触）
            HandDrawnCardBorder(
                mainColor = Color.White.copy(alpha = 0.7f),
                accentColor = CutePink,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
            )
            // 3) 手绘涂鸦装饰（角落星光 / 波浪线 / 圆点，随 bgPhase 缓慢漂移）
            DoodleDecorations(
                modifier = Modifier.fillMaxSize(),
                phase = bgPhase
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // v1.8.7：CSS 式动态装饰——顶部流光扫过（与卡通角色叠放，不占额外布局）
            val shineTransition = rememberInfiniteTransition(label = "card_shine")
            val shinePhase by shineTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
                label = "card_shine_phase"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp),
                contentAlignment = Alignment.Center
            ) {
                // 流光扫过层（先绘制，透明渐变边缘）
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer { translationX = (shinePhase - 0.5f) * 760f }
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.055f),
                                    Color.White.copy(alpha = 0.11f),
                                    Color.White.copy(alpha = 0.055f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                // 手绘对话气泡（涂鸦风，动态弹跳）——CSS 手绘弹窗专属「叮咚」提示
                val bubble = rememberInfiniteTransition(label = "bubble_bounce")
                val bubbleDy by bubble.animateFloat(
                    initialValue = -1.5f,
                    targetValue = 1.5f,
                    animationSpec = infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
                    label = "bubble_dy"
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(x = 58.dp, y = 0.dp)
                        .graphicsLayer { translationY = bubbleDy }
                        .background(
                            color = Color.White.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 4.dp, bottomEnd = 14.dp, bottomStart = 14.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = CuteYellow.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(topStart = 14.dp, topEnd = 4.dp, bottomEnd = 14.dp, bottomStart = 14.dp)
                        )
                        .padding(horizontal = 11.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "叮咚～发现新版本啦！",
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                // 顶部：动态卡通角色（Canvas 全手绘）
                MusicCatMascot(
                    mood = mood,
                    isDownloading = state is CartoonUpdateState.Downloading,
                    modifier = Modifier
                        .size(184.dp, 162.dp)
                        .testTag("cartoon_mascot")
                )
            }

            // 标题（手绘涂鸦感：彩虹渐变 + 轻微浮动）
            val titleFloat = rememberInfiniteTransition(label = "title_float")
            val titleDy by titleFloat.animateFloat(
                initialValue = -2.5f,
                targetValue = 2.5f,
                animationSpec = infiniteRepeatable(tween(1300, easing = FastOutSlowInEasing), RepeatMode.Reverse),
                label = "title_dy"
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.graphicsLayer { translationY = titleDy }
            ) {
                Text(
                    text = "发现新版本",
                    style = TextStyle(
                        brush = Brush.linearGradient(listOf(CuteCyan, CutePurple, CutePink)),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                if (info?.isDemo == true) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("演示模式", fontSize = 10.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // 版本对照条：旧版 → 新版
            VersionCompareBar(
                oldVersion = currentVersion,
                newVersion = info?.versionName ?: newVersion ?: "新版本"
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 更新说明
            ReleaseNotesBox(notes = info?.releaseNotes ?: listOf("优化使用体验，修复已知问题"))
            Spacer(modifier = Modifier.height(12.dp))

            // 状态区（下载进度 / 火箭 / 彩带 / 错误 / 权限）
            when (state) {
                is CartoonUpdateState.Downloading -> {
                    DownloadProgressSection(
                        progress = state.progress,
                        downloaded = state.bytesDownloaded,
                        total = state.totalBytes
                    )
                }

                CartoonUpdateState.Installing -> {
                    InstallingSection()
                }

                is CartoonUpdateState.Done -> {
                    DoneSection(onDone = onDone, onRestartApp = { onRestartApp?.invoke() ?: onDone() })
                }

                is CartoonUpdateState.Error -> {
                    ErrorSection(
                        message = state.message,
                        canRetry = state.canRetry,
                        onRetry = onRetry,
                        onDismiss = onDismiss,
                        showDismiss = !isForce
                    )
                }

                CartoonUpdateState.NeedInstallPermission -> {
                    // v1.7.8：强制更新时不允许「暂不更新」跳过，只能去开权限或继续
                    NeedPermissionSection(
                        onOpenSettings = onOpenInstallSettings,
                        onInstall = onInstall,
                        onDismiss = onDismiss,
                        force = isForce
                    )
                }

                CartoonUpdateState.DownloadReady -> {
                    ReadyInstallSection(onInstall = onInstall)
                }

                CartoonUpdateState.Checking -> {
                    CheckingSection()
                }

                is CartoonUpdateState.Found -> {
                    // v1.7.8：强制更新时无“稍后再说”，只有【立即更新】；下方附【官方群】入口
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (!isForce) {
                            GradientButton(
                                text = "稍后再说",
                                gradient = listOf(Color(0xFF3A3A4E), Color(0xFF45455C)),
                                modifier = Modifier.weight(1f),
                                onClick = onDismiss
                            )
                        }
                        GradientButton(
                            text = "立即更新",
                            gradient = listOf(CuteCyan, CutePurple, CutePink),
                            modifier = Modifier.weight(if (isForce) 1f else 1.35f),
                            onClick = onStartDownload,
                            pulsing = true
                        )
                    }
                    // 官方群按钮（可选）：点击跳转官方 QQ 群
                    if (onOpenGroup != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        TextButtonGhost(
                            text = "💬 官方群 · 遇到问题来反馈",
                            onClick = onOpenGroup,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                CartoonUpdateState.Idle -> {}
            }
            }
        }
    }
}

/* ==================== 角色绘制 ==================== */

@Composable
private fun MusicCatMascot(
    mood: MascotMood,
    isDownloading: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "mascot")

    val bounce by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1100, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bounce"
    )
    val blink by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3400, easing = LinearEasing), RepeatMode.Restart),
        label = "blink"
    )
    val wave by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "wave"
    )
    val notePhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Restart),
        label = "note_phase"
    )
    val sparklePhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
        label = "sparkle_phase"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // 归一化画布 200x190
        val sx = w / 200f
        val sy = h / 190f
        val s = minOf(sx, sy)
        val originX = (w - 200f * s) / 2f + 2f * s
        val originY = (h - 190f * s) / 2f

        // 全体缩放 + 弹跳
        withTransformScope(s, originX, originY, bounce * 3f * s) {
            val blinkClosed = blink > 0.965f
            val eyeRy = when {
                blinkClosed -> 1.5f
                mood == MascotMood.SAD -> 5f
                else -> 13f
            }

            // ---- 耳机 ----
            drawArc(
                color = CutePurple,
                startAngle = 205f,
                sweepAngle = 130f,
                useCenter = false,
                topLeft = Offset(40f, 26f),
                size = Size(120f, 140f),
                style = Stroke(width = 11f, cap = StrokeCap.Round)
            )
            drawRoundRect(
                color = CutePurple,
                topLeft = Offset(28f, 82f),
                size = Size(22f, 42f),
                cornerRadius = CornerRadius(10f)
            )
            drawRoundRect(
                color = CutePurple,
                topLeft = Offset(150f, 82f),
                size = Size(22f, 42f),
                cornerRadius = CornerRadius(10f)
            )
            drawRoundRect(
                color = Color(0xFF4B3FA6),
                topLeft = Offset(33f, 90f),
                size = Size(12f, 16f),
                cornerRadius = CornerRadius(6f)
            )
            drawRoundRect(
                color = Color(0xFF4B3FA6),
                topLeft = Offset(155f, 90f),
                size = Size(12f, 16f),
                cornerRadius = CornerRadius(6f)
            )

            // ---- 耳朵 ----
            drawCircle(Brush.radialGradient(listOf(CreamTop, CreamBottom), center = Offset(56f, 52f), radius = 15f), radius = 15f, center = Offset(56f, 52f))
            drawCircle(Brush.radialGradient(listOf(CreamTop, CreamBottom), center = Offset(144f, 52f), radius = 15f), radius = 15f, center = Offset(144f, 52f))
            drawCircle(CutePink.copy(alpha = 0.75f), radius = 7f, center = Offset(56f, 54f))
            drawCircle(CutePink.copy(alpha = 0.75f), radius = 7f, center = Offset(144f, 54f))

            // ---- 头 ----
            drawCircle(
                brush = Brush.radialGradient(listOf(CreamTop, CreamBottom), center = Offset(100f, 84f), radius = 60f),
                radius = 56f,
                center = Offset(100f, 96f)
            )
            drawCircle(
                color = Color(0xFFE8A87C).copy(alpha = 0.55f),
                radius = 56f,
                center = Offset(100f, 96f),
                style = Stroke(width = 3f)
            )

            // ---- 眼睛 ----
            if (!blinkClosed) {
                // 眼睛高光
                drawCircle(Color.White.copy(alpha = 0.9f), radius = 3.6f, center = Offset(82f, 86f))
                drawCircle(Color.White.copy(alpha = 0.9f), radius = 3.6f, center = Offset(126f, 86f))
            }
            drawOval(
                color = InkDark,
                topLeft = Offset(68f, 94f - eyeRy),
                size = Size(20f, eyeRy * 2f)
            )
            drawOval(
                color = InkDark,
                topLeft = Offset(112f, 94f - eyeRy),
                size = Size(20f, eyeRy * 2f)
            )

            // ---- 腮红 ----
            drawOval(CutePink.copy(alpha = 0.5f), topLeft = Offset(48f, 106f), size = Size(18f, 11f))
            drawOval(CutePink.copy(alpha = 0.5f), topLeft = Offset(134f, 106f), size = Size(18f, 11f))

            // ---- 嘴巴 ----
            if (mood == MascotMood.SAD) {
                drawArc(
                    color = InkDark,
                    startAngle = 175f,
                    sweepAngle = 130f,
                    useCenter = false,
                    topLeft = Offset(86f, 106f),
                    size = Size(28f, 20f),
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )
            } else {
                drawArc(
                    color = InkDark,
                    startAngle = 20f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(84f, 100f),
                    size = Size(32f, 22f),
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )
            }

            // ---- 身体 ----
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(BodyTop, BodyBottom)),
                topLeft = Offset(60f, 130f),
                size = Size(80f, 52f),
                cornerRadius = CornerRadius(20f)
            )
            drawOval(Color(0xFFFFE4AE), topLeft = Offset(74f, 142f), size = Size(52f, 26f))

            // ---- 脚 ----
            drawOval(BodyBottom, topLeft = Offset(76f, 176f), size = Size(22f, 12f))
            drawOval(BodyBottom, topLeft = Offset(102f, 176f), size = Size(22f, 12f))

            // ---- 右臂（持音符）----
            drawLine(
                color = BodyTop,
                start = Offset(136f, 148f),
                end = Offset(160f, 126f),
                strokeWidth = 11f,
                cap = StrokeCap.Round
            )
            drawCircle(BodyTop, radius = 8f, center = Offset(160f, 126f))
            drawMusicNote(x = 170f, y = 108f, scale = 1.05f, color = CutePink, alpha = 1f)

            // ---- 左臂（挥手）----
            val waveDeg = wave * 22f
            rotate(waveDeg, pivot = Offset(64f, 148f)) {
                drawLine(
                    color = BodyTop,
                    start = Offset(64f, 148f),
                    end = Offset(36f, 122f),
                    strokeWidth = 11f,
                    cap = StrokeCap.Round
                )
                drawCircle(BodyTop, radius = 8f, center = Offset(36f, 122f))
            }
        }

        // ---- 漂浮音符（三枚，环绕头顶）----
        val noteColors = listOf(CuteCyan, CutePink, CuteYellow)
        repeat(3) { i ->
            val phase = (notePhase + i * 0.33f) % 1f
            val rise = phase * 56f * s
            val sway = sin((notePhase + i * 0.5f) * PI * 2f).toFloat() * 7f * s
            val alpha = when {
                phase < 0.15f -> phase / 0.15f
                phase > 0.8f -> (1f - phase) / 0.2f
                else -> 1f
            }
            val nx = originX + (if (i == 0) 30f else if (i == 1) 168f else 96f) * s + sway
            val ny = originY + (150f - rise) * s
            drawMusicNote(x = nx, y = ny, scale = (0.8f + phase * 0.4f) * s, color = noteColors[i], alpha = alpha.coerceIn(0f, 1f), rotation = phase * 40f)
        }

        // ---- 星光闪烁 ----
        val sparkleAlpha = (0.5f + 0.5f * sin(sparklePhase * PI * 2f).toFloat())
        drawSparkle(center = Offset(originX + 16f * s, originY + 28f * s), r = 6f * s, color = CuteYellow, alpha = sparkleAlpha.coerceIn(0.15f, 0.9f), rotation = sparklePhase * 90f)
        drawSparkle(center = Offset(originX + 186f * s, originY + 40f * s), r = 5f * s, color = CuteCyan, alpha = (1f - sparkleAlpha).coerceIn(0.15f, 0.9f), rotation = -sparklePhase * 90f)

        // 下载中：头顶小圆环
        if (isDownloading) {
            drawArc(
                color = Color.White.copy(alpha = 0.16f),
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = Offset(originX + 172f * s, originY + 6f * s),
                size = Size(26f * s, 26f * s),
                style = Stroke(width = 4f * s, cap = StrokeCap.Round)
            )
            drawArc(
                color = CuteCyan,
                startAngle = -90f, sweepAngle = 120f + bounce * 60f, useCenter = false,
                topLeft = Offset(originX + 172f * s, originY + 6f * s),
                size = Size(26f * s, 26f * s),
                style = Stroke(width = 4f * s, cap = StrokeCap.Round)
            )
        }
    }
}

/** 在归一化坐标系中绘制角色（缩放 + 平移 + 弹跳偏移） */
private fun DrawScope.withTransformScope(
    scale: Float,
    originX: Float,
    originY: Float,
    bounceDy: Float,
    block: DrawScope.() -> Unit
) {
    withTransform({
        translate(left = originX, top = originY + bounceDy)
        scale(scale, scale, pivot = Offset.Zero)
    }) {
        block()
    }
}

/** 手绘音符 ♪ */
private fun DrawScope.drawMusicNote(
    x: Float,
    y: Float,
    scale: Float,
    color: Color,
    alpha: Float = 1f,
    rotation: Float = 0f
) {
    rotate(rotation, pivot = Offset(x, y)) {
        val r = 6f * scale
        drawOval(color = color, topLeft = Offset(x - r, y - r * 2.4f), size = Size(r * 2f, r * 1.8f), alpha = alpha)
        drawRoundRect(
            color = color,
            topLeft = Offset(x + r * 0.55f, y - r * 2.4f),
            size = Size(r * 0.55f, r * 2.8f),
            cornerRadius = CornerRadius(r * 0.25f),
            alpha = alpha
        )
        val flag = Path().apply {
            moveTo(x + r * 1.0f, y - r * 2.4f)
            quadraticTo(x + r * 2.6f, y - r * 1.6f, x + r * 1.1f, y - r * 0.3f)
            lineTo(x + r * 1.05f, y - r * 0.5f)
            quadraticTo(x + r * 2.1f, y - r * 1.5f, x + r * 1.05f, y - r * 2.1f)
            close()
        }
        drawPath(flag, color, alpha = alpha)
    }
}

/** 手绘四角星芒 */
private fun DrawScope.drawSparkle(
    center: Offset,
    r: Float,
    color: Color,
    alpha: Float = 1f,
    rotation: Float
) {
    rotate(rotation, pivot = center) {
        val p = Path().apply {
            moveTo(center.x, center.y - r)
            quadraticTo(center.x + r * 0.22f, center.y - r * 0.22f, center.x + r, center.y)
            quadraticTo(center.x + r * 0.22f, center.y + r * 0.22f, center.x, center.y + r)
            quadraticTo(center.x - r * 0.22f, center.y + r * 0.22f, center.x - r, center.y)
            quadraticTo(center.x - r * 0.22f, center.y - r * 0.22f, center.x, center.y - r)
            close()
        }
        drawPath(p, color, alpha = alpha)
    }
}

/* ==================== 内容区块 ==================== */

@Composable
private fun VersionCompareBar(oldVersion: String, newVersion: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 旧版本
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Text(
                text = "v$oldVersion",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.55f),
                textDecoration = TextDecoration.LineThrough
            )
        }
        // 箭头
        Text("➜", color = CuteYellow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        // 新版本
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(CuteCyan, CutePurple, CutePink)),
                    RoundedCornerShape(50)
                )
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
            Text(
                text = "v$newVersion",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ReleaseNotesBox(notes: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.22f), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .heightIn(max = 108.dp)
            .verticalScroll(rememberScrollState())
    ) {
        notes.forEach { note ->
            Row(
                modifier = Modifier.padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Brush.linearGradient(listOf(CuteCyan, CutePink)), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = note,
                    color = Color.White.copy(alpha = 0.88f),
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

/* ==================== 状态区块 ==================== */

@Composable
private fun DownloadProgressSection(progress: Float, downloaded: Long, total: Long) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // v1.8.7：CSS 式动态进度条（渐变流动 + 高光扫过），进度条走满后直接进入安装
        GradientProgressBar(progress = progress, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        Box(contentAlignment = Alignment.Center) {
            ProgressRing(progress = progress, size = 92.dp)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                val mbText = if (total > 0) {
                    String.format("%.1f MB", downloaded / 1048576.0)
                } else ""
                val mbText2 = if (total > 0) " / " + String.format("%.1f MB", total / 1048576.0) else ""
                val fullMbText = mbText + mbText2
                Text(
                    text = fullMbText,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        EqualizerBars(modifier = Modifier.size(72.dp, 20.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "正在下载新版本，进度条走完将直接安装，请保持网络连接…",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 11.sp
        )
    }
}

/**
 * v1.8.7：CSS 式动态进度条——渐变底色流动 + 高光扫过（类似网页进度条动画），
 * 下载进度实时驱动，走满 100% 后立即进入安装流程。
 */
@Composable
private fun GradientProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(260),
        label = "bar_progress"
    )
    val transition = rememberInfiniteTransition(label = "grad_bar")
    val shimmer by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1500, easing = LinearEasing), RepeatMode.Restart),
        label = "bar_shimmer"
    )
    Box(
        modifier = modifier
            .height(12.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(Color.White.copy(alpha = 0.12f))
    ) {
        // 渐变填充（进度驱动宽度）
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(99.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(CuteCyan, CutePurple, CutePink, CuteYellow, CuteCyan)
                    )
                )
        )
        // 高光扫过（只扫填充区域）
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(99.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .graphicsLayer { translationX = (shimmer - 0.5f) * 420f }
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

@Composable
private fun ProgressRing(progress: Float, size: androidx.compose.ui.unit.Dp) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(300),
        label = "ring_progress"
    )
    Canvas(modifier = Modifier.size(size)) {
        val stroke = 7.dp.toPx()
        val inset = stroke / 2f
        val minDim = minOf(this.size.width, this.size.height)
        drawCircle(
            color = Color.White.copy(alpha = 0.12f),
            radius = minDim / 2f - inset,
            center = center,
            style = Stroke(width = stroke)
        )
        drawArc(
            brush = Brush.sweepGradient(
                listOf(CuteCyan, CutePurple, CutePink, CuteCyan),
                center = center
            ),
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = Size(this.size.width - inset * 2f, this.size.height - inset * 2f),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun EqualizerBars(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "eq")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "eq_time"
    )
    Canvas(modifier = modifier) {
        val barWidth = size.width / 7f
        repeat(5) { i ->
            val phase = (t + i * 0.18f) % 1f
            val h = (0.25f + 0.55f * abs(sin(phase * PI * 2f)).toFloat()) * size.height
            val x = (i + 1) * barWidth - barWidth / 2f
            val color = when (i) {
                0, 4 -> CuteCyan
                1, 3 -> CutePurple
                else -> CutePink
            }
            drawRoundRect(
                color = color,
                topLeft = Offset(x - barWidth / 4f, size.height - h),
                size = Size(barWidth / 2f, h),
                cornerRadius = CornerRadius(barWidth / 4f)
            )
        }
    }
}

@Composable
private fun InstallingSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        RocketLaunch(modifier = Modifier.size(120.dp, 96.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "正在安装新版本…（将自动替换旧版本）",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "安装完成即可体验全新功能 ✨",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 11.sp
        )
    }
}

@Composable
private fun RocketLaunch(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "rocket")
    val flame by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(160, easing = LinearEasing), RepeatMode.Reverse),
        label = "flame"
    )
    val shake by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(90, easing = LinearEasing), RepeatMode.Restart),
        label = "shake"
    )
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val top = 6f
        val bodyW = 34f
        val bodyH = 52f
        val shakeX = (if (shake > 0.5f) 1.6f else -1.6f)

        rotate(shakeX, pivot = Offset(cx, top + bodyH / 2f)) {
            // 火焰
            val flameLen = 14f + flame * 14f
            val flamePath = Path().apply {
                moveTo(cx - 10f, top + bodyH)
                quadraticTo(cx - 6f, top + bodyH + flameLen * 0.55f, cx, top + bodyH + flameLen)
                quadraticTo(cx + 6f, top + bodyH + flameLen * 0.55f, cx + 10f, top + bodyH)
                close()
            }
            drawPath(flamePath, Brush.linearGradient(listOf(CuteYellow, CuteOrange)))

            // 机身
            drawRoundRect(
                color = Color(0xFFE9E9F4),
                topLeft = Offset(cx - bodyW / 2f, top),
                size = Size(bodyW, bodyH),
                cornerRadius = CornerRadius(12f)
            )
            // 舷窗
            drawCircle(CuteCyan.copy(alpha = 0.25f), radius = 9f, center = Offset(cx, top + 22f))
            drawCircle(Color(0xFF9BE8FF), radius = 5.5f, center = Offset(cx, top + 22f))
            // 鼻锥
            val nose = Path().apply {
                moveTo(cx, top - 16f)
                lineTo(cx - bodyW / 2f, top + 6f)
                lineTo(cx + bodyW / 2f, top + 6f)
                close()
            }
            drawPath(nose, Brush.linearGradient(listOf(CutePink, CutePurple)))
            // 尾翼
            val finL = Path().apply {
                moveTo(cx - bodyW / 2f, top + bodyH - 14f)
                lineTo(cx - bodyW / 2f - 12f, top + bodyH + 2f)
                lineTo(cx - bodyW / 2f, top + bodyH)
                close()
            }
            val finR = Path().apply {
                moveTo(cx + bodyW / 2f, top + bodyH - 14f)
                lineTo(cx + bodyW / 2f + 12f, top + bodyH + 2f)
                lineTo(cx + bodyW / 2f, top + bodyH)
                close()
            }
            drawPath(finL, CuteOrange)
            drawPath(finR, CuteOrange)
        }
    }
}

@Composable
private fun DoneSection(onDone: () -> Unit, onRestartApp: () -> Unit) {
    var burst by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { burst = true }
    val progress by animateFloatAsState(
        targetValue = if (burst) 1f else 0f,
        animationSpec = tween(1400, easing = FastOutSlowInEasing),
        label = "confetti"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            ConfettiBurst(progress = progress, modifier = Modifier.size(140.dp, 72.dp))
        }
        Text(
            text = "🎉 更新完成！",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "新版本已安装成功，重启应用即可体验全新功能 ✨",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 10.5.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        GradientButton(
            text = "立即重启",
            gradient = listOf(CuteGreen, CuteCyan),
            modifier = Modifier.fillMaxWidth(),
            onClick = onRestartApp,
            pulsing = true
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextButtonGhost(text = "稍后再说", onClick = onDone)
    }
}

@Composable
private fun ConfettiBurst(progress: Float, modifier: Modifier = Modifier) {
    val particles = remember {
        List(24) { i ->
            val angle = Math.toRadians((i * 360f / 24f + 15f).toDouble())
            ConfettiParticle(
                angle = angle,
                speed = 60f + (i % 5) * 14f,
                size = 5f + (i % 3) * 2f,
                color = listOf(CuteCyan, CutePink, CuteYellow, CuteGreen, CutePurple, CuteOrange)[i % 6]
            )
        }
    }
    Canvas(modifier = modifier) {
        particles.forEach { p ->
            val t = progress.coerceIn(0f, 1f)
            val dist = p.speed * t
            val x = center.x + cos(p.angle).toFloat() * dist
            val y = center.y + sin(p.angle).toFloat() * dist * 0.85f + t * t * 42f
            val alpha = (1f - t).coerceIn(0f, 1f)
            rotate(t * 540f, pivot = Offset(x, y)) {
                drawRect(
                    color = p.color,
                    topLeft = Offset(x - p.size / 2f, y - p.size / 2f),
                    size = Size(p.size, p.size),
                    alpha = alpha
                )
            }
        }
    }
}

private data class ConfettiParticle(
    val angle: Double,
    val speed: Float,
    val size: Float,
    val color: Color
)

@Composable
private fun ErrorSection(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    showDismiss: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = message,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (showDismiss) {
                GradientButton(
                    text = "稍后再说",
                    gradient = listOf(Color(0xFF3A3A4E), Color(0xFF45455C)),
                    modifier = Modifier.weight(1f),
                    onClick = onDismiss
                )
            }
            if (canRetry) {
                GradientButton(
                    text = "重试",
                    gradient = listOf(CuteCyan, CutePurple, CutePink),
                    modifier = Modifier.weight(1.2f),
                    onClick = onRetry,
                    pulsing = true
                )
            }
        }
    }
}

@Composable
private fun NeedPermissionSection(
    onOpenSettings: () -> Unit,
    onInstall: () -> Unit,
    onDismiss: () -> Unit,
    force: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "安装未完成，可点击下方按钮重新尝试\n系统将自动调用安装器完成安装",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp,
            lineHeight = 19.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        GradientButton(
            text = "重新安装",
            gradient = listOf(CuteCyan, CutePurple, CutePink),
            modifier = Modifier.fillMaxWidth(),
            onClick = onInstall,
            pulsing = true
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "点击后将自动继续安装流程",
            color = Color.White.copy(alpha = 0.45f),
            fontSize = 10.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // v1.7.8：强制更新时隐藏「暂不更新」，只能去开权限或继续安装
            if (!force) {
                TextButtonGhost(text = "暂不更新", onClick = onDismiss, modifier = Modifier.weight(1f))
            }
            GradientButton(
                text = "重新安装",
                gradient = listOf(CuteGreen, CuteCyan),
                modifier = Modifier.weight(1.2f),
                onClick = onInstall,
                pulsing = true
            )
        }
    }
}

@Composable
private fun ReadyInstallSection(onInstall: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "新版本已下载完成 🎁",
            color = CuteYellow,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        GradientButton(
            text = "立即安装",
            gradient = listOf(CuteGreen, CuteCyan, CutePurple),
            modifier = Modifier.fillMaxWidth(),
            onClick = onInstall,
            pulsing = true
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "安装时将自动卸载旧版本并安装新版本",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 10.5.sp
        )
    }
}

@Composable
private fun CheckingSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "正在检测最新版本…",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
    }
}

/* ==================== 通用组件 ==================== */

@Composable
private fun GradientButton(
    text: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    pulsing: Boolean = false
) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse_scale"
    )
    val scale = if (pulsing) pulse else 1f
    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .background(
                brush = Brush.horizontalGradient(gradient),
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() }
            .padding(vertical = 13.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun TextButtonGhost(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.White.copy(alpha = 0.6f),
        fontSize = 12.sp,
        modifier = modifier
            .clickable { onClick() }
            .padding(6.dp)
    )
}

/* ==================== 私有工具 ==================== */

/* ==================== v1.8.9 全新手绘 CSS 风格组件 ==================== */

/**
 * 手绘描边卡片边框：Canvas 抖动线条绘制涂鸦风圆角矩形，
 * 外加双层笔触与两枚「连接点」圆点角标，模拟真实手绘感。
 */
@Composable
private fun HandDrawnCardBorder(
    mainColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val inset = 4.dp.toPx()
        val j = 2.2f
        // 主描边（轻微抖动）
        val p = Path().apply {
            moveTo(inset + 10f, inset + j)
            quadraticTo(inset + w / 2f, inset - j, inset + w - 12f, inset + j)
            quadraticTo(inset + w + j, inset + 16f, inset + w - j, inset + h / 2f)
            quadraticTo(inset + w + j, inset + h - 16f, inset + w - 12f, inset + h - j)
            quadraticTo(inset + w / 2f, inset + h + j, inset + 12f, inset + h - j)
            quadraticTo(inset - j, inset + h - 16f, inset + j, inset + h / 2f)
            quadraticTo(inset - j, inset + 16f, inset + 10f, inset + j)
            close()
        }
        drawPath(p, color = mainColor.copy(alpha = 0.8f), style = Stroke(width = 2.2f, cap = StrokeCap.Round))
        // 第二层更淡的描边：制造「画了两笔」的手绘感
        val p2 = Path().apply {
            moveTo(inset + 18f, inset + 6f)
            quadraticTo(inset + w / 2f, inset + 2f, inset + w - 20f, inset + 6f)
            quadraticTo(inset + w - 2f, inset + 20f, inset + w - 6f, inset + h / 2f)
            quadraticTo(inset + w - 2f, inset + h - 20f, inset + w - 20f, inset + h - 6f)
            quadraticTo(inset + w / 2f, inset + h - 2f, inset + 20f, inset + h - 6f)
            quadraticTo(inset + 2f, inset + h - 20f, inset + 6f, inset + h / 2f)
            quadraticTo(inset + 2f, inset + 20f, inset + 18f, inset + 6f)
            close()
        }
        drawPath(p2, color = accentColor.copy(alpha = 0.45f), style = Stroke(width = 1.4f, cap = StrokeCap.Round))
        // 手绘「连接点」圆点角标
        drawCircle(accentColor.copy(alpha = 0.9f), radius = 3.2f, center = Offset(inset + 10f, inset + 8f))
        drawCircle(CuteYellow.copy(alpha = 0.9f), radius = 2.6f, center = Offset(inset + w - 12f, inset + h - 8f))
    }
}

/**
 * 手绘涂鸦装饰：角落星光 + 波浪线 + 圆点（随 bgPhase 缓慢漂移，CSS 粒子感）
 */
@Composable
private fun DoodleDecorations(modifier: Modifier = Modifier, phase: Float = 0f) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val drift = phase * 10f
        // 左上角星光
        drawSparkle(center = Offset(22f, 26f + drift * 0.3f), r = 6f, color = CuteYellow.copy(alpha = 0.8f), rotation = phase * 40f)
        // 右上角星光
        drawSparkle(center = Offset(w - 24f, 22f - drift * 0.2f), r = 5f, color = CuteCyan.copy(alpha = 0.7f), rotation = -phase * 50f)
        // 底部波浪线（手绘涂鸦）
        val wave = Path().apply {
            moveTo(24f, h - 26f)
            for (i in 0 until 8) {
                val x0 = 24f + i * (w - 48f) / 8f
                val x1 = 24f + (i + 1) * (w - 48f) / 8f
                val y = h - 26f + (if (i % 2 == 0) 4f else -4f) + drift * 0.1f
                quadraticTo((x0 + x1) / 2f, y, x1, h - 26f)
            }
        }
        drawPath(wave, color = CuteGreen.copy(alpha = 0.45f), style = Stroke(width = 1.6f, cap = StrokeCap.Round))
        // 底部圆点虚线（漂移）
        repeat(6) { i ->
            val x = 30f + i * 52f + phase * 6f
            drawCircle(Color.White.copy(alpha = 0.18f), radius = 2f, center = Offset(x % (w - 40f) + 20f, h - 12f))
        }
    }
}
