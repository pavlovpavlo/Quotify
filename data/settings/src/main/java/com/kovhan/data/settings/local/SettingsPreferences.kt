package com.kovhan.data.settings.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object SettingsPreferences {
    object Settings {
        val THEME = stringPreferencesKey("app_theme")
        val LANGUAGE = stringPreferencesKey("app_language")
    }

    object Onboarding {
        val COMPLETED = booleanPreferencesKey("onboarding_completed")
    }
}
