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
import com.kovhan.core.ui.component.quote.QuoteActionCard
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.search.presentation.search.SearchScreenAction

@Composable
internal fun SearchQuoteResults(
    quotes: List<EnrichedQuote>,
    query: String,
    expandedMenuId: String?,
    action: SearchScreenAction,
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
            QuoteActionCard(
                quote = quote,
                highlightQuery = query.ifEmpty { null },
                menuExpanded = expandedMenuId == quote.id,
                onToggleMenu = { action.onQuoteMenuToggled(quote.id) },
                onDismissMenu = action::onQuoteMenuDismissed,
                onEdit = { action.onEditQuoteRequested(quote) },
                onMove = { action.onMoveQuoteRequested(quote.id) },
                onDelete = { action.onDeleteQuoteRequested(quote.id) },
            )
        }
    }
}
