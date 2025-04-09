package com.kovhan.feature.main.presentation.favorites.navigation

class FavoritesScreenNavAction(
    val navigateBack: () -> Unit = { },
    val navigateToQuoteDetails: (String) -> Unit = { }
) 