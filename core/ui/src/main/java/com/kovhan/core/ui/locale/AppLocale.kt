package com.kovhan.core.ui.locale

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import java.util.Locale

/**
 * Context carrying the in-app language, published once by the activity.
 *
 * Dialogs and modal bottom sheets render in their own window, and every window
 * owner re-provides [LocalContext] with its raw window context — which drops the
 * app language override. This local survives that boundary, so overlays can
 * restore it via [ProvideAppLocale].
 */
val LocalAppLocaleContext = staticCompositionLocalOf<Context?> { null }

fun Context.withAppLocale(languageTag: String): Context {
    val locale = Locale.forLanguageTag(languageTag)
    val configuration = Configuration(resources.configuration).apply {
        setLocale(locale)
        setLocales(LocaleList(locale))
    }
    return LocalizedContext(this, createConfigurationContext(configuration))
}

private class LocalizedContext(
    base: Context,
    private val localized: Context,
) : ContextWrapper(base) {

    override fun getResources(): Resources = localized.resources

    override fun getAssets(): AssetManager = localized.assets
}

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
        LocalResources provides localizedContext.resources,
        content = content,
    )
}
