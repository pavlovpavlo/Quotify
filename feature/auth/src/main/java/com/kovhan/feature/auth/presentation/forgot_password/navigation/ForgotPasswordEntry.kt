package com.kovhan.feature.auth.presentation.forgot_password.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.ForgotPasswordKey
import com.kovhan.core.navigation.LoginKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.auth.presentation.forgot_password.ForgotPasswordScreen
import com.kovhan.feature.auth.presentation.forgot_password.ForgotPasswordScreenViewModel
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenEffect

@Composable
internal fun ForgotPasswordEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<ForgotPasswordScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val navAction = object : ForgotPasswordScreenNavAction {
        override fun navigateBack() {
            coordinator.goBack()
        }

        override fun navigateToLogin() =
            coordinator.navigate(LoginKey(), popUpTo = ForgotPasswordKey, inclusive = true)
    }

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
    )
}
