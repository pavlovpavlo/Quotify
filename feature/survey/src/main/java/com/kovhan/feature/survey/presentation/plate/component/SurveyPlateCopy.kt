package com.kovhan.feature.survey.presentation.plate.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun SurveyPlateCopy(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.size3),
    ) {
        Text(
            text = title,
            color = colors.textPrimary,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.W700,
                lineHeight = 19.sp,
            ),
        )
        Text(
            text = subtitle,
            color = colors.textSecondary,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 12.sp,
                lineHeight = 17.sp,
            ),
        )
    }
}
