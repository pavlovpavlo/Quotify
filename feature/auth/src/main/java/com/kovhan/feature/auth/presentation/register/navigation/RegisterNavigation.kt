package com.kovhan.feature.auth.presentation.register.navigation

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
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.feature.auth.presentation.register.RegisterScreen
import com.kovhan.feature.auth.presentation.register.RegisterScreenViewModel
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenEffect

fun NavGraphBuilder.registerScreen(
    navAction: RegisterScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<AuthGraph.RegisterScreen>{
        val viewModel = hiltViewModel<RegisterScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is RegisterScreenEffect.None -> {
                        // Нічого не робимо
                    }
                    else -> {
                        // Обробка інших ефектів у майбутньому
                    }
                }
            }
        }
        
        RegisterScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 