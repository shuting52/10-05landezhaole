package com.example.viewmodel

import com.example.model.ActivityLog
import com.example.model.AdminScreen
import com.example.model.CategoryItem
import com.example.model.ConnState
import com.example.model.ConsoleConfig
import com.example.model.FullSettingsConfig
import com.example.model.IpMonitorConfig
import com.example.model.MarqueeConfig
import com.example.model.ResourceButton
import com.example.model.ResourceCard
import com.example.model.SkillItem
import com.example.model.SplashConfig
import com.example.model.StatItem
import com.example.model.TextItem
import com.example.model.ThemeKitConfig
import com.example.model.ToolItem
import com.example.model.UpdateDialogConfig
import com.example.model.WelcomeConfig

/**
 * 反编译重建 · 控制台 UI 状态
 * 由 v2.1.0 APK 反编译的 AdminUiState data class 重建（45 个字段与原始一致）
 */
data class AdminUiState(
    val currentScreen: AdminScreen = AdminScreen.DASHBOARD,
    val connState: ConnState = ConnState.IDLE,
    val githubOwner: String = "shuting52",
    val githubRepo: String = "10-05landezhaole",
    val githubBranch: String = "main",
    val githubToken: String = "",
    val toastMessage: String = "",
    val dateRange: String = "近7天",
    val categories: List<CategoryItem> = emptyList(),
    val allCards: List<ResourceCard> = emptyList(),
    val buttons: List<ResourceButton> = emptyList(),
    val skills: List<SkillItem> = emptyList(),
    val texts: List<TextItem> = emptyList(),
    val stats: List<StatItem> = emptyList(),
    val toolsList: List<ToolItem> = emptyList(),
    val activityLogs: List<ActivityLog> = emptyList(),
    val operationLogs: List<ActivityLog> = emptyList(),
    val splashConfig: SplashConfig = SplashConfig(),
    val welcomeConfig: WelcomeConfig = WelcomeConfig(),
    val marqueeConfig: MarqueeConfig = MarqueeConfig(),
    val updateDialogConfig: UpdateDialogConfig = UpdateDialogConfig(),
    val consoleConfig: ConsoleConfig = ConsoleConfig(),
    val ipMonitorConfig: IpMonitorConfig = IpMonitorConfig(),
    val aiNotice: String = "",
    val themeKitConfig: ThemeKitConfig = ThemeKitConfig(),
    val fullSettings: FullSettingsConfig = FullSettingsConfig(),
    val versionCode: Int = 0,
    val versionName: String = "",
    val versionForce: Boolean = false,
    val versionApkUrl: String = "",
    val versionApkUrlRaw: String = "",
    val versionChangelog: List<String> = emptyList(),
    val appName: String = "",
    val slogan: String = "",
    val lastSyncAt: String = "",
    val errorMsg: String = "",
    val mode: String = "url",
    val uploadingLabel: String = "",
    val notifReview: Boolean = false,
    val notifDownload: Boolean = false,
    val notifWeekly: Boolean = false,
    val isLoading: Boolean = false,
    val isConnecting: Boolean = false,
    val isPublishing: Boolean = false,
    val isUploadingFile: Boolean = false,
)
