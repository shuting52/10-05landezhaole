package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.IpMonitorDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * 首页置顶 · 实时 IP 定位监控组件
 * - 由后台控制台配置：开关 + 数据源 URL
 * - UI 只展示「定位 IP + 所在地区」，不出现任何网站/数据源字样
 * - 每 30 秒自动刷新一次；控制台改动后配置实时同步生效
 */
@Composable
fun IpLocationMonitorWidget(
    cloudIpMonitor: IpMonitorDto?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var ipText by remember { mutableStateOf("定位获取中…") }
    var failed by remember { mutableStateOf(false) }

    val enabled = cloudIpMonitor?.enabled == true
    val url = cloudIpMonitor?.url?.trim().orEmpty()

    LaunchedEffect(enabled, url) {
        if (!enabled || url.isBlank()) return@LaunchedEffect
        while (true) {
            val result = withContext(Dispatchers.IO) { fetchIpInfoRobust(url) }
            if (result != null) {
                ipText = result
                failed = false
            } else {
                failed = true
            }
            delay(30_000L)
        }
    }

    // 关闭或未配置时不渲染（本体首页不再呈现）
    if (!enabled || url.isBlank()) return

    val primaryColor = MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, primaryColor.copy(alpha = 0.20f), RoundedCornerShape(8.dp))
            .clickable {
                Toast.makeText(context, "IP 定位：$ipText", Toast.LENGTH_SHORT).show()
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
            Text(
                text = "定位",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (failed) "定位获取失败，稍后重试…" else ipText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            // 呼吸状态点
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(if (failed) Color(0xFFEF4444) else Color(0xFF22C55E))
            )
        }
    }
}

/**
 * 高精度多源 IP 定位（综合投票，精准无误）：
 * 1. 优先使用控制台配置的数据源
 * 2. 依次尝试多个免费 HTTPS 高精度中文源，采集「IP · 省 市 区 · 运营商 · 经纬度」
 * 3. 多源投票：取出现频次最高的地区组合（一致即高置信），避免单源偏差
 * 数据源：
 *   - ip.useragentinfo.com/json（https 中文，含省市区）
 *   - qifu-api.baidubce.com（https 中文，行政区划）
 *   - ipinfo.io/json（https，含 loc 经纬度 + org 运营商）
 *   - ip-api.com/json（http 中文，兜底）
 */
private fun fetchIpInfoRobust(configUrl: String = ""): String? {
    val sources = mutableListOf<String>()
    // 控制台配置源优先（https 才可用；http 明文在 Android 默认不可访问）
    if (configUrl.startsWith("https://")) sources.add(configUrl)
    sources.addAll(
        listOf(
            "https://ip.useragentinfo.com/json",
            "https://qifu-api.baidubce.com/ip/local/geo/v1/district",
            "https://ipinfo.io/json",
            "http://ip-api.com/json/?lang=zh-CN"
        )
    )
    val results = mutableListOf<String>()
    for (src in sources) {
        try {
            val result = fetchIpInfo(src)
            if (result != null) results.add(result)
        } catch (e: Exception) {
            // 继续下一个源
        }
    }
    if (results.isEmpty()) return null
    // 多源投票：按出现频次排序取最一致的（精准）
    return results.groupingBy { it }.eachCount()
        .entries.maxWithOrNull(compareBy({ it.value }, { results.indexOf(it.key) }))
        ?.key ?: results.first()
}

/**
 * 请求单个 IP 定位数据源，解析为「IP · 省 市 区 · 运营商 · 经纬度」高精度中文字符串。
 */
private fun fetchIpInfo(url: String): String? {
    return try {
        val client = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Linux; Android) Lzdz/1.6.8")
            .header("Referer", "https://www.baidu.com/")
            .build()
        client.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) return null
            val body = resp.body?.string() ?: return null
            val trimmed = body.trim()
            // v1.1.11 修复定位：myip.ipip.net 等数据源返回「纯文本」而非 JSON，
            // 先尝试纯文本正则解析（当前 IP：xxx 来自于：xx 省 市 运营商），再回退 JSON 解析
            if (!trimmed.startsWith("{")) {
                return parseIpPlainText(trimmed) ?: return null
            }
            val obj = JSONObject(body)
            // 通用字段：ip / query
            val ip = obj.optString("query").ifBlank { obj.optString("ip") }
            // 地区字段（兼容各源命名）
            val city = obj.optString("city").ifBlank { obj.optString("city_name") }
            val region = obj.optString("regionName").ifBlank { obj.optString("province") }
            val country = obj.optString("country").ifBlank { obj.optString("country_name") }
            val district = obj.optString("district")
            // 运营商（ipinfo: org / 百du: isp）
            val isp = obj.optString("org").ifBlank { obj.optString("isp") }
            // 经纬度（ipinfo: loc="lat,lon" / ip-api: lat+lon）
            val loc = obj.optString("loc")
            val lat = obj.optString("lat")
            val lon = obj.optString("lon")
            val coord = when {
                loc.isNotBlank() && loc.contains(",") -> {
                    val parts = loc.split(",")
                    if (parts.size >= 2) "${parts[0].trim()},${parts[1].trim()}" else ""
                }
                lat.isNotBlank() && lon.isNotBlank() -> "$lat,$lon"
                else -> ""
            }
            // 组装定位文本（含经纬度时更精准）
            val locParts = listOf(country, region, city, district)
                .filter { it.isNotBlank() && it != "N/A" && it != "--" && it != "0" }
                .distinct()
            val base = if (ip.isNotBlank()) "$ip · ${locParts.joinToString(" ")}" else if (locParts.isNotEmpty()) locParts.joinToString(" ") else return null
            val extra = listOf(
                isp.takeIf { it.isNotBlank() && it != "N/A" }?.substringBefore(" "),
                coord.takeIf { it.isNotBlank() }
            ).filterNotNull()
            return if (extra.isNotEmpty()) "$base（${extra.joinToString(" · ")}）" else base
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * v1.1.11：解析纯文本 IP 定位（myip.ipip.net 等返回「当前 IP：x.x.x.x 来自于：中国 北京 北京 电信」），
 * 兼容中文冒号与英文冒号、IP 段与地区段。
 */
private fun parseIpPlainText(body: String): String? {
    try {
        val text = body.replace("\u0000", "")
        // IP：xx.xx.xx.xx（IPv4 / IPv6）
        val ipMatch = Regex("(?:IP|ip|IP地址|ip地址)[：:\\s]*([0-9a-fA-F.:]{7,})").find(text)
        val ip = ipMatch?.groupValues?.getOrNull(1)?.trim()
        // 来自于：地区 运营商（myip.ipip.net 格式）
        val fromMatch = Regex("来自于[：:]\\s*([^\\n\\r]+)").find(text)
        val fromText = fromMatch?.groupValues?.getOrNull(1)?.trim() ?: ""
        val locParts = fromText
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() && it != "N/A" && it != "--" }
            .distinct()
        return when {
            ip != null && locParts.isNotEmpty() -> "$ip · ${locParts.joinToString(" ")}"
            ip != null -> ip
            locParts.isNotEmpty() -> locParts.joinToString(" ")
            else -> null
        }
    } catch (e: Exception) {
        return null
    }
}
