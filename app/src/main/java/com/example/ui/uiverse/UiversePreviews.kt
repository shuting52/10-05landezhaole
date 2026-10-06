package com.example.ui.uiverse

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
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UiverseItemPreview(
    item: UiverseItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F101A)),
        contentAlignment = Alignment.Center
    ) {
        when {
            item.id.contains("style_1") -> TiltCardPreview()
            item.id.contains("style_2") -> GlassDualRingLoaderPreview()
            item.id.contains("style_3") -> ThickButtonsPreview()
            item.id.contains("style_4") -> BottomBarAppBarPreview()
            item.id.contains("style_5") -> CapsuleSetrowPreview()
            item.category == UiverseCategory.BUTTONS -> ButtonPreview(item.id)
            item.category == UiverseCategory.CARDS -> CardPreview(item.id)
            item.category == UiverseCategory.LOADERS -> LoaderPreview(item.id)
            item.category == UiverseCategory.INPUTS -> InputPreview(item.id)
            item.category == UiverseCategory.TOGGLE_SWITCHES -> SwitchPreview(item.id)
            item.category == UiverseCategory.CHECKBOXES -> CheckboxPreview(item.id)
            item.category == UiverseCategory.RADIO_BUTTONS -> RadioPreview(item.id)
            item.category == UiverseCategory.FORMS -> FormPreview(item.id)
            item.category == UiverseCategory.PATTERNS -> PatternPreview(item.id)
            item.category == UiverseCategory.TOOLTIPS -> TooltipPreview(item.id)
            item.category == UiverseCategory.UI_KITS -> UiKitPreview(item.associatedKit ?: UiKitPreset.CYBERPUNK_NEON)
            else -> GenericPreview(item.name)
        }
    }
}

@Composable
fun ButtonPreview(id: String) {
    when {
        id.contains("cyber") -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0D0E1A))
                    .border(1.5.dp, Color(0xFF00F0FF), RoundedCornerShape(6.dp))
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "CYBER_ACT",
                    color = Color(0xFF00F0FF),
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }
        id.contains("glass") -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0x44FFFFFF), Color(0x11FFFFFF))
                        )
                    )
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(Color(0x88FFFFFF), Color(0x22FFFFFF))),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Explore Glass",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
        id.contains("brutal") -> {
            Box(
                modifier = Modifier
                    .background(Color(0xFF000000), RoundedCornerShape(4.dp))
                    .padding(bottom = 3.dp, end = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFFFE600))
                        .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "POP BUTTON",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
        }
        id.contains("retro") -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFFFCC00))
                    .border(2.dp, Color(0xFF000000), RoundedCornerShape(2.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "INSERT COIN",
                    color = Color.Black,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
        else -> {
            // Neumorphic / Default
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE2E8F0))
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Soft Clay",
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun CardPreview(id: String) {
    when {
        id.contains("cyber") -> {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(65.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0D0E18))
                    .border(1.5.dp, Color(0xFF00F0FF), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "HUD TERMINAL", color = Color(0xFF00F0FF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = ">> Status: ONLINE", color = Color(0xFF94A3B8), fontSize = 9.sp)
                }
            }
        }
        id.contains("glass") -> {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(65.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(Color(0x33FFFFFF), Color(0x11FFFFFF))))
                    .border(1.dp, Color(0x55FFFFFF), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "Aurora Glass", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Translucent frosted", color = Color(0xCCFFFFFF), fontSize = 9.sp)
                }
            }
        }
        id.contains("brutal") -> {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(65.dp)
                    .background(Color.Black, RoundedCornerShape(6.dp))
                    .padding(bottom = 3.dp, end = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(text = "BRUTAL CARD", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        Text(text = "Solid 4px Drop Shadow", color = Color.DarkGray, fontSize = 9.sp)
                    }
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(65.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE2E8F0))
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "Neumorphic Card", color = Color(0xFF1E293B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Dual soft shadows", color = Color(0xFF64748B), fontSize = 9.sp)
                }
            }
        }
    }
}

@Composable
fun LoaderPreview(id: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "loader_preview")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Restart),
        label = "angle"
    )

    when {
        id.contains("glitch") || id.contains("cyber") -> {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .rotate(angle)
                    .border(2.dp, Color(0xFF00F0FF), CircleShape),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF003C))
                )
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .rotate(angle)
                    .border(
                        3.dp,
                        Brush.sweepGradient(listOf(Color(0xFF6366F1), Color(0xFFEC4899), Color.Transparent)),
                        CircleShape
                    )
            )
        }
    }
}

