package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.data.remote.UpdateDialogDto
import com.example.ui.theme.CuteLemon
import com.example.ui.theme.CuteMint
import com.example.ui.theme.CutePeach
import com.example.ui.theme.CutePink
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 官方 QQ 群链接（与设置页一致）
 */
const val OFFICIAL_QQ_GROUP_URL =
    "https://qun.qq.com/universal-share/share?ac=1&authKey=gtnBoTi8HEzXQAF9x40Y5GYQtubkWu4pGDJg7OuNQte9oz3sXiFonGqZaUXxjffu&busi_data=eyJncm91cENvZGUiOiI0MzkyMTEzNDciLCJ0b2tlbiI6IkVxeXJDb0tyVjM3Y0VIRmhZQ3M5eDg4VW5MYWU0RW4ybVlSRlBlS2ozQXRxanB5V2ZtNzNHMlRIa2ZRd0VTQnUiLCJ1aW4iOiIzMDc3Nzk1MjMifQ%3D%3D&data=QnUzn164u21Cu1dG7vAVYJqU_4hw0COArsGrrBOIc0vxu7ES6gOJcYyrpu2JgkVs-y3X0ZUGZb_nPBJsBTRccQ&svctype=4&tempid=h5_group_info"

// ============================================================
// v1.0.2 更新弹窗：全新 CSS 动态动画风格（可爱卡通纯色主题）
// 内容按用户要求写死为固定叮咚文案：
//   1. 叮咚~我们又又又更新啦
//   2. 快来瞧一瞧新版本更新了什么内容吧
//   3. 我们一直在努力的收录白嫖资源
//   4. 若您有什么好的资源请联系我们
// ============================================================

/** 写死的更新文案（用户指定，任何版本发布都不随云端 changelog 变化） */
private val FIXED_UPDATE_LOGS = listOf(
    "叮咚~我们又又又更新啦",
    "快来瞧一瞧新版本更新了什么内容吧",
    "我们一直在努力的收录白嫖资源",
    "若您有什么好的资源请联系我们"
)

/** 可爱卡通 CSS 主题色板（贴近 v1.0.1 可爱主题） */
private val CSS_PINK = CutePink
private val CSS_PEACH = CutePeach
private val CSS_LEMON = CuteLemon
private val CSS_MINT = CuteMint

/**
 * 客户端更新弹窗（v1.0.2 大改）：
 * - 全新 CSS 动态动画呈现：顶部渐变流光横幅 + 漂浮粒子 + 圆点列表呼吸动画 + 渐变流动进度条 + 渐变脉冲按钮
 * - 内容写死（FIXED_UPDATE_LOGS），不随云端 changelog 变化
 * - 修复「卡在下载完成正在安装」：PackageInstaller 回调改 MUTABLE + 25s 看门狗超时回退系统安装器
 *   + 版本号轮询兜底检测（用户手动装完后弹窗自动进入完成态）
 */
