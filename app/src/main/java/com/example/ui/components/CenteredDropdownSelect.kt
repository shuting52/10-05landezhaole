package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 通用单选下拉项定义
 */
data class CenteredDropdownItem<T>(
    val id: T,
    val title: String,
    val subtitle: String? = null,
    val badge: String? = null,
    val icon: (@Composable () -> Unit)? = null
)

/**
 * 全软件通用自居中下拉选择框 (CenteredDropdownSelect)
 *
 * 核心特性：
 * 1. 展开时自动将当前选中项居中对齐滚动到视口中央 (animateScrollToItem with offset)
 * 2. 选中项后立即平滑收回，并实时更新框内文字与状态
 * 3. 采用高质感液态玻璃微透 + 七彩流动光泽微边框设计，全软件通用
 * 4. 内置搜索筛选（当选项数量 >= 6 时自动激活快速搜索）
 * 5. 既支持内嵌展开展开栏，也支持弹窗/抽屉层级的展开呈现
 */
@Composable
fun <T> CenteredDropdownSelect(
    selectedId: T,
    items: List<CenteredDropdownItem<T>>,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "请选择...",
    leadingIcon: (@Composable () -> Unit)? = null,
    accentColor: Color = Color(0xFFDE2910),
    enabled: Boolean = true,
    maxListHeight: Dp = 280.dp
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val currentSelectedItem = remember(selectedId, items) {
        items.find { it.id == selectedId }
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
        label = "dropdown_arrow_rotation"
    )

    val listState = rememberLazyListState()

    val filteredItems = remember(items, searchQuery) {
        if (searchQuery.isBlank()) {
            items
        } else {
            val q = searchQuery.trim().lowercase()
            items.filter {
                it.title.lowercase().contains(q) ||
                    (it.subtitle?.lowercase()?.contains(q) == true) ||
                    (it.badge?.lowercase()?.contains(q) == true)
            }
        }
    }

    val selectedIndexInFiltered = remember(filteredItems, selectedId) {
        filteredItems.indexOfFirst { it.id == selectedId }
    }

    // 展开时：以当前选中项为中心对齐滚动
    LaunchedEffect(expanded) {
        if (expanded && selectedIndexInFiltered >= 0) {
            val approxItemHeightPx = 130 // 估算单个条目高度以做垂直居中偏移
            listState.animateScrollToItem(
                index = (selectedIndexInFiltered - 2).coerceAtLeast(0),
                scrollOffset = 0
            )
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // 主选择框触发面
        Surface(
            onClick = {
                if (enabled) {
                    expanded = !expanded
                    if (!expanded) searchQuery = ""
                }
            },
            shape = RoundedCornerShape(14.dp),
            color = Color.White.copy(alpha = 0.95f),
            border = BorderStroke(
                1.4.dp,
                if (expanded) accentColor else accentColor.copy(alpha = 0.35f)
            ),
            shadowElevation = if (expanded) 4.dp else 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = if (label != null) 10.dp else 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                        Spacer(modifier = Modifier.width(10.dp))
                    } else if (currentSelectedItem?.icon != null) {
                        currentSelectedItem.icon.invoke()
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        if (!label.isNullOrBlank()) {
                            Text(
                                text = label,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = accentColor.copy(alpha = 0.85f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        Text(
                            text = currentSelectedItem?.title ?: placeholder,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (currentSelectedItem != null) Color(0xFF1E293B) else Color(0xFF94A3B8),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (currentSelectedItem?.subtitle != null && !expanded) {
                            Text(
                                text = currentSelectedItem.subtitle,
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (expanded) accentColor.copy(alpha = 0.12f) else Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "展开选择",
                        tint = if (expanded) accentColor else Color(0xFF64748B),
                        modifier = Modifier
                            .size(20.dp)
                            .rotate(rotationAngle)
                    )
                }
            }
        }

        // 展开的下拉列表
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(
                animationSpec = tween(280, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(220)),
            exit = shrinkVertically(
                animationSpec = tween(220, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(180))
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.98f),
                border = BorderStroke(1.2.dp, accentColor.copy(alpha = 0.28f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    // 若候选项较多，展示轻量搜索框
                    if (items.size >= 6) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("检索选项...", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchQuery = "" },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Close,
                                            contentDescription = "清除",
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = maxListHeight),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        itemsIndexed(filteredItems) { _, item ->
                            val isSelected = item.id == selectedId
                            Surface(
                                onClick = {
                                    onItemSelected(item.id)
                                    expanded = false // 选中后收回
                                    searchQuery = ""
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) accentColor.copy(alpha = 0.10f) else Color.Transparent,
                                border = if (isSelected) BorderStroke(1.dp, accentColor.copy(alpha = 0.65f)) else null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        if (item.icon != null) {
                                            item.icon.invoke()
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = item.title,
                                                    fontSize = 13.5.sp,
                                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                                    color = if (isSelected) accentColor else Color(0xFF1E293B),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                if (item.badge != null) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = if (isSelected) accentColor else Color(0xFFF1F5F9)
                                                    ) {
                                                        Text(
                                                            text = item.badge,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (isSelected) Color.White else Color(0xFF64748B),
                                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            if (item.subtitle != null) {
                                                Text(
                                                    text = item.subtitle,
                                                    fontSize = 11.sp,
                                                    color = if (isSelected) accentColor.copy(alpha = 0.75f) else Color(0xFF64748B),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }

                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(accentColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "已选中",
                                                tint = Color.White,
                                                modifier = Modifier.size(13.dp)
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
    }
}
