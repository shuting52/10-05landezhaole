package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AdminRepository
import com.example.model.ActivityLog
import com.example.model.AdminScreen
import com.example.model.ButtonType
import com.example.model.CardStatus
import com.example.model.CategoryItem
import com.example.model.ConnState
import com.example.model.ConsoleConfig
import com.example.model.FullSettingsConfig
import com.example.model.GithubConfig
import com.example.model.IpMonitorConfig
import com.example.model.LogActionType
import com.example.model.MarqueeConfig
import com.example.model.ResourceButton
import com.example.model.ResourceCard
import com.example.model.SkillItem
import com.example.model.SplashConfig
import com.example.model.StatItem
import com.example.model.SubCategoryItem
import com.example.model.TextItem
import com.example.model.ThemeKitConfig
import com.example.model.ToolItem
import com.example.model.UpdateDialogConfig
import com.example.model.WelcomeConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 反编译重建 · 控制台核心逻辑（ViewModel）
 *
 * 由 v2.1.0 APK 反编译的 AdminViewModel 重建为可读 Kotlin：
 * - 持有 admin-data.json 的 JSONObject（rootJson）作为唯一真相源
 * - 各 save*/delete* 方法修改 rootJson 后经 persistAndRefresh 写回 GitHub
 * - loadAdmin 支持 API / 镜像链双路径读取
 * - publishRelease 自动 bump 版本并同步 updateDialog（弹更新窗）
 */
class AdminViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AdminRepository(application)

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    private var rootJson: JSONObject? = null
    private var currentSha: String = ""
    private val customLogs = mutableListOf<ActivityLog>()

    init {
        val cfg = repo.config
        _uiState.value = _uiState.value.copy(
            githubOwner = cfg.owner,
            githubRepo = cfg.repo,
            githubBranch = cfg.branch,
            githubToken = repo.token,
            notifReview = repo.getNotificationPref("review"),
            notifDownload = repo.getNotificationPref("download"),
            notifWeekly = repo.getNotificationPref("weekly"),
        )
        loadAdmin()
    }

    // ---------------- 导航 & 弹窗 ----------------

    fun navigateTo(screen: AdminScreen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun showToast(msg: String) {
        _uiState.value = _uiState.value.copy(toastMessage = msg)
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = "")
    }

    fun setDateRange(range: String) {
        _uiState.value = _uiState.value.copy(dateRange = range)
        showToast("已切换到「$range」")
    }

    fun updateGithubInputs(token: String, owner: String, repoName: String, branch: String) {
        _uiState.value = _uiState.value.copy(
            githubToken = token,
            githubOwner = owner,
            githubRepo = repoName,
            githubBranch = branch,
        )
    }

    fun toggleNotification(key: String, enabled: Boolean) {
        repo.setNotificationPref(key, enabled)
        _uiState.value = when (key) {
            "review" -> _uiState.value.copy(notifReview = enabled)
            "download" -> _uiState.value.copy(notifDownload = enabled)
            "weekly" -> _uiState.value.copy(notifWeekly = enabled)
            else -> _uiState.value
        }
        showToast(if (enabled) "已开启通知" else "已关闭通知")
    }

    fun updateBasicSettingsInputs(appName: String, slogan: String) {
        val s = _uiState.value
        _uiState.value = s.copy(
            appName = appName,
            slogan = slogan,
            fullSettings = s.fullSettings.copy(appName = appName, slogan = slogan),
        )
    }

    // ---------------- 加载 / 连接 ----------------

    fun loadAdmin(preferMirror: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMsg = "")
            try {
                val (text, sha) = if (preferMirror) {
                    repo.ghReadMirror()
                } else {
                    runCatching { repo.ghReadText() }.getOrElse {
                        repo.ghReadMirror()
                    }
                }
                parseAndPublishState(JSONObject(text), ConnState.CONNECTED, nowTimeStr(), "")
                currentSha = sha
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    connState = ConnState.ERROR,
                    errorMsg = e.message ?: "加载失败",
                )
                // 本地缓存兜底
                runCatching {
                    parseAndPublishState(JSONObject(repo.readLocalOrBundled()), ConnState.READONLY, nowTimeStr(), e.message ?: "")
                }
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun connectGithub() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isConnecting = true, errorMsg = "")
            repo.setConfig(GithubConfig(
                _uiState.value.githubOwner,
                _uiState.value.githubRepo,
                _uiState.value.githubBranch,
            ))
            repo.token = _uiState.value.githubToken
            try {
                // 先走 API，失败走镜像链
                val (text, sha) = runCatching { repo.ghReadText() }.getOrElse { repo.ghReadMirror() }
                parseAndPublishState(JSONObject(text), ConnState.CONNECTED, nowTimeStr(), "")
                currentSha = sha
                showToast("连接成功")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    connState = ConnState.ERROR,
                    errorMsg = e.message ?: "连接失败",
                )
                showToast("连接失败：${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isConnecting = false)
            }
        }
    }

    // ---------------- 解析 admin-data.json → UI 状态 ----------------

    private fun parseAndPublishState(json: JSONObject, connState: ConnState, syncTime: String, errMsg: String) {
        rootJson = json

        // home.categories[] → categories / allCards
        val home = json.optJSONObject("home")
        val cats = home?.optJSONArray("categories") ?: JSONArray()
        val categories = mutableListOf<CategoryItem>()
        val allCards = mutableListOf<ResourceCard>()
        for (i in 0 until cats.length()) {
            val c = cats.optJSONObject(i) ?: continue
            val sub = c.optJSONArray("subcategories")
            val subs = mutableListOf<SubCategoryItem>()
            if (sub != null) for (j in 0 until sub.length()) {
                val s = sub.optJSONObject(j) ?: continue
                subs += SubCategoryItem(s.optString("id"), s.optString("name"))
            }
            val cat = CategoryItem(
                id = c.optString("id"),
                name = c.optString("name"),
                iconKey = c.optString("iconKey"),
                order = c.optInt("order", i + 1),
                status = c.optString("status", "enabled"),
                desc = c.optString("desc"),
                cardCount = 0,
                subcategories = subs,
            )
            categories += cat
            // 卡片
            val cards = c.optJSONArray("cards")
            if (cards != null) for (j in 0 until cards.length()) {
                val card = cards.optJSONObject(j) ?: continue
                allCards += ResourceCard(
                    id = card.optString("id"),
                    name = card.optString("title"),
                    description = card.optString("desc"),
                    buttonType = ButtonType.LINK,
                    size = card.optString("size", "-"),
                    downloads = 0,
                    status = CardStatus.PUBLISHED,
                    updatedAt = "-",
                    category = cat.name,
                    url = card.optString("url"),
                    icon = card.optString("icon"),
                    fallbackText = card.optString("fallbackText"),
                    categoryId = cat.id,
                    subcatId = card.optString("subcatId", "all"),
                    badge = card.optString("badge"),
                    badgeType = card.optString("badgeType"),
                    highlights = card.optString("highlights"),
                    rawJson = card,
                )
            }
        }
        // cardCount 回填
        val cardCounts = allCards.groupBy { it.categoryId }.mapValues { it.value.size }
        categories.forEach { cat ->
            cat.cardCount = cardCounts[cat.id] ?: 0
        }

        // software[]
        val softwares = json.optJSONArray("software")
        val buttons = mutableListOf<ResourceButton>()
        if (softwares != null) for (i in 0 until softwares.length()) {
            val s = softwares.optJSONObject(i) ?: continue
            buttons += ResourceButton(
                id = s.optString("id"),
                name = s.optString("title"),
                type = if (s.optString("mode", "url") != "url") ButtonType.DOWNLOAD else ButtonType.LINK,
                usageCount = 0,
                status = s.optString("status", "enabled"),
                updatedAt = s.optString("updatedAt", "-"),
                url = s.optString("url"),
                desc = s.optString("desc"),
                author = s.optString("author"),
                badge = s.optString("badge"),
                badgeType = s.optString("badgeType"),
                tags = s.optString("tags"),
                apkUrl = s.optString("apkUrl"),
                previewUrl = s.optString("previewUrl"),
                iconUrl = s.optString("iconUrl"),
                mode = s.optString("mode", "url"),
                rawJson = s,
            )
        }

        // skills[]
        val skillsArr = json.optJSONArray("skills")
        val skills = mutableListOf<SkillItem>()
        if (skillsArr != null) for (i in 0 until skillsArr.length()) {
            val s = skillsArr.optJSONObject(i) ?: continue
            skills += SkillItem(
                id = s.optString("id"),
                title = s.optString("title"),
                type = s.optString("type", "skill"),
                promptType = s.optString("promptType"),
                prompt = s.optString("prompt"),
                url = s.optString("url"),
                author = s.optString("author"),
                badge = s.optString("badge"),
                tags = s.optString("tags"),
                previewUrl = s.optString("previewUrl"),
                mediaUrl = s.optString("mediaUrl"),
                iconUrl = s.optString("iconUrl"),
                mode = s.optString("mode", "url"),
                desc = s.optString("desc"),
                rawJson = s,
            )
        }

        // uiText
        val uiTexts = json.optJSONObject("uiText")
        val texts = mutableListOf<TextItem>()
        if (uiTexts != null) for (key in uiTexts.keys()) {
            texts += TextItem(
                id = "txt_$key",
                key = key,
                title = key,
                content = uiTexts.optString(key),
                category = "UI 文本",
            )
        }

        // 配置段
        val ver = json.optJSONObject("version")
        val upd = json.optJSONObject("updateDialog")
        val splash = json.optJSONObject("splash")
        val welcome = json.optJSONObject("welcome")
        val marquee = json.optJSONObject("marquee")
        val console = json.optJSONObject("console")
        val ipMon = json.optJSONObject("ipMonitor")
        val themeKit = json.optJSONObject("themeKit")
        val settings = json.optJSONObject("settings")

        val stats = mutableListOf<StatItem>()

        _uiState.value = _uiState.value.copy(
            connState = connState,
            lastSyncAt = syncTime,
            errorMsg = errMsg,
            categories = categories,
            allCards = allCards,
            buttons = buttons,
            skills = skills,
            texts = texts,
            stats = stats,
            splashConfig = SplashConfig(
                type = splash?.optString("type") ?: "",
                mediaUrl = splash?.optString("mediaUrl") ?: "",
                durationSeconds = splash?.optInt("durationSeconds") ?: 0,
                bgColor = splash?.optString("bgColor") ?: "",
                customHtml = splash?.optString("customHtml") ?: "",
            ),
            welcomeConfig = WelcomeConfig(
                enabled = welcome?.optBoolean("enabled") ?: false,
                title = welcome?.optString("title") ?: "",
                welcomeText = welcome?.optString("welcomeText") ?: "",
                content = welcome?.optString("content") ?: "",
                imageUrl = welcome?.optString("imageUrl") ?: "",
                buttonText = welcome?.optString("buttonText") ?: "",
                ratio = welcome?.optString("ratio") ?: "",
            ),
            marqueeConfig = MarqueeConfig(
                enabled = marquee?.optBoolean("enabled") ?: false,
                defaultText = marquee?.optString("defaultText") ?: "",
                icon = marquee?.optString("icon") ?: "",
                segments = marquee?.optJSONArray("segments")?.let { arr ->
                    (0 until arr.length()).map { arr.optString(it) }
                } ?: emptyList(),
            ),
            updateDialogConfig = UpdateDialogConfig(
                title = upd?.optString("title") ?: "",
                confirmText = upd?.optString("confirmText") ?: "",
                cancelText = upd?.optString("cancelText") ?: "",
                changelog = upd?.optJSONArray("changelog")?.let { arr ->
                    (0 until arr.length()).map { arr.optString(it) }
                } ?: emptyList(),
                customCss = upd?.optString("customCss") ?: "",
                customHtml = upd?.optString("customHtml") ?: "",
            ),
            consoleConfig = ConsoleConfig(
                version = console?.optString("version") ?: "2.1.0",
                code = console?.optInt("code") ?: 58,
                apkUrl = console?.optString("apkUrl") ?: "",
            ),
            ipMonitorConfig = IpMonitorConfig(
                enabled = ipMon?.optBoolean("enabled") ?: false,
                url = ipMon?.optString("url") ?: "",
            ),
            themeKitConfig = ThemeKitConfig(
                appBarCss = themeKit?.optString("appBarCss") ?: "",
                bottomBarCss = themeKit?.optString("bottomBarCss") ?: "",
                buttonCss = themeKit?.optString("buttonCss") ?: "",
                cardCss = themeKit?.optString("cardCss") ?: "",
                dialogCss = themeKit?.optString("dialogCss") ?: "",
                searchCss = themeKit?.optString("searchCss") ?: "",
                splashCss = themeKit?.optString("splashCss") ?: "",
                settingsPageCss = themeKit?.optString("settingsPageCss") ?: "",
                statusBarCss = themeKit?.optString("statusBarCss") ?: "",
                globalCss = themeKit?.optString("globalCss") ?: "",
                customThemeCss = themeKit?.optString("customThemeCss") ?: "",
                customThemeHtml = themeKit?.optString("customThemeHtml") ?: "",
            ),
            fullSettings = FullSettingsConfig(
                appName = settings?.optString("appName") ?: "懒得找了",
                slogan = settings?.optString("slogan") ?: "",
                logoUrl = settings?.optString("logoUrl") ?: "",
                packageName = settings?.optString("packageName") ?: "",
                aboutText = settings?.optString("aboutText") ?: "",
                feedbackEmail = settings?.optString("feedbackEmail") ?: "",
                officialWebsite = settings?.optString("officialWebsite") ?: "",
                qqGroupUrl = settings?.optString("qqGroupUrl") ?: "",
                qqGroupUin = settings?.optString("qqGroupUin") ?: "",
                contactQQ = settings?.optString("contactQQ") ?: "",
                contactWechat = settings?.optString("contactWechat") ?: "",
                contactAlipay = settings?.optString("contactAlipay") ?: "",
                securityEnabled = settings?.optBoolean("securityEnabled") ?: false,
                securityExpectedSha = settings?.optString("securityExpectedSha") ?: "",
                serverShutdownEnabled = settings?.optBoolean("serverShutdownEnabled") ?: false,
                serverShutdownNotice = settings?.optString("serverShutdownNotice") ?: "",
            ),
            appName = settings?.optString("appName") ?: "懒得找了",
            slogan = settings?.optString("slogan") ?: "",
            versionCode = ver?.optInt("code") ?: 0,
            versionName = ver?.optString("name") ?: "",
            versionForce = ver?.optBoolean("force") ?: false,
            versionApkUrl = ver?.optString("apkUrl") ?: "",
            versionApkUrlRaw = ver?.optString("apkUrlRaw") ?: "",
            versionChangelog = ver?.optJSONArray("changelog")?.let { arr ->
                (0 until arr.length()).map { arr.optString(it) }
            } ?: emptyList(),
        )
    }

    // ---------------- 记录操作日志 ----------------

    private fun nowTimeStr(): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(Date())

    private fun recordLog(action: LogActionType, content: String) {
        val color = when (action) {
            LogActionType.CREATE -> 0xFF44B37B
            LogActionType.UPDATE -> 0xFFE9A23B
            LogActionType.PUBLISH -> 0xFFE94B4B
            LogActionType.DELETE -> 0xFFE94B4B
            LogActionType.REVIEW -> 0xFF5B8FF9
        }
        val cfg = repo.config
        customLogs.add(0, ActivityLog(
            id = "log_${System.currentTimeMillis()}",
            operator = "管理员",
            avatarColorHex = color,
            action = action,
            content = content,
            time = nowTimeStr(),
            ip = "${cfg.owner}/${cfg.repo}",
        ))
        _uiState.value = _uiState.value.copy(activityLogs = customLogs.toList())
    }

    // ---------------- 写回 GitHub（核心持久化） ----------------

    /**
     * 将修改后的 rootJson 写回 GitHub（带 409 冲突重试），
     * 更新本地缓存 + 记录日志 + 刷新连接状态。
     * @param commitMsg  提交信息
     * @param targetMode "content"（内容同步） / "release"（发布新版本）
     */
    private suspend fun persistAndRefresh(
        json: JSONObject,
        commitMsg: String,
        targetMode: String = "content",
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val newSha = repo.ghWriteText(
                path = AdminRepository.CONFIG_PATH,
                content = json.toString(2),
                sha = currentSha,
                message = commitMsg,
            )
            currentSha = newSha
            repo.saveLocalCache(json.toString(2))
            runCatching { repo.purgeCdn() }
            recordLog(
                if (targetMode == "release") LogActionType.PUBLISH else LogActionType.UPDATE,
                commitMsg,
            )
            true
        } catch (e: Exception) {
            recordLog(LogActionType.UPDATE, "保存失败：${e.message}")
            false
        }
    }

    // ---------------- 卡片 ----------------

    fun saveCard(
        existingCard: ResourceCard?,
        name: String,
        description: String,
        url: String,
        buttonType: ButtonType,
        status: CardStatus,
        categoryName: String,
        subcatId: String,
        icon: String,
        fallbackText: String,
        badge: String,
        badgeType: String,
        highlights: String,
    ) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val home = json.optJSONObject("home")
            val cats = home?.optJSONArray("categories") ?: return@launch
            var targetCat: JSONObject? = null
            for (i in 0 until cats.length()) {
                val c = cats.optJSONObject(i) ?: continue
                if (c.optString("name") == categoryName) { targetCat = c; break }
            }
            if (targetCat == null) { showToast("分类「$categoryName」不存在，请先创建"); return@launch }
            val cards = targetCat.optJSONArray("cards") ?: JSONArray().also { targetCat.put("cards", it) }

            val card = JSONObject()
                .put("id", existingCard?.id ?: "site_${System.currentTimeMillis()}")
                .put("title", name)
                .put("desc", description)
                .put("url", url)
                .put("icon", icon)
                .put("fallbackText", fallbackText)
                .put("badge", badge)
                .put("badgeType", badgeType)
                .put("highlights", highlights)
                .put("categoryId", targetCat.optString("id"))
                .put("subcatId", subcatId)

            val idx = (0 until cards.length()).firstOrNull { cards.optJSONObject(it)?.optString("id") == existingCard?.id }
            if (idx != null) cards.put(idx, card) else cards.put(card)

            val ok = persistAndRefresh(json, "console: 卡片「$name」${if (existingCard != null) "编辑" else "新增"}")
            if (ok) { showToast("已保存卡片「$name」"); loadAdmin(true) }
        }
    }

    fun deleteCard(card: ResourceCard) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val cats = json.optJSONObject("home")?.optJSONArray("categories") ?: return@launch
            for (i in 0 until cats.length()) {
                val c = cats.optJSONObject(i) ?: continue
                val cards = c.optJSONArray("cards") ?: continue
                for (j in 0 until cards.length()) {
                    if (cards.optJSONObject(j)?.optString("id") == card.id) {
                        cards.remove(j)
                        val ok = persistAndRefresh(json, "console: 删除卡片 ${card.id}")
                        if (ok) { showToast("已删除卡片"); loadAdmin(true) }
                        return@launch
                    }
                }
            }
            showToast("卡片不存在")
        }
    }

    // ---------------- 本地文件上传 ----------------

    fun uploadLocalFile(uri: Uri, subFolder: String = "auto", onSuccess: (String, String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUploadingFile = true, uploadingLabel = "正在上传…")
            try {
                val folder = if (subFolder == "auto") {
                    val name = uri.toString().substringAfterLast("/", "file")
                    if (name.lowercase(Locale.ROOT).endsWith(".apk")) "dist/apk" else "dist/uploads"
                } else subFolder
                val rawUrl = repo.uploadBinaryFile(uri, null, folder)
                recordLog(LogActionType.CREATE, "上传文件 ${rawUrl.substringAfterLast("/")}")
                showToast("上传成功")
                onSuccess(rawUrl, rawUrl.substringAfterLast("/"))
            } catch (e: Exception) {
                showToast("上传失败：${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isUploadingFile = false, uploadingLabel = "")
            }
        }
    }

    // ---------------- 软件 / Skill / 文本 ----------------

    fun saveSoftware(
        existing: ResourceButton?,
        name: String, url: String, desc: String, author: String,
        badge: String, badgeType: String, tags: String,
        apkUrl: String, previewUrl: String, iconUrl: String, mode: String,
    ) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val arr = json.optJSONArray("software") ?: JSONArray().also { json.put("software", it) }
            val item = JSONObject()
                .put("id", existing?.id ?: "sw_${System.currentTimeMillis()}")
                .put("title", name).put("url", url).put("desc", desc)
                .put("author", author).put("badge", badge).put("badgeType", badgeType)
                .put("tags", tags).put("apkUrl", apkUrl).put("previewUrl", previewUrl)
                .put("iconUrl", iconUrl).put("mode", mode)
            val idx = (0 until arr.length()).firstOrNull { arr.optJSONObject(it)?.optString("id") == existing?.id }
            if (idx != null) arr.put(idx, item) else arr.put(item)
            val ok = persistAndRefresh(json, "console: 软件「$name」${if (existing != null) "编辑" else "新增"}")
            if (ok) { showToast("已保存软件「$name」"); loadAdmin(true) }
        }
    }

    fun deleteSoftware(btn: ResourceButton) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val arr = json.optJSONArray("software") ?: return@launch
            for (i in 0 until arr.length()) {
                if (arr.optJSONObject(i)?.optString("id") == btn.id) {
                    arr.remove(i)
                    val ok = persistAndRefresh(json, "console: 删除软件 ${btn.id}")
                    if (ok) { showToast("已删除软件"); loadAdmin(true) }
                    return@launch
                }
            }
            showToast("软件不存在")
        }
    }

    fun saveSkill(
        existing: SkillItem?,
        title: String, desc: String, promptType: String, prompt: String,
        url: String, author: String, badge: String, tags: String,
        previewUrl: String, mediaUrl: String, iconUrl: String, mode: String,
    ) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val arr = json.optJSONArray("skills") ?: JSONArray().also { json.put("skills", it) }
            val item = JSONObject()
                .put("id", existing?.id ?: "sk_${System.currentTimeMillis()}")
                .put("title", title).put("desc", desc).put("promptType", promptType)
                .put("prompt", prompt).put("url", url).put("author", author)
                .put("badge", badge).put("tags", tags).put("previewUrl", previewUrl)
                .put("mediaUrl", mediaUrl).put("iconUrl", iconUrl).put("mode", mode)
                .put("type", existing?.type ?: "skill")
            val idx = (0 until arr.length()).firstOrNull { arr.optJSONObject(it)?.optString("id") == existing?.id }
            if (idx != null) arr.put(idx, item) else arr.put(item)
            val ok = persistAndRefresh(json, "console: Skill「$title」${if (existing != null) "编辑" else "新增"}")
            if (ok) { showToast("已保存 Skill「$title」"); loadAdmin(true) }
        }
    }

    fun deleteSkill(skill: SkillItem) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val arr = json.optJSONArray("skills") ?: return@launch
            for (i in 0 until arr.length()) {
                if (arr.optJSONObject(i)?.optString("id") == skill.id) {
                    arr.remove(i)
                    val ok = persistAndRefresh(json, "console: 删除 Skill ${skill.id}")
                    if (ok) { showToast("已删除 Skill"); loadAdmin(true) }
                    return@launch
                }
            }
            showToast("Skill 不存在")
        }
    }

    fun saveText(keyName: String, content: String, isNew: Boolean) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val uiTexts = json.optJSONObject("uiText") ?: JSONObject().also { json.put("uiText", it) }
            uiTexts.put(keyName, content)
            val ok = persistAndRefresh(json, "console: UI 文本「$keyName」${if (isNew) "新增" else "更新"}")
            if (ok) { showToast("已保存文本「$keyName」"); loadAdmin(true) }
        }
    }

    fun deleteText(item: TextItem) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val uiTexts = json.optJSONObject("uiText") ?: return@launch
            if (!uiTexts.has(item.key)) { showToast("文本不存在"); return@launch }
            uiTexts.remove(item.key)
            val ok = persistAndRefresh(json, "console: 删除文本 ${item.key}")
            if (ok) { showToast("已删除文本"); loadAdmin(true) }
        }
    }

    // ---------------- 分类 ----------------

    fun saveCategory(existing: CategoryItem?, name: String, iconKey: String, desc: String, subcategories: List<SubCategoryItem>) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val home = json.optJSONObject("home")
            val cats = home?.optJSONArray("categories") ?: JSONArray().also { home?.put("categories", it) }
            val cat = JSONObject()
                .put("id", existing?.id ?: "cat_${System.currentTimeMillis()}")
                .put("name", name)
                .put("iconKey", iconKey)
                .put("desc", desc)
                .put("order", existing?.order ?: cats.length() + 1)
                .put("subcategories", JSONArray().apply {
                    subcategories.forEach { put(JSONObject().put("id", it.id).put("name", it.name)) }
                })
            if (existing == null) cat.put("cards", JSONArray())
            val idx = (0 until cats.length()).firstOrNull { cats.optJSONObject(it)?.optString("id") == existing?.id }
            if (idx != null) cats.put(idx, cat) else cats.put(cat)
            val ok = persistAndRefresh(json, "console: 分类「$name」${if (existing != null) "编辑" else "新增"}")
            if (ok) { showToast("已保存分类「$name」"); loadAdmin(true) }
        }
    }

    fun deleteCategory(cat: CategoryItem) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val cats = json.optJSONObject("home")?.optJSONArray("categories") ?: return@launch
            for (i in 0 until cats.length()) {
                if (cats.optJSONObject(i)?.optString("id") == cat.id) {
                    cats.remove(i)
                    val ok = persistAndRefresh(json, "console: 删除分类 ${cat.id}")
                    if (ok) { showToast("已删除分类"); loadAdmin(true) }
                    return@launch
                }
            }
            showToast("分类不存在")
        }
    }

    fun moveCategory(id: String, dir: Int) {
        val current = _uiState.value.categories
            .sortedBy { it.order }
            .toMutableList()
        val idx = current.indexOfFirst { it.id == id }
        if (idx < 0) return
        val target = idx + dir
        if (target < 0 || target >= current.size) return
        val tmp = current[idx]
        current[idx] = current[target]
        current[target] = tmp
        current.forEachIndexed { i, c -> c.order = i + 1 }
        _uiState.value = _uiState.value.copy(categories = current)
        showToast("排序已调整，请点「保存排序」同步到本体")
    }

    fun saveCategoryOrder() {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            val cats = json.optJSONObject("home")?.optJSONArray("categories") ?: return@launch
            val orderMap = _uiState.value.categories.withIndex().associate { it.value.id to (it.index + 1) }
            for (i in 0 until cats.length()) {
                val c = cats.optJSONObject(i) ?: continue
                orderMap[c.optString("id")]?.let { c.put("order", it) }
            }
            val ok = persistAndRefresh(json, "console: 保存分类排序")
            if (ok) { showToast("分类排序已同步"); loadAdmin(true) }
        }
    }

    // ---------------- 配置段保存 ----------------

    fun saveSplashConfig(cfg: SplashConfig) {
        viewModelScope.launch { saveConfigObject("splash", cfg.toJson(), "console: 保存开屏配置") }
    }

    fun saveWelcomeConfig(cfg: WelcomeConfig) {
        viewModelScope.launch { saveConfigObject("welcome", cfg.toJson(), "console: 保存欢迎页配置") }
    }

    fun saveMarqueeConfig(cfg: MarqueeConfig) {
        viewModelScope.launch { saveConfigObject("marquee", cfg.toJson(), "console: 保存跑马灯配置") }
    }

    fun saveUpdateDialogAndVersionConfig(
        updCfg: UpdateDialogConfig, vName: String, vCode: Int, vForce: Boolean,
        vApkUrl: String, vApkUrlRaw: String,
    ) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            json.put("updateDialog", updCfg.toJson())
            val ver = json.optJSONObject("version") ?: JSONObject().also { json.put("version", it) }
            ver.put("name", vName).put("code", vCode).put("force", vForce)
                .put("apkUrl", vApkUrl).put("apkUrlRaw", vApkUrlRaw)
            val ok = persistAndRefresh(json, "console: 保存更新弹窗与版本配置（v$vName / code $vCode）")
            if (ok) { showToast("已保存更新配置"); loadAdmin(true) }
        }
    }

    fun saveMiscModulesConfig(consoleCfg: ConsoleConfig, ipCfg: IpMonitorConfig, aiNoticeText: String, tools: List<ToolItem>) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            json.put("console", consoleCfg.toJson())
            json.put("ipMonitor", ipCfg.toJson())
            json.put("_ai_notice", aiNoticeText)
            json.put("tools", JSONArray().apply {
                tools.forEach { put(it.toJson()) }
            })
            val ok = persistAndRefresh(json, "console: 保存控制台/IP监控/工具箱配置")
            if (ok) { showToast("已保存模块配置"); loadAdmin(true) }
        }
    }

    fun saveThemeKitConfig(cfg: ThemeKitConfig) {
        viewModelScope.launch { saveConfigObject("themeKit", cfg.toJson(), "console: 保存主题工具箱配置") }
    }

    fun saveFullSettings(cfg: FullSettingsConfig) {
        viewModelScope.launch {
            val json = rootJson ?: return@launch
            json.put("settings", cfg.toJson())
            // 兼容旧字段：appName/slogan 顶层同步
            json.put("appName", cfg.appName).put("slogan", cfg.slogan)
            val ok = persistAndRefresh(json, "console: 保存系统设置")
            if (ok) { showToast("设置已保存"); loadAdmin(true) }
        }
    }

    fun saveBasicSettings() {
        val s = _uiState.value
        saveFullSettings(s.fullSettings.copy(appName = s.appName, slogan = s.slogan))
    }

    private suspend fun saveConfigObject(key: String, cfgJson: JSONObject, msg: String) {
        val json = rootJson ?: return
        json.put(key, cfgJson)
        val ok = persistAndRefresh(json, msg)
        if (ok) { showToast("已保存"); loadAdmin(true) }
    }

    // ---------------- 应用到设备 / 发布 ----------------

    fun applyToDevice() {
        // 将当前 UI 输入同步为内存 rootJson（不写仓库），供本体连接时读取
        val s = _uiState.value
        val json = rootJson ?: return
        json.put("appName", s.appName).put("slogan", s.slogan)
        if (s.fullSettings.appName.isNotBlank()) {
            json.put("settings", s.fullSettings.toJson())
        }
        showToast("已应用到设备（下次本体轮询生效）")
    }

    fun publishRelease(changeDesc: String = "更新") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPublishing = true)
            try {
                val json = rootJson ?: return@launch
                // bump 版本
                val ver = json.optJSONObject("version")
                    ?: JSONObject().also { json.put("version", it) }
                val oldCode = ver.optInt("code", 0)
                val oldName = ver.optString("name", "1.0")
                val newCode = oldCode + 1
                val parts = oldName.split(".")
                val newName = runCatching {
                    parts.dropLast(1).joinToString(".") + "." + (parts.lastOrNull()?.toIntOrNull()?.plus(1) ?: 1)
                }.getOrDefault("$oldName.1")
                ver.put("code", newCode).put("name", newName)
                // 更新弹窗
                val changelog = listOf("✨ v$newName 更新来啦～", changeDesc)
                ver.put("changelog", JSONArray().apply { changelog.forEach { put(it) } })
                json.put("updateDialog", JSONObject()
                    .put("title", "懒得找了 v$newName 已上线")
                    .put("changelog", JSONArray().apply { changelog.forEach { put(it) } })
                    .put("confirmText", "立即更新")
                    .put("cancelText", "稍后再说"))
                val ok = persistAndRefresh(json, "console: 发布新版本 v$newName (code $newCode)", "release")
                if (ok) {
                    showToast("已发布 v$newName，本体将弹更新窗")
                    loadAdmin(true)
                } else {
                    showToast("发布失败，请检查 Token 与网络")
                }
            } finally {
                _uiState.value = _uiState.value.copy(isPublishing = false)
            }
        }
    }
}

