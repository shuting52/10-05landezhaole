package com.example.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * 国内直连高速图标加载器（无需 VPN，支持自动过滤第三方 API 返回的「默认 Chrome 地球占位图」）
 */
object FastFaviconImageLoader {
    private var instance: ImageLoader? = null

    fun get(context: Context): ImageLoader {
        return instance ?: synchronized(this) {
            instance ?: run {
                val okHttp = OkHttpClient.Builder()
                    .connectTimeout(2200, TimeUnit.MILLISECONDS)
                    .readTimeout(2200, TimeUnit.MILLISECONDS)
                    .callTimeout(3200, TimeUnit.MILLISECONDS)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .retryOnConnectionFailure(true)
                    .addInterceptor { chain ->
                        val req = chain.request().newBuilder()
                            .header(
                                "User-Agent",
                                "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Safari/537.36"
                            )
                            // 仅请求 PNG/ICO/JPEG/WebP，严禁声明 image/avif 或 image/heic，
                            // 防止 CDN（如字节/阿里/Cloudflare）返回 AVIF 流触发 Android 底层 Codec2 (c2.android.av1.decoder) 报错 6
                            .header("Accept", "image/png,image/x-icon,image/vnd.microsoft.icon,image/jpeg,image/webp;q=0.9")
                            .build()
                        val resp = chain.proceed(req)
                        val contentType = resp.header("Content-Type")?.lowercase() ?: ""
                        if (contentType.contains("avif") || contentType.contains("heic") || contentType.contains("heif") || contentType.contains("svg") || contentType.contains("text/html")) {
                            resp.close()
                            throw java.io.IOException("Unsupported codec format for bitmap decoder: $contentType")
                        }
                        // 检查响应头前 32 字节（Magic Bytes）：许多国内 CDN 在返回 AVIF/HEIF/HTML 时 Content-Type 仍标为 image/png 或 octet-stream，
                        // 一旦流入 BitmapFactory 会触发 Android 底层 Codec2 (c2.android.av1.decoder / heic) 报错 "Failed to query component interface for required system resources: 6"
                        val peekBytes = try { resp.peekBody(32).bytes() } catch (_: Exception) { ByteArray(0) }
                        if (peekBytes.size >= 12) {
                            // ISOBMFF (AVIF / HEIC / HEIF / MP4) 第 4..7 字节为 "ftyp" (0x66, 0x74, 0x79, 0x70)
                            val isIsoBmff = peekBytes[4] == 0x66.toByte() &&
                                peekBytes[5] == 0x74.toByte() &&
                                peekBytes[6] == 0x79.toByte() &&
                                peekBytes[7] == 0x70.toByte()
                            val headStr = String(peekBytes, Charsets.ISO_8859_1).trimStart().lowercase()
                            val isXmlOrHtml = headStr.startsWith("<svg") || headStr.startsWith("<!doctype") || headStr.startsWith("<html") || headStr.startsWith("<?xml")
                            if (isIsoBmff || isXmlOrHtml) {
                                resp.close()
                                throw java.io.IOException("Blocked AVIF/HEIF/SVG/HTML magic bytes from entering BitmapFactory")
                            }
                        }
                        resp
                    }
                    .build()

                kotlin.concurrent.thread {
                    try {
                        context.applicationContext.cacheDir.resolve("site_favicons_v4_cn").deleteRecursively()
                        context.applicationContext.cacheDir.resolve("image_cache").deleteRecursively()
                    } catch (_: Exception) {}
                }

                ImageLoader.Builder(context.applicationContext)
                    .okHttpClient(okHttp)
                    .allowHardware(false)
                    .bitmapFactoryExifOrientationPolicy(coil.decode.ExifOrientationPolicy.RESPECT_PERFORMANCE)
                    .memoryCache {
                        MemoryCache.Builder(context.applicationContext)
                            .maxSizePercent(0.25)
                            .build()
                    }
                    .diskCache {
                        DiskCache.Builder()
                            .directory(context.applicationContext.cacheDir.resolve("site_favicons_v5_no_avif"))
                            .maxSizeBytes(150L * 1024 * 1024)
                            .build()
                    }
                    .crossfade(120)
                    .build().also {
                        instance = it
                        coil.Coil.setImageLoader(it)
                    }
            }
        }
    }
}

/**
 * 国内网络自动获取站点真实图标（无需 VPN）：
 * 1. 内置主流站点与短剧/影视/AI/工具国内直连 CDN 高清官方图标库
 * 2. 站点源站直连探测（`https://domain/favicon.ico`、`https://www.domain/favicon.ico`、`apple-touch-icon.png`）
 * 3. 实时 HTML `<head>` 嗅探（自动解析网页源码中的 `<link rel="icon">` / `apple-touch-icon` / `og:image`）
 * 4. 国内多节点 Favicon 镜像服务兜底，并带「通用 Chrome/地球假图标」像素指纹识别与自动剔除
 */
object FaviconHelper {

