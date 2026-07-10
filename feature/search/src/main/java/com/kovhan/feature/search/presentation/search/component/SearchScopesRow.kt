package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.search.presentation.search.mvi.SearchScope

internal data class SearchScopeCounts(
    val quotes: Int,
    val folders: Int,
    val tags: Int,
    val books: Int,
    val authors: Int,
)

@Composable
internal fun SearchScopesRow(
    scope: SearchScope,
    onScopeChange: (SearchScope) -> Unit,
    counts: SearchScopeCounts,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(
                start = dimensions.size16,
                end = dimensions.size16,
                top = dimensions.size4,
                bottom = dimensions.size12,
            ),
        horizontalArrangement = Arrangement.spacedBy(dimensions.size8),
    ) {
        SearchScopeChip(
            label = stringResource(DsR.string.search_scope_quotes),
            count = counts.quotes,
            active = scope == SearchScope.QUOTES,
            onClick = { onScopeChange(SearchScope.QUOTES) },
        )
        SearchScopeChip(
            label = stringResource(DsR.string.search_scope_folders),
            count = counts.folders,
            active = scope == SearchScope.FOLDERS,
            onClick = { onScopeChange(SearchScope.FOLDERS) },
        )
        SearchScopeChip(
            label = stringResource(DsR.string.search_scope_tags),
            count = counts.tags,
            active = scope == SearchScope.TAGS,
            onClick = { onScopeChange(SearchScope.TAGS) },
        )
        SearchScopeChip(
            label = stringResource(DsR.string.search_scope_books),
            count = counts.books,
            active = scope == SearchScope.BOOKS,
            onClick = { onScopeChange(SearchScope.BOOKS) },
        )
        SearchScopeChip(
            label = stringResource(DsR.string.search_scope_authors),
            count = counts.authors,
            active = scope == SearchScope.AUTHORS,
            onClick = { onScopeChange(SearchScope.AUTHORS) },
        )
    }
}

@Composable
private fun SearchScopeChip(
    label: String,
    count: Int,
    active: Boolean,
    onClick: () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
    val shape = RoundedCornerShape(dimensions.radiusFull)

    Row(
        modifier = Modifier
            .height(34.dp)
            .clip(shape)
            .background(if (active) colors.accentPrimary else colors.bgElevated)
            .border(
                width = dimensions.size1,
                color = if (active) colors.accentPrimary else colors.borderStrong,
                shape = shape,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = dimensions.size13),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size6),
    ) {
        Text(
            text = label,
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = if (active) colors.textOnAccent else colors.textSecondary,
        )

        Box(
            modifier = Modifier
                .clip(shape)
                .background(
                    if (active) {
                        Color.White.copy(alpha = 0.22f)
                    } else {
                        colors.bgSecondary
                    },
                )
                .padding(horizontal = dimensions.size6, vertical = dimensions.size1),
        ) {
            Text(
                text = count.toString(),
                style = typography.meta,
                color = if (active) colors.textOnAccent else colors.textTertiary,
            )
        }
    }
}
