package com.kovhan.domain.survey.use_case

import com.kovhan.domain.survey.SurveyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Id of a survey the user pushed back with "Later" — the profile plate keeps it
 * reachable until it is finished or turned down.
 */
class ObservePostponedSurveyUseCase @Inject constructor(
    private val repository: SurveyRepository,
) {
    operator fun invoke(): Flow<String?> = repository.observeStatuses().map { statuses ->
        statuses.postponed.keys.firstOrNull { !statuses.isSettled(it) }
    }
}
