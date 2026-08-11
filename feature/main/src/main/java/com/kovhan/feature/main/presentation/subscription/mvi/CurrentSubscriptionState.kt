package com.kovhan.feature.main.presentation.subscription.mvi

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.ui.UiState

data class CurrentSubscriptionState(
    val isLoading: Boolean = true,
    val status: SubscriptionStatus = SubscriptionStatus.None,
    val isEntitled: Boolean = false,
) : UiState
