package com.kovhan.core.ui.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Whether at least one Quotify home-screen widget is currently placed. Uses the
 * receiver's fully-qualified name (feature:widget) as a string so any module can
 * check presence without depending on that module.
 */
object HomeWidgetPresence {

    private const val RECEIVER_CLASS =
        "com.kovhan.feature.widget.glance.QuotifyGlanceWidgetReceiver"

    fun isPlaced(context: Context): Boolean = runCatching {
        AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context.packageName, RECEIVER_CLASS))
            .isNotEmpty()
    }.getOrDefault(false)
}

/**
 * Reactive [HomeWidgetPresence.isPlaced], re-checked on every `ON_RESUME` so that
 * removing the widget from the home screen flips CTAs back to "add" as soon as
 * the user returns to the app.
 */
@Composable
fun rememberHomeWidgetPlaced(): Boolean {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var placed by remember { mutableStateOf(HomeWidgetPresence.isPlaced(context)) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                placed = HomeWidgetPresence.isPlaced(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return placed
}
