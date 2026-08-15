package com.kovhan.data.settings.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kovhan.core.datastore.getOnes
import com.kovhan.core.datastore.put
import com.kovhan.domain.review.ReviewPromptRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewPromptLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>,
) : ReviewPromptRepository {

    override suspend fun installedAt(): Long = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
    }.getOrDefault(0L)

    override suspend fun isAsked(): Boolean =
        dataStore.getOnes(SettingsPreferences.Review.ASKED, false)

    override suspend fun markAsked() = dataStore.put(SettingsPreferences.Review.ASKED, true)
}
