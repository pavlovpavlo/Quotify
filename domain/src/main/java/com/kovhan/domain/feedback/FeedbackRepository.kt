package com.kovhan.domain.feedback

import com.kovhan.core.models.feedback.Feedback
import com.kovhan.core.models.feedback.FeedbackSource
import kotlinx.coroutines.flow.Flow

interface FeedbackRepository {

    suspend fun submit(feedback: Feedback)

    fun observeGiven(source: FeedbackSource): Flow<Boolean>

    suspend fun canSubmit(): Boolean
}
