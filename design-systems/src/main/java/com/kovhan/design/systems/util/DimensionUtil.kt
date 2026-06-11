package com.kovhan.design.systems.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.window.layout.WindowMetricsCalculator

private const val WIDTH_MEDIUM = 720F
private const val HEIGHT_MEDIUM = 690F
private const val DENSITY_MULTIPLIER = 0.5F
private const val DEFAULT_DENSITY_MULTIPLIER = 1F
private const val AVERAGE_DIVIDER = 2

@Composable
private fun getCurrentWindowSize(): DpSize {
    val activity = LocalContext.current.findActivity()
    val density = LocalDensity.current
    val metrics = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
    return with(density) { metrics.bounds.toComposeRect().size.toDpSize() }
}

private tailrec fun Context.findActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> error("Expected an Activity context but found $this")
}

private fun calculateDimensionMultiplier(size: Float, mediumSize: Float): Float {
    val multiplier = size / mediumSize
    val multiplierRemainder = DEFAULT_DENSITY_MULTIPLIER - multiplier
    return DEFAULT_DENSITY_MULTIPLIER - multiplierRemainder * DENSITY_MULTIPLIER
}

@Composable
fun getDimensionMultiplier(): Float {
    val size = getCurrentWindowSize()
    val multiplierWidth = calculateDimensionMultiplier(size.width.value, WIDTH_MEDIUM)
    val multiplierHeight = calculateDimensionMultiplier(size.height.value, HEIGHT_MEDIUM)
    return (multiplierWidth + multiplierHeight) / AVERAGE_DIVIDER
}