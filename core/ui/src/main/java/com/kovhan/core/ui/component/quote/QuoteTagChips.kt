package com.kovhan.core.ui.component.quote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kovhan.design.systems.QuotifyMaterialTheme

/** Read-only `#tag` pills shown on quote cards. */
@Composable
fun QuoteTagChips(
    tags: List<String>,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size8),
        verticalArrangement = Arrangement.spacedBy(dimensions.size8),
    ) {
        tags.forEach { tag ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensions.radiusFull))
                    .background(colors.bgSecondary)
                    .padding(horizontal = dimensions.size9, vertical = dimensions.size3),
            ) {
                Text(
                    text = "#$tag",
                    style = typography.caption,
                    color = colors.textSecondary,
                )
            }
        }
    }
}
