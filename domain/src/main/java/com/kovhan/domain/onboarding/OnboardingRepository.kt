package com.kovhan.domain.onboarding

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun isCompleted(): Flow<Boolean>

    suspend fun setCompleted(completed: Boolean)

    fun isFabTooltipDismissed(): Flow<Boolean>

    suspend fun setFabTooltipDismissed(dismissed: Boolean)
}
