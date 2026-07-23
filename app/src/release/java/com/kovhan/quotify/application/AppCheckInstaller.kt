package com.kovhan.quotify.application

import android.content.Context
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

/**
 * Release variant: App Check is backed by Play Integrity. Requires the release
 * signing certificate's SHA-256 to be registered for the app in the Firebase
 * console, and works most reliably for builds distributed through Google Play.
 */
internal object AppCheckInstaller {

    fun install(context: Context) {
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
    }
}
