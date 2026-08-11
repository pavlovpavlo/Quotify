package com.kovhan.feature.main.presentation.profile.mvi

import com.kovhan.core.ui.UiEffect

sealed class ProfileScreenEffect : UiEffect {
    data object NavigateToAuth : ProfileScreenEffect()
    data object OpenThemeSheet : ProfileScreenEffect()
    data object OpenLanguageSheet : ProfileScreenEffect()
    data object OpenReminderSheet : ProfileScreenEffect()
    data object OpenWidgetSettings : ProfileScreenEffect()
    data object OpenPaywall : ProfileScreenEffect()
    data object OpenSubscription : ProfileScreenEffect()
}
