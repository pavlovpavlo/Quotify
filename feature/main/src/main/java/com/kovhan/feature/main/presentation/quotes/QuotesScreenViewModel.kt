package com.kovhan.feature.main.presentation.quotes

import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenEffect
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenIntent
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenState
import javax.inject.Inject

class QuotesScreenViewModel @Inject constructor() : 
    BaseViewModel<QuotesScreenState, QuotesScreenEffect>(QuotesScreenState()), 
    QuotesScreenIntent {
} 