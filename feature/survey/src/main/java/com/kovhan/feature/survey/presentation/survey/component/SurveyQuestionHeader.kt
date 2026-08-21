package com.kovhan.feature.survey.presentation.survey.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.NewsreaderFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun SurveyQuestionHeader(
    title: String,
    hint: String?,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = colors.textPrimary,
            style = TextStyle(
                fontFamily = NewsreaderFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.W600,
                lineHeight = 31.sp,
            ),
        )

        if (hint != null) {
            Spacer(Modifier.height(dimensions.size6))

            Text(
                text = hint,
                color = colors.textSecondary,
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                ),
            )
        }
    }
}
