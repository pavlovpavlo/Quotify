package com.kovhan.feature.main.presentation.edit_profile.navigation

import androidx.compose.runtime.Stable

@Stable
interface EditProfileScreenNavAction {
    fun navigateBack()
    fun navigateToAuth()

    companion object {
        val Empty: EditProfileScreenNavAction = EmptyEditProfileScreenNavAction
    }
}

private object EmptyEditProfileScreenNavAction : EditProfileScreenNavAction {
    override fun navigateBack() = Unit
    override fun navigateToAuth() = Unit
}
