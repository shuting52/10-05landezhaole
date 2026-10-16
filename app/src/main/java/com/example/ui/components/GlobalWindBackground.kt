package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalUiverseState
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.DesignStyle
import com.example.ui.components.ComponentThemeResolver
import com.example.ui.components.LocalComponentThemes
import com.example.ui.uiverse.PatternStylePreset
import com.example.ui.uiverse.UiKitPreset
import com.example.ui.uiverse.DynamicEffectPreset
import com.example.ui.uiverse.RingEffectConfigState
import androidx.compose.animation.core.LinearEasing
import kotlin.math.cos
import kotlin.math.sin

// Uiverse.io Warm Peach Wind Gradient Palette
val WindPeach1 = Color(0xFFFEC195)
val WindPeach2 = Color(0xFFFCC196)
val WindPeach3 = Color(0xFFFABD92)
val WindPeach4 = Color(0xFFFAC097)
val WindPeach5 = Color(0xFFFAC39C)

/**
 * 全局胶囊化风动背景（Global Capsule Wind Background）— v1.5.4 重写版
 * - 主题应用机制重写：直接从 LocalThemeUiColors 读色，不再靠参数传递，杜绝中间断链
 * - 背景基色恒为主题 background，装饰层全部从主题色派生，切换主题必变色
 * - 保留：动态胶囊、媒体背景、设计风格、动态效果
 */
