package com.kovhan.feature.subscription.presentation.paywall

import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.PurchaseFlowFailure
import com.kovhan.core.models.billing.isEntitled
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.design.systems.R
import com.kovhan.domain.billing.BillingProducts
import com.kovhan.domain.billing.use_case.GetPremiumProductUseCase
import com.kovhan.domain.billing.use_case.LaunchPurchaseUseCase
import com.kovhan.domain.billing.use_case.ObservePurchaseFlowFailuresUseCase
import com.kovhan.domain.billing.use_case.ObservePurchaseVerificationsUseCase
import com.kovhan.domain.billing.use_case.RestorePurchasesUseCase
import com.kovhan.feature.subscription.presentation.common.byOfferBenefit
import com.kovhan.feature.subscription.presentation.common.planDisplayOrder
import com.kovhan.feature.subscription.presentation.paywall.mvi.PaywallEffect
import com.kovhan.feature.subscription.presentation.paywall.mvi.PaywallIntent
import com.kovhan.feature.subscription.presentation.paywall.mvi.PaywallState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val getPremiumProduct: GetPremiumProductUseCase,
    private val launchPurchase: LaunchPurchaseUseCase,
    private val restorePurchases: RestorePurchasesUseCase,
    observePurchaseVerifications: ObservePurchaseVerificationsUseCase,
    observePurchaseFlowFailures: ObservePurchaseFlowFailuresUseCase,
) : BaseViewModel<PaywallState, PaywallEffect>(PaywallState()), PaywallIntent {

    init {
        loadOffers()

        observePurchaseFlowFailures()
            .onEach { failure ->
                publishState { copy(isPurchasing = false) }
                if (failure == PurchaseFlowFailure.FAILED) {
                    showSnackbar(SnackbarMessage.error(R.string.paywall_purchase_failed))
                }
            }
            .launchIn(viewModelScope)

        observePurchaseVerifications()
            .onEach { outcome ->
                val awaited = uiState.value.isPurchasing || uiState.value.isRestoring
                if (!awaited) return@onEach

                publishState { copy(isPurchasing = false, isRestoring = false) }
                when (outcome) {
                    is Outcome.Success -> if (outcome.data.isEntitled()) {
                        publishState { copy(purchaseSucceeded = true) }
                    }

                    is Outcome.Failure ->
                        showSnackbar(SnackbarMessage.error(R.string.paywall_verification_failed))
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onPlanSelected(basePlanId: String) =
        publishState { copy(selectedBasePlanId = basePlanId) }

    override fun onSubscribeClicked() {
        val offer = uiState.value.selectedOffer ?: return
        if (uiState.value.isPurchasing) return

        publishState { copy(isPurchasing = true) }
        if (!launchPurchase(offer.offerToken)) {
            publishState { copy(isPurchasing = false) }
            showSnackbar(SnackbarMessage.error(R.string.paywall_purchase_failed))
        }
    }

    override fun onRestoreClicked() {
        if (uiState.value.isRestoring) return
        publishState { copy(isRestoring = true) }
        viewModelScope.launch {
            runCatching { restorePurchases() }
            delay(RESTORE_GRACE_MS)
            if (uiState.value.purchaseSucceeded) return@launch
            publishState { copy(isRestoring = false) }
            showSnackbar(SnackbarMessage.info(R.string.paywall_restore_empty))
        }
    }

    override fun onRetryClicked() = loadOffers()

    private fun loadOffers() {
        publishState { copy(isLoading = true, loadFailed = false) }
        viewModelScope.launch {
            val product = runCatching { getPremiumProduct() }.getOrNull()
            val offers = product?.offers
                ?.filter {
                    it.offerToken.isNotBlank() &&
                        it.offerId != BillingProducts.OFFER_YEARLY_SPECIAL
                }
                ?.groupBy { it.basePlanId }
                ?.mapNotNull { (_, variants) -> variants.maxWithOrNull(byOfferBenefit) }
                ?.sortedBy { offer ->
                    planDisplayOrder.indexOf(offer.basePlanId)
                        .takeIf { it >= 0 } ?: planDisplayOrder.size
                }
                .orEmpty()

            publishState {
                copy(
                    isLoading = false,
                    loadFailed = offers.isEmpty(),
                    offers = offers,
                    selectedBasePlanId = selectedBasePlanId ?: offers.firstOrNull()?.basePlanId,
                )
            }
        }
    }

    private companion object {
        const val RESTORE_GRACE_MS = 3_000L
    }
}
