package com.example.ui.screens.toolbox

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

/**
 * v1.1.18 优化：环境检测（ping0.cc/env）
 *
 * 呈现优化：
 * - 顶部线性加载进度条（WebView onProgressChanged 实时更新）
 * - 加载失败自动显示错误页 + 重试按钮（网络不可达时不白屏）
 * - WebView 设置优化：JS/本地存储/视口自适应，沉浸式展示
 * - 返回 / 刷新工具栏
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EnvCheckSection(modifier: Modifier = Modifier) {
    var progress by remember { mutableStateOf(0) }
    var loadError by remember { mutableStateOf(false) }
    var loadFinished by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var loadKey by remember { mutableStateOf(0) } // 重试时重建 WebView

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 标题卡
        Surface(
            color = Color(0xFFFFFDF9).copy(alpha = 0.9f),
            border = BorderStroke(1.dp, Color(0xFF6C5CE7).copy(alpha = 0.55f)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("? 环境检测 · ping0.cc", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFF6C5CE7))
                Text("IP 地址 · 地理位置 · 代理/VPN · 网络类型 一键检测", fontSize = 11.sp, color = Color(0xFF7A6A9A), modifier = Modifier.padding(top = 2.dp))
            }
        }

        // 工具栏：返回 / 刷新
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFF3F0FF), modifier = Modifier.weight(1f)) {
                IconButton(onClick = { webView?.goBack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = Color(0xFF6C5CE7))
                }
            }
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFF3F0FF), modifier = Modifier.weight(1f)) {
                IconButton(onClick = { webView?.reload(); loadError = false }) {
                    Icon(Icons.Filled.Refresh, contentDescription = "刷新", tint = Color(0xFF6C5CE7))
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = if (loadError) "加载失败" else "ping0.cc/env",
                fontSize = 11.sp,
                color = if (loadError) Color(0xFFE53935) else Color(0xFF9A8FC0),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(3f)
            )
        }

        // 加载进度条（加载中显示）
        if (progress in 1 until 100) {
            LinearProgressIndicator(
                progress = { progress / 100f },
                color = Color(0xFF6C5CE7),
                trackColor = Color(0xFFEDE9FF),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // WebView 检测页 / 失败重试页
        if (loadError) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF6F4FF), RoundedCornerShape(18.dp))
            ) {
                Text("?", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text("页面加载失败，请检查网络后重试", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A3A6A), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = { loadError = false; loadKey++; progress = 0; loadFinished = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("重新加载", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(16.dp))
            ) {
                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    progress = 5
                                    loadError = false
                                }
                                override fun onPageFinished(view: WebView?, url: String?) {
                                    progress = 100
                                    loadFinished = true
                                }
                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        loadError = true
                                    }
                                }
                            }
                            webChromeClient = object : android.webkit.WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    progress = newProgress
                                    if (newProgress >= 100) {
                                        loadFinished = true
                                        progress = 100
                                    }
                                }
                            }
                            loadUrl("https://ping0.cc/env")
                            webView = this
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { wv -> if (loadKey > 0) wv.loadUrl("https://ping0.cc/env") }
                )
            }
        }
    }
}
