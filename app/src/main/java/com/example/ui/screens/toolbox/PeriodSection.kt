package com.example.ui.screens.toolbox

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * v1.4.21 生理期记录全面升级：
 * - 记录扩展：流量（轻/中/重）+ 症状多选 + 心情 + 备注，支持编辑
 * - 智能预测：下次经期 / 排卵日 / 易孕期（周期长度可手动设或自动取平均）
 * - 状态横幅：经期中 / 距下次 X 天 / 排卵期 / 易孕期 / 安全期
 * - 经期日历：月视图标记实际经期·预测经期·排卵日·易孕期·今天，可翻月
 * - 统计：平均周期 / 平均经期 / 记录总数 / 周期规律性
 * - 设置：周期天数、经期天数、自动周期开关；导出分享文本
 * - 全套跟随主题色（经期红仅作日历语义色）
 */

private const val PREFS = "lzdz_period"
private const val KEY = "records"
private const val KEY_SETTINGS = "settings"

// 经期语义色（日历标记用）
private val PeriodRed = Color(0xFFE5486B)
private val PeriodRedLight = Color(0xFFFFD9E2)
private val FertileYellowLight = Color(0xFFFFF3D1)
private val OvulationPurple = Color(0xFF9B6BF3)

private data class PeriodRecord(
    val id: Long,
    val startDate: String,
    val endDate: String,
    val note: String,
    val flow: Int = 1,                    // 0=轻 1=中 2=重
    val symptoms: List<String> = emptyList(),
    val mood: Int = -1                    // -1=未记 0..4
)

private data class PeriodSettings(
    val cycleLen: Int = 28,               // 手动周期天数
    val periodLen: Int = 5,               // 经期天数
    val autoCycle: Boolean = true         // 自动用平均周期
)

private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
private val monthFmt = SimpleDateFormat("yyyy年M月", Locale.CHINA)

private val FlowLabels = listOf("量少", "适中", "量多")
private val MoodLabels = listOf("😢", "😟", "😐", "🙂", "😄")
private val MoodNames = listOf("低落", "烦躁", "平静", "不错", "开心")
private val SymptomOptions = listOf("痛经", "头痛", "腰酸", "乏力", "情绪波动", "失眠", "食欲大增", "乳房胀痛", "水肿", "长痘")

// ---------- 日期工具 ----------
private fun parseDate(s: String): Date? = try { dateFmt.parse(s) } catch (_: Exception) { null }
private fun fmtDate(d: Date): String = dateFmt.format(d)
private fun addDays(d: Date, n: Int): Date {
    val c = Calendar.getInstance().apply { time = d }
    c.add(Calendar.DAY_OF_MONTH, n)
    return c.time
}
private fun daysBetween(a: Date, b: Date): Int = ((b.time - a.time) / 86400000L).toInt()
private fun todayStart(): Date {
    val c = Calendar.getInstance()
    c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
    return c.time
}

// ---------- 持久化 ----------
private fun loadRecords(context: Context): List<PeriodRecord> {
    val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]") ?: "[]"
    val out = mutableListOf<PeriodRecord>()
    try {
        val arr = JSONArray(raw)
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val syms = mutableListOf<String>()
            val sa = o.optJSONArray("symptoms")
            if (sa != null) for (j in 0 until sa.length()) syms.add(sa.optString(j))
            out.add(
                PeriodRecord(
                    o.optLong("id"), o.optString("start"), o.optString("end"), o.optString("note"),
                    o.optInt("flow", 1), syms, o.optInt("mood", -1)
                )
            )
        }
    } catch (_: Exception) {}
    return out.sortedByDescending { it.startDate }
}

private fun saveRecords(context: Context, list: List<PeriodRecord>) {
    val arr = JSONArray()
    list.forEach { r ->
        val o = JSONObject()
        o.put("id", r.id); o.put("start", r.startDate); o.put("end", r.endDate)
        o.put("note", r.note); o.put("flow", r.flow); o.put("mood", r.mood)
        val sa = JSONArray(); r.symptoms.forEach { sa.put(it) }; o.put("symptoms", sa)
        arr.put(o)
    }
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, arr.toString()).apply()
}

