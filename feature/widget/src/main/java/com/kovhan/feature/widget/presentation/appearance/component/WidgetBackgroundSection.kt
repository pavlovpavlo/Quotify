package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.appearance.mvi.WidgetAppearanceIntent

/** The minimal style has no background of its own, so it offers no picker. */
@Composable
internal fun WidgetBackgroundSection(
    settings: WidgetStyleSettings,
    intent: WidgetAppearanceIntent,
) {
    when (settings) {
        is WidgetStyleSettings.Minimal -> Unit

        is WidgetStyleSettings.Classic ->
            WidgetLabeledSection(stringResource(DsR.string.widget_appearance_background)) {
                WidgetTonePicker(
                    selectedToneId = settings.toneId,
                    onToneSelected = intent::onToneSelected,
                )
            }

        is WidgetStyleSettings.Cover ->
            WidgetLabeledSection(stringResource(DsR.string.widget_appearance_background)) {
                WidgetCoverPicker(
                    selectedCoverId = settings.coverId,
                    blurEnabled = settings.blurEnabled,
                    onCoverSelected = intent::onCoverSelected,
                )
            }
    }
}
