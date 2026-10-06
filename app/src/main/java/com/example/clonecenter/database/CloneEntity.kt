package com.example.clonecenter.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clone_instances")
data class CloneEntity(
    @PrimaryKey
    val id: String,

    val packageName: String,

    val appLabel: String,

    val profileUserId: Int?,

    val state: String,

    val createdAt: Long
)
