package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocaleQuotifyColors = compositionLocalOf { quotifyLightPalette }

@Immutable
data class QuotifyColorPalette(
    val backgroundColor: Color,
    val primary: Color = Colors.primary,
    val secondary: Color = Colors.btnColor,
    val tabBg: Color = Colors.tabBg,
    val textColorPrimary: Color = Colors.textColorPrimary,
    val textColorGray: Color = Colors.textColorGray,
    val btnColor: Color = Colors.btnColor,
    val btnColorDisabled: Color = Colors.btnColorDisabled,
    val dialogTextFieldBg: Color = Colors.dialogTextFieldBg,
    val textFieldBg: Color = Colors.white,
    val textColorError: Color = Colors.textColorError,
)

val quotifyLightPalette = QuotifyColorPalette(
    backgroundColor = Colors.lightBackgroundColor
)

val quotifyDarkPalette = QuotifyColorPalette(
    backgroundColor = Colors.darkBackgroundColor
)
