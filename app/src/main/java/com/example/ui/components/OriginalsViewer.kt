package com.example.ui.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 2026-10-24：设计原稿馆 —— 用户要求"原封不动呈现到软件里面"。
 * 呈现方式按原稿类型区分，内容本身一律不改：
 * - HTML 原稿：放进 assets 原样文件，用 WebView 原样渲染（含真模糊与动效，不翻译成 Compose）
 * - LiquidGlassScreen.kt：原样放进 assets 以源码呈现；
 *   该文件直接进工程编不过（类型与版本两类问题），原稿不动；
 *   2026-10-25 起另有转换版可运行（com.example.liquidglass.LiquidGlassScreen，
 *   仅改 3 处兼容性问题，其余原样；入口在原稿馆「可运行演示」区）
 * - 两包源码（下载模块 / 主题模块）：原样放进 assets，以源码全文形式呈现；
 *   其中 Dart 是 Flutter 代码，在 Kotlin 软件里跑不起来，故只呈现不移植；
 *   下载模块的 Kotlin 版与软件现有下载器不是同一份，为避免弄坏正在用的下载，只呈现不替换
 */
private sealed interface OriginalPage {
    object PageList : OriginalPage
    object NativeGlass : OriginalPage
    data class Html(val title: String, val asset: String) : OriginalPage
    data class SourceDir(val title: String, val base: String, val files: List<String>) : OriginalPage
    data class SourceFile(val title: String, val asset: String) : OriginalPage
}

private val downloadFiles = listOf(
    "README.md",
    "lib/download_config.dart",
    "lib/download_logic.dart",
    "lib/downloader.dart",
    "lib/ui/download_progress_card.dart",
    "android/kotlin/NativeDownloader.kt",
    "android/kotlin/DownloadKeepAlive.kt",
    "android/kotlin/DownloadNames.kt"
)

private val themeFiles = listOf(
    "README.md",
    "lib/ui/palette.dart",
    "lib/ui/glass.dart",
    "lib/ui/theme_appearance_page.dart"
)

@Composable
fun OriginalsViewer(onDismiss: () -> Unit, onApplyTheme: ((String) -> Unit)? = null) {
    var page by remember { mutableStateOf<OriginalPage>(OriginalPage.PageList) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            when (val p = page) {
                is OriginalPage.PageList -> OriginalsList(
                    onOpen = { page = it },
                    onDismiss = onDismiss,
                    onApplyTheme = onApplyTheme
                )
                is OriginalPage.Html -> OriginalHtmlPage(
                    title = p.title,
                    asset = p.asset,
                    onBack = { page = OriginalPage.PageList }
                )
                is OriginalPage.NativeGlass -> {
                    ViewerTopBar(title = "液态玻璃演示页（转换版·可运行）", onBack = { page = OriginalPage.PageList })
                    Box(modifier = Modifier.weight(1f)) {
                        com.example.liquidglass.LiquidGlassTheme {
                            com.example.liquidglass.LiquidGlassScreen()
                        }
                    }
                }
                is OriginalPage.SourceDir -> OriginalSourceDirPage(
                    title = p.title,
                    base = p.base,
                    files = p.files,
                    onBack = { page = OriginalPage.PageList },
                    onOpenFile = { file -> page = OriginalPage.SourceFile(file, "${p.base}/$file") }
                )
                is OriginalPage.SourceFile -> OriginalSourceFilePage(
                    title = p.title,
                    asset = p.asset,
                    onBack = { page = OriginalPage.PageList }
                )
            }
        }
    }
}

