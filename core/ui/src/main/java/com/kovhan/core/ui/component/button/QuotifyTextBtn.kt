package com.kovhan.core.ui.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.component.button.QuotifyButtonAccent
import com.kovhan.design.systems.component.button.QuotifyButtonSize
import com.kovhan.design.systems.component.button.QuotifyButtonVariant

/**
 * Convenience for a small ghost button — common enough (Skip, Cancel, inline
 * "Forgot password?") to deserve its own name. Delegates to [QuotifyButton]
 * so styling stays in lockstep with the rest of the system.
 *
 * Defaults to *no ripple* because ghost buttons sit on the page background
 * with no container — a ripple looks like a stray smudge there. Pass
 * `withRipple = true` to opt back in.
 */
@Composable
fun QuotifyTextBtn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: QuotifyButtonAccent = QuotifyButtonAccent.Neutral,
    size: QuotifyButtonSize = QuotifyButtonSize.Small,
    withRipple: Boolean = false,
) {
    QuotifyButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        variant = QuotifyButtonVariant.Ghost,
        size = size,
        accent = accent,
        enabled = enabled,
        withRipple = withRipple,
    )
}
