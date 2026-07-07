package com.kovhan.feature.main.presentation.quotes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.quotes.component.QuoteOfTheDayCard
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenIntent
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenState
import com.kovhan.feature.main.presentation.quotes.navigation.QuotesScreenNavAction

private val FolioEasing = CubicBezierEasing(0.22f, 0.61f, 0.36f, 1f)
private const val REVEAL_DURATION_MS = 260

@Composable
fun QuotesScreen(
    state: QuotesScreenState,
    intent: QuotesScreenIntent,
    navAction: QuotesScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val favouritesName = stringResource(R.string.collection_favourites)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(paddingValues),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensions.screenPadding),
        ) {
            Spacer(Modifier.height(dimensions.size16))

            if (state.dailyQuote != null) {
                val revealState = remember { MutableTransitionState(false).apply { targetState = true } }
                AnimatedVisibility(
                    visibleState = revealState,
                    enter = fadeIn(tween(REVEAL_DURATION_MS)) +
                        expandVertically(
                            animationSpec = tween(REVEAL_DURATION_MS, easing = FolioEasing),
                            expandFrom = Alignment.Top,
                        ),
                    exit = fadeOut(tween(REVEAL_DURATION_MS)) +
                        shrinkVertically(
                            animationSpec = tween(REVEAL_DURATION_MS, easing = FolioEasing),
                            shrinkTowards = Alignment.Top,
                        ),
                ) {
                    QuoteOfTheDayCard(
                        text = state.displayText,
                        author = state.displayAuthor,
                        book = state.displayBook,
                        isFavourite = state.isDailyQuoteFavourite,
                        isFavouriteLoading = state.isDailyQuoteFavouriteLoading,
                        onClose = navAction::showHideDailyQuoteDialog,
                        onToggleFavourite = { intent.onToggleDailyQuoteFavourite(favouritesName) },
                    )
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
        }
    }
}
