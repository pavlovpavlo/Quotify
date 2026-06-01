package com.kovhan.feature.main.presentation.favorites.navigation

import androidx.compose.runtime.Stable

@Stable
interface FavoritesScreenNavAction {
    fun navigateBack()
    fun navigateToQuoteDetails(quoteId: String)

    companion object {
        val Empty: FavoritesScreenNavAction = EmptyFavoritesScreenNavAction
    }
}

private object EmptyFavoritesScreenNavAction : FavoritesScreenNavAction {
    override fun navigateBack() = Unit
    override fun navigateToQuoteDetails(quoteId: String) = Unit
} 