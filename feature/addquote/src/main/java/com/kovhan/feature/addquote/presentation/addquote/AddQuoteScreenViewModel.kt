package com.kovhan.feature.addquote.presentation.addquote

import android.net.Uri
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.analytics.AiFeatureName
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.AddQuoteSource
import com.kovhan.core.analytics.AddQuoteStep
import com.kovhan.core.analytics.CameraFailure
import com.kovhan.core.analytics.InputMethod
import com.kovhan.core.analytics.PermissionResult
import com.kovhan.core.analytics.PermissionType
import com.kovhan.core.analytics.VoiceFailure
import com.kovhan.core.analytics.event.AddQuoteInitiated
import com.kovhan.core.analytics.event.AiLimitReached
import com.kovhan.core.analytics.event.QuoteAddClosed
import com.kovhan.core.analytics.event.CameraFailed
import com.kovhan.core.analytics.event.PermissionResultEvent
import com.kovhan.core.analytics.event.ScanCompleted
import com.kovhan.core.analytics.event.ScanCompletedEdit
import com.kovhan.core.analytics.event.VoiceCompleted
import com.kovhan.core.analytics.event.VoiceCompletedEdit
import com.kovhan.core.analytics.event.VoiceFailed
import com.kovhan.core.models.Outcome
import com.kovhan.core.models.quote.QuoteLimits
import com.kovhan.core.navigation.AddQuoteTab
import com.kovhan.core.navigation.models.AddQuoteEntryPoint
import com.kovhan.core.navigation.models.QuoteInputMethod
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
    private val analytics: AnalyticsTracker,
) : BaseViewModel<AddQuoteScreenState, AddQuoteScreenEffect>(AddQuoteScreenState()),
    AddQuoteScreenIntent {

    private var tabInitialized = false
    private var voiceBaseText = ""
    private var voiceJob: Job? = null
    private var scanStartedAt = 0L
    private var recognizedText = ""
    private var dictatedText = ""
    private var source = AddQuoteSource.TAB

    init {
        publishState { copy(isVoiceAvailable = isVoiceInputAvailable()) }
    }

    override fun onInitialTab(tab: AddQuoteTab, entryPoint: AddQuoteEntryPoint) {
        if (tabInitialized) return
        tabInitialized = true
        source = when (entryPoint) {
            AddQuoteEntryPoint.TAB -> AddQuoteSource.TAB
            AddQuoteEntryPoint.EMPTY_COLLECTION -> AddQuoteSource.EMPTY_COLLECTION
        }
        analytics.track(AddQuoteInitiated(tab.toInputMethod(), source))
        publishState { copy(selectedTab = tab) }
        if (tab == AddQuoteTab.SCAN) refreshScanGate()
    }

    override fun onTabSelected(tab: AddQuoteTab) {
        if (uiState.value.isRecording) onMicReleased()
        if (tab != uiState.value.selectedTab) {
            analytics.track(AddQuoteInitiated(tab.toInputMethod(), source))
        }
        publishState { copy(selectedTab = tab) }
        if (tab == AddQuoteTab.SCAN) refreshScanGate()
    }

    private fun AddQuoteTab.toNavInputMethod(): QuoteInputMethod = when (this) {
        AddQuoteTab.TEXT -> QuoteInputMethod.TEXT
        AddQuoteTab.SCAN -> QuoteInputMethod.CAMERA
        AddQuoteTab.VOICE -> QuoteInputMethod.VOICE
    }

    private fun AddQuoteTab.toInputMethod(): InputMethod = when (this) {
        AddQuoteTab.TEXT -> InputMethod.TEXT
        AddQuoteTab.SCAN -> InputMethod.CAMERA
        AddQuoteTab.VOICE -> InputMethod.VOICE
    }

    override fun onScanRetry() = refreshScanGate()

    private fun refreshScanGate() {
        viewModelScope.launch {
            if (!checkConnectivity()) {
                publishState { copy(scanOffline = true, scanAiDenial = null) }
                return@launch
            }
            val denial = (checkAiAccess(AiFeature.SCAN) as? AiAccess.Denied)?.reason
            if (denial != null) analytics.track(AiLimitReached(AiFeatureName.CAMERA))
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
            runCatching {
                observeVoiceInput().collect { recognized ->
                    val combined = if (voiceBaseText.isBlank()) {
                        recognized
                    } else {
                        "$voiceBaseText $recognized"
                    }
                    dictatedText = QuoteLimits.normalizeText(combined)
                    publishState {
                        copy(
                            quote = TextFieldValue(
                                dictatedText,
                                TextRange(dictatedText.length),
                            ),
                        )
                    }
                }
            }.onFailure {
                analytics.track(VoiceFailed(VoiceFailure.RECOGNITION_FAILED))
                publishState { copy(isRecording = false) }
            }
        }
    }

    override fun onMicReleased() {
        if (!uiState.value.isRecording) return
        voiceJob?.cancel()
        voiceJob = null
        val recognized = uiState.value.quote.text.trim()
        if (recognized.length > voiceBaseText.length) {
            analytics.track(VoiceCompleted(recognized.length))
        } else {
            analytics.track(VoiceFailed(VoiceFailure.NO_SPEECH_DETECTED))
        }
        publishState { copy(isRecording = false) }
    }

    override fun onScanImagePicked(image: Uri) {
        viewModelScope.launch {
            when (val access = checkAiAccess(AiFeature.SCAN)) {
                is AiAccess.Denied -> {
                    analytics.track(AiLimitReached(AiFeatureName.CAMERA))
                    publishState { copy(pickedImage = null, scanAiDenial = access.reason) }
                }
                AiAccess.Allowed -> publishState { copy(scanAiDenial = null, pickedImage = image) }
            }
        }
    }

    override fun onScanCropConfirmed(image: Uri) {
        viewModelScope.launch {
            scanStartedAt = System.currentTimeMillis()
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
                    recognizedText = QuoteLimits.normalizeText(
                        lines.joinToString(" ").replace(WHITESPACE, " "),
                    )
                    if (lines.isEmpty()) {
                        analytics.track(CameraFailed(CameraFailure.NO_TEXT_DETECTED))
                    } else {
                        analytics.track(
                            ScanCompleted(
                                textLength = recognizedText.length,
                                durationSeconds = elapsedScanSeconds(),
                            ),
                        )
                    }
                    publishState {
                        copy(
                            isScanning = false,
                            pickedImage = null,
                            scanLines = lines,
                            scanNoTextFound = lines.isEmpty(),
                        )
                    }
                }

                is Outcome.Failure -> {
                    analytics.track(
                        CameraFailed(
                            when (outcome.error) {
                                com.kovhan.core.models.AiError.Offline -> CameraFailure.UNKNOWN
                                com.kovhan.core.models.AiError.Unknown ->
                                    CameraFailure.RECOGNITION_FAILED
                            },
                        ),
                    )
                    publishState {
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
        val normalized = QuoteLimits.normalizeText(text.replace(WHITESPACE, " "))
        if (normalized.isBlank()) return
        analytics.track(ScanCompletedEdit(normalized != recognizedText))
        publishState { copy(quote = TextFieldValue(normalized)) }
        publishEffect(AddQuoteScreenEffect.ProceedToDetails(normalized, QuoteInputMethod.CAMERA))
    }

    override fun onNextClicked() {
        val current = uiState.value
        if (!current.canProceed) return
        val text = current.quote.text.trim()
        if (current.selectedTab == AddQuoteTab.VOICE && dictatedText.isNotBlank()) {
            analytics.track(VoiceCompletedEdit(text != dictatedText.trim()))
        }
        publishEffect(
            AddQuoteScreenEffect.ProceedToDetails(text, current.selectedTab.toNavInputMethod()),
        )
    }

    private fun elapsedScanSeconds(): Long =
        (System.currentTimeMillis() - scanStartedAt).coerceAtLeast(0L) / 1000

    override fun onCloseClicked() {
        analytics.track(QuoteAddClosed(AddQuoteStep.QUOTE_INPUT))
        publishEffect(AddQuoteScreenEffect.Close)
    }

    override fun onUpgradeClicked() = publishEffect(AddQuoteScreenEffect.OpenPaywall)

    override fun onCameraPermissionResult(granted: Boolean) =
        trackPermission(PermissionType.CAMERA, granted)

    override fun onMicrophonePermissionResult(granted: Boolean) =
        trackPermission(PermissionType.MICROPHONE, granted)

    private fun trackPermission(permission: PermissionType, granted: Boolean) {
        analytics.track(
            PermissionResultEvent(
                permission = permission,
                result = if (granted) PermissionResult.GRANTED else PermissionResult.DENIED,
            ),
        )
        if (permission == PermissionType.CAMERA && !granted) {
            analytics.track(CameraFailed(CameraFailure.PERMISSION_DENIED))
        }
        if (permission == PermissionType.MICROPHONE && !granted) {
            analytics.track(VoiceFailed(VoiceFailure.PERMISSION_DENIED))
        }
    }

    private companion object {
        val WHITESPACE = Regex("\\s+")
    }
}
