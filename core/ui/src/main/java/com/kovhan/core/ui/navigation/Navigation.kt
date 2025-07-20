package com.kovhan.core.ui.navigation

import kotlinx.serialization.Serializable

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

@Serializable
data object AuthGraph : NavigationGraph() {
    @Serializable
    data object LoginScreen
    
    @Serializable
    data object RegisterScreen
    
    @Serializable
    data object ForgotPasswordScreen
}

@Serializable
data object MainGraph : NavigationGraph() {
    @Serializable
    data object HomeScreen
    
    @Serializable
    data object QuotesScreen
    
    @Serializable
    data object FavoritesScreen
    
    @Serializable
    data object ProfileScreen
}

val NavigationGraph.showBottomBar: Boolean
    get() = when (this) {
        is MainGraph -> true
        else -> false
    }

val SplashGraph.startDestination: SplashGraph.SplashScreen
    get() = SplashGraph.SplashScreen

val OnboardingGraph.startDestination: OnboardingGraph.OnboardingScreen
    get() = OnboardingGraph.OnboardingScreen

val AuthGraph.startDestination: AuthGraph.LoginScreen
    get() = AuthGraph.LoginScreen

val MainGraph.startDestination: MainGraph.HomeScreen
    get() = MainGraph.HomeScreen

fun findGraphByRoute(route: String?): NavigationGraph? {
    if (route == null) return null

    return when {
        route.startsWith("splash", ignoreCase = true) -> SplashGraph
        route.startsWith("onboarding", ignoreCase = true) -> OnboardingGraph
        route.startsWith("auth", ignoreCase = true) || 
        route.startsWith("login", ignoreCase = true) || 
        route.startsWith("register", ignoreCase = true) || 
        route.startsWith("forgot_password", ignoreCase = true) -> AuthGraph
        route.startsWith("main", ignoreCase = true) || 
        route.startsWith("home", ignoreCase = true) || 
        route.startsWith("quotes", ignoreCase = true) || 
        route.startsWith("favorites", ignoreCase = true) || 
        route.startsWith("profile", ignoreCase = true) -> MainGraph
        
        else -> null
    }
} 