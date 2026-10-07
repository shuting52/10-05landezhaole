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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.ConsoleConfig
import com.example.model.IpMonitorConfig
import com.example.model.MarqueeConfig
import com.example.model.SplashConfig
import com.example.model.ThemeKitConfig
import com.example.model.ToolItem
import com.example.model.UpdateDialogConfig
import com.example.model.WelcomeConfig
import com.example.ui.components.PageHeader
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Ink
import com.example.ui.theme.Mist
import com.example.ui.theme.Paper
import com.example.ui.theme.PaperSoft
import com.example.viewmodel.AdminUiState

// ============================================================================
// 模块配置 / 主题工具箱（Kotlin Compose 重写版）
// ============================================================================

@Composable
fun AppModulesScreen(
    uiState: AdminUiState,
    onSaveSplash: (SplashConfig) -> Unit,
    onSaveWelcome: (WelcomeConfig) -> Unit,
    onSaveMarquee: (MarqueeConfig) -> Unit,
    onSaveUpdateDialogAndVersion: (UpdateDialogConfig, String, Int, Boolean, String, String) -> Unit,
    onSaveMiscModules: (ConsoleConfig, IpMonitorConfig, String, List<ToolItem>) -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("开屏", "欢迎", "跑马灯", "更新弹窗", "杂项")

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(title = "模块配置", description = "开屏 / 欢迎 / 跑马灯 / 更新弹窗 / 杂项")
        TabRow(selectedTabIndex = selectedTab, containerColor = PaperSoft) {
            tabs.forEachIndexed { i, t ->
                Tab(selected = selectedTab == i, onClick = { selectedTab = i }, text = { Text(t) })
            }
        }
        when (selectedTab) {
            0 -> SplashEditorCard(uiState.splashConfig, onSaveSplash)
            1 -> WelcomeEditorCard(uiState.welcomeConfig, onSaveWelcome)
            2 -> MarqueeEditorCard(uiState.marqueeConfig, onSaveMarquee)
            3 -> UpdateDialogEditorCard(uiState, onSaveUpdateDialogAndVersion)
            else -> MiscModulesEditorCard(uiState, onSaveMiscModules)
        }
    }
}

