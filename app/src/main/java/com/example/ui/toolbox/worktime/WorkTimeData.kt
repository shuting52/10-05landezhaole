package com.example.ui.toolbox.worktime

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/** 班次类型（对标 jigongshi） */
enum class ShiftType(val label: String) {
    NORMAL("正班"),
    OVERTIME("工作日加班"),
    WEEKEND("周末加班"),
    HOLIDAY("节假加班"),
    LEAVE("请假"),
    REST("休息")
}

/** 单日打卡记录 */
data class WorkRecord(
    val date: String, // yyyy-MM-dd
    val shiftType: ShiftType = ShiftType.NORMAL,
    val normalHours: Double = 8.0,
    val overtimeHours: Double = 0.0,
    val note: String = ""
) {
    fun totalHours(): Double = normalHours + overtimeHours
}

/** 薪资配置 */
data class SalaryConfig(
    val baseSalary: Double = 5000.0, // 月底薪
    val overtimeRate: Double = 1.5,   // 平时加班倍率
    val weekendRate: Double = 2.0,    // 周末倍率
    val holidayRate: Double = 3.0     // 节假倍率
) {
    /** 时薪 = 月薪 / 21.75 / 8 */
    fun hourly(): Double = if (baseSalary > 0) baseSalary / 21.75 / 8 else 0.0
}

/** 单日薪资估算 */
fun WorkRecord.estimatePay(config: SalaryConfig): Double {
    val h = config.hourly()
    if (h <= 0) return 0.0
    return when (shiftType) {
        ShiftType.NORMAL -> normalHours * h + overtimeHours * h * config.overtimeRate
        ShiftType.OVERTIME -> normalHours * h + overtimeHours * h * config.overtimeRate
        ShiftType.WEEKEND -> totalHours() * h * config.weekendRate
        ShiftType.HOLIDAY -> totalHours() * h * config.holidayRate
        ShiftType.LEAVE, ShiftType.REST -> 0.0
    }
}

/** SharedPreferences 持久化 */
object WorkTimeStore {
    private const val PREFS = "lzdz_worktime"
    private const val KEY_RECORDS = "records"
    private const val KEY_SALARY = "salary"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun loadRecords(context: Context): MutableMap<String, WorkRecord> {
        val map = mutableMapOf<String, WorkRecord>()
        try {
            val arr = JSONArray(prefs(context).getString(KEY_RECORDS, "[]") ?: "[]")
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val r = WorkRecord(
                    date = o.getString("date"),
                    shiftType = try { ShiftType.valueOf(o.optString("shiftType", "NORMAL")) }
                        catch (_: Exception) { ShiftType.NORMAL },
                    normalHours = o.optDouble("normalHours", 8.0),
                    overtimeHours = o.optDouble("overtimeHours", 0.0),
                    note = o.optString("note", "")
                )
                map[r.date] = r
            }
        } catch (_: Exception) { }
        return map
    }

    fun saveRecords(context: Context, records: Map<String, WorkRecord>) {
        try {
            val arr = JSONArray()
            records.values.forEach { r ->
                arr.put(JSONObject().apply {
                    put("date", r.date)
                    put("shiftType", r.shiftType.name)
                    put("normalHours", r.normalHours)
                    put("overtimeHours", r.overtimeHours)
                    put("note", r.note)
                })
            }
            prefs(context).edit().putString(KEY_RECORDS, arr.toString()).apply()
        } catch (_: Exception) { }
    }

    fun loadSalary(context: Context): SalaryConfig {
        return try {
            val o = JSONObject(prefs(context).getString(KEY_SALARY, "{}") ?: "{}")
            SalaryConfig(
                baseSalary = o.optDouble("baseSalary", 5000.0),
                overtimeRate = o.optDouble("overtimeRate", 1.5),
                weekendRate = o.optDouble("weekendRate", 2.0),
                holidayRate = o.optDouble("holidayRate", 3.0)
            )
        } catch (_: Exception) { SalaryConfig() }
    }

    fun saveSalary(context: Context, config: SalaryConfig) {
        try {
            val o = JSONObject().apply {
                put("baseSalary", config.baseSalary)
                put("overtimeRate", config.overtimeRate)
                put("weekendRate", config.weekendRate)
                put("holidayRate", config.holidayRate)
            }
            prefs(context).edit().putString(KEY_SALARY, o.toString()).apply()
        } catch (_: Exception) { }
    }
}

val dateFmt: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
val monthFmt: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年M月")

fun LocalDate.key(): String = format(dateFmt)
