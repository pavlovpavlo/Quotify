package com.kovhan.feature.main.presentation.profile.mvi

import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme

interface ProfileScreenIntent {
    fun onSignOutClicked()
    fun onUpgradeClicked()
    fun onRateClicked()
    fun onFeedbackClicked()
    fun onSupportClicked()
    fun onCreateWidgetClicked()
    fun onShowQuoteOfDayToggled(enabled: Boolean)
    fun onNotificationsToggled(enabled: Boolean)
    fun onReminderTimeClicked()
    fun onReminderTimeSelected(hour: Int, minute: Int)
    fun onAppearanceClicked()
    fun onThemeSelected(theme: AppTheme)
    fun onLanguageClicked()
    fun onLanguageSelected(language: AppLanguage)

    companion object {
        val Empty: ProfileScreenIntent = object : ProfileScreenIntent {
            override fun onSignOutClicked() = Unit
            override fun onUpgradeClicked() = Unit
            override fun onRateClicked() = Unit
            override fun onFeedbackClicked() = Unit
            override fun onSupportClicked() = Unit
            override fun onCreateWidgetClicked() = Unit
            override fun onShowQuoteOfDayToggled(enabled: Boolean) = Unit
            override fun onNotificationsToggled(enabled: Boolean) = Unit
            override fun onReminderTimeClicked() = Unit
            override fun onReminderTimeSelected(hour: Int, minute: Int) = Unit
            override fun onAppearanceClicked() = Unit
            override fun onThemeSelected(theme: AppTheme) = Unit
            override fun onLanguageClicked() = Unit
            override fun onLanguageSelected(language: AppLanguage) = Unit
        }
    }
}
