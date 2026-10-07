package com.example.data.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * 视频分享链接 → 直链解析器（v1.9.1 工具箱「即存下载」内置实现用）
 *
 * 背景：之前「即存」只能外链引导用户去下载独立的即存 App；
 * 现在把「复制链接 → 解析 → 下载视频」能力内置到本体软件，无需安装任何额外 App。
 *
 * 解析原理（轻量、无第三方密钥）：
 * 1. 用带移动端浏览器 UA 的请求访问分享短链（抖音/快手/小红书/B 站等），自动跟随重定向；
 * 2. 拿到 H5 落地页 HTML 后，依次尝试提取视频直链：
 *    - og:video / og:video:url / og:video:secure_url 社交分享标签
 *    - 页面 JSON 中的 playAddr / play_addr / playUrl / videoUrl 等字段
 *    - 通用 http(s) .mp4 / .m3u8 / .webm / .mov 直链
 *    - 相对路径视频并补全为绝对地址
 * 3. 返回候选直链列表（按优先级排序，UI 取第一个交给 DownloadManager 下载）。
 *
 * 说明：部分平台（如抖音）对分享页做加密/校验，静态 HTML 提取不一定 100% 成功，
 * 失败时 UI 会提示「安装即存 App 备用」，保证功能可用性兜底。
 */
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
     * 解析分享链接，返回按优先级排序的视频直链候选列表；解析不到返回空列表。
     */
    suspend fun resolve(context: Context, shareUrl: String): List<String> = withContext(Dispatchers.IO) {
        val url = shareUrl.trim()
        if (url.isBlank()) return@withContext emptyList()
        val candidates = mutableListOf<String>()
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
                if (!resp.isSuccessful) return@withContext emptyList()
                val finalUrl = resp.request.url.toString()
                val html = resp.body?.string() ?: return@withContext emptyList()
                candidates.addAll(extractVideos(html, finalUrl))
            }
        } catch (_: Exception) {
            // 网络异常/解析失败统一返回空，由 UI 提示兜底
        }
        candidates
            .map { it.replace("${'\\'}u002F", "/").replace("${'\\'}/", "/").trim() }
            .filter { it.isNotBlank() && it.length > 12 }
            .distinct()
            .take(5)
    }

    /** 从落地页 HTML 中提取视频直链候选（按提取优先级排序） */
    private fun extractVideos(html: String, pageUrl: String): List<String> {
        if (html.isBlank()) return emptyList()
        val out = mutableListOf<String>()

        // 1. og:video 系列社交分享标签（最标准）
        Regex(
            """(?i)(?:property|name)\s*=\s*["']og:video(?::url|:secure_url|:type)?["']\s+content\s*=\s*["']([^"']+)["']"""
        ).findAll(html).forEach { match ->
            val v = match.groupValues[1].trim()
            if (isVideoUrl(v)) out.add(v)
        }

        // 2. 页面 JSON 内嵌字段：playAddr / play_addr / playUrl / downloadAddr / videoUrl
        Regex(
            """(?i)["'](?:playAddr|play_addr|playAddrV2|playUrl|downloadAddr|download_addr|videoUrl|video_url|url_list)["']\s*:\s*["']([^"']+)["']"""
        ).findAll(html).forEach { match ->
            val v = match.groupValues[1].trim()
            // url_list 有时是数组元素 ["..."]，长度足够且含 http 才收
            if (v.startsWith("http") || v.contains("http")) {
                val clean = v.trimStart('"', '[').trimEnd('"', ']', ',').trim()
                if (isVideoUrl(clean) || clean.contains("http")) out.add(clean.ifBlank { v })
            }
        }

        // 3. 通用直链：http(s) 结尾 mp4 / m3u8 / webm / mov
        Regex(
            """https?://[^\s"'<>\\]+?\.(?:mp4|m3u8|webm|mov)(?:\?[^\s"'<>\\]*)?|https?://[^\s"'<>\\]*?/(?:play|down)[^\s"'<>\\]*\.(?:mp4|m3u8|webm|mov)(?:\?[^\s"'<>\\]*)?""",
            RegexOption.IGNORE_CASE
        ).findAll(html).forEach { match ->
            val v = match.value.trim()
            if (isVideoUrl(v)) out.add(v)
        }

        // 4. 相对路径视频（前面必须非字母/数字，避免抓到 JS 变量）
        Regex(
            """(?<![A-Za-z0-9_/])/(?:[A-Za-z0-9_\-./]+?)\.(?:mp4|webm|mov)(?:\?[^\s"'<>]*)?(?![A-Za-z0-9])""",
            RegexOption.IGNORE_CASE
        ).findAll(html).forEach { match ->
            val rel = match.value.trim()
            if (rel.startsWith("/") && !rel.startsWith("//")) {
                runCatching {
                    val u = java.net.URI(pageUrl)
                    if (u.scheme != null && u.host != null) {
                        val abs = u.scheme + "://" + u.host + rel
                        if (isVideoUrl(abs)) out.add(abs)
                    }
                }
            }
        }

        return out
    }

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
}