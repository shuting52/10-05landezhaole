package com.example.ui.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// Frutiger Aero · 千禧复兴 —— 集中定义
// 设计稿：~/workspace/user/files/s11-aero_4_9724.html
// ============================================================

object AeroColors {
    val sky1 = Color(0xFF7EC8F7)
    val sky2 = Color(0xFFE8F9FF)
    val blue = Color(0xFF00A8E8)
    val blueDeep = Color(0xFF0090C9)
    val blueLight = Color(0xFF35C3F5)
    val green = Color(0xFF7ED957)
    val greenDeep = Color(0xFF5DBE3D)
    val greenLight = Color(0xFF9BEF83)
    val ink = Color(0xFF0A2540)
    val subText = Color(0xFF33576F)
    val mutedText = Color(0xFF5B7A90)
    val hintText = Color(0xFF8AA5B8)
    val starGold = Color(0xFFFFB800)
}

object AeroBrush {
    /** 天空背景渐变 */
    val skyBackground = Brush.verticalGradient(
        listOf(
            Color(0xFF7EC8F7),
            Color(0xFFBDE4FB),
            Color(0xFFDFF3FE),
            Color(0xFFE8F9FF)
        )
    )
    /** 主按钮蓝渐变 */
    val blueButton = Brush.verticalGradient(
        listOf(Color(0xFF35C3F5), AeroColors.blue, AeroColors.blueDeep)
    )
    /** 绿按钮渐变 */
    val greenButton = Brush.verticalGradient(
        listOf(Color(0xFF9BEF83), AeroColors.green, AeroColors.greenDeep)
    )
    /** 卡片玻璃白渐变 */
    val cardGlass = Brush.verticalGradient(
        listOf(Color.White.copy(alpha = 0.75f), Color.White.copy(alpha = 0.55f))
    )
    /** 顶部高光条（按钮 ::after） */
    val topGloss = Brush.verticalGradient(
        listOf(Color.White.copy(alpha = 0.5f), Color.White.copy(alpha = 0f))
    )
    /** 斜向反射层（hero ::before, 115deg） */
    val diagonalSheen = Brush.linearGradient(
        listOf(
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.08f),
            Color.Transparent
        ),
        start = Offset(0f, 0f),
        end = Offset(800f, 400f)
    )
    /** 开关轨道（关） */
    val switchTrackOff = Brush.verticalGradient(
        listOf(Color(0xFFB8D2E2), Color(0xFFD3E6F2))
    )
    /** 开关轨道（开） */
    val switchTrackOn = Brush.verticalGradient(
        listOf(AeroColors.blueDeep, AeroColors.blue)
    )
    /** 旋钮高光 */
    val knobGloss = Brush.radialGradient(
        listOf(Color.White, Color(0xFFE8F4FB), Color(0xFFCFE8F6)),
        center = Offset(0.35f, 0.3f)
    )
}

object AeroDimen {
    val cardRadius = 24.dp
    val buttonRadius = 22.dp
    val inputRadius = 18.dp
    val pillRadius = 999.dp
}

/** 顶部一线高光（所有卡片/按钮） */
fun Modifier.aeroTopHighlight(
    radius: Dp = 24.dp,
    alpha: Float = 0.6f
): Modifier = this.drawBehind {
    val strokeW = 1.dp.toPx()
    drawRoundRect(
        color = Color.White.copy(alpha = alpha),
        topLeft = Offset(strokeW / 2, strokeW / 2),
        size = androidx.compose.ui.geometry.Size(size.width - strokeW, strokeW * 2),
        cornerRadius = CornerRadius(radius.toPx()),
        style = Stroke(strokeW)
    )
}

// ============================================================
// fa-card · 玻璃卡片
// ============================================================
@Composable
fun AeroCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickMod = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(AeroDimen.cardRadius), ambientColor = Color(0xFF0A2540).copy(alpha = 0.12f))
            .clip(RoundedCornerShape(AeroDimen.cardRadius))
            .background(AeroBrush.cardGlass)
            .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(AeroDimen.cardRadius))
            .aeroTopHighlight(AeroDimen.cardRadius)
            .then(clickMod)
            .padding(18.dp)
    ) {
        content()
    }
}

