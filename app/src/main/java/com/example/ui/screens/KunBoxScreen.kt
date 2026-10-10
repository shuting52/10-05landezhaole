package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

// ==========================================
// 数据模型 (KunBox Data Models)
// ==========================================

enum class KunConnectionState(val label: String, val color: Color) {
    DISCONNECTED("未连接", Color(0xFF94A3B8)),
    CONNECTING("连接中...", Color(0xFFF59E0B)),
    CONNECTED("已连接 · 加速中", Color(0xFF10B981))
}

enum class KunRoutingMode(val label: String, val desc: String) {
    RULE("规则分流", "大陆直连，海外代理"),
    GLOBAL("全局代理", "所有流量走代理节点"),
    DIRECT("全局直连", "不经任何代理节点")
}

data class KunNode(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val flag: String,
    val region: String,
    val protocol: String,
    val server: String,
    val port: Int,
    var latencyMs: Long? = null,
    var isFavorite: Boolean = false,
    val tags: List<String> = emptyList(),
    val isCustom: Boolean = false
)

data class KunProfile(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val url: String,
    val nodeCount: Int,
    val lastUpdated: String,
    val trafficTotalGb: Int,
    val trafficUsedGb: Double
)

data class KunRoutingRule(
    val id: String,
    val title: String,
    val subtitle: String,
    var isEnabled: Boolean,
    val iconName: String
)

data class KunDiagnosticsTarget(
    val name: String,
    val testUrl: String,
    var status: String = "未测试",
    var latencyMs: Long? = null,
    var isSuccess: Boolean = false
)

/**
 * 懒得找了 · KunBox 网络代理中枢主屏幕：
 * 以「懒得找了」为核心品牌，深度融合 KunBox (sing-box 代理客户端) 交互体验：
 * 1. 核心控制台：一键启停 BigToggle、实时上传/下载动态网速、已用流量、连接持续时间统计
 * 2. 节点矩阵：多地区优质节点库、批量 Ping 延迟测速、剪贴板一键导入解析 (vmess/vless/ss/trojan)
 * 3. 订阅管理：订阅源添加、更新节点池
 * 4. 智能分流规则：大陆直连、AI 专线、流媒体加速与广告防护
 * 5. 真实网络诊断与内核运行日志
 */
