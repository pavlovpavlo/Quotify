package com.kovhan.data.auth.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kovhan.domain.onboarding.OnboardingRepository
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository, OnboardingRepository {

    override fun observeTheme(): Flow<AppTheme> =
        context.settingsDataStore.data.map { prefs ->
            prefs[KEY_THEME]
                ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                ?: AppTheme.SYSTEM
        }

    override suspend fun setTheme(theme: AppTheme) {
        context.settingsDataStore.edit { prefs -> prefs[KEY_THEME] = theme.name }
    }

    override fun observeLanguage(): Flow<AppLanguage> =
        context.settingsDataStore.data.map { prefs ->
            AppLanguage.fromTag(prefs[KEY_LANGUAGE])
        }

    override suspend fun setLanguage(language: AppLanguage) {
        context.settingsDataStore.edit { prefs -> prefs[KEY_LANGUAGE] = language.tag }
    }

    override fun isCompleted(): Flow<Boolean> =
        context.settingsDataStore.data.map { it[KEY_ONBOARDING_COMPLETED] ?: false }

    override suspend fun setCompleted(completed: Boolean) {
        context.settingsDataStore.edit { it[KEY_ONBOARDING_COMPLETED] = completed }
    }

    private companion object {
        val KEY_THEME = stringPreferencesKey("app_theme")
        val KEY_LANGUAGE = stringPreferencesKey("app_language")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }
}
