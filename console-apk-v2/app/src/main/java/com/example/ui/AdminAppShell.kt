package com.example.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AdminScreen
import com.example.model.ConnState
import com.example.model.title
import com.example.model.route
import com.example.ui.screens.AppModulesScreen
import com.example.ui.screens.ButtonManagementScreen
import com.example.ui.screens.CardManagementScreen
import com.example.ui.screens.CategoryManagementScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.OperationLogsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SkillManagementScreen
import com.example.ui.screens.TextManagementScreen
import com.example.ui.screens.ThemeKitScreen
import com.example.ui.theme.Cinnabar
import com.example.ui.theme.Gold
import com.example.ui.theme.Ink
import com.example.ui.theme.InkBlack
import com.example.ui.theme.Paper
import com.example.ui.theme.PaperSoft
import com.example.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

// ============================================================================
// 控制台主壳（Kotlin Compose 重写版）：
// ModalDrawer(侧边导航) + Scaffold(顶栏/底栏/Snackbar) + 屏幕分发
// ============================================================================

@Composable
fun AdminAppShell(viewModel: AdminViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.toastMessage) {
        val msg = uiState.toastMessage
        if (msg.isNotEmpty()) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    BackHandler(drawerState.isOpen || uiState.currentScreen != AdminScreen.DASHBOARD) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            viewModel.navigateTo(AdminScreen.DASHBOARD)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(276.dp),
                drawerContainerColor = Ink,
                drawerContentColor = Paper,
            ) {
                GuochaoSidebarContent(
                    currentScreen = uiState.currentScreen,
                    versionName = uiState.versionName,
                    onSelectScreen = { screen ->
                        viewModel.navigateTo(screen)
                        scope.launch { drawerState.close() }
                    },
                    onCloudConnect = {
                        scope.launch { drawerState.close() }
                        viewModel.showToast("已对接 GitHub 仓库 admin-data.json 云端数据中枢")
                    },
                )
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Paper,
            topBar = {
                GuochaoTopBar(
                    title = uiState.currentScreen.title,
                    connState = uiState.connState,
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onNavigateSettings = { viewModel.navigateTo(AdminScreen.SETTINGS) },
                    onApplyQuickSync = { viewModel.applyToDevice() },
                    onShowToast = { viewModel.showToast(it) },
                )
            },
            bottomBar = {
                GuochaoBottomBar(
                    currentScreen = uiState.currentScreen,
                    onSelectScreen = { viewModel.navigateTo(it) },
                    onOpenMoreDrawer = { scope.launch { drawerState.open() } },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding).fillMaxSize()) {
                when (uiState.currentScreen) {
                    AdminScreen.DASHBOARD -> DashboardScreen(
                        uiState, viewModel::setDateRange, viewModel::navigateTo,
                        viewModel::saveCard, viewModel::deleteCard, viewModel::showToast,
                    )
                    AdminScreen.CARDS -> CardManagementScreen(
                        uiState, viewModel::saveCard, viewModel::deleteCard, viewModel::showToast,
                    )
                    AdminScreen.CATEGORIES -> CategoryManagementScreen(
                        uiState, viewModel::moveCategory, viewModel::saveCategoryOrder,
                        viewModel::saveCategory, viewModel::deleteCategory, viewModel::showToast,
                    )
                    AdminScreen.BUTTONS -> ButtonManagementScreen(
                        uiState, viewModel::saveSoftware, viewModel::deleteSoftware,
                        viewModel::uploadLocalFile, viewModel::showToast,
                    )
                    AdminScreen.SKILLS -> SkillManagementScreen(
                        uiState, viewModel::saveSkill, viewModel::deleteSkill,
                        viewModel::uploadLocalFile, viewModel::showToast,
                    )
                    AdminScreen.TEXTS -> TextManagementScreen(
                        uiState, viewModel::saveText, viewModel::deleteText, viewModel::showToast,
                    )
                    AdminScreen.LOGS -> OperationLogsScreen(uiState, viewModel::showToast)
                    AdminScreen.SETTINGS -> SettingsScreen(
                        uiState,
                        { t, o, r -> viewModel.updateGithubInputs(t, o, r, uiState.githubBranch) },
                        viewModel::connectGithub,
                        { viewModel.loadAdmin(false) },
                        viewModel::applyToDevice,
                        viewModel::publishRelease,
                        viewModel::updateBasicSettingsInputs,
                        viewModel::saveBasicSettings,
                        viewModel::saveFullSettings,
                        viewModel::toggleNotification,
                    )
                    AdminScreen.APP_MODULES -> AppModulesScreen(
                        uiState, viewModel::saveSplashConfig, viewModel::saveWelcomeConfig,
                        viewModel::saveMarqueeConfig, viewModel::saveUpdateDialogAndVersionConfig,
                        viewModel::saveMiscModulesConfig,
                    )
                    AdminScreen.THEME_KIT -> ThemeKitScreen(uiState, viewModel::saveThemeKitConfig)
                    AdminScreen.ENTRIES -> DashboardScreen(
                        uiState, viewModel::setDateRange, viewModel::navigateTo,
                        viewModel::saveCard, viewModel::deleteCard, viewModel::showToast,
                    )
                }
            }
        }
    }
}

// ---------------- 顶栏 ----------------

