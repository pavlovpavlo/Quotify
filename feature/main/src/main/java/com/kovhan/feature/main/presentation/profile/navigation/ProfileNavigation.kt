package com.kovhan.feature.main.presentation.profile.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.feature.main.presentation.profile.ProfileScreen
import com.kovhan.feature.main.presentation.profile.ProfileScreenViewModel
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenEffect

// Константи для швидших анімацій
private const val ANIMATION_DURATION = 150 // Мілісекунди

internal fun NavGraphBuilder.profileScreen(
    navAction: ProfileScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<MainGraph.ProfileScreen>{
        val viewModel = hiltViewModel<ProfileScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is ProfileScreenEffect.None -> {
                        // Нічого не робимо
                    }
                    else -> {
                        // Обробка інших ефектів у майбутньому
                    }
                }
            }
        }
        
        ProfileScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 