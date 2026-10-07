package com.example.model

import org.json.JSONObject

/**
 * 反编译重建 · 数据模型
 * 由 v2.1.0 APK 反编译的 Kotlin data class 重建（字段/顺序与原始一致）
 */
data class FullSettingsConfig(
    val appName: String = "",
    val slogan: String = "",
    val logoUrl: String = "",
    val packageName: String = "",
    val aboutText: String = "",
    val feedbackEmail: String = "",
    val officialWebsite: String = "",
    val qqGroupUrl: String = "",
    val qqGroupUin: String = "",
    val contactQQ: String = "",
    val contactWechat: String = "",
    val contactAlipay: String = "",
    val securityEnabled: Boolean = false,
    val securityExpectedSha: String = "",
    val serverShutdownEnabled: Boolean = false,
    val serverShutdownNotice: String = ""
)