/** 卡片小节标题（fa-sec + fa-dot） */
@Composable
fun AeroSectionTitle(text: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(AeroColors.blue)
                .shadow(4.dp, CircleShape, ambientColor = AeroColors.blue)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = AeroColors.ink)
    }
}

// ============================================================
// fa-btn · 按钮（含变体）
// ============================================================
enum class AeroButtonStyle { Blue, Green, Ghost, Outline }

@Composable
fun AeroButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: AeroButtonStyle = AeroButtonStyle.Blue,
    enabled: Boolean = true,
    small: Boolean = false,
    large: Boolean = false,
    block: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val interaction = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = 1f, // 简化：按压态由 clickable ripple 承担
        label = "aeroBtnScale"
    )
    val bg = when (style) {
        AeroButtonStyle.Blue -> AeroBrush.blueButton
        AeroButtonStyle.Green -> AeroBrush.greenButton
        AeroButtonStyle.Ghost -> Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.65f), Color.White.copy(alpha = 0.65f))
        )
        AeroButtonStyle.Outline -> Brush.verticalGradient(
            listOf(Color.Transparent, Color.Transparent)
        )
    }
    val textColor = when (style) {
        AeroButtonStyle.Blue, AeroButtonStyle.Green -> Color.White
        AeroButtonStyle.Ghost, AeroButtonStyle.Outline -> AeroColors.blueDeep
    }
    val radius = when {
        small -> 16.dp
        large -> 26.dp
        else -> AeroDimen.buttonRadius
    }
    val vPad = when {
        small -> 9.dp
        large -> 17.dp
        else -> 14.dp
    }
    val hPad = when {
        small -> 18.dp
        large -> 36.dp
        else -> 28.dp
    }
    val fontSize = when {
        small -> 13.sp
        large -> 17.sp
        else -> 15.sp
    }
    val borderMod = when (style) {
        AeroButtonStyle.Ghost -> Modifier.border(1.dp, AeroColors.blue.copy(alpha = 0.35f), RoundedCornerShape(radius))
        AeroButtonStyle.Outline -> Modifier.border(2.dp, AeroColors.blue, RoundedCornerShape(radius))
        else -> Modifier
    }
    val glowColor = when (style) {
        AeroButtonStyle.Green -> AeroColors.green.copy(alpha = 0.4f)
        else -> AeroColors.blue.copy(alpha = 0.38f)
    }

    Box(
        modifier = modifier
            .then(if (block) Modifier.fillMaxWidth() else Modifier)
            .scale(scale)
            .shadow(6.dp, RoundedCornerShape(radius), ambientColor = glowColor)
            .clip(RoundedCornerShape(radius))
            .background(bg)
            .then(borderMod)
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = hPad, vertical = vPad),
        contentAlignment = Alignment.Center
    ) {
        // 顶部 46% 高光条（::after）
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.5f), Color.White.copy(alpha = 0f))
                    )
                )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                color = if (enabled) textColor else textColor.copy(alpha = 0.5f)
            )
        }
    }
}

