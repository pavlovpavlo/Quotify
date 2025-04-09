package com.kovhan.core.ui.navigation

import androidx.annotation.DrawableRes

// Клас для представлення вкладки нижньої навігації
data class Tab(
    val destination: Any,
    val title: String,
    @DrawableRes val icon: Int
)

sealed class TabScreen

data object HomeTabScreen : TabScreen()
data object QuotesTabScreen : TabScreen()
data object FavoritesTabScreen : TabScreen() 
data object ProfileTabScreen : TabScreen()

enum class TabEnum(val screen: TabScreen, val destination: Any) {
    HOME(HomeTabScreen, MainGraph.HomeScreen),
    QUOTES(QuotesTabScreen, MainGraph.QuotesScreen),
    FAVORITES(FavoritesTabScreen, MainGraph.FavoritesScreen),
    PROFILE(ProfileTabScreen, MainGraph.ProfileScreen);
    
    companion object {
        fun fromRoute(route: String?): TabEnum? {
            if (route == null) return null
            
            return when {
                route.contains("home", ignoreCase = true) -> HOME
                route.contains("quotes", ignoreCase = true) -> QUOTES
                route.contains("favorites", ignoreCase = true) -> FAVORITES
                route.contains("profile", ignoreCase = true) -> PROFILE
                else -> null
            }
        }
    }
} 