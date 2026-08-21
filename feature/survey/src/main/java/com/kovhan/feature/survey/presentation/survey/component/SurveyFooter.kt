package com.kovhan.feature.survey.presentation.survey.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun SurveyFooter(
    primaryText: String,
    primaryEnabled: Boolean,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    skipText: String? = null,
    onSkipClick: () -> Unit = {},
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.bgPrimary)
            .padding(
                start = dimensions.size20,
                end = dimensions.size20,
                top = dimensions.size10,
                bottom = dimensions.size20,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.size4),
    ) {
        QuotifyButton(
            modifier = Modifier.fillMaxWidth(),
            text = primaryText,
            onClick = onPrimaryClick,
            enabled = primaryEnabled,
            variant = QuotifyButtonVariant.Filled,
            accent = QuotifyButtonAccent.Primary,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(dimensions.size52),
        )

        if (skipText != null) {
            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = skipText,
                onClick = onSkipClick,
                variant = QuotifyButtonVariant.Ghost,
                accent = QuotifyButtonAccent.Neutral,
                withRipple = false,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(dimensions.size44),
            )
        }
    }
}
