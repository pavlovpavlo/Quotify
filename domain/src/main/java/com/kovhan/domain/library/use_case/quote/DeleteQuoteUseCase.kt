package com.kovhan.domain.library.use_case.quote

import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.widget.use_case.content.HandleWidgetQuoteRemovalUseCase
import javax.inject.Inject

class DeleteQuoteUseCase @Inject constructor(
    private val repository: QuoteRepository,
    private val handleWidgetQuoteRemoval: HandleWidgetQuoteRemovalUseCase,
) {
    suspend operator fun invoke(id: String) {
        repository.deleteById(id)
        handleWidgetQuoteRemoval(id)
    }
}
