package com.kovhan.core.ui.activity

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
class ActivityRequiredModule {

    @Provides
    @IntoSet
    fun provideRateAppUseCase(useCase: RateAppUseCase): ActivityRequired = useCase

    @Provides
    @IntoSet
    fun provideContactSupportUseCase(useCase: ContactSupportUseCase): ActivityRequired = useCase
}
