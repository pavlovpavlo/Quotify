package com.kovhan.data.config.di

import com.kovhan.data.config.repository.AppConfigRepositoryImpl
import com.kovhan.domain.appconfig.AppConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigDataModule {
    @Binds
    @Singleton
    abstract fun bindAppConfigRepository(impl: AppConfigRepositoryImpl): AppConfigRepository
}
