package com.kovhan.core.ui.extensions

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Smoothly translates the composable up and down forever, like a feather floating in still air.
 *
 * - [amplitude] half-range of motion; total travel is `2 * amplitude`
 * - [durationMs] time for one direction; full cycle is `2 * durationMs`
 * - [phaseDelayMs] shifts the start so multiple elements don't move in lock-step
 */
@Composable
fun Modifier.floatingY(
    amplitude: Dp = 6.dp,
    durationMs: Int = 2400,
    phaseDelayMs: Int = 0,
): Modifier = composed {
    val density = LocalDensity.current
    val amplitudePx = with(density) { amplitude.toPx() }

    val transition = rememberInfiniteTransition(label = "floatingY")
    val translation by transition.animateFloat(
        initialValue = -amplitudePx,
        targetValue = amplitudePx,
        animationSpec = infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(
                durationMillis = durationMs,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(phaseDelayMs),
        ),
        label = "floatingYTranslation",
    )

    graphicsLayer { translationY = translation }
}
