package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val StarFillColor = Color(0xFFFFC73A)
val StarStrokeColor = Color(0xFF666666)

/**
 * Interactive App Rating Dialog implementing the Uiverse.io animated star rating:
 * - 5-star rating with #ffc73a fill and #666 stroke
 * - Idle dash animation (@keyframes idle)
 * - "yippee" scale pop animation upon selection (@keyframes yippee)
 * - Tag feedback chips and custom message input
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppRatingDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var rating by remember { mutableIntStateOf(5) }
    var feedbackText by remember { mutableStateOf("") }
    val selectedTags = remember { mutableStateListOf("资源全面实用", "极速纯净无广", "界面设计精美") }

    val presetTags = listOf(
        "资源全面实用",
        "极速纯净无广",
        "最强嘴替好玩",
        "界面设计精美",
        "分类清晰直达",
        "随心抽很有趣",
        "良心开发者"
    )

    LiquidGlassDialogShell(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = "给软件打分",
        subtitle = "您的每一个好评，都是支撑独立作者持续更新的源动力",
        centerTitle = true,
        headerIcon = {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFF59E0B), Color(0xFFFFD166))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.RateReview,
                    contentDescription = null,
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Uiverse.io Animated 5-Star Rating Row
            UiverseRatingBar(
                rating = rating,
                onRatingChanged = { rating = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Dynamic Rating Assessment
            val scoreText = when (rating) {
                5 -> "⭐⭐⭐⭐⭐ 五星满分 · 非常满意！感谢您的厚爱！"
                4 -> "⭐⭐⭐⭐ 四星满意 · 体验良好，我们会继续精进！"
                3 -> "⭐⭐⭐ 三星及格 · 感谢支持，我们会继续完善功能！"
                2 -> "⭐⭐ 两星鞭策 · 抱歉体验未达预期，我们会持续整改！"
                else -> "⭐ 一星建议 · 虚心听取指教，期待您的批评建议！"
            }

            Text(
                text = scoreText,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (rating >= 4) Color(0xFFD97706) else MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Tags
            Text(
                text = "快捷好评标签",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                presetTags.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                        },
                        label = { Text(text = tag, fontSize = 11.5.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StarFillColor.copy(alpha = 0.25f),
                            selectedLabelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFFD97706)
                                )
                            }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                placeholder = { Text("留下您想对作者说的话或建议（选填）...", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        openAppMarket(context)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Shop, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("市场评分", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "非常感谢您的 ${rating} 星好评！祝您生活愉快！",
                            Toast.LENGTH_LONG
                        ).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StarFillColor,
                        contentColor = Color(0xFF181818)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text("提交好评 ✦", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

/**
 * Animated 5-star bar implementing the Uiverse.io CSS keyframes (idle and yippee pop)
 */
@Composable
private fun UiverseRatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "star_idle")
    val dashPhase by infiniteTransition.animateFloat(
        initialValue = 24f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dashPhase"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val isFilled = i <= rating
            val scope = rememberCoroutineScope()
            val scaleAnim = remember { Animatable(1f) }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .scale(scaleAnim.value)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onRatingChanged(i)
                        // Trigger yippee pop animation: 1f -> 0.3f -> 1.25f -> 1f
                        scope.launch {
                            scaleAnim.animateTo(0.35f, tween(120, easing = FastOutSlowInEasing))
                            scaleAnim.animateTo(1.25f, tween(200, easing = FastOutSlowInEasing))
                            scaleAnim.animateTo(1.0f, tween(150, easing = FastOutSlowInEasing))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(34.dp)) {
                    drawUiverseStar(
                        isFilled = isFilled,
                        fillColor = StarFillColor,
                        strokeColor = if (isFilled) StarFillColor else StarStrokeColor,
                        dashPhase = if (isFilled) 0f else dashPhase
                    )
                }
            }
        }
    }
}

/**
 * Draws a 5-point star path with either full gold fill or animated dash stroke
 */
private fun DrawScope.drawUiverseStar(
    isFilled: Boolean,
    fillColor: Color,
    strokeColor: Color,
    dashPhase: Float
) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cy = h / 2f
    val outerR = w / 2f
    val innerR = outerR * 0.42f

    val path = Path()
    var angle = -PI / 2.0
    val step = PI / 5.0

    for (i in 0 until 10) {
        val r = if (i % 2 == 0) outerR else innerR
        val x = (cx + r * cos(angle)).toFloat()
        val y = (cy + r * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        angle += step
    }
    path.close()

    if (isFilled) {
        // Filled state
        drawPath(path = path, color = fillColor)
        drawPath(
            path = path,
            color = fillColor,
            style = Stroke(width = 2.dp.toPx())
        )
    } else {
        // Outline state with stroke-dasharray: 12 and idle animation
        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(
                width = 2.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), dashPhase)
            )
        )
    }
}

private fun openAppMarket(context: Context) {
    try {
        val uri = Uri.parse("market://details?id=${context.packageName}")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            "感谢您的支持！请在应用商店搜索「懒得找了」进行评分～",
            Toast.LENGTH_SHORT
        ).show()
    }
}
