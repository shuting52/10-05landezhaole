package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.R
import coil.imageLoader
import com.example.ui.components.IpLocationMonitorWidget
import com.example.ui.components.IpMonitorWidget
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.NavData
import com.example.data.local.db.UserItemRecord
import com.example.data.model.BadgeType
import com.example.data.model.NavCard
import com.example.data.remote.UpdateDialogDto
import com.example.data.remote.VersionDto
import com.example.ui.components.AddSiteDialog
import com.example.ui.components.AppUpdateDialog
import com.example.ui.components.AtmosphereOverlay
import com.example.ui.components.CategorySitesDialog
import com.example.ui.components.CustomRadioBottomNav
import com.example.ui.components.CuteWelcomeDialog
import com.example.ui.components.GlobalWindBackground
import com.example.ui.components.HideAndSeekLoader
import com.example.ui.components.ResourceCard
import com.example.ui.components.SaharaWaveButton
import com.example.ui.components.SiteBrandIcon
import com.example.ui.components.SiteDetailDialog
import com.example.ui.components.SplashScreenOverlay
import com.example.ui.theme.LocalUiverseState
import com.example.ui.uiverse.CardStylePreset
import com.example.ui.uiverse.InputStylePreset
import com.example.ui.uiverse.UiverseDialog
import com.example.ui.theme.FlameRed
import com.example.ui.theme.SunsetOrange
import com.example.ui.viewmodel.AppBottomTab
import com.example.ui.viewmodel.NavViewModel

