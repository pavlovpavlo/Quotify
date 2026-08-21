package com.kovhan.feature.main.presentation.profile

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.DailyQuoteResult
import com.kovhan.core.analytics.event.HideDailyQuoteFinished
import com.kovhan.core.analytics.event.HideDailyQuoteInitiated
import com.kovhan.core.analytics.event.RateUsInitiated
import com.kovhan.core.analytics.event.VisitProfile
import com.kovhan.core.ui.activity.ContactSupportUseCase
import com.kovhan.core.ui.activity.RateAppUseCase
import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.design.systems.R
import com.kovhan.domain.auth.use_case.GetUserUseCase
import com.kovhan.domain.auth.use_case.SignOutUseCase
import com.kovhan.domain.billing.use_case.ObserveIsSubscribedUseCase
import com.kovhan.domain.feedback.use_case.CanSubmitFeedbackUseCase
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import com.kovhan.domain.daily.use_case.DismissDailyQuoteForTodayUseCase
import com.kovhan.domain.settings.use_case.GetDailyQuoteEnabledUseCase
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.settings.use_case.GetProfileStatisticUseCase
import com.kovhan.domain.settings.use_case.GetThemeUseCase
import com.kovhan.domain.settings.use_case.SetDailyQuoteEnabledUseCase
import com.kovhan.domain.settings.use_case.SetLanguageUseCase
import com.kovhan.domain.settings.use_case.SetThemeUseCase
import com.kovhan.domain.survey.use_case.ObservePostponedSurveyUseCase
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
    getDailyQuoteEnabled: GetDailyQuoteEnabledUseCase,
    private val setTheme: SetThemeUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val setDailyQuoteEnabled: SetDailyQuoteEnabledUseCase,
    private val dismissDailyQuoteForToday: DismissDailyQuoteForTodayUseCase,
    private val signOut: SignOutUseCase,
    private val rateApp: RateAppUseCase,
    private val contactSupport: ContactSupportUseCase,
    private val canSubmitFeedback: CanSubmitFeedbackUseCase,
    observePostponedSurvey: ObservePostponedSurveyUseCase,
    observeIsSubscribed: ObserveIsSubscribedUseCase,
    private val analytics: AnalyticsTracker,
    getProfileStatisticUseCase: GetProfileStatisticUseCase
) : BaseViewModel<ProfileScreenState, ProfileScreenEffect>(ProfileScreenState()),
    ProfileScreenIntent {

    init {
        analytics.track(VisitProfile)

        // Профіль лише слухає кеш — синком займається SubscriptionSyncManager.
        observeIsSubscribed()
            .onEach { premium -> publishState { copy(isPremium = premium) } }
            .launchIn(viewModelScope)

        getUser()
            .onEach { user -> publishState { copy(user = user) } }
            .launchIn(viewModelScope)

        getTheme()
            .onEach { theme -> publishState { copy(theme = theme) } }
            .launchIn(viewModelScope)

        getLanguage()
            .onEach { language -> publishState { copy(language = language) } }
            .launchIn(viewModelScope)

        getDailyQuoteEnabled()
            .onEach { enabled -> publishState { copy(showQuoteOfDay = enabled) } }
            .launchIn(viewModelScope)

        getProfileStatisticUseCase()
            .onEach { statistic -> publishState { copy(stats = statistic) } }
            .launchIn(viewModelScope)

        observePostponedSurvey()
            .onEach { surveyId -> publishState { copy(postponedSurveyId = surveyId) } }
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

    override fun onUpgradeClicked() = publishEffect(
        if (uiState.value.isPremium) {
            ProfileScreenEffect.OpenSubscription
        } else {
            ProfileScreenEffect.OpenPaywall
        },
    )

    override fun onRateClicked() {
        analytics.track(RateUsInitiated)
        rateApp()
    }

    override fun onFeedbackClicked() {
        viewModelScope.launch {
            publishEffect(
                if (canSubmitFeedback()) {
                    ProfileScreenEffect.OpenFeedbackDialog
                } else {
                    ProfileScreenEffect.ShowFeedbackThrottled
                },
            )
        }
    }

    override fun onSupportClicked() {
        if (!contactSupport()) {
            showSnackbar(SnackbarMessage.error(R.string.support_email_no_app))
        }
    }

    override fun onCreateWidgetClicked() = publishEffect(ProfileScreenEffect.OpenWidgetSettings)

    override fun onShowQuoteOfDayToggled(enabled: Boolean) {
        // Вимкнення проходить через той самий діалог, що й приховування цитати
        // дня в бібліотеці — там ще є варіант сховати лише на сьогодні.
        if (!enabled) {
            analytics.track(HideDailyQuoteInitiated(""))
            publishEffect(ProfileScreenEffect.ConfirmHideDailyQuote)
            return
        }
        publishState { copy(showQuoteOfDay = true) }
        viewModelScope.launch { setDailyQuoteEnabled(true) }
    }

    fun onHideDailyQuoteForever() {
        analytics.track(HideDailyQuoteFinished("", DailyQuoteResult.REMOVE))
        publishState { copy(showQuoteOfDay = false) }
        viewModelScope.launch { setDailyQuoteEnabled(false) }
    }

    fun onHideDailyQuoteToday() {
        analytics.track(HideDailyQuoteFinished("", DailyQuoteResult.HIDE_FOR_TODAY))
        viewModelScope.launch { dismissDailyQuoteForToday() }
    }

    fun onDailyQuoteHideCancelled() =
        analytics.track(HideDailyQuoteFinished("", DailyQuoteResult.KEEP))

    override fun onNotificationsToggled(enabled: Boolean) =
        publishState { copy(notificationsEnabled = enabled) }

    override fun onReminderTimeClicked() = publishEffect(ProfileScreenEffect.OpenReminderSheet)

    override fun onReminderTimeSelected(hour: Int, minute: Int) =
        publishState { copy(reminderHour = hour, reminderMinute = minute) }

    override fun onAppearanceClicked() = publishEffect(ProfileScreenEffect.OpenThemeSheet)

    override fun onThemeSelected(theme: AppTheme) {
        viewModelScope.launch { setTheme(theme) }
    }

    override fun onLanguageClicked() = publishEffect(ProfileScreenEffect.OpenLanguageSheet)

    override fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch { setLanguage(language) }
    }
}
