package com.example.ui.uiverse

import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassDialogShell
import com.example.ui.components.streamingBorder
import com.example.ui.theme.ThemePreset
import com.example.ui.uiverse.ActiveUiverseState
import com.example.ui.uiverse.DynamicEffectPreset
import com.example.ui.uiverse.UiKitPreset
import com.example.ui.uiverse.UiverseItem

/**
 * v1.6.8：主题切换已删除，仅保留自定义背景
 * 用户要求删除主题切换功能，保留自定义背景上传
 */
@Composable
fun UiverseDialog(
    isOpen: Boolean,
    onClose: () -> Unit,
    activeState: ActiveUiverseState = ActiveUiverseState(),
    onApplyKit: (UiKitPreset) -> Unit = {},
    onApplyCustomCss: (css: String, html: String) -> Unit = { _, _ -> },
    onResetDefault: () -> Unit = {},
    onApplyItemAsComponent: (UiverseItem) -> Unit = {},
    onApplyComponentTheme: (compId: String, css: String) -> Unit = { _, _ -> },
    componentThemes: Map<String, String> = emptyMap(),
    localBgMediaType: String = "none",
    onPickLocalImage: () -> Unit = {},
    onPickLocalVideo: () -> Unit = {},
    onClearLocalBgMedia: () -> Unit = {},
    onApplyGradientTheme: (ThemePreset) -> Unit = {},
    onApplyDynamicEffect: (DynamicEffectPreset) -> Unit = {},
    activeGradientThemeId: String = "",
    activeDynamicEffect: DynamicEffectPreset = DynamicEffectPreset.NONE
) {
    if (!isOpen) return

    val context = LocalContext.current

    LiquidGlassDialogShell(
        onDismissRequest = onClose,
        title = "自定义背景",
        subtitle = "上传图片或视频作为软件背景",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // v1.4.22：自定义背景 —— 用户上传图片/视频作为软件背景（保留）
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.75f))
                    .border(1.dp, Color(0xFFE8590C).copy(alpha = 0.2f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Image, contentDescription = null, tint = Color(0xFFE8590C), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("自定义背景", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A2C12))
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = when (localBgMediaType) {
                            "image" -> "已设置图片"
                            "video" -> "已设置视频"
                            else -> "未设置"
                        },
                        fontSize = 11.sp,
                        color = if (localBgMediaType == "none") Color(0xFF8A6A4A) else Color(0xFFE8590C),
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "上传本地图片或视频作为全软件背景（自动循环播放）",
                    fontSize = 10.5.sp, color = Color(0xFF8A6A4A), lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onPickLocalImage,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上传图片", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = onPickLocalVideo,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Videocam, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上传视频", fontSize = 12.sp)
                    }
                    if (localBgMediaType != "none") {
                        OutlinedButton(
                            onClick = onClearLocalBgMedia,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Filled.DeleteOutline, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("清除", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 底部操作栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onClose,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier
                        .weight(1f)
                        .streamingBorder(
                            cornerRadius = 14.dp,
                            strokeWidth = 1.4.dp,
                            glowWidth = 3.dp,
                            baseBorderColor = Color(0xFFA855F7).copy(alpha = 0.5f),
                            rainbow = true,
                            showGlow = true
                        )
                ) {
                    Text("完成", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
