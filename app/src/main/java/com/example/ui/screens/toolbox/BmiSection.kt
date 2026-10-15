package com.example.ui.screens.toolbox

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors

/**
 * v1.4.22 新工具：BMI 健康计算
 * - 输入身高体重，计算 BMI 指数并给出健康分级与建议
 */
@Composable
fun BmiSection() {
    val themeColors = LocalThemeUiColors.current
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf<Double?>(null) }

    fun calc() {
        val h = height.toDoubleOrNull()
        val w = weight.toDoubleOrNull()
        bmi = if (h != null && w != null && h > 50 && h < 250 && w > 10 && w < 300) {
            w / ((h / 100) * (h / 100))
        } else null
    }

    val (level, advice, levelColor) = when (val b = bmi) {
        null -> Triple("", "", themeColors.textMuted)
        else -> when {
            b < 18.5 -> Triple("偏瘦", "建议加强营养，适当增肌，规律三餐", themeColors.secondary)
            b < 24 -> Triple("正常", "保持得很好！继续规律作息和运动", themeColors.primary)
            b < 28 -> Triple("超重", "建议控制饮食、增加有氧运动", themeColors.primary)
            else -> Triple("肥胖", "建议咨询医生，制定科学减重计划", themeColors.primary)
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
            Icon(Icons.Filled.MonitorWeight, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("BMI 健康计算", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        }
        Text("身体质量指数 = 体重(kg) ÷ 身高(m)²", fontSize = 12.sp, color = themeColors.textMuted)

        OutlinedTextField(
            value = height,
            onValueChange = { height = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("身高（厘米）", fontSize = 12.sp) },
            placeholder = { Text("如：170", fontSize = 13.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("体重（千克）", fontSize = 12.sp) },
            placeholder = { Text("如：65", fontSize = 13.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Button(
            onClick = { calc() },
            colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("计算 BMI", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        bmi?.let { b ->
            Spacer(modifier = Modifier.height(4.dp))
            Text("你的 BMI", fontSize = 12.sp, color = themeColors.textMuted)
            Text(
                "%.1f".format(b),
                fontSize = 44.sp, fontWeight = FontWeight.Black, color = themeColors.primary
            )
            Text(level, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = levelColor)
            // 参考条：15 ~ 35
            val progress = ((b - 15) / 20).toFloat().coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = themeColors.primary,
                trackColor = themeColors.surface
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("15", "18.5", "24", "28", "35+").forEach {
                    Text(it, fontSize = 10.sp, color = themeColors.textMuted)
                }
            }
            Text(advice, fontSize = 13.sp, color = themeColors.text, lineHeight = 20.sp)
            Text("仅供参考，身体不适请及时就医", fontSize = 11.sp, color = themeColors.textMuted)
        }
    }
}
