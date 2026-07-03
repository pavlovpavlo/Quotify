package com.kovhan.data.auth.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.kovhan.core.datastore.delete
import com.kovhan.core.datastore.put
import com.kovhan.core.models.AuthUser
import com.kovhan.data.auth.source.UserCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserCache {

    override fun observe(): Flow<AuthUser?> =
        dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { prefs ->
                val uid = prefs[AuthPreferences.User.UID] ?: return@map null
                AuthUser(
                    uid = uid,
                    email = prefs[AuthPreferences.User.EMAIL],
                    displayName = prefs[AuthPreferences.User.DISPLAY_NAME],
                    isEmailVerified = prefs[AuthPreferences.User.EMAIL_VERIFIED] ?: false,
                    photoUrl = prefs[AuthPreferences.User.PHOTO_URL],
                    username = prefs[AuthPreferences.User.USERNAME],
                    isGoogleAccount = prefs[AuthPreferences.User.IS_GOOGLE] ?: false,
                )
            }

    override suspend fun cache(user: AuthUser) {
        dataStore.edit { prefs ->
            prefs[AuthPreferences.User.UID] = user.uid
            user.email?.let { prefs[AuthPreferences.User.EMAIL] = it } ?: prefs.remove(AuthPreferences.User.EMAIL)
            user.displayName?.let { prefs[AuthPreferences.User.DISPLAY_NAME] = it } ?: prefs.remove(AuthPreferences.User.DISPLAY_NAME)
            user.photoUrl?.let { prefs[AuthPreferences.User.PHOTO_URL] = it } ?: prefs.remove(AuthPreferences.User.PHOTO_URL)
            prefs[AuthPreferences.User.EMAIL_VERIFIED] = user.isEmailVerified
            prefs[AuthPreferences.User.IS_GOOGLE] = user.isGoogleAccount
            user.username?.let { prefs[AuthPreferences.User.USERNAME] = it }
        }
    }

    override suspend fun setDisplayName(displayName: String) =
        dataStore.put(AuthPreferences.User.DISPLAY_NAME, displayName)

    override suspend fun setUsername(username: String) =
        dataStore.put(AuthPreferences.User.USERNAME, username)

    override suspend fun setEmail(email: String) =
        dataStore.put(AuthPreferences.User.EMAIL, email)

    override suspend fun setPhotoUrl(url: String?) {
        if (url != null) dataStore.put(AuthPreferences.User.PHOTO_URL, url)
        else dataStore.delete(AuthPreferences.User.PHOTO_URL)
    }

    override suspend fun clear() {
        // Remove only user/session keys; keep device-level settings (theme, language, onboarding).
        dataStore.edit { prefs ->
            prefs.remove(AuthPreferences.User.UID)
            prefs.remove(AuthPreferences.User.EMAIL)
            prefs.remove(AuthPreferences.User.DISPLAY_NAME)
            prefs.remove(AuthPreferences.User.PHOTO_URL)
            prefs.remove(AuthPreferences.User.EMAIL_VERIFIED)
            prefs.remove(AuthPreferences.User.USERNAME)
            prefs.remove(AuthPreferences.User.IS_GOOGLE)
        }
    }
}
