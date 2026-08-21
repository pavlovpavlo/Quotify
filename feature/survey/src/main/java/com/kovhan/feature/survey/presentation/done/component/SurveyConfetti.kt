package com.kovhan.feature.survey.presentation.done.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import kotlin.math.floor
import kotlin.random.Random

private const val PIECE_COUNT = 34
private const val FADE_IN_PROGRESS = 0.08f
private const val FADE_OUT_PROGRESS = 0.85f
private const val STREAMER_LENGTH_FACTOR = 2.6f

private data class ConfettiPiece(
    val left: Float,
    val delaySeconds: Float,
    val durationSeconds: Float,
    val driftDp: Float,
    val spinDegrees: Float,
    val colorIndex: Int,
    val isStreamer: Boolean,
    val widthDp: Float,
    val heightDp: Float,
)

/** Falling paper + serpentine strips behind the thank-you screen. */
@Composable
internal fun SurveyConfetti(modifier: Modifier = Modifier) {
    val colors = QuotifyMaterialTheme.colors
    val palette = remember(colors) {
        listOf(colors.accentPrimary, colors.accentPremium, colors.accentSaved, colors.accentAi)
    }
    val pieces = remember { generatePieces() }

    var elapsedSeconds by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        val startMillis = withFrameMillis { it }
        while (true) {
            withFrameMillis { frameMillis ->
                elapsedSeconds = (frameMillis - startMillis) / 1000f
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())

        pieces.forEach { piece ->
            val cycle = (elapsedSeconds - piece.delaySeconds) / piece.durationSeconds
            if (cycle < 0f) return@forEach

            val progress = cycle - floor(cycle)
            val alpha = alphaFor(progress)
            if (alpha <= 0f) return@forEach

            val width = piece.widthDp.dp.toPx()
            val height = piece.heightDp.dp.toPx() *
                if (piece.isStreamer) STREAMER_LENGTH_FACTOR else 1f

            val x = size.width * piece.left + piece.driftDp.dp.toPx() * progress
            val y = -height + progress * (size.height + height * 2f)

            rotate(degrees = piece.spinDegrees * progress, pivot = Offset(x, y)) {
                drawRoundRect(
                    color = palette[piece.colorIndex % palette.size].copy(alpha = alpha),
                    topLeft = Offset(x - width / 2f, y),
                    size = Size(width, height),
                    cornerRadius = cornerRadius,
                )
            }
        }
    }
}

private fun alphaFor(progress: Float): Float = when {
    progress < FADE_IN_PROGRESS -> progress / FADE_IN_PROGRESS
    progress > FADE_OUT_PROGRESS -> 1f - (progress - FADE_OUT_PROGRESS) / (1f - FADE_OUT_PROGRESS)
    else -> 1f
}

private fun generatePieces(): List<ConfettiPiece> = List(PIECE_COUNT) { index ->
    ConfettiPiece(
        left = 0.02f + Random.nextFloat() * 0.94f,
        delaySeconds = Random.nextFloat() * 1.6f,
        durationSeconds = 2.4f + Random.nextFloat() * 1.8f,
        driftDp = Random.nextFloat() * 80f - 40f,
        spinDegrees = (180f + Random.nextFloat() * 720f) * if (Random.nextBoolean()) 1f else -1f,
        colorIndex = index,
        isStreamer = index % 3 == 0,
        widthDp = 4f + Random.nextFloat() * 4f,
        heightDp = 10f + Random.nextFloat() * 8f,
    )
}
