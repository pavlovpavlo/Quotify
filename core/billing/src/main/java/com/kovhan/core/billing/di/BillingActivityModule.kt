package com.kovhan.core.billing.di

import com.kovhan.core.billing.BillingService
import com.kovhan.core.ui.activity.ActivityRequired
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class BillingActivityModule {

    @Binds
    @IntoSet
    abstract fun bindBillingService(service: BillingService): ActivityRequired
}