package com.kovhan.design.systems

import androidx.annotation.RawRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf

val LocalAnimation = compositionLocalOf { quotifyLightAnimation }

@Immutable
data class QuotifyAnimation(
    @RawRes val loading: Int = R.raw.load_anim
)

val quotifyLightAnimation = QuotifyAnimation(
)

val quotifyDarkAnimation = QuotifyAnimation(
)