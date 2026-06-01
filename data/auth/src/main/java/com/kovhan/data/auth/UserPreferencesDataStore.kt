package com.kovhan.data.auth

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kovhan.domain.auth.AuthUser
import com.kovhan.domain.auth.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userDataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) : UserRepository {

    override fun observeUser(): Flow<AuthUser?> =
        context.userDataStore.data.map { prefs ->
            val uid = prefs[KEY_UID] ?: return@map null
            AuthUser(
                uid = uid,
                email = prefs[KEY_EMAIL],
                displayName = prefs[KEY_DISPLAY_NAME],
                isEmailVerified = prefs[KEY_EMAIL_VERIFIED] ?: false,
            )
        }

    override suspend fun cacheUser(user: AuthUser) {
        context.userDataStore.edit { prefs ->
            prefs[KEY_UID] = user.uid
            user.email?.let { prefs[KEY_EMAIL] = it } ?: prefs.remove(KEY_EMAIL)
            user.displayName?.let { prefs[KEY_DISPLAY_NAME] = it } ?: prefs.remove(KEY_DISPLAY_NAME)
            prefs[KEY_EMAIL_VERIFIED] = user.isEmailVerified
        }
    }

    override suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }

    private companion object {
        val KEY_UID = stringPreferencesKey("user_uid")
        val KEY_EMAIL = stringPreferencesKey("user_email")
        val KEY_DISPLAY_NAME = stringPreferencesKey("user_display_name")
        val KEY_EMAIL_VERIFIED = booleanPreferencesKey("user_email_verified")
    }
}
