package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.remote.IpMonitorDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 高精度真实网络定位数据模型
 * - 免开启手机 GPS 定位
 * - 无需连接 VPN 代理
 * - 支持多源并发竞速与智能共识
 */
data class RealTimeLocation(
    val ip: String = "",
    val country: String = "",
    val province: String = "",
    val city: String = "",
    val district: String = "",
    val isp: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isVpn: Boolean = false,
    val networkType: String = "网络直连",
    val syncTimestamp: Long = System.currentTimeMillis()
) {
    /** 简洁一行文本（用于首页顶部定位条） */
    fun toDisplayText(): String {
        val locParts = listOf(country, province, city, district)
            .filter { it.isNotBlank() && it != "N/A" && it != "--" && it != "0" }
            .distinct()
        val locStr = if (locParts.isNotEmpty()) locParts.joinToString(" ") else "未知地区"
        val extra = mutableListOf<String>()
        if (isp.isNotBlank() && isp != "N/A" && isp != "--") {
            extra.add(isp.substringBefore(" ").take(12))
        }
        if (latitude != null && longitude != null && latitude != 0.0 && longitude != 0.0) {
            extra.add(String.format(Locale.US, "%.2f,%.2f", latitude, longitude))
        }
        val extraStr = if (extra.isNotEmpty()) "（${extra.joinToString(" · ")}）" else ""
        return if (ip.isNotBlank()) "$ip · $locStr$extraStr" else "$locStr$extraStr"
    }

    fun toCoordinatesString(): String {
        return if (latitude != null && longitude != null && latitude != 0.0 && longitude != 0.0) {
            String.format(Locale.US, "纬度 %.4f · 经度 %.4f", latitude, longitude)
        } else {
            "运营商基站精准网段定位"
        }
    }
}

/**
 * 实时高精度网络定位管理器（单例）
 * 在没有开启手机 GPS 定位和 VPN 的情况下，通过多源运营商交叉验证精确解析真实物理定位。
 */
object RealTimeLocationManager {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .build()

    var cachedLocation: RealTimeLocation? = null
        private set

