package com.kovhan.feature.main.presentation.quotes.mvi

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.ui.UiState
import com.kovhan.domain.settings.AppLanguage

data class QuotesScreenState(
    val isDailyQuoteLoading: Boolean = true,
    val areFoldersLoading: Boolean = true,
    val dailyQuote: DailyQuote? = null,
    val displayText: String = "",
    val displayAuthor: String? = null,
    val displayBook: String? = null,
    val language: AppLanguage = AppLanguage.default,
    val isDailyQuoteFavourite: Boolean = false,
    val isDailyQuoteFavouriteLoading: Boolean = false,
    val folders: List<SavedCollection> = emptyList(),
) : UiState {
    val isLoading: Boolean
        get() = isDailyQuoteLoading || areFoldersLoading
}
