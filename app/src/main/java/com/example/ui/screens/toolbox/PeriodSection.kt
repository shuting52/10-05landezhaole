package com.example.ui.screens.toolbox

import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * v1.1.18 新工具：生理期记录
 *
 * - 添加记录：开始日期 / 结束日期 / 备注
 * - 保存 / 删除，本地持久化
 * - 看板：最近记录、周期天数、预计下次日期
 */

private const val PREFS = "lzdz_period"
private const val KEY = "records"

private data class PeriodRecord(val id: Long, val startDate: String, val endDate: String, val note: String)

private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)

private fun loadRecords(context: Context): List<PeriodRecord> {
    val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]") ?: "[]"
    val out = mutableListOf<PeriodRecord>()
    try {
        val arr = JSONArray(raw)
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            out.add(PeriodRecord(o.optLong("id"), o.optString("start"), o.optString("end"), o.optString("note")))
        }
    } catch (_: Exception) {}
    return out.sortedByDescending { it.startDate }
}

private fun saveRecords(context: Context, list: List<PeriodRecord>) {
    val arr = JSONArray()
    list.forEach { r ->
        val o = JSONObject()
        o.put("id", r.id)
        o.put("start", r.startDate)
        o.put("end", r.endDate)
        o.put("note", r.note)
        arr.put(o)
    }
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, arr.toString()).apply()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var records by remember { mutableStateOf(loadRecords(context)) }
    var showAddDialog by remember { mutableStateOf(false) }

    // 看板数据
    val latest = records.firstOrNull()
    val periodDays = latest?.let { r ->
        try {
            val s = dateFmt.parse(r.startDate)
            val e = dateFmt.parse(r.endDate)
            if (s != null && e != null) {
                ((e.time - s.time) / 86400000L).toInt().coerceAtLeast(1)
            } else null
        } catch (_: Exception) { null }
    }
    val nextDate = latest?.let { r ->
        try {
            val s = dateFmt.parse(r.startDate)
            if (s != null) {
                val cal = Calendar.getInstance().apply { time = s }
                cal.add(Calendar.DAY_OF_MONTH, 28)
                dateFmt.format(cal.time)
            } else null
        } catch (_: Exception) { null }
    }
    val avgCycle = if (records.size >= 2) {
        // 相邻两条开始日期间隔平均
        val gaps = mutableListOf<Long>()
        for (i in 1 until records.size) {
            try {
                val a = dateFmt.parse(records[i - 1].startDate)
                val b = dateFmt.parse(records[i].startDate)
                if (a != null && b != null) {
                    gaps.add((a.time - b.time) / 86400000L)
                }
            } catch (_: Exception) {}
        }
        if (gaps.isNotEmpty()) gaps.average().toInt() else null
    } else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 标题卡
        Surface(
            color = Color(0xFFFFFDF9).copy(alpha = 0.9f),
            border = BorderStroke(1.dp, Color(0xFFF472B6).copy(alpha = 0.6f)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("? 生理期记录", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFFDB2777))
                Text("开始/结束日期 · 备注 · 保存删除 · 周期看板", fontSize = 11.sp, color = Color(0xFF9A5A7A), modifier = Modifier.padding(top = 2.dp))
            }
        }

        // 看板：动态环形周期图 + 数据卡
        val cycleLen = if (avgCycle != null) avgCycle else 28
        val daysSinceStart = latest?.let { r ->
            try {
                val s = dateFmt.parse(r.startDate)
                if (s != null) {
                    ((System.currentTimeMillis() - s.time) / 86400000L).toInt().coerceAtLeast(0)
                } else 0
            } catch (_: Exception) { 0 }
        } ?: 0
        val dayInCycle = daysSinceStart % cycleLen
        val cycleProgress = dayInCycle.toFloat() / cycleLen.toFloat()

        Surface(
            color = Color(0xFFFFF1F7),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Color(0xFFF9A8D4).copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("? 周期看板", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color(0xFFDB2777))
                Spacer(modifier = Modifier.height(10.dp))
                // v1.1.24：动态环形周期图（渐变流动弧 + 移动光点动画）
                if (latest != null) {
                    CycleRing(
                        progress = cycleProgress,
                        dayInCycle = dayInCycle,
                        cycleLen = cycleLen,
                        nextDate = nextDate ?: "—"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DashboardCell("最近开始", latest?.startDate ?: "—", Modifier.weight(1f))
                    DashboardCell("周期天数", periodDays?.toString() ?: "—", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DashboardCell("预计下次", nextDate ?: "—", Modifier.weight(1f))
                    DashboardCell("平均周期", if (avgCycle != null) "${avgCycle} 天" else "—", Modifier.weight(1f))
                }
            }
        }

        // 记录列表
        if (records.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFFFF7FB), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("还没有记录，点击下方「添加记录」开始记录吧～", fontSize = 12.sp, color = Color(0xFFB08A9A))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(records, key = { it.id }) { r ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFF9A8D4).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color(0xFFFFE9F2), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("?", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${r.startDate} → ${r.endDate}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A1F2E))
                            if (r.note.isNotBlank()) {
                                Text(r.note, fontSize = 11.sp, color = Color(0xFF9A5A7A), maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
                            }
                        }
                        IconButton(onClick = {
                            records = records.filterNot { it.id == r.id }
                            saveRecords(context, records)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Color(0xFFE53935), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // 添加按钮
        Button(
            onClick = { showAddDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB2777)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text("添加记录", fontWeight = FontWeight.Bold)
        }
    }

    if (showAddDialog) {
        PeriodAddDialog(
            onDismiss = { showAddDialog = false },
            onSave = { start, end, note ->
                val newRec = PeriodRecord(System.currentTimeMillis(), start, end, note)
                records = (records + newRec).sortedByDescending { it.startDate }
                saveRecords(context, records)
                showAddDialog = false
            }
        )
    }
}

