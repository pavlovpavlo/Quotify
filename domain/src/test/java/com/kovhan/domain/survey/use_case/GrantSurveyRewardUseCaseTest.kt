package com.kovhan.domain.survey.use_case

import com.kovhan.core.models.survey.SurveyStatuses
import com.kovhan.domain.survey.SurveyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GrantSurveyRewardUseCase")
class GrantSurveyRewardUseCaseTest {

    private val repository = mockk<SurveyRepository>(relaxed = true)
    private val useCase = GrantSurveyRewardUseCase(repository)

    private fun completedAfterwards(vararg ids: String) {
        coEvery { repository.markCompleted(any()) } returns Unit
        every { repository.observeStatuses() } returns flowOf(SurveyStatuses(completed = ids.toSet()))
    }

    @Test
    @DisplayName("marks the survey done and grants the next cover")
    fun grantsNextCover() = runTest {
        completedAfterwards("quotify_v1")

        val outcome = useCase("quotify_v1")

        coVerify { repository.markCompleted("quotify_v1") }
        assertEquals("special_1", outcome.coverId)
        assertEquals(1, outcome.completedSurveys)
    }

    @Test
    @DisplayName("grants the cover matching the number of finished surveys")
    fun grantsByCount() = runTest {
        completedAfterwards("a", "b", "c")

        assertEquals("special_3", useCase("c").coverId)
    }

    @Test
    @DisplayName("reports no cover left once all are handed out")
    fun reportsDepleted() = runTest {
        completedAfterwards(*Array(11) { "survey_$it" })

        val outcome = useCase("survey_10")

        assertNull(outcome.coverId)
        assertEquals(11, outcome.completedSurveys)
    }
}
