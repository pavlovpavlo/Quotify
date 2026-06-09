package com.kovhan.feature.auth.presentation.register.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.AuthGraph
import com.kovhan.core.ui.snackbar.SnackbarMessageEffect
import com.kovhan.design.systems.R
import com.kovhan.feature.auth.presentation.google.rememberGoogleSignInClient
import com.kovhan.feature.auth.presentation.register.RegisterScreen
import com.kovhan.feature.auth.presentation.register.RegisterScreenViewModel
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenEffect
import kotlinx.coroutines.launch

internal fun NavGraphBuilder.registerScreen(
    navAction: RegisterScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<AuthGraph.RegisterScreen> {
        val viewModel = hiltViewModel<RegisterScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val googleSignInClient = rememberGoogleSignInClient()
        val snackbarHostState = remember { SnackbarHostState() }

        SnackbarMessageEffect(viewModel.snackbar, snackbarHostState)

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
            snackbarHostState = snackbarHostState,
        )
    }
}
