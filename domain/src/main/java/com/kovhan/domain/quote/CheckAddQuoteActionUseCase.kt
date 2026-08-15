package com.kovhan.domain.quote

import com.kovhan.core.models.quote.AddQuoteAction
import com.kovhan.domain.library.QuoteRepository
import javax.inject.Inject


class CheckAddQuoteActionUseCase @Inject constructor(
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(isWidgetPlaced: Boolean): AddQuoteAction {
        val count = quoteRepository.countWithoutFavourite()

        return when {
            count == 1 -> AddQuoteAction.Feedback
            count == 3 && !isWidgetPlaced -> AddQuoteAction.Widget
            else -> AddQuoteAction.None
        }
    }
}
