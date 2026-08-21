package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.survey.Survey
import com.kovhan.core.models.survey.SurveyStatuses
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.survey.SurveyRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

@DisplayName("ResolveSurveyInviteUseCase")
class ResolveSurveyInviteUseCaseTest {

    private val repository = mockk<SurveyRepository>()
    private val getLanguage = mockk<GetLanguageUseCase>()
    private val useCase = ResolveSurveyInviteUseCase(repository, getLanguage)

    private val now = 1_700_000_000_000L

    private fun given(surveys: List<Survey>, statuses: SurveyStatuses) {
        every { getLanguage() } returns flowOf(AppLanguage.UKRAINIAN)
        coEvery { repository.surveys("uk") } returns surveys
        coEvery { repository.statuses() } returns statuses
    }

    @Test
    @DisplayName("offers the first survey the user has not settled")
    fun offersFirstPending() = runTest {
        given(
            surveys = listOf(Survey("done"), Survey("next"), Survey("later")),
            statuses = SurveyStatuses(completed = setOf("done")),
        )

        assertEquals("next", useCase(now)?.id)
    }

    @Test
    @DisplayName("skips surveys the user turned down")
    fun skipsDismissed() = runTest {
        given(
            surveys = listOf(Survey("dismissed"), Survey("fresh")),
            statuses = SurveyStatuses(skipped = setOf("dismissed")),
        )

        assertEquals("fresh", useCase(now)?.id)
    }

    @Test
    @DisplayName("holds back a survey postponed within the cooldown")
    fun holdsBackPostponed() = runTest {
        given(
            surveys = listOf(Survey("postponed")),
            statuses = SurveyStatuses(postponed = mapOf("postponed" to now - TimeUnit.DAYS.toMillis(2))),
        )

        assertNull(useCase(now))
    }

    @Test
    @DisplayName("offers a postponed survey again once the cooldown passed")
    fun offersAfterCooldown() = runTest {
        given(
            surveys = listOf(Survey("postponed")),
            statuses = SurveyStatuses(postponed = mapOf("postponed" to now - TimeUnit.DAYS.toMillis(8))),
        )

        assertEquals("postponed", useCase(now)?.id)
    }

    @Test
    @DisplayName("offers nothing when the config is empty")
    fun emptyConfig() = runTest {
        given(surveys = emptyList(), statuses = SurveyStatuses())

        assertNull(useCase(now))
    }
}
