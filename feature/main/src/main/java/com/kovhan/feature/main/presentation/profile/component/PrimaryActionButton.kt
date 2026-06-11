package com.kovhan.feature.main.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

/**
 * Full-width primary action button used in profile bottom sheets.
 * Disabled state matches the spec: [QuotifyColorPalette.borderStrong] container,
 * [QuotifyColorPalette.textTertiary] label.
 */
@Composable
internal fun PrimaryActionButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val container = when {
        !enabled -> colors.borderStrong
        pressed -> colors.accentPrimaryHover
        else -> colors.accentPrimary
    }
    val content = if (enabled) colors.textOnAccent else colors.textTertiary

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusMd))
            .background(container)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = content,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.W600,
            ),
        )
    }
}
