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
                    "pipixia.com" in lower -> parseDouyin(url) // 皮皮虾与抖音同源 _ROUTER_DATA
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

    private fun unescapeUrl(u: String): String =
        u.replace("\\u002F", "/").replace("\\/", "/").replace("\\u0026", "&").trim()

    private fun pickNonWebp(urls: List<String>): String =
        urls.firstOrNull { it.isNotBlank() && !it.lowercase().endsWith(".webp") }
            ?: urls.firstOrNull { it.isNotBlank() } ?: ""

    // ================= 抖音（深度解析 + 自动分类：视频直链 vs 图集原图） =================
    private fun parseDouyin(shareUrl: String): MediaParseResult? {
        return try {
            val finalUrl = getFinalRedirectUrl(shareUrl)
            var videoId = extractDouyinId(finalUrl).ifBlank { extractDouyinId(shareUrl) }
            val isNote = "/note/" in finalUrl || "/note/" in shareUrl
            if (videoId.isBlank()) return null

            var title: String? = null
            var author: String? = null
            var coverUrl: String? = null
            var videoUrl = ""
            val images = mutableListOf<String>()

            // 1. 尝试 slidesinfo / iteminfo 官方接口
            for (suffix in listOf("", "&request_source=200")) {
                val api = "https://www.iesdouyin.com/web/api/v2/aweme/slidesinfo/?aweme_ids=%5B$videoId%5D$suffix"
                val j = getJson(api, UA, "https://www.iesdouyin.com/share/video/$videoId/")
                val details = j?.optJSONArray("aweme_details")
                if (details != null && details.length() > 0) {
                    val item = details.getJSONObject(0)
                    title = item.optString("desc", "").takeIf { it.isNotBlank() }
                    author = item.optJSONObject("author")?.optString("nickname")?.takeIf { it.isNotBlank() }
                    val video = item.optJSONObject("video")
                    val playAddr = video?.optJSONObject("play_addr")
                    val urlList = playAddr?.optJSONArray("url_list")
                    if (urlList != null && urlList.length() > 0) {
                        val noWm = urlList.optString(0).replace("playwm", "play")
                        videoUrl = getRedirectLocation(noWm, UA) ?: noWm
                    }
                    val coverList = video?.optJSONObject("cover")?.optJSONArray("url_list")
                    if (coverList != null) {
                        val urls = (0 until coverList.length()).map { coverList.optString(it) }
                        coverUrl = pickNonWebp(urls).takeIf { it.startsWith("http") }
                    }
                    val imgArr = item.optJSONArray("images")
                    if (imgArr != null) {
                        for (i in 0 until imgArr.length()) {
                            val uList = imgArr.optJSONObject(i)?.optJSONArray("url_list") ?: continue
                            val urls = (0 until uList.length()).map { uList.optString(it) }
                            pickNonWebp(urls).takeIf { it.startsWith("http") }?.let { images.add(it) }
                        }
                    }
                    break
                }
            }

            // 2. 网页 HTML 抽取（针对 _ROUTER_DATA / _SSR_DATA / 元数据）
            val sharePath = if (isNote) "note" else "video"
            val html = getString("https://www.iesdouyin.com/share/$sharePath/$videoId/", UA)
            if (html != null) {
                if (title == null) {
                    title = Regex("""(?i)(?:property|name)\s*=\s*["']og:title["']\s+content\s*=\s*["']([^"']+)["']""")
                        .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.isNotBlank() }
                }
                if (coverUrl == null) {
                    coverUrl = Regex("""(?i)(?:property|name)\s*=\s*["']og:image(?::url|:secure_url)?["']\s+content\s*=\s*["']([^"']+)["']""")
                        .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.startsWith("http") }
                }
                if (videoUrl.isBlank()) {
                    // 匹配 HTML 中的真实 play 视频直链
                    Regex("""https?://[^\s"'<>\\]+?(?:playwm|play_addr|video/tos/|snssdk\.com/aweme/v1/play)[^\s"'<>\\]*""")
                        .find(html)?.value?.let { v ->
                            videoUrl = unescapeUrl(v).replace("playwm", "play")
                        }
                }
            }

            // 3. 视频直链保底：抖音官方标准无水印视频直链
            if (!isNote && videoUrl.isBlank()) {
                videoUrl = "https://aweme.snssdk.com/aweme/v1/play/?video_id=$videoId&ratio=1080p&line=0"
            }

            // 4. 严格自动分类：如果是视频，绝不能当成图集保存
            if (!isNote && videoUrl.isNotBlank()) {
                return MediaParseResult(
                    videoUrls = listOf(videoUrl),
                    imageUrls = emptyList(), // 视频模式下清空图片，防止被误识别为图集保存
                    coverUrl = coverUrl ?: "https://p3-sign.douyinpic.com/tos-cn-p-0015/$videoId",
                    title = title ?: "抖音短视频 · $videoId",
                    author = author ?: "抖音作者",
                    platform = "抖音",
                    qualityOptions = listOf("原画高清" to videoUrl)
                )
            } else if (isNote && images.isNotEmpty()) {
                // 图集模式（/note/ 真实多图）
                return MediaParseResult(
                    videoUrls = emptyList(),
                    imageUrls = images.distinct().take(20),
                    coverUrl = coverUrl ?: images.firstOrNull(),
                    title = title ?: "抖音精选图集 · $videoId",
                    author = author ?: "抖音作者",
                    platform = "抖音"
                )
            }

            if (videoUrl.isNotBlank()) {
                MediaParseResult(
                    videoUrls = listOf(videoUrl),
                    imageUrls = emptyList(),
                    coverUrl = coverUrl,
                    title = title ?: "抖音短视频 · $videoId",
                    author = author ?: "抖音作者",
                    platform = "抖音",
                    qualityOptions = listOf("原画高清" to videoUrl)
                )
            } else null
        } catch (_: Exception) { null }
    }

    private fun extractDouyinId(url: String): String {
        return try {
            val u = java.net.URL(url)
            val query = u.query ?: ""
            // modal_id 参数（精选页）
            Regex("[?&]modal_id=(\\d+)").find("?$query")?.groupValues?.get(1)?.let { return it }
            val path = u.path.trim('/').trim()
            if (path.isEmpty()) return ""
            // /video/{id} / /note/{id} / /share/video/{id}
            val last = path.split("/").last()
            if (last.matches(Regex("\\d{10,25}"))) return last
            ""
        } catch (_: Exception) { "" }
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
                    Regex(""""url"\s*:\s*"(https?://[^"]*?(?:sns-img|xhscdn|xiaohongshu)[^"]*?\.(?:jpg|jpeg|png|webp)[^"]*?)"""")
                        .findAll(state).forEach { m ->
                            val v = unescapeUrl(m.groupValues[1])
                            if (isImageUrl(v)) images.add(v)
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
            val distinctImages = images.map(::unescapeUrl).distinct().take(20)
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
        val isVideoPlatform = listOf("douyin", "kuaishou", "bilibili", "ixigua", "haokan", "pipixia", "tiktok", "weishi")
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