    private val htmlDiscoveredIconCache = ConcurrentHashMap<String, String>()

    fun extractHost(rawUrl: String): String {
        return try {
            val normalized = if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) rawUrl else "https://$rawUrl"
            val uri = URI(normalized)
            val host = uri.host ?: ""
            if (host.isNotBlank()) host.lowercase() else {
                rawUrl.lowercase()
                    .removePrefix("https://")
                    .removePrefix("http://")
                    .substringBefore("/")
                    .substringBefore("?")
                    .substringBefore(":")
            }
        } catch (_: Exception) {
            rawUrl.lowercase()
                .removePrefix("https://")
                .removePrefix("http://")
                .substringBefore("/")
                .substringBefore("?")
                .substringBefore(":")
        }
    }

    fun extractDomain(rawUrl: String): String {
        return extractHost(rawUrl).removePrefix("www.")
    }

    fun extractRootDomain(domain: String): String {
        val parts = domain.split(".")
        if (parts.size <= 2) return domain
        val secondLevelTlds = setOf("com.cn", "net.cn", "org.cn", "gov.cn", "edu.cn", "co.jp", "co.uk", "com.hk", "com.tw")
        val tail2 = "${parts[parts.size - 2]}.${parts.last()}"
        return if (secondLevelTlds.contains(tail2) && parts.size >= 3) {
            "${parts[parts.size - 3]}.$tail2"
        } else {
            tail2
        }
    }

    /**
     * 国内 CDN 直连官方高清图标映射（0 延迟直出，无需 VPN，彻底解决子域名或防盗链导致的假图标）
     */
    private val directBrandIcons = mapOf(
        // 短剧与国内主流视频平台（国内阿里/字节/腾讯/爱奇艺/优酷/芒果/快手/B站官方国内 CDN）
        "hongguo.tv" to "https://lf3-static.bytednsdoc.com/obj/eden-cn/pipieh7nupabozups/toutiao_web_pc/tt-icon.png",
        "hongguofilm.com" to "https://lf3-static.bytednsdoc.com/obj/eden-cn/pipieh7nupabozups/toutiao_web_pc/tt-icon.png",
        "hongguodrama.com" to "https://lf3-static.bytednsdoc.com/obj/eden-cn/pipieh7nupabozups/toutiao_web_pc/tt-icon.png",
        "fanqienovel.com" to "https://lf-fe-scm.fqnovelstatic.com/obj/fqnovel-fe/ novel_web_pc/favicon.ico",
        "douyin.com" to "https://lf1-cdn-tos.bytegoofy.com/goofy/ies/douyin_web/public/favicon.ico",
        "iesdouyin.com" to "https://lf1-cdn-tos.bytegoofy.com/goofy/ies/douyin_web/public/favicon.ico",
        "huoshan.com" to "https://lf1-cdn-tos.bytegoofy.com/goofy/ies/douyin_web/public/favicon.ico",
        "ixigua.com" to "https://sf1-cdn-tos.bdxiguastatic.com/obj/ttfe/xigua_fe/xigua_favicon.ico",
        "iqiyi.com" to "https://www.iqiyipic.com/common/fix/128-128-logo.png",
        "suike.iqiyi.com" to "https://www.iqiyipic.com/common/fix/128-128-logo.png",
        "qq.com" to "https://v.qq.com/favicon.ico",
        "v.qq.com" to "https://v.qq.com/favicon.ico",
        "mgtv.com" to "https://www.mgtv.com/favicon.ico",
        "youku.com" to "https://img.alicdn.com/tfs/TB1WeJ9Xrj1gK0jSZFuXXcrHpXa-195-195.png",
        "kuaishou.com" to "https://static.yximgs.com/udata/pkg/WEB-LIVE/kwai_icon.7f685a97.ico",
        "bilibili.com" to "https://www.bilibili.com/favicon.ico",
        "miguvideo.com" to "https://www.miguvideo.com/favicon.ico",
        "haokan.baidu.com" to "https://haokan.baidu.com/favicon.ico",
        "le.com" to "https://www.le.com/favicon.ico",
        "1905.com" to "https://www.1905.com/favicon.ico",
        "sohu.com" to "https://tv.sohu.com/favicon.ico",
        "pptv.com" to "https://www.pptv.com/favicon.ico",
        "acfun.cn" to "https://cdn.aixifan.com/ico/favicon.ico",
        "xiaohongshu.com" to "https://www.xiaohongshu.com/favicon.ico",
        "weibo.com" to "https://weibo.com/favicon.ico",
        "zhihu.com" to "https://static.zhihu.com/heifetz/favicon.ico",
        "baidu.com" to "https://www.baidu.com/favicon.ico",
        "taobao.com" to "https://www.taobao.com/favicon.ico",
        "jd.com" to "https://www.jd.com/favicon.ico",
        "pinduoduo.com" to "https://www.pinduoduo.com/homeFavicon.ico",
        "meituan.com" to "https://www.meituan.com/favicon.ico",
        "ctrip.com" to "https://www.ctrip.com/favicon.ico",
        "163.com" to "https://music.163.com/favicon.ico",
        "music.163.com" to "https://s1.music.126.net/style/favicon.ico",
        "y.qq.com" to "https://y.qq.com/favicon.ico",
        "kugou.com" to "https://www.kugou.com/root/favicon.ico",
        "kuwo.cn" to "https://www.kuwo.cn/favicon.ico",
        "喜马拉雅" to "https://www.ximalaya.com/favicon.ico",
        "ximalaya.com" to "https://www.ximalaya.com/favicon.ico",
        "douban.com" to "https://img1.doubanio.com/favicon.ico",
        "csdn.net" to "https://g.csdnimg.cn/static/logo/favicon32.ico",
        "juejin.cn" to "https://lf3-cdn-tos.bytescm.com/obj/static/xitu_juejin_web//static/favicons/favicon-32x32.png",
        "gitee.com" to "https://gitee.com/favicon.ico",
        "oschina.net" to "https://www.oschina.net/favicon.ico",
        "cnblogs.com" to "https://common.cnblogs.com/favicon.ico",
        "v2ex.com" to "https://www.v2ex.com/static/icon-192.png",
        "52pojie.cn" to "https://www.52pojie.cn/favicon.ico",
        "sspai.com" to "https://cdn.sspai.com/sspai/assets/img/favicon/icon_60.png",
        "ithome.com" to "https://www.ithome.com/favicon.ico",
        "36kr.com" to "https://36kr.com/favicon.ico",
        "huxiu.com" to "https://www.huxiu.com/favicon.ico",

        // 国内与全球热门 AI 平台（国内直连图标源）
        "deepseek.com" to "https://cdn.deepseek.com/chat/icon.png",
        "chat.deepseek.com" to "https://cdn.deepseek.com/chat/icon.png",
        "kimi.moonshot.cn" to "https://statics.moonshot.cn/kimi-chat/favicon.ico",
        "moonshot.cn" to "https://statics.moonshot.cn/kimi-chat/favicon.ico",
        "doubao.com" to "https://lf-flow-web-cdn.doubao.com/obj/flow-doubao/doubao/web/logo-icon.png",
        "yiyan.baidu.com" to "https://nlp-eb.cdn.bcebos.com/logo/favicon.ico",
        "tongyi.aliyun.com" to "https://img.alicdn.com/imgextra/i4/O1CN01ef3C5G1W5i4fH03F8_!!6000000002737-2-tps-128-128.png",
        "qianwen.aliyun.com" to "https://img.alicdn.com/imgextra/i4/O1CN01ef3C5G1W5i4fH03F8_!!6000000002737-2-tps-128-128.png",
        "aliyun.com" to "https://img.alicdn.com/tfs/TB1_ZXuNcfpK1RjSZFOXXa6nFXa-32-32.ico",
        "lingma.aliyun.com" to "https://img.alicdn.com/imgextra/i1/O1CN01fUe7vL1Yx83p0R6Vq_!!6000000003120-2-tps-64-64.png",
        "chatglm.cn" to "https://chatglm.cn/favicon.ico",
        "bigmodel.cn" to "https://bigmodel.cn/favicon.ico",
        "yuanbao.tencent.com" to "https://yuanbao.tencent.com/favicon.ico",
        "xinghuo.xfyun.cn" to "https://xinghuo.xfyun.cn/spark-icon.ico",
        "metaso.cn" to "https://metaso.cn/favicon.ico",
        "tiangong.cn" to "https://www.tiangong.cn/favicon.ico",
        "hailuoai.com" to "https://hailuoai.com/favicon.ico",
        "klingai.kuaishou.com" to "https://klingai.kuaishou.com/favicon.ico",
        "klingai.com" to "https://klingai.kuaishou.com/favicon.ico",
        "jimeng.jianying.com" to "https://lf3-lv-buz.vlabstatic.com/obj/image-lvweb-buz/common/images/dreamina-v1.ico",
        "coze.cn" to "https://lf-coze-web-cdn.coze.cn/obj/coze-web-cn/obric/coze/favicon.1970.png",
        "trae.cn" to "https://www.trae.cn/favicon.ico",
        "trae.ai" to "https://www.trae.cn/favicon.ico",
        "chatgpt.com" to "https://cdn.oaistatic.com/_next/static/media/apple-touch-icon.82af6fe1.png",
        "openai.com" to "https://cdn.oaistatic.com/_next/static/media/apple-touch-icon.82af6fe1.png",
        "claude.ai" to "https://claude.ai/favicon.ico",
        "anthropic.com" to "https://www.anthropic.com/favicon.ico",
        "github.com" to "https://github.githubassets.com/favicons/favicon.png",
        "cursor.com" to "https://www.cursor.com/favicon.ico",
        "cline.bot" to "https://cline.bot/favicon.ico",
        "lovable.dev" to "https://lovable.dev/favicon.ico",
        "bolt.new" to "https://bolt.new/favicon.ico",
        "windsurf.com" to "https://windsurf.com/favicon.ico"
    )

    /**
     * 构建某站点的多级国内直连候选图标链（无需 VPN，源站直连优先，避免第三方 API 返回假 Chrome 地球图标）：
     * 1. 命中的国内大厂官方高清 CDN 图标（0 延迟、100% 准确）
     * 2. 后台 HTML `<head>` 实时嗅探到的 `<link rel="icon">` / `apple-touch-icon` 真实地址
     * 3. 站点自身源站及根域名直连 `/favicon.ico`、`/favicon.png`、`/apple-touch-icon.png`（国内网络直接请求目标网站）
     * 4. 显式配置的 iconUrl（若与上面不重复）
     * 5. 国内多节点 Favicon 镜像 API（带通用假图标像素拦截）
     */
    fun buildCandidateUrls(url: String, title: String = "", explicitIcon: String = ""): List<String> {
        val host = extractHost(url)
        val domain = host.removePrefix("www.")
        val rootDomain = extractRootDomain(domain)
        if (domain.isBlank()) return emptyList()

        val result = LinkedHashSet<String>()

        // 1) 根据完整域名、根域名或标题匹配内置官方国内 CDN 图标
        directBrandIcons[host]?.let { result.add(it) }
        directBrandIcons[domain]?.let { result.add(it) }
        directBrandIcons[rootDomain]?.let { result.add(it) }
        when {
            title.contains("红果") -> directBrandIcons["hongguo.tv"]?.let { result.add(it) }
            title.contains("番茄") -> directBrandIcons["fanqienovel.com"]?.let { result.add(it) }
            title.contains("抖音") || title.contains("火山") -> directBrandIcons["douyin.com"]?.let { result.add(it) }
            title.contains("西瓜") -> directBrandIcons["ixigua.com"]?.let { result.add(it) }
            title.contains("爱奇艺") || title.contains("随刻") -> directBrandIcons["iqiyi.com"]?.let { result.add(it) }
            title.contains("腾讯") -> directBrandIcons["v.qq.com"]?.let { result.add(it) }
            title.contains("优酷") -> directBrandIcons["youku.com"]?.let { result.add(it) }
            title.contains("芒果") || title.contains("大芒") -> directBrandIcons["mgtv.com"]?.let { result.add(it) }
            title.contains("快手") -> directBrandIcons["kuaishou.com"]?.let { result.add(it) }
            title.contains("哔哩") || title.contains("B站") -> directBrandIcons["bilibili.com"]?.let { result.add(it) }
            title.contains("百度") || title.contains("好看") -> directBrandIcons["haokan.baidu.com"]?.let { result.add(it) }
            title.contains("咪咕") -> directBrandIcons["miguvideo.com"]?.let { result.add(it) }
        }

        // 2) 若 HTML 嗅探器已解析出该域名的真实 <link rel="icon">，高优使用
        htmlDiscoveredIconCache[domain]?.takeIf { it.isNotBlank() }?.let { result.add(it) }

        // 3) 显式传入的 icon（如果不是明显的死链占位）
        if (explicitIcon.isNotBlank() && (explicitIcon.startsWith("http://") || explicitIcon.startsWith("https://"))) {
            result.add(explicitIcon)
        }

        // 4) 国内网络直连站点源站本身（无需任何第三方中转或 VPN）
        result.add("https://$host/favicon.ico")
        if (!host.startsWith("www.")) {
            result.add("https://www.$domain/favicon.ico")
        }
        if (rootDomain != domain) {
            result.add("https://www.$rootDomain/favicon.ico")
            result.add("https://$rootDomain/favicon.ico")
        }
        result.add("https://$host/apple-touch-icon.png")
        result.add("https://$host/favicon.png")

        // 5) 国内无需 VPN 的 Favicon 聚合镜像（获取后会自动过像素指纹检测，若是默认 Chrome 地球假图标则自动丢弃）
        result.add("https://favicon.im/$domain?larger=true")
        if (rootDomain != domain) {
            result.add("https://favicon.im/$rootDomain?larger=true")
        }
        result.add("https://api.iowen.cn/favicon/$domain.png")
        if (rootDomain != domain) {
            result.add("https://api.iowen.cn/favicon/$rootDomain.png")
        }
        result.add("https://favicon.cccyun.cc/$domain")

        return result.toList()
    }

    /**
     * 后台轻量嗅探目标站点的 HTML `<head>`（纯国内网络直连，无需 VPN），
     * 提取真实的 `<link rel="icon" / "shortcut icon" / "apple-touch-icon">` 或 `<meta property="og:image">`。
     */
    suspend fun discoverIconFromHtml(rawUrl: String): String? = withContext(Dispatchers.IO) {
        val domain = extractDomain(rawUrl)
        if (domain.isBlank()) return@withContext null
        htmlDiscoveredIconCache[domain]?.let { cached ->
            return@withContext cached.ifBlank { null }
        }

        val targetUrl = if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) rawUrl else "https://$rawUrl"
        try {
            val conn = (URL(targetUrl).openConnection() as HttpURLConnection).apply {
                connectTimeout = 2200
                readTimeout = 2200
                instanceFollowRedirects = true
                setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0 Mobile Safari/537.36"
                )
                setRequestProperty("Accept", "text/html,application/xhtml+xml;q=0.9,*/*;q=0.8")
            }
            val code = conn.responseCode
            if (code in 200..399) {
                val finalUrl = conn.url
                val baseHost = "${finalUrl.protocol}://${finalUrl.host}"
                val reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
                val sb = StringBuilder()
                var line: String?
                var chars = 0
                while (reader.readLine().also { line = it } != null && chars < 48000) {
                    sb.append(line).append('\n')
                    chars += (line?.length ?: 0)
                    if (line?.contains("</head>", ignoreCase = true) == true) break
                }
                reader.close()
                val headHtml = sb.toString()

                // 匹配 <link ... rel="...icon..." ... href="..."> 或 href 在 rel 之前
                val linkTags = Regex("<link[^>]+>", RegexOption.IGNORE_CASE).findAll(headHtml)
                var bestHref: String? = null
                for (match in linkTags) {
                    val tag = match.value
                    val relMatch = Regex("rel\\s*=\\s*[\"']([^\"']+)[\"']", RegexOption.IGNORE_CASE).find(tag)
                    val rel = relMatch?.groupValues?.get(1)?.lowercase() ?: continue
                    if (rel.contains("icon")) {
                        val hrefMatch = Regex("href\\s*=\\s*[\"']([^\"']+)[\"']", RegexOption.IGNORE_CASE).find(tag)
                        val href = hrefMatch?.groupValues?.get(1)?.trim() ?: continue
                        val lowerHref = href.lowercase().substringBefore("?")
                        if (href.isNotBlank() && !href.startsWith("data:") &&
                            !lowerHref.endsWith(".svg") && !lowerHref.endsWith(".avif") && !lowerHref.endsWith(".heic")
                        ) {
                            bestHref = href
                            if (rel.contains("apple-touch-icon") || lowerHref.endsWith(".png") || lowerHref.endsWith(".ico")) {
                                break
                            }
                        }
                    }
                }

                if (bestHref != null) {
                    val resolved = when {
                        bestHref.startsWith("http://") || bestHref.startsWith("https://") -> bestHref
                        bestHref.startsWith("//") -> "https:$bestHref"
                        bestHref.startsWith("/") -> "$baseHost$bestHref"
                        else -> "$baseHost/$bestHref"
                    }
                    htmlDiscoveredIconCache[domain] = resolved
                    return@withContext resolved
                }
            }
        } catch (_: Exception) {
        }
        htmlDiscoveredIconCache[domain] = ""
        null
    }

    /**
     * 检测第三方 Favicon API（如 iowen.cn / cccyun.cc / kucat.cn）在找不到图标时返回的「默认伪图标」：
     * - 典型特征 1：iowen.cn 默认返回一个 4 色 Chrome 球或灰白小地球，或者 1x1 空白图；
     * - 典型特征 2：纯单色/全透明或尺寸过小（< 8px）。
     */
    fun isLikelyGenericPlaceholderBitmap(bmp: Bitmap, sourceUrl: String): Boolean {
        val w = bmp.width
        val h = bmp.height
        if (w <= 4 || h <= 4) return true

        val isFromThirdPartyApi = sourceUrl.contains("iowen.cn") ||
            sourceUrl.contains("cccyun.cc") ||
            sourceUrl.contains("kucat.cn") ||
            sourceUrl.contains("favicon.im")
        if (!isFromThirdPartyApi) return false

        return try {
            // 采样检测：第三方 API 在抓取失败时常返回固定尺寸的 Chrome 地球图标或灰阶地球
            // 统计红、绿、黄、蓝中心环特征（Chrome 默认球中心是蓝色圆点，外圈红黄绿，且四角完全透明）
            val cx = w / 2
            val cy = h / 2
            val centerPixel = bmp.getPixel(cx, cy)
            val cAlpha = (centerPixel ushr 24) and 0xFF
            val cR = (centerPixel ushr 16) and 0xFF
            val cG = (centerPixel ushr 8) and 0xFF
            val cB = centerPixel and 0xFF

            // Chrome 默认图标特征：中心点为鲜艳蓝色 (B > 170, R < 95)，上方偏红 (R > 190, G < 90)，下方偏绿/黄
            val topPixel = bmp.getPixel(cx, (h * 0.24f).toInt().coerceIn(0, h - 1))
            val tR = (topPixel ushr 16) and 0xFF
            val tG = (topPixel ushr 8) and 0xFF
            val tB = topPixel and 0xFF

            val leftPixel = bmp.getPixel((w * 0.24f).toInt().coerceIn(0, w - 1), (h * 0.65f).toInt().coerceIn(0, h - 1))
            val lR = (leftPixel ushr 16) and 0xFF
            val lG = (leftPixel ushr 8) and 0xFF
            val lB = leftPixel and 0xFF

            val rightPixel = bmp.getPixel((w * 0.76f).toInt().coerceIn(0, w - 1), (h * 0.65f).toInt().coerceIn(0, h - 1))
            val rR = (rightPixel ushr 16) and 0xFF
            val rG = (rightPixel ushr 8) and 0xFF
            val rB = rightPixel and 0xFF

            val isChromeCenterBlue = cAlpha > 180 && cB > 165 && cR < 105 && cG in 80..185
            val isChromeTopRed = tR > 185 && tG < 100 && tB < 100
            val isChromeBottomGreenOrYellow = (lG > 135 && lB < 110) || (rR > 190 && rG > 150 && rB < 100)

            if (isChromeCenterBlue && isChromeTopRed && isChromeBottomGreenOrYellow) {
                return true
            }

            // 检测是否整张图几乎全灰/全透明（无有效色彩信息）
            var nonTransparent = 0
            var colorfulPixels = 0
            val stepX = (w / 6).coerceAtLeast(1)
            val stepY = (h / 6).coerceAtLeast(1)
            var x = 0
            while (x < w) {
                var y = 0
                while (y < h) {
                    val p = bmp.getPixel(x, y)
                    val a = (p ushr 24) and 0xFF
                    if (a > 40) {
                        nonTransparent++
                        val r = (p ushr 16) and 0xFF
                        val g = (p ushr 8) and 0xFF
                        val b = p and 0xFF
                        if (abs(r - g) > 18 || abs(g - b) > 18 || abs(r - b) > 18) {
                            colorfulPixels++
                        }
                    }
                    y += stepY
                }
                x += stepX
            }
            nonTransparent == 0 || (nonTransparent > 8 && colorfulPixels == 0)
        } catch (_: Exception) {
            false
        }
    }
}

