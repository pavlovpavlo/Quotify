package com.kovhan.feature.widget.presentation.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.widget.rememberHomeWidgetPlaced
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.settings.component.WidgetFeedbackSection
import com.kovhan.feature.widget.presentation.settings.component.WidgetOptionsSection
import com.kovhan.feature.widget.presentation.settings.component.WidgetSettingsFooter
import com.kovhan.feature.widget.presentation.settings.component.WidgetSettingsTopBar
import com.kovhan.feature.widget.presentation.settings.component.WidgetSourceSection
import com.kovhan.feature.widget.presentation.settings.component.WidgetStyleSection
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsIntent
import com.kovhan.feature.widget.presentation.settings.mvi.WidgetSettingsState
import androidx.compose.ui.res.stringResource

@Composable
internal fun WidgetSettingsScreen(
    state: WidgetSettingsState,
    intent: WidgetSettingsIntent,
    onBack: () -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val widgetPlaced = rememberHomeWidgetPlaced()

    BackHandler(onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
            return@Box
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        ) {
            WidgetSettingsTopBar(
                title = stringResource(DsR.string.widget_settings_title),
                onBack = onBack,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                WidgetSourceSection(state = state, intent = intent)
                if (widgetPlaced && !state.feedbackGiven) {
                    WidgetFeedbackSection(onFeedback = intent::onFeedbackSelected)
                }
                WidgetOptionsSection(state = state, intent = intent)
                WidgetStyleSection(state = state, intent = intent)
            }

            WidgetSettingsFooter(
                text = stringResource(
                    if (widgetPlaced) {
                        DsR.string.widget_settings_cta_update
                    } else {
                        DsR.string.widget_settings_cta
                    },
                ),
                onClick = intent::onAddToHomeClicked,
            )
        }
    }
}
