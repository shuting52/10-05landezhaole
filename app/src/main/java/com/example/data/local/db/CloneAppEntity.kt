package com.example.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clone_apps")
data class CloneAppEntity(
    @PrimaryKey val id: String, // format: "${packageName}_${cloneIndex}"
    val originalAppName: String,
    val packageName: String,
    val cloneName: String, // e.g. "微信一", "微信二", "微信三"
    val cloneIndex: Int, // 1, 2, 3...
    val isPinnedToDesktop: Boolean = true,
    val launchCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLaunchAt: Long = 0L
)
