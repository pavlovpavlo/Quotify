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

/**
 * Redraws the widget without advancing the rotation. Used after a device reboot
 * or an app update, where the launcher restores the widget but Glance has no
 * session to render into — leaving the placeholder loading layout on screen
 * until something calls [updateAll].
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
            val hours = entryPoint.observeWidgetSettings().invoke().first().frequencyHours
            WidgetRotationScheduler.ensureScheduled(applicationContext, hours)
        }
        runCatching {
            entryPoint.rebuildWidgetSnapshot().invoke()
            QuotifyGlanceWidget().updateAll(applicationContext)
        }
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
