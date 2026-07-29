package com.kovhan.domain.widget.use_case.content

import com.kovhan.domain.widget.WidgetContentRepository
import javax.inject.Inject

/**
 * When the quote currently shown on the widget is deleted, rebuild the snapshot
 * (dropping it) and rotate to the next quote. No-op if the removed quote isn't
 * the one on the widget.
 */
class HandleWidgetQuoteRemovalUseCase @Inject constructor(
    private val repository: WidgetContentRepository,
    private val rebuildSnapshot: RebuildWidgetSnapshotUseCase,
    private val rotateWidgetQuote: RotateWidgetQuoteUseCase,
) {
    suspend operator fun invoke(removedQuoteId: String) {
        if (repository.getCurrentQuoteId() != removedQuoteId) return
        rebuildSnapshot()
        rotateWidgetQuote()
    }
}
