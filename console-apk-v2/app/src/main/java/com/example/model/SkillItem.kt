package com.example.model

import org.json.JSONObject

/**
 * 反编译重建 · 数据模型
 * 由 v2.1.0 APK 反编译的 Kotlin data class 重建（字段/顺序与原始一致）
 */
data class SkillItem(
    val id: String = "",
    val title: String = "",
    val type: String = "",
    val promptType: String = "",
    val prompt: String = "",
    val url: String = "",
    val author: String = "",
    val badge: String = "",
    val tags: String = "",
    val previewUrl: String = "",
    val mediaUrl: String = "",
    val iconUrl: String = "",
    val mode: String = "url",
    val desc: String = "",
    val rawJson: JSONObject? = null
)
