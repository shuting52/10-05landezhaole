package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.ButtonType
import com.example.model.CardStatus
import com.example.model.CategoryItem
import com.example.model.ResourceCard
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Ink

// ============================================================================
// 控制台弹窗组件（Kotlin Compose 重写版，视觉与 v2.1.0 发布物一致）
// ============================================================================

/** 卡片编辑/新增弹窗：12 个字段 + 保存校验 */
@Composable
fun CardFormDialog(
    open: Boolean,
    card: ResourceCard?,
    categories: List<String>,
    categoryObjects: List<CategoryItem>,
    onDismiss: () -> Unit,
    onSubmit: (ResourceCard?, String, String, String, ButtonType, CardStatus, String, String, String, String, String, String) -> Unit,
) {
    if (!open) return

    var name by remember { mutableStateOf(card?.name ?: "") }
    var desc by remember { mutableStateOf(card?.description ?: "") }
    var url by remember { mutableStateOf(card?.url ?: "") }
    var buttonType by remember { mutableStateOf(card?.buttonType ?: ButtonType.LINK) }
    var status by remember { mutableStateOf(card?.status ?: CardStatus.DRAFT) }
    var category by remember { mutableStateOf(card?.category ?: (categories.firstOrNull() ?: "")) }
    var subcatId by remember { mutableStateOf(card?.subcatId ?: "") }
    var icon by remember { mutableStateOf(card?.icon ?: "") }
    var fallbackText by remember { mutableStateOf(card?.fallbackText ?: "") }
    var badge by remember { mutableStateOf(card?.badge ?: "") }
    var badgeType by remember { mutableStateOf(card?.badgeType ?: "") }
    var highlights by remember { mutableStateOf(card?.highlights ?: "") }
    var error by remember { mutableStateOf("") }

    var btnExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }
    var catExpanded by remember { mutableStateOf(false) }
    var subcatExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (card != null) "编辑卡片" else "新增卡片", style = MaterialTheme.typography.titleLarge, color = Ink) },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("名称 *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("描述") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("链接 URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                // 按钮类型
                ExposedDropdownMenuBox(expanded = btnExpanded, onExpandedChange = { btnExpanded = it }) {
                    OutlinedTextField(
                        value = buttonType.label,
                        onValueChange = {},
                        label = { Text("按钮类型") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = btnExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                    )
                    ExposedDropdownMenu(expanded = btnExpanded, onDismissRequest = { btnExpanded = false }) {
                        ButtonType.entries.forEach { t ->
                            DropdownMenuItem(text = { Text(t.label) }, onClick = { buttonType = t; btnExpanded = false })
                        }
                    }
                }
                // 状态
                ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = it }) {
                    OutlinedTextField(
                        value = status.label,
                        onValueChange = {},
                        label = { Text("状态") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                    )
                    ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                        CardStatus.entries.forEach { s ->
                            DropdownMenuItem(text = { Text(s.label) }, onClick = { status = s; statusExpanded = false })
                        }
                    }
                }
                // 分类
                ExposedDropdownMenuBox(expanded = catExpanded, onExpandedChange = { catExpanded = it }) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        label = { Text("分类") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                    )
                    ExposedDropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                        categories.forEach { c ->
                            DropdownMenuItem(text = { Text(c) }, onClick = { category = c; catExpanded = false })
                        }
                    }
                }
                // 子分类
                val subs = categoryObjects.firstOrNull { it.name == category }?.subcategories.orEmpty()
                ExposedDropdownMenuBox(expanded = subcatExpanded, onExpandedChange = { subcatExpanded = it }) {
                    OutlinedTextField(
                        value = subcatId,
                        onValueChange = {},
                        label = { Text("子分类") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subcatExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                    )
                    ExposedDropdownMenu(expanded = subcatExpanded, onDismissRequest = { subcatExpanded = false }) {
                        subs.forEach { s ->
                            DropdownMenuItem(text = { Text(s.name) }, onClick = { subcatId = s.id; subcatExpanded = false })
                        }
                    }
                }
                OutlinedTextField(
                    value = icon,
                    onValueChange = { icon = it },
                    label = { Text("图标") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = fallbackText,
                    onValueChange = { fallbackText = it },
                    label = { Text("兜底文字") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = badge,
                    onValueChange = { badge = it },
                    label = { Text("徽章文字") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = badgeType,
                    onValueChange = { badgeType = it },
                    label = { Text("徽章类型") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = highlights,
                    onValueChange = { highlights = it },
                    label = { Text("亮点") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                )
                if (error.isNotEmpty()) {
                    Text(error, color = Cinnabar, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isBlank()) {
                        error = "请填写卡片名称"
                        return@TextButton
                    }
                    onSubmit(
                        card, name, desc, url, buttonType, status,
                        category, subcatId, icon, fallbackText, badge, badgeType,
                    )
                },
            ) { Text("保存", color = Cinnabar) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}

/** 删除确认弹窗：标题 + 目标名 + 可选警告 */
@Composable
fun ConfirmDeleteDialog(
    open: Boolean,
    title: String,
    itemName: String,
    extraWarning: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!open) return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, style = MaterialTheme.typography.titleLarge, color = Ink) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("确定要删除「$itemName」吗？", style = MaterialTheme.typography.bodyLarge)
                if (!extraWarning.isNullOrBlank()) {
                    Text(extraWarning, color = Cinnabar, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("删除", color = Cinnabar) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}
