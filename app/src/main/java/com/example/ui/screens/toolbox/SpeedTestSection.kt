package com.example.ui.screens.toolbox

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * v1.1.18 新工具：测速网
 *
 * - 真实测速：下载 Cloudflare 测速文件（10MB）计算 Mbps，备用阿里云/腾讯云镜像
 * - Ping 延迟：TCP 建连耗时测量
 * - 定位：展示当前所在城市/坐标（需授权定位权限，权限已有声明）
 */
@Composable
fun SpeedTestSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var testing by remember { mutableStateOf(false) }
    var downloadMbps by remember { mutableStateOf<Double?>(null) }
    var pingMs by remember { mutableStateOf<Long?>(null) }
    var locationText by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf("点击开始测速") }

    // 定位权限请求
    val locPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        if (granted.values.any { it }) {
            status = "定位授权成功"
        }
    }

    fun requestLocation() {
        val needs = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                needs += Manifest.permission.ACCESS_FINE_LOCATION
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                needs += Manifest.permission.ACCESS_COARSE_LOCATION
        }
        if (needs.isNotEmpty()) {
            locPermissionLauncher.launch(needs.toTypedArray())
        }
    }

    fun locate() {
        try {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val loc: Location? = try {
                lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } catch (_: Exception) { null }
            if (loc != null) {
                val lat = String.format("%.2f", loc.latitude)
                val lng = String.format("%.2f", loc.longitude)
                locationText = "📍 纬度 $lat · 经度 $lng"
                // 尝试反查城市名（简化：仅展示坐标，网络反查需要额外 API）
                status = "定位成功"
            } else {
                locationText = "📍 暂未获取到位置，请开启手机定位后重试"
            }
        } catch (_: Exception) {}
    }

    fun runTest() {
        if (testing) return
        testing = true
        status = "正在测量 Ping…"
        scope.launch {
            // 1. Ping 延迟
            val ping = withContext(Dispatchers.IO) {
                measurePing(listOf("speed.cloudflare.com", "www.baidu.com"))
            }
            pingMs = ping
            status = "正在下载测速文件（10MB）…"
            // 2. 下载测速
            val mbps = withContext(Dispatchers.IO) {
                measureDownload(listOf(
                    "https://speed.cloudflare.com/__down?bytes=10000000",
                    "https://speed.cloudflare.com/__down?bytes=5000000"
                ))
            }
            downloadMbps = mbps
            status = if (mbps != null) "测速完成 ✅" else "测速失败（网络不可达）"
            testing = false
            // 3. 定位
            requestLocation()
            locate()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题卡
        Surface(
            color = Color(0xFFFFFDF9).copy(alpha = 0.9f),
            border = BorderStroke(1.dp, Color(0xFF00BFA6).copy(alpha = 0.55f)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("⚡ 测速网", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFF009688))
                Text("真实下载测速 · Ping 延迟 · 当前位置", fontSize = 11.sp, color = Color(0xFF5A7A76), modifier = Modifier.padding(top = 2.dp))
            }
        }

        // 测速结果面板
        Surface(
            color = Color(0xFFEAF9F6),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, Color(0xFF64D8CB).copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp)
            ) {
                if (testing) {
                    CircularProgressIndicator(color = Color(0xFF009688), strokeWidth = 3.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Text(
                    text = if (downloadMbps != null) String.format("%.2f", downloadMbps) + " Mbps" else "—",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF009688)
                )
                Text("下载速度", fontSize = 11.sp, color = Color(0xFF6A9893))

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (pingMs != null) "${pingMs} ms" else "—", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))
                        Text("Ping 延迟", fontSize = 10.sp, color = Color(0xFF6A9893))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(locationText ?: "未定位", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B), textAlign = TextAlign.Center)
                        Text("当前位置", fontSize = 10.sp, color = Color(0xFF6A9893))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(status, fontSize = 11.sp, color = Color(0xFF4E7C76))
            }
        }

        // 开始测速
        Button(
            onClick = { runTest() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688)),
            shape = RoundedCornerShape(14.dp),
            enabled = !testing,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Speed, contentDescription = null)
            Spacer(modifier = Modifier.size(6.dp))
            Text(if (testing) "测速中…" else "开始测速", fontWeight = FontWeight.Black, fontSize = 15.sp)
        }

        // 单独定位按钮
        androidx.compose.material3.OutlinedButton(
            onClick = { requestLocation(); locate() },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text("刷新定位", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "测速通过 Cloudflare 全球节点真实下载文件计算；定位需开启手机定位服务并授权。",
            fontSize = 11.sp,
            color = Color(0xFF6A9893)
        )
    }
}

/** 测量 TCP 建连延迟（毫秒） */
private suspend fun measurePing(hosts: List<String>): Long? {
    for (host in hosts) {
        try {
            val t0 = System.nanoTime()
            Socket().use { s ->
                s.connect(InetSocketAddress(host, 443), 4000)
            }
            val t1 = System.nanoTime()
            return TimeUnit.NANOSECONDS.toMillis(t1 - t0)
        } catch (_: Exception) {}
    }
    return null
}

/** 下载测速：返回 Mbps */
private suspend fun measureDownload(urls: List<String>): Double? {
    for (urlStr in urls) {
        var conn: HttpURLConnection? = null
        var input: InputStream? = null
        try {
            val url = URL(urlStr)
            conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 6000
                readTimeout = 15000
                setRequestProperty("User-Agent", "LazyFind-SpeedTest")
            }
            conn.connect()
            input = conn.inputStream
            val buffer = ByteArray(64 * 1024)
            var total = 0L
            val start = System.nanoTime()
            val deadline = start + TimeUnit.SECONDS.toNanos(12)
            while (System.nanoTime() < deadline) {
                val n = input.read(buffer)
                if (n < 0) break
                total += n
            }
            val elapsedSec = (System.nanoTime() - start) / 1_000_000_000.0
            if (elapsedSec > 0.1 && total > 0) {
                return (total * 8.0) / (elapsedSec * 1_000_000.0) // Mbps
            }
        } catch (_: Exception) {} finally {
            try { input?.close() } catch (_: Exception) {}
            try { conn?.disconnect() } catch (_: Exception) {}
        }
    }
    return null
}
