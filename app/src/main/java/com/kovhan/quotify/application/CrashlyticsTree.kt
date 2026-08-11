package com.kovhan.quotify.application

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * Release builds planted nothing, so every `Timber.e` — including the ones that
 * explain a failed Google sign-in or a rejected Gemini call — was dropped on the
 * floor and the only symptom left was a silent failure. Warnings and errors now
 * reach Crashlytics as breadcrumbs, with throwables recorded as non-fatals.
 */
internal class CrashlyticsTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= Log.WARN

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(if (tag == null) message else "$tag: $message")
        if (t != null) crashlytics.recordException(t)
    }
}
