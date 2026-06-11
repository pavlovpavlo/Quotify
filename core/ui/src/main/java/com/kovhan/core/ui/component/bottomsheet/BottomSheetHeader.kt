package com.kovhan.core.ui.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun BottomSheetHeader(
    title: String?,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensions.space5,
                bottom = if (title != null) dimensions.space3 else dimensions.space2,
                start = dimensions.space6,
                end = dimensions.space6,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .height(5.dp)
                .width(36.dp)
                .background(
                    color = colors.borderStrong,
                    shape = RoundedCornerShape(dimensions.radiusFull),
                ),
        )

        if (title != null) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.space5),
                style = QuotifyMaterialTheme.typography.h4,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
