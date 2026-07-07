package com.kovhan.feature.splash.presentation.splash

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.use_case.IsLoggedInUseCase
import com.kovhan.domain.daily.use_case.PrefetchDailyQuotesUseCase
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
) : BaseViewModel<SplashScreenState, SplashScreenEffect>(SplashScreenState()), SplashScreenIntent {

    init {
        // Warm the local daily-quote cache from Firestore while the intro plays.
        viewModelScope.launch { runCatching { prefetchDailyQuotes() } }

        viewModelScope.launch {
            // Mirrors the lottie intro length so navigation feels paced, not abrupt.
            delay(SPLASH_MIN_DURATION_MS)
            val effect = when {
                !getOnboardingCompleted.await() -> SplashScreenEffect.NavigateToOnboarding
                isLoggedIn() -> SplashScreenEffect.NavigateToMain
                else -> SplashScreenEffect.NavigateToAuth
            }
            publishEffect(effect)
        }
    }

    private companion object {
        const val SPLASH_MIN_DURATION_MS = 3000L
    }
}