/**
 * v1.1.24：动态环形周期图——渐变流动弧 + 移动光点动画
 * 显示当前处于周期第几天、进度百分比与预计下次日期
 */
@Composable
private fun CycleRing(
    progress: Float,
    dayInCycle: Int,
    cycleLen: Int,
    nextDate: String
) {
    val transition = rememberInfiniteTransition(label = "cycle_ring")
    // 弧线渐变角度缓慢旋转（流动感）
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycle_sweep"
    )
    // 光点沿弧线移动
    val dot by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycle_dot"
    )
    // 呼吸
    val breathe by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cycle_breathe"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val strokeWidth = 13.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val startAngle = -90f
            val sweepAngle = 360f * progress.coerceIn(0f, 1f)

            // 背景环
            drawArc(
                color = Color(0xFFFFDDE8),
                startAngle = startAngle,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            // 渐变流动进度弧
            val brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFFFF8FB1),
                    Color(0xFFF472B6),
                    Color(0xFFDB2777),
                    Color(0xFFFF8FB1)
                ),
                center = center
            )
            drawArc(
                brush = brush,
                startAngle = startAngle + sweep % 360f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth * breathe, cap = StrokeCap.Round)
            )
            // 移动光点
            val dotAngle = (-90f + dot * 360f) * PI.toFloat() / 180f
            val dotR = radius
            val dotPos = Offset(
                center.x + cos(dotAngle) * dotR,
                center.y + sin(dotAngle) * dotR
            )
            drawCircle(
                color = Color.White,
                radius = strokeWidth * 0.55f,
                center = dotPos
            )
            drawCircle(
                color = Color(0xFFDB2777),
                radius = strokeWidth * 0.32f,
                center = dotPos
            )
        }
        // 中心信息
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "第 $dayInCycle 天",
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFDB2777)
            )
            Text(
                "${(progress * 100).toInt()}% · 周期 ${cycleLen} 天",
                fontSize = 10.sp,
                color = Color(0xFFB06A8A)
            )
            Text(
                "预计 $nextDate",
                fontSize = 10.sp,
                color = Color(0xFFB06A8A)
            )
        }
    }
}

@Composable
private fun DashboardCell(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White.copy(alpha = 0.7f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFFDB2777))
            Text(label, fontSize = 10.sp, color = Color(0xFF9A5A7A))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodAddDialog(
    onDismiss: () -> Unit,
    onSave: (start: String, end: String, note: String) -> Unit
) {
    var showStartPicker by remember { mutableStateOf(true) }
    var showEndPicker by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val todayMillis = System.currentTimeMillis()

    // 开始日期选择
    if (showStartPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = todayMillis)
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false; onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    val ms = state.selectedDateMillis ?: todayMillis
                    startDate = dateFmt.format(Date(ms))
                    showStartPicker = false
                    showEndPicker = true
                }) { Text("确定") }
            },
            dismissButton = { TextButton(onClick = { showStartPicker = false; onDismiss() }) { Text("取消") } }
        ) { DatePicker(state = state) }
    }

    // 结束日期选择
    if (showEndPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = todayMillis)
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false; onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    val ms = state.selectedDateMillis ?: todayMillis
                    endDate = dateFmt.format(Date(ms))
                    showEndPicker = false
                }) { Text("确定") }
            },
            dismissButton = { TextButton(onClick = { showEndPicker = false; onDismiss() }) { Text("取消") } }
        ) { DatePicker(state = state) }
    }

    // 保存弹窗
    if (!showStartPicker && !showEndPicker) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("?", fontSize = 18.sp)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("保存生理期记录", fontWeight = FontWeight.Black)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // 开始日期（点击重新选）
                    OutlinedTextField(
                        value = if (startDate.isBlank()) "请选择开始日期" else startDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("开始日期") },
                        trailingIcon = { Text("?", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showStartPicker = true }
                    )
                    // 结束日期
                    OutlinedTextField(
                        value = if (endDate.isBlank()) "请选择结束日期" else endDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("结束日期") },
                        trailingIcon = { Text("?", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showEndPicker = true }
                    )
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it.take(60) },
                        label = { Text("备注（可选）") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (error != null) {
                        Text(error!!, fontSize = 11.sp, color = Color(0xFFE53935))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (startDate.isBlank() || endDate.isBlank()) {
                            error = "请选择开始和结束日期"
                            return@Button
                        }
                        try {
                            val s = dateFmt.parse(startDate)
                            val e = dateFmt.parse(endDate)
                            if (s == null || e == null) {
                                error = "日期解析失败"
                                return@Button
                            }
                            if (e.time < s.time) {
                                error = "结束日期不能早于开始日期"
                                return@Button
                            }
                        } catch (_: Exception) {
                            error = "日期格式错误"
                            return@Button
                        }
                        onSave(startDate, endDate, note.trim())
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDB2777))
                ) { Text("保存", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("取消") }
            }
        )
    }
}
