package com.kovhan.feature.addquote.presentation.addquote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.core.navigation.AddQuoteTab
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.addquote.presentation.addquote.component.AddQuoteFooter
import com.kovhan.feature.addquote.presentation.addquote.component.AddQuoteTabs
import com.kovhan.feature.addquote.presentation.addquote.component.AddQuoteTopBar
import com.kovhan.feature.addquote.presentation.addquote.component.scan.AddQuoteScanTab
import com.kovhan.feature.addquote.presentation.addquote.component.text.AddQuoteTextTab
import com.kovhan.feature.addquote.presentation.addquote.component.voice.AddQuoteVoiceTab
import com.kovhan.feature.addquote.presentation.addquote.mvi.AddQuoteScreenIntent
import com.kovhan.feature.addquote.presentation.addquote.mvi.AddQuoteScreenState

@Composable
fun AddQuoteScreen(
    state: AddQuoteScreenState,
    intent: AddQuoteScreenIntent,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            )
            .imePadding(),
    ) {
        AddQuoteTopBar(onClose = intent::onCloseClicked)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            AddQuoteTabs(
                modifier = Modifier
                    .padding(horizontal = dimensions.space5)
                    .padding(bottom = dimensions.space4),
                selected = state.selectedTab,
                onSelect = intent::onTabSelected,
            )

            Box(modifier = Modifier.weight(1f)) {
                when (state.selectedTab) {
                    AddQuoteTab.TEXT -> Column(
                        modifier = Modifier
                            .padding(horizontal = dimensions.space5)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        AddQuoteTextTab(
                            value = state.quote,
                            onValueChange = intent::onQuoteChanged,
                            requestFocus = true,
                        )
                    }

                    AddQuoteTab.SCAN -> AddQuoteScanTab(
                        mode = state.scanMode,
                        pickedImage = state.pickedImage,
                        lines = state.scanLines,
                        noTextFound = state.scanNoTextFound,
                        offline = state.scanOffline,
                        aiDenial = state.scanAiDenial,
                        onImagePicked = intent::onScanImagePicked,
                        onCropConfirmed = intent::onScanCropConfirmed,
                        onCropCancelled = intent::onScanCropCancelled,
                        onRetake = intent::onScanRetake,
                        onRetry = intent::onScanRetry,
                        onUpgrade = intent::onUpgradeClicked,
                        onProceed = intent::onScanProceed,
                    )

                    AddQuoteTab.VOICE -> AddQuoteVoiceTab(
                        modifier = Modifier.padding(horizontal = dimensions.space5),
                        quote = state.quote,
                        isRecording = state.isRecording,
                        isVoiceAvailable = state.isVoiceAvailable,
                        onQuoteChange = intent::onQuoteChanged,
                        onMicPressed = intent::onMicPressed,
                        onMicReleased = intent::onMicReleased,
                    )
                }
            }
        }

        if (state.selectedTab != AddQuoteTab.SCAN) {
            AddQuoteFooter(
                onNext = intent::onNextClicked,
                enabled = state.canProceed,
            )
        }
    }
}
