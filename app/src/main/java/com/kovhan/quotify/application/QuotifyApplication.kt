package com.kovhan.quotify.application

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.kovhan.core.ui.util.ContextUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class QuotifyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val debuggable = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (debuggable) {
            Timber.plant(Timber.DebugTree())
        }
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            if (debuggable) {
                DebugAppCheckProviderFactory.getInstance()
            } else {
                PlayIntegrityAppCheckProviderFactory.getInstance()
            },
        )
    }

    override fun attachBaseContext(base: Context) {
        val localeUpdatedContext = ContextUtils.updateConfiguration(base)
        super.attachBaseContext(localeUpdatedContext)
    }
}
