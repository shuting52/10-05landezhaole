package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdminScreen
import com.example.model.ButtonType
import com.example.model.CardStatus
import com.example.model.LogActionType
import com.example.model.StatIcon
import com.example.model.StatItem
import com.example.model.StatTone
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Gold
import com.example.ui.theme.GoldDark
import com.example.ui.theme.Ink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JadeGreen
import com.example.ui.theme.Mist
import com.example.ui.theme.MistSoft
import com.example.ui.theme.PaperSoft
import com.example.ui.theme.StatCinnabarBorder
import com.example.ui.theme.StatCinnabarStart
import com.example.ui.theme.StatGoldBorder
import com.example.ui.theme.StatGoldStart
import com.example.ui.theme.StatGoldText
import com.example.ui.theme.StatInkBorder
import com.example.ui.theme.StatInkStart
import com.example.ui.theme.StatOrangeBorder
import com.example.ui.theme.StatOrangeStart
import com.example.ui.theme.StatOrangeText

// ============================================================================
// 控制台公共组件（Kotlin Compose 重写版，视觉与 v2.1.0 发布物一致）
// ============================================================================

/** 页头：标题 + 描述 + 右侧操作区 */
@Composable
fun PageHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, color = Ink)
            if (description.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkBlack.copy(alpha = 0.6f),
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { actions() }
    }
}

