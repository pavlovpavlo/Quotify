package com.kovhan.feature.widget.glance

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build

object WidgetPinner {

    /** False when the launcher can't pin programmatically — user adds manually. */
    fun pin(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false
        val manager = AppWidgetManager.getInstance(context)
        if (!manager.isRequestPinAppWidgetSupported) return false
        val provider = ComponentName(context, QuotifyGlanceWidgetReceiver::class.java)
        return manager.requestPinAppWidget(provider, null, null)
    }
}
