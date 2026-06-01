package com.kovhan.feature.auth.presentation.complete.mvi

import com.kovhan.core.ui.UiEffect

sealed class CompleteScreenEffect : UiEffect {
    data object NavigateToSignUp : CompleteScreenEffect()
    data object NavigateToSignIn : CompleteScreenEffect()
    /** "Зроблю пізніше" — skip auth and drop the user into the app. */
    data object NavigateToMain : CompleteScreenEffect()
}
