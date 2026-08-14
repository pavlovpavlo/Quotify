package com.kovhan.feature.addquote.presentation.details.component.tageditor

import com.kovhan.core.ui.UiEffect
import com.kovhan.domain.ai.AiDenialReason

sealed class TagSheetEffect : UiEffect {
    data class ShowAiLimitDialog(val reason: AiDenialReason) : TagSheetEffect()

    data object ShowOfflineDialog : TagSheetEffect()

    /** Плашка з замком: безкоштовному плану AI-теги закриті цілком. */
    data object OpenPaywall : TagSheetEffect()
}
