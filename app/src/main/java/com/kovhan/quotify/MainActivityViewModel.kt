package com.kovhan.quotify

import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.snackbar.SnackbarMessageSource
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.onboarding.use_case.GetFabTooltipDismissedUseCase
import com.kovhan.domain.billing.use_case.GetSpecialOfferUseCase
import com.kovhan.domain.library.use_case.sync.SyncLibraryUseCase
import com.kovhan.domain.onboarding.use_case.SetFabTooltipDismissedUseCase
import com.kovhan.domain.premium.use_case.MarkOfferShownUseCase
import com.kovhan.domain.premium.use_case.ResolveOfferTriggerUseCase
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.settings.use_case.GetThemeUseCase
import com.kovhan.domain.survey.use_case.ResolveSurveyInviteUseCase
import com.kovhan.quotify.mvi.MainActivityEffect
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    getTheme: GetThemeUseCase,
    getLanguage: GetLanguageUseCase,
    getFabTooltipDismissed: GetFabTooltipDismissedUseCase,
    private val setFabTooltipDismissed: SetFabTooltipDismissedUseCase,
    private val resolveOfferTrigger: ResolveOfferTriggerUseCase,
    private val resolveSurveyInvite: ResolveSurveyInviteUseCase,
    private val getSpecialOffer: GetSpecialOfferUseCase,
    private val markOfferShown: MarkOfferShownUseCase,
    private val syncLibrary: SyncLibraryUseCase,
    snackbarMessageSource: SnackbarMessageSource,
) : BaseViewModel<MainActivityState, MainActivityEffect>(MainActivityState()), MainIntent {

    val snackbarMessages: Flow<SnackbarMessage> = snackbarMessageSource.messages

    private var wasDockVisible = false
    private var autoDismissJob: Job? = null

    init {
        getTheme()
            .onEach { theme -> publishState { copy(theme = theme) } }
            .launchIn(viewModelScope)

        getLanguage()
            .onEach { language -> publishState { copy(language = language) } }
            .launchIn(viewModelScope)

        getFabTooltipDismissed()
            .onEach { dismissed ->
                publishState { copy(isFabTooltipVisible = !dismissed) }
                if (!dismissed && wasDockVisible) scheduleFabTooltipAutoDismiss()
            }
            .launchIn(viewModelScope)
    }

    override fun onAppForegrounded() {
        viewModelScope.launch {
            runCatching { syncLibrary() }
                .onFailure { Timber.w(it, "Library sync on foreground failed") }
        }

        viewModelScope.launch {
            val trigger = runCatching { resolveOfferTrigger() }.getOrNull()
            if (trigger != null && runCatching { getSpecialOffer() }.getOrNull() != null) {
                publishState { copy(pendingOfferTrigger = trigger) }
                return@launch
            }

            val survey = runCatching { resolveSurveyInvite() }
                .onFailure { Timber.w(it, "Survey invite resolution failed") }
                .getOrNull() ?: return@launch
            publishState { copy(pendingSurveyId = survey.id) }
        }
    }

    override fun onOfferShown() {
        val trigger = uiState.value.pendingOfferTrigger ?: return
        publishState { copy(pendingOfferTrigger = null) }
        viewModelScope.launch { markOfferShown(trigger) }
    }

    override fun onSurveyInviteShown() {
        publishState { copy(pendingSurveyId = null) }
    }

    override fun onFabClicked() = dismissFabTooltip()

    override fun onDockVisibilityChanged(visible: Boolean) {
        if (wasDockVisible && !visible) dismissFabTooltip()
        if (!wasDockVisible && visible && uiState.value.isFabTooltipVisible) {
            scheduleFabTooltipAutoDismiss()
        }
        wasDockVisible = visible
    }

    private fun scheduleFabTooltipAutoDismiss() {
        autoDismissJob?.cancel()
        autoDismissJob = viewModelScope.launch {
            delay(FAB_TOOLTIP_VISIBLE_MS)
            dismissFabTooltip()
        }
    }

    private fun dismissFabTooltip() {
        if (!uiState.value.isFabTooltipVisible) return
        autoDismissJob?.cancel()
        viewModelScope.launch { setFabTooltipDismissed() }
    }

    private companion object {
        const val FAB_TOOLTIP_VISIBLE_MS = 5_000L
    }
}