@Composable
fun GlobalWindBackground(
    modifier: Modifier = Modifier,
    bgMediaType: String = "none",
    bgMediaUrl: String = "",
    dynamicEffect: DynamicEffectPreset = DynamicEffectPreset.NONE,
    content: @Composable BoxScope.() -> Unit
) {
    val theme = LocalThemeUiColors.current
    // v1.5.4：主题色直读，不再经参数传递
    val themeBgColor: Color = theme.background
    val themePrimaryColor: Color = theme.primary
    // v1.5.6：背景渐变用 backgroundGradient（主题 gradientColors），不再误用 cardGradient（卡片色）
    val themeGradientColors: List<Color> = theme.backgroundGradient
    val designStyle: DesignStyle = theme.designStyle
    val uiverse = LocalUiverseState.current
    val infiniteTransition = rememberInfiniteTransition(label = "capsule_wind_background")

    // 1. 胶囊流光与漂移核心动画 (周期 2.8s 平滑往复)
    val windShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "windShift"
    )

    // 2. 悬浮微胶囊群轻摆摇曳角度 (3.2s)
    val slay1Angle by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "slay1"
    )

    // 3. 辅动微胶囊摆角 (2.6s)
    val slay2Angle by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "slay2"
    )

    // 4. 浮光呼吸脉冲 (2.2s)
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    // 5. 全站液体玻璃连续流光与环形旋转相位 (0..2π)
    val ringSpinPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringSpinPhase"
    )

    val ringAmplitude = RingEffectConfigState.amplitude

    val activePrimary = themePrimaryColor
    // v1.1.10：控制台「主题工具箱」global 组件可覆盖全局背景色（优先级：控制台 > 主题预设 > 默认）
    val globalComp = ComponentThemeResolver.resolve(LocalComponentThemes.current, "global")
    // v1.1.22：global.css 纯白背景（默认配置）视为未定制，强制恢复经典皮肤胶囊渐变背景，不再整页白底
    val globalBg = globalComp?.backgroundColor
    val effectiveGlobalBg = if (globalBg != null && globalBg != Color.White) globalBg else null
    val activeBg = effectiveGlobalBg ?: themeBgColor

    Box(modifier = modifier.fillMaxSize()) {
        // ========== 1. 底层动态全屏画布：渲染多层悬浮流动胶囊与微光粒子群 ==========
        when {
            // v1.2.0：动态效果主题优先（环形特效，可叠加在任意主题上）
            dynamicEffect != DynamicEffectPreset.NONE -> {
                DynamicEffectBackground(
                    effect = dynamicEffect,
                    baseColor = themeBgColor,
                    accent = activePrimary,
                    windShift = windShift,
                    slay1Angle = slay1Angle,
                    slay2Angle = slay2Angle,
                    glowPulse = glowPulse,
                    ringSpinPhase = ringSpinPhase,
                    amplitude = ringAmplitude,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // 🎨 14大艺术与设计流派专属背景渲染（覆盖拟物/扁平/Material/新拟态/玻璃拟态/粘土拟态/液态玻璃/新粗野/极简/包豪斯/复古未来/粗野/反设计/酸性设计）
            designStyle != DesignStyle.DEFAULT -> {
                DesignStyleBackground(
                    style = designStyle,
                    baseColor = activeBg,
                    primaryColor = activePrimary,
                    secondaryColor = themePrimaryColor,
                    windShift = windShift,
                    slay1Angle = slay1Angle,
                    slay2Angle = slay2Angle,
                    glowPulse = glowPulse,
                    ringSpinPhase = ringSpinPhase,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // v1.2.0：渐变颜色主题（柔和渐变背景）
            themeGradientColors.size >= 2 -> {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(themeGradientColors[0], themeGradientColors[1]),
                            start = Offset.Zero,
                            end = Offset(size.width, size.height)
                        )
                    )
                }
            }
            uiverse.patternStyle == PatternStylePreset.CYBER_GRID || uiverse.activeKit == UiKitPreset.CYBERPUNK_NEON -> {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(Color(0xFF070913))
                    val gridSize = 36.dp.toPx()
                    val cols = (size.width / gridSize).toInt() + 2
                    val rows = (size.height / gridSize).toInt() + 2
                    for (i in 0..cols) {
                        drawLine(
                            color = Color(0xFF00F0FF).copy(alpha = 0.07f),
                            start = Offset(i * gridSize, 0f),
                            end = Offset(i * gridSize, size.height)
                        )
                    }
                    for (j in 0..rows) {
                        drawLine(
                            color = Color(0xFFFF0055).copy(alpha = 0.05f),
                            start = Offset(0f, j * gridSize + (windShift * gridSize % gridSize)),
                            end = Offset(size.width, j * gridSize + (windShift * gridSize % gridSize))
                        )
                    }
                    // 赛博朋克风：大型悬浮荧光胶囊
                    drawCapsule(
                        center = Offset(size.width * 0.5f, size.height * 0.35f),
                        length = size.width * 0.88f,
                        thickness = 130.dp.toPx(),
                        angleDegrees = -18f + windShift * 4f,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF00F0FF).copy(alpha = 0.18f), Color.Transparent)
                        )
                    )
                    drawCapsule(
                        center = Offset(size.width * 0.65f, size.height * 0.72f),
                        length = size.width * 0.75f,
                        thickness = 100.dp.toPx(),
                        angleDegrees = 24f - windShift * 5f,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFF0055).copy(alpha = 0.14f), Color.Transparent)
                        )
                    )
                }
            }
            uiverse.patternStyle == PatternStylePreset.DOT_MATRIX || uiverse.activeKit == UiKitPreset.NEO_BRUTALISM_POP -> {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(Color(0xFFFFFEE8))
                    val spacing = 28.dp.toPx()
                    val cols = (size.width / spacing).toInt() + 1
                    val rows = (size.height / spacing).toInt() + 1
                    for (i in 0..cols) {
                        for (j in 0..rows) {
                            drawCircle(
                                color = Color.Black.copy(alpha = 0.09f),
                                radius = 2.dp.toPx(),
                                center = Offset(i * spacing, j * spacing)
                            )
                        }
                    }
                    // 新粗野风：带有黑描边的醒目黄色/粉色实心大胶囊
                    drawCapsule(
                        center = Offset(size.width * 0.5f, size.height * 0.30f),
                        length = size.width * 0.85f,
                        thickness = 120.dp.toPx(),
                        angleDegrees = -12f,
                        color = Color(0xFFFFDF00).copy(alpha = 0.35f)
                    )
                    drawCapsule(
                        center = Offset(size.width * 0.5f, size.height * 0.30f),
                        length = size.width * 0.85f,
                        thickness = 120.dp.toPx(),
                        angleDegrees = -12f,
                        color = Color.Black.copy(alpha = 0.25f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
            uiverse.patternStyle == PatternStylePreset.HEXAGON_MESH || uiverse.activeKit == UiKitPreset.GLASSMORPHISM_AURORA -> {
                // v1.1.11：透明磨砂改胶囊——白色底 + 柔和半透明胶囊（非深色磨砂玻璃）
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(Color(0xFFFFFFFF))
                    // 极光渐变胶囊体
                    drawCapsule(
                        center = Offset(size.width * (0.35f + windShift * 0.1f), size.height * 0.28f),
                        length = size.width * 0.95f,
                        thickness = 160.dp.toPx(),
                        angleDegrees = -25f,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF6366F1).copy(alpha = 0.14f), Color.Transparent)
                        )
                    )
                    drawCapsule(
                        center = Offset(size.width * (0.68f - windShift * 0.1f), size.height * 0.75f),
                        length = size.width * 0.85f,
                        thickness = 140.dp.toPx(),
                        angleDegrees = 20f,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFEC4899).copy(alpha = 0.10f), Color.Transparent)
                        )
                    )
                }
            }
            uiverse.customStyle?.backgroundBrush != null -> {
                Box(modifier = Modifier.fillMaxSize().background(uiverse.customStyle.backgroundBrush))
            }
            // v1.1.22：纯白背景（global.css 默认）视为未定制，落入经典胶囊渐变分支
            uiverse.customStyle?.backgroundColor != null && uiverse.customStyle.backgroundColor != Color.White -> {
                Box(modifier = Modifier.fillMaxSize().background(uiverse.customStyle.backgroundColor))
            }
            else -> {
                // 国风山水动态背景（远山如黛 · 层峦叠翠 · 烟波浩渺 · 竹影青鹤）
                val base = themeBgColor
                val accent = themePrimaryColor
                // v1.4.11 修复：主题切换背景跟随——此前 jadeGreen/goldGlint/底衬渐变写死国风绿，
                // 无渐变主题（如经典科技蓝）切换后背景仍显示绿色山水。现全部从主题色派生。
                val jadeGreen = accent
                val goldGlint = accent

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // 1. 山水底衬水墨晕染渐变
                    val startX = -width * 0.1f + windShift * width * 0.2f
                    val startY = 0f + windShift * height * 0.08f
                    val endX = width * 1.1f
                    val endY = height * 1.0f

                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                base,
                                lerp(base, accent, 0.07f),
                                lerp(base, accent, 0.04f),
                                base.copy(alpha = 0.95f)
                            ),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY)
                        )
                    )

                    // 2. 远山如黛（层叠山脊 1：轻柔青墨远山，平缓起伏）
                    val mtnShift1 = (windShift - 0.5f) * width * 0.04f
                    val mtnPath1 = Path().apply {
                        moveTo(-20f, height * 0.62f)
                        cubicTo(
                            width * 0.22f + mtnShift1, height * 0.48f,
                            width * 0.42f + mtnShift1, height * 0.56f,
                            width * 0.62f + mtnShift1, height * 0.46f
                        )
                        cubicTo(
                            width * 0.78f + mtnShift1, height * 0.38f,
                            width * 0.90f + mtnShift1, height * 0.52f,
                            width + 20f, height * 0.50f
                        )
                        lineTo(width + 20f, height)
                        lineTo(-20f, height)
                        close()
                    }
                    drawPath(
                        path = mtnPath1,
                        brush = Brush.verticalGradient(
                            listOf(
                                accent.copy(alpha = 0.12f),
                                jadeGreen.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            startY = height * 0.42f,
                            endY = height * 0.85f
                        )
                    )

                    // 3. 近峰叠翠（层叠山脊 2：苍翠奇峰，峰峦起伏）
                    val mtnShift2 = -(windShift - 0.5f) * width * 0.06f
                    val mtnPath2 = Path().apply {
                        moveTo(-20f, height * 0.72f)
                        cubicTo(
                            width * 0.18f + mtnShift2, height * 0.60f,
                            width * 0.36f + mtnShift2, height * 0.54f,
                            width * 0.52f + mtnShift2, height * 0.65f
                        )
                        cubicTo(
                            width * 0.70f + mtnShift2, height * 0.72f,
                            width * 0.86f + mtnShift2, height * 0.58f,
                            width + 20f, height * 0.66f
                        )
                        lineTo(width + 20f, height)
                        lineTo(-20f, height)
                        close()
                    }
                    drawPath(
                        path = mtnPath2,
                        brush = Brush.verticalGradient(
                            listOf(
                                jadeGreen.copy(alpha = 0.15f),
                                accent.copy(alpha = 0.10f),
                                Color.Transparent
                            ),
                            startY = height * 0.52f,
                            endY = height * 0.95f
                        )
                    )

                    // 4. 烟波浩渺（动态平滑水波微漾线）
                    for (i in 0..2) {
                        val waveBaseY = height * (0.80f + i * 0.07f)
                        val wavePhase = (windShift + i * 0.33f) * (2f * Math.PI.toFloat())
                        val wavePath = Path().apply {
                            moveTo(0f, waveBaseY)
                            val segments = 8
                            for (s in 1..segments) {
                                val x = width * (s.toFloat() / segments)
                                val prevX = width * ((s - 1).toFloat() / segments)
                                val midX = (prevX + x) / 2f
                                val offsetY = kotlin.math.sin(wavePhase + s * 0.8f) * 6.dp.toPx()
                                quadraticTo(midX, waveBaseY + offsetY, x, waveBaseY)
                            }
                        }
                        drawPath(
                            path = wavePath,
                            color = jadeGreen.copy(alpha = 0.18f - i * 0.04f),
                            style = Stroke(width = (1.5f - i * 0.2f).dp.toPx())
                        )
                    }

                    // 5. 青鸟白鹤·飞鸟剪影（在山峦上空翱翔）
                    val birdX1 = (width * 0.28f + (windShift - 0.5f) * width * 0.12f)
                    val birdY1 = height * 0.22f + (slay1Angle * 0.4f)
                    drawPath(
                        path = Path().apply {
                            moveTo(birdX1 - 14.dp.toPx(), birdY1 + 5.dp.toPx())
                            quadraticTo(birdX1 - 6.dp.toPx(), birdY1 - 5.dp.toPx(), birdX1, birdY1)
                            quadraticTo(birdX1 + 6.dp.toPx(), birdY1 - 5.dp.toPx(), birdX1 + 14.dp.toPx(), birdY1 + 5.dp.toPx())
                        },
                        color = accent.copy(alpha = 0.35f),
                        style = Stroke(width = 1.8.dp.toPx())
                    )
                    val birdX2 = (width * 0.72f - (windShift - 0.5f) * width * 0.10f)
                    val birdY2 = height * 0.28f - (slay2Angle * 0.3f)
                    drawPath(
                        path = Path().apply {
                            moveTo(birdX2 - 10.dp.toPx(), birdY2 + 4.dp.toPx())
                            quadraticTo(birdX2 - 4.dp.toPx(), birdY2 - 3.dp.toPx(), birdX2, birdY2)
                            quadraticTo(birdX2 + 4.dp.toPx(), birdY2 - 3.dp.toPx(), birdX2 + 10.dp.toPx(), birdY2 + 4.dp.toPx())
                        },
                        color = accent.copy(alpha = 0.25f),
                        style = Stroke(width = 1.4.dp.toPx())
                    )

                    // 6. 竹韵落叶与灵动微光（随风飘舞的竹叶粒子）
                    val leaves = listOf(
                        Triple(width * 0.15f, height * 0.32f + windShift * 24f, slay1Angle * 2.5f),
                        Triple(width * 0.85f, height * 0.45f - windShift * 20f, -slay2Angle * 2f),
                        Triple(width * 0.48f, height * 0.78f + windShift * 16f, slay1Angle * 1.8f),
                        Triple(width * 0.92f, height * 0.88f - windShift * 14f, 15f)
                    )
                    leaves.forEach { (lx, ly, rot) ->
                        drawCapsule(
                            center = Offset(lx, ly),
                            length = 28.dp.toPx(),
                            thickness = 7.dp.toPx(),
                            angleDegrees = rot,
                            brush = Brush.horizontalGradient(
                                listOf(jadeGreen.copy(alpha = 0.30f), goldGlint.copy(alpha = 0.25f))
                            )
                        )
                    }

                    // 7. 云雾流光柔晕（左上与右下呼吸光晕）
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(jadeGreen.copy(alpha = 0.14f * glowPulse), Color.Transparent),
                            center = Offset(width * 0.15f, height * 0.20f),
                            radius = 240.dp.toPx()
                        ),
                        center = Offset(width * 0.15f, height * 0.20f),
                        radius = 240.dp.toPx()
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(goldGlint.copy(alpha = 0.10f * glowPulse), Color.Transparent),
                            center = Offset(width * 0.82f, height * 0.75f),
                            radius = 220.dp.toPx()
                        ),
                        center = Offset(width * 0.82f, height * 0.75f),
                        radius = 220.dp.toPx()
                    )
                }
            }
        }

        // ========== 2. 自定义背景媒体（图片/视频）：以大胶囊容器裁剪呈现 ==========
        if (bgMediaType == "image" && bgMediaUrl.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .border(2.dp, activePrimary.copy(alpha = 0.35f), RoundedCornerShape(36.dp))
            ) {
                coil.compose.AsyncImage(
                    model = bgMediaUrl,
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // 半透明遮罩保证前景内容可读
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
            }
        } else if (bgMediaType == "video" && bgMediaUrl.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .border(2.dp, activePrimary.copy(alpha = 0.35f), RoundedCornerShape(36.dp))
            ) {
                androidx.compose.ui.viewinterop.AndroidView(
                    factory = { ctx ->
                        android.view.TextureView(ctx).apply {
                            var player: android.media.MediaPlayer? = null
                            surfaceTextureListener = object : android.view.TextureView.SurfaceTextureListener {
                                override fun onSurfaceTextureAvailable(surface: android.graphics.SurfaceTexture, width: Int, height: Int) {
                                    try {
                                        val mp = android.media.MediaPlayer()
                                        player = mp
                                        mp.setDataSource(ctx, android.net.Uri.parse(bgMediaUrl))
                                        mp.setSurface(android.view.Surface(surface))
                                        mp.isLooping = true
                                        mp.setVolume(0f, 0f)
                                        mp.setOnPreparedListener { p ->
                                            try { p.start() } catch (_: Exception) {}
                                        }
                                        mp.setOnVideoSizeChangedListener { _, vw, vh ->
                                            if (vw > 0 && vh > 0) {
                                                val viewW = width.toFloat().coerceAtLeast(1f)
                                                val viewH = height.toFloat().coerceAtLeast(1f)
                                                // v1.1.14：修复视频背景被放大裁切问题——改为 fit 模式：
                                                // 保持视频原始比例完整显示（不放大、不裁切），居中摆放
                                                val s = kotlin.math.min(viewW / vw, viewH / vh)
                                                val matrix = android.graphics.Matrix().apply {
                                                    setScale(s, s)
                                                    postTranslate((viewW - vw * s) / 2f, (viewH - vh * s) / 2f)
                                                }
                                                setTransform(matrix)
                                            }
                                        }
                                        mp.setOnErrorListener { _, _, _ ->
                                            try { mp.release() } catch (_: Exception) {}
                                            player = null
                                            true
                                        }
                                        mp.prepareAsync()
                                    } catch (_: Exception) {
                                    }
                                }

                                override fun onSurfaceTextureSizeChanged(surface: android.graphics.SurfaceTexture, width: Int, height: Int) {}
                                override fun onSurfaceTextureDestroyed(surface: android.graphics.SurfaceTexture): Boolean {
                                    try {
                                        player?.reset()
                                        player?.release()
                                    } catch (_: Exception) {}
                                    player = null
                                    return true
                                }
                                override fun onSurfaceTextureUpdated(surface: android.graphics.SurfaceTexture) {}
                            }
                        }
                    },
                    onRelease = { view ->
                        // TextureView surface destruction handles release
                    },
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
            }
        }

        // ========== 3. 全站液体玻璃主舞台背衬（Full-Site Liquid Glass Stage Framing） ==========
        // 全站背景统一叠加液体玻璃冰晶折射层、游走高光水滴、曲面镜面反射带与双层高光玻璃描边
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color(0xFFF6F2FF).copy(alpha = 0.14f),
                            Color(0xFFFFEEF5).copy(alpha = 0.16f),
                            Color.White.copy(alpha = 0.30f)
                        ),
                        start = Offset.Zero,
                        end = Offset(1200f, 1800f)
                    )
                )
                .border(
                    width = 1.6.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.92f),
                            Color.White.copy(alpha = 0.42f),
                            activePrimary.copy(alpha = 0.45f),
                            Color.White.copy(alpha = 0.88f)
                        )
                    ),
                    shape = RoundedCornerShape(36.dp)
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // 1. 游走液态冰晶折射光斑 A（左上方柔光水滴透镜）
                val orb1X = w * (0.24f + 0.14f * cos(ringSpinPhase))
                val orb1Y = h * (0.20f + 0.10f * sin(ringSpinPhase))
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.42f),
                            activePrimary.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = Offset(orb1X, orb1Y),
                        radius = w * 0.48f
                    ),
                    center = Offset(orb1X, orb1Y),
                    radius = w * 0.48f
                )

                // 2. 游走液态冰晶折射光斑 B（右中下方彩色折射透镜）
                val orb2X = w * (0.76f + 0.14f * sin(ringSpinPhase * 0.85f))
                val orb2Y = h * (0.64f + 0.12f * cos(ringSpinPhase * 0.85f))
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            activePrimary.copy(alpha = 0.16f),
                            Color.White.copy(alpha = 0.32f),
                            Color.Transparent
                        ),
                        center = Offset(orb2X, orb2Y),
                        radius = w * 0.46f
                    ),
                    center = Offset(orb2X, orb2Y),
                    radius = w * 0.46f
                )

                // 3. 液体玻璃内置柔光同心环（随环形样式幅度实时缩放与呼吸）
                val ringScale = ringAmplitude.scaleFactor
                val ringStroke = 1.4.dp.toPx() * ringAmplitude.strokeMultiplier
                val glassRingCenter1 = Offset(
                    w * (0.78f + 0.04f * cos(ringSpinPhase) * ringAmplitude.motionMultiplier),
                    h * (0.24f + 0.03f * sin(ringSpinPhase) * ringAmplitude.motionMultiplier)
                )
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.55f),
                            activePrimary.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.12f),
                            Color.White.copy(alpha = 0.55f)
                        ),
                        center = glassRingCenter1
                    ),
                    radius = 78.dp.toPx() * ringScale * glowPulse,
                    center = glassRingCenter1,
                    style = Stroke(width = ringStroke)
                )

                val glassRingCenter2 = Offset(
                    w * (0.22f - 0.04f * sin(ringSpinPhase) * ringAmplitude.motionMultiplier),
                    h * (0.72f + 0.03f * cos(ringSpinPhase) * ringAmplitude.motionMultiplier)
                )
                drawCircle(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.48f),
                            Color(0xFFFFD700).copy(alpha = 0.25f),
                            activePrimary.copy(alpha = 0.22f),
                            Color.White.copy(alpha = 0.48f)
                        ),
                        center = glassRingCenter2
                    ),
                    radius = 64.dp.toPx() * ringScale,
                    center = glassRingCenter2,
                    style = Stroke(width = ringStroke * 0.85f)
                )

                // 4. 顶部曲面液体玻璃高光反射带
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.52f),
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = h * 0.22f
                    ),
                    topLeft = Offset(10.dp.toPx(), 4.dp.toPx()),
                    size = Size((w - 20.dp.toPx()).coerceAtLeast(0f), h * 0.20f),
                    cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx())
                )
            }
        }

        // ========== 4. 顶部摇曳胶囊氛围挂件（Cute Swaying Capsule Wind Badges） ==========
        // 挂件 1：右上角摇曳小胶囊
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-16).dp, y = 14.dp)
                .size(width = 34.dp, height = 18.dp)
                .rotate(slay1Angle)
                .alpha(0.55f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCapsule(
                    center = Offset(size.width / 2f, size.height / 2f),
                    length = size.width,
                    thickness = size.height,
                    color = activePrimary.copy(alpha = 0.70f)
                )
                drawCapsule(
                    center = Offset(size.width / 2f, size.height / 2f),
                    length = size.width,
                    thickness = size.height,
                    color = Color.White.copy(alpha = 0.60f),
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }
        }

        // 挂件 2：左上角摇曳微型胶囊
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 38.dp, y = 12.dp)
                .size(width = 24.dp, height = 12.dp)
                .rotate(slay2Angle)
                .alpha(0.50f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCapsule(
                    center = Offset(size.width / 2f, size.height / 2f),
                    length = size.width,
                    thickness = size.height,
                    color = activePrimary.copy(alpha = 0.60f)
                )
            }
        }

        // 挂件 3：左上极侧轻摇小胶囊
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 10.dp, y = 16.dp)
                .size(width = 28.dp, height = 14.dp)
                .rotate(slay1Angle * 0.8f)
                .alpha(0.52f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCapsule(
                    center = Offset(size.width / 2f, size.height / 2f),
                    length = size.width,
                    thickness = size.height,
                    color = activePrimary.copy(alpha = 0.55f)
                )
            }
        }

        // ========== 5. 应用前台内容层 ==========
        content()
    }
}

