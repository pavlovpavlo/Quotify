package com.kovhan.core.ui.component.button

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Visual style of the button:
 * - [Filled]   — solid background, contrasting label
 * - [Tonal]    — soft accent tint background, accent-colored label
 * - [Outlined] — transparent background, accent border + label
 * - [Ghost]    — no background or border, accent label only
 */
enum class QuotifyButtonVariant { Filled, Tonal, Outlined, Ghost }

/** Size preset mapping to height, padding, icon size and label text style. */
enum class QuotifyButtonSize { Small, Medium, Large }

/** Semantic accent role pulled from the Folio accent palette. */
enum class QuotifyButtonAccent { Primary, Ai, Saved, Premium, Neutral, Destructive }

/** Resolved colors for one button in one state. */
@Immutable
data class QuotifyButtonColors(
    val container: Color,
    val content: Color,
    val border: Color,
    val disabledContainer: Color,
    val disabledContent: Color,
    val disabledBorder: Color,
)

/** Resolved dimensions for one button size. */
@Immutable
data class QuotifyButtonSizeSpec(
    val height: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
    val iconGap: Dp,
    val borderWidth: Dp,
    val textStyle: TextStyle,
    val shape: Shape,
) {
    companion object {
        val DefaultBorderWidth: Dp = 1.dp
    }
}
