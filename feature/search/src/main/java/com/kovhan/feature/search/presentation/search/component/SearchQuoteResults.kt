package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.ui.component.emptystate.DefaultEmptyState
import com.kovhan.core.ui.component.quote.ReadOnlyQuoteCard
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun SearchQuoteResults(
    quotes: List<EnrichedQuote>,
    query: String,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    if (quotes.isEmpty()) {
        DefaultEmptyState()
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
        verticalArrangement = Arrangement.spacedBy(dimensions.size12),
    ) {
        items(items = quotes, key = { it.id }) { quote ->
            ReadOnlyQuoteCard(
                quote = quote,
                highlightQuery = query.ifEmpty { null },
            )
        }
    }
}
