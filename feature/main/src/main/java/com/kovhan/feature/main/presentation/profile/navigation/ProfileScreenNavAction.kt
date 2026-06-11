package com.kovhan.feature.main.presentation.profile.navigation

import androidx.compose.runtime.Stable

@Stable
interface ProfileScreenNavAction {
    fun navigateBack()
    fun navigateToSettings()
    fun navigateToEditProfile()
    fun navigateToAbout()
    fun navigateToAuth()

    companion object {
        val Empty: ProfileScreenNavAction = EmptyProfileScreenNavAction
    }
}

private object EmptyProfileScreenNavAction : ProfileScreenNavAction {
    override fun navigateBack() = Unit
    override fun navigateToSettings() = Unit
    override fun navigateToEditProfile() = Unit
    override fun navigateToAbout() = Unit
    override fun navigateToAuth() = Unit
}
