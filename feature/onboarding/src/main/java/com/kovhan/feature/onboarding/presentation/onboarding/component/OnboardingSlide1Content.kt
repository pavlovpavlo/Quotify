package com.kovhan.feature.onboarding.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.extensions.floatingY
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun OnboardingSlide1Content(modifier: Modifier = Modifier) {
    OnboardingSlideScaffold(
        modifier = modifier,
        title = R.string.onboarding_slide_1_title,
        body = R.string.onboarding_slide_1_body,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(QuotifyMaterialTheme.images.onboardingSlide1),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 18.dp, end = 20.dp)
                .size(40.dp)
                .rotate(-8f)
                .floatingY(amplitude = 3.dp, durationMs = 2400),
            painter = painterResource(R.drawable.quote_onboarding_icon),
            contentDescription = null,
        )
    }
}
