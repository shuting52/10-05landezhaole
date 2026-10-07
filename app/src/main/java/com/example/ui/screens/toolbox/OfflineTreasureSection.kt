package com.example.ui.screens.toolbox

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FlameRed
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SunsetOrange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun OfflineTreasureSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var selectedTool by remember { mutableStateOf("LED_BANNER") } // LED_BANNER, WOODEN_FISH, DECIDER, SOS_LIGHT

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Hero Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.58f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.75f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(SunsetOrange, FlameRed))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "离线百宝 · 实用黑科技小组件",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "手持LED滚动弹幕 · 电子功德木鱼 · 随机做决定 · SOS白光爆闪",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 2. 小工具标签栏
        val tools = listOf(
            "LED_BANNER" to "📺 手持LED弹幕",
            "WOODEN_FISH" to "🪵 电子功德木鱼",
            "DECIDER" to "🎲 随机做决定器",
            "SOS_LIGHT" to "🚨 SOS屏幕爆闪"
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tools) { (id, label) ->
                val isSelected = selectedTool == id
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedTool = id },
                    label = {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FlameRed,
                        selectedLabelColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // 3. 子工具具体呈现
        when (selectedTool) {
            "LED_BANNER" -> LedBannerTool()
            "WOODEN_FISH" -> WoodenFishTool()
            "DECIDER" -> DeciderTool()
            "SOS_LIGHT" -> SosLightTool()
        }
    }
}

/**
 * 1. 手持 LED 滚动弹幕
 */
@Composable
private fun LedBannerTool() {
    val context = LocalContext.current
    var bannerText by remember { mutableStateOf("") }
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var isFullscreen by remember { mutableStateOf(false) }
    var scrollSpeed by remember { mutableIntStateOf(2) } // 1: 慢, 2: 中, 3: 快

    val neonColors = listOf(
        Color(0xFFFFEB3B) to "荧光黄",
        Color(0xFFFF5722) to "火焰橙",
        Color(0xFF00E5FF) to "电光青",
        Color(0xFFFF4081) to "霓虹粉",
        Color(0xFF00E676) to "激光绿",
        Color.White to "纯净白"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "手持滚动跑马灯配置",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = bannerText,
                onValueChange = { bannerText = it },
                label = { Text("输入弹幕大字") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "霓虹文字色彩：", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                neonColors.forEachIndexed { idx, (color, name) ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColorIndex == idx) 2.dp else 1.dp,
                                color = if (selectedColorIndex == idx) Color.Black else Color.LightGray,
                                shape = CircleShape
                            )
                            .clickable { selectedColorIndex = idx }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { isFullscreen = true },
                colors = ButtonDefaults.buttonColors(containerColor = FlameRed),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Fullscreen, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("开启全屏滚动横幅 (点击退出)")
            }
        }
    }

    if (isFullscreen) {
        Dialog(
            onDismissRequest = { isFullscreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "banner_scroll")
            val scrollX by infiniteTransition.animateFloat(
                initialValue = 800f,
                targetValue = -1200f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = when (scrollSpeed) {
                            1 -> 8000
                            2 -> 5000
                            else -> 3000
                        },
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "scrollX"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { isFullscreen = false },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bannerText.ifBlank { "懒得找了" },
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    color = neonColors[selectedColorIndex].first,
                    modifier = Modifier.offset { IntOffset(scrollX.roundToInt(), 0) }
                )

                Text(
                    text = "轻触屏幕任意位置关闭",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                )
            }
        }
    }
}

/**
 * 2. 电子功德木鱼
 */
