package com.example.ui.uiverse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassDialogShell
import com.example.ui.components.streamingBorder
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.AppTheme

/**
 * v1.7.17：自定义背景弹窗（主题预设已全部删除）
 * 仅保留：上传图片/视频背景、透明磨砂效果、清除。
 */
@Composable
fun UiverseDialog(
    isOpen: Boolean,
    onClose: () -> Unit,
    localBgMediaType: String = "none",
    bgFrosted: Boolean = false,
    onPickLocalImage: () -> Unit = {},
    onPickLocalVideo: () -> Unit = {},
    onClearLocalBgMedia: () -> Unit = {},
    onToggleFrosted: (Boolean) -> Unit = {},
    // v1.7.27：App 主题选择
    appTheme: AppTheme = AppTheme.AERO,
    onThemeChange: (AppTheme) -> Unit = {}
) {
    if (!isOpen) return

    val currentUiColors = LocalThemeUiColors.current

    LiquidGlassDialogShell(
        onDismissRequest = onClose,
        title = "主题外观",
        subtitle = "选择喜欢的 App 主题风格",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // v1.7.27：主题选择
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, currentUiColors.primary.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Text("App 主题", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppTheme.values().forEach { theme ->
                        val selected = appTheme == theme
                        Surface(
                            onClick = { onThemeChange(theme) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (selected) currentUiColors.primary.copy(alpha = 0.12f) else Color(0xFFF1F5F9),
                            border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, currentUiColors.primary) else null,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // 主题色预览条
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when (theme) {
                                                AppTheme.AERO -> Color(0xFF00A8E8)
                                                AppTheme.LEVER -> Color(0xFF1269EF)
                                                AppTheme.AURORA -> Color(0xFFFF6B4A)
                                            }
                                        )
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    theme.displayName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) currentUiColors.primary else Color(0xFF1E293B)
                                )
                                Text(
                                    theme.desc,
                                    fontSize = 10.5.sp,
                                    color = Color(0xFF64748B),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // 自定义背景卡片
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, currentUiColors.primary.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Image, contentDescription = null, tint = currentUiColors.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("自定义图片/视频背景", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (localBgMediaType == "none") Color(0xFFE2E8F0) else currentUiColors.primary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = when (localBgMediaType) {
                                "image" -> "已应用图片背景"
                                "video" -> "已应用视频背景"
                                else -> "未设置"
                            },
                            fontSize = 11.sp,
                            color = if (localBgMediaType == "none") Color(0xFF64748B) else currentUiColors.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "上传手机本地喜欢的壁纸图片或 MP4 动态视频，作为全软件的背景底图（自动循环播放）",
                    fontSize = 11.5.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onPickLocalImage,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = currentUiColors.primary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上传图片", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onPickLocalVideo,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = currentUiColors.secondary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上传视频", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                    }
                    if (localBgMediaType != "none") {
                        OutlinedButton(
                            onClick = onClearLocalBgMedia,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                            modifier = Modifier.weight(0.8f)
                        ) {
                            Icon(Icons.Filled.DeleteOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("清除", fontSize = 12.sp)
                        }
                    }
                }
            }

            // 透明磨砂效果开关
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, currentUiColors.primary.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("透明磨砂", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "背景模糊成毛玻璃效果，前景内容更清晰易读",
                        fontSize = 11.5.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Switch(
                    checked = bgFrosted,
                    onCheckedChange = onToggleFrosted,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = currentUiColors.primary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCBD5E1)
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 底部完成按钮（流光边框）
            Button(
                onClick = onClose,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = currentUiColors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .streamingBorder(
                        strokeWidth = 2.dp,
                        cornerRadius = 14.dp
                    )
            ) {
                Text("完成设置", color = Color.White, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