// v1.2.6：中国红标准色（截图2要求：绿色改为中国红）
private val ChinaRed = Color(0xFFDE2910)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NavViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()
    val filteredCards by viewModel.filteredCards.collectAsStateWithLifecycle()

    val favUrls = favorites.map { it.url }.toSet()

    val uploadedSoftware by viewModel.uploadedSoftware.collectAsStateWithLifecycle()
    val uploadedSkills by viewModel.uploadedSkills.collectAsStateWithLifecycle()
    val uploadedPrompts by viewModel.uploadedPrompts.collectAsStateWithLifecycle()
    val customSites by viewModel.customSites.collectAsStateWithLifecycle()

    val totalResourceCount = remember(uiState.categories, customSites) {
        uiState.categories.sumOf { it.cards.size } + customSites.size
    }

    var showAddSiteDialog by remember { mutableStateOf(false) }
    var showCategoryBottomSheet by remember { mutableStateOf(false) }
    var showCloudUpdateDialog by remember { mutableStateOf(false) }
    var updateDialogDismissed by remember { mutableStateOf(false) }
    var welcomeDialogDismissed by remember { mutableStateOf(false) }
    var showShutdownDialog by remember { mutableStateOf(false) }

    // 启动时恢复自定义背景（跨重启持久）
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("lzdz_bg_prefs", Context.MODE_PRIVATE)
        val type = prefs.getString("bg_type", "none") ?: "none"
        val path = prefs.getString("bg_path", "") ?: ""
        if (type != "none" && path.isNotBlank()) {
            viewModel.setLocalBgMedia(type, "file://$path")
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 本地背景媒体优先（主题版块本机选择），无本地媒体时回退云端背景
        val bgType = uiState.localBgMediaType.ifBlank { "none" }.let {
            if (it != "none") it else (uiState.cloudSettings?.bgMedia?.type ?: "none")
        }
        val bgUrl = uiState.localBgMediaUri.ifBlank {
            uiState.cloudSettings?.bgMedia?.url.orEmpty()
        }
        GlobalWindBackground(
            bgMediaType = bgType,
            bgMediaUrl = bgUrl,
            // v1.0.4：主题切换优化——背景跟随软件背景（主题背景色）同步
            themeBgColor = uiState.currentTheme.bgColor,
            themePrimaryColor = uiState.currentTheme.primaryColor
        ) {
            // v1.8.7：背景媒体（图片/视频）激活时，全局白色 background/surface 自动转为半透明磨砂，
            // 让背景透出（设置页、卡片、各 Tab 均生效）；无背景媒体时保持原样
            val mediaBgActive = bgType == "image" || bgType == "video"
            val frostedScheme = if (mediaBgActive) {
                MaterialTheme.colorScheme.copy(
                    surface = Color.White.copy(alpha = 0.38f),
                    surfaceVariant = Color.White.copy(alpha = 0.22f),
                    background = Color.White.copy(alpha = 0.16f)
                )
            } else {
                MaterialTheme.colorScheme
            }
            MaterialTheme(colorScheme = frostedScheme) {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets.statusBars,
                bottomBar = {
                    CustomRadioBottomNav(
                        selectedTab = uiState.currentTab,
                        onTabSelected = { viewModel.switchTab(it) },
                        uiText = uiState.cloudUiText
                    )
                }
            ) { paddingValues ->
            when (uiState.currentTab) {
                AppBottomTab.HOME -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(
                            top = paddingValues.calculateTopPadding() + 8.dp,
                            bottom = paddingValues.calculateBottomPadding() + 16.dp,
                            start = 8.dp,
                            end = 8.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("nav_main_grid")
                    ) {
                        // 1. Header & Brand Banner
                        item(span = { GridItemSpan(3) }) {
                            HeaderBrandSection(
                                favoriteCount = favorites.size,
                                historyCount = history.size,
                                totalResourceCount = totalResourceCount,
                                onOpenFavorites = { viewModel.setFavoritesModalVisible(true) },
                                onOpenHistory = { viewModel.setHistoryModalVisible(true) },
                                onOpenTheme = { viewModel.setThemeDialogVisible(true) },
                                onOpenAddSite = { showAddSiteDialog = true },
                                onTriggerSplash = { viewModel.showSplash() },
                                cloudMarquee = uiState.cloudMarquee,
                                cloudIpMonitor = uiState.cloudIpMonitor,
                                cloudAppName = uiState.cloudUiText?.homeTitle?.ifBlank { uiState.cloudSettings?.appName?.ifBlank { "懒得找了" } ?: "懒得找了" } ?: uiState.cloudSettings?.appName?.ifBlank { "懒得找了" } ?: "懒得找了",
                                cloudLogo = uiState.cloudSettings?.logoUrl.orEmpty(),
                                componentThemes = uiState.activeUiverseState.componentThemes
                            )
                        }

                        // 2. 随心抽按钮 (分类标签已按要求从主页移除，仅在随心抽弹窗内部保留)
                        item(span = { GridItemSpan(3) }) {
                            SaharaWaveButton(
                                onClick = { viewModel.rollLuckyCard() }
                            )
                        }

                        // 4. Search Box
                        item(span = { GridItemSpan(3) }) {
                            SearchSection(
                                query = uiState.searchQuery,
                                onQueryChange = { viewModel.updateSearchQuery(it) },
                                componentThemes = uiState.activeUiverseState.componentThemes,
                                // v1.2.6：搜索框占位文字可云端覆盖
                                placeholderText = uiState.cloudUiText?.searchPlaceholder
                            )
                        }

                        // 5. Result Counter
                        item(span = { GridItemSpan(3) }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFDE2910))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (uiState.searchQuery.isNotBlank()) "🔍 搜索结果 (${filteredCards.size})" else "🎯 资源宝库 (${filteredCards.size})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2C0E11)
                                    )
                                }
                            }
                        }
                        itemsIndexed(filteredCards, key = { index, card -> "${card.id}_${card.url}_$index" }) { _, card ->
                            // v1.0.18 角标优化：NEW 角标改用实际名字（所属分类名）代替
                            val realBadge = card.copy(
                                badge = if (card.badge.isNullOrBlank() || card.badge == "NEW")
                                    uiState.categories.firstOrNull { it.id == card.categoryId }?.name ?: card.badge
                                else card.badge
                            )
                            ResourceCard(
                                card = realBadge,
                                isFavorite = favUrls.contains(card.url),
                                onCardClick = { viewModel.openCard(context, it) },
                                onFavoriteToggle = { viewModel.toggleFavorite(it, context) },
                                onCardLongClick = { viewModel.showDetail(it) },
                                // v1.1.4 主题分支：组件级定制真正生效
                                componentThemes = uiState.activeUiverseState.componentThemes
                            )
                        }

                        // Empty State
                        if (filteredCards.isEmpty()) {
                            item(span = { GridItemSpan(3) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = uiState.cloudUiText?.emptyText?.ifBlank { "没有找到相关资源" } ?: "没有找到相关资源",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        TextButton(onClick = { viewModel.updateSearchQuery("") }) {
                                            Text("清空搜索条件")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                AppBottomTab.SOFTWARE -> {
                    UploadHubScreen(
                        // v1.2.6：软件库标题/副标题可云端覆盖
                        title = uiState.cloudUiText?.softwareTitle?.ifBlank { "懒得找了-软件库" } ?: "懒得找了-软件库",
                        subtitle = uiState.cloudUiText?.softwareSubtitle?.ifBlank { "一些PJ应用来源于网络～如有侵权请联系下架。失效也及时反馈哟" } ?: "一些PJ应用来源于网络～如有侵权请联系下架。失效也及时反馈哟",
                        resourceType = "software",
                        resources = uploadedSoftware,
                        onDelete = { id -> viewModel.deleteUploadedResource(id) },
                        modifier = Modifier.padding(paddingValues),
                        showDelete = false,
                        // v1.0.4：软件自动分类/自动icon + 一排三个横排网格呈现；
                        // v1.0.18 增加 .u-tab 推荐/关注/热门筛选（关注=收藏）
                        favoriteUrls = favUrls,
                        gridMode = true
                    )
                }
                AppBottomTab.SKILL -> {
                    var skillSubTabIndex by remember { mutableIntStateOf(0) } // 0: 提示词区, 1: Skill 技能库
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        // v1.2.6：Skill 顶部 Tab 采用液态玻璃折射半透胶囊条
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.44f),
                                            Color(0xFFF5F0FF).copy(alpha = 0.30f),
                                            Color(0xFFFFEDF5).copy(alpha = 0.34f),
                                            Color.White.copy(alpha = 0.48f)
                                        )
                                    )
                                )
                                .border(
                                    width = 1.2.dp,
                                    brush = Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.92f),
                                            Color(0xFFD8B4FE).copy(alpha = 0.45f),
                                            Color.White.copy(alpha = 0.88f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                androidx.compose.material3.FilterChip(
                                    selected = skillSubTabIndex == 0,
                                    onClick = { skillSubTabIndex = 0 },
                                    label = { Text(uiState.cloudUiText?.promptTab?.ifBlank { "提示词区" } ?: "提示词区", fontWeight = FontWeight.Bold, fontSize = 11.5.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                                androidx.compose.material3.FilterChip(
                                    selected = skillSubTabIndex == 1,
                                    onClick = { skillSubTabIndex = 1 },
                                    label = { Text(uiState.cloudUiText?.skillTab?.ifBlank { "Skill 技能库" } ?: "Skill 技能库", fontWeight = FontWeight.Bold, fontSize = 11.5.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        if (skillSubTabIndex == 0) {
                            PromptHubSubView(
                                prompts = uploadedPrompts,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            UploadHubScreen(
                                title = "Skill · 技能库",
                                subtitle = "Skill 技能包竖排列表 · 点击卡片查看详情（视频预览 / 下载 / 跳转）",
                                resourceType = "skill",
                                resources = uploadedSkills,
                                onDelete = { id -> viewModel.deleteUploadedResource(id) },
                                modifier = Modifier.fillMaxSize(),
                                showDelete = false,
                                // v1.0.5：软件版块统一“竖排改横排”，Skill 技能库也采用一排三个横排网格
                                gridMode = true
                            )
                        }
                    }
                }
                AppBottomTab.TOOLBOX -> {
                    ToolboxScreen(
                        cloudTools = uiState.cloudTools,
                        modifier = Modifier.padding(paddingValues),
                        // v1.2.6：工具箱标题/副标题可云端覆盖
                        title = uiState.cloudUiText?.toolTitle,
                        subtitle = uiState.cloudUiText?.toolboxSubtitle
                    )
                }
                AppBottomTab.SETTINGS -> {
                    SettingsScreen(
                        currentTheme = uiState.currentTheme,
                        onOpenThemeSwitcher = { viewModel.setThemeDialogVisible(true) },
                        cloudUpdate = uiState.cloudUpdate,
                        cloudVersion = uiState.cloudVersion,
                        cloudSettings = uiState.cloudSettings,
                        onCheckUpdate = { viewModel.refreshRemoteConfig() },
                        modifier = Modifier.padding(paddingValues),
                        // v1.2.6：设置页标题/副标题可云端覆盖
                        uiText = uiState.cloudUiText
                    )
                }
            }
        }
        }
    }

        // v1.1.12 修复：开屏改用新版粒子动画 SplashScreenOverlay（云端 splash 配置驱动，控制台可实时同步）
        if (uiState.isSplashVisible) {
            SplashScreenOverlay(
                isVisible = true,
                onDismiss = { viewModel.dismissSplash() },
                splash = uiState.cloudSplash,
                splashReady = uiState.isCloudReady,
                // v1.2.8：开屏文字可云端覆盖
                uiText = uiState.cloudUiText
            )
        }

        // v1.0.9：软件停止运营——控制台开关开启时强制弹窗，仅「确认」按钮，点击后强行退出
        val shutdownCfg = uiState.cloudSettings?.serverShutdown
        LaunchedEffect(uiState.isCloudReady, shutdownCfg?.enabled) {
            if (uiState.isCloudReady && shutdownCfg?.enabled == true) {
                showShutdownDialog = true
            }
        }
        if (showShutdownDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { /* 强制弹窗，不可关闭 */ },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⛔", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("软件停止运营", fontWeight = FontWeight.Black, color = Color(0xFFE53935))
                    }
                },
                text = {
                    Text(
                        text = shutdownCfg?.notice?.ifBlank {
                            "感谢您一直以来的支持！本软件已停止运营，由此给您带来的不便敬请谅解。"
                        } ?: "感谢您一直以来的支持！本软件已停止运营，由此给您带来的不便敬请谅解。",
                        fontSize = 13.5.sp,
                        lineHeight = 21.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // 点击「确认」后软件强制关闭
                            try {
                                (context as? android.app.Activity)?.finish()
                            } catch (_: Exception) {}
                            kotlin.system.exitProcess(0)
                        },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("确认", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // 云端实时更新弹窗：控制台发布新版本后，本体启动自动弹出更新提醒。
        // 已展示过的版本号持久化记录，避免重复弹窗；新版本安装后（本地code==云端code）不再提示。
        val cloudVersion = uiState.cloudVersion
        val cloudUpdate = uiState.cloudUpdate
        val prefs = remember { context.getSharedPreferences("lzdz_update_prefs", Context.MODE_PRIVATE) }
        // 死命令（1.7.1 修复时序）：更新弹窗只在开屏动画完全结束后呈现；
        // 联系作者二维码预加载：云端配置就绪后把三张二维码提前缓存到 Coil 内存/磁盘缓存，
        // 打开「联系作者」弹窗时秒开，彻底消除二维码加载延迟
        val qrPreloadUrls = listOf(
            uiState.cloudSettings?.contactWechat,
            uiState.cloudSettings?.contactQQ,
            uiState.cloudSettings?.contactAlipay
        ).filter { !it.isNullOrBlank() }
        LaunchedEffect(qrPreloadUrls) {
            if (qrPreloadUrls.isNotEmpty()) {
                try {
                    val loader = com.example.ui.components.FastFaviconImageLoader.get(context)
                    qrPreloadUrls.forEach { url ->
                        loader.enqueue(
                            coil.request.ImageRequest.Builder(context)
                                .data(url)
                                .memoryCacheKey(url)
                                .diskCacheKey(url)
                                .build()
                        )
                    }
                } catch (_: Exception) {}
            }
        }

        // 有新版本（cloudCode > localCode）就强制弹出且不可自行关闭，直到自动下载安装完成；
        // 已是最新版本（cloudCode <= localCode）绝不弹窗。
        // 不依赖「是否看过」记录：只要云端有更新就弹，避免倒计时后不弹、需手动检测的问题。
        LaunchedEffect(uiState.isCloudReady, cloudVersion?.code, uiState.isSplashVisible) {
            val localCode = com.example.BuildConfig.VERSION_CODE
            val cloudCode = cloudVersion?.code ?: 0
            // 开屏已结束 && 云端配置已就绪 && 云端版本高于本地 → 强制弹窗
            if (!uiState.isSplashVisible && cloudVersion != null && uiState.isCloudReady && cloudCode > localCode) {
                showCloudUpdateDialog = true
            }
        }
        if (showCloudUpdateDialog && cloudVersion != null) {
            AppUpdateDialog(
                onDismiss = {
                    showCloudUpdateDialog = false
                    updateDialogDismissed = true
                },
                versionName = "v${cloudVersion.name}",
                onUpdateFinished = {
                    // v1.0.13 修复「安装新版本时旧版本软件闪退」：
                    // 安装成功后 PackageInstaller 会终止旧进程并拉起新版本，
                    // 此处只需关闭弹窗状态即可，禁止再执行任何 Activity/Context 操作，
                    // 避免在旧进程被杀的瞬间访问已销毁的组件导致闪退。
                    showCloudUpdateDialog = false
                    updateDialogDismissed = true
                },
                // 云端弹窗配置缺失时用默认值兜底，确保弹窗一定渲染
                update = cloudUpdate ?: com.example.data.remote.UpdateDialogDto(
                    title = "发现新版本",
                    changelog = cloudVersion.changelog,
                    confirmText = "立即更新",
                    cancelText = "稍后再说"
                ),
                apkUrl = cloudVersion.apkUrl.ifBlank { null },
                // v1.0.16：改为点击「立即更新」后才开始下载安装（不自动下载），
                // 下载完成直接用系统安装器安装，绝不卡在安装中
                forceUpdate = true,
                autoDownload = false
            )
        }

        // 云端欢迎界面弹窗：仅开屏结束后才展示（避免开屏期间弹窗盖在开屏之上）
        // v1.7.2：改为可爱卡通动态绘制弹窗（底部滑入 + 表情摇摆 + 粉紫渐变），欢迎语每行一条独立呈现
        val cloudWelcome = uiState.cloudWelcome
        if (uiState.isCloudReady && !uiState.isSplashVisible && cloudWelcome?.enabled == true && !welcomeDialogDismissed) {
            CuteWelcomeDialog(
                welcome = cloudWelcome,
                onDismiss = { welcomeDialogDismissed = true }
            )
        }

        // Category Tags Expansion BottomSheet ("分类标签" 点击展开所有站点分类)
        if (showCategoryBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCategoryBottomSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                CategoryTagsSheetContent(
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    totalResourceCount = totalResourceCount,
                    onCategorySelect = { catId ->
                        viewModel.selectCategory(catId)
                        showCategoryBottomSheet = false
                    },
                    onRollLucky = {
                        showCategoryBottomSheet = false
                        viewModel.rollLuckyCard()
                    },
                    onClose = { showCategoryBottomSheet = false },
                    // v1.2.6：文字可云端覆盖
                    uiText = uiState.cloudUiText
                )
            }
        }

        // Lucky Draw BottomSheet ("分类列表")
        if (uiState.isLuckyModalVisible && uiState.luckyCard != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideLuckyModal() },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                LuckyDrawSheetContent(
                    card = uiState.luckyCard!!,
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategorySelect = { catId ->
                        viewModel.selectCategory(catId)
                        viewModel.rollLuckyCard(catId)
                    },
                    onReroll = { catId -> viewModel.rollLuckyCard(catId) },
                    onOpen = {
                        viewModel.openCard(context, uiState.luckyCard!!)
                        viewModel.hideLuckyModal()
                    },
                    onFavorite = { viewModel.toggleFavorite(uiState.luckyCard!!, context) },
                    isFavorite = favUrls.contains(uiState.luckyCard!!.url),
                    favoriteUrls = favUrls,
                    onCardClick = { card ->
                        viewModel.openCard(context, card)
                    },
                    onFavoriteToggle = { card ->
                        viewModel.toggleFavorite(card, context)
                    },
                    onClose = { viewModel.hideLuckyModal() },
                    uiText = uiState.cloudUiText
                )
            }
        }

        // Favorites Dialog / BottomSheet
        if (uiState.isFavoritesModalVisible) {
            FavoritesSheet(
                favorites = favorites,
                onOpen = {
                    val card = NavCard(
                        id = it.url,
                        title = it.title,
                        url = it.url,
                        desc = it.desc ?: "",
                        badge = it.badge,
                        icon = it.iconUrl ?: ""
                    )
                    viewModel.showDetail(card)
                },
                onRemove = {
                    val card = NavCard(id = it.url, title = it.title, url = it.url)
                    viewModel.toggleFavorite(card, context)
                },
                onDismiss = { viewModel.setFavoritesModalVisible(false) }
            )
        }

        // History Dialog / BottomSheet
        if (uiState.isHistoryModalVisible) {
            HistorySheet(
                history = history,
                onOpen = {
                    val card = NavCard(
                        id = it.url,
                        title = it.title,
                        url = it.url,
                        desc = it.desc ?: "",
                        badge = it.badge,
                        icon = it.iconUrl ?: ""
                    )
                    viewModel.showDetail(card)
                },
                onClear = { viewModel.clearHistory(context) },
                onDismiss = { viewModel.setHistoryModalVisible(false) }
            )
        }

        // Site Detail Dialog (站点详细内容：这个站点是干嘛的、有什么特别之处、立即直达)
        uiState.activeDetailCard?.let { card ->
            SiteDetailDialog(
                card = card,
                isFavorite = favUrls.contains(card.url),
                onDismiss = { viewModel.hideDetail() },
                onOpenDirectly = {
                    viewModel.openCard(context, card)
                    viewModel.hideDetail()
                },
                onToggleFavorite = {
                    viewModel.toggleFavorite(card, context)
                },
                onCopyUrl = {
                    copyToClipboard(context, card.url)
                    android.widget.Toast.makeText(context, "已复制站点网址到剪贴板", android.widget.Toast.LENGTH_SHORT).show()
                },
                onShare = {
                    shareText(context, "【${card.title}】${card.desc}\n访问链接：${card.url}")
                }
            )
        }

        // Atmosphere Effect Overlay (Fireworks, Money, Dragon, God)
        AtmosphereOverlay(effect = uiState.atmosphereEffect)

        // Uiverse.io Skin & UI Kit Studio Dialog
        if (uiState.isThemeDialogVisible) {
            val coroutineScope = rememberCoroutineScope()
            // 选择背景文件 → 复制到应用私有目录（跨重启持久）→ 全局应用 → 关闭主题弹窗
            fun applyCustomBg(uri: Uri?, type: String) {
                if (uri == null) return
                coroutineScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        try {
                            val dir = File(context.filesDir, "custom_bg")
                            dir.mkdirs()
                            val ext = if (type == "video") ".mp4" else ".jpg"
                            val target = File(dir, "custom_bg_${System.currentTimeMillis()}$ext")
                            context.contentResolver.openInputStream(uri)?.use { input ->
                                target.outputStream().use { output -> input.copyTo(output) }
                            }
                            // 持久化类型与路径（App 重启后恢复）
                            context.getSharedPreferences("lzdz_bg_prefs", Context.MODE_PRIVATE)
                                .edit()
                                .putString("bg_type", type)
                                .putString("bg_path", target.absolutePath)
                                .apply()
                            "file://${target.absolutePath}"
                        } catch (e: Exception) {
                            // 复制失败：回退使用内容 URI（当前会话仍可用）
                            try {
                                context.contentResolver.takePersistableUriPermission(
                                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )
                            } catch (e2: Exception) { }
                            uri.toString()
                        }
                    }
                    viewModel.setLocalBgMedia(type, result)
                    // 关闭主题弹窗，让用户立刻看到全局背景效果
                    viewModel.setThemeDialogVisible(false)
                    Toast.makeText(
                        context,
                        if (type == "image") "已应用自定义图片背景，全局生效" else "已应用自定义视频背景，全局生效",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            val bgImageLauncher = rememberLauncherForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
            ) { uri -> applyCustomBg(uri, "image") }
            val bgVideoLauncher = rememberLauncherForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
            ) { uri -> applyCustomBg(uri, "video") }
            UiverseDialog(
                isOpen = true,
                onClose = { viewModel.setThemeDialogVisible(false) },
                activeState = uiState.activeUiverseState,
                onResetDefault = {
                    viewModel.resetUiverseToDefault()
                },
                onApplyGradientTheme = { theme ->
                    viewModel.setTheme(theme)
                },
                activeGradientThemeId = uiState.currentTheme.id
            )
        }

        // Add Site Dialog with Automatic Deduplication and Auto Metadata Fetching
        if (showAddSiteDialog) {
            AddSiteDialog(
                onDismiss = { showAddSiteDialog = false },
                onCheckDuplicate = { url, title ->
                    viewModel.checkSiteDuplicate(url, title)
                },
                onConfirmAdd = { title, url, desc, categoryId, badge, iconUrl ->
                    viewModel.addNewSite(title, url, desc, categoryId, badge, iconUrl)
                }
            )
        }
    }
}

