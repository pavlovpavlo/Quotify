package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.widget.WidgetCovers
import com.kovhan.core.models.widget.WidgetTones
import com.kovhan.core.ui.component.color.QuotifyColorSwatch
import com.kovhan.core.ui.component.color.QuotifySwatch
import com.kovhan.core.ui.mapper.WidgetBackgroundMapper
import com.kovhan.core.ui.mapper.WidgetCoverMapper
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun WidgetTonePicker(
    selectedToneId: String,
    onToneSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensions.size7),
    ) {
        WidgetTones.ALL.forEach { toneId ->
            QuotifyColorSwatch(
                modifier = Modifier.width(TONE_CELL),
                color = WidgetBackgroundMapper.fill(toneId),
                selected = selectedToneId == toneId,
                onClick = { onToneSelected(toneId) },
                checkTint = WidgetBackgroundMapper.colors(toneId).readableText,
            )
        }
    }
}

@Composable
internal fun WidgetCoverPicker(
    selectedCoverId: String,
    blurEnabled: Boolean,
    onCoverSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusSm)

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(dimensions.size7),
    ) {
        WidgetCovers.ALL.forEach { coverId ->
            QuotifySwatch(
                modifier = Modifier.width(COVER_CELL),
                selected = selectedCoverId == coverId,
                onClick = { onCoverSelected(coverId) },
                shape = shape,
                swatchSize = dimensions.size78,
                haloColor = WidgetCoverMapper.averageColor(coverId).copy(alpha = HALO_ALPHA),
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(WidgetCoverMapper.imageRes(coverId, blurEnabled)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

// Narrower than it is tall, so the swatch reads as the same pill used by the
// collection colour picker rather than a circle.
private val TONE_CELL = 46.dp
private val COVER_CELL = 88.dp
private const val HALO_ALPHA = 0.2f