// ---------------- 模型 → JSON 工具（写回仓库用） ----------------

fun SplashConfig.toJson() = JSONObject()
    .put("type", type).put("mediaUrl", mediaUrl)
    .put("durationSeconds", durationSeconds).put("bgColor", bgColor)
    .put("customHtml", customHtml)

fun WelcomeConfig.toJson() = JSONObject()
    .put("enabled", enabled).put("title", title).put("welcomeText", welcomeText)
    .put("content", content).put("imageUrl", imageUrl)
    .put("buttonText", buttonText).put("ratio", ratio)

fun MarqueeConfig.toJson() = JSONObject()
    .put("enabled", enabled).put("defaultText", defaultText).put("icon", icon)
    .put("segments", JSONArray().apply { segments.forEach { put(it) } })

fun UpdateDialogConfig.toJson() = JSONObject()
    .put("title", title).put("confirmText", confirmText).put("cancelText", cancelText)
    .put("changelog", JSONArray().apply { changelog.forEach { put(it) } })
    .put("customCss", customCss).put("customHtml", customHtml)

fun ConsoleConfig.toJson() = JSONObject()
    .put("version", version).put("code", code).put("apkUrl", apkUrl)

fun IpMonitorConfig.toJson() = JSONObject()
    .put("enabled", enabled).put("url", url)

