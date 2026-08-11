package com.kovhan.feature.main.presentation.subscription.mvi

import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.UiState

data class PaywallState(
    val isLoading: Boolean = true,
    val productId: String? = null,
    val offers: List<PremiumOffer> = emptyList(),
    val log: List<String> = emptyList(),
    val isPremium: Boolean = false,
    val subscriptionStatus: String? = null,
) : UiState
