package com.example.model

import org.json.JSONObject

/**
 * 反编译重建 · 数据模型
 * 由 v2.1.0 APK 反编译的 Kotlin data class 重建（字段/顺序与原始一致）
 */
data class UpdateDialogConfig(
    val title: String = "",
    val confirmText: String = "",
    val cancelText: String = "",
    val changelog: List<String> = emptyList(),
    val customCss: String = "",
    val customHtml: String = ""
)
