package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import com.example.data.remote.SettingsDto
import com.example.data.remote.UpdateDialogDto
import com.example.data.remote.VersionDto
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.ui.components.AppRatingDialog
import com.example.ui.components.AppUpdateDialog
import com.example.ui.components.UBadge
import com.example.ui.components.UBadgeVariant
import com.example.ui.components.UListContainer
import com.example.ui.components.UListRow
import com.example.ui.components.UiverseSettingsRow
import com.example.ui.components.UiverseChevron
import com.example.ui.components.QuadrantCardGrid
import com.example.ui.components.QuadrantCardItem
import com.example.ui.components.QuadrantPosition
import com.example.ui.components.DrawContactAuthorIcon
import com.example.ui.components.DrawOfficialWebsiteGroupIcon
import com.example.ui.components.DrawSoftwareFeedbackIcon
import com.example.ui.components.DrawOfficialChatGroupIcon
import com.example.ui.components.DrawAppRatingIcon
import com.example.ui.components.DrawShareAppIcon
import com.example.ui.components.DrawCheckUpdateIcon
import com.example.ui.components.ComponentThemeResolver
import com.example.ui.components.LocalComponentThemes
import com.example.ui.components.DrawThemeAppearanceIcon
import com.example.ui.components.OfficialWebsiteDialog
import com.example.ui.components.ShareSoftwareDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.NavViewModel
import com.example.ui.theme.AeroCard
import com.example.ui.theme.AeroSectionTitle
import com.example.ui.theme.AeroSegmented
import com.example.ui.theme.AeroSwitch
import com.example.ui.theme.AeroSlider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CuteLemon
import com.example.ui.theme.CutePeach
import com.example.ui.theme.CutePink
import com.example.ui.theme.LocalThemeUiColors
import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.SupportAgent
import com.example.ui.components.FeedbackDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.text.style.TextAlign

const val OFFICIAL_QQ_GROUP_URL =
    "https://qun.qq.com/universal-share/share?ac=1&authKey=gtnBoTi8HEzXQAF9x40Y5GYQtubkWu4pGDJg7OuNQte9oz3sXiFonGqZaUXxjffu&busi_data=eyJncm91cENvZGUiOiI0MzkyMTEzNDciLCJ0b2tlbiI6IkVxeXJDb0tyVjM3Y0VIRmhZQ3M5eDg4VW5MYWU0RW4ybVlSRlBlS2ozQXRxanB5V2ZtNzNHMlRIa2ZRd0VTQnUiLCJ1aW4iOiIzMDc3Nzk1MjMifQ%3D%3D&data=QnUzn164u21Cu1dG7vAVYJqU_4hw0COArsGrrBOIc0vxu7ES6gOJcYyrpu2JgkVs-y3X0ZUGZb_nPBJsBTRccQ&svctype=4&tempid=h5_group_info"

@Composable
fun CuteCartoonLiquidGlassDialog(
    onDismissRequest: () -> Unit,
    title: String,
    confirmButtonText: String = "知道了",
    onConfirm: () -> Unit = onDismissRequest,
    content: @Composable () -> Unit
) {
    com.example.ui.components.LiquidGlassDialogShell(
        onDismissRequest = onDismissRequest,
        title = title,
        subtitle = "懒得找了 · 纯净安全保障",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            content()

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalThemeUiColors.current.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(confirmButtonText, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
            }
        }
    }
}

