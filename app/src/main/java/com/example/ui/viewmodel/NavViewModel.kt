package com.example.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.NavData
import com.example.data.local.db.UserItemRecord
import com.example.data.model.NavCard
import com.example.data.model.NavCategory
import com.example.data.model.SearchEngine
import com.example.data.remote.AdminData
import com.example.data.remote.CategoryDto
import com.example.data.remote.IpMonitorDto
import com.example.data.remote.MarqueeDto
import com.example.data.remote.RemoteConfigRepository
import com.example.data.remote.SettingsDto
import com.example.data.remote.SplashDto
import com.example.data.remote.ToolDto
import com.example.data.remote.UiTextDto
import com.example.data.remote.UpdateDialogDto
import com.example.data.remote.VersionDto
import com.example.data.remote.WelcomeDto
import com.example.data.remote.toNavCategory
import com.example.data.remote.toNavCard
import com.example.data.repository.NavRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.URLEncoder

import com.example.data.local.db.UploadedResourceEntity
import com.example.ui.theme.AtmosphereEffect
import com.example.ui.theme.ThemePreset
import com.example.ui.theme.ThemePresetsRepository
import com.example.ui.uiverse.ActiveUiverseState
import com.example.ui.uiverse.UiKitPreset
import com.example.ui.uiverse.UiverseItem
import com.example.ui.uiverse.UiverseCssEngine
import com.example.ui.uiverse.CardStylePreset
import com.example.ui.uiverse.ButtonStylePreset
import com.example.ui.uiverse.InputStylePreset
import com.example.ui.uiverse.LoaderStylePreset
import com.example.ui.uiverse.PatternStylePreset
import com.example.ui.uiverse.DynamicEffectPreset
import java.util.UUID

enum class AppBottomTab(val title: String) {
    HOME("首页"),
    SOFTWARE("软件"),
    SKILL("资源"),
    TOOLBOX("工具箱"),
    SETTINGS("设置")
}

data class NavUiState(
    val currentTab: AppBottomTab = AppBottomTab.HOME,
    val currentTheme: ThemePreset = ThemePresetsRepository.defaultTheme,
    val atmosphereEffect: AtmosphereEffect = AtmosphereEffect.NONE,
    // v1.2.0：动态效果主题
    val dynamicEffect: DynamicEffectPreset = DynamicEffectPreset.NONE,
    val isThemeDialogVisible: Boolean = false,
    val activeUiverseState: ActiveUiverseState = ActiveUiverseState(),
    val categories: List<NavCategory> = NavData.categories,
    val selectedCategoryId: String = NavData.categories.firstOrNull()?.id ?: "ai_models",
    val selectedSubcatId: String = "all",
    val searchQuery: String = "",
    val selectedEngineIndex: Int = 0,
    val luckyCard: NavCard? = null,
    val isLuckyModalVisible: Boolean = false,
    val isFavoritesModalVisible: Boolean = false,
    val isHistoryModalVisible: Boolean = false,
    val activeDetailCard: NavCard? = null,
    val isSplashVisible: Boolean = true,
    val isCloudReady: Boolean = false,
    val cloudVersion: VersionDto? = null,
    val cloudSplash: SplashDto? = null,
    val cloudWelcome: WelcomeDto? = null,
    val cloudUpdate: UpdateDialogDto? = null,
    val cloudSettings: SettingsDto? = null,
    val cloudMarquee: MarqueeDto? = null,
    val cloudIpMonitor: IpMonitorDto? = null,
    // v1.8.7：云端工具箱扩展工具（控制台增删）
    val cloudTools: List<ToolDto> = emptyList(),
    // v1.2.6：控制台「UI 文本编辑」——本体文字/按钮云端覆盖值（缺省用本地默认）
    val cloudUiText: UiTextDto? = null,
    // 本地背景媒体（主题版块直接本机选择，无需控制台）：type = none/image/video, uri 为本地内容 URI
    val localBgMediaType: String = "none",
    val localBgMediaUri: String = ""
)

