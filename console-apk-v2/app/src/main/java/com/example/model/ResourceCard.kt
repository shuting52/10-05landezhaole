package com.example.model

import org.json.JSONObject

/**
 * 反编译重建 · 数据模型
 * 由 v2.1.0 APK 反编译的 Kotlin data class 重建（字段/顺序与原始一致）
 */
data class ResourceCard(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val buttonType: ButtonType = ButtonType.LINK,
    val size: String = "",
    val downloads: Int = 0,
    val status: CardStatus = CardStatus.DRAFT,
    val updatedAt: String = "",
    val category: String = "",
    val url: String = "",
    val icon: String = "",
    val fallbackText: String = "",
    val categoryId: String = "",
    val subcatId: String = "",
    val badge: String = "",
    val badgeType: String = "",
    val highlights: String = "",
    val rawJson: JSONObject? = null
)
