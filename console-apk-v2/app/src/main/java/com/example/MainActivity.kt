package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AdminAppShell
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AdminViewModel

/**
 * 控制台 v2 入口（Kotlin Compose 重写版）
 * 由 v2.1.0 反编译 MainActivity 还原
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AdminAppShellEntry()
            }
        }
    }
}

@Composable
private fun AdminAppShellEntry() {
    val vm: AdminViewModel = viewModel()
    AdminAppShell(vm)
}
