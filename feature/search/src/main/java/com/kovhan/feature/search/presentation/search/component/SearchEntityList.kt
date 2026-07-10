package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun <T> SearchEntityList(
    items: List<T>,
    key: (T) -> String,
    rowContent: @Composable (T) -> Unit,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    if (items.isEmpty()) {
        SearchEmptyState()
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = dimensions.size18,
            end = dimensions.size18,
            top = dimensions.size16,
            bottom = dimensions.size28,
        ),
        verticalArrangement = Arrangement.spacedBy(dimensions.size8),
    ) {
        items(items = items, key = key) { item ->
            rowContent(item)
        }
    }
}
