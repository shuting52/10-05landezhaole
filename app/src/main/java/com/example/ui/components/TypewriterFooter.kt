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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GppGood
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
 * 软件合规资质与备案号官方页脚（打字机动态版）。
 *
 * 2026-10-23：用户要求截图中的备案页脚"换成动态页脚的形式呈现"——
 * 备案号逐字打出（带闪烁光标），各区段 staggered 淡入上滑。
 * 动画只在首次进入组合时播放一次，滚动回来不重播。
 */
@Composable
fun TypewriterFooter(
    modifier: Modifier = Modifier,
    uiText: com.example.data.remote.UiTextDto? = null
) {
    val licenseFull = "备案号：粤ICP备20260921号-1A"
    var entered by remember { mutableStateOf(false) }
    var typedCount by remember { mutableStateOf(0) }
    var cursorOn by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        entered = true
        delay(350)
        // 打字机：逐字打出备案号
        for (i in 1..licenseFull.length) {
            typedCount = i
            delay(75)
        }
        // 光标闪烁 3 次后熄灭
        repeat(6) {
            delay(380)
            cursorOn = !cursorOn
        }
        cursorOn = false
    }

    // 区段 staggered 入场：淡入 + 轻微上滑
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

        // 2. 官方移动应用程序备案许可认证卡（备案号打字机打出）
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

                    // 打字机备案号 + 闪烁光标
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xFF2563EB))) {
                                append(licenseFull.take(typedCount))
                            }
                            if (cursorOn && typedCount < licenseFull.length) {
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

        // 3. 安全检测与纯净无广告标识
        AnimatedVisibility(visible = entered, enter = sectionEnter(1150)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "国家移动安全检测合格",
                        fontSize = 9.5.sp,
                        color = Color(0xFF059669),
                        fontWeight = FontWeight.Medium
                    )
                }

                Text("•", fontSize = 10.sp, color = Color(0xFFCBD5E1))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.GppGood,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "纯净绿色安全软件",
                        fontSize = 9.5.sp,
                        color = Color(0xFF2563EB),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 4. 官方版权归属
        AnimatedVisibility(visible = entered, enter = sectionEnter(1350)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = "懒得找了 (LanDeZhaoLe) · 官方出品",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Copyright © 2026 懒得找了团队 · 保留所有权利",
                    fontSize = 9.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}
