package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.ResourceButton
import com.example.model.SkillItem
import com.example.model.TextItem
import com.example.ui.components.ButtonTypeBadge
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.components.EmptyStateView
import com.example.ui.components.EnabledBadge
import com.example.ui.components.PageHeader
import com.example.ui.components.SearchBarField
import com.example.viewmodel.AdminUiState
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Ink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.Mist
import com.example.ui.theme.Paper
import com.example.ui.theme.PaperSoft

// ============================================================================
// 软件 / Skill / UI 文本管理（Kotlin Compose 重写版）
// ============================================================================

@Composable
fun ButtonManagementScreen(
    uiState: AdminUiState,
    onSaveSoftware: (ResourceButton?, String, String, String, String, String, String, String, String, String, String, String) -> Unit,
    onDeleteSoftware: (ResourceButton) -> Unit,
    onUploadFile: (Uri, String, (String, String) -> Unit) -> Unit,
    onShowToast: (String) -> Unit,
) {
    var keyword by remember { mutableStateOf("") }
    var dialogOpen by remember { mutableStateOf(false) }
    var editingBtn by remember { mutableStateOf<ResourceButton?>(null) }
    var deletingBtn by remember { mutableStateOf<ResourceButton?>(null) }

    var formName by remember { mutableStateOf("") }
    var formUrl by remember { mutableStateOf("") }
    var formDesc by remember { mutableStateOf("") }
    var formAuthor by remember { mutableStateOf("") }
    var formBadge by remember { mutableStateOf("") }
    var formBadgeType by remember { mutableStateOf("") }
    var formTags by remember { mutableStateOf("") }
    var formApkUrl by remember { mutableStateOf("") }
    var formPreviewUrl by remember { mutableStateOf("") }
    var formIconUrl by remember { mutableStateOf("") }
    var formMode by remember { mutableStateOf("url") }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onUploadFile(it, "auto") { downloadUrl, fileName ->
            onShowToast("已上传：$fileName")
            if (formIconUrl.isBlank()) formIconUrl = downloadUrl
        } }
    }

    val filtered = uiState.buttons.filter {
        val kw = keyword.trim()
        kw.isEmpty() || it.name.contains(kw, ignoreCase = true) || it.desc.contains(kw, ignoreCase = true) || it.author.contains(kw, ignoreCase = true)
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(
            title = "软件管理",
            description = "共 ${uiState.buttons.size} 个软件 · 支持上传 APK / 预览图",
            actions = {
                OutlinedButton(
                    onClick = { filePicker.launch("*/*") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Mist),
                ) {
                    Icon(Icons.Filled.Upload, contentDescription = "上传", modifier = Modifier.width(16.dp))
                    Text("上传文件", modifier = Modifier.padding(start = 4.dp))
                }
                Button(
                    onClick = {
                        editingBtn = null
                        formName = ""; formUrl = ""; formDesc = ""; formAuthor = ""
                        formBadge = ""; formBadgeType = ""; formTags = ""
                        formApkUrl = ""; formPreviewUrl = ""; formIconUrl = ""; formMode = "url"
                        dialogOpen = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "新增", modifier = Modifier.width(16.dp))
                    Text("新增", modifier = Modifier.padding(start = 4.dp))
                }
            },
        )

        SearchBarField(
            value = keyword,
            onValueChange = { keyword = it },
            placeholder = "搜索软件名称 / 描述 / 作者…",
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        if (filtered.isEmpty()) {
            EmptyStateView(
                title = "没有找到软件",
                description = "换个关键词，或点击右上角「新增」",
            )
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                filtered.forEach { btn ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSoft,
                        border = BorderStroke(1.dp, Mist),
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        btn.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Ink,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false),
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    ButtonTypeBadge(btn.type)
                                }
                                if (btn.desc.isNotEmpty()) {
                                    Text(btn.desc, style = MaterialTheme.typography.bodySmall, color = InkBlack.copy(alpha = 0.6f), maxLines = 2, overflow = TextOverflow.Ellipsis)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (btn.author.isNotEmpty()) Text("作者：${btn.author}", style = MaterialTheme.typography.labelSmall, color = InkBlack.copy(alpha = 0.5f))
                                    if (btn.badge.isNotEmpty()) Text(" · ${btn.badge}", style = MaterialTheme.typography.labelSmall, color = InkBlack.copy(alpha = 0.5f))
                                    Spacer(Modifier.width(6.dp))
                                    EnabledBadge(btn.mode == "apk")
                                }
                            }
                            IconButton(
                                onClick = {
                                    editingBtn = btn
                                    formName = btn.name; formUrl = btn.url; formDesc = btn.desc; formAuthor = btn.author
                                    formBadge = btn.badge; formBadgeType = btn.badgeType; formTags = btn.tags
                                    formApkUrl = btn.apkUrl; formPreviewUrl = btn.previewUrl; formIconUrl = btn.iconUrl; formMode = btn.mode
                                    dialogOpen = true
                                },
                            ) { Icon(Icons.Filled.Edit, contentDescription = "编辑") }
                            IconButton(onClick = { deletingBtn = btn }) { Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Cinnabar) }
                        }
                    }
                }
            }
        }
    }

    // 软件表单弹窗
    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = { Text(if (editingBtn != null) "编辑软件" else "新增软件", style = MaterialTheme.typography.titleLarge, color = Ink) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedTextField(formName, { formName = it }, label = { Text("名称 *") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formUrl, { formUrl = it }, label = { Text("链接 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formDesc, { formDesc = it }, label = { Text("描述") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                    OutlinedTextField(formAuthor, { formAuthor = it }, label = { Text("作者") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(formBadge, { formBadge = it }, label = { Text("徽章") }, singleLine = true, modifier = Modifier.weight(1f))
                        OutlinedTextField(formBadgeType, { formBadgeType = it }, label = { Text("徽章类型") }, singleLine = true, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(formTags, { formTags = it }, label = { Text("标签（逗号分隔）") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formApkUrl, { formApkUrl = it }, label = { Text("APK 直链") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formPreviewUrl, { formPreviewUrl = it }, label = { Text("预览图 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formIconUrl, { formIconUrl = it }, label = { Text("图标 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formMode, { formMode = it }, label = { Text("模式（url / apk）") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (formName.isBlank()) { onShowToast("请填写软件名称"); return@TextButton }
                        onSaveSoftware(editingBtn, formName, formUrl, formDesc, formAuthor, formBadge, formBadgeType, formTags, formApkUrl, formPreviewUrl, formIconUrl, formMode)
                        dialogOpen = false
                    },
                ) { Text("保存", color = Cinnabar) }
            },
            dismissButton = { TextButton(onClick = { dialogOpen = false }) { Text("取消") } },
        )
    }

    deletingBtn?.let { d ->
        ConfirmDeleteDialog(
            open = true,
            title = "删除软件",
            itemName = d.name,
            extraWarning = "删除后本体将同步移除该软件",
            onConfirm = { onDeleteSoftware(d); deletingBtn = null },
            onDismiss = { deletingBtn = null },
        )
    }
}

@Composable
fun SkillManagementScreen(
    uiState: AdminUiState,
    onSaveSkill: (SkillItem?, String, String, String, String, String, String, String, String, String, String, String, String) -> Unit,
    onDeleteSkill: (SkillItem) -> Unit,
    onUploadFile: (Uri, String, (String, String) -> Unit) -> Unit,
    onShowToast: (String) -> Unit,
) {
    var keyword by remember { mutableStateOf("") }
    var dialogOpen by remember { mutableStateOf(false) }
    var editingSkill by remember { mutableStateOf<SkillItem?>(null) }
    var deletingSkill by remember { mutableStateOf<SkillItem?>(null) }

    var formTitle by remember { mutableStateOf("") }
    var formDesc by remember { mutableStateOf("") }
    var formPromptType by remember { mutableStateOf("") }
    var formPrompt by remember { mutableStateOf("") }
    var formUrl by remember { mutableStateOf("") }
    var formAuthor by remember { mutableStateOf("") }
    var formBadge by remember { mutableStateOf("") }
    var formTags by remember { mutableStateOf("") }
    var formPreviewUrl by remember { mutableStateOf("") }
    var formMediaUrl by remember { mutableStateOf("") }
    var formIconUrl by remember { mutableStateOf("") }
    var formMode by remember { mutableStateOf("url") }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onUploadFile(it, "auto") { downloadUrl, fileName ->
            onShowToast("已上传：$fileName")
            if (formIconUrl.isBlank()) formIconUrl = downloadUrl
        } }
    }

    val filtered = uiState.skills.filter {
        val kw = keyword.trim()
        kw.isEmpty() || it.title.contains(kw, ignoreCase = true) || it.desc.contains(kw, ignoreCase = true)
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(
            title = "Skill 管理",
            description = "共 ${uiState.skills.size} 个 Skill",
            actions = {
                OutlinedButton(
                    onClick = { filePicker.launch("*/*") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Mist),
                ) {
                    Icon(Icons.Filled.Upload, contentDescription = "上传", modifier = Modifier.width(16.dp))
                    Text("上传文件", modifier = Modifier.padding(start = 4.dp))
                }
                Button(
                    onClick = {
                        editingSkill = null
                        formTitle = ""; formDesc = ""; formPromptType = ""; formPrompt = ""
                        formUrl = ""; formAuthor = ""; formBadge = ""; formTags = ""
                        formPreviewUrl = ""; formMediaUrl = ""; formIconUrl = ""; formMode = "url"
                        dialogOpen = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "新增", modifier = Modifier.width(16.dp))
                    Text("新增", modifier = Modifier.padding(start = 4.dp))
                }
            },
        )

        SearchBarField(
            value = keyword,
            onValueChange = { keyword = it },
            placeholder = "搜索 Skill 标题 / 描述…",
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        if (filtered.isEmpty()) {
            EmptyStateView(title = "没有找到 Skill", description = "换个关键词，或点击右上角「新增」")
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                filtered.forEach { skill ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSoft,
                        border = BorderStroke(1.dp, Mist),
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    skill.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Ink,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                if (skill.desc.isNotEmpty()) {
                                    Text(skill.desc, style = MaterialTheme.typography.bodySmall, color = InkBlack.copy(alpha = 0.6f), maxLines = 2, overflow = TextOverflow.Ellipsis)
                                }
                                Text(
                                    (listOfNotNull(skill.promptType, skill.author, skill.badge).joinToString(" · ")).ifBlank { "type: ${skill.type}" },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = InkBlack.copy(alpha = 0.5f),
                                )
                            }
                            IconButton(
                                onClick = {
                                    editingSkill = skill
                                    formTitle = skill.title; formDesc = skill.desc; formPromptType = skill.promptType; formPrompt = skill.prompt
                                    formUrl = skill.url; formAuthor = skill.author; formBadge = skill.badge; formTags = skill.tags
                                    formPreviewUrl = skill.previewUrl; formMediaUrl = skill.mediaUrl; formIconUrl = skill.iconUrl; formMode = skill.mode
                                    dialogOpen = true
                                },
                            ) { Icon(Icons.Filled.Edit, contentDescription = "编辑") }
                            IconButton(onClick = { deletingSkill = skill }) { Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Cinnabar) }
                        }
                    }
                }
            }
        }
    }

    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = { Text(if (editingSkill != null) "编辑 Skill" else "新增 Skill", style = MaterialTheme.typography.titleLarge, color = Ink) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedTextField(formTitle, { formTitle = it }, label = { Text("标题 *") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formDesc, { formDesc = it }, label = { Text("描述") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                    OutlinedTextField(formPromptType, { formPromptType = it }, label = { Text("提示词类型") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formPrompt, { formPrompt = it }, label = { Text("提示词") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                    OutlinedTextField(formUrl, { formUrl = it }, label = { Text("链接 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(formAuthor, { formAuthor = it }, label = { Text("作者") }, singleLine = true, modifier = Modifier.weight(1f))
                        OutlinedTextField(formBadge, { formBadge = it }, label = { Text("徽章") }, singleLine = true, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(formTags, { formTags = it }, label = { Text("标签（逗号分隔）") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formPreviewUrl, { formPreviewUrl = it }, label = { Text("预览图 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formMediaUrl, { formMediaUrl = it }, label = { Text("媒体 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formIconUrl, { formIconUrl = it }, label = { Text("图标 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(formMode, { formMode = it }, label = { Text("模式（url / apk）") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (formTitle.isBlank()) { onShowToast("请填写 Skill 标题"); return@TextButton }
                        onSaveSkill(editingSkill, formTitle, formDesc, formPromptType, formPrompt, formUrl, formAuthor, formBadge, formTags, formPreviewUrl, formMediaUrl, formIconUrl, formMode)
                        dialogOpen = false
                    },
                ) { Text("保存", color = Cinnabar) }
            },
            dismissButton = { TextButton(onClick = { dialogOpen = false }) { Text("取消") } },
        )
    }

    deletingSkill?.let { d ->
        ConfirmDeleteDialog(
            open = true,
            title = "删除 Skill",
            itemName = d.title,
            extraWarning = "删除后本体将同步移除该 Skill",
            onConfirm = { onDeleteSkill(d); deletingSkill = null },
            onDismiss = { deletingSkill = null },
        )
    }
}

@Composable
fun TextManagementScreen(
    uiState: AdminUiState,
    onSaveText: (String, String, Boolean) -> Unit,
    onDeleteText: (TextItem) -> Unit,
    onShowToast: (String) -> Unit,
) {
    var keyword by remember { mutableStateOf("") }
    var dialogOpen by remember { mutableStateOf(false) }
    var isNewText by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<TextItem?>(null) }
    var deletingItem by remember { mutableStateOf<TextItem?>(null) }
    var formKey by remember { mutableStateOf("") }
    var formContent by remember { mutableStateOf("") }

    val filtered = uiState.texts.filter {
        val kw = keyword.trim()
        kw.isEmpty() || it.key.contains(kw, ignoreCase = true) || it.title.contains(kw, ignoreCase = true) || it.content.contains(kw, ignoreCase = true)
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(
            title = "UI 文本管理",
            description = "共 ${uiState.texts.size} 条文本 · 本体界面文案实时同步",
            actions = {
                Button(
                    onClick = {
                        editingItem = null
                        isNewText = true
                        formKey = ""
                        formContent = ""
                        dialogOpen = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "新增", modifier = Modifier.width(16.dp))
                    Text("新增", modifier = Modifier.padding(start = 4.dp))
                }
            },
        )

        SearchBarField(
            value = keyword,
            onValueChange = { keyword = it },
            placeholder = "搜索 key / 标题 / 内容…",
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        if (filtered.isEmpty()) {
            EmptyStateView(title = "没有找到文本", description = "换个关键词，或点击右上角「新增」")
        } else {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                filtered.forEach { item ->
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable {
                            editingItem = item
                            isNewText = false
                            formKey = item.key
                            formContent = item.content
                            dialogOpen = true
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSoft,
                        border = BorderStroke(1.dp, Mist),
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    item.key,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Cinnabar,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                if (item.title.isNotEmpty()) {
                                    Text(item.title, style = MaterialTheme.typography.titleMedium, color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Text(
                                    item.content,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InkBlack.copy(alpha = 0.6f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            IconButton(onClick = { deletingItem = item }) { Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Cinnabar) }
                        }
                    }
                }
            }
        }
    }

    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = { Text(if (isNewText) "新增文本" else "编辑文本", style = MaterialTheme.typography.titleLarge, color = Ink) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = formKey,
                        onValueChange = { formKey = it },
                        label = { Text("Key *") },
                        singleLine = true,
                        enabled = isNewText,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = formContent,
                        onValueChange = { formContent = it },
                        label = { Text("内容 *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (formKey.isBlank()) { onShowToast("请填写 Key"); return@TextButton }
                        onSaveText(formKey, formContent, isNewText)
                        dialogOpen = false
                    },
                ) { Text("保存", color = Cinnabar) }
            },
            dismissButton = { TextButton(onClick = { dialogOpen = false }) { Text("取消") } },
        )
    }

    deletingItem?.let { d ->
        ConfirmDeleteDialog(
            open = true,
            title = "删除文本",
            itemName = d.key,
            extraWarning = "本体对应位置的文案将恢复默认",
            onConfirm = { onDeleteText(d); deletingItem = null },
            onDismiss = { deletingItem = null },
        )
    }
}
