package com.kovhan.feature.main.presentation.quotes.navigation

class QuotesScreenNavAction(
    val onBack: () -> Unit = { },
    val navigateToQuoteDetails: (String) -> Unit = { }
) 