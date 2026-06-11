package com.kovhan.feature.onboarding.presentation.onboarding.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.kovhan.design.systems.QuotifyMaterialTheme


@Composable
internal fun OnboardingTextBlock(
    @StringRes title: Int,
    @StringRes body: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(QuotifyMaterialTheme.dimensions.space3),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(title),
            style = QuotifyMaterialTheme.typography.h3,
            color = QuotifyMaterialTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(body),
            style = QuotifyMaterialTheme.typography.body,
            color = QuotifyMaterialTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
