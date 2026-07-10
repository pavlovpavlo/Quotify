package com.kovhan.feature.splash.presentation.splash.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.LoginKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfflineBlockingDialogKey
import com.kovhan.core.navigation.OnboardingKey
import com.kovhan.core.navigation.QuotesKey
import com.kovhan.core.navigation.SplashKey
import com.kovhan.feature.splash.navigation.SplashScreenNavAction
import com.kovhan.feature.splash.presentation.splash.SplashScreen
import com.kovhan.feature.splash.presentation.splash.SplashScreenViewModel
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenEffect

@Composable
internal fun SplashEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<SplashScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val navAction = object : SplashScreenNavAction {
        override fun onBack() {
            coordinator.goBack()
        }

        override fun navigateToOnboarding() {
            coordinator.navigate(OnboardingKey, popUpTo = SplashKey, inclusive = true)
        }

        override fun navigateToMain() {
            coordinator.navigate(QuotesKey, popUpTo = SplashKey, inclusive = true)
        }

        override fun navigateToAuth() {
            coordinator.navigate(LoginKey, popUpTo = SplashKey, inclusive = true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event ->
            when (event) {
                SplashScreenEffect.NavigateToOnboarding -> navAction.navigateToOnboarding()
                SplashScreenEffect.NavigateToAuth -> navAction.navigateToAuth()
                SplashScreenEffect.NavigateToMain -> navAction.navigateToMain()
                SplashScreenEffect.ShowOfflineBlock -> coordinator.showDialog(OfflineBlockingDialogKey)
            }
        }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_OFFLINE_RETRY)
            .collect { confirmed -> if (confirmed == true) viewModel.onRetry() }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_OFFLINE_DISMISS)
            .collect { confirmed -> if (confirmed == true) context.findActivity()?.finish() }
    }

    SplashScreen(
        state = state.value,
        intent = viewModel,
        paddingValues = paddingValues,
        navAction = navAction,
    )
}

private fun Context.findActivity(): Activity? {
    var current: Context? = this
    while (current is ContextWrapper) {
        if (current is Activity) return current
        current = current.baseContext
    }
    return null
}
