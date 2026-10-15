package com.example.ui.screens.toolbox

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.LocalThemeUiColors
import org.json.JSONObject
import kotlin.random.Random

/**
 * v1.4.20 做图指令大全重制版：
 * - assets/image_prompts.json：160 分类 / 1496 条指令（全域覆盖）
 * - 两级布局：分类宫格（2 列）→ 指令列表；顶部搜索全局直达；随机一条发现灵感
 * - 每条：一键复制 key / 复制全文（key + 中文说明）
 */
private data class PromptItem(val key: String, val desc: String)
private data class PromptCategory(val id: String, val name: String, val prompts: List<PromptItem>)

private fun loadPromptCategories(context: Context): List<PromptCategory> {
    return try {
        val json = context.assets.open("image_prompts.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val arr = root.getJSONArray("categories")
        buildList {
            for (i in 0 until arr.length()) {
                val c = arr.getJSONObject(i)
                val parr = c.getJSONArray("prompts")
                val prompts = buildList {
                    for (j in 0 until parr.length()) {
                        val p = parr.getJSONObject(j)
                        add(PromptItem(p.getString("key"), p.getString("desc")))
                    }
                }
                add(PromptCategory(c.getString("id"), c.getString("name"), prompts))
            }
        }
    } catch (e: Exception) {
        emptyList()
    }
}

@Composable
fun ImagePromptsSection() {
    val context = LocalContext.current
    val themeUi = LocalThemeUiColors.current
    val categories = remember { loadPromptCategories(context) }
    val totalPrompts = remember(categories) { categories.sumOf { it.prompts.size } }
    var query by remember { mutableStateOf("") }
    var openedCategory by remember { mutableStateOf<PromptCategory?>(null) }
    var randomPick by remember { mutableStateOf<Pair<PromptCategory, PromptItem>?>(null) }

    fun copyText(label: String, text: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText(label, text))
        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
    }

    // 全局搜索结果（分类名/key/中文说明）
    val searchResults: List<Pair<PromptCategory, PromptItem>> = remember(query, categories) {
        if (query.isBlank()) emptyList()
        else categories.flatMap { c ->
            val hitCat = c.name.contains(query, ignoreCase = true)
            c.prompts.filter {
                hitCat || it.key.contains(query, ignoreCase = true) || it.desc.contains(query, ignoreCase = true)
            }.map { c to it }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
        // ===== 标题区 =====
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "🎨 做图指令大全",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = themeUi.text,
                modifier = Modifier.weight(1f)
            )
            // 随机一条
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Brush.linearGradient(listOf(themeUi.primary, themeUi.secondary)))
                    .clickable {
                        val all = categories.flatMap { c -> c.prompts.map { c to it } }
                        if (all.isNotEmpty()) randomPick = all[Random.nextInt(all.size)]
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Casino, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("随机一条", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        Text(
            text = "${categories.size} 分类 · $totalPrompts 条指令 · 点复制 key，长按整行复制全文",
            fontSize = 12.sp,
            color = themeUi.textMuted,
            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
        )

        // ===== 搜索框 =====
        OutlinedTextField(
            value = query,
            onValueChange = { query = it; openedCategory = null },
            placeholder = { Text("全库搜索，如：黏土 / 霓虹 / 宠物 / 婚礼", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = themeUi.textMuted) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = themeUi.primary,
                unfocusedBorderColor = themeUi.primary.copy(alpha = 0.25f),
                cursorColor = themeUi.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        when {
            // ===== 搜索结果（扁平，带分类标签） =====
            query.isNotBlank() -> {
                Text(
                    text = "全库命中 ${searchResults.size} 条",
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, color = themeUi.textMuted,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(searchResults, key = { it.second.key }) { (cat, item) ->
                        PromptRow(
                            categoryName = cat.name,
                            item = item,
                            onCopyKey = { copyText("image_prompt", item.key) },
                            onCopyFull = { copyText("image_prompt_full", "${item.key}\n${item.desc}") }
                        )
                    }
                    if (searchResults.isEmpty()) {
                        item { EmptyHint("没有匹配的指令，换个关键词试试") }
                    }
                }
            }
            // ===== 分类内指令列表（二级） =====
            openedCategory != null -> {
                val cat = openedCategory!!
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { openedCategory = null }
                        .padding(vertical = 4.dp)
                        .padding(bottom = 6.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "返回", tint = themeUi.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("全部分类", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = themeUi.primary)
                    Text("  /  ${cat.name}（${cat.prompts.size} 条）", fontSize = 13.sp, color = themeUi.textMuted)
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(cat.prompts, key = { it.key }) { item ->
                        PromptRow(
                            categoryName = null,
                            item = item,
                            onCopyKey = { copyText("image_prompt", item.key) },
                            onCopyFull = { copyText("image_prompt_full", "${item.key}\n${item.desc}") }
                        )
                    }
                }
            }
            // ===== 分类宫格（一级） =====
            else -> {
                if (categories.isEmpty()) {
                    EmptyHint("指令库加载失败，请重启 App 试试")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(categories, key = { it.id }) { cat ->
                            CategoryCard(
                                cat = cat,
                                onClick = { openedCategory = cat }
                            )
                        }
                    }
                }
            }
        }
    }

    // ===== 随机一条弹窗（覆盖全屏） =====
    randomPick?.let { (cat, item) ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable { randomPick = null },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(themeUi.surface)
                    .border(1.dp, themeUi.primary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = themeUi.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("🎲 灵感来了", fontSize = 15.sp, fontWeight = FontWeight.Black, color = themeUi.text)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(cat.name, fontSize = 11.sp, color = themeUi.textMuted)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(item.key, fontSize = 16.sp, fontWeight = FontWeight.Black, color = themeUi.primary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(item.desc, fontSize = 13.sp, color = themeUi.textMuted, lineHeight = 19.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(listOf(themeUi.primary, themeUi.secondary)))
                            .clickable {
                                copyText("image_prompt", item.key)
                                randomPick = null
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("复制 key", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, themeUi.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable {
                                val all = categories.flatMap { c -> c.prompts.map { c to it } }
                                if (all.isNotEmpty()) randomPick = all[Random.nextInt(all.size)]
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("换一条", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = themeUi.primary)
                    }
                }
            }
        }
    }
    }
}

/** 分类宫格卡片 */
@Composable
private fun CategoryCard(cat: PromptCategory, onClick: () -> Unit) {
    val themeUi = LocalThemeUiColors.current
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(themeUi.surface.copy(alpha = 0.8f))
            .border(1.dp, themeUi.primary.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        // 顶部色条装饰
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(Brush.linearGradient(listOf(themeUi.primary, themeUi.secondary)))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = cat.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = themeUi.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${cat.prompts.size} 条指令 →",
            fontSize = 11.5.sp,
            color = themeUi.textMuted
        )
    }
}

/** 单条指令行：key + 中文说明 + 复制按钮（整行可点复制 key） */
@Composable
private fun PromptRow(
    categoryName: String?,
    item: PromptItem,
    onCopyKey: () -> Unit,
    onCopyFull: () -> Unit
) {
    val themeUi = LocalThemeUiColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(themeUi.surface.copy(alpha = 0.75f))
            .border(1.dp, themeUi.primary.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
            .clickable { onCopyKey() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (categoryName != null) {
                Text(
                    text = categoryName,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeUi.secondary,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = item.key,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = themeUi.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = item.desc,
                fontSize = 12.5.sp,
                color = themeUi.textMuted,
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 复制 key
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(listOf(themeUi.primary, themeUi.secondary)))
                    .clickable { onCopyKey() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ContentCopy, contentDescription = "复制key", tint = Color.White, modifier = Modifier.size(17.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            // 复制全文
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, themeUi.primary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                    .clickable { onCopyFull() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("全文", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = themeUi.primary)
            }
        }
    }
}

@Composable
private fun EmptyHint(text: String) {
    val themeUi = LocalThemeUiColors.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 13.sp, color = themeUi.textMuted)
    }
}
