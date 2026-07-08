package com.kovhan.data.voice.di

import com.kovhan.data.voice.AndroidVoiceInputRepository
import com.kovhan.domain.voice.VoiceInputRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VoiceDataModule {
    @Binds
    @Singleton
    abstract fun bindVoiceInputRepository(impl: AndroidVoiceInputRepository): VoiceInputRepository
}
