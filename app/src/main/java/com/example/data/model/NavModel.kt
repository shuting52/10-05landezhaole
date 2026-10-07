package com.example.data.model

enum class BadgeType {
    ROSE,
    NEW,
    GOLD,
    BLUE
}

data class NavCard(
    val id: String,
    val title: String,
    val url: String,
    val icon: String = "",
    val fallbackText: String = "",
    val badge: String? = null,
    val badgeType: BadgeType = BadgeType.ROSE,
    val desc: String = "",
    val categoryId: String = "",
    val subcatId: String = "",
    val highlights: String = ""
)

data class NavSubCategory(
    val id: String,
    val name: String
)

data class NavCategory(
    val id: String,
    val name: String,
    val iconKey: String = "grid",
    val desc: String = "",
    val subcategories: List<NavSubCategory> = emptyList(),
    val cards: List<NavCard> = emptyList()
)

data class SearchEngine(
    val name: String,
    val placeholder: String,
    val urlTemplate: String? = null // null for in-app search
)
