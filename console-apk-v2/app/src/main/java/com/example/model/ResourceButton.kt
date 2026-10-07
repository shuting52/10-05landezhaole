package com.example.model

import org.json.JSONObject

/**
 * 反编译重建 · 数据模型
 * 由 v2.1.0 APK 反编译的 Kotlin data class 重建（字段/顺序与原始一致）
 */
data class ResourceButton(
    val id: String = "",
    val name: String = "",
    val type: ButtonType = ButtonType.LINK,
    val usageCount: Int = 0,
    val status: String = "",
    val updatedAt: String = "",
    val url: String = "",
    val desc: String = "",
    val author: String = "",
    val badge: String = "",
    val badgeType: String = "",
    val tags: String = "",
    val apkUrl: String = "",
    val previewUrl: String = "",
    val iconUrl: String = "",
    val mode: String = "url",
    val rawJson: JSONObject? = null
)
