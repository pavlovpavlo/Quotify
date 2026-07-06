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
}

@Composable
fun collectionColor(tone: String): Color =
    CollectionColorMapper.toColor(tone, QuotifyMaterialTheme.colors)