    /** 获取当前网络连接类型与是否处于 VPN 环境 */
    fun getNetworkInfo(context: Context): Pair<String, Boolean> {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return Pair("网络连接", false)
            val activeNet = cm.activeNetwork ?: return Pair("蜂窝/Wi-Fi", false)
            val caps = cm.getNetworkCapabilities(activeNet) ?: return Pair("网络连接", false)
            val isVpn = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                    !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
            val type = when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi 无线网络"
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "蜂窝移动网络 (5G/4G)"
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "有线以太网"
                else -> "宽带直连"
            }
            Pair(type, isVpn)
        } catch (_: Exception) {
            Pair("网络连接", false)
        }
    }

    /**
     * 并发竞速请求多个高精度真实定位源（免GPS、无需VPN）
     */
    suspend fun getLocation(context: Context, configUrl: String = ""): RealTimeLocation? = withContext(Dispatchers.IO) {
        val (netType, isVpn) = getNetworkInfo(context)

        val results = supervisorScope {
            val tasks = mutableListOf(
                async { fetchFromBilibili() },
                async { fetchFromIpip() },
                async { fetchFromIpWhoIs() },
                async { fetchFromZxinc() },
                async { fetchFromBaidu() },
                async { fetchFromIpInfo() }
            )
            if (configUrl.isNotBlank() && configUrl.startsWith("https://")) {
                tasks.add(async { fetchFromUrl(configUrl) })
            }

            tasks.mapNotNull { task ->
                try {
                    task.await()
                } catch (_: Exception) {
                    null
                }
            }
        }

        if (results.isEmpty()) {
            return@withContext cachedLocation
        }

        val merged = mergeResults(results, netType, isVpn)
        cachedLocation = merged
        merged
    }

    private fun fetchFromBilibili(): RealTimeLocation? {
        return try {
            val req = Request.Builder()
                .url("https://api.bilibili.com/x/web-interface/zone")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) Mobile")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val root = JSONObject(body)
                if (root.optInt("code", -1) != 0) return null
                val data = root.optJSONObject("data") ?: return null
                val ip = data.optString("addr").trim()
                val country = data.optString("country").trim()
                val province = data.optString("province").trim()
                val city = data.optString("city").trim()
                val isp = data.optString("isp").trim()
                val lat = data.optDouble("latitude", Double.NaN).takeIf { !it.isNaN() }
                val lon = data.optDouble("longitude", Double.NaN).takeIf { !it.isNaN() }
                if (ip.isBlank() && province.isBlank() && city.isBlank()) return null
                RealTimeLocation(
                    ip = ip,
                    country = country,
                    province = province,
                    city = city,
                    isp = isp,
                    latitude = lat,
                    longitude = lon
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchFromIpip(url: String = "https://myip.ipip.net/"): RealTimeLocation? {
        return try {
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) Mobile")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string()?.trim() ?: return null
                if (body.startsWith("{")) {
                    return parseGeneralJson(body)
                }
                parsePlainTextIpip(body)
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchFromIpWhoIs(): RealTimeLocation? {
        return try {
            val req = Request.Builder()
                .url("https://ipwho.is/?lang=zh-CN")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) Mobile")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val obj = JSONObject(body)
                if (!obj.optBoolean("success", true)) return null
                val ip = obj.optString("ip")
                val country = obj.optString("country")
                val region = obj.optString("region")
                val city = obj.optString("city")
                val lat = obj.optDouble("latitude", Double.NaN).takeIf { !it.isNaN() }
                val lon = obj.optDouble("longitude", Double.NaN).takeIf { !it.isNaN() }
                val conn = obj.optJSONObject("connection")
                val isp = conn?.optString("isp") ?: obj.optString("isp")
                RealTimeLocation(
                    ip = ip,
                    country = country,
                    province = region,
                    city = city,
                    isp = isp,
                    latitude = lat,
                    longitude = lon
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchFromZxinc(): RealTimeLocation? {
        return try {
            val req = Request.Builder()
                .url("https://ip.zxinc.org/api.php?type=json")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) Mobile")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val obj = JSONObject(body)
                if (obj.optInt("code", -1) != 0) return null
                val data = obj.optJSONObject("data") ?: return null
                val ip = data.optString("myip")
                val loc = data.optString("location")
                val local = data.optString("local")
                val parts = loc.split("\t").filter { it.isNotBlank() }
                val country = parts.getOrNull(0).orEmpty()
                val province = parts.getOrNull(1).orEmpty()
                val city = parts.getOrNull(2).orEmpty()
                val isp = local.ifBlank { parts.getOrNull(3).orEmpty() }
                RealTimeLocation(
                    ip = ip,
                    country = country,
                    province = province,
                    city = city,
                    isp = isp
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchFromBaidu(): RealTimeLocation? {
        return try {
            val req = Request.Builder()
                .url("https://opendata.baidu.com/api.php?resource_id=6006&oe=utf8&query=")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) Mobile")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val obj = JSONObject(body)
                val dataArr = obj.optJSONArray("data") ?: return null
                val item = dataArr.optJSONObject(0) ?: return null
                val ip = item.optString("origip")
                val loc = item.optString("location")
                val parts = loc.split(" ").filter { it.isNotBlank() }
                val country = parts.getOrNull(0).orEmpty()
                val province = parts.getOrNull(1).orEmpty()
                val city = parts.getOrNull(2).orEmpty()
                val isp = parts.drop(3).joinToString(" ")
                RealTimeLocation(
                    ip = ip,
                    country = country,
                    province = province,
                    city = city,
                    isp = isp
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchFromIpInfo(): RealTimeLocation? {
        return try {
            val req = Request.Builder()
                .url("https://ipinfo.io/json")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) Mobile")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                val obj = JSONObject(body)
                val ip = obj.optString("ip")
                val country = obj.optString("country")
                val region = obj.optString("region")
                val city = obj.optString("city")
                val org = obj.optString("org")
                val loc = obj.optString("loc")
                var lat: Double? = null
                var lon: Double? = null
                if (loc.contains(",")) {
                    val p = loc.split(",")
                    lat = p.getOrNull(0)?.toDoubleOrNull()
                    lon = p.getOrNull(1)?.toDoubleOrNull()
                }
                RealTimeLocation(
                    ip = ip,
                    country = country,
                    province = region,
                    city = city,
                    isp = org,
                    latitude = lat,
                    longitude = lon
                )
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchFromUrl(url: String): RealTimeLocation? {
        return try {
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) Mobile")
                .build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string()?.trim() ?: return null
                if (body.startsWith("{")) {
                    parseGeneralJson(body)
                } else {
                    parsePlainTextIpip(body)
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun parseGeneralJson(body: String): RealTimeLocation? {
        return try {
            val obj = JSONObject(body)
            val ip = obj.optString("query").ifBlank { obj.optString("ip") }
            val city = obj.optString("city").ifBlank { obj.optString("city_name") }
            val region = obj.optString("regionName").ifBlank { obj.optString("province") }
            val country = obj.optString("country").ifBlank { obj.optString("country_name") }
            val district = obj.optString("district")
            val isp = obj.optString("org").ifBlank { obj.optString("isp") }
            val loc = obj.optString("loc")
            val lat = obj.optDouble("lat", Double.NaN).takeIf { !it.isNaN() }
                ?: loc.split(",").getOrNull(0)?.toDoubleOrNull()
            val lon = obj.optDouble("lon", Double.NaN).takeIf { !it.isNaN() }
                ?: loc.split(",").getOrNull(1)?.toDoubleOrNull()
            RealTimeLocation(
                ip = ip,
                country = country,
                province = region,
                city = city,
                district = district,
                isp = isp,
                latitude = lat,
                longitude = lon
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun parsePlainTextIpip(text: String): RealTimeLocation? {
        return try {
            val clean = text.replace("\u0000", "")
            val ipMatch = Regex("(?:IP|ip|IP地址|ip地址)[：:\\s]*([0-9a-fA-F.:]{7,})").find(clean)
            val ip = ipMatch?.groupValues?.getOrNull(1)?.trim().orEmpty()
            val fromMatch = Regex("来自于[：:]\\s*([^\\n\\r]+)").find(clean)
            val fromStr = fromMatch?.groupValues?.getOrNull(1)?.trim().orEmpty()
            val tokens = fromStr.split(Regex("\\s+")).filter { it.isNotBlank() && it != "N/A" }
            val country = tokens.getOrNull(0).orEmpty()
            val province = tokens.getOrNull(1).orEmpty()
            val city = tokens.getOrNull(2).orEmpty()
            val isp = tokens.drop(3).joinToString(" ")
            if (ip.isBlank() && tokens.isEmpty()) return null
            RealTimeLocation(
                ip = ip,
                country = country,
                province = province,
                city = city,
                isp = isp
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun mergeResults(
        list: List<RealTimeLocation>,
        networkType: String,
        isVpn: Boolean
    ): RealTimeLocation {
        val bestIp = list.map { it.ip }.filter { it.isNotBlank() }
            .groupingBy { it }.eachCount().maxByOrNull { it.value }?.key.orEmpty()

        val bestLoc = list.maxByOrNull {
            (if (it.province.isNotBlank()) 2 else 0) +
            (if (it.city.isNotBlank()) 3 else 0) +
            (if (it.district.isNotBlank()) 2 else 0) +
            (if (it.latitude != null && it.latitude != 0.0) 2 else 0) +
            (if (it.isp.isNotBlank() && !it.isp.contains("Google")) 1 else 0)
        } ?: list.first()

        val bestLat = list.firstOrNull { it.latitude != null && it.latitude != 0.0 }?.latitude
        val bestLon = list.firstOrNull { it.longitude != null && it.longitude != 0.0 }?.longitude
        val bestIsp = list.firstOrNull {
            it.isp.isNotBlank() && it.isp != "N/A" &&
            (it.isp.contains("电信") || it.isp.contains("联通") || it.isp.contains("移动") || it.isp.contains("广电") || it.isp.contains("教育网"))
        }?.isp ?: bestLoc.isp

        return RealTimeLocation(
            ip = bestIp.ifBlank { bestLoc.ip },
            country = bestLoc.country,
            province = bestLoc.province,
            city = bestLoc.city,
            district = bestLoc.district,
            isp = bestIsp,
            latitude = bestLat ?: bestLoc.latitude,
            longitude = bestLon ?: bestLoc.longitude,
            isVpn = isVpn,
            networkType = networkType,
            syncTimestamp = System.currentTimeMillis()
        )
    }
}

/**
 * 首页置顶 · 实时 IP 定位监控组件
 * - 由后台控制台配置：开关 + 数据源 URL
 * - UI 只展示「定位 IP + 所在地区」，不出现任何网站/数据源字样
 * - 实时同步方式呈现真实定位，无须开启手机GPS定位与VPN，精准无误
 * - 点击可弹出详细真实定位看板并支持一键手动实时重新同步
 */
@Composable
fun IpLocationMonitorWidget(
    cloudIpMonitor: IpMonitorDto?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var locationData by remember { mutableStateOf<RealTimeLocation?>(RealTimeLocationManager.cachedLocation) }
    var syncing by remember { mutableStateOf(false) }
    var failed by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }

    val enabled = cloudIpMonitor?.enabled == true
    val url = cloudIpMonitor?.url?.trim().orEmpty()

    // 旋转动画（同步中指示）
    val infiniteTransition = rememberInfiniteTransition(label = "sync_rotate")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    fun doSync() {
        if (syncing) return
        syncing = true
        scope.launch {
            val result = RealTimeLocationManager.getLocation(context, url)
            if (result != null) {
                locationData = result
                failed = false
            } else {
                failed = locationData == null
            }
            syncing = false
        }
    }

    LaunchedEffect(enabled, url) {
        if (!enabled) return@LaunchedEffect
        while (true) {
            doSync()
            delay(25_000L) // 每 25 秒自动保活与实时同步
        }
    }

    // 关闭或未配置时不渲染
    if (!enabled) return

    val primaryColor = MaterialTheme.colorScheme.primary
    val displayLocationText = locationData?.toDisplayText() ?: if (failed) "定位获取失败，点击重新同步…" else "真实定位同步中…"

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, primaryColor.copy(alpha = 0.20f), RoundedCornerShape(8.dp))
            .clickable {
                showDetailDialog = true
            }
            .testTag("ip_location_monitor"),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.40f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            // 定位图标与呼吸底座
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "定位",
                    tint = primaryColor,
                    modifier = Modifier.size(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // 标题
            Text(
                text = "定位",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Spacer(modifier = Modifier.width(4.dp))

            // 实时同步状态微标
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(primaryColor.copy(alpha = 0.10f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (syncing) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "同步中",
                            tint = primaryColor,
                            modifier = Modifier
                                .size(9.dp)
                                .rotate(rotationAngle)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (failed) Color(0xFFEF4444) else Color(0xFF22C55E))
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (syncing) "实时同步中" else "实时同步",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // 核心定位信息展示（高精度物理地区与运营商）
            Text(
                text = displayLocationText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.90f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(4.dp))

            // 网络接入环境标识标签
            val netBadge = when {
                locationData?.isVpn == true -> "代理"
                locationData?.networkType?.contains("Wi-Fi") == true -> "Wi-Fi"
                locationData?.networkType?.contains("5G") == true -> "5G"
                else -> "直连"
            }
            Text(
                text = netBadge,
                fontSize = 9.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.70f),
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.60f))
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            )
        }
    }

    // 点击弹出的「实时真实定位详情看板」
    if (showDetailDialog && locationData != null) {
        RealTimeLocationDetailDialog(
            location = locationData!!,
            isSyncing = syncing,
            onDismiss = { showDetailDialog = false },
            onRefresh = {
                doSync()
                Toast.makeText(context, "正在实时同步最新真实定位…", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

/**
 * 实时定位看板弹窗：
 * 展示真实物理定位、经纬度、公网 IP、运营商及无VPN直连真实状态，提供一键刷新与复制功能
 */
@Composable
fun RealTimeLocationDetailDialog(
    location: RealTimeLocation,
    isSyncing: Boolean,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val syncTimeStr = remember(location.syncTimestamp) {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.format(Date(location.syncTimestamp))
    }

    val infiniteTransition = rememberInfiniteTransition(label = "dialog_refresh")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dialog_rotation"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // 顶部标题与状态标签
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(primaryColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "定位",
                            tint = primaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "真实定位 · 实时同步",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "免开启GPS · 无需VPN · 多源共识",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onRefresh,
                        enabled = !isSyncing,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "刷新",
                            tint = primaryColor,
                            modifier = Modifier
                                .size(20.dp)
                                .then(if (isSyncing) Modifier.rotate(rotationAngle) else Modifier)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 物理位置卡片
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📍 真实物理位置",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = primaryColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val regionDetail = listOf(location.country, location.province, location.city, location.district)
                            .filter { it.isNotBlank() && it != "N/A" && it != "--" }
                            .distinct()
                            .joinToString(" ")
                        Text(
                            text = regionDetail.ifBlank { "未知真实位置" },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = location.toCoordinatesString(),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 网络 IP 与运营商卡片
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "🌐 当前公网 IP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryColor
                            )
                            if (location.ip.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("IP", location.ip)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "已复制 IP 地址: ${location.ip}", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = "复制",
                                        tint = primaryColor,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "复制",
                                        fontSize = 10.sp,
                                        color = primaryColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = location.ip.ifBlank { "获取中" },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "网络运营商：" + (location.isp.ifBlank { "电信/移动/联通/本地宽带" }),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 同步与接入环境卡片
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF22C55E),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "连接状态：" + (if (location.isVpn) "检测到 VPN 虚拟连接" else "本地直连（未开启VPN，真实定位）"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "接入环境：${location.networkType} · 同步时间：$syncTimeStr",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 底部操作按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRefresh,
                        enabled = !isSyncing,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            modifier = Modifier
                                .size(14.dp)
                                .then(if (isSyncing) Modifier.rotate(rotationAngle) else Modifier)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isSyncing) "同步中…" else "立即重新同步", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("完成", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * 兼容旧版调用的同步方法（维持 API 兼容性）
 */
fun fetchIpInfoRobust(configUrl: String = ""): String? {
    return try {
        kotlinx.coroutines.runBlocking {
            RealTimeLocationManager.getLocation(LocalStaticContext.appContext, configUrl)?.toDisplayText()
        }
    } catch (_: Exception) {
        null
    }
}

/** 静态 Context 兜底 */
private object LocalStaticContext {
    val appContext: Context
        get() {
            return try {
                val actThread = Class.forName("android.app.ActivityThread")
                val getAppMethod = actThread.getMethod("currentApplication")
                getAppMethod.invoke(null) as Context
            } catch (_: Exception) {
                try {
                    val appGlobals = Class.forName("android.app.AppGlobals")
                    val getInitialApp = appGlobals.getMethod("getInitialApplication")
                    getInitialApp.invoke(null) as Context
                } catch (_: Exception) {
                    throw IllegalStateException("Context unavailable")
                }
            }
        }
}
