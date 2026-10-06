package com.example.clonecenter.model

/**
 * 已安装应用信息（分身助手扫描结果）
 */
data class InstalledApp(
    val packageName: String,
    val label: String,
    val activityName: String,
    val isSystemApp: Boolean,
    val icon: android.graphics.drawable.Drawable
)
