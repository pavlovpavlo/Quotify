package com.kovhan.domain.widget.use_case.content

import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuoteByIdUseCase
import com.kovhan.domain.widget.WidgetContentRepository
import com.kovhan.domain.widget.toWidgetQuote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Reactive current widget quote — for the in-app style preview (§5) and any
 * live widget host, so the shown quote follows edits to its source data.
 */
class ObserveWidgetQuoteUseCase @Inject constructor(
    private val repository: WidgetContentRepository,
    private val observeEnrichedById: ObserveEnrichedQuoteByIdUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<WidgetQuote?> =
        repository.observeCurrentQuoteId().flatMapLatest { id ->
            if (id == null) flowOf(null)
            else observeEnrichedById(id).map { it?.toWidgetQuote() }
        }
}
