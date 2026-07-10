package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun LibrarySearchBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusFull)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.buttonHeight)
            .clip(shape)
            .background(colors.bgElevated)
            .border(dimensions.size1, colors.borderStrong, shape)
            .clickable(onClick = onClick)
            .padding(start = dimensions.size14, end = dimensions.space2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size10),
    ) {
        Image(
            modifier = Modifier.size(dimensions.size18),
            painter = painterResource(R.drawable.ic_search),
            contentDescription = stringResource(R.string.library_search_cd),
            colorFilter = ColorFilter.tint(colors.borderStrong),
        )
        Text(
            text = stringResource(R.string.library_search_placeholder),
            style = typography.body,
            color = colors.textTertiary,
        )
    }
}
