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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.FullSettingsConfig
import com.example.ui.components.PageHeader
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Ink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.Mist
import com.example.ui.theme.Paper
import com.example.ui.theme.PaperSoft
import com.example.viewmodel.AdminUiState

// ============================================================================
// 设置页（Kotlin Compose 重写版）：连接 / 发布 / 基础设置 / 通知 / 完整设置
// ============================================================================

@Composable
fun SettingsScreen(
    uiState: AdminUiState,
    onUpdateGithubInputs: (String, String, String) -> Unit,
    onConnectGithub: () -> Unit,
    onRefreshAdmin: () -> Unit,
    onApplyToDevice: () -> Unit,
    onPublishRelease: (String) -> Unit,
    onUpdateBasicSettingsInputs: (String, String) -> Unit,
    onSaveBasicSettings: () -> Unit,
    onSaveFullSettings: (FullSettingsConfig) -> Unit,
    onToggleNotification: (String, Boolean) -> Unit,
) {
    var token by remember { mutableStateOf(uiState.githubToken) }
    var owner by remember { mutableStateOf(uiState.githubOwner) }
    var repo by remember { mutableStateOf(uiState.githubRepo) }
    var appName by remember { mutableStateOf(uiState.appName) }
    var slogan by remember { mutableStateOf(uiState.slogan) }

    // 完整设置字段
    var logoUrl by remember { mutableStateOf(uiState.fullSettings.logoUrl) }
    var packageName by remember { mutableStateOf(uiState.fullSettings.packageName) }
    var aboutText by remember { mutableStateOf(uiState.fullSettings.aboutText) }
    var feedbackEmail by remember { mutableStateOf(uiState.fullSettings.feedbackEmail) }
    var officialWebsite by remember { mutableStateOf(uiState.fullSettings.officialWebsite) }
    var qqGroupUin by remember { mutableStateOf(uiState.fullSettings.qqGroupUin) }
    var qqGroupUrl by remember { mutableStateOf(uiState.fullSettings.qqGroupUrl) }
    var contactQQ by remember { mutableStateOf(uiState.fullSettings.contactQQ) }
    var contactWechat by remember { mutableStateOf(uiState.fullSettings.contactWechat) }
    var contactAlipay by remember { mutableStateOf(uiState.fullSettings.contactAlipay) }
    var securityEnabled by remember { mutableStateOf(uiState.fullSettings.securityEnabled) }
    var securityExpectedSha by remember { mutableStateOf(uiState.fullSettings.securityExpectedSha) }
    var serverShutdownEnabled by remember { mutableStateOf(uiState.fullSettings.serverShutdownEnabled) }
    var serverShutdownNotice by remember { mutableStateOf(uiState.fullSettings.serverShutdownNotice) }

    var confirmReleaseOpen by remember { mutableStateOf(false) }
    var releaseNote by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        // ========== GitHub 连接 ==========
        PageHeader(title = "GitHub 连接", description = "控制台通过 Token 读写云端 admin-data.json")
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(14.dp),
            color = PaperSoft,
            border = BorderStroke(1.dp, Mist),
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it; onUpdateGithubInputs(token, owner, repo) },
                    label = { Text("GitHub Token") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = owner,
                        onValueChange = { owner = it; onUpdateGithubInputs(token, owner, repo) },
                        label = { Text("Owner") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = repo,
                        onValueChange = { repo = it; onUpdateGithubInputs(token, owner, repo) },
                        label = { Text("Repo") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                }
                Button(
                    onClick = onConnectGithub,
                    enabled = !uiState.isConnecting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                ) {
                    Text(if (uiState.isConnecting) "连接中…" else "保存并连接", style = MaterialTheme.typography.labelLarge)
                }
                if (uiState.connState.name.isNotEmpty()) {
                    Text(
                        "连接状态：${uiState.connState.name} · ${uiState.errorMsg.ifBlank { "已连接" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkBlack.copy(alpha = 0.6f),
                    )
                }
            }
        }

        // ========== 操作区 ==========
        PageHeader(title = "云端操作", description = "刷新 / 应用 / 发布新版本")
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(
                onClick = onRefreshAdmin,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Mist),
                modifier = Modifier.weight(1f),
            ) { Text("刷新数据", style = MaterialTheme.typography.labelLarge) }
            OutlinedButton(
                onClick = onApplyToDevice,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Mist),
                modifier = Modifier.weight(1f),
            ) { Text("✅ 应用并实时同步", style = MaterialTheme.typography.labelLarge) }
            Button(
                onClick = { confirmReleaseOpen = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                modifier = Modifier.weight(1f),
            ) { Text("发布版本", style = MaterialTheme.typography.labelLarge) }
        }

        // ========== 基础设置 ==========
        PageHeader(title = "基础设置", description = "App 名称与标语（内容同步模式生效）")
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(14.dp),
            color = PaperSoft,
            border = BorderStroke(1.dp, Mist),
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = appName,
                    onValueChange = { appName = it; onUpdateBasicSettingsInputs(appName, slogan) },
                    label = { Text("App 名称") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = slogan,
                    onValueChange = { slogan = it; onUpdateBasicSettingsInputs(appName, slogan) },
                    label = { Text("Slogan") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    onClick = onSaveBasicSettings,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                ) { Text("保存基础设置", style = MaterialTheme.typography.labelLarge) }
            }
        }

        // ========== 通知开关 ==========
        PageHeader(title = "通知设置", description = "控制台操作提醒")
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SettingToggleRow("审核提醒", "卡片审核状态变化时通知", uiState.notifReview) { onToggleNotification("review", it) }
            SettingToggleRow("下载提醒", "有用户下载资源时通知", uiState.notifDownload) { onToggleNotification("download", it) }
            SettingToggleRow("每周汇总", "每周发送运营数据汇总", uiState.notifWeekly) { onToggleNotification("weekly", it) }
        }

        // ========== 完整设置 ==========
        PageHeader(title = "完整设置", description = "品牌信息 / 联系方式 / 安全策略")
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(14.dp),
            color = PaperSoft,
            border = BorderStroke(1.dp, Mist),
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(logoUrl, { logoUrl = it }, label = { Text("Logo URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(packageName, { packageName = it }, label = { Text("包名") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(aboutText, { aboutText = it }, label = { Text("关于文本") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                OutlinedTextField(feedbackEmail, { feedbackEmail = it }, label = { Text("反馈邮箱") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(officialWebsite, { officialWebsite = it }, label = { Text("官网") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(qqGroupUin, { qqGroupUin = it }, label = { Text("QQ 群号") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(qqGroupUrl, { qqGroupUrl = it }, label = { Text("QQ 群链接") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(contactQQ, { contactQQ = it }, label = { Text("联系 QQ") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(contactWechat, { contactWechat = it }, label = { Text("联系微信") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(contactAlipay, { contactAlipay = it }, label = { Text("联系支付宝") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                SettingToggleRow("安全校验", "开启 APK 安全校验（SHA 白名单）", securityEnabled) { securityEnabled = it }
                OutlinedTextField(securityExpectedSha, { securityExpectedSha = it }, label = { Text("期望 SHA") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                SettingToggleRow("停机维护", "显示停机维护公告", serverShutdownEnabled) { serverShutdownEnabled = it }
                OutlinedTextField(serverShutdownNotice, { serverShutdownNotice = it }, label = { Text("停机公告") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                Button(
                    onClick = {
                        onSaveFullSettings(
                            FullSettingsConfig(
                                appName = appName, slogan = slogan, logoUrl = logoUrl, packageName = packageName,
                                aboutText = aboutText, feedbackEmail = feedbackEmail, officialWebsite = officialWebsite,
                                qqGroupUrl = qqGroupUrl, qqGroupUin = qqGroupUin, contactQQ = contactQQ,
                                contactWechat = contactWechat, contactAlipay = contactAlipay,
                                securityEnabled = securityEnabled, securityExpectedSha = securityExpectedSha,
                                serverShutdownEnabled = serverShutdownEnabled, serverShutdownNotice = serverShutdownNotice,
                            )
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
                ) { Text("保存完整设置", style = MaterialTheme.typography.labelLarge) }
            }
        }
    }

    // ========== 发布确认弹窗 ==========
    if (confirmReleaseOpen) {
        AlertDialog(
            onDismissRequest = { confirmReleaseOpen = false },
            title = {
                Text("⚠️ 确认发布新版本 v${uiState.versionName}", style = MaterialTheme.typography.titleLarge, color = Cinnabar)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("版本将自动递增，发布后所有用户会收到更新弹窗。", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(
                        value = releaseNote,
                        onValueChange = { releaseNote = it },
                        label = { Text("更新说明（可选）") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onPublishRelease(releaseNote)
                        confirmReleaseOpen = false
                    },
                    modifier = Modifier.testTag("confirm_publish_release_btn"),
                ) { Text("确认发布", color = Cinnabar) }
            },
            dismissButton = {
                TextButton(onClick = { confirmReleaseOpen = false }) { Text("取消") }
            },
        )
    }
}

/** 设置项开关行 */
@Composable
fun SettingToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = PaperSoft,
        border = BorderStroke(1.dp, Mist),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.labelLarge, color = Ink)
                if (description.isNotEmpty()) {
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        color = InkBlack.copy(alpha = 0.6f),
                    )
                }
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
