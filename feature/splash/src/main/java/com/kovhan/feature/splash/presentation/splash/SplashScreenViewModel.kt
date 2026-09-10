package com.kovhan.feature.splash.presentation.splash

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.event.OfflineShown
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.appconfig.use_case.CheckUpdateRequiredUseCase
import com.kovhan.domain.auth.use_case.IsLoggedInUseCase
import com.kovhan.domain.connectivity.use_case.CheckConnectivityUseCase
import com.kovhan.domain.daily.use_case.PrefetchDailyQuotesUseCase
import com.kovhan.domain.library.use_case.sync.SyncLibraryUseCase
import com.kovhan.domain.onboarding.use_case.GetOnboardingCompletedUseCase
import com.kovhan.domain.premium.use_case.MarkStartupPaywallShownUseCase
import com.kovhan.domain.premium.use_case.ShouldShowStartupPaywallUseCase
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenEffect
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenIntent
import com.kovhan.feature.splash.presentation.splash.mvi.SplashScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getOnboardingCompleted: GetOnboardingCompletedUseCase,
    private val isLoggedIn: IsLoggedInUseCase,
    private val prefetchDailyQuotes: PrefetchDailyQuotesUseCase,
    private val checkConnectivity: CheckConnectivityUseCase,
    private val checkUpdateRequired: CheckUpdateRequiredUseCase,
    private val subscriptionRepository: SubscriptionRepository,
    private val syncLibrary: SyncLibraryUseCase,
    private val shouldShowStartupPaywall: ShouldShowStartupPaywallUseCase,
    private val markStartupPaywallShown: MarkStartupPaywallShownUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<SplashScreenState, SplashScreenEffect>(SplashScreenState()), SplashScreenIntent {

    private val createdAt = System.currentTimeMillis()

    init {
        runStartupGate()
    }

    /** Re-run the gate when the user taps "Retry" in the offline blocking dialog. */
    fun onRetry() {
        runStartupGate()
    }

    private fun runStartupGate() {
        viewModelScope.launch {
            if (checkConnectivity()) {
                if (runCatching { checkUpdateRequired() }.getOrDefault(false)) {
                    publishEffect(SplashScreenEffect.ShowUpdateRequired)
                    return@launch
                }
                runCatching { prefetchDailyQuotes() }
                runCatching { syncLibrary(force = true) }
                awaitMinDuration()
                navigateOnward()
            } else if (subscriptionRepository.isSubscribed()) {
                // Offline but subscribed — proceed with the locally cached library.
                awaitMinDuration()
                navigateOnward()
            } else {
                analytics.track(OfflineShown)
                publishEffect(SplashScreenEffect.ShowOfflineBlock)
            }
        }
    }

    private suspend fun navigateOnward() {
        val effect = when {
            !getOnboardingCompleted.await() -> SplashScreenEffect.NavigateToOnboarding
            isLoggedIn() -> mainEffect()
            else -> SplashScreenEffect.NavigateToAuth
        }
        publishEffect(effect)
    }

    private suspend fun mainEffect(): SplashScreenEffect {
        val show = runCatching { shouldShowStartupPaywall() }.getOrDefault(false)
        if (!show) return SplashScreenEffect.NavigateToMain
        runCatching { markStartupPaywallShown() }
        return SplashScreenEffect.NavigateToMainWithPaywall
    }

    /** Keep the intro on screen until the Lottie has played through once. */
    private suspend fun awaitMinDuration() {
        val remaining = SplashAnimation.MIN_VISIBLE_MS - (System.currentTimeMillis() - createdAt)
        if (remaining > 0) delay(remaining)
    }
}
