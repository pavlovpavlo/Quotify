package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kovhan.core.models.widget.WidgetSettings
import java.util.concurrent.TimeUnit

object WidgetRotationScheduler {

    private const val WORK_NAME = "quotify_widget_rotation"

    /** Create the periodic rotation if absent; leaves an existing one untouched (no timer reset). */
    fun ensureScheduled(context: Context, frequencyHours: Int) =
        enqueue(context, frequencyHours, ExistingPeriodicWorkPolicy.KEEP)

    /** Replace the interval of the existing rotation (used when the frequency changes). */
    fun reschedule(context: Context, frequencyHours: Int) =
        enqueue(context, frequencyHours, ExistingPeriodicWorkPolicy.UPDATE)

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    private fun enqueue(
        context: Context,
        frequencyHours: Int,
        policy: ExistingPeriodicWorkPolicy,
    ) {
        val hours = frequencyHours.coerceAtLeast(WidgetSettings.MIN_FREQUENCY_HOURS).toLong()
        val request = PeriodicWorkRequestBuilder<WidgetRotationWorker>(hours, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORK_NAME, policy, request)
    }
}
