package com.kovhan.feature.entitydetails.presentation.collection_style

import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.billing.use_case.ObserveIsSubscribedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class EditCollectionStyleState(val isPremium: Boolean = false) : UiState

sealed class EditCollectionStyleEffect : UiEffect

/**
 * Тримає лише ознаку підписки — сам чернетковий стиль лишається локальним
 * станом шита. Слухаємо потік, щоб після покупки замок зник на цьому ж шиті,
 * без перевідкриття.
 */
@HiltViewModel
class EditCollectionStyleViewModel @Inject constructor(
    observeIsSubscribed: ObserveIsSubscribedUseCase,
) : BaseViewModel<EditCollectionStyleState, EditCollectionStyleEffect>(EditCollectionStyleState()) {

    init {
        observeIsSubscribed()
            .onEach { premium -> publishState { copy(isPremium = premium) } }
            .launchIn(viewModelScope)
    }
}
