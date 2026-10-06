package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import com.example.data.model.BadgeType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NavCard
import com.example.data.model.NavCategory
import com.example.ui.theme.FlameRed
import com.example.ui.theme.SunsetOrange

/**
 * 随心抽中的分类独立弹窗：
 * 展示所有分类标签，支持切换选中分类并直接浏览呈现该分类下的所有站点内容列表。
 * 用户可在此弹窗中：
 * 1. 自由切换查看任意分类（或全部站点）
 * 2. 浏览当前分类下的所有站点卡片，包含图标、标题、介绍和直达/抽选操作
 * 3. 一键在当前分类中直接定向随心抽
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySitesDialog(
    categories: List<NavCategory>,
    selectedCategoryId: String,
    onSelectCategory: (String) -> Unit,
    onCardClick: (NavCard) -> Unit,
    onFavoriteToggle: (NavCard) -> Unit,
    favoriteUrls: Set<String>,
    onRollInCategory: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var activeCategory by remember(selectedCategoryId) { mutableStateOf(selectedCategoryId) }
    var filterQuery by remember { mutableStateOf("") }

    val currentCategoryObject = remember(activeCategory, categories) {
        categories.find { it.id == activeCategory }
    }

    val categorySites = remember(activeCategory, categories, filterQuery) {
        val allCards = if (activeCategory.isBlank()) {
            categories.flatMap { it.cards }.distinctBy { it.url.ifBlank { "${it.id}_${it.title}" } }
        } else {
            (currentCategoryObject?.cards ?: emptyList()).distinctBy { it.url.ifBlank { "${it.id}_${it.title}" } }
        }

        val filtered = if (filterQuery.isBlank()) {
            allCards
        } else {
            allCards.filter {
                it.title.contains(filterQuery, ignoreCase = true) ||
                it.desc.contains(filterQuery, ignoreCase = true) ||
                it.url.contains(filterQuery, ignoreCase = true)
            }
        }
        filtered.sortedWith(
            compareByDescending { card ->
                when {
                    card.badgeType == BadgeType.NEW ||
                            card.badge?.equals("NEW", ignoreCase = true) == true ||
                            card.badge?.contains("新", ignoreCase = true) == true -> 2
                    card.badgeType == BadgeType.ROSE || card.badgeType == BadgeType.GOLD -> 1
                    else -> 0
                }
            }
        )
    }

    val totalSitesCount = remember(categories) {
        categories.sumOf { it.cards.size }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White.copy(alpha = 0.72f),
        scrimColor = Color(0xFF140D26).copy(alpha = 0.36f),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // 1. Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.linearGradient(listOf(FlameRed, SunsetOrange))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Extension,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "分类站点大全",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentCategoryObject != null)
                                "${currentCategoryObject.name} · 共${categorySites.size}个站点"
                            else
                                "全部收录站点 (${categories.size}大分类 · 共${totalSitesCount}个站点)",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "关闭")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. 下拉分类选择框（以当前选中项为中心对齐，选中后自动收回并更新文本与全量站点联动）
            val dropdownItems = remember(categories) {
                listOf(
                    CenteredDropdownItem(
                        id = "",
                        title = "全部分类",
                        subtitle = "浏览全部收录站点 (${totalSitesCount}个)",
                        badge = "${totalSitesCount}"
                    )
                ) + categories.map { cat ->
                    CenteredDropdownItem(
                        id = cat.id,
                        title = cat.name,
                        subtitle = "${cat.cards.size} 个精选站点",
                        badge = "${cat.cards.size}"
                    )
                }
            }

            CenteredDropdownSelect(
                selectedId = activeCategory,
                items = dropdownItems,
                onItemSelected = { selectedCatId ->
                    activeCategory = selectedCatId
                    onSelectCategory(selectedCatId)
                },
                label = "当前浏览分类",
                placeholder = "选择分类...",
                accentColor = FlameRed,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Search & Quick Action Toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = filterQuery,
                    onValueChange = { filterQuery = it },
                    placeholder = { Text("在当前分类检索站点...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    trailingIcon = {
                        if (filterQuery.isNotEmpty()) {
                            IconButton(onClick = { filterQuery = "" }) {
                                Icon(Icons.Filled.Close, contentDescription = "清除", modifier = Modifier.size(14.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 44.dp)
                )

                Button(
                    onClick = {
                        onRollInCategory(activeCategory)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FlameRed),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("定向抽取", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Sites Content Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                if (categorySites.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "当前分类下未检索到站点",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                } else {
                    itemsIndexed(categorySites, key = { index, card -> "${card.id}_${card.categoryId}_${card.url}_$index" }) { _, card ->
                        ResourceCard(
                            card = card,
                            isFavorite = favoriteUrls.contains(card.url),
                            onCardClick = { onCardClick(it) },
                            onFavoriteToggle = { onFavoriteToggle(it) }
                        )
                    }
                }
            }
        }
    }
}