// ============================================================
// fa-pagehead · 页头
// ============================================================
@Composable
fun AeroPageHead(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 2.dp, bottom = 16.dp, top = 6.dp)) {
        Text(
            text = title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = AeroColors.ink,
            letterSpacing = 0.5.sp
        )
        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = AeroColors.subText,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// ============================================================
// fa-hero · 大玻璃 Hero 卡
// ============================================================
@Composable
fun AeroHero(
    tag: String,
    title: String,
    enSubtitle: String,
    desc: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    deco: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(28.dp), ambientColor = Color(0xFF0A2540).copy(alpha = 0.12f))
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.75f), Color.White.copy(alpha = 0.28f))
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(28.dp))
            .aeroTopHighlight(28.dp)
            .padding(30.dp, 26.dp)
    ) {
        // 斜向反射层
        Box(modifier = Modifier.matchParentSize().background(AeroBrush.diagonalSheen))
        if (deco != null) {
            Box(modifier = Modifier.align(Alignment.BottomEnd).padding(18.dp)) { deco() }
        }
        Column {
            // tag 胶囊
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(AeroColors.blue.copy(alpha = 0.1f))
                    .border(1.dp, AeroColors.blue.copy(alpha = 0.3f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = tag,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AeroColors.blueDeep,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontSize = 34.sp, fontWeight = FontWeight.Black, color = AeroColors.ink, lineHeight = 40.sp)
            Text(
                text = enSubtitle,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = AeroColors.blueDeep,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = desc,
                fontSize = 14.sp,
                color = Color(0xFF2C4F66),
                lineHeight = 25.sp,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            AeroButton(text = buttonText, onClick = onButtonClick)
        }
    }
}

// ============================================================
// fa-input · 输入框
// ============================================================
@Composable
fun AeroInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    label: String = "",
    singleLine: Boolean = true
) {
    Column(modifier = modifier.padding(bottom = 14.dp)) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF25465E),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 15.sp,
                color = AeroColors.ink
            ),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(AeroDimen.inputRadius))
                        .background(Color.White.copy(alpha = 0.75f))
                        .border(
                            1.5.dp,
                            AeroColors.blue.copy(alpha = 0.3f),
                            RoundedCornerShape(AeroDimen.inputRadius)
                        )
                        .padding(13.dp, 16.dp)
                ) {
                    if (value.isEmpty() && placeholder.isNotBlank()) {
                        Text(text = placeholder, fontSize = 15.sp, color = AeroColors.hintText)
                    }
                    inner()
                }
            }
        )
    }
}

// ============================================================
// fa-switch · 开关（含 💧 水滴）
// ============================================================
@Composable
fun AeroSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    desc: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (title.isNotBlank()) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AeroColors.ink)
            }
            if (desc.isNotBlank()) {
                Text(text = desc, fontSize = 12.sp, color = AeroColors.mutedText, modifier = Modifier.padding(top = 2.dp))
            }
        }
        // 56x32 轨道
        val knobOffset by animateFloatAsState(
            targetValue = if (checked) 24.dp.value else 0.dp.value,
            animationSpec = tween(250, easing = FastOutSlowInEasing),
            label = "aeroSwitchKnob"
        )
        Box(
            modifier = Modifier
                .size(56.dp, 32.dp)
                .shadow(2.dp, CircleShape, ambientColor = Color(0xFF0A2540).copy(alpha = 0.18f))
                .clip(CircleShape)
                .background(if (checked) AeroBrush.switchTrackOn else AeroBrush.switchTrackOff)
                .aeroTopHighlight(999.dp, 0.6f)
        ) {
            if (checked) {
                // 💧 水滴
                Text(
                    text = "💧",
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
                )
            }
            // 26px 旋钮
            Box(
                modifier = Modifier
                    .offset(x = (3 + knobOffset).dp)
                    .align(Alignment.CenterStart)
                    .size(26.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(AeroBrush.knobGloss)
            )
        }
    }
}

// ============================================================
// fa-checkbox · 复选框
// ============================================================
@Composable
fun AeroCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(if (checked) AeroBrush.blueButton else Color.White.copy(alpha = 0.8f))
                .border(
                    1.5.dp,
                    if (checked) Color.Transparent else AeroColors.blue.copy(alpha = 0.4f),
                    RoundedCornerShape(9.dp)
                )
                .aeroTopHighlight(9.dp, 0.6f),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Text(text = "✓", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AeroColors.ink)
    }
}

