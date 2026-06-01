package com.kovhan.core.ui.navigation

enum class TabEnum(val destination: Any) {
    LIBRARY(MainGraph.QuotesScreen),
    PROFILE(MainGraph.ProfileScreen);

    companion object {
        fun fromRoute(route: String?): TabEnum? {
            if (route == null) return null
            return when {
                route.contains("quotes", ignoreCase = true) -> LIBRARY
                route.contains("profile", ignoreCase = true) -> PROFILE
                else -> null
            }
        }
    }
}
