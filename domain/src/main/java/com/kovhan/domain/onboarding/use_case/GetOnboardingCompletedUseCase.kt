package com.kovhan.domain.onboarding.use_case

import com.kovhan.domain.onboarding.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetOnboardingCompletedUseCase
    @Inject
    constructor(
        private val repository: OnboardingRepository,
    ) {
        operator fun invoke(): Flow<Boolean> = repository.isCompleted()

        suspend fun await(): Boolean = repository.isCompleted().first()
    }
