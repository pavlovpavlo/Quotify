package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.survey.SurveyAnswer
import com.kovhan.core.models.survey.SurveyResponse
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.survey.SurveyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SubmitSurveyResponseUseCase @Inject constructor(
    private val repository: SurveyRepository,
    private val getLanguage: GetLanguageUseCase,
) {
    suspend operator fun invoke(
        surveyId: String,
        answers: List<SurveyAnswer>,
        completed: Boolean,
    ) = repository.submitResponse(
        SurveyResponse(
            surveyId = surveyId,
            answers = answers,
            completed = completed,
            language = getLanguage().first().tag,
        ),
    )
}
