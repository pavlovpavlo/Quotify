package com.kovhan.domain.widget.use_case.content

import com.kovhan.domain.daily.use_case.EnsureDailyQuoteUseCase
import com.kovhan.domain.widget.WidgetContentRepository
import com.kovhan.domain.widget.use_case.settings.ObserveWidgetSettingsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Recomputes the persisted widget snapshot from the current source once.
 * Call on triggers: app start, widget enabled, source changed, after a remote
 * library refresh. Safe to run from a background worker.
 */
class RebuildWidgetSnapshotUseCase @Inject constructor(
    private val observeSettings: ObserveWidgetSettingsUseCase,
    private val ensureDailyQuote: EnsureDailyQuoteUseCase,
    private val resolveIds: ResolveWidgetQuoteIdsUseCase,
    private val repository: WidgetContentRepository,
) {
    suspend operator fun invoke() {
        if (observeSettings().first().includeDailyQuote) {
            runCatching { ensureDailyQuote() }
        }
        repository.replaceSnapshot(resolveIds().first())
    }
}
