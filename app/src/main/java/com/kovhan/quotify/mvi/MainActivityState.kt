package com.kovhan.quotify.mvi

import com.kovhan.core.ui.UiState
import com.kovhan.domain.premium.OfferTrigger
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme

data class MainActivityState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: AppLanguage = AppLanguage.default,
    val isFabTooltipVisible: Boolean = false,
    val pendingOfferTrigger: OfferTrigger? = null,
    val pendingSurveyId: String? = null,
) : UiState
