package com.kovhan.domain.review.use_case

import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.review.ReviewPrompt
import com.kovhan.domain.review.ReviewPromptRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShouldAskForReviewUseCase @Inject constructor(
    private val repository: ReviewPromptRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(now: Long = System.currentTimeMillis()): Boolean {
        if (repository.isAsked()) return false

        val installedAt = repository.installedAt().takeIf { it > 0L } ?: return false
        val daysInstalled = TimeUnit.MILLISECONDS.toDays(now - installedAt)
        if (daysInstalled < ReviewPrompt.MIN_DAYS_INSTALLED) return false

        return quoteRepository.count() > 0
    }
}