// ---------------- HEADER SECTION ----------------
@Composable
private fun HeaderBrandSection(
    favoriteCount: Int,
    historyCount: Int,
    totalResourceCount: Int,
    onOpenFavorites: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenTheme: () -> Unit,
    onOpenAddSite: () -> Unit,
    onTriggerSplash: () -> Unit,
    cloudMarquee: com.example.data.remote.MarqueeDto? = null,
    cloudIpMonitor: com.example.data.remote.IpMonitorDto? = null,
    cloudAppName: String = "懒得找了",
    cloudLogo: String = "",
    // v1.1.4 主题分支：组件级定制（home_header）
    componentThemes: Map<String, String> = emptyMap()
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    // v1.1.4：组件定制覆盖（home_header）
    val headerComp = com.example.ui.components.ComponentThemeResolver.resolve(componentThemes, "home_header")

    val headerShape = RoundedCornerShape(headerComp?.cornerRadius ?: 28.dp)
    val headerBorderBrush = Brush.linearGradient(
        listOf(
            Color.White.copy(alpha = 0.94f),
            Color.White.copy(alpha = 0.45f),
            Color(0xFFDE2910).copy(alpha = 0.45f),
            Color.White.copy(alpha = 0.90f)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = headerShape)
            .clip(headerShape)
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.56f),
                        Color(0xFFF6F2FF).copy(alpha = 0.38f),
                        Color(0xFFFFEEF5).copy(alpha = 0.42f),
                        Color.White.copy(alpha = 0.60f)
                    )
                )
            )
            .border(
                width = 1.6.dp,
                brush = headerBorderBrush,
                shape = headerShape
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // v1.1.12：删除「盛世华诞」顶部横幅标（用户要求）
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Official Brand Logo（云端可更换软件图标）
                if (cloudLogo.isNotBlank()) {
                    coil.compose.AsyncImage(
                        model = cloudLogo,
                        contentDescription = "软件图标",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_brand_logo),
                        contentDescription = "软件图标",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    AnimatedBrandTitle(text = cloudAppName)
                    Spacer(modifier = Modifier.height(2.dp))
                    DynamicOnlineCountWidget(
                        totalResourceCount = totalResourceCount,
                        primaryColor = primaryColor
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 右侧区域：迷你3D翻牌时钟（背景与软件实时同步）
                HeaderMiniFlipClock(
                    primaryColor = primaryColor
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 首页置顶 · 实时 IP 定位监控（后台控制台可开关/配 URL，只显示定位 IP）
            IpLocationMonitorWidget(
                cloudIpMonitor = cloudIpMonitor,
                modifier = Modifier.fillMaxWidth()
            )

            // 24小时跑马灯公告（云端控制台可开关、自定义图标与逐小时文案）
            IpMonitorWidget(
                modifier = Modifier.fillMaxWidth(),
                cloudMarquee = cloudMarquee
            )
        }
    }
}

/**
 * 顶部 "懒得找了" 动态品牌标题：流光渐变与呼吸微动效
 */
@Composable
fun AnimatedBrandTitle(
    modifier: Modifier = Modifier,
    text: String = "懒得找了"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "brand_shimmer_transition")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "brand_shimmer_offset"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle_scale"
    )

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF3B30),
            Color(0xFFFF8C00),
            Color(0xFFFFCC00),
            Color(0xFFFF2D55),
            Color(0xFFFF3B30)
        ),
        start = Offset(shimmerOffset, 0f),
        end = Offset(shimmerOffset + 240f, 60f)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Black,
            fontSize = 17.5.sp,
            style = TextStyle(brush = gradientBrush),
            letterSpacing = 0.5.sp
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFDE2910))
                .border(0.6.dp, Color(0xFFFFD700), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text("精选", color = Color(0xFFFFD700), fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
        }
        Text(
            text = "✨",
            fontSize = 12.sp,
            modifier = Modifier.scale(scale)
        )
    }
}

