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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalThemeUiColors
import org.json.JSONObject

/**
 * v1.4.18 新工具：ChatGPT做图指令大全
 * - assets/image_prompts.json：46 分类 / 356 条指令（用户提供，清洗去重）
 * - 分类横滑切换 + 关键词搜索 + 一键复制指令 key
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
    var selectedId by remember { mutableStateOf(categories.firstOrNull()?.id ?: "") }
    var query by remember { mutableStateOf("") }

    fun copyKey(key: String) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("image_prompt", key))
        Toast.makeText(context, "已复制：$key", Toast.LENGTH_SHORT).show()
    }

    val selected = categories.firstOrNull { it.id == selectedId }
    val shown: List<PromptItem> = remember(selected, query) {
        val base = selected?.prompts ?: emptyList()
        if (query.isBlank()) base
        else base.filter {
            it.key.contains(query, ignoreCase = true) || it.desc.contains(query, ignoreCase = true)
        }
    }
    // 全局搜索命中数（跨分类）
    val globalHits = remember(query) {
        if (query.isBlank()) 0
        else categories.sumOf { c -> c.prompts.count { it.key.contains(query, ignoreCase = true) || it.desc.contains(query, ignoreCase = true) } }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 标题
        Text(
            text = "🎨 ChatGPT做图指令大全",
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            color = themeUi.text,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = "${categories.size} 分类 · ${categories.sumOf { it.prompts.size }} 条指令，点击复制 key",
            fontSize = 12.sp,
            color = themeUi.textMuted,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 搜索框
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("搜索指令，如：黏土 / 霓虹 / 宠物", fontSize = 13.sp) },
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
                .padding(bottom = 8.dp)
        )
        if (query.isNotBlank()) {
            Text(
                text = "当前分类命中 ${shown.size} 条 · 全库命中 $globalHits 条（切换分类查看）",
                fontSize = 11.sp,
                color = themeUi.textMuted,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        // 分类横滑
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            items(categories, key = { it.id }) { cat ->
                val isSel = cat.id == selectedId
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSel) Brush.linearGradient(listOf(themeUi.primary, themeUi.secondary))
                            else Brush.linearGradient(listOf(themeUi.surface.copy(alpha = 0.7f), themeUi.surface.copy(alpha = 0.7f)))
                        )
                        .border(
                            1.dp,
                            if (isSel) Color.Transparent else themeUi.primary.copy(alpha = 0.3f),
                            RoundedCornerShape(50)
                        )
                        .clickable { selectedId = cat.id; query = "" }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${cat.name} ${cat.prompts.size}",
                        fontSize = 13.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSel) Color.White else themeUi.text
                    )
                }
            }
        }

        // 指令列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(shown, key = { it.key }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(themeUi.surface.copy(alpha = 0.75f))
                        .border(1.dp, themeUi.primary.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
                        .clickable { copyKey(item.key) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
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
                            lineHeight = 17.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(listOf(themeUi.primary, themeUi.secondary))
                            )
                            .clickable { copyKey(item.key) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.ContentCopy,
                            contentDescription = "复制",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            if (shown.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("没有匹配的指令，换个关键词试试", fontSize = 13.sp, color = themeUi.textMuted)
                    }
                }
            }
        }
    }
}
