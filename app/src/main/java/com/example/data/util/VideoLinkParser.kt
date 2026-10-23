package com.example.data.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * 分享链接 → 媒体直链解析器 v2（v1.4.21 去水印大升级）
 *
 * 移植自 GitHub 开源项目（遵循其开源协议）：
 * - https://github.com/wujunwei928/parse-video-py （MIT）：抖音 / 快手 / 微博 / 西瓜 / 好看视频解析逻辑
 * - https://github.com/Johnserf-Seed/f2 （Apache-2.0）：抖音无水印直链思路（playwm→play）
 * - 保留 v1.4.13 自研：小红书 __INITIAL_STATE__、og:image 真实封面、通用回退提取
 *
 * 解析策略：平台专用解析器 → 通用 HTML 提取回退；解析不到真实直链时返回空，
 * UI 明确提示失败，禁止把分享页链接伪装成可下载直链。
 */
data class MediaParseResult(
    val videoUrls: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val coverUrl: String? = null,
    val title: String? = null,
    val author: String? = null,          // v1.4.21：真实作者昵称
    val platform: String? = null,        // v1.4.21：平台名
    val qualityOptions: List<Pair<String, String>> = emptyList(), // v1.4.31：画质选项（标签 to 直链）
    val livePhotos: List<LivePhoto> = emptyList(), // v1.6.5：Live 实况图（静态图 + 动态视频成对）
    val description: String? = null,     // v1.7.0：文案/描述
    val topics: List<String> = emptyList() // v1.7.0：话题标签
)

/** v1.6.5：Live 实况图 —— 一张静态封面 + 一段动态视频 */
data class LivePhoto(
    val imageUrl: String,
    val videoUrl: String
)

object VideoLinkParser {

    private const val UA =
        "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1"
    private const val UA_ANDROID =
        "Mozilla/5.0 (Linux; Android 14; 23127PN0CC) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36"

