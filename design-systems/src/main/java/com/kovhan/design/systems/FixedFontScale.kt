package com.kovhan.design.systems

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun FixedFontScale(content: @Composable () -> Unit) {
    val density = LocalDensity.current
    if (density.fontScale == 1f) {
        content()
        return
    }
    CompositionLocalProvider(
        LocalDensity provides Density(density = density.density, fontScale = 1f),
        content = content,
    )
}
