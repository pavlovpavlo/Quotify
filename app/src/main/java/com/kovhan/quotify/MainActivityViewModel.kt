package com.kovhan.quotify

import com.kovhan.core.ui.snackbar.SnackbarMessage
import com.kovhan.core.ui.snackbar.SnackbarMessageSource
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.onboarding.use_case.GetFabTooltipDismissedUseCase
import com.kovhan.domain.onboarding.use_case.SetFabTooltipDismissedUseCase
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.settings.use_case.GetThemeUseCase
import com.kovhan.quotify.mvi.MainActivityEffect
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    getTheme: GetThemeUseCase,
    getLanguage: GetLanguageUseCase,
    getFabTooltipDismissed: GetFabTooltipDismissedUseCase,
    private val setFabTooltipDismissed: SetFabTooltipDismissedUseCase,
    snackbarMessageSource: SnackbarMessageSource,
) : BaseViewModel<MainActivityState, MainActivityEffect>(MainActivityState()), MainIntent {

    val snackbarMessages: Flow<SnackbarMessage> = snackbarMessageSource.messages

    private var wasDockVisible = false

    init {
        getTheme()
            .onEach { theme -> publishState { copy(theme = theme) } }
            .launchIn(viewModelScope)

        getLanguage()
            .onEach { language -> publishState { copy(language = language) } }
            .launchIn(viewModelScope)

        getFabTooltipDismissed()
            .onEach { dismissed -> publishState { copy(isFabTooltipVisible = !dismissed) } }
            .launchIn(viewModelScope)
    }

    override fun onFabClicked() = dismissFabTooltip()

    override fun onDockVisibilityChanged(visible: Boolean) {
        if (wasDockVisible && !visible) dismissFabTooltip()
        wasDockVisible = visible
    }

    private fun dismissFabTooltip() {
        if (!uiState.value.isFabTooltipVisible) return
        viewModelScope.launch { setFabTooltipDismissed() }
    }
}
