package com.kovhan.feature.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
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

fun NavGraphBuilder.mainGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    navigation<MainGraph>(
        startDestination = MainGraph.startDestination
    ) {
        homeScreen(
            navAction = HomeScreenNavAction(
                navigateBack = {
                    navController.navigateUp()
                }
            ),
            paddingValues = paddingValues
        )
        
        quotesScreen(
            navAction = QuotesScreenNavAction(
                onBack = {
                    navController.navigateUp()
                }
            ),
            paddingValues = paddingValues
        )
        
        favoritesScreen(
            navAction = FavoritesScreenNavAction(
                navigateBack = {
                    navController.navigateUp()
                }
            ),
            paddingValues = paddingValues
        )
        
        profileScreen(
            navAction = ProfileScreenNavAction(
                navigateBack = {
                    navController.navigateUp()
                }
            ),
            paddingValues = paddingValues
        )
    }
} 