fun ThemeKitConfig.toJson() = JSONObject()
    .put("appBarCss", appBarCss).put("bottomBarCss", bottomBarCss).put("buttonCss", buttonCss)
    .put("cardCss", cardCss).put("dialogCss", dialogCss).put("searchCss", searchCss)
    .put("splashCss", splashCss).put("settingsPageCss", settingsPageCss)
    .put("statusBarCss", statusBarCss).put("globalCss", globalCss)
    .put("customThemeCss", customThemeCss).put("customThemeHtml", customThemeHtml)

fun FullSettingsConfig.toJson() = JSONObject()
    .put("appName", appName).put("slogan", slogan).put("logoUrl", logoUrl)
    .put("packageName", packageName).put("aboutText", aboutText)
    .put("feedbackEmail", feedbackEmail).put("officialWebsite", officialWebsite)
    .put("qqGroupUrl", qqGroupUrl).put("qqGroupUin", qqGroupUin)
    .put("contactQQ", contactQQ).put("contactWechat", contactWechat)
    .put("contactAlipay", contactAlipay)
    .put("securityEnabled", securityEnabled).put("securityExpectedSha", securityExpectedSha)
    .put("serverShutdownEnabled", serverShutdownEnabled)
    .put("serverShutdownNotice", serverShutdownNotice)

fun ToolItem.toJson() = JSONObject()
    .put("id", id).put("title", title).put("url", url).put("desc", desc)
