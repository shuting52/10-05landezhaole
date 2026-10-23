package com.example.ui.toolbox.worktime

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import java.time.LocalDate
import java.time.YearMonth

/** 工时打卡（移植自 shuting52/jigongshi 核心功能） */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkTimeScreen(onClose: () -> Unit) {
    val context = LocalContext.current
    val theme = LocalThemeUiColors.current
    var tab by remember { mutableStateOf(0) }
    var records by remember { mutableStateOf(WorkTimeStore.loadRecords(context)) }
    var salary by remember { mutableStateOf(WorkTimeStore.loadSalary(context)) }

    fun persistRecords() = WorkTimeStore.saveRecords(context, records)

    Column(modifier = Modifier.fillMaxSize()) {
        // 顶栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "关闭", tint = theme.text)
            }
            Text(
                "工时打卡",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = theme.text,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(48.dp))
        }
        // 页签
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(theme.text.copy(alpha = 0.06f))
                .padding(4.dp)
        ) {
            listOf("打卡", "统计", "设置").forEachIndexed { i, label ->
                val selected = tab == i
                Surface(
                    shape = RoundedCornerShape(9.dp),
                    color = if (selected) theme.primary else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { tab = i }
                ) {
                    Text(
                        label,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) Color.White else theme.textMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 9.dp)
                    )
                }
            }
        }
        when (tab) {
            0 -> CheckInTab(
                records = records,
                onSave = { r -> records = records.toMutableMap().also { it[r.date] = r }; persistRecords() },
                onDelete = { date -> records = records.toMutableMap().also { it.remove(date) }; persistRecords() }
            )
            1 -> StatsTab(records = records, salary = salary)
            2 -> SettingsTab(
                salary = salary,
                onSave = { salary = it; WorkTimeStore.saveSalary(context, it) }
            )
        }
    }
}

/** 班次颜色 */
fun ShiftType.color(): Color = when (this) {
    ShiftType.NORMAL -> Color(0xFF1257C9)
    ShiftType.OVERTIME -> Color(0xFFF59E0B)
    ShiftType.WEEKEND -> Color(0xFF8B5CF6)
    ShiftType.HOLIDAY -> Color(0xFFEF4444)
    ShiftType.LEAVE -> Color(0xFF6E7887)
    ShiftType.REST -> Color(0xFF9AA5B1)
}

// ================= 打卡页 =================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckInTab(
    records: Map<String, WorkRecord>,
    onSave: (WorkRecord) -> Unit,
    onDelete: (String) -> Unit
) {
    val theme = LocalThemeUiColors.current
    var month by remember { mutableStateOf(YearMonth.now()) }
    var editDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp)) {
        // 月份切换
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
        // 星期头
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("一", "二", "三", "四", "五", "六", "日").forEach {
                Text(
                    it, fontSize = 12.sp, color = theme.textMuted,
                    textAlign = TextAlign.Center, modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        // 日历格
        val firstDay = month.atDay(1)
        val offset = (firstDay.dayOfWeek.value - 1) // 周一起始
        val daysInMonth = month.lengthOfMonth()
        val cells = (0 until offset + daysInMonth).toList()
        cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { idx ->
                    val dayNum = idx - offset + 1
                    if (dayNum < 1 || dayNum > daysInMonth) {
                        Spacer(modifier = Modifier.weight(1f).padding(2.dp))
                    } else {
                        val date = month.atDay(dayNum)
                        val rec = records[date.key()]
                        val isToday = date == LocalDate.now()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        rec != null -> rec.shiftType.color().copy(alpha = 0.16f)
                                        isToday -> theme.primary.copy(alpha = 0.10f)
                                        else -> Color.Transparent
                                    }
                                )
                                .border(
                                    1.dp,
                                    if (isToday) theme.primary else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { editDate = date }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "$dayNum",
                                    fontSize = 14.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = theme.text
                                )
                                if (rec != null) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(rec.shiftType.color())
                                    )
                                } else {
                                    Spacer(modifier = Modifier.size(6.dp))
                                }
                            }
                        }
                    }
                }
                // 补齐空位
                repeat(7 - week.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // 图例
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ShiftType.values().take(4).forEach { st ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(st.color()))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(st.label, fontSize = 11.sp, color = theme.textMuted)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("点日期记录打卡 · 长按可多选（单条编辑）", fontSize = 11.sp, color = theme.textMuted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }

    // 编辑弹窗
    editDate?.let { date ->
        RecordSheet(
            date = date,
            existing = records[date.key()],
            onSave = { onSave(it); editDate = null },
            onDelete = { onDelete(date.key()); editDate = null },
            onDismiss = { editDate = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordSheet(
    date: LocalDate,
    existing: WorkRecord?,
    onSave: (WorkRecord) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val theme = LocalThemeUiColors.current
    var shift by remember { mutableStateOf(existing?.shiftType ?: ShiftType.NORMAL) }
    var normalH by remember { mutableStateOf(existing?.normalHours?.toString() ?: "8") }
    var overtimeH by remember { mutableStateOf(existing?.overtimeHours?.toString() ?: "0") }
    var note by remember { mutableStateOf(existing?.note ?: "") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(bottom = 28.dp)
        ) {
            Text(
                "${date.format(dateFmt)} 打卡",
                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = theme.text
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("班次类型", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = theme.textMuted)
            Spacer(modifier = Modifier.height(8.dp))
            // 班次选择（3列）
            ShiftType.values().toList().chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { st ->
                        val selected = shift == st
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) st.color() else theme.text.copy(alpha = 0.05f),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { shift = st }
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (selected) {
                                    Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    st.label, fontSize = 12.5.sp, fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else theme.text
                                )
                            }
                        }
                    }
                    repeat(3 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = normalH,
                    onValueChange = { normalH = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("正班时长(小时)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = overtimeH,
                    onValueChange = { overtimeH = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("加班时长(小时)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("备注（可选）") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (existing != null) {
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.DeleteOutline, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("删除")
                    }
                }
                Button(
                    onClick = {
                        onSave(
                            WorkRecord(
                                date = date.key(),
                                shiftType = shift,
                                normalHours = normalH.toDoubleOrNull() ?: 0.0,
                                overtimeHours = overtimeH.toDoubleOrNull() ?: 0.0,
                                note = note
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primary),
                    modifier = Modifier.weight(2f)
                ) {
                    Text("保存打卡", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
