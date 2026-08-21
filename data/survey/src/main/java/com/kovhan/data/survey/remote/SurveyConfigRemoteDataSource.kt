package com.kovhan.data.survey.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kovhan.data.survey.util.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurveyConfigRemoteDataSource @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) {
    /** Raw JSON payload; empty when the parameter is missing or the fetch failed. */
    suspend fun rawConfig(): String {
        val activated = runCatching { remoteConfig.fetchAndActivate().await() }
            .onFailure { Timber.w(it, "Survey config fetch failed") }
            .getOrDefault(false)

        val raw = remoteConfig.getString(KEY_SURVEYS)
        Timber.d("Survey config: activated=%s, %d chars", activated, raw.length)
        return raw
    }

    private companion object {
        const val KEY_SURVEYS = "android_surveys"
    }
}
