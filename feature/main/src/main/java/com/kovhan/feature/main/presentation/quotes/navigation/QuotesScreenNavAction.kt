package com.kovhan.feature.main.presentation.quotes.navigation

import androidx.compose.runtime.Stable

@Stable
interface QuotesScreenNavAction {
    fun onBack()
    fun navigateToQuoteDetails(quoteId: String)

    companion object {
        val Empty: QuotesScreenNavAction = EmptyQuotesScreenNavAction
    }
}

private object EmptyQuotesScreenNavAction : QuotesScreenNavAction {
    override fun onBack() = Unit
    override fun navigateToQuoteDetails(quoteId: String) = Unit
} 