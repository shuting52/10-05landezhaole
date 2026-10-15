package com.example.ui.screens.toolbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors

/**
 * v1.4.22 新工具：单位换算
 * - 长度 / 重量 / 温度三类，常用单位双向换算
 */
private data class UnitDef(val name: String, val toBase: Double) // toBase：换算到基准单位的系数

private val LengthUnits = listOf(
    UnitDef("千米 km", 1000.0), UnitDef("米 m", 1.0), UnitDef("分米 dm", 0.1),
    UnitDef("厘米 cm", 0.01), UnitDef("毫米 mm", 0.001), UnitDef("英里 mi", 1609.344),
    UnitDef("英尺 ft", 0.3048), UnitDef("英寸 in", 0.0254), UnitDef("里", 500.0), UnitDef("丈", 3.333333)
)
private val WeightUnits = listOf(
    UnitDef("吨 t", 1000.0), UnitDef("千克 kg", 1.0), UnitDef("克 g", 0.001),
    UnitDef("毫克 mg", 0.000001), UnitDef("斤", 0.5), UnitDef("两", 0.05),
    UnitDef("磅 lb", 0.45359237), UnitDef("盎司 oz", 0.028349523125)
)

@Composable
fun UnitConverterSection() {
    val themeColors = LocalThemeUiColors.current
    var category by remember { mutableStateOf(0) } // 0长度 1重量 2温度
    var input by remember { mutableStateOf("1") }
    var fromIdx by remember { mutableStateOf(1) }
    var toIdx by remember { mutableStateOf(3) }
    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    val categories = listOf("长度", "重量", "温度")
    val tempUnits = listOf("摄氏度 ℃", "华氏度 ℉", "开尔文 K")

    fun convertTemp(v: Double, from: Int, to: Int): Double {
        val c = when (from) { 0 -> v; 1 -> (v - 32) * 5 / 9; else -> v - 273.15 }
        return when (to) { 0 -> c; 1 -> c * 9 / 5 + 32; else -> c + 273.15 }
    }

    val result: String = run {
        val v = input.toDoubleOrNull() ?: return@run "—"
        if (category == 2) {
            val r = convertTemp(v, fromIdx.coerceIn(0, 2), toIdx.coerceIn(0, 2))
            "%.4f".format(r).trimEnd('0').trimEnd('.')
        } else {
            val units = if (category == 0) LengthUnits else WeightUnits
            val f = units[fromIdx.coerceIn(units.indices)]
            val t = units[toIdx.coerceIn(units.indices)]
            val r = v * f.toBase / t.toBase
            "%.6f".format(r).trimEnd('0').trimEnd('.')
        }
    }

    @Composable
    fun UnitPicker(label: String, units: List<String>, selected: Int, expanded: Boolean, onExpand: (Boolean) -> Unit, onPick: (Int) -> Unit) {
        Box {
            OutlinedButton(
                onClick = { onExpand(true) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("$label：${units[selected.coerceIn(units.indices)]}", fontSize = 13.sp, maxLines = 1)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { onExpand(false) }) {
                units.forEachIndexed { i, u ->
                    DropdownMenuItem(text = { Text(u, fontSize = 13.sp) }, onClick = { onPick(i); onExpand(false) })
                }
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
            Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("单位换算", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEachIndexed { i, c ->
                val selected = category == i
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            category = i; fromIdx = 0; toIdx = 1
                            fromExpanded = false; toExpanded = false
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        c, fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) themeColors.primary else themeColors.textMuted
                    )
                }
            }
        }

        OutlinedTextField(
            value = input,
            onValueChange = { input = it.filter { ch -> ch.isDigit() || ch == '.' || ch == '-' } },
            label = { Text("输入数值", fontSize = 12.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        if (category == 2) {
            UnitPicker("从", tempUnits, fromIdx, fromExpanded, { fromExpanded = it }) { fromIdx = it }
            UnitPicker("到", tempUnits, toIdx, toExpanded, { toExpanded = it }) { toIdx = it }
        } else {
            val units = if (category == 0) LengthUnits else WeightUnits
            val names = units.map { it.name }
            UnitPicker("从", names, fromIdx, fromExpanded, { fromExpanded = it }) { fromIdx = it }
            UnitPicker("到", names, toIdx, toExpanded, { toExpanded = it }) { toIdx = it }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text("换算结果", fontSize = 12.sp, color = themeColors.textMuted)
        Text(
            text = result,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = themeColors.primary
        )
    }
}
