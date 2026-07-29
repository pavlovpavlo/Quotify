package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.design.systems.R as DsR
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first

class QuotifyGlanceWidget : GlanceAppWidget() {

    // Exact size so the quote can grow its line count as the widget is resized.
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java,
        )

        // Ensure timer rotation is scheduled — covers widgets placed by an older
        // build (before the scheduler existed), where onEnabled never fired.
        runCatching {
            val hours = entryPoint.observeWidgetSettings().invoke().first().frequencyHours
            WidgetRotationScheduler.ensureScheduled(context, hours)
        }

        runCatching { entryPoint.rebuildWidgetSnapshot().invoke() }
        var quote: WidgetQuote? = runCatching { entryPoint.getWidgetQuote().invoke() }.getOrNull()
        if (quote == null) {
            quote = runCatching { entryPoint.rotateWidgetQuote().invoke() }.getOrNull()
        }

        val strings = WidgetStrings(
            emptyTitle = context.getString(DsR.string.widget_empty_title),
            emptyHint = context.getString(DsR.string.widget_empty_hint),
        )

        provideContent {
            WidgetContent(quote = quote, strings = strings)
        }
    }
}