/**
 * 顶部 3D 翻牌时钟（修复日期变化动画重叠与卡顿 + GPU 硬件加速层 + requestAnimationFrame 帧同步平滑秒数翻页）：
 * - 启动及每 60 秒自动向多节点网络时间源（淘宝/苏宁/百度/腾讯/Cloudflare HTTP Date）校准真实网络时间戳；
 * - 采用 `withFrameNanos`（Compose 原生 `requestAnimationFrame` VSYNC 帧回调）平滑驱动整秒检测与翻页插值，消除定时器漂移；
 * - 日期变化（跨分钟/跨天/网络时间校准跳变）时：公历与农历在后台按「天」粒度预计算，且在翻页动画进行中严禁穿插日期重组，彻底消除动画重叠与主线程卡顿；
 * - 翻牌容器与翻转叶片启用 `CompositingStrategy.Offscreen` 硬件加速离屏合成（等效 CSS `will-change: transform; transform: translateZ(0); backface-visibility: hidden`）。
 */
@Composable
fun HeaderMiniFlipClock(
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val bgColor = MaterialTheme.colorScheme.background
    val beijingTz = remember { java.util.TimeZone.getTimeZone("Asia/Shanghai") }

    // 判断当前软件背景/主题亮度，实时同步翻牌卡片上下半页与边框色彩
    val luminance = (surfaceColor.red * 0.299f + surfaceColor.green * 0.587f + surfaceColor.blue * 0.114f)
    val isDarkBg = luminance < 0.45f

    // 与软件背景实时同步的上下半页配色（增加明显立体明暗层次与翻折阴影，使翻页折痕和立体翻片一眼可见）
    val topHalfBg = if (isDarkBg) {
        Color(
            red = (surfaceColor.red * 0.86f + primaryColor.red * 0.14f).coerceIn(0f, 1f),
            green = (surfaceColor.green * 0.86f + primaryColor.green * 0.14f).coerceIn(0f, 1f),
            blue = (surfaceColor.blue * 0.86f + primaryColor.blue * 0.14f).coerceIn(0f, 1f),
            alpha = 0.98f
        )
    } else {
        Color(
            red = (bgColor.red * 0.94f + primaryColor.red * 0.06f).coerceIn(0f, 1f),
            green = (bgColor.green * 0.94f + primaryColor.green * 0.06f).coerceIn(0f, 1f),
            blue = (bgColor.blue * 0.94f + primaryColor.blue * 0.06f).coerceIn(0f, 1f),
            alpha = 0.98f
        )
    }

    val bottomHalfBg = if (isDarkBg) {
        Color(
            red = (surfaceColor.red * 0.62f + primaryColor.red * 0.38f).coerceIn(0f, 1f),
            green = (surfaceColor.green * 0.62f + primaryColor.green * 0.38f).coerceIn(0f, 1f),
            blue = (surfaceColor.blue * 0.62f + primaryColor.blue * 0.38f).coerceIn(0f, 1f),
            alpha = 0.98f
        )
    } else {
        Color(
            red = (bgColor.red * 0.72f + primaryColor.red * 0.28f).coerceIn(0f, 1f),
            green = (bgColor.green * 0.72f + primaryColor.green * 0.28f).coerceIn(0f, 1f),
            blue = (bgColor.blue * 0.72f + primaryColor.blue * 0.28f).coerceIn(0f, 1f),
            alpha = 0.98f
        )
    }

    val digitColor = if (isDarkBg) Color(0xFFEEEEEE) else primaryColor
    val splitLineColor = if (isDarkBg) Color.Black.copy(alpha = 0.85f) else primaryColor.copy(alpha = 0.48f)
    val cardBorderColor = primaryColor.copy(alpha = 0.45f)

    // 网络时间偏移量（毫秒）：真实网络时间 - System.currentTimeMillis()
    var networkOffsetMs by remember { androidx.compose.runtime.mutableLongStateOf(0L) }
    var isNetTimeSynced by remember { mutableStateOf(false) }

    // 后台协程定期向权威网络时间源校准真实时间（平滑渐进修正，防止校准瞬间打断正在进行的秒数翻页动画）
    LaunchedEffect(Unit) {
        while (true) {
            val fetchedEpochMs = withContext(Dispatchers.IO) {
                fetchAccurateNetworkEpochMillis()
            }
            if (fetchedEpochMs != null && fetchedEpochMs > 1700000000000L) {
                val newOffset = fetchedEpochMs - System.currentTimeMillis()
                // 若偏移量微差小于 150ms 则保持稳定不乱跳，防止引发额外重复翻页
                if (!isNetTimeSynced || kotlin.math.abs(newOffset - networkOffsetMs) > 150L) {
                    networkOffsetMs = newOffset
                }
                isNetTimeSynced = true
            }
            delay(60_000L) // 每 60 秒重新校准一次网络时间
        }
    }

    // requestAnimationFrame (withFrameNanos) 平滑整秒检测：
    // 在接近整秒边界时自动切入 VSYNC 逐帧对齐，精准在帧首触发秒数翻转，杜绝 delay 误差导致的跳秒或卡顿
    var currentEpochSec by remember {
        androidx.compose.runtime.mutableLongStateOf((System.currentTimeMillis() + networkOffsetMs) / 1000L)
    }

    LaunchedEffect(networkOffsetMs) {
        while (true) {
            val nowMs = System.currentTimeMillis() + networkOffsetMs
            val remainderMs = nowMs % 1000L
            val remainToNextSec = 1000L - remainderMs
            if (remainToNextSec > 48L) {
                // 距离下一秒尚远时先休眠至帧前窗口，节省 CPU
                delay(remainToNextSec - 32L)
            }
            // 进入 requestAnimationFrame (withFrameNanos) VSYNC 硬件帧同步环，精确捕捉整秒翻转帧
            androidx.compose.runtime.withFrameNanos {
                val frameNowMs = System.currentTimeMillis() + networkOffsetMs
                val frameSec = frameNowMs / 1000L
                if (frameSec != currentEpochSec) {
                    currentEpochSec = frameSec
                }
            }
        }
    }

    // 复用单个 Calendar 实例计算时分秒，避免每秒分配新对象导致 GC 抖动
    val timeCalcCal = remember(beijingTz) { Calendar.getInstance(beijingTz) }
    val currentTimeStr = remember(currentEpochSec) {
        timeCalcCal.timeInMillis = currentEpochSec * 1000L
        val h = timeCalcCal.get(Calendar.HOUR_OF_DAY)
        val m = timeCalcCal.get(Calendar.MINUTE)
        val s = timeCalcCal.get(Calendar.SECOND)
        charArrayOf(
            ('0'.code + h / 10).toChar(),
            ('0'.code + h % 10).toChar(),
            ('0'.code + m / 10).toChar(),
            ('0'.code + m % 10).toChar(),
            ('0'.code + s / 10).toChar(),
            ('0'.code + s % 10).toChar()
        ).concatToString()
    }

    // 按东八区「自然日」粒度标识（仅在跨天 00:00:00 或首次校准跨天时才变化，彻底避免每分钟/每秒触发日期与农历重算）
    val currentBeijingDayKey = remember(currentEpochSec) {
        (currentEpochSec + 8L * 3600L) / 86_400L
    }

    var solarDateWeekText by remember {
        mutableStateOf(formatSolarDateWeekText(currentEpochSec * 1000L, beijingTz))
    }
    var lunarDateText by remember {
        mutableStateOf(formatChineseLunarDate(currentEpochSec * 1000L, beijingTz))
    }

    // 日期变化时：延迟到 6 张时分秒翻牌动画结束后（520ms 后）在后台线程异步更新公历与农历，
    // 彻底避免 00:00:00 日期跳变瞬间与 6 张翻页卡片同时重组造成的动画重叠与掉帧卡顿
    LaunchedEffect(currentBeijingDayKey) {
        val targetMillis = currentEpochSec * 1000L
        // 若正处于跨天整点（00:00:00），先礼让 500ms 确保 6 张数字翻页动画独占 GPU 满帧跑完
        val secOfDay = (currentEpochSec + 8L * 3600L) % 86_400L
        if (secOfDay == 0L) {
            delay(500L)
        }
        val (newSolar, newLunar) = withContext(Dispatchers.Default) {
            formatSolarDateWeekText(targetMillis, beijingTz) to
                formatChineseLunarDate(targetMillis, beijingTz)
        }
        solarDateWeekText = newSolar
        lunarDateText = newLunar
    }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(vertical = 1.dp)
            // 硬件加速合成层（等效 CSS transform: translate3d(0,0,0); will-change: transform）
            .graphicsLayer {
                compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.ModulateAlpha
            }
            .testTag("header_mini_flip_clock")
    ) {
        // 1. 放大版 3D 翻牌时钟主体（h1 h2 : m1 m2 : s1 s2）
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.5.dp)
        ) {
            // Group 1: h1, h2
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                FlipDigitCard(
                    digit = currentTimeStr.getOrElse(0) { '0' },
                    topHalfBg = topHalfBg,
                    bottomHalfBg = bottomHalfBg,
                    digitColor = digitColor,
                    splitLineColor = splitLineColor,
                    borderColor = cardBorderColor
                )
                FlipDigitCard(
                    digit = currentTimeStr.getOrElse(1) { '0' },
                    topHalfBg = topHalfBg,
                    bottomHalfBg = bottomHalfBg,
                    digitColor = digitColor,
                    splitLineColor = splitLineColor,
                    borderColor = cardBorderColor
                )
            }

            // Colon :
            Text(
                text = ":",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = digitColor,
                modifier = Modifier.padding(horizontal = 0.5.dp)
            )

            // Group 2: m1, m2
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                FlipDigitCard(
                    digit = currentTimeStr.getOrElse(2) { '0' },
                    topHalfBg = topHalfBg,
                    bottomHalfBg = bottomHalfBg,
                    digitColor = digitColor,
                    splitLineColor = splitLineColor,
                    borderColor = cardBorderColor
                )
                FlipDigitCard(
                    digit = currentTimeStr.getOrElse(3) { '0' },
                    topHalfBg = topHalfBg,
                    bottomHalfBg = bottomHalfBg,
                    digitColor = digitColor,
                    splitLineColor = splitLineColor,
                    borderColor = cardBorderColor
                )
            }

            // Colon :
            Text(
                text = ":",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = digitColor,
                modifier = Modifier.padding(horizontal = 0.5.dp)
            )

            // Group 3: s1, s2
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                FlipDigitCard(
                    digit = currentTimeStr.getOrElse(4) { '0' },
                    topHalfBg = topHalfBg,
                    bottomHalfBg = bottomHalfBg,
                    digitColor = digitColor,
                    splitLineColor = splitLineColor,
                    borderColor = cardBorderColor
                )
                FlipDigitCard(
                    digit = currentTimeStr.getOrElse(5) { '0' },
                    topHalfBg = topHalfBg,
                    bottomHalfBg = bottomHalfBg,
                    digitColor = digitColor,
                    splitLineColor = splitLineColor,
                    borderColor = cardBorderColor
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        // 2. 公历：年月日 + 星期几（带网络校准状态小圆点）
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(if (isNetTimeSynced) Color(0xFF10B981) else primaryColor.copy(alpha = 0.6f))
            )
            Text(
                text = solarDateWeekText,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f),
                maxLines = 1
            )
        }

        // 3. 农历 / 阴历：干支生肖年 + 农历月日
        Text(
            text = lunarDateText,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = primaryColor.copy(alpha = 0.92f),
            maxLines = 1
        )
    }
}