/**
 * 绘制任意旋转角度、支持渐变/纯色、填充/描边的几何胶囊（Capsule / Pill Shape）
 */
private fun DrawScope.drawCapsule(
    center: Offset,
    length: Float,
    thickness: Float,
    angleDegrees: Float = 0f,
    color: Color = Color.Unspecified,
    brush: Brush? = null,
    style: DrawStyle = Fill
) {
    rotate(angleDegrees, pivot = center) {
        val topLeft = Offset(center.x - length / 2f, center.y - thickness / 2f)
        val pillSize = Size(length, thickness)
        val cornerRadius = CornerRadius(thickness / 2f, thickness / 2f)
        if (brush != null) {
            drawRoundRect(
                brush = brush,
                topLeft = topLeft,
                size = pillSize,
                cornerRadius = cornerRadius,
                style = style
            )
        } else {
            drawRoundRect(
                color = color,
                topLeft = topLeft,
                size = pillSize,
                cornerRadius = cornerRadius,
                style = style
            )
        }
    }
}

/**
 * 环形特效主题背景（Canvas 动态环形渲染，可叠加在任意颜色/渐变/液体玻璃主题上）
 * 10 大环形特效：霓虹脉冲环 / 星轨双旋环 / 液态波纹环 / 赤金耀斑环 / 全息棱镜环 / 流光泡泡环 / 极光涡流环 / 星芒粒子环 / 冰晶折射环 / 声波律动环
 */