/**
 * Resolves a vector icon fallback for the website based on URL or title keywords.
 */
fun resolveCategoryVectorIcon(url: String, title: String = ""): ImageVector {
    val lowerUrl = url.lowercase()
    val lowerTitle = title.lowercase()

    return when {
        lowerUrl.contains("chatgpt") || lowerUrl.contains("deepseek") || lowerUrl.contains("claude") ||
        lowerUrl.contains("ai") || lowerUrl.contains("gpt") || lowerTitle.contains("ai") ||
        lowerTitle.contains("智能") || lowerTitle.contains("大模型") || lowerTitle.contains("灵") || lowerTitle.contains("问") ->
            Icons.Filled.AutoAwesome

        lowerUrl.contains("video") || lowerUrl.contains("movie") || lowerUrl.contains("bilibili") ||
        lowerUrl.contains("youtube") || lowerUrl.contains("jianpian") || lowerUrl.contains("ys") ||
        lowerTitle.contains("影视") || lowerTitle.contains("电影") || lowerTitle.contains("剧") || lowerTitle.contains("短剧") ->
            Icons.Filled.Movie

        lowerUrl.contains("music") || lowerUrl.contains("fm") || lowerUrl.contains("radio") ||
        lowerUrl.contains("mp3") || lowerTitle.contains("音乐") || lowerTitle.contains("电台") || lowerTitle.contains("歌") ->
            Icons.Filled.MusicNote

        lowerUrl.contains("book") || lowerUrl.contains("novel") || lowerUrl.contains("read") ||
        lowerTitle.contains("书") || lowerTitle.contains("小说") || lowerTitle.contains("阅读") || lowerTitle.contains("轻小说") ->
            Icons.Filled.MenuBook

        lowerUrl.contains("game") || lowerUrl.contains("steam") || lowerUrl.contains("poki") ||
        lowerTitle.contains("游戏") || lowerTitle.contains("红警") || lowerTitle.contains("switch") ->
            Icons.Filled.SportsEsports

        lowerUrl.contains("git") || lowerUrl.contains("code") || lowerUrl.contains("dev") ||
        lowerUrl.contains("linux") || lowerTitle.contains("代码") || lowerTitle.contains("开发") || lowerTitle.contains("编程") ->
            Icons.Filled.Code

        lowerUrl.contains("search") || lowerUrl.contains("baidu") || lowerUrl.contains("bing") ||
        lowerUrl.contains("google") || lowerTitle.contains("搜索") ->
            Icons.Filled.Search

        lowerUrl.contains("pan") || lowerUrl.contains("cloud") || lowerTitle.contains("网盘") || lowerTitle.contains("云盘") ->
            Icons.Filled.CloudQueue

        lowerUrl.contains("jd") || lowerUrl.contains("meituan") || lowerUrl.contains("ele") ||
        lowerTitle.contains("红包") || lowerTitle.contains("打车") || lowerTitle.contains("特价") ->
            Icons.Filled.ShoppingBag

        lowerUrl.contains("paper") || lowerUrl.contains("study") || lowerUrl.contains("learn") ||
        lowerTitle.contains("论文") || lowerTitle.contains("白皮书") || lowerTitle.contains("指南") ->
            Icons.Filled.School

        lowerUrl.contains("tool") || lowerTitle.contains("工具") || lowerTitle.contains("助手") ->
            Icons.Filled.Build

        else -> Icons.Filled.Language
    }
}

