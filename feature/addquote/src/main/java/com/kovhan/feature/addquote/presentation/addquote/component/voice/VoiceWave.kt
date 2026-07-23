package com.kovhan.feature.addquote.presentation.addquote.component.voice

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.lerp
import com.kovhan.design.systems.QuotifyMaterialTheme
import kotlin.math.PI
import kotlin.math.sin

private const val WAVE_BAR_COUNT = 9

@Composable
internal fun VoiceWave(
    active: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val trackAlpha by animateFloatAsState(
        targetValue = if (active) 1f else 0.45f,
        animationSpec = tween(durationMillis = 220, easing = CubicBezierEasing(0.22f, 0.61f, 0.36f, 1f)),
        label = "VoiceWaveAlpha",
    )

    val transition = rememberInfiniteTransition(label = "VoiceWave")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "VoiceWavePhase",
    )

    Row(
        modifier = modifier
            .height(dimensions.size30)
            .alpha(trackAlpha),
        horizontalArrangement = Arrangement.spacedBy(dimensions.size4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(WAVE_BAR_COUNT) { index ->
            val barHeight = if (active) {
                val wave = (sin(phase + index * 0.7f) + 1f) / 2f
                lerp(dimensions.size7, dimensions.size26, wave)
            } else {
                dimensions.size7
            }

            Box(
                modifier = Modifier
                    .width(dimensions.size4)
                    .height(barHeight)
                    .clip(RoundedCornerShape(dimensions.radiusFull))
                    .background(colors.accentSaved),
            )
        }
    }
}
