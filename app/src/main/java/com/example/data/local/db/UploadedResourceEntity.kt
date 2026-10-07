package com.example.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "uploaded_resources")
data class UploadedResourceEntity(
    @PrimaryKey
    val id: String,
    val type: String, // "software" or "skill" or "prompt_image" / "prompt_video"
    val title: String,
    val desc: String,
    val url: String,
    val author: String,
    val badge: String = "用户上传",
    val tags: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    // 控制台以文件方式发布的下载资源（APK / ZIP / MD），本体软件可直接下载
    val fileUrl: String = "",
    val fileType: String = "", // 文件类型：APK / ZIP / MD
    // v5 新增：提示词正文 / 预览图 / 图标 / 演示视频 / 下载模式
    val prompt: String = "",
    val previewUrl: String = "",
    val iconUrl: String = "",
    val mediaUrl: String = "",
    val mode: String = "file" // file=文件下载 / url=URL跳转
)