    private val clientFollow: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .build()
    }
    private val clientNoFollow: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(true)
            .build()
    }

    // ---------------- 主入口 ----------------
    suspend fun resolveMedia(context: Context, shareUrl: String): MediaParseResult =
        withContext(Dispatchers.IO) {
            val url = shareUrl.trim()
            if (url.isBlank()) return@withContext MediaParseResult()
            try {
                // 1. 直链直接自动分析判定（避免把直接的 mp4/mov 走网页抓取）
                val cleanUrl = url.substringBefore('?')
                val lowerClean = cleanUrl.lowercase()
                if (lowerClean.endsWith(".mp4") || lowerClean.endsWith(".mov") || lowerClean.endsWith(".webm") || lowerClean.endsWith(".m3u8") || lowerClean.endsWith(".flv")) {
                    val rawFileName = cleanUrl.substringAfterLast('/')
                    return@withContext MediaParseResult(
                        videoUrls = listOf(url),
                        title = rawFileName.ifBlank { "视频直链_${System.currentTimeMillis() % 10000}" },
                        platform = "直链视频"
                    )
                }
                if (lowerClean.endsWith(".jpg") || lowerClean.endsWith(".jpeg") || lowerClean.endsWith(".png") || lowerClean.endsWith(".webp") || lowerClean.endsWith(".gif")) {
                    val rawFileName = cleanUrl.substringAfterLast('/')
                    return@withContext MediaParseResult(
                        imageUrls = listOf(url),
                        title = rawFileName.ifBlank { "图片直链_${System.currentTimeMillis() % 10000}" },
                        platform = "直链图片"
                    )
                }

                val lower = url.lowercase()
                val specific: MediaParseResult? = when {
                    "douyin.com" in lower || "iesdouyin.com" in lower -> parseDouyin(url)
                    "kuaishou.com" in lower || "kwai.com" in lower || "v.kuaishou" in lower -> parseKuaishou(url)
                    "weibo.com" in lower || "weibo.cn" in lower -> parseWeibo(url)
                    "bilibili.com" in lower || "b23.tv" in lower || "bili" in lower -> parseBilibili(url)
                    "ixigua.com" in lower -> parseXigua(url)
                    "haokan.baidu.com" in lower -> parseHaokan(url)
                    "xiaohongshu.com" in lower || "xhslink.com" in lower -> parseXiaohongshu(url)
                    "weishi.qq.com" in lower -> parseWeishi(url)
                    "pipix" in lower -> parsePipixia(url) // v1.7.45：皮皮虾专用解析（移植 qushuiyin/ppxia.php）；分享域名为 h5.pipix.com，"pipixia.com" 匹配不到
                    "ippzone.com" in lower || "pipigx.com" in lower -> parsePipigx(url) // v1.7.45：皮皮搞笑
                    "izuiyou" in lower -> parseZuiyou(url) // v1.7.45：最右
                    "doubao.com" in lower -> parseDoubao(url) // v1.7.45：豆包视频
                    "xiaoyunque.jianying.com" in lower -> parseYunque(url) // 2026-10-23：小云雀视频（移植 MIT 项目 ihmily/doubao-nomark 的 yunque_video_parse）
                    "jimeng.jianying.com" in lower || "jimeng.aiseet.atry.com" in lower -> parseJimeng(url) // v1.7.45：即梦AI
                    "x.com" in lower || "twitter.com" in lower -> parseX(url) // v1.4.29：X 视频直链
                    "tiktok.com" in lower || "vt.tiktok" in lower || "vm.tiktok" in lower -> parseTikTok(url) // v1.4.30：TikTok 无水印
                    else -> null
                }
                if (specific != null && (specific.videoUrls.isNotEmpty() || specific.imageUrls.isNotEmpty())) {
                    return@withContext specific
                }
                // 通用回退
                val html = getString(url, referer = url) ?: return@withContext (specific ?: MediaParseResult())
                val generic = extractMedia(html, url)
                // 合并专用解析的标题/作者/封面（通用提取可能没有）
                if (specific != null) {
                    generic.copy(
                        title = specific.title ?: generic.title,
                        author = specific.author ?: generic.author,
                        coverUrl = specific.coverUrl ?: generic.coverUrl,
                        platform = specific.platform ?: generic.platform,
                        videoUrls = if (specific.videoUrls.isNotEmpty()) specific.videoUrls else generic.videoUrls,
                        imageUrls = if (specific.videoUrls.isNotEmpty() && generic.videoUrls.isEmpty()) specific.imageUrls else generic.imageUrls
                    )
                } else generic
            } catch (_: Exception) {
                MediaParseResult()
            }
        }

    /** 兼容旧接口：只取视频直链 */
    suspend fun resolve(context: Context, shareUrl: String): List<String> =
        resolveMedia(context, shareUrl).videoUrls

    // ---------------- HTTP 工具 ----------------
    private fun getString(url: String, ua: String = UA_ANDROID, referer: String? = null): String? {
        return try {
            val builder = Request.Builder().url(url).header("User-Agent", ua)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
            if (referer != null) builder.header("Referer", referer)
            clientFollow.newCall(builder.get().build()).execute().use { resp ->
                if (!resp.isSuccessful) return null
                resp.body?.string()
            }
        } catch (_: Exception) { null }
    }

    private fun getFinalRedirectUrl(url: String, ua: String = UA): String {
        return try {
            val req = Request.Builder().url(url).header("User-Agent", ua).get().build()
            clientFollow.newCall(req).execute().use { resp ->
                resp.request.url.toString()
            }
        } catch (_: Exception) { url }
    }

    private fun getRedirectLocation(url: String, ua: String = UA): String? {
        return try {
            val req = Request.Builder().url(url).header("User-Agent", ua).get().build()
            clientNoFollow.newCall(req).execute().use { resp ->
                resp.header("Location")?.takeIf { it.isNotBlank() }
            }
        } catch (_: Exception) { null }
    }

    private fun postForm(url: String, body: String, headers: Map<String, String>): String? {
        return try {
            val builder = Request.Builder().url(url)
            headers.forEach { (k, v) -> builder.header(k, v) }
            val reqBody = body.toRequestBody("application/x-www-form-urlencoded".toMediaType())
            clientFollow.newCall(builder.post(reqBody).build()).execute().use { resp ->
                if (!resp.isSuccessful) return null
                resp.body?.string()
            }
        } catch (_: Exception) { null }
    }

    private fun getJson(url: String, ua: String = UA, referer: String? = null): JSONObject? {
        return try {
            val s = getString(url, ua, referer) ?: return null
            JSONObject(s)
        } catch (_: Exception) { null }
    }

    /** POST JSON 并解析返回的 JSONObject（皮皮搞笑/最右/豆包/即梦用） */
    private fun postJson(
        url: String,
        jsonBody: String,
        headers: Map<String, String>,
        ua: String = UA
    ): JSONObject? {
        return try {
            val builder = Request.Builder().url(url).header("User-Agent", ua)
            headers.forEach { (k, v) -> builder.header(k, v) }
            val reqBody = jsonBody.toRequestBody("application/json".toMediaType())
            clientFollow.newCall(builder.post(reqBody).build()).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val s = resp.body?.string() ?: return null
                JSONObject(s)
            }
        } catch (_: Exception) { null }
    }

    private fun unescapeUrl(u: String): String =
        u.replace("\\u002F", "/").replace("\\/", "/").replace("\\u0026", "&").trim()

    private fun pickNonWebp(urls: List<String>): String =
        urls.firstOrNull { it.isNotBlank() && !it.lowercase().endsWith(".webp") }
            ?: urls.firstOrNull { it.isNotBlank() } ?: ""

    // ================= 抖音（移植 short_videos/DouyinParser.php，MIT） =================
    // 免 Cookie 详情接口：api.amemv.com/aweme/v1/aweme/detail/，视频/图集/实况通用
    // 原画直链：vid 经 aweme.snssdk.com 302 解析
    private fun parseDouyin(shareUrl: String): MediaParseResult? {
        return try {
            var url = extractShareUrlSv(shareUrl) ?: return null
            // 短链或无 ID 先跟随重定向
            val host = try { java.net.URL(url).host.lowercase() } catch (_: Exception) { "" }
            if (host == "v.douyin.com" || "douyin.com" !in host || extractAwemeIdSv(url).isBlank()) {
                url = getFinalRedirectUrl(url)
            }
            val awemeId = extractAwemeIdSv(url).ifBlank { return null }
            if (!awemeId.matches(Regex("[1-9][0-9]{15,19}"))) return null

            // 免 Cookie 详情接口
            val apiUrl = "https://api.amemv.com/aweme/v1/aweme/detail/" +
                "?aweme_id=$awemeId&aid=6383&device_platform=web&version_code=190500&version_name=19.5.0"
            val jsonStr = getString(apiUrl, UA_MOBILE_SV) ?: return null
            val root = try { JSONObject(jsonStr) } catch (_: Exception) { return null }
            if (root.optInt("status_code", -1) != 0) return null
            val detail = root.optJSONObject("aweme_detail") ?: return null
            if (detail.optString("aweme_id") != awemeId) return null

            formatDouyinDetail(detail, awemeId)
        } catch (_: Exception) { null }
    }

    private val UA_MOBILE_SV =
        "Mozilla/5.0 (Linux; Android 13; 2210132C Build/TKQ1.220829.002) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Mobile Safari/537.36"
    private val UA_DESKTOP_SV =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"

    /** 从分享文本提取首个 http(s) 链接 */
    private fun extractShareUrlSv(text: String): String? {
        val m = Regex("https?://[^\\s\"'<>]+", RegexOption.IGNORE_CASE).find(text) ?: return null
        return m.value.trimEnd('\'', '"', '，', '。', '；', ',', '.', ';')
    }

    /** 提取 aweme_id（数字 ID） */
    private fun extractAwemeIdSv(url: String): String {
        return try {
            val u = java.net.URL(url)
            // /video/{id} / /note/{id}
            val last = u.path.trim('/').split("/").lastOrNull() ?: ""
            if (last.matches(Regex("\\d{16,20}"))) return last
            // query 参数
            Regex("[?&](?:aweme_id|modal_id)=(\\d{16,20})").find(url)?.groupValues?.get(1) ?: ""
        } catch (_: Exception) { "" }
    }

    /** 格式化抖音详情 JSON → MediaParseResult */
    private fun formatDouyinDetail(detail: JSONObject, awemeId: String): MediaParseResult? {
        val title = detail.optString("desc", "")
        val authorObj = detail.optJSONObject("author")
        val author = authorObj?.optString("nickname")?.takeIf { it.isNotBlank() }

        // 封面
        val coverUrl = pickDouyinCover(detail)

        // 图集 / 实况
        var imagesJson = detail.optJSONArray("images")
        if (imagesJson == null || imagesJson.length() == 0) {
            imagesJson = detail.optJSONArray("image_list")
        }
        if (imagesJson != null && imagesJson.length() > 0) {
            val images = mutableListOf<String>()
            val livePhotos = mutableListOf<LivePhoto>()
            for (i in 0 until imagesJson.length()) {
                val img = imagesJson.optJSONObject(i) ?: continue
                val imgUrl = pickDouyinImageUrl(img) ?: continue
                images.add(imgUrl)
                // 实况视频
                val videoInfo = img.optJSONObject("video")
                var liveVideoUrl = extractLiveVideoUrl(videoInfo)
                if (liveVideoUrl != null) {
                    liveVideoUrl = liveVideoUrl.replace("playwm", "play")
                    // vid 转原画
                    val vid = extractVid(videoInfo)
                    val resolved = if (vid != null) resolveOriginalUrl(vid) else null
                    livePhotos.add(LivePhoto(imgUrl, (resolved ?: liveVideoUrl).let { ensureMp4(it) }))
                }
            }
            if (livePhotos.isNotEmpty()) {
                return MediaParseResult(
                    videoUrls = emptyList(),
                    imageUrls = images.distinct(),
                    coverUrl = coverUrl ?: images.firstOrNull(),
                    title = title.ifBlank { "抖音实况 · $awemeId" },
                    author = author ?: "抖音作者",
                    platform = "抖音",
                    livePhotos = livePhotos
                )
            }
            return MediaParseResult(
                videoUrls = emptyList(),
                imageUrls = images.distinct().take(20),
                coverUrl = coverUrl ?: images.firstOrNull(),
                title = title.ifBlank { "抖音精选图集 · $awemeId" },
                author = author ?: "抖音作者",
                platform = "抖音"
            )
        }

        // 视频
        val video = detail.optJSONObject("video") ?: return null
        val (bestUrl, backups, gearName) = extractBestVideo(video)
        var main = bestUrl?.replace("playwm", "play")?.let { toHttps(it) }
        // 原画优先：vid 302 解析
        val vid = extractVid(video)
        var quality = gearName
        if (vid != null) {
            val original = resolveOriginalUrl(vid)
            if (original != null) {
                if (main != null && main != original) {
                    // 原 main 降为备选
                }
                main = original
                quality = "original"
            }
        }
        main = main?.let { ensureMp4(it) } ?: return null
        val finalBackups = backups.mapNotNull {
            val c = ensureMp4(toHttps(it.replace("playwm", "play")))
            if (c != main) c else null
        }.distinct()

        return MediaParseResult(
            videoUrls = listOf(main),
            imageUrls = emptyList(),
            coverUrl = coverUrl,
            title = title.ifBlank { "抖音短视频 · $awemeId" },
            author = author ?: "抖音作者",
            platform = "抖音",
            qualityOptions = listOf((if (quality == "original") "原画" else quality.ifBlank { "高清" }) to main) +
                finalBackups.take(3).mapIndexed { idx, u -> "备选${idx + 1}" to u }
        )
    }

    /** 从 bit_rate 列表选最高画质 */
    private fun extractBestVideo(video: JSONObject): Triple<String?, List<String>, String> {
        var bitRateList = video.optJSONArray("bitRateList")
        if (bitRateList == null || bitRateList.length() == 0) {
            bitRateList = video.optJSONArray("bit_rate")
        }
        var bestUrl: String? = null
        var gearName = ""
        val backups = mutableListOf<String>()
        if (bitRateList != null && bitRateList.length() > 0) {
            // 按 bitRate 降序
            val items = (0 until bitRateList.length()).mapNotNull { bitRateList.optJSONObject(it) }
                .sortedByDescending { it.optLong("bitRate", it.optLong("bit_rate", 0)) }
            for (item in items) {
                val candidates = collectPlayUrls(item)
                if (candidates.isEmpty()) continue
                // 优先 v3-web
                val v3 = candidates.firstOrNull { "v3-web" in it }
                val v26 = candidates.firstOrNull { "v26-web" in it }
                val best = v3 ?: v26 ?: candidates[0]
                if (bestUrl == null) {
                    bestUrl = best
                    gearName = item.optString("gearName", item.optString("gear_name", ""))
                }
                for (c in candidates) {
                    if (c != bestUrl && c !in backups) backups.add(c)
                }
                if (bestUrl != null && backups.isNotEmpty()) break
            }
        }
        if (bestUrl == null) {
            // 兜底：play_addr.url_list[0]
            val playAddr = video.optJSONObject("play_addr")
            val urlList = playAddr?.optJSONArray("url_list")
            if (urlList != null && urlList.length() > 0) {
                bestUrl = urlList.optString(0)
            }
        }
        return Triple(bestUrl, backups, gearName)
    }

    /** 收集 rateItem 中的播放地址候选 */
    private fun collectPlayUrls(rateItem: JSONObject): List<String> {
        val result = mutableListOf<String>()
        // play_addr.url_list
        val playAddr = rateItem.optJSONObject("play_addr")
        val urlList = playAddr?.optJSONArray("url_list")
        if (urlList != null) {
            for (i in 0 until urlList.length()) {
                urlList.optString(i)?.takeIf { it.startsWith("http") }?.let { result.add(it) }
            }
        }
        // 直接 url_list
        val directList = rateItem.optJSONArray("url_list")
        if (directList != null) {
            for (i in 0 until directList.length()) {
                directList.optString(i)?.takeIf { it.startsWith("http") }?.let { result.add(it) }
            }
        }
        return result.distinct()
    }

    /** 提取 vid（用于原画解析） */
    private fun extractVid(videoInfo: JSONObject?): String? {
        if (videoInfo == null) return null
        // video_id / vid 字段
        videoInfo.optString("vid")?.takeIf { it.isNotBlank() }?.let { return it }
        videoInfo.optString("video_id")?.takeIf { it.isNotBlank() }?.let { return it }
        // 从 play 地址提取 v0xxx
        val playAddr = videoInfo.optJSONObject("play_addr")
        val urlList = playAddr?.optJSONArray("url_list")
        if (urlList != null) {
            for (i in 0 until urlList.length()) {
                val u = urlList.optString(i)
                Regex("video_id=([a-z0-9]+)", RegexOption.IGNORE_CASE).find(u)?.groupValues?.get(1)?.let { return it }
                Regex("/(v[a-z0-9]{20,})/", RegexOption.IGNORE_CASE).find(u)?.groupValues?.get(1)?.let { return it }
            }
        }
        return null
    }

    /** vid → 原画直链（302 跟随） */
    private fun resolveOriginalUrl(vid: String): String? {
        return try {
            val playUrl = "https://aweme.snssdk.com/aweme/v1/play/?video_id=$vid&ratio=1080p&line=0"
            getRedirectLocation(playUrl, UA_DESKTOP_SV)?.let { toHttps(it) }
        } catch (_: Exception) { null }
    }

    /** 图集单图取 URL */
    private fun pickDouyinImageUrl(img: JSONObject): String? {
        // urlList[0] / url_list[0]
        img.optJSONArray("urlList")?.let {
            if (it.length() > 0) {
                val u = it.optString(0)
                if (u.startsWith("http")) return toHttps(u)
            }
        }
        img.optJSONArray("url_list")?.let {
            for (i in 0 until it.length()) {
                val u = it.optString(i)
                if (u.startsWith("http") && "webp" !in u.lowercase()) return toHttps(u)
            }
            if (it.length() > 0) {
                val u = it.optString(0)
                if (u.startsWith("http")) return toHttps(u)
            }
        }
        return null
    }

    /** 实况视频 URL 提取 */
    private fun extractLiveVideoUrl(videoInfo: JSONObject?): String? {
        if (videoInfo == null) return null
        val playAddr = videoInfo.optJSONObject("play_addr")
        val urlList = playAddr?.optJSONArray("url_list")
        if (urlList != null && urlList.length() > 0) {
            val u = urlList.optString(0)
            if (u.startsWith("http")) return u
        }
        return null
    }

    /** 封面提取 */
    private fun pickDouyinCover(detail: JSONObject): String? {
        val video = detail.optJSONObject("video") ?: return null
        val keys = listOf("originCover", "cover", "dynamicCover")
        for (k in keys) {
            val obj = video.optJSONObject(k) ?: continue
            val urlList = obj.optJSONArray("url_list") ?: continue
            for (i in 0 until urlList.length()) {
                val u = urlList.optString(i)
                if (u.startsWith("http") && "webp" !in u.lowercase()) return toHttps(u)
            }
        }
        // detail.cover
        detail.optString("cover")?.takeIf { it.startsWith("http") }?.let { return toHttps(it) }
        return null
    }

    private fun toHttps(u: String): String {
        return if (u.startsWith("http://")) "https://" + u.removePrefix("http://") else u
    }

    private fun ensureMp4(u: String): String {
        return if ("mime_type=" in u) u
        else if ("?" in u) "$u&mime_type=video_mp4"
        else "$u?mime_type=video_mp4"
    }

    // ================= 快手（移植 parse-video-py/kuaishou.py，MIT） =================
    private fun parseKuaishou(shareUrl: String): MediaParseResult? {
        return try {
            val loc = getRedirectLocation(shareUrl) ?: return null
            val fixed = loc.replace("/fw/long-video/", "/fw/photo/")
            val html = getString(fixed, UA, "https://v.kuaishou.com/") ?: return null
            val initJson = Regex("""window\.INIT_STATE\s*=\s*(.*?)</script>""", RegexOption.DOT_MATCHES_ALL)
                .find(html)?.groupValues?.get(1)?.trim() ?: return null
            val root = JSONObject(initJson)
            var photo: JSONObject? = null
            val keys = root.keys()
            while (keys.hasNext()) {
                val item = root.optJSONObject(keys.next()) ?: continue
                if (item.has("result") && item.has("photo")) {
                    if (item.optInt("result", 0) == 1) { photo = item.optJSONObject("photo"); break }
                }
            }
            photo ?: return null
            var videoUrl = ""
            val mvUrls = photo.optJSONArray("mainMvUrls")
            if (mvUrls != null && mvUrls.length() > 0) {
                videoUrl = mvUrls.optJSONObject(0)?.optString("url", "") ?: ""
            }
            val coverList = photo.optJSONArray("coverUrls")
            val coverUrl = if (coverList != null && coverList.length() > 0)
                coverList.optJSONObject(0)?.optString("url")?.takeIf { it.startsWith("http") } else null
            // 图集
            val images = mutableListOf<String>()
            val atlas = photo.optJSONObject("ext_params")?.optJSONObject("atlas")
            val cdnList = atlas?.optJSONArray("cdn")
            val atlasList = atlas?.optJSONArray("list")
            if (cdnList != null && atlasList != null && cdnList.length() > 0) {
                val cdn = cdnList.optString(0)
                for (i in 0 until atlasList.length()) {
                    images.add("https://$cdn/${atlasList.optString(i)}")
                }
            }
            MediaParseResult(
                videoUrls = listOfNotNull(videoUrl.takeIf { it.startsWith("http") }),
                imageUrls = images.distinct().take(20),
                coverUrl = coverUrl,
                title = photo.optString("caption", "").takeIf { it.isNotBlank() },
                author = photo.optString("userName", "").takeIf { it.isNotBlank() },
                platform = "快手"
            )
        } catch (_: Exception) { null }
    }

    // ================= 微博（移植 parse-video-py/weibo.py，MIT） =================
    private fun parseWeibo(shareUrl: String): MediaParseResult? {
        return try {
            val lower = shareUrl.lowercase()
            // 视频页：h5 API
            if ("show?fid=" in lower || "/tv/show/" in lower) {
                val fid = Regex("[?&]fid=([^&]+)").find(shareUrl)?.groupValues?.get(1)
                    ?: shareUrl.trim('/').split("/").last().substringBefore('?')
                if (fid.isBlank()) return null
                val apiUrl = "https://h5.video.weibo.com/api/component?page=/show/$fid"
                val body = "data={\"Component_Play_Playinfo\":{\"oid\":\"$fid\"}}"
                val resp = postForm(
                    apiUrl, body,
                    mapOf(
                        "User-Agent" to UA,
                        "Referer" to "https://h5.video.weibo.com/show/$fid",
                        "Content-Type" to "application/x-www-form-urlencoded"
                    )
                ) ?: return null
                val data = JSONObject(resp).optJSONObject("data")?.optJSONObject("Component_Play_Playinfo")
                    ?: return null
                var videoUrl = data.optString("stream_url", "")
                val urls = data.optJSONObject("urls")
                if (urls != null && urls.length() > 0) {
                    val first = urls.optString(urls.keys().next(), "")
                    if (first.isNotBlank()) videoUrl = "https:$first"
                }
                val cover = "https:" + data.optString("cover_image", "")
                return MediaParseResult(
                    videoUrls = listOfNotNull(videoUrl.takeIf { it.startsWith("http") }),
                    coverUrl = cover.takeIf { it.startsWith("https:") && it.length > 8 },
                    title = data.optString("title", "").takeIf { it.isNotBlank() },
                    author = data.optString("author", "").takeIf { it.isNotBlank() },
                    platform = "微博"
                )
            }
            // 微博正文：m.weibo.cn API
            val postId = shareUrl.trim('/').split("/").last().substringBefore('?')
            if (postId.isBlank()) return null
            val api = "https://m.weibo.cn/statuses/show?id=$postId"
            val req = Request.Builder().url(api)
                .header("User-Agent", UA)
                .header("Referer", "https://m.weibo.cn/")
                .header("X-Requested-With", "XMLHttpRequest")
                .get().build()
            val respStr = try {
                clientFollow.newCall(req).execute().use { r -> if (r.isSuccessful) r.body?.string() else null }
            } catch (_: Exception) { null } ?: return null
            val data = JSONObject(respStr).optJSONObject("data") ?: return null
            val user = data.optJSONObject("user")
            val images = mutableListOf<String>()
            val pics = data.optJSONArray("pics")
            if (pics != null) {
                for (i in 0 until pics.length()) {
                    val pic = pics.optJSONObject(i) ?: continue
                    val url = pic.optJSONObject("large")?.optString("url")
                        ?: pic.optJSONObject("original")?.optString("url")
                        ?: pic.optJSONObject("bmiddle")?.optString("url") ?: ""
                    if (url.startsWith("http")) images.add(url)
                }
            }
            // 正文内视频
            val videos = mutableListOf<String>()
            val pageInfo = data.optJSONObject("page_info")
            val mediaInfo = pageInfo?.optJSONObject("media_info")
            if (mediaInfo != null) {
                listOf("mp4_hd_mp4", "mp4_720p_mp4", "stream_url").forEach { k ->
                    mediaInfo.optString(k, "").takeIf { it.startsWith("http") }?.let { videos.add(it) }
                }
            }
            MediaParseResult(
                videoUrls = videos.distinct().take(5),
                imageUrls = images.distinct().take(20),
                coverUrl = pageInfo?.optJSONObject("page_pic")?.optString("url")?.takeIf { it.startsWith("http") },
                title = stripHtml(data.optString("text", "")).take(60).takeIf { it.isNotBlank() },
                author = user?.optString("screen_name")?.takeIf { it.isNotBlank() },
                platform = "微博"
            )
        } catch (_: Exception) { null }
    }

    private fun stripHtml(s: String): String =
        s.replace(Regex("<[^>]+>"), "").replace(Regex("\\s+"), " ").trim()

    // ================= B站（移植 parse-video-py/bilibili.py，MIT） =================
    private fun parseBilibili(shareUrl: String): MediaParseResult? {
        return try {
            var bvid = Regex("""[Bb][Vv][0-9A-Za-z]{10}""").find(shareUrl)?.value ?: ""
            if (bvid.isBlank() && "b23.tv" in shareUrl.lowercase()) {
                val loc = getRedirectLocation(shareUrl) ?: ""
                bvid = Regex("""[Bb][Vv][0-9A-Za-z]{10}""").find(loc)?.value ?: ""
            }
            if (bvid.isBlank()) return null
            // av 号转 bv
            var bv = bvid
            val avMatch = Regex("""av(\d+)""", RegexOption.IGNORE_CASE).find(shareUrl)
            if (avMatch != null) {
                val aid = avMatch.groupValues[1]
                val j = getJson("https://api.bilibili.com/x/web-interface/view?aid=$aid")
                bv = j?.optJSONObject("data")?.optString("bvid", "") ?: bvid
            }
            val view = getJson("https://api.bilibili.com/x/web-interface/view?bvid=$bv")
            val viewData = view?.optJSONObject("data") ?: return null
            val pages = viewData.optJSONArray("pages") ?: return null
            if (pages.length() == 0) return null
            val cid = pages.optJSONObject(0)?.optLong("cid", 0) ?: 0
            if (cid == 0L) return null
            val playUrl =
                "https://api.bilibili.com/x/player/playurl?otype=json&fnver=0&fnval=0&qn=80&bvid=$bv&cid=$cid&platform=html5"
            val play = getJson(playUrl)
            val durl = play?.optJSONObject("data")?.optJSONArray("durl") ?: return null
            if (durl.length() == 0) return null
            val videoUrl = durl.optJSONObject(0)?.optString("url", "") ?: ""
            val owner = viewData.optJSONObject("owner")
            MediaParseResult(
                videoUrls = listOfNotNull(videoUrl.takeIf { it.startsWith("http") }),
                coverUrl = viewData.optString("pic", "").takeIf { it.startsWith("http") },
                title = viewData.optString("title", "").takeIf { it.isNotBlank() },
                author = owner?.optString("name")?.takeIf { it.isNotBlank() },
                platform = "哔哩哔哩"
            )
        } catch (_: Exception) { null }
    }

    // ================= 西瓜视频（移植 parse-video-py/xigua.py，MIT） =================
    private fun parseXigua(shareUrl: String): MediaParseResult? {
        return try {
            var videoId = shareUrl.trim('/').split("/").last().substringBefore('?')
            if (videoId.isBlank() || "ixigua.com" !in videoId) {
                val loc = getRedirectLocation(shareUrl) ?: ""
                videoId = loc.split("?")[0].trim('/').split("/").last()
            }
            if (videoId.isBlank()) return null
            val reqUrl = "https://m.ixigua.com/douyin/share/video/$videoId" +
                "?aweme_type=107&schema_type=1&utm_source=copy&utm_campaign=client_share&utm_medium=android&app=aweme"
            val html = getString(reqUrl, UA) ?: return null
            val routerJson = Regex("""window\._ROUTER_DATA\s*=\s*(.*?)</script>""", RegexOption.DOT_MATCHES_ALL)
                .find(html)?.groupValues?.get(1)?.trim() ?: return null
            val item = JSONObject(routerJson)
                .optJSONObject("loaderData")
                ?.optJSONObject("video_(id)/page")
                ?.optJSONObject("videoInfoRes")
                ?.optJSONArray("item_list")
                ?.optJSONObject(0) ?: return null
            val video = item.optJSONObject("video") ?: return null
            val urlList = video.optJSONObject("play_addr")?.optJSONArray("url_list") ?: return null
            if (urlList.length() == 0) return null
            val videoUrl = urlList.optString(0).replace("playwm", "play")
            val coverList = video.optJSONObject("cover")?.optJSONArray("url_list")
            val coverUrl = if (coverList != null && coverList.length() > 0)
                coverList.optString(0).takeIf { it.startsWith("http") } else null
            val author = item.optJSONObject("author")
            MediaParseResult(
                videoUrls = listOfNotNull(videoUrl.takeIf { it.startsWith("http") }),
                coverUrl = coverUrl,
                title = item.optString("desc", "").takeIf { it.isNotBlank() },
                author = author?.optString("nickname")?.takeIf { it.isNotBlank() },
                platform = "西瓜视频"
            )
        } catch (_: Exception) { null }
    }

    // ================= 好看视频（移植 parse-video-py/haokan.py，MIT） =================
    private fun parseHaokan(shareUrl: String): MediaParseResult? {
        return try {
            val vid = Regex("[?&]vid=([^&]+)").find(shareUrl)?.groupValues?.get(1) ?: return null
            val j = getJson("https://haokan.baidu.com/v?_format=json&vid=$vid", UA) ?: return null
            if (j.optInt("errno", -1) != 0) return null
            val video = j.optJSONObject("data")?.optJSONObject("apiData")?.optJSONObject("curVideoMeta")
                ?: return null
            val mth = video.optJSONObject("mth")
            MediaParseResult(
                videoUrls = listOfNotNull(video.optString("playurl", "").takeIf { it.startsWith("http") }),
                coverUrl = video.optString("poster", "").takeIf { it.startsWith("http") },
                title = video.optString("title", "").takeIf { it.isNotBlank() },
                author = mth?.optString("author_name")?.takeIf { it.isNotBlank() },
                platform = "好看视频"
            )
        } catch (_: Exception) { null }
    }

    // ================= 小红书（v1.4.13 自研保留） =================

    /**
     * 2026-10-23：小红书图片去水印。
     * 网页版 __INITIAL_STATE__ 给的是 sns-webpic-qc 带 !style 后缀的 H5 图，
     * CDN 会在图上烘焙"小红书"水印（真机实测确认）。
     * 实测：用 fileId 直取移动端 CDN（https://sns-img-qc.xhscdn.com/<fileId>）
     * 即为无水印原图（体积更大、画质更好）。模式不匹配时原样返回。
     */
    private fun dewatermarkXiaohongshuUrl(url: String): String {
        return try {
            if (!url.contains("sns-webpic-")) return url
            val fileId = url.substringAfterLast('/').substringBefore('!').substringBefore('?')
            if (fileId.length < 20 || !fileId.all { it.isLetterOrDigit() }) return url
            "https://sns-img-qc.xhscdn.com/$fileId"
        } catch (_: Exception) { url }
    }

    private fun parseXiaohongshu(shareUrl: String): MediaParseResult? {
        return try {
            val html = getString(shareUrl) ?: return null
            val videos = mutableListOf<String>()
            val images = mutableListOf<String>()
            var desc: String? = null
            val topics = mutableListOf<String>()
            Regex("""window\.__INITIAL_STATE__\s*=\s*(\{.*?\})\s*</script>""", RegexOption.DOT_MATCHES_ALL)
                .find(html)?.groupValues?.get(1)?.let { state ->
                    Regex(""""masterUrl"\s*:\s*"([^"]+)"""").findAll(state).forEach { m ->
                        val v = unescapeUrl(m.groupValues[1])
                        if (v.startsWith("http")) videos.add(v)
                    }
                    Regex(""""url"\s*:\s*"([^"]*?/[^"]*?\.(?:mp4)(?:[^"]*)?)"""").findAll(state).forEach { m ->
                        val v = unescapeUrl(m.groupValues[1])
                        if (v.startsWith("http") && isVideoUrl(v)) videos.add(v)
                    }
                    // 2026-10-23：小红书图片去水印 —— 网页版 imageList 里是 sns-webpic-qc 带 !style
                    // 后缀的 H5 图（CDN 烘焙"小红书"水印），且 URL 以 !h5_1080jpg 结尾无 .jpg，
                    // 旧正则匹配不上。直接取每项的 fileId，拼移动端 CDN 即为无水印原图（真机验过）。
                    Regex(""""fileId"\s*:\s*"([A-Za-z0-9]{20,})"""").findAll(state).forEach { m ->
                        val fid = m.groupValues[1]
                        images.add("https://sns-img-qc.xhscdn.com/$fid")
                    }
                    if (images.isEmpty()) {
                        // 回退：旧正则（兼容 sns-img 等带 .jpg 的老格式）
                        Regex(""""url"\s*:\s*"(https?://[^"]*?(?:sns-img|xhscdn|xiaohongshu)[^"]*?\.(?:jpg|jpeg|png|webp)[^"]*?)"""")
                            .findAll(state).forEach { m ->
                                val v = unescapeUrl(m.groupValues[1])
                                if (isImageUrl(v)) images.add(dewatermarkXiaohongshuUrl(v))
                            }
                    }
                    // v1.7.0：文案（desc）
                    Regex(""""desc"\s*:\s*"((?:[^"\\]|\\.)*)"""").find(state)?.groupValues?.get(1)?.let { d ->
                        val cleanDesc = d.replace("\\n", "\n").replace("\\u002F", "/").replace("\\\"", "\"").trim()
                        if (cleanDesc.isNotBlank()) desc = cleanDesc
                    }
                    // v1.7.0：话题 —— 从 desc 提取 #话题#，再从 tagList 补充
                    desc?.let { d ->
                        Regex("""#([^#\s]{1,30})#""").findAll(d).forEach { tm ->
                            val t = tm.groupValues[1].trim()
                            if (t.isNotBlank() && !topics.contains(t)) topics.add(t)
                        }
                    }
                    Regex(""""tagList"\s*:\s*\[(.*?)\]""", RegexOption.DOT_MATCHES_ALL).find(state)?.groupValues?.get(1)?.let { tl ->
                        Regex(""""name"\s*:\s*"([^"]+)"""").findAll(tl).forEach { tm ->
                            val t = tm.groupValues[1].trim().removePrefix("#").removeSuffix("#")
                            if (t.isNotBlank() && !topics.contains(t)) topics.add(t)
                        }
                    }
                }
            // 标题
            val title = Regex("""(?i)og:title["']\s+content\s*=\s*["']([^"']+)["']""")
                .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.isNotBlank() }
            // v1.6.5：Live 实况图识别 —— 图文 + 视频同时存在时配对
            val livePhotos = mutableListOf<LivePhoto>()
            val distinctVideos = videos.map(::unescapeUrl).distinct().take(5)
            // 2026-10-23：小红书图片去水印（webpic H5 图 → 移动端原图）
            val distinctImages = images.map(::unescapeUrl).distinct().take(20).map(::dewatermarkXiaohongshuUrl)
            if (distinctImages.isNotEmpty() && distinctVideos.isNotEmpty()) {
                distinctImages.forEach { imgUrl ->
                    livePhotos.add(LivePhoto(imageUrl = imgUrl, videoUrl = distinctVideos[0]))
                }
            }
            MediaParseResult(
                videoUrls = distinctVideos,
                imageUrls = distinctImages,
                title = title,
                platform = "小红书",
                livePhotos = livePhotos.take(20),
                description = desc,
                topics = topics.take(10)
            )
        } catch (_: Exception) { null }
    }

    // ================= 微视（通用 JSON 提取） =================
    private fun parseWeishi(shareUrl: String): MediaParseResult? {
        return try {
            val html = getString(shareUrl) ?: return null
            val videos = mutableListOf<String>()
            Regex(""""video_url"\s*:\s*"([^"]+)"""").findAll(html).forEach { m ->
                val v = unescapeUrl(m.groupValues[1])
                if (v.startsWith("http")) videos.add(v)
            }
            if (videos.isEmpty()) return null
            MediaParseResult(videoUrls = videos.distinct().take(5), platform = "微视")
        } catch (_: Exception) { null }
    }

    /**
     * TikTok（海外版抖音）视频解析（v1.4.30 新增）
     * TikTok 官方反爬极严，直接解析网页不可靠，改用免费第三方 API：
     * 1. tikwm.com（主）：https://www.tikwm.com/api/?url=...，返回 play（无水印）/hdplay（高清无水印）
     * 2. slbjs workers（备）：https://tdownv4.sl-bjs.workers.dev/?down=...
     * 两个都是免费、无需 key 的公开 API
     */
    private fun parseTikTok(shareUrl: String): MediaParseResult? {
        return try {
            // 主：tikwm
            var result = parseTikTokViaTikwm(shareUrl)
            // 备：slbjs workers
            if (result == null) {
                result = parseTikTokViaSlbjs(shareUrl)
            }
            result
        } catch (_: Exception) { null }
    }

    private fun parseTikTokViaTikwm(shareUrl: String): MediaParseResult? {
        return try {
            val api = "https://www.tikwm.com/api/?url=" + java.net.URLEncoder.encode(shareUrl, "UTF-8")
            val j = getJson(api) ?: return null
            if (j.optInt("code", -1) != 0) return null
            val data = j.optJSONObject("data") ?: return null
            // play=无水印，hdplay=高清无水印，wmplay=带水印（备用）
            val hdPlay = data.optString("hdplay", "").takeIf { it.startsWith("http") }
            val play = data.optString("play", "").takeIf { it.startsWith("http") }
            val wmPlay = data.optString("wmplay", "").takeIf { it.startsWith("http") }
            val videoUrl = hdPlay ?: play ?: wmPlay ?: return null
            val coverUrl = data.optString("cover", "").takeIf { it.startsWith("http") }
                ?: data.optString("origin_cover", "").takeIf { it.startsWith("http") }
            // v1.4.31：画质选项
            val qualities = mutableListOf<Pair<String, String>>()
            hdPlay?.let { qualities.add("高清" to it) }
            play?.let { qualities.add("标清" to it) }
            wmPlay?.let { if (qualities.isEmpty()) qualities.add("原画" to it) }
            MediaParseResult(
                videoUrls = listOf(videoUrl),
                coverUrl = coverUrl,
                title = data.optString("title", "").takeIf { it.isNotBlank() },
                author = data.optJSONObject("author")?.optString("nickname")?.takeIf { it.isNotBlank() }
                    ?: data.optString("author", "").takeIf { it.isNotBlank() },
                platform = "TikTok",
                qualityOptions = qualities
            )
        } catch (_: Exception) { null }
    }

    private fun parseTikTokViaSlbjs(shareUrl: String): MediaParseResult? {
        return try {
            val api = "https://tdownv4.sl-bjs.workers.dev/?down=" + java.net.URLEncoder.encode(shareUrl, "UTF-8")
            val j = getJson(api) ?: return null
            val downloadUrl = j.optString("download_url", "").takeIf { it.startsWith("http") } ?: return null
            val author = j.optJSONObject("author")
            MediaParseResult(
                videoUrls = listOf(downloadUrl),
                title = j.optString("title", "").takeIf { it.isNotBlank() },
                author = author?.optString("nickname")?.takeIf { it.isNotBlank() }
                    ?: author?.optString("username")?.takeIf { it.isNotBlank() },
                platform = "TikTok"
            )
        } catch (_: Exception) { null }
    }

    /**
     * X (Twitter) 视频解析（v1.4.29 新增）
     * X 视频本身无水印，直接从网页 HTML 提取 video.twimg.com 直链
     * 优先取最高清晰度的 mp4（720p+），过滤 m3u8
     */
    private fun parseX(shareUrl: String): MediaParseResult? {
        return try {
            val html = getString(shareUrl, UA_ANDROID) ?: return null
            val videos = mutableListOf<String>()
            // 提取所有 video.twimg.com 的 mp4 直链
            Regex("""https://video\.twimg\.com/[^"'\\\s]+\.mp4[^"'\\\s]*""").findAll(html).forEach { m ->
                val v = unescapeUrl(m.groupValues[0])
                if (v.startsWith("http")) videos.add(v)
            }
            if (videos.isEmpty()) return null
            // 按分辨率排序，优先高清（720x1280 等）
            val sorted = videos.distinct().sortedByDescending { url ->
                Regex("""(\d+)x(\d+)""").find(url)?.let {
                    it.groupValues[1].toIntOrNull()?.times(it.groupValues[2].toIntOrNull() ?: 0) ?: 0
                } ?: 0
            }
            // 提取标题
            val title = Regex("""<title[^>]*>([^<]+)</title>""").find(html)?.groupValues?.get(1)?.trim()
                ?.replace(" / X", "")?.takeIf { it.isNotBlank() }
            // 提取封面图：og:image 或 pbs.twimg.com 图片
            var coverUrl: String? = null
            Regex("""(?i)(?:property|name)\s*=\s*["']og:image["']\s+content\s*=\s*["']([^"']+)["']""")
                .find(html)?.let { coverUrl = unescapeUrl(it.groupValues[1]).takeIf { u -> u.startsWith("http") } }
            if (coverUrl == null) {
                Regex("""https://pbs\.twimg\.com/[^"'\\\s]+\.(jpg|jpeg|png)[^"'\\\s]*""").find(html)?.let {
                    coverUrl = unescapeUrl(it.groupValues[0]).takeIf { u -> u.startsWith("http") }
                }
            }
            // v1.4.31：画质选项（按分辨率标注）
            val qualities = sorted.take(3).map { url ->
                val res = Regex("""(\d+)x(\d+)""").find(url)
                val label = if (res != null) {
                    val h = res.groupValues[2].toIntOrNull() ?: 0
                    when {
                        h >= 1080 -> "1080P"
                        h >= 720 -> "720P"
                        h >= 480 -> "480P"
                        else -> "标清"
                    }
                } else "原画"
                label to url
            }
            MediaParseResult(
                videoUrls = sorted.take(3),
                platform = "X",
                title = title,
                coverUrl = coverUrl,
                qualityOptions = qualities
            )
        } catch (_: Exception) { null }
    }

    // ================= 皮皮搞笑（移植 qushuiyin/api/pipigx.php，MIT） =================
    // 分享链接 query 取 pid/mid → POST h5.pipigx.com/ppapi/share/fetch_content
    private fun parsePipigx(shareUrl: String): MediaParseResult? {
        return try {
            var url = extractShareUrlSv(shareUrl) ?: return null
            var pid = Regex("[?&]pid=([^&]+)").find(url)?.groupValues?.get(1)
            var mid = Regex("[?&]mid=([^&]+)").find(url)?.groupValues?.get(1)
            if (pid.isNullOrBlank() || mid.isNullOrBlank()) {
                url = getFinalRedirectUrl(url)
                pid = Regex("[?&]pid=([^&]+)").find(url)?.groupValues?.get(1)
                mid = Regex("[?&]mid=([^&]+)").find(url)?.groupValues?.get(1)
            }
            if (pid.isNullOrBlank() || mid.isNullOrBlank()) return null
            val pidLong = pid.toLongOrNull() ?: return null
            val midLong = mid.toLongOrNull() ?: return null
            val apiUrl = "https://h5.pipigx.com/ppapi/share/fetch_content"
            val body = "{\"pid\":$pidLong,\"mid\":$midLong,\"type\":\"post\"}"
            val j = postJson(apiUrl, body, mapOf("Content-Type" to "application/json")) ?: return null
            val post = j.optJSONObject("data")?.optJSONObject("post") ?: return null
            val videos = post.optJSONArray("videos") ?: return null
            if (videos.length() == 0) return null
            val v0 = videos.optJSONObject(0) ?: return null
            val videoUrl = v0.optString("url", "").takeIf { it.startsWith("http") } ?: return null
            val thumb = v0.optString("thumb", "")
            val coverUrl = if (thumb.isNotBlank()) "https://file.ippzone.com/img/frame/id/$thumb" else null
            MediaParseResult(
                videoUrls = listOf(videoUrl),
                coverUrl = coverUrl,
                title = post.optString("content", "").takeIf { it.isNotBlank() },
                platform = "皮皮搞笑"
            )
        } catch (_: Exception) { null }
    }

    // ================= 皮皮虾（移植 qushuiyin/api/ppxia.php，MIT） =================
    // 短链跟随重定向 → /item/{id} → h5.pipix.com/bds/cell/cell_h5_comment
    private fun parsePipixia(shareUrl: String): MediaParseResult? {
        return try {
            var url = extractShareUrlSv(shareUrl) ?: return null
            var finalUrl = getFinalRedirectUrl(url)
            var itemId = Regex("item/([^?/#]+)").find(finalUrl)?.groupValues?.get(1)
            if (itemId.isNullOrBlank()) {
                finalUrl = getFinalRedirectUrl(finalUrl)
                itemId = Regex("item/([^?/#]+)").find(finalUrl)?.groupValues?.get(1)
            }
            if (itemId.isNullOrBlank()) return null
            val apiUrl =
                "https://h5.pipix.com/bds/cell/cell_h5_comment/?count=5&aid=1319&app_name=super&cell_id=$itemId"
            val j = getJson(apiUrl) ?: return null
            // PHP 取 cell_comments[1] 为正文；兼容处理：优先 [1]，否则找第一个含 comment_info.item 的
            val comments = j.optJSONObject("data")?.optJSONArray("cell_comments") ?: return null
            var item: JSONObject? = null
            if (comments.length() > 1) {
                item = comments.optJSONObject(1)?.optJSONObject("comment_info")?.optJSONObject("item")
            }
            if (item == null) {
                for (i in 0 until comments.length()) {
                    item = comments.optJSONObject(i)?.optJSONObject("comment_info")?.optJSONObject("item")
                    if (item != null) break
                }
            }
            item ?: return null
            val videos = mutableListOf<String>()
            item.optJSONObject("video")?.optJSONObject("video_high")
                ?.optJSONArray("url_list")?.optJSONObject(0)?.optString("url", "")
                ?.takeIf { it.startsWith("http") }?.let { videos.add(it) }
            val images = mutableListOf<String>()
            val multiImage = item.optJSONObject("note")?.optJSONArray("multi_image")
            if (multiImage != null) {
                for (i in 0 until multiImage.length()) {
                    multiImage.optJSONObject(i)?.optJSONArray("url_list")?.optJSONObject(0)
                        ?.optString("url", "")?.takeIf { it.startsWith("http") }?.let { images.add(it) }
                }
            }
            if (videos.isEmpty() && images.isEmpty()) return null
            val author = item.optJSONObject("author")
            MediaParseResult(
                videoUrls = videos,
                imageUrls = images.distinct().take(20),
                coverUrl = item.optJSONObject("cover")?.optJSONArray("url_list")?.optJSONObject(0)
                    ?.optString("url", "")?.takeIf { it.startsWith("http") },
                title = item.optString("content", "").takeIf { it.isNotBlank() },
                author = author?.optString("name")?.takeIf { it.isNotBlank() },
                platform = "皮皮虾"
            )
        } catch (_: Exception) { null }
    }

    // ================= 最右（移植 qushuiyin/api/zuiyou.php，MIT） =================
    // 分享链接取 pid → POST share.xiaochuankeji.cn/planck/share/post/detail_h5
    private fun parseZuiyou(shareUrl: String): MediaParseResult? {
        return try {
            var url = extractShareUrlSv(shareUrl) ?: return null
            var pid = Regex("[?&]pid=([^&]+)").find(url)?.groupValues?.get(1)
            if (pid.isNullOrBlank()) {
                url = getFinalRedirectUrl(url)
                pid = Regex("[?&]pid=([^&]+)").find(url)?.groupValues?.get(1)
            }
            if (pid.isNullOrBlank()) return null
            val pidLong = pid.toLongOrNull() ?: return null
            val apiUrl = "https://share.xiaochuankeji.cn/planck/share/post/detail_h5"
            val body = "{\"pid\":$pidLong,\"h_av\":\"5.2.13.011\"}"
            val j = postJson(
                apiUrl, body,
                mapOf("Content-Type" to "application/json", "Accept" to "application/json")
            ) ?: return null
            val post = j.optJSONObject("data")?.optJSONObject("post") ?: return null
            val member = post.optJSONObject("member")
            val imgs = post.optJSONArray("imgs")
            val img0 = if (imgs != null && imgs.length() > 0) imgs.optJSONObject(0) else null
            val vid = img0?.optString("id", "") ?: ""
            var videoUrl: String? = null
            if (vid.isNotBlank()) {
                videoUrl = post.optJSONObject("videos")?.optJSONObject(vid)?.optString("url", "")
                    ?.takeIf { it.startsWith("http") }
            }
            // 兜底：videos 对象里第一个带 http 直链的
            if (videoUrl == null) {
                val videosObj = post.optJSONObject("videos")
                if (videosObj != null) {
                    val keys = videosObj.keys()
                    while (keys.hasNext()) {
                        val u = videosObj.optJSONObject(keys.next())?.optString("url", "") ?: ""
                        if (u.startsWith("http")) {
                            videoUrl = u
                            break
                        }
                    }
                }
            }
            if (videoUrl == null) return null
            val coverUrl = img0?.optJSONObject("urls")?.optJSONObject("540_webp")
                ?.optJSONArray("urls")?.optString(0)?.takeIf { it.startsWith("http") }
            MediaParseResult(
                videoUrls = listOf(videoUrl),
                coverUrl = coverUrl,
                title = post.optString("content", "").takeIf { it.isNotBlank() },
                author = member?.optString("name")?.takeIf { it.isNotBlank() },
                platform = "最右"
            )
        } catch (_: Exception) { null }
    }

    // ================= 豆包视频（移植 qushuiyin/api/doubao/video/DoubaoParser.php，MIT） =================
    // 分享链接取 share_id/video_id → POST www.doubao.com/creativity/share/get_video_share_info
    // 注意：data 为透传结构，防御式提取；Cookie 为参考实现自带，可能过期
    private fun parseDoubao(shareUrl: String): MediaParseResult? {
        return try {
            var url = extractShareUrlSv(shareUrl) ?: return null
            var shareId = Regex("[?&]share_id=([^&]+)").find(url)?.groupValues?.get(1)
            var videoId = Regex("[?&]video_id=([^&]+)").find(url)?.groupValues?.get(1)
            if (shareId.isNullOrBlank() || videoId.isNullOrBlank()) {
                url = getFinalRedirectUrl(url)
                shareId = Regex("[?&]share_id=([^&]+)").find(url)?.groupValues?.get(1)
                videoId = Regex("[?&]video_id=([^&]+)").find(url)?.groupValues?.get(1)
            }
            if (shareId.isNullOrBlank() || videoId.isNullOrBlank()) return null
            val apiUrl = "https://www.doubao.com/creativity/share/get_video_share_info" +
                "?version_code=20800&language=zh-CN&device_platform=web&aid=497858&real_aid=497858" +
                "&pkg_type=release_version&device_id=&pc_version=3.8.3&region=&sys_region=" +
                "&samantha_web=1&use-olympus-account=1&web_tab_id=4d5d17a6-6729-4c1e-9f55-09f093100f0a"
            val body = "{\"share_id\":\"$shareId\",\"vid\":\"$videoId\",\"creation_id\":\"\"}"
            val headers = mapOf(
                "Content-Type" to "application/json",
                "Accept" to "application/json, text/plain, */*",
                "Origin" to "https://www.doubao.com",
                "Referer" to "https://www.doubao.com/video-sharing?share_id=$shareId&source_type=mobile&video_id=$videoId&share_scene=video_viewer",
                "Cookie" to "i18next=zh-CN; ttwid=1%7CFGYNcsGXqieXVBP4QNemEXTZLZoArf0NEPXt1WhSmm8%7C1772615343%7Cc6580d4acaa52df7479b610305cc558176ba8dfcdd55bf8d9ce6fe86d3948009"
            )
            val j = postJson(apiUrl, body, headers, UA_DESKTOP_SV) ?: return null
            if (j.optInt("code", -1) != 0) return null
            val data = j.optJSONObject("data") ?: return null
            val videoUrl = findBestVideoUrlInJson(data)?.let(::dewatermarkDoubaoUrl) ?: return null
            MediaParseResult(
                videoUrls = listOf(videoUrl),
                coverUrl = data.optString("cover_url", "").takeIf { it.startsWith("http") }
                    ?: data.optString("cover", "").takeIf { it.startsWith("http") },
                title = data.optString("title", "").takeIf { it.isNotBlank() }
                    ?: data.optString("desc", "").takeIf { it.isNotBlank() },
                platform = "豆包"
            )
        } catch (_: Exception) { null }
    }

    /**
     * 2026-10-23：豆包视频去水印（参考 MIT 项目 ihmily/doubao-nomark）。
     * get_video_share_info 返回的 play_info.main/backup 带 lr=video_gen_watermark_dyn（动态水印），
     * 实测去掉 lr 参数后签名依然有效且可下载，download=true 也一并去掉以便直接播放。
     */
    private fun dewatermarkDoubaoUrl(url: String): String {
        return try {
            val uri = android.net.Uri.parse(url)
            if (uri.getQueryParameter("lr") == null) return url
            val b = uri.buildUpon().clearQuery()
            uri.queryParameterNames.forEach { k ->
                if (k != "lr" && k != "download") {
                    uri.getQueryParameters(k).forEach { v -> b.appendQueryParameter(k, v) }
                }
            }
            b.build().toString()
        } catch (_: Exception) { url }
    }

    // ================= 小云雀（移植 MIT 项目 ihmily/doubao-nomark 的 yunque_video_parse） =================
    // 分享链接跟随重定向取 share_id/share_sec_did/share_sec_uid
    // → POST xiaoyunque.jianying.com/luckycat/cn/jianying/campaign/v1/pippit/share/landing_page
    private fun parseYunque(shareUrl: String): MediaParseResult? {
        return try {
            val finalUrl = getFinalRedirectUrl(shareUrl)
            val uri = android.net.Uri.parse(finalUrl)
            val shareId = uri.getQueryParameter("share_id")
            val secDid = uri.getQueryParameter("share_sec_did")
            val secUid = uri.getQueryParameter("share_sec_uid")
            if (shareId.isNullOrBlank() || secDid.isNullOrBlank() || secUid.isNullOrBlank()) return null
            val apiUrl = "https://xiaoyunque.jianying.com/luckycat/cn/jianying/campaign/v1/pippit/share/landing_page"
            val body = JSONObject().apply {
                put("query_params", JSONObject().apply {
                    put("content_type", "video")
                    put("home_input_type", "VIDEO_PART")
                    put("scene", "agent_tool")
                    put("share_campaign_key", "pippit_invite_fission")
                    put("share_id", shareId)
                    put("share_sec_did", secDid)
                    put("share_sec_uid", secUid)
                })
            }.toString()
            val headers = mapOf(
                "Content-Type" to "application/json",
                "Origin" to "https://xiaoyunque.jianying.com"
            )
            val j = postJson(apiUrl, body, headers, UA_DESKTOP_SV) ?: return null
            val data = j.optJSONObject("data") ?: return null
            val pageInfo = data.optJSONObject("page_info") ?: return null
            val videoInfo = pageInfo.optJSONObject("generate_page")
                ?.optJSONObject("item_info")
                ?.optJSONArray("video_info")
                ?.optJSONObject(0) ?: return null
            val videoUrl = videoInfo.optString("video_url", "").takeIf { it.startsWith("http") } ?: return null
            MediaParseResult(
                videoUrls = listOf(videoUrl),
                coverUrl = videoInfo.optString("cover_url", "").takeIf { it.startsWith("http") },
                platform = "小云雀"
            )
        } catch (_: Exception) { null }
    }

    /** 在未知结构的 JSON 中递归找最像视频直链的 URL（豆包 data 透传结构用） */
    private fun findBestVideoUrlInJson(obj: JSONObject): String? {
        var best: String? = null
        var bestScore = -1
        fun score(key: String, value: String): Int {
            val k = key.lowercase()
            if ("avatar" in k || "cover" in k || "image" in k || "thumb" in k || "icon" in k || "logo" in k) return -1
            var s = 0
            if (value.endsWith(".mp4") || ".mp4?" in value) s += 10
            if ("video" in k) s += 5
            if ("play" in k || "url" in k) s += 3
            if ("video" in value) s += 2
            return s
        }
        fun walk(o: Any?, key: String) {
            when (o) {
                is JSONObject -> {
                    val keys = o.keys()
                    while (keys.hasNext()) {
                        val k = keys.next()
                        walk(o.opt(k), k)
                    }
                }
                is org.json.JSONArray -> {
                    for (i in 0 until o.length()) walk(o.opt(i), key)
                }
                is String -> {
                    if (o.startsWith("http")) {
                        val s = score(key, o)
                        if (s > bestScore) {
                            bestScore = s
                            best = o
                        }
                    }
                }
            }
        }
        walk(obj, "")
        return best?.takeIf { bestScore > 0 }
    }

    // ================= 即梦AI（移植 qushuiyin/api/jimengai/JimengParser.php，MIT） =================
    // 分享链接取 item_id（query 参数或末段数字；短链 /s/ 先跟随重定向）
    // → POST jimeng.jianying.com/mweb/v1/get_item_info
    private fun parseJimeng(shareUrl: String): MediaParseResult? {
        return try {
            var url = extractShareUrlSv(shareUrl) ?: return null
            var itemId = extractJimengId(url)
            if (itemId.isNullOrBlank() && "/s/" in url) {
                url = getFinalRedirectUrl(url)
                itemId = extractJimengId(url)
            }
            if (itemId.isNullOrBlank()) return null
            val apiUrl = "https://jimeng.jianying.com/mweb/v1/get_item_info"
            val body = "{\"published_item_id\":\"$itemId\"}"
            val j = postJson(apiUrl, body, mapOf("Content-Type" to "application/json"), UA_DESKTOP_SV)
                ?: return null
            if (j.optString("ret", "") != "0") return null
            val data = j.optJSONObject("data") ?: return null
            val common = data.optJSONObject("common_attr")
            val author = data.optJSONObject("author")
            val video = data.optJSONObject("video")
            // 视频：transcoded_video.origin.video_url 优先，其次 origin_video.video_url
            val transcoded = video?.optJSONObject("transcoded_video")
            var videoUrl = transcoded?.optJSONObject("origin")?.optString("video_url", "")
                ?.takeIf { it.startsWith("http") }
            if (videoUrl == null) {
                videoUrl = video?.optJSONObject("origin_video")?.optString("video_url", "")
                    ?.takeIf { it.startsWith("http") }
            }
            if (videoUrl == null) return null
            // 备选画质
            val qualities = mutableListOf<Pair<String, String>>("原画" to videoUrl)
            if (transcoded != null) {
                val keys = transcoded.keys()
                while (keys.hasNext()) {
                    val q = keys.next()
                    if (q == "origin") continue
                    val u = transcoded.optJSONObject(q)?.optString("video_url", "") ?: ""
                    if (u.startsWith("http") && u != videoUrl) qualities.add(q to u)
                }
            }
            // 封面：cover_url_map 1080/720/original/首个
            var coverUrl: String? = null
            val coverMap = common?.optJSONObject("cover_url_map")
            if (coverMap != null) {
                for (k in listOf("1080", "720", "original")) {
                    val u = coverMap.optString(k, "")
                    if (u.startsWith("http")) {
                        coverUrl = u
                        break
                    }
                }
                if (coverUrl == null) {
                    val keys = coverMap.keys()
                    while (keys.hasNext()) {
                        val u = coverMap.optString(keys.next(), "")
                        if (u.startsWith("http")) {
                            coverUrl = u
                            break
                        }
                    }
                }
            }
            if (coverUrl == null) {
                coverUrl = common?.optString("cover_url", "")?.takeIf { it.startsWith("http") }
            }
            MediaParseResult(
                videoUrls = listOf(videoUrl),
                coverUrl = coverUrl,
                title = common?.optString("description", "")?.takeIf { it.isNotBlank() },
                author = author?.optString("name")?.takeIf { it.isNotBlank() },
                platform = "即梦AI",
                qualityOptions = qualities.distinctBy { it.second }.take(4)
            )
        } catch (_: Exception) { null }
    }

    /** 即梦 item_id：query 的 item_id/id，或末段纯数字 */
    private fun extractJimengId(url: String): String? {
        return try {
            Regex("[?&](?:item_id|id)=([^&]+)").find(url)?.groupValues?.get(1)?.takeIf { it.isNotBlank() }
                ?: Regex("/(\\d+)(?:[?#]|$)").find(url)?.groupValues?.get(1)
        } catch (_: Exception) { null }
    }

    // ================= 通用回退（v1.4.13 逻辑保留） =================
    private fun extractMedia(html: String, pageUrl: String): MediaParseResult {
        if (html.isBlank()) return MediaParseResult()
        val videos = mutableListOf<String>()
        val images = mutableListOf<String>()

        Regex("""(?i)(?:property|name)\s*=\s*["']og:video(?::url|:secure_url)?["']\s+content\s*=\s*["']([^"']+)["']""")
            .findAll(html).forEach { m ->
                val v = m.groupValues[1].trim()
                if (isVideoUrl(v)) videos.add(v)
            }
        // twitter:player:stream
        Regex("""(?i)(?:property|name)\s*=\s*["']twitter:player:stream["']\s+content\s*=\s*["']([^"']+)["']""")
            .findAll(html).forEach { m ->
                val v = m.groupValues[1].trim()
                if (isVideoUrl(v)) videos.add(v)
            }
        Regex("""(?i)["'](?:playAddr|play_addr|playUrl|videoUrl|video_url)["']\s*:\s*["']([^"']+)["']""")
            .findAll(html).forEach { m ->
                val v = unescapeUrl(m.groupValues[1].trim().trim('"'))
                if (v.startsWith("http") && (isVideoUrl(v) || v.contains("play"))) videos.add(v)
            }
        Regex(
            """https?://[^\s"'<>\\]+?\.(?:mp4|m3u8|webm|mov)(?:\?[^\s"'<>\\]*)?""",
            RegexOption.IGNORE_CASE
        ).findAll(html).forEach { m ->
            val v = m.value.trim()
            if (isVideoUrl(v)) videos.add(v)
        }

        // HTML5 <video src="..."> 与 <source src="..."> 提取
        Regex("""(?i)<video[^>]+src\s*=\s*["']([^"']+)["']""").findAll(html).forEach { m ->
            val v = unescapeUrl(m.groupValues[1].trim())
            if (v.startsWith("http") && isVideoUrl(v)) videos.add(v)
        }
        Regex("""(?i)<source[^>]+src\s*=\s*["']([^"']+)["']""").findAll(html).forEach { m ->
            val v = unescapeUrl(m.groupValues[1].trim())
            if (v.startsWith("http") && isVideoUrl(v)) videos.add(v)
        }

        val cover = Regex("""(?i)(?:property|name)\s*=\s*["'](?:og:image|twitter:image)(?::url|:secure_url)?["']\s+content\s*=\s*["']([^"']+)["']""")
            .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.startsWith("http") }
        val title = Regex("""(?i)(?:property|name)\s*=\s*["']og:title["']\s+content\s*=\s*["']([^"']+)["']""")
            .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.isNotBlank() }
        Regex("""(?i)(?:property|name)\s*=\s*["']og:image(?::url|:secure_url)?["']\s+content\s*=\s*["']([^"']+)["']""")
            .findAll(html).forEach { m ->
                val v = m.groupValues[1].trim()
                if (isImageUrl(v)) images.add(v)
            }
        // v1.6.5：JSON-LD 里的 image 字段
        Regex(""""image"\s*:\s*"([^"]+?\.(?:jpg|jpeg|png|webp|gif)(?:\?[^"]*)?)"""", RegexOption.IGNORE_CASE)
            .findAll(html).forEach { m ->
                val v = unescapeUrl(m.groupValues[1].trim())
                if (isImageUrl(v)) images.add(v)
            }
        // v1.6.5：<img> 标签的大图（过滤小图标/avatar）
        Regex("""<img[^>]+src\s*=\s*["']([^"']+)["']""", RegexOption.IGNORE_CASE)
            .findAll(html).forEach { m ->
                val v = unescapeUrl(m.groupValues[1].trim())
                if (isImageUrl(v) && !v.contains("avatar", true) && !v.contains("icon", true)
                    && !v.contains("logo", true) && !v.contains("favicon", true)) images.add(v)
            }

        // v1.6.5：Live 实况图 —— 通用页同时有图和视频时配对
        val distinctVideos = videos.map(::unescapeUrl).filter { it.isNotBlank() }.distinct().take(5)
        val distinctImages = images.map(::unescapeUrl).filter { it.isNotBlank() }.distinct().take(20)
        val livePhotos = mutableListOf<LivePhoto>()
        if (distinctImages.isNotEmpty() && distinctVideos.isNotEmpty()) {
            distinctImages.forEach { imgUrl ->
                livePhotos.add(LivePhoto(imageUrl = imgUrl, videoUrl = distinctVideos[0]))
            }
        }

        // v1.7.0：通用 og:description 文案
        val description = Regex("""(?i)(?:property|name)\s*=\s*["']og:description["']\s+content\s*=\s*["']([^"']+)["']""")
            .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.isNotBlank() }

        // 若来自明确的短视频平台且未解析出视频，避免将网页背景图/图标作为图片兜底误导用户
        val isVideoPlatform = listOf("douyin", "kuaishou", "bilibili", "ixigua", "haokan", "pipix", "tiktok", "weishi",
            "pipigx", "ippzone", "izuiyou", "doubao", "jimeng")
            .any { pageUrl.contains(it, ignoreCase = true) }
        val filteredImages = if (isVideoPlatform && distinctVideos.isEmpty()) emptyList() else distinctImages

        return MediaParseResult(
            videoUrls = distinctVideos,
            imageUrls = filteredImages,
            coverUrl = cover,
            title = title,
            livePhotos = livePhotos.take(20),
            description = description
        )
    }

    private fun isVideoUrl(url: String): Boolean {
        if (!url.startsWith("http")) return false
        if (url.contains("html") && !url.contains("video")) return false
        val clean = url.substringBefore('?').lowercase()
        return clean.endsWith(".mp4") ||
            clean.endsWith(".m3u8") ||
            clean.endsWith(".webm") ||
            clean.endsWith(".mov") ||
            clean.endsWith(".flv") ||
            url.contains("playurl", ignoreCase = true) ||
            url.contains("play_addr", ignoreCase = true) ||
            url.contains("video", ignoreCase = true) ||
            url.contains(".mp4?", ignoreCase = true) ||
            url.contains("/video/", ignoreCase = true) ||
            url.contains("/mp4/", ignoreCase = true) ||
            url.contains("snssdk.com/aweme/v1/play", ignoreCase = true)
    }

    private fun isImageUrl(url: String): Boolean {
        if (!url.startsWith("http")) return false
        val clean = url.substringBefore('?').lowercase()
        return clean.endsWith(".jpg") || clean.endsWith(".jpeg") ||
            clean.endsWith(".png") || clean.endsWith(".webp") ||
            url.contains("sns-img") || url.contains("xhscdn")
    }
}