@Composable
private fun SplashEditorCard(config: SplashConfig, onSave: (SplashConfig) -> Unit) {
    var type by remember { mutableStateOf(config.type) }
    var mediaUrl by remember { mutableStateOf(config.mediaUrl) }
    var duration by remember { mutableStateOf(config.durationSeconds.toString()) }
    var bgColor by remember { mutableStateOf(config.bgColor) }
    var customHtml by remember { mutableStateOf(config.customHtml) }
    EditorCard(title = "开屏配置", desc = "本体启动时的开屏展示（视频 / 图片 / HTML）") {
        OutlinedTextField(type, { type = it }, label = { Text("类型（video / image / html）") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(mediaUrl, { mediaUrl = it }, label = { Text("媒体 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(duration, { duration = it }, label = { Text("时长（秒）") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(bgColor, { bgColor = it }, label = { Text("背景色") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(customHtml, { customHtml = it }, label = { Text("自定义 HTML") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        SaveButton { onSave(SplashConfig(type, mediaUrl, duration.toIntOrNull() ?: 0, bgColor, customHtml)) }
    }
}

@Composable
private fun WelcomeEditorCard(config: WelcomeConfig, onSave: (WelcomeConfig) -> Unit) {
    var enabled by remember { mutableStateOf(config.enabled) }
    var title by remember { mutableStateOf(config.title) }
    var welcomeText by remember { mutableStateOf(config.welcomeText) }
    var content by remember { mutableStateOf(config.content) }
    var imageUrl by remember { mutableStateOf(config.imageUrl) }
    var buttonText by remember { mutableStateOf(config.buttonText) }
    var ratio by remember { mutableStateOf(config.ratio) }
    EditorCard(title = "欢迎页配置", desc = "本体首次启动的欢迎弹窗") {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("启用欢迎页", style = MaterialTheme.typography.labelLarge, color = Ink)
            Spacer(Modifier.weight(1f))
            Switch(checked = enabled, onCheckedChange = { enabled = it })
        }
        OutlinedTextField(title, { title = it }, label = { Text("标题") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(welcomeText, { welcomeText = it }, label = { Text("欢迎语") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(content, { content = it }, label = { Text("内容") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text("图片 URL") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(buttonText, { buttonText = it }, label = { Text("按钮文字") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(ratio, { ratio = it }, label = { Text("比例（如 3:2）") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        SaveButton { onSave(WelcomeConfig(enabled, title, welcomeText, content, imageUrl, buttonText, ratio)) }
    }
}

@Composable
private fun MarqueeEditorCard(config: MarqueeConfig, onSave: (MarqueeConfig) -> Unit) {
    var enabled by remember { mutableStateOf(config.enabled) }
    var defaultText by remember { mutableStateOf(config.defaultText) }
    var icon by remember { mutableStateOf(config.icon) }
    var segmentsText by remember { mutableStateOf(config.segments.joinToString("\n")) }
    EditorCard(title = "跑马灯配置", desc = "本体顶部的滚动公告，每行一段") {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("启用跑马灯", style = MaterialTheme.typography.labelLarge, color = Ink)
            Spacer(Modifier.weight(1f))
            Switch(checked = enabled, onCheckedChange = { enabled = it })
        }
        OutlinedTextField(defaultText, { defaultText = it }, label = { Text("默认文本") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(icon, { icon = it }, label = { Text("图标") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(segmentsText, { segmentsText = it }, label = { Text("分段（每行一段）") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        SaveButton { onSave(MarqueeConfig(enabled, defaultText, icon, segmentsText.lines().map { it.trim() }.filter { it.isNotEmpty() })) }
    }
}

@Composable
private fun UpdateDialogEditorCard(uiState: AdminUiState, onSave: (UpdateDialogConfig, String, Int, Boolean, String, String) -> Unit) {
    val cfg = uiState.updateDialogConfig
    var title by remember { mutableStateOf(cfg.title) }
    var confirmText by remember { mutableStateOf(cfg.confirmText) }
    var cancelText by remember { mutableStateOf(cfg.cancelText) }
    var changelogText by remember { mutableStateOf(cfg.changelog.joinToString("\n")) }
    var customCss by remember { mutableStateOf(cfg.customCss) }
    var customHtml by remember { mutableStateOf(cfg.customHtml) }
    var vName by remember { mutableStateOf(uiState.versionName) }
    var vCodeStr by remember { mutableStateOf(uiState.versionCode.toString()) }
    var vForce by remember { mutableStateOf(uiState.versionForce) }
    var vApkUrl by remember { mutableStateOf(uiState.versionApkUrl) }
    var vApkUrlRaw by remember { mutableStateOf(uiState.versionApkUrlRaw) }
    EditorCard(title = "更新弹窗与版本", desc = "本体更新提示（四要素同步，谨慎修改）") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(vName, { vName = it }, label = { Text("版本名") }, singleLine = true, modifier = Modifier.weight(1f))
            OutlinedTextField(vCodeStr, { vCodeStr = it }, label = { Text("版本 code") }, singleLine = true, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(title, { title = it }, label = { Text("弹窗标题") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(confirmText, { confirmText = it }, label = { Text("确认按钮") }, singleLine = true, modifier = Modifier.weight(1f))
            OutlinedTextField(cancelText, { cancelText = it }, label = { Text("取消按钮") }, singleLine = true, modifier = Modifier.weight(1f))
        }
        OutlinedTextField(changelogText, { changelogText = it }, label = { Text("更新日志（每行一条）") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        OutlinedTextField(vApkUrl, { vApkUrl = it }, label = { Text("APK 直链") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(vApkUrlRaw, { vApkUrlRaw = it }, label = { Text("APK raw 链接") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("强制更新（不可取消）", style = MaterialTheme.typography.labelLarge, color = Ink)
            Spacer(Modifier.weight(1f))
            Switch(checked = vForce, onCheckedChange = { vForce = it })
        }
        OutlinedTextField(customCss, { customCss = it }, label = { Text("自定义 CSS") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(customHtml, { customHtml = it }, label = { Text("自定义 HTML") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        SaveButton {
            onSave(
                UpdateDialogConfig(title, confirmText, cancelText, changelogText.lines().map { it.trim() }.filter { it.isNotEmpty() }, customCss, customHtml),
                vName, vCodeStr.toIntOrNull() ?: 0, vForce, vApkUrl, vApkUrlRaw,
            )
        }
    }
}

@Composable
private fun MiscModulesEditorCard(uiState: AdminUiState, onSave: (ConsoleConfig, IpMonitorConfig, String, List<ToolItem>) -> Unit) {
    val console = uiState.consoleConfig
    val ip = uiState.ipMonitorConfig
    var consoleVer by remember { mutableStateOf(console.version) }
    var consoleCode by remember { mutableStateOf(console.code.toString()) }
    var consoleApk by remember { mutableStateOf(console.apkUrl) }
    var ipEnabled by remember { mutableStateOf(ip.enabled) }
    var ipUrl by remember { mutableStateOf(ip.url) }
    var aiNotice by remember { mutableStateOf(uiState.aiNotice) }
    var toolsText by remember {
        mutableStateOf(uiState.toolsList.joinToString("\n") { "${it.title}|${it.url}|${it.desc}" })
    }
    EditorCard(title = "杂项模块", desc = "控制台自更新 / IP 监控 / AI 公告 / 工具箱") {
        OutlinedTextField(consoleVer, { consoleVer = it }, label = { Text("控制台版本名") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(consoleCode, { consoleCode = it }, label = { Text("控制台版本 code") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(consoleApk, { consoleApk = it }, label = { Text("控制台 APK 链接") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("启用 IP 监控", style = MaterialTheme.typography.labelLarge, color = Ink)
            Spacer(Modifier.weight(1f))
            Switch(checked = ipEnabled, onCheckedChange = { ipEnabled = it })
        }
        OutlinedTextField(ipUrl, { ipUrl = it }, label = { Text("IP 监控接口") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(aiNotice, { aiNotice = it }, label = { Text("AI 公告") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(toolsText, { toolsText = it }, label = { Text("工具箱（每行 标题|链接|描述）") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        SaveButton {
            val tools = toolsText.lines().mapNotNull { line ->
                val parts = line.trim().split("|")
                if (parts.isEmpty() || parts[0].isBlank()) null
                else ToolItem(id = "tool_${parts[0].hashCode()}", title = parts[0], url = parts.getOrElse(1) { "" }, desc = parts.getOrElse(2) { "" })
            }
            onSave(
                ConsoleConfig(consoleVer, consoleCode.toIntOrNull() ?: 0, consoleApk),
                IpMonitorConfig(ipEnabled, ipUrl),
                aiNotice,
                tools,
            )
        }
    }
}

/** 主题工具箱：全局 CSS 编辑器 */
@Composable
fun ThemeKitScreen(uiState: AdminUiState, onSaveThemeKit: (ThemeKitConfig) -> Unit) {
    val cfg = uiState.themeKitConfig
    var appBarCss by remember { mutableStateOf(cfg.appBarCss) }
    var bottomBarCss by remember { mutableStateOf(cfg.bottomBarCss) }
    var buttonCss by remember { mutableStateOf(cfg.buttonCss) }
    var cardCss by remember { mutableStateOf(cfg.cardCss) }
    var dialogCss by remember { mutableStateOf(cfg.dialogCss) }
    var searchCss by remember { mutableStateOf(cfg.searchCss) }
    var splashCss by remember { mutableStateOf(cfg.splashCss) }
    var settingsPageCss by remember { mutableStateOf(cfg.settingsPageCss) }
    var statusBarCss by remember { mutableStateOf(cfg.statusBarCss) }
    var globalCss by remember { mutableStateOf(cfg.globalCss) }
    var customThemeCss by remember { mutableStateOf(cfg.customThemeCss) }
    var customThemeHtml by remember { mutableStateOf(cfg.customThemeHtml) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        PageHeader(title = "主题工具箱", description = "本体各组件 CSS 覆盖（高级）")
        EditorCard(title = "CSS 覆盖", desc = "留空表示使用默认样式") {
            CssField("顶栏", appBarCss) { appBarCss = it }
            CssField("底栏", bottomBarCss) { bottomBarCss = it }
            CssField("按钮", buttonCss) { buttonCss = it }
            CssField("卡片", cardCss) { cardCss = it }
            CssField("弹窗", dialogCss) { dialogCss = it }
            CssField("搜索", searchCss) { searchCss = it }
            CssField("开屏", splashCss) { splashCss = it }
            CssField("设置页", settingsPageCss) { settingsPageCss = it }
            CssField("状态栏", statusBarCss) { statusBarCss = it }
            CssField("全局", globalCss) { globalCss = it }
            CssField("自定义主题 CSS", customThemeCss) { customThemeCss = it }
            CssField("自定义主题 HTML", customThemeHtml) { customThemeHtml = it }
            SaveButton {
                onSaveThemeKit(
                    ThemeKitConfig(
                        appBarCss = appBarCss, bottomBarCss = bottomBarCss, buttonCss = buttonCss,
                        cardCss = cardCss, dialogCss = dialogCss, searchCss = searchCss,
                        splashCss = splashCss, settingsPageCss = settingsPageCss, statusBarCss = statusBarCss,
                        globalCss = globalCss, customThemeCss = customThemeCss, customThemeHtml = customThemeHtml,
                    )
                )
            }
        }
    }
}

@Composable
private fun CssField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        minLines = 2,
    )
}

/** 编辑器卡片外壳：标题 + 描述 + 表单内容 + 保存按钮 */
@Composable
private fun EditorCard(
    title: String,
    desc: String,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(14.dp),
        color = PaperSoft,
        border = BorderStroke(1.dp, Mist),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = Ink)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = Ink.copy(alpha = 0.6f))
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SaveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Cinnabar, contentColor = Paper),
    ) {
        Text("保存", style = MaterialTheme.typography.labelLarge)
    }
}
