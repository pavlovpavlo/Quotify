package com.kovhan.data.settings.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kovhan.core.datastore.get
import com.kovhan.core.datastore.put
import com.kovhan.domain.onboarding.OnboardingRepository
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository, OnboardingRepository {

    override fun observeTheme(): Flow<AppTheme> =
        dataStore.get(SettingsPreferences.Settings.THEME).map { stored ->
            stored?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() } ?: AppTheme.SYSTEM
        }

    override suspend fun setTheme(theme: AppTheme) =
        dataStore.put(SettingsPreferences.Settings.THEME, theme.name)

    override fun observeLanguage(): Flow<AppLanguage> =
        dataStore.get(SettingsPreferences.Settings.LANGUAGE).map { AppLanguage.fromTag(it) }

    override suspend fun setLanguage(language: AppLanguage) =
        dataStore.put(SettingsPreferences.Settings.LANGUAGE, language.tag)

    override fun isCompleted(): Flow<Boolean> =
        dataStore.get(SettingsPreferences.Onboarding.COMPLETED, false)

    override suspend fun setCompleted(completed: Boolean) =
        dataStore.put(SettingsPreferences.Onboarding.COMPLETED, completed)
}
