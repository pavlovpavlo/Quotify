package com.kovhan.data.feedback.repository

import com.kovhan.core.models.feedback.Feedback
import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.data.feedback.local.FeedbackLocalDataSource
import com.kovhan.data.feedback.remote.FeedbackRemoteDataSource
import com.kovhan.domain.feedback.FeedbackRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackRepositoryImpl @Inject constructor(
    private val remote: FeedbackRemoteDataSource,
    private val local: FeedbackLocalDataSource,
) : FeedbackRepository {

    override suspend fun submit(feedback: Feedback) {
        if (!remote.submit(feedback)) return
        local.markGiven(feedback.source)
        local.markSubmittedAt(System.currentTimeMillis())
    }

    override fun observeGiven(source: FeedbackSource): Flow<Boolean> = local.observeGiven(source)

    override suspend fun canSubmit(): Boolean =
        System.currentTimeMillis() - local.lastSubmittedAt() >= SUBMIT_INTERVAL_MS

    private companion object {
        const val SUBMIT_INTERVAL_MS = 24L * 60 * 60 * 1000
    }
}
