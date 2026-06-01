package com.kovhan.feature.auth.presentation.complete.mvi

interface CompleteScreenIntent {
    fun onSignUpClicked()
    fun onSignInClicked()
    fun onLaterClicked()

    companion object {
        val Empty: CompleteScreenIntent = object : CompleteScreenIntent {
            override fun onSignUpClicked() = Unit
            override fun onSignInClicked() = Unit
            override fun onLaterClicked() = Unit
        }
    }
}
