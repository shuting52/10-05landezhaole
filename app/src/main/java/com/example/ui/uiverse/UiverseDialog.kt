package com.example.ui.uiverse

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.LiquidGlassDialogShell
import com.example.ui.components.streamingBorder
import com.example.ui.theme.ThemePreset
import com.example.ui.theme.ThemePresetsRepository

/**
 * 主题切换弹窗：
 * - 纯正液态玻璃质感背景呈现
 * - 边框周围七彩霓虹流光跑动特效
 * - 原杂乱功能全部移除，完全采用极简、高质感「暗黑风格按钮」代替
 * - 支持一键切换多种暗黑高质感黑金/曜石/极夜/赛博朋克深空主题与默认经典
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
    val allDarkThemes = ThemePresetsRepository.darkThemes

    LiquidGlassDialogShell(
        onDismissRequest = onClose,
        title = "主题切换",
        subtitle = "液态高透玻璃质感 · 暗黑质感风格矩阵",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 提示胶囊
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.88f))
                    .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFFA855F7), Color(0xFF38BDF8)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DarkMode,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "暗黑极客质感 · 一键触控切换",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF1F5F9)
                        )
                        Text(
                            text = "轻触下方任意暗黑风格按钮，立即沉浸于全新视觉基调",
                            fontSize = 10.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // 1. 默认与经典恢复按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DarkThemeButton(
                    title = "盛世华诞 (默认)",
                    subtitle = "国潮红金 · 喜庆欢歌",
                    accentColor = Color(0xFFDE2910),
                    isSelected = activeGradientThemeId == ThemePresetsRepository.shengshiTheme.id || activeGradientThemeId.isEmpty(),
                    onClick = {
                        onResetDefault()
                        onApplyGradientTheme(ThemePresetsRepository.shengshiTheme)
                        Toast.makeText(context, "已切换为「盛世华诞」默认国潮皮肤", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    modifier = Modifier.weight(1f)
                )
                DarkThemeButton(
                    title = "经典科技蓝",
                    subtitle = "经典明亮 · 清爽纯粹",
                    accentColor = Color(0xFF2196F3),
                    isSelected = activeGradientThemeId == ThemePresetsRepository.classicTheme.id,
                    onClick = {
                        onApplyGradientTheme(ThemePresetsRepository.classicTheme)
                        Toast.makeText(context, "已切换为「经典科技蓝」皮肤", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // 2. 暗黑风格按钮列表（深邃曜石、极夜星空、赛博朋克深空、夜间护眼暗绿）
            allDarkThemes.forEach { darkTheme ->
                DarkThemeButton(
                    title = darkTheme.name,
                    subtitle = when (darkTheme.id) {
                        "dark_obsidian" -> "曜石紫金流光 · 极客深邃黑"
                        "dark_midnight" -> "极夜深蓝星空 · 沉稳冷峻"
                        "dark_cyberpunk" -> "赛博霓虹电光 · 炫彩粉青"
                        "dark_emerald" -> "暗夜翡翠幽绿 · 柔光护眼"
                        else -> "高级暗黑格调质感"
                    },
                    accentColor = darkTheme.primaryColor,
                    secondaryColor = darkTheme.secondaryColor,
                    isSelected = activeGradientThemeId == darkTheme.id,
                    onClick = {
                        onApplyGradientTheme(darkTheme)
                        Toast.makeText(context, "已开启暗黑模式「${darkTheme.name}」", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 底部操作栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onResetDefault()
                        onApplyGradientTheme(ThemePresetsRepository.shengshiTheme)
                        Toast.makeText(context, "已重置为系统默认皮肤", Toast.LENGTH_SHORT).show()
                        onClose()
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("重置默认", fontSize = 12.sp)
                }

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

/**
 * 暗黑风格高质感专属按钮
 */
@Composable
private fun DarkThemeButton(
    title: String,
    subtitle: String,
    accentColor: Color,
    secondaryColor: Color = accentColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0D1117).copy(alpha = 0.94f),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.8.dp else 1.dp,
            color = if (isSelected) accentColor else Color.White.copy(alpha = 0.15f)
        ),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (isSelected) {
                    Modifier.streamingBorder(
                        cornerRadius = 16.dp,
                        strokeWidth = 1.6.dp,
                        glowWidth = 3.2.dp,
                        baseBorderColor = accentColor.copy(alpha = 0.6f),
                        rainbow = true,
                        showGlow = true
                    )
                } else Modifier
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                // 颜色指示光点
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(accentColor, secondaryColor)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) accentColor else Color(0xFFF1F5F9)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.5.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.18f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "当前使用",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }
        }
    }
}
