package com.kovhan.quotify.navigation.dock

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/** Mirrors the web `prefers-reduced-motion` query: animations off when the system scale is 0. */
@Composable
internal fun rememberMotionEnabled(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        val scale = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        )
        scale != 0f
    }
}
