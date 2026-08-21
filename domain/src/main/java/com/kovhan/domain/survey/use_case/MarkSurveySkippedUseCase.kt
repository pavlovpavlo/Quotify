package com.kovhan.domain.survey.use_case

import com.kovhan.domain.survey.SurveyRepository
import javax.inject.Inject

class MarkSurveySkippedUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    suspend operator fun invoke(surveyId: String) = repository.markSkipped(surveyId)
}
