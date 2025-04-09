package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocaleQuotifyColors = compositionLocalOf { quotifyLightPalette }

@Immutable
data class QuotifyColorPalette(
    val backgroundColor: Color,
    val primary: Color,
    val secondary: Color
)

val quotifyLightPalette = QuotifyColorPalette(
    backgroundColor = Colors.lightBackgroundColor,
    primary = Colors.black,
    secondary = Colors.gray
)

val quotifyDarkPalette = QuotifyColorPalette(
    backgroundColor = Colors.darkBackgroundColor,
    primary = Colors.white,
    secondary = Colors.lightGray
)
