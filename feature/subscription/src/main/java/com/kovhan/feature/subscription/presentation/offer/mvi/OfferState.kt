package com.kovhan.feature.subscription.presentation.offer.mvi

import com.kovhan.core.models.billing.PremiumOffer
import com.kovhan.core.ui.UiState

data class OfferState(
    val isLoading: Boolean = true,
    val offer: PremiumOffer? = null,
    val isPurchasing: Boolean = false,
    val purchaseSucceeded: Boolean = false,
) : UiState
