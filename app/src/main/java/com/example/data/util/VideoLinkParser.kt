package com.example.data.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * 分享链接 → 媒体直链解析器（v1.4.13 去水印模块重构）
 *
 * v1.4.13 修复/新增：
 * 1. 修复「解析成功但无预览图」：封面不再写死 Unsplash，优先取 og:image / twitter:image，
 *    取不到则返回 null 由 UI 显示本地占位。
 * 2. 修复「下载下来的是 HTML 不是 mp4」：resolve() 解析不到真实直链时返回空，
 *    UI 不再把分享页链接伪装成视频直链（之前兜底 listOf(target) 导致下载到 HTML 页面）。
 * 3. 新增小红书 window.__INITIAL_STATE__ 提取：视频 masterUrl、图文 imageList。
 * 4. 新增图片直链提取（小红书图文/各平台 og:image 组图），供「一键下载图片存相册」。
 */
data class MediaParseResult(
    val videoUrls: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val coverUrl: String? = null,
    val title: String? = null
)

object VideoLinkParser {

    private const val UA =
        "Mozilla/5.0 (Linux; Android 14; 23127PN0CC) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(25, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * 解析分享链接，返回视频/图片直链 + 封面 + 标题。
     * 什么都没解析到时返回空结果（videoUrls/imageUrls 均为空），调用方应提示解析失败，
     * 禁止把原始分享链接当作可下载直链。
     */
    suspend fun resolveMedia(context: Context, shareUrl: String): MediaParseResult =
        withContext(Dispatchers.IO) {
            val url = shareUrl.trim()
            if (url.isBlank()) return@withContext MediaParseResult()
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", UA)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .header("Referer", url)
                    .get()
                    .build()
                client.newCall(request).execute().use { resp ->
                    if (!resp.isSuccessful) return@withContext MediaParseResult()
                    val finalUrl = resp.request.url.toString()
                    val html = resp.body?.string() ?: return@withContext MediaParseResult()
                    // 落地页本身是直链（极少数情况）
                    val ct = resp.header("Content-Type", "").lowercase()
                    if (ct.contains("video/") || finalUrl.substringBefore('?').lowercase().endsWith(".mp4")) {
                        return@withContext MediaParseResult(videoUrls = listOf(finalUrl))
                    }
                    return@withContext extractMedia(html, finalUrl)
                }
            } catch (_: Exception) {
                MediaParseResult()
            }
        }

    /** 兼容旧接口：只取视频直链 */
    suspend fun resolve(context: Context, shareUrl: String): List<String> =
        resolveMedia(context, shareUrl).videoUrls

    /** 从 HTML 提取视频/图片/封面/标题 */
    private fun extractMedia(html: String, pageUrl: String): MediaParseResult {
        if (html.isBlank()) return MediaParseResult()
        val videos = mutableListOf<String>()
        val images = mutableListOf<String>()

        // ---- 0. 小红书 __INITIAL_STATE__（视频 masterUrl / 图文原图） ----
        Regex("""window\.__INITIAL_STATE__\s*=\s*(\{.*?\})\s*</script>""", RegexOption.DOT_MATCHES_ALL)
            .find(html)?.groupValues?.get(1)?.let { state ->
                // 视频直链
                Regex(""""masterUrl"\s*:\s*"([^"]+)"""").findAll(state).forEach { m ->
                    val v = unescapeUrl(m.groupValues[1])
                    if (v.startsWith("http")) videos.add(v)
                }
                Regex(""""url"\s*:\s*"([^"]*?/[^"]*?\.(?:mp4)(?:[^"]*)?)"""").findAll(state).forEach { m ->
                    val v = unescapeUrl(m.groupValues[1])
                    if (v.startsWith("http") && isVideoUrl(v)) videos.add(v)
                }
                // 图文原图（sns-img / xhscdn）
                Regex(""""url"\s*:\s*"(https?://[^"]*?(?:sns-img|xhscdn|xiaohongshu)[^"]*?\.(?:jpg|jpeg|png|webp)[^"]*?)"""")
                    .findAll(state).forEach { m ->
                        val v = unescapeUrl(m.groupValues[1])
                        if (isImageUrl(v)) images.add(v)
                    }
            }

        // ---- 1. og:video 社交标签 ----
        Regex("""(?i)(?:property|name)\s*=\s*["']og:video(?::url|:secure_url)?["']\s+content\s*=\s*["']([^"']+)["']""")
            .findAll(html).forEach { m ->
                val v = m.groupValues[1].trim()
                if (isVideoUrl(v)) videos.add(v)
            }

        // ---- 2. 页面 JSON 内嵌视频字段 ----
        Regex("""(?i)["'](?:playAddr|play_addr|playAddrV2|playUrl|downloadAddr|download_addr|videoUrl|video_url|url_list)["']\s*:\s*["']([^"']+)["']""")
            .findAll(html).forEach { m ->
                val v = m.groupValues[1].trim()
                if (v.contains("http")) {
                    val clean = unescapeUrl(v.trimStart('"', '[').trimEnd('"', ']', ',').trim())
                    if (isVideoUrl(clean) || clean.contains("http")) videos.add(clean.ifBlank { v })
                }
            }

        // ---- 3. 通用视频直链 ----
        Regex(
            """https?://[^\s"'<>\\]+?\.(?:mp4|m3u8|webm|mov)(?:\?[^\s"'<>\\]*)?""",
            RegexOption.IGNORE_CASE
        ).findAll(html).forEach { m ->
            val v = m.value.trim()
            if (isVideoUrl(v)) videos.add(v)
        }

        // ---- 4. 封面：og:image / twitter:image ----
        val cover = Regex("""(?i)(?:property|name)\s*=\s*["'](?:og:image|twitter:image)(?::url|:secure_url)?["']\s+content\s*=\s*["']([^"']+)["']""")
            .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.startsWith("http") }

        // ---- 5. 标题：og:title ----
        val title = Regex("""(?i)(?:property|name)\s*=\s*["']og:title["']\s+content\s*=\s*["']([^"']+)["']""")
            .find(html)?.groupValues?.get(1)?.trim()?.takeIf { it.isNotBlank() }

        // ---- 6. 图片：og:image 全组（部分平台有多张） ----
        Regex("""(?i)(?:property|name)\s*=\s*["']og:image(?::url|:secure_url)?["']\s+content\s*=\s*["']([^"']+)["']""")
            .findAll(html).forEach { m ->
                val v = m.groupValues[1].trim()
                if (isImageUrl(v)) images.add(v)
            }

        return MediaParseResult(
            videoUrls = videos.map(::unescapeUrl).filter { it.isNotBlank() }.distinct().take(5),
            imageUrls = images.map(::unescapeUrl).filter { it.isNotBlank() }.distinct().take(20),
            coverUrl = cover,
            title = title
        )
    }

    private fun unescapeUrl(u: String): String =
        u.replace("\\u002F", "/").replace("\\/", "/").replace("\\u0026", "&").trim()

    /** 粗校验：看起来像可下载的视频地址 */
    private fun isVideoUrl(url: String): Boolean {
        if (!url.startsWith("http")) return false
        if (url.contains("html") && !url.contains("video")) return false
        val clean = url.substringBefore('?').lowercase()
        return clean.endsWith(".mp4") ||
            clean.endsWith(".m3u8") ||
            clean.endsWith(".webm") ||
            clean.endsWith(".mov") ||
            url.contains("playurl") ||
            url.contains("play_addr") ||
            url.contains("video/") ||
            clean.contains("video")
    }

    private fun isImageUrl(url: String): Boolean {
        if (!url.startsWith("http")) return false
        val clean = url.substringBefore('?').lowercase()
        return clean.endsWith(".jpg") || clean.endsWith(".jpeg") ||
            clean.endsWith(".png") || clean.endsWith(".webp") ||
            url.contains("sns-img") || url.contains("xhscdn")
    }
}
