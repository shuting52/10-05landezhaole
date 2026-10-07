package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// v1.1.4 设置页分支：Uiverse 新拟态/霓虹风格设置行组件
// 依据用户提供的 UiverseSettingsRow 规范实现：
//   白色卡片 + 琥珀橙硬阴影 + 深蓝墨描边 + 圆角 + 图标/标题/尾部插槽
// ============================================================

/** Uiverse 墨色（深蓝灰描边/文字） */
val UiverseInk = Color(0xFF1E2A38)

/** Uiverse 琥珀橙（主阴影/强调色） */
val UiverseAmber = Color(0xFFFFB020)

/** Uiverse 天蓝（图标底色） */
val UiverseBlue = Color(0xFF4D96FF)

/** Uiverse 草莓粉（次强调色） */
val UiversePink = Color(0xFFFF5E8A)

/** Uiverse 背景（浅蓝灰） */
val UiverseBg = Color(0xFFF0F4FA)

/** Uiverse 文字弱化色（底部未选中/辅助文字）v1.1.12 */
val UiverseTextMuted = Color(0xFF6B7A8F)

/** Uiverse 分隔线色（列表分隔）v1.1.12 */
val UiverseTrack = Color(0xFFE2E8F0)

/**
 * v1.1.12：UiverseDotsLoader 开屏动画（用户指定样式）——
 * 草莓粉/琥珀/天蓝/草莓粉四色圆点依次弹跳，2.5dp 墨色描边。
 */
@Composable
fun UiverseDotsLoader(modifier: Modifier = Modifier) {
    val colors = listOf(UiversePink, UiverseAmber, UiverseBlue, UiversePink)
    val infiniteTransition = rememberInfiniteTransition(label = "uiverse_dots")

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        colors.forEachIndexed { index, color ->
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -16f,
                animationSpec = infiniteRepeatable(
                    animation = tween(450, delayMillis = index * 120, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dotOffsetY_$index"
            )

            Box(
                modifier = Modifier
                    .offset(y = offsetY.dp)
                    .size(20.dp)
                    .background(color, CircleShape)
                    .border(2.5.dp, UiverseInk, CircleShape)
            )
        }
    }
}

/**
 * neoShadow：白色卡片 + 右下偏移硬阴影 + 粗描边（Neo-Brutalism / Uiverse 风格）。
 * 对应 CSS：`box-shadow:.35em .35em 0 var(--amber); border:3.5px solid var(--ink)`
 */
fun Modifier.neoShadow(
    offsetX: Dp = 3.5.dp,
    offsetY: Dp = 3.5.dp,
    shadowColor: Color = UiverseAmber,
    borderColor: Color = UiverseInk,
    borderWidth: Dp = 3.5.dp,
    shape: RoundedCornerShape = RoundedCornerShape(14.dp),
    backgroundColor: Color = Color.White
): Modifier = this
    .shadow(
        elevation = 6.dp,
        shape = shape,
        ambientColor = shadowColor.copy(alpha = 0.85f),
        spotColor = shadowColor
    )
    .background(backgroundColor, shape)
    .border(borderWidth, borderColor, shape)

/**
 * 设置页行：图标 + 标题 + 尾部插槽（箭头/开关/徽标）。
 * 采用用户指定的 Uiverse 风格：白底 + 3.5dp 深蓝墨描边 + 3.5dp 琥珀硬阴影 + 圆角。
 */
@Composable
fun UiverseSettingsRow(
    icon: String,
    title: String,
    subtitle: String = "",
    trailing: @Composable () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .neoShadow(
                offsetX = 3.5.dp,
                offsetY = 3.5.dp,
                shadowColor = UiverseAmber,
                borderColor = UiverseInk,
                borderWidth = 3.5.dp,
                shape = RoundedCornerShape(14.dp),
                backgroundColor = Color.White
            )
            // v1.0.16 修复：此前漏掉 clickable，设置行完全无法点击！
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, color = UiverseInk, fontSize = 14.sp)
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = UiverseInk.copy(alpha = 0.55f)
                )
            }
        }
        trailing()
    }
}

/** 设置行尾部箭头（›） */
@Composable
fun UiverseChevron() {
    Text("›", fontSize = 16.sp, fontWeight = FontWeight.Black, color = UiverseInk.copy(alpha = 0.5f))
}
