package com.kovhan.data.billing.di

import com.kovhan.core.ui.activity.ActivityRequired
import com.kovhan.data.billing.repository.BillingRepositoryImpl
import com.kovhan.data.billing.sync.SubscriptionSyncManager
import com.kovhan.domain.billing.BillingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BillingDataModule {

    @Binds
    @Singleton
    abstract fun bindBillingRepository(impl: BillingRepositoryImpl): BillingRepository

    @Binds
    @IntoSet
    abstract fun bindSubscriptionSyncManager(impl: SubscriptionSyncManager): ActivityRequired
}
