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
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "cute_cartoon_sparkle")
        val floatPhase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 6.283185f,
            animationSpec = infiniteRepeatable(tween(3600, easing = LinearEasing), RepeatMode.Restart),
            label = "float_bubble"
        )
        val bobbingY = (kotlin.math.sin(floatPhase) * 6f)
        val sparkleAlpha = (0.5f + 0.45f * kotlin.math.sin(floatPhase * 2f))

        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(26.dp))
                .shadow(20.dp, RoundedCornerShape(26.dp)),
            shape = RoundedCornerShape(26.dp),
            color = Color.White.copy(alpha = 0.90f),
            border = BorderStroke(
                1.5.dp,
                Brush.linearGradient(
                    listOf(
                        Color.White,
                        Color(0xFFF472B6).copy(alpha = 0.6f),
                        Color(0xFF38BDF8).copy(alpha = 0.5f),
                        Color(0xFFA78BFA).copy(alpha = 0.6f),
                        Color.White
                    )
                )
            )
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height
                    drawCircle(
                        color = Color(0xFFFFB6C1).copy(alpha = 0.35f * sparkleAlpha),
                        radius = 14.dp.toPx(),
                        center = Offset(w * 0.88f, 28.dp.toPx() + bobbingY)
                    )
                    drawCircle(
                        color = Color(0xFF38BDF8).copy(alpha = 0.30f * (1.2f - sparkleAlpha)),
                        radius = 9.dp.toPx(),
                        center = Offset(24.dp.toPx(), 45.dp.toPx() - bobbingY)
                    )
                    drawCircle(
                        color = Color(0xFFA78BFA).copy(alpha = 0.28f * sparkleAlpha),
                        radius = 7.dp.toPx(),
                        center = Offset(w * 0.82f, h - 35.dp.toPx() + bobbingY)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🐰", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "✨ 懒得找了 · 纯净安全保障",
                                fontSize = 11.sp,
                                color = Color(0xFF8B5CF6),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

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
                confirmButtonText = "知道了",
                onConfirm = { activeDialogType = null }
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    // v1.0.1：全新动态 CSS 品牌标签（渐变流光 + 呼吸动画）
                    DynamicCssBrandTag()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "「懒得找了」致力于打造一个纯净、聚合、高效的资源与工具导航平台。让大家不用再到处求资源、不用忍受满屏广告垃圾，一键直达互联网精品！\n\n" +
                                "✨ 我们的初心与承诺：\n" +
                                "· 纯净体验：无任何强制广告流，启动极速，开箱即用\n" +
                                "· 隐私安全：核心浏览与收藏数据皆存放于设备本地加密空间，不上传个人隐私\n" +
                                "· 开放生态：支持作者自主发布分享优质工具，与广大互联网爱好者共同成长\n\n" +
                                "感谢大家一路以来的支持与反馈，我们会持续迭代，把更多真正实用的宝藏工具带给每一位朋友。",
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        "terms" -> {
            CuteCartoonLiquidGlassDialog(
                onDismissRequest = { activeDialogType = null },
                title = "用户协议",
                confirmButtonText = "我已阅读并同意",
                onConfirm = { activeDialogType = null }
            ) {
                Column(
                    modifier = Modifier
                        .height(340.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "【引言与总则】\n" +
                                "欢迎您使用「懒得找了」应用及相关服务！在您使用本软件前，请务必审慎阅读、充分理解本协议各条款内容。当您开始使用本应用，即视为您已充分理解并同意接受本协议的全部约定。\n\n" +
                                "第一条：服务内容与形式\n" +
                                "1.1 本应用为一款综合型资源索引与效率工具集合体，提供聚合搜索、分类直达、实用工具箱、作者资源分享及本地文件管理等功能。\n" +
                                "1.2 本应用坚持免强制注册登录机制，绝大多数功能均可本地离线或免密无感畅享。\n\n" +
                                "第二条：知识产权与免责声明\n" +
                                "2.1 本应用所收录展示的第三方网站、产品链接及商标标识，其知识产权均归各原始权利人所有。本应用仅提供超链接导航服务，不对第三方站点的真实性、有效性、安全性承担连带保证责任。\n" +
                                "2.2 当您点击导航直达跳转至外部第三方网站时，请严格遵守外部网站的使用协议并提高安全防范意识，注意保护个人财产与账号密码安全。\n\n" +
                                "第三条：用户行为与自主发布规范\n" +
                                "3.1 用户使用本应用（包括作者上传软件与Skill技能模块）时，必须遵守中华人民共和国相关法律法规，不得利用本应用从事任何违法违规行为。\n" +
                                "3.2 严禁上传含有病毒、木马、恶意扣费、侵犯他人隐私或侵犯知识产权的文件及链接。平台有权对违规内容立即执行下架、清除或限制访问。\n\n" +
                                "第四条：免责与不可抗力\n" +
                                "4.1 因互联网网络波动、通信运营商故障、黑客攻击、系统维护或不可抗力导致的服务中断或延迟，平台将在第一时间内全力修复，但不承担由此引起的间接损失。\n\n" +
                                "第五条：协议修改与终止\n" +
                                "5.1 平台有权根据法律法规变化或业务运营需要对本协议进行修订，修改后的协议将在应用内及时公布更新。",
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        "privacy" -> {
            CuteCartoonLiquidGlassDialog(
                onDismissRequest = { activeDialogType = null },
                title = "隐私政策",
                confirmButtonText = "我已充分理解",
                onConfirm = { activeDialogType = null }
            ) {
                Column(
                    modifier = Modifier
                        .height(340.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "【引言与承诺】\n" +
                                "「懒得找了」深知个人信息安全对您的重要性。我们始终恪守“最小必要”、“存储本地化”、“安全透明”原则，致力于为您提供无干扰、无追踪的纯净数字环境。\n\n" +
                                "第一条：我们收集与处理的信息\n" +
                                "1.1 本地偏好数据：您的主题选择、深浅色模式、每日使用习惯记录完全保存在您本地手机的 SQLite / Room 沙盒中，绝不上报云端服务器。\n" +
                                "1.2 收藏夹与浏览记录：您收藏的网站或历史点击记录均属于设备本地私有数据，不经过任何后台远程统计，您可以随时在应用内一键清除。\n" +
                                "1.3 IP实时监控定位信息：主界面顶部展示的 IP 归属地数据，仅通过公开接口（如 myip.ipip.net）请求当前客户端的出网 IP 及大致城市，仅用于客户端本地呈现网络健康状况，服务器端不予持久化存储或归档。\n\n" +
                                "第二条：权限调用与使用声明\n" +
                                "2.1 网络访问权限（INTERNET）：仅用于加载导航列表、网络健康监测以及打开外部链接。\n" +
                                "2.2 文档/文件选择器：当您使用「本地上传APK」或「本地上传Skill技能包（ZIP/MD）」时，系统仅调用 Android 系统原生的文件选择器（Storage Access Framework），我们仅读取您主动授权选中的单个文件元信息，不会扫描或遍历您的私有相册与手机存储。\n\n" +
                                "第三条：第三方服务与SDK声明\n" +
                                "3.1 本应用不接入任何弹窗广告SDK、个性化广告追踪SDK或后台常驻定位SDK，杜绝隐私泄露风险。\n\n" +
                                "第四条：用户权利与自主控制\n" +
                                "4.1 随时删除：您可以随时在设置或各个模块中删除您添加的本地资源与自定义代码主题，数据即刻在本地被永久擦除。\n\n" +
                                "第五条：政策更新与联系方式\n" +
                                "5.1 我们会根据业务发展更新隐私政策，重大变更将通过应用内公告方式予以公示。",
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White.copy(alpha = 0.90f),
        shape = RoundedCornerShape(26.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(26.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White,
                        Color(0xFFF472B6).copy(alpha = 0.6f),
                        Color(0xFF38BDF8).copy(alpha = 0.5f),
                        Color(0xFFA78BFA).copy(alpha = 0.6f),
                        Color.White
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SupportAgent,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("联系作者", fontWeight = FontWeight.Black, fontSize = 16.sp)
                    Text("扫码支持或添加好友交流", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
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
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onOpenFeedback()
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Filled.BugReport, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("软件反馈", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("关闭")
                }
            }
        }
    )
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

