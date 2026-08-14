package com.kovhan.feature.subscription.presentation.paywall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.FixedFontScale
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.subscription.presentation.paywall.component.PaywallFooter
import com.kovhan.feature.subscription.presentation.paywall.component.PaywallOffersBody
import com.kovhan.feature.subscription.presentation.paywall.component.PaywallTopBar
import com.kovhan.feature.subscription.presentation.paywall.component.state.PaywallErrorState
import com.kovhan.feature.subscription.presentation.paywall.component.state.PaywallLoadingState
import com.kovhan.feature.subscription.presentation.paywall.component.state.PaywallSuccessState
import com.kovhan.feature.subscription.presentation.paywall.mvi.PaywallIntent
import com.kovhan.feature.subscription.presentation.paywall.mvi.PaywallState

@Composable
fun PaywallScreen(
    state: PaywallState,
    intent: PaywallIntent,
    onClose: () -> Unit,
    onOpenLink: (title: String, url: String) -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors

    FixedFontScale {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bgPrimary)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        ) {
            if (state.purchaseSucceeded) {
                PaywallSuccessState(onDone = onClose)
                return@Box
            }

            val selectedOffer = state.selectedOffer

            when {
                state.isLoading -> PaywallLoadingState(Modifier.fillMaxSize())

                state.loadFailed || selectedOffer == null -> PaywallErrorState(
                    onRetry = intent::onRetryClicked,
                    modifier = Modifier.fillMaxSize(),
                )

                else -> Column(modifier = Modifier.fillMaxSize()) {
                    PaywallOffersBody(
                        offer = selectedOffer,
                        modifier = Modifier.weight(1f),
                    )
                    PaywallFooter(
                        offers = state.offers,
                        selectedOffer = selectedOffer,
                        isBusy = state.isPurchasing || state.isRestoring,
                        onPlanSelected = intent::onPlanSelected,
                        onSubscribe = intent::onSubscribeClicked,
                        onOpenLink = onOpenLink,
                    )
                }
            }

            PaywallTopBar(
                isRestoring = state.isRestoring,
                onRestore = intent::onRestoreClicked,
                onClose = onClose,
            )
        }
    }
}
