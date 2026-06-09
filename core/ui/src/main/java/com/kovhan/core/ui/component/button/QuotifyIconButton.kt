package com.kovhan.core.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material3.LocalContentColor
import com.kovhan.core.ui.extensions.debouncedClickable

/**
 * Circular icon-only button. Same variant + accent semantics as [QuotifyButton],
 * sized by the [size] (touch target).
 *
 * [content] receives a `LocalContentColor` matching the resolved button color,
 * so a child `Icon` will pick the correct tint automatically.
 */
@Composable
fun QuotifyIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: QuotifyButtonVariant = QuotifyButtonVariant.Ghost,
    accent: QuotifyButtonAccent = QuotifyButtonAccent.Neutral,
    enabled: Boolean = true,
    loading: Boolean = false,
    withRipple: Boolean = true,
    size: Dp = 40.dp,
    colors: QuotifyButtonColors = QuotifyButtonDefaults.colors(variant, accent),
    content: @Composable () -> Unit,
) {
    val isInteractive = enabled && !loading
    val container = if (isInteractive) colors.container else colors.disabledContainer
    val contentColor = if (isInteractive) colors.content else colors.disabledContent
    val border = if (isInteractive) colors.border else colors.disabledBorder
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(container)
            .then(
                if (border != Color.Transparent) Modifier.border(1.dp, border, CircleShape)
                else Modifier
            )
            .debouncedClickable(
                interactionSource = interactionSource,
                indication = if (withRipple) ripple(color = contentColor, bounded = false) else null,
                enabled = isInteractive,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(size * 0.5f),
                color = contentColor,
                strokeWidth = 2.dp,
            )
        } else {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                content()
            }
        }
    }
}
