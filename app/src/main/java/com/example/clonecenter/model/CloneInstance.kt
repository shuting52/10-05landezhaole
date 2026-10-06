package com.example.clonecenter.model

/**
 * 分身实例（Work Profile 中创建的应用分身记录）
 */
data class CloneInstance(
    val id: String,
    val packageName: String,
    val appLabel: String,
    val profileUserId: Int?,
    val state: String,
    val createdAt: Long
)
