package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.kovhan.core.ui.widget.HomeWidgetPresence
import com.kovhan.domain.widget.use_case.content.EnsureWidgetQuoteUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pushes in-app changes to the placed widget straight away — without this the
 * home screen keeps the previous source and quote until the next rotation tick.
 */
@Singleton
class WidgetRefresher @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ensureWidgetQuote: EnsureWidgetQuoteUseCase,
) {

    /** [rotate] picks a new quote — pass it when the change makes the current one unrepresentative. */
    suspend fun refresh(rotate: Boolean = false) {
        if (!HomeWidgetPresence.isPlaced(context)) return

        // Redrawing is kept separate from picking a quote: an appearance change
        // must reach the home screen even if resolving the quote fails.
        runCatching { ensureWidgetQuote(forceRotate = rotate) }
            .onFailure { Timber.e(it, "Widget: failed to prepare quote for refresh") }

        runCatching { QuotifyGlanceWidget().updateAll(context) }
            .onFailure { Timber.e(it, "Widget: failed to redraw") }
    }
}
