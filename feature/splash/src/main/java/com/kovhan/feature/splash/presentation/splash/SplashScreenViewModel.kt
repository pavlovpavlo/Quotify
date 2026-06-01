package com.kovhan.feature.splash.presentation.splash

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.onboarding.GetOnboardingCompletedUseCase
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
) : BaseViewModel<SplashScreenState, SplashScreenEffect>(SplashScreenState()), SplashScreenIntent {

    init {
        viewModelScope.launch {
            // Mirrors the lottie intro length so navigation feels paced, not abrupt.
            delay(SPLASH_MIN_DURATION_MS)
            val effect = if (getOnboardingCompleted.await()) {
                SplashScreenEffect.NavigateToAuth
            } else {
                SplashScreenEffect.NavigateToOnboarding
            }
            publishEffect(effect)
        }
    }

    private companion object {
        const val SPLASH_MIN_DURATION_MS = 3000L
    }
}
