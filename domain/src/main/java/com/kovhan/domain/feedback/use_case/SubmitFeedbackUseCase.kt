package com.kovhan.domain.feedback.use_case

import com.kovhan.core.models.feedback.Feedback
import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.domain.feedback.FeedbackRepository
import javax.inject.Inject

class SubmitFeedbackUseCase @Inject constructor(
    private val repository: FeedbackRepository,
) {
    suspend operator fun invoke(liked: Boolean, comment: String, source: FeedbackSource) =
        repository.submit(
            Feedback(
                liked = liked,
                comment = comment.trim(),
                source = source,
            ),
        )
}