@Composable
private fun DynamicEffectBackground(
    effect: DynamicEffectPreset,
    baseColor: Color,
    accent: Color,
    windShift: Float,
    slay1Angle: Float,
    slay2Angle: Float,
    glowPulse: Float,
    ringSpinPhase: Float,
    amplitude: com.example.ui.uiverse.RingAmplitudePreset,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val scale = amplitude.scaleFactor
        val strokeMul = amplitude.strokeMultiplier
        val motionMul = amplitude.motionMultiplier
        val ringCount = amplitude.ringCount
        val spinDeg = (ringSpinPhase * 180f / Math.PI.toFloat())

        when (effect) {
            DynamicEffectPreset.HEART_RING -> {
                // 灵动爱心环：温润粉红半透渐变底 + 多层由内而外律动扩散的粉霞/霓虹立体爱心光环
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFFF0F5), Color(0xFFFFE4E6), Color(0xFFFAF5FF)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val centers = listOf(
                    Offset(w * 0.50f, h * 0.32f),
                    Offset(w * 0.20f, h * 0.72f),
                    Offset(w * 0.82f, h * 0.65f)
                )
                centers.forEachIndexed { cIdx, center ->
                    val baseR = (if (cIdx == 0) 80.dp else 48.dp).toPx() * scale
                    for (rIdx in 0 until ringCount) {
                        val pulseShift = ((windShift + rIdx * 0.25f) % 1f)
                        val hr = baseR * (0.42f + rIdx * 0.30f + pulseShift * 0.20f * motionMul)
                        val alpha = (0.65f - rIdx * 0.10f).coerceIn(0.15f, 0.85f)
                        val heartPath = Path().apply {
                            val cx = center.x
                            val cy = center.y
                            moveTo(cx, cy + hr * 0.40f)
                            cubicTo(cx - hr * 0.85f, cy - hr * 0.35f, cx - hr * 0.85f, cy - hr * 1.05f, cx, cy - hr * 0.55f)
                            cubicTo(cx + hr * 0.85f, cy - hr * 1.05f, cx + hr * 0.85f, cy - hr * 0.35f, cx, cy + hr * 0.40f)
                            close()
                        }
                        drawPath(
                            path = heartPath,
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFF43F5E).copy(alpha = alpha),
                                    Color(0xFFFB7185).copy(alpha = alpha * 0.85f),
                                    Color(0xFFA855F7).copy(alpha = alpha)
                                )
                            ),
                            style = Stroke(width = (2.6f - rIdx * 0.25f).coerceAtLeast(1.2f).dp.toPx() * strokeMul)
                        )
                    }
                }
            }
            DynamicEffectPreset.CURVED_ARC -> {
                // 弧度飞天环：大跨度优雅偏心弧圈与彩色光束带
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFF0FDF4), Color(0xFFECFDF5), Color(0xFFEFF6FF)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val cx = w * 0.5f
                val cy = h * 0.38f
                for (rIdx in 0 until ringCount) {
                    val radius = (120.dp.toPx() + rIdx * 35.dp.toPx()) * scale
                    val sweep = 180f + 60f * sin(ringSpinPhase + rIdx)
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                Color(0xFF10B981).copy(alpha = 0.65f),
                                Color(0xFF06B6D4).copy(alpha = 0.70f),
                                Color(0xFF3B82F6).copy(alpha = 0.50f),
                                Color(0xFF10B981).copy(alpha = 0.65f)
                            ),
                            center = Offset(cx, cy)
                        ),
                        startAngle = spinDeg + rIdx * 45f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(cx - radius, cy - radius * 0.75f),
                        size = Size(radius * 2, radius * 1.5f),
                        style = Stroke(width = (3f - rIdx * 0.3f).coerceAtLeast(1.2f).dp.toPx() * strokeMul)
                    )
                }
            }
            DynamicEffectPreset.INFINITY_LOOP -> {
                // 莫比乌斯环：无穷大双向光环能量场
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF), Color(0xFFFDF4FF)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val cx1 = w * 0.36f
                val cx2 = w * 0.64f
                val cy = h * 0.35f
                val loopR = 55.dp.toPx() * scale
                drawCircle(
                    brush = Brush.sweepGradient(listOf(Color(0xFF8B5CF6), Color(0xFFEC4899), Color(0xFF8B5CF6)), center = Offset(cx1, cy)),
                    radius = loopR,
                    center = Offset(cx1, cy),
                    style = Stroke(width = 3.dp.toPx() * strokeMul)
                )
                drawCircle(
                    brush = Brush.sweepGradient(listOf(Color(0xFFEC4899), Color(0xFF8B5CF6), Color(0xFFEC4899)), center = Offset(cx2, cy)),
                    radius = loopR,
                    center = Offset(cx2, cy),
                    style = Stroke(width = 3.dp.toPx() * strokeMul)
                )
            }
            DynamicEffectPreset.HAND_DRAWN -> {
                // 1. 霓虹脉冲环：清透液态底 + 多层向外呼吸扩散的霓虹双色脉冲同心环
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFF3EEFF), Color(0xFFE9F4FF), Color(0xFFFFEEF6)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val centers = listOf(
                    Offset(w * 0.50f, h * 0.34f),
                    Offset(w * 0.22f, h * 0.74f),
                    Offset(w * 0.80f, h * 0.68f)
                )
                centers.forEachIndexed { cIdx, center ->
                    val baseR = (if (cIdx == 0) 95.dp else 58.dp).toPx() * scale
                    for (rIdx in 0 until ringCount) {
                        val pulseShift = ((windShift + rIdx * 0.22f) % 1f)
                        val radius = baseR * (0.45f + rIdx * 0.28f + pulseShift * 0.22f * motionMul)
                        val alpha = (0.58f - rIdx * 0.08f).coerceIn(0.12f, 0.75f)
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF6366F1).copy(alpha = alpha),
                                    Color(0xFFEC4899).copy(alpha = alpha * 0.85f),
                                    Color(0xFF00F0FF).copy(alpha = alpha),
                                    Color(0xFF6366F1).copy(alpha = alpha)
                                ),
                                center = center
                            ),
                            radius = radius,
                            center = center,
                            style = Stroke(width = (2.4f - rIdx * 0.25f).coerceAtLeast(1f).dp.toPx() * strokeMul)
                        )
                    }
                }
            }
            DynamicEffectPreset.STICKER -> {
                // 2. 星轨双旋环：粉紫微光底 + 正反双向旋转的星轨椭圆环与环上行星锚点
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFDF2F8), Color(0xFFF3E8FF)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val mainCenter = Offset(w * 0.5f, h * 0.42f)
                for (i in 0 until ringCount) {
                    val rx = (75 + i * 34).dp.toPx() * scale
                    val ry = (42 + i * 20).dp.toPx() * scale
                    val dir = if (i % 2 == 0) 1f else -1f
                    val angle = spinDeg * dir * (0.6f + i * 0.15f) + i * 28f
                    rotate(angle, pivot = mainCenter) {
                        drawOval(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFF472B6).copy(alpha = 0.52f),
                                    Color(0xFF8B5CF6).copy(alpha = 0.40f),
                                    Color.White.copy(alpha = 0.75f)
                                )
                            ),
                            topLeft = Offset(mainCenter.x - rx, mainCenter.y - ry),
                            size = Size(rx * 2f, ry * 2f),
                            style = Stroke(width = 2.dp.toPx() * strokeMul)
                        )
                        // 轨道卫星亮点
                        drawCircle(
                            color = Color(0xFFEC4899).copy(alpha = 0.78f),
                            radius = 4.5.dp.toPx() * strokeMul,
                            center = Offset(mainCenter.x + rx, mainCenter.y)
                        )
                    }
                }
            }
            DynamicEffectPreset.TRENDY -> {
                // 3. 液态波纹环：冰蓝粉紫液态底 + 多重交叠水波涟漪扩散环
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFE8F5FF), Color(0xFFF3E9FF), Color(0xFFFFEBF4)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val rippleOrigins = listOf(
                    Offset(w * 0.30f + windShift * 18f * motionMul, h * 0.28f),
                    Offset(w * 0.74f - windShift * 18f * motionMul, h * 0.52f),
                    Offset(w * 0.42f, h * 0.80f - windShift * 14f * motionMul)
                )
                rippleOrigins.forEachIndexed { idx, origin ->
                    for (r in 0 until ringCount) {
                        val radius = (38 + r * 30).dp.toPx() * scale * (0.92f + 0.12f * sin(ringSpinPhase + r + idx))
                        drawCircle(
                            color = if (r % 2 == 0) Color(0xFF00B4D8).copy(alpha = (0.42f - r * 0.06f).coerceAtLeast(0.10f))
                            else Color(0xFFFF6A88).copy(alpha = (0.38f - r * 0.05f).coerceAtLeast(0.10f)),
                            radius = radius,
                            center = origin,
                            style = Stroke(width = 2.2.dp.toPx() * strokeMul)
                        )
                    }
                }
            }
            DynamicEffectPreset.NATIONAL_DAY -> {
                // 4. 赤金耀斑环：国潮暖霞底 + 鎏金与朱红交织的日冕耀斑环
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFFF3E6), Color(0xFFFFE4D6), Color(0xFFFFF8EB)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val sunCenter = Offset(w * 0.50f, h * 0.32f)
                for (i in 0 until ringCount) {
                    val r = (52 + i * 34).dp.toPx() * scale * glowPulse
                    rotate(spinDeg * (if (i % 2 == 0) 0.5f else -0.4f), pivot = sunCenter) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFFDE2910).copy(alpha = 0.55f),
                                    Color(0xFFFFD700).copy(alpha = 0.68f),
                                    Color(0xFFFF7A00).copy(alpha = 0.35f),
                                    Color(0xFFDE2910).copy(alpha = 0.55f)
                                ),
                                center = sunCenter
                            ),
                            radius = r,
                            center = sunCenter,
                            style = Stroke(width = (2.6f - i * 0.25f).coerceAtLeast(1.1f).dp.toPx() * strokeMul)
                        )
                    }
                }
            }
            DynamicEffectPreset.NEUMORPHIC -> {
                // 5. 全息棱镜环：丝绸银白底 + 七彩光谱折射立体浮雕同心环
                drawRect(Color(0xFFEEF2F8))
                val prismCenter = Offset(w * 0.50f, h * 0.45f)
                for (i in 0 until ringCount) {
                    val r = (56 + i * 36).dp.toPx() * scale
                    // 浮雕外高光环
                    drawCircle(
                        color = Color.White.copy(alpha = 0.85f),
                        radius = r + 3.dp.toPx(),
                        center = Offset(prismCenter.x - 3.dp.toPx(), prismCenter.y - 3.dp.toPx()),
                        style = Stroke(width = 3.2.dp.toPx() * strokeMul)
                    )
                    // 浮雕内投影环 + 全息渐变描边
                    rotate(spinDeg * 0.6f + i * 45f, pivot = prismCenter) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF38BDF8).copy(alpha = 0.55f),
                                    Color(0xFFA855F7).copy(alpha = 0.55f),
                                    Color(0xFF34D399).copy(alpha = 0.55f),
                                    Color(0xFFF472B6).copy(alpha = 0.55f),
                                    Color(0xFF38BDF8).copy(alpha = 0.55f)
                                ),
                                center = prismCenter
                            ),
                            radius = r,
                            center = prismCenter,
                            style = Stroke(width = 2.4.dp.toPx() * strokeMul)
                        )
                    }
                }
            }
            DynamicEffectPreset.Q_CARTOON -> {
                // 6. 流光泡泡环：糖果液态玻璃底 + 漂浮七彩肥皂泡光环群
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFFF5EB), Color(0xFFECF7FF)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val bubbleRingColors = listOf(
                    Color(0xFFFF7EB3),
                    Color(0xFF60A5FA),
                    Color(0xFFFBBF24),
                    Color(0xFF34D399)
                )
                val totalBubbles = (ringCount * 3).coerceAtLeast(6)
                repeat(totalBubbles) { i ->
                    val bx = ((i * 137 % 1000) / 1000f) * w + sin(ringSpinPhase + i) * 18f * motionMul
                    val by = ((i * 89 % 1000) / 1000f) * h + cos(ringSpinPhase * 0.8f + i) * 16f * motionMul
                    val br = (18 + (i % 4) * 11).dp.toPx() * scale
                    val c = bubbleRingColors[i % bubbleRingColors.size]
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color.White.copy(alpha = 0.45f), c.copy(alpha = 0.16f), Color.Transparent),
                            center = Offset(bx, by),
                            radius = br * 1.2f
                        ),
                        radius = br,
                        center = Offset(bx, by)
                    )
                    drawCircle(
                        color = c.copy(alpha = 0.55f),
                        radius = br,
                        center = Offset(bx, by),
                        style = Stroke(width = 1.8.dp.toPx() * strokeMul)
                    )
                    // 泡泡高光弧点
                    drawCircle(
                        color = Color.White.copy(alpha = 0.85f),
                        radius = br * 0.18f,
                        center = Offset(bx - br * 0.35f, by - br * 0.35f)
                    )
                }
            }
            DynamicEffectPreset.AURORA -> {
                // 7. 极光涡流环：深邃极夜底 + 翡翠绿/极光紫旋转涡流光环
                drawRect(Color(0xFF0B1124))
                val vortexCenter = Offset(w * 0.50f, h * 0.44f)
                for (i in 0 until ringCount + 1) {
                    val r = (46 + i * 32).dp.toPx() * scale * (0.94f + 0.08f * sin(ringSpinPhase + i))
                    rotate(spinDeg * (0.8f - i * 0.12f), pivot = vortexCenter) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF00FF87).copy(alpha = 0.62f),
                                    Color(0xFF60EFFF).copy(alpha = 0.48f),
                                    Color(0xFF8F00FF).copy(alpha = 0.55f),
                                    Color.Transparent,
                                    Color(0xFF00FF87).copy(alpha = 0.62f)
                                ),
                                center = vortexCenter
                            ),
                            radius = r,
                            center = vortexCenter,
                            style = Stroke(width = (3.4f - i * 0.3f).coerceAtLeast(1.4f).dp.toPx() * strokeMul)
                        )
                    }
                }
            }
            DynamicEffectPreset.FIREFLY -> {
                // 8. 星芒粒子环：森系暗夜底 + 围绕核心公转的流金萤火粒子星环
                drawRect(Color(0xFF0E1B16))
                val hub = Offset(w * 0.50f, h * 0.44f)
                for (i in 0 until ringCount) {
                    val r = (54 + i * 34).dp.toPx() * scale
                    drawCircle(
                        color = Color(0xFFFFE57F).copy(alpha = 0.22f),
                        radius = r,
                        center = hub,
                        style = Stroke(width = 1.2.dp.toPx() * strokeMul)
                    )
                    val dots = 6 + i * 2
                    for (d in 0 until dots) {
                        val ang = ringSpinPhase * (if (i % 2 == 0) 1f else -0.8f) + d * (2 * Math.PI / dots).toFloat()
                        val px = hub.x + cos(ang) * r
                        val py = hub.y + sin(ang) * r
                        val dotR = (3.2f + (d % 2) * 1.5f).dp.toPx() * strokeMul * glowPulse
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(Color(0xFFFFE57F), Color(0xFF34D399).copy(alpha = 0.4f), Color.Transparent),
                                center = Offset(px, py),
                                radius = dotR * 2.6f
                            ),
                            radius = dotR * 2.2f,
                            center = Offset(px, py)
                        )
                    }
                }
            }
            DynamicEffectPreset.SNOW -> {
                // 9. 冰晶折射环：冰川透白底 + 冰蓝六棱晶格双层折射光环
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFEDF7FF), Color(0xFFDCEEFF), Color(0xFFF5FAFF)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val iceCenters = listOf(
                    Offset(w * 0.50f, h * 0.36f),
                    Offset(w * 0.25f, h * 0.74f),
                    Offset(w * 0.78f, h * 0.72f)
                )
                iceCenters.forEachIndexed { idx, c ->
                    val baseR = (if (idx == 0) 84.dp else 50.dp).toPx() * scale
                    for (i in 0 until ringCount) {
                        val r = baseR * (0.5f + i * 0.30f)
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.90f),
                                    Color(0xFF38BDF8).copy(alpha = 0.52f),
                                    Color.White.copy(alpha = 0.85f),
                                    Color(0xFF60A5FA).copy(alpha = 0.48f),
                                    Color.White.copy(alpha = 0.90f)
                                ),
                                center = c
                            ),
                            radius = r,
                            center = c,
                            style = Stroke(width = 2.dp.toPx() * strokeMul)
                        )
                    }
                }
            }
            DynamicEffectPreset.DRIZZLE -> {
                // 10. 声波律动环：极简雾面蓝灰底 + 随节拍起伏缩放的均衡器声波环
                drawRect(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFEEF4FA), Color(0xFFE3EDF7)),
                        start = Offset.Zero, end = Offset(w, h)
                    )
                )
                val center = Offset(w * 0.50f, h * 0.44f)
                for (i in 0 until ringCount + 1) {
                    val beat = sin(ringSpinPhase * 2f + i * 0.9f) * 14.dp.toPx() * motionMul
                    val r = ((48 + i * 30).dp.toPx() * scale + beat).coerceAtLeast(16.dp.toPx())
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(
                                Color(0xFF3B82F6).copy(alpha = 0.52f),
                                Color(0xFF06B6D4).copy(alpha = 0.60f),
                                Color(0xFF8B5CF6).copy(alpha = 0.48f),
                                Color(0xFF3B82F6).copy(alpha = 0.52f)
                            ),
                            center = center
                        ),
                        radius = r,
                        center = center,
                        style = Stroke(width = (2.4f + (i % 2) * 0.8f).dp.toPx() * strokeMul)
                    )
                }
            }
            DynamicEffectPreset.NONE -> {
                drawRect(baseColor)
            }
        }
    }
}

