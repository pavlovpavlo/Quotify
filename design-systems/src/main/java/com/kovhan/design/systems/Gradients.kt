package com.kovhan.design.systems

import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode

object Gradients {
    fun offerHero(start: Color, end: Color): Brush =
        Brush.verticalGradient(listOf(start, end))

    fun offerCta(start: Color, end: Color): Brush = Brush.linearGradient(
        colors = listOf(start, end),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    fun premium(start: Color, middle: Color, end: Color): Brush = Brush.linearGradient(
        colorStops = arrayOf(
            0F to start,
            0.45F to middle,
            1F to end,
        ),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    fun getSkeleton(
        colorBackground: Color,
        colorProgress: Color,
        progress: Float = 0F
    ): ShaderBrush {
        return object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val startX = size.width * -1.5F
                val endX = size.width * 1.5F
                val offset = startX + (endX - startX) * progress
                return LinearGradientShader(
                    colors = listOf(colorBackground, colorProgress, colorBackground),
                    from = Offset(offset, 0F),
                    to = Offset(size.width + offset, 0F),
                    tileMode = TileMode.Clamp
                )
            }
        }
    }
}