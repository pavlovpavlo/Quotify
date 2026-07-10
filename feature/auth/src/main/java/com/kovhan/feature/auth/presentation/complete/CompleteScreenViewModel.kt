package com.kovhan.feature.auth.presentation.complete

import com.kovhan.core.models.onFailure
import com.kovhan.core.models.onSuccess
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.use_case.guest.ContinueAsGuestUseCase
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenEffect
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenIntent
import com.kovhan.feature.auth.presentation.complete.mvi.CompleteScreenState
import com.kovhan.feature.auth.presentation.util.toSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteScreenViewModel @Inject constructor(
    private val continueAsGuest: ContinueAsGuestUseCase,
) : BaseViewModel<CompleteScreenState, CompleteScreenEffect>(CompleteScreenState()),
    CompleteScreenIntent {

    override fun onSignUpClicked() {
        publishEffect(CompleteScreenEffect.NavigateToSignUp)
    }

    override fun onSignInClicked() {
        publishEffect(CompleteScreenEffect.NavigateToSignIn)
    }

    override fun onLaterClicked() {
        if (uiState.value.isGuestLoading) return
        publishState { copy(isGuestLoading = true) }
        viewModelScope.launch {
            continueAsGuest()
                .onSuccess { publishEffect(CompleteScreenEffect.NavigateToMain) }
                .onFailure { error ->
                    publishState { copy(isGuestLoading = false) }
                    showSnackbar(error.toSnackbar())
                }
        }
    }
}
