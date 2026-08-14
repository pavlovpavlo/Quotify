package com.kovhan.feature.main.presentation.quotes.mvi

import com.kovhan.core.ui.UiEffect

sealed class QuotesScreenEffect : UiEffect {
    data object None : QuotesScreenEffect()

    /** Безкоштовний план вичерпав ліміт збережених цитат. */
    data object OpenPaywall : QuotesScreenEffect()
} 