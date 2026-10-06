package com.nationalday.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 国庆主题 · 软件开屏启动页 (Splash Screen)
 * 精准文件路径: app/src/main/java/com/nationalday/ui/splash/SplashScreen.kt
 */
@Composable
fun NationalDaySplashScreen(
    onFinish: () -> Unit = {}
) {
    var secondsLeft by remember { mutableIntStateOf(3) }

    // 标题光晕微放大呼吸动效
    val transition = rememberInfiniteTransition(label = "splash_glow")
    val scale by transition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(tween(1400, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "scale"
    )

    // 倒计时逻辑
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF7F1D1D), // 熟褐红
                        Color(0xFF991B1B), // 中国红
                        Color(0xFF450A0A)  // 深朱砂
                    )
                )
            )
    ) {
        // 右上角跳过按钮（带倒计时微圆环）
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Black.copy(alpha = 0.45f))
                .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .clickable(onClick = onFinish)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("跳过", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("${secondsLeft}s", color = Color(0xFFFFD700), fontSize = 12.sp, fontWeight = FontWeight.Black)
        }

        // 中央大标题与烫金图腾
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer { scaleX = scale; scaleY = scale },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🇨🇳", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "盛世华章 · 举国同庆",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFFD700),
                letterSpacing = 2.sp
            )
            Text(
                text = "祝全国 Android 开发者节日快乐 · 愿祖国繁荣昌盛",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // 底部品牌与版权标识
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("国庆全套移动应用客户端", color = Color(0xFFFFD700).copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Copyright © 2026 Android Studio Craft. All rights reserved.", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
        }
    }
}