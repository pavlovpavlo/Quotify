package com.kovhan.data.settings.di

import com.kovhan.data.settings.local.SettingsLocalDataSource
import com.kovhan.data.settings.local.WidgetSettingsLocalDataSource
import com.kovhan.domain.onboarding.OnboardingRepository
import com.kovhan.domain.settings.SettingsRepository
import com.kovhan.domain.widget.WidgetSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsDataModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsLocalDataSource): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(impl: SettingsLocalDataSource): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindWidgetSettingsRepository(
        impl: WidgetSettingsLocalDataSource,
    ): WidgetSettingsRepository
}
