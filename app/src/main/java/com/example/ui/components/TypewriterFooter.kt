package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 软件合规资质与备案号官方页脚（24 小时不间断轮播版）。
 *
 * 2026-10-23：用户要求页脚"24小时不间断轮播"——
 * 顶部备案卡保持静态（官方认证），底部标语区无限循环：
 * 逐字打出 → 停留 → 快速删除 → 下一条，全天候循环。
 * 离开设置页时协程自动取消，不耗电。
 */
@Composable
fun TypewriterFooter(
    modifier: Modifier = Modifier,
    uiText: com.example.data.remote.UiTextDto? = null
) {
    val licenseFull = "备案号：粤ICP备20260921号-1A"
    var entered by remember { mutableStateOf(false) }
    var licenseTyped by remember { mutableStateOf(0) }
    var licenseCursor by remember { mutableStateOf(true) }

    // 24 小时轮播标语
    val carouselLines = remember {
        listOf(
            "✓ 国家移动安全检测合格",
            "✓ 纯净绿色安全软件",
            "懒得找了 (LanDeZhaoLe) · 官方出品",
            "Copyright © 2026 懒得找了团队 · 保留所有权利"
        )
    }
    var carouselIndex by remember { mutableStateOf(0) }
    var carouselTyped by remember { mutableStateOf(0) }
    var carouselCursor by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        entered = true
        // 备案号打字机（播一次）
        delay(400)
        for (i in 1..licenseFull.length) {
            licenseTyped = i
            delay(70)
        }
        repeat(6) {
            delay(380)
            licenseCursor = !licenseCursor
        }
        licenseCursor = false
    }

    // 24 小时不间断轮播（独立协程，无限循环）
    LaunchedEffect(Unit) {
        delay(1200) // 等备案号打完再开始轮播
        while (true) {
            val line = carouselLines[carouselIndex]
            // 逐字打出
            for (i in 1..line.length) {
                carouselTyped = i
                carouselCursor = true
                delay(65)
            }
            // 停留展示
            repeat(7) {
                delay(380)
                carouselCursor = !carouselCursor
            }
            carouselCursor = true
            // 快速删除
            for (i in line.length downTo 0) {
                carouselTyped = i
                delay(22)
            }
            delay(350)
            carouselIndex = (carouselIndex + 1) % carouselLines.size
        }
    }

    fun sectionEnter(delayMs: Int) =
        fadeIn(animationSpec = tween(500, delayMillis = delayMs)) +
                slideInVertically(
                    animationSpec = tween(500, delayMillis = delayMs),
                    initialOffsetY = { it / 4 }
                )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. 顶部精致极简柔光分割线
        AnimatedVisibility(visible = entered, enter = sectionEnter(0)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, Color(0xFFE2E8F0), Color(0xFFCBD5E1))
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3B82F6))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFCBD5E1), Color(0xFFE2E8F0), Color.Transparent)
                            )
                        )
                )
            }
        }

        // 2. 官方移动应用程序备案许可认证卡（静态）
        AnimatedVisibility(visible = entered, enter = sectionEnter(120)) {
            Surface(
                color = Color.White.copy(alpha = 0.82f),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VerifiedUser,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "工业和信息化部移动互联网应用程序备案",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xFF2563EB))) {
                                append(licenseFull.take(licenseTyped))
                            }
                            if (licenseCursor && licenseTyped < licenseFull.length) {
                                withStyle(SpanStyle(color = Color(0xFF2563EB))) {
                                    append("▍")
                                }
                            }
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "公安联网备案：粤公网安备 44010602009210号",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }

        // 3. 24 小时不间断轮播标语区
        AnimatedVisibility(visible = entered, enter = sectionEnter(300)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp),
                contentAlignment = Alignment.Center
            ) {
                val line = carouselLines[carouselIndex]
                val lineColor = when (carouselIndex) {
                    0 -> Color(0xFF059669)
                    1 -> Color(0xFF2563EB)
                    2 -> Color(0xFF475569)
                    else -> Color(0xFF94A3B8)
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = lineColor,
                                fontWeight = if (carouselIndex < 2) FontWeight.Medium else FontWeight.Bold
                            )
                        ) {
                            append(line.take(carouselTyped))
                        }
                        if (carouselCursor) {
                            withStyle(SpanStyle(color = lineColor)) {
                                append("▍")
                            }
                        }
                    },
                    fontSize = if (carouselIndex == 3) 9.sp else 10.5.sp
                )
            }
        }
    }
}
