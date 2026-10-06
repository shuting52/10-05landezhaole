package com.example.data.remote

import com.example.data.model.BadgeType
import com.example.data.model.NavCard
import com.example.data.model.NavCategory
import com.example.data.model.NavSubCategory

/**
 * 云端配置数据模型（admin-data.json）
 * 由「懒得找了·云端控制台」程序发布，本体启动时拉取并覆盖本地硬编码数据。
 * 字段缺失时全部使用默认值兜底，保证解析容错。
 */

data class AdminData(
    val version: VersionDto? = null,
    val home: HomeDto? = null,
    val software: List<SoftwareDto> = emptyList(),
    val skills: List<SkillDto> = emptyList(),
    val settings: SettingsDto? = null,
    val splash: SplashDto? = null,
    val welcome: WelcomeDto? = null,
    val updateDialog: UpdateDialogDto? = null,
    val marquee: MarqueeDto? = null,
    val console: ConsoleDto? = null,
    val ipMonitor: IpMonitorDto? = null,
    // v1.8.7：控制台可增删的「云端工具箱工具」
    val tools: List<ToolDto> = emptyList(),
    // v1.1.6：控制台「主题工具箱」（兼容顶层放置）
    val themeKit: Map<String, Any?> = emptyMap(),
    // v1.2.6：控制台「UI 文本编辑」——本体所有文字/按钮可云端覆盖（缺省用本地硬编码）
    val uiText: UiTextDto? = null
)

/** 控制台「UI 文本编辑」数据（本体所有文字/按钮的云端覆盖值，缺省为空=用本地默认） */
data class UiTextDto(
    val homeTitle: String = "",
    val slogan: String = "",
    val splashLoading: String = "",
    val splashSub: String = "",
    val toolTitle: String = "",
    val emptyText: String = "",
    val btnConfirm: String = "",
    val btnCancel: String = "",
    val btnSave: String = "",
    val searchPlaceholder: String = "",
    val luckyTitle: String = "",
    val luckyButton: String = "",
    val rerollButton: String = "",
    val selectCategory: String = "",
    val allSites: String = "",
    val skillTab: String = "",
    val promptTab: String = "",
    // v1.2.6：工具箱副标题、软件库标题/副标题、设置页标题/副标题（控制台 UI 文本编辑补充归纳）
    val toolboxSubtitle: String = "",
    val softwareTitle: String = "",
    val softwareSubtitle: String = "",
    val settingsTitle: String = "",
    val settingsSubtitle: String = "",
    // ===== v1.2.0：全面扩充 UI 文本管理（覆盖本体所有界面文字） =====
    val homeHeaderGreet: String = "",
    val homeSearchHint: String = "",
    val homeCatMore: String = "",
    val homeNoResult: String = "",
    val homeAllCount: String = "",
    val softwareInstall: String = "",
    val softwareOpen: String = "",
    val softwareDownload: String = "",
    val softwareEmpty: String = "",
    val skillDownload: String = "",
    val skillJump: String = "",
    val skillNoPrompt: String = "",
    val skillEmpty: String = "",
    val toolboxTip: String = "",
    val tabHome: String = "",
    val tabSoftware: String = "",
    val tabSkill: String = "",
    val tabToolbox: String = "",
    val tabSettings: String = "",
    val aboutTitle: String = "",
    val aboutVersion: String = "",
    val contactQQ: String = "",
    val contactWechat: String = "",
    val feedback: String = "",
    val checkUpdate: String = "",
    val themeTitle: String = "",
    val splashTip: String = "",
    val welcomeButton: String = "",
    val updateTitle: String = "",
    val updateButton: String = "",
    val updateLater: String = "",
    val marqueeDefault: String = "",
    // v1.2.0：控制台「➕ 添加自定义文本」的任意字段（key→文字），供未来 UI 扩展读取
    val custom: Map<String, String> = emptyMap()
)

/** 云端工具箱扩展工具（控制台增删，实时同步到本体工具箱页） */
data class ToolDto(
    val id: String = "",
    val name: String = "",
    val desc: String = "",
    val url: String = "",
    val icon: String = "🔧",
    val badge: String = ""
)

/** 控制台程序自身版本（与本体软件更新完全分离，不参与本体版本判断） */
data class ConsoleDto(
    val version: String = "1.0.0",
    val code: Int = 1,
    val apkUrl: String = ""
)

/** 首页置顶实时 IP 定位监控（控制台可开关/配 URL，UI 只显示定位 IP，不显示网站字样） */
data class IpMonitorDto(
    val enabled: Boolean = false,
    val url: String = ""
)

data class VersionDto(
    val code: Int = 0,
    val name: String = "1.0",
    val changelog: List<String> = emptyList(),
    val force: Boolean = false,
    val apkUrl: String = "",
    val apkUrlRaw: String = ""
)

data class HomeDto(
    val categories: List<CategoryDto> = emptyList()
)

data class CategoryDto(
    val id: String = "",
    val name: String = "",
    val iconKey: String = "grid",
    val desc: String = "",
    val subcategories: List<SubCategoryDto> = emptyList(),
    val cards: List<CardDto> = emptyList()
)

data class SubCategoryDto(
    val id: String = "",
    val name: String = ""
)

data class CardDto(
    val id: String = "",
    val title: String = "",
    val url: String = "",
    val icon: String = "",
    val fallbackText: String = "",
    val badge: String? = null,
    val badgeType: String = "ROSE",
    val desc: String = "",
    val categoryId: String = "",
    val subcatId: String = "",
    val highlights: String = ""
)

