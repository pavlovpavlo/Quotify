package com.kovhan.feature.addquote.presentation.addquote.mvi

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.navigation.AddQuoteTab
import com.kovhan.core.ui.UiState
import com.kovhan.domain.ai.AiDenialReason

data class AddQuoteScreenState(
    val selectedTab: AddQuoteTab = AddQuoteTab.TEXT,
    val quote: TextFieldValue = TextFieldValue(),
    val isRecording: Boolean = false,
    val isVoiceAvailable: Boolean = false,
    val pickedImage: Uri? = null,
    val scanLines: List<String> = emptyList(),
    val isScanning: Boolean = false,
    val scanNoTextFound: Boolean = false,
    val scanAiDenial: AiDenialReason? = null,
    val scanOffline: Boolean = false,
) : UiState {
    val canProceed: Boolean
        get() = selectedTab != AddQuoteTab.SCAN && quote.text.isNotBlank()

    val scanMode: ScanMode
        get() = when {
            isScanning -> ScanMode.SCANNING
            scanLines.isNotEmpty() -> ScanMode.SELECT
            pickedImage != null -> ScanMode.CROP
            else -> ScanMode.LIVE
        }
}

enum class ScanMode { LIVE, CROP, SCANNING, SELECT }
