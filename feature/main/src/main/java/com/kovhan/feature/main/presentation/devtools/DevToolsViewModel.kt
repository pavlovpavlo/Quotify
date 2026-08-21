package com.kovhan.feature.main.presentation.devtools

import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.design.systems.R
import com.kovhan.domain.survey.use_case.ResetSurveyStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data object DevToolsState : UiState

sealed interface DevToolsEffect : UiEffect

@HiltViewModel
class DevToolsViewModel @Inject constructor(
    private val resetSurveyState: ResetSurveyStateUseCase,
) : BaseViewModel<DevToolsState, DevToolsEffect>(DevToolsState) {

    fun onResetSurveyStateClicked() {
        viewModelScope.launch {
            resetSurveyState()
            showSnackbar(R.string.dev_tools_survey_reset_done)
        }
    }
}
