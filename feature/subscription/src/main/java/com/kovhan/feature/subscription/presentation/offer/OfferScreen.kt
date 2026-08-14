package com.kovhan.feature.subscription.presentation.offer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.FixedFontScale
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.common.discountPercentOrNull
import com.kovhan.feature.subscription.presentation.common.displayPrice
import com.kovhan.feature.subscription.presentation.common.introDisplayPriceOrNull
import com.kovhan.feature.subscription.presentation.common.introMonthlyOrNull
import com.kovhan.feature.subscription.presentation.offer.component.OfferHero
import com.kovhan.feature.subscription.presentation.offer.component.OfferSheet
import com.kovhan.feature.subscription.presentation.offer.mvi.OfferIntent
import com.kovhan.feature.subscription.presentation.offer.mvi.OfferState

@Composable
fun OfferScreen(
    state: OfferState,
    intent: OfferIntent,
    onClose: () -> Unit,
    onOpenLink: (title: String, url: String) -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val locale = LocalConfiguration.current.locales[0]
    val offer = state.offer

    FixedFontScale {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bgPrimary)
                .padding(bottom = paddingValues.calculateBottomPadding()),
        ) {
            if (state.isLoading || offer == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colors.accentPrimary,
                )
                return@Box
            }

            Column(modifier = Modifier.fillMaxSize()) {
                OfferHero(
                    onClose = onClose,
                    topInset = paddingValues.calculateTopPadding(),
                    modifier = Modifier.weight(1f),
                )

                OfferSheet(
                    modifier = Modifier.offset(y = (-22).dp),
                    priceNow = offer.introDisplayPriceOrNull(locale) ?: offer.displayPrice(locale),
                    priceWas = offer.introAmountMicros?.let { offer.displayPrice(locale) },
                    discountPercent = offer.discountPercentOrNull(),
                    perMonth = offer.introMonthlyOrNull(locale),
                    isPurchasing = state.isPurchasing,
                    onClaim = intent::onClaimClicked,
                    onDismiss = onClose,
                    onOpenLink = onOpenLink,
                )
            }
        }
    }
}
