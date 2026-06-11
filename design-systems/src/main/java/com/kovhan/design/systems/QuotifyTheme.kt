package com.kovhan.design.systems

import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object QuotifyMaterialTheme {
    val colors: QuotifyColorPalette
        @Composable get() = LocaleQuotifyColors.current

    val images: QuotifyImagePalette
        @Composable get() = LocaleQuotifyImages.current

    val typography: QuotifyTypography
        @Composable get() = LocalTypography.current

    val dimensions: QuotifyDimensions
        @Composable get() = LocalDimensions.current

    val alpha: QuotifyAlpha
        @Composable get() = LocalAlpha.current

    val animations: QuotifyAnimation
        @Composable get() = LocalAnimation.current

    val system: QuotifySystem
        @Composable get() = LocalSystem.current
}

/**
 * Root theme for the app. Drives:
 *
 * - Compose palette/typography/dimensions/images via [CompositionLocalProvider]
 * - System bars (status + nav): transparent background, icon color flipped to
 *   match [isDarkTheme] so they stay readable in light and dark mode.
 *
 * [isDarkTheme] defaults to [isSystemInDarkTheme], so the app follows the
 * system setting out of the box and reacts live to Settings → Display →
 * Dark mode toggle. Pass an explicit value once a user-overridable theme
 * preference exists.
 */
@Composable
fun QuotifyAppTheme(
    activity: AppCompatActivity,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    LaunchedEffect(isDarkTheme) {
        // SystemBarStyle.dark -> dark *background* style -> system shows LIGHT icons.
        // SystemBarStyle.light -> light background -> system shows DARK icons.
        // We want icons to contrast with our app surface, so:
        //   dark theme app -> dark style -> light icons
        //   light theme app -> light style -> dark icons
        activity.enableEdgeToEdge(
            statusBarStyle = if (isDarkTheme) {
                SystemBarStyle.dark(Color.Transparent.toArgb())
            } else {
                SystemBarStyle.light(
                    Color.Transparent.toArgb(),
                    Color.Transparent.toArgb(),
                )
            },
            navigationBarStyle = if (isDarkTheme) {
                SystemBarStyle.dark(Color.Transparent.toArgb())
            } else {
                SystemBarStyle.light(
                    Color.Transparent.toArgb(),
                    Color.Transparent.toArgb(),
                )
            },
        )
    }

    // Single CompositionLocalProvider call site so flipping [isDarkTheme] only
    // swaps the provided palette — [content] stays mounted. Branching into two
    // separate composables here would remount the whole tree (and reset the
    // NavHost) on every theme change.
    CompositionLocalProvider(
        LocaleQuotifyColors provides if (isDarkTheme) quotifyDarkPalette else quotifyLightPalette,
        LocaleQuotifyImages provides if (isDarkTheme) quotifyDarkImages else quotifyLightImages,
        LocalTypography provides provideTypography(),
        LocalDimensions provides provideDimensions(),
        LocalAnimation provides if (isDarkTheme) quotifyDarkAnimation else quotifyLightAnimation,
        LocalSystem provides if (isDarkTheme) quotifyDarkSystem else quotifyLightSystem,
    ) {
        MaterialTheme(shapes = Shapes(), content = content)
    }
}
