package com.kovhan.feature.survey.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun SurveyEyebrow(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = QuotifyMaterialTheme.colors.accentPrimary,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = TextStyle(
            fontFamily = InterFamily,
            fontSize = 11.sp,
            fontWeight = FontWeight.W700,
            letterSpacing = 0.12.em,
        ),
    )
}
