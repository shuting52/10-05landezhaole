package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.ButtonType
import com.example.model.CardStatus
import com.example.model.CategoryItem
import com.example.model.ResourceCard
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.GoldDark
import com.example.ui.theme.Ink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.Mist
import com.example.ui.theme.Paper

// ============================================================================
// 卡片表格区（Kotlin Compose 重写版）：搜索/筛选/分页/列表/新增/导出
// ============================================================================

@Composable
fun CardTableSection(
    title: String,
    description: String,
    allCards: List<ResourceCard>,
    categories: List<String>,
    categoryObjects: List<CategoryItem>,
    pageSize: Int = 10,
    showFilters: Boolean = true,
    showHeaderActions: Boolean = true,
    onSaveCard: (ResourceCard?, String, String, String, ButtonType, CardStatus, String, String, String, String, String, String) -> Unit,
    onDeleteCard: (ResourceCard) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var keyword by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("all") }
    var selectedCategory by remember { mutableStateOf("all") }
    var page by remember { mutableIntStateOf(1) }
    var formOpen by remember { mutableStateOf(false) }
    var editingCard by remember { mutableStateOf<ResourceCard?>(null) }
    var deletingCard by remember { mutableStateOf<ResourceCard?>(null) }

    // 筛选
    val filtered = remember(keyword, selectedStatus, selectedCategory, allCards) {
        allCards.filter { c ->
            val kw = keyword.trim()
            (kw.isEmpty() || c.name.contains(kw, ignoreCase = true) || c.description.contains(kw, ignoreCase = true) || c.url.contains(kw, ignoreCase = true)) &&
                (selectedStatus == "all" || c.status.name == selectedStatus) &&
                (selectedCategory == "all" || c.category == selectedCategory)
        }
    }
    val totalPages = if (pageSize <= 0) 1 else (filtered.size + pageSize - 1) / pageSize
    val paged = filtered.drop((page - 1) * pageSize).take(pageSize)

    Column(modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(
            title = title,
            description = description,
            actions = {
                if (showHeaderActions) {
                    OutlinedButton(
                        onClick = {
                            onShowToast("已导出卡片列表（CSV，共 ${filtered.size} 条）")
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Cinnabar),
                    ) {
                        Icon(Icons.Filled.FileDownload, contentDescription = "导出", modifier = Modifier.width(16.dp))
                        Text("导出", modifier = Modifier.padding(start = 4.dp))
                    }
                    OutlinedButton(
                        onClick = {
                            editingCard = null
                            formOpen = true
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Cinnabar),
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "新增", modifier = Modifier.width(16.dp))
                        Text("新增", modifier = Modifier.padding(start = 4.dp))
                    }
                }
            },
        )

        SearchBarField(
            value = keyword,
            onValueChange = {
                keyword = it
                page = 1
            },
            placeholder = "搜索卡片名称 / 描述 / 链接…",
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        if (showFilters) {
            Spacer(Modifier.padding(top = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedStatus == "all",
                    onClick = { selectedStatus = "all"; page = 1 },
                    label = { Text("全部") },
                )
                CardStatus.entries.forEach { s ->
                    FilterChip(
                        selected = selectedStatus == s.name,
                        onClick = { selectedStatus = s.name; page = 1 },
                        label = { Text(s.label) },
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedCategory == "all",
                    onClick = { selectedCategory = "all"; page = 1 },
                    label = { Text("全部分类") },
                )
                categories.forEach { c ->
                    FilterChip(
                        selected = selectedCategory == c,
                        onClick = { selectedCategory = c; page = 1 },
                        label = { Text(c) },
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            EmptyStateView(
                title = "没有找到卡片",
                description = if (keyword.isNotEmpty()) "换个关键词试试，或点击右上角「新增」" else "还没有卡片，点击右上角「新增」添加第一张",
            )
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                paged.forEach { card ->
                    CardItemRow(
                        card = card,
                        onView = {
                            val url = card.url.ifBlank { card.category }
                            onShowToast("查看《${card.name}》· $url")
                        },
                        onEdit = {
                            editingCard = card
                            formOpen = true
                        },
                        onDelete = { deletingCard = card },
                    )
                }
            }
            PaginationBar(
                page = page,
                pageSize = pageSize,
                total = filtered.size,
                onPageChange = { page = it },
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }

    // 表单弹窗
    CardFormDialog(
        open = formOpen,
        card = editingCard,
        categories = categories,
        categoryObjects = categoryObjects,
        onDismiss = { formOpen = false },
        onSubmit = { card, name, desc, url, btnType, status, cat, subcatId, icon, fallbackText, badge, badgeType ->
            onSaveCard(card, name, desc, url, btnType, status, cat, subcatId, icon, fallbackText, badge, badgeType)
            formOpen = false
        },
    )

    // 删除确认弹窗
    deletingCard?.let { dc ->
        ConfirmDeleteDialog(
            open = true,
            title = "删除卡片",
            itemName = dc.name,
            extraWarning = "删除后本体将同步移除该卡片，此操作不可撤销",
            onConfirm = {
                onDeleteCard(dc)
                deletingCard = null
            },
            onDismiss = { deletingCard = null },
        )
    }
}

/** 卡片行：分类/子分类徽章 + 名称/描述/链接 + 状态 + 操作按钮 */
@Composable
fun CardItemRow(
    card: ResourceCard,
    onView: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().testTag("card_item_${card.id}"),
        shape = RoundedCornerShape(14.dp),
        color = Paper,
        border = BorderStroke(1.dp, Mist),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (card.category.isNotEmpty()) {
                        Text(
                            card.category,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = Ink,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    if (card.subcatId.isNotEmpty()) {
                        Text(
                            card.subcatId,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = GoldDark,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    ButtonTypeBadge(card.buttonType)
                }
                Spacer(Modifier.padding(top = 4.dp))
                Text(
                    card.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (card.description.isNotEmpty()) {
                    Text(
                        card.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = InkBlack.copy(alpha = 0.6f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.padding(top = 4.dp))
                CardStatusBadge(card.status)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onView) {
                    Icon(Icons.Filled.Visibility, contentDescription = "查看")
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "编辑")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Cinnabar)
                }
            }
        }
    }
}
