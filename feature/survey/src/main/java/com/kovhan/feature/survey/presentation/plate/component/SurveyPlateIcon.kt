package com.kovhan.feature.survey.presentation.plate.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.survey.presentation.plate.SurveyPlateVariant

@Composable
internal fun SurveyPlateIcon(
    variant: SurveyPlateVariant,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val filled = variant != SurveyPlateVariant.CARD
    val shape = if (filled) {
        RoundedCornerShape(dimensions.radiusFull)
    } else {
        RoundedCornerShape(dimensions.size10)
    }

    Box(
        modifier = modifier
            .size(dimensions.size40)
            .clip(shape)
            .background(if (filled) colors.accentSaved else colors.accentPremiumSoft),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.size(if (filled) dimensions.size19 else dimensions.size21),
            painter = painterResource(DsR.drawable.ic_survey_chart),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                if (filled) colors.textOnAccent else colors.accentPremium,
            ),
        )
    }
}
