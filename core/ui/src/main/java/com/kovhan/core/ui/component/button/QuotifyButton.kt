package com.kovhan.core.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.extensions.debouncedClickable
import com.kovhan.design.systems.component.button.QuotifyButtonAccent
import com.kovhan.design.systems.component.button.QuotifyButtonColors
import com.kovhan.design.systems.component.button.QuotifyButtonDefaults
import com.kovhan.design.systems.component.button.QuotifyButtonSize
import com.kovhan.design.systems.component.button.QuotifyButtonSizeSpec
import com.kovhan.design.systems.component.button.QuotifyButtonVariant

/**
 * Folio-spec button. Pick a [variant], [accent] and [size] and the colors,
 * paddings, icon size and label style fall out of the design system.
 *
 *   QuotifyButton(text = "Save", onClick = ::save)                         // primary filled large
 *   QuotifyButton(text = "Cancel", variant = Ghost, accent = Neutral, ...)
 *   QuotifyButton(text = "Delete", accent = Destructive, ...)
 *
 * For one-off tweaks, copy and override:
 *
 *   colors = QuotifyButtonDefaults.colors(Filled, Primary).copy(content = …)
 */
@Composable
fun QuotifyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: QuotifyButtonVariant = QuotifyButtonVariant.Filled,
    size: QuotifyButtonSize = QuotifyButtonSize.Large,
    accent: QuotifyButtonAccent = QuotifyButtonAccent.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    withRipple: Boolean = true,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    colors: QuotifyButtonColors = QuotifyButtonDefaults.colors(variant, accent),
    sizeSpec: QuotifyButtonSizeSpec = QuotifyButtonDefaults.sizeSpec(size),
) {
    val isInteractive = enabled && !loading
    val container = if (isInteractive) colors.container else colors.disabledContainer
    val content = if (isInteractive) colors.content else colors.disabledContent
    val border = if (isInteractive) colors.border else colors.disabledBorder
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = sizeSpec.height)
            .height(sizeSpec.height)
            .clip(sizeSpec.shape)
            .background(container)
            .then(
                if (border != androidx.compose.ui.graphics.Color.Transparent) {
                    Modifier.border(sizeSpec.borderWidth, border, sizeSpec.shape)
                } else Modifier
            )
            .debouncedClickable(
                interactionSource = interactionSource,
                indication = if (withRipple) ripple(color = content) else null,
                enabled = isInteractive,
                role = Role.Button,
                // Short debounce — only blocks accidental double-taps. The previous
                // 1.5s default ate intentional fast taps (e.g. tapping "Next" twice
                // in a row through the onboarding pager).
                debounceInterval = 300L,
                onClick = onClick,
            )
            .padding(horizontal = sizeSpec.horizontalPadding),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(sizeSpec.iconSize),
                color = content,
                strokeWidth = 2.dp,
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    sizeSpec.iconGap,
                    Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    ButtonIcon(painter = leadingIcon, color = content, size = sizeSpec.iconSize)
                }
                Text(
                    text = text,
                    color = content,
                    style = sizeSpec.textStyle,
                )
                if (trailingIcon != null) {
                    ButtonIcon(painter = trailingIcon, color = content, size = sizeSpec.iconSize)
                }
            }
        }
    }
}

@Composable
private fun ButtonIcon(painter: Painter, color: androidx.compose.ui.graphics.Color, size: androidx.compose.ui.unit.Dp) {
    androidx.compose.foundation.Image(
        modifier = Modifier.size(size),
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(color),
    )
}
