package com.kovhan.feature.survey.presentation.survey.mvi

import com.kovhan.core.ui.UiEffect

sealed interface SurveyEffect : UiEffect {
    data object Exit : SurveyEffect

    /** Хрестик посеред опитування — питаємо, це «пізніше» чи «більше не показувати». */
    data object ConfirmExit : SurveyEffect
    data object OpenWidgetSettings : SurveyEffect
}
