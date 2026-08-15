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
        if (remote.submit(feedback)) local.markGiven(feedback.source)
    }

    override fun observeGiven(source: FeedbackSource): Flow<Boolean> = local.observeGiven(source)
}
