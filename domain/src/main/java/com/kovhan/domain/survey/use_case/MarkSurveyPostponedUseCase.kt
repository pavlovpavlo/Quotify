package com.kovhan.domain.survey.use_case

import com.kovhan.domain.survey.SurveyRepository
import javax.inject.Inject

class MarkSurveyPostponedUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    suspend operator fun invoke(surveyId: String) = repository.markPostponed(surveyId)
}
