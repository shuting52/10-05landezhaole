package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.ActivityLog
import com.example.model.AdminScreen
import com.example.model.ButtonType
import com.example.model.CardStatus
import com.example.model.ResourceCard
import com.example.ui.components.CardFormDialog
import com.example.ui.components.CardTableSection
import com.example.ui.components.EmptyStateView
import com.example.ui.components.LogActionBadge
import com.example.ui.components.PageHeader
import com.example.ui.components.StatCard
import com.example.viewmodel.AdminUiState
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Ink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.Mist
import com.example.ui.theme.Paper
import com.example.ui.theme.PaperSoft

// ============================================================================
// 工作台 / 卡片管理 / 活动日志（Kotlin Compose 重写版）
// ============================================================================

@Composable
fun DashboardScreen(
    uiState: AdminUiState,
    onDateRangeChange: (String) -> Unit,
    onNavigate: (AdminScreen) -> Unit,
    onSaveCard: (ResourceCard?, String, String, String, ButtonType, CardStatus, String, String, String, String, String, String) -> Unit,
    onDeleteCard: (ResourceCard) -> Unit,
    onShowToast: (String) -> Unit,
) {
    val dateRanges = listOf("近7天", "近30天", "近90天")
    var createOpen by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp),
    ) {
        PageHeader(
            title = "工作台",
            description = "云端数据总览 · ${uiState.lastSyncAt.ifBlank { "尚未同步" }}",
            actions = {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = PaperSoft,
                    border = BorderStroke(1.dp, Mist),
                ) {
                    Row(Modifier.padding(horizontal = 6.dp)) {
                        dateRanges.forEach { r ->
                            Text(
                                r,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 9.dp)
                                    .clickable { onDateRangeChange(r) },
                                color = if (uiState.dateRange == r) Cinnabar else InkBlack.copy(alpha = 0.55f),
                                fontWeight = if (uiState.dateRange == r) FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { createOpen = true },
                    modifier = Modifier.testTag("dashboard_create_card_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                ) {
                    Text("＋ 新增卡片", style = MaterialTheme.typography.labelLarge)
                }
            },
        )

        // 统计卡（每行 2 个）
        uiState.stats.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                row.forEach { stat ->
                    StatCard(item = stat, onNavigate = onNavigate, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        PageHeader(title = "最近动态", description = "云端操作记录")

        if (uiState.activityLogs.isEmpty()) {
            EmptyStateView(
                title = "暂无动态",
                description = "发布版本、修改内容后会在这里留下记录",
            )
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.activityLogs.take(6).forEach { log ->
                    ActivityLogRow(log)
                }
            }
        }
    }

    CardFormDialog(
        open = createOpen,
        card = null,
        categories = uiState.categories.map { it.name },
        categoryObjects = uiState.categories,
        onDismiss = { createOpen = false },
        onSubmit = { _, name, desc, url, btnType, status, cat, subcatId, icon, fallbackText, badge, badgeType ->
            onSaveCard(null, name, desc, url, btnType, status, cat, subcatId, icon, fallbackText, badge, badgeType)
            createOpen = false
        },
    )
}

/** 活动日志行 */
@Composable
fun ActivityLogRow(log: ActivityLog) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = PaperSoft,
        border = BorderStroke(1.dp, Mist),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (log.avatarColorHex != 0L) Color(log.avatarColorHex) else Ink),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    log.operator.firstOrNull()?.toString() ?: "?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        log.operator,
                        style = MaterialTheme.typography.labelLarge,
                        color = Ink,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.width(6.dp))
                    LogActionBadge(log.action)
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    log.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkBlack.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(log.time, style = MaterialTheme.typography.labelSmall, color = InkBlack.copy(alpha = 0.45f))
                if (log.ip.isNotEmpty()) {
                    Text(log.ip, style = MaterialTheme.typography.labelSmall, color = InkBlack.copy(alpha = 0.35f))
                }
            }
        }
    }
}

/** 卡片管理页：复用卡片表格区 */
@Composable
fun CardManagementScreen(
    uiState: AdminUiState,
    onSaveCard: (ResourceCard?, String, String, String, ButtonType, CardStatus, String, String, String, String, String, String) -> Unit,
    onDeleteCard: (ResourceCard) -> Unit,
    onShowToast: (String) -> Unit,
) {
    CardTableSection(
        title = "卡片管理",
        description = "共 ${uiState.allCards.size} 张卡片 · 支持搜索 / 筛选 / 分页",
        allCards = uiState.allCards,
        categories = uiState.categories.map { it.name },
        categoryObjects = uiState.categories,
        onSaveCard = onSaveCard,
        onDeleteCard = onDeleteCard,
        onShowToast = onShowToast,
    )
}
