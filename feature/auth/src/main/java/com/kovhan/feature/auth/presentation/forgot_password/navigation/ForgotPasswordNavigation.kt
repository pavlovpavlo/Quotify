package com.kovhan.feature.auth.presentation.forgot_password.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.core.ui.snackbar.SnackbarMessageEffect
import com.kovhan.feature.auth.presentation.forgot_password.ForgotPasswordScreen
import com.kovhan.feature.auth.presentation.forgot_password.ForgotPasswordScreenViewModel
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenEffect

internal fun NavGraphBuilder.forgotPasswordScreen(
    navAction: ForgotPasswordScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<AuthGraph.ForgotPasswordScreen> {
        val viewModel = hiltViewModel<ForgotPasswordScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }

        SnackbarMessageEffect(viewModel.snackbar, snackbarHostState)

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    ForgotPasswordScreenEffect.NavigateToSignIn -> navAction.navigateToLogin()
                    ForgotPasswordScreenEffect.NavigateBack -> navAction.navigateBack()
                }
            }
        }

        ForgotPasswordScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
        )
    }
}
