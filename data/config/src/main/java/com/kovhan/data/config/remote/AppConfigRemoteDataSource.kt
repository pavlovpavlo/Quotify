package com.kovhan.data.config.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfigRemoteDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) {
    suspend fun minSupportedVersionCode(): Long {
        runCatching { remoteConfig.fetchAndActivate().await() }
        return remoteConfig.getLong(KEY_MIN_VERSION_CODE)
    }

    private companion object {
        const val KEY_MIN_VERSION_CODE = "android_min_version_code"
    }
}
