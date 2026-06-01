package com.kovhan.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.navigation
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.core.ui.navigation.startDestination
import com.kovhan.feature.main.presentation.favorites.navigation.FavoritesScreenNavAction
import com.kovhan.feature.main.presentation.favorites.navigation.favoritesScreen
import com.kovhan.feature.main.presentation.home.navigation.HomeScreenNavAction
import com.kovhan.feature.main.presentation.home.navigation.homeScreen
import com.kovhan.feature.main.presentation.profile.navigation.ProfileScreenNavAction
import com.kovhan.feature.main.presentation.profile.navigation.profileScreen
import com.kovhan.feature.main.presentation.quotes.navigation.QuotesScreenNavAction
import com.kovhan.feature.main.presentation.quotes.navigation.quotesScreen

fun NavController.navigateToMainGraph(builder: NavOptionsBuilder.() -> Unit = { }){
    navigate(
        route = MainGraph,
        builder = builder,
    )
}

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    navigateToAuth: () -> Unit,
) {
    navigation<MainGraph>(
        startDestination = MainGraph.startDestination
    ) {
        homeScreen(
            navAction = object : HomeScreenNavAction {
                override fun navigateBack() {
                    navController.navigateUp()
                }
            },
            paddingValues = paddingValues
        )
        
        quotesScreen(
            navAction = object : QuotesScreenNavAction {
                override fun onBack() {
                    navController.navigateUp()
                }
                override fun navigateToQuoteDetails(quoteId: String) {
                    // TODO: Implement quote details navigation
                }
            },
            paddingValues = paddingValues
        )
        
        favoritesScreen(
            navAction = object : FavoritesScreenNavAction {
                override fun navigateBack() {
                    navController.navigateUp()
                }
                override fun navigateToQuoteDetails(quoteId: String) {
                    // TODO: Implement quote details navigation
                }
            },
            paddingValues = paddingValues
        )
        
        profileScreen(
            navAction = object : ProfileScreenNavAction {
                override fun navigateBack() {
                    navController.navigateUp()
                }
                override fun navigateToSettings() {
                    // TODO: Implement settings navigation
                }
                override fun navigateToAuth() {
                    navigateToAuth()
                }
            },
            paddingValues = paddingValues
        )
    }
} 