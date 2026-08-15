package com.kovhan.domain.feedback.use_case

import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.domain.feedback.FeedbackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFeedbackGivenUseCase @Inject constructor(
    private val repository: FeedbackRepository,
) {
    operator fun invoke(source: FeedbackSource): Flow<Boolean> = repository.observeGiven(source)
}
