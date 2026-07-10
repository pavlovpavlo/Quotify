package com.kovhan.feature.main.presentation.quotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import com.kovhan.feature.main.presentation.quotes.component.LibraryFoldersSection
import com.kovhan.feature.main.presentation.quotes.component.LibrarySearchBar
import com.kovhan.feature.main.presentation.quotes.component.QuoteOfTheDayCard
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenIntent
import com.kovhan.feature.main.presentation.quotes.mvi.QuotesScreenState
import com.kovhan.feature.main.presentation.quotes.navigation.QuotesScreenNavAction

@Composable
fun QuotesScreen(
    state: QuotesScreenState,
    intent: QuotesScreenIntent,
    navAction: QuotesScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography
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

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensions.size2, bottom = dimensions.size12),
                text = stringResource(R.string.library_title),
                style = typography.h3,
                color = colors.textPrimary,
            )

            LibrarySearchBar(onClick = navAction::openSearch)

            Spacer(Modifier.height(dimensions.size14))

            if (state.dailyQuote != null) {
                QuoteOfTheDayCard(
                    text = state.displayText,
                    author = state.displayAuthor,
                    book = state.displayBook,
                    isFavourite = state.isDailyQuoteFavourite,
                    isFavouriteLoading = state.isDailyQuoteFavouriteLoading,
                    onClose = navAction::showHideDailyQuoteDialog,
                    onToggleFavourite = { intent.onToggleDailyQuoteFavourite(favouritesName) },
                )
                Spacer(Modifier.height(dimensions.size16))
            }

            LibraryFoldersSection(
                folders = state.folders,
                onOpenFolder = navAction::openFolder,
                onCreateFolder = navAction::createFolder,
            )

            Spacer(Modifier.height(dimensions.size96 + dimensions.size12))
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
        }
    }
}
