package com.kovhan.feature.auth.di

import com.kovhan.feature.auth.presentation.google.CredentialGoogleSignInClient
import com.kovhan.feature.auth.presentation.google.GoogleSignInClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthFeatureModule {

    @Binds
    @Singleton
    abstract fun bindGoogleSignInClient(impl: CredentialGoogleSignInClient): GoogleSignInClient
}
