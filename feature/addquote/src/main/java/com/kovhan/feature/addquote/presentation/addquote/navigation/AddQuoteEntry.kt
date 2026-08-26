package com.kovhan.feature.addquote.presentation.addquote.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovhan.core.navigation.AddQuoteKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.models.PaywallOrigin
import com.kovhan.core.navigation.QuoteDetailsKey
import com.kovhan.core.navigation.models.QuoteInputMethod
import com.kovhan.feature.addquote.presentation.addquote.AddQuoteScreen
import com.kovhan.feature.addquote.presentation.addquote.AddQuoteScreenViewModel
import com.kovhan.feature.addquote.presentation.addquote.mvi.AddQuoteScreenEffect

@Composable
internal fun AddQuoteEntry(
    key: AddQuoteKey,
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    val viewModel = hiltViewModel<AddQuoteScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val navAction = object : AddQuoteScreenNavAction {
        override fun close() {
            coordinator.goBack()
        }

        override fun proceedToDetails(quote: String, inputMethod: QuoteInputMethod) {
            coordinator.navigate(
                QuoteDetailsKey(
                    quote = quote,
                    inputMethod = inputMethod,
                    collectionId = key.collectionId,
                ),
            )
        }

        override fun openPaywall() {
            coordinator.navigate(PaywallKey(PaywallOrigin.LIMIT))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onInitialTab(key.tab, key.entryPoint)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                AddQuoteScreenEffect.Close -> navAction.close()
                is AddQuoteScreenEffect.ProceedToDetails ->
                    navAction.proceedToDetails(effect.quote, effect.inputMethod)
                AddQuoteScreenEffect.OpenPaywall -> navAction.openPaywall()
            }
        }
    }

    AddQuoteScreen(
        state = state.value,
        intent = viewModel,
        paddingValues = paddingValues,
    )
}