private fun loadSettings(context: Context): PeriodSettings {
    val sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    return try {
        val o = JSONObject(sp.getString(KEY_SETTINGS, "{}") ?: "{}")
        PeriodSettings(o.optInt("cycleLen", 28), o.optInt("periodLen", 5), o.optBoolean("autoCycle", true))
    } catch (_: Exception) { PeriodSettings() }
}

private fun saveSettings(context: Context, s: PeriodSettings) {
    val o = JSONObject()
    o.put("cycleLen", s.cycleLen); o.put("periodLen", s.periodLen); o.put("autoCycle", s.autoCycle)
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY_SETTINGS, o.toString()).apply()
}

// ---------- 预测模型 ----------
private class PeriodForecast(
    val cycleLen: Int,
    val periodLen: Int,
    val nextStart: Date?,
    val ovulation: Date?,
    val fertileStart: Date?,
    val fertileEnd: Date?,
    val avgCycle: Int?,
    val avgPeriod: Int?,
    val regularity: String
)

private fun buildForecast(records: List<PeriodRecord>, settings: PeriodSettings): PeriodForecast {
    var avgCycle: Int? = null
    if (records.size >= 2) {
        val gaps = mutableListOf<Int>()
        val sorted = records.sortedBy { it.startDate }
        for (i in 1 until sorted.size) {
            val a = parseDate(sorted[i - 1].startDate); val b = parseDate(sorted[i].startDate)
            if (a != null && b != null) {
                val g = daysBetween(a, b)
                if (g in 15..60) gaps.add(g)
            }
        }
        if (gaps.isNotEmpty()) avgCycle = (gaps.average() + 0.5).toInt()
    }
    var avgPeriod: Int? = null
    run {
        val lens = records.mapNotNull { r ->
            val s = parseDate(r.startDate); val e = parseDate(r.endDate)
            if (s != null && e != null) { val d = daysBetween(s, e) + 1; if (d in 1..15) d else null } else null
        }
        if (lens.isNotEmpty()) avgPeriod = (lens.average() + 0.5).toInt()
    }
    val regularity = if (records.size >= 3 && avgCycle != null) {
        val sorted = records.sortedBy { it.startDate }
        val gaps = mutableListOf<Int>()
        for (i in 1 until sorted.size) {
            val a = parseDate(sorted[i - 1].startDate); val b = parseDate(sorted[i].startDate)
            if (a != null && b != null) { val g = daysBetween(a, b); if (g in 15..60) gaps.add(g) }
        }
        if (gaps.size >= 2) {
            val maxDev = gaps.maxOf { abs(it - avgCycle) }
            when {
                maxDev <= 2 -> "很规律"
                maxDev <= 5 -> "较规律"
                else -> "不太规律"
            }
        } else "数据不足"
    } else "数据不足"

    val cycleLen = if (settings.autoCycle && avgCycle != null) avgCycle else settings.cycleLen
    val periodLen = avgPeriod ?: settings.periodLen

    val lastStart = records.firstOrNull()?.let { parseDate(it.startDate) }
    val nextStart = lastStart?.let { addDays(it, cycleLen) }
    val ovulation = nextStart?.let { addDays(it, -14) }
    val fertileStart = ovulation?.let { addDays(it, -5) }
    val fertileEnd = ovulation?.let { addDays(it, 1) }
    return PeriodForecast(cycleLen, periodLen, nextStart, ovulation, fertileStart, fertileEnd, avgCycle, avgPeriod, regularity)
}

