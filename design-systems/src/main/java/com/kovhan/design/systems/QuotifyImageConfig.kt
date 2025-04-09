package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.annotation.DrawableRes
import com.kovhan.design.systems.R

val LocaleQuotifyImages = compositionLocalOf { quotifyLightImages }

@Immutable
class QuotifyImagePalette(
    val home: Int,
    val quotes: Int,
    val favorites: Int,
    val profile: Int
)

val quotifyLightImages = QuotifyImagePalette(
    home = R.drawable.ic_home,
    quotes = R.drawable.ic_quotes,
    favorites = R.drawable.ic_favorites,
    profile = R.drawable.ic_profile
)

val quotifyDarkImages = QuotifyImagePalette(
    home = R.drawable.ic_home,
    quotes = R.drawable.ic_quotes,
    favorites = R.drawable.ic_favorites,
    profile = R.drawable.ic_profile
)