package com.example.ui.components

import com.example.data.model.NavCard
import java.net.URI

data class SiteProfile(
    val categoryLabel: String,
    val domain: String,
    val purpose: String,
    val highlights: List<String>,
    val tags: List<String>
)

object SiteFeatureResolver {
    fun resolve(card: NavCard): SiteProfile {
        val domain = try {
            val host = URI(card.url).host ?: card.url
            host.removePrefix("www.")
        } catch (e: Exception) {
            card.url
        }

        val highlights = if (!card.highlights.isNullOrBlank()) {
            card.highlights.split("；", ";").map { it.trim() }.filter { it.isNotBlank() }
        } else {
            listOf(
                "全天候稳定在线与极速响应",
                "经过严格安全检测与隐私合规验证",
                "界面简洁纯净，开箱即用"
            )
        }

        val tags = mutableListOf<String>()
        val badgeStr = card.badge.orEmpty()
        if (badgeStr.isNotBlank()) tags.add(badgeStr)
        val shortTag = if (!card.fallbackText.isNullOrBlank()) card.fallbackText else card.title.take(4)
        tags.add(shortTag)
        tags.add("高分推荐")

        return SiteProfile(
            categoryLabel = if (badgeStr.isNotBlank()) badgeStr else "优质站点",
            domain = domain,
            purpose = card.desc.ifBlank { "高品质精选在线服务与实用生产力工具，支持一键访问与即时体验。" },
            highlights = highlights,
            tags = tags
        )
    }
}
