package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import timber.log.Timber

/**
 * Brings the widget back in sync with its content. Used after a device reboot,
 * an app update or a day change, where the launcher keeps the widget on screen
 * but Glance has no session to render into — leaving the stale (or placeholder)
 * layout until something calls [updateAll].
 *
 * The quote is resolved here rather than left to `provideGlance`, so a live
 * session picks the new quote up through its flows even when the render prelude
 * does not run again.
 */
class WidgetRefreshWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val entryPoint =
            EntryPointAccessors.fromApplication(
                applicationContext,
                WidgetEntryPoint::class.java,
            )
        runCatching {
            val settings = entryPoint.observeWidgetSettings().invoke().first()
            WidgetRotationScheduler.ensureScheduled(applicationContext, settings.frequencyHours)
            WidgetDayChangeScheduler.sync(applicationContext, settings.includeDailyQuote)
        }.onFailure { Timber.e(it, "Widget: failed to schedule rotation") }

        runCatching {
            entryPoint.ensureWidgetQuote().invoke()
            QuotifyGlanceWidget().updateAll(applicationContext)
        }.onFailure { Timber.e(it, "Widget: refresh worker failed") }
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "quotify_widget_refresh"

        fun enqueue(context: Context) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<WidgetRefreshWorker>().build(),
            )
        }
    }
}
