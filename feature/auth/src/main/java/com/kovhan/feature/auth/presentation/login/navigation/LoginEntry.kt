package com.kovhan.feature.auth.presentation.login.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.CompleteKey
import com.kovhan.core.navigation.ForgotPasswordKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuotesKey
import com.kovhan.core.navigation.RegisterKey
import com.kovhan.core.navigation.WebViewKey
import com.kovhan.core.ui.snackbar.SnackbarMessageEffect
import com.kovhan.design.systems.R
import com.kovhan.feature.auth.presentation.google.rememberGoogleSignInClient
import com.kovhan.feature.auth.presentation.login.LoginScreen
import com.kovhan.feature.auth.presentation.login.LoginScreenViewModel
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenEffect
import kotlinx.coroutines.launch

@Composable
internal fun LoginEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
    confirmDelete: Boolean = false,
) {
    val viewModel = hiltViewModel<LoginScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleSignInClient = rememberGoogleSignInClient()
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(viewModel.snackbar, snackbarHostState)

    LaunchedEffect(confirmDelete) {
        if (confirmDelete) viewModel.enableConfirmDelete()
    }

    val navAction = object : LoginScreenNavAction {
        override fun navigateBack() {
            coordinator.goBack()
        }

        override fun navigateToMain() = coordinator.navigateAndClearBackStack(QuotesKey)
        override fun navigateToRegister() = coordinator.navigate(RegisterKey)
        override fun navigateToForgotPassword() = coordinator.navigate(ForgotPasswordKey)
        override fun navigateToWebView(title: String, url: String) =
            coordinator.navigate(WebViewKey(title = title, url = url))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                LoginScreenEffect.LaunchGoogleSignIn -> scope.launch {
                    viewModel.onGoogleSignInResult(googleSignInClient.signIn(context))
                }
                LoginScreenEffect.NavigateToMain -> navAction.navigateToMain()
                LoginScreenEffect.DeleteCompleted -> coordinator.navigateAndClearBackStack(CompleteKey)
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
