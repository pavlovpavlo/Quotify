package com.kovhan.feature.survey.presentation.invite.mvi

import com.kovhan.core.models.survey.SurveyInvite
import com.kovhan.core.ui.UiEffect
import com.kovhan.core.ui.UiState

data class SurveyInviteState(
    val invite: SurveyInvite? = null,
) : UiState

sealed interface SurveyInviteEffect : UiEffect
