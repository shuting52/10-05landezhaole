package com.example.data

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.example.model.GithubConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 反编译重建 · 数据层（GitHub API 对接）
 *
 * 由 v2.1.0 APK 反编译的 AdminRepository 重建为可读 Kotlin：
 * - Token / 仓库配置存于 SharedPreferences（绝不硬编码）
 * - ghReadText：Contents API 读取（base64 解码 + 大文件 download_url 兜底 + 本地缓存）
 * - ghReadMirror：8 节点 CDN 镜像链只读（HTML 劫持检测 + JSON 校验）
 * - ghWriteText：Contents API PUT（带 sha 乐观锁 + 409 冲突重试）
 * - uploadBinaryFile：上传本地文件（APK/ZIP/MD/图片…）到 dist/ 目录
 * - purgeCdn：写入后刷新 jsDelivr，本体秒级生效
 */
class AdminRepository(private val context: Context) {

    companion object {
        const val DEFAULT_OWNER = "shuting52"
        const val DEFAULT_REPO = "10-05landezhaole"
        const val DEFAULT_BRANCH = "main"
        const val CONFIG_PATH = "admin-data.json"
        private const val CACHE_FILE_NAME = "cached_admin_data.json"
        private const val PLACEHOLDER_TOKEN = "YOUR_GITHUB_PAT_HERE"
        private const val PREFS_NAME = "lzdz_admin_prefs"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    // ---------------- Token / 仓库配置 ----------------

    var token: String
        get() {
            val saved = prefs.getString("lzdz_gh_token", "")?.trim().orEmpty()
            if (saved.isNotBlank() && saved != PLACEHOLDER_TOKEN) return saved
            // BuildConfig 占位（不硬编码真实 Token）
            val fromBuildConfig = "".trim()
            return if (fromBuildConfig.isBlank() || fromBuildConfig == PLACEHOLDER_TOKEN) "" else fromBuildConfig
        }
        set(value) {
            prefs.edit().putString("lzdz_gh_token", value.trim()).apply()
        }

    var config: GithubConfig
        get() {
            val owner = prefs.getString("lzdz_gh_owner", DEFAULT_OWNER)?.takeUnless { it.isBlank() } ?: DEFAULT_OWNER
            val repo = prefs.getString("lzdz_gh_repo", DEFAULT_REPO)?.takeUnless { it.isBlank() } ?: DEFAULT_REPO
            val branch = prefs.getString("lzdz_gh_branch", DEFAULT_BRANCH)?.takeUnless { it.isBlank() } ?: DEFAULT_BRANCH
            return GithubConfig(owner, repo, branch)
        }
        set(cfg) {
            prefs.edit()
                .putString("lzdz_gh_owner", cfg.owner.trim().takeUnless { it.isBlank() } ?: DEFAULT_OWNER)
                .putString("lzdz_gh_repo", cfg.repo.trim().takeUnless { it.isBlank() } ?: DEFAULT_REPO)
                .putString("lzdz_gh_branch", cfg.branch.trim().takeUnless { it.isBlank() } ?: DEFAULT_BRANCH)
                .apply()
        }

    // ---------------- 通知偏好 ----------------

    fun getNotificationPref(key: String, default: Boolean = false): Boolean =
        prefs.getBoolean("notif_$key", default)

    fun setNotificationPref(key: String, value: Boolean) {
        prefs.edit().putBoolean("notif_$key", value).apply()
    }

    // ---------------- 本地缓存 ----------------

    /** 读取本地缓存（优先内置 assets 兜底） */
    fun readLocalOrBundled(): String {
        try {
            val cache = context.getFileStreamPath(CACHE_FILE_NAME)
            if (cache.exists() && cache.length() > 100) {
                return context.openFileInput(CACHE_FILE_NAME).bufferedReader(Charsets.UTF_8).use { it.readText() }
            }
        } catch (_: Exception) {
        }
        return context.assets.open(CONFIG_PATH).bufferedReader(Charsets.UTF_8).use { it.readText() }
    }

    /** 保存本地缓存（离线/弱网兜底） */
    fun saveLocalCache(jsonText: String) {
        try {
            context.openFileOutput(CACHE_FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(jsonText.toByteArray(Charsets.UTF_8))
            }
        } catch (_: Exception) {
        }
    }

    // ---------------- HTML 劫持检测 ----------------

    fun looksLikeHtml(text: String): Boolean {
        val t = text.trim().lowercase(Locale.ROOT)
        return t.startsWith("<!doctype") || t.startsWith("<html") || t.startsWith("<head") || t.startsWith("<body")
    }

    // ---------------- 读取（Contents API，返回内容 + sha） ----------------

    /** 官方 API 读取：base64 解码；content 为空时走 download_url 下载大文件 */
    suspend fun ghReadText(path: String = CONFIG_PATH): Pair<String, String> =
        withContext(Dispatchers.IO) {
            val cfg = config
            val url = "https://api.github.com/repos/${cfg.owner}/${cfg.repo}/contents/$path?ref=${cfg.branch}"
            val req = newRequest(url)
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) throw IllegalStateException("读取失败 HTTP ${resp.code}: $path")
                val json = JSONObject(resp.body?.string() ?: throw IllegalStateException("空响应"))
                val sha = json.optString("sha", "")
                val contentB64 = json.optString("content", "").replace("\n", "").replace("\r", "")
                if (contentB64.isNotEmpty()) {
                    val text = String(Base64.decode(contentB64, Base64.DEFAULT), Charsets.UTF_8)
                    saveLocalCache(text)
                    return@withContext Pair(text, sha)
                }
                // 大文件：download_url 下载
                val downloadUrl = json.optString("download_url", "")
                if (downloadUrl.isBlank()) throw IllegalStateException("未返回文件内容")
                val dlReq = newRequest(downloadUrl)
                val bytes = client.newCall(dlReq).execute().use { r ->
                    if (!r.isSuccessful) throw IllegalStateException("下载大文件失败 HTTP ${r.code}")
                    r.body?.bytes() ?: throw IllegalStateException("空内容")
                }
                val text = String(bytes, Charsets.UTF_8)
                saveLocalCache(text)
                Pair(text, sha)
            }
        }

    /** 镜像链只读：8 节点 CDN 兜底（无 Token 可用），校验 HTML 劫持 + JSON 合法性 */
    suspend fun ghReadMirror(path: String = CONFIG_PATH): Pair<String, String> =
        withContext(Dispatchers.IO) {
            val cfg = config
            val cacheBust = SimpleDateFormat("yyyyMMddHHmm", Locale.US).format(Date())
            val mirrors = listOf(
                "https://testingcf.jsdelivr.net/gh/${cfg.owner}/${cfg.repo}@${cfg.branch}/$path?v=$cacheBust",
                "https://cdn.jsdelivr.net/gh/${cfg.owner}/${cfg.repo}@${cfg.branch}/$path?v=$cacheBust",
                "https://fastly.jsdelivr.net/gh/${cfg.owner}/${cfg.repo}@${cfg.branch}/$path?v=$cacheBust",
                "https://gcore.jsdelivr.net/gh/${cfg.owner}/${cfg.repo}@${cfg.branch}/$path?v=$cacheBust",
                "https://ghfast.top/https://raw.githubusercontent.com/${cfg.owner}/${cfg.repo}/${cfg.branch}/$path",
                "https://ghproxy.net/https://raw.githubusercontent.com/${cfg.owner}/${cfg.repo}/${cfg.branch}/$path",
                "https://raw.gitmirror.com/${cfg.owner}/${cfg.repo}/${cfg.branch}/$path",
                "https://raw.githubusercontent.com/${cfg.owner}/${cfg.repo}/${cfg.branch}/$path",
            )
            for (url in mirrors) {
                try {
                    client.newCall(Request.Builder().url(url).build()).execute().use { resp ->
                        if (resp.isSuccessful) {
                            val text = resp.body?.string() ?: return@use
                            if (looksLikeHtml(text)) return@use
                            JSONObject(text) // 校验 JSON 合法
                            saveLocalCache(text)
                            return@withContext Pair(text, "")
                        }
                    }
                } catch (_: Exception) {
                }
            }
            // 全部失败 → 本地缓存/内置兜底
            Pair(readLocalOrBundled(), "")
        }

    // ---------------- 写入（Contents API PUT，409 冲突重试） ----------------

    /**
     * 写入文件：带 sha 乐观锁；409 冲突时重读最新 sha 重试（最多 5 次）。
     * 返回写入后的新 sha。
     */
    suspend fun ghWriteText(path: String = CONFIG_PATH, content: String, sha: String, message: String): String {
        if (token.isBlank()) throw IllegalStateException("请先在「系统设置」中配置 GitHub Token")
        val cfg = config
        var currentSha = sha
        for (attempt in 0 until 5) {
            val url = "https://api.github.com/repos/${cfg.owner}/${cfg.repo}/contents/$path"
            val payload = JSONObject()
                .put("message", message)
                .put("content", Base64.encodeToString(content.toByteArray(Charsets.UTF_8), Base64.NO_WRAP))
                .apply { if (currentSha.isNotEmpty()) put("sha", currentSha) }
            val req = Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("Authorization", "Bearer ${token}")
                .header("Content-Type", "application/json")
                .put(payload.toString().toRequestBody())
                .build()
            val (respCode, respBody) = runCatching {
                client.newCall(req).execute().use {
                    it.code to it.body?.string().orEmpty()
                }
            }.getOrElse { throw IllegalStateException("写入失败: $it") }

            if (respCode == 409) {
                // 冲突：重读最新内容再写
                val (_, latestSha) = ghReadText(path)
                currentSha = latestSha
                continue
            }
            if (respCode !in 200..299) {
                throw IllegalStateException("写入失败 HTTP $respCode: ${respBody.take(200)}")
            }
            return JSONObject(respBody).optJSONObject("content")?.optString("sha")
                ?: JSONObject(respBody).optString("sha")
        }
        throw IllegalStateException("写入冲突重试次数超限")
    }

    // ---------------- 上传本地文件 ----------------

    /**
     * 上传本地文件（APK / ZIP / MD / 图片 / 视频等）到仓库 dist/ 目录，
     * 返回 raw 直链；自动刷新 jsDelivr CDN。
     */
    suspend fun uploadBinaryFile(
        uri: Uri,
        customFileName: String? = null,
        subFolder: String = "dist/uploads",
    ): String = withContext(Dispatchers.IO) {
        if (token.isBlank()) throw IllegalStateException("请先在「系统设置」中配置 GitHub Token")
        val cfg = config
        // 读取文件字节
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalStateException("读取本地文件失败")
        val originalName = customFileName
            ?: runCatching {
                context.contentResolver.query(uri, null, null, null, null)?.use { c ->
                    if (c.moveToFirst()) c.getColumnIndex("_display_name").takeIf { it >= 0 }?.let { c.getString(it) }
                }
            }.getOrNull()
            ?: "upload_${System.currentTimeMillis()}.bin"
        val safeName = Regex("[\\\\/:*?\"<>|\\s]+").replace(originalName, "_").trim('_')
        val path = "$subFolder/$safeName"
        // 已存在则带 sha 覆盖
        var existingSha = ""
        runCatching {
            val (_, sha) = ghReadText(path)
            existingSha = sha
        }
        val payload = JSONObject()
            .put("message", "upload: $safeName")
            .put("content", Base64.encodeToString(bytes, Base64.NO_WRAP))
            .apply { if (existingSha.isNotEmpty()) put("sha", existingSha) }
        val url = "https://api.github.com/repos/${cfg.owner}/${cfg.repo}/contents/$path"
        val req = Request.Builder()
            .url(url)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .header("Authorization", "Bearer ${token}")
            .header("Content-Type", "application/json")
            .put(payload.toString().toRequestBody())
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) {
                val t = resp.body?.string().orEmpty()
                throw IllegalStateException("上传失败 HTTP ${resp.code}: ${t.take(200)}")
            }
        }
        val rawUrl = "https://raw.githubusercontent.com/${cfg.owner}/${cfg.repo}/${cfg.branch}/$path"
        runCatching { purgeCdn(path) }
        rawUrl
    }

    // ---------------- CDN 刷新 ----------------

    /** 刷新 jsDelivr CDN 缓存（写入后调用，本体秒级生效） */
    suspend fun purgeCdn(path: String = CONFIG_PATH): Boolean = withContext(Dispatchers.IO) {
        val cfg = config
        val url = "https://purge.jsdelivr.net/gh/${cfg.owner}/${cfg.repo}@${cfg.branch}/$path"
        try {
            client.newCall(Request.Builder().url(url).build()).execute().use { it.isSuccessful }
        } catch (_: Exception) {
            false
        }
    }

    // ---------------- 私有工具 ----------------

    private fun newRequest(url: String): Request {
        val builder = Request.Builder()
            .url(url)
            .header("Accept", "application/vnd.github+json")
            .header("X-GitHub-Api-Version", "2022-11-28")
        if (token.isNotBlank()) builder.header("Authorization", "Bearer $token")
        return builder.build()
    }

    private fun String.toRequestBody() =
        okhttp3.RequestBody.create(this.toMediaType(), this)
}
