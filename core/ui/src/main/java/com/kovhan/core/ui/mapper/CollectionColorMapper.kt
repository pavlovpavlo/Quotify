package com.kovhan.core.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kovhan.design.systems.QuotifyColorPalette
import com.kovhan.design.systems.QuotifyMaterialTheme


object CollectionColorMapper {

    const val DEFAULT_TONE = "terra"

    val tones: List<String> = listOf("terra", "olive", "gold", "ai", "plum", "teal")

    fun toColor(tone: String, palette: QuotifyColorPalette): Color =
        when (tone.lowercase()) {
            "terra" -> palette.accentPrimary
            "olive" -> palette.accentSaved
            "gold" -> palette.accentPremium
            "ai" -> palette.accentAi
            "plum" -> palette.accentPlum
            "teal" -> palette.accentTeal
            else -> palette.accentPrimary
        }

    /** Soft background per tone — mirrors the `--accent-*-soft` tokens; plum/teal use a 16% wash. */
    fun toSoftColor(tone: String, palette: QuotifyColorPalette): Color =
        when (tone.lowercase()) {
            "terra" -> palette.accentPrimarySoft
            "olive" -> palette.accentSavedSoft
            "gold" -> palette.accentPremiumSoft
            "ai" -> palette.accentAiSoft
            "plum" -> palette.accentPlum.copy(alpha = 0.16f)
            "teal" -> palette.accentTeal.copy(alpha = 0.16f)
            else -> palette.accentPrimarySoft
        }

    /** Foreground/icon colour per tone — gold reads better with its hover shade. */
    fun toIconColor(tone: String, palette: QuotifyColorPalette): Color =
        if (tone.lowercase() == "gold") palette.accentPremiumHover else toColor(tone, palette)
}

@Composable
fun collectionColor(tone: String): Color =
    CollectionColorMapper.toColor(tone, QuotifyMaterialTheme.colors)
