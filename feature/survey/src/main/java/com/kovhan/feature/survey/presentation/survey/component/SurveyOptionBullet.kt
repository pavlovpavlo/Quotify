package com.kovhan.feature.survey.presentation.survey.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.core.models.survey.SurveyQuestionType

private const val BULLET_ANIMATION_MS = 160

@Composable
internal fun SurveyOptionBullet(
    type: SurveyQuestionType,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    val shape = when (type) {
        SurveyQuestionType.SINGLE -> RoundedCornerShape(dimensions.radiusFull)
        SurveyQuestionType.MULTI -> RoundedCornerShape(dimensions.size6)
    }

    val background by animateColorAsState(
        targetValue = if (selected) colors.accentPrimary else Color.Transparent,
        animationSpec = tween(BULLET_ANIMATION_MS),
        label = "survey_bullet_background",
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.accentPrimary else colors.borderStrong,
        animationSpec = tween(BULLET_ANIMATION_MS),
        label = "survey_bullet_border",
    )

    Box(
        modifier = modifier
            .size(dimensions.size22)
            .clip(shape)
            .background(background)
            .border(1.5.dp, borderColor, shape),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Image(
                modifier = Modifier.size(dimensions.size13),
                painter = painterResource(DsR.drawable.ic_check),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.textOnAccent),
            )
        }
    }
}
