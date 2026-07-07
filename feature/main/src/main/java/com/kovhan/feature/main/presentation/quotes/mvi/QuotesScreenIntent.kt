package com.kovhan.feature.main.presentation.quotes.mvi

interface QuotesScreenIntent {
    fun onToggleDailyQuoteFavourite(favouritesName: String)
    fun onHideDailyQuoteForever()
    fun onHideDailyQuoteToday()
}

class DefaultQuotesScreenIntent : QuotesScreenIntent {
    override fun onToggleDailyQuoteFavourite(favouritesName: String) = Unit
    override fun onHideDailyQuoteForever() = Unit
    override fun onHideDailyQuoteToday() = Unit
}
