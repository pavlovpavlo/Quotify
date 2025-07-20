package com.kovhan.feature.auth.presentation.forgot_password.navigation

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
import com.kovhan.feature.auth.presentation.forgot_password.ForgotPasswordScreen
import com.kovhan.feature.auth.presentation.forgot_password.ForgotPasswordScreenViewModel
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenEffect

fun NavGraphBuilder.forgotPasswordScreen(
    navAction: ForgotPasswordScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<AuthGraph.ForgotPasswordScreen>{
        val viewModel = hiltViewModel<ForgotPasswordScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is ForgotPasswordScreenEffect.None -> {
                        // Нічого не робимо
                    }
                    else -> {
                        // Обробка інших ефектів у майбутньому
                    }
                }
            }
        }
        
        ForgotPasswordScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 