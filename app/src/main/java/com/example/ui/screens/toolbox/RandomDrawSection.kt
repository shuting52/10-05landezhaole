package com.example.ui.screens.toolbox

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import kotlin.random.Random

/**
 * v1.4.22 新工具：随机抽签
 * - 随机数 / 抛硬币 / 掷骰子 / 名单抽取，一站式随机决策
 */
@Composable
fun RandomDrawSection() {
    val themeColors = LocalThemeUiColors.current
    val context = LocalContext.current

    var mode by remember { mutableStateOf(0) } // 0随机数 1硬币 2骰子 3名单
    var minVal by remember { mutableStateOf("1") }
    var maxVal by remember { mutableStateOf("100") }
    var nameList by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val modes = listOf("随机数", "抛硬币", "掷骰子", "名单抽取")

    fun draw() {
        result = when (mode) {
            0 -> {
                val a = minVal.toIntOrNull() ?: 1
                val b = maxVal.toIntOrNull() ?: 100
                if (a > b) "最小值不能大于最大值" else Random.nextInt(a, b + 1).toString()
            }
            1 -> if (Random.nextBoolean()) "正面 🎉" else "反面 🌙"
            2 -> {
                val d = Random.nextInt(1, 7)
                val faces = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
                "${faces[d - 1]}  $d 点"
            }
            else -> {
                val names = nameList.split("\n", "，", ",", "、", " ")
                    .map { it.trim() }.filter { it.isNotEmpty() }
                if (names.isEmpty()) "请先输入名单（每行一个名字）"
                else names[Random.nextInt(names.size)]
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Casino, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("随机抽签", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            modes.forEachIndexed { i, m ->
                val selected = mode == i
                OutlinedButton(
                    onClick = { mode = i; result = "" },
                    shape = RoundedCornerShape(20.dp),
                    colors = if (selected)
                        ButtonDefaults.outlinedButtonColors(containerColor = themeColors.primary.copy(alpha = 0.12f))
                    else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(m, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) themeColors.primary else themeColors.textMuted)
                }
            }
        }

        when (mode) {
            0 -> Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = minVal, onValueChange = { minVal = it.filter { c -> c.isDigit() || c == '-' } },
                    label = { Text("最小值", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true
                )
                OutlinedTextField(
                    value = maxVal, onValueChange = { maxVal = it.filter { c -> c.isDigit() || c == '-' } },
                    label = { Text("最大值", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), singleLine = true
                )
            }
            3 -> OutlinedTextField(
                value = nameList, onValueChange = { nameList = it },
                label = { Text("名单（每行一个名字）", fontSize = 12.sp) },
                placeholder = { Text("张三\n李四\n王五", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                minLines = 4, maxLines = 8
            )
        }

        Button(
            onClick = { draw() },
            colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Casino, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("开始抽取", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        if (result.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("抽取结果", fontSize = 12.sp, color = themeColors.textMuted)
                Text(
                    result,
                    fontSize = 38.sp, fontWeight = FontWeight.Black, color = themeColors.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(ClipData.newPlainText("抽签结果", result))
                        Toast.makeText(context, "结果已复制", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("复制结果", fontSize = 12.sp)
                }
            }
        }
    }
}
