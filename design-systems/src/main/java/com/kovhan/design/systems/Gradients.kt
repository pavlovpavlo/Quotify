package com.kovhan.design.systems

import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode

object Gradients {
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