package com.example.model

import org.json.JSONObject

/**
 * 反编译重建 · 数据模型
 * 由 v2.1.0 APK 反编译的 Kotlin data class 重建（字段/顺序与原始一致）
 */
data class StatItem(
    val id: String = "",
    val label: String = "",
    val value: String = "",
    val delta: String = "",
    val deltaLabel: String = "",
    val actionLabel: String = "",
    val icon: StatIcon = StatIcon.USERS,
    val tone: StatTone = StatTone.GOLD,
    val actionScreen: AdminScreen = AdminScreen.DASHBOARD
)
