package com.kovhan.data.settings.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object SettingsPreferences {
    object Settings {
        val THEME = stringPreferencesKey("app_theme")
        val LANGUAGE = stringPreferencesKey("app_language")
        val DAILY_QUOTE_ENABLED = booleanPreferencesKey("daily_quote_enabled")
    }

    object Onboarding {
        val COMPLETED = booleanPreferencesKey("onboarding_completed")
        val FAB_TOOLTIP_DISMISSED = booleanPreferencesKey("fab_tooltip_dismissed")
    }

    object Widget {
        val SOURCE = stringPreferencesKey("widget_source")
        val INCLUDE_DAILY_QUOTE = booleanPreferencesKey("widget_include_daily_quote")
        val FREQUENCY_HOURS = intPreferencesKey("widget_frequency_hours")
        val STYLE = stringPreferencesKey("widget_style")
    }
}
