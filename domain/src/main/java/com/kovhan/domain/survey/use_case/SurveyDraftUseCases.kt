package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.survey.SurveyDraft
import com.kovhan.domain.survey.SurveyRepository
import javax.inject.Inject

class SaveSurveyDraftUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    suspend operator fun invoke(draft: SurveyDraft) = repository.saveDraft(draft)
}

class GetSurveyDraftUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    suspend operator fun invoke(surveyId: String): SurveyDraft? = repository.draft(surveyId)
}

class ClearSurveyDraftUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    suspend operator fun invoke(surveyId: String) = repository.clearDraft(surveyId)
}
