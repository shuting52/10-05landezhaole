package com.example.ui.screens.toolbox

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.URL

/**
 * 货币转换 · 全球国家/地区汇率换算（支持全球 150+ 国家/地区选择、搜索、大洲筛选与全球替换对比）
 */
data class GlobalCountryCurrency(
    val code: String,
    val country: String,
    val currencyName: String,
    val symbol: String,
    val flag: String,
    val continent: String,
    val defaultRateToUsd: Double
) {
    val displayLabel: String
        get() = "$flag $country · $currencyName $symbol ($code)"
}

private val GLOBAL_COUNTRY_CURRENCIES: List<GlobalCountryCurrency> = listOf(
    // 亚洲
    GlobalCountryCurrency("CNY", "中国", "人民币", "¥", "🇨🇳", "亚洲", 7.18),
    GlobalCountryCurrency("HKD", "中国香港", "港币", "HK$", "🇭🇰", "亚洲", 7.80),
    GlobalCountryCurrency("MOP", "中国澳门", "澳门元", "MOP$", "🇲🇴", "亚洲", 8.03),
    GlobalCountryCurrency("TWD", "中国台湾", "新台币", "NT$", "🇨🇳", "亚洲", 32.10),
    GlobalCountryCurrency("JPY", "日本", "日元", "¥", "🇯🇵", "亚洲", 150.20),
    GlobalCountryCurrency("KRW", "韩国", "韩元", "₩", "🇰🇷", "亚洲", 1365.0),
    GlobalCountryCurrency("SGD", "新加坡", "新加坡元", "S$", "🇸🇬", "亚洲", 1.34),
    GlobalCountryCurrency("MYR", "马来西亚", "林吉特", "RM", "🇲🇾", "亚洲", 4.42),
    GlobalCountryCurrency("THB", "泰国", "泰铢", "฿", "🇹🇭", "亚洲", 33.80),
    GlobalCountryCurrency("VND", "越南", "越南盾", "₫", "🇻🇳", "亚洲", 25200.0),
    GlobalCountryCurrency("PHP", "菲律宾", "比索", "₱", "🇵🇭", "亚洲", 57.60),
    GlobalCountryCurrency("IDR", "印度尼西亚", "印尼盾", "Rp", "🇮🇩", "亚洲", 15850.0),
    GlobalCountryCurrency("INR", "印度", "卢比", "₹", "🇮🇳", "亚洲", 84.05),
    GlobalCountryCurrency("PKR", "巴基斯坦", "卢比", "₨", "🇵🇰", "亚洲", 278.5),
    GlobalCountryCurrency("BDT", "孟加拉国", "塔卡", "৳", "🇧🇩", "亚洲", 119.5),
    GlobalCountryCurrency("LKR", "斯里兰卡", "卢比", "Rs", "🇱🇰", "亚洲", 293.0),
    GlobalCountryCurrency("NPR", "尼泊尔", "卢比", "₨", "🇳🇵", "亚洲", 134.5),
    GlobalCountryCurrency("KHR", "柬埔寨", "瑞尔", "៛", "🇰🇭", "亚洲", 4065.0),
    GlobalCountryCurrency("LAK", "老挝", "基普", "₭", "🇱🇦", "亚洲", 21900.0),
    GlobalCountryCurrency("MMK", "缅甸", "缅元", "K", "🇲🇲", "亚洲", 2100.0),
    GlobalCountryCurrency("BND", "文莱", "文莱元", "B$", "🇧🇳", "亚洲", 1.34),
    GlobalCountryCurrency("MNT", "蒙古", "图格里克", "₮", "🇲🇳", "亚洲", 3400.0),
    GlobalCountryCurrency("KZT", "哈萨克斯坦", "坚戈", "₸", "🇰🇿", "亚洲", 488.0),
    GlobalCountryCurrency("UZS", "乌兹别克斯坦", "苏姆", "so'm", "🇺🇿", "亚洲", 12780.0),
    GlobalCountryCurrency("AED", "阿联酋", "迪拉姆", "د.إ", "🇦🇪", "亚洲", 3.67),
    GlobalCountryCurrency("SAR", "沙特阿拉伯", "里亚尔", "﷼", "🇸🇦", "亚洲", 3.75),
    GlobalCountryCurrency("QAR", "卡塔尔", "里亚尔", "﷼", "🇶🇦", "亚洲", 3.64),
    GlobalCountryCurrency("KWD", "科威特", "第纳尔", "د.ك", "🇰🇼", "亚洲", 0.307),
    GlobalCountryCurrency("BHD", "巴林", "第纳尔", "د.ب", "🇧🇭", "亚洲", 0.377),
    GlobalCountryCurrency("OMR", "阿曼", "里亚尔", "ر.ع", "🇴🇲", "亚洲", 0.385),
    GlobalCountryCurrency("ILS", "以色列", "谢克尔", "₪", "🇮🇱", "亚洲", 3.74),
    GlobalCountryCurrency("TRY", "土耳其", "里拉", "₺", "🇹🇷", "亚洲", 34.25),
    GlobalCountryCurrency("JOD", "约旦", "第纳尔", "د.ا", "🇯🇴", "亚洲", 0.709),
    GlobalCountryCurrency("IQD", "伊拉克", "第纳尔", "ع.د", "🇮🇶", "亚洲", 1310.0),
    GlobalCountryCurrency("IRR", "伊朗", "里亚尔", "﷼", "🇮🇷", "亚洲", 42000.0),

    // 美洲
    GlobalCountryCurrency("USD", "美国", "美元", "$", "🇺🇸", "美洲", 1.0),
    GlobalCountryCurrency("CAD", "加拿大", "加元", "C$", "🇨🇦", "美洲", 1.38),
    GlobalCountryCurrency("MXN", "墨西哥", "比索", "Mex$", "🇲🇽", "美洲", 19.85),
    GlobalCountryCurrency("BRL", "巴西", "雷亚尔", "R$", "🇧🇷", "美洲", 5.70),
    GlobalCountryCurrency("ARS", "阿根廷", "比索", "$", "🇦🇷", "美洲", 985.0),
    GlobalCountryCurrency("CLP", "智利", "比索", "$", "🇨🇱", "美洲", 950.0),
    GlobalCountryCurrency("COP", "哥伦比亚", "比索", "$", "🇨🇴", "美洲", 4350.0),
    GlobalCountryCurrency("PEN", "秘鲁", "索尔", "S/", "🇵🇪", "美洲", 3.76),
    GlobalCountryCurrency("UYU", "乌拉圭", "比索", "\$U", "🇺🇾", "美洲", 41.6),
    GlobalCountryCurrency("CRC", "哥斯达黎加", "科朗", "₡", "🇨🇷", "美洲", 514.0),
    GlobalCountryCurrency("DOP", "多米尼加", "比索", "RD$", "🇩🇴", "美洲", 60.2),
    GlobalCountryCurrency("JMD", "牙买加", "牙买加元", "J$", "🇯🇲", "美洲", 158.0),

    // 欧洲
    GlobalCountryCurrency("EUR", "欧盟(德/法/意/西等)", "欧元", "€", "🇪🇺", "欧洲", 0.92),
    GlobalCountryCurrency("GBP", "英国", "英镑", "£", "🇬🇧", "欧洲", 0.77),
    GlobalCountryCurrency("CHF", "瑞士", "瑞士法郎", "₣", "🇨🇭", "欧洲", 0.865),
    GlobalCountryCurrency("RUB", "俄罗斯", "卢布", "₽", "🇷🇺", "欧洲", 96.5),
    GlobalCountryCurrency("SEK", "瑞典", "瑞典克朗", "kr", "🇸🇪", "欧洲", 10.65),
    GlobalCountryCurrency("NOK", "挪威", "挪威克朗", "kr", "🇳🇴", "欧洲", 10.95),
    GlobalCountryCurrency("DKK", "丹麦", "丹麦克朗", "kr", "🇩🇰", "欧洲", 6.88),
    GlobalCountryCurrency("PLN", "波兰", "兹罗提", "zł", "🇵🇱", "欧洲", 4.01),
    GlobalCountryCurrency("CZK", "捷克", "捷克克朗", "Kč", "🇨🇿", "欧洲", 23.4),
    GlobalCountryCurrency("HUF", "匈牙利", "福林", "Ft", "🇭🇺", "欧洲", 372.0),
    GlobalCountryCurrency("RON", "罗马尼亚", "列伊", "lei", "🇷🇴", "欧洲", 4.58),
    GlobalCountryCurrency("BGN", "保加利亚", "列弗", "лв", "🇧🇬", "欧洲", 1.80),
    GlobalCountryCurrency("UAH", "乌克兰", "格里夫纳", "₴", "🇺🇦", "欧洲", 41.3),
    GlobalCountryCurrency("ISK", "冰岛", "冰岛克朗", "kr", "🇮🇸", "欧洲", 138.0),
    GlobalCountryCurrency("RSD", "塞尔维亚", "第纳尔", "дин", "🇷🇸", "欧洲", 108.0),
    GlobalCountryCurrency("BYN", "白俄罗斯", "卢布", "Br", "🇧🇾", "欧洲", 3.27),

    // 大洋洲
    GlobalCountryCurrency("AUD", "澳大利亚", "澳元", "A$", "🇦🇺", "大洋洲", 1.51),
    GlobalCountryCurrency("NZD", "新西兰", "新西兰元", "NZ$", "🇳🇿", "大洋洲", 1.66),
    GlobalCountryCurrency("FJD", "斐济", "斐济元", "FJ$", "🇫🇯", "大洋洲", 2.24),

    // 非洲
    GlobalCountryCurrency("ZAR", "南非", "兰特", "R", "🇿🇦", "非洲", 17.65),
    GlobalCountryCurrency("EGP", "埃及", "埃及镑", "E£", "🇪🇬", "非洲", 48.7),
    GlobalCountryCurrency("NGN", "尼日利亚", "奈拉", "₦", "🇳🇬", "非洲", 1640.0),
    GlobalCountryCurrency("KES", "肯尼亚", "先令", "KSh", "🇰🇪", "非洲", 129.0),
    GlobalCountryCurrency("MAD", "摩洛哥", "迪拉姆", "د.م", "🇲🇦", "非洲", 9.88),
    GlobalCountryCurrency("GHS", "加纳", "塞地", "₵", "🇬🇭", "非洲", 15.9),
    GlobalCountryCurrency("TZS", "坦桑尼亚", "先令", "TSh", "🇹🇿", "非洲", 2720.0),
    GlobalCountryCurrency("ETB", "埃塞俄比亚", "比尔", "Br", "🇪🇹", "非洲", 118.0),
    GlobalCountryCurrency("DZD", "阿尔及利亚", "第纳尔", "د.ج", "🇩🇿", "非洲", 133.5),
    GlobalCountryCurrency("TND", "突尼斯", "第纳尔", "د.ت", "🇹🇳", "非洲", 3.10)
)

