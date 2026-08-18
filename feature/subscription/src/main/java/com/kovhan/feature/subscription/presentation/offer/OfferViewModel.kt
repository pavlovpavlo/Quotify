package com.kovhan.feature.subscription.presentation.offer

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
    observePurchaseVerifications: ObservePurchaseVerificationsUseCase,
    observePurchaseFlowFailures: ObservePurchaseFlowFailuresUseCase,
) : BaseViewModel<OfferState, OfferEffect>(OfferState()), OfferIntent {

    init {
        loadOffer()

        observePurchaseVerifications()
            .onEach { outcome ->
                if (!uiState.value.isPurchasing) return@onEach

                publishState { copy(isPurchasing = false) }
                when (outcome) {
                    is Outcome.Success -> if (outcome.data.isEntitled()) {
                        publishState { copy(purchaseSucceeded = true) }
                    }

                    is Outcome.Failure ->
                        showSnackbar(SnackbarMessage.error(R.string.paywall_verification_failed))
                }
            }
            .launchIn(viewModelScope)

        observePurchaseFlowFailures()
            .onEach { failure ->
                when (failure) {
                    PurchaseFlowFailure.ALREADY_OWNED -> awaitAlreadyOwnedVerification()

                    PurchaseFlowFailure.FAILED -> {
                        publishState { copy(isPurchasing = false) }
                        showSnackbar(SnackbarMessage.error(R.string.paywall_purchase_failed))
                    }

                    PurchaseFlowFailure.CANCELLED -> publishState { copy(isPurchasing = false) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun awaitAlreadyOwnedVerification() {
        viewModelScope.launch {
            delay(BillingWaits.VERIFICATION_GRACE_MS)
            if (uiState.value.purchaseSucceeded) return@launch
            publishState { copy(isPurchasing = false) }
            showSnackbar(SnackbarMessage.error(R.string.paywall_verification_failed))
        }
    }

    override fun onClaimClicked() {
        val offer = uiState.value.offer ?: return
        if (uiState.value.isPurchasing) return

        publishState { copy(isPurchasing = true) }
        if (!launchPurchase(offer.offerToken)) {
            publishState { copy(isPurchasing = false) }
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
