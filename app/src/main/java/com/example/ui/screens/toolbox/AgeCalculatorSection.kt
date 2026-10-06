package com.example.ui.screens.toolbox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.FlameRed
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SunsetOrange
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val CHINESE_ZODIAC = arrayOf("鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪")
private val HEAVENLY_STEMS = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
private val EARTHLY_BRANCHES = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

/** 根据出生月/日精准判断西方星座 */
fun getWesternZodiac(month: Int, day: Int): String {
    val m = month.coerceIn(1, 12)
    val d = day.coerceIn(1, 31)
    return when {
        (m == 1 && d >= 20) || (m == 2 && d <= 18) -> "♒ 水瓶座"
        (m == 2 && d >= 19) || (m == 3 && d <= 20) -> "♓ 双鱼座"
        (m == 3 && d >= 21) || (m == 4 && d <= 19) -> "♈ 白羊座"
        (m == 4 && d >= 20) || (m == 5 && d <= 20) -> "♉ 金牛座"
        (m == 5 && d >= 21) || (m == 6 && d <= 21) -> "♊ 双子座"
        (m == 6 && d >= 22) || (m == 7 && d <= 22) -> "♋ 巨蟹座"
        (m == 7 && d >= 23) || (m == 8 && d <= 22) -> "♌ 狮子座"
        (m == 8 && d >= 23) || (m == 9 && d <= 22) -> "♍ 处女座"
        (m == 9 && d >= 23) || (m == 10 && d <= 23) -> "♎ 天秤座"
        (m == 10 && d >= 24) || (m == 11 && d <= 22) -> "♏ 天蝎座"
        (m == 11 && d >= 23) || (m == 12 && d <= 21) -> "♐ 射手座"
        else -> "♑ 摩羯座"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeCalculatorSection(modifier: Modifier = Modifier) {
    var birthYear by remember { mutableIntStateOf(2000) }
    var birthMonth by remember { mutableIntStateOf(1) } // 1-12
    var birthDay by remember { mutableIntStateOf(1) }
    var showDatePicker by remember { mutableStateOf(false) }

    val currentYear = remember { Calendar.getInstance().get(Calendar.YEAR) }
    var inputAgeText by remember { mutableStateOf((currentYear - 2000).toString()) }
    // 出生月/日文本输入（独立字符串 state，允许删除数字至空，修复“删不了数字”问题）
    var monthText by remember { mutableStateOf(birthMonth.toString()) }
    var dayText by remember { mutableStateOf(birthDay.toString()) }
    // 当通过日期选择器/其它方式改变出生月/日时，同步文本
    LaunchedEffect(birthMonth) { if (birthMonth != (monthText.toIntOrNull() ?: 0)) monthText = birthMonth.toString() }
    LaunchedEffect(birthDay) { if (birthDay != (dayText.toIntOrNull() ?: 0)) dayText = birthDay.toString() }

    val applyAge = { ageVal: Int ->
        val safe = ageVal.coerceIn(0, 150)
        inputAgeText = safe.toString()
        birthYear = Calendar.getInstance().get(Calendar.YEAR) - safe
    }

    // 星座（由出生月/日精准判断）
    val zodiacSign = remember(birthMonth, birthDay) {
        getWesternZodiac(birthMonth, birthDay)
    }

    var currentMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // 每秒刷新一次活过的秒数
    LaunchedEffect(Unit) {
        while (true) {
            currentMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    val birthCalendar = remember(birthYear, birthMonth, birthDay) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, birthYear)
            set(Calendar.MONTH, birthMonth - 1)
            set(Calendar.DAY_OF_MONTH, birthDay)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    val birthMillis = birthCalendar.timeInMillis
    val livedMillis = (currentMillis - birthMillis).coerceAtLeast(0L)
    val livedSeconds = livedMillis / 1000
    val livedDays = livedSeconds / 86400
    val livedHours = livedSeconds / 3600
    val livedMinutes = livedSeconds / 60

    // 精确年月日推算
    val nowCalendar = Calendar.getInstance().apply { timeInMillis = currentMillis }
    var calcYears = nowCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
    var calcMonths = nowCalendar.get(Calendar.MONTH) - birthCalendar.get(Calendar.MONTH)
    var calcDays = nowCalendar.get(Calendar.DAY_OF_MONTH) - birthCalendar.get(Calendar.DAY_OF_MONTH)

    if (calcDays < 0) {
        val prevMonth = (nowCalendar.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
        calcDays += prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        calcMonths--
    }
    if (calcMonths < 0) {
        calcMonths += 12
        calcYears--
    }
    calcYears = calcYears.coerceAtLeast(0)

    // 生肖与干支纪年
    val zodiac = remember(birthYear) {
        val index = (birthYear - 4) % 12
        CHINESE_ZODIAC[if (index < 0) index + 12 else index]
    }
    val stemBranch = remember(birthYear) {
        val stemIndex = (birthYear - 4) % 10
        val branchIndex = (birthYear - 4) % 12
        val s = HEAVENLY_STEMS[if (stemIndex < 0) stemIndex + 10 else stemIndex]
        val b = EARTHLY_BRANCHES[if (branchIndex < 0) branchIndex + 12 else branchIndex]
        "$s$b$zodiac 年"
    }

    // 出生星期
    val birthWeekday = remember(birthCalendar) {
        when (birthCalendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "星期日"
            Calendar.MONDAY -> "星期一"
            Calendar.TUESDAY -> "星期二"
            Calendar.WEDNESDAY -> "星期三"
            Calendar.THURSDAY -> "星期四"
            Calendar.FRIDAY -> "星期五"
            Calendar.SATURDAY -> "星期六"
            else -> ""
        }
    }

    // 下次生日倒计时
    val nextBirthdayDays = remember(birthMonth, birthDay, currentMillis) {
        val currentYear = nowCalendar.get(Calendar.YEAR)
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, birthMonth - 1)
            set(Calendar.DAY_OF_MONTH, birthDay)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        if (nextBirthday.timeInMillis < currentMillis) {
            nextBirthday.set(Calendar.YEAR, currentYear + 1)
        }
        val diff = nextBirthday.timeInMillis - currentMillis
        (diff / 86400000L).coerceAtLeast(0L)
    }

    // 人生电量计算 (假定基准预期寿命 80 岁 = 29220 天)
    val lifeExpectancyYears = 80
    val totalLifeDays = lifeExpectancyYears * 365.25
    val lifeProgress = (livedDays / totalLifeDays).toFloat().coerceIn(0f, 1f)
    val remainingDays = (totalLifeDays - livedDays).toLong().coerceAtLeast(0L)
    val remainingWeekends = remainingDays / 7
    val remainingMeals = remainingDays * 3
    val remainingParentVisits = (remainingDays / 365).coerceAtLeast(0L)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Hero Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.58f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.75f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(FlameRed, SunsetOrange))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "年龄推算 · 人生进度时钟",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "生辰精准计算 · 生肖天干地支 · 时光沙漏与陪伴度量",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(containerColor = FlameRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("选择生日", fontSize = 12.sp)
                }
            }
        }

        // 2. 任意输入年龄推算交互面板 (恢复之前功能)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.72f)),
            border = BorderStroke(1.dp, FlameRed.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = FlameRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "可任意输入年龄推算",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "支持0~150任意年龄即时测算",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            val current = inputAgeText.toIntOrNull() ?: calcYears
                            applyAge(current - 1)
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    ) {
                        Icon(Icons.Filled.Remove, contentDescription = "减1岁", tint = MaterialTheme.colorScheme.onSurface)
                    }

                    OutlinedTextField(
                        value = inputAgeText,
                        onValueChange = { newText ->
                            val filtered = newText.filter { it.isDigit() }
                            inputAgeText = filtered
                            filtered.toIntOrNull()?.let { age ->
                                if (age in 0..150) {
                                    val currCal = Calendar.getInstance()
                                    birthYear = currCal.get(Calendar.YEAR) - age
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("输入任意年龄 (岁)", fontSize = 11.sp) },
                        suffix = { Text("岁", fontWeight = FontWeight.Bold, color = FlameRed) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlameRed,
                            cursorColor = FlameRed
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {
                            val current = inputAgeText.toIntOrNull() ?: calcYears
                            applyAge(current + 1)
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "加1岁", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 出生月/日输入：配合年龄精准推算星座（允许随意删除数字）
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "出生",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = monthText,
                        onValueChange = { t ->
                            val filtered = t.filter { it.isDigit() }.take(2)
                            monthText = filtered  // 直接保存文本，允许为空，不强制回填
                            filtered.toIntOrNull()?.let { v ->
                                birthMonth = v.coerceIn(1, 12)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("月份", fontSize = 10.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SunsetOrange,
                            cursorColor = SunsetOrange
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = dayText,
                        onValueChange = { t ->
                            val filtered = t.filter { it.isDigit() }.take(2)
                            dayText = filtered  // 直接保存文本，允许为空
                            filtered.toIntOrNull()?.let { v ->
                                birthDay = v.coerceIn(1, 31)
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("日期", fontSize = 10.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SunsetOrange,
                            cursorColor = SunsetOrange
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "→ $zodiacSign",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonPurple
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "快捷设定：",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf(1, 6, 12, 16, 18, 20, 22, 25, 28, 30, 35, 40, 50, 60, 70, 80, 100)) { quickAge ->
                        val isSelected = inputAgeText == quickAge.toString()
                        FilterChip(
                            selected = isSelected,
                            onClick = { applyAge(quickAge) },
                            label = { Text("${quickAge}岁", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FlameRed.copy(alpha = 0.15f),
                                selectedLabelColor = FlameRed
                            )
                        )
                    }
                }
            }
        }

        // 3. 当前生辰档案概览卡
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "出生日期：${birthYear}年${birthMonth}月${birthDay}日 ($birthWeekday)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SunsetOrange.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = stemBranch,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunsetOrange,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 星座 + 生肖 + 干支精准档案
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(
                        title = "星座",
                        value = zodiacSign,
                        isHighlight = true,
                        modifier = Modifier.weight(1f)
                    )
                    MetricBox(
                        title = "生肖",
                        value = "$zodiac 年生（${stemBranch}）",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 精准岁数展示
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "你已在世界上生活了：",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$calcYears",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = FlameRed
                    )
                    Text(text = "岁", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$calcMonths",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SunsetOrange
                    )
                    Text(text = "个月", fontSize = 13.sp)
                    Text(
                        text = "$calcDays",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SunsetOrange
                    )
                    Text(text = "天", fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 累计细化数据 4 宫格
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(title = "累计天数", value = "$livedDays 天", modifier = Modifier.weight(1f))
                    MetricBox(title = "累计小时", value = "$livedHours 小时", modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(title = "累计分钟", value = "$livedMinutes 分", modifier = Modifier.weight(1f))
                    MetricBox(title = "累计秒数 (实时)", value = "$livedSeconds 秒", isHighlight = true, modifier = Modifier.weight(1f))
                }
            }
        }

        // 3. 下个生日与人生进度条
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f)),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Cake, contentDescription = null, tint = FlameRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "距离下一个生日还有：",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = if (nextBirthdayDays == 0L) "🎂 今天就是生日！祝你快乐！" else "$nextBirthdayDays 天",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = FlameRed
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 人生进度沙漏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.HourglassBottom, contentDescription = null, tint = SunsetOrange, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "80岁人生电量进度：",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "%.2f%%".format(lifeProgress * 100f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = SunsetOrange
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { lifeProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = SunsetOrange,
                    trackColor = Color.Black.copy(alpha = 0.08f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 时光情感具象化指标
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = SunsetOrange, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "时光刻度：大约还剩 $remainingWeekends 个周末、可享用 $remainingMeals 顿一日三餐。", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Favorite, contentDescription = null, tint = FlameRed, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "亲情陪伴：若每年过年回老家一次，见父母的次数仅剩约 $remainingParentVisits 次。珍惜当下，常回家看看。", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }

    // Material 3 Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = birthCalendar.timeInMillis
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val cal = Calendar.getInstance().apply { timeInMillis = millis }
                            birthYear = cal.get(Calendar.YEAR)
                            birthMonth = cal.get(Calendar.MONTH) + 1
                            birthDay = cal.get(Calendar.DAY_OF_MONTH)
                            inputAgeText = (Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR)).coerceAtLeast(0).toString()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("确认", color = FlameRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    isHighlight: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isHighlight) FlameRed.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, if (isHighlight) FlameRed.copy(alpha = 0.25f) else Color.Transparent),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = title, fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = if (isHighlight) FlameRed else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
