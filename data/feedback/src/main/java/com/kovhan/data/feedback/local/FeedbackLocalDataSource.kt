package com.kovhan.data.feedback.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.kovhan.core.datastore.get
import com.kovhan.core.datastore.put
import com.kovhan.core.models.feedback.FeedbackSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    fun observeGiven(source: FeedbackSource): Flow<Boolean> = dataStore.get(key(source), false)

    suspend fun markGiven(source: FeedbackSource) = dataStore.put(key(source), true)

    suspend fun lastSubmittedAt(): Long = dataStore.get(LAST_SUBMITTED_AT, 0L).first()

    suspend fun markSubmittedAt(timestamp: Long) = dataStore.put(LAST_SUBMITTED_AT, timestamp)

    private fun key(source: FeedbackSource) =
        booleanPreferencesKey("feedback_given_${source.name.lowercase()}")

    private companion object {
        val LAST_SUBMITTED_AT = longPreferencesKey("feedback_last_submitted_at")
    }
}
