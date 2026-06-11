package com.kovhan.feature.main.presentation.edit_profile.mvi

import com.kovhan.core.ui.UiState
import com.kovhan.domain.auth.AuthUser

enum class EditField { NAME, USERNAME, EMAIL }

sealed interface EditProfileSheet {
    data object Photo : EditProfileSheet
    data class Field(val field: EditField) : EditProfileSheet
}

sealed interface EditProfileDialog {
    data object Logout : EditProfileDialog
    data object Delete : EditProfileDialog
}

data class EditProfileState(
    val user: AuthUser? = null,
    val sheet: EditProfileSheet? = null,
    val dialog: EditProfileDialog? = null,
    val isProcessing: Boolean = false,
) : UiState
