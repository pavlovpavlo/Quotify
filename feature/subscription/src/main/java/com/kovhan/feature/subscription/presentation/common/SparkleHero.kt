package com.kovhan.feature.subscription.presentation.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyColorPalette
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

internal val SparkleHeroHeight = 190.dp
private val IllustrationHeight = 186.dp
private val GlowSize = 180.dp

private enum class SparkShape { Burst, Star, Dot }

private enum class SparkTone { Premium, Primary, Saved }

private data class SparkSpec(
    val shape: SparkShape,
    val tone: SparkTone,
    val size: Dp,
    val x: Dp,
    val y: Dp,
    val rotation: Float,
    val delayMillis: Int,
    val durationMillis: Int,
    val minAlpha: Float,
)

private val sparkSpecs = listOf(
    SparkSpec(SparkShape.Burst, SparkTone.Premium, 38.dp, (-118).dp, (-118).dp, -8f, 0, 2600, 0.35f),
    SparkSpec(SparkShape.Star, SparkTone.Primary, 20.dp, (-96).dp, (-44).dp, 14f, 900, 3400, 0.25f),
    SparkSpec(SparkShape.Dot, SparkTone.Saved, 26.dp, (-134).dp, (-64).dp, 0f, 1700, 2900, 0.3f),
    SparkSpec(SparkShape.Burst, SparkTone.Premium, 24.dp, (-104).dp, (-166).dp, 18f, 2300, 3100, 0.4f),
    SparkSpec(SparkShape.Star, SparkTone.Premium, 30.dp, 116.dp, (-150).dp, -12f, 400, 2800, 0.3f),
    SparkSpec(SparkShape.Dot, SparkTone.Primary, 22.dp, 128.dp, (-88).dp, 0f, 1300, 3600, 0.25f),
    SparkSpec(SparkShape.Burst, SparkTone.Saved, 32.dp, 100.dp, (-34).dp, 6f, 2000, 2500, 0.35f),
    SparkSpec(SparkShape.Star, SparkTone.Premium, 16.dp, 92.dp, (-186).dp, 22f, 2900, 3200, 0.2f),
    SparkSpec(SparkShape.Dot, SparkTone.Premium, 18.dp, (-70).dp, (-190).dp, 0f, 600, 3000, 0.3f),
)

@Composable
internal fun SparkleHero(
    @DrawableRes illustrationRes: Int,
    modifier: Modifier = Modifier,
    showGlow: Boolean = true,
    heroHeight: Dp? = SparkleHeroHeight,
    sparkSpread: Float = 1f,
) {
    val colors = QuotifyMaterialTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (heroHeight != null) Modifier.height(heroHeight) else Modifier.fillMaxHeight()),
        contentAlignment = Alignment.BottomCenter,
    ) {
        if (showGlow) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(GlowSize)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(colors.accentSavedSoft, Color.Transparent),
                        ),
                        shape = CircleShape,
                    ),
            )
        }

        Image(
            modifier = if (heroHeight != null) {
                Modifier.height(IllustrationHeight)
            } else {
                Modifier.fillMaxHeight()
            },
            painter = painterResource(illustrationRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

        sparkSpecs.forEach { spec -> Spark(spec, sparkSpread) }
    }
}

@Composable
private fun BoxScope.Spark(spec: SparkSpec, spread: Float) {
    val colors = QuotifyMaterialTheme.colors
    val transition = rememberInfiniteTransition(label = "spark")
    val animation = infiniteRepeatable<Float>(
        animation = tween(durationMillis = spec.durationMillis, delayMillis = spec.delayMillis),
        repeatMode = RepeatMode.Reverse,
    )
    val alpha by transition.animateFloat(
        initialValue = spec.minAlpha,
        targetValue = 1f,
        animationSpec = animation,
        label = "sparkAlpha",
    )
    val scale by transition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1f,
        animationSpec = animation,
        label = "sparkScale",
    )

    Image(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(x = spec.x * spread, y = spec.y * spread)
            .size(spec.size)
            .rotate(spec.rotation)
            .alpha(alpha)
            .scale(scale),
        painter = painterResource(spec.shape.drawableRes()),
        contentDescription = null,
        colorFilter = ColorFilter.tint(spec.tone.color(colors)),
    )
}

private fun SparkShape.drawableRes(): Int = when (this) {
    SparkShape.Burst -> R.drawable.ic_spark
    SparkShape.Star -> R.drawable.ic_spark_star
    SparkShape.Dot -> R.drawable.ic_spark_dot
}

private fun SparkTone.color(colors: QuotifyColorPalette): Color = when (this) {
    SparkTone.Premium -> colors.accentPremium
    SparkTone.Primary -> colors.accentPrimary
    SparkTone.Saved -> colors.accentSaved
}
