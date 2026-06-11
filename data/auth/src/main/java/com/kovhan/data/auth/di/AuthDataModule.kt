package com.kovhan.data.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kovhan.data.auth.local.SettingsDataStore
import com.kovhan.data.auth.remote.CredentialGoogleSignInClient
import com.kovhan.data.auth.repository.FirebaseAuthRepository
import com.kovhan.data.auth.repository.UserRepositoryImpl
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.GoogleSignInClient
import com.kovhan.domain.auth.UserRepository
import com.kovhan.domain.onboarding.OnboardingRepository
import com.kovhan.domain.settings.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AuthProvidesModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: FirebaseAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsDataStore): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(impl: SettingsDataStore): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindGoogleSignInClient(impl: CredentialGoogleSignInClient): GoogleSignInClient
}
