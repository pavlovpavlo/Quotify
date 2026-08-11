package com.kovhan.feature.main.presentation.subscription

import com.kovhan.core.models.billing.SubscriptionStatus
import com.kovhan.core.models.billing.isEntitled
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.billing.use_case.GetSubscriptionStatusUseCase
import com.kovhan.domain.billing.use_case.RefreshSubscriptionUseCase
import com.kovhan.feature.main.presentation.subscription.mvi.CurrentSubscriptionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CurrentSubscriptionEffect : UiEffect

/** Тимчасовий екран без дизайну — показує, що бекенд записав у `users/{uid}.premium`. */
@HiltViewModel
class CurrentSubscriptionViewModel @Inject constructor(
    private val getSubscriptionStatus: GetSubscriptionStatusUseCase,
    private val refreshSubscription: RefreshSubscriptionUseCase,
) : BaseViewModel<CurrentSubscriptionState, CurrentSubscriptionEffect>(CurrentSubscriptionState()) {

    init {
        load { getSubscriptionStatus() }
    }

    fun onRefreshClicked() = load { refreshSubscription() }

    private fun load(fetch: suspend () -> SubscriptionStatus) {
        publishState { copy(isLoading = true) }
        viewModelScope.launch {
            val status = runCatching { fetch() }.getOrDefault(SubscriptionStatus.None)
            publishState {
                copy(isLoading = false, status = status, isEntitled = status.isEntitled())
            }
        }
    }
}
