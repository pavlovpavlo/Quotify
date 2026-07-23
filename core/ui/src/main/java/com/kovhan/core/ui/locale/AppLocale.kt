package com.kovhan.core.ui.locale

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

/**
 * Context carrying the in-app language, published once by the activity.
 *
 * Dialogs and modal bottom sheets render in their own window, and every window
 * owner re-provides [LocalContext] with its raw window context — which drops the
 * app language override. This local survives that boundary, so overlays can
 * restore it via [ProvideAppLocale].
 */
val LocalAppLocaleContext = staticCompositionLocalOf<Context?> { null }

@Composable
fun ProvideAppLocale(content: @Composable () -> Unit) {
    val localizedContext = LocalAppLocaleContext.current

    if (localizedContext == null || localizedContext === LocalContext.current) {
        content()
        return
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedContext.resources.configuration,
        content = content,
    )
}