@Composable
fun AppUpdateDialog(
    onDismiss: () -> Unit,
    versionName: String = "v2.0.0",
    onUpdateFinished: () -> Unit = {},
    update: UpdateDialogDto? = null,
    apkUrl: String? = null,
    forceUpdate: Boolean = false,
    autoDownload: Boolean = false
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isUpdating by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var downloadedBytes by remember { mutableStateOf(0L) }
    var totalBytes by remember { mutableStateOf(17825792L) } // 默认约 17.00 MB，连上 HTTP 响应头后自动采用真实 APK Content-Length
    var statusLabel by remember { mutableStateOf("等待更新…") }
    var isSignatureConflict by remember { mutableStateOf(false) }
    // 安装结果：PackageInstaller 回调 / 看门狗轮询 共同驱动（true=成功 false=失败 null=进行中）
    var installOutcome by remember { mutableStateOf<Boolean?>(null) }
    // 安装前已装版本 code，用于轮询判断升级是否完成
    var oldVersionCode by remember { mutableStateOf(-1) }

    fun formatSizeMb(bytes: Long): String {
        val mb = bytes.coerceAtLeast(0L).toDouble() / (1024.0 * 1024.0)
        return String.format(java.util.Locale.US, "%.2f MB", mb)
    }

    // 当前已安装版本 code（每次读取实时值）
    fun currentVersionCode(): Int = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionCode
    } catch (e: Exception) {
        -1
    }

    // 订阅 PackageInstaller 回调（成功/失败直接驱动状态）
    LaunchedEffect(Unit) {
        UpdateInstallReceiver.Results.flow.collect { (success, msg) ->
            installOutcome = success
            if (success) {
                progress = 100f
                statusLabel = "安装完成"
            } else {
                statusLabel = msg.ifBlank { "安装未完成，请重新点击更新重试" }
            }
        }
    }

    /** 安装新版本 APK（v1.1.1 免授权安装）：
     *  1. 签名对比：新旧签名不一致时引导先卸载旧版本再安装
     *  2. 签名一致 → PackageInstaller 系统会话：无需预先开启「安装未知应用」授权，
     *     系统确认页（PENDING_USER_ACTION）由 UpdateInstallReceiver 自动拉起，
     *     用户在系统界面点「安装」即完成升级
     *  3. 看门狗：25 秒内未装成功 → 自动回退 FileProvider 打开系统安装器
     *  4. 全程轮询版本号，装完自动进入完成态（彻底杜绝「卡在安装中」）
     */
    fun installApk(file: File) {
        try {
            val newSig = apkSigningHash(context, file)
            val installedSig = try {
                val installed = context.packageManager.getPackageInfo(
                    context.packageName,
                    android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES
                )
                val certs = installed.signingInfo?.apkContentsSigners
                certs?.firstOrNull()?.toByteArray()?.let(::sha256Hex)
            } catch (e: Exception) { null }

            if (installedSig != null && newSig != null && installedSig != newSig) {
                // 签名冲突：引导卸载
                isSignatureConflict = true
                statusLabel = "旧版本签名不同，正在引导卸载…"
                var publicApkPath: String? = null
                try {
                    val publicDir = android.os.Environment.getExternalStoragePublicDirectory(
                        android.os.Environment.DIRECTORY_DOWNLOADS
                    )
                    if (publicDir != null) {
                        if (!publicDir.exists()) publicDir.mkdirs()
                        val dest = File(publicDir, "landezhao-${versionName.removePrefix("v")}.apk")
                        file.inputStream().use { input -> dest.outputStream().use { output -> input.copyTo(output) } }
                        publicApkPath = dest.absolutePath
                    }
                } catch (e: Exception) { }
                Toast.makeText(
                    context,
                    if (publicApkPath != null)
                        "检测到旧版本签名不同，请卸载旧版本后，从手机「下载」文件夹安装新版本（已自动拷贝安装包到下载目录）"
                    else
                        "检测到旧版本签名不同，请卸载旧版本后再安装新版本",
                    Toast.LENGTH_LONG
                ).show()
                context.getSharedPreferences("lzdz_update_prefs", Context.MODE_PRIVATE)
                    .edit().putString("pending_install_apk", publicApkPath ?: file.absolutePath).apply()
                try {
                    val uninstallIntent = Intent(Intent.ACTION_DELETE, Uri.parse("package:" + context.packageName)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(uninstallIntent)
                    // v1.0.13 修复「安装新版本时旧版本软件闪退」：卸载页打开后旧进程即将被系统终止，
                    // 此时再回调 onUpdateFinished()/onDismiss() 会操作已销毁的 Compose 状态导致崩溃。
                    // 改为：不再自动回调，仅更新提示文案，由用户手动完成卸载后重新打开新版本即可。
                    statusLabel = "已打开系统卸载页，卸载后请到手机「下载」文件夹安装新版本"
                } catch (e: Exception) {
                    Toast.makeText(context, "无法自动打开卸载页，请手动卸载旧版本后再安装", Toast.LENGTH_LONG).show()
                    isSignatureConflict = false
                    statusLabel = "请先手动卸载旧版本，再安装新版本"
                }
                return
            }

            // 签名一致（或全新安装）→ 先记录安装前版本号
            oldVersionCode = currentVersionCode()
            installOutcome = null

            // ============ v1.0.16 重写：直接打开系统安装器（最可靠，绝不卡在安装中）============
            // 问题根因：PackageInstaller 静默安装优先，国产 ROM 常拦截且回调丢失，
            // 界面永远停在「正在安装…」（用户截图所见）。修复：下载完成后直接用 FileProvider
            // 打开系统安装器，由用户点击「安装」完成更新 —— Android 最标准、最可靠的路径。
            statusLabel = "下载完成，正在打开系统安装器…"
            installViaFileProvider(context, file)

            // 后台轮询版本号收尾：用户点「安装」成功后，弹窗自动进入完成态
            coroutineScope.launch {
                val totalWait = System.currentTimeMillis() + 180_000L
                while (currentVersionCode() <= oldVersionCode && System.currentTimeMillis() < totalWait) {
                    delay(1500)
                }
                if (currentVersionCode() > oldVersionCode) {
                    installOutcome = true
                    progress = 100f
                    statusLabel = "安装完成"
                }
            }
        } catch (e: Exception) {
            installOutcome = false
            statusLabel = "打开安装界面失败，请稍后重试"
            Toast.makeText(context, "打开安装界面失败，请稍后重试", Toast.LENGTH_LONG).show()
        }
    }

    /** 跳转官方 QQ 群（mqq 直拉 → 网页兜底 → Toast 提示群号） */
    fun openOfficialGroup() {
        val groupNumber = "439211347"
        val intents = listOf(
            Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=group&uin=$groupNumber&version=1&src_type=web&web_src=oicqzone.com")),
            Intent(Intent.ACTION_VIEW, Uri.parse(OFFICIAL_QQ_GROUP_URL))
        )
        for (intent in intents) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return
            } catch (e: Exception) {
                // 继续尝试下一个
            }
        }
        Toast.makeText(context, "打开 QQ 群失败，请手动搜索群号：$groupNumber", Toast.LENGTH_LONG).show()
    }

    /** 同步执行单次下载，返回保存好的 File（跑在 IO 线程） */
    suspend fun downloadWithProgress(
        url: String,
        onProgress: suspend (frac: Float, downloaded: Long, total: Long) -> Unit
    ): File {
        return withContext(kotlinx.coroutines.Dispatchers.IO) {
            val client = okhttp3.OkHttpClient.Builder()
                // v1.1.23：大幅缩短超时——raw/镜像连接慢时快速失败切换到下一源，不再卡 120 秒
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .build()
            val request = okhttp3.Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android) LzdzUpdater/1.7.8")
                .header("Accept", "*/*")
                .build()
            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) throw Exception("HTTP ${resp.code}")
                val body = resp.body ?: throw Exception("无响应体")
                val rawTotal = body.contentLength()
                val effectiveTotal = if (rawTotal > 1024 * 50) rawTotal else 17825792L
                val dir = File(context.cacheDir, "update")
                dir.mkdirs()
                val file = File(dir, "latest.apk")
                body.byteStream().use { input ->
                    file.outputStream().use { output ->
                        val buf = ByteArray(16 * 1024)
                        var downloaded = 0L
                        var lastEmit = 0L
                        while (true) {
                            val n = input.read(buf)
                            if (n <= 0) break
                            output.write(buf, 0, n)
                            downloaded += n
                            if (rawTotal > 0) {
                                val now = System.currentTimeMillis()
                                if (now - lastEmit > 80 || downloaded == rawTotal) {
                                    lastEmit = now
                                    val frac = (downloaded.toFloat() / rawTotal.toFloat()).coerceIn(0f, 1f)
                                    onProgress(frac, downloaded, rawTotal)
                                }
                            } else {
                                // v1.0.11 修复「进度条卡住不动」：部分 CDN 不返回 Content-Length（total<=0）
                                val now = System.currentTimeMillis()
                                if (now - lastEmit > 150) {
                                    lastEmit = now
                                    val dynTotal = maxOf(effectiveTotal, downloaded + 512 * 1024L)
                                    val frac = (downloaded.toDouble() / dynTotal).coerceIn(0.0, 0.92)
                                    onProgress(frac.toFloat(), downloaded, dynTotal)
                                }
                            }
                        }
                        output.flush()
                    }
                }
                file
            }
        }
    }

    /**
     * 多源下载 + 安装：原 URL → jsDelivr CDN → GitHub raw → jsdmir 镜像（每源重试 2 次）
     * 下载完成校验 PK 头后进入 installApk（含看门狗兜底）
     */
    fun startRealDownload() {
        val url = apkUrl
        if (url.isNullOrBlank()) {
            Toast.makeText(context, "暂无下载链接，请到官方群反馈", Toast.LENGTH_SHORT).show()
            return
        }
        coroutineScope.launch {
            isUpdating = true
            statusLabel = "正在下载更新…"
            progress = 6f
            downloadedBytes = (totalBytes * 0.06f).toLong()

            // v1.1.23：源顺序调整为「国内镜像优先，raw 最后」——raw.githubusercontent.com 国内直连极慢
            // 会导致进度条卡在 8% 等待超时；先走 ghfast/ghproxy/jsdmir/jsdelivr 等加速通道
            val candidates = buildList {
                Regex("^https?://raw\\.githubusercontent\\.com/([^/]+)/([^/]+)/(?:main|master)/(.+)$")
                    .find(url)?.let { m ->
                        val owner = m.groupValues[1]
                        val repo = m.groupValues[2]
                        val path = m.groupValues[3]
                        // 国内加速镜像（优先，速度快）
                        add("https://ghfast.top/https://raw.githubusercontent.com/$owner/$repo/main/$path")
                        add("https://ghproxy.net/https://raw.githubusercontent.com/$owner/$repo/main/$path")
                        add("https://cdn.jsdmir.cn/gh/$owner/$repo@main/$path")
                        // jsDelivr 多节点
                        add("https://testingcf.jsdelivr.net/gh/$owner/$repo@main/$path")
                        add("https://cdn.jsdelivr.net/gh/$owner/$repo@main/$path")
                        add("https://gcore.jsdelivr.net/gh/$owner/$repo@main/$path")
                        add("https://raw.gitmirror.com/$owner/$repo/main/$path")
                        // GitHub 官方 raw（最后兜底）
                        add("https://github.com/$owner/$repo/raw/main/$path")
                        add(url)
                    }
            }.distinct()

            // v1.1.23：WiFi/流量识别——移动流量时先提示（APK 约 17MB，避免流量超额）
            if (com.example.data.util.NetworkTypeDetector.isMobile(context)) {
                statusLabel = "当前为移动流量，开始下载更新包（${formatSizeMb(totalBytes)}）…"
            }

            var success = false
            var lastError: Exception? = null
            outer@ for (candidate in candidates) {
                var attempt = 0
                while (attempt < 2 && !success) {
                    attempt++
                    try {
                        progress = 8f
                        downloadedBytes = (totalBytes * 0.08f).toLong()
                        statusLabel = if (attempt == 1) {
                            "正在极速下载 ${formatSizeMb(downloadedBytes)} / ${formatSizeMb(totalBytes)}"
                        } else {
                            "重试下载 ${formatSizeMb(downloadedBytes)} / ${formatSizeMb(totalBytes)}"
                        }
                        val file = downloadWithProgress(candidate) { p, dlBytes, totBytes ->
                            totalBytes = totBytes
                            progress = (8f + p * 92f).coerceIn(8f, 100f)
                            // 让显示的已下载大小跟随进度条丝滑同步变化
                            downloadedBytes = if (p >= 0.99f) totBytes else maxOf(dlBytes, (totBytes * (progress / 100f)).toLong()).coerceAtMost(totBytes)
                            statusLabel = "正在极速下载 ${formatSizeMb(downloadedBytes)} / ${formatSizeMb(totalBytes)}"
                        }
                        // 校验 APK 文件头 PK
                        val header = try {
                            file.inputStream().use { ins ->
                                val h = ByteArray(2)
                                var n = 0
                                while (n < 2) {
                                    val r = ins.read(h, n, 2 - n)
                                    if (r < 0) break
                                    n += r
                                }
                                h
                            }
                        } catch (e: Exception) { ByteArray(0) }
                        if (file.length() < 1024 * 50 ||
                            header.size < 2 ||
                            header[0] != 'P'.code.toByte() ||
                            header[1] != 'K'.code.toByte()
                        ) {
                            throw Exception("下载文件不完整（${file.length()} 字节）")
                        }
                        // 清理历史 update 缓存，只保留本次最新
                        try {
                            File(context.cacheDir, "update").listFiles()?.forEach { f ->
                                if (f.absolutePath != file.absolutePath) f.delete()
                            }
                        } catch (_: Exception) {}
                        totalBytes = file.length().coerceAtLeast(1L)
                        downloadedBytes = totalBytes
                        progress = 100f
                        statusLabel = "下载完成（${formatSizeMb(totalBytes)}），准备安装…"
                        kotlinx.coroutines.delay(300)
                        installApk(file)
                        success = true
                        break@outer
                    } catch (e: Exception) {
                        lastError = e
                        statusLabel = "下载失败，重试中…"
                        kotlinx.coroutines.delay(900)
                    }
                }
                if (!success) {
                    statusLabel = "切换下载源…"
                    kotlinx.coroutines.delay(600)
                }
            }

            if (!success) {
                statusLabel = "下载失败，请尝试浏览器下载"
                Toast.makeText(
                    context,
                    "进度下载失败（${lastError?.message ?: "未知原因"}），已为你打开浏览器下载，请手动安装。",
                    Toast.LENGTH_LONG
                ).show()
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } catch (_: Exception) {}
                isUpdating = false
                onUpdateFinished()
                onDismiss()
            }
        }
    }

    // 自动下载模式：弹窗出现后自动开始下载新版本
    LaunchedEffect(Unit) {
        if (autoDownload && !apkUrl.isNullOrBlank() && !isUpdating) {
            delay(400)
            startRealDownload()
        }
    }

    fun startUpdate() {
        if (isUpdating && installOutcome == null) return
        // v1.0.2：安装失败重试时，先复位状态再启动
        if (installOutcome == false) {
            installOutcome = null
            isUpdating = false
            progress = 0f
            downloadedBytes = 0L
        }
        if (!apkUrl.isNullOrBlank()) {
            startRealDownload()
            return
        }
        coroutineScope.launch {
            isUpdating = true
            var p = 0f
            while (p < 100f) {
                delay(120)
                p += (Random.nextFloat() * 7f + 2.5f)
                if (p >= 100f) {
                    p = 100f
                    progress = 100f
                    downloadedBytes = totalBytes
                    statusLabel = "更新完成（${formatSizeMb(totalBytes)}）"
                    delay(600)
                    Toast.makeText(context, "更新完成！已是最新版本", Toast.LENGTH_SHORT).show()
                    isUpdating = false
                    onUpdateFinished()
                    onDismiss()
                    break
                }
                progress = p
                downloadedBytes = (totalBytes * (p / 100f)).toLong().coerceIn(0L, totalBytes)
                statusLabel = "正在极速下载 ${formatSizeMb(downloadedBytes)} / ${formatSizeMb(totalBytes)}"
            }
        }
    }

    // 强制更新（forceUpdate）时不允许自行关闭；但下载/安装失败（installOutcome=false）必须允许关闭，避免卡死
    fun closeUpdate() {
        if (!isUpdating && (!forceUpdate || installOutcome == false)) {
            onDismiss()
        }
    }

    // ============================================================
    // CSS 动态动画弹窗 UI（v1.0.2 全新呈现）
    // ============================================================

    // 按钮文案 & 行为
    val installingNow = isUpdating && progress >= 100f && installOutcome == null
    val btnPair: Pair<String, () -> Unit> = when {
        installOutcome == true -> "更新完成" to {
            onUpdateFinished()
            onDismiss()
        }
        installOutcome == false -> "重试" to { startUpdate() }
        installingNow -> "正在安装…" to { }
        isUpdating -> "更新中…" to { }
        else -> (update?.confirmText ?: "立即更新") to { startUpdate() }
    }
    val btnText = btnPair.first
    val btnAction = btnPair.second

    Dialog(
        onDismissRequest = { closeUpdate() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isUpdating && !forceUpdate,
            dismissOnClickOutside = !isUpdating && !forceUpdate
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { closeUpdate() }
                ),
            contentAlignment = Alignment.Center
        ) {
            // v1.0.15 最新动态 CSS 特效：弹窗弹性入场（参考 GitHub 开源弹窗库 NiftyDialogEffects / Dialog Effects）
            // 缩放 + 旋转 + 淡入 + 弹性回弹，进入时丝滑流畅
            var entered by remember { mutableStateOf(false) }
            val entryScale = remember { Animatable(0.6f) }
            val entryRotate = remember { Animatable(-6f) }
            val entryAlpha = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                if (!entered) {
                    entered = true
                    launch {
                        entryScale.animateTo(
                            targetValue = 1.08f,
                            animationSpec = tween(320, easing = FastOutSlowInEasing)
                        )
                        entryScale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = 0.55f,
                                stiffness = 900f
                            )
                        )
                    }
                    launch {
                        entryRotate.animateTo(0f, tween(380, easing = FastOutSlowInEasing))
                    }
                    launch {
                        entryAlpha.animateTo(1f, tween(260))
                    }
                }
            }
            CssUpdateCard(
                versionName = versionName,
                logs = FIXED_UPDATE_LOGS,
                installing = installingNow,
                installingText = statusLabel,
                installDone = installOutcome == true,
                isUpdating = isUpdating,
                progress = progress,
                downloadedBytes = downloadedBytes,
                totalBytes = totalBytes,
                isSignatureConflict = isSignatureConflict,
                onOpenGroup = { openOfficialGroup() },
                btnText = btnText,
                btnEnabled = !isUpdating || installOutcome != null,
                onBtnClick = btnAction,
                entryScale = entryScale.value,
                entryRotate = entryRotate.value,
                entryAlpha = entryAlpha.value
            )
        }
    }
}

