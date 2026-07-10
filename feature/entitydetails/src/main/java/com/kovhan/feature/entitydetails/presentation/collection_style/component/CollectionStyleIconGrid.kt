package com.kovhan.feature.entitydetails.presentation.collection_style.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.kovhan.core.ui.mapper.CollectionColorMapper
import com.kovhan.core.ui.mapper.CollectionIconMapper
import com.kovhan.design.systems.QuotifyMaterialTheme
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
internal fun CollectionStyleIconGrid(
    selectedIconId: String,
    selectedTone: String,
    onIconChange: (String) -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val tone = CollectionColorMapper.toColor(selectedTone, colors)
    val iconIds = remember { CollectionIconMapper.iconIds }

    Column(verticalArrangement = Arrangement.spacedBy(dimensions.size8)) {
        iconIds.chunked(6).forEach { rowIcons ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensions.size8),
            ) {
                rowIcons.forEach { iconId ->
                    val selected = iconId == selectedIconId
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(dimensions.radiusMd))
                            .background(if (selected) tone else colors.bgSecondary)
                            .clickable { onIconChange(iconId) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            modifier = Modifier.size(dimensions.size21),
                            painter = painterResource(CollectionIconMapper.toDrawableRes(iconId)),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                if (selected) colors.textOnAccent else colors.textSecondary,
                            ),
                        )
                    }
                }

                repeat(6 - rowIcons.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
