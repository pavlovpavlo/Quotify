package com.kovhan.data.survey.di

import com.kovhan.data.survey.repository.SurveyRepositoryImpl
import com.kovhan.domain.survey.SurveyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SurveyDataModule {
    @Binds
    @Singleton
    abstract fun bindSurveyRepository(impl: SurveyRepositoryImpl): SurveyRepository
}
