package com.kovhan.feature.onboarding.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.kovhan.domain.onboarding.OnboardingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.onboardingDataStore by preferencesDataStore(name = "onboarding_prefs")

@Singleton
class OnboardingPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) : OnboardingRepository {

    override fun isCompleted(): Flow<Boolean> =
        context.onboardingDataStore.data.map { it[KEY_COMPLETED] ?: false }

    override suspend fun setCompleted(completed: Boolean) {
        context.onboardingDataStore.edit { it[KEY_COMPLETED] = completed }
    }

    private companion object {
        val KEY_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }
}
