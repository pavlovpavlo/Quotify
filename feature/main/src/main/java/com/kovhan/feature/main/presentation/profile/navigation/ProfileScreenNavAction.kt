package com.kovhan.feature.main.presentation.profile.navigation

import androidx.compose.runtime.Stable

@Stable
interface ProfileScreenNavAction {
    fun navigateBack()
    fun navigateToEditProfile()
    fun navigateToAbout()
    fun navigateToAuth()

    fun navigateToDevTools()

    fun navigateToSurvey(surveyId: String)

    companion object {
        val Empty: ProfileScreenNavAction = EmptyProfileScreenNavAction
    }
}

private object EmptyProfileScreenNavAction : ProfileScreenNavAction {
    override fun navigateBack() = Unit
    override fun navigateToEditProfile() = Unit
    override fun navigateToAbout() = Unit
    override fun navigateToAuth() = Unit
    override fun navigateToDevTools() = Unit
    override fun navigateToSurvey(surveyId: String) = Unit
}