/**
 * 根据站点域名与标题生成专属渐变品牌色板（确保不同站点色彩鲜明区分，绝不千篇一律同色同图）
 */
fun resolveBrandPalette(url: String, title: String = ""): Pair<Color, Color> {
    val lower = (url + " " + title).lowercase()
    return when {
        lower.contains("红果") || lower.contains("hongguo") -> Color(0xFFFF2E4D) to Color(0xFFFF6B35)
        lower.contains("河马") || lower.contains("hema") -> Color(0xFF00B4D8) to Color(0xFF0077B6)
        lower.contains("星芽") || lower.contains("xingya") -> Color(0xFFFF9F1C) to Color(0xFFFFBF69)
        lower.contains("番茄") || lower.contains("fanqie") -> Color(0xFFFF3B30) to Color(0xFFFF7A45)
        lower.contains("抖音") || lower.contains("douyin") -> Color(0xFF161823) to Color(0xFFFE2C55)
        lower.contains("快手") || lower.contains("kuaishou") -> Color(0xFFFF4906) to Color(0xFFFF8800)
        lower.contains("爱奇艺") || lower.contains("随刻") || lower.contains("iqiyi") -> Color(0xFF00BE06) to Color(0xFF00E676)
        lower.contains("腾讯") || lower.contains("v.qq") -> Color(0xFF007ACC) to Color(0xFF00C6FF)
        lower.contains("芒果") || lower.contains("mgtv") -> Color(0xFFFF5F00) to Color(0xFFFFA000)
        lower.contains("优酷") || lower.contains("youku") -> Color(0xFF009BFF) to Color(0xFFFF2975)
        lower.contains("火山") || lower.contains("huoshan") -> Color(0xFFFF3D00) to Color(0xFFFF9100)
        lower.contains("西瓜") || lower.contains("ixigua") -> Color(0xFFF85959) to Color(0xFFFF2E63)
        lower.contains("bilibili") || lower.contains("哔哩") -> Color(0xFFFB7299) to Color(0xFFFF9DB5)
        lower.contains("deepseek") -> Color(0xFF2563EB) to Color(0xFF4F46E5)
        lower.contains("chatgpt") || lower.contains("openai") -> Color(0xFF10A37F) to Color(0xFF059669)
        lower.contains("claude") -> Color(0xFFD97706) to Color(0xFFB45309)
        lower.contains("github") -> Color(0xFF24292F) to Color(0xFF4B5563)
        lower.contains("zhihu") || lower.contains("知乎") -> Color(0xFF0084FF) to Color(0xFF38BDF8)
        lower.contains("baidu") || lower.contains("百度") || lower.contains("好看") -> Color(0xFF2932E1) to Color(0xFF4F46E5)
        else -> {
            // 按站点名称+域名哈希出 12 组高颜值渐变色，确保每个站点都有自己独一无二的品牌视觉
            val palettes = listOf(
                Color(0xFF6366F1) to Color(0xFF8B5CF6),
                Color(0xFFEC4899) to Color(0xFFF43F5E),
                Color(0xFF0EA5E9) to Color(0xFF2563EB),
                Color(0xFF10B981) to Color(0xFF059669),
                Color(0xFFF59E0B) to Color(0xFFEA580C),
                Color(0xFF8B5CF6) to Color(0xFFD946EF),
                Color(0xFF14B8A6) to Color(0xFF0284C7),
                Color(0xFFF43F5E) to Color(0xFFFB923C),
                Color(0xFF3B82F6) to Color(0xFF6366F1),
                Color(0xFF06B6D4) to Color(0xFF3B82F6),
                Color(0xFFA855F7) to Color(0xFFEC4899),
                Color(0xFF22C55E) to Color(0xFF14B8A6)
            )
            val hash = abs((title.ifBlank { url }).hashCode())
            palettes[hash % palettes.size]
        }
    }
}

