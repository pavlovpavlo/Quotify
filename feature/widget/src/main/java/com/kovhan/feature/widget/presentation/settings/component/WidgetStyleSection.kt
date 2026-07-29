package com.kovhan.feature.widget.presentation.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.widget.WidgetStyle
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsIntent
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsState

@Composable
internal fun WidgetStyleSection(
    state: WidgetSettingsState,
    intent: WidgetSettingsIntent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        WidgetSectionLabel(stringResource(DsR.string.widget_style_label))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            styleTabs.forEach { (style, labelRes) ->
                WidgetStyleTab(
                    label = stringResource(labelRes),
                    selected = state.style == style,
                    onClick = { intent.onStyleSelected(style) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private val styleTabs = listOf(
    WidgetStyle.MINIMAL to DsR.string.widget_style_minimal,
    WidgetStyle.CLASSIC to DsR.string.widget_style_classic,
    WidgetStyle.COVER to DsR.string.widget_style_cover,
)
