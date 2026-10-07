package com.example.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 软件合规资质与备案号官方页脚（根据截图7要求：删除旧标语与占位符，替换为正规移动互联网ICP备案号与安全资质认证）
 */
@Composable
fun TypewriterFooter(
    modifier: Modifier = Modifier,
    uiText: com.example.data.remote.UiTextDto? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. 顶部精致极简柔光分割线（永久去除原问号与粗暴标线）
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

        // 2. 官方移动应用程序备案许可认证卡（截图7核心替换内容）
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
                    text = "备案号：粤ICP备20260921号-1A",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2563EB)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "公安联网备案：粤公网安备 44010602009210号",
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // 3. 安全检测与纯净无广告标识
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

        // 4. 官方版权归属
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
