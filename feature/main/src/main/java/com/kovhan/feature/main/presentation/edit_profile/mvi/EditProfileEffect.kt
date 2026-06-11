package com.kovhan.feature.main.presentation.edit_profile.mvi

import com.kovhan.core.ui.UiEffect
import com.kovhan.core.navigation.EditField

sealed class EditProfileEffect : UiEffect {
    data object NavigateToAuth : EditProfileEffect()
    data object OpenPhotoSheet : EditProfileEffect()
    data class OpenFieldSheet(val field: EditField, val initialValue: String) : EditProfileEffect()
    data object OpenLogoutDialog : EditProfileEffect()
    data object OpenDeleteDialog : EditProfileEffect()
}
