package com.kovhan.domain.onboarding

import javax.inject.Inject

class SetOnboardingCompletedUseCase
    @Inject
    constructor(
        private val repository: OnboardingRepository,
    ) {
        suspend operator fun invoke(completed: Boolean = true) {
            repository.setCompleted(completed)
        }
    }
