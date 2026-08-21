package com.kovhan.feature.widget.glance

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import com.kovhan.core.ui.widget.HomeWidgetPresence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object WidgetPinner {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** False when the launcher can't pin programmatically — user adds manually. */
    fun pin(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false
        val manager = AppWidgetManager.getInstance(context)
        if (!manager.isRequestPinAppWidgetSupported) return false
        val provider = ComponentName(context, QuotifyGlanceWidgetReceiver::class.java)
        return manager.requestPinAppWidget(provider, null, null)
    }

    /**
     * [pin] only opens the system dialog — the widget appears once the user
     * confirms it there, which may never happen. Waits for it to actually show
     * up before reporting success, so nothing is announced over that dialog.
     * Runs outside the screen's scope: the settings screen closes right away.
     */
    fun awaitPlacement(context: Context, onPlaced: () -> Unit) {
        val appContext = context.applicationContext
        scope.launch {
            repeat(POLL_ATTEMPTS) {
                delay(POLL_INTERVAL_MS)
                if (HomeWidgetPresence.isPlaced(appContext)) {
                    onPlaced()
                    return@launch
                }
            }
        }
    }

    private const val POLL_INTERVAL_MS = 400L
    private const val POLL_ATTEMPTS = 75
}
