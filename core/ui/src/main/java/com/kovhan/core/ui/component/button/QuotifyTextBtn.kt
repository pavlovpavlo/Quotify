package com.kovhan.core.ui.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Convenience for a small ghost button (Skip, Cancel, "Forgot password?").
 * Delegates to [QuotifyButton]. Defaults to no ripple since ghost buttons sit
 * directly on the page background.
 */
@Composable
fun QuotifyTextBtn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
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
        loading = loading,
        withRipple = withRipple,
    )
}
