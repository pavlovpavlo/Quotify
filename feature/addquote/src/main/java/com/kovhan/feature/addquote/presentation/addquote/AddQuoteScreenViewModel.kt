package com.kovhan.feature.addquote.presentation.addquote

import android.net.Uri
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.models.Outcome
import com.kovhan.core.navigation.AddQuoteTab
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.ai.AiAccess
import com.kovhan.domain.ai.AiFeature
import com.kovhan.domain.ai.use_case.CheckAiAccessUseCase
import com.kovhan.domain.ai.use_case.RecordAiRequestUseCase
import com.kovhan.domain.connectivity.use_case.CheckConnectivityUseCase
import com.kovhan.domain.scan.use_case.RecognizeTextUseCase
import com.kovhan.domain.voice.use_case.IsVoiceInputAvailableUseCase
import com.kovhan.domain.voice.use_case.ObserveVoiceInputUseCase
import com.kovhan.feature.addquote.presentation.addquote.mvi.AddQuoteScreenEffect
import com.kovhan.feature.addquote.presentation.addquote.mvi.AddQuoteScreenIntent
import com.kovhan.feature.addquote.presentation.addquote.mvi.AddQuoteScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuoteScreenViewModel @Inject constructor(
    private val isVoiceInputAvailable: IsVoiceInputAvailableUseCase,
    private val observeVoiceInput: ObserveVoiceInputUseCase,
    private val recognizeText: RecognizeTextUseCase,
    private val checkAiAccess: CheckAiAccessUseCase,
    private val recordAiRequest: RecordAiRequestUseCase,
    private val checkConnectivity: CheckConnectivityUseCase,
) : BaseViewModel<AddQuoteScreenState, AddQuoteScreenEffect>(AddQuoteScreenState()),
    AddQuoteScreenIntent {

    private var tabInitialized = false
    private var voiceBaseText = ""
    private var voiceJob: Job? = null

    init {
        publishState { copy(isVoiceAvailable = isVoiceInputAvailable()) }
    }

    override fun onInitialTab(tab: AddQuoteTab) {
        if (tabInitialized) return
        tabInitialized = true
        publishState { copy(selectedTab = tab) }
        if (tab == AddQuoteTab.SCAN) refreshScanGate()
    }

    override fun onTabSelected(tab: AddQuoteTab) {
        if (uiState.value.isRecording) onMicReleased()
        publishState { copy(selectedTab = tab) }
        if (tab == AddQuoteTab.SCAN) refreshScanGate()
    }

    override fun onScanRetry() = refreshScanGate()

    private fun refreshScanGate() {
        viewModelScope.launch {
            if (!checkConnectivity()) {
                publishState { copy(scanOffline = true, scanAiDenial = null) }
                return@launch
            }
            val denial = (checkAiAccess(AiFeature.SCAN) as? AiAccess.Denied)?.reason
            publishState { copy(scanOffline = false, scanAiDenial = denial) }
        }
    }

    override fun onQuoteChanged(value: TextFieldValue) = publishState { copy(quote = value) }

    override fun onMicPressed() {
        val current = uiState.value
        if (current.selectedTab != AddQuoteTab.VOICE || !current.isVoiceAvailable) return

        voiceBaseText = current.quote.text.trim()
        publishState { copy(isRecording = true) }

        voiceJob = viewModelScope.launch {
            observeVoiceInput().collect { recognized ->
                val combined = if (voiceBaseText.isBlank()) {
                    recognized
                } else {
                    "$voiceBaseText $recognized"
                }
                publishState { copy(quote = TextFieldValue(combined, TextRange(combined.length))) }
            }
        }
    }

    override fun onMicReleased() {
        if (!uiState.value.isRecording) return
        voiceJob?.cancel()
        voiceJob = null
        publishState { copy(isRecording = false) }
    }

    override fun onScanImagePicked(image: Uri) {
        viewModelScope.launch {
            when (val access = checkAiAccess(AiFeature.SCAN)) {
                is AiAccess.Denied -> publishState { copy(pickedImage = null, scanAiDenial = access.reason) }
                AiAccess.Allowed -> publishState { copy(scanAiDenial = null, pickedImage = image) }
            }
        }
    }

    override fun onScanCropConfirmed(image: Uri) {
        viewModelScope.launch {
            publishState {
                copy(
                    pickedImage = image,
                    isScanning = true,
                    scanLines = emptyList(),
                    scanNoTextFound = false,
                    scanOffline = false,
                )
            }
            when (val outcome = recognizeText(image)) {
                is Outcome.Success -> {
                    // Count the request only when the model actually ran.
                    recordAiRequest()
                    val lines = outcome.data.map { it.text }
                    publishState {
                        copy(
                            isScanning = false,
                            pickedImage = null,
                            scanLines = lines,
                            scanNoTextFound = lines.isEmpty(),
                        )
                    }
                }

                is Outcome.Failure -> publishState {
                    when (outcome.error) {
                        com.kovhan.core.models.AiError.Offline ->
                            copy(isScanning = false, pickedImage = null, scanOffline = true)

                        com.kovhan.core.models.AiError.Unknown ->
                            copy(isScanning = false, pickedImage = null, scanNoTextFound = true)
                    }
                }
            }
        }
    }

    override fun onScanCropCancelled() = publishState { copy(pickedImage = null) }

    override fun onScanRetake() = publishState {
        copy(
            isScanning = false,
            pickedImage = null,
            scanLines = emptyList(),
            scanNoTextFound = false,
            scanOffline = false,
        )
    }

    override fun onScanProceed(text: String) {
        val normalized = text.replace(WHITESPACE, " ").trim()
        if (normalized.isBlank()) return
        publishState { copy(quote = TextFieldValue(normalized)) }
        publishEffect(AddQuoteScreenEffect.ProceedToDetails(normalized))
    }

    override fun onNextClicked() {
        val current = uiState.value
        if (!current.canProceed) return
        publishEffect(AddQuoteScreenEffect.ProceedToDetails(current.quote.text.trim()))
    }

    override fun onCloseClicked() = publishEffect(AddQuoteScreenEffect.Close)

    override fun onUpgradeClicked() = publishEffect(AddQuoteScreenEffect.OpenPaywall)

    private companion object {
        val WHITESPACE = Regex("\\s+")
    }
}
