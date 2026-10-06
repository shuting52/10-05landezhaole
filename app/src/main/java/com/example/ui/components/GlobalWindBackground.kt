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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalUiverseState
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
 * 全局胶囊化风动背景（Global Capsule Wind Background）
 * - 整体背景升级为「大胶囊舞台底衬 + 动态悬浮流光胶囊群 + 微光胶囊轮廓」
 * - 全屏背景与自定义媒体均以胶囊形态呈现（圆角 36dp 大胶囊轮廓与内部漂浮动感胶囊）
 * - 支持跟随主题色同步变色（霓虹绿地图/国庆红/暖桃风），彻底落实 UI 整体胶囊化设计
 */
@Composable
fun GlobalWindBackground(
    modifier: Modifier = Modifier,
    bgMediaType: String = "none",
    bgMediaUrl: String = "",
    // 主题切换优化——背景跟随软件背景（主题背景色）同步
    themeBgColor: Color? = null,
    themePrimaryColor: Color? = null,
    // v1.2.0：渐变主题背景 + 动态效果主题
    themeGradientColors: List<Color> = emptyList(),
    dynamicEffect: DynamicEffectPreset = DynamicEffectPreset.NONE,
    content: @Composable BoxScope.() -> Unit
) {
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

    val activePrimary = themePrimaryColor ?: Color(0xFF00C080)
    // v1.1.10：控制台「主题工具箱」global 组件可覆盖全局背景色（优先级：控制台 > 主题预设 > 默认）
    val globalComp = ComponentThemeResolver.resolve(LocalComponentThemes.current, "global")
    // v1.1.22：global.css 纯白背景（默认配置）视为未定制，强制恢复经典皮肤胶囊渐变背景，不再整页白底
    val globalBg = globalComp?.backgroundColor
    val effectiveGlobalBg = if (globalBg != null && globalBg != Color.White) globalBg else null
    val activeBg = effectiveGlobalBg ?: (themeBgColor ?: Color(0xFFFFFFFF))

    Box(modifier = modifier.fillMaxSize()) {
        // ========== 1. 底层动态全屏画布：渲染多层悬浮流动胶囊与微光粒子群 ==========
        when {
            // v1.2.0：动态效果主题优先（环形特效，可叠加在任意主题上）
            dynamicEffect != DynamicEffectPreset.NONE -> {
                DynamicEffectBackground(
                    effect = dynamicEffect,
                    baseColor = themeBgColor ?: Color(0xFFFFF7EC),
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
                // 默认主题胶囊风背景（跟随当前主题色：如霓虹绿地图/国庆红/暖桃风）
                val base = themeBgColor ?: WindPeach1
                val accent = themePrimaryColor ?: WindPeach5

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // 1. 底衬渐变
                    val startX = -width * 0.2f + windShift * width * 0.3f
                    val startY = 0f + windShift * height * 0.1f
                    val endX = width * 1.1f + windShift * width * 0.2f
                    val endY = height * 1.0f

                    if (themeBgColor != null) {
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    base,
                                    base.copy(alpha = 0.94f),
                                    Color(
                                        red = (base.red + accent.red) / 2f,
                                        green = (base.green + accent.green) / 2f,
                                        blue = (base.blue + accent.blue) / 2f
                                    ).copy(alpha = 0.88f),
                                    base
                                ),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY)
                            )
                        )
                    } else {
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(WindPeach1, WindPeach2, WindPeach3, WindPeach4, WindPeach5),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY)
                            )
                        )
                    }

                    // 2. 大型核心流光胶囊 1（斜穿背景的主胶囊体）
                    val capsule1Center = Offset(
                        width * 0.50f + (windShift - 0.5f) * width * 0.08f,
                        height * 0.36f + (windShift - 0.5f) * height * 0.06f
                    )
                    drawCapsule(
                        center = capsule1Center,
                        length = width * 0.98f,
                        thickness = 175.dp.toPx() * glowPulse,
                        angleDegrees = -22f + (windShift - 0.5f) * 6f,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.26f),
                                accent.copy(alpha = 0.10f),
                                Color.Transparent
                            )
                        )
                    )
                    // 核心胶囊的高光微边框
                    drawCapsule(
                        center = capsule1Center,
                        length = width * 0.98f,
                        thickness = 175.dp.toPx() * glowPulse,
                        angleDegrees = -22f + (windShift - 0.5f) * 6f,
                        color = accent.copy(alpha = 0.16f),
                        style = Stroke(width = 1.5.dp.toPx())
                    )

                    // 3. 次级流动胶囊 2（右上方浮动胶囊）
                    val capsule2Center = Offset(
                        width * 0.82f - (windShift - 0.5f) * width * 0.10f,
                        height * 0.18f + (windShift - 0.5f) * height * 0.04f
                    )
                    drawCapsule(
                        center = capsule2Center,
                        length = width * 0.70f,
                        thickness = 115.dp.toPx(),
                        angleDegrees = 26f - (windShift - 0.5f) * 8f,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.20f),
                                Color.Transparent
                            )
                        )
                    )
                    drawCapsule(
                        center = capsule2Center,
                        length = width * 0.70f,
                        thickness = 115.dp.toPx(),
                        angleDegrees = 26f - (windShift - 0.5f) * 8f,
                        color = accent.copy(alpha = 0.12f),
                        style = Stroke(width = 1.2.dp.toPx())
                    )

                    // 4. 次级流动胶囊 3（左下方底座胶囊）
                    val capsule3Center = Offset(
                        width * 0.24f + (windShift - 0.5f) * width * 0.08f,
                        height * 0.76f - (windShift - 0.5f) * height * 0.05f
                    )
                    drawCapsule(
                        center = capsule3Center,
                        length = width * 0.80f,
                        thickness = 135.dp.toPx(),
                        angleDegrees = -18f + (windShift - 0.5f) * 6f,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                accent.copy(alpha = 0.18f),
                                accent.copy(alpha = 0.06f)
                            )
                        )
                    )

                    // 5. 动感悬浮小胶囊微粒群（国潮红金·Floating Capsule Constellation）
                    val goldGlint = Color(0xFFFFD700)
                    // 胶囊粒子 A：左上微光悬浮 (华夏朱红 + 鎏金边)
                    drawCapsule(
                        center = Offset(width * 0.16f, height * 0.12f + windShift * 18f),
                        length = 48.dp.toPx(),
                        thickness = 16.dp.toPx(),
                        angleDegrees = slay1Angle * 2.2f,
                        brush = Brush.horizontalGradient(
                            listOf(accent.copy(alpha = 0.45f), goldGlint.copy(alpha = 0.35f))
                        )
                    )
                    drawCapsule(
                        center = Offset(width * 0.16f, height * 0.12f + windShift * 18f),
                        length = 48.dp.toPx(),
                        thickness = 16.dp.toPx(),
                        angleDegrees = slay1Angle * 2.2f,
                        color = goldGlint.copy(alpha = 0.45f),
                        style = Stroke(width = 1.2.dp.toPx())
                    )

                    // 胶囊粒子 B：右上发光胶囊环 (鎏金华彩)
                    drawCapsule(
                        center = Offset(width * 0.88f, height * 0.26f - windShift * 15f),
                        length = 64.dp.toPx(),
                        thickness = 20.dp.toPx(),
                        angleDegrees = -32f + slay2Angle * 1.5f,
                        brush = Brush.linearGradient(
                            listOf(goldGlint.copy(alpha = 0.50f), accent.copy(alpha = 0.35f))
                        ),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // 胶囊粒子 C：左侧居中漂浮小胶囊 (祥瑞锦红)
                    drawCapsule(
                        center = Offset(width * 0.10f, height * 0.48f + windShift * 22f),
                        length = 42.dp.toPx(),
                        thickness = 14.dp.toPx(),
                        angleDegrees = 18f,
                        color = accent.copy(alpha = 0.30f)
                    )

                    // 胶囊粒子 D：右侧居中细长胶囊 (金丝织带)
                    drawCapsule(
                        center = Offset(width * 0.92f, height * 0.58f - windShift * 20f),
                        length = 58.dp.toPx(),
                        thickness = 16.dp.toPx(),
                        angleDegrees = -24f + slay1Angle,
                        brush = Brush.horizontalGradient(
                            listOf(accent.copy(alpha = 0.32f), goldGlint.copy(alpha = 0.30f))
                        )
                    )

                    // 胶囊粒子 E：中下方横向微动胶囊 (赤金华缎)
                    drawCapsule(
                        center = Offset(width * 0.52f, height * 0.84f + windShift * 14f),
                        length = 76.dp.toPx(),
                        thickness = 22.dp.toPx(),
                        angleDegrees = 10f - windShift * 5f,
                        brush = Brush.horizontalGradient(
                            listOf(accent.copy(alpha = 0.38f), goldGlint.copy(alpha = 0.25f))
                        )
                    )

                    // 胶囊粒子 F：右下角小胶囊点缀
                    drawCapsule(
                        center = Offset(width * 0.82f, height * 0.92f),
                        length = 36.dp.toPx(),
                        thickness = 12.dp.toPx(),
                        angleDegrees = -15f,
                        color = goldGlint.copy(alpha = 0.30f)
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
