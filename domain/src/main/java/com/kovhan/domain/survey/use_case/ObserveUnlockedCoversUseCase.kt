package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.widget.WidgetSpecialCovers
import com.kovhan.domain.survey.SurveyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Reward covers the user has earned — one per finished survey. */
class ObserveUnlockedCoversUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    operator fun invoke(): Flow<List<String>> = repository.observeStatuses()
        .map { statuses -> WidgetSpecialCovers.unlocked(statuses.completed.size) }
}
