package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.sin

import com.example.ui.theme.LocalUiverseState
import com.example.ui.uiverse.ButtonStylePreset
import androidx.compose.foundation.border

/**
 * Sahara Wave Button matching the exact visual style in the screenshot:
 * - Vivid glowing red-orange gradient (#E50914 -> #FF5722 -> #FFA000)
 * - 52dp height capsule with 26dp rounded corner shape and glowing shadow
 * - Flowing animated sine wave sand-dune effect across the background
 * - Left: 4-point sparkle icon with "WELCOME" & "SAHARA · 分类列表"
 * - Right: Translucent pill with "点击抽选"
 * - Dynamically adapts full UI design to Uiverse selected button kits!
 * v1.1.15：默认文案改为「全部资源 / 分类列表 / 查看分类」，默认渐变改为中国红系
 */
@Composable
fun SaharaWaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    welcomeText: String = "全部资源",
    titleText: String = "分类列表",
    actionText: String = "查看分类"
) {
    val uiverse = LocalUiverseState.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth press feedback
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "pressScale"
    )

    // Flowing sine wave animation
    val infiniteTransition = rememberInfiniteTransition(label = "sahara_wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    val buttonShape = when (uiverse.buttonStyle) {
        ButtonStylePreset.CYBERPUNK_GLOW -> RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 16.dp)
        ButtonStylePreset.BRUTALIST_OFFSET -> RoundedCornerShape(6.dp)
        ButtonStylePreset.GLASS_AURORA -> RoundedCornerShape(26.dp)
        ButtonStylePreset.NEUMORPHIC_PUSH -> RoundedCornerShape(26.dp)
        ButtonStylePreset.RETRO_COIN -> RoundedCornerShape(2.dp)
        ButtonStylePreset.CUSTOM -> RoundedCornerShape(uiverse.customStyle?.cornerRadius ?: 26.dp)
        else -> RoundedCornerShape(26.dp)
    }

    val buttonElevation = when (uiverse.buttonStyle) {
        ButtonStylePreset.CYBERPUNK_GLOW -> 8.dp
        ButtonStylePreset.BRUTALIST_OFFSET -> 5.dp
        ButtonStylePreset.GLASS_AURORA -> 6.dp
        ButtonStylePreset.NEUMORPHIC_PUSH -> 5.dp
        ButtonStylePreset.RETRO_COIN -> 4.dp
        ButtonStylePreset.CUSTOM -> uiverse.customStyle?.shadowElevation ?: 8.dp
        else -> 8.dp
    }

    val spotColor = when (uiverse.buttonStyle) {
        ButtonStylePreset.CYBERPUNK_GLOW -> Color(0xFF00F0FF)
        ButtonStylePreset.BRUTALIST_OFFSET -> Color.Black
        ButtonStylePreset.GLASS_AURORA -> Color(0xFF6366F1)
        ButtonStylePreset.NEUMORPHIC_PUSH -> Color(0xFF94A3B8)
        ButtonStylePreset.RETRO_COIN -> Color(0xFFFFCC00)
        ButtonStylePreset.CUSTOM -> uiverse.customStyle?.shadowColor ?: Color(0xFFFF5722)
        else -> Color(0xFFFF5722)
    }

    val buttonBgModifier: Modifier = when (uiverse.buttonStyle) {
        ButtonStylePreset.CYBERPUNK_GLOW -> Modifier.background(Color(0xFF0F101A))
        ButtonStylePreset.BRUTALIST_OFFSET -> Modifier.background(Color(0xFFFFE600))
        ButtonStylePreset.GLASS_AURORA -> Modifier.background(Color(0x44FFFFFF))
        ButtonStylePreset.NEUMORPHIC_PUSH -> Modifier.background(Color(0xFFE2E8F0))
        ButtonStylePreset.RETRO_COIN -> Modifier.background(Color(0xFF16213E))
        ButtonStylePreset.CUSTOM -> {
            val custom = uiverse.customStyle
            val bgBrush = custom?.backgroundBrush
            val bgColor = custom?.backgroundColor
            when {
                bgBrush != null -> Modifier.background(bgBrush)
                bgColor != null -> Modifier.background(bgColor)
                else -> Modifier.background(
                    // v1.1.15：中国红系渐变（用户要求）
                    Brush.horizontalGradient(listOf(Color(0xFFC8102E), Color(0xFFDE2910), Color(0xFFFF5722)))
                )
            }
        }
        else -> Modifier.background(
            Brush.horizontalGradient(listOf(Color(0xFFC8102E), Color(0xFFDE2910), Color(0xFFFF5722)))
        )
    }

    val buttonBorderModifier: Modifier = when (uiverse.buttonStyle) {
        ButtonStylePreset.CYBERPUNK_GLOW -> Modifier.border(1.5.dp, Color(0xFF00F0FF), buttonShape)
        ButtonStylePreset.BRUTALIST_OFFSET -> Modifier.border(3.dp, Color.Black, buttonShape)
        ButtonStylePreset.GLASS_AURORA -> Modifier.border(
            1.5.dp,
            Brush.linearGradient(listOf(Color(0x88FFFFFF), Color(0x44A855F7))),
            buttonShape
        )
        ButtonStylePreset.NEUMORPHIC_PUSH -> Modifier.border(1.dp, Color(0xFFCBD5E1), buttonShape)
        ButtonStylePreset.RETRO_COIN -> Modifier.border(2.dp, Color(0xFFFFCC00), buttonShape)
        ButtonStylePreset.CUSTOM -> {
            val custom = uiverse.customStyle
            val customBorder = custom?.borderColor
            val bColor = if (customBorder != null && customBorder != Color.Transparent) customBorder else Color.Transparent
            val bWidth = custom?.borderWidth ?: 0.dp
            if (bWidth > 0.dp && bColor != Color.Transparent) Modifier.border(bWidth, bColor, buttonShape) else Modifier
        }
        else -> Modifier
    }

    val isDarkText = uiverse.buttonStyle == ButtonStylePreset.BRUTALIST_OFFSET || uiverse.buttonStyle == ButtonStylePreset.NEUMORPHIC_PUSH
    val mainTextColor = when {
        uiverse.buttonStyle == ButtonStylePreset.CYBERPUNK_GLOW -> Color(0xFF00F0FF)
        uiverse.buttonStyle == ButtonStylePreset.RETRO_COIN -> Color(0xFFFFCC00)
        isDarkText -> Color(0xFF0F172A)
        uiverse.buttonStyle == ButtonStylePreset.CUSTOM -> uiverse.customStyle?.textColor ?: Color.White
        else -> Color.White
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .scale(pressScale)
            .shadow(
                elevation = buttonElevation,
                shape = buttonShape,
                ambientColor = spotColor.copy(alpha = 0.3f),
                spotColor = spotColor
            )
            .clip(buttonShape)
            .then(buttonBgModifier)
            .then(buttonBorderModifier)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .height(52.dp)
            .testTag("sahara_wave_button"),
        contentAlignment = Alignment.Center
    ) {
        // Dynamic flowing sine wave background for non-brutalist styles
        if (uiverse.buttonStyle != ButtonStylePreset.BRUTALIST_OFFSET) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Wave Layer 1
                val path1 = Path().apply {
                    moveTo(0f, h)
                    for (x in 0..w.toInt() step 6) {
                        val angle = (x / w) * 2 * PI.toFloat() + waveOffset
                        val y = h * 0.58f + sin(angle) * (h * 0.22f)
                        lineTo(x.toFloat(), y)
                    }
                    lineTo(w, h)
                    close()
                }
                drawPath(path = path1, color = mainTextColor.copy(alpha = 0.10f))

                // Wave Layer 2
                val path2 = Path().apply {
                    moveTo(0f, h)
                    for (x in 0..w.toInt() step 6) {
                        val angle = (x / w) * 3 * PI.toFloat() - waveOffset * 0.7f
                        val y = h * 0.65f + sin(angle) * (h * 0.16f)
                        lineTo(x.toFloat(), y)
                    }
                    lineTo(w, h)
                    close()
                }
                drawPath(path = path2, color = mainTextColor.copy(alpha = 0.08f))

                // Subtle top gloss highlight
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.22f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = h * 0.5f
                    )
                )
            }
        }

        // Button Content: Left Sparkle + Texts, Right Pill
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left 4-Point Sparkle
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = mainTextColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Brand & Title
            Column {
                Text(
                    text = welcomeText,
                    color = mainTextColor.copy(alpha = 0.85f),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    lineHeight = 12.sp
                )
                Text(
                    text = titleText,
                    color = mainTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.4.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Right Translucent / Styled Pill Capsule ("点击抽选")
            val rightPillBg = when {
                uiverse.buttonStyle == ButtonStylePreset.BRUTALIST_OFFSET -> Color.Black
                uiverse.buttonStyle == ButtonStylePreset.CYBERPUNK_GLOW -> Color(0xFF00F0FF).copy(alpha = 0.2f)
                isDarkText -> Color.Black.copy(alpha = 0.1f)
                else -> Color.White.copy(alpha = 0.25f)
            }
            val rightPillTextColor = when {
                uiverse.buttonStyle == ButtonStylePreset.BRUTALIST_OFFSET -> Color.White
                uiverse.buttonStyle == ButtonStylePreset.CYBERPUNK_GLOW -> Color(0xFF00F0FF)
                isDarkText -> Color(0xFF0F172A)
                else -> Color.White
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(rightPillBg)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = actionText,
                    color = rightPillTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
