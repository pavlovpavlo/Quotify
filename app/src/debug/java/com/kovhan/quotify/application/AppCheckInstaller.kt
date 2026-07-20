package com.kovhan.quotify.application

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

/**
 * Debug variant: App Check runs on the debug provider with a *fixed* debug
 * token. Because the secret is pinned here instead of being regenerated on
 * every install, it only has to be registered once in the Firebase console
 * (App Check → Manage debug tokens) and then works on any machine or after a
 * reinstall. The token grants an App Check bypass only while it stays on the
 * allow list, so it is safe to keep in the debug source set and can be revoked
 * at any time from the console.
 */
internal object AppCheckInstaller {

    private const val DEBUG_SECRET_PREFS = "com.google.firebase.appcheck.debug.store"
    private const val DEBUG_SECRET_KEY = "com.google.firebase.appcheck.debug.DEBUG_SECRET"
    private const val DEBUG_TOKEN = "3f9c2a7e-6b1d-4e28-9a5f-7c0d8e21b4a6"

    fun install(context: Context) {
        val prefsName = "$DEBUG_SECRET_PREFS.${FirebaseApp.getInstance().persistenceKey}"
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            .edit()
            .putString(DEBUG_SECRET_KEY, DEBUG_TOKEN)
            .apply()
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
    }
}
