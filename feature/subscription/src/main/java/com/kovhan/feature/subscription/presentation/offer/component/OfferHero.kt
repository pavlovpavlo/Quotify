package com.kovhan.feature.subscription.presentation.offer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.SparkleHero

private val BarTopInset = 14.dp
private val BarHeight = 36.dp
private val BarBottomGap = 10.dp
private const val SparkSpread = 1.25f

@Composable
internal fun OfferHero(
    onClose: () -> Unit,
    topInset: Dp,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.offer.heroGradient),
    ) {
        SparkleHero(
            illustrationRes = QuotifyMaterialTheme.images.imgSpecialOffer,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topInset + BarTopInset + BarHeight + BarBottomGap),
            showGlow = false,
            heroHeight = null,
            sparkSpread = SparkSpread,
        )

        OfferHeroBar(
            onClose = onClose,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = topInset + BarTopInset, start = 14.dp, end = 14.dp),
        )
    }
}
