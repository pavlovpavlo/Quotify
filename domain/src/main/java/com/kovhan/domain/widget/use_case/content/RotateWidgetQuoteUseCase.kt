package com.kovhan.domain.widget.use_case.content

import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.domain.widget.WidgetContentRepository
import javax.inject.Inject

/**
 * Advances the widget to the next quote and returns it. Picks a random quote
 * from the snapshot that has not been shown yet; once every quote has been
 * shown the seen set resets and a fresh cycle starts (no repeats until
 * exhausted — mirrors the daily-quote selection logic).
 *
 * Invoked by the background rotation task (WorkManager) on the configured
 * frequency, and on demand when the source snapshot changes.
 */
class RotateWidgetQuoteUseCase @Inject constructor(
    private val repository: WidgetContentRepository,
    private val resolveQuote: ResolveWidgetQuoteUseCase,
) {
    suspend operator fun invoke(): WidgetQuote? {
        val snapshot = repository.getSnapshotIds()
        if (snapshot.isEmpty()) {
            repository.setCurrentQuoteId(null)
            return null
        }

        val seen = repository.getSeenIds().toSet()
        val pool = snapshot.filterNot { it in seen }.ifEmpty {
            repository.resetSeen()
            snapshot
        }

        val next = pool.random()
        repository.markSeen(next)
        repository.setCurrentQuoteId(next)
        return resolveQuote(next)
    }
}