fun resolveBrandTint(url: String): Color = resolveBrandPalette(url).first

/**
 * 提取站点核心品牌徽章字/Emoji（用于当目标网站本身无独立服务器时的专属精致 App 图标绘制）
 */
private fun resolveSiteBadgeSymbol(title: String, fallbackText: String, url: String): String {
    val cleanFallback = fallbackText.trim()
    // 如果 fallbackText 是 Emoji（如 🍿、🦛、⭐、🍅、🎵），优先展示该专属图形标
    if (cleanFallback.isNotEmpty() && cleanFallback.codePointAt(0) > 0x2000) {
        return String(Character.toChars(cleanFallback.codePointAt(0)))
    }
    val cleanTitle = title.trim()
        .removePrefix("https://")
        .removePrefix("http://")
        .removePrefix("www.")
    if (cleanTitle.isNotEmpty()) {
        val firstCp = cleanTitle.codePointAt(0)
        return String(Character.toChars(firstCp)).uppercase()
    }
    val domain = FaviconHelper.extractDomain(url)
    return domain.take(1).uppercase().ifBlank { "★" }
}

/**
 * SiteBrandIcon：自动获取对应站点的真实 Icon 图标（支持国内网络直连自动获取，无需借助 VPN）
 * 核心特性：
 * 1. 国内大厂与热门站点（红果短剧、番茄、抖音、爱奇艺随刻、腾讯、优酷、芒果、快手、DeepSeek、Kimi 等）直连国内 CDN 官方高清图标；
 * 2. 站点源站直连 `/favicon.ico` + 后台 HTML `<head>` 自动解析 `<link rel="icon">` / `apple-touch-icon`（纯国内网络直连）；
 * 3. 智能像素检测：自动识别并拦截第三方 API 返回的「Chrome 四色地球默认占位图」，绝不把假 Chrome 图标当成站点图标；
 * 4. 专属品牌微缩图标底座：每个站点根据自身品牌色板与专属符号生成立体微缩 App 图标，杜绝千篇一律的橙色场记板。
 */
