package com.kovhan.feature.onboarding.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.extensions.floatingY
import com.kovhan.design.systems.QuotifyColorPalette
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

private data class FloatingChipSpec(
    val textRes: Int,
    val alignment: Alignment,
    val offsetX: Int,
    val offsetY: Int,
    val amplitude: Int,
    val durationMs: Int,
    val phaseDelayMs: Int,
    val tone: (QuotifyColorPalette) -> ChipTone,
)

private data class ChipTone(
    val background: androidx.compose.ui.graphics.Color,
    val content: androidx.compose.ui.graphics.Color,
    val borderAlpha: Float,
    val borderBase: androidx.compose.ui.graphics.Color = content,
)

private val Slide2Chips = listOf(
    FloatingChipSpec(
        textRes = R.string.onboarding_chip_philosophy,
        alignment = Alignment.TopStart,
        offsetX = 4, offsetY = 8,
        amplitude = 3, durationMs = 2400, phaseDelayMs = 0,
    ) { ChipTone(it.accentSavedSoft, it.accentSaved, borderAlpha = 0.25f) },
    FloatingChipSpec(
        textRes = R.string.onboarding_chip_author,
        alignment = Alignment.TopEnd,
        offsetX = -4, offsetY = 14,
        amplitude = 4, durationMs = 2600, phaseDelayMs = 400,
    ) { ChipTone(it.accentAiSoft, it.accentAi, borderAlpha = 0.22f) },
    FloatingChipSpec(
        textRes = R.string.onboarding_chip_favorites,
        alignment = Alignment.CenterEnd,
        offsetX = 2, offsetY = -10,
        amplitude = 3, durationMs = 2800, phaseDelayMs = 800,
    ) { ChipTone(it.accentPremiumSoft, it.accentPremium, borderAlpha = 0.3f) },
    FloatingChipSpec(
        textRes = R.string.onboarding_chip_life,
        alignment = Alignment.BottomStart,
        offsetX = 6, offsetY = -18,
        amplitude = 4, durationMs = 2500, phaseDelayMs = 1200,
    ) {
        ChipTone(
            background = it.accentPrimarySoft,
            content = it.accentPrimaryHover,
            borderAlpha = 0.25f,
            borderBase = it.accentPrimary,
        )
    },
    FloatingChipSpec(
        textRes = R.string.onboarding_chip_science,
        alignment = Alignment.BottomEnd,
        offsetX = -6, offsetY = -6,
        amplitude = 3, durationMs = 2700, phaseDelayMs = 1600,
    ) { ChipTone(it.accentAiSoft, it.accentAi, borderAlpha = 0.4f) },
)

@Composable
internal fun OnboardingSlide2Content(modifier: Modifier = Modifier) {
    val palette = QuotifyMaterialTheme.colors

    OnboardingSlideScaffold(
        modifier = modifier,
        title = R.string.onboarding_slide_2_title,
        body = R.string.onboarding_slide_2_body,
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = QuotifyMaterialTheme.dimensions.space4),
            painter = painterResource(QuotifyMaterialTheme.images.onboardingSlide2),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        Slide2Chips.forEach { spec ->
            val tone = spec.tone(palette)
            FloatingChip(spec = spec, tone = tone)
        }
    }
}

@Composable
private fun BoxScope.FloatingChip(spec: FloatingChipSpec, tone: ChipTone) {
    OnboardingChip(
        text = stringResource(spec.textRes),
        background = tone.background,
        contentColor = tone.content,
        border = tone.borderBase.copy(alpha = tone.borderAlpha),
        modifier = Modifier
            .align(spec.alignment)
            .offset(x = spec.offsetX.dp, y = spec.offsetY.dp)
            .floatingY(
                amplitude = spec.amplitude.dp,
                durationMs = spec.durationMs,
                phaseDelayMs = spec.phaseDelayMs,
            ),
    )
}
