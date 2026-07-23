package com.kovhan.feature.entitydetails.presentation.entity_details.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.ui.mapper.CollectionColorMapper
import com.kovhan.core.ui.mapper.CollectionIconMapper
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.entitydetails.presentation.entity_details.mvi.EntityDetailsSummary as EntityDetailsSummaryModel

@Composable
fun EntityDetailsSummary(
    type: EntityType,
    summary: EntityDetailsSummaryModel,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val toneKey = when (type) {
        EntityType.COLLECTION -> summary.tone
        EntityType.TAG -> "ai"
        EntityType.BOOK -> "gold"
        EntityType.AUTHOR -> "olive"
    }
    val iconRes = when (type) {
        EntityType.COLLECTION -> CollectionIconMapper.toDrawableRes(summary.iconId)
        EntityType.TAG -> DsR.drawable.ic_hash
        EntityType.BOOK -> DsR.drawable.ic_collection_book
        EntityType.AUTHOR -> DsR.drawable.ic_user
    }
    val softBg = CollectionColorMapper.toSoftColor(toneKey, colors)
    val iconColor = CollectionColorMapper.toIconColor(toneKey, colors)

    Row(
        modifier = Modifier.padding(horizontal = dimensions.size18),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size9),
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.size28)
                .clip(RoundedCornerShape(dimensions.size8))
                .background(softBg),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(dimensions.size16),
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(iconColor),
            )
        }

        if (summary.quoteCount > 0) {
            Text(
                text = pluralStringResource(
                    DsR.plurals.library_folder_quote_count,
                    summary.quoteCount,
                    summary.quoteCount,
                ),
                style = typography.caption.copy(fontWeight = FontWeight.W600),
                color = colors.textTertiary,
            )
        }
    }
}
