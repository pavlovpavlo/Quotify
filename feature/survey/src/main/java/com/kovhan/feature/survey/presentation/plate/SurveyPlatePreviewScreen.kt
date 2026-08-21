package com.kovhan.feature.survey.presentation.plate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.survey.presentation.plate.component.SurveyPlatePreviewSection

@Composable
internal fun SurveyPlatePreviewScreen(
    onBack: () -> Unit,
    onPlateClick: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        QuotifyTopBar(
            title = stringResource(DsR.string.survey_plate_preview_title),
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = dimensions.size20),
        ) {
            val pagePadding = Modifier.padding(horizontal = dimensions.size22)

            SurveyPlatePreviewSection(
                modifier = pagePadding,
                label = stringResource(DsR.string.survey_plate_preview_card),
            ) {
                SurveyPlate(
                    onClick = onPlateClick,
                    variant = SurveyPlateVariant.CARD,
                )
            }

            SurveyPlatePreviewSection(
                modifier = pagePadding,
                label = stringResource(DsR.string.survey_plate_preview_gold),
            ) {
                SurveyPlate(
                    onClick = onPlateClick,
                    variant = SurveyPlateVariant.GOLD,
                )
            }

            SurveyPlatePreviewSection(
                label = stringResource(DsR.string.survey_plate_preview_gold_full),
                labelModifier = pagePadding,
            ) {
                SurveyPlate(
                    onClick = onPlateClick,
                    variant = SurveyPlateVariant.GOLD_FULL,
                )
            }
        }
    }
}
