package com.kovhan.domain.widget.use_case.content

import com.kovhan.domain.widget.WidgetContentRepository
import com.kovhan.domain.widget.WidgetRedrawer
import javax.inject.Inject

class HandleWidgetQuoteRemovalUseCase @Inject constructor(
    private val repository: WidgetContentRepository,
    private val rebuildSnapshot: RebuildWidgetSnapshotUseCase,
    private val rotateWidgetQuote: RotateWidgetQuoteUseCase,
    private val redrawer: WidgetRedrawer,
) {
    suspend operator fun invoke(removedQuoteId: String) {
        if (repository.getCurrentQuoteId() != removedQuoteId) return
        rebuildSnapshot()
        rotateWidgetQuote()
        redrawer.redraw()
    }
}
