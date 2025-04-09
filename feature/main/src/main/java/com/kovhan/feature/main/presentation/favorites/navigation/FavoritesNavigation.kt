package com.kovhan.feature.main.presentation.favorites.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.feature.main.presentation.favorites.FavoritesScreen
import com.kovhan.feature.main.presentation.favorites.FavoritesScreenViewModel
import com.kovhan.feature.main.presentation.favorites.mvi.FavoritesScreenEffect

// Константи для швидших анімацій
private const val ANIMATION_DURATION = 150 // Мілісекунди

internal fun NavGraphBuilder.favoritesScreen(
    navAction: FavoritesScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<MainGraph.FavoritesScreen> {
        val viewModel = hiltViewModel<FavoritesScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is FavoritesScreenEffect.None -> {
                        // Нічого не робимо
                    }
                    else -> {
                        // Обробка інших ефектів у майбутньому
                    }
                }
            }
        }
        
        FavoritesScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 