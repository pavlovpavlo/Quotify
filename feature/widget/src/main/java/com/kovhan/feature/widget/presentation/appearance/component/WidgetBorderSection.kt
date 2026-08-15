package com.kovhan.feature.widget.presentation.appearance.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kovhan.core.models.widget.WidgetBorderColor
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTones
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.appearance.mvi.WidgetAppearanceIntent

@Composable
internal fun WidgetBorderSection(
    settings: WidgetStyleSettings,
    intent: WidgetAppearanceIntent,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(verticalArrangement = Arrangement.spacedBy(dimensions.space4)) {
        WidgetAppearanceToggleRow(
            title = stringResource(DsR.string.widget_appearance_border_title),
            subtitle = stringResource(DsR.string.widget_appearance_border_subtitle),
            checked = settings.borderEnabled,
            onCheckedChange = intent::onBorderToggled,
        )

        AnimatedVisibility(visible = settings.borderEnabled) {
            WidgetLabeledSection(stringResource(DsR.string.widget_appearance_border_color)) {
                WidgetTonePicker(
                    selectedToneId = settings.borderToneId(),
                    onToneSelected = intent::onBorderToneSelected,
                )
            }
        }
    }
}

/** Falls back to the fill tone so the list opens on something related to the widget. */
private fun WidgetStyleSettings.borderToneId(): String =
    (borderColor as? WidgetBorderColor.Tone)?.toneId
        ?: (this as? WidgetStyleSettings.Classic)?.toneId
        ?: WidgetTones.DEFAULT
