package com.kovhan.feature.addquote.presentation.addquote.mvi

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.navigation.AddQuoteTab
import com.kovhan.core.navigation.models.AddQuoteEntryPoint

interface AddQuoteScreenIntent {
    fun onInitialTab(tab: AddQuoteTab, entryPoint: AddQuoteEntryPoint)
    fun onTabSelected(tab: AddQuoteTab)
    fun onQuoteChanged(value: TextFieldValue)
    fun onMicPressed()
    fun onMicReleased()
    fun onScanImagePicked(image: Uri)
    fun onScanCropConfirmed(image: Uri)
    fun onScanCropCancelled()
    fun onScanRetake()
    fun onScanRetry()
    fun onScanProceed(text: String)
    fun onNextClicked()
    fun onCloseClicked()
    fun onUpgradeClicked()
    fun onCameraPermissionResult(granted: Boolean)
    fun onMicrophonePermissionResult(granted: Boolean)

    companion object {
        val Empty: AddQuoteScreenIntent = object : AddQuoteScreenIntent {
            override fun onInitialTab(tab: AddQuoteTab, entryPoint: AddQuoteEntryPoint) = Unit
            override fun onTabSelected(tab: AddQuoteTab) = Unit
            override fun onQuoteChanged(value: TextFieldValue) = Unit
            override fun onMicPressed() = Unit
            override fun onMicReleased() = Unit
            override fun onScanImagePicked(image: Uri) = Unit
            override fun onScanCropConfirmed(image: Uri) = Unit
            override fun onScanCropCancelled() = Unit
            override fun onScanRetake() = Unit
            override fun onScanRetry() = Unit
            override fun onScanProceed(text: String) = Unit
            override fun onNextClicked() = Unit
            override fun onCloseClicked() = Unit
            override fun onUpgradeClicked() = Unit
            override fun onCameraPermissionResult(granted: Boolean) = Unit
            override fun onMicrophonePermissionResult(granted: Boolean) = Unit
        }
    }
}
