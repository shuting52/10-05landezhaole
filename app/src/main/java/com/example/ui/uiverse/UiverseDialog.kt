package com.example.ui.uiverse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassDialogShell
import com.example.ui.components.streamingBorder
import com.example.ui.theme.APP_THEMES
import com.example.ui.theme.DEFAULT_APP_THEME_ID
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.ThemeCategory

/**
 * v1.7.17：自定义背景弹窗（主题预设已全部删除）
 * 2026-10-23：jicun 式外观四件套 —— 系统主题 / 自定义背景图 / 底栏外观样式 / 界面缩放大小
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
    // 2026-10-23：14 套新主题切换（点击即全局应用）
    appThemeId: String = DEFAULT_APP_THEME_ID,
    onAppThemeChange: (String) -> Unit = {},
    uiScale: Float = 1f,
    onUiScaleChange: (Float) -> Unit = {},
    hideTabLabels: Boolean = false,
    onHideTabLabelsChange: (Boolean) -> Unit = {},
    glassBottomBar: Boolean = true,
    onGlassBottomBarChange: (Boolean) -> Unit = {}
) {
    if (!isOpen) return

    val currentUiColors = LocalThemeUiColors.current

    LiquidGlassDialogShell(
        onDismissRequest = onClose,
        title = "主题外观",
        subtitle = "自定义背景",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ============ 2026-10-23：主题切换（14 套，点击即全局应用） ============
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, currentUiColors.primary.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("主题切换", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = currentUiColors.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            "点击立即应用并关闭",
                            fontSize = 10.5.sp,
                            color = currentUiColors.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                // 2026-10-23：15套主题按分类收纳展示
                val groupedThemes = APP_THEMES.groupBy { it.category }
                ThemeCategory.values().forEach { cat ->
                    val catItems = groupedThemes[cat] ?: return@forEach
                    Text(
                        cat.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                    )
                    catItems.forEach { theme ->
                    val selected = appThemeId == theme.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selected) theme.colors.primary.copy(alpha = 0.10f)
                                else Color(0xFFF1F5F9)
                            )
                            .border(
                                1.dp,
                                if (selected) theme.colors.primary else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            // 2026-10-23：点主题即应用+关闭弹窗；背景跟随主题走（清除自定义背景图/视频）
                            .clickable {
                                onAppThemeChange(theme.id)
                                onClearLocalBgMedia()
                                onClose()
                            }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 主题预览色块：背景渐变 + 主色圆点
                        androidx.compose.foundation.layout.Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        theme.colors.backgroundGradient.take(3)
                                    )
                                )
                                .border(
                                    1.dp,
                                    theme.colors.border,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(14.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(theme.colors.primary)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "${theme.tag} · ${theme.name}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                theme.desc + if (theme.isDark) " · 深色" else "",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        if (selected) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = theme.colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            // ============ jicun 式：界面缩放大小 ============
            var scaleDraft by remember(uiScale) { mutableFloatStateOf(uiScale) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, currentUiColors.primary.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("界面缩放大小", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "${(scaleDraft * 100).toInt()}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentUiColors.primary
                    )
                }
                Slider(
                    value = scaleDraft,
                    onValueChange = { scaleDraft = it },
                    onValueChangeFinished = { onUiScaleChange(scaleDraft) },
                    valueRange = 0.8f..1.3f,
                    colors = SliderDefaults.colors(
                        thumbColor = currentUiColors.primary,
                        activeTrackColor = currentUiColors.primary
                    )
                )
                Text("拖动调整，松手应用（80%–130%）", fontSize = 11.5.sp, color = Color(0xFF64748B))
            }

            // ============ jicun 式：底栏外观样式 ============
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .border(1.dp, currentUiColors.primary.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("底栏外观样式", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), modifier = Modifier.padding(vertical = 8.dp))
                AppearanceSwitchRow(
                    title = "底栏文字标识隐藏",
                    subtitle = "开启后隐藏底栏的文字，只留图标",
                    checked = hideTabLabels,
                    onCheckedChange = onHideTabLabelsChange,
                    accent = currentUiColors.primary
                )
                AppearanceSwitchRow(
                    title = "底栏液态玻璃风格",
                    subtitle = "关闭后改用渐变按钮样式",
                    checked = glassBottomBar,
                    onCheckedChange = onGlassBottomBarChange,
                    accent = currentUiColors.primary
                )
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
                        Text("上传图片", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = currentUiColors.onPrimary)
                    }
                    Button(
                        onClick = onPickLocalVideo,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = currentUiColors.secondary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("上传视频", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = currentUiColors.onPrimary)
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
                Text("完成设置", color = currentUiColors.onPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/** jicun 式外观开关行 */
@Composable
private fun AppearanceSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    accent: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, fontSize = 11.5.sp, color = Color(0xFF64748B), lineHeight = 16.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = accent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFCBD5E1)
            )
        )
    }
}
