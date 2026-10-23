package com.example.ui.toolbox.worktime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import java.time.YearMonth

// ================= 统计页 =================

@Composable
fun StatsTab(records: Map<String, WorkRecord>, salary: SalaryConfig) {
    val theme = LocalThemeUiColors.current
    var month by remember { mutableStateOf(YearMonth.now()) }

    val monthRecords = records.values
        .filter { it.date.startsWith(month.toString().substring(0, 7)) }
        .sortedBy { it.date }

    val workDays = monthRecords.count { it.shiftType != ShiftType.LEAVE && it.shiftType != ShiftType.REST }
    val totalHours = monthRecords.sumOf { it.totalHours() }
    val overtimeHours = monthRecords.sumOf { it.overtimeHours }
    val totalPay = monthRecords.sumOf { it.estimatePay(salary) }
    val leaveDays = monthRecords.count { it.shiftType == ShiftType.LEAVE }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            // 月份切换
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { month = month.minusMonths(1) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "上月", tint = theme.primary)
                }
                Text(
                    month.format(monthFmt),
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = theme.text,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                IconButton(onClick = { month = month.plusMonths(1) }) {
                    Icon(Icons.Filled.ArrowForward, contentDescription = "下月", tint = theme.primary)
                }
            }
        }
        item {
            // 汇总卡
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(theme.primary)
                    .padding(16.dp)
            ) {
                Text("本月预估薪资", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                Text(
                    "¥${String.format("%.0f", totalPay)}",
                    fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatMini("出勤", "${workDays}天")
                    StatMini("总工时", "${String.format("%.1f", totalHours)}h")
                    StatMini("加班", "${String.format("%.1f", overtimeHours)}h")
                    StatMini("请假", "${leaveDays}天")
                }
            }
        }
        if (monthRecords.isEmpty()) {
            item {
                Text(
                    "本月暂无打卡记录",
                    fontSize = 13.sp, color = theme.textMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                )
            }
        } else {
            items(monthRecords) { r ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(theme.text.copy(alpha = 0.04f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(r.date, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.text)
                        Text(
                            "${r.shiftType.label} · ${String.format("%.1f", r.totalHours())}h" +
                                if (r.note.isNotBlank()) " · ${r.note}" else "",
                            fontSize = 11.5.sp, color = theme.textMuted
                        )
                    }
                    Text(
                        "¥${String.format("%.0f", r.estimatePay(salary))}",
                        fontSize = 14.sp, fontWeight = FontWeight.Bold, color = theme.primary
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
}

@Composable
private fun StatMini(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f))
    }
}

// ================= 设置页 =================

@Composable
fun SettingsTab(salary: SalaryConfig, onSave: (SalaryConfig) -> Unit) {
    val theme = LocalThemeUiColors.current
    var base by remember(salary) { mutableStateOf(salary.baseSalary.toString()) }
    var otRate by remember(salary) { mutableStateOf(salary.overtimeRate.toString()) }
    var weRate by remember(salary) { mutableStateOf(salary.weekendRate.toString()) }
    var hoRate by remember(salary) { mutableStateOf(salary.holidayRate.toString()) }
    var saved by remember { mutableStateOf(false) }

    fun num(s: String) = s.toDoubleOrNull() ?: 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(theme.text.copy(alpha = 0.04f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("薪资设置", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = theme.text)
                Text(
                    "时薪 = 月底薪 ÷ 21.75 ÷ 8",
                    fontSize = 11.5.sp, color = theme.textMuted
                )
                SalaryField("月底薪（元）", base, { base = it.filter { c -> c.isDigit() || c == '.' } })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SalaryField("平时加班倍率", otRate, { otRate = it.filter { c -> c.isDigit() || c == '.' } }, Modifier.weight(1f))
                    SalaryField("周末倍率", weRate, { weRate = it.filter { c -> c.isDigit() || c == '.' } }, Modifier.weight(1f))
                    SalaryField("节假倍率", hoRate, { hoRate = it.filter { c -> c.isDigit() || c == '.' } }, Modifier.weight(1f))
                }
                Button(
                    onClick = {
                        onSave(
                            SalaryConfig(
                                baseSalary = num(base),
                                overtimeRate = num(otRate).takeIf { it > 0 } ?: 1.5,
                                weekendRate = num(weRate).takeIf { it > 0 } ?: 2.0,
                                holidayRate = num(hoRate).takeIf { it > 0 } ?: 3.0
                            )
                        )
                        saved = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("保存设置", fontWeight = FontWeight.Bold)
                }
                if (saved) {
                    Text(
                        "已保存 ✓",
                        fontSize = 12.sp, color = Color(0xFF16A34A),
                        textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(theme.text.copy(alpha = 0.04f))
                    .padding(16.dp)
            ) {
                Text("计薪规则", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = theme.text)
                Spacer(modifier = Modifier.height(6.dp))
                listOf(
                    "正班：正班时长 × 时薪 ＋ 加班时长 × 时薪 × 平时倍率",
                    "周末加班：总工时 × 时薪 × 周末倍率",
                    "节假加班：总工时 × 时薪 × 节假倍率",
                    "请假 / 休息：不计薪"
                ).forEach {
                    Text("· $it", fontSize = 12.sp, color = theme.textMuted, lineHeight = 20.sp)
                }
            }
        }
        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
}

@Composable
private fun SalaryField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}
