package com.kovhan.data.feedback.di

import com.kovhan.data.feedback.repository.FeedbackRepositoryImpl
import com.kovhan.domain.feedback.FeedbackRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedbackDataModule {
    @Binds
    @Singleton
    abstract fun bindFeedbackRepository(impl: FeedbackRepositoryImpl): FeedbackRepository
}
