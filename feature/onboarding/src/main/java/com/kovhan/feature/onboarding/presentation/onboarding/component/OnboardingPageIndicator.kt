package com.kovhan.feature.onboarding.presentation.onboarding.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme


@Composable
fun OnboardingPageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(QuotifyMaterialTheme.dimensions.space2),
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isActive) 26.dp else 8.dp,
                animationSpec = tween(durationMillis = 250),
                label = "indicatorWidth",
            )
            val color by animateColorAsState(
                targetValue = if (isActive) {
                    QuotifyMaterialTheme.colors.accentPrimary
                } else {
                    QuotifyMaterialTheme.colors.borderStrong
                },
                animationSpec = tween(durationMillis = 250),
                label = "indicatorColor",
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}
