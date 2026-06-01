package com.kovhan.feature.onboarding.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun OnboardingSlide3Content(modifier: Modifier = Modifier) {
    val images = QuotifyMaterialTheme.images
    val heroRes = if (LocalConfiguration.current.locales[0].language == "uk") {
        images.onboardingSlide3Ukr
    } else {
        images.onboardingSlide3Eng
    }

    OnboardingSlideScaffold(
        modifier = modifier,
        title = R.string.onboarding_slide_3_title,
        body = R.string.onboarding_slide_3_body,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(heroRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
