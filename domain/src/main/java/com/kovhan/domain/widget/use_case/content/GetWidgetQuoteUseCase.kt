package com.kovhan.domain.widget.use_case.content

import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuoteByIdUseCase
import com.kovhan.domain.widget.WidgetContentRepository
import com.kovhan.domain.widget.toWidgetQuote
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Returns the quote the widget should currently render (the one last picked by
 * [RotateWidgetQuoteUseCase]). Used by the widget's render pass.
 */
class GetWidgetQuoteUseCase @Inject constructor(
    private val repository: WidgetContentRepository,
    private val observeEnrichedById: ObserveEnrichedQuoteByIdUseCase,
) {
    suspend operator fun invoke(): WidgetQuote? {
        val id = repository.getCurrentQuoteId() ?: return null
        return observeEnrichedById(id).first()?.toWidgetQuote()
    }
}