@Composable
private fun WoodenFishTool() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var count by remember { mutableLongStateOf(0L) }
    var scaleAnim by remember { mutableStateOf(1f) }
    var autoPlay by remember { mutableStateOf(false) }

    // 漂浮 +1 记录
    var plusOneKey by remember { mutableIntStateOf(0) }

    val hitFish = {
        count++
        plusOneKey++
        triggerSimpleVibrate(context, 35)
        coroutineScope.launch {
            scaleAnim = 0.88f
            delay(80)
            scaleAnim = 1.05f
            delay(80)
            scaleAnim = 1f
        }
    }

    // 自动敲击协程
    LaunchedEffect(autoPlay) {
        while (autoPlay) {
            delay(750)
            hitFish()
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "今日累计功德：$count",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = SunsetOrange
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("自动积德", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = autoPlay,
                        onCheckedChange = { autoPlay = it },
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // 木鱼主体图标
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(scaleAnim)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF8D6E63), Color(0xFF4E342E))
                            )
                        )
                        .clickable { hitFish() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SelfImprovement,
                        contentDescription = "木鱼",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(68.dp)
                    )
                }

                // 功德 +1 悬浮升空
                androidx.compose.animation.AnimatedVisibility(
                    visible = plusOneKey > 0,
                    enter = fadeIn(tween(100)),
                    exit = fadeOut(tween(400)),
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    Text(
                        text = "功德 +1 · 烦恼-1",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB300),
                        modifier = Modifier.offset(y = (-10).dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "点击木鱼即可敲击積德 · 烦恼归零",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { count = 0 },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("清空计数", fontSize = 11.sp)
            }
        }
    }
}

/**
 * 3. 随机做决定器 (选择困难症终结者)
 */
@Composable
private fun DeciderTool() {
    val context = LocalContext.current
    var currentDecisionType by remember { mutableStateOf("FOOD") } // FOOD, COIN, DICE
    var decisionResult by remember { mutableStateOf("点击下方立即摇出决定！") }
    var isRolling by remember { mutableStateOf(false) }

    val foodOptions = listOf("火锅 🍲", "沙拉轻食 🥗", "螺蛳粉 🍜", "烧烤撸串 🍢", "自己下厨 🍳", "汉堡披萨 🍔", "川菜小炒 🥘", "减脂断食 💧", "日料寿司 🍣")

    val rollDecision = {
        isRolling = true
        when (currentDecisionType) {
            "FOOD" -> {
                decisionResult = "今天就吃：${foodOptions.random()}！"
            }
            "COIN" -> {
                val isHeads = (0..1).random() == 0
                decisionResult = if (isHeads) "🪙 硬币正面（支持/去做）" else "🪙 硬币反面（拒绝/放弃）"
            }
            "DICE" -> {
                val points = (1..6).random()
                decisionResult = "🎲 掷出点数：$points 点"
            }
        }
        triggerSimpleVibrate(context, 40)
        isRolling = false
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("FOOD" to "今天吃什么", "COIN" to "抛硬币", "DICE" to "掷骰子").forEach { (type, label) ->
                    val isSelected = currentDecisionType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            currentDecisionType = type
                            decisionResult = "点击下方立即摇出决定！"
                        },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FlameRed,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, SunsetOrange.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = decisionResult,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { rollDecision() },
                colors = ButtonDefaults.buttonColors(containerColor = SunsetOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Casino, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("立即随机抽取", fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * 4. SOS 屏幕爆闪与手电筒补光灯
 */
@Composable
private fun SosLightTool() {
    var isSosRunning by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.FlashOn, contentDescription = null, tint = FlameRed, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "紧急求救 SOS 屏幕高频爆闪",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "开启后全屏白光将按照国际标准摩尔斯电码 (... --- ...) 极高亮度循环爆闪，夜间骑行防追尾或户外迷路求援极具穿透力。",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { isSosRunning = true },
                colors = ButtonDefaults.buttonColors(containerColor = FlameRed),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("启动全屏 SOS 闪烁模式", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (isSosRunning) {
        Dialog(
            onDismissRequest = { isSosRunning = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            var flashOn by remember { mutableStateOf(true) }

            // 按照摩斯电码持续爆闪
            LaunchedEffect(isSosRunning) {
                val morseSequence = listOf(150L, 150L, 150L, 150L, 150L, 400L, 400L, 150L, 400L, 150L, 400L, 400L, 150L, 150L, 150L, 150L, 150L, 1000L)
                while (isSosRunning) {
                    for (delayTime in morseSequence) {
                        flashOn = !flashOn
                        delay(delayTime)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (flashOn) Color.White else Color.Black)
                    .clickable { isSosRunning = false },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SOS 模式运行中 · 点击任意位置退出",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = if (flashOn) Color.Black else Color.White
                )
            }
        }
    }
}

private fun triggerSimpleVibrate(context: Context, durationMs: Long) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vm?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            v?.vibrate(durationMs)
        }
    } catch (_: Exception) {}
}
