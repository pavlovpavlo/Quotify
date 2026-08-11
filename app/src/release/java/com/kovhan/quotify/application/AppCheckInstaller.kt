package com.kovhan.quotify.application

import android.content.Context
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import timber.log.Timber

/**
 * Release variant: App Check is backed by Play Integrity. Requires the release
 * signing certificate's SHA-256 to be registered for the app in the Firebase
 * console, and works most reliably for builds distributed through Google Play.
 */
internal object AppCheckInstaller {

    fun install(context: Context) {
        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
        logTokenAvailability(appCheck)
    }

    /**
     * When App Check is enforced but Play Integrity cannot mint a token, every
     * Firebase call — sign-in included — fails with an opaque error. Recording
     * the token result once at startup turns that into a readable breadcrumb.
     */
    private fun logTokenAvailability(appCheck: FirebaseAppCheck) {
        appCheck.getAppCheckToken(false)
            .addOnSuccessListener { Timber.i("App Check: token acquired") }
            .addOnFailureListener { Timber.e(it, "App Check: failed to acquire token") }
    }
}
