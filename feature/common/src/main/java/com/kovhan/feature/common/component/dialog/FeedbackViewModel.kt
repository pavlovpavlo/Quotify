package com.kovhan.feature.common.component.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.domain.feedback.use_case.SubmitFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val submitFeedback: SubmitFeedbackUseCase,
) : ViewModel() {

    fun submit(liked: Boolean, comment: String, source: FeedbackSource) {
        viewModelScope.launch { submitFeedback(liked, comment, source) }
    }
}
