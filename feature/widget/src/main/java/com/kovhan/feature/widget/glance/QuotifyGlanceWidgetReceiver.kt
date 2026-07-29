package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class QuotifyGlanceWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = QuotifyGlanceWidget()

    // Rotation is (re)ensured from provideGlance on every render; here we only
    // stop it once the last widget is removed.
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetRotationScheduler.cancel(context)
    }
}
