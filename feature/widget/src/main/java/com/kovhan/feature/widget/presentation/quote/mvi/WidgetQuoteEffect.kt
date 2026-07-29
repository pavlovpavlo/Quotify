package com.kovhan.feature.widget.presentation.quote.mvi

import com.kovhan.core.navigation.QuoteEditDraft
import com.kovhan.core.ui.UiEffect

sealed interface WidgetQuoteEffect : UiEffect {
    data class OpenEditSheet(
        val draft: QuoteEditDraft,
        val authorOptions: List<String>,
        val bookOptions: List<String>,
        val tagPool: List<String>,
    ) : WidgetQuoteEffect

    data class OpenDeleteDialog(val quoteId: String) : WidgetQuoteEffect

    data object Edited : WidgetQuoteEffect

    data object Deleted : WidgetQuoteEffect
}
