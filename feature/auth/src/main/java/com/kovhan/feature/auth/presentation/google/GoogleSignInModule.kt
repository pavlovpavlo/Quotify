package com.kovhan.feature.auth.presentation.google

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class GoogleSignInModule {
    @Binds
    @Singleton
    abstract fun bindGoogleSignInClient(impl: CredentialGoogleSignInClient): GoogleSignInClient
}
