package com.kovhan.feature.survey.presentation.plate.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kovhan.design.systems.InterFamily
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun SurveyPlatePreviewSection(
    label: String,
    modifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = labelModifier.padding(bottom = dimensions.size8),
            text = label,
            color = colors.textSecondary,
            style = TextStyle(
                fontFamily = InterFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.W600,
            ),
        )

        content()

        Spacer(Modifier.height(dimensions.size20))
    }
}