/** 当前阶段判断 */
private sealed class PhaseInfo(val title: String, val desc: String)
private class PhasePeriod(val day: Int, val total: Int) : PhaseInfo("经期第 $day 天", "好好休息，多喝热水")
private class PhaseBefore(val days: Int) : PhaseInfo(if (days == 0) "经期就在今天" else "距下次经期 $days 天", "经期将至，注意保暖")
private class PhaseOvulation : PhaseInfo("排卵日", "易孕期峰值")
private class PhaseFertile : PhaseInfo("易孕期", "受孕概率较高")
private class PhaseSafe : PhaseInfo("安全期", "相对安全，放松心情")
private class PhaseNone : PhaseInfo("开始记录吧", "添加第一条经期记录，开启智能预测")

private fun currentPhase(records: List<PeriodRecord>, fc: PeriodForecast, today: Date): PhaseInfo {
    if (records.isEmpty()) return PhaseNone()
    // 是否在某次实际经期内
    for (r in records) {
        val s = parseDate(r.startDate); val e = parseDate(r.endDate)
        if (s != null && e != null && !today.before(s) && !today.after(e)) {
            return PhasePeriod(daysBetween(s, today) + 1, daysBetween(s, e) + 1)
        }
    }
    val t = fmtDate(today)
    fc.nextStart?.let { ns ->
        val pe = addDays(ns, fc.periodLen - 1)
        if (!today.before(ns) && !today.after(pe)) return PhaseBefore(0)
        val d = daysBetween(today, ns)
        if (d in 1..7) return PhaseBefore(d)
    }
    fc.ovulation?.let { if (fmtDate(it) == t) return PhaseOvulation() }
    val fs = fc.fertileStart
    val fe = fc.fertileEnd
    if (fs != null && fe != null && !today.before(fs) && !today.after(fe)) {
        return PhaseFertile()
    }
    return PhaseSafe()
}

