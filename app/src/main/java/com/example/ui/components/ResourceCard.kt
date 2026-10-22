package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import com.example.ui.theme.DesignStyle
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.data.model.NavCard
import com.example.ui.theme.FlameRed
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.LocalUiverseState
import com.example.ui.theme.SunsetOrange
import com.example.ui.uiverse.CardStylePreset

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResourceCard(
    card: NavCard,
    isFavorite: Boolean,
    onCardClick: (NavCard) -> Unit,
    onFavoriteToggle: (NavCard) -> Unit,
    onCardLongClick: ((NavCard) -> Unit)? = null,
    modifier: Modifier = Modifier,
    // v1.1.4 主题分支：组件级主题定制（componentThemes）真正生效
    componentThemes: Map<String, String> = emptyMap()
) {
    val uiverse = LocalUiverseState.current
    // v1.2.2：主题 UI 色板——卡片背景/文字/边框跟随当前主题（默认经典=浅粉浅紫浅蓝，切主题后跟随主题色）
    val themeUi = LocalThemeUiColors.current

    // v1.1.4：若用户在主题切换中定制了「站点卡片」组件，则覆盖默认 cardStyle
    val cardCompStyle = ComponentThemeResolver.resolve(componentThemes, "card_item")

    val cardCornerRadius = when (uiverse.cardStyle) {
        CardStylePreset.CYBERPUNK -> 14.dp
        CardStylePreset.GLASSMORPHISM -> 16.dp
        CardStylePreset.NEUMORPHISM -> 16.dp
        CardStylePreset.NEO_BRUTALISM -> 6.dp
        CardStylePreset.RETRO_PIXEL -> 2.dp
        CardStylePreset.HOLOGRAPHIC -> 14.dp
        CardStylePreset.LUXURY_GOLD -> 12.dp
        CardStylePreset.CUSTOM -> (uiverse.customStyle?.cornerRadius ?: 50.dp)
        else -> 50.dp
    }.let {
        when {
            cardCompStyle != null -> cardCompStyle.cornerRadius
            themeUi.designStyle == DesignStyle.LIQUID_GLASS && uiverse.cardStyle == CardStylePreset.DEFAULT -> 28.dp
            else -> it
        }
    }

    val designStyle = themeUi.designStyle
    val useLiquidGlassSurface = designStyle == DesignStyle.LIQUID_GLASS &&
        uiverse.cardStyle == CardStylePreset.DEFAULT && cardCompStyle == null

    val cardShape = when {
        cardCompStyle != null -> RoundedCornerShape(cardCompStyle.cornerRadius)
        designStyle == DesignStyle.NEO_BRUTALISM -> RoundedCornerShape(8.dp)
        designStyle == DesignStyle.BRUTALISM -> RoundedCornerShape(0.dp)
        designStyle == DesignStyle.ANTI_DESIGN -> RoundedCornerShape(3.dp)
        designStyle == DesignStyle.FLAT_DESIGN -> RoundedCornerShape(4.dp)
        designStyle == DesignStyle.MATERIAL_DESIGN -> RoundedCornerShape(16.dp)
        designStyle == DesignStyle.CLAYMORPHISM -> RoundedCornerShape(22.dp)
        designStyle == DesignStyle.MINIMALISM -> RoundedCornerShape(6.dp)
        designStyle == DesignStyle.BAUHAUS -> RoundedCornerShape(4.dp)
        designStyle == DesignStyle.RETROFUTURISM -> RoundedCornerShape(10.dp)
        designStyle == DesignStyle.LIQUID_GLASS -> RoundedCornerShape(28.dp)
        designStyle == DesignStyle.GLASSMORPHISM -> RoundedCornerShape(18.dp)
        designStyle == DesignStyle.NEUMORPHISM -> RoundedCornerShape(16.dp)
        designStyle == DesignStyle.SKEUOMORPHISM -> RoundedCornerShape(12.dp)
        designStyle == DesignStyle.ACID_GRAPHICS -> RoundedCornerShape(8.dp)
        designStyle == DesignStyle.HAND_DRAWN -> RoundedCornerShape(24.dp)
        designStyle == DesignStyle.CARTOON -> RoundedCornerShape(28.dp)
        uiverse.cardStyle == CardStylePreset.CYBERPUNK -> RoundedCornerShape(topStart = 0.dp, topEnd = 14.dp, bottomEnd = 0.dp, bottomStart = 14.dp)
        uiverse.cardStyle == CardStylePreset.GLASSMORPHISM -> RoundedCornerShape(16.dp)
        uiverse.cardStyle == CardStylePreset.NEUMORPHISM -> RoundedCornerShape(16.dp)
        uiverse.cardStyle == CardStylePreset.NEO_BRUTALISM -> RoundedCornerShape(6.dp)
        uiverse.cardStyle == CardStylePreset.RETRO_PIXEL -> RoundedCornerShape(2.dp)
        uiverse.cardStyle == CardStylePreset.HOLOGRAPHIC -> RoundedCornerShape(14.dp)
        uiverse.cardStyle == CardStylePreset.LUXURY_GOLD -> RoundedCornerShape(12.dp)
        uiverse.cardStyle == CardStylePreset.CUSTOM -> RoundedCornerShape(uiverse.customStyle?.cornerRadius ?: 50.dp)
        else -> RoundedCornerShape(50.dp)
    }

    val cardElevation = when {
        designStyle == DesignStyle.FLAT_DESIGN || designStyle == DesignStyle.BRUTALISM || designStyle == DesignStyle.MINIMALISM -> 0.dp
        designStyle == DesignStyle.CLAYMORPHISM -> 5.dp
        designStyle == DesignStyle.NEO_BRUTALISM -> 4.dp
        designStyle == DesignStyle.SKEUOMORPHISM -> 4.dp
        designStyle == DesignStyle.MATERIAL_DESIGN || designStyle == DesignStyle.NEUMORPHISM -> 3.dp
        designStyle == DesignStyle.LIQUID_GLASS -> 7.dp
        designStyle == DesignStyle.GLASSMORPHISM || designStyle == DesignStyle.RETROFUTURISM || designStyle == DesignStyle.ACID_GRAPHICS -> 2.dp
        uiverse.cardStyle == CardStylePreset.CYBERPUNK -> 4.dp
        uiverse.cardStyle == CardStylePreset.GLASSMORPHISM -> 6.dp
        uiverse.cardStyle == CardStylePreset.NEUMORPHISM -> 4.dp
        uiverse.cardStyle == CardStylePreset.NEO_BRUTALISM -> 4.dp
        uiverse.cardStyle == CardStylePreset.RETRO_PIXEL -> 3.dp
        uiverse.cardStyle == CardStylePreset.HOLOGRAPHIC -> 5.dp
        uiverse.cardStyle == CardStylePreset.LUXURY_GOLD -> 4.dp
        uiverse.cardStyle == CardStylePreset.CUSTOM -> uiverse.customStyle?.shadowElevation ?: 1.dp
        else -> 1.dp
    }

    val shadowSpotColor = when (uiverse.cardStyle) {
        CardStylePreset.CYBERPUNK -> Color(0xFF00F0FF)
        CardStylePreset.GLASSMORPHISM -> Color(0xFF6366F1)
        CardStylePreset.NEO_BRUTALISM -> Color.Black
        CardStylePreset.RETRO_PIXEL -> Color.Black
        CardStylePreset.LUXURY_GOLD -> Color(0xFFD4AF37)
        CardStylePreset.CUSTOM -> uiverse.customStyle?.shadowColor ?: Color.Black
        else -> Color.Black
    }

    val titleColor = when (uiverse.cardStyle) {
        CardStylePreset.CYBERPUNK -> Color(0xFF00F0FF)
        CardStylePreset.GLASSMORPHISM -> Color(0xFF1E293B)
        CardStylePreset.NEUMORPHISM -> Color(0xFF1E293B)
        CardStylePreset.NEO_BRUTALISM -> Color.Black
        CardStylePreset.RETRO_PIXEL -> Color(0xFFE94560)
        CardStylePreset.LUXURY_GOLD -> Color(0xFFD4AF37)
        // v1.2.2：CUSTOM 若仅为 global.css 纯白背景，文字用主题文字色；真正定制色仍尊重
        CardStylePreset.CUSTOM -> if (uiverse.customStyle?.backgroundColor == Color.White) themeUi.text
            else (uiverse.customStyle?.textColor ?: themeUi.text)
        // 默认风格：主题文字色（配合主题卡片渐变）
        else -> themeUi.text
    }.let { if (cardCompStyle?.textColor != null) cardCompStyle.textColor else it }

    val descColor = when (uiverse.cardStyle) {
        CardStylePreset.CYBERPUNK -> Color(0xFF94A3B8)
        CardStylePreset.NEO_BRUTALISM -> Color(0xFF334155)
        // v1.2.2：CUSTOM 纯白默认时描述用主题弱化文字色
        CardStylePreset.CUSTOM -> if (uiverse.customStyle?.backgroundColor == Color.White) themeUi.textMuted
            else (uiverse.customStyle?.textColor ?: MaterialTheme.colorScheme.onSurfaceVariant).copy(alpha = 0.75f)
        // 默认风格：主题弱化文字色（浅色渐变上可读）
        else -> themeUi.textMuted
    }

    // v1.1.4 主题分支：组件定制覆盖（卡片背景/文字色由 componentThemes 决定时优先）
    val compBg = cardCompStyle?.backgroundColor
    val compText = cardCompStyle?.textColor

    val cardBgModifier: Modifier = when (uiverse.cardStyle) {
        CardStylePreset.CYBERPUNK -> Modifier.background(Color(0xFF0F101A))
        CardStylePreset.GLASSMORPHISM -> Modifier.background(Color(0xE6FFFFFF))
        CardStylePreset.NEUMORPHISM -> Modifier.background(Color(0xFFF1F5F9))
        CardStylePreset.NEO_BRUTALISM -> Modifier.background(Color(0xFFFFFFFF))
        CardStylePreset.RETRO_PIXEL -> Modifier.background(Color(0xFF1A1A2E))
        CardStylePreset.HOLOGRAPHIC -> Modifier.background(
            Brush.linearGradient(listOf(Color(0xFFFAF5FF), Color(0xFFF0FDF4)))
        )
        CardStylePreset.LUXURY_GOLD -> Modifier.background(Color(0xFF1A1A1A))
        // v1.2.2：CUSTOM 若仅为 global.css 纯白背景，卡片用主题卡片渐变；真正定制色仍尊重
        CardStylePreset.CUSTOM -> {
            val custom = uiverse.customStyle
            val bgBrush = custom?.backgroundBrush
            val bgColor = custom?.backgroundColor
            when {
                bgBrush != null -> Modifier.background(bgBrush)
                bgColor != null && bgColor != Color.White -> Modifier.background(bgColor)
                else -> Modifier.background(
                    if (themeUi.cardGradient.size >= 2)
                        Brush.linearGradient(themeUi.cardGradient)
                    else Brush.linearGradient(listOf(themeUi.surface, themeUi.surface))
                )
            }
        }
        // 默认风格：主题卡片渐变背景（跟随当前主题）
        else -> Modifier.background(
            if (themeUi.cardGradient.size >= 2)
                Brush.linearGradient(themeUi.cardGradient)
            else Brush.linearGradient(listOf(themeUi.surface, themeUi.surface))
        )
    }.let { if (compBg != null) it.then(Modifier.background(compBg)) else it }

    val cardBorderModifier: Modifier = when {
        designStyle == DesignStyle.NEO_BRUTALISM -> Modifier.border(2.5.dp, Color.Black, cardShape)
        designStyle == DesignStyle.BRUTALISM -> Modifier.border(2.dp, Color.Black, cardShape)
        designStyle == DesignStyle.ANTI_DESIGN -> Modifier.border(2.2.dp, Color(0xFF00FF66), cardShape)
        designStyle == DesignStyle.FLAT_DESIGN -> Modifier
        designStyle == DesignStyle.MATERIAL_DESIGN -> Modifier.border(1.dp, Color(0xFF6750A4).copy(alpha = 0.25f), cardShape)
        designStyle == DesignStyle.CLAYMORPHISM -> Modifier.border(1.5.dp, Color.White.copy(alpha = 0.85f), cardShape)
        designStyle == DesignStyle.MINIMALISM -> Modifier.border(0.8.dp, Color(0xFFE4E4E7), cardShape)
        designStyle == DesignStyle.BAUHAUS -> Modifier.border(2.dp, Color.Black, cardShape)
        designStyle == DesignStyle.RETROFUTURISM -> Modifier.border(1.5.dp, Brush.linearGradient(listOf(Color(0xFF00F0FF), Color(0xFFFF007F))), cardShape)
        designStyle == DesignStyle.LIQUID_GLASS -> Modifier.border(1.4.dp, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.95f), Color(0xFF00C6FF).copy(alpha = 0.5f))), cardShape)
        designStyle == DesignStyle.GLASSMORPHISM -> Modifier.border(1.2.dp, Color.White.copy(alpha = 0.45f), cardShape)
        designStyle == DesignStyle.NEUMORPHISM -> Modifier.border(1.2.dp, Brush.linearGradient(listOf(Color.White, Color(0xFFCBD5E1))), cardShape)
        designStyle == DesignStyle.SKEUOMORPHISM -> Modifier.border(1.5.dp, Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFF8B5A2B))), cardShape)
        designStyle == DesignStyle.ACID_GRAPHICS -> Modifier.border(1.8.dp, Brush.sweepGradient(listOf(Color(0xFFCCFF00), Color(0xFFB026FF), Color(0xFF00F0FF), Color(0xFFCCFF00))), cardShape)
        uiverse.cardStyle == CardStylePreset.CYBERPUNK -> Modifier.border(1.5.dp, Color(0xFF00F0FF), cardShape)
        uiverse.cardStyle == CardStylePreset.GLASSMORPHISM -> Modifier.border(
            1.2.dp,
            Brush.linearGradient(listOf(Color(0x88FFFFFF), Color(0x44A855F7), Color(0x336366F1))),
            cardShape
        )
        uiverse.cardStyle == CardStylePreset.NEUMORPHISM -> Modifier.border(1.dp, Color(0xFFCBD5E1), cardShape)
        uiverse.cardStyle == CardStylePreset.NEO_BRUTALISM -> Modifier.border(2.5.dp, Color(0xFF000000), cardShape)
        uiverse.cardStyle == CardStylePreset.RETRO_PIXEL -> Modifier.border(2.dp, Color(0xFFE94560), cardShape)
        uiverse.cardStyle == CardStylePreset.HOLOGRAPHIC -> Modifier.border(
            1.5.dp,
            Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF06B6D4))),
            cardShape
        )
        uiverse.cardStyle == CardStylePreset.LUXURY_GOLD -> Modifier.border(1.2.dp, Color(0xFFD4AF37), cardShape)
        uiverse.cardStyle == CardStylePreset.CUSTOM -> {
            val custom = uiverse.customStyle
            val customBorder = custom?.borderColor
            val bColor = if (customBorder != null && customBorder != Color.Transparent) customBorder
            // v1.1.22：仅 global.css 纯白默认（无主动边框定制）时，恢复经典金色边框，保证卡片有清晰描边
            else if (custom?.backgroundColor == Color.White) Color(0xFFFFD700).copy(alpha = 0.55f)
            else MaterialTheme.colorScheme.outline
            val bWidth = custom?.borderWidth ?: 1.dp
            Modifier.border(bWidth, bColor, cardShape)
        }
        // v1.2.2：默认边框也跟随主题主色
        else -> Modifier.border(1.2.dp, themeUi.border, cardShape)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, end = 2.dp)
            .testTag("resource_card_${card.id}")
    ) {
        // Main Card Body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = cardElevation,
                    shape = cardShape,
                    spotColor = shadowSpotColor,
                    ambientColor = shadowSpotColor.copy(alpha = 0.2f)
                )
                .clip(cardShape)
                .then(cardBgModifier)
                .streamingBorder(
                    cornerRadius = cardCornerRadius,
                    strokeWidth = if (useLiquidGlassSurface) 1.6.dp else 1.4.dp,
                    glowWidth = 3.2.dp,
                    accentColor = if (useLiquidGlassSurface) themeUi.secondary else themeUi.primary,
                    tailColor = if (useLiquidGlassSurface) themeUi.accent.copy(alpha = 0.72f) else themeUi.primary.copy(alpha = 0.45f),
                    baseBorderColor = if (useLiquidGlassSurface) Color.White.copy(alpha = 0.46f) else themeUi.border.copy(alpha = 0.35f),
                    phaseOffset = (card.id.hashCode().let { if (it < 0) -it else it } % 360).toFloat(),
                    showGlow = !useLiquidGlassSurface,
                    rainbow = !useLiquidGlassSurface
                )
                .combinedClickable(
                    onClick = { onCardClick(card) },
                    onLongClick = { onCardLongClick?.invoke(card) }
                )
                .padding(horizontal = 7.dp, vertical = 7.dp)
        ) {
            if (useLiquidGlassSurface) {
                // 保留背景里的彩色折射，用深色玻璃底、斜向高光和双层亮边塑造厚度。
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.13f),
                                    themeUi.secondary.copy(alpha = 0.08f),
                                    themeUi.surface.copy(alpha = 0.24f),
                                    themeUi.accent.copy(alpha = 0.09f)
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.22f),
                                    Color.White.copy(alpha = 0.06f),
                                    Color.Transparent,
                                    themeUi.secondary.copy(alpha = 0.035f),
                                    themeUi.accent.copy(alpha = 0.11f)
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                        .border(
                            0.7.dp,
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.30f),
                                    Color.Transparent,
                                    themeUi.secondary.copy(alpha = 0.20f),
                                    themeUi.accent.copy(alpha = 0.18f)
                                )
                            ),
                            cardShape
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.56f),
                                    themeUi.secondary.copy(alpha = 0.52f),
                                    Color.White.copy(alpha = 0.30f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            } else {
                // 其他已选择的卡片风格沿用原有渲染。
                RainbowSpinBackground(Modifier.fillMaxSize())
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(alpha = 0.72f),
                                    Color(0xFFF6F2FF).copy(alpha = 0.62f),
                                    Color.White.copy(alpha = 0.76f)
                                )
                            )
                        )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Automatic Site Favicon Icon (Automatic fetch, strictly zero text fallback)
                SiteBrandIcon(
                    url = card.url,
                    title = card.title,
                    fallbackText = card.fallbackText,
                    iconUrl = card.icon,
                    size = 24.dp
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Title and description（三排布局下保证站点名称完整可见）
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (card.desc.isNotBlank()) {
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = card.desc,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 8.5.sp,
                                color = descColor
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Favorite Toggle Button
                IconButton(
                    onClick = { onFavoriteToggle(card) },
                    modifier = Modifier
                        .size(20.dp)
                        .testTag("favorite_button_${card.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (isFavorite) "已收藏" else "加入收藏",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else descColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }

        // v1.1.12：删除所有角标功能（用户要求）——不再渲染公司角标 / RibbonBadge
    }
}

// ============================================================
// v1.0.19 角标动态特效（沿用原 RibbonBadge 的 CSS keyframes 动态设计）
// 流光扫过 + 呼吸缩放 + 摇摆，所有角标统一动态呈现，不遮挡站点内容
// ============================================================
@Composable
private fun badgeFlow(): Float {
    val transition = rememberInfiniteTransition(label = "company_badge_flow")
    val flow by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "company_badge_flow_v"
    )
    return flow
}

@Composable
private fun badgeBreathe(): Float {
    val transition = rememberInfiniteTransition(label = "company_badge_breathe")
    val s by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "company_badge_breathe_v"
    )
    return s
}

@Composable
private fun badgeWobble(): Float {
    val transition = rememberInfiniteTransition(label = "company_badge_wobble")
    val w by transition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "company_badge_wobble_v"
    )
    return w
}