@Composable
private fun ViewerTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0B1220))
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, "返回", tint = Color.White)
        }
        Text(
            text = title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun OriginalsList(
    onOpen: (OriginalPage) -> Unit,
    onDismiss: () -> Unit,
    onApplyTheme: ((String) -> Unit)?
) {
    ViewerTopBar(title = "设计原稿（原封不动）", onBack = onDismiss)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "以下均为你发来的原稿，未经改动：HTML 原样渲染，源码原样呈现。HTML 原稿点右边的「应用」可直接设为软件主题。",
            fontSize = 12.sp,
            color = Color(0xFF5B7A90)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OriginalSectionTitle("原稿页面（原样渲染）")
        OriginalRow(
            "液态玻璃 · 可交互预览",
            "HTML 原稿，WebView 原样打开",
            onClick = { onOpen(OriginalPage.Html("液态玻璃 · 可交互预览", "originals/liquid-glass.html")) },
            onApply = onApplyTheme?.let { apply -> { apply("liquid"); onDismiss() } }
        )
        OriginalRow(
            "macOS 风格程序坞",
            "HTML 原稿，WebView 原样打开",
            onClick = { onOpen(OriginalPage.Html("macOS 风格程序坞", "originals/dock.html")) },
            onApply = onApplyTheme?.let { apply -> { apply("dock"); onDismiss() } }
        )
        OriginalRow(
            "AERO · 千禧复兴 Frutiger Aero",
            "HTML 原稿，WebView 原样打开",
            onClick = { onOpen(OriginalPage.Html("AERO · 千禧复兴", "originals/aero.html")) },
            onApply = onApplyTheme?.let { apply -> { apply("minimal"); onDismiss() } }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OriginalSectionTitle("可运行演示（转换版）")
        OriginalRow("液态玻璃演示页（可运行）", "转换版：原稿不动，另做一份能编过的版本进软件运行") {
            onOpen(OriginalPage.NativeGlass)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OriginalSectionTitle("演示页源码（原样呈现）")
        OriginalRow("液态玻璃演示页源码", "LiquidGlassScreen.kt 原样呈现，全文可看") {
            onOpen(OriginalPage.SourceFile("LiquidGlassScreen.kt", "originals-src/liquidglass/LiquidGlassScreen.kt"))
        }
        Spacer(modifier = Modifier.height(16.dp))
        OriginalSectionTitle("源码（原样呈现）")
        OriginalRow("下载模块源码", "Flutter + Kotlin，取自 shuiyingongju（只呈现，不替换现有下载器）") {
            onOpen(OriginalPage.SourceDir("下载模块源码", "originals-src/download", downloadFiles))
        }
        OriginalRow("主题模块源码", "Flutter，深浅两档 + 液态玻璃组件，取自 shuiyingongju") {
            onOpen(OriginalPage.SourceDir("主题模块源码", "originals-src/theme", themeFiles))
        }
    }
}

@Composable
private fun OriginalSectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0A2540),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun OriginalRow(
    title: String,
    subtitle: String,
    onApply: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0A2540))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 12.sp, color = Color(0xFF5B7A90))
        }
        if (onApply != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "应用",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007AFF),
                modifier = Modifier
                    .clickable(onClick = onApply)
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            )
        }
        Icon(Icons.Filled.KeyboardArrowRight, "打开", tint = Color(0xFF9FB3D1))
    }
}

@Composable
private fun OriginalHtmlPage(title: String, asset: String, onBack: () -> Unit) {
    ViewerTopBar(title = title, onBack = onBack)
    AndroidView(
        factory = { c ->
            WebView(c).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = WebViewClient()
                loadUrl("file:///android_asset/$asset")
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun OriginalSourceDirPage(
    title: String,
    base: String,
    files: List<String>,
    onBack: () -> Unit,
    onOpenFile: (String) -> Unit
) {
    ViewerTopBar(title = title, onBack = onBack)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = base, fontSize = 12.sp, color = Color(0xFF5B7A90))
        Spacer(modifier = Modifier.height(12.dp))
        files.forEach { file ->
            OriginalRow(file, "点开查看全文") { onOpenFile(file) }
        }
    }
}

@Composable
private fun OriginalSourceFilePage(title: String, asset: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val text = remember(asset) {
        try {
            context.assets.open(asset).bufferedReader().readText()
        } catch (e: Exception) {
            "读取失败：${e.message}"
        }
    }
    ViewerTopBar(title = title, onBack = onBack)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF0A2540)
        )
    }
}
