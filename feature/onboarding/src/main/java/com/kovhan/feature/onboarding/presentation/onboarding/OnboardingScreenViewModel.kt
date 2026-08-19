package com.kovhan.feature.onboarding.presentation.onboarding

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.event.OnboardingFinished
import com.kovhan.core.analytics.event.OnboardingSkip
import com.kovhan.core.analytics.event.OnboardingStarted
import com.kovhan.core.analytics.event.OnboardingStep
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.onboarding.use_case.SetOnboardingCompletedUseCase
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingScreenEffect
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingScreenIntent
import com.kovhan.feature.onboarding.presentation.onboarding.mvi.OnboardingScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingScreenViewModel @Inject constructor(
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<OnboardingScreenState, OnboardingScreenEffect>(OnboardingScreenState()),
    OnboardingScreenIntent {

    init {
        analytics.track(OnboardingStarted)
        analytics.track(OnboardingStep(stepNumber(0)))
    }

    override fun onPageChanged(page: Int) {
        if (page != uiState.value.currentPage) analytics.track(OnboardingStep(stepNumber(page)))
        publishState { copy(currentPage = page) }
    }

    override fun onNextClicked() {
        val state = uiState.value
        if (state.isLastPage) {
            complete()
        } else {
            val nextPage = state.currentPage + 1
            analytics.track(OnboardingStep(stepNumber(nextPage)))
            publishState { copy(currentPage = nextPage) }
            publishEffect(OnboardingScreenEffect.GoToPage(nextPage))
        }
    }

    override fun onSkipClicked() {
        analytics.track(OnboardingSkip(stepNumber(uiState.value.currentPage)))
        complete()
    }

    private fun complete() {
        if (uiState.value.isCompleting) return
        publishState { copy(isCompleting = true) }
        viewModelScope.launch {
            setOnboardingCompleted()
            analytics.track(OnboardingFinished)
            publishEffect(OnboardingScreenEffect.NavigateToComplete)
        }
    }

    private fun stepNumber(page: Int): Int = page + 1
}
