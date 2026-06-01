package com.kovhan.feature.auth.presentation.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.core.ui.snackbar.SnackbarMessageEffect
import com.kovhan.design.systems.R
import com.kovhan.feature.auth.presentation.login.LoginScreen
import com.kovhan.feature.auth.presentation.login.LoginScreenViewModel
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenEffect

internal fun NavGraphBuilder.loginScreen(
    navAction: LoginScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<AuthGraph.LoginScreen> {
        val viewModel = hiltViewModel<LoginScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val snackbarHostState = remember { SnackbarHostState() }

        SnackbarMessageEffect(viewModel.snackbar, snackbarHostState)

        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    LoginScreenEffect.NavigateToMain -> navAction.navigateToMain()
                    LoginScreenEffect.NavigateToSignUp -> navAction.navigateToRegister()
                    LoginScreenEffect.NavigateToForgotPassword -> navAction.navigateToForgotPassword()
                    LoginScreenEffect.NavigateBack -> navAction.navigateBack()
                    is LoginScreenEffect.OpenPrivacyPolicy -> navAction.navigateToWebView(
                        title = context.getString(R.string.auth_privacy_policy_title),
                        url = effect.url,
                    )
                    is LoginScreenEffect.OpenTermsOfService -> navAction.navigateToWebView(
                        title = context.getString(R.string.auth_terms_of_service_title),
                        url = effect.url,
                    )
                }
            }
        }

        LoginScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState,
        )
    }
}
