package com.kovhan.domain.widget.use_case.content

import com.kovhan.domain.widget.WidgetContentRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Recomputes the persisted widget snapshot from the current source once.
 * Call on triggers: app start, widget enabled, source changed, after a remote
 * library refresh. Safe to run from a background worker.
 */
class RebuildWidgetSnapshotUseCase @Inject constructor(
    private val resolveIds: ResolveWidgetQuoteIdsUseCase,
    private val repository: WidgetContentRepository,
) {
    suspend operator fun invoke() {
        repository.replaceSnapshot(resolveIds().first())
    }
}
