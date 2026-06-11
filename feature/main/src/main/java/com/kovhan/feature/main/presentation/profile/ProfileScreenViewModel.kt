package com.kovhan.feature.main.presentation.profile

import com.kovhan.core.ui.activity.RateAppUseCase
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.auth.use_case.GetUserUseCase
import com.kovhan.domain.auth.use_case.SignOutUseCase
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.settings.use_case.GetThemeUseCase
import com.kovhan.domain.settings.use_case.SetLanguageUseCase
import com.kovhan.domain.settings.use_case.SetThemeUseCase
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenEffect
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenIntent
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    getUser: GetUserUseCase,
    getTheme: GetThemeUseCase,
    getLanguage: GetLanguageUseCase,
    private val setTheme: SetThemeUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val signOut: SignOutUseCase,
    private val rateApp: RateAppUseCase,
) : BaseViewModel<ProfileScreenState, ProfileScreenEffect>(ProfileScreenState()),
    ProfileScreenIntent {

    init {
        getUser()
            .onEach { user -> publishState { copy(user = user) } }
            .launchIn(viewModelScope)

        getTheme()
            .onEach { theme -> publishState { copy(theme = theme) } }
            .launchIn(viewModelScope)

        getLanguage()
            .onEach { language -> publishState { copy(language = language) } }
            .launchIn(viewModelScope)
    }

    override fun onSignOutClicked() {
        if (uiState.value.isSigningOut) return
        publishState { copy(isSigningOut = true) }
        viewModelScope.launch {
            signOut()
            publishState { copy(isSigningOut = false) }
            publishEffect(ProfileScreenEffect.NavigateToAuth)
        }
    }

    override fun onUpgradeClicked() = Unit

    override fun onRateClicked() = rateApp()

    override fun onCreateWidgetClicked() = Unit

    override fun onShowQuoteOfDayToggled(enabled: Boolean) = publishState { copy(showQuoteOfDay = enabled) }

    override fun onNotificationsToggled(enabled: Boolean) = publishState { copy(notificationsEnabled = enabled) }

    override fun onReminderTimeClicked() = publishState { copy(isTimePickerVisible = true) }

    override fun onReminderTimeSelected(hour: Int, minute: Int) =
        publishState { copy(reminderHour = hour, reminderMinute = minute) }

    override fun onTimePickerDismissed() = publishState { copy(isTimePickerVisible = false) }

    override fun onAppearanceClicked() = publishState { copy(isThemeSheetVisible = true) }

    override fun onThemeSelected(theme: AppTheme) {
        viewModelScope.launch { setTheme(theme) }
        publishState { copy(isThemeSheetVisible = false) }
    }

    override fun onThemeSheetDismissed() = publishState { copy(isThemeSheetVisible = false) }

    override fun onLanguageClicked() = publishState { copy(isLanguageSheetVisible = true) }

    override fun onLanguageSelected(language: AppLanguage) {
        publishState { copy(isLanguageSheetVisible = false) }
        viewModelScope.launch { setLanguage(language) }
    }

    override fun onLanguageSheetDismissed() = publishState { copy(isLanguageSheetVisible = false) }
}