class NavViewModel(
    private val repository: NavRepository,
    private val remoteConfigRepository: RemoteConfigRepository? = null,
    private val appContext: Context? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(NavUiState())
    val uiState: StateFlow<NavUiState> = _uiState

    // ===== 本地「隐藏墓碑」：本体删除云端条目后，云端轮询不再把它同步回来 =====
    // （解决「本体删除后过一会儿又出现」的同步不到位问题）
    private val hiddenPrefs: android.content.SharedPreferences? =
        appContext?.getSharedPreferences("landezhao_hidden_resources", Context.MODE_PRIVATE)

    private fun hiddenIds(): Set<String> =
        hiddenPrefs?.getStringSet("hidden_ids", emptySet()) ?: emptySet()

    private fun isHidden(id: String): Boolean = hiddenIds().contains(id)

    private fun hideResource(id: String) {
        val prefs = hiddenPrefs ?: return
        val set = prefs.getStringSet("hidden_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
        set.add(id)
        prefs.edit().putStringSet("hidden_ids", set).apply()
    }

    private fun unhideResource(id: String) {
        val prefs = hiddenPrefs ?: return
        val set = prefs.getStringSet("hidden_ids", emptySet())?.toMutableSet() ?: mutableSetOf()
        set.remove(id)
        prefs.edit().putStringSet("hidden_ids", set).apply()
    }

    // 云端全局主题代码签名（v1.1.10：改为内容签名对比——控制台「应用」后内容变化立即重新生效，
    // 内容未变时不重复应用，避免每 5 秒轮询重复覆盖用户手动修改的主题）
    private var appliedCloudThemeSignature: String? = null
    // v1.1.6：控制台「主题工具箱」组件级代码签名（同样按内容变化触发）
    private var appliedCloudThemeKitSignature: String? = null

    val favorites: StateFlow<List<UserItemRecord>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history: StateFlow<List<UserItemRecord>> = repository.history
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uploadedSoftware: StateFlow<List<UploadedResourceEntity>> = repository.getUploadedSoftware()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uploadedSkills: StateFlow<List<UploadedResourceEntity>> = repository.getUploadedSkills()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uploadedPrompts: StateFlow<List<UploadedResourceEntity>> = repository.getUploadedPrompts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUploadedResources: StateFlow<List<UploadedResourceEntity>> = repository.getAllUploadedResourcesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customSites: StateFlow<List<UploadedResourceEntity>> = repository.getCustomSites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Pre-seed some popular initial software and skills if empty
        viewModelScope.launch {
            seedInitialResources()
        }
        // 拉取云端配置：分类 / 开屏 / 欢迎 / 更新弹窗，实时覆盖本地数据
        viewModelScope.launch {
            refreshRemoteConfig()
        }
        // 定期后台检测云端更新：每 60 秒轮询一次 GitHub API 直读（v1.1.16：由 5 秒改为 60 秒——
        // 匿名 API 配额 60 次/小时，5 秒轮询十几分钟即耗尽，之后被迫回退 CDN 镜像（jsDelivr 缓存最长 12 小时），
        // 导致控制台改完内容后本体长时间不同步。60 秒轮询刚好吃满配额，始终走 API 直读保证实时）。
        // 首次启动已立即拉取，后续每 60 秒增量同步；设置页另有人工「检查更新」可即时刷新。
        viewModelScope.launch {
            while (isActive) {
                kotlinx.coroutines.delay(60_000L)
                refreshRemoteConfig()
            }
        }
    }

    suspend fun refreshRemoteConfig(): Pair<Boolean, VersionDto?> {
        val remote = remoteConfigRepository ?: return Pair(false, null)
        val data: AdminData? = remote.fetchAdminData()
        if (data == null) {
            _uiState.value = _uiState.value.copy(isCloudReady = true)
            return Pair(false, null)
        }
        val cloudCats = data.home?.categories ?: emptyList()
        val localCats = NavData.categories
        // v1.7.8 写死规则：站点只能增加不能删除原站点，除非是重复站点。
        // 合并策略：云端与本地并集，按 normalizedUrl 去重——云端出现重复 URL 时覆盖本地版本，本地独有保留。
        val allCatsById = (localCats.map { it.id } + cloudCats.map { it.id }).distinct()
        val newCats = allCatsById.map { catId ->
            val localCat = localCats.firstOrNull { it.id == catId }
            val cloudCat = cloudCats.firstOrNull { it.id == catId }
            // v1.7.8 修复：cloudCat 是 CategoryDto、localCat 是 NavCategory，类型不同不能直接 ?: 合并，
            // 统一转成 NavCategory 后合并（云端优先），避免 Elvis 推断成 Any 导致 copy 无法解析
            val baseCat: NavCategory = if (cloudCat != null) cloudCat.toNavCategory() else localCat ?: NavCategory(id = catId, name = catId)
            val localCards = localCat?.cards ?: emptyList()
            val cloudCards = cloudCat?.cards ?: emptyList()
            // 合并：云端优先（覆盖同名），本地独有保留
            val merged = (cloudCards.map { c -> normalizeSiteUrl(c.url) to c.toNavCard() } +
                localCards.map { c -> normalizeSiteUrl(c.url) to c })
                .distinctBy { it.first }
                .map { it.second }
            // 选择云端的元信息为优先（保留设置 / 名称），如果不存在则用本地
            baseCat.copy(cards = merged)
        }
        val current = _uiState.value
        val validSelectedId = newCats.any { it.id == current.selectedCategoryId }
        // 新站点自动置顶：带 NEW/新 角标的卡片排到每个分类最前，让最新更新第一时间呈现在首页
        val sortedCats = newCats.map { cat ->
            cat.copy(
                cards = cat.cards.sortedByDescending { card ->
                    val badge = card.badge ?: ""
                    when {
                        badge.contains("NEW", ignoreCase = true) -> 3
                        badge.contains("新") -> 2
                        badge.equals("HOT", ignoreCase = true) -> 1
                        else -> 0
                    }
                }
            )
        }
        _uiState.value = current.copy(
            categories = sortedCats,
            selectedCategoryId = if (validSelectedId) current.selectedCategoryId else (newCats.firstOrNull()?.id ?: "all"),
            isCloudReady = true,
            cloudVersion = data.version,
            cloudSplash = data.splash,
            cloudWelcome = data.welcome,
            cloudUpdate = data.updateDialog,
            cloudSettings = data.settings,
            cloudMarquee = data.marquee,
            cloudIpMonitor = data.ipMonitor,
            // v1.8.7：云端工具箱工具列表
            cloudTools = data.tools,
            // v1.2.6：控制台「UI 文本编辑」云端覆盖值
            cloudUiText = data.uiText
        )
        // 控制台软件/Skill 增删改 → 本体实时同步（删除：云端已移除的条目从本地库同步删除）
        syncCloudResources(data)
        // 云端全局主题自定义代码（控制台「设置-主题切换」配置）：内容签名变化时自动重新应用
        // v1.1.10 修复「控制台改主题本体无变化」：不再用一次性标志，改为签名对比
        val cloudCss = data.settings?.customThemeCss?.ifBlank { null }
        val cssSig = cloudCss?.hashCode()?.toString() ?: ""
        if (cssSig != appliedCloudThemeSignature) {
            appliedCloudThemeSignature = cssSig
            if (cloudCss != null) {
                try {
                    applyUiverseCustomCss(cloudCss, data.settings?.customThemeHtml ?: "")
                } catch (e: Exception) {
                    // 主题代码解析失败不影响主流程
                }
            }
        }
        // v1.1.6 需求 4：控制台「主题工具箱」组件级自定义代码 → 本体逐组件应用
        // 支持组件：appBar 顶栏 / bottomBar 底栏 / splash 开屏 / statusBar 状态栏 /
        // card 卡片 / button 按钮 / dialog 弹窗 / search 搜索框 / global 全局 / settingsPage 设置页
        // v1.1.10 修复：签名变化即重新应用，控制台「应用」后本体 5 秒内实时生效
        // v1.1.10 修复「控制台改主题本体无变化」根因 2：
        // 云端 settings.themeKit 可能为空 {}（非 null），?: 会被空 {} 截断导致顶层真实 themeKit 被忽略。
        // 改为：取非空的那份（settings.themeKit 优先，空则回退顶层 themeKit，再空则回退 null）
        val cloudThemeKit = (data.settings?.themeKit?.takeIf { it.isNotEmpty() })
            ?: (data.themeKit?.takeIf { it.isNotEmpty() })
        val kitSig = cloudThemeKit?.toString()?.hashCode()?.toString() ?: ""
        if (kitSig != appliedCloudThemeKitSignature) {
            appliedCloudThemeKitSignature = kitSig
            if (cloudThemeKit != null) {
                try {
                    applyCloudThemeKit(cloudThemeKit)
                } catch (e: Exception) {
                    // 主题代码解析失败不影响主流程
                }
            }
        }
        val hasNewVersion = (data.version?.code ?: 0) > com.example.BuildConfig.VERSION_CODE
        return Pair(hasNewVersion, data.version)
    }

    /**
     * 云端软件/Skill 与本体双向同步（v1.8.7 增强）：
     * - 云端有 → 本体新增/更新（覆盖同名 id）
     * - 云端已删除 → 本体同步删除（删除控制台删掉的 sw_/sk_ 条目）
     * 注意：站点（首页卡片）仍遵守写死规则4「只增不删」，此处只同步软件/Skill 资源库。
     */
    private suspend fun syncCloudResources(data: AdminData) {
        try {
            val cloudSoftwares = data.software
            val cloudSkills = data.skills
            // ✓ 云端 id 全集（用于删除本地已被云端下架的条目）
            val cloudSwIds = cloudSoftwares.map { it.id }.toSet()
            val cloudSkIds = cloudSkills.map { it.id }.toSet()

            // v1.9.1 修复「软件/Skill 版块自动一闪一闪」：改为幂等同步。
            // 一次性读取本地全部资源，内容未变化的条目直接跳过写入——
            // 否则每 5 秒轮询对每个资源执行 REPLACE，且 timestamp 每次都更新，
            // 会持续触发 Room Flow 发射新列表，导致列表频繁无效化闪烁。
            val localAll = repository.getAllUploadedResources()
            val localById = localAll.associateBy { it.id }

            /** 判断云端软件/Skill 与本地同 id 条目内容是否一致（一致则跳过写入） */
            fun unchanged(old: UploadedResourceEntity?, fields: UploadedResourceEntity): Boolean {
                if (old == null) return false
                return old.type == fields.type &&
                    old.title == fields.title &&
                    old.desc == fields.desc &&
                    old.url == fields.url &&
                    old.author == fields.author &&
                    old.badge == fields.badge &&
                    old.tags == fields.tags &&
                    old.fileUrl == fields.fileUrl &&
                    old.prompt == fields.prompt &&
                    old.previewUrl == fields.previewUrl &&
                    old.mediaUrl == fields.mediaUrl &&
                    old.iconUrl == fields.iconUrl &&
                    old.mode == fields.mode
            }

            cloudSoftwares.forEach { sw ->
                // v1.1.15：本体主动删除的云端条目（墓碑）不再被云端轮询恢复
                if (isHidden(sw.id)) return@forEach
                // v1.7.4 修复：控制台文件模式把上传文件直链存在 apkUrl 字段，必须映射到 fileUrl，
                // 否则本体拿不到下载链接（显示「未配置下载」）。URL 模式 apkUrl 为跳转直链。
                val fileLink = sw.apkUrl.ifBlank { sw.url }
                val candidate = UploadedResourceEntity(
                    id = sw.id,
                    type = "software",
                    title = sw.title,
                    desc = sw.desc,
                    url = sw.url,
                    author = sw.author,
                    badge = sw.badge.ifBlank { "站长推荐" },
                    tags = sw.tags,
                    fileUrl = fileLink,
                    iconUrl = sw.iconUrl,
                    mode = sw.mode
                )
                // v1.9.1：内容未变化跳过写入，避免轮询引发列表闪烁
                if (!unchanged(localById[sw.id], candidate)) {
                    repository.saveUploadedResource(candidate)
                }
            }
            cloudSkills.forEach { sk ->
                // v1.1.15：本体主动删除的云端条目（墓碑）不再被云端轮询恢复
                if (isHidden(sk.id)) return@forEach
                // v1.7.4：Skill 文件模式直链在 url 字段；URL 模式 url 为跳转直链
                val candidate = UploadedResourceEntity(
                    id = sk.id,
                    type = if (sk.promptType == "prompt_image" || sk.promptType == "prompt_video") sk.promptType else "skill",
                    title = sk.title,
                    desc = sk.desc,
                    url = sk.url,
                    author = sk.author,
                    badge = sk.badge.ifBlank { "站长推荐" },
                    tags = sk.tags,
                    fileUrl = sk.url,
                    prompt = sk.prompt,
                    previewUrl = sk.previewUrl,
                    mediaUrl = sk.mediaUrl,
                    iconUrl = sk.iconUrl,
                    mode = sk.mode
                )
                // v1.9.1：内容未变化跳过写入，避免轮询引发列表闪烁
                if (!unchanged(localById[sk.id], candidate)) {
                    repository.saveUploadedResource(candidate)
                }
            }

            // v1.8.7：云端下架 → 本体同步删除（控制台删除某软件/Skill 后点「应用」，本体实时移除）
            // v1.1.15：删除判定不再限定 sw_/sk_ 前缀（兼容 init- 等云端 id 风格），
            // 只要属于云端管理类型且不在云端 id 全集即视为下架；用户本地上传（res_）条目永不自动删除。
            try {
                localAll.forEach { local ->
                    val isCloudType = local.type == "software" || local.type == "skill" ||
                        local.type == "prompt_image" || local.type == "prompt_video"
                    // 云端管理的条目：非用户本地上传（用户上传 id 以 res_ 开头、badge=作者投递）
                    val isCloudManaged = isCloudType && !local.id.startsWith("res_") && !local.id.startsWith("init-")
                    val inCloud = when (local.type) {
                        "software" -> local.id in cloudSwIds
                        else -> local.id in cloudSkIds
                    }
                    if (isCloudManaged && !inCloud) {
                        repository.deleteUploadedResource(local.id)
                        // 云端已下架该条目：清除本地墓碑（之后控制台重新添加可正常显示）
                        unhideResource(local.id)
                    }
                }
            } catch (e: Exception) {
                // 本地删除失败不阻断主流程
            }
        } catch (e: Exception) {
            // 同步失败不阻断主流程
        }
    }

    private suspend fun seedInitialResources() {
        // Pre-seed 3 software and 3 skills for initial showcase
        val defaultSoftware = listOf(
            UploadedResourceEntity(
                id = "init-sw-1",
                type = "software",
                title = "Geek Uninstaller",
                desc = "极简高效的Windows彻底卸载清理神器，免安装单文件，强制剔除注册表残留。",
                url = "https://geekuninstaller.com/",
                author = "Thomas",
                badge = "站长自用",
                tags = "Windows / 清理"
            ),
            UploadedResourceEntity(
                id = "init-sw-2",
                type = "software",
                title = "PotPlayer 64bit 绿色优化版",
                desc = "全球极佳的高清影音播放器，内置无缝解码器，支持4K HDR与杜比视界蓝光直出。",
                url = "https://potplayer.daum.net/",
                author = "Daum",
                badge = "装机必备",
                tags = "影音播放 / 4K"
            ),
            UploadedResourceEntity(
                id = "init-sw-3",
                type = "software",
                title = "Everything 极速搜盘",
                desc = "1秒内秒搜百万本地文件的搜索神器，毫秒级响应，几乎零内存占用。",
                url = "https://www.voidtools.com/",
                author = "Voidtools",
                badge = "神器",
                tags = "生产力 / 搜索"
            )
        )

        val defaultSkills = listOf(
            UploadedResourceEntity(
                id = "init-sk-1",
                type = "skill",
                title = "Clean Architecture 架构设计指南",
                desc = "指导编写符合MVVM高内聚低耦合的现代Android Kotlin架构与依赖注入规范。",
                url = "https://developer.android.com/topic/architecture",
                author = "Android Team",
                badge = "架构Skill",
                tags = "Kotlin / M3"
            ),
            UploadedResourceEntity(
                id = "init-sk-2",
                type = "skill",
                title = "Gemini 顶级 Prompt 提示词工程",
                desc = "让大模型思考更深度、代码生成更严谨的结构化提示词设计模式与角色设定方案。",
                url = "https://ai.google.dev/",
                author = "DeepMind Lab",
                badge = "AI特化",
                tags = "Prompt / LLM"
            ),
            UploadedResourceEntity(
                id = "init-sk-3",
                type = "skill",
                title = "Robolectric 极速本地无头单元测试",
                desc = "无须真机与模拟器即可在JVM上瞬间执行Android UI与逻辑测试的高效方案。",
                url = "https://robolectric.org/",
                author = "Google OpenSource",
                badge = "测试Skill",
                tags = "CI/CD / JVM"
            )
        )

        val defaultPrompts = listOf(
            UploadedResourceEntity(
                id = "init-pm-1",
                type = "prompt_image",
                title = "未来赛博都市 · 霓虹飞车雨夜",
                desc = "赛博朋克立体都市雨夜全景，飞驰流光悬浮车与高耸全息霓虹招牌，电影质感光影与水汽倒影",
                url = "https://midjourney.com",
                author = "CyberArt Lab",
                badge = "Midjourney v6.1",
                tags = "赛博朋克 / 8K渲染 / 电影质感",
                prompt = "Cinematic shot of a sprawling futuristic cyberpunk mega city at midnight, flying vehicles with glowing neon trails, holographic billboards reflecting on wet rain streets, dense vertical architecture, ultra-detailed 8k resolution, volumetric atmospheric smoke, photorealistic octane render --ar 16:9 --v 6.1 --style raw",
                previewUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80",
                mode = "url"
            ),
            UploadedResourceEntity(
                id = "init-pm-2",
                type = "prompt_image",
                title = "幽竹提灯 · 汉服古风唯美少女",
                desc = "江南竹林薄雾暮色，汉服少女手持莲花暖光花灯，流萤点点与柔和月色，大师级胶片人像质感",
                url = "https://flux1.art",
                author = "国风美学社",
                badge = "FLUX.1-dev",
                tags = "国风汉服 / 唯美写实 / 柔光胶片",
                prompt = "A breathtakingly beautiful young Asian woman in exquisite ethereal flowing emerald Hanfu silk dress, holding a delicate glowing lotus lantern, standing in a misty twilight bamboo forest, floating golden fireflies, volumetric soft moonlight, 85mm portrait lens photography, hyperrealistic skin pores, masterpiece --ar 3:4 --steps 30",
                previewUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
                mode = "url"
            ),
            UploadedResourceEntity(
                id = "init-pm-3",
                type = "prompt_video",
                title = "Sora 电影级 · 金龙穿云越峰长镜头",
                desc = "【视频生成】Sora电影级镜头：万丈云海间东方金鳞神龙乘风掠过奇峰险峦，古刹悬阁隐于落日余晖中",
                url = "https://openai.com/sora",
                author = "Sora Visionary",
                badge = "Sora 4K",
                tags = "Sora / 电影运镜 / 东方神话",
                prompt = "Cinematic drone tracking shot: A majestic oriental golden dragon with shimmering scales and flowing whiskers soars through sea of clouds above dramatic misty mountain peaks, ancient cliffside pagodas below, sunset golden hour rays penetrating mist, smooth epic cinematic motion, photorealistic 4k movie footage --motion 7 --fps 60 --duration 8s",
                previewUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
                mode = "url"
            ),
            UploadedResourceEntity(
                id = "init-pm-4",
                type = "prompt_image",
                title = "可爱3D黏土宇航机甲 · IP潮玩设计",
                desc = "Q版赛博太空机甲小机器人，悬浮全息彩色数字水晶，磨砂黏土质感与柔光棚拍，3D潮玩盲盒风格",
                url = "https://midjourney.com",
                author = "潮玩三维实验室",
                badge = "C4D/Blender",
                tags = "3D潮玩 / 黏土渲染 / 盲盒设计",
                prompt = "Chibi cute futuristic astronaut robot mascot floating with colorful holographic data crystals, smooth matte clay texture, soft studio ambient occlusion lighting, clean pastel gradients, ray tracing, C4D and Blender 3D render, behance trending digital toy art --ar 1:1 --v 6.1",
                previewUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80",
                mode = "url"
            ),
            UploadedResourceEntity(
                id = "init-pm-5",
                type = "prompt_video",
                title = "可灵AI · 暴雨夜未来霓虹机车狂飙",
                desc = "【视频生成】快手可灵AI：暴雨霓虹高架未来超导机车极速破空狂飙，后轮水雾飞溅与低角度动态追焦",
                url = "https://klingai.kuaishou.com",
                author = "Kling 动力流",
                badge = "快手可灵",
                tags = "可灵AI / 动态追焦 / 赛博狂飙",
                prompt = "Low-angle dynamic tracking camera chasing a futuristic sleek electric motorbike speeding through a neon-soaked cyberpunk highway in heavy rain, glowing wheels spraying water droplets, rear thruster flames, camera shaking with speed, realistic motion blur, 4K 60fps --mode high_performance --speed fast",
                previewUrl = "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=800&auto=format&fit=crop&q=80",
                mode = "url"
            )
        )

        defaultSoftware.forEach { repository.saveUploadedResource(it) }
        defaultSkills.forEach { repository.saveUploadedResource(it) }
        defaultPrompts.forEach { repository.saveUploadedResource(it) }
    }

    fun triggerSyncFromCloud(onComplete: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val (hasNew, ver) = refreshRemoteConfig()
                onComplete?.invoke(true)
            } catch (e: Exception) {
                onComplete?.invoke(false)
            }
        }
    }

    fun switchTab(tab: AppBottomTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun setTheme(preset: ThemePreset) {
        _uiState.value = _uiState.value.copy(
            currentTheme = preset,
            atmosphereEffect = preset.atmosphereEffect
        )
    }

    fun setAtmosphereEffect(effect: AtmosphereEffect) {
        _uiState.value = _uiState.value.copy(atmosphereEffect = effect)
    }

    // v1.2.0：应用动态效果主题
    fun setDynamicEffect(effect: DynamicEffectPreset) {
        _uiState.value = _uiState.value.copy(dynamicEffect = effect)
    }

    fun setThemeDialogVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(isThemeDialogVisible = visible)
    }

    fun applyUiverseKit(kit: UiKitPreset) {
        val newTheme = ThemePreset(
            id = kit.id,
            name = kit.displayName,
            style = kit.id,
            categoryName = "Uiverse",
            primaryColor = kit.primaryColor,
            secondaryColor = kit.secondaryColor,
            bgColor = kit.backgroundColor,
            surfaceColor = kit.surfaceColor,
            textColor = kit.textColor
        )
        val newCardStyle = when (kit) {
            UiKitPreset.STYLE_1_TILT_MAGNETIC -> CardStylePreset.TILT_3D_MAGNETIC
            UiKitPreset.STYLE_3_THICK_BUTTON -> CardStylePreset.STYLE_3_THICK_CARD
            UiKitPreset.STYLE_4_BOTTOMBAR_APPBAR -> CardStylePreset.STYLE_4_BOLD_FRAME
            UiKitPreset.STYLE_5_CAPSULE_SETROW -> CardStylePreset.STYLE_5_CAPSULE_ROW
            UiKitPreset.CYBERPUNK_NEON -> CardStylePreset.CYBERPUNK
            UiKitPreset.GLASSMORPHISM_AURORA -> CardStylePreset.GLASSMORPHISM
            UiKitPreset.NEUMORPHISM_CLAY -> CardStylePreset.NEUMORPHISM
            UiKitPreset.NEO_BRUTALISM_POP -> CardStylePreset.NEO_BRUTALISM
            UiKitPreset.RETRO_8BIT_ARCADE -> CardStylePreset.RETRO_PIXEL
            UiKitPreset.HOLOGRAPHIC_PRISM -> CardStylePreset.HOLOGRAPHIC
            UiKitPreset.LUXURY_OBSIDIAN_GOLD -> CardStylePreset.LUXURY_GOLD
            else -> CardStylePreset.DEFAULT
        }
        val newBtnStyle = when (kit) {
            UiKitPreset.STYLE_2_GLASS_LOADER -> ButtonStylePreset.STYLE_2_GLASS_PILL
            UiKitPreset.STYLE_3_THICK_BUTTON -> ButtonStylePreset.STYLE_3_THICK_SHADOW
            UiKitPreset.STYLE_4_BOTTOMBAR_APPBAR -> ButtonStylePreset.STYLE_3_THICK_SHADOW
            UiKitPreset.CYBERPUNK_NEON -> ButtonStylePreset.CYBERPUNK_GLOW
            UiKitPreset.GLASSMORPHISM_AURORA -> ButtonStylePreset.GLASS_AURORA
            UiKitPreset.NEUMORPHISM_CLAY -> ButtonStylePreset.NEUMORPHIC_PUSH
            UiKitPreset.NEO_BRUTALISM_POP -> ButtonStylePreset.BRUTALIST_OFFSET
            UiKitPreset.RETRO_8BIT_ARCADE -> ButtonStylePreset.RETRO_COIN
            else -> ButtonStylePreset.DEFAULT
        }
        val newInputStyle = when (kit) {
            UiKitPreset.STYLE_2_GLASS_LOADER -> InputStylePreset.STYLE_2_GLASS_GLOW
            UiKitPreset.STYLE_3_THICK_BUTTON -> InputStylePreset.NEO_BRUTALIST_BOX
            UiKitPreset.CYBERPUNK_NEON -> InputStylePreset.CYBER_TERMINAL
            UiKitPreset.GLASSMORPHISM_AURORA -> InputStylePreset.GLASS_INSET
            UiKitPreset.NEO_BRUTALISM_POP -> InputStylePreset.NEO_BRUTALIST_BOX
            else -> InputStylePreset.DEFAULT
        }
        val newLoaderStyle = when (kit) {
            UiKitPreset.STYLE_2_GLASS_LOADER -> LoaderStylePreset.STYLE_2_DUAL_RING
            UiKitPreset.CYBERPUNK_NEON -> LoaderStylePreset.CYBER_GLITCH
            UiKitPreset.GLASSMORPHISM_AURORA -> LoaderStylePreset.ORBIT_PLANETS
            UiKitPreset.NEUMORPHISM_CLAY -> LoaderStylePreset.NEUMORPHIC_PULSE
            UiKitPreset.RETRO_8BIT_ARCADE -> LoaderStylePreset.CYBER_GLITCH
            else -> LoaderStylePreset.BOUNCING_BARS
        }
        val newPatternStyle = when (kit) {
            UiKitPreset.STYLE_1_TILT_MAGNETIC -> PatternStylePreset.HEXAGON_MESH
            UiKitPreset.STYLE_4_BOTTOMBAR_APPBAR -> PatternStylePreset.DOT_MATRIX
            UiKitPreset.CYBERPUNK_NEON -> PatternStylePreset.CYBER_GRID
            UiKitPreset.GLASSMORPHISM_AURORA -> PatternStylePreset.HEXAGON_MESH
            UiKitPreset.NEUMORPHISM_CLAY -> PatternStylePreset.DEFAULT_WIND
            UiKitPreset.NEO_BRUTALISM_POP -> PatternStylePreset.DOT_MATRIX
            UiKitPreset.RETRO_8BIT_ARCADE -> PatternStylePreset.BLUEPRINT
            else -> PatternStylePreset.DEFAULT_WIND
        }

        _uiState.value = _uiState.value.copy(
            currentTheme = ThemePresetsRepository.defaultTheme,
            activeUiverseState = _uiState.value.activeUiverseState.copy(
                activeKit = kit,
                cardStyle = newCardStyle,
                buttonStyle = newBtnStyle,
                inputStyle = newInputStyle,
                loaderStyle = newLoaderStyle,
                patternStyle = newPatternStyle,
                customStyle = null
            )
        )
    }

    fun applyUiverseCustomCss(css: String, html: String) {
        val parsed = UiverseCssEngine.parseCss(css, html)
        // v1.1.15 修复「白底绿字」：云端自定义 CSS 不再覆盖本体主题色，经典皮肤恒定生效
        // （自定义 CSS 仍可作用于组件样式 activeUiverseState，但不改变全局主题颜色）
        _uiState.value = _uiState.value.copy(
            currentTheme = ThemePresetsRepository.defaultTheme,
            activeUiverseState = _uiState.value.activeUiverseState.copy(
                activeKit = UiKitPreset.CUSTOM_CODE,
                cardStyle = CardStylePreset.CUSTOM,
                buttonStyle = ButtonStylePreset.CUSTOM,
                inputStyle = InputStylePreset.CUSTOM,
                loaderStyle = LoaderStylePreset.CUSTOM,
                patternStyle = PatternStylePreset.CUSTOM,
                customStyle = parsed,
                customCssInput = css,
                customHtmlInput = html
            )
        )
    }

    fun applyUiverseItem(item: UiverseItem) {
        item.associatedKit?.let {
            applyUiverseKit(it)
            return
        }
        if (item.cssCode.isNotBlank()) {
            applyUiverseCustomCss(item.cssCode, item.htmlCode)
        }
    }

    fun resetUiverseToDefault() {
        _uiState.value = _uiState.value.copy(
            currentTheme = ThemePresetsRepository.defaultTheme,
            activeUiverseState = ActiveUiverseState()
        )
    }

    /**
     * 组件级定制：为指定 UI 组件独立应用自定义 CSS（精准修改单个组件样式，不影响其他组件）
     * 组件清单：首页头部/底部导航/搜索框/站点卡片/提示词卡片/软件卡片/工具箱卡片/弹窗/按钮/输入框
     */
    fun applyComponentTheme(compId: String, css: String) {
        val current = _uiState.value.activeUiverseState
        val newMap = current.componentThemes.toMutableMap().apply {
            if (css.isBlank()) remove(compId) else put(compId, css)
        }
        _uiState.value = _uiState.value.copy(
            activeUiverseState = current.copy(componentThemes = newMap)
        )
    }

    /**
     * v1.1.6 需求 4：应用控制台「主题工具箱」的组件级自定义代码。
     * themeKit: Map<组件id, {css, html}> —— 每个组件的自定义 CSS/HTML。
     * 解析后写入 componentThemes（组件级）与全局 customThemeCss。
     * v1.1.8 同步修复：云端组件 id 与本体消费端 id 统一映射（含别名），
     * 确保 appBar/顶栏、card/卡片、search/搜索框等全部组件都能实时生效。
     */
    fun applyCloudThemeKit(themeKit: Map<String, Any?>) {
        val globalCss = (themeKit["global"] as? Map<*, *>)?.get("css") as? String ?: ""
        if (globalCss.isNotBlank()) {
            try {
                applyUiverseCustomCss(globalCss, "")
            } catch (e: Exception) { }
        }
        // 逐组件应用：云端 id → 本体消费端 id（一个云端组件可写多个消费端别名，保证全部组件生效）
        val compMap = listOf(
            "appBar" to listOf("appBar", "home_header", "top_bar"),
            "bottomBar" to listOf("bottomBar", "bottom_nav"),
            "splash" to listOf("splash", "splash_screen"),
            "statusBar" to listOf("statusBar", "status_bar"),
            "card" to listOf("card", "card_item", "software_card"),
            "button" to listOf("button", "btn"),
            "dialog" to listOf("dialog", "update_dialog", "welcome_dialog"),
            "search" to listOf("search", "search_box"),
            "settingsPage" to listOf("settingsPage", "settings_page", "setrow")
        )
        compMap.forEach { (cloudId, compIds) ->
            val css = (themeKit[cloudId] as? Map<*, *>)?.get("css") as? String ?: ""
            if (css.isNotBlank()) {
                compIds.forEach { applyComponentTheme(it, css) }
            }
        }
    }

    fun uploadResource(type: String, title: String, desc: String, url: String, author: String, tags: String) {
        viewModelScope.launch {
            val entity = UploadedResourceEntity(
                id = "res_${UUID.randomUUID().toString().take(8)}",
                type = type,
                title = title,
                desc = desc,
                url = url,
                author = author,
                badge = "作者投递",
                tags = tags
            )
            repository.saveUploadedResource(entity)
        }
    }

    fun deleteUploadedResource(id: String) {
        viewModelScope.launch {
            repository.deleteUploadedResource(id)
            // 记录隐藏墓碑：云端轮询同步时跳过该 id，避免「删除后又出现」
            hideResource(id)
        }
    }

    /**
     * URL normalizer to reliably detect and exclude duplicate sites:
     * e.g. "https://www.bilibili.com/" -> "bilibili.com"
     */
    fun normalizeSiteUrl(rawUrl: String): String {
        return rawUrl.trim()
            .lowercase()
            .removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("www.")
            .substringBefore("?")
            .substringBefore("#")
            .trimEnd('/')
    }

    /**
     * Checks if a site with the same normalized URL or exact title already exists.
     * Returns the duplicate NavCard if found, or null if completely unique.
     */
    fun checkSiteDuplicate(url: String, title: String): NavCard? {
        val normUrl = normalizeSiteUrl(url)
        val cleanTitle = title.trim().lowercase()

        val allExisting = (NavData.getAllCards() + customSites.value.map { entity ->
            NavCard(
                id = entity.id,
                title = entity.title,
                desc = entity.desc,
                url = entity.url,
                icon = "",
                categoryId = entity.author.ifBlank { "tools" },
                subcatId = "all",
                badge = entity.badge
            )
        }).distinctBy { normalizeSiteUrl(it.url) }

        return allExisting.firstOrNull { card ->
            val cardNormUrl = normalizeSiteUrl(card.url)
            val cardTitle = card.title.trim().lowercase()
            (normUrl.isNotEmpty() && cardNormUrl == normUrl) ||
                    (cleanTitle.isNotEmpty() && cardTitle == cleanTitle)
        }
    }

    /**
     * Adds a new site with automatic duplicate exclusion.
     * Returns true if successfully added, or false if excluded as duplicate.
     */
    fun addNewSite(
        title: String,
        url: String,
        desc: String,
        categoryId: String,
        badge: String = "NEW",
        iconUrl: String = ""
    ): Boolean {
        val duplicate = checkSiteDuplicate(url, title)
        if (duplicate != null) {
            return false // Automatically excluded!
        }
        viewModelScope.launch {
            val entity = UploadedResourceEntity(
                id = "custom_site_${UUID.randomUUID().toString().take(8)}",
                type = "custom_site",
                title = title,
                desc = desc,
                url = url,
                author = categoryId,
                badge = badge.ifBlank { "NEW" },
                tags = if (iconUrl.isNotBlank()) iconUrl else "custom_site"
            )
            repository.saveUploadedResource(entity)
        }
        return true
    }

    val filteredCards: StateFlow<List<NavCard>> = combine(_uiState, favorites, customSites) { state, _, customList ->
        val query = state.searchQuery.trim().lowercase()

        // Map custom sites into NavCards with NEW badge type
        val customNavCards = customList.map { entity ->
            val customIcon = if (entity.tags.startsWith("http://") || entity.tags.startsWith("https://")) {
                entity.tags
            } else ""
            NavCard(
                id = entity.id,
                title = entity.title,
                desc = entity.desc,
                url = entity.url,
                icon = customIcon,
                categoryId = entity.author.ifBlank { "ai_models" },
                subcatId = "all",
                badge = entity.badge.ifBlank { "NEW" },
                badgeType = com.example.data.model.BadgeType.NEW
            )
        }

        // Automatic global deduplication across user-added and built-in sites (custom takes precedence on top)
        val allCards = (customNavCards + NavData.getAllCards()).distinctBy { normalizeSiteUrl(it.url) }

        // Pin new sites (custom sites and sites marked with NEW badge) to the top
        fun prioritizeNewSites(cards: List<NavCard>): List<NavCard> {
            return cards.sortedWith(
                compareByDescending<NavCard> { card ->
                    when {
                        // User customized new sites first
                        customNavCards.any { normalizeSiteUrl(it.url) == normalizeSiteUrl(card.url) } -> 3
                        // Built-in cards with NEW badge
                        card.badgeType == com.example.data.model.BadgeType.NEW ||
                                card.badge?.equals("NEW", ignoreCase = true) == true ||
                                card.badge?.contains("新", ignoreCase = true) == true -> 2
                        // Other hot/featured badges
                        card.badgeType == com.example.data.model.BadgeType.ROSE ||
                                card.badgeType == com.example.data.model.BadgeType.GOLD -> 1
                        else -> 0
                    }
                }
            )
        }

        if (query.isNotEmpty()) {
            val searchResults = allCards.filter {
                it.title.lowercase().contains(query) ||
                        it.desc.lowercase().contains(query) ||
                        (it.badge?.lowercase()?.contains(query) == true)
            }
            prioritizeNewSites(searchResults)
        } else {
            val currentCategory = state.categories.firstOrNull { it.id == state.selectedCategoryId }
                ?: state.categories.first()

            val categoryCustomCards = customNavCards.filter {
                it.categoryId == currentCategory.id || (currentCategory.id == "ai_models" && (it.categoryId == "ai" || it.categoryId == "ai_models"))
            }
            // Newly added sites pinned on top of the category
            val mergedCategoryCards = (categoryCustomCards + currentCategory.cards).distinctBy { normalizeSiteUrl(it.url) }

            val rawList = if (state.selectedSubcatId == "all") {
                mergedCategoryCards
            } else {
                mergedCategoryCards.filter { it.subcatId == state.selectedSubcatId }
            }
            prioritizeNewSites(rawList)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCategory(categoryId: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = categoryId,
            selectedSubcatId = "all",
            searchQuery = ""
        )
    }

    fun selectSubcategory(subcatId: String) {
        _uiState.value = _uiState.value.copy(selectedSubcatId = subcatId)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectSearchEngine(index: Int) {
        _uiState.value = _uiState.value.copy(selectedEngineIndex = index)
    }

    fun executeSearch(context: Context) {
        val query = _uiState.value.searchQuery.trim()
        val engine = NavData.searchEngines.getOrNull(_uiState.value.selectedEngineIndex) ?: NavData.searchEngines[0]

        if (engine.urlTemplate != null && query.isNotEmpty()) {
            val encoded = URLEncoder.encode(query, "UTF-8")
            val targetUrl = String.format(engine.urlTemplate, encoded)
            openUrl(context, targetUrl, "${engine.name}: $query")
        }
    }

    fun openCard(context: Context, card: NavCard) {
        viewModelScope.launch {
            repository.recordVisit(card)
        }
        openUrl(context, card.url, card.title)
    }

    fun openUrl(context: Context, url: String, label: String = "") {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "无法打开链接: $url", Toast.LENGTH_SHORT).show()
        }
    }

    fun toggleFavorite(card: NavCard, context: Context? = null) {
        viewModelScope.launch {
            val isFav = repository.toggleFavorite(card)
            if (context != null) {
                val msg = if (isFav) "已收藏: ${card.title}" else "已取消收藏"
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showDetail(card: NavCard) {
        _uiState.value = _uiState.value.copy(activeDetailCard = card)
    }

    fun hideDetail() {
        _uiState.value = _uiState.value.copy(activeDetailCard = null)
    }

    fun rollLuckyCard(categoryId: String? = null) {
        val pool = if (!categoryId.isNullOrBlank()) {
            NavData.categories.find { it.id == categoryId }?.cards?.takeIf { it.isNotEmpty() } ?: NavData.getAllCards()
        } else {
            NavData.getAllCards()
        }
        val randomCard = pool.randomOrNull() ?: return
        _uiState.value = _uiState.value.copy(
            luckyCard = randomCard,
            isLuckyModalVisible = true
        )
    }

    fun hideLuckyModal() {
        _uiState.value = _uiState.value.copy(isLuckyModalVisible = false)
    }

    fun setFavoritesModalVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(isFavoritesModalVisible = visible)
    }

    fun setHistoryModalVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(isHistoryModalVisible = visible)
    }

    fun clearHistory(context: Context? = null) {
        viewModelScope.launch {
            repository.clearHistory()
            context?.let {
                Toast.makeText(it, "已清空浏览历史", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun dismissSplash() {
        _uiState.value = _uiState.value.copy(isSplashVisible = false)
    }

    fun showSplash() {
        _uiState.value = _uiState.value.copy(isSplashVisible = true)
    }

    // ============ 本地背景媒体（主题版块本机上传） ============
    /** 设置本地背景媒体：type 为 image/video/none，uri 为本地内容 URI */
    fun setLocalBgMedia(type: String, uri: String) {
        _uiState.value = _uiState.value.copy(
            localBgMediaType = type,
            localBgMediaUri = uri
        )
    }

    /** 清除本地背景媒体（恢复默认/云端背景） */
    fun clearLocalBgMedia() {
        _uiState.value = _uiState.value.copy(
            localBgMediaType = "none",
            localBgMediaUri = ""
        )
    }
}

class NavViewModelFactory(
    private val repository: NavRepository,
    private val remoteConfigRepository: RemoteConfigRepository? = null,
    private val application: android.app.Application? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NavViewModel(repository, remoteConfigRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
