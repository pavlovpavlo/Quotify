package com.kovhan.feature.onboarding.presentation.onboarding.mvi

import com.kovhan.core.ui.UiState

data class OnboardingScreenState(
    val currentPage: Int = 0,
    val isCompleting: Boolean = false,
) : UiState {
    val isLastPage: Boolean get() = currentPage == LAST_PAGE_INDEX

    companion object {
        const val PAGE_COUNT = 3
        const val LAST_PAGE_INDEX = PAGE_COUNT - 1
    }
}