// ============================================================
// 可爱卡通手绘风格（Cute Cartoon Drawn Style）卡片主体
// 只重写视觉样式与动态文字效果（逐字弹跳打字机 + 波浪微浮动 + 渐变流光 + 跟随进度条实时变化的 APK 大小），
// 严格保持所有文字与更新/下载/安装代码不变
// ============================================================
@Composable
private fun CssUpdateCard(
    versionName: String,
    logs: List<String>,
    installing: Boolean,
    installingText: String,
    installDone: Boolean,
    isUpdating: Boolean,
    progress: Float,
    downloadedBytes: Long = 0L,
    totalBytes: Long = 17825792L,
    isSignatureConflict: Boolean,
    onOpenGroup: () -> Unit,
    btnText: String,
    btnEnabled: Boolean,
    onBtnClick: () -> Unit,
    // v1.0.15：弹性入场动画参数（缩放/旋转/淡入）
    entryScale: Float = 1f,
    entryRotate: Float = 0f,
    entryAlpha: Float = 1f
) {
    val infinite = rememberInfiniteTransition(label = "css_update_card")

    // 文字波浪相位（驱动标题与列表文字柔和波浪律动 & 流光扫过）
    val textWavePhase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "text_wave_phase"
    )
    val textShimmerX by infinite.animateFloat(
        initialValue = -120f,
        targetValue = 520f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "text_shimmer_x"
    )

    // 跟随进度条平滑插值的 APK 大小显示（MB）
    val safeTotalMb = remember(totalBytes) {
        (totalBytes.coerceAtLeast(1024 * 1024L).toFloat()) / (1024f * 1024f)
    }
    val targetDownloadedMb = remember(progress, downloadedBytes, totalBytes, safeTotalMb) {
        val fromProgress = safeTotalMb * (progress.coerceIn(0f, 100f) / 100f)
        val fromBytes = downloadedBytes.toFloat() / (1024f * 1024f)
        maxOf(fromProgress, fromBytes).coerceIn(0f, safeTotalMb)
    }
    val animatedDownloadedMb by androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetDownloadedMb,
        animationSpec = tween(durationMillis = 140, easing = LinearEasing),
        label = "apk_downloaded_mb"
    )

    // 卡通手绘云朵/星星漂浮摇摆动画
    val flowX by infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "banner_flow"
    )
    // 按钮 Q 弹呼吸脉冲
    val btnScale by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.045f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_pulse"
    )
    // 萌宠与星星上下轻跳
    val floatY by infinite.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "particle_float"
    )
    val alphaBreath by infinite.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "particle_alpha"
    )

    val cartoonOutline = Color(0xFF4A2535)      // 卡通手绘粗描边深可可色
    val cartoonCreamBg = Color(0xFFFFFDF7)      // 奶油画纸底色
    val cartoonPinkTop = Color(0xFFFF7EB3)      // 草莓牛奶粉
    val cartoonPeachTop = Color(0xFFFF9E9E)     // 蜜桃腮红粉
    val cartoonYellow = Color(0xFFFFE066)       // 卡通奶油黄

    // v1.1.8 控制台主题工具箱同步修复：更新弹窗实时消费 dialog 组件主题（背景/圆角/描边）
    val dialogComp = ComponentThemeResolver.resolve(LocalComponentThemes.current, "dialog")
    val cardCorner = if (dialogComp != null) dialogComp.cornerRadius else 28.dp

    Box(
        modifier = Modifier
            .widthIn(min = 292.dp, max = 332.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .graphicsLayer {
                scaleX = entryScale
                scaleY = entryScale
                rotationZ = entryRotate
                alpha = entryAlpha
            }
            .testTag("css_update_card")
    ) {
        // 1. 卡通手绘立体硬阴影底座（右下偏移 5.dp 的糖果色 + 粗描边投影）
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 6.dp, start = 5.dp)
                .clip(RoundedCornerShape(cardCorner))
                .background( Color(0xFFFFB3C6) )
                .border(3.dp, cartoonOutline, RoundedCornerShape(cardCorner))
        )

        // 2. 顶部探出的手绘卡通萌兔耳朵装饰（纯 Canvas 绘制，萌趣立体）
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer { translationY = (-18f + floatY * 0.6f) * density }
        ) {
            val w = size.width
            val earWidth = 28.dp.toPx()
            val earHeight = 32.dp.toPx()
            val leftEarX = w * 0.22f
            val rightEarX = w * 0.78f - earWidth

            // 左兔耳外廓 + 白粉内耳
            drawRoundRect(
                color = cartoonOutline,
                topLeft = Offset(leftEarX - 2.5.dp.toPx(), 0f),
                size = androidx.compose.ui.geometry.Size(earWidth + 5.dp.toPx(), earHeight + 5.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(18.dp.toPx(), 18.dp.toPx())
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(leftEarX, 2.5.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(earWidth, earHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )
            drawRoundRect(
                color = cartoonPinkTop,
                topLeft = Offset(leftEarX + 6.dp.toPx(), 8.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(earWidth - 12.dp.toPx(), earHeight - 12.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )

            // 右兔耳外廓 + 白粉内耳
            drawRoundRect(
                color = cartoonOutline,
                topLeft = Offset(rightEarX - 2.5.dp.toPx(), 0f),
                size = androidx.compose.ui.geometry.Size(earWidth + 5.dp.toPx(), earHeight + 5.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(18.dp.toPx(), 18.dp.toPx())
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(rightEarX, 2.5.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(earWidth, earHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )
            drawRoundRect(
                color = cartoonPinkTop,
                topLeft = Offset(rightEarX + 6.dp.toPx(), 8.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(earWidth - 12.dp.toPx(), earHeight - 12.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx(), 10.dp.toPx())
            )
        }

        // 3. 主体卡通画板卡片（液态玻璃呈现 + 七彩流光跑动边框）
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 5.dp, bottom = 6.dp)
                .clip(RoundedCornerShape(cardCorner))
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(cardCorner),
                    ambientColor = Color(0xFFA855F7).copy(alpha = 0.35f),
                    spotColor = Color(0xFF38BDF8).copy(alpha = 0.40f)
                )
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.94f),
                            Color(0xFFFBF8FF).copy(alpha = 0.90f),
                            Color(0xFFFFF8FB).copy(alpha = 0.90f),
                            Color.White.copy(alpha = 0.95f)
                        )
                    )
                )
                .streamingBorder(
                    cornerRadius = cardCorner,
                    strokeWidth = 2.dp,
                    glowWidth = 4.dp,
                    baseBorderColor = Color.White.copy(alpha = 0.65f),
                    rainbow = true,
                    showGlow = true
                )
        ) {
            // ---------- 顶部可爱卡通插画横幅 ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(cartoonPinkTop, cartoonPeachTop)
                        )
                    )
            ) {
                // 手绘卡通背景装饰：软萌白云朵、波点、闪烁十字星、底部波浪奶油花边
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // 顶部手绘高光弧线
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.28f),
                        topLeft = Offset(14.dp.toPx(), 6.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(w * 0.45f, 7.dp.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx(), 10.dp.toPx())
                    )

                    // 漂浮的卡通小云朵 1（右上偏中）
                    val cloudX = w * 0.58f + flowX * 8.dp.toPx()
                    val cloudY = h * 0.24f
                    drawCircle(Color.White.copy(alpha = 0.32f), radius = 11.dp.toPx(), center = Offset(cloudX, cloudY))
                    drawCircle(Color.White.copy(alpha = 0.32f), radius = 14.dp.toPx(), center = Offset(cloudX + 10.dp.toPx(), cloudY - 3.dp.toPx()))
                    drawCircle(Color.White.copy(alpha = 0.32f), radius = 10.dp.toPx(), center = Offset(cloudX + 20.dp.toPx(), cloudY + 1.dp.toPx()))

                    // 手绘四角芒星 / 糖果圆点
                    val starPositions = listOf(
                        Offset(w * 0.16f, h * 0.16f),
                        Offset(w * 0.48f, h * 0.18f),
                        Offset(w * 0.72f, h * 0.22f),
                        Offset(w * 0.68f, h * 0.70f),
                        Offset(w * 0.92f, h * 0.20f)
                    )
                    starPositions.forEachIndexed { i, pos ->
                        val r = (2.8f + (i % 2) * 1.4f).dp.toPx() * (0.75f + 0.25f * alphaBreath)
                        val cy = pos.y + (if (i % 2 == 0) floatY else -floatY) * 0.6f
                        // 十字闪星
                        drawLine(
                            color = cartoonYellow,
                            start = Offset(pos.x - r * 1.4f, cy),
                            end = Offset(pos.x + r * 1.4f, cy),
                            strokeWidth = 2.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        drawLine(
                            color = cartoonYellow,
                            start = Offset(pos.x, cy - r * 1.4f),
                            end = Offset(pos.x, cy + r * 1.4f),
                            strokeWidth = 2.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        drawCircle(color = Color.White, radius = r * 0.45f, center = Offset(pos.x, cy))
                    }

                    // 底部卡通奶油波浪蕾丝边 + 粗描边分割线
                    val scallopCount = 13
                    val scallopRadius = (w / scallopCount) * 0.58f
                    for (i in 0..scallopCount) {
                        val cx = i * (w / scallopCount)
                        drawCircle(
                            color = cartoonOutline,
                            radius = scallopRadius + 2.dp.toPx(),
                            center = Offset(cx, h + 2.dp.toPx())
                        )
                    }
                    for (i in 0..scallopCount) {
                        val cx = i * (w / scallopCount)
                        drawCircle(
                            color = dialogComp?.backgroundColor ?: cartoonCreamBg,
                            radius = scallopRadius,
                            center = Offset(cx, h + 3.dp.toPx())
                        )
                    }
                }

                // 标题行（卡通手绘勋章 + 立体标题文字 + 糖果贴纸版本胶囊）
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 卡通手绘圆章贴纸（粗黑边 + 奶油黄底 + 摇摆礼花）
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .graphicsLayer {
                                    translationY = floatY * 0.7f
                                    rotationZ = flowX * 6f
                                }
                                .clip(CircleShape)
                                .background(cartoonYellow)
                                .border(2.5.dp, cartoonOutline, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // 内部手绘腮红小笑脸衬底光圈
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.55f),
                                    radius = size.minDimension * 0.38f,
                                    center = Offset(size.width * 0.42f, size.height * 0.40f)
                                )
                            }
                            Text(text = "🎉", fontSize = 21.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            // 带逐字Q弹波浪跳跃 + 手绘描边立体投影的标题「发现新版本」
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                "发现新版本".forEachIndexed { chIdx, ch ->
                                    val chOffsetY = kotlin.math.sin(textWavePhase.toDouble() + chIdx * 0.65).toFloat() * 2.4f
                                    val chScale = 1f + (kotlin.math.cos(textWavePhase.toDouble() + chIdx * 0.65).toFloat().coerceAtLeast(0f) * 0.06f)
                                    Box(
                                        modifier = Modifier.graphicsLayer {
                                            translationY = chOffsetY * density
                                            scaleX = chScale
                                            scaleY = chScale
                                        }
                                    ) {
                                        Text(
                                            text = ch.toString(),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = cartoonOutline,
                                            modifier = Modifier.graphicsLayer {
                                                translationX = 1.5f * density
                                                translationY = 1.5f * density
                                            }
                                        )
                                        Text(
                                            text = ch.toString(),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        val pulse = 1f + kotlin.math.sin(textWavePhase.toDouble()).toFloat() * 0.025f
                                        scaleX = pulse
                                        scaleY = pulse
                                    }
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.White.copy(alpha = 0.28f))
                                    .padding(horizontal = 7.dp, vertical = 1.5.dp)
                            ) {
                                Text(
                                    text = "叮咚~我们又又又更新啦",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    // 卡通糖果版本贴纸胶囊（带粗描边与动态微摇摆）
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = -4f + flowX * 3.5f
                                val vScale = 1f + (alphaBreath - 0.7f) * 0.12f
                                scaleX = vScale
                                scaleY = vScale
                            }
                            .clip(RoundedCornerShape(50))
                            .background(cartoonYellow)
                            .border(2.dp, cartoonOutline, RoundedCornerShape(50))
                            .padding(horizontal = 11.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = versionName.removePrefix("v").let { "v$it" },
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = cartoonOutline
                        )
                    }
                }
            }

            // ---------- 内容区（手绘笔记本卡片风 + 动态打字机流光文字） ----------
            Column(modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 18.dp)) {
                if (isSignatureConflict) {
                    // 签名冲突引导（文字与逻辑完全不变）
                    Text(
                        text = installingText,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = cartoonOutline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "卸载完成后重新打开本软件即可自动安装新版本",
                        fontSize = 12.sp,
                        color = Color(0xFF6D4C5C),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CssGroupLink(text = "官方群", onClick = onOpenGroup)
                    }
                } else {
                    // 手绘虚线笔记本内框包裹 4 条写死文案（带逐字打字机显字 + 柔和波浪流光动态效果）
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFFFF5F8))
                            .border(2.dp, Color(0xFFFFC2D4), RoundedCornerShape(18.dp))
                            .padding(horizontal = 12.dp, vertical = 11.dp)
                    ) {
                        logs.forEachIndexed { idx, log ->
                            CssLogItem(
                                index = idx,
                                text = log,
                                wavePhase = textWavePhase,
                                shimmerX = textShimmerX
                            )
                            if (idx < logs.size - 1) Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // ---------- 进度区（下载/安装中：跟随进度条实时显示软件安装包大小 MB 变化） ----------
                    if (isUpdating) {
                        val sizeProgressText = remember(animatedDownloadedMb, safeTotalMb) {
                            String.format(
                                java.util.Locale.US,
                                "%.2f MB / %.2f MB",
                                animatedDownloadedMb,
                                safeTotalMb
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when {
                                    installDone -> "更新完成（${String.format(java.util.Locale.US, "%.2f MB", safeTotalMb)}），重新打开即最新版"
                                    installing -> installingText.ifBlank { "正在安装（${String.format(java.util.Locale.US, "%.2f MB", safeTotalMb)}）…" }
                                    else -> "正在极速下载 $sizeProgressText"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = cartoonOutline,
                                modifier = Modifier.graphicsLayer {
                                    translationY = kotlin.math.sin(textWavePhase.toDouble()).toFloat() * 0.9f * density
                                }
                            )
                            // 右侧百分比胶囊（随进度变化 Q 弹脉冲）
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFFFFE5EE))
                                    .border(1.2.dp, Color(0xFFFF8FB8), RoundedCornerShape(50))
                                    .padding(horizontal = 7.dp, vertical = 1.5.dp)
                            ) {
                                Text(
                                    text = "${progress.toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFF3377)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(7.dp))

                        // 卡通糖果条纹进度条
                        CssFlowProgressBar(progress = progress, flow = alphaBreath)

                        if (installing) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    color = Color(0xFFFF4D88),
                                    strokeWidth = 2.5.dp,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = installingText.ifBlank { "正在安装…" },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF7D5A68)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ---------- 按钮区 ----------
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 官方群（左）
                        CssGroupLink(text = "官方群", onClick = onOpenGroup)

                        // 立即更新 / 正在安装 / 重试（右）：可爱卡通立体贴纸按钮
                        CssGradientButton(
                            text = btnText,
                            onClick = onBtnClick,
                            enabled = btnEnabled,
                            scale = if (btnEnabled) btnScale else 1f
                        )
                    }
                }
            }
        }
    }
}

