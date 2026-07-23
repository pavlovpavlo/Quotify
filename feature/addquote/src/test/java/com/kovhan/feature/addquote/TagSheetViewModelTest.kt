package com.kovhan.feature.addquote

import app.cash.turbine.test
import com.kovhan.core.models.AiError
import com.kovhan.core.models.Outcome
import com.kovhan.domain.ai.AiAccess
import com.kovhan.domain.ai.AiDenialReason
import com.kovhan.domain.ai.use_case.CheckAiAccessUseCase
import com.kovhan.domain.ai.use_case.RecordAiRequestUseCase
import com.kovhan.domain.ai.use_case.SuggestTagsUseCase
import com.kovhan.domain.connectivity.use_case.CheckConnectivityUseCase
import com.kovhan.feature.addquote.presentation.details.component.tageditor.TagSheetEffect
import com.kovhan.feature.addquote.presentation.details.component.tageditor.TagSheetViewModel
import com.kovhan.feature.addquote.presentation.details.mvi.AiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("TagSheetViewModel AI generation")
class TagSheetViewModelTest {

    private val suggestTags: SuggestTagsUseCase = mockk()
    private val checkAiAccess: CheckAiAccessUseCase = mockk()
    private val recordAiRequest: RecordAiRequestUseCase = mockk(relaxed = true)
    private val checkConnectivity: CheckConnectivityUseCase = mockk()

    private fun viewModel() = TagSheetViewModel(
        suggestTags,
        checkAiAccess,
        recordAiRequest,
        checkConnectivity,
    ).apply { initialize("a quote", emptyList(), emptyList(), emptyList()) }

    @Test
    @DisplayName("offline shows the offline dialog and does not record a request")
    fun offlineShowsDialog() = runBlocking {
        coEvery { checkConnectivity() } returns false

        val vm = viewModel()
        vm.uiEffect.test {
            vm.onGenerateAiTags()
            assertEquals(TagSheetEffect.ShowOfflineDialog, awaitItem())
        }
        coVerify(exactly = 0) { recordAiRequest() }
    }

    @Test
    @DisplayName("a successful suggestion records the request and shows the tags")
    fun successRecords() = runBlocking {
        coEvery { checkConnectivity() } returns true
        coEvery { checkAiAccess() } returns AiAccess.Allowed
        coEvery { suggestTags("a quote") } returns Outcome.Success(listOf("hope", "time"))

        val vm = viewModel()
        vm.uiState.test {
            vm.onGenerateAiTags()
            var state = awaitItem()
            while (state.aiState != AiState.DONE) state = awaitItem()
            assertEquals(listOf("hope", "time"), state.aiTags)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(timeout = 2_000) { recordAiRequest() }
    }

    @Test
    @DisplayName("a quota denial shows the AI limit dialog, not the offline dialog")
    fun denialShowsLimitDialog() = runBlocking {
        coEvery { checkConnectivity() } returns true
        coEvery { checkAiAccess() } returns AiAccess.Denied(AiDenialReason.FREE_LIMIT_REACHED)

        val vm = viewModel()
        vm.uiEffect.test {
            vm.onGenerateAiTags()
            assertEquals(
                TagSheetEffect.ShowAiLimitDialog(AiDenialReason.FREE_LIMIT_REACHED),
                awaitItem(),
            )
        }
        coVerify(exactly = 0) { recordAiRequest() }
    }
}