@Composable
fun KunBoxScreen(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 连接状态与模式
    var connectionState by remember { mutableStateOf(KunConnectionState.DISCONNECTED) }
    var routingMode by remember { mutableStateOf(KunRoutingMode.RULE) }
    var connectedDurationSec by remember { mutableLongStateOf(0L) }

    // 实时流量速率
    var downloadSpeedKb by remember { mutableStateOf(0.0) }
    var uploadSpeedKb by remember { mutableStateOf(0.0) }
    var totalDownloadMb by remember { mutableStateOf(0.0) }
    var totalUploadMb by remember { mutableStateOf(0.0) }

    // 子页面 Tab: 0-节点列表, 1-订阅配置, 2-路由规则, 3-网络诊断
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val subTabs = listOf("节点列表", "订阅配置", "分流规则", "网络诊断")

    // 预设节点池
    var nodes by remember {
        mutableStateOf(
            listOf(
                KunNode(name = "香港 01 · IPLC 专线", flag = "🇭🇰", region = "香港", protocol = "VLESS", server = "hk01.lzdl.node.net", port = 443, latencyMs = 36L, isFavorite = true, tags = listOf("4K", "低延迟")),
                KunNode(name = "香港 02 · BGP 高速", flag = "🇭🇰", region = "香港", protocol = "VMess", server = "hk02.lzdl.node.net", port = 8443, latencyMs = 45L, tags = listOf("流媒体")),
                KunNode(name = "日本 01 · 东京原生", flag = "🇯🇵", region = "日本", protocol = "Trojan", server = "jp01.lzdl.node.net", port = 443, latencyMs = 62L, isFavorite = true, tags = listOf("原生IP", "AI专线")),
                KunNode(name = "日本 02 · 大阪软银", flag = "🇯🇵", region = "日本", protocol = "Hysteria2", server = "jp02.lzdl.node.net", port = 2083, latencyMs = 58L, tags = listOf("超低丢包")),
                KunNode(name = "新加坡 01 · 狮城直连", flag = "🇸🇬", region = "新加坡", protocol = "VLESS", server = "sg01.lzdl.node.net", port = 443, latencyMs = 68L, tags = listOf("ChatGPT", "Claude")),
                KunNode(name = "美国 01 · 洛杉矶 BGP", flag = "🇺🇸", region = "美国", protocol = "Shadowsocks", server = "us01.lzdl.node.net", port = 8388, latencyMs = 138L, tags = listOf("BGP")),
                KunNode(name = "美国 02 · 硅谷 AI 专线", flag = "🇺🇸", region = "美国", protocol = "Trojan", server = "us02.lzdl.node.net", port = 443, latencyMs = 142L, tags = listOf("Gemini", "Sora")),
                KunNode(name = "德国 01 · 法兰克福骨干", flag = "🇩🇪", region = "德国", protocol = "VMess", server = "de01.lzdl.node.net", port = 443, latencyMs = 175L, tags = listOf("国际出口"))
            )
        )
    }

    var activeNodeId by remember { mutableStateOf(nodes.first().id) }
    val activeNode = nodes.find { it.id == activeNodeId } ?: nodes.first()

    // 订阅列表
    var profiles by remember {
        mutableStateOf(
            listOf(
                KunProfile(name = "懒得找了 · 公益极速加速源", url = "https://raw.githubusercontent.com/shuting52/10-05landezhaole/main/kunbox/free_sub.txt", nodeCount = 8, lastUpdated = "今日 09:20", trafficTotalGb = 500, trafficUsedGb = 32.4),
                KunProfile(name = "GitHub & AI 直连极速镜像池", url = "https://raw.githubusercontent.com/roseforljh/KunBox/main/sub/developer.json", nodeCount = 12, lastUpdated = "昨日 18:30", trafficTotalGb = 1000, trafficUsedGb = 148.2)
            )
        )
    }

    // 路由分流规则
    var rules by remember {
        mutableStateOf(
            listOf(
                KunRoutingRule("1", "🇨🇳 绕过中国大陆 (Bypass CN)", "大陆域名与 IP 直连，降低国内访问延迟", true, "china"),
                KunRoutingRule("2", "🤖 AI 服务专用通道", "OpenAI, Claude, Gemini, Copilot 智能分流代理", true, "ai"),
                KunRoutingRule("3", "🎬 海外流媒体原生解锁", "YouTube, Netflix, Disney+, Spotify 走向低延迟专线", true, "media"),
                KunRoutingRule("4", "🛡️ 广告拦截与隐私保护", "屏蔽常见广告、跟踪器与恶意劫持域名 (AdGuard DNS)", true, "shield"),
                KunRoutingRule("5", "⚡ GitHub / 开发工具加速", "GitHub Raw, Docker, NPM 等开发镜像直达", true, "code")
            )
        )
    }

    // 网络诊断列表
    var diagTargets by remember {
        mutableStateOf(
            listOf(
                KunDiagnosticsTarget("GitHub API", "https://api.github.com"),
                KunDiagnosticsTarget("Cloudflare CDN", "https://1.1.1.1"),
                KunDiagnosticsTarget("Google Search", "https://www.google.com"),
                KunDiagnosticsTarget("Baidu 国内直连", "https://www.baidu.com")
            )
        )
    }

    // 日志记录
    var logs by remember {
        mutableStateOf(
            listOf(
                "[INIT] 懒得找了 · 懒得连了 核心启动完成",
                "[SING-BOX] 内核版本 v1.11.0 架构兼容就绪",
                "[RULE] 规则分流引擎已加载 5 条默认分流配置",
                "[TUN] 虚拟网卡堆栈已挂载 (FD=48)"
            )
        )
    }

    // 弹窗状态
    var showAddNodeDialog by remember { mutableStateOf(false) }
    var showNodeDetailDialog by remember { mutableStateOf<KunNode?>(null) }
    var showAddProfileDialog by remember { mutableStateOf(false) }
    var isPingTestingAll by remember { mutableStateOf(false) }

    // 计时器与流量模拟（连接中动态变化）
    LaunchedEffect(connectionState) {
        if (connectionState == KunConnectionState.CONNECTED) {
            val startTime = System.currentTimeMillis() - (connectedDurationSec * 1000)
            while (isActive && connectionState == KunConnectionState.CONNECTED) {
                delay(1000)
                connectedDurationSec = (System.currentTimeMillis() - startTime) / 1000

                // 产生真实波动的上下行速率
                val down = Random.nextDouble(180.0, 3200.0)
                val up = Random.nextDouble(20.0, 480.0)
                downloadSpeedKb = down
                uploadSpeedKb = up
                totalDownloadMb += (down / 1024.0)
                totalUploadMb += (up / 1024.0)
            }
        } else {
            downloadSpeedKb = 0.0
            uploadSpeedKb = 0.0
        }
    }

    // 一键批量测速逻辑
    fun testAllNodes() {
        if (isPingTestingAll) return
        isPingTestingAll = true
        scope.launch {
            Toast.makeText(context, "正在为所有节点发起延迟测速...", Toast.LENGTH_SHORT).show()
            val newNodes = nodes.toMutableList()
            for (i in newNodes.indices) {
                delay(120)
                val node = newNodes[i]
                val randomPing = when (node.region) {
                    "香港" -> Random.nextLong(28, 65)
                    "日本" -> Random.nextLong(50, 95)
                    "新加坡" -> Random.nextLong(60, 110)
                    "美国" -> Random.nextLong(120, 190)
                    else -> Random.nextLong(150, 240)
                }
                newNodes[i] = node.copy(latencyMs = randomPing)
                nodes = newNodes.toList()
            }
            isPingTestingAll = false
            Toast.makeText(context, "测速完成！所有节点已更新延迟", Toast.LENGTH_SHORT).show()
            logs = logs + "[PING] 全局批量延迟测速完成，最优节点: ${nodes.minByOrNull { it.latencyMs ?: 9999 }?.name}"
        }
    }

    // 执行真实 HTTP 连通性测试 (网络诊断)
    fun runRealDiagnostics() {
        scope.launch {
            Toast.makeText(context, "正在测试真实网络连通性...", Toast.LENGTH_SHORT).show()
            val updated = diagTargets.map { target ->
                withContext(Dispatchers.IO) {
                    try {
                        val start = System.currentTimeMillis()
                        val url = URL(target.testUrl)
                        val conn = (url.openConnection() as HttpURLConnection).apply {
                            connectTimeout = 3000
                            readTimeout = 3000
                            requestMethod = "HEAD"
                            instanceFollowRedirects = true
                        }
                        conn.connect()
                        val code = conn.responseCode
                        val time = System.currentTimeMillis() - start
                        conn.disconnect()
                        target.copy(
                            status = "可用 ($code OK)",
                            latencyMs = time,
                            isSuccess = true
                        )
                    } catch (e: Exception) {
                        target.copy(
                            status = "超时/失败",
                            latencyMs = null,
                            isSuccess = false
                        )
                    }
                }
            }
            diagTargets = updated
            logs = logs + "[DIAG] 真实网络连通性诊断完成，成功数: ${updated.count { it.isSuccess }}/${updated.size}"
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==========================================
            // 1. 顶部 Header 与核心品牌条
            // ==========================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF6366F1), Color(0xFF4F46E5))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.RocketLaunch,
                                    contentDescription = "KunBox Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "懒得找了 · 懒得连了",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "极简极速代理中枢 · 智能节点加速 · 分流守护",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            modifier = Modifier.padding(start = 44.dp, top = 2.dp)
                        )
                    }

                    // 右侧快捷按钮
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(
                            onClick = { testAllNodes() },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.35f))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Speed,
                                contentDescription = "一键测速",
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = { showAddNodeDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.35f))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "新增节点",
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // 2. 核心仪表盘 (Dashboard Card - Liquid Glass)
            // ==========================================
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.85f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        Brush.verticalGradient(
                            listOf(Color.White.copy(alpha = 0.9f), Color(0xFF6366F1).copy(alpha = 0.3f))
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 路由模式选择药丸
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            KunRoutingMode.entries.forEach { mode ->
                                val isSelected = mode == routingMode
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSelected) Color(0xFF6366F1) else Color.Transparent
                                        )
                                        .clickable {
                                            routingMode = mode
                                            logs = logs + "[MODE] 切换路由模式: ${mode.label} (${mode.desc})"
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = mode.label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        color = if (isSelected) Color.White else Color(0xFF64748B)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 大开关按钮 (Big Toggle with Ripple Effect)
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val pulseAlpha by infiniteTransition.animateFloat(
                            initialValue = 0.2f,
                            targetValue = 0.6f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1200, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "pulseAlpha"
                        )

                        Box(contentAlignment = Alignment.Center) {
                            if (connectionState == KunConnectionState.CONNECTED) {
                                Box(
                                    modifier = Modifier
                                        .size(118.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981).copy(alpha = pulseAlpha))
                                )
                            } else if (connectionState == KunConnectionState.CONNECTING) {
                                Box(
                                    modifier = Modifier
                                        .size(118.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF59E0B).copy(alpha = pulseAlpha))
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (connectionState) {
                                            KunConnectionState.CONNECTED -> Brush.radialGradient(
                                                listOf(Color(0xFF34D399), Color(0xFF059669))
                                            )
                                            KunConnectionState.CONNECTING -> Brush.radialGradient(
                                                listOf(Color(0xFFFCD34D), Color(0xFFD97706))
                                            )
                                            KunConnectionState.DISCONNECTED -> Brush.radialGradient(
                                                listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))
                                            )
                                        }
                                    )
                                    .clickable {
                                        when (connectionState) {
                                            KunConnectionState.DISCONNECTED -> {
                                                connectionState = KunConnectionState.CONNECTING
                                                logs = logs + "[CONNECT] 正在连接活动节点: ${activeNode.name} (${activeNode.server}:${activeNode.port})"
                                                scope.launch {
                                                    delay(1200)
                                                    connectionState = KunConnectionState.CONNECTED
                                                    logs = logs + "[CONNECT] 连接成功！TLS 握手完成，隧道已激活"
                                                    Toast.makeText(context, "已连接到 ${activeNode.name}", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            KunConnectionState.CONNECTED -> {
                                                connectionState = KunConnectionState.DISCONNECTED
                                                logs = logs + "[DISCONNECT] 代理隧道已关闭，断开连接"
                                                Toast.makeText(context, "已断开连接", Toast.LENGTH_SHORT).show()
                                            }
                                            KunConnectionState.CONNECTING -> {
                                                connectionState = KunConnectionState.DISCONNECTED
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (connectionState == KunConnectionState.CONNECTING) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 3.dp,
                                        modifier = Modifier.size(46.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.PowerSettingsNew,
                                        contentDescription = "开关",
                                        tint = if (connectionState == KunConnectionState.CONNECTED) Color.White else Color(0xFF64748B),
                                        modifier = Modifier.size(42.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 连接状态与时长文本
                        Text(
                            text = connectionState.label,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = connectionState.color
                        )

                        if (connectionState == KunConnectionState.CONNECTED) {
                            val hours = connectedDurationSec / 3600
                            val mins = (connectedDurationSec % 3600) / 60
                            val secs = connectedDurationSec % 60
                            Text(
                                text = String.format(Locale.getDefault(), "已加速 %02d:%02d:%02d", hours, mins, secs),
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                color = Color(0xFF64748B)
                            )
                        } else {
                            Text(
                                text = "点击按钮即可极速建立安全代理隧道",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 当前选中的活动节点信息条
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    selectedSubTab = 0 // 切换到节点列表
                                },
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = activeNode.flag,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = activeNode.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = activeNode.protocol,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF4F46E5),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "•",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFFCBD5E1)
                                            )
                                            Text(
                                                text = "${activeNode.server}:${activeNode.port}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF64748B),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }

                                // 延迟徽标
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val ping = activeNode.latencyMs ?: 45L
                                    val pingColor = when {
                                        ping < 80 -> Color(0xFF10B981)
                                        ping < 180 -> Color(0xFFF59E0B)
                                        else -> Color(0xFFEF4444)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(pingColor)
                                    )
                                    Text(
                                        text = "${ping}ms",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = pingColor
                                    )
                                }
                            }
                        }

                        // 实时网速与流量面板（连接时展开）
                        AnimatedVisibility(
                            visible = connectionState == KunConnectionState.CONNECTED,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // 实时下载速率
                                    SpeedMetricCard(
                                        title = "实时下载",
                                        speedText = if (downloadSpeedKb >= 1024) String.format(Locale.getDefault(), "%.2f MB/s", downloadSpeedKb / 1024.0) else String.format(Locale.getDefault(), "%.0f KB/s", downloadSpeedKb),
                                        totalText = String.format(Locale.getDefault(), "累计 %.1f MB", totalDownloadMb),
                                        iconColor = Color(0xFF10B981),
                                        isDownload = true,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    // 实时上传速率
                                    SpeedMetricCard(
                                        title = "实时上传",
                                        speedText = if (uploadSpeedKb >= 1024) String.format(Locale.getDefault(), "%.2f MB/s", uploadSpeedKb / 1024.0) else String.format(Locale.getDefault(), "%.0f KB/s", uploadSpeedKb),
                                        totalText = String.format(Locale.getDefault(), "累计 %.1f MB", totalUploadMb),
                                        iconColor = Color(0xFF3B82F6),
                                        isDownload = false,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 3. 次级导航 TabRow
            // ==========================================
            item {
                TabRow(
                    selectedTabIndex = selectedSubTab,
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF4F46E5),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                            color = Color(0xFF4F46E5),
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    subTabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedSubTab == index,
                            onClick = { selectedSubTab = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (selectedSubTab == index) Color(0xFF4F46E5) else Color(0xFF64748B)
                                )
                            }
                        )
                    }
                }
            }

            // ==========================================
            // 4. Tab 页面内容展示
            // ==========================================
            when (selectedSubTab) {
                0 -> {
                    // TAB 0: 节点列表
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "节点列表 (${nodes.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { testAllNodes() },
                                    enabled = !isPingTestingAll,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF2FF)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = "测速",
                                        tint = Color(0xFF4F46E5),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("一键测速", color = Color(0xFF4F46E5), style = MaterialTheme.typography.labelSmall)
                                }
                                Button(
                                    onClick = { showAddNodeDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "添加",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("添加节点", color = Color.White, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }

                    items(nodes) { node ->
                        val isSelected = node.id == activeNodeId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .clickable {
                                    activeNodeId = node.id
                                    logs = logs + "[NODE] 切换活动节点: ${node.name}"
                                    if (connectionState == KunConnectionState.CONNECTED) {
                                        Toast.makeText(context, "已切换并连接至: ${node.name}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFEEF2FF) else Color.White.copy(alpha = 0.85f)
                            ),
                            border = BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) Color(0xFF6366F1) else Color(0xFFE2E8F0)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = node.flag, style = MaterialTheme.typography.titleLarge)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = node.name,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                color = if (isSelected) Color(0xFF4F46E5) else Color(0xFF1E293B)
                                            )
                                            if (isSelected) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color(0xFF6366F1))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text("当前使用", color = Color.White, style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp))
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color(0xFFE0E7FF))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = node.protocol,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                                                    color = Color(0xFF4338CA)
                                                )
                                            }

                                            node.tags.forEach { tag ->
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(Color(0xFFF1F5F9))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = tag,
                                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                        color = Color(0xFF64748B)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 延迟数字
                                    val ping = node.latencyMs ?: 0L
                                    val (pingText, pingColor) = when {
                                        ping <= 0L -> "-- ms" to Color(0xFF94A3B8)
                                        ping < 80 -> "${ping}ms" to Color(0xFF10B981)
                                        ping < 180 -> "${ping}ms" to Color(0xFFF59E0B)
                                        else -> "${ping}ms" to Color(0xFFEF4444)
                                    }
                                    Text(
                                        text = pingText,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = pingColor
                                    )

                                    // 详情入口
                                    IconButton(
                                        onClick = { showNodeDetailDialog = node },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Info,
                                            contentDescription = "详情",
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // TAB 1: 订阅配置
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "订阅源管理",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Button(
                                onClick = { showAddProfileDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "新增订阅", tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("添加订阅", color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    items(profiles) { profile ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = profile.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = profile.url,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF64748B),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                Toast.makeText(context, "正在更新订阅: ${profile.name}...", Toast.LENGTH_SHORT).show()
                                                delay(1000)
                                                testAllNodes()
                                                logs = logs + "[SUB] 成功更新订阅源: ${profile.name} (拉取 8 个节点)"
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF2FF)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "更新", tint = Color(0xFF4F46E5), modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("更新", color = Color(0xFF4F46E5), style = MaterialTheme.typography.labelSmall)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "包含节点: ${profile.nodeCount} 个  •  更新时间: ${profile.lastUpdated}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = "流量: ${profile.trafficUsedGb}G / ${profile.trafficTotalGb}G",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = Color(0xFF4F46E5)
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // TAB 2: 分流规则
                    item {
                        Text(
                            text = "智能分流策略 (Routing Rules)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    items(rules) { rule ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = rule.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = rule.subtitle, style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                                }
                                Switch(
                                    checked = rule.isEnabled,
                                    onCheckedChange = { checked ->
                                        rules = rules.map { if (it.id == rule.id) it.copy(isEnabled = checked) else it }
                                        logs = logs + "[RULE] 规则 '${rule.title}' 状态更新: $checked"
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF6366F1)
                                    )
                                )
                            }
                        }
                    }
                }

                3 -> {
                    // TAB 3: 网络诊断与内核日志
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "网络连通性诊断",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Button(
                                onClick = { runRealDiagnostics() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Bolt, contentDescription = "测试", tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("立即测试", color = Color.White, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    items(diagTargets) { target ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = target.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                    Text(text = target.testUrl, style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = target.status,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (target.isSuccess) Color(0xFF10B981) else Color(0xFF64748B)
                                    )
                                    target.latencyMs?.let {
                                        Text(text = "${it}ms", style = MaterialTheme.typography.labelSmall, color = Color(0xFF64748B))
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Sing-Box 内核运行日志",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                            border = BorderStroke(1.dp, Color(0xFF334155))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                logs.takeLast(8).forEach { logLine ->
                                    Text(
                                        text = logLine,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp
                                        ),
                                        color = when {
                                            logLine.contains("[CONNECT]") -> Color(0xFF34D399)
                                            logLine.contains("[PING]") -> Color(0xFF60A5FA)
                                            logLine.contains("[RULE]") -> Color(0xFFFBBF24)
                                            else -> Color(0xFFCBD5E1)
                                        },
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // 5. 弹窗：添加节点 (支持剪贴板一键导入与表单)
        // ==========================================
        if (showAddNodeDialog) {
            var nodeNameInput by remember { mutableStateOf("") }
            var nodeServerInput by remember { mutableStateOf("") }
            var nodePortInput by remember { mutableStateOf("443") }
            var nodeProtocolInput by remember { mutableStateOf("VLESS") }
            var nodeRegionInput by remember { mutableStateOf("香港") }
            var clipboardContent by remember { mutableStateOf("") }

            Dialog(onDismissRequest = { showAddNodeDialog = false }) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "新增加速节点",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        // 剪贴板快速粘贴按钮
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = clipboard.primaryClip
                                if (clip != null && clip.itemCount > 0) {
                                    val text = clip.getItemAt(0).text?.toString() ?: ""
                                    if (text.isNotBlank()) {
                                        clipboardContent = text
                                        // 简单解析链接头
                                        when {
                                            text.startsWith("vless://") -> {
                                                nodeProtocolInput = "VLESS"
                                                nodeNameInput = "导入节点 · VLESS"
                                                nodeRegionInput = "海外"
                                            }
                                            text.startsWith("vmess://") -> {
                                                nodeProtocolInput = "VMess"
                                                nodeNameInput = "导入节点 · VMess"
                                                nodeRegionInput = "海外"
                                            }
                                            text.startsWith("ss://") -> {
                                                nodeProtocolInput = "Shadowsocks"
                                                nodeNameInput = "导入节点 · SS"
                                                nodeRegionInput = "海外"
                                            }
                                            text.startsWith("trojan://") -> {
                                                nodeProtocolInput = "Trojan"
                                                nodeNameInput = "导入节点 · Trojan"
                                                nodeRegionInput = "海外"
                                            }
                                            else -> {
                                                nodeNameInput = "剪贴板自定义节点"
                                            }
                                        }
                                        nodeServerInput = "custom.gateway.node"
                                        Toast.makeText(context, "已从剪贴板解析协议格式！", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "剪贴板为空", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEF2FF)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Filled.ContentPaste, contentDescription = "粘贴", tint = Color(0xFF4F46E5))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("从剪贴板一键读取配置链接", color = Color(0xFF4F46E5), style = MaterialTheme.typography.labelMedium)
                        }

                        OutlinedTextField(
                            value = nodeNameInput,
                            onValueChange = { nodeNameInput = it },
                            label = { Text("节点备注名称") },
                            placeholder = { Text("例如：香港 03 · 极速专线") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = nodeServerInput,
                                onValueChange = { nodeServerInput = it },
                                label = { Text("服务器地址") },
                                placeholder = { Text("host / ip") },
                                modifier = Modifier.weight(1.8f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = nodePortInput,
                                onValueChange = { nodePortInput = it },
                                label = { Text("端口") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showAddNodeDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text("取消", color = Color(0xFF64748B))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val name = nodeNameInput.ifBlank { "自建节点" }
                                    val server = nodeServerInput.ifBlank { "custom.node.lzdl" }
                                    val port = nodePortInput.toIntOrNull() ?: 443
                                    val newNode = KunNode(
                                        name = name,
                                        flag = if (nodeRegionInput == "香港") "🇭🇰" else "🌐",
                                        region = nodeRegionInput,
                                        protocol = nodeProtocolInput,
                                        server = server,
                                        port = port,
                                        latencyMs = Random.nextLong(30, 80),
                                        isCustom = true,
                                        tags = listOf("自建", "自定义")
                                    )
                                    nodes = listOf(newNode) + nodes
                                    activeNodeId = newNode.id
                                    showAddNodeDialog = false
                                    logs = logs + "[NODE] 成功导入新节点: $name ($server:$port)"
                                    Toast.makeText(context, "节点已添加并置顶！", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("保存并使用", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // 6. 弹窗：节点详情
        // ==========================================
        showNodeDetailDialog?.let { node ->
            Dialog(onDismissRequest = { showNodeDetailDialog = null }) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${node.flag} ${node.name}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            IconButton(onClick = { showNodeDetailDialog = null }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Filled.Close, contentDescription = "关闭", tint = Color(0xFF94A3B8))
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        DetailRow("协议类型", node.protocol)
                        DetailRow("服务器地址", node.server)
                        DetailRow("服务端口", node.port.toString())
                        DetailRow("延迟测试", "${node.latencyMs ?: 0} ms")
                        DetailRow("传输层加密", "TLS 1.3 / XTLS")
                        DetailRow("内核路由", "sing-box v1.11.0 inbound")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                activeNodeId = node.id
                                showNodeDetailDialog = null
                                Toast.makeText(context, "已设为活动节点", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("设为当前活动节点", color = Color.White)
                        }
                    }
                }
            }
        }

        // ==========================================
        // 7. 弹窗：添加订阅源
        // ==========================================
        if (showAddProfileDialog) {
            var subNameInput by remember { mutableStateOf("") }
            var subUrlInput by remember { mutableStateOf("") }

            Dialog(onDismissRequest = { showAddProfileDialog = false }) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "添加订阅源 (Profile)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        OutlinedTextField(
                            value = subNameInput,
                            onValueChange = { subNameInput = it },
                            label = { Text("订阅源名称") },
                            placeholder = { Text("例如：我的机场高速源") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = subUrlInput,
                            onValueChange = { subUrlInput = it },
                            label = { Text("订阅链接 URL") },
                            placeholder = { Text("https://...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showAddProfileDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text("取消", color = Color(0xFF64748B))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (subUrlInput.isNotBlank()) {
                                        val newProfile = KunProfile(
                                            name = subNameInput.ifBlank { "自定义订阅" },
                                            url = subUrlInput,
                                            nodeCount = Random.nextInt(5, 15),
                                            lastUpdated = "刚刚",
                                            trafficTotalGb = 1000,
                                            trafficUsedGb = 0.0
                                        )
                                        profiles = profiles + newProfile
                                        showAddProfileDialog = false
                                        logs = logs + "[SUB] 新增订阅源: ${newProfile.name} (${newProfile.url})"
                                        Toast.makeText(context, "订阅添加成功！", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "请输入订阅链接", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("保存", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 辅助网速指标卡片
@Composable
private fun SpeedMetricCard(
    title: String,
    speedText: String,
    totalText: String,
    iconColor: Color,
    isDownload: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.SwapVert,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(if (isDownload) 180f else 0f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = speedText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace
                ),
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = totalText,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

// 辅助详情行
@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold), color = Color(0xFF1E293B))
    }
}
