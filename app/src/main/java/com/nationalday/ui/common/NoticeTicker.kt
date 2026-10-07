package com.nationalday.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 国庆潮流国潮风 · 跑马灯通知胶囊
 */
@Composable
fun NationalDayNoticeTicker(
    notice: String = "欢迎使用懒得找了～海量白嫖资源等你探索，遇到问题请到官方群反馈！",
    modifier: Modifier = Modifier
) {
    val pillShape = RoundedCornerShape(50)
    val transition = rememberInfiniteTransition(label = "ticker_glow")
    val pulseScale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    val redGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFDE2910),
            Color(0xFF991B1B),
            Color(0xFF7F1D1D)
        )
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(pillShape)
            .background(redGradient)
            .border(1.2.dp, Color(0xFFFFD700), pillShape)
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(Color(0xFFFFD700).copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Text("🏮", fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = notice,
            color = Color(0xFFFFFAF0),
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFFFD700))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "公告",
                color = Color(0xFF7F1D1D),
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
