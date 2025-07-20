package com.kovhan.feature.main.presentation.home.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.feature.main.presentation.home.HomeScreen
import com.kovhan.feature.main.presentation.home.HomeScreenViewModel
import com.kovhan.feature.main.presentation.home.mvi.HomeScreenEffect


internal fun NavGraphBuilder.homeScreen(
    navAction: HomeScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<MainGraph.HomeScreen>{
        val viewModel = hiltViewModel<HomeScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is HomeScreenEffect.None -> {
                        // Нічого не робимо
                    }
                    else -> {
                        // Обробка інших ефектів у майбутньому
                    }
                }
            }
        }
        
        HomeScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 