@Composable
fun InputPreview(id: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (id.contains("glass")) Color(0x33FFFFFF) else Color(0xFF0A0E18))
            .border(
                1.5.dp,
                if (id.contains("cyber")) Color(0xFF00F0FF) else if (id.contains("glass")) Color(0x66FFFFFF) else Color(0xFFFFE600),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = if (id.contains("cyber")) Color(0xFF00F0FF) else Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (id.contains("cyber")) ">> INPUT..." else "Search...",
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun SwitchPreview(id: String) {
    var checked by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .width(52.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (checked) Color(0xFF6366F1) else Color(0xFF334155))
            .clickable { checked = !checked }
            .padding(3.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Composable
fun CheckboxPreview(id: String) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF0F172A))
            .border(1.5.dp, Color(0xFF00F0FF), RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = Color(0xFF00F0FF),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun RadioPreview(id: String) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .border(2.dp, Color(0xFF6366F1), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(Color(0xFF6366F1))
        )
    }
}

@Composable
fun FormPreview(id: String) {
    Column(
        modifier = Modifier
            .width(170.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x33FFFFFF))
            .border(1.dp, Color(0x44FFFFFF), RoundedCornerShape(8.dp))
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .background(Color(0x22FFFFFF), RoundedCornerShape(3.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(12.dp)
                .background(Color(0xFF6366F1), RoundedCornerShape(3.dp))
        )
    }
}

@Composable
fun PatternPreview(id: String) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 16.dp.toPx()
        for (x in 0..(size.width / step).toInt()) {
            for (y in 0..(size.height / step).toInt()) {
                drawCircle(
                    color = Color(0xFF00F0FF).copy(alpha = 0.3f),
                    radius = 1.5.dp.toPx(),
                    center = Offset(x * step, y * step)
                )
            }
        }
    }
}

@Composable
fun TooltipPreview(id: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, Color(0xFF00F0FF), RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = "Tooltip active", color = Color(0xFF00F0FF), fontSize = 10.sp)
    }
}

@Composable
fun TiltCardPreview(modifier: Modifier = Modifier) {
    var rotX by remember { mutableFloatStateOf(0f) }
    var rotY by remember { mutableFloatStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "glowSpin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    Box(
        modifier = modifier
            .width(220.dp)
            .height(95.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        rotY = (rotY + dragAmount.x * 0.4f).coerceIn(-24f, 24f)
                        rotX = (rotX - dragAmount.y * 0.4f).coerceIn(-24f, 24f)
                    },
                    onDragEnd = {
                        rotX = 0f
                        rotY = 0f
                    },
                    onDragCancel = {
                        rotX = 0f
                        rotY = 0f
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // tc-glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(angle)
                .background(
                    Brush.sweepGradient(
                        listOf(
                            Color(0xFF6C63FF),
                            Color(0xFFFF2D78),
                            Color(0xFF00E5FF),
                            Color(0xFF6C63FF)
                        )
                    ),
                    RoundedCornerShape(18.dp)
                )
                .blur(10.dp)
        )
        // tc-face
        Box(
            modifier = Modifier
                .fillMaxSize(0.92f)
                .graphicsLayer {
                    rotationX = rotX
                    rotationY = rotY
                    cameraDistance = 12f * density
                }
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0x33FFFFFF), Color(0x11FFFFFF))
                    )
                )
                .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(16.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✦", color = Color(0xFF00E5FF), fontSize = 14.sp, fontWeight = FontWeight.Black)
                Text("磁吸跟随卡", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("触摸滑动 · 3D跟随旋转", color = Color(0xDDFFFFFF), fontSize = 9.sp)
            }
        }
    }
}