@Composable
fun SettingsScreen(
    cloudUpdate: UpdateDialogDto? = null,
    cloudVersion: VersionDto? = null,
    cloudSettings: SettingsDto? = null,
    onCheckUpdate: (suspend () -> Pair<Boolean, VersionDto?>)? = null,
    modifier: Modifier = Modifier,
    // v1.2.6：设置页标题/副标题（控制台 UI 文本可云端覆盖）
    uiText: com.example.data.remote.UiTextDto? = null,
    // 2026-10-24：恢复外观设置入口（背景/底栏/缩放）
    viewModel: NavViewModel? = null,
    onPickBgImage: () -> Unit = {},
    onPickBgVideo: () -> Unit = {},
    onClearBg: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var activeDialogType by remember { mutableStateOf<String?>(null) }
    var isCheckingUpdate by remember { mutableStateOf(false) }
    // 检查更新反馈弹窗：无新版本时点击「检查更新」弹出独立弹窗「已是最新版本」
    var showLatestVersionDialog by remember { mutableStateOf(false) }
    // 2026-10-24：外观与背景独立窗口（主题/背景/底栏/缩放集中到弹窗内）
    var showAppearanceDialog by remember { mutableStateOf(false) }

    // 云端新版本检测：云端 versionCode 大于本地时视为有新版本
    val hasNewCloudVersion = (cloudVersion?.code ?: 0) > com.example.BuildConfig.VERSION_CODE

    // v1.1.10：设置页组件级主题（控制台「主题工具箱」settingsPage 组件实时生效，
    // 控制台更改后点击「应用」，本体 5 秒轮询自动拉取并应用）
    val settingsComp = ComponentThemeResolver.resolve(LocalComponentThemes.current, "settingsPage")
    val settingsShape = RoundedCornerShape(settingsComp?.cornerRadius ?: 20.dp)

    // 自动检测：进入设置页无需手动点击，自动获取云端仓库最新版本状态并实时刷新
    LaunchedEffect(Unit) {
        onCheckUpdate?.invoke()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("settings_screen")
    ) {
        // v1.1.10：设置页整体主题容器（液态玻璃折射半透胶囊）
        val customBg = settingsComp?.backgroundColor
        val customBorder = settingsComp?.borderColor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(settingsShape)
                .background(
                    if (customBg != null) {
                        Brush.linearGradient(listOf(customBg, customBg))
                    } else {
                        // v1.4.15：跟随主题卡片渐变
                        val cardGrad = LocalThemeUiColors.current.cardGradient
                        Brush.linearGradient(
                            if (cardGrad.size >= 2) cardGrad
                            else listOf(Color.White.copy(alpha = 0.44f), Color.White.copy(alpha = 0.48f))
                        )
                    }
                )
                .border(
                    width = settingsComp?.borderWidth ?: 1.3.dp,
                    brush = if (customBorder != null) {
                        Brush.linearGradient(listOf(customBorder, customBorder))
                    } else {
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.94f),
                                Color(0xFFD8B4FE).copy(alpha = 0.45f),
                                Color.White.copy(alpha = 0.88f)
                            )
                        )
                    },
                    shape = settingsShape
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
        // Header（v1.2.2：图标/标题颜色跟随当前主题色，设置版块与软件主题同步）
        val themeUi = LocalThemeUiColors.current
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                themeUi.primary,
                                themeUi.secondary
                            )
                        )
                    )
                    .border(1.dp, themeUi.secondary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = uiText?.settingsTitle?.ifBlank { "设置" } ?: "设置",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        ),
                        color = themeUi.primary
                    )
                }
            }
        }
        } // v1.1.10 settingsPage 主题容器闭合

        Spacer(modifier = Modifier.height(16.dp))

        // 1. 官方社群矩阵（联系作者、官网群、软件反馈、官方群聊）
        Text(
            text = "🌐 官方社群矩阵",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = LocalThemeUiColors.current.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        QuadrantCardGrid(
            items = listOf(
                QuadrantCardItem(
                    title = "联系作者",
                    position = QuadrantPosition.TOP_LEFT,
                    defaultBgColor = Color(0xFFFFFCFC),
                    activeBgColor = Color(0xFFCC39A4),
                    defaultIconColor = Color(0xFFFF1717),
                    spotShadowColor = Color(0x700AE96E),
                    iconDrawer = { color -> DrawContactAuthorIcon(color) },
                    onClick = { activeDialogType = "contact_author" }
                ),
                QuadrantCardItem(
                    title = "官网群",
                    position = QuadrantPosition.TOP_RIGHT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFFFF9811),
                    defaultIconColor = Color(0xFFF34500),
                    spotShadowColor = Color(0x60F35C05),
                    iconDrawer = { color -> DrawOfficialWebsiteGroupIcon(color) },
                    onClick = {
                        val site = cloudSettings?.officialWebsite?.trim()
                        if (!site.isNullOrBlank()) {
                            try {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(site)).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                })
                            } catch (_: Exception) {
                                Toast.makeText(context, "无法打开链接: $site", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            activeDialogType = "official_website"
                        }
                    }
                ),
                QuadrantCardItem(
                    title = "软件反馈",
                    position = QuadrantPosition.BOTTOM_LEFT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFFFC0202),
                    defaultIconColor = Color(0xFF24292E),
                    spotShadowColor = Color(0x60FF4F09),
                    iconDrawer = { color -> DrawSoftwareFeedbackIcon(color) },
                    onClick = { activeDialogType = "feedback_bug" }
                ),
                QuadrantCardItem(
                    title = "官方群聊",
                    position = QuadrantPosition.BOTTOM_RIGHT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFF750000),
                    defaultIconColor = Color(0xFF8C9EFF),
                    spotShadowColor = Color(0x60FD2626),
                    iconDrawer = { color -> DrawOfficialChatGroupIcon(color) },
                    onClick = {
                        openQqGroup(
                            context,
                            groupUrl = cloudSettings?.qqGroupUrl?.ifBlank { OFFICIAL_QQ_GROUP_URL } ?: OFFICIAL_QQ_GROUP_URL,
                            groupUin = cloudSettings?.qqGroupUin?.ifBlank { "439211347" } ?: "439211347"
                        )
                    }
                )
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. 常用功能与版本更新（应用评分、分享软件、检查更新、主题外观）
        Text(
            text = "✨ 锦囊与更新",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = LocalThemeUiColors.current.primary,
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        // 独立动态警告标识：检测到有新版本时，以醒目呼吸横幅告知用户
        if (hasNewCloudVersion) {
            Surface(
                onClick = { activeDialogType = "update" },
                color = Color(0xFFFDE8E8),
                border = BorderStroke(1.5.dp, Color(0xFFE53935).copy(alpha = 0.45f)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.CloudDownload,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "发现新版本 v${cloudVersion?.name ?: ""}，点击立即更新！",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE53935),
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        QuadrantCardGrid(
            items = listOf(
                QuadrantCardItem(
                    title = "应用评分",
                    position = QuadrantPosition.TOP_LEFT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFFF59E0B),
                    defaultIconColor = Color(0xFFD97706),
                    spotShadowColor = Color(0x60F59E0B),
                    iconDrawer = { color -> DrawAppRatingIcon(color) },
                    onClick = { activeDialogType = "rating" }
                ),
                QuadrantCardItem(
                    title = "分享软件",
                    position = QuadrantPosition.TOP_RIGHT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFF10B981),
                    defaultIconColor = Color(0xFF059669),
                    spotShadowColor = Color(0x6010B981),
                    iconDrawer = { color -> DrawShareAppIcon(color) },
                    onClick = { activeDialogType = "share_software" }
                ),
                QuadrantCardItem(
                    title = if (isCheckingUpdate) "获取中…" else if (hasNewCloudVersion) "有新版更新" else "检查更新",
                    position = QuadrantPosition.BOTTOM_LEFT,
                    defaultBgColor = if (hasNewCloudVersion) Color(0xFFFFF1F1) else Color.White,
                    activeBgColor = Color(0xFF3B82F6),
                    defaultIconColor = if (hasNewCloudVersion) Color(0xFFE53935) else Color(0xFF2563EB),
                    spotShadowColor = Color(0x603B82F6),
                    iconDrawer = { color -> DrawCheckUpdateIcon(color) },
                    onClick = {
                        if (isCheckingUpdate) return@QuadrantCardItem
                        if (onCheckUpdate != null) {
                            coroutineScope.launch {
                                isCheckingUpdate = true
                                val (hasNew, ver) = onCheckUpdate()
                                isCheckingUpdate = false
                                if (hasNew) {
                                    activeDialogType = "update"
                                } else {
                                    showLatestVersionDialog = true
                                }
                            }
                        } else {
                            activeDialogType = "update"
                        }
                    }
                )
            )
        )

        Spacer(modifier = Modifier.height(14.dp))
        // 2026-10-24：外观与背景改为独立窗口（点击入口打开弹窗，内含主题/背景/底栏/缩放）
        if (viewModel != null) {
            Text(
                text = "\uD83C\uDFA8 外观与背景",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = LocalThemeUiColors.current.primary,
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )
            AeroCard {
                SettingsClickableItem(
                    title = "外观与背景",
                    subtitle = "主题风格 · 背景图片/视频 · 底栏样式 · 界面缩放",
                    icon = Icons.Filled.Palette,
                    iconColor = Color(0xFF00A8E8),
                    onClick = { showAppearanceDialog = true }
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Footer Brand Info with Typewriter Effect
        com.example.ui.components.TypewriterFooter(uiText = uiText)
    }

    // 检查更新反馈：无新版本时点击「检查更新」→ 独立弹窗「已是最新版本」（可爱卡通动态液体玻璃状呈现）
    if (showLatestVersionDialog) {
        CuteCartoonLiquidGlassDialog(
            onDismissRequest = { showLatestVersionDialog = false },
            title = "检查更新",
            confirmButtonText = "太棒啦",
            onConfirm = { showLatestVersionDialog = false }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text("🎉", fontSize = 42.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "已是最新版本",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF10B981)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "当前 v${com.example.BuildConfig.VERSION_NAME} 已是最新版本，无需更新～",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }

    // 2026-10-24：外观与背景独立窗口
    if (showAppearanceDialog && viewModel != null) {
        AppearanceDialog(
            viewModel = viewModel,
            onDismiss = { showAppearanceDialog = false },
            onPickBgImage = onPickBgImage,
            onPickBgVideo = onPickBgVideo,
            onClearBg = onClearBg
        )
    }

    // Modal Dialogs for Policies & About Us
    when (activeDialogType) {
        "feedback_bug" -> {
            FeedbackDialog(
                onDismiss = { activeDialogType = null },
                // v1.0.4：读取官方反馈邮箱，修复反馈收不到问题
                cloudSettings = cloudSettings
            )
        }
        "update" -> {
            AppUpdateDialog(
                onDismiss = { activeDialogType = null },
                versionName = "v${cloudVersion?.name ?: "2.0.0"}",
                onUpdateFinished = {
                    Toast.makeText(context, "已成功升级至最新版本 v${cloudVersion?.name ?: "2.0.0"}！", Toast.LENGTH_SHORT).show()
                },
                update = cloudUpdate,
                apkUrl = cloudVersion?.apkUrl?.ifBlank { null },
                forceUpdate = cloudVersion?.force == true,
                // v1.0.16：点击「立即更新」后才开始下载安装（不自动下载）
                autoDownload = false
            )
        }
        "official_website" -> {
            OfficialWebsiteDialog(
                onDismiss = { activeDialogType = null },
                websiteUrl = cloudSettings?.officialWebsite ?: ""
            )
        }
        "rating" -> {
            AppRatingDialog(onDismiss = { activeDialogType = null })
        }
        "share_software" -> {
            ShareSoftwareDialog(onDismiss = { activeDialogType = null })
        }
        "contact_author" -> {
            ContactAuthorDialog(
                context = context,
                cloudSettings = cloudSettings,
                onDismiss = { activeDialogType = null },
                onOpenFeedback = { activeDialogType = "feedback_bug" }
            )
        }
    }
}

@Composable
private fun AppearanceDialog(
    viewModel: NavViewModel,
    onDismiss: () -> Unit,
    onPickBgImage: () -> Unit,
    onPickBgVideo: () -> Unit,
    onClearBg: () -> Unit
) {
    val appearanceState by viewModel.uiState.collectAsStateWithLifecycle()
    com.example.ui.components.LiquidGlassDialogShell(
        onDismissRequest = onDismiss,
        title = "🎨 外观与背景",
        subtitle = "主题风格 · 背景 · 底栏 · 缩放"
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {
            // 主题风格
            AeroSectionTitle(text = "主题风格")
            val themeOptions = com.example.ui.theme.APP_THEMES.map { it.name }
            val themeSelected = com.example.ui.theme.APP_THEMES.indexOfFirst { it.id == appearanceState.appThemeId }.coerceAtLeast(0)
            AeroSegmented(
                options = themeOptions,
                selectedIndex = themeSelected,
                onSelect = { idx ->
                    com.example.ui.theme.APP_THEMES.getOrNull(idx)?.let { viewModel.setAppTheme(it.id) }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = com.example.ui.theme.APP_THEMES.getOrNull(themeSelected)?.colors?.designStyle?.desc ?: "",
                fontSize = 12.sp,
                color = Color(0xFF5B7A90),
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            // 背景图片 / 视频
            AeroSectionTitle(text = "背景图片 / 视频")
            SettingsClickableItem(
                title = "选择背景图片",
                subtitle = if (appearanceState.localBgMediaType == "image") "已设置自定义图片" else "从相册选择",
                icon = Icons.Filled.Image,
                iconColor = Color(0xFF00A8E8),
                onClick = onPickBgImage
            )
            SettingsClickableItem(
                title = "选择背景视频",
                subtitle = if (appearanceState.localBgMediaType == "video") "已设置自定义视频" else "从相册选择",
                icon = Icons.Filled.Videocam,
                iconColor = Color(0xFF7ED957),
                onClick = onPickBgVideo
            )
            if (appearanceState.localBgMediaType == "image" || appearanceState.localBgMediaType == "video") {
                SettingsClickableItem(
                    title = "清除自定义背景",
                    subtitle = "恢复默认主题背景",
                    icon = Icons.Filled.DeleteOutline,
                    iconColor = Color(0xFFC0392B),
                    onClick = onClearBg
                )
            }
            // 底栏样式
            AeroSectionTitle(text = "底栏样式")
            AeroSwitch(
                checked = appearanceState.glassBottomBar,
                onCheckedChange = { viewModel.setGlassBottomBar(it) },
                title = "底栏液态玻璃风格",
                desc = "关闭则使用渐变按钮样式"
            )
            // 界面缩放
            AeroSectionTitle(text = "界面缩放")
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Text(
                    text = "当前 ${(appearanceState.uiScale * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = Color(0xFF5B7A90),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AeroSlider(
                    value = appearanceState.uiScale,
                    onValueChange = { viewModel.setUiScale(it) },
                    valueRange = 0.8f..1.2f
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    // 2026-10-24 Frutiger Aero：fa-row 风格（44px 圆角图标 + 标题 + 副标题 + 箭头）
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 13.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color.White.copy(alpha = 0.9f), Color(0xFFBEE1F5).copy(alpha = 0.7f))
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0A2540))
            if (!subtitle.isNullOrBlank()) {
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF5B7A90)
                )
            }
        }
        Text(
            text = "›",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF0090C9)
        )
    }
}

