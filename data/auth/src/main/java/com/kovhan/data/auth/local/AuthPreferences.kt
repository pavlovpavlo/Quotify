package com.kovhan.data.auth.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object AuthPreferences {
    object User {
        val UID = stringPreferencesKey("user_uid")
        val EMAIL = stringPreferencesKey("user_email")
        val DISPLAY_NAME = stringPreferencesKey("user_display_name")
        val PHOTO_URL = stringPreferencesKey("user_photo_url")
        val EMAIL_VERIFIED = booleanPreferencesKey("user_email_verified")
        val USERNAME = stringPreferencesKey("user_username")
        val IS_GOOGLE = booleanPreferencesKey("user_is_google")
        val IS_ANONYMOUS = booleanPreferencesKey("user_is_anonymous")
    }
}