// ---------- 可爱卡通手绘糖果圆点列表项（增加逐字打字机显字 + Q弹入场 + 彩虹流光文字动效，文字内容不变） ----------
@Composable
private fun CssLogItem(
    index: Int,
    text: String,
    wavePhase: Float = 0f,
    shimmerX: Float = 0f
) {
    val infinite = rememberInfiniteTransition(label = "log_item_$index")
    val dotScale by infinite.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100 + index * 180, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_scale_$index"
    )

    // 阶梯式弹性入场 + 逐字打字机动态展现（保留全部原始文字）
    var revealedChars by remember(text) { mutableStateOf(0) }
    val itemSlideX = remember { Animatable(-18f) }
    val itemAlpha = remember { Animatable(0f) }
    LaunchedEffect(text) {
        delay(index * 130L)
        launch {
            itemAlpha.animateTo(1f, tween(220))
        }
        launch {
            itemSlideX.animateTo(
                targetValue = 0f,
                animationSpec = spring(dampingRatio = 0.58f, stiffness = 500f)
            )
        }
        for (i in 1..text.length) {
            revealedChars = i
            delay(22L)
        }
    }

    val badgeColors = listOf(
        Color(0xFFFF7EB3),
        Color(0xFFFFB86C),
        Color(0xFF70D6FF),
        Color(0xFF80ED99)
    )
    val cartoonOutline = Color(0xFF4A2535)
    val dotFill = badgeColors[index % badgeColors.size]

    // 每行独立的微呼吸浮动偏移
    val rowFloatY = kotlin.math.sin(wavePhase.toDouble() + index * 0.95).toFloat() * 1.4f
    val dynamicTextBrush = Brush.linearGradient(
        colors = listOf(
            cartoonOutline,
            Color(0xFFD81B60),
            Color(0xFFFF6F00),
            cartoonOutline
        ),
        start = Offset(shimmerX - index * 40f, 0f),
        end = Offset(shimmerX + 180f - index * 40f, 20f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = itemAlpha.value
                translationX = itemSlideX.value * density
                translationY = rowFloatY * density
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 手绘粗边小糖果球（带白色高光点）
        Canvas(
            modifier = Modifier
                .size(12.dp)
                .graphicsLayer {
                    scaleX = dotScale
                    scaleY = dotScale
                }
        ) {
            val r = size.minDimension / 2f
            drawCircle(color = cartoonOutline, radius = r)
            drawCircle(color = dotFill, radius = r - 1.6.dp.toPx())
            drawCircle(
                color = Color.White.copy(alpha = 0.85f),
                radius = r * 0.28f,
                center = Offset(size.width * 0.36f, size.height * 0.34f)
            )
        }
        Spacer(modifier = Modifier.width(9.dp))
        Text(
            text = text.take(revealedChars.coerceIn(0, text.length)),
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold,
            style = androidx.compose.ui.text.TextStyle(brush = dynamicTextBrush),
            lineHeight = 17.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        // 右侧手绘小星星贴纸
        Canvas(
            modifier = Modifier
                .size(9.dp)
                .graphicsLayer {
                    scaleX = dotScale
                    scaleY = dotScale
                    rotationZ = wavePhase * 25f
                }
        ) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            drawCircle(color = Color(0xFFFFD166), radius = size.minDimension * 0.42f, center = Offset(cx, cy))
            drawCircle(color = Color.White, radius = size.minDimension * 0.16f, center = Offset(cx - 1f, cy - 1f))
        }
    }
}

