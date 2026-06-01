package com.kovhan.feature.main.presentation.profile.mvi

interface ProfileScreenIntent {
    fun onSignOutClicked()

    companion object {
        val Empty: ProfileScreenIntent = object : ProfileScreenIntent {
            override fun onSignOutClicked() = Unit
        }
    }
}
