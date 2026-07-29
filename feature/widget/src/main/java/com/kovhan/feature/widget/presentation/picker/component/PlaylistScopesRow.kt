package com.kovhan.feature.widget.presentation.picker.component

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
import com.kovhan.core.models.LibrarySearchResults
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.picker.mvi.PickerScope

@Composable
internal fun PlaylistScopesRow(
    scope: PickerScope,
    onScopeChange: (PickerScope) -> Unit,
    results: LibrarySearchResults,
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
        ScopeChip(DsR.string.search_scope_quotes, results.quotes.size, scope == PickerScope.QUOTES) {
            onScopeChange(PickerScope.QUOTES)
        }
        ScopeChip(DsR.string.search_scope_folders, results.folders.size, scope == PickerScope.FOLDERS) {
            onScopeChange(PickerScope.FOLDERS)
        }
        ScopeChip(DsR.string.search_scope_tags, results.tags.size, scope == PickerScope.TAGS) {
            onScopeChange(PickerScope.TAGS)
        }
        ScopeChip(DsR.string.search_scope_books, results.books.size, scope == PickerScope.BOOKS) {
            onScopeChange(PickerScope.BOOKS)
        }
        ScopeChip(DsR.string.search_scope_authors, results.authors.size, scope == PickerScope.AUTHORS) {
            onScopeChange(PickerScope.AUTHORS)
        }
    }
}

@Composable
private fun ScopeChip(
    labelRes: Int,
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
            text = stringResource(labelRes),
            style = typography.caption.copy(fontWeight = FontWeight.W600),
            color = if (active) colors.textOnAccent else colors.textSecondary,
        )
        Box(
            modifier = Modifier
                .clip(shape)
                .background(if (active) Color.White.copy(alpha = 0.22f) else colors.bgSecondary)
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
