package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.widget.WidgetSpecialCovers
import com.kovhan.domain.survey.SurveyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Marks the survey as finished and hands over the reward cover it earned.
 * [SurveyRewardOutcome.coverId] is null once every cover has been given away.
 */
class GrantSurveyRewardUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    suspend operator fun invoke(surveyId: String): SurveyRewardOutcome {
        repository.markCompleted(surveyId)
        val completed = repository.observeStatuses().first().completed.size
        return SurveyRewardOutcome(
            coverId = WidgetSpecialCovers.rewardFor(completed),
            completedSurveys = completed,
        )
    }
}

data class SurveyRewardOutcome(
    val coverId: String?,
    val completedSurveys: Int,
)
