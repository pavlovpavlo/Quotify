package com.kovhan.feature.widget.presentation.quote.mvi

import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.ui.UiState

data class WidgetQuoteState(
    val isLoading: Boolean = true,
    val quote: EnrichedQuote? = null,
    val menuVisible: Boolean = false,
    val isDaily: Boolean = false,
) : UiState
