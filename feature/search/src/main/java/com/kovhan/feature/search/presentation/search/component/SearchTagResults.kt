package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.ui.component.emptystate.DefaultEmptyState
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.search.presentation.search.navigation.SearchScreenNavAction

@Composable
internal fun SearchTagResults(
    tags: List<SavedTag>,
    navAction: SearchScreenNavAction,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    if (tags.isEmpty()) {
        DefaultEmptyState()
        return
    }

    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = dimensions.size18,
                end = dimensions.size18,
                top = dimensions.size16,
                bottom = dimensions.size28,
            ),
        horizontalArrangement = Arrangement.spacedBy(dimensions.size8),
        verticalArrangement = Arrangement.spacedBy(dimensions.size8),
    ) {
        tags.forEach { tag ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensions.radiusFull))
                    .background(colors.bgElevated)
                    .border(
                        width = dimensions.size1,
                        color = colors.borderStrong,
                        shape = RoundedCornerShape(dimensions.radiusFull),
                    )
                    .clickable { navAction.openEntity(EntityType.TAG, tag.id, tag.name) }
                    .padding(horizontal = dimensions.size12, vertical = dimensions.size7),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimensions.size6),
            ) {
                Text(
                    text = "#",
                    style = typography.caption.copy(fontWeight = FontWeight.W700),
                    color = colors.accentPrimary,
                )
                Text(
                    text = tag.name,
                    style = typography.caption.copy(fontWeight = FontWeight.W600),
                    color = colors.textSecondary,
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(dimensions.radiusFull))
                        .background(colors.bgSecondary)
                        .padding(horizontal = dimensions.size6, vertical = dimensions.size1),
                ) {
                    Text(
                        text = (tag.quoteCount ?: 0).toString(),
                        style = typography.meta,
                        color = colors.textTertiary,
                    )
                }
            }
        }
    }
}
