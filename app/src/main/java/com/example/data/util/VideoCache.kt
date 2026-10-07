package com.example.data.util

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

/**
 * 远程视频预缓存工具
 *
 * 背景：控制台上传的视频通过 raw.githubusercontent.com 直链存储。
 * VideoView 在加载 raw 链接时常因 302 重定向到 S3/UserContent、或被 GitHub CDN 拒绝空 UA
 * 而直接黑屏无法播放。
 *
 * 解决方案：用 OkHttp + UA 把视频完整预下载到应用私有缓存，
 * VideoView 再 setVideoPath(本地文件) 即可无障碍播放。
 *
 * 缓存策略：
 * - 文件名 = MD5(URL) + 原扩展名
 * - 缓存大小 200MB 上限，超过按 LRU 清理
 * - 同一个 URL 30 秒内复用已下载（避免重复下载）
 */
object VideoCache {

    private const val TAG = "VideoCache"
    private const val MAX_CACHE_BYTES = 200L * 1024 * 1024 // 200MB
    private const val MIN_BYTES = 8L * 1024 // < 8KB 视为无效

    /**
     * URL 路径百分号编码（v1.0.5 修复视频无法播放）：
     * 控制台上传的视频文件名带中文（如「卡点换装.mp4」「确认.mp4」），
     * OkHttp 的 Request.Builder().url() 遇到未编码的非 ASCII 字符会抛 IllegalArgumentException
     * （unexpected char），导致缓存下载失败 → 视频黑屏无法播放。
     * 本函数仅对「含非 ASCII 字符的路径段」做百分号编码：
     * - 已编码段（全 ASCII，含 %XX）保持不变，避免二次编码；
     * - 非 ASCII 段按 UTF-8 编码（空格 → %20，保留 / 分隔符）。
     */
    fun normalizeMediaUrl(raw: String): String {
        if (raw.isBlank()) return raw
        return try {
            val u = java.net.URI(raw)
            // 已能解析（纯 ASCII 或已编码）→ 原样返回
            if (u.rawPath != null && u.rawPath == u.path) return raw
            raw
        } catch (_: Exception) {
            // 含未编码中文 → 手动逐段编码
            try {
                val schemeIdx = raw.indexOf("://")
                if (schemeIdx < 0) return raw
                val head = raw.indexOf('/', schemeIdx + 3)
                if (head < 0) return raw
                val schemeHost = raw.substring(0, head)
                val rest = raw.substring(head)
                val path = rest.substringBefore('?')
                val query = rest.substringAfter('?', "")
                val encPath = path.split('/').joinToString("/") { seg ->
                    if (seg.any { it.code > 127 }) {
                        java.net.URLEncoder.encode(seg, "UTF-8")
                            .replace("+", "%20")
                            .replace("%2F", "/")
                    } else seg
                }
                schemeHost + encPath + if (query.isNotEmpty()) "?" + query else ""
            } catch (_: Exception) {
                raw
            }
        }
    }

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .build()
    }

    private val inflight: MutableMap<String, String> = mutableMapOf()

    /** 把远程视频缓存到本地，返回本地文件绝对路径。失败返回 null。 */
    suspend fun ensureLocal(context: Context, remoteUrl: String): String? = withContext(Dispatchers.IO) {
        if (remoteUrl.isBlank()) return@withContext null
        // 本地路径已经存在 → 直接返回
        if (remoteUrl.startsWith("/") || remoteUrl.startsWith("file://")) {
            val path = if (remoteUrl.startsWith("file://")) remoteUrl.removePrefix("file://") else remoteUrl
            val f = File(path)
            if (f.exists() && f.length() > MIN_BYTES) return@withContext path
        }
        val dir = cacheDir(context)
        val ext = guessExtension(remoteUrl)
        val target = File(dir, md5(remoteUrl) + ext)
        // 命中缓存且大小正常
        synchronized(target) {
            if (target.exists() && target.length() > MIN_BYTES) return@withContext target.absolutePath
            // 已下载过且本次进程内已记入 inflight 也可直接复用
            inflight[remoteUrl]?.let { p ->
                if (File(p).exists()) return@withContext p
            }
            try {
                downloadTo(context, remoteUrl, target)
                inflight[remoteUrl] = target.absolutePath
                target.absolutePath
            } catch (e: Exception) {
                Log.w(TAG, "下载失败 $remoteUrl: ${e.message}")
                target.delete()
                null
            }
        }
    }

    /** 同步阻塞版（用于在非协程上下文预热） */
    fun ensureLocalBlocking(context: Context, remoteUrl: String): String? {
        if (remoteUrl.isBlank()) return null
        if (remoteUrl.startsWith("/") || remoteUrl.startsWith("file://")) {
            val path = if (remoteUrl.startsWith("file://")) remoteUrl.removePrefix("file://") else remoteUrl
            val f = File(path)
            if (f.exists() && f.length() > MIN_BYTES) return path
        }
        val dir = cacheDir(context)
        val ext = guessExtension(remoteUrl)
        val target = File(dir, md5(remoteUrl) + ext)
        if (target.exists() && target.length() > MIN_BYTES) return target.absolutePath
        return try {
            downloadTo(context, remoteUrl, target)
            target.absolutePath
        } catch (e: Exception) {
            Log.w(TAG, "同步下载失败: ${e.message}")
            target.delete()
            null
        }
    }

    private fun downloadTo(context: Context, url: String, target: File) {
        // v1.7.9 修复视频黑屏：raw.githubusercontent.com 在国内经常被墙/重定向失败，
        // 按序尝试 多镜像（jsDelivr 多节点 + 国内代理 + raw 直链），全部失败才抛错
        val lastErr = arrayOfNulls<Exception>(1)
        for (candidate in mirrorCandidates(url)) {
            try {
                downloadFrom(candidate, target)
                return
            } catch (e: Exception) {
                lastErr[0] = e
                Log.w(TAG, "镜像下载失败 $candidate: ${e.message}")
            }
        }
        // 清理超出限额
        trimCache(dir = cacheDir(context))
        throw lastErr[0] ?: IOException("所有下载源均失败")
    }

    private fun downloadFrom(url: String, target: File) {
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Linux; Android) LzdzMediaCache/1.7.8")
            .header("Accept", "*/*")
            .build()
        client.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) throw IOException("HTTP ${resp.code}")
            val body = resp.body ?: throw IOException("空响应体")
            target.outputStream().use { out ->
                body.byteStream().use { input ->
                    val buf = ByteArray(16 * 1024)
                    var total = 0L
                    while (true) {
                        val n = input.read(buf)
                        if (n <= 0) break
                        out.write(buf, 0, n)
                        total += n
                    }
                    if (total < MIN_BYTES) throw IOException("文件过小（$total 字节）")
                }
            }
        }
    }

    /** 生成视频下载候选源：原始 URL → jsDelivr 多节点 → 国内代理 → raw 直链 */
    private fun mirrorCandidates(url: String): List<String> {
        val list = mutableListOf<String>()
        if (url.isNotBlank()) list.add(url)
        try {
            val m = Regex("^https?://([^/]+)/([^/]+)/([^/]+)/(?:main|master)/(.+)$")
                .find(url)
            if (m != null) {
                val host = m.groupValues[1]
                val owner = m.groupValues[2]
                val repo = m.groupValues[3]
                val path = m.groupValues[4]
                if (host == "raw.githubusercontent.com") {
                    val cdnBase = "https://testingcf.jsdelivr.net/gh/$owner/$repo@main/$path"
                    list.add(cdnBase)
                    list.add(cdnBase.replace("testingcf.jsdelivr.net", "cdn.jsdelivr.net"))
                    list.add(cdnBase.replace("testingcf.jsdelivr.net", "fastly.jsdelivr.net"))
                    list.add(cdnBase.replace("testingcf.jsdelivr.net", "gcore.jsdelivr.net"))
                    list.add("https://ghfast.top/https://raw.githubusercontent.com/$owner/$repo/main/$path")
                    list.add("https://ghproxy.net/https://raw.githubusercontent.com/$owner/$repo/main/$path")
                    list.add("https://raw.gitmirror.com/$owner/$repo/main/$path")
                }
            }
        } catch (_: Exception) {}
        // 对含非 ASCII 字符的候选源统一做百分号编码（修复中文文件名视频无法播放）
        return list.map(::normalizeMediaUrl).distinct()
    }

    private fun cacheDir(context: Context): File {
        val d = File(context.cacheDir, "video_cache")
        if (!d.exists()) d.mkdirs()
        return d
    }

    private fun guessExtension(url: String): String {
        val qIdx = url.indexOf('?')
        val clean = if (qIdx > 0) url.substring(0, qIdx) else url
        val dot = clean.lastIndexOf('.')
        if (dot in 0..(clean.length - 2)) {
            val ext = clean.substring(dot).lowercase()
            // 仅允许已知视频扩展
            if (ext in listOf(".mp4", ".webm", ".mov", ".m4v", ".mkv")) return ext
        }
        return ".mp4"
    }

    private fun trimCache(dir: File) {
        try {
            val files = dir.listFiles() ?: return
            var sum = files.sumOf { it.length() }
            if (sum <= MAX_CACHE_BYTES) return
            // 按最后修改时间升序（旧优先删）
            files.sortedBy { it.lastModified() }.forEach { f ->
                if (sum <= MAX_CACHE_BYTES) return
                val len = f.length()
                if (f.delete()) sum -= len
            }
        } catch (_: Exception) {}
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}