@Composable
fun SiteBrandIcon(
    url: String,
    title: String,
    fallbackText: String = "",
    iconUrl: String = "",
    size: Dp = 34.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = remember(context) { FastFaviconImageLoader.get(context) }

    var discoveredHtmlIcon by remember(url) { mutableStateOf<String?>(null) }
    val candidates = remember(url, title, iconUrl, discoveredHtmlIcon) {
        val base = FaviconHelper.buildCandidateUrls(url, title, iconUrl)
        if (!discoveredHtmlIcon.isNullOrBlank() && !base.contains(discoveredHtmlIcon)) {
            listOf(discoveredHtmlIcon!!) + base
        } else {
            base
        }
    }

    var loadStage by remember(url, discoveredHtmlIcon) { mutableIntStateOf(0) }
    var isGenuineIconLoaded by remember(url) { mutableStateOf(false) }

    // 若前序源站直连未命中，自动在后台直连目标网站 HTML 解析真实 <link rel="icon">（无需 VPN）
    LaunchedEffect(url) {
        val domain = FaviconHelper.extractDomain(url)
        if (domain.isNotBlank() && !isGenuineIconLoaded) {
            val found = FaviconHelper.discoverIconFromHtml(url)
            if (!found.isNullOrBlank()) {
                discoveredHtmlIcon = found
            }
        }
    }

    val (brandStart, brandEnd) = remember(url, title) { resolveBrandPalette(url, title) }
    val badgeSymbol = remember(title, fallbackText, url) { resolveSiteBadgeSymbol(title, fallbackText, url) }
    val currentTargetUrl = candidates.getOrNull(loadStage)

    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation = 1.5.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = if (isGenuineIconLoaded) {
                        listOf(Color.White, Color(0xFFF8FAFC))
                    } else {
                        listOf(brandStart, brandEnd)
                    }
                )
            )
            .border(
                0.8.dp,
                if (isGenuineIconLoaded) brandStart.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.45f),
                RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // 1. 专属品牌底座：当真实网络图标尚未加载完成或目标站点无服务器时，展示高辨识度专属品牌立体图标
        if (!isGenuineIconLoaded) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // 顶部玻璃高光弧
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White.copy(alpha = 0.38f), Color.Transparent)
                    ),
                    topLeft = Offset.Zero,
                    size = Size(this.size.width, this.size.height * 0.48f),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                // 右下角微光装饰点
                drawCircle(
                    color = Color.White.copy(alpha = 0.22f),
                    radius = this.size.minDimension * 0.28f,
                    center = Offset(this.size.width * 0.82f, this.size.height * 0.82f)
                )
            }
            Text(
                text = badgeSymbol,
                color = Color.White,
                fontSize = (size.value * 0.46f).sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }

        // 2. 真实站点网络图标层（自动级联回退 + 自动剔除第三方 API 的假 Chrome 地球占位图）
        if (currentTargetUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(currentTargetUrl)
                    .allowHardware(false) // 允许读取像素以精准识别并拦截第三方 API 的 Chrome 假图标
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (isGenuineIconLoaded) 2.5.dp else 0.dp),
                onSuccess = { state ->
                    val bmp = (state.result.drawable as? BitmapDrawable)?.bitmap
                    if (bmp != null && FaviconHelper.isLikelyGenericPlaceholderBitmap(bmp, currentTargetUrl)) {
                        // 识别到第三方 API 返回了「通用 Chrome 地球占位图」，自动丢弃并切下一候选源
                        isGenuineIconLoaded = false
                        if (loadStage < candidates.size - 1) {
                            loadStage += 1
                        }
                    } else {
                        isGenuineIconLoaded = true
                    }
                },
                onError = {
                    if (loadStage < candidates.size - 1) {
                        loadStage += 1
                    }
                }
            )
        }
    }
}

