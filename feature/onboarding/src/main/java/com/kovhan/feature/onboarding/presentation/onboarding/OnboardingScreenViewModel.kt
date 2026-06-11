package com.kovhan.feature.onboarding.presentation.onboarding

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
) : BaseViewModel<OnboardingScreenState, OnboardingScreenEffect>(OnboardingScreenState()),
    OnboardingScreenIntent {

    override fun onPageChanged(page: Int) {
        publishState { copy(currentPage = page) }
    }

    override fun onNextClicked() {
        val state = uiState.value
        if (state.isLastPage) {
            complete()
        } else {
            val nextPage = state.currentPage + 1
            publishState { copy(currentPage = nextPage) }
            publishEffect(OnboardingScreenEffect.GoToPage(nextPage))
        }
    }

    override fun onSkipClicked() {
        complete()
    }

    private fun complete() {
        if (uiState.value.isCompleting) return
        publishState { copy(isCompleting = true) }
        viewModelScope.launch {
            setOnboardingCompleted()
            publishEffect(OnboardingScreenEffect.NavigateToComplete)
        }
    }
}