fun openQqGroup(context: Context, groupUrl: String = OFFICIAL_QQ_GROUP_URL, groupUin: String = "439211347") {
    // v1.0.4：不再自动复制群号，点击「官方交流群」直接唤起 QQ 加群 / 跳转指定群链接
    var launched = false
    try {
        val qqIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin=$groupUin&card_type=group&source=qrcode")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(qqIntent)
        launched = true
        Toast.makeText(context, "正在唤起QQ加入官方群...", Toast.LENGTH_SHORT).show()
    } catch (_: Exception) {
    }

    if (!launched) {
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(groupUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
            Toast.makeText(context, "正在打开官方群...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "打开失败，请手动在QQ中搜索群号: $groupUin", Toast.LENGTH_LONG).show()
        }
    }
}

private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "无法打开链接: $url", Toast.LENGTH_SHORT).show()
    }
}

private fun copyText(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    Toast.makeText(context, "已复制 $label 到剪贴板", Toast.LENGTH_SHORT).show()
}

@Composable
private fun ContactAuthorDialog(
    context: Context,
    onDismiss: () -> Unit,
    onOpenFeedback: () -> Unit = {},
    cloudSettings: SettingsDto? = null
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("微信投喂", "QQ", "微信")

    // 联系作者二维码「写死」逻辑：控制台上传后持久化到本地，
    // 只要不重新上传新的二维码，后续更新都不会回退到内置图或替换为旧图。
    val qrPrefs = remember { context.getSharedPreferences("lzdz_contact_qr", Context.MODE_PRIVATE) }
    var qqQr by remember { mutableStateOf(qrPrefs.getString("qr_qq", "") ?: "") }
    var wxQr by remember { mutableStateOf(qrPrefs.getString("qr_wechat", "") ?: "") }
    var aliQr by remember { mutableStateOf(qrPrefs.getString("qr_alipay", "") ?: "") }
    LaunchedEffect(cloudSettings?.contactQQ, cloudSettings?.contactWechat, cloudSettings?.contactAlipay) {
        // 云端上传了新二维码 → 更新本地缓存（写死）；云端为空 → 保留上次缓存的二维码
        cloudSettings?.contactQQ?.takeIf { it.isNotBlank() }?.let {
            qrPrefs.edit().putString("qr_qq", it).apply()
            qqQr = it
        }
        cloudSettings?.contactWechat?.takeIf { it.isNotBlank() }?.let {
            qrPrefs.edit().putString("qr_wechat", it).apply()
            wxQr = it
        }
        cloudSettings?.contactAlipay?.takeIf { it.isNotBlank() }?.let {
            qrPrefs.edit().putString("qr_alipay", it).apply()
            aliQr = it
        }
    }

    fun openAlipay() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi/startapp?saId=10000007")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val launchIntent = context.packageManager.getLaunchIntentForPackage("com.eg.android.AlipayGphone")
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                } else {
                    Toast.makeText(context, "请先保存或截屏二维码，在支付宝中扫码投喂", Toast.LENGTH_LONG).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(context, "请截屏二维码，打开支付宝扫一扫", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openQq() {
        try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage("com.tencent.mobileqq")
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
            } else {
                Toast.makeText(context, "未检测到QQ，请先截屏二维码在QQ中扫一扫添加好友", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "打开QQ失败，请截屏后扫一扫", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWeChat() {
        try {
            val launchIntent = context.packageManager.getLaunchIntentForPackage("com.tencent.mm")
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
            } else {
                Toast.makeText(context, "未检测到微信，请先截屏二维码在微信中扫一扫添加好友", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "打开微信失败，请截屏后扫一扫", Toast.LENGTH_SHORT).show()
        }
    }

    com.example.ui.components.LiquidGlassDialogShell(
        onDismissRequest = onDismiss,
        title = "联系作者",
        subtitle = "扫码支持或添加好友交流 · 开发不易感谢陪伴",
        centerTitle = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = tabTitle,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> {
                    // 微信投喂
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF07C160).copy(alpha = 0.3f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(220.dp)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ContactQrImage(url = aliQr, contentDescription = "微信投喂扫码")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "投喂作者",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF07C160)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "开发不易，投喂作者一杯奶茶呗～",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { openWeChat() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF07C160)),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text("唤醒微信", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    copyText(context, "官方交流QQ群", cloudSettings?.qqGroupUin?.ifBlank { "439211347" } ?: "439211347")
                                },
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("复制QQ群号", fontSize = 12.sp)
                            }
                        }
                    }
                }
                1 -> {
                    // QQ
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1976D2).copy(alpha = 0.3f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(220.dp)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ContactQrImage(url = qqQr, contentDescription = "QQ扫码")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "扫一扫 加好友",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1976D2)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "QQ 扫码加好友交流～",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { openQq() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text("唤醒QQ", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    copyText(context, "官方交流QQ群", cloudSettings?.qqGroupUin?.ifBlank { "439211347" } ?: "439211347")
                                },
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("复制QQ群号", fontSize = 12.sp)
                            }
                        }
                    }
                }
                2 -> {
                    // 微信
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF07C160).copy(alpha = 0.3f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(220.dp)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ContactQrImage(url = wxQr, contentDescription = "微信扫码")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "扫一扫 加好友",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF07C160)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "微信扫码加好友交流～",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { openWeChat() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF07C160)),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text("唤醒微信", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    copyText(context, "官方交流QQ群", cloudSettings?.qqGroupUin?.ifBlank { "439211347" } ?: "439211347")
                                },
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("复制QQ群号", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onOpenFeedback()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.BugReport, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("软件反馈", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("关闭")
                }
            }
        }
    }
}
/**
 * 联系二维码：控制台上传的二维码「写死」生效——
 * 一旦存在云端/缓存二维码，仅展示该二维码，不再呈现内置二维码；
 * 只有从未上传过二维码时才显示内置占位图。
 */
@Composable
private fun ContactQrImage(
    url: String,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(LocalThemeUiColors.current.surface, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (url.isNotBlank()) {
            // v1.0.7：只显示控制台上传的二维码（永久保留、无延迟），已删除软件自带内置二维码
            // 用 remember + 预加载缓存：首次加载后立即呈现，不再有延迟
            val ctx = LocalContext.current
            // v1.0.7：复用全局 ImageLoader 预加载二维码到内存缓存，打开弹窗即无延迟呈现
            val loader = remember(ctx) { com.example.ui.components.FastFaviconImageLoader.get(ctx) }
            LaunchedEffect(url) {
                loader.enqueue(
                    coil.request.ImageRequest.Builder(ctx)
                        .data(url)
                        .memoryCacheKey("contact_qr_$url")
                        .build()
                )
            }
            coil.compose.AsyncImage(
                model = url,
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // 控制台未上传二维码时：显示文字提示（不展示任何内置二维码图）
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                Text(text = "?", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "二维码待上传",
                    fontSize = 11.sp,
                    color = LocalThemeUiColors.current.textMuted
                )
            }
        }
    }
}