/** 统计卡：按 tone 换底色/描边/强调色，点击可跳转对应页面 */
@Composable
fun StatCard(
    item: StatItem,
    onNavigate: (AdminScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (bgStart, borderColor, accentColor) = when (item.tone) {
        StatTone.CINNABAR -> Triple(StatCinnabarStart, StatCinnabarBorder, Cinnabar)
        StatTone.GOLD -> Triple(StatGoldStart, StatGoldBorder, StatGoldText)
        StatTone.INK -> Triple(StatInkStart, StatInkBorder, Ink)
        StatTone.ORANGE -> Triple(StatOrangeStart, StatOrangeBorder, StatOrangeText)
    }
    val iconVec = when (item.icon) {
        StatIcon.LAYERS -> Icons.Filled.Layers
        StatIcon.DOWNLOAD -> Icons.Filled.Download
        StatIcon.USERS -> Icons.Filled.AutoAwesome
        StatIcon.CLIPBOARD -> Icons.Filled.Verified
    }
    var cardModifier = modifier.fillMaxWidth().testTag("stat_card_${item.id}")
    if (item.actionScreen != null) {
        cardModifier = cardModifier.clickable { onNavigate(item.actionScreen) }
    }
    Surface(
        modifier = cardModifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(Modifier.fillMaxWidth().background(bgStart).padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(iconVec, contentDescription = item.label, tint = accentColor, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkBlack.copy(alpha = 0.55f),
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        item.value,
                        style = MaterialTheme.typography.titleLarge,
                        color = Ink,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            if (item.delta.isNotEmpty() || item.actionLabel.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (item.delta.isNotEmpty()) {
                        Text(
                            item.delta,
                            style = MaterialTheme.typography.labelMedium,
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    if (item.deltaLabel.isNotEmpty()) {
                        Text(
                            item.deltaLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = InkBlack.copy(alpha = 0.45f),
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    if (item.actionLabel.isNotEmpty()) {
                        Text(
                            item.actionLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = accentColor,
                        )
                    }
                }
            }
        }
    }
}

/** 卡片状态徽章 */
@Composable
fun CardStatusBadge(
    status: CardStatus,
    modifier: Modifier = Modifier,
) {
    val (bg, fg) = when (status) {
        CardStatus.PUBLISHED -> JadeGreen.copy(alpha = 0.12f) to JadeGreen
        CardStatus.REVIEWING -> Gold.copy(alpha = 0.16f) to GoldDark
        CardStatus.DRAFT -> Mist to InkBlack.copy(alpha = 0.65f)
    }
    Surface(modifier = modifier, shape = CircleShape, color = bg) {
        Text(
            status.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = fg,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

/** 按钮类型徽章 */
@Composable
fun ButtonTypeBadge(
    type: ButtonType,
    modifier: Modifier = Modifier,
) {
    val (bg, fg) = when (type) {
        ButtonType.DOWNLOAD -> Cinnabar.copy(alpha = 0.12f) to Cinnabar
        ButtonType.LINK -> Ink.copy(alpha = 0.12f) to Ink
        ButtonType.COPY -> Gold.copy(alpha = 0.16f) to GoldDark
        ButtonType.CONTACT -> MistSoft to InkBlack.copy(alpha = 0.75f)
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bg,
        border = if (type == ButtonType.CONTACT) BorderStroke(1.dp, Mist) else null,
    ) {
        Text(
            type.label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            color = fg,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

/** 日志操作徽章 */
@Composable
fun LogActionBadge(
    action: LogActionType,
    modifier: Modifier = Modifier,
) {
    val (bg, fg) = when (action) {
        LogActionType.CREATE -> JadeGreen.copy(alpha = 0.12f) to JadeGreen
        LogActionType.UPDATE -> Gold.copy(alpha = 0.16f) to GoldDark
        LogActionType.PUBLISH -> Cinnabar.copy(alpha = 0.12f) to Cinnabar
        LogActionType.DELETE -> Cinnabar.copy(alpha = 0.18f) to Cinnabar
        LogActionType.REVIEW -> Ink.copy(alpha = 0.12f) to Ink
    }
    Surface(modifier = modifier, shape = RoundedCornerShape(8.dp), color = bg) {
        Text(
            action.label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            color = fg,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

/** 启用状态徽章 */
@Composable
fun EnabledBadge(
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val (bg, fg, label) = if (enabled) {
        Triple(JadeGreen.copy(alpha = 0.12f), JadeGreen, "启用")
    } else {
        Triple(Mist, InkBlack.copy(alpha = 0.6f), "停用")
    }
    Surface(modifier = modifier, shape = CircleShape, color = bg) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = fg,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

/** 搜索框：前置放大镜 + 有内容时显示清除按钮 */
@Composable
fun SearchBarField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    testTag: String = "search_input",
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth().testTag(testTag),
        placeholder = {
            Text(
                placeholder,
                color = InkBlack.copy(alpha = 0.45f),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = "搜索", tint = InkBlack.copy(alpha = 0.45f))
        },
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = "清除搜索", tint = InkBlack.copy(alpha = 0.45f))
                }
            }
        } else null,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = PaperSoft,
            unfocusedContainerColor = PaperSoft,
            cursorColor = Cinnabar,
            focusedBorderColor = Mist,
            unfocusedBorderColor = Mist,
            focusedLeadingIconColor = Cinnabar,
            unfocusedLeadingIconColor = Mist,
        ),
        keyboardOptions = KeyboardOptions.Default,
    )
}

/** 分页条：上一页 / 页码 / 下一页 */
@Composable
fun PaginationBar(
    page: Int,
    pageSize: Int,
    total: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val totalPages = if (pageSize <= 0) 1 else (total + pageSize - 1) / pageSize
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { if (page > 1) onPageChange(page - 1) }, enabled = page > 1) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "上一页")
        }
        Text(
            "$page / $totalPages",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = Cinnabar,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium,
        )
        IconButton(onClick = { if (page < totalPages) onPageChange(page + 1) }, enabled = page < totalPages) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "下一页")
        }
    }
}

/** 空状态视图：标题 + 描述 + 可选操作按钮 */
@Composable
fun EmptyStateView(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("📭", fontSize = 40.sp)
        Spacer(Modifier.height(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = Ink)
        Spacer(Modifier.height(4.dp))
        Text(
            description,
            style = MaterialTheme.typography.bodyMedium,
            color = InkBlack.copy(alpha = 0.45f),
            textAlign = TextAlign.Center,
        )
        if (action != null) {
            Spacer(Modifier.height(16.dp))
            action()
        }
    }
}
