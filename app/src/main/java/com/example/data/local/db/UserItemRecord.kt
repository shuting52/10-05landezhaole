package com.example.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_records")
data class UserItemRecord(
    @PrimaryKey
    val url: String,
    val title: String,
    val categoryName: String = "",
    val badge: String? = null,
    val desc: String? = null,
    val iconUrl: String? = null,
    val isFavorite: Boolean = false,
    val visitedTimestamp: Long = 0L,
    val visitCount: Int = 0
)
