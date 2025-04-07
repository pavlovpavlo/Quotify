package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf

val LocalSystem = compositionLocalOf { quotifyLightSystem }

@Immutable
data class QuotifySystem(
    val isDarkTheme: Boolean = false
)

val quotifyLightSystem = QuotifySystem(
    isDarkTheme = false
)

val quotifyDarkSystem = QuotifySystem(
    isDarkTheme = true
)