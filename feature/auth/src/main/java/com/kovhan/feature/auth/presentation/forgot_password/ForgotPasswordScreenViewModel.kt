package com.kovhan.feature.auth.presentation.forgot_password

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.use_case.SendPasswordResetUseCase
import com.kovhan.domain.auth.use_case.ValidateAuthInputUseCase
import com.kovhan.core.models.onFailure
import com.kovhan.core.models.onSuccess
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenEffect
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenIntent
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenState
import com.kovhan.feature.auth.presentation.util.toSnackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordScreenViewModel @Inject constructor(
    private val sendPasswordReset: SendPasswordResetUseCase,
    private val validateInput: ValidateAuthInputUseCase,
) : BaseViewModel<ForgotPasswordScreenState, ForgotPasswordScreenEffect>(ForgotPasswordScreenState()),
    ForgotPasswordScreenIntent {

    override fun onEmailChanged(value: TextFieldValue) = publishState { copy(email = value, errorMessage = null, errorValidationMessage = null) }

    override fun onSendClicked() {
        val current = uiState.value
        if (!current.canSubmit) return
        validateInput(email = current.email.text)?.let { error ->
            publishState { copy(isLoading = false, errorValidationMessage = error) }
            return
        }

        publishState { copy(isLoading = true) }
        viewModelScope.launch {
            sendPasswordReset(current.email.text)
                .onSuccess {
                    publishState { copy(isLoading = false, errorMessage = null) }
                    publishEffect(ForgotPasswordScreenEffect.NavigateBack)
                }
                .onFailure {
                    publishState { copy(isLoading = false, errorMessage = it) }
                }
        }
    }

    override fun onBackToSignInClicked() {
        publishEffect(ForgotPasswordScreenEffect.NavigateToSignIn)
    }

    override fun onBackClicked() {
        publishEffect(ForgotPasswordScreenEffect.NavigateBack)
    }
}
