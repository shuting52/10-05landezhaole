package com.example.ui.screens.toolbox

import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * 车牌摇号 · 全国 34 省级行政区及全部地级市/自治州选号
 */

/** 全国城市数据：省份简称 → (省份名, 城市列表[城市名, 字母]) */
private val NationalCityData: List<Pair<String, Pair<String, List<Pair<String, String>>>>> = listOf(
    "京" to ("北京" to listOf("北京城区" to "A", "北京出租" to "B", "北京远郊" to "G", "北京警备" to "C")),
    "津" to ("天津" to listOf("天津市区" to "A", "天津塘沽" to "B", "天津郊县" to "C", "天津公交" to "D")),
    "冀" to ("河北" to listOf("石家庄" to "A", "唐山" to "B", "秦皇岛" to "C", "邯郸" to "D", "邢台" to "E", "保定" to "F", "张家口" to "G", "承德" to "H", "沧州" to "J", "廊坊" to "R", "衡水" to "T")),
    "晋" to ("山西" to listOf("太原" to "A", "大同" to "B", "阳泉" to "C", "长治" to "D", "晋城" to "E", "朔州" to "F", "忻州" to "H", "吕梁" to "J", "晋中" to "K", "临汾" to "L", "运城" to "M")),
    "蒙" to ("内蒙古" to listOf("呼和浩特" to "A", "包头" to "B", "乌海" to "C", "赤峰" to "D", "呼伦贝尔" to "E", "兴安盟" to "F", "通辽" to "G", "锡林郭勒" to "H", "乌兰察布" to "J", "鄂尔多斯" to "K", "巴彦淖尔" to "L", "阿拉善" to "M")),
    "辽" to ("辽宁" to listOf("沈阳" to "A", "大连" to "B", "鞍山" to "C", "抚顺" to "D", "本溪" to "E", "丹东" to "F", "锦州" to "G", "营口" to "H", "阜新" to "J", "辽阳" to "K", "盘锦" to "L", "铁岭" to "M", "朝阳" to "N", "葫芦岛" to "P")),
    "吉" to ("吉林" to listOf("长春" to "A", "吉林市" to "B", "四平" to "C", "辽源" to "D", "通化" to "E", "白山" to "F", "松原" to "J", "白城" to "G", "延边" to "H")),
    "黑" to ("黑龙江" to listOf("哈尔滨" to "A", "齐齐哈尔" to "B", "牡丹江" to "C", "佳木斯" to "D", "大庆" to "E", "伊春" to "F", "鸡西" to "G", "鹤岗" to "H", "双鸭山" to "J", "七台河" to "K", "绥化" to "M", "黑河" to "N", "大兴安岭" to "P")),
    "沪" to ("上海" to listOf("上海市区" to "A", "上海浦东" to "B", "上海远郊" to "C", "上海增号" to "D")),
    "苏" to ("江苏" to listOf("南京" to "A", "无锡" to "B", "徐州" to "C", "常州" to "D", "苏州" to "E", "南通" to "F", "连云港" to "G", "淮安" to "H", "盐城" to "J", "扬州" to "K", "镇江" to "L", "泰州" to "M", "宿迁" to "N")),
    "浙" to ("浙江" to listOf("杭州" to "A", "宁波" to "B", "温州" to "C", "绍兴" to "D", "湖州" to "E", "嘉兴" to "F", "金华" to "G", "衢州" to "H", "舟山" to "L", "台州" to "J", "丽水" to "K")),
    "皖" to ("安徽" to listOf("合肥" to "A", "芜湖" to "B", "蚌埠" to "C", "淮南" to "D", "马鞍山" to "E", "淮北" to "F", "铜陵" to "G", "安庆" to "H", "黄山" to "J", "阜阳" to "K", "宿州" to "L", "滁州" to "M", "六安" to "N", "宣城" to "P", "池州" to "R", "亳州" to "S")),
    "闽" to ("福建" to listOf("福州" to "A", "莆田" to "B", "泉州" to "C", "厦门" to "D", "漳州" to "E", "龙岩" to "F", "三明" to "G", "南平" to "H", "宁德" to "J")),
    "赣" to ("江西" to listOf("南昌" to "A", "赣州" to "B", "宜春" to "C", "吉安" to "D", "上饶" to "E", "抚州" to "F", "九江" to "G", "景德镇" to "H", "萍乡" to "J", "新余" to "K", "鹰潭" to "L")),
    "鲁" to ("山东" to listOf("济南" to "A", "青岛" to "B", "淄博" to "C", "枣庄" to "D", "东营" to "E", "烟台" to "F", "潍坊" to "G", "济宁" to "H", "泰安" to "J", "威海" to "K", "日照" to "L", "滨州" to "M", "德州" to "N", "聊城" to "P", "临沂" to "Q", "菏泽" to "R")),
    "豫" to ("河南" to listOf("郑州" to "A", "开封" to "B", "洛阳" to "C", "平顶山" to "D", "安阳" to "E", "鹤壁" to "F", "新乡" to "G", "焦作" to "H", "濮阳" to "J", "许昌" to "K", "漯河" to "L", "三门峡" to "M", "商丘" to "N", "周口" to "P", "驻马店" to "Q", "南阳" to "R", "信阳" to "S", "济源" to "U")),
    "鄂" to ("湖北" to listOf("武汉" to "A", "黄石" to "B", "十堰" to "C", "宜昌" to "E", "襄阳" to "F", "鄂州" to "G", "荆门" to "H", "孝感" to "K", "荆州" to "D", "黄冈" to "J", "咸宁" to "L", "随州" to "S", "恩施" to "Q", "仙桃" to "M", "潜江" to "N", "天门" to "R")),
    "湘" to ("湖南" to listOf("长沙" to "A", "株洲" to "B", "湘潭" to "C", "衡阳" to "D", "邵阳" to "E", "岳阳" to "F", "常德" to "J", "张家界" to "G", "益阳" to "H", "郴州" to "L", "永州" to "M", "怀化" to "N", "娄底" to "K", "湘西" to "U")),
    "粤" to ("广东" to listOf("广州" to "A", "深圳" to "B", "珠海" to "C", "汕头" to "D", "佛山" to "E", "韶关" to "F", "湛江" to "G", "肇庆" to "H", "江门" to "J", "茂名" to "K", "惠州" to "L", "梅州" to "M", "汕尾" to "N", "河源" to "P", "阳江" to "Q", "清远" to "R", "东莞" to "S", "中山" to "T", "潮州" to "U", "揭阳" to "V", "云浮" to "W")),
    "桂" to ("广西" to listOf("南宁" to "A", "柳州" to "B", "桂林" to "C", "梧州" to "D", "北海" to "E", "崇左" to "F", "来宾" to "G", "贺州" to "J", "玉林" to "K", "百色" to "L", "河池" to "M", "钦州" to "N", "防城港" to "P", "贵港" to "R")),
    "琼" to ("海南" to listOf("海口" to "A", "三亚" to "B", "三沙" to "C", "琼海" to "C", "儋州" to "F")),
    "渝" to ("重庆" to listOf("重庆主城" to "A", "重庆江北" to "B", "重庆永川" to "C", "重庆万州" to "F")),
    "川" to ("四川" to listOf("成都" to "A", "绵阳" to "B", "自贡" to "C", "攀枝花" to "D", "泸州" to "E", "德阳" to "F", "广元" to "H", "遂宁" to "J", "内江" to "K", "乐山" to "L", "资阳" to "M", "宜宾" to "Q", "南充" to "R", "达州" to "S", "雅安" to "T", "阿坝" to "U", "甘孜" to "V", "凉山" to "W", "广安" to "X", "巴中" to "Y", "眉山" to "Z")),
    "贵" to ("贵州" to listOf("贵阳" to "A", "六盘水" to "B", "遵义" to "C", "铜仁" to "D", "黔西南" to "E", "毕节" to "F", "安顺" to "G", "黔东南" to "H", "黔南" to "J")),
    "云" to ("云南" to listOf("昆明" to "A", "昭通" to "C", "曲靖" to "D", "楚雄" to "E", "玉溪" to "F", "红河" to "G", "文山" to "H", "普洱" to "J", "西双版纳" to "K", "大理" to "L", "保山" to "M", "德宏" to "N", "丽江" to "P", "怒江" to "Q", "迪庆" to "R", "临沧" to "S")),
    "藏" to ("西藏" to listOf("拉萨" to "A", "昌都" to "B", "山南" to "C", "日喀则" to "D", "那曲" to "E", "阿里" to "F", "林芝" to "G")),
    "陕" to ("陕西" to listOf("西安" to "A", "铜川" to "B", "宝鸡" to "C", "咸阳" to "D", "渭南" to "E", "汉中" to "F", "安康" to "G", "商洛" to "H", "延安" to "J", "榆林" to "K", "杨凌" to "V")),
    "甘" to ("甘肃" to listOf("兰州" to "A", "嘉峪关" to "B", "金昌" to "C", "白银" to "D", "天水" to "E", "酒泉" to "F", "张掖" to "G", "武威" to "H", "定西" to "J", "陇南" to "K", "平凉" to "L", "庆阳" to "M", "临夏" to "N", "甘南" to "P")),
    "青" to ("青海" to listOf("西宁" to "A", "海东" to "B", "海北" to "C", "黄南" to "D", "海南州" to "E", "果洛" to "F", "玉树" to "G", "海西" to "H")),
    "宁" to ("宁夏" to listOf("银川" to "A", "石嘴山" to "B", "吴忠" to "C", "固原" to "D", "中卫" to "E")),
    "新" to ("新疆" to listOf("乌鲁木齐" to "A", "昌吉" to "B", "石河子" to "C", "奎屯" to "D", "博尔塔拉" to "E", "伊犁" to "F", "塔城" to "G", "阿勒泰" to "H", "克拉玛依" to "J", "吐鲁番" to "K", "哈密" to "L", "巴音郭楞" to "M", "阿克苏" to "N", "克孜勒苏" to "P", "喀什" to "Q", "和田" to "R")),
    "港" to ("香港" to listOf("香港" to "Z")),
    "澳" to ("澳门" to listOf("澳门" to "Z")),
    "台" to ("台湾" to listOf("台北" to "A", "高雄" to "B", "台中" to "C", "台南" to "D"))
)

