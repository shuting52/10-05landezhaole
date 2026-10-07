package com.example.model

import org.json.JSONObject

/**
 * 反编译重建 · 数据模型
 * 由 v2.1.0 APK 反编译的 Kotlin data class 重建（字段/顺序与原始一致）
 */
data class CategoryItem(
    val id: String = "",
    val name: String = "",
    val iconKey: String = "",
    var order: Int = 0,
    val status: String = "",
    val desc: String = "",
    var cardCount: Int = 0,
    val subcategories: List<SubCategoryItem> = emptyList()
)
