package com.kovhan.data.auth.di

import com.kovhan.data.auth.local.UserLocalDataSource
import com.kovhan.data.auth.remote.CloudinaryPhotoUploader
import com.kovhan.data.auth.remote.FirestoreUserProfileDataSource
import com.kovhan.data.auth.repository.FirebaseAuthRepository
import com.kovhan.data.auth.repository.UserRepositoryImpl
import com.kovhan.data.auth.source.PhotoUploader
import com.kovhan.data.auth.source.RemoteUserProfileSource
import com.kovhan.data.auth.source.UserCache
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    abstract fun bindUserCache(impl: UserLocalDataSource): UserCache

    @Binds
    @Singleton
    abstract fun bindRemoteUserProfileSource(impl: FirestoreUserProfileDataSource): RemoteUserProfileSource

    @Binds
    @Singleton
    abstract fun bindPhotoUploader(impl: CloudinaryPhotoUploader): PhotoUploader
}
