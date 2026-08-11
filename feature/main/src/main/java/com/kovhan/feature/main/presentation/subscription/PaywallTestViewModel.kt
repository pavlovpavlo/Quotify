package com.kovhan.feature.main.presentation.subscription

import com.kovhan.core.models.Outcome
import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.billing.use_case.GetPremiumProductUseCase
import com.kovhan.domain.billing.use_case.GetSubscriptionStatusUseCase
import com.kovhan.domain.billing.use_case.LaunchPurchaseUseCase
import com.kovhan.domain.billing.use_case.ObservePurchaseVerificationsUseCase
import com.kovhan.domain.billing.use_case.RestorePurchasesUseCase
import com.kovhan.feature.main.presentation.subscription.mvi.PaywallState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PaywallEffect : UiEffect

/** Тимчасовий екран без дизайну — перевіряє наскрізний флоу білінгу. */
@HiltViewModel
class PaywallTestViewModel @Inject constructor(
    private val getPremiumProduct: GetPremiumProductUseCase,
    private val launchPurchase: LaunchPurchaseUseCase,
    private val restorePurchases: RestorePurchasesUseCase,
    private val getSubscriptionStatus: GetSubscriptionStatusUseCase,
    observeVerifications: ObservePurchaseVerificationsUseCase,
) : BaseViewModel<PaywallState, PaywallEffect>(PaywallState()) {

    init {
        loadProduct()
        refreshStatus()

        observeVerifications()
            .onEach { outcome ->
                when (outcome) {
                    is Outcome.Success -> {
                        publishState {
                            copy(
                                isPremium = outcome.data.isActive,
                                subscriptionStatus = outcome.data.status,
                            )
                        }
                        log(
                            "Верифікація: entitled=${outcome.data.isActive}, " +
                                "status=${outcome.data.status}, expiresAt=${outcome.data.expiresAt}",
                        )
                    }

                    is Outcome.Failure -> log("Верифікація не вдалась: ${outcome.error}")
                }
            }
            .launchIn(viewModelScope)
    }

    private fun refreshStatus() {
        viewModelScope.launch {
            val status = runCatching { getSubscriptionStatus() }.getOrNull() ?: return@launch
            publishState {
                copy(isPremium = status.isActive, subscriptionStatus = status.status)
            }
        }
    }

    fun loadProduct() {
        publishState { copy(isLoading = true) }
        viewModelScope.launch {
            val product = runCatching { getPremiumProduct() }.getOrNull()
            publishState {
                copy(
                    isLoading = false,
                    productId = product?.productId,
                    offers = product?.offers.orEmpty(),
                )
            }
            log(
                if (product == null) {
                    "Продукт не отримано (немає з'єднання з Play або товар недоступний)"
                } else {
                    "Отримано ${product.offers?.size ?: 0} оферів для ${product.productId}"
                },
            )
        }
    }

    fun onBuyClicked(offer: PremiumOffer) {
        val launched = launchPurchase(offer.offerToken)
        log(
            if (launched) {
                "Запущено оплату: ${offer.basePlanId}"
            } else {
                "Не вдалось відкрити діалог оплати: ${offer.basePlanId}"
            },
        )
    }

    fun onRestoreClicked() {
        log("Відновлення покупок…")
        viewModelScope.launch { runCatching { restorePurchases() } }
    }

    private fun log(message: String) = publishState { copy(log = log + message) }
}