@Composable
fun GlassDualRingLoaderPreview(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "ringSpin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )
    val coreScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "coreScale"
    )

    Row(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Dual-color Glass Ring
        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(3.dp, Color(0x22FFFFFF), CircleShape)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(spinAngle)
                    .border(
                        3.dp,
                        Brush.sweepGradient(listOf(Color(0xFF8B84FF), Color(0xFFFF2D78), Color.Transparent)),
                        CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(15.dp)
                    .scale(coreScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color.White.copy(alpha = 0.9f), Color(0xFF6C63FF).copy(alpha = 0.5f))
                        )
                    )
            )
        }

        // 5 Bouncing Pills
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            for (i in 0..4) {
                val bounceOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = -7f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, delayMillis = i * 110, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "bounce_$i"
                )
                Box(
                    modifier = Modifier
                        .offset(y = bounceOffset.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFFFFFFFF), Color(0xFF6C63FF))
                            )
                        )
                )
            }
        }

        // Mini Glass Button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(9.dp))
                .background(
                    Brush.linearGradient(listOf(Color(0xFF6C63FF), Color(0xFFFF2D78)))
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text("提交", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ThickButtonsPreview(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // u-btn (立即下载)
        Box(
            modifier = Modifier
                .background(Color(0xFF0A3D63), RoundedCornerShape(10.dp))
                .padding(bottom = 3.dp, end = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFF6B4A))
                    .border(2.5.dp, Color(0xFF0A3D63), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("立即下载", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }

        // u-btn alt (开始使用)
        Box(
            modifier = Modifier
                .background(Color(0xFF0A3D63), RoundedCornerShape(10.dp))
                .padding(bottom = 3.dp, end = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF48DBFB))
                    .border(2.5.dp, Color(0xFF0A3D63), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("开始使用", color = Color(0xFF0A3D63), fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }

        // u-btn ghost (了解更多)
        Box(
            modifier = Modifier
                .background(Color(0xFFFF6B4A), RoundedCornerShape(10.dp))
                .padding(bottom = 3.dp, end = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(2.5.dp, Color(0xFF0A3D63), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("了解更多", color = Color(0xFF0A3D63), fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun BottomBarAppBarPreview(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(0.92f),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // u-appbar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF0A3D63))
                .border(2.dp, Color(0xFF00D2D3), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⌂", color = Color(0xFF00D2D3), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(6.dp))
                Text("极客立体顶栏", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
            }
        }

        // u-bottombar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A3D63), RoundedCornerShape(11.dp))
                .padding(bottom = 2.5.dp, end = 2.5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(11.dp))
                    .background(Color.White)
                    .border(2.dp, Color(0xFF0A3D63), RoundedCornerShape(11.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("⌂ 首页" to true, "▦ 组件" to false, "♥ 收藏" to false, "☰ 我的" to false).forEach { (title, on) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (on) Color(0xFF0A3D63) else Color.Transparent)
                            .padding(vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (on) Color.White else Color(0xFF7A8CA0),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CapsuleSetrowPreview(modifier: Modifier = Modifier) {
    var isDarkOn by remember { mutableStateOf(true) }
    Box(
        modifier = modifier
            .fillMaxWidth(0.92f)
            .background(Color(0xFFFF9F43), RoundedCornerShape(12.dp))
            .padding(bottom = 3.dp, end = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(2.5.dp, Color(0xFF0A3D63), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("深色模式", fontWeight = FontWeight.Black, color = Color(0xFF26303C), fontSize = 12.sp)
            // u-switch
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (isDarkOn) Color(0xFF0A3D63) else Color(0xFFCBD5E1))
                    .border(1.5.dp, Color(0xFF0A3D63), RoundedCornerShape(11.dp))
                    .clickable { isDarkOn = !isDarkOn }
                    .padding(2.dp),
                contentAlignment = if (isDarkOn) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(if (isDarkOn) Color(0xFFFF9F43) else Color.White)
                )
            }
        }
    }
}

@Composable
fun UiKitPreview(kit: UiKitPreset) {
    when (kit) {
        UiKitPreset.STYLE_1_TILT_MAGNETIC -> TiltCardPreview()
        UiKitPreset.STYLE_2_GLASS_LOADER -> GlassDualRingLoaderPreview()
        UiKitPreset.STYLE_3_THICK_BUTTON -> ThickButtonsPreview()
        UiKitPreset.STYLE_4_BOTTOMBAR_APPBAR -> BottomBarAppBarPreview()
        UiKitPreset.STYLE_5_CAPSULE_SETROW -> CapsuleSetrowPreview()
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(kit.backgroundColor)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(kit.surfaceColor)
                        .border(1.5.dp, kit.primaryColor, RoundedCornerShape(10.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(kit.primaryColor)
                    )
                    Text(
                        text = kit.displayName.substringBefore(" "),
                        color = kit.textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GenericPreview(name: String) {
    Text(text = name, color = Color.White, fontSize = 12.sp)
}
