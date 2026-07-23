package com.kovhan.feature.main.presentation.quotes.navigation

import androidx.compose.runtime.Stable

@Stable
interface QuotesScreenNavAction {
    fun onBack()
    fun navigateToQuoteDetails(quoteId: String)
    fun showHideDailyQuoteDialog()
    fun openSearch()
    fun openFolder(collectionId: String)
    fun createFolder()

    companion object {
        val Empty: QuotesScreenNavAction = EmptyQuotesScreenNavAction
    }
}

private object EmptyQuotesScreenNavAction : QuotesScreenNavAction {
    override fun onBack() = Unit
    override fun navigateToQuoteDetails(quoteId: String) = Unit
    override fun showHideDailyQuoteDialog() = Unit
    override fun openSearch() = Unit
    override fun openFolder(collectionId: String) = Unit
    override fun createFolder() = Unit
}