package com.kovhan.domain.onboarding.use_case

import com.kovhan.domain.onboarding.OnboardingRepository
import javax.inject.Inject

class SetFabTooltipDismissedUseCase
    @Inject
    constructor(
        private val repository: OnboardingRepository,
    ) {
        suspend operator fun invoke(dismissed: Boolean = true) {
            repository.setFabTooltipDismissed(dismissed)
        }
    }
