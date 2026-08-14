package com.kovhan.feature.subscription.presentation.current.mvi

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.ui.UiState

data class CurrentSubscriptionState(
    val isLoading: Boolean = true,
    val status: SubscriptionStatus = SubscriptionStatus.None,
    val isEntitled: Boolean = false,
    val planPriceMicros: Long? = null,
    val planCurrency: String? = null,
) : UiState
