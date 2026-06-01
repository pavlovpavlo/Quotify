package com.kovhan.feature.onboarding.data

import com.kovhan.domain.onboarding.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingDataModule {

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        impl: OnboardingPreferences,
    ): OnboardingRepository
}
