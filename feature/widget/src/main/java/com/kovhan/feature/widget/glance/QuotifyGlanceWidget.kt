package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.design.systems.R as DsR
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import timber.log.Timber

class QuotifyGlanceWidget : GlanceAppWidget() {

    // Exact size so the quote can grow its line count as the widget is resized.
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java,
        )

        val initialSettings: WidgetSettings = runCatching {
            entryPoint.observeWidgetSettings().invoke().first()
        }.onFailure { Timber.e(it, "Widget: failed to read settings") }
            .getOrNull() ?: WidgetSettings()

        // Ensure timer rotation is scheduled — covers widgets placed by an older
        // build (before the scheduler existed), where onEnabled never fired.
        runCatching { WidgetRotationScheduler.ensureScheduled(context, initialSettings.frequencyHours) }
            .onFailure { Timber.e(it, "Widget: failed to schedule rotation") }

        val initialQuote: WidgetQuote? = runCatching { entryPoint.ensureWidgetQuote().invoke() }
            .onFailure { Timber.e(it, "Widget: failed to resolve quote") }
            .getOrNull()

        val strings = WidgetStrings(
            emptyTitle = context.getString(DsR.string.widget_empty_title),
            emptyHint = context.getString(DsR.string.widget_empty_hint),
        )

        // Both sources are observed rather than snapshotted, so an appearance
        // change or an edit to the shown quote redraws the widget on its own —
        // an explicit updateAll is only needed to revive a dead session.
        provideContent {
            val settings by entryPoint.observeWidgetSettings().invoke()
                .collectAsState(initial = initialSettings)
            val quote by entryPoint.observeWidgetQuote().invoke()
                .collectAsState(initial = initialQuote)

            WidgetContent(
                quote = quote,
                settings = settings.activeStyleSettings,
                strings = strings,
            )
        }
    }
}
