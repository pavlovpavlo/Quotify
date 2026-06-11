package com.kovhan.feature.main.presentation.edit_profile

import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.snackbar.SnackbarType
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.design.systems.R
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.use_case.DeleteAccountUseCase
import com.kovhan.domain.auth.use_case.GetUserUseCase
import com.kovhan.domain.auth.use_case.RefreshUserUseCase
import com.kovhan.domain.auth.use_case.RemoveProfilePhotoUseCase
import com.kovhan.domain.auth.use_case.SendPasswordResetUseCase
import com.kovhan.domain.auth.use_case.SetProfilePhotoUseCase
import com.kovhan.domain.auth.use_case.SignOutUseCase
import com.kovhan.domain.auth.use_case.UpdateEmailUseCase
import com.kovhan.domain.auth.use_case.UpdateUserProfileUseCase
import com.kovhan.domain.auth.use_case.UpdateUsernameUseCase
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditField
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileDialog
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileEffect
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileIntent
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileSheet
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    getUser: GetUserUseCase,
    private val refreshUser: RefreshUserUseCase,
    private val updateName: UpdateUserProfileUseCase,
    private val updateUsername: UpdateUsernameUseCase,
    private val updateEmail: UpdateEmailUseCase,
    private val setPhoto: SetProfilePhotoUseCase,
    private val removePhoto: RemoveProfilePhotoUseCase,
    private val sendPasswordReset: SendPasswordResetUseCase,
    private val deleteAccount: DeleteAccountUseCase,
    private val signOut: SignOutUseCase,
) : BaseViewModel<EditProfileState, EditProfileEffect>(EditProfileState()),
    EditProfileIntent {

    init {
        getUser()
            .onEach { user -> publishState { copy(user = user) } }
            .launchIn(viewModelScope)

        // Pick up a verified email change made via the confirmation link.
        viewModelScope.launch { refreshUser() }
    }

    override fun onPhotoClicked() = publishState { copy(sheet = EditProfileSheet.Photo) }

    override fun onFieldClicked(field: EditField) =
        publishState { copy(sheet = EditProfileSheet.Field(field)) }

    override fun onPasswordClicked() {
        val email = uiState.value.user?.email?.takeIf { it.isNotBlank() } ?: return
        viewModelScope.launch {
            when (sendPasswordReset(email)) {
                is AuthResult.Success ->
                    showSnackbar(R.string.edit_password_reset_sent, SnackbarType.Success)
                is AuthResult.Failure ->
                    showSnackbar(SnackbarMessage.error(R.string.auth_error_generic))
            }
        }
    }

    override fun onSheetDismissed() = publishState { copy(sheet = null) }

    override fun onFieldSaved(field: EditField, value: String) {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return
        publishState { copy(sheet = null) }
        viewModelScope.launch {
            when (field) {
                EditField.NAME -> updateName(trimmed)
                EditField.USERNAME -> updateUsername(trimmed)
                EditField.EMAIL -> when (val result = updateEmail(trimmed)) {
                    is AuthResult.Success ->
                        showSnackbar(R.string.edit_email_verification_sent, SnackbarType.Success)
                    is AuthResult.Failure -> showSnackbar(result.error.toSnackbar())
                }
            }
        }
    }

    override fun onPhotoPicked(uri: String) {
        publishState { copy(sheet = null) }
        viewModelScope.launch {
            if (!setPhoto(uri)) {
                showSnackbar(SnackbarMessage.error(R.string.edit_photo_upload_failed))
            }
        }
    }

    override fun onPhotoRemoved() {
        publishState { copy(sheet = null) }
        viewModelScope.launch { removePhoto() }
    }

    override fun onLogoutClicked() = publishState { copy(dialog = EditProfileDialog.Logout) }

    override fun onDeleteAccountClicked() = publishState { copy(dialog = EditProfileDialog.Delete) }

    override fun onDialogDismissed() = publishState { copy(dialog = null) }

    override fun onLogoutConfirmed() {
        if (uiState.value.isProcessing) return
        publishState { copy(isProcessing = true, dialog = null) }
        viewModelScope.launch {
            signOut()
            publishState { copy(isProcessing = false) }
            publishEffect(EditProfileEffect.NavigateToAuth)
        }
    }

    override fun onDeleteConfirmed() {
        if (uiState.value.isProcessing) return
        publishState { copy(isProcessing = true, dialog = null) }
        viewModelScope.launch {
            deleteAccount()
            signOut()
            publishState { copy(isProcessing = false) }
            publishEffect(EditProfileEffect.NavigateToAuth)
        }
    }
}
