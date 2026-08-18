package com.kovhan.domain.feedback.use_case

import com.kovhan.domain.feedback.FeedbackRepository
import javax.inject.Inject

class CanSubmitFeedbackUseCase @Inject constructor(
    private val repository: FeedbackRepository,
) {
    suspend operator fun invoke(): Boolean = repository.canSubmit()
}
