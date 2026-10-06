package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.remote.WelcomeDto

/**
 * 可爱卡通动态欢迎弹窗（v1.7.2）
 * - 底部滑入 + 淡入动画，顶部卡通表情摇摆
 * - 粉紫渐变可爱卡片，问候语大字 + 欢迎语列表（每行一条）
 * - 比例可调：compact 紧凑 / standard 标准 / wide 宽幅
 */
@Composable
fun CuteWelcomeDialog(
    welcome: WelcomeDto,
    onDismiss: () -> Unit
) {
    // 弹窗比例：控制台可调节
    val ratioFactor = when (welcome.ratio) {
        "compact" -> 0.82f
        "wide" -> 0.96f
        else -> 0.9f
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            // 底部滑入 + 淡入动画
            var appear by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { appear = true }
            val slideY by animateFloatAsState(
                targetValue = if (appear) 0f else 160f,
                animationSpec = tween(durationMillis = 500),
                label = "cuteSlideY"
            )
            val fadeAlpha by animateFloatAsState(
                targetValue = if (appear) 1f else 0f,
                animationSpec = tween(durationMillis = 450),
                label = "cuteFade"
            )

            // 顶部卡通表情摇摆动画
            val bounce by rememberInfiniteTransition(label = "cuteBounce")
                .animateFloat(
                    initialValue = -8f,
                    targetValue = 8f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(650),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "cuteBounce"
                )
            // 星星眨闪
            val twinkle by rememberInfiniteTransition(label = "cuteTwinkle")
                .animateFloat(
                    initialValue = 0.4f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(550),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "cuteTwinkle"
                )

            // 欢迎语列表（每行一条）
            val lines = welcome.content.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
            val title = welcome.title.ifBlank { "欢迎来到懒得找了" }
            val greeting = welcome.welcomeText.ifBlank { "嗨～欢迎回家！" }

            Box(
                modifier = Modifier
                    .fillMaxWidth(ratioFactor)
                    .graphicsLayer {
                        translationY = slideY * density
                        alpha = fadeAlpha
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                // 顶部卡通装饰：摇摆小怪兽 + 闪烁星星
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = -38.dp)
                        .graphicsLayer { rotationZ = bounce },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("✨", fontSize = 20.sp, modifier = Modifier.graphicsLayer { alpha = twinkle })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🥰", fontSize = 42.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("✨", fontSize = 20.sp, modifier = Modifier.graphicsLayer { alpha = twinkle })
                }

                // 可爱粉紫渐变卡片
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(26.dp),
                            ambientColor = Color(0x66FF6EC7),
                            spotColor = Color(0x338D5CFF)
                        )
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFFFFF1FA),
                                    Color(0xFFF3E8FF),
                                    Color(0xFFFFF6E8)
                                )
                            )
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        )
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 标题
                    Text(
                        text = title,
                        fontSize = if (welcome.ratio == "compact") 17.sp else 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF4A2E7A),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 问候语大字（渐变强调）
                    Text(
                        text = greeting,
                        fontSize = if (welcome.ratio == "compact") 14.sp else 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8D5CFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 欢迎配图（可选）
                    if (welcome.imageUrl.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        coil.compose.AsyncImage(
                            model = welcome.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (welcome.ratio == "wide") 190.dp else if (welcome.ratio == "compact") 110.dp else 150.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    // 欢迎语列表（每行一条，带可爱小圆点）
                    if (lines.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            lines.forEach { line ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Brush.linearGradient(listOf(Color(0xFFFF6EC7), Color(0xFF8D5CFF))))
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = line,
                                        fontSize = if (welcome.ratio == "compact") 12.5.sp else 13.5.sp,
                                        lineHeight = 19.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF4B4453),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 开始按钮（渐变可爱大按钮）
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (welcome.ratio == "compact") 40.dp else 46.dp)
                            .clip(RoundedCornerShape(23.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFFF6EC7), Color(0xFF8D5CFF))
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDismiss
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = welcome.buttonText.ifBlank { "开始体验" },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