// ================= 主界面 =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val themeUi = LocalThemeUiColors.current
    var records by remember { mutableStateOf(loadRecords(context)) }
    var settings by remember { mutableStateOf(loadSettings(context)) }
    var showEditor by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<PeriodRecord?>(null) }
    var showSettings by remember { mutableStateOf(false) }
    var monthOffset by remember { mutableStateOf(0) }

    val today = remember { todayStart() }
    val fc = remember(records, settings) { buildForecast(records, settings) }
    val phase = remember(records, fc) { currentPhase(records, fc, today) }

    fun persist(r: List<PeriodRecord>) {
        records = r.sortedByDescending { it.startDate }
        saveRecords(context, records)
    }

    fun shareExport() {
        val sb = StringBuilder("🌸 我的生理期记录\n")
        sb.append("平均周期 ${fc.avgCycle?.let { "${it}天" } ?: "—"} · 平均经期 ${fc.avgPeriod?.let { "${it}天" } ?: "—"}\n")
        fc.nextStart?.let { sb.append("预计下次 ${fmtDate(it)}\n") }
        fc.ovulation?.let { sb.append("预计排卵 ${fmtDate(it)}\n") }
        sb.append("\n历史记录（${records.size} 条）：\n")
        records.sortedBy { it.startDate }.forEach { r ->
            sb.append("${r.startDate}→${r.endDate} 流量${FlowLabels.getOrElse(r.flow) { "?" }}")
            if (r.symptoms.isNotEmpty()) sb.append(" ${r.symptoms.joinToString("/")}")
            if (r.note.isNotBlank()) sb.append(" ${r.note}")
            sb.append("\n")
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"; putExtra(Intent.EXTRA_TEXT, sb.toString())
        }
        context.startActivity(Intent.createChooser(intent, "导出生理期记录"))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ---- 标题卡 ----
        Surface(
            color = themeUi.surface.copy(alpha = 0.9f),
            border = BorderStroke(1.dp, themeUi.primary.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("🌸 生理期记录", fontSize = 17.sp, fontWeight = FontWeight.Black, color = themeUi.text)
                    Text(
                        "经期日历 · 排卵预测 · 症状心情 · 周期统计",
                        fontSize = 11.sp, color = themeUi.textMuted, modifier = Modifier.padding(top = 2.dp)
                    )
                }
                IconButton(onClick = { showSettings = true }) {
                    Icon(Icons.Filled.Settings, contentDescription = "设置", tint = themeUi.primary)
                }
                IconButton(onClick = { shareExport() }) {
                    Icon(Icons.Filled.Share, contentDescription = "导出", tint = themeUi.primary)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { PhaseBanner(phase = phase) }

            item {
                Surface(
                    color = themeUi.surface.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, themeUi.primary.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        val latest = records.firstOrNull()
                        if (latest != null) {
                            val ls = parseDate(latest.startDate)
                            val dayInCycle = if (ls != null) (daysBetween(ls, today).coerceAtLeast(0) % fc.cycleLen) else 0
                            CycleRing(
                                progress = dayInCycle.toFloat() / fc.cycleLen.toFloat(),
                                dayInCycle = dayInCycle,
                                cycleLen = fc.cycleLen,
                                nextDate = fc.nextStart?.let { fmtDate(it) } ?: "—"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            DashboardCell("预计下次", fc.nextStart?.let { fmtDate(it) } ?: "—", Modifier.weight(1f))
                            DashboardCell("排卵日", fc.ovulation?.let { fmtDate(it) } ?: "—", Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            DashboardCell(
                                "平均周期",
                                (fc.avgCycle?.let { "${it}天" } ?: "${fc.cycleLen}天") +
                                        if (settings.autoCycle && fc.avgCycle != null) "(自动)" else "",
                                Modifier.weight(1f)
                            )
                            DashboardCell("规律性", fc.regularity, Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                PeriodCalendarCard(
                    records = records,
                    forecast = fc,
                    today = today,
                    monthOffset = monthOffset,
                    onMonthChange = { monthOffset += it }
                )
            }

            if (records.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(themeUi.surface.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .padding(vertical = 28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("还没有记录，点击下方「添加记录」开始吧～", fontSize = 12.sp, color = themeUi.textMuted)
                    }
                }
            } else {
                items(records, key = { it.id }) { r ->
                    RecordRow(
                        record = r,
                        onEdit = { editing = r; showEditor = true },
                        onDelete = { persist(records.filterNot { it.id == r.id }) }
                    )
                }
            }
        }

        Button(
            onClick = { editing = null; showEditor = true },
            colors = ButtonDefaults.buttonColors(containerColor = themeUi.primary, contentColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text("添加记录", fontWeight = FontWeight.Bold)
        }
    }

    if (showEditor) {
        PeriodEditorDialog(
            initial = editing,
            onDismiss = { showEditor = false },
            onSave = { rec ->
                persist(records.filterNot { it.id == rec.id } + rec)
                showEditor = false
            }
        )
    }

    if (showSettings) {
        PeriodSettingsDialog(
            initial = settings,
            onDismiss = { showSettings = false },
            onSave = {
                settings = it
                saveSettings(context, it)
                showSettings = false
                Toast.makeText(context, "设置已保存", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

/** 状态横幅 */
@Composable
private fun PhaseBanner(phase: PhaseInfo) {
    val themeUi = LocalThemeUiColors.current
    val (bg1, bg2) = when (phase) {
        is PhasePeriod -> PeriodRed to Color(0xFFFF8FB1)
        is PhaseBefore -> Color(0xFFFF9A3D) to Color(0xFFFFD66B)
        is PhaseOvulation -> OvulationPurple to Color(0xFFC4A5FF)
        is PhaseFertile -> Color(0xFFE8A93D) to Color(0xFFFFD66B)
        else -> themeUi.primary to themeUi.secondary
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(bg1, bg2)))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(phase.title, fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
            Text(phase.desc, fontSize = 11.5.sp, color = Color.White.copy(alpha = 0.9f), modifier = Modifier.padding(top = 2.dp))
        }
        Text("🌸", fontSize = 30.sp)
    }
}

/** 周期环（主题色版） */
@Composable
private fun CycleRing(progress: Float, dayInCycle: Int, cycleLen: Int, nextDate: String) {
    val themeUi = LocalThemeUiColors.current
    val transition = rememberInfiniteTransition(label = "cycle_ring")
    val sweep by transition.animateFloat(0f, 360f, infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart), label = "sweep")
    val dot by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart), label = "dot")
    val breathe by transition.animateFloat(0.92f, 1.06f, infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "breathe")

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(150.dp)) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val strokeWidth = 13.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val tl = Offset(center.x - radius, center.y - radius)
            val sz = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
            drawArc(
                color = themeUi.primary.copy(alpha = 0.15f),
                startAngle = -90f, sweepAngle = 360f, useCenter = false,
                topLeft = tl, size = sz, style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(themeUi.primary, themeUi.secondary, themeUi.primary), center = center),
                startAngle = -90f + sweep % 360f, sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false, topLeft = tl, size = sz,
                style = Stroke(width = strokeWidth * breathe, cap = StrokeCap.Round)
            )
            val dotAngle = (-90f + dot * 360f) * PI.toFloat() / 180f
            val dotPos = Offset(center.x + cos(dotAngle) * radius, center.y + sin(dotAngle) * radius)
            drawCircle(Color.White, strokeWidth * 0.55f, dotPos)
            drawCircle(themeUi.primary, strokeWidth * 0.32f, dotPos)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("第 $dayInCycle 天", fontSize = 19.sp, fontWeight = FontWeight.Black, color = themeUi.text)
            Text("${(progress * 100).toInt()}% · 周期 $cycleLen 天", fontSize = 10.sp, color = themeUi.textMuted)
            Text("预计 $nextDate", fontSize = 10.sp, color = themeUi.textMuted)
        }
    }
}

@Composable
private fun DashboardCell(label: String, value: String, modifier: Modifier = Modifier) {
    val themeUi = LocalThemeUiColors.current
    Surface(color = themeUi.surface, shape = RoundedCornerShape(12.dp), modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)) {
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = themeUi.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(label, fontSize = 10.sp, color = themeUi.textMuted)
        }
    }
}

