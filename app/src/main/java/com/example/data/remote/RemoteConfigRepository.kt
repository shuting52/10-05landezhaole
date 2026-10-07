package com.example.data.remote

import android.content.Context
import android.util.Base64
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * 云端配置仓库：拉取「云台-懒得找了」发布的 admin-data.json。
 * 优先通过 GitHub API 读取（绕过 raw CDN 缓存，实现控制台「应用」后本体零延迟感知），
 * 失败时回退 raw 直链读取；全部失败返回 null，由调用方回退本地默认数据。
 */
class RemoteConfigRepository(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapter = moshi.adapter(AdminData::class.java)

    private val owner = "shuting52"
    private val repo = "10-05landezhaole"
    private val branch = "main"
    private val filePath = "admin-data.json"

    // GitHub API 直读（无 CDN 缓存，实时，但匿名限 60 次/小时/IP）
    private val apiUrl = "https://api.github.com/repos/$owner/$repo/contents/$filePath?ref=$branch"
    // v1.2.4 修复：jsDelivr CDN 缓存导致旧版收不到更新弹窗（最长 12h）——
    // 镜像 URL 附加 cache-bust 参数，发版后 CDN 缓存键立即变化，本体下次轮询即拿到最新数据
    private val cacheBust = "20261004"
    // 只读镜像链（国内网络下 cdn.jsdelivr.net 主站可能被 DNS 污染，多节点顺延）
    private val mirrorUrls = listOf(
        "https://testingcf.jsdelivr.net/gh/$owner/$repo@$branch/$filePath?v=$cacheBust",
        "https://cdn.jsdelivr.net/gh/$owner/$repo@$branch/$filePath?v=$cacheBust",
        "https://fastly.jsdelivr.net/gh/$owner/$repo@$branch/$filePath?v=$cacheBust",
        "https://gcore.jsdelivr.net/gh/$owner/$repo@$branch/$filePath?v=$cacheBust",
        "https://ghfast.top/https://raw.githubusercontent.com/$owner/$repo/$branch/$filePath?v=$cacheBust",
        "https://ghproxy.net/https://raw.githubusercontent.com/$owner/$repo/$branch/$filePath?v=$cacheBust",
        "https://raw.gitmirror.com/$owner/$repo/$branch/$filePath?v=$cacheBust",
        "https://raw.githubusercontent.com/$owner/$repo/$branch/$filePath?v=$cacheBust"
    )

    suspend fun fetchAdminData(): AdminData? = withContext(Dispatchers.IO) {
        // 优先级：API（实时）→ 镜像链（jsDelivr 多节点 / raw 代理 / raw 兜底）
        fetchFromApi() ?: fetchFromMirrors()
    }

    /** 阻塞版读取（供安全校验等非协程场景使用） */
    fun fetchAdminDataBlocking(): AdminData? {
        return fetchFromApi() ?: fetchFromMirrors()
    }

    /** v1.2.5 修复：镜像链改为「遍历全部镜像，取版本号最高的结果」——
     * 解决 jsDelivr 边缘节点 CDN 缓存不一致（部分节点返回旧版本）时，
     * 旧数据被当作「成功」提前返回、挡住新数据的问题。
     * 任一镜像返回的 code 最高者即采用；全部失败才返回 null。 */
    private fun fetchFromMirrors(): AdminData? {
        var best: AdminData? = null
        var bestCode = 0
        for (url in mirrorUrls) {
            val data = fetchJsonText(url)?.let { adapter.fromJson(it) }
            if (data != null) {
                val code = data.version?.code ?: 0
                if (code > bestCode) {
                    bestCode = code
                    best = data
                }
            }
        }
        return best
    }

    /** 拉取 URL 文本；返回网页（HTML）而非 JSON 时视为被拦截，跳过该来源 */
    private fun fetchJsonText(url: String): String? {
        return try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val trimmed = body.trimStart()
                if (trimmed.startsWith("<") && !trimmed.startsWith("<{")) return null
                if (trimmed.startsWith("{")) body else null
            }
        } catch (e: Exception) {
            null
        }
    }

    /** 通过 GitHub API 读取（contents API 返回 base64 内容，需解码） */
    private fun fetchFromApi(): AdminData? {
        return try {
            val request = Request.Builder().url(apiUrl).build()
            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val json = resp.body?.string() ?: return null
                // contents API 响应含 "content" 字段（base64）
                val contentField = org.json.JSONObject(json).optString("content", "")
                if (contentField.isBlank()) return null
                val decoded = String(
                    Base64.decode(contentField.replace("\n", ""), Base64.DEFAULT),
                    Charsets.UTF_8
                )
                adapter.fromJson(decoded)
            }
        } catch (e: Exception) {
            null
        }
    }
}