// ============================================================
// fa-slider · 滑杆
// ============================================================
@Composable
fun AeroSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val fraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.7f))
                .drawBehind {
                    // 蓝色填充部分
                    val fillW = size.width * fraction
                    if (fillW > 0) {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                listOf(AeroColors.blue, Color(0xFF6FD6FF))
                            ),
                            size = androidx.compose.ui.geometry.Size(fillW, size.height),
                            cornerRadius = CornerRadius(size.height / 2)
                        )
                    }
                }
                .aeroTopHighlight(999.dp, 0.6f)
        ) {
            // 白色高光滑块 26px
            Box(
                modifier = Modifier
                    .offset(x = ((fraction * 100).dp - 13.dp).coerceAtLeast(0.dp))
                    .align(Alignment.CenterStart)
                    .size(26.dp)
                    .shadow(2.dp, CircleShape, ambientColor = AeroColors.blue.copy(alpha = 0.4f))
                    .clip(CircleShape)
                    .background(AeroBrush.knobGloss)
                    .border(1.dp, AeroColors.blue.copy(alpha = 0.35f), CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.7f))
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            Text(
                text = value.toInt().toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AeroColors.blueDeep
            )
        }
    }
}

// ============================================================
// fa-seg · 分段选择器
// ============================================================
@Composable
fun AeroSegmented(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.5f))
            .border(1.dp, Color.White.copy(alpha = 0.85f), CircleShape)
            .aeroTopHighlight(999.dp, 0.6f)
            .padding(5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEachIndexed { i, opt ->
            val selected = i == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(if (selected) AeroBrush.blueButton else Color.Transparent)
                    .then(
                        if (selected) Modifier.shadow(
                            3.dp,
                            CircleShape,
                            ambientColor = AeroColors.blue.copy(alpha = 0.35f)
                        ) else Modifier
                    )
                    .clickable { onSelect(i) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = opt,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (selected) Color.White else Color(0xFF4A6B8A)
                )
            }
        }
    }
}

// ============================================================
// fa-prog · 进度条
// ============================================================
@Composable
fun AeroProgress(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    green: Boolean = false
) {
    Column(modifier = modifier.padding(vertical = 5.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AeroColors.subText)
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AeroColors.subText
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.65f))
                .drawBehind {
                    drawRoundRect(
                        color = Color(0xFF0A2540).copy(alpha = 0.08f),
                        style = Stroke(1.dp.toPx())
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(
                        if (green) Brush.horizontalGradient(
                            listOf(AeroColors.greenDeep, Color(0xFFA5EC82))
                        ) else Brush.horizontalGradient(
                            listOf(AeroColors.blue, Color(0xFF6FD6FF))
                        )
                    )
            ) {
                // 顶部高光
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.White.copy(alpha = 0.45f), Color.White.copy(alpha = 0f))
                            )
                        )
                )
            }
        }
    }
}

// ============================================================
// fa-stat · 数据统计
// ============================================================
@Composable
fun AeroStat(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(18.dp))
            .aeroTopHighlight(18.dp, 0.6f)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = AeroColors.blueDeep)
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AeroColors.mutedText)
    }
}

@Composable
fun AeroStatsRow(stats: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stats.forEach { (v, l) ->
            AeroStat(value = v, label = l, modifier = Modifier.weight(1f))
        }
    }
}

// ============================================================
// fa-badge · 徽标
// ============================================================
enum class AeroBadgeStyle { Blue, Green, Soft }

@Composable
fun AeroBadge(text: String, style: AeroBadgeStyle = AeroBadgeStyle.Blue, modifier: Modifier = Modifier) {
    val bg = when (style) {
        AeroBadgeStyle.Blue -> AeroBrush.blueButton
        AeroBadgeStyle.Green -> AeroBrush.greenButton
        AeroBadgeStyle.Soft -> Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.7f), Color.White.copy(alpha = 0.7f))
        )
    }
    val textColor = when (style) {
        AeroBadgeStyle.Soft -> AeroColors.blueDeep
        else -> Color.White
    }
    val borderMod = if (style == AeroBadgeStyle.Soft) {
        Modifier.border(1.dp, AeroColors.blue.copy(alpha = 0.35f), CircleShape)
    } else Modifier
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bg)
            .then(borderMod)
            .aeroTopHighlight(999.dp, 0.6f)
            .padding(horizontal = 13.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
    }
}

