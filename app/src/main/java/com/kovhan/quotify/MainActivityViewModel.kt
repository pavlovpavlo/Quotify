package com.kovhan.quotify

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.settings.use_case.GetThemeUseCase
import com.kovhan.quotify.mvi.MainActivityEffect
import com.kovhan.quotify.mvi.MainActivityState
import com.kovhan.quotify.mvi.MainIntent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    getTheme: GetThemeUseCase,
    getLanguage: GetLanguageUseCase,
) : BaseViewModel<MainActivityState, MainActivityEffect>(MainActivityState()), MainIntent {

    init {
        getTheme()
            .onEach { theme -> publishState { copy(theme = theme) } }
            .launchIn(viewModelScope)

        getLanguage()
            .onEach { language -> publishState { copy(language = language) } }
            .launchIn(viewModelScope)
    }
}
