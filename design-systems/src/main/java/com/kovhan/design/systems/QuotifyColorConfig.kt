package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocaleQuotifyColors = compositionLocalOf { quotifyLightPalette }

@Immutable
data class QuotifyColorPalette(
    val backgroundColor: Color
)

val quotifyLightPalette = QuotifyColorPalette(
    backgroundColor = Colors.lightBackgroundColor,
)

val quotifyDarkPalette = QuotifyColorPalette(
    backgroundColor = Colors.darkBackgroundColor,
)
