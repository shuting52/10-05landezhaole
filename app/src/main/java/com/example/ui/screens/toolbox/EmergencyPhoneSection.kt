package com.example.ui.screens.toolbox

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.ui.theme.FlameRed

/**
 * 紧急电话工具（v1.0.5 全面升级）
 *
 * - 全域服务电话逐一分类（急救救援/交通出行/政务服务/…），并采用「上下收纳」形式呈现：
 *   所有分类自上而下排布，点击分类标题展开/收起该分类的号码（默认展开第一个）；
 * - 每个紧急电话后都有「拨打」按钮，可一键快捷呼出（无权限自动打开拨号盘预填）；
 * - 覆盖全中国：地区选择精确到 省 → 市 → 县(区) → 镇(乡) → 村 六级，
 *   内置 34 个省级行政区全部地级市/区县，镇乡村可自由输入覆盖任意村落
 */
@Composable
fun EmergencyPhoneSection() {
    val context = LocalContext.current

    // 分类收纳：展开中的分类 id 集合（默认全部展开，方便直接查看补全后的所有紧急电话，也可一键全部展开/收起或搜索）
    var expandedCats by remember { mutableStateOf(NATIONAL_EMERGENCY_CATEGORIES.map { it.id }.toSet()) }
    var searchKeyword by remember { mutableStateOf("") }

    fun callNumber(number: String) {
        val clean = number.filter { it.isDigit() || it == '+' }
        try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$clean")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent)
            } else {
                // 无权限：打开拨号盘预填号码（同样一键可呼出）
                val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$clean")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    context.startActivity(dial)
                } catch (e: Exception) {
                    Toast.makeText(context, "无法打开拨号盘", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            try {
                val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$clean")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(dial)
            } catch (e2: Exception) {
                Toast.makeText(context, "无法呼出该号码", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 8.dp)) {
        // ---------- 顶部横幅 ----------
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = Color.White.copy(alpha = 0.55f),
            border = BorderStroke(1.dp, FlameRed.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(listOf(FlameRed, Color(0xFFFF9500)))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "紧急电话 · 全国通用",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "救援/道路/举报/法律 · 一键快捷呼出 · 全国通用",
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 快速搜索与一键展开/收起全部
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = { searchKeyword = it },
                placeholder = { Text("搜索单位名称、电话号码或用途（如：110 / 反诈 / 顺丰）", fontSize = 11.5.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            val allIds = remember { NATIONAL_EMERGENCY_CATEGORIES.map { it.id }.toSet() }
            val isAllExpanded = expandedCats.size == allIds.size
            OutlinedButton(
                onClick = {
                    expandedCats = if (isAllExpanded) emptySet() else allIds
                },
                shape = RoundedCornerShape(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(if (isAllExpanded) "全部收起" else "全部展开", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ---------- 分类收纳列表（上下展示，点击标题展开/收起） ----------
        NATIONAL_EMERGENCY_CATEGORIES.forEach { cat ->
            val q = searchKeyword.trim()
            val matchedNumbers = if (q.isEmpty()) {
                cat.numbers
            } else {
                cat.numbers.filter {
                    it.name.contains(q, ignoreCase = true) ||
                        it.number.contains(q, ignoreCase = true) ||
                        it.desc.contains(q, ignoreCase = true) ||
                        cat.title.contains(q, ignoreCase = true)
                }
            }
            if (matchedNumbers.isEmpty()) return@forEach

            val isExpanded = q.isNotEmpty() || (cat.id in expandedCats)
            // 分类标题栏（点击展开/收起）
            Surface(
                onClick = {
                    expandedCats = if (cat.id in expandedCats) expandedCats - cat.id else expandedCats + cat.id
                },
                shape = RoundedCornerShape(12.dp),
                color = if (isExpanded) FlameRed.copy(alpha = 0.10f) else Color.White.copy(alpha = 0.55f),
                border = BorderStroke(
                    1.dp,
                    if (isExpanded) FlameRed.copy(alpha = 0.45f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = cat.icon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = cat.title,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isExpanded) FlameRed else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${matchedNumbers.size} 个号码",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (isExpanded) "收起" else "展开",
                        tint = if (isExpanded) FlameRed else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            // 展开的号码列表
            if (isExpanded) {
                matchedNumbers.forEach { num ->
                    EmergencyNumberRow(
                        name = num.name,
                        number = num.number,
                        desc = num.desc,
                        icon = cat.icon,
                        onCall = { callNumber(num.number) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

/** 单个号码卡片 */
@Composable
private fun EmergencyNumberRow(
    name: String,
    number: String,
    desc: String,
    icon: String,
    onCall: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // icon 圆形徽标
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(FlameRed.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 17.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            // 名称 + 号码
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = number,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = FlameRed
                    )
                    if (desc.isNotBlank()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = desc,
                            fontSize = 9.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            // 拨打按钮
            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(containerColor = FlameRed),
                shape = RoundedCornerShape(10.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Icon(Icons.Filled.Call, contentDescription = "拨打", tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("拨打", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/**
 * 地区选择弹窗：省 → 市 → 县/区 → 镇/乡 → 村 六级
 * 省/市/县(区) 内置全中国数据逐级选择；镇/乡、村两级提供「自由输入」覆盖任意村落
 */
@Composable
private fun RegionPickerDialog(
    regions: List<Region>,
    currentProvince: String?,
    currentCity: RegionCity?,
    currentCounty: RegionCounty?,
    currentTown: String,
    currentVillage: String,
    onSelect: (String, RegionCity, RegionCounty?, String, String) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    // 当前选择的省/市/县对象（进入时沿用现有选择）
    var selProvince by remember { mutableStateOf(currentProvince) }
    var selCity by remember { mutableStateOf(currentCity) }
    var selCounty by remember { mutableStateOf(currentCounty) }
    var town by remember { mutableStateOf(currentTown) }
    var village by remember { mutableStateOf(currentVillage) }
    // 当前所在层级：0 省 / 1 市 / 2 县 / 3 镇乡村确认
    var level by remember { mutableStateOf(if (currentProvince == null) 0 else if (currentCity == null) 1 else if (currentCounty == null) 2 else 3) }

    fun confirm() {
        val p = selProvince ?: "全国"
        val reg = regions.firstOrNull { it.province == p }
        onSelect(p, selCity ?: reg?.cities?.firstOrNull() ?: RegionCity(name = p), selCounty, town, village)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .clip(RoundedCornerShape(18.dp)),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                // 标题栏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (level) {
                                0 -> "① 选择省份"
                                1 -> "② 选择市 / 自治州"
                                2 -> "③ 选择县 / 区"
                                else -> "④ 填写镇 / 乡 / 村"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = buildString {
                                append(selProvince ?: "全国")
                                selCity?.let { append(" · ${it.name}") }
                                selCounty?.let { append(" · ${it.name}") }
                                if (town.isNotBlank()) append(" · $town")
                                if (village.isNotBlank()) append(" · $village")
                            }.ifBlank { "尚未选择" },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row {
                        OutlinedButton(
                            onClick = onClear,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("恢复全国", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(onClick = onDismiss, shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                            Box(modifier = Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Close, contentDescription = "关闭", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                when (level) {
                    // ========== ① 省份 ==========
                    0 -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxSize()) {
                            items(regions, key = { it.province }) { region ->
                                SelectRow(
                                    text = region.province + "（${region.cities.size}市）",
                                    onClick = {
                                        selProvince = region.province
                                        selCity = null
                                        selCounty = null
                                        town = ""
                                        village = ""
                                        level = 1
                                    }
                                )
                            }
                        }
                    }
                    // ========== ② 市 / 自治州 ==========
                    1 -> {
                        val prov = regions.firstOrNull { it.province == selProvince }
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxSize()) {
                            item {
                                BackRow(text = "◂ 返回省份（${selProvince ?: ""}）") { level = 0 }
                            }
                            if (prov != null) {
                                items(prov.cities, key = { it.name }) { city ->
                                    SelectRow(
                                        text = city.name + "（区号 ${city.areaCode}）",
                                        onClick = {
                                            selCity = city
                                            selCounty = null
                                            town = ""
                                            village = ""
                                            level = 2
                                        }
                                    )
                                }
                            }
                        }
                    }
                    // ========== ③ 县 / 区 ==========
                    2 -> {
                        val city = selCity
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxSize()) {
                            item {
                                BackRow(text = "◂ 返回市（${city?.name ?: ""}）") { level = 1 }
                            }
                            if (city != null && city.counties.isNotEmpty()) {
                                items(city.counties, key = { it.name }) { county ->
                                    SelectRow(
                                        text = county.name,
                                        onClick = {
                                            selCounty = county
                                            town = ""
                                            village = ""
                                            level = 3
                                        }
                                    )
                                }
                            } else {
                                item { Text("该市暂无区县数据，可直接进入下一步填写镇/乡/村", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                                item { Spacer(modifier = Modifier.height(4.dp)) }
                            }
                        }
                    }
                    // ========== ④ 镇 / 乡 / 村（自由输入） ==========
                    else -> {
                        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                            if (selCounty != null) {
                                BackRow(text = "◂ 返回县/区（${selCounty?.name}）") { level = 2 }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            Text("镇 / 乡（可自由输入，覆盖任意镇/乡）", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            OutlinedTextField(
                                value = town,
                                onValueChange = { town = it },
                                placeholder = { Text("如：西丽街道 / 南村镇 / 城关镇…", fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("村（可自由输入，覆盖任意村）", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            OutlinedTextField(
                                value = village,
                                onValueChange = { village = it },
                                placeholder = { Text("如：上村 / 中心村 / 大坪村…", fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { confirm() },
                                colors = ButtonDefaults.buttonColors(containerColor = FlameRed),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().height(46.dp)
                            ) {
                                Text("✅ 确定地区（点击保存）", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

/** 普通选择行 */
@Composable
private fun SelectRow(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text("▸", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** 返回行 */
@Composable
private fun BackRow(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = FlameRed)
        }
    }
}