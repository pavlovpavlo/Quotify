package com.kovhan.feature.addquote.presentation.addquote.mvi

import com.kovhan.core.ui.UiEffect

sealed class AddQuoteScreenEffect : UiEffect {
    data object Close : AddQuoteScreenEffect()
    data class ProceedToDetails(val quote: String) : AddQuoteScreenEffect()
    data object OpenPaywall : AddQuoteScreenEffect()
}
