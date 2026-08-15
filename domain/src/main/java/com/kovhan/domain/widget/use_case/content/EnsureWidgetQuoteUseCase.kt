package com.kovhan.domain.widget.use_case.content

import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.core.models.widget.WidgetSettings
import com.kovhan.domain.widget.WidgetContentRepository
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * The widget's render entry point: refreshes the snapshot, then returns the
 * quote to draw — rotating first when it is due.
 *
 * Rotation cannot rely on the WorkManager task alone: a periodic request first
 * fires only at the end of its interval and is deferred further in Doze, which
 * left the widget stuck on one quote. The launcher redraws far more often, so
 * elapsed time since the last rotation is the real clock and the worker is only
 * a nudge to redraw.
 */
class EnsureWidgetQuoteUseCase @Inject constructor(
    private val repository: WidgetContentRepository,
    private val observeSettings: ObserveWidgetSettingsUseCase,
    private val rebuildSnapshot: RebuildWidgetSnapshotUseCase,
    private val rotateQuote: RotateWidgetQuoteUseCase,
    private val getQuote: GetWidgetQuoteUseCase,
) {
    suspend operator fun invoke(forceRotate: Boolean = false): WidgetQuote? {
        rebuildSnapshot()

        val current = repository.getCurrentQuoteId()
        val snapshot = repository.getSnapshotIds()
        val settings = observeSettings().first()

        val due = forceRotate ||
            current == null ||
            current !in snapshot ||
            isIntervalElapsed(settings)

        if (due) return rotateQuote()

        return getQuote() ?: rotateQuote()
    }

    private suspend fun isIntervalElapsed(settings: WidgetSettings): Boolean {
        val lastRotatedAt = repository.getLastRotatedAt()
        if (lastRotatedAt <= 0L) return true
        val hours = settings.frequencyHours.coerceAtLeast(WidgetSettings.MIN_FREQUENCY_HOURS)
        return System.currentTimeMillis() - lastRotatedAt >= TimeUnit.HOURS.toMillis(hours.toLong())
    }
}
