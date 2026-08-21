package com.kovhan.domain.survey

import com.kovhan.core.models.survey.Survey
import com.kovhan.core.models.survey.SurveyDraft
import com.kovhan.core.models.survey.SurveyResponse
import com.kovhan.core.models.survey.SurveyStatuses
import kotlinx.coroutines.flow.Flow

interface SurveyRepository {

    /** Valid, enabled surveys from Remote Config, resolved for [language], in config order. */
    suspend fun surveys(language: String): List<Survey>

    suspend fun survey(surveyId: String, language: String): Survey?

    /** Local statuses refreshed from the user profile when the network allows. */
    suspend fun statuses(): SurveyStatuses

    fun observeStatuses(): Flow<SurveyStatuses>

    suspend fun markCompleted(surveyId: String)

    suspend fun markSkipped(surveyId: String)

    suspend fun markPostponed(surveyId: String)

    suspend fun submitResponse(response: SurveyResponse)

    suspend fun saveDraft(draft: SurveyDraft)

    suspend fun draft(surveyId: String): SurveyDraft?

    suspend fun clearDraft(surveyId: String)

    suspend fun resetState()
}

object SurveyRules {
    /** How long "Later" keeps the invite away before it may be offered again. */
    const val POSTPONE_COOLDOWN_DAYS = 7L
}
