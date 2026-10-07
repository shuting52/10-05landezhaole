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
import com.example.ui.components.DrawAboutUsIcon
import com.example.ui.components.DrawUserTermsIcon
import com.example.ui.components.DrawPrivacyPolicyIcon
import com.example.ui.components.DrawChildPrivacyIcon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CuteLemon
import com.example.ui.theme.CutePeach
import com.example.ui.theme.CutePink
import com.example.ui.theme.LocalThemeUiColors
import com.example.ui.theme.ThemePreset
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
                    containerColor = Color(0xFF8B5CF6)
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
    currentTheme: ThemePreset,
    onOpenThemeSwitcher: () -> Unit,
    cloudUpdate: UpdateDialogDto? = null,
    cloudVersion: VersionDto? = null,
    cloudSettings: SettingsDto? = null,
    onCheckUpdate: (suspend () -> Pair<Boolean, VersionDto?>)? = null,
    modifier: Modifier = Modifier,
    // v1.2.6：设置页标题/副标题（控制台 UI 文本可云端覆盖）
    uiText: com.example.data.remote.UiTextDto? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var activeDialogType by remember { mutableStateOf<String?>(null) }
    var isCheckingUpdate by remember { mutableStateOf(false) }
    // 检查更新反馈弹窗：无新版本时点击「检查更新」弹出独立弹窗「已是最新版本」
    var showLatestVersionDialog by remember { mutableStateOf(false) }

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
                        Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.44f),
                                Color(0xFFF6F0FF).copy(alpha = 0.28f),
                                Color(0xFFFFEDF5).copy(alpha = 0.32f),
                                Color.White.copy(alpha = 0.48f)
                            )
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
                    tint = Color(0xFFFFFAF0),
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
            color = Color(0xFFDE2910),
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
            color = Color(0xFFDE2910),
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
                ),
                QuadrantCardItem(
                    title = "主题外观",
                    position = QuadrantPosition.BOTTOM_RIGHT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFF8B5CF6),
                    defaultIconColor = Color(0xFF7C3AED),
                    spotShadowColor = Color(0x608B5CF6),
                    iconDrawer = { color -> DrawThemeAppearanceIcon(color) },
                    onClick = onOpenThemeSwitcher
                )
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 3. 协议与关于（关于我们、用户协议、隐私政策、儿童隐私政策）
        Text(
            text = "📜 协议与关于",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFDE2910),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )
        QuadrantCardGrid(
            items = listOf(
                QuadrantCardItem(
                    title = "关于我们",
                    position = QuadrantPosition.TOP_LEFT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFFDE2910),
                    defaultIconColor = Color(0xFFB91C1C),
                    spotShadowColor = Color(0x60DE2910),
                    iconDrawer = { color -> DrawAboutUsIcon(color) },
                    onClick = { activeDialogType = "about" }
                ),
                QuadrantCardItem(
                    title = "用户协议",
                    position = QuadrantPosition.TOP_RIGHT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFF0D9488),
                    defaultIconColor = Color(0xFF0F766E),
                    spotShadowColor = Color(0x600D9488),
                    iconDrawer = { color -> DrawUserTermsIcon(color) },
                    onClick = { activeDialogType = "terms" }
                ),
                QuadrantCardItem(
                    title = "隐私政策",
                    position = QuadrantPosition.BOTTOM_LEFT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFF4F46E5),
                    defaultIconColor = Color(0xFF4338CA),
                    spotShadowColor = Color(0x604F46E5),
                    iconDrawer = { color -> DrawPrivacyPolicyIcon(color) },
                    onClick = { activeDialogType = "privacy" }
                ),
                QuadrantCardItem(
                    title = "儿童隐私政策",
                    position = QuadrantPosition.BOTTOM_RIGHT,
                    defaultBgColor = Color.White,
                    activeBgColor = Color(0xFFEA580C),
                    defaultIconColor = Color(0xFFC2410C),
                    spotShadowColor = Color(0x60EA580C),
                    iconDrawer = { color -> DrawChildPrivacyIcon(color) },
                    onClick = { activeDialogType = "child_privacy" }
                )
            )
        )

        Spacer(modifier = Modifier.height(26.dp))

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
        "about" -> {
            CuteCartoonLiquidGlassDialog(
                onDismissRequest = { activeDialogType = null },
                title = "关于我们",
                confirmButtonText = "我知道了",
                onConfirm = { activeDialogType = null }
            ) {
                Column(
                    modifier = Modifier
                        .height(380.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DynamicCssBrandTag()
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "「懒得找了」· 纯净聚合生态",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "「懒得找了」是一款坚持以“用户体验至上、免搜索一键直达、纯粹高效”为宗旨的全球数字化精品资源与效率工具导航平台。\n\n" +
                                "在这个信息爆炸却又充斥着垃圾广告、付费陷阱与虚假下载链接的时代，寻找一个真正好用、安全、干净的工具往往需要耗费大量的时间和精力。我们开发这款软件的初衷，正是为了让所有互联网爱好者、极客、设计师、开发者以及普通用户，都能够「告别繁琐检索，一键直达互联网的真正宝藏」。\n\n" +
                                "✨ 我们的核心基石与产品理念：\n" +
                                "1. 【极速纯净 · 拒绝干扰】\n" +
                                "本软件完全摒弃任何形式的开屏流氓广告、横幅弹窗推广和强制引流。从启动到使用，始终保持毫秒级响应，开箱即用，还给用户最清爽的交互环境。\n\n" +
                                "2. 【数据去中心化 · 本地绝对安全】\n" +
                                "所有个人收藏、使用记录、本地样式配置均直接持久化于您设备本地的沙盒加密数据库中。我们不设立中心化账号体系，杜绝任何个人隐私数据的云端泄漏隐患。\n\n" +
                                "3. 【全球视野 · 聚合前沿引擎】\n" +
                                "不仅涵盖全球最新最前沿的 AI 大模型、智能搜索、数字画布与前沿开发套件，更全新收录了超 100 款全球精选云端沙盒浏览器（包含 Muse 及全球多节点云端浏览解决方案），助您跨越设备性能与地域限制，畅享安全隔离的极速云端冲浪。\n\n" +
                                "4. 【开放生态 · 互利共赢】\n" +
                                "我们支持作者自发布生态与离线百宝箱工具集，持续搜罗和整理全网开源、绿色无毒的实用软件及 Skill 技能库，与广大互联网技术同行者一同成长。\n\n" +
                                "感谢每一位支持与陪伴「懒得找了」走过每一次版本迭代的朋友！您的每一次点击、每一条反馈，都是推动我们不断精进、打磨极致细节的最大动力。",
                        fontSize = 12.5.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        "terms" -> {
            CuteCartoonLiquidGlassDialog(
                onDismissRequest = { activeDialogType = null },
                title = "用户协议",
                confirmButtonText = "我已认真阅读并同意",
                onConfirm = { activeDialogType = null }
            ) {
                Column(
                    modifier = Modifier
                        .height(380.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "用户服务协议与法律声明",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "【重要须知与前言】\n" +
                                "欢迎您选择并使用「懒得找了」（以下统称“本软件”或“本平台”）。在您正式安装、访问或使用本软件提供的各项服务前，请您务必审慎、完整地阅读并充分理解本《用户协议》各项条款，特别是免除或限制责任的条款、法律适用及争议解决条款。当您点击“同意”或实际使用本软件，即视为您已与本软件开发者达成具有法律效力的协议。\n\n" +
                                "第一条：服务宗旨与功能范畴\n" +
                                "1.1 本软件为一款开放、综合型的网络资源信息索引、分类直达导航以及本地多功能效率工具箱平台。\n" +
                                "1.2 服务内容包括但不限于：第三方官方网站快速索引直达、云端沙盒浏览器导航、AI效率工具聚合、实用离线生活/工作百宝箱、车标识别、测速、IP环境诊断参考、用户自定义本地收藏管理等。\n" +
                                "1.3 本平台绝大多数基础服务均支持免登录、免注册畅享，旨在为用户提供极致轻便的使用体验。\n\n" +
                                "第二条：第三方资源免责与外链指引\n" +
                                "2.1 本软件所展示的所有第三方网站名称、商标、LOGO、产品介绍及超链接跳转入口，其所有知识产权与合法权益均属于各原始官方权利人所有。本软件仅提供便于公众查找的技术索引与外链导航服务。\n" +
                                "2.2 当您点击导航项离开本软件并访问外部第三方网站时，外部网站的运营、内容合规性、服务可用性及安全性均独立于本软件。本软件无法亦无权对第三方网站的实际服务做出任何形式的担保或承担连带保证责任。请您在访问外部站点时提高网络安全与反诈防范意识，妥善保管个人财产与账号信息。\n\n" +
                                "第三条：用户合法合规使用准则\n" +
                                "3.1 用户在使用本软件的各项功能（包括但不限于自定义添加站点、作者自主上传软件、提交反馈与Skill技能包）时，必须严格遵守《中华人民共和国网络安全法》《中华人民共和国数据安全法》及相关现行法律法规。\n" +
                                "3.2 严禁任何用户利用本软件从事危害国家安全、宣扬恐怖暴力、传播淫秽色情、实施网络诈骗、发布计算机病毒木马、侵犯他人名誉权或知识产权等违法犯罪活动。\n" +
                                "3.3 如发现任何违规外链或恶意内容，平台有权在不事先通知的情况下立即执行阻断、清理、下架或限制访问。\n\n" +
                                "第四条：免责与不可抗力条款\n" +
                                "4.1 鉴于互联网网络的特殊性，因运营商网络故障、电信主干线路拥堵、第三方服务器宕机、黑客恶意攻击、政府管制或不可抗力等非本软件直接过错原因造成的服务中断、延迟或数据异常，开发者将在力所能及范围内积极组织修复，但不承担由此可能产生的任何间接经济损失。\n" +
                                "4.2 工具箱内包含的如“IP纯净度参考”、“网络测速”等检测工具，其测试结果受制于公网公开接口及节点运营商波动，仅供技术研究比对参考，不作为权威司法或商业鉴定的排他性依据。\n\n" +
                                "第五条：知识产权与软件版权保护\n" +
                                "5.1 本软件的整体架构设计、界面UI排版、代码编译产物、图标设计及独创视觉元素，其版权与知识产权均归「懒得找了」开发者依法所有，受法律保护。未经明确书面许可，严禁对其进行非法二次打包篡改或恶意商业化反编译。\n\n" +
                                "第六条：协议修改、补充与生效\n" +
                                "6.1 开发者保留根据法律法规变动、业务升级或功能调整对本协议内容适时予以修订的权利。更新后的协议条款将通过版本更新或端内弹窗公示，一经公布即生效。",
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        "privacy" -> {
            CuteCartoonLiquidGlassDialog(
                onDismissRequest = { activeDialogType = null },
                title = "隐私政策",
                confirmButtonText = "我已充分理解并知悉",
                onConfirm = { activeDialogType = null }
            ) {
                Column(
                    modifier = Modifier
                        .height(380.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "个人信息与隐私安全保护指南",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "【承诺与总则】\n" +
                                "「懒得找了」（以下统称“我们”）深知个人信息和隐私数据对您的极端重要性。我们始终恪守“最小必要化采集”、“数据存储本地化”、“透明安全”、“严禁非法买卖与追踪”的严苛准则。本隐私政策旨在向您详尽披露我们如何对待您的个人信息，请您务必仔细阅读。\n\n" +
                                "第一条：我们绝不收集的核心敏感信息\n" +
                                "1.1 我们坚决执行无账户、免实名绑定的纯净模式，绝不会主动索取、收集、保存或上传您的真实姓名、身份证件号码、人脸生物特征、家庭住址、通讯录好友名单、银行卡或支付凭据信息。\n" +
                                "1.2 本软件严禁接入任何带有跨应用追踪、常驻后台监听或用户画像营销特征的商业化第三方广告监测SDK。\n\n" +
                                "第二条：本地数据沙盒存储机制\n" +
                                "2.1 【个人偏好与主题配置】：您在软件中设置的主题风格、深色模式切换、环形菜单自定义偏好、界面布局等，均100%保存在您手机本地的 SQLite / SharedPreferences 安全沙盒中，不产生任何云端上传行为。\n" +
                                "2.2 【收藏夹与历史痕迹】：您标记的常用网站、自定义添加的专属外链、离线百宝箱的使用记录，均由您的手机本地数据库独立管控，您可以随时在应用内一键清除或随软件卸载而物理销毁。\n\n" +
                                "第三条：必要网络交互与权限最小化调用\n" +
                                "3.1 【网络访问权限 (INTERNET)】：用于同步由公开GitHub仓库驱动的云端导航最新数据源、检查软件新版本、加载站点Favicon图标，以及在您点击外链时唤醒内置/系统浏览器直达目标站点。\n" +
                                "3.2 【IP网络环境检测参考】：主界面及百宝箱中的“IP位置与纯净度”功能，仅通过向公开权威公共查询接口（如 myip.ipip.net / ipinfo 等）发送瞬时网络请求以获取客户端当前的公共出口IP与归属地，仅用于本地即时呈现网络连通状态参考，服务器端绝不进行任何持久化追踪归档。\n" +
                                "3.3 【系统文件选择器】：当您使用本地软件上传或导入功能时，软件严格调用 Android 系统官方的安全存储框架（Storage Access Framework），仅读取您明确选中的单个特定文件，绝对不会扫描、遍历或窥探您相册内的私密照片或机密文件。\n\n" +
                                "第四条：用户对个人数据的自主管理权利\n" +
                                "4.1 您拥有对自己本地数据的绝对控制权：您可随时在“设置”或各模块中清空缓存、清除搜索历史、重置默认主题，所有操作均在设备本地物理生效，无法恢复且不留痕迹。\n\n" +
                                "第五条：未成年人特殊保护体系\n" +
                                "5.1 我们特别关注未成年人身心健康，平台所收录内容持续进行人工与算法审查，坚决剔除一切不利于青少年健康成长的有害信息。\n\n" +
                                "第六条：隐私政策修订与监督反馈\n" +
                                "6.1 随着软件业务的演进与法律法规更新，我们可能对本政策做出审慎调整，并在软件内更新公示。若您对个人隐私保护有任何疑问或改进建议，可通过「软件反馈」或官方社群随时联系我们。",
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        "child_privacy" -> {
            CuteCartoonLiquidGlassDialog(
                onDismissRequest = { activeDialogType = null },
                title = "儿童隐私政策",
                confirmButtonText = "知晓并遵守",
                onConfirm = { activeDialogType = null }
            ) {
                Column(
                    modifier = Modifier
                        .height(340.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "【未成年人特殊关怀声明】\n" +
                                "「懒得找了」高度重视并积极履行对未成年人及不满十四周岁儿童的个人信息安全保护义务。本政策旨在说明我们如何守护青少年的健康用网与隐私安全。\n\n" +
                                "第一条：监护人指导与协同责任\n" +
                                "1.1 若您为未满十四周岁的儿童或未成年人，在使用本软件前，请务必请您的父母或其他法定监护人仔细阅读并理解本政策，并在监护人的指导与同意下使用本应用。\n" +
                                "1.2 监护人应当协助未成年人树立正确的网络价值观与安全防范意识，监督其网络活动，合理规划使用设备时长。\n\n" +
                                "第二条：严格的儿童信息零收集原则\n" +
                                "2.1 本应用坚持无账户、无实名绑定的纯净架构，我们绝不会主动索取、收集、保存、出售或向任何第三方披露不满十四周岁儿童的姓名、身份证号、人脸特征、住址或联系方式。\n" +
                                "2.2 本应用禁止利用任何算法对儿童行为进行商业化画像、消费倾向分析或精准营销推送。\n\n" +
                                "第三条：绿色内容与安全防护机制\n" +
                                "3.1 内容过滤：我们持续审核与筛查收录的导航内容，杜绝涉黄、暴恐、不良低俗、诱导打赏或网络赌博等危害身心健康的有害信息。\n" +
                                "3.2 防沉迷与健康提示：倡导青少年劳逸结合，避免过度用眼，建立健康作息。\n\n" +
                                "第四条：监护人权利与快速响应救济通道\n" +
                                "4.1 若监护人发现未成年人在未经许可的情况下上传了可能涉及个人隐私的内容，可通过官方社群渠道联系我们，我们将在核实后第一时间进行下架与删除处理。",
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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
private fun SettingsClickableItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = if (subtitle.isNullOrBlank()) 14.dp else 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (!subtitle.isNullOrBlank()) {
                Text(
                    subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(14.dp)
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
    val tabs = listOf("支付宝", "QQ", "微信")

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
                    // 支付宝
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1677FF).copy(alpha = 0.3f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(220.dp)
                                    .padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                ContactQrImage(url = aliQr, contentDescription = "支付宝扫码")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "投喂作者",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1677FF)
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
                                onClick = { openAlipay() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1677FF)),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text("唤醒支付宝", fontSize = 12.sp)
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
            .background(Color(0xFFFAFAFA), RoundedCornerShape(12.dp)),
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
                    color = Color(0xFF9E9E9E)
                )
            }
        }
    }
}

/**
 * v1.0.1 关于我们·全新动态 CSS 品牌标签：渐变流光 + 呼吸浮动动画
 * 内容固定为「懒得找了-307779523」，圆角胶囊 + 粉橙黄渐变流光扫过，可爱卡通风格贴合新主题
 */
@Composable
private fun DynamicCssBrandTag() {
    val infinite = rememberInfiniteTransition(label = "about_css_brand_tag")
    // 渐变流光：从左侧扫到右侧再循环
    val shift by infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tag_shift"
    )
    // 呼吸浮动：轻微放大缩小让标签更有生命力
    val breathe by infinite.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tag_breathe"
    )
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .graphicsLayer {
                scaleX = breathe
                scaleY = breathe
            }
            .clip(RoundedCornerShape(50))
            .background(
                Brush.linearGradient(
                    colors = listOf(CutePink, CutePeach, CuteLemon, CutePink),
                    start = Offset(shift * 700f, 0f),
                    end = Offset(shift * 700f + 460f, 0f)
                )
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🧸",
                fontSize = 15.sp
            )
            Text(
                text = "懒得找了-307779523",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

