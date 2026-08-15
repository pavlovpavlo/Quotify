package com.kovhan.domain.review.use_case

import com.kovhan.domain.review.ReviewPromptRepository
import javax.inject.Inject

class MarkReviewAskedUseCase @Inject constructor(
    private val repository: ReviewPromptRepository,
) {
    suspend operator fun invoke() = repository.markAsked()
}