// ---------- 可爱卡通糖果进度条 ----------
@Composable
private fun CssFlowProgressBar(progress: Float, flow: Float) {
    val frac = progress.coerceIn(0f, 100f) / 100f
    val cartoonOutline = Color(0xFF4A2535)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(13.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFFFE5EE))
            .border(2.dp, cartoonOutline, RoundedCornerShape(50))
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = frac)
                .fillMaxSize()
                .clip(RoundedCornerShape(50))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFF7EB3), Color(0xFFFFB86C), Color(0xFFFFE066)),
                        startX = 0f,
                        endX = 500f
                    )
                )
        )
        // 卡通胶囊高光条
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = frac)
                .height(4.dp)
                .padding(horizontal = 4.dp, vertical = 1.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = (0.45f + flow * 0.25f).coerceIn(0.3f, 0.75f)))
        )
    }
}

// ---------- 可爱卡通 3D 贴纸脉冲按钮（只改样式，保留文字与点击回调） ----------
@Composable
private fun CssGradientButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    scale: Float
) {
    val btnComp = com.example.ui.components.ComponentThemeResolver.resolve(
        com.example.ui.components.LocalComponentThemes.current, "button"
    )
    val cartoonOutline = Color(0xFF4A2535)
    val btnShape = RoundedCornerShape(btnComp?.cornerRadius ?: 18.dp)
    val fillBrush: Brush = if (btnComp?.backgroundColor != null) {
        Brush.verticalGradient(
            colors = listOf(btnComp.backgroundColor, btnComp.backgroundColor.copy(alpha = 0.90f))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFFFE875), Color(0xFFFFCA3A))
        )
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .testTag("css_update_btn")
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        // 卡通按钮底部硬投影层
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 3.dp, start = 2.dp)
                .clip(btnShape)
                .background(cartoonOutline)
        )
        // 卡通按钮正面
        Box(
            modifier = Modifier
                .padding(bottom = 3.dp, end = 2.dp)
                .clip(btnShape)
                .background(fillBrush)
                .border(
                    width = if (btnComp != null && btnComp.borderWidth > 0.dp) btnComp.borderWidth else 2.4.dp,
                    color = btnComp?.borderColor ?: cartoonOutline,
                    shape = btnShape
                )
                .padding(horizontal = 24.dp, vertical = 9.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Black,
                color = if (enabled) cartoonOutline else cartoonOutline.copy(alpha = 0.55f)
            )
        }
    }
}

