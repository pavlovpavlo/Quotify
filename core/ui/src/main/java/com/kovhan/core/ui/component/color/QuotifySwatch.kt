package com.kovhan.core.ui.component.color

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/**
 * A pickable style swatch: the fill is supplied by the caller, this owns the
 * selection ring and check mark. Shared by the collection colour picker and the
 * widget background pickers so all three read as the same control.
 */
@Composable
fun QuotifySwatch(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusFull),
    haloColor: Color = Color.Transparent,
    selectedBorderColor: Color = QuotifyMaterialTheme.colors.textPrimary,
    checkTint: Color = Color.White,
    swatchSize: Dp = QuotifyMaterialTheme.dimensions.size52,
    fill: @Composable BoxScope.() -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = dimensions.size2,
                    color = if (selected) haloColor else Color.Transparent,
                    shape = shape,
                )
                .padding(dimensions.size5)
                .height(swatchSize)
                .clip(shape)
                .then(
                    if (selected) {
                        Modifier.border(dimensions.size2, selectedBorderColor, shape)
                    } else {
                        Modifier
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(modifier = Modifier.fillMaxSize().clip(shape), content = fill)

            if (selected) {
                Image(
                    modifier = Modifier.size(dimensions.size22),
                    painter = painterResource(DsR.drawable.ic_check),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(checkTint),
                )
            }
        }
    }
}

/** [QuotifySwatch] whose fill is a flat colour. */
@Composable
fun QuotifyColorSwatch(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusFull),
    selectedBorderColor: Color = QuotifyMaterialTheme.colors.textPrimary,
    checkTint: Color = Color.White,
    swatchSize: Dp = QuotifyMaterialTheme.dimensions.size52,
) {
    // Near-white and near-black fills vanish against the sheet behind them, so
    // they get a contrasting hairline to keep their edge readable.
    val outline = when {
        color.luminance() > OUTLINE_LIGHT_LUMINANCE -> Color.Black
        color.luminance() < OUTLINE_DARK_LUMINANCE -> Color.White
        else -> null
    }

    QuotifySwatch(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        haloColor = color.copy(alpha = HALO_ALPHA),
        selectedBorderColor = selectedBorderColor,
        checkTint = checkTint,
        swatchSize = swatchSize,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .then(
                    if (outline != null) {
                        Modifier.border(
                            QuotifyMaterialTheme.dimensions.size1,
                            outline.copy(alpha = OUTLINE_ALPHA),
                            shape,
                        )
                    } else {
                        Modifier
                    },
                ),
        )
    }
}

private const val HALO_ALPHA = 0.2f
private const val OUTLINE_ALPHA = 0.55f
private const val OUTLINE_LIGHT_LUMINANCE = 0.8f
private const val OUTLINE_DARK_LUMINANCE = 0.05f
