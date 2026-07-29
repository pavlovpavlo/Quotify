package com.kovhan.feature.widget.glance

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors

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
            entryPoint.rebuildWidgetSnapshot().invoke()
            entryPoint.rotateWidgetQuote().invoke()
            QuotifyGlanceWidget().updateAll(applicationContext)
        }
        return Result.success()
    }
}