/**
 * 🎨 14大艺术与设计流派全套背景渲染器
 * 针对每种设计风格精细定制专属质感、光影、结构与动态效果
 */
@Composable
private fun DesignStyleBackground(
    style: DesignStyle,
    baseColor: Color,
    primaryColor: Color,
    secondaryColor: Color,
    windShift: Float,
    slay1Angle: Float,
    slay2Angle: Float,
    glowPulse: Float,
    ringSpinPhase: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        when (style) {
            DesignStyle.SKEUOMORPHISM -> {
                // 1️⃣ 拟物化：v1.5.4重写——基色从主题派生，不再写死
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            lerp(baseColor, Color.White, 0.12f),
                            baseColor,
                            lerp(baseColor, Color.Black, 0.08f)
                        ),
                        center = Offset(w * 0.45f, h * 0.35f),
                        radius = (w * 0.95f)
                    )
                )
                // 四周边框精细双道缝线（从主题主色派生）
                val stitchPad = 14.dp.toPx()
                val stitchColor = primaryColor.copy(alpha = 0.38f)
                val stitchWidth = 1.4.dp.toPx()
                // 顶部缝线
                var sx = stitchPad + 18.dp.toPx()
                while (sx < w - stitchPad - 18.dp.toPx()) {
                    drawLine(stitchColor, Offset(sx, stitchPad), Offset(sx + 8.dp.toPx(), stitchPad), strokeWidth = stitchWidth)
                    drawLine(stitchColor, Offset(sx, h - stitchPad), Offset(sx + 8.dp.toPx(), h - stitchPad), strokeWidth = stitchWidth)
                    sx += 14.dp.toPx()
                }
                var sy = stitchPad + 18.dp.toPx()
                while (sy < h - stitchPad - 18.dp.toPx()) {
                    drawLine(stitchColor, Offset(stitchPad, sy), Offset(stitchPad, sy + 8.dp.toPx()), strokeWidth = stitchWidth)
                    drawLine(stitchColor, Offset(w - stitchPad, sy), Offset(w - stitchPad, sy + 8.dp.toPx()), strokeWidth = stitchWidth)
                    sy += 14.dp.toPx()
                }
                // 四角立体黄铜铆钉
                val rivets = listOf(
                    Offset(stitchPad + 6.dp.toPx(), stitchPad + 6.dp.toPx()),
                    Offset(w - stitchPad - 6.dp.toPx(), stitchPad + 6.dp.toPx()),
                    Offset(stitchPad + 6.dp.toPx(), h - stitchPad - 6.dp.toPx()),
                    Offset(w - stitchPad - 6.dp.toPx(), h - stitchPad - 6.dp.toPx())
                )
                rivets.forEach { rivet ->
                    // 阴影
                    drawCircle(Color(0x33000000), radius = 6.5.dp.toPx(), center = rivet + Offset(1.5.dp.toPx(), 2.dp.toPx()))
                    // 黄铜环
                    drawCircle(
                        brush = Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFF8B5A2B))),
                        radius = 6.dp.toPx(),
                        center = rivet
                    )
                    // 铆钉内芯凹凸
                    drawCircle(Color(0xFF5C3A21), radius = 3.5.dp.toPx(), center = rivet)
                    // 金属高光点
                    drawCircle(Color.White.copy(alpha = 0.85f), radius = 1.2.dp.toPx(), center = rivet - Offset(1.5.dp.toPx(), 1.5.dp.toPx()))
                }
            }

            DesignStyle.FLAT_DESIGN -> {
                // 2️⃣ 扁平化（v1.4.15修复：此前写死浅灰0xFFECF0F1，主题切换背景不变；现全从主题色派生）
                drawRect(baseColor)
                val flatCyan = primaryColor.copy(alpha = 0.12f)
                val flatBlue = secondaryColor.copy(alpha = 0.10f)
                val flatOrange = primaryColor.copy(alpha = 0.08f)

                val path1 = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(w * 0.85f, 0f)
                    lineTo(0f, h * 0.45f)
                    close()
                }
                drawPath(path1, flatCyan)

                val path2 = Path().apply {
                    moveTo(w, h * 0.25f)
                    lineTo(w, h * 0.90f)
                    lineTo(w * 0.25f, h)
                    lineTo(w * 0.55f, h * 0.35f)
                    close()
                }
                drawPath(path2, flatBlue)

                drawRect(
                    color = flatOrange,
                    topLeft = Offset(w * 0.10f, h * 0.58f),
                    size = Size(w * 0.50f, 60.dp.toPx())
                )
            }

            DesignStyle.MATERIAL_DESIGN -> {
                // 3️⃣ 材质设计：Google M3纸张层级感、悬浮纸片海拔阴影、几何纸质平铺
                drawRect(Color(0xFFFEF7FF))
                // 纸张网格点
                val dotSpacing = 28.dp.toPx()
                var dy = 16.dp.toPx()
                while (dy < h) {
                    var dx = 16.dp.toPx()
                    while (dx < w) {
                        drawCircle(Color(0xFF6750A4).copy(alpha = 0.06f), radius = 1.5.dp.toPx(), center = Offset(dx, dy))
                        dx += dotSpacing
                    }
                    dy += dotSpacing
                }
                // 漂浮的 Material 纸片 1（带柔和纸张落差投影）
                val sheet1 = Offset(w * 0.15f, h * 0.18f + sin(windShift * 3.14f) * 10f)
                drawRoundRect(
                    color = Color(0x18000000),
                    topLeft = sheet1 + Offset(0f, 8.dp.toPx()),
                    size = Size(w * 0.70f, 130.dp.toPx()),
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                )
                drawRoundRect(
                    color = Color(0xFFF3EDF7).copy(alpha = 0.85f),
                    topLeft = sheet1,
                    size = Size(w * 0.70f, 130.dp.toPx()),
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                )
                // 纸片 2
                val sheet2 = Offset(w * 0.08f, h * 0.56f - cos(windShift * 3.14f) * 8f)
                drawRoundRect(
                    color = Color(0x12000000),
                    topLeft = sheet2 + Offset(0f, 6.dp.toPx()),
                    size = Size(w * 0.84f, 160.dp.toPx()),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
                drawRoundRect(
                    color = Color(0xFFFFFFFF).copy(alpha = 0.90f),
                    topLeft = sheet2,
                    size = Size(w * 0.84f, 160.dp.toPx()),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }

            DesignStyle.NEUMORPHISM -> {
                // 4️⃣ 新拟态：v1.5.4重写——基色从主题派生
                drawRect(baseColor)
                val neuDomes = listOf(
                    Triple(Offset(w * 0.28f, h * 0.22f), 80.dp.toPx(), 160.dp.toPx()),
                    Triple(Offset(w * 0.72f, h * 0.58f), 100.dp.toPx(), 190.dp.toPx()),
                    Triple(Offset(w * 0.35f, h * 0.82f), 70.dp.toPx(), 130.dp.toPx())
                )
                neuDomes.forEach { (center, rw, rh) ->
                    // 凸起深影（右下，从主题色派生）
                    drawRoundRect(
                        color = lerp(baseColor, Color.Black, 0.18f).copy(alpha = 0.45f * glowPulse),
                        topLeft = Offset(center.x - rw / 2 + 10.dp.toPx(), center.y - rh / 2 + 10.dp.toPx()),
                        size = Size(rw, rh),
                        cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx())
                    )
                    // 凸起亮光（左上）
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.80f * glowPulse),
                        topLeft = Offset(center.x - rw / 2 - 8.dp.toPx(), center.y - rh / 2 - 8.dp.toPx()),
                        size = Size(rw, rh),
                        cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx())
                    )
                    // 表面平滑过渡
                    drawRoundRect(
                        color = Color(0xFFE0E5EC),
                        topLeft = Offset(center.x - rw / 2, center.y - rh / 2),
                        size = Size(rw, rh),
                        cornerRadius = CornerRadius(32.dp.toPx(), 32.dp.toPx())
                    )
                }
            }

            DesignStyle.GLASSMORPHISM -> {
                // 5️⃣ 玻璃拟态（v1.4.15修复：此前写死深蓝星空底，主题切换背景不变；现全从主题色派生）
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lerp(baseColor, primaryColor, 0.08f),
                            baseColor,
                            lerp(baseColor, Color.Black, 0.06f)
                        )
                    )
                )
                // 弥散大光斑（主色、副色）
                val orb1 = Offset(w * 0.25f + cos(ringSpinPhase) * 30f, h * 0.26f + sin(ringSpinPhase) * 30f)
                val orb2 = Offset(w * 0.75f - sin(ringSpinPhase) * 35f, h * 0.62f + cos(ringSpinPhase) * 25f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.35f), Color.Transparent),
                        center = orb1,
                        radius = 160.dp.toPx()
                    ),
                    radius = 160.dp.toPx(),
                    center = orb1
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(secondaryColor.copy(alpha = 0.38f), Color.Transparent),
                        center = orb2,
                        radius = 180.dp.toPx()
                    ),
                    radius = 180.dp.toPx(),
                    center = orb2
                )
                // 悬浮磨砂玻璃板与1px镜面亮边
                val glassPanel = Offset(w * 0.08f, h * 0.20f)
                drawRoundRect(
                    color = Color.White.copy(alpha = 0.08f),
                    topLeft = glassPanel,
                    size = Size(w * 0.84f, 180.dp.toPx()),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
                drawRoundRect(
                    brush = Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.45f), Color.Transparent, Color.White.copy(alpha = 0.20f))
                    ),
                    topLeft = glassPanel,
                    size = Size(w * 0.84f, 180.dp.toPx()),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }

            DesignStyle.CLAYMORPHISM -> {
                // 6️⃣ 粘土拟态（v1.4.15修复：此前写死蓝灰底+糖果泡泡，主题切换背景不变；现全从主题色派生）
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lerp(baseColor, primaryColor, 0.06f),
                            baseColor,
                            lerp(baseColor, primaryColor, 0.03f)
                        )
                    )
                )
                val bubbles = listOf(
                    Triple(Offset(w * 0.25f, h * 0.22f), 55.dp.toPx(), primaryColor),
                    Triple(Offset(w * 0.78f, h * 0.40f), 70.dp.toPx(), secondaryColor),
                    Triple(Offset(w * 0.32f, h * 0.68f), 65.dp.toPx(), primaryColor),
                    Triple(Offset(w * 0.75f, h * 0.85f), 50.dp.toPx(), secondaryColor)
                )
                bubbles.forEachIndexed { idx, (center, r, col) ->
                    val dy = sin(ringSpinPhase + idx * 1.5f) * 12.dp.toPx()
                    val pos = center + Offset(0f, dy)
                    // 厚重软阴影
                    drawCircle(lerp(baseColor, Color.Black, 0.18f).copy(alpha = 0.45f), radius = r * 1.05f, center = pos + Offset(4.dp.toPx(), 8.dp.toPx()))
                    // 粘土球体
                    drawCircle(col.copy(alpha = 0.82f), radius = r, center = pos)
                    // 顶部Q版高光
                    drawCircle(
                        Color.White.copy(alpha = 0.65f),
                        radius = r * 0.35f,
                        center = pos - Offset(r * 0.32f, r * 0.32f)
                    )
                }
            }

            DesignStyle.LIQUID_GLASS -> {
                // 7️⃣ 液态玻璃（v1.4.15修复：此前写死浅蓝底，主题切换背景不变；现从主题色派生）
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lerp(baseColor, secondaryColor, 0.10f),
                            baseColor,
                            lerp(baseColor, primaryColor, 0.06f)
                        )
                    )
                )
                // 动态液态高透光折射环（3 层水波纹扩散）
                val center = Offset(w * 0.50f, h * 0.42f)
                for (i in 1..4) {
                    val waveR = ((40 + i * 45).dp.toPx() + sin(ringSpinPhase * 1.5f + i) * 16.dp.toPx()).coerceAtLeast(10f)
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(
                                secondaryColor.copy(alpha = 0.35f),
                                primaryColor.copy(alpha = 0.22f),
                                secondaryColor.copy(alpha = 0.30f),
                                secondaryColor.copy(alpha = 0.35f)
                            ),
                            center = center
                        ),
                        radius = waveR,
                        center = center,
                        style = Stroke(width = 3.5.dp.toPx())
                    )
                }
                // 游走的高光水珠
                val dropPos = Offset(w * 0.70f + cos(ringSpinPhase) * 40f, h * 0.25f + sin(ringSpinPhase) * 25f)
                drawCircle(Color.White.copy(alpha = 0.75f), radius = 16.dp.toPx(), center = dropPos)
                drawCircle(secondaryColor.copy(alpha = 0.40f), radius = 16.dp.toPx(), center = dropPos, style = Stroke(2.dp.toPx()))
            }

            DesignStyle.NEO_BRUTALISM -> {
                // 8️⃣ 新粗野主义：高能波普黄色底、3px纯黑粗边框、纯黑45°硬位移投影、波普半色调
                drawRect(Color(0xFFFFFDE8))
                // 点阵半色调
                val dotGap = 22.dp.toPx()
                var py = 12.dp.toPx()
                while (py < h) {
                    var px = 12.dp.toPx()
                    while (px < w) {
                        drawCircle(Color.Black.copy(alpha = 0.08f), radius = 1.8.dp.toPx(), center = Offset(px, py))
                        px += dotGap
                    }
                    py += dotGap
                }
                // 粗野方块 1（带纯黑硬投影）
                val box1 = Offset(w * 0.12f, h * 0.20f)
                drawRoundRect(
                    color = Color.Black,
                    topLeft = box1 + Offset(6.dp.toPx(), 6.dp.toPx()),
                    size = Size(w * 0.76f, 110.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
                drawRoundRect(
                    color = Color(0xFFFFDE59),
                    topLeft = box1,
                    size = Size(w * 0.76f, 110.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
                drawRoundRect(
                    color = Color.Black,
                    topLeft = box1,
                    size = Size(w * 0.76f, 110.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx()),
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            DesignStyle.MINIMALISM -> {
                // 9️⃣ 极简主义：v1.5.4重写——基色从主题派生，不再写死
                drawRect(baseColor)
                // 极细对齐线（从主题文字色派生）
                val fineLineColor = primaryColor.copy(alpha = 0.06f)
                val lineSpacing = 48.dp.toPx()
                var lx = lineSpacing
                while (lx < w) {
                    drawLine(fineLineColor, Offset(lx, 0f), Offset(lx, h), strokeWidth = 0.8.dp.toPx())
                    lx += lineSpacing
                }
                var ly = lineSpacing
                while (ly < h) {
                    drawLine(fineLineColor, Offset(0f, ly), Offset(w, ly), strokeWidth = 0.8.dp.toPx())
                    ly += lineSpacing
                }
                // 单个极其纤细的高雅同心圆（右上角，从主题色派生）
                drawCircle(
                    color = primaryColor.copy(alpha = 0.08f),
                    radius = 90.dp.toPx(),
                    center = Offset(w * 0.88f, h * 0.15f),
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            DesignStyle.BAUHAUS -> {
                // 🔟 包豪斯风格：红黄蓝三原色构图、经典正圆/正方/三角形绝对几何、形式服务于功能
                drawRect(Color(0xFFFDFBF7))
                // 蓝色正方大矩形
                drawRect(
                    color = Color(0xFF003049).copy(alpha = 0.88f),
                    topLeft = Offset(w * 0.08f, h * 0.18f),
                    size = Size(100.dp.toPx(), 100.dp.toPx())
                )
                // 黄色正圆形
                drawCircle(
                    color = Color(0xFFF4D03F).copy(alpha = 0.92f),
                    radius = 60.dp.toPx(),
                    center = Offset(w * 0.72f, h * 0.28f)
                )
                // 红色三角形
                val triPath = Path().apply {
                    moveTo(w * 0.50f, h * 0.60f)
                    lineTo(w * 0.20f, h * 0.78f)
                    lineTo(w * 0.80f, h * 0.78f)
                    close()
                }
                drawPath(triPath, Color(0xFFD62828).copy(alpha = 0.88f))
                // 包豪斯黑色结构梁
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, h * 0.48f),
                    end = Offset(w, h * 0.52f),
                    strokeWidth = 6.dp.toPx()
                )
            }

            DesignStyle.RETROFUTURISM -> {
                // 1️⃣1️⃣ 复古未来主义：80年代地平线网格（Synthwave）、激光扫描线、霓虹夕阳
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF120826), Color(0xFF2A0845), Color(0xFF0D031A))
                    )
                )
                // 远方数字霓虹夕阳
                val sunCenter = Offset(w * 0.50f, h * 0.40f)
                drawCircle(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFFFF007F), Color(0xFFFFD200)),
                        startY = sunCenter.y - 70.dp.toPx(),
                        endY = sunCenter.y + 70.dp.toPx()
                    ),
                    radius = 65.dp.toPx(),
                    center = sunCenter
                )
                // 遮挡太阳的百叶窗条纹
                for (i in 0..6) {
                    val stripY = sunCenter.y - 10.dp.toPx() + i * 11.dp.toPx()
                    drawLine(Color(0xFF2A0845), Offset(sunCenter.x - 70.dp.toPx(), stripY), Offset(sunCenter.x + 70.dp.toPx(), stripY), strokeWidth = (2 + i).dp.toPx())
                }
                // 地平线透视网格
                val horizonY = h * 0.45f
                drawLine(Color(0xFF00F0FF), Offset(0f, horizonY), Offset(w, horizonY), strokeWidth = 2.dp.toPx())
                for (i in 0..12) {
                    val gy = horizonY + (i * i * 3.5f).dp.toPx()
                    if (gy <= h) {
                        drawLine(Color(0xFF00F0FF).copy(alpha = 0.45f), Offset(0f, gy), Offset(w, gy), strokeWidth = 1.dp.toPx())
                    }
                }
                // 透视纵向发射线
                for (j in -6..6) {
                    val bottomX = w * 0.50f + j * 45.dp.toPx()
                    drawLine(Color(0xFFFF007F).copy(alpha = 0.38f), Offset(w * 0.50f, horizonY), Offset(bottomX, h), strokeWidth = 1.2.dp.toPx())
                }
            }

            DesignStyle.BRUTALISM -> {
                // 1️⃣2️⃣ 粗野主义：未修饰水泥灰板、裸露直角框架、等宽技术刻度、0dp无圆角
                drawRect(Color(0xFFDCDDDE))
                // 水泥粗颗粒
                val concreteGrid = 32.dp.toPx()
                var cy = 0f
                while (cy < h) {
                    var cx = 0f
                    while (cx < w) {
                        // 技术对齐十字标 (+)
                        drawLine(Color(0xFF888888), Offset(cx - 3.dp.toPx(), cy), Offset(cx + 3.dp.toPx(), cy), strokeWidth = 1.dp.toPx())
                        drawLine(Color(0xFF888888), Offset(cx, cy - 3.dp.toPx()), Offset(cx, cy + 3.dp.toPx()), strokeWidth = 1.dp.toPx())
                        cx += concreteGrid
                    }
                    cy += concreteGrid
                }
                // 裸露结构工字框（直角无倒角）
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(w * 0.08f, h * 0.22f),
                    size = Size(w * 0.84f, 150.dp.toPx()),
                    style = Stroke(width = 2.5.dp.toPx())
                )
                // 建筑内部结构梁
                drawLine(Color.Black, Offset(w * 0.08f, h * 0.22f + 40.dp.toPx()), Offset(w * 0.92f, h * 0.22f + 40.dp.toPx()), strokeWidth = 1.5.dp.toPx())
            }

            DesignStyle.ANTI_DESIGN -> {
                // 1️⃣3️⃣ 反设计：反叛朋克倾斜构图、毒性荧光绿对撞高能紫、破坏秩序的不和谐色带
                drawRect(Color(0xFF0A0014))
                // 倾斜黄黑警戒色斑
                rotate(-14f, pivot = Offset(w * 0.5f, h * 0.35f)) {
                    drawRect(
                        color = Color(0xFF00FF66),
                        topLeft = Offset(-w * 0.2f, h * 0.28f),
                        size = Size(w * 1.4f, 44.dp.toPx())
                    )
                    // 警示黑条
                    for (i in 0..20) {
                        val kx = -w * 0.2f + i * 36.dp.toPx()
                        drawLine(Color.Black, Offset(kx, h * 0.28f), Offset(kx + 20.dp.toPx(), h * 0.28f + 44.dp.toPx()), strokeWidth = 10.dp.toPx())
                    }
                }
                // 反叛朋克倾斜粉块
                rotate(8f, pivot = Offset(w * 0.5f, h * 0.65f)) {
                    drawRect(
                        color = Color(0xFFFF0055).copy(alpha = 0.85f),
                        topLeft = Offset(w * 0.15f, h * 0.62f),
                        size = Size(w * 0.70f, 90.dp.toPx())
                    )
                    drawRect(
                        color = Color(0xFF00FF66),
                        topLeft = Offset(w * 0.15f, h * 0.62f),
                        size = Size(w * 0.70f, 90.dp.toPx()),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }

            DesignStyle.ACID_GRAPHICS -> {
                // 1️⃣4️⃣ 酸性设计：迷幻液态金属铬光（Chrome Mercury）、高能波纹折射、彩虹水银流动
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0C071E), Color(0xFF1B0E38), Color(0xFF080414))
                    )
                )
                // 液态铬水银池
                val mercuryCenter = Offset(w * 0.50f, h * 0.38f)
                val chromeBrush = Brush.sweepGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFB026FF),
                        Color(0xFFCCFF00),
                        Color(0xFF00F0FF),
                        Color(0xFFFF007F),
                        Color(0xFFE2E8F0),
                        Color(0xFFFFFFFF)
                    ),
                    center = mercuryCenter
                )
                for (i in 1..3) {
                    val mr = (50 + i * 40).dp.toPx() + sin(ringSpinPhase * 1.8f + i) * 14.dp.toPx()
                    drawCircle(chromeBrush, radius = mr, center = mercuryCenter, style = Stroke(width = 4.dp.toPx()))
                }
                // 迷幻中心金属光球
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color.White, Color(0xFFCCFF00), Color(0xFFB026FF), Color.Transparent),
                        center = mercuryCenter,
                        radius = 45.dp.toPx()
                    ),
                    radius = 45.dp.toPx(),
                    center = mercuryCenter
                )
            }

            else -> {
                drawRect(baseColor)
            }
        }
    }
}