// ============================================================
// fa-chip · 筛选芯片
// ============================================================
@Composable
fun AeroChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(if (selected) AeroBrush.blueButton else Color.White.copy(alpha = 0.55f))
            .border(
                1.5.dp,
                if (selected) Color.Transparent else AeroColors.blue.copy(alpha = 0.35f),
                CircleShape
            )
            .then(
                if (selected) Modifier.shadow(
                    4.dp,
                    CircleShape,
                    ambientColor = AeroColors.blue.copy(alpha = 0.35f)
                ) else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else AeroColors.subText
        )
    }
}

// ============================================================
// fa-row · 列表行
// ============================================================
@Composable
fun AeroRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    tag: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 44px 圆角图标
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.9f), Color(0xFFBEE1F5).copy(alpha = 0.7f))
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(15.dp))
                .aeroTopHighlight(15.dp, 0.6f),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) icon() else Text(text = "💧", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AeroColors.ink)
            Text(text = subtitle, fontSize = 12.sp, color = AeroColors.mutedText)
        }
        if (tag != null) {
            AeroBadge(text = tag, style = AeroBadgeStyle.Green)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = "›", fontSize = 18.sp, fontWeight = FontWeight.Black, color = AeroColors.blueDeep)
    }
}

// ============================================================
// fa-banner · 信息横幅
// ============================================================
enum class AeroBannerStyle { Info, Ok }

@Composable
fun AeroBanner(
    title: String,
    message: String,
    style: AeroBannerStyle = AeroBannerStyle.Info,
    modifier: Modifier = Modifier,
    icon: String = "💧"
) {
    val bg = when (style) {
        AeroBannerStyle.Info -> AeroColors.blue.copy(alpha = 0.12f)
        AeroBannerStyle.Ok -> AeroColors.green.copy(alpha = 0.16f)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .aeroTopHighlight(20.dp, 0.6f)
            .padding(14.dp, 16.dp)
    ) {
        Text(text = icon, fontSize = 22.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = AeroColors.ink)
            Text(
                text = message,
                fontSize = 13.5.sp,
                color = AeroColors.subText,
                lineHeight = 21.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

// ============================================================
// fa-modal · 弹窗
// ============================================================
@Composable
fun AeroModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    icon: String = "💧",
    title: String,
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible) return
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.92f,
        animationSpec = tween(280, easing = FastOutSlowInEasing),
        label = "aeroModalScale"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .scale(scale)
                .shadow(20.dp, RoundedCornerShape(28.dp), ambientColor = Color(0xFF0A2540).copy(alpha = 0.3f))
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White.copy(alpha = 0.92f))
                .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(28.dp))
                .aeroTopHighlight(28.dp, 0.6f)
                .padding(26.dp, 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 46.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AeroColors.ink)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                fontSize = 13.5.sp,
                color = Color(0xFF4A6B8A),
                lineHeight = 23.sp
            )
            Spacer(modifier = Modifier.height(18.dp))
            AeroButton(text = buttonText, onClick = { onButtonClick(); onDismiss() }, block = true)
        }
    }
}

// ============================================================
// fa-toast · 深色胶囊 Toast
// ============================================================
@Composable
fun AeroToast(message: String, visible: Boolean, modifier: Modifier = Modifier) {
    if (!visible || message.isBlank()) return
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFF0A2540).copy(alpha = 0.88f))
            .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
            .shadow(8.dp, CircleShape, ambientColor = Color(0xFF0A2540).copy(alpha = 0.35f))
            .padding(horizontal = 22.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ============================================================
// fa-bub · 底栏选中气泡（浮动动画）
// ============================================================
@Composable
fun AeroBubble(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "aeroBub")
    val floatY by transition.animateFloat(
        initialValue = 0f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aeroBubFloat"
    )
    Text(
        text = "🫧",
        fontSize = 16.sp,
        modifier = modifier.offset(y = floatY.dp)
    )
}
