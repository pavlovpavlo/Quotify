package com.kovhan.feature.subscription.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.extensions.debouncedClickable
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * CTA пейволу. [com.kovhan.core.ui.component.button.QuotifyButton] приймає
 * суцільний колір, а тут за макетом потрібен той самий оливковий градієнт,
 * що й у герой-картці — тому окремий композабл, а не переоприділення кольорів.
 */
@Composable
internal fun PremiumGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    height: Dp = 52.dp,
    brush: Brush = QuotifyMaterialTheme.colors.premium.gradient,
) {
    val premium = QuotifyMaterialTheme.colors.premium
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusFull)
    val interactionSource = remember { MutableInteractionSource() }
    val isInteractive = enabled && !loading

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .alpha(if (isInteractive) 1f else 0.5f)
            .clip(shape)
            .background(brush)
            .debouncedClickable(
                interactionSource = interactionSource,
                indication = ripple(color = premium.onGradient),
                enabled = isInteractive,
                role = Role.Button,
                debounceInterval = 300L,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(QuotifyMaterialTheme.dimensions.iconMd),
                color = premium.onGradient,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                color = premium.onGradient,
                style = typography.bodyStrong,
            )
        }
    }
}
