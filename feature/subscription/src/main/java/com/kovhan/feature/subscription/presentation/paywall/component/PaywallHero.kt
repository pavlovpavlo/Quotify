package com.kovhan.feature.subscription.presentation.paywall.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.SparkleHero

@Composable
internal fun PaywallHero(modifier: Modifier = Modifier) {
    SparkleHero(
        illustrationRes = QuotifyMaterialTheme.images.imgPremiumReader,
        modifier = modifier,
    )
}
