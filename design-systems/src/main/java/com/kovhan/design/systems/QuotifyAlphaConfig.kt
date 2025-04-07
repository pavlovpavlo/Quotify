package com.kovhan.design.systems

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf

val LocalAlpha = compositionLocalOf { alpha }

@Immutable
data class QuotifyAlpha(
    val alpha_0: Float = 0F,
    val alpha_4: Float = 0.04F,
    val alpha_5: Float = 0.05F,
    val alpha_6: Float = 0.06F,
    val alpha_10: Float = 0.10F,
    val alpha_12: Float = 0.12F,
    val alpha_15: Float = 0.15F,
    val alpha_20: Float = 0.20F,
    val alpha_25: Float = 0.25F,
    val alpha_30: Float = 0.30F,
    val alpha_35: Float = 0.35F,
    val alpha_40: Float = 0.40F,
    val alpha_50: Float = 0.50F,
    val alpha_60: Float = 0.60F,
    val alpha_65: Float = 0.65F,
    val alpha_80: Float = 0.80F,
    val alpha_90: Float = 0.90F,
    val alpha_100: Float = 1F,
)

val alpha = QuotifyAlpha()