/**
 * 格式化公历「YYYY年MM月DD日 星期X」
 */
private fun formatSolarDateWeekText(epochMillis: Long, timeZone: java.util.TimeZone): String {
    val cal = Calendar.getInstance(timeZone).apply {
        timeInMillis = epochMillis
    }
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH) + 1
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val weekNames = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
    val weekStr = weekNames[(cal.get(Calendar.DAY_OF_WEEK) - 1).coerceIn(0, 6)]
    return String.format(Locale.CHINA, "%04d年%02d月%02d日 %s", year, month, day, weekStr)
}

/**
 * 从多个高可用网络接口同步真实 UTC 毫秒时间戳（多源兜底：淘宝 NTP API -> 苏宁时间 API -> 百度/腾讯/Cloudflare HTTP Date 响应头）
 */
private fun fetchAccurateNetworkEpochMillis(): Long? {
    // 1. JSON 时间戳接口优先（毫秒级精度）
    val jsonEndpoints = listOf(
        "https://acs.m.taobao.com/gw/mtop.common.getTimestamp/",
        "https://f.m.suning.com/api/ct.do"
    )
    for (endpoint in jsonEndpoints) {
        try {
            val conn = (java.net.URL(endpoint).openConnection() as java.net.HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 2500
                readTimeout = 2500
                useCaches = false
                setRequestProperty("Cache-Control", "no-cache")
            }
            val startReq = System.currentTimeMillis()
            if (conn.responseCode in 200..299) {
                val body = conn.inputStream.bufferedReader().use { it.readText() }
                val rttHalf = (System.currentTimeMillis() - startReq) / 2L
                // 淘宝返回 {"data":{"t":"172809..."}}，苏宁返回 {"currentTime":172809...}
                val match = Regex("""(?:"t"\s*:\s*"|"currentTime"\s*:\s*)(\d{13})""").find(body)
                val ts = match?.groupValues?.getOrNull(1)?.toLongOrNull()
                if (ts != null && ts > 1700000000000L) {
                    conn.disconnect()
                    return ts + rttHalf
                }
            }
            conn.disconnect()
        } catch (_: Exception) {
        }
    }

    // 2. 权威大厂 HTTP 响应头 Date 字段兜底（精确到秒）
    val headEndpoints = listOf(
        "https://www.baidu.com",
        "https://qq.com",
        "https://www.cloudflare.com",
        "https://www.apple.com"
    )
    for (endpoint in headEndpoints) {
        try {
            val conn = (java.net.URL(endpoint).openConnection() as java.net.HttpURLConnection).apply {
                requestMethod = "HEAD"
                connectTimeout = 2500
                readTimeout = 2500
                useCaches = false
            }
            val startReq = System.currentTimeMillis()
            conn.connect()
            val serverDate = conn.date
            val rttHalf = (System.currentTimeMillis() - startReq) / 2L
            conn.disconnect()
            if (serverDate > 1700000000000L) {
                return serverDate + rttHalf
            }
        } catch (_: Exception) {
        }
    }
    return null
}

