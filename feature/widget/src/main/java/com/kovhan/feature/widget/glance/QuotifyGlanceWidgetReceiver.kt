package com.kovhan.feature.widget.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.event.WidgetRemoved
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuotifyGlanceWidgetReceiver : GlanceAppWidgetReceiver() {

    @Inject
    lateinit var analytics: AnalyticsTracker

    override val glanceAppWidget: GlanceAppWidget = QuotifyGlanceWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val remaining = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(android.content.ComponentName(context, javaClass))
            .isNotEmpty()
        analytics.track(WidgetRemoved(hadWidgetSettings = remaining))
    }

    // Rotation is (re)ensured from provideGlance on every render; here we only
    // stop it once the last widget is removed.
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WidgetRotationScheduler.cancel(context)
    }
}
