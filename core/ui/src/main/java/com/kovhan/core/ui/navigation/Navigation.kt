package com.kovhan.core.ui.navigation

import kotlinx.serialization.Serializable

// Графи навігації
@Serializable
sealed class NavigationGraph

@Serializable
data object SplashGraph : NavigationGraph() {
    @Serializable
    data object SplashScreen
}

@Serializable
data object OnboardingGraph : NavigationGraph() {
    @Serializable
    data object OnboardingScreen
}

// Основний граф, що включає в себе всі таби
@Serializable
data object MainGraph : NavigationGraph() {
    // Екрани головного графа
    @Serializable
    data object HomeScreen
    
    @Serializable
    data object QuotesScreen
    
    @Serializable
    data object FavoritesScreen
    
    @Serializable
    data object ProfileScreen
}

// Extension property для відображення BottomBar
val NavigationGraph.showBottomBar: Boolean
    get() = when (this) {
        is MainGraph -> true
        else -> false
    }

// Отримання початкового екрану для графа
val SplashGraph.startDestination: SplashGraph.SplashScreen
    get() = SplashGraph.SplashScreen

val OnboardingGraph.startDestination: OnboardingGraph.OnboardingScreen
    get() = OnboardingGraph.OnboardingScreen

val MainGraph.startDestination: MainGraph.HomeScreen
    get() = MainGraph.HomeScreen

// Функції для роботи з маршрутами в NavDestination
fun findGraphByRoute(route: String?): NavigationGraph? {
    if (route == null) return null
    
    // Перевіряємо по префіксу маршруту до якого графа він належить
    return when {
        route.startsWith("splash", ignoreCase = true) -> SplashGraph
        route.startsWith("onboarding", ignoreCase = true) -> OnboardingGraph
        route.startsWith("main", ignoreCase = true) || 
        route.startsWith("home", ignoreCase = true) || 
        route.startsWith("quotes", ignoreCase = true) || 
        route.startsWith("favorites", ignoreCase = true) || 
        route.startsWith("profile", ignoreCase = true) -> MainGraph
        
        else -> null
    }
} 