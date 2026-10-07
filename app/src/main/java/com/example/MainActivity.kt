package com.example

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
                    themePreset = uiState.currentTheme,
                    uiverseState = uiState.activeUiverseState
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

