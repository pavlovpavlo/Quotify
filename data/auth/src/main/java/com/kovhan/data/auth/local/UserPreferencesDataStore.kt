package com.kovhan.data.auth.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kovhan.domain.auth.AuthUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userDataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun observe(): Flow<AuthUser?> =
        context.userDataStore.data.map { prefs ->
            val uid = prefs[KEY_UID] ?: return@map null
            AuthUser(
                uid = uid,
                email = prefs[KEY_EMAIL],
                displayName = prefs[KEY_DISPLAY_NAME],
                isEmailVerified = prefs[KEY_EMAIL_VERIFIED] ?: false,
                photoUrl = prefs[KEY_PHOTO_URL],
                username = prefs[KEY_USERNAME],
                isGoogleAccount = prefs[KEY_IS_GOOGLE] ?: false,
            )
        }

    suspend fun cache(user: AuthUser) {
        context.userDataStore.edit { prefs ->
            prefs[KEY_UID] = user.uid
            user.email?.let { prefs[KEY_EMAIL] = it } ?: prefs.remove(KEY_EMAIL)
            user.displayName?.let { prefs[KEY_DISPLAY_NAME] = it } ?: prefs.remove(KEY_DISPLAY_NAME)
            user.photoUrl?.let { prefs[KEY_PHOTO_URL] = it } ?: prefs.remove(KEY_PHOTO_URL)
            prefs[KEY_EMAIL_VERIFIED] = user.isEmailVerified
            prefs[KEY_IS_GOOGLE] = user.isGoogleAccount
            user.username?.let { prefs[KEY_USERNAME] = it }
        }
    }

    suspend fun setDisplayName(displayName: String) {
        context.userDataStore.edit { it[KEY_DISPLAY_NAME] = displayName }
    }

    suspend fun setUsername(username: String) {
        context.userDataStore.edit { it[KEY_USERNAME] = username }
    }

    suspend fun setEmail(email: String) {
        context.userDataStore.edit { it[KEY_EMAIL] = email }
    }

    suspend fun setPhotoUrl(url: String?) {
        context.userDataStore.edit {
            if (url != null) it[KEY_PHOTO_URL] = url else it.remove(KEY_PHOTO_URL)
        }
    }

    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }

    private companion object {
        val KEY_UID = stringPreferencesKey("user_uid")
        val KEY_EMAIL = stringPreferencesKey("user_email")
        val KEY_DISPLAY_NAME = stringPreferencesKey("user_display_name")
        val KEY_PHOTO_URL = stringPreferencesKey("user_photo_url")
        val KEY_EMAIL_VERIFIED = booleanPreferencesKey("user_email_verified")
        val KEY_USERNAME = stringPreferencesKey("user_username")
        val KEY_IS_GOOGLE = booleanPreferencesKey("user_is_google")
    }
}