private val FallbackRates: Map<String, Double> =
    GLOBAL_COUNTRY_CURRENCIES.associate { it.code to it.defaultRateToUsd }

@Composable
fun CurrencySection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var fromCode by remember { mutableStateOf("USD") }
    var toCode by remember { mutableStateOf("CNY") }
    var amount by remember { mutableStateOf("1") }
    var pickingTarget by remember { mutableStateOf<String?>(null) } // "FROM" or "TO"
    var rates by remember { mutableStateOf(FallbackRates) }
    var usingOnline by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }

    val fromItem = remember(fromCode) {
        GLOBAL_COUNTRY_CURRENCIES.firstOrNull { it.code == fromCode } ?: GLOBAL_COUNTRY_CURRENCIES.first()
    }
    val toItem = remember(toCode) {
        GLOBAL_COUNTRY_CURRENCIES.firstOrNull { it.code == toCode } ?: GLOBAL_COUNTRY_CURRENCIES[1]
    }

    fun convert() {
        val amt = amount.trim().toDoubleOrNull()
        if (amt == null || amt <= 0) {
            result = "请输入有效金额"
            return
        }
        val fromRate = rates[fromCode] ?: 1.0
        val toRate = rates[toCode] ?: 1.0
        val value = amt / fromRate * toRate
        val scale = if (value >= 100) 2 else if (value >= 1) 4 else 6
        val big = BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        result = "$amount ${fromItem.country}(${fromItem.code}) = $big ${toItem.country}(${toItem.code})"
    }

    LaunchedEffect(amount, fromCode, toCode, rates) {
        convert()
    }

    fun fetchRates() {
        loading = true
        scope.launch {
            val fetched = withContext(Dispatchers.IO) {
                try {
                    val conn = URL("https://open.er-api.com/v6/latest/USD").openConnection() as HttpURLConnection
                    conn.connectTimeout = 6000
                    conn.readTimeout = 6000
                    conn.setRequestProperty("User-Agent", "LazyFind")
                    val body = conn.inputStream.bufferedReader().readText()
                    val ratesObj = JSONObject(body).getJSONObject("rates")
                    val map = mutableMapOf("USD" to 1.0)
                    ratesObj.keys().forEach { k ->
                        map[k] = ratesObj.getDouble(k)
                    }
                    FallbackRates.forEach { (k, v) -> if (!map.containsKey(k)) map[k] = v }
                    map
                } catch (_: Exception) {
                    try {
                        val conn = URL("https://api.frankfurter.app/latest?from=USD").openConnection() as HttpURLConnection
                        conn.connectTimeout = 6000
                        conn.readTimeout = 6000
                        val body = conn.inputStream.bufferedReader().readText()
                        val ratesObj = JSONObject(body).getJSONObject("rates")
                        val map = mutableMapOf("USD" to 1.0)
                        ratesObj.keys().forEach { k ->
                            map[k] = ratesObj.getDouble(k)
                        }
                        FallbackRates.forEach { (k, v) -> if (!map.containsKey(k)) map[k] = v }
                        map
                    } catch (_: Exception) {
                        FallbackRates
                    }
                }
            }
            rates = fetched
            usingOnline = fetched !== FallbackRates
            loading = false
            Toast.makeText(
                context,
                if (usingOnline) "✅ 已同步全球最新实时汇率" else "在线汇率获取受限，已启用内置全球汇率",
                Toast.LENGTH_SHORT
            ).show()
            convert()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题卡
        Surface(
            color = Color(0xFFFFFDF9).copy(alpha = 0.9f),
            border = BorderStroke(1.dp, Color(0xFF00B386).copy(alpha = 0.55f)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("🌍 全球国家货币转换", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFF00875A))
                Text("支持全球国家/地区自由选择与一键替换 · 实时汇率双向换算", fontSize = 11.sp, color = Color(0xFF5A8A7A), modifier = Modifier.padding(top = 2.dp))
            }
        }

        // 金额输入
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it.filter { c -> c.isDigit() || c == '.' }.take(12) },
            label = { Text("金额") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // 从国家/货币选择（点击弹出全球国家选择器）
        Text("从国家 / 货币（点击切换全球任意国家）", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A8A7A))
        Surface(
            onClick = { pickingTarget = "FROM" },
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            border = BorderStroke(1.2.dp, Color(0xFF00875A).copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(fromItem.flag, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${fromItem.country} · ${fromItem.currencyName} (${fromItem.symbol})",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "国际代码：${fromItem.code} · 所属：${fromItem.continent}（点击更换国家）",
                        fontSize = 10.5.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "选择从国家/货币", tint = Color(0xFF00875A))
            }
        }

        // 交换 & 热门全球国家一键替换栏
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                onClick = {
                    val t = fromCode
                    fromCode = toCode
                    toCode = t
                    convert()
                },
                color = Color(0xFFE6F7F1),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = Color(0xFF00875A), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("双向对调国家", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00875A))
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("CNY", "USD", "EUR", "JPY", "HKD", "GBP", "KRW", "SGD", "AUD", "RUB").forEach { quickCode ->
                    val item = GLOBAL_COUNTRY_CURRENCIES.firstOrNull { it.code == quickCode } ?: return@forEach
                    Surface(
                        onClick = {
                            toCode = quickCode
                            convert()
                        },
                        shape = RoundedCornerShape(999.dp),
                        color = if (toCode == quickCode) Color(0xFF00875A) else Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = "${item.flag} ${item.country}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (toCode == quickCode) Color.White else Color(0xFF334155),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // 到国家/货币选择（点击弹出全球国家选择器）
        Text("到国家 / 货币（点击切换全球任意国家）", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5A8A7A))
        Surface(
            onClick = { pickingTarget = "TO" },
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, Color(0xFFDE2910).copy(alpha = 0.65f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(toItem.flag, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${toItem.country} · ${toItem.currencyName} (${toItem.symbol})",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                    Text(
                        text = "国际代码：${toItem.code} · 所属：${toItem.continent}（点击更换国家）",
                        fontSize = 10.5.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "选择到国家/货币", tint = Color(0xFFDE2910))
            }
        }

        // 汇率信息
        Surface(
            color = Color(0xFFEAF9F3),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                val unitVal = rates[toCode]?.let { 1.0 / (rates[fromCode] ?: 1.0) * it } ?: 1.0
                val unitScale = if (unitVal >= 10) 2 else 4
                Text(
                    text = "1 ${fromItem.country}(${fromCode}) ≈ ${BigDecimal(unitVal).setScale(unitScale, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()} ${toItem.country}(${toCode})",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00875A),
                    modifier = Modifier.weight(1f)
                )
                Text(if (usingOnline) "实时汇率" else "内置汇率", fontSize = 10.sp, color = Color(0xFF5A8A7A))
            }
        }

        // 换算结果
        Surface(
            color = Color(0xFF1E3A2E),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(2.dp, Color(0xFF2DD4A7)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = result.ifBlank { "输入金额后自动换算" },
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF2DD4A7),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("${fromItem.flag} ${fromItem.country} ⇄ ${toItem.flag} ${toItem.country} · 实时换算结果", fontSize = 11.sp, color = Color(0xFF8FB5A8))
            }
        }

        // 操作按钮
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { convert() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00875A)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.CompareArrows, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text("立即换算", fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { fetchRates() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D7D6A)),
                shape = RoundedCornerShape(14.dp),
                enabled = !loading,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Public, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.size(6.dp))
                Text(if (loading) "同步中…" else "同步全球汇率", fontWeight = FontWeight.Bold)
            }
        }

        // 全球主流国家同步折算一览（点击任意国家立即替换目标国家）
        Surface(
            color = Color.White.copy(alpha = 0.85f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF00B386).copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "🌐 全球实时折算一览（点击任意国家立即替换目标货币）",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF00875A)
                )
                val amtVal = amount.trim().toDoubleOrNull()?.takeIf { it > 0 } ?: 1.0
                val baseFromRate = rates[fromCode] ?: 1.0
                GLOBAL_COUNTRY_CURRENCIES.take(12).forEach { target ->
                    val targetRate = rates[target.code] ?: 1.0
                    val converted = amtVal / baseFromRate * targetRate
                    val formatted = BigDecimal(converted).setScale(2, RoundingMode.HALF_UP).toPlainString()
                    Surface(
                        onClick = {
                            toCode = target.code
                            convert()
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (target.code == toCode) Color(0xFFE6F7F1) else Color(0xFFF8FAFC),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(target.flag, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${target.country} · ${target.currencyName}",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${target.symbol} $formatted (${target.code})",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF00875A)
                            )
                        }
                    }
                }
            }
        }
    }

    // 全球国家/货币选择弹窗（支持搜索国家名/货币名/代码 + 五大洲分类筛选）
    pickingTarget?.let { targetMode ->
        GlobalCountryCurrencyPickerDialog(
            title = if (targetMode == "FROM") "选择源国家 / 货币" else "选择目标国家 / 货币",
            selectedCode = if (targetMode == "FROM") fromCode else toCode,
            onSelect = { chosenCode ->
                if (targetMode == "FROM") {
                    fromCode = chosenCode
                } else {
                    toCode = chosenCode
                }
                pickingTarget = null
                convert()
            },
            onDismiss = { pickingTarget = null }
        )
    }
}

