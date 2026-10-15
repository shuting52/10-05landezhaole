package com.example.ui.screens.toolbox

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import org.json.JSONArray
import org.json.JSONObject

/**
 * v1.4.22 新工具：倒数日 / 纪念日
 * - 添加重要日子（生日/考试/纪念日），自动计算剩余天数或已过去天数
 * - 本地持久化保存
 */
private data class CountDay(val title: String, val dateMillis: Long)

private const val COUNTDAY_PREFS = "lzdz_countdays"
private val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

private fun loadCountDays(context: Context): List<CountDay> {
    return try {
        val raw = context.getSharedPreferences(COUNTDAY_PREFS, Context.MODE_PRIVATE)
            .getString("list", "[]") ?: "[]"
        val arr = JSONArray(raw)
        (0 until arr.length()).map {
            val o = arr.getJSONObject(it)
            CountDay(o.getString("title"), o.getLong("date"))
        }.sortedBy { it.dateMillis }
    } catch (_: Exception) { emptyList() }
}

private fun saveCountDays(context: Context, list: List<CountDay>) {
    try {
        val arr = JSONArray()
        list.forEach { arr.put(JSONObject().put("title", it.title).put("date", it.dateMillis)) }
        context.getSharedPreferences(COUNTDAY_PREFS, Context.MODE_PRIVATE)
            .edit().putString("list", arr.toString()).apply()
    } catch (_: Exception) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownSection() {
    val themeColors = LocalThemeUiColors.current
    val context = LocalContext.current
    var days by remember { mutableStateOf(loadCountDays(context)) }
    var showAdd by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDate by remember { mutableStateOf<Long?>(null) }
    var showPicker by remember { mutableStateOf(false) }

    fun todayStart(): Long {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Event, contentDescription = null, tint = themeColors.primary, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("倒数日 · 纪念日", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { newTitle = ""; newDate = null; showAdd = true }) {
                Icon(Icons.Filled.Add, contentDescription = "添加", tint = themeColors.primary)
            }
        }

        if (showAdd) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("事件名称", fontSize = 12.sp) },
                    placeholder = { Text("如：生日、考试、发工资", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedButton(
                    onClick = { showPicker = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        newDate?.let { dateFmt.format(Date(it)) } ?: "选择日期",
                        fontSize = 13.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            if (newTitle.isBlank() || newDate == null) {
                                Toast.makeText(context, "请填写名称并选择日期", Toast.LENGTH_SHORT).show()
                            } else {
                                val updated = (days + CountDay(newTitle.trim(), newDate!!)).sortedBy { it.dateMillis }
                                days = updated
                                saveCountDays(context, updated)
                                showAdd = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = themeColors.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("保存", fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                    OutlinedButton(
                        onClick = { showAdd = false },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("取消", fontSize = 13.sp) }
                }
            }
        }

        if (showPicker) {
            val pickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showPicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        pickerState.selectedDateMillis?.let { newDate = it }
                        showPicker = false
                    }) { Text("确定") }
                },
                dismissButton = {
                    TextButton(onClick = { showPicker = false }) { Text("取消") }
                }
            ) { DatePicker(state = pickerState) }
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (days.isEmpty()) {
            Text("还没有倒数日，点击右上角 + 添加第一个重要日子", fontSize = 13.sp, color = themeColors.textMuted)
        } else {
            val today = todayStart()
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(days, key = { it.title + it.dateMillis }) { item ->
                    val diffDays = TimeUnit.MILLISECONDS.toDays(item.dateMillis - today).toInt()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = themeColors.text)
                            Text(dateFmt.format(Date(item.dateMillis)), fontSize = 11.sp, color = themeColors.textMuted)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                when {
                                    diffDays > 0 -> "还有 $diffDays 天"
                                    diffDays == 0 -> "就是今天！"
                                    else -> "已过去 ${-diffDays} 天"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = themeColors.primary
                            )
                        }
                        IconButton(onClick = {
                            val updated = days - item
                            days = updated
                            saveCountDays(context, updated)
                        }) {
                            Icon(Icons.Filled.DeleteOutline, contentDescription = "删除", tint = themeColors.textMuted, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
