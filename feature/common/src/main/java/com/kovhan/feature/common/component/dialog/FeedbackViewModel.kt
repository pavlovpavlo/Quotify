package com.kovhan.feature.common.component.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.FeedbackReason
import com.kovhan.core.analytics.FeedbackResult
import com.kovhan.core.analytics.event.FeedbackSubmitted
import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.domain.feedback.use_case.SubmitFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val submitFeedback: SubmitFeedbackUseCase,
    private val analytics: AnalyticsTracker,
) : ViewModel() {

    fun submit(liked: Boolean, comment: String, source: FeedbackSource) {
        analytics.track(
            FeedbackSubmitted(
                reason = source.toAnalytics(),
                result = if (liked) FeedbackResult.LIKE else FeedbackResult.DISLIKE,
                hasComment = comment.isNotBlank(),
            ),
        )
        viewModelScope.launch { submitFeedback(liked, comment, source) }
    }

    private fun FeedbackSource.toAnalytics(): FeedbackReason = when (this) {
        FeedbackSource.WIDGET -> FeedbackReason.WIDGET
        FeedbackSource.SETTINGS -> FeedbackReason.SETTINGS
        FeedbackSource.GENERAL -> FeedbackReason.FIRST_QUOTE
    }
}