data class SoftwareDto(
    val id: String = "",
    val type: String = "software",
    val title: String = "",
    val desc: String = "",
    val url: String = "",
    val author: String = "",
    val badge: String = "",
    val tags: String = "",
    val apkUrl: String = "",
    val fileUrl: String = "",
    val previewUrl: String = "",
    val iconUrl: String = "",
    val mode: String = "file" // file=文件下载 / url=URL跳转下载
)

data class SkillDto(
    val id: String = "",
    val type: String = "skill",
    val promptType: String = "skill", // skill / prompt_image / prompt_video
    val title: String = "",
    val desc: String = "",
    val prompt: String = "",
    val url: String = "",
    val author: String = "",
    val badge: String = "",
    val tags: String = "",
    val previewUrl: String = "",
    val mediaUrl: String = "",
    val fileUrl: String = "",
    val iconUrl: String = "",
    val mode: String = "file" // file=文件下载 / url=URL跳转
)

data class SettingsDto(
    val appName: String = "懒得找了",
    val slogan: String = "",
    val aboutText: String = "",
    val contactQQ: String = "",
    val contactWechat: String = "",
    val contactAlipay: String = "",
    val qqGroupUrl: String = "",
    val qqGroupUin: String = "",
    val officialWebsite: String = "",
    val feedbackEmail: String = "",
    val customThemeCss: String = "",
    val customThemeHtml: String = "",
    // 品牌更改：图标 / 包名
    val logoUrl: String = "",
    val packageName: String = "",
    // 安全加固：签名自校验
    val security: SecurityDto? = null,
    // v1.0.9：软件停止运营开关（控制台设置，开=本体强制提示并退出）
    val serverShutdown: ServerShutdownDto? = null,
    // 全局背景媒体（控制台上传图片/视频，url 空则用内置）
    val bgMedia: BgMediaDto? = null,
    // 内置歌手海报（控制台可上传替换）
    val celebrityPosters: List<CelebrityPosterDto> = emptyList(),
    // UI 组件级主题（每个组件独立代码定制）
    val componentThemes: Map<String, String> = emptyMap(),
    // v1.1.6 控制台「主题工具箱」：Map<组件id, {css, html}>（appBar/bottomBar/splash/card/...）
    val themeKit: Map<String, Any?> = emptyMap()
)

/** 安全加固配置：开启后运行时校验自身签名，防止二次打包篡改 */
data class SecurityDto(
    val enabled: Boolean = false,
    val expectedSha: String = ""
)

/** v1.0.9：软件停止运营开关（控制台设置） */
data class ServerShutdownDto(
    val enabled: Boolean = false,
    val notice: String = ""
)

/** 全局背景媒体：图片 / 视频 */
data class BgMediaDto(
    val type: String = "none", // none / image / video
    val url: String = ""
)

/** 内置歌手海报 */
data class CelebrityPosterDto(
    val name: String = "",
    val url: String = ""
)

data class SplashDto(
    val type: String = "default",
    val customHtml: String = "",
    val mediaUrl: String = "",
    val durationSeconds: Int = 3,
    val bgColor: String = "#0B0B1A"
)

data class WelcomeDto(
    val enabled: Boolean = false,
    val title: String = "欢迎使用",
    val content: String = "",
    val welcomeText: String = "",
    val imageUrl: String = "",
    val buttonText: String = "开始使用",
    // 弹窗比例：compact 紧凑 / standard 标准 / wide 宽幅（控制台可调）
    val ratio: String = "standard"
)

data class UpdateDialogDto(
    val title: String = "发现新版本",
    val changelog: List<String> = emptyList(),
    val confirmText: String = "立即更新",
    val cancelText: String = "稍后再说",
    val customCss: String = "",
    val customHtml: String = ""
)

/**
 * 主页跑马灯公告：支持 24 小时时间段轮播，控制台可改图标与文案。
 */
data class MarqueeDto(
    val enabled: Boolean = true,
    val icon: String = "📢",
    val defaultText: String = "欢迎使用懒得找应用软件，这里的资源丰富，很多资源都是可以白嫖的。请自寻探索~~",
    val segments: List<MarqueeSegmentDto> = emptyList()
)

data class MarqueeSegmentDto(
    val start: Int = 0,
    val end: Int = 23,
    val text: String = ""
)

// ---------- 转换函数：云端 DTO -> 本体领域模型 ----------

fun CategoryDto.toNavCategory(): NavCategory {
    val subs = subcategories.map { NavSubCategory(it.id, it.name) }
    val cardList = cards.map { it.toNavCard() }
    return NavCategory(
        id = id,
        name = name,
        iconKey = iconKey,
        desc = desc,
        subcategories = subs,
        cards = cardList
    )
}

fun CardDto.toNavCard(): NavCard {
    return NavCard(
        id = id,
        title = title,
        url = url,
        icon = icon,
        fallbackText = fallbackText,
        badge = badge,
        badgeType = badgeType.toBadgeType(),
        desc = desc,
        categoryId = categoryId,
        subcatId = subcatId,
        highlights = highlights
    )
}

fun String.toBadgeType(): BadgeType = when (uppercase()) {
    "NEW" -> BadgeType.NEW
    "GOLD" -> BadgeType.GOLD
    "BLUE" -> BadgeType.BLUE
    else -> BadgeType.ROSE
}
