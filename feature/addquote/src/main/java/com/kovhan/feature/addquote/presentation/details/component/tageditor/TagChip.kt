package com.kovhan.feature.addquote.presentation.details.component.tageditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun TagChip(
    tag: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(dimensions.radiusFull))
            .background(colors.accentSavedSoft)
            .padding(start = dimensions.space3, end = dimensions.space1, top = dimensions.size5, bottom = dimensions.size5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "#$tag",
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = colors.accentSaved,
        )
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .padding(start = dimensions.size2)
                .size(dimensions.size24)
                .clip(RoundedCornerShape(dimensions.radiusFull))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size14),
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.details_tag_remove_cd),
                colorFilter = ColorFilter.tint(colors.accentSaved),
            )
        }
    }
}
