package com.kovhan.feature.subscription.presentation.paywall.mvi

import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.UiState

data class PaywallState(
    val isLoading: Boolean = true,
    val loadFailed: Boolean = false,
    val offers: List<PremiumOffer> = emptyList(),
    val selectedBasePlanId: String? = null,
    val isPurchasing: Boolean = false,
    val isRestoring: Boolean = false,
    val purchaseSucceeded: Boolean = false,
) : UiState {

    val selectedOffer: PremiumOffer?
        get() = offers.firstOrNull { it.basePlanId == selectedBasePlanId } ?: offers.firstOrNull()
}
