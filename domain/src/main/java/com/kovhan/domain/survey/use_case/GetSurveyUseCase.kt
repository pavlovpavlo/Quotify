package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.survey.Survey
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.survey.SurveyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetSurveyUseCase @Inject constructor(
    private val repository: SurveyRepository,
    private val getLanguage: GetLanguageUseCase,
) {
    suspend operator fun invoke(surveyId: String): Survey? =
        repository.survey(surveyId, getLanguage().first().tag)
}
