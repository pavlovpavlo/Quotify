package com.kovhan.feature.addquote

import android.net.Uri
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.models.AiError
import com.kovhan.core.models.Outcome
import com.kovhan.domain.ai.AiAccess
import com.kovhan.domain.ai.AiDenialReason
import com.kovhan.domain.ai.AiFeature
import com.kovhan.domain.ai.use_case.CheckAiAccessUseCase
import com.kovhan.domain.ai.use_case.RecordAiRequestUseCase
import com.kovhan.domain.billing.use_case.ObserveIsSubscribedUseCase
import com.kovhan.domain.connectivity.use_case.CheckConnectivityUseCase
import com.kovhan.domain.scan.model.RecognizedTextLine
import com.kovhan.domain.scan.use_case.RecognizeTextUseCase
import com.kovhan.domain.voice.use_case.IsVoiceInputAvailableUseCase
import com.kovhan.domain.voice.use_case.ObserveVoiceInputUseCase
import com.kovhan.feature.addquote.presentation.addquote.AddQuoteScreenViewModel
import com.kovhan.feature.addquote.presentation.addquote.mvi.AddQuoteScreenState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("AddQuoteScreenViewModel scan")
class AddQuoteScreenViewModelTest {

    private val isVoiceInputAvailable: IsVoiceInputAvailableUseCase = mockk()
    private val observeVoiceInput: ObserveVoiceInputUseCase = mockk(relaxed = true)
    private val recognizeText: RecognizeTextUseCase = mockk()
    private val checkAiAccess: CheckAiAccessUseCase = mockk(relaxed = true)
    private val recordAiRequest: RecordAiRequestUseCase = mockk(relaxed = true)
    private val checkConnectivity: CheckConnectivityUseCase = mockk()
    private val observeIsSubscribed: ObserveIsSubscribedUseCase = mockk()
    private val analytics: AnalyticsTracker = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        every { isVoiceInputAvailable() } returns false
        every { observeIsSubscribed() } returns flowOf(false)
    }

    private fun viewModel() = AddQuoteScreenViewModel(
        isVoiceInputAvailable,
        observeVoiceInput,
        recognizeText,
        checkAiAccess,
        recordAiRequest,
        checkConnectivity,
        observeIsSubscribed,
        analytics,
    )

    @Test
    @DisplayName("successful recognition records the AI request and shows the lines")
    fun successRecords() = runBlocking {
        val uri = mockk<Uri>()
        coEvery { checkAiAccess(AiFeature.SCAN) } returns AiAccess.Allowed
        coEvery { recognizeText(uri) } returns Outcome.Success(listOf(RecognizedTextLine("hello")))

        val vm = viewModel()
        vm.uiState.test {
            vm.onScanCropConfirmed(uri)
            val state = awaitState { it.scanLines.isNotEmpty() }
            assertEquals(listOf("hello"), state.scanLines)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(timeout = 2_000) { recordAiRequest() }
    }

    @Test
    @DisplayName("offline recognition flags the offline prompt and does not record a request")
    fun offlineDoesNotRecord() = runBlocking {
        val uri = mockk<Uri>()
        coEvery { checkAiAccess(AiFeature.SCAN) } returns AiAccess.Allowed
        coEvery { recognizeText(uri) } returns Outcome.Failure(AiError.Offline)

        val vm = viewModel()
        vm.uiState.test {
            vm.onScanCropConfirmed(uri)
            val state = awaitState { it.scanOffline }
            assertTrue(state.scanOffline)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 0) { recordAiRequest() }
    }

    @Test
    @DisplayName("a spent quota gates the confirmed crop instead of recognizing it")
    fun spentQuotaGatesCrop() = runBlocking {
        val uri = mockk<Uri>()
        coEvery { checkAiAccess(AiFeature.SCAN) } returns
            AiAccess.Denied(AiDenialReason.DAILY_LIMIT_REACHED)

        val vm = viewModel()
        vm.uiState.test {
            vm.onScanCropConfirmed(uri)
            val state = awaitState { it.scanAiDenial != null }
            assertEquals(AiDenialReason.DAILY_LIMIT_REACHED, state.scanAiDenial)
            assertTrue(!state.scanNoTextFound)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 0) { recognizeText(uri) }
    }

    @Test
    @DisplayName("recognition failure on a spent quota shows the limit card, not a scan error")
    fun failureOnSpentQuotaShowsLimit() = runBlocking {
        val uri = mockk<Uri>()
        coEvery { checkAiAccess(AiFeature.SCAN) } returnsMany listOf(
            AiAccess.Allowed,
            AiAccess.Denied(AiDenialReason.MONTHLY_LIMIT_REACHED),
        )
        coEvery { recognizeText(uri) } returns Outcome.Failure(AiError.Unknown)

        val vm = viewModel()
        vm.uiState.test {
            vm.onScanCropConfirmed(uri)
            val state = awaitState { it.scanAiDenial != null }
            assertEquals(AiDenialReason.MONTHLY_LIMIT_REACHED, state.scanAiDenial)
            assertTrue(!state.scanNoTextFound)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    @DisplayName("retry while offline shows the offline prompt")
    fun retryOffline() = runBlocking {
        coEvery { checkConnectivity() } returns false

        val vm = viewModel()
        vm.uiState.test {
            vm.onScanRetry()
            val state = awaitState { it.scanOffline }
            assertTrue(state.scanOffline)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private suspend fun ReceiveTurbine<AddQuoteScreenState>.awaitState(
        predicate: (AddQuoteScreenState) -> Boolean,
    ): AddQuoteScreenState {
        while (true) {
            val item = awaitItem()
            if (predicate(item)) return item
        }
    }
}
