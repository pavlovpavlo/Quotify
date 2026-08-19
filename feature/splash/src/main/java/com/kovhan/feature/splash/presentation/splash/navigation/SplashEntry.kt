package com.kovhan.feature.splash.presentation.splash.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.CompleteKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfflineBlockingSheetKey
import com.kovhan.core.navigation.OnboardingKey
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.models.PaywallOrigin
import com.kovhan.core.navigation.QuotesKey
import com.kovhan.core.navigation.SplashKey
import com.kovhan.core.navigation.UpdateRequiredDialogKey
import com.kovhan.core.ui.extensions.getActivity
import com.kovhan.core.ui.extensions.openAppInStore
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

        override fun navigateToMainWithPaywall() {
            // Спершу бібліотека, потім пейвол поверх — щоб «назад» повертало в застосунок.
            coordinator.navigate(QuotesKey, popUpTo = SplashKey, inclusive = true)
            coordinator.navigate(PaywallKey(PaywallOrigin.BANNER))
        }

        override fun navigateToAuth() {
            coordinator.navigate(CompleteKey, popUpTo = SplashKey, inclusive = true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event ->
            when (event) {
                SplashScreenEffect.NavigateToOnboarding -> navAction.navigateToOnboarding()
                SplashScreenEffect.NavigateToAuth -> navAction.navigateToAuth()
                SplashScreenEffect.NavigateToMain -> navAction.navigateToMain()
                SplashScreenEffect.NavigateToMainWithPaywall -> navAction.navigateToMainWithPaywall()
                SplashScreenEffect.ShowOfflineBlock -> coordinator.showBottomSheet(OfflineBlockingSheetKey)
                SplashScreenEffect.ShowUpdateRequired -> coordinator.showDialog(UpdateRequiredDialogKey)
            }
        }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_OFFLINE_RETRY)
            .collect { confirmed -> if (confirmed == true) viewModel.onRetry() }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_OFFLINE_DISMISS)
            .collect { confirmed -> if (confirmed == true) context.getActivity()?.finish() }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_UPDATE_REQUIRED_UPDATE)
            .collect { confirmed -> if (confirmed == true) context.openAppInStore() }
    }

    LaunchedEffect(Unit) {
        coordinator.observeResult<Boolean>(NavigationCoordinator.KEY_UPDATE_REQUIRED_EXIT)
            .collect { confirmed -> if (confirmed == true) context.getActivity()?.finish() }
    }

    SplashScreen(
        state = state.value,
        intent = viewModel,
        paddingValues = paddingValues,
        navAction = navAction,
    )
}
