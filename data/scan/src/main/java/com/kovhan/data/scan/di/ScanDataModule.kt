package com.kovhan.data.scan.di

import com.kovhan.data.scan.GeminiTagSuggestionRepository
import com.kovhan.data.scan.GeminiTextRecognitionRepository
import com.kovhan.domain.ai.TagSuggestionRepository
import com.kovhan.domain.scan.TextRecognitionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScanDataModule {
    @Binds
    @Singleton
    abstract fun bindTextRecognitionRepository(
        impl: GeminiTextRecognitionRepository,
    ): TextRecognitionRepository

    @Binds
    @Singleton
    abstract fun bindTagSuggestionRepository(
        impl: GeminiTagSuggestionRepository,
    ): TagSuggestionRepository
}
