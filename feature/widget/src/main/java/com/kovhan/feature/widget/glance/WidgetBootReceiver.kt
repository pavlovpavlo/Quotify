package com.kovhan.feature.widget.glance

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * After a reboot the launcher restores the widget from its initial layout, but
 * nothing re-renders it: `updatePeriodMillis` is 0 and Glance only runs
 * `provideGlance` on an explicit update. Without this the placeholder stays on
 * screen until the app is opened. The same applies after the app is replaced,
 * where the pending WorkManager rotation is dropped as well.
 */
class WidgetBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            -> WidgetRefreshWorker.enqueue(context.applicationContext)
        }
    }
}
