package com.kovhan.feature.auth.presentation.login.navigation

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
import com.kovhan.feature.auth.presentation.login.LoginScreen
import com.kovhan.feature.auth.presentation.login.LoginScreenViewModel
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenEffect

fun NavGraphBuilder.loginScreen(
    navAction: LoginScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<AuthGraph.LoginScreen>{
        val viewModel = hiltViewModel<LoginScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is LoginScreenEffect.None -> {
                        // Нічого не робимо
                    }
                    else -> {
                        // Обробка інших ефектів у майбутньому
                    }
                }
            }
        }
        
        LoginScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 