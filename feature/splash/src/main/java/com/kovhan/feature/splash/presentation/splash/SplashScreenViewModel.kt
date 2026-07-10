package com.kovhan.feature.splash.presentation.splash

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.ai.SubscriptionRepository
import com.kovhan.domain.auth.use_case.IsLoggedInUseCase
import com.kovhan.domain.connectivity.use_case.CheckConnectivityUseCase
import com.kovhan.domain.daily.use_case.PrefetchDailyQuotesUseCase
import com.kovhan.domain.library.use_case.sync.RefreshLibraryUseCase
import com.kovhan.domain.library.use_case.sync.SyncPendingChangesUseCase
import com.kovhan.domain.onboarding.use_case.GetOnboardingCompletedUseCase
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
    private val subscriptionRepository: SubscriptionRepository,
    private val syncPendingChanges: SyncPendingChangesUseCase,
    private val refreshLibrary: RefreshLibraryUseCase,
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
                runCatching { prefetchDailyQuotes() }
                // Push queued offline changes first, then pull the fresh remote state.
                val drained = runCatching { syncPendingChanges() }.getOrDefault(false)
                if (drained) runCatching { refreshLibrary() }
                awaitMinDuration()
                navigateOnward()
            } else if (subscriptionRepository.isSubscribed()) {
                // Offline but subscribed — proceed with the locally cached library.
                awaitMinDuration()
                navigateOnward()
            } else {
                publishEffect(SplashScreenEffect.ShowOfflineBlock)
            }
        }
    }

    private suspend fun navigateOnward() {
        val effect = when {
            !getOnboardingCompleted.await() -> SplashScreenEffect.NavigateToOnboarding
            isLoggedIn() -> SplashScreenEffect.NavigateToMain
            else -> SplashScreenEffect.NavigateToAuth
        }
        publishEffect(effect)
    }

    /** Keep the intro on screen for at least [SPLASH_MIN_DURATION_MS] on the first pass. */
    private suspend fun awaitMinDuration() {
        val remaining = SPLASH_MIN_DURATION_MS - (System.currentTimeMillis() - createdAt)
        if (remaining > 0) delay(remaining)
    }

    private companion object {
        const val SPLASH_MIN_DURATION_MS = 3000L
    }
}
