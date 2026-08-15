package com.kovhan.data.settings.di

import com.kovhan.data.settings.local.ReviewPromptLocalDataSource
import com.kovhan.data.settings.local.SettingsLocalDataSource
import com.kovhan.data.settings.local.WidgetSettingsLocalDataSource
import com.kovhan.domain.onboarding.OnboardingRepository
import com.kovhan.domain.premium.OfferPromptRepository
import com.kovhan.domain.premium.PaywallPromptRepository
import com.kovhan.domain.review.ReviewPromptRepository
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
    abstract fun bindPaywallPromptRepository(
        impl: SettingsLocalDataSource,
    ): PaywallPromptRepository

    @Binds
    @Singleton
    abstract fun bindOfferPromptRepository(
        impl: SettingsLocalDataSource,
    ): OfferPromptRepository

    @Binds
    @Singleton
    abstract fun bindWidgetSettingsRepository(
        impl: WidgetSettingsLocalDataSource,
    ): WidgetSettingsRepository

    @Binds
    @Singleton
    abstract fun bindReviewPromptRepository(
        impl: ReviewPromptLocalDataSource,
    ): ReviewPromptRepository
}
