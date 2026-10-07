package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NavCard
import com.example.data.util.SiteMetadataFetcher
import com.example.ui.theme.FlameRed
import com.example.ui.theme.JadeGreen
import com.example.ui.theme.SunsetOrange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddSiteDialog(
    onDismiss: () -> Unit,
    onCheckDuplicate: (url: String, title: String) -> NavCard?,
    onConfirmAdd: (title: String, url: String, desc: String, categoryId: String, badge: String, iconUrl: String) -> Boolean
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var urlInput by remember { mutableStateOf("") }
    var titleInput by remember { mutableStateOf("") }
    var descInput by remember { mutableStateOf("") }
    var badgeInput by remember { mutableStateOf("NEW") }
    var iconUrlInput by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("ai") }

    var isFetchingMetadata by remember { mutableStateOf(false) }
    var fetchStatusMessage by remember { mutableStateOf("") }

    val categories = listOf(
        "coding_tools" to "编程工具",
        "ai" to "AI大模型",
        "media" to "常用影视",
        "software" to "实用软件",
        "study" to "学习站点",
        "tools" to "效率工具",
        "design" to "设计美化",
        "dev" to "开发者",
        "life" to "综合生活"
    )

    // Dynamic duplicate detection
    val duplicateMatch = remember(urlInput, titleInput) {
        if (urlInput.trim().length >= 4 || titleInput.trim().length >= 2) {
            onCheckDuplicate(urlInput, titleInput)
        } else null
    }

    val isDuplicate = duplicateMatch != null
    val canSubmit = urlInput.isNotBlank() && titleInput.isNotBlank() && !isDuplicate

    // Function to trigger auto extraction
    fun triggerAutoFetch(urlToFetch: String) {
        val trimmed = urlToFetch.trim()
        if (trimmed.length < 5) return
        coroutineScope.launch {
            isFetchingMetadata = true
            fetchStatusMessage = "正在自动获取站点标题与高清图标..."
            val meta = SiteMetadataFetcher.fetch(trimmed)
            if (meta.isSuccess) {
                if (titleInput.isBlank() || titleInput == urlToFetch) {
                    titleInput = meta.title
                }
                if (descInput.isBlank() && meta.desc.isNotBlank()) {
                    descInput = meta.desc
                }
                if (meta.iconUrl.isNotBlank()) {
                    iconUrlInput = meta.iconUrl
                }
                fetchStatusMessage = "✅ 已自动获取：【${meta.title}】与专属高清图标"
            } else {
                fetchStatusMessage = "⚡ 已自动提取域名并生成离线向量与云端图标"
                if (titleInput.isBlank()) {
                    titleInput = meta.title
                }
                if (meta.iconUrl.isNotBlank()) {
                    iconUrlInput = meta.iconUrl
                }
            }
            isFetchingMetadata = false
        }
    }

    // Auto trigger when user pastes/types valid looking URL
    LaunchedEffect(urlInput) {
        val trimmed = urlInput.trim()
        if (trimmed.length >= 8 && (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.contains("."))) {
            delay(700)
            if (titleInput.isBlank()) {
                triggerAutoFetch(trimmed)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.78f),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddLink,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "收录新站点 (自动去重与识别)",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.5.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "自动获取站点图标与名称，自动排除重复站点",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // URL input + Auto-fetch Button
                OutlinedTextField(
                    value = urlInput,
                    onValueChange = {
                        urlInput = it
                    },
                    label = { Text("站点网址 (必填)") },
                    placeholder = { Text("如：https://dreammaker.vobile.com.cn") },
                    singleLine = true,
                    trailingIcon = {
                        if (isFetchingMetadata) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else if (urlInput.trim().length >= 4) {
                            IconButton(onClick = { triggerAutoFetch(urlInput) }) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = "自动获取",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_site_url_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                // Quick Auto-fetch button
                if (urlInput.trim().length >= 4) {
                    OutlinedButton(
                        onClick = { triggerAutoFetch(urlInput) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isFetchingMetadata
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = SunsetOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isFetchingMetadata) "正在智能解析中..." else "自动获取站点名字与图标",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunsetOrange
                        )
                    }
                }

                // Fetch status text
                if (fetchStatusMessage.isNotBlank()) {
                    Text(
                        text = fetchStatusMessage,
                        fontSize = 11.sp,
                        color = JadeGreen,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Live Icon and Title Preview
                if (urlInput.isNotBlank() || titleInput.isNotBlank()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SiteBrandIcon(
                                url = urlInput.ifBlank { "https://example.com" },
                                title = titleInput.ifBlank { "站点预览" },
                                iconUrl = iconUrlInput,
                                size = 36.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = titleInput.ifBlank { "待获取站点名称" },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    RibbonBadge(
                                        text = badgeInput.ifBlank { "NEW" },
                                        badgeType = com.example.data.model.BadgeType.NEW
                                    )
                                }
                                Text(
                                    text = if (iconUrlInput.isNotBlank()) "已自动提取并绑定官方高清图标" else "已自动匹配多重云端与离线图标引擎",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = { Text("站点名称 (必填，可自动获取)") },
                    placeholder = { Text("例如：DreamMaker 或 OnSolo") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_site_title_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                // Real-time Duplicate Status Banner
                if (isDuplicate) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = FlameRed.copy(alpha = 0.12f)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(FlameRed)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Warning, contentDescription = null, tint = FlameRed, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "已自动检测并筛选排除重复站点！",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = FlameRed
                                )
                                Text(
                                    text = "该站点已在导航中收录为【${duplicateMatch?.title}】(${duplicateMatch?.url})，原站点不受影响，禁止重复添加。",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else if (urlInput.trim().length >= 4 && titleInput.trim().length >= 2) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = JadeGreen.copy(alpha = 0.12f)),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(JadeGreen)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = JadeGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "去重筛选通过：全新独特站点，将打上NEW新角标并置顶展示",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = JadeGreen
                            )
                        }
                    }
                }

                // Category selection
                Text(
                    text = "选择所属分类：",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { (catId, catName) ->
                        val isSelected = selectedCategoryId == catId
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedCategoryId = catId }
                        ) {
                            Text(
                                text = catName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = descInput,
                    onValueChange = { descInput = it },
                    label = { Text("站点简介 (选填)") },
                    placeholder = { Text("简要描述该站点是干嘛的、有什么特别之处") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = badgeInput,
                    onValueChange = { badgeInput = it },
                    label = { Text("角标新标签 (默认NEW)") },
                    placeholder = { Text("NEW") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (canSubmit) {
                        val formattedUrl = if (!urlInput.startsWith("http://") && !urlInput.startsWith("https://")) {
                            "https://${urlInput.trim()}"
                        } else {
                            urlInput.trim()
                        }
                        val success = onConfirmAdd(
                            titleInput.trim(),
                            formattedUrl,
                            descInput.trim().ifBlank { "高价值收录站点，提供专业数字化与生产力服务。" },
                            selectedCategoryId,
                            badgeInput.trim().ifBlank { "NEW" },
                            iconUrlInput.trim()
                        )
                        if (success) {
                            Toast.makeText(context, "✅ 站点收录成功！已添加NEW角标并置顶主界面", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        } else {
                            Toast.makeText(context, "⚠️ 重复站点已被系统自动排除", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = canSubmit,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDuplicate) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_add_site_button")
            ) {
                if (isDuplicate) {
                    Icon(Icons.Filled.Block, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("已排除重复站点", fontSize = 12.sp)
                } else {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("确认收录并置顶", fontSize = 12.sp)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
