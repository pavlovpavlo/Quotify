package com.kovhan.quotify.application

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import com.kovhan.core.ui.util.ContextUtils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class QuotifyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val debuggable = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        Timber.plant(if (debuggable) Timber.DebugTree() else CrashlyticsTree())
        AppCheckInstaller.install(this)
    }

    override fun attachBaseContext(base: Context) {
        val localeUpdatedContext = ContextUtils.updateConfiguration(base)
        super.attachBaseContext(localeUpdatedContext)
    }
}
