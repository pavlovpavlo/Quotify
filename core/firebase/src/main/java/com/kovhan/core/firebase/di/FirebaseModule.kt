package com.kovhan.core.firebase.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.kovhan.core.firebase.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(fetchIntervalSeconds())
                    .build(),
            )
        }

    /**
     * Debug builds fetch every time: otherwise a value changed in the console
     * stays invisible for an hour and looks like a broken feature.
     */
    private fun fetchIntervalSeconds(): Long =
        if (BuildConfig.DEBUG) 0L else REMOTE_CONFIG_FETCH_INTERVAL_SECONDS

    private const val REMOTE_CONFIG_FETCH_INTERVAL_SECONDS = 3600L
}
