package com.kovhan.feature.widget.presentation.picker.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.ui.component.quote.ReadOnlyQuoteCard
import com.kovhan.design.systems.QuotifyMaterialTheme

/** A read-only quote card that toggles picked state, with a corner check marker. */
@Composable
internal fun PickableQuoteCard(
    quote: EnrichedQuote,
    query: String,
    picked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val shape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusLg)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle,
            ),
    ) {
        ReadOnlyQuoteCard(
            quote = quote,
            highlightQuery = query.ifEmpty { null },
            trailingAction = {
                PickMarker(
                    picked = picked,
                    size = 26.dp,
                    modifier = Modifier.align(Alignment.TopEnd),
                )
            },
        )

        if (picked) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(shape)
                    .border(1.5.dp, colors.accentPrimary, shape),
            )
        }
    }
}
