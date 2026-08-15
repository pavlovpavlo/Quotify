package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors
import timber.log.Timber

class WidgetRotationWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            WidgetEntryPoint::class.java,
        )
        runCatching {
            entryPoint.ensureWidgetQuote().invoke(forceRotate = true)
            QuotifyGlanceWidget().updateAll(applicationContext)
        }.onFailure { Timber.e(it, "Widget: rotation worker failed") }
        return Result.success()
    }
}
