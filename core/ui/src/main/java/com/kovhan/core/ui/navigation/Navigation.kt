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

/**
 * "You're all set" screen shown once after onboarding, before the user picks
 * sign-in vs sign-up. Lives in its own graph so the post-onboarding handoff
 * is independent from the auth flow itself.
 */
@Serializable
data object CompleteGraph : NavigationGraph() {
    @Serializable
    data object CompleteScreen
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

/**
 * Generic external-page graph. Any feature can navigate here with
 * `navController.navigateToWebView(title, url)` — handy for Privacy Policy,
 * Terms of Service, blog posts, etc.
 */
@Serializable
data object WebViewGraph : NavigationGraph() {
    @Serializable
    data class WebViewScreen(val title: String, val url: String)
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

val CompleteGraph.startDestination: CompleteGraph.CompleteScreen
    get() = CompleteGraph.CompleteScreen

val AuthGraph.startDestination: AuthGraph.LoginScreen
    get() = AuthGraph.LoginScreen

val MainGraph.startDestination: MainGraph.QuotesScreen
    get() = MainGraph.QuotesScreen

fun findGraphByRoute(route: String?): NavigationGraph? {
    if (route == null) return null

    return when {
        route.startsWith("splash", ignoreCase = true) -> SplashGraph
        route.startsWith("onboarding", ignoreCase = true) -> OnboardingGraph
        route.contains("CompleteGraph", ignoreCase = true) ||
        route.contains("CompleteScreen", ignoreCase = true) -> CompleteGraph
        route.startsWith("auth", ignoreCase = true) ||
        route.startsWith("login", ignoreCase = true) ||
        route.startsWith("register", ignoreCase = true) ||
        route.startsWith("forgot_password", ignoreCase = true) -> AuthGraph
        route.startsWith("main", ignoreCase = true) ||
        route.startsWith("home", ignoreCase = true) ||
        route.startsWith("quotes", ignoreCase = true) ||
        route.startsWith("favorites", ignoreCase = true) ||
        route.startsWith("profile", ignoreCase = true) -> MainGraph
        route.contains("WebView", ignoreCase = true) -> WebViewGraph

        else -> null
    }
} 