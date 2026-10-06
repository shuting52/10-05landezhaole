package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 软件统一弹窗壳体（液态玻璃背景 + 七彩流光跑动边框）
 * 满足全站弹窗（联系作者 / 官方网站 / 软件反馈 / 打分 / 分享 / 检查更新 / 主题切换 / 关于 / 协议 / 隐私）的统一高级视觉体验
 */
@Composable
fun LiquidGlassDialogShell(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    centerTitle: Boolean = true,
    showCloseButton: Boolean = true,
    cornerRadius: Dp = 26.dp,
    headerIcon: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val shape = RoundedCornerShape(cornerRadius)

        val infiniteTransition = rememberInfiniteTransition(label = "glass_bubble_anim")
        val floatPhase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 6.283185f,
            animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
            label = "float_bubble"
        )
        val bobbingY = (kotlin.math.sin(floatPhase) * 5f)
        val sparkleAlpha = (0.55f + 0.40f * kotlin.math.sin(floatPhase * 2f))

        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .clip(shape)
                .shadow(24.dp, shape, ambientColor = Color(0xFFA855F7).copy(alpha = 0.25f), spotColor = Color(0xFF38BDF8).copy(alpha = 0.35f))
                .streamingBorder(
                    cornerRadius = cornerRadius,
                    strokeWidth = 1.8.dp,
                    glowWidth = 3.8.dp,
                    baseBorderColor = Color.White.copy(alpha = 0.65f),
                    rainbow = true,
                    showGlow = true
                ),
            shape = shape,
            color = Color.White.copy(alpha = 0.92f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.95f),
                                Color(0xFFFBF8FF).copy(alpha = 0.90f),
                                Color(0xFFFFF9FC).copy(alpha = 0.92f),
                                Color.White.copy(alpha = 0.96f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(1000f, 1600f)
                        )
                    )
            ) {
                // 液态玻璃折射漫反射光斑
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height
                    drawCircle(
                        color = Color(0xFFFFB6C1).copy(alpha = 0.25f * sparkleAlpha),
                        radius = 20.dp.toPx(),
                        center = Offset(w * 0.88f, 32.dp.toPx() + bobbingY)
                    )
                    drawCircle(
                        color = Color(0xFF38BDF8).copy(alpha = 0.22f * (1.2f - sparkleAlpha)),
                        radius = 14.dp.toPx(),
                        center = Offset(28.dp.toPx(), 55.dp.toPx() - bobbingY)
                    )
                    drawCircle(
                        color = Color(0xFFA78BFA).copy(alpha = 0.20f * sparkleAlpha),
                        radius = 16.dp.toPx(),
                        center = Offset(w * 0.80f, h - 45.dp.toPx() + bobbingY)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    // 顶部标题栏
                    if (title != null || headerIcon != null || showCloseButton) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            if (centerTitle) {
                                // 标题完全居中布局
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (headerIcon != null) {
                                        headerIcon()
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    if (!title.isNullOrBlank()) {
                                        Text(
                                            text = title,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    if (!subtitle.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = subtitle,
                                            fontSize = 11.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                if (showCloseButton) {
                                    IconButton(
                                        onClick = onDismissRequest,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = "关闭",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            } else {
                                // 靠左传统布局
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        if (headerIcon != null) {
                                            headerIcon()
                                            Spacer(modifier = Modifier.width(10.dp))
                                        }
                                        Column {
                                            if (!title.isNullOrBlank()) {
                                                Text(
                                                    text = title,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            if (!subtitle.isNullOrBlank()) {
                                                Text(
                                                    text = subtitle,
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                    if (showCloseButton) {
                                        IconButton(
                                            onClick = onDismissRequest,
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Close,
                                                contentDescription = "关闭",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 主体内容
                    content()
                }
            }
        }
    }
}
