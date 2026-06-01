package com.kovhan.feature.onboarding.presentation.onboarding.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun OnboardingSlideScaffold(
    @StringRes title: Int,
    @StringRes body: Int,
    modifier: Modifier = Modifier,
    hero: @Composable BoxScope.() -> Unit,
) {
    val dimens = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimens.space8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
            content = hero,
        )

        OnboardingTextBlock(
            title = title,
            body = body,
            modifier = Modifier.padding(
                top = dimens.space5,
                bottom = dimens.space4,
            ),
        )
    }
}
