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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import kotlin.random.Random

/**
 * v1.4.22 新工具：随机密码生成器
 * - 长度 6~32 可调，大写/小写/数字/符号自由组合
 * - 一键生成 + 一键复制
 */
@Composable
fun PasswordGeneratorSection() {
    val themeColors = LocalThemeUiColors.current
    val context = LocalContext.current

    var length by remember { mutableStateOf(16) }
    var useUpper by remember { mutableStateOf(true) }
    var useLower by remember { mutableStateOf(true) }
    var useDigit by remember { mutableStateOf(true) }
    var useSymbol by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }

    fun generate() {
        val pool = buildString {
            if (useUpper) append("ABCDEFGHJKLMNPQRSTUVWXYZ")
            if (useLower) append("abcdefghijkmnpqrstuvwxyz")
            if (useDigit) append("23456789")
            if (useSymbol) append("!@#$%^&*()-_=+[]{};:,.<>?")
        }
        if (pool.isEmpty()) {
            Toast.makeText(context, "请至少勾选一种字符类型", Toast.LENGTH_SHORT).show()
            return
        }
        password = (1..length).map { pool[Random.nextInt(pool.length)] }.joinToString("")
    }

    @Composable
    fun CheckRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onChange,
                colors = CheckboxDefaults.colors(checkedColor = themeColors.primary)
            )
            Text(label, fontSize = 13.sp, color = themeColors.text)
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
            Icon(Icons.Filled.Key, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("随机密码生成器", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        }

        if (password.isNotEmpty()) {
            Text(
                text = password,
                fontSize = 17.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = themeColors.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                lineHeight = 24.sp
            )
        } else {
            Text("点击生成按钮，创建高强度随机密码", fontSize = 13.sp, color = themeColors.textMuted)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("密码长度：$length 位", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = themeColors.text)
            Spacer(modifier = Modifier.weight(1f))
        }
        Slider(
            value = length.toFloat(),
            onValueChange = { length = it.toInt() },
            valueRange = 6f..32f,
            steps = 25,
            modifier = Modifier.fillMaxWidth()
        )

        CheckRow("大写字母 (A-Z)", useUpper) { useUpper = it }
        CheckRow("小写字母 (a-z)", useLower) { useLower = it }
        CheckRow("数字 (0-9)", useDigit) { useDigit = it }
        CheckRow("特殊符号 (!@#…)", useSymbol) { useSymbol = it }

        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { generate() },
                colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("生成密码", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            OutlinedButton(
                onClick = {
                    if (password.isEmpty()) {
                        Toast.makeText(context, "请先生成密码", Toast.LENGTH_SHORT).show()
                    } else {
                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(ClipData.newPlainText("密码", password))
                        Toast.makeText(context, "密码已复制", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("复制密码", fontSize = 13.sp)
            }
        }
        Text("提示：密码仅在本地生成，不会上传网络", fontSize = 11.sp, color = themeColors.textMuted)
    }
}
