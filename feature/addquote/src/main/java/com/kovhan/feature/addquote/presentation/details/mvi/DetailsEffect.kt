package com.kovhan.feature.addquote.presentation.details.mvi

import com.kovhan.core.ui.UiEffect

sealed class DetailsEffect : UiEffect {
    data object Back : DetailsEffect()
    data object Close : DetailsEffect()
    data object OpenTagSheet : DetailsEffect()
    data class ProceedToSave(val draft: QuoteDraft) : DetailsEffect()
}
