package com.kovhan.feature.subscription.presentation.offer

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.PaywallSource
import com.kovhan.core.analytics.PaywallType
import com.kovhan.core.analytics.PurchaseFailure
import com.kovhan.core.analytics.event.SubscriptionClosed
import com.kovhan.core.analytics.event.SubscriptionOpened
import com.kovhan.core.analytics.event.SubscriptionPurchaseCanceled
import com.kovhan.core.analytics.event.SubscriptionPurchaseFailed
import com.kovhan.core.analytics.event.SubscriptionPurchaseFinished
import com.kovhan.core.analytics.event.SubscriptionPurchaseInitiated
import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.PurchaseFlowFailure
import com.kovhan.core.models.billing.isEntitled
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.design.systems.R
import com.kovhan.domain.billing.BillingWaits
import com.kovhan.domain.billing.use_case.GetSpecialOfferUseCase
import com.kovhan.domain.billing.use_case.LaunchPurchaseUseCase
import com.kovhan.domain.billing.use_case.ObservePurchaseFlowFailuresUseCase
import com.kovhan.domain.billing.use_case.ObservePurchaseVerificationsUseCase
import com.kovhan.feature.subscription.presentation.offer.mvi.OfferEffect
import com.kovhan.feature.subscription.presentation.offer.mvi.OfferIntent
import com.kovhan.feature.subscription.presentation.offer.mvi.OfferState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    private val getSpecialOffer: GetSpecialOfferUseCase,
    private val launchPurchase: LaunchPurchaseUseCase,
    private val analytics: AnalyticsTracker,
    observePurchaseVerifications: ObservePurchaseVerificationsUseCase,
    observePurchaseFlowFailures: ObservePurchaseFlowFailuresUseCase,
) : BaseViewModel<OfferState, OfferEffect>(OfferState()), OfferIntent {

    private var source: PaywallSource = PaywallSource.BANNER

    fun onScreenOpened(source: PaywallSource) {
        this.source = source
        analytics.track(SubscriptionOpened(source, PaywallType.SPECIAL_OFFER))
    }

    fun onScreenClosed() {
        if (uiState.value.purchaseSucceeded) return
        analytics.track(SubscriptionClosed(source, PaywallType.SPECIAL_OFFER))
    }

    private fun productId(): String = uiState.value.offer?.basePlanId.orEmpty()

    private fun trackPurchaseFailed(failure: PurchaseFailure) {
        analytics.track(
            SubscriptionPurchaseFailed(
                source = source,
                type = PaywallType.SPECIAL_OFFER,
                productId = productId(),
                failure = failure,
            ),
        )
    }

    init {
        loadOffer()

        observePurchaseVerifications()
            .onEach { outcome ->
                if (!uiState.value.isPurchasing) return@onEach

                publishState { copy(isPurchasing = false) }
                when (outcome) {
                    is Outcome.Success -> if (outcome.data.isEntitled()) {
                        publishState { copy(purchaseSucceeded = true) }
                        analytics.track(
                            SubscriptionPurchaseFinished(
                                source = source,
                                type = PaywallType.SPECIAL_OFFER,
                                productId = productId(),
                            ),
                        )
                    }

                    is Outcome.Failure -> {
                        trackPurchaseFailed(PurchaseFailure.VERIFICATION_FAILED)
                        showSnackbar(SnackbarMessage.error(R.string.paywall_verification_failed))
                    }
                }
            }
            .launchIn(viewModelScope)

        observePurchaseFlowFailures()
            .onEach { failure ->
                when (failure) {
                    PurchaseFlowFailure.ALREADY_OWNED -> awaitAlreadyOwnedVerification()

                    PurchaseFlowFailure.FAILED -> {
                        publishState { copy(isPurchasing = false) }
                        trackPurchaseFailed(PurchaseFailure.BILLING_ERROR)
                        showSnackbar(SnackbarMessage.error(R.string.paywall_purchase_failed))
                    }

                    PurchaseFlowFailure.CANCELLED -> {
                        publishState { copy(isPurchasing = false) }
                        analytics.track(
                            SubscriptionPurchaseCanceled(
                                source = source,
                                type = PaywallType.SPECIAL_OFFER,
                                productId = productId(),
                            ),
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun awaitAlreadyOwnedVerification() {
        viewModelScope.launch {
            delay(BillingWaits.VERIFICATION_GRACE_MS)
            if (uiState.value.purchaseSucceeded) return@launch
            publishState { copy(isPurchasing = false) }
            trackPurchaseFailed(PurchaseFailure.ALREADY_OWNED)
            showSnackbar(SnackbarMessage.error(R.string.paywall_verification_failed))
        }
    }

    override fun onClaimClicked() {
        val offer = uiState.value.offer ?: return
        if (uiState.value.isPurchasing) return

        publishState { copy(isPurchasing = true) }
        analytics.track(
            SubscriptionPurchaseInitiated(
                source = source,
                type = PaywallType.SPECIAL_OFFER,
                productId = offer.basePlanId,
            ),
        )
        if (!launchPurchase(offer.offerToken)) {
            publishState { copy(isPurchasing = false) }
            trackPurchaseFailed(PurchaseFailure.PRODUCT_UNAVAILABLE)
            showSnackbar(SnackbarMessage.error(R.string.paywall_purchase_failed))
        }
    }

    private fun loadOffer() {
        viewModelScope.launch {
            val offer = getSpecialOffer()
            publishState { copy(isLoading = false, offer = offer) }
        }
    }
}
