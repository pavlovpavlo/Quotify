package com.kovhan.quotify.application

import android.app.Application
import android.content.Context
import com.kovhan.core.ui.util.ContextUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QuotifyApplication : Application() {

    override fun attachBaseContext(base: Context) {
        val localeUpdatedContext = ContextUtils.updateConfiguration(base)
        super.attachBaseContext(localeUpdatedContext)
    }
}