private val BlueChars = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ"

private fun genPlate(province: String, cityLetter: String, isNewEnergy: Boolean): String {
    val prefix = province + cityLetter
    return if (isNewEnergy) {
        val second = if (Random.nextBoolean()) "D" else "F"
        val rest = buildString { repeat(5) { append(BlueChars[Random.nextInt(BlueChars.length)]) } }
        prefix + second + rest
    } else {
        val first = BlueChars[Random.nextInt(2, BlueChars.length)]
        val rest = buildString { repeat(4) { append(BlueChars[Random.nextInt(10)]) } }
        prefix + first + rest
    }
}

@Composable
fun LicensePlateSection(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isUnit by remember { mutableStateOf(false) }
    var isNewEnergy by remember { mutableStateOf(false) }

    // 省份/城市选择（默认粤A广州）
    var selectedProvinceIdx by remember { mutableStateOf(18) }
    var selectedCityIdx by remember { mutableStateOf(0) }
    var provinceDialogOpen by remember { mutableStateOf(false) }
    var cityDialogOpen by remember { mutableStateOf(false) }
    var typeMenuOpen by remember { mutableStateOf(false) }
    var identityMenuOpen by remember { mutableStateOf(false) }

    val currentProvince = NationalCityData[selectedProvinceIdx.coerceIn(0, NationalCityData.lastIndex)]
    val currentCity = currentProvince.second.second[selectedCityIdx.coerceIn(0, currentProvince.second.second.lastIndex)]

    var pool by remember {
        mutableStateOf((0 until 10).map { genPlate(currentProvince.first, currentCity.second, false) })
    }
    var rolling by remember { mutableStateOf(false) }
    var rollingPlate by remember { mutableStateOf("") }
    var finalPlate by remember { mutableStateOf<String?>(null) }

    fun regeneratePool(provIdx: Int = selectedProvinceIdx, cIdx: Int = selectedCityIdx, newEnergy: Boolean = isNewEnergy) {
        val p = NationalCityData[provIdx.coerceIn(0, NationalCityData.lastIndex)]
        val c = p.second.second[cIdx.coerceIn(0, p.second.second.lastIndex)]
        pool = (0 until 10).map { genPlate(p.first, c.second, newEnergy) }
    }

    fun selectProvince(idx: Int) {
        selectedProvinceIdx = idx
        selectedCityIdx = 0
        provinceDialogOpen = false
        regeneratePool(provIdx = idx, cIdx = 0)
    }

    fun selectCity(idx: Int) {
        selectedCityIdx = idx
        cityDialogOpen = false
        regeneratePool(provIdx = selectedProvinceIdx, cIdx = idx)
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
            border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.7f)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("🚘 车牌摇号 · 全国城市", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color(0xFFDE2910))
                Text("全国 34 省级行政区 · 300+ 地级市联动 · 号牌池一键摇号", fontSize = 11.sp, color = Color(0xFF7A4A45), modifier = Modifier.padding(top = 2.dp))
            }
        }

        // 1. 选号设置：身份（下拉菜单）
        Text("身份", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A4A45))
        Box {
            Surface(
                onClick = { identityMenuOpen = true },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.2.dp, Color(0xFF7A4A45).copy(alpha = 0.38f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("选择身份", fontSize = 10.5.sp, color = Color(0xFF7A4A45))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isUnit) "🏢 单位车辆" else "👤 个人车辆",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "选择身份", tint = Color(0xFF7A4A45))
                }
            }
            DropdownMenu(expanded = identityMenuOpen, onDismissRequest = { identityMenuOpen = false }) {
                DropdownMenuItem(
                    text = { Text("👤 个人车辆", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
                    onClick = { isUnit = false; identityMenuOpen = false; regeneratePool() }
                )
                DropdownMenuItem(
                    text = { Text("🏢 单位车辆", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
                    onClick = { isUnit = true; identityMenuOpen = false; regeneratePool() }
                )
            }
        }

        // 2. 选号设置：省份（点击弹出全国 34 省级行政区检索选择面板）
        Text("省份", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A4A45))
        Surface(
            onClick = { provinceDialogOpen = true },
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.2.dp, Color(0xFFDE2910).copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("选择省份（全国 34 省级行政区）", fontSize = 10.5.sp, color = Color(0xFF7A4A45))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${currentProvince.second.first}（简称：${currentProvince.first} · 含 ${currentProvince.second.second.size} 个城市）",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "选择省份", tint = Color(0xFFDE2910))
            }
        }

        // 3. 选号设置：城市（点击弹出当前省份/或全国城市检索选择面板）
        Text("城市", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A4A45))
        Surface(
            onClick = { cityDialogOpen = true },
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, Color(0xFFDE2910).copy(alpha = 0.75f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("选择城市（支持全国任意城市检索）", fontSize = 10.5.sp, color = Color(0xFFDE2910))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${currentCity.first} · 发牌机关代号 ${currentProvince.first}${currentCity.second}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "选择城市", tint = Color(0xFFDE2910))
            }
        }

        // 4. 选号设置：类型（下拉菜单）
        Text("类型", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A4A45))
        Box {
            Surface(
                onClick = { typeMenuOpen = true },
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.2.dp, Color(0xFF7A4A45).copy(alpha = 0.38f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("选择号牌类型", fontSize = 10.5.sp, color = Color(0xFF7A4A45))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isNewEnergy) "🟢 新能源绿牌（8位）" else "🔵 燃油蓝牌（7位）",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    }
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "选择号牌类型", tint = Color(0xFF7A4A45))
                }
            }
            DropdownMenu(expanded = typeMenuOpen, onDismissRequest = { typeMenuOpen = false }) {
                DropdownMenuItem(
                    text = { Text("🔵 燃油蓝牌（7位标准号牌）", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
                    onClick = {
                        isNewEnergy = false
                        typeMenuOpen = false
                        regeneratePool(newEnergy = false)
                    }
                )
                DropdownMenuItem(
                    text = { Text("🟢 新能源绿牌（8位小型/大型新能源）", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
                    onClick = {
                        isNewEnergy = true
                        typeMenuOpen = false
                        regeneratePool(newEnergy = true)
                    }
                )
            }
        }

        // 号牌池
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "号牌池（${currentProvince.first}${currentCity.second} · 10 选 1）",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF3B1F1F)
            )
            androidx.compose.material3.TextButton(onClick = { regeneratePool() }) {
                Text("刷新号牌池", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDE2910))
            }
        }

        // 池内号牌两列
        pool.chunked(2).forEach { rowList ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowList.forEach { p ->
                    Surface(
                        onClick = {
                            finalPlate = p
                            rollingPlate = p
                            Toast.makeText(context, "已选定号牌：$p", Toast.LENGTH_SHORT).show()
                        },
                        color = if (isNewEnergy) Color(0xFFECFDF5) else Color(0xFFF3F7FF),
                        border = BorderStroke(
                            1.dp,
                            if (isNewEnergy) Color(0xFF10B981).copy(alpha = 0.4f) else Color(0xFF3B82F6).copy(alpha = 0.35f)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = p,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isNewEnergy) Color(0xFF047857) else Color(0xFF1A4FA0),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
                if (rowList.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }

        // 摇号结果展示
        Surface(
            color = if (isNewEnergy) Color(0xFF064E3B) else Color(0xFF1E2A4A),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(2.dp, Color(0xFFFFD700)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Text(
                    text = if (rolling) rollingPlate.ifBlank { "摇号中…" } else (finalPlate ?: "点击开始摇号"),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFD700),
                    letterSpacing = 3.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (finalPlate != null && !rolling) "🎉 恭喜摇中（${currentProvince.second.first}·${currentCity.first}）！" else "车牌摇号 · 好运加持",
                    fontSize = 11.sp,
                    color = Color(0xFF9FB4E8)
                )
            }
        }

        // 开始摇号按钮
        Button(
            onClick = {
                if (rolling) return@Button
                finalPlate = null
                rolling = true
                scope.launch {
                    repeat(28) { i ->
                        rollingPlate = genPlate(currentProvince.first, currentCity.second, isNewEnergy)
                        delay(if (i < 20) 70L else (70 + (i - 20) * 60).toLong())
                    }
                    val picked = pool.ifEmpty {
                        (0 until 10).map { genPlate(currentProvince.first, currentCity.second, isNewEnergy) }
                    }.random()
                    finalPlate = picked
                    rolling = false
                    rollingPlate = picked
                    Toast.makeText(context, "🎉 摇中号牌：$picked", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDE2910)),
            shape = RoundedCornerShape(14.dp),
            enabled = !rolling,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.DirectionsCar, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (rolling) "摇号中…" else "开始摇号", fontWeight = FontWeight.Black, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "说明：已覆盖全国 34 个省级行政区与 300+ 地级市，摇号结果仅供参考娱乐，实际选号以当地车管所规定为准。",
            fontSize = 11.sp,
            color = Color(0xFF9A7B6B)
        )
    }

    // 省份选择弹窗（支持搜索省份名或车牌简称）
    if (provinceDialogOpen) {
        var query by remember { mutableStateOf("") }
        val filteredProvinces = remember(query) {
            val q = query.trim()
            NationalCityData.mapIndexed { idx, pair -> idx to pair }.filter { (_, pair) ->
                q.isEmpty() ||
                    pair.first.contains(q, ignoreCase = true) ||
                    pair.second.first.contains(q, ignoreCase = true) ||
                    pair.second.second.any { it.first.contains(q, ignoreCase = true) }
            }
        }
        Dialog(
            onDismissRequest = { provinceDialogOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .heightIn(max = 600.dp),
                shape = RoundedCornerShape(22.dp),
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
                            Text("选择省份（全国 34 省级行政区）", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFDE2910))
                            Text("点击任意省份自动切换对应城市列表", fontSize = 11.sp, color = Color(0xFF7A4A45))
                        }
                        Surface(
                            onClick = { provinceDialogOpen = false },
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
                        value = query,
                        onValueChange = { query = it },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        placeholder = { Text("搜索省份名或简称（如：广东 / 粤 / 浙江）", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        itemsIndexed(filteredProvinces) { _, (origIdx, item) ->
                            val isSel = origIdx == selectedProvinceIdx
                            Surface(
                                onClick = { selectProvince(origIdx) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) Color(0xFFFFF1F0) else Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSel) Color(0xFFDE2910) else Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFDE2910)
                                    ) {
                                        Text(
                                            text = item.first,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = item.second.first,
                                        fontSize = 14.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${item.second.second.size} 个城市",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 城市选择弹窗（支持当前省份城市快速点选 + 全国 300+ 城市直接跨省搜索）
    if (cityDialogOpen) {
        var cityQuery by remember { mutableStateOf("") }
        data class FlatCityOption(
            val provIdx: Int,
            val provShort: String,
            val provName: String,
            val cityIdx: Int,
            val cityName: String,
            val cityLetter: String
        )
        val displayCities = remember(cityQuery, selectedProvinceIdx) {
            val q = cityQuery.trim()
            if (q.isEmpty()) {
                val p = NationalCityData[selectedProvinceIdx]
                p.second.second.mapIndexed { cIdx, c ->
                    FlatCityOption(selectedProvinceIdx, p.first, p.second.first, cIdx, c.first, c.second)
                }
            } else {
                // 搜索框有输入时，支持直接跨省搜索全国任意城市或车牌代码（如输入“深圳”或“浙B”）
                val all = mutableListOf<FlatCityOption>()
                NationalCityData.forEachIndexed { pIdx, p ->
                    p.second.second.forEachIndexed { cIdx, c ->
                        val plateCode = "${p.first}${c.second}"
                        if (c.first.contains(q, ignoreCase = true) ||
                            p.second.first.contains(q, ignoreCase = true) ||
                            plateCode.contains(q, ignoreCase = true)
                        ) {
                            all.add(FlatCityOption(pIdx, p.first, p.second.first, cIdx, c.first, c.second))
                        }
                    }
                }
                all
            }
        }

        Dialog(
            onDismissRequest = { cityDialogOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .heightIn(max = 600.dp),
                shape = RoundedCornerShape(22.dp),
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
                            Text(
                                text = "选择城市（${currentProvince.second.first} · 可搜全国）",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFDE2910)
                            )
                            Text("支持直接输入全国任意城市名或车牌前缀（如：深圳 / 苏E）", fontSize = 11.sp, color = Color(0xFF7A4A45))
                        }
                        Surface(
                            onClick = { cityDialogOpen = false },
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
                        value = cityQuery,
                        onValueChange = { cityQuery = it },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        placeholder = { Text("输入全国任意城市名或代号（如：成都 / 粤B / 杭州）", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        itemsIndexed(displayCities) { _, opt ->
                            val isSel = opt.provIdx == selectedProvinceIdx && opt.cityIdx == selectedCityIdx
                            Surface(
                                onClick = {
                                    selectedProvinceIdx = opt.provIdx
                                    selectedCityIdx = opt.cityIdx
                                    cityDialogOpen = false
                                    regeneratePool(provIdx = opt.provIdx, cIdx = opt.cityIdx)
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSel) Color(0xFFFFF1F0) else Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSel) Color(0xFFDE2910) else Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF1A4FA0)
                                    ) {
                                        Text(
                                            text = "${opt.provShort}${opt.cityLetter}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "${opt.provName} · ${opt.cityName}",
                                        fontSize = 14.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
