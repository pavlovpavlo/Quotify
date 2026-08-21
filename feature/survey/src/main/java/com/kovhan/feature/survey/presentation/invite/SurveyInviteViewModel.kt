package com.kovhan.feature.survey.presentation.invite

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.SurveyInviteChoice
import com.kovhan.core.analytics.event.SurveyInviteAnswered
import com.kovhan.core.analytics.event.SurveyInviteShown
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.survey.use_case.GetSurveyUseCase
import com.kovhan.domain.survey.use_case.MarkSurveyPostponedUseCase
import com.kovhan.domain.survey.use_case.MarkSurveySkippedUseCase
import com.kovhan.feature.survey.presentation.invite.mvi.SurveyInviteEffect
import com.kovhan.feature.survey.presentation.invite.mvi.SurveyInviteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyInviteViewModel @Inject constructor(
    private val getSurvey: GetSurveyUseCase,
    private val markSkipped: MarkSurveySkippedUseCase,
    private val markPostponed: MarkSurveyPostponedUseCase,
    private val analytics: AnalyticsTracker,
) : BaseViewModel<SurveyInviteState, SurveyInviteEffect>(SurveyInviteState()) {

    private var surveyId: String? = null

    fun initialize(surveyId: String) {
        if (this.surveyId == surveyId) return
        this.surveyId = surveyId

        analytics.track(SurveyInviteShown(surveyId))
        viewModelScope.launch {
            val invite = getSurvey(surveyId)?.invite ?: return@launch
            publishState { copy(invite = invite) }
        }
    }

    fun onStart() = track(SurveyInviteChoice.START)

    fun onLater() {
        track(SurveyInviteChoice.LATER)
        surveyId?.let { id -> viewModelScope.launch { markPostponed(id) } }
    }

    fun onDismiss() {
        track(SurveyInviteChoice.DISMISS)
        surveyId?.let { id -> viewModelScope.launch { markSkipped(id) } }
    }

    private fun track(choice: SurveyInviteChoice) {
        val id = surveyId ?: return
        analytics.track(SurveyInviteAnswered(id, choice))
    }
}