/** 经期日历卡 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PeriodCalendarCard(
    records: List<PeriodRecord>,
    forecast: PeriodForecast,
    today: Date,
    monthOffset: Int,
    onMonthChange: (Int) -> Unit
) {
    val themeUi = LocalThemeUiColors.current
    val baseCal = Calendar.getInstance().apply { time = today; add(Calendar.MONTH, monthOffset) }
    val year = baseCal.get(Calendar.YEAR)
    val month = baseCal.get(Calendar.MONTH)

    val periodDays = mutableSetOf<String>()
    records.forEach { r ->
        val s = parseDate(r.startDate); val e = parseDate(r.endDate)
        if (s != null && e != null) {
            var d = s
            while (!d.after(e)) { periodDays.add(fmtDate(d)); d = addDays(d, 1) }
        }
    }
    val predictedDays = mutableSetOf<String>()
    forecast.nextStart?.let { ns ->
        var cur = ns
        repeat(3) {
            for (i in 0 until forecast.periodLen) predictedDays.add(fmtDate(addDays(cur, i)))
            cur = addDays(cur, forecast.cycleLen)
        }
    }
    val ovulationDays = mutableSetOf<String>()
    forecast.ovulation?.let { ov ->
        var cur = ov
        repeat(3) { ovulationDays.add(fmtDate(cur)); cur = addDays(cur, forecast.cycleLen) }
    }
    val fertileDays = mutableSetOf<String>()
    val fertileStart = forecast.fertileStart
    val fertileEnd = forecast.fertileEnd
    if (fertileStart != null && fertileEnd != null) {
        var baseS = fertileStart; var baseE = fertileEnd
        repeat(3) {
            var d = baseS
            while (!d.after(baseE)) { fertileDays.add(fmtDate(d)); d = addDays(d, 1) }
            baseS = addDays(baseS, forecast.cycleLen); baseE = addDays(baseE, forecast.cycleLen)
        }
    }
    fertileDays.removeAll(ovulationDays)

    val firstCal = Calendar.getInstance().apply { set(year, month, 1) }
    val offset = firstCal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = firstCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val todayStr = fmtDate(today)
    val weekTitles = listOf("日", "一", "二", "三", "四", "五", "六")

    Surface(
        color = themeUi.surface.copy(alpha = 0.75f),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, themeUi.primary.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { onMonthChange(-1) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "上月", tint = themeUi.primary)
                }
                Text(
                    monthFmt.format(firstCal.time),
                    fontSize = 15.sp, fontWeight = FontWeight.Black, color = themeUi.text,
                    modifier = Modifier.weight(1f), textAlign = TextAlign.Center
                )
                IconButton(onClick = { onMonthChange(1) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = "下月", tint = themeUi.primary)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                weekTitles.forEach {
                    Text(it, fontSize = 11.sp, color = themeUi.textMuted, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            for (row in 0 until 6) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val dayNum = row * 7 + col - offset + 1
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                            if (dayNum in 1..daysInMonth) {
                                val dc = Calendar.getInstance().apply { set(year, month, dayNum) }.time
                                val ds = fmtDate(dc)
                                val isPeriod = ds in periodDays
                                val isPredicted = !isPeriod && ds in predictedDays
                                val isOvulation = ds in ovulationDays
                                val isFertile = !isOvulation && ds in fertileDays
                                val isToday = ds == todayStr
                                val bg = when {
                                    isPeriod -> PeriodRed
                                    isOvulation -> OvulationPurple
                                    isFertile -> FertileYellowLight
                                    isPredicted -> PeriodRedLight
                                    else -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(bg)
                                        .then(
                                            if (isToday) Modifier.border(2.dp, themeUi.primary, CircleShape)
                                            else if (isPredicted) Modifier.border(1.dp, PeriodRed.copy(alpha = 0.5f), CircleShape)
                                            else Modifier
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$dayNum",
                                        fontSize = 12.sp,
                                        fontWeight = if (isPeriod || isOvulation || isToday) FontWeight.Black else FontWeight.Normal,
                                        color = if (isPeriod || isOvulation) Color.White else themeUi.text
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LegendDot(PeriodRed, "经期")
                LegendDot(PeriodRedLight, "预测经期", border = true)
                LegendDot(OvulationPurple, "排卵日")
                LegendDot(FertileYellowLight, "易孕期", border = true)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LegendDot(color: Color, label: String, border: Boolean = false) {
    val themeUi = LocalThemeUiColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
                .then(if (border) Modifier.border(1.dp, themeUi.primary.copy(alpha = 0.4f), CircleShape) else Modifier)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 11.sp, color = themeUi.textMuted)
    }
}

/** 记录行（可编辑/删除，展示流量症状心情） */
@Composable
private fun RecordRow(record: PeriodRecord, onEdit: () -> Unit, onDelete: () -> Unit) {
    val themeUi = LocalThemeUiColors.current
    var confirmDelete by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(themeUi.surface.copy(alpha = 0.85f))
            .border(1.dp, themeUi.primary.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PeriodRedLight),
            contentAlignment = Alignment.Center
        ) {
            Text("🌸", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("${record.startDate} → ${record.endDate}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = themeUi.text)
            Row(modifier = Modifier.padding(top = 3.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "🩸${FlowLabels.getOrElse(record.flow) { "?" }}",
                    fontSize = 10.5.sp, color = themeUi.textMuted,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(themeUi.primary.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
                if (record.mood in MoodLabels.indices) {
                    Text(
                        "${MoodLabels[record.mood]}${MoodNames[record.mood]}",
                        fontSize = 10.5.sp, color = themeUi.textMuted,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(themeUi.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            val extra = buildList {
                if (record.symptoms.isNotEmpty()) add(record.symptoms.joinToString("·"))
                if (record.note.isNotBlank()) add(record.note)
            }.joinToString(" | ")
            if (extra.isNotBlank()) {
                Text(extra, fontSize = 11.sp, color = themeUi.textMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 3.dp))
            }
        }
        IconButton(onClick = onEdit, modifier = Modifier.size(34.dp)) {
            Icon(Icons.Filled.Edit, contentDescription = "编辑", tint = themeUi.primary, modifier = Modifier.size(17.dp))
        }
        IconButton(onClick = { confirmDelete = true }, modifier = Modifier.size(34.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "删除", tint = Color(0xFFE53935), modifier = Modifier.size(17.dp))
        }
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("删除这条记录？", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = { Text("${record.startDate} → ${record.endDate}", fontSize = 13.sp) },
            confirmButton = {
                Button(
                    onClick = { confirmDelete = false; onDelete() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) { Text("删除") }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("取消") } }
        )
    }
}

/** 添加/编辑弹窗 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun PeriodEditorDialog(
    initial: PeriodRecord?,
    onDismiss: () -> Unit,
    onSave: (PeriodRecord) -> Unit
) {
    val themeUi = LocalThemeUiColors.current
    var showStartPicker by remember { mutableStateOf(initial == null) }
    var showEndPicker by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(initial?.startDate ?: "") }
    var endDate by remember { mutableStateOf(initial?.endDate ?: "") }
    var note by remember { mutableStateOf(initial?.note ?: "") }
    var flow by remember { mutableStateOf(initial?.flow ?: 1) }
    var symptoms by remember { mutableStateOf(initial?.symptoms ?: emptyList()) }
    var mood by remember { mutableStateOf(initial?.mood ?: -1) }
    var error by remember { mutableStateOf<String?>(null) }
    val todayMillis = System.currentTimeMillis()

    if (showStartPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = parseDate(startDate)?.time ?: todayMillis)
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false; if (initial == null && startDate.isBlank()) onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    startDate = fmtDate(Date(state.selectedDateMillis ?: todayMillis))
                    showStartPicker = false; showEndPicker = true
                }) { Text("确定") }
            },
            dismissButton = { TextButton(onClick = { showStartPicker = false; if (initial == null && startDate.isBlank()) onDismiss() }) { Text("取消") } }
        ) { DatePicker(state = state) }
    }
    if (showEndPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = parseDate(endDate)?.time ?: parseDate(startDate)?.time ?: todayMillis)
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endDate = fmtDate(Date(state.selectedDateMillis ?: todayMillis))
                    showEndPicker = false
                }) { Text("确定") }
            },
            dismissButton = { TextButton(onClick = { showEndPicker = false }) { Text("取消") } }
        ) { DatePicker(state = state) }
    }

    if (!showStartPicker && !showEndPicker) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(if (initial == null) "🌸 添加记录" else "🌸 编辑记录", fontWeight = FontWeight.Black, fontSize = 16.sp) },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, themeUi.primary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .clickable { showStartPicker = true }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text("开始\n${startDate.ifBlank { "选择" }}", fontSize = 12.sp, color = themeUi.text, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, themeUi.primary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                    .clickable { showEndPicker = true }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text("结束\n${endDate.ifBlank { "选择" }}", fontSize = 12.sp, color = themeUi.text, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                    item {
                        Text("经血量", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = themeUi.text)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FlowLabels.forEachIndexed { i, label ->
                                val sel = flow == i
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (sel) themeUi.primary else themeUi.primary.copy(alpha = 0.08f))
                                        .clickable { flow = i }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (sel) Color.White else themeUi.text)
                                }
                            }
                        }
                    }
                    item {
                        Text("症状（可多选）", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = themeUi.text)
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SymptomOptions.forEach { s ->
                                val sel = s in symptoms
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(if (sel) themeUi.primary else themeUi.primary.copy(alpha = 0.08f))
                                        .border(1.dp, themeUi.primary.copy(alpha = if (sel) 0f else 0.25f), RoundedCornerShape(50))
                                        .clickable { symptoms = if (sel) symptoms - s else symptoms + s }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(s, fontSize = 11.5.sp, color = if (sel) Color.White else themeUi.text)
                                }
                            }
                        }
                    }
                    item {
                        Text("心情", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = themeUi.text)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            MoodLabels.forEachIndexed { i, m ->
                                val sel = mood == i
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (sel) themeUi.primary.copy(alpha = 0.18f) else Color.Transparent)
                                        .border(1.dp, themeUi.primary.copy(alpha = if (sel) 0.6f else 0.15f), RoundedCornerShape(10.dp))
                                        .clickable { mood = if (sel) -1 else i }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(m, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                    item {
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it.take(60) },
                            label = { Text("备注（可选）", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    if (error != null) {
                        item { Text(error!!, fontSize = 11.sp, color = Color(0xFFE53935)) }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (startDate.isBlank() || endDate.isBlank()) { error = "请选择开始和结束日期"; return@Button }
                        val s = parseDate(startDate); val e = parseDate(endDate)
                        if (s == null || e == null) { error = "日期解析失败"; return@Button }
                        if (e.before(s)) { error = "结束日期不能早于开始日期"; return@Button }
                        onSave(
                            PeriodRecord(
                                id = initial?.id ?: System.currentTimeMillis(),
                                startDate = startDate, endDate = endDate, note = note.trim(),
                                flow = flow, symptoms = symptoms, mood = mood
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = themeUi.primary, contentColor = Color.White)
                ) { Text("保存", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } }
        )
    }
}

/** 设置弹窗 */
@Composable
private fun PeriodSettingsDialog(
    initial: PeriodSettings,
    onDismiss: () -> Unit,
    onSave: (PeriodSettings) -> Unit
) {
    val themeUi = LocalThemeUiColors.current
    var cycleLen by remember { mutableStateOf(initial.cycleLen.toString()) }
    var periodLen by remember { mutableStateOf(initial.periodLen.toString()) }
    var autoCycle by remember { mutableStateOf(initial.autoCycle) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("⚙️ 周期设置", fontWeight = FontWeight.Black, fontSize = 16.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("自动计算周期", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = themeUi.text)
                        Text("有足够记录时用平均周期预测", fontSize = 11.sp, color = themeUi.textMuted)
                    }
                    Switch(checked = autoCycle, onCheckedChange = { autoCycle = it })
                }
                OutlinedTextField(
                    value = cycleLen,
                    onValueChange = { cycleLen = it.filter { c -> c.isDigit() }.take(2) },
                    label = { Text("周期天数（手动）", fontSize = 12.sp) },
                    suffix = { Text("天", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !autoCycle
                )
                OutlinedTextField(
                    value = periodLen,
                    onValueChange = { periodLen = it.filter { c -> c.isDigit() }.take(2) },
                    label = { Text("经期天数", fontSize = 12.sp) },
                    suffix = { Text("天", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "说明：排卵日 = 下次经期前 14 天；易孕期 = 排卵日前 5 天至后 1 天。预测仅供参考，身体不适请及时就医。",
                    fontSize = 10.5.sp, color = themeUi.textMuted, lineHeight = 15.sp
                )
                if (error != null) Text(error!!, fontSize = 11.sp, color = Color(0xFFE53935))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val c = cycleLen.toIntOrNull(); val p = periodLen.toIntOrNull()
                    if (c == null || c !in 15..60) { error = "周期天数应在 15~60 天"; return@Button }
                    if (p == null || p !in 1..15) { error = "经期天数应在 1~15 天"; return@Button }
                    onSave(PeriodSettings(c, p, autoCycle))
                },
                colors = ButtonDefaults.buttonColors(containerColor = themeUi.primary, contentColor = Color.White)
            ) { Text("保存", fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } }
    )
}