/**
 * 计算并格式化中国传统农历/阴历字符串（使用 Android ICU ChineseCalendar，精准支持干支纪年、十二生肖、闰月及传统初一至三十表达）
 */
private fun formatChineseLunarDate(epochMillis: Long, timeZone: java.util.TimeZone): String {
    return try {
        val chineseCal = android.icu.util.ChineseCalendar(
            android.icu.util.TimeZone.getTimeZone(timeZone.id),
            Locale.CHINA
        ).apply {
            timeInMillis = epochMillis
        }

        val stems = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
        val branches = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
        val zodiacs = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")

        // ChineseCalendar.YEAR 返回 1..60 的甲子循环序数
        val cycleYear = (chineseCal.get(android.icu.util.ChineseCalendar.YEAR) - 1).coerceAtLeast(0)
        val stem = stems[cycleYear % 10]
        val branch = branches[cycleYear % 12]
        val zodiac = zodiacs[cycleYear % 12]

        val isLeapMonth = chineseCal.get(android.icu.util.ChineseCalendar.IS_LEAP_MONTH) == 1
        val lunarMonth = chineseCal.get(android.icu.util.ChineseCalendar.MONTH) // 0..11
        val lunarDay = chineseCal.get(android.icu.util.ChineseCalendar.DAY_OF_MONTH) // 1..30

        val monthNames = arrayOf("正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊")
        val monthStr = (if (isLeapMonth) "闰" else "") + monthNames[lunarMonth.coerceIn(0, 11)] + "月"

        val dayNames = arrayOf(
            "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
        )
        val dayStr = dayNames[(lunarDay - 1).coerceIn(0, 29)]

        "农历(阴历) ${stem}${branch}${zodiac}年·${monthStr}${dayStr}"
    } catch (_: Throwable) {
        "农历(阴历) 实时同步"
    }
}

/**
 * 单个数字 3D 翻牌组件（硬件加速 CSS 属性映射 + requestAnimationFrame 帧级平滑插值 + 防动画重叠保护）：
 * - 硬件加速属性：容器与翻动叶片开启 `CompositingStrategy.Offscreen` 硬件纹理缓存与 `clip = true`（等效 CSS `will-change: transform; transform: translate3d(0,0,0); backface-visibility: hidden; transform-style: preserve-3d`）；
 * - 防动画重叠：当新数字到达时，若上一次翻转尚未结束，立即在单帧内将上一轮收尾归位（`fromDigit = toDigit`），随后基于 `withFrameNanos`（`requestAnimationFrame`）逐帧驱动 `0° -> -180°` 3D 翻转，彻底杜绝多个翻转图层叠加。
 */
@Composable
private fun FlipDigitCard(
    digit: Char,
    topHalfBg: Color,
    bottomHalfBg: Color,
    digitColor: Color,
    splitLineColor: Color,
    borderColor: Color
) {
    // fromDigit：翻动前的旧数字；toDigit：翻动后的目标新数字
    var fromDigit by remember { mutableStateOf(digit) }
    var toDigit by remember { mutableStateOf(digit) }
    // 使用 FloatState 配合 withFrameNanos (requestAnimationFrame) 实现零协程中断开销的 60/120Hz 帧级平滑插值
    var rotationXState by remember { androidx.compose.runtime.mutableFloatStateOf(0f) }

    LaunchedEffect(digit) {
        if (digit != toDigit) {
            // 防止快速跳变或跨天多卡同时翻转时出现动画重叠：先立即锁定上一状态为起点
            fromDigit = toDigit
            toDigit = digit
            rotationXState = 0f

            val durationNs = 440_000_000L // 440ms，留足 560ms 余量防止下一秒重入重叠
            var startFrameNs = 0L

            // requestAnimationFrame (withFrameNanos) 平滑循环
            while (true) {
                val finished = androidx.compose.runtime.withFrameNanos { frameTimeNs ->
                    if (startFrameNs == 0L) startFrameNs = frameTimeNs
                    val elapsedNs = (frameTimeNs - startFrameNs).coerceAtLeast(0L)
                    val rawProgress = (elapsedNs.toFloat() / durationNs.toFloat()).coerceIn(0f, 1f)
                    // 拟真重力加速缓动曲线（等效 CSS cubic-bezier(0.35, 0.0, 0.25, 1.0)）
                    val eased = cubicBezierSmoothFlip(rawProgress)
                    rotationXState = -180f * eased
                    rawProgress >= 1f
                }
                if (finished) break
            }

            // 动画完成收尾：静态上下半页同步为新数字，复位旋转角
            rotationXState = 0f
            fromDigit = toDigit
        }
    }

    val cardWidth = 17.5.dp
    val cardHeight = 26.dp
    val halfHeight = 13.dp
    val cornerRadius = 3.5.dp
    val digitFontSize = 18.sp

    // 是否正处于翻页动画过程中（derivedStateOf 仅在布尔状态切换时触发重组，中间旋转帧全走 GPU graphicsLayer）
    val isFlipping by remember {
        androidx.compose.runtime.derivedStateOf {
            rotationXState < -0.5f && rotationXState > -179.5f
        }
    }
    val isTopFlapPhase by remember {
        androidx.compose.runtime.derivedStateOf {
            rotationXState >= -90f
        }
    }

    Box(
        modifier = Modifier
            .size(width = cardWidth, height = cardHeight)
            // 启用 GPU 硬件加速纹理合成层（等效 CSS will-change: transform; transform: translateZ(0)）
            .graphicsLayer {
                compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen
                clip = true
                shape = RoundedCornerShape(cornerRadius)
            }
            .shadow(1.5.dp, RoundedCornerShape(cornerRadius))
            .clip(RoundedCornerShape(cornerRadius))
            .border(0.8.dp, borderColor, RoundedCornerShape(cornerRadius))
    ) {
        // 1. 底层静态上半部分：始终显示新数字 toDigit 的上半截（当上半翻页片向下翻开时逐渐露出）
        FlipHalfSlice(
            digit = toDigit,
            isTopHalf = true,
            cardHeight = cardHeight,
            halfHeight = halfHeight,
            cornerRadius = cornerRadius,
            bgColor = topHalfBg,
            digitColor = digitColor,
            digitFontSize = digitFontSize,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // 2. 底层静态下半部分：翻页完成前显示旧数字 fromDigit 下半截，翻页落定后显示新数字 toDigit 下半截
        val staticBottomChar = if (isFlipping) fromDigit else toDigit
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(halfHeight)
                .align(Alignment.BottomCenter)
        ) {
            FlipHalfSlice(
                digit = staticBottomChar,
                isTopHalf = false,
                cardHeight = cardHeight,
                halfHeight = halfHeight,
                cornerRadius = cornerRadius,
                bgColor = bottomHalfBg,
                digitColor = digitColor,
                digitFontSize = digitFontSize
            )
            // 当翻页片在下半区翻落时，底层静态下半页叠加渐弱阴影，增强真实机械翻牌纵深感
            if (isFlipping && !isTopFlapPhase) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val progress = ((-rotationXState - 90f) / 90f).coerceIn(0f, 1f)
                            alpha = (1f - progress) * 0.28f
                        }
                        .background(Color.Black)
                )
            }
        }

        // 3. 动态 3D 翻动页片 (.num.flip)：启用独立 GPU 离屏合成与背面剔除（backface-visibility: hidden）
        if (isFlipping) {
            if (isTopFlapPhase) {
                // 阶段 A（0° -> -90°）：上半页片沿中轴线向下翻转，正面显示旧数字 fromDigit 上半截
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(halfHeight)
                        .align(Alignment.TopCenter)
                        .graphicsLayer {
                            compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen
                            this.rotationX = rotationXState
                            this.transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f)
                            this.cameraDistance = 4.5f * density
                        }
                ) {
                    FlipHalfSlice(
                        digit = fromDigit,
                        isTopHalf = true,
                        cardHeight = cardHeight,
                        halfHeight = halfHeight,
                        cornerRadius = cornerRadius,
                        bgColor = topHalfBg,
                        digitColor = digitColor,
                        digitFontSize = digitFontSize
                    )
                    // 随翻转角度加深的动态背光阴影 + 翻页上沿高光边
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                            .graphicsLayer {
                                val progress = (-rotationXState / 90f).coerceIn(0f, 1f)
                                alpha = progress * 0.35f
                            }
                            .background(Color.Black)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.2.dp)
                            .align(Alignment.TopCenter)
                            .background(primaryColorForFlapEdge(digitColor))
                    )
                }
            } else {
                // 阶段 B（-90° -> -180°）：下半页片从中轴线向下合拢，背面显示新数字 toDigit 下半截
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(halfHeight)
                        .align(Alignment.BottomCenter)
                        .graphicsLayer {
                            compositingStrategy = androidx.compose.ui.graphics.CompositingStrategy.Offscreen
                            this.rotationX = rotationXState + 180f
                            this.transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f)
                            this.cameraDistance = 4.5f * density
                        }
                ) {
                    FlipHalfSlice(
                        digit = toDigit,
                        isTopHalf = false,
                        cardHeight = cardHeight,
                        halfHeight = halfHeight,
                        cornerRadius = cornerRadius,
                        bgColor = bottomHalfBg,
                        digitColor = digitColor,
                        digitFontSize = digitFontSize
                    )
                    // 随合拢过程消退的立体阴影 + 翻页下沿高光边
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
                            .graphicsLayer {
                                val remain = ((180f + rotationXState) / 90f).coerceIn(0f, 1f)
                                alpha = remain * 0.32f
                            }
                            .background(Color.Black)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.2.dp)
                            .align(Alignment.BottomCenter)
                            .background(primaryColorForFlapEdge(digitColor))
                    )
                }
            }
        }

        // 4. 中间机械轴折痕黑线 (.nums:before)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.1.dp)
                .align(Alignment.Center)
                .background(splitLineColor)
        )
    }
}

