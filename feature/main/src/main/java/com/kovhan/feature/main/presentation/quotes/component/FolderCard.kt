package com.kovhan.feature.main.presentation.quotes.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.ui.mapper.CollectionIconMapper
import com.kovhan.core.ui.mapper.collectionColor
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun FolderCard(
    collection: SavedCollection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val tone = collectionColor(collection.iconColor)
    val cardShape = RoundedCornerShape(dimensions.radiusLg)

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = dimensions.size14)
                .size(width = dimensions.size38, height = dimensions.size10)
                .clip(RoundedCornerShape(topStart = dimensions.radiusSm, topEnd = dimensions.radiusSm))
                .background(tone.copy(alpha = 0.16f)),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.size7)
                .fillMaxHeight()
                .clip(cardShape)
                .background(colors.bgElevated)
                .border(dimensions.size1, colors.border, cardShape)
                .clickable(onClick = onClick)
                .padding(dimensions.size15),
        ) {
            Box(
                modifier = Modifier
                    .size(dimensions.size40)
                    .clip(RoundedCornerShape(dimensions.radiusLg))
                    .background(tone.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(dimensions.iconMd),
                    painter = painterResource(CollectionIconMapper.toDrawableRes(collection.iconId)),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(tone),
                )
            }

            Text(
                modifier = Modifier.padding(top = dimensions.size12),
                text = collection.name,
                style = typography.readingBody.copy(fontWeight = FontWeight.W600),
                color = colors.textPrimary,
            )

            Text(
                modifier = Modifier.padding(top = dimensions.size4),
                text = pluralStringResource(
                    R.plurals.library_folder_quote_count,
                    collection.quoteCount ?: 0,
                    collection.quoteCount ?: 0,
                ),
                style = typography.caption,
                color = colors.textTertiary,
            )
        }
    }
}