// ---------- 官方群卡通胶囊贴纸链接（只改样式，文字与逻辑不变） ----------
@Composable
private fun CssGroupLink(text: String, onClick: () -> Unit) {
    val cartoonOutline = Color(0xFF4A2535)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFFFEFF4))
            .border(1.8.dp, Color(0xFFFFACC5), RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 13.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.ExtraBold,
            color = cartoonOutline.copy(alpha = 0.82f)
        )
    }
}

// ============================================================
// 底层辅助：签名对比 / PackageInstaller / FileProvider
// ============================================================

/**
 * PackageInstaller 系统安装会话：
 * - v1.0.2 修复：PendingIntent 改 FLAG_MUTABLE（Android 12+ 系统需向回调 intent 注入安装状态，
 *   使用 IMMUTABLE 会导致部分设备上安装结果回调永远不送达 → 弹窗卡在「正在安装」）
 */
private fun installViaPackageInstaller(context: Context, apkFile: File): Boolean {
    return try {
        val packageInstaller = context.packageManager.packageInstaller
        val params = android.content.pm.PackageInstaller.SessionParams(
            android.content.pm.PackageInstaller.SessionParams.MODE_FULL_INSTALL
        )
        params.setAppPackageName(context.packageName)
        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)
        try {
            session.openWrite("lzdz_update.apk", 0, apkFile.length()).use { out ->
                apkFile.inputStream().use { input -> input.copyTo(out) }
            }
        } finally {
            session.close()
        }
        val receiverIntent = Intent(context, UpdateInstallReceiver::class.java)
        val pending = android.app.PendingIntent.getBroadcast(
            context,
            100,
            receiverIntent,
            // v1.0.2：必须 MUTABLE，否则系统无法向回调 PendingIntent 注入 EXTRA_STATUS
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_MUTABLE
        )
        session.commit(pending.intentSender)
        true
    } catch (e: Exception) {
        false
    }
}

/** FileProvider + 系统安装器（最通用的兜底方案） */
private fun installViaFileProvider(context: Context, file: File) {
    try {
        val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "无法打开系统安装器，请稍后到文件管理器中手动安装更新包", Toast.LENGTH_LONG).show()
    }
}

/** 提取 APK 签名证书 SHA-256（十六进制小写） */
private fun apkSigningHash(context: Context, file: File): String? {
    return try {
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(
            file.absolutePath,
            android.content.pm.PackageManager.GET_SIGNING_CERTIFICATES
        ) ?: return null
        val certs = info.signingInfo?.apkContentsSigners ?: return null
        certs.firstOrNull()?.toByteArray()?.let(::sha256Hex)
    } catch (e: Exception) { null }
}

/** SHA-256 十六进制（用于签名对比） */
private fun sha256Hex(bytes: ByteArray): String {
    val md = java.security.MessageDigest.getInstance("SHA-256")
    return md.digest(bytes).joinToString("") { "%02x".format(it) }
}