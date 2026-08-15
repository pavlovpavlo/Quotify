package com.kovhan.domain.widget.use_case.content

import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.domain.widget.WidgetContentRepository
import javax.inject.Inject

/**
 * Returns the quote the widget should currently render (the one last picked by
 * [RotateWidgetQuoteUseCase]). Used by the widget's render pass.
 */
class GetWidgetQuoteUseCase @Inject constructor(
    private val repository: WidgetContentRepository,
    private val resolveQuote: ResolveWidgetQuoteUseCase,
) {
    suspend operator fun invoke(): WidgetQuote? {
        val ref = repository.getCurrentQuoteId() ?: return null
        return resolveQuote(ref)
    }
}
