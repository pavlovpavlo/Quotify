package com.kovhan.feature.survey.presentation.plate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.survey.presentation.plate.component.SurveyPlateCopy
import com.kovhan.feature.survey.presentation.plate.component.SurveyPlateIcon

/**
 * Survey entry point on the profile screen — shown once the invite has been
 * postponed, so the questionnaire stays reachable.
 */
@Composable
fun SurveyPlate(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: SurveyPlateVariant = SurveyPlateVariant.CARD,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val isCard = variant == SurveyPlateVariant.CARD
    val shape = if (variant == SurveyPlateVariant.GOLD_FULL) {
        RoundedCornerShape(dimensions.space0)
    } else {
        RoundedCornerShape(dimensions.radiusLg)
    }
    val horizontalPadding: Dp = when (variant) {
        SurveyPlateVariant.CARD -> dimensions.size14
        SurveyPlateVariant.GOLD -> dimensions.size14
        SurveyPlateVariant.GOLD_FULL -> dimensions.size22
    }
    val verticalPadding: Dp = if (isCard) dimensions.size14 else dimensions.size16

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(if (isCard) dimensions.size2 else dimensions.space0, shape)
            .clip(shape)
            .background(if (isCard) colors.bgElevated else colors.accentSavedSoft)
            .border(dimensions.size1, if (isCard) colors.border else Color.Transparent, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size13),
    ) {
        SurveyPlateIcon(variant = variant)

        SurveyPlateCopy(
            modifier = Modifier.weight(1f),
            title = stringResource(DsR.string.survey_plate_title),
            subtitle = stringResource(DsR.string.survey_plate_subtitle),
        )

        Image(
            modifier = Modifier.size(dimensions.size18),
            painter = painterResource(DsR.drawable.ic_chevron_right),
            contentDescription = null,
            colorFilter = ColorFilter.tint(colors.textSecondary),
        )
    }
}
