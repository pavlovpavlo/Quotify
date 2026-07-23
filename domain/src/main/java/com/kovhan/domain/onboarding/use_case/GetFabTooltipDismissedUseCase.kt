package com.kovhan.domain.onboarding.use_case

import com.kovhan.domain.onboarding.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetFabTooltipDismissedUseCase
    @Inject
    constructor(
        private val repository: OnboardingRepository,
    ) {
        operator fun invoke(): Flow<Boolean> = repository.isFabTooltipDismissed()

        suspend fun await(): Boolean = repository.isFabTooltipDismissed().first()
    }