@Composable
private fun GuochaoTopBar(
    title: String,
    connState: ConnState,
    onOpenDrawer: () -> Unit,
    onNavigateSettings: () -> Unit,
    onApplyQuickSync: () -> Unit,
    onShowToast: (String) -> Unit,
) {
    var userMenuExpanded by remember { mutableStateOf(false) }
    val connBadgeLabel = when (connState) {
        ConnState.CONNECTED -> "云端已连接"
        ConnState.READONLY -> "只读模式"
        ConnState.LOADING -> "连接中…"
        ConnState.ERROR -> "连接异常"
        ConnState.IDLE, ConnState.API -> "未连接"
    }
    val badgeColor = when (connState) {
        ConnState.CONNECTED -> Gold
        ConnState.READONLY -> GoldDark
        ConnState.LOADING -> Ink.copy(alpha = 0.6f)
        ConnState.ERROR -> Cinnabar
        ConnState.IDLE, ConnState.API -> Ink.copy(alpha = 0.45f)
    }
    Surface(Modifier.fillMaxWidth(), color = Paper.copy(alpha = 0.95f), shadowElevation = 1.dp) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onOpenDrawer) {
                Icon(Icons.Filled.Menu, contentDescription = "打开菜单")
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = Ink)
                Text(connBadgeLabel, style = MaterialTheme.typography.labelSmall, color = badgeColor)
            }
            Box {
                IconButton(onClick = { userMenuExpanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "用户菜单")
                }
                DropdownMenu(expanded = userMenuExpanded, onDismissRequest = { userMenuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text("⚙️ 设置") },
                        onClick = { userMenuExpanded = false; onNavigateSettings() },
                    )
                    DropdownMenuItem(
                        text = { Text("✅ 应用并实时同步") },
                        onClick = { userMenuExpanded = false; onApplyQuickSync() },
                    )
                    DropdownMenuItem(
                        text = { Text("退出登录") },
                        onClick = { userMenuExpanded = false; onShowToast("演示环境：退出登录已模拟") },
                    )
                }
            }
        }
    }
}

// ---------------- 底部导航 ----------------

@Composable
private fun GuochaoBottomBar(
    currentScreen: AdminScreen,
    onSelectScreen: (AdminScreen) -> Unit,
    onOpenMoreDrawer: () -> Unit,
) {
    val primaryTabs = listOf(
        AdminScreen.DASHBOARD,
        AdminScreen.CARDS,
        AdminScreen.APP_MODULES,
        AdminScreen.SETTINGS,
    )
    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Cinnabar,
        selectedTextColor = Cinnabar,
        indicatorColor = Paper,
        unselectedIconColor = InkBlack.copy(alpha = 0.6f),
        unselectedTextColor = InkBlack.copy(alpha = 0.6f),
    )
    NavigationBar(containerColor = PaperSoft, contentColor = InkBlack, tonalElevation = 4.dp) {
        primaryTabs.forEach { screen ->
            val selected = currentScreen == screen
            val tabTitle = if (screen == AdminScreen.APP_MODULES) "本体模块" else screen.title
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectScreen(screen) },
                icon = { Icon(screenIcon(screen), contentDescription = screen.title) },
                label = {
                    Text(
                        tabTitle,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    )
                },
                colors = itemColors,
                modifier = Modifier.testTag("bottom_nav_${screen.route}"),
            )
        }
        val isOther = currentScreen !in primaryTabs
        NavigationBarItem(
            selected = isOther,
            onClick = onOpenMoreDrawer,
            icon = { Icon(Icons.Filled.MoreHoriz, contentDescription = "更多模块") },
            label = {
                Text(
                    if (isOther) currentScreen.title.take(4) else "全部模块",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isOther) FontWeight.Bold else FontWeight.Medium,
                )
            },
            colors = itemColors,
            modifier = Modifier.testTag("bottom_nav_more"),
        )
    }
}

// ---------------- 侧边导航 ----------------

@Composable
private fun GuochaoSidebarContent(
    currentScreen: AdminScreen,
    versionName: String,
    onSelectScreen: (AdminScreen) -> Unit,
    onCloudConnect: () -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("懒得找了", style = MaterialTheme.typography.displaySmall, color = Gold)
        Text("云端控制台 v$versionName", style = MaterialTheme.typography.labelSmall, color = Paper.copy(alpha = 0.55f))
        Spacer(Modifier.height(20.dp))
        AdminScreen.entries.forEach { screen ->
            val selected = currentScreen == screen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) Cinnabar.copy(alpha = 0.22f) else Color.Transparent)
                    .clickable { onSelectScreen(screen) }
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    screenIcon(screen),
                    contentDescription = screen.title,
                    tint = if (selected) Gold else Paper.copy(alpha = 0.85f),
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    screen.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selected) Gold else Paper,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onCloudConnect)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.CloudDone, contentDescription = "对接云端", tint = Gold)
            Spacer(Modifier.width(10.dp))
            Text("对接云端数据中枢", style = MaterialTheme.typography.labelLarge, color = Paper.copy(alpha = 0.85f))
        }
    }
}

// ---------------- 图标映射 ----------------

private fun screenIcon(screen: AdminScreen): ImageVector = when (screen) {
    AdminScreen.DASHBOARD -> Icons.Filled.Dashboard
    AdminScreen.CARDS -> Icons.Filled.Layers
    AdminScreen.CATEGORIES -> Icons.Filled.AccountTree
    AdminScreen.BUTTONS -> Icons.Filled.TouchApp
    AdminScreen.SKILLS -> Icons.Filled.AutoAwesome
    AdminScreen.APP_MODULES -> Icons.Filled.Widgets
    AdminScreen.THEME_KIT -> Icons.Filled.Palette
    AdminScreen.TEXTS -> Icons.Filled.TextFields
    AdminScreen.SETTINGS -> Icons.Filled.Settings
    AdminScreen.LOGS -> Icons.Filled.ReceiptLong
    AdminScreen.ENTRIES -> Icons.Filled.List
}
