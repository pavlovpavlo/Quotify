package com.kovhan.feature.main.presentation.quotes.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.feature.main.presentation.quotes.QuotesScreen
import com.kovhan.feature.main.presentation.quotes.QuotesScreenViewModel
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenEffect

// Константи для швидших анімацій
private const val ANIMATION_DURATION = 150 // Мілісекунди

internal fun NavGraphBuilder.quotesScreen(
    navAction: QuotesScreenNavAction,
    paddingValues: PaddingValues
) {
    composable<MainGraph.QuotesScreen>{
        val viewModel = hiltViewModel<QuotesScreenViewModel>()
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is QuotesScreenEffect.None -> {
                        // Нічого не робимо
                    }
                    else -> {
                        // Обробка інших ефектів у майбутньому
                    }
                }
            }
        }
        
        QuotesScreen(
            state = state.value,
            intent = viewModel,
            navAction = navAction,
            paddingValues = paddingValues
        )
    }
} 