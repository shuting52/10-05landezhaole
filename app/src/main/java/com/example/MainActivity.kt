package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.db.AppDatabase
import com.example.data.repository.NavRepository
import com.example.ui.components.LocalComponentThemes
import com.example.ui.components.LocalStreamingBorderAngle
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.NavViewModel
import com.example.data.remote.RemoteConfigRepository
import com.example.ui.viewmodel.NavViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 异步预热全局共享 ImageLoader，避免主线程 I/O 阻塞首帧导致 SurfaceSyncGroup 超时
        kotlin.concurrent.thread {
            com.example.ui.components.FastFaviconImageLoader.get(applicationContext)
        }

        // 安全加固：异步读取云端签名校验配置，开启时验证自身签名（防止二次打包篡改）
        SecurityGuard.verifyInBackground(applicationContext)

        // v1.5.3：版本变化时清理残留的旧版本 APK（安装成功后弹窗可能来不及清理）
        kotlin.concurrent.thread {
            try {
                val prefs = applicationContext.getSharedPreferences("lzdz_update_prefs", Context.MODE_PRIVATE)
                val lastCode = prefs.getInt("last_version_code", 0)
                val curCode = try {
                    applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0).let {
                        if (android.os.Build.VERSION.SDK_INT >= 28) it.longVersionCode.toInt() else it.versionCode
                    }
                } catch (_: Exception) { 0 }
                if (curCode > 0 && lastCode > 0 && curCode != lastCode) {
                    com.example.ui.components.cleanupOldApks(applicationContext)
                }
                if (curCode > 0) prefs.edit().putInt("last_version_code", curCode).apply()
            } catch (_: Exception) {}
        }

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = NavRepository(database.itemRecordDao(), database.uploadedResourceDao(), database.cloneAppDao())
        val remoteConfigRepository = RemoteConfigRepository(applicationContext)
        val factory = NavViewModelFactory(repository, remoteConfigRepository, application)
        val viewModel = ViewModelProvider(this, factory)[NavViewModel::class.java]

        setContent {
            val uiState = viewModel.uiState.collectAsState().value
            // 全局单一主导边框流光角度驱动，保证全软件上百张卡片流畅同步慢速优雅跑动
            val infiniteTransition = rememberInfiniteTransition(label = "global_border_stream")
            val streamingBorderAngle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 7600, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "streamingBorderAngle"
            )

            // v1.1.8 控制台主题工具箱同步修复：
            // 把云端 themeKit 解析出的组件主题表通过 CompositionLocal 提供给全部 UI 组件实时消费
            val compThemes = uiState.activeUiverseState.componentThemes
            CompositionLocalProvider(
                LocalComponentThemes provides compThemes,
                LocalStreamingBorderAngle provides streamingBorderAngle
            ) {
                MyApplicationTheme(
                    uiverseState = uiState.activeUiverseState,
                    appThemeId = uiState.appThemeId
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun Greeting(name: String, modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}