/**
 * 专门针对 3D 翻牌时钟调优的平滑缓动函数（模拟物理重力加速落下并在末端柔和贴合）
 */
private fun cubicBezierSmoothFlip(t: Float): Float {
    val clamped = t.coerceIn(0f, 1f)
    // 前半段重力加速，后半段平滑落定（无回弹抖动、无线性生硬感）
    return if (clamped < 0.5f) {
        2.2f * clamped * clamped
    } else {
        val inv = 1f - clamped
        1f - 2.2f * inv * inv
    }.coerceIn(0f, 1f)
}

private fun primaryColorForFlapEdge(digitColor: Color): Color =
    digitColor.copy(alpha = 0.55f)

/**
 * 渲染单张翻牌的上半截或下半截：
 * 使用 clipToBounds + 固定全高居中容器偏置，严格只裁出完整大数字的上 50% 或下 50%，保证上下拼合严丝合缝。
 */
@Composable
private fun FlipHalfSlice(
    digit: Char,
    isTopHalf: Boolean,
    cardHeight: androidx.compose.ui.unit.Dp,
    halfHeight: androidx.compose.ui.unit.Dp,
    cornerRadius: androidx.compose.ui.unit.Dp,
    bgColor: Color,
    digitColor: Color,
    digitFontSize: androidx.compose.ui.unit.TextUnit,
    modifier: Modifier = Modifier
) {
    val shape = if (isTopHalf) {
        RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
    } else {
        RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(halfHeight)
            .clip(shape)
            .background(bgColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(
                    align = if (isTopHalf) Alignment.Top else Alignment.Bottom,
                    unbounded = true
                )
                .height(cardHeight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = digit.toString(),
                color = digitColor,
                fontSize = digitFontSize,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = digitFontSize
            )
        }
    }
}

/**
 * 真实感在线人数动态增减组件：按时段拟真、微小波动、呼吸绿点与即时增减浮标
 */
@Composable
fun DynamicOnlineCountWidget(
    totalResourceCount: Int,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    // 根据一天中不同时间段计算真实基准在线人数（白天约1200-1400，晚高峰约1500-1800，深夜约400-600）
    val baseCount = remember(hour) {
        when (hour) {
            in 0..6 -> 460 + (hour * 40)
            in 7..11 -> 880 + ((hour - 7) * 90)
            in 12..17 -> 1260 + ((hour - 12) * 40)
            in 18..22 -> 1520 + ((hour - 18) * 55)
            else -> 1050
        }
    }

    var onlineCount by remember { mutableIntStateOf(baseCount + Random.nextInt(-18, 22)) }
    var lastDelta by remember { mutableIntStateOf(0) }

    // 每 2.8 ~ 4.8 秒自然增减波动 (+1, -1, +2, -2, +3...)
    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2800, 4800))
            val delta = Random.nextInt(-3, 5)
            if (delta != 0) {
                lastDelta = delta
                onlineCount = (onlineCount + delta).coerceIn(300, 2800)
            }
        }
    }

    // 实时状态绿点呼吸动效
    val pulseTransition = rememberInfiniteTransition(label = "pulse_live_dot")
    val dotAlpha by pulseTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            // v1.1.15：禁止点击在线人数（不再弹出 Toast 弹窗，用户要求）
            .padding(vertical = 1.dp)
    ) {
        Text(
            text = "收录 $totalResourceCount+ 华夏宝藏",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDE2910)
        )
    }
}

// ---------------- SEARCH SECTION ----------------
@Composable
private fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    // v1.1.4 主题分支：组件级定制（search_box）
    componentThemes: Map<String, String> = emptyMap(),
    // v1.2.6：搜索框占位文字（控制台 UI 文本可覆盖）
    placeholderText: String? = null
) {
    val uiverse = LocalUiverseState.current
    val primaryColor = MaterialTheme.colorScheme.primary
    // v1.1.4：组件定制覆盖（search_box）
    val searchComp = com.example.ui.components.ComponentThemeResolver.resolve(componentThemes, "search_box")
    val inputShape = when (uiverse.inputStyle) {
        InputStylePreset.CYBER_TERMINAL -> RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomEnd = 0.dp, bottomStart = 12.dp)
        InputStylePreset.GLASS_INSET -> RoundedCornerShape(16.dp)
        InputStylePreset.NEO_BRUTALIST_BOX -> RoundedCornerShape(6.dp)
        InputStylePreset.CUSTOM -> RoundedCornerShape(uiverse.customStyle?.cornerRadius ?: 28.dp)
        else -> RoundedCornerShape(28.dp)
    }.let { if (searchComp != null) RoundedCornerShape(searchComp.cornerRadius) else it }
    val containerColor = when (uiverse.inputStyle) {
        InputStylePreset.CYBER_TERMINAL -> Color(0xFF0F101A)
        InputStylePreset.GLASS_INSET -> Color(0x55FFFFFF)
        InputStylePreset.NEO_BRUTALIST_BOX -> Color.White
        InputStylePreset.CUSTOM -> uiverse.customStyle?.backgroundColor ?: Color.White.copy(alpha = 0.56f)
        else -> Color.White.copy(alpha = 0.56f)
    }.let { if (searchComp?.backgroundColor != null) searchComp.backgroundColor else it }
    val focusedBorderColor = when (uiverse.inputStyle) {
        InputStylePreset.CYBER_TERMINAL -> Color(0xFF00F0FF)
        InputStylePreset.NEO_BRUTALIST_BOX -> Color.Black
        InputStylePreset.CUSTOM -> uiverse.customStyle?.borderColor?.takeIf { it != Color.Transparent } ?: Color.White.copy(alpha = 0.92f)
        else -> Color.White.copy(alpha = 0.92f)
    }
    val unfocusedBorderColor = when (uiverse.inputStyle) {
        InputStylePreset.CYBER_TERMINAL -> Color(0xFF00F0FF).copy(alpha = 0.4f)
        InputStylePreset.NEO_BRUTALIST_BOX -> Color.Black
        InputStylePreset.GLASS_INSET -> Color(0x88FFFFFF)
        else -> Color.White.copy(alpha = 0.78f)
    }

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(placeholderText?.ifBlank { "🔍 探索全网精选资源、AI、实用工具、影视..." } ?: "🔍 探索全网精选资源、AI、实用工具、影视...", fontSize = 13.sp, maxLines = 1)
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "搜索", tint = Color(0xFFDE2910))
        },
        trailingIcon = {
            // v1.1.15：固定占位宽度——避免输入时清除按钮出现导致输入框宽度突变、光标跳到文字前
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "清除")
                    }
                }
            }
        },
        singleLine = true,
        shape = inputShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("search_text_input")
    )
}

