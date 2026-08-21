package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.survey.Survey
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.survey.SurveyRepository
import com.kovhan.domain.survey.SurveyRules
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * The survey to offer on app entry — the first configured one the user has
 * neither finished nor turned down, skipping anything postponed less than
 * [SurveyRules.POSTPONE_COOLDOWN_DAYS] ago.
 */
class ResolveSurveyInviteUseCase @Inject constructor(
    private val repository: SurveyRepository,
    private val getLanguage: GetLanguageUseCase,
) {
    suspend operator fun invoke(now: Long = System.currentTimeMillis()): Survey? {
        val language = getLanguage().first().tag
        val surveys = repository.surveys(language)
        if (surveys.isEmpty()) {
            Timber.d("Survey invite: config has no usable surveys")
            return null
        }

        val statuses = repository.statuses()
        val cooldown = TimeUnit.DAYS.toMillis(SurveyRules.POSTPONE_COOLDOWN_DAYS)

        val invite = surveys.firstOrNull { survey ->
            !statuses.isSettled(survey.id) &&
                !statuses.isPostponedWithin(survey.id, cooldown, now)
        }

        Timber.d(
            "Survey invite: %s of %s, completed=%s skipped=%s postponed=%s",
            invite?.id,
            surveys.map { it.id },
            statuses.completed,
            statuses.skipped,
            statuses.postponed.keys,
        )
        return invite
    }
}
