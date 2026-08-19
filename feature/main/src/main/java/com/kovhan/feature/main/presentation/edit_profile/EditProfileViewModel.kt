package com.kovhan.feature.main.presentation.edit_profile

import com.kovhan.core.analytics.AccountDeleteResult
import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.event.AccountDeleteFinished
import com.kovhan.core.analytics.event.LogoutFinished
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.snackbar.SnackbarType
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.design.systems.R
import com.kovhan.core.models.Outcome
import com.kovhan.domain.auth.model.AuthError
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
import com.kovhan.core.navigation.EditField
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileEffect
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileIntent
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
    private val analytics: AnalyticsTracker,
) : BaseViewModel<EditProfileState, EditProfileEffect>(EditProfileState()),
    EditProfileIntent {

    init {
        getUser()
            .onEach { user -> publishState { copy(user = user) } }
            .launchIn(viewModelScope)

        // Pick up a verified email change made via the confirmation link.
        viewModelScope.launch { refreshUser() }
    }

    override fun onPhotoClicked() = publishEffect(EditProfileEffect.OpenPhotoSheet)

    override fun onFieldClicked(field: EditField) {
        val user = uiState.value.user
        val email = user?.email.orEmpty()
        val initialValue = when (field) {
            EditField.NAME -> user?.displayName.orEmpty()
            EditField.USERNAME ->
                user?.username
                    ?: if (user?.isGoogleAccount == true) {
                        email.substringBefore("@").takeIf { it.isNotBlank() }.orEmpty()
                    } else {
                        ""
                    }
            EditField.EMAIL -> email
        }
        publishEffect(EditProfileEffect.OpenFieldSheet(field, initialValue))
    }

    override fun onPasswordClicked() {
        val email = uiState.value.user?.email?.takeIf { it.isNotBlank() } ?: return
        viewModelScope.launch {
            when (val result = sendPasswordReset(email)) {
                is Outcome.Success ->
                    showSnackbar(R.string.edit_password_reset_sent, SnackbarType.Success)
                is Outcome.Failure ->
                    showSnackbar(result.error.toSnackbar())
            }
        }
    }

    override fun onFieldSaved(field: EditField, value: String) {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            when (field) {
                EditField.NAME -> updateName(trimmed)
                EditField.USERNAME -> updateUsername(trimmed)
                EditField.EMAIL -> when (val result = updateEmail(trimmed)) {
                    is Outcome.Success ->
                        showSnackbar(R.string.edit_email_verification_sent, SnackbarType.Success)
                    is Outcome.Failure -> showSnackbar(result.error.toSnackbar())
                }
            }
        }
    }

    override fun onPhotoPicked(uri: String) {
        viewModelScope.launch {
            if (!setPhoto(uri)) {
                showSnackbar(SnackbarMessage.error(R.string.edit_photo_upload_failed))
            }
        }
    }

    override fun onPhotoRemoved() {
        viewModelScope.launch { removePhoto() }
    }

    override fun onCameraPermissionDenied() =
        showSnackbar(SnackbarMessage.error(R.string.edit_photo_camera_permission))

    override fun onSignInOrRegisterClicked() = publishEffect(EditProfileEffect.NavigateToLogin)

    override fun onLogoutClicked() = publishEffect(EditProfileEffect.OpenLogoutDialog)

    override fun onDeleteAccountClicked() = publishEffect(EditProfileEffect.OpenDeleteDialog)

    override fun onLogoutConfirmed() {
        if (uiState.value.isProcessing) return
        publishState { copy(isProcessing = true) }
        viewModelScope.launch {
            signOut()
            analytics.track(LogoutFinished)
            publishState { copy(isProcessing = false) }
            publishEffect(EditProfileEffect.NavigateToAuth)
        }
    }

    override fun onDeleteConfirmed() {
        if (uiState.value.isProcessing) return
        publishState { copy(isProcessing = true) }
        viewModelScope.launch {
            when (val result = deleteAccount()) {
                is Outcome.Success -> {
                    signOut()
                    analytics.track(AccountDeleteFinished(AccountDeleteResult.DELETED))
                    publishState { copy(isProcessing = false) }
                    publishEffect(EditProfileEffect.NavigateToAuth)
                }

                is Outcome.Failure -> {
                    publishState { copy(isProcessing = false) }
                    if (result.error == AuthError.RecentLoginRequired) {
                        publishEffect(EditProfileEffect.NavigateToConfirmDelete)
                    } else {
                        showSnackbar(result.error.toSnackbar())
                    }
                }
            }
        }
    }
}