// ---------------- CATEGORY TAGS EXPANSION SHEET CONTENT ----------------
@Composable
private fun CategoryTagsSheetContent(
    categories: List<com.example.data.model.NavCategory>,
    selectedCategoryId: String,
    totalResourceCount: Int,
    onCategorySelect: (String) -> Unit,
    onRollLucky: () -> Unit,
    onClose: () -> Unit,
    // v1.2.6：文字可云端覆盖
    uiText: com.example.data.remote.UiTextDto? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .padding(horizontal = 16.dp)
    ) {
        // Sheet Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(listOf(FlameRed, SunsetOrange))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Extension,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "站点分类标签",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "点击分类标签直达对应收录资源 (${categories.size}大分类 · 共${totalResourceCount}个站点)",
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "关闭")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Top Quick Action Row: "全部资源" & "随心抽一个"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val isAllSelected = selectedCategoryId.isBlank()
            OutlinedButton(
                onClick = { onCategorySelect("") },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isAllSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent
                ),
                border = BorderStroke(
                    1.2.dp,
                    if (isAllSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1.2f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isAllSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = (uiText?.allSites?.ifBlank { "全部站点" } ?: "全部站点") + " ($totalResourceCount)",
                    fontSize = 12.5.sp,
                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isAllSelected) ChinaRed else MaterialTheme.colorScheme.onSurface
                )
            }

            Button(
                onClick = onRollLucky,
                colors = ButtonDefaults.buttonColors(containerColor = SunsetOrange),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(uiText?.luckyButton?.ifBlank { "随心抽一个" } ?: "随心抽一个", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Categories Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            items(categories, key = { it.id }) { cat ->
                val isSelected = cat.id == selectedCategoryId
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        else
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    ),
                    border = BorderStroke(
                        if (isSelected) 1.5.dp else 1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelect(cat.id) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when {
                                    cat.name.contains("AI") || cat.name.contains("大模型") -> Icons.Filled.Psychology
                                    cat.name.contains("画布") || cat.name.contains("设计") -> Icons.Filled.ColorLens
                                    cat.name.contains("代码") || cat.name.contains("编程") -> Icons.Filled.Terminal
                                    cat.name.contains("视频") || cat.name.contains("影音") -> Icons.Filled.Extension
                                    else -> Icons.Filled.Bookmark
                                },
                                contentDescription = null,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = cat.name,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${cat.cards.size} 个站点",
                                fontSize = 10.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- LUCKY DRAW SHEET CONTENT ----------------
@Composable
private fun LuckyDrawSheetContent(
    card: NavCard,
    categories: List<com.example.data.model.NavCategory>,
    selectedCategoryId: String,
    onCategorySelect: (String) -> Unit,
    onReroll: (String?) -> Unit,
    onOpen: () -> Unit,
    onFavorite: () -> Unit,
    isFavorite: Boolean,
    favoriteUrls: Set<String> = emptySet(),
    onCardClick: (NavCard) -> Unit = {},
    onFavoriteToggle: (NavCard) -> Unit = {},
    onClose: () -> Unit,
    // v1.2.6：控制台 UI 文本覆盖（标题/按钮文字可云端自定义）
    uiText: com.example.data.remote.UiTextDto? = null
) {
    var currentCatId by remember(selectedCategoryId) { mutableStateOf(selectedCategoryId) }
    var showCategorySitesDialog by remember { mutableStateOf(false) }

    val activeCatName = categories.find { it.id == currentCatId }?.name
    val activeCatTotalCount = if (currentCatId.isBlank()) {
        categories.sumOf { it.cards.size }
    } else {
        categories.find { it.id == currentCatId }?.cards?.size ?: 0
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                // v1.2.6：默认「分类列表」（原懒人随心抽更名），控制台可云端覆盖标题
                text = uiText?.luckyTitle?.ifBlank { "✨ 分类列表 · 今日宝藏" } ?: "✨ 分类列表 · 今日宝藏",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = ChinaRed
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "关闭")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 分类标签区域 (采用独立弹窗呈现所有分类与站点内容)
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFFFF0DF),
            border = BorderStroke(1.dp, Color(0xFFFFD7B2)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showCategorySitesDialog = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ChinaRed.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Extension,
                            contentDescription = null,
                            tint = ChinaRed,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "抽选分类",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (activeCatName != null) activeCatName else "全站宝藏",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChinaRed
                            )
                        }
                        Text(
                            text = "已覆盖 $activeCatTotalCount 个精品站点 · 点击浏览所有分类站点",
                            fontSize = 10.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ChinaRed)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = uiText?.selectCategory?.ifBlank { "选择分类" } ?: "选择分类",
                        color = Color.White,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Highlight Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(listOf(ChinaRed, SunsetOrange)),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(18.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = card.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // v1.1.12：删除所有角标功能（用户要求）
                }

                // 所属分类标签显示 (点击也可直接打开独立分类站点弹窗)
                val belongCat = categories.find { it.id == card.categoryId }?.name ?: card.categoryId
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            .clickable {
                                currentCatId = card.categoryId
                                showCategorySitesDialog = true
                            }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "分类 · $belongCat (点击查看分类站点)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = card.desc.ifBlank { "优质实用工具 / 宝藏影视导航站点" },
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = card.url,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { onReroll(currentCatId.ifBlank { null }) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp)
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(uiText?.rerollButton?.ifBlank { "再抽一次" } ?: "再抽一次")
            }

            Button(
                onClick = onOpen,
                modifier = Modifier.weight(1.3f),
                colors = ButtonDefaults.buttonColors(containerColor = ChinaRed),
                shape = RoundedCornerShape(22.dp)
            ) {
                Icon(Icons.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("立即直达")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // 独立分类弹窗：展示所有分类及该分类下的所有站点内容
    if (showCategorySitesDialog) {
        CategorySitesDialog(
            categories = categories,
            selectedCategoryId = currentCatId,
            onSelectCategory = { newCatId ->
                currentCatId = newCatId
                onCategorySelect(newCatId)
            },
            onCardClick = { clickedCard ->
                showCategorySitesDialog = false
                onCardClick(clickedCard)
            },
            onFavoriteToggle = onFavoriteToggle,
            favoriteUrls = favoriteUrls,
            onRollInCategory = { targetCatId ->
                currentCatId = targetCatId
                onCategorySelect(targetCatId)
                onReroll(targetCatId.ifBlank { null })
            },
            onDismiss = { showCategorySitesDialog = false }
        )
    }
}

// ---------------- FAVORITES SHEET ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesSheet(
    favorites: List<UserItemRecord>,
    onOpen: (UserItemRecord) -> Unit,
    onRemove: (UserItemRecord) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White.copy(alpha = 0.72f),
        scrimColor = Color(0xFF140D26).copy(alpha = 0.36f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "我的收藏 (${favorites.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "关闭")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无收藏，点击卡片右上角星标即可收藏！",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    favorites.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.56f),
                                            Color(0xFFF6F0FF).copy(alpha = 0.38f),
                                            Color.White.copy(alpha = 0.60f)
                                        )
                                    )
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(14.dp))
                                .clickable { onOpen(item) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SiteBrandIcon(
                                url = item.url,
                                title = item.title,
                                iconUrl = item.iconUrl ?: "",
                                size = 30.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                if (!item.desc.isNullOrBlank()) {
                                    Text(
                                        item.desc,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            IconButton(onClick = { onRemove(item) }) {
                                Icon(Icons.Filled.Bookmark, contentDescription = "移除收藏", tint = FlameRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- HISTORY SHEET ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistorySheet(
    history: List<UserItemRecord>,
    onOpen: (UserItemRecord) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White.copy(alpha = 0.72f),
        scrimColor = Color(0xFF140D26).copy(alpha = 0.36f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "浏览历史 (${history.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row {
                    if (history.isNotEmpty()) {
                        IconButton(onClick = onClear) {
                            Icon(Icons.Filled.DeleteSweep, contentDescription = "清空历史", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "关闭")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无浏览历史，点击任意资源卡片即可记录！",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    history.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.56f),
                                            Color(0xFFF6F0FF).copy(alpha = 0.38f),
                                            Color.White.copy(alpha = 0.60f)
                                        )
                                    )
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(14.dp))
                                .clickable { onOpen(item) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SiteBrandIcon(
                                url = item.url,
                                title = item.title,
                                iconUrl = item.iconUrl ?: "",
                                size = 30.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    item.url,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                Icons.Filled.OpenInBrowser,
                                contentDescription = "打开",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- CLIPBOARD & SHARING UTILS ----------------
private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("URL", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
}

private fun shareText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "分享链接")
    context.startActivity(shareIntent)
}

/**
 * v1.0.7：Skill 技能库 CSS 动态特效「NEW」角标
 * 渐变流光扫过 + 呼吸缩放动画，叠加在技能库标签右上角
 */
@Composable
private fun DynamicNewBadge(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "skill_new_badge")
    val flow by infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "badge_flow"
    )
    val breathe by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badge_breathe"
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = breathe
                scaleY = breathe
            }
            .clip(RoundedCornerShape(50))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF6B9D),
                        Color(0xFFFFB199),
                        Color(0xFFFFE08A),
                        Color(0xFFFF6B9D)
                    ),
                    start = androidx.compose.ui.geometry.Offset(flow * 300f, 0f),
                    end = androidx.compose.ui.geometry.Offset(flow * 300f + 220f, 0f)
                )
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "NEW",
            fontSize = 8.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
    }
}

