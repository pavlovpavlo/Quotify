package com.kovhan.feature.addquote.presentation.addquote.component.voice

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun VoiceMicButton(
    recording: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val container by animateColorAsState(
        targetValue = if (recording) colors.accentPrimaryHover else colors.accentPrimary,
        animationSpec = tween(durationMillis = 180),
        label = "VoiceMicColor",
    )
    val scale by animateFloatAsState(
        targetValue = if (recording) 1.04f else 1f,
        animationSpec = tween(durationMillis = 180),
        label = "VoiceMicScale",
    )

    Box(
        modifier = modifier.size(dimensions.size96 + dimensions.size40),
        contentAlignment = Alignment.Center,
    ) {
        if (recording) {
            PulseRing(delayMillis = 0)
            PulseRing(delayMillis = 1200)
        }

        Box(
            modifier = Modifier
                .scale(scale)
                .size(dimensions.size96)
                .shadow(
                    elevation = dimensions.size14,
                    shape = CircleShape,
                    clip = false,
                    spotColor = colors.accentPrimary,
                    ambientColor = colors.accentPrimary,
                )
                .clip(CircleShape)
                .background(container)
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectTapGestures(
                        onPress = {
                            onStart()
                            tryAwaitRelease()
                            onStop()
                        },
                    )
                },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size38),
                painter = painterResource(QuotifyMaterialTheme.images.dockInputVoice),
                contentDescription = stringResource(R.string.add_quote_voice_mic_cd),
                colorFilter = ColorFilter.tint(colors.textOnAccent),
            )
        }
    }
}

@Composable
private fun PulseRing(delayMillis: Int) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val transition = rememberInfiniteTransition(label = "VoicePulse")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(delayMillis),
        ),
        label = "VoicePulseProgress",
    )

    Box(
        modifier = Modifier
            .size(dimensions.size96)
            .graphicsLayer {
                val current = 1f + progress * 0.55f
                scaleX = current
                scaleY = current
                alpha = 0.5f * (1f - progress)
            }
            .clip(CircleShape)
            .background(colors.accentPrimary),
    )
}
