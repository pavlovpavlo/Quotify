package com.kovhan.feature.survey.presentation.survey.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.core.models.survey.SurveyQuestionType

private const val ROW_ANIMATION_MS = 160

@Composable
internal fun SurveyOptionRow(
    label: String,
    type: SurveyQuestionType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val shape = RoundedCornerShape(dimensions.radiusLg)

    val background by animateColorAsState(
        targetValue = if (selected) colors.accentPrimarySoft else colors.bgElevated,
        animationSpec = tween(ROW_ANIMATION_MS),
        label = "survey_option_background",
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.accentPrimary else colors.border,
        animationSpec = tween(ROW_ANIMATION_MS),
        label = "survey_option_border",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = dimensions.size52)
            .clip(shape)
            .background(background)
            .border(dimensions.size1, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = dimensions.size14, vertical = dimensions.size12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.size12),
    ) {
        SurveyOptionBullet(type = type, selected = selected)

        Text(
            text = label,
            color = colors.textPrimary,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.W500,
                lineHeight = 20.sp,
            ),
        )
    }
}