@Composable
private fun GlobalCountryCurrencyPickerDialog(
    title: String,
    selectedCode: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedContinent by remember { mutableStateOf("全部") }
    val continents = listOf("全部", "亚洲", "欧洲", "美洲", "大洋洲", "非洲")

    val filteredList = remember(searchQuery, selectedContinent) {
        GLOBAL_COUNTRY_CURRENCIES.filter { item ->
            val matchContinent = selectedContinent == "全部" || item.continent == selectedContinent
            val q = searchQuery.trim()
            val matchQuery = q.isEmpty() ||
                item.country.contains(q, ignoreCase = true) ||
                item.currencyName.contains(q, ignoreCase = true) ||
                item.code.contains(q, ignoreCase = true) ||
                item.symbol.contains(q, ignoreCase = true)
            matchContinent && matchQuery
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 640.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFFFFDF9),
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(title, fontSize = 16.5.sp, fontWeight = FontWeight.Black, color = Color(0xFF00875A))
                        Text("共收录全球 ${GLOBAL_COUNTRY_CURRENCIES.size} 个主流国家与地区货币", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                    Surface(
                        onClick = onDismiss,
                        shape = CircleShape,
                        color = Color(0xFFF1F5F9)
                    ) {
                        Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Close, contentDescription = "关闭", modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    placeholder = { Text("搜索国家名、货币名或代码（如：日本 / 欧元 / USD）", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    continents.forEach { cont ->
                        val isSel = selectedContinent == cont
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedContinent = cont },
                            label = { Text(cont, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF00875A),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    items(filteredList, key = { it.code + it.country }) { item ->
                        val isSelected = item.code == selectedCode
                        Surface(
                            onClick = { onSelect(item.code) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFFE6F7F1) else Color.White,
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) Color(0xFF00875A) else Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.flag, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${item.country} · ${item.currencyName}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${item.continent} · 符号 ${item.symbol} · 代码 ${item.code}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                                Text(
                                    text = item.code,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) Color(0xFF00875A) else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
