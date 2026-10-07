package com.example.data.model

enum class PromptMediaType(val label: String, val iconKey: String) {
    IMAGE("图片Prompt", "image"),
    VIDEO("视频Prompt", "video")
}

data class PromptItem(
    val id: String,
    val title: String,
    val promptText: String,
    val negativePrompt: String = "",
    val chineseDesc: String = "",
    val mediaType: PromptMediaType = PromptMediaType.IMAGE,
    val previewDrawableRes: Int? = null,
    val previewMediaUri: String? = null,
    val videoDuration: String? = null,
    val targetModel: String = "Midjourney v6.1",
    val aspectRatio: String = "16:9",
    val category: String = "赛博科幻",
    val parameters: String = "--ar 16:9 --v 6.1 --stylize 250",
    val author: String = "社区精选",
    val copyCount: Int = 1200,
    val isCustom: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
