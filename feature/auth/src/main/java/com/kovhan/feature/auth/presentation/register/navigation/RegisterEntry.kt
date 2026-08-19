package com.kovhan.feature.auth.presentation.register.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.LoginKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.QuotesKey
import com.kovhan.core.navigation.RegisterKey
import com.kovhan.core.navigation.models.AuthEntryPoint
import com.kovhan.core.navigation.WebViewKey
import com.kovhan.design.systems.R
import com.kovhan.feature.auth.presentation.analytics.toAnalytics
import com.kovhan.feature.auth.presentation.google.rememberGoogleSignInClient
import com.kovhan.feature.auth.presentation.register.RegisterScreen
import com.kovhan.feature.auth.presentation.register.RegisterScreenViewModel
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenEffect
import kotlinx.coroutines.launch

@Composable
internal fun RegisterEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
    entryPoint: AuthEntryPoint = AuthEntryPoint.FIRST_LAUNCH,
) {
    val viewModel = hiltViewModel<RegisterScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(entryPoint) {
        viewModel.onScreenOpened(entryPoint.toAnalytics())
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleSignInClient = rememberGoogleSignInClient()

    val navAction = object : RegisterScreenNavAction {
        override fun navigateBack() {
            coordinator.goBack()
        }

        override fun navigateToMain() = coordinator.navigateAndClearBackStack(QuotesKey)
        override fun navigateToLogin() =
            coordinator.navigate(LoginKey(), popUpToClass = RegisterKey::class, inclusive = true)
        override fun navigateToWebView(title: String, url: String) =
            coordinator.navigate(WebViewKey(title = title, url = url))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                RegisterScreenEffect.LaunchGoogleSignIn -> scope.launch {
                    viewModel.onGoogleSignInResult(googleSignInClient.signIn(context))
                }
                RegisterScreenEffect.NavigateToMain -> navAction.navigateToMain()
                RegisterScreenEffect.NavigateToSignIn -> navAction.navigateToLogin()
                RegisterScreenEffect.NavigateBack -> navAction.navigateBack()
                is RegisterScreenEffect.OpenPrivacyPolicy -> navAction.navigateToWebView(
                    title = context.getString(R.string.auth_privacy_policy_title),
                    url = effect.url,
                )
                is RegisterScreenEffect.OpenTermsOfService -> navAction.navigateToWebView(
                    title = context.getString(R.string.auth_terms_of_service_title),
                    url = effect.url,
                )
            }
        }
    }

    RegisterScreen(
        state = state.value,
        intent = viewModel,
        navAction = navAction,
        paddingValues = paddingValues,
    )
}
