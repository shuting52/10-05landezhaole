package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.ActivityLog
import com.example.model.CategoryItem
import com.example.model.LogActionType
import com.example.model.SubCategoryItem
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.EmptyStateView
import com.example.ui.components.PageHeader
import com.example.ui.components.PaginationBar
import com.example.ui.components.SearchBarField
import com.example.viewmodel.AdminUiState
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Ink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.Mist
import com.example.ui.theme.Paper
import com.example.ui.theme.PaperSoft
import java.text.NumberFormat

// ============================================================================
// 分类管理 / 操作日志（Kotlin Compose 重写版）
// ============================================================================

@Composable
fun CategoryManagementScreen(
    uiState: AdminUiState,
    onMoveCategory: (String, Int) -> Unit,
    onSaveOrder: () -> Unit,
    onSaveCategory: (CategoryItem?, String, String, String, List<SubCategoryItem>) -> Unit,
    onDeleteCategory: (CategoryItem) -> Unit,
    onShowToast: (String) -> Unit,
) {
    var dialogOpen by remember { mutableStateOf(false) }
    var editingCat by remember { mutableStateOf<CategoryItem?>(null) }
    var catNameInput by remember { mutableStateOf("") }
    var catIconInput by remember { mutableStateOf("folder") }
    var catDescInput by remember { mutableStateOf("") }
    var catSubcatsText by remember { mutableStateOf("all:全部") }
    var deletingCat by remember { mutableStateOf<CategoryItem?>(null) }

    val sortedCats = uiState.categories.sortedBy { it.order }
    val nf = remember { NumberFormat.getInstance() }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(
            title = "分类管理",
            description = "共 ${sortedCats.size} 个分类 · 排序后点「保存排序」生效",
            actions = {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        editingCat = null
                        catNameInput = ""
                        catIconInput = "folder"
                        catDescInput = ""
                        catSubcatsText = "all:全部"
                        dialogOpen = true
                    },
                    modifier = Modifier.testTag("create_category_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                ) {
                    Text("＋ 新增分类", style = MaterialTheme.typography.labelLarge)
                }
            },
        )

        if (sortedCats.isEmpty()) {
            EmptyStateView(
                title = "还没有分类",
                description = "点击右上角「新增分类」创建第一个分类",
            )
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                sortedCats.forEachIndexed { idx, cat ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSoft,
                        border = BorderStroke(1.dp, Mist),
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "#${cat.order}",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Ink,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        cat.iconKey,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Cinnabar,
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        cat.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Ink,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                if (cat.desc.isNotEmpty()) {
                                    Text(
                                        cat.desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = InkBlack.copy(alpha = 0.6f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    cat.subcategories.take(3).forEach { s ->
                                        Text(
                                            s.name,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            color = InkBlack.copy(alpha = 0.55f),
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    }
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "${nf.format(cat.cardCount.toLong())} 卡片",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = InkBlack.copy(alpha = 0.4f),
                                    )
                                }
                            }
                            IconButton(onClick = { onMoveCategory(cat.id, -1) }, enabled = idx > 0) {
                                Icon(
                                    Icons.Filled.ArrowUpward,
                                    contentDescription = "上移",
                                    modifier = Modifier.size(18.dp),
                                    tint = if (idx > 0) InkBlack else InkBlack.copy(alpha = 0.25f),
                                )
                            }
                            IconButton(onClick = { onMoveCategory(cat.id, 1) }, enabled = idx < sortedCats.lastIndex) {
                                Icon(
                                    Icons.Filled.ArrowDownward,
                                    contentDescription = "下移",
                                    modifier = Modifier.size(18.dp),
                                    tint = if (idx < sortedCats.lastIndex) InkBlack else InkBlack.copy(alpha = 0.25f),
                                )
                            }
                            IconButton(
                                onClick = {
                                    editingCat = cat
                                    catNameInput = cat.name
                                    catIconInput = cat.iconKey
                                    catDescInput = cat.desc
                                    catSubcatsText = cat.subcategories.joinToString("\n") { "${it.id}:${it.name}" }
                                    dialogOpen = true
                                },
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = "编辑")
                            }
                            IconButton(onClick = { deletingCat = cat }) {
                                Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Cinnabar)
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                OutlinedButton(
                    onClick = onSaveOrder,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Mist),
                ) {
                    Text("保存排序", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }

    // 编辑 / 新增分类弹窗
    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = { Text(if (editingCat != null) "编辑分类" else "新增分类", style = MaterialTheme.typography.titleLarge, color = Ink) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = catNameInput,
                        onValueChange = { catNameInput = it },
                        label = { Text("分类名称 *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = catIconInput,
                        onValueChange = { catIconInput = it },
                        label = { Text("图标 key") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = catDescInput,
                        onValueChange = { catDescInput = it },
                        label = { Text("描述") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                    )
                    OutlinedTextField(
                        value = catSubcatsText,
                        onValueChange = { catSubcatsText = it },
                        label = { Text("子分类（每行 一个，格式 id:名称）") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (catNameInput.trim().isEmpty()) {
                            onShowToast("请填写分类名称")
                            return@TextButton
                        }
                        val subs = catSubcatsText.lines().mapNotNull { line ->
                            val t = line.trim()
                            if (t.isEmpty()) null
                            else {
                                val parts = t.split(":")
                                if (parts.size == 2) SubCategoryItem(parts[0].trim(), parts[1].trim())
                                else SubCategoryItem(t, t)
                            }
                        }
                        val finalSubs = if (subs.isEmpty()) listOf(SubCategoryItem("all", "全部")) else subs
                        onSaveCategory(editingCat, catNameInput, catIconInput, catDescInput, finalSubs)
                        dialogOpen = false
                    },
                ) { Text("保存", color = Cinnabar) }
            },
            dismissButton = {
                TextButton(onClick = { dialogOpen = false }) { Text("取消") }
            },
        )
    }

    deletingCat?.let { dc ->
        ConfirmDeleteDialog(
            open = true,
            title = "删除分类",
            itemName = dc.name,
            extraWarning = "该分类下的卡片不会被删除，但会失去分类归属",
            onConfirm = {
                onDeleteCategory(dc)
                deletingCat = null
            },
            onDismiss = { deletingCat = null },
        )
    }
}

/** 操作日志页：搜索 + 按操作类型筛选 + 分页 */
@Composable
fun OperationLogsScreen(
    uiState: AdminUiState,
    onShowToast: (String) -> Unit,
) {
    var keyword by remember { mutableStateOf("") }
    var selectedAction by remember { mutableStateOf("all") }
    var page by remember { mutableIntStateOf(1) }

    val filtered = remember(keyword, selectedAction, uiState.operationLogs) {
        uiState.operationLogs.filter { log ->
            val kw = keyword.trim()
            (kw.isEmpty() || log.content.contains(kw, ignoreCase = true) || log.operator.contains(kw, ignoreCase = true)) &&
                (selectedAction == "all" || log.action.name == selectedAction)
        }
    }
    val pageSize = 10
    val totalPages = if (pageSize <= 0) 1 else (filtered.size + pageSize - 1) / pageSize
    val paged = filtered.drop((page - 1) * pageSize).take(pageSize)

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(
            title = "操作日志",
            description = "共 ${filtered.size} 条记录",
            actions = {
                Spacer(Modifier.weight(1f))
                OutlinedButton(
                    onClick = { onShowToast("已导出操作日志（CSV）") },
                    modifier = Modifier.testTag("export_logs_btn"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Mist),
                ) {
                    Icon(Icons.Filled.FileDownload, contentDescription = "导出", modifier = Modifier.size(16.dp))
                    Text("导出", modifier = Modifier.padding(start = 4.dp))
                }
            },
        )

        SearchBarField(
            value = keyword,
            onValueChange = {
                keyword = it
                page = 1
            },
            placeholder = "搜索操作人 / 内容…",
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = selectedAction == "all",
                onClick = { selectedAction = "all"; page = 1 },
                label = { Text("全部") },
            )
            LogActionType.entries.forEach { a ->
                FilterChip(
                    selected = selectedAction == a.name,
                    onClick = { selectedAction = a.name; page = 1 },
                    label = { Text(a.label) },
                )
            }
        }

        if (filtered.isEmpty()) {
            EmptyStateView(
                title = "暂无日志",
                description = "操作记录会显示在这里",
            )
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                paged.forEach { log ->
                    ActivityLogRow(log)
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
}
