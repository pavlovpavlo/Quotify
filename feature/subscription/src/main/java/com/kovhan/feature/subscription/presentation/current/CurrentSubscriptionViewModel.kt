package com.kovhan.feature.subscription.presentation.current

import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.models.billing.isEntitled
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.billing.use_case.GetPremiumProductUseCase
import com.kovhan.domain.billing.use_case.ObserveSubscriptionStatusUseCase
import com.kovhan.feature.subscription.presentation.current.mvi.CurrentSubscriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

sealed class CurrentSubscriptionEffect : UiEffect

/**
 * Екран лише читає кеш статусу — оновлює його `SubscriptionSyncManager`
 * при старті застосунку та поверненні з фону, тож кнопка «оновити» тут зайва.
 * Ціну тарифу Firestore не зберігає, тому підтягуємо її з Play за base plan.
 */
@HiltViewModel
class CurrentSubscriptionViewModel @Inject constructor(
    private val getPremiumProduct: GetPremiumProductUseCase,
    observeSubscriptionStatus: ObserveSubscriptionStatusUseCase,
) : BaseViewModel<CurrentSubscriptionState, CurrentSubscriptionEffect>(CurrentSubscriptionState()) {

    init {
        observeSubscriptionStatus()
            .onEach { status ->
                publishState {
                    copy(
                        isLoading = false,
                        status = status,
                        isEntitled = status.isEntitled(),
                    )
                }
            }
            .launchIn(viewModelScope)

        observeSubscriptionStatus()
            .map { it.basePlanId }
            .distinctUntilChanged()
            .onEach { basePlanId ->
                val offer = loadOffer(basePlanId)
                publishState {
                    copy(
                        planPriceMicros = offer?.priceAmountMicros,
                        planCurrency = offer?.priceCurrencyCode,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun loadOffer(basePlanId: String?): PremiumOffer? {
        if (basePlanId.isNullOrBlank()) return null
        val product = runCatching { getPremiumProduct() }.getOrNull() ?: return null
        return product.offers?.firstOrNull { it.basePlanId == basePlanId }
    }
}
