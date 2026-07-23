package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import com.kovhan.core.ui.mapper.CollectionColorMapper
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
internal fun SearchEntityRow(
    iconRes: Int,
    toneKey: String?,
    round: Boolean,
    name: String,
    count: Int,
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val rowShape = RoundedCornerShape(dimensions.radiusLg)
    val iconShape = if (round) CircleShape else RoundedCornerShape(dimensions.radiusLg)
    val iconBg = if (toneKey != null) {
        CollectionColorMapper.toSoftColor(toneKey, colors)
    } else {
        colors.bgSecondary
    }
    val iconTint = if (toneKey != null) {
        CollectionColorMapper.toIconColor(toneKey, colors)
    } else {
        colors.textSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(rowShape)
            .background(colors.bgElevated)
            .border(
                width = dimensions.size1,
                color = colors.border,
                shape = rowShape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = dimensions.size12, vertical = dimensions.size11),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size13),
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size38)
                .clip(iconShape)
                .background(iconBg),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size19),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconTint),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = typography.bodyStrong,
                color = colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.padding(top = dimensions.size2),
                text = pluralStringResource(DsR.plurals.library_folder_quote_count, count, count),
                style = typography.caption,
                color = colors.textTertiary,
            )
        }

        Image(
            modifier = Modifier.size(dimensions.size18),
            painter = painterResource(DsR.drawable.ic_chevron_right),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.textTertiary),
        )
    }
}
