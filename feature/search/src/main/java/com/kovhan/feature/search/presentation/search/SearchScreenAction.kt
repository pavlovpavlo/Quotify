package com.kovhan.feature.search.presentation.search

import androidx.compose.runtime.Stable
import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.feature.search.presentation.search.mvi.SearchScope

@Stable
interface SearchScreenAction {
    fun onQueryChange(query: String)
    fun onScopeChange(scope: SearchScope)
    fun onQuoteMenuToggled(quoteId: String)
    fun onQuoteMenuDismissed()
    fun onEditQuoteRequested(quote: EnrichedQuote)
    fun onMoveQuoteRequested(quoteId: String)
    fun onDeleteQuoteRequested(quoteId: String)

    companion object {
        val Empty: SearchScreenAction = EmptySearchScreenAction
    }
}

private object EmptySearchScreenAction : SearchScreenAction {
    override fun onQueryChange(query: String) = Unit
    override fun onScopeChange(scope: SearchScope) = Unit
    override fun onQuoteMenuToggled(quoteId: String) = Unit
    override fun onQuoteMenuDismissed() = Unit
    override fun onEditQuoteRequested(quote: EnrichedQuote) = Unit
    override fun onMoveQuoteRequested(quoteId: String) = Unit
    override fun onDeleteQuoteRequested(quoteId: String) = Unit
}
