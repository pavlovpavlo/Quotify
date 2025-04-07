package com.kovhan.design.systems

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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

@Composable
fun QuotifyAppTheme(
    isDarkIcons: Boolean = false,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    if (isDarkTheme) {
        QuotifyDarkTheme(content = content)
        SideEffect {
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = false
            )
            systemUiController.setNavigationBarColor(
                color = Colors.darkBackgroundColor,
                darkIcons = false
            )
        }
    } else {
        QuotifyLightTheme(content = content)
        SideEffect {
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = isDarkIcons
            )
            systemUiController.setNavigationBarColor(
                color = Colors.lightBackgroundColor,
                darkIcons = true
            )
        }
    }
}

@Composable
private fun QuotifyLightTheme(
    content: @Composable () -> Unit
) {
    val quotifyColors = quotifyLightPalette
    val quotifyImages = quotifyLightImages
    val quotifyAnimation = quotifyLightAnimation

    CompositionLocalProvider(
        LocaleQuotifyColors provides quotifyColors,
        LocaleQuotifyImages provides quotifyImages,
        LocalTypography provides provideTypography(),
        LocalDimensions provides provideDimensions(),
        LocalAnimation provides quotifyAnimation,
        LocalSystem provides quotifyLightSystem
    ) {
        MaterialTheme(
            shapes = Shapes(),
            content = content
        )
    }
}

@Composable
private fun QuotifyDarkTheme(
    content: @Composable () -> Unit
) {
    val quotifyColors = quotifyDarkPalette
    val quotifyImages = quotifyDarkImages
    val quotifyAnimation = quotifyDarkAnimation

    CompositionLocalProvider(
        LocaleQuotifyColors provides quotifyColors,
        LocaleQuotifyImages provides quotifyImages,
        LocalTypography provides provideTypography(),
        LocalDimensions provides provideDimensions(),
        LocalAnimation provides quotifyAnimation,
        LocalSystem provides quotifyDarkSystem
    ) {
        MaterialTheme(
            shapes = Shapes(),
            content = content
        )
    }
}
