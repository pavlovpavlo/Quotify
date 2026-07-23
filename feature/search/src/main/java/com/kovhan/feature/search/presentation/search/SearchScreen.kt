package com.kovhan.feature.search.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.search.presentation.search.component.SearchField
import com.kovhan.feature.search.presentation.search.component.SearchResults
import com.kovhan.feature.search.presentation.search.component.SearchScopeCounts
import com.kovhan.feature.search.presentation.search.component.SearchScopesRow
import com.kovhan.feature.search.presentation.search.component.SearchTopBar
import com.kovhan.feature.search.presentation.search.mvi.SearchState
import com.kovhan.feature.search.presentation.search.navigation.SearchScreenNavAction

@Composable
internal fun SearchScreen(
    state: SearchState,
    action: SearchScreenAction,
    navAction: SearchScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val results = state.results

    BackHandler(enabled = true, onBack = navAction::onClose)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
            ),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchTopBar(onClose = navAction::onClose)

            SearchField(
                query = state.query,
                onQueryChange = action::onQueryChange,
                onClear = { action.onQueryChange("") },
            )

            SearchScopesRow(
                scope = state.scope,
                onScopeChange = action::onScopeChange,
                counts = SearchScopeCounts(
                    quotes = results.quotes.size,
                    folders = results.folders.size,
                    tags = results.tags.size,
                    books = results.books.size,
                    authors = results.authors.size,
                ),
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = colors.accentPrimary)
                }
            } else {
                SearchResults(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    scope = state.scope,
                    query = state.query.trim(),
                    quotes = results.quotes,
                    folders = results.folders,
                    books = results.books,
                    authors = results.authors,
                    tags = results.tags,
                    navAction = navAction,
                )
            }
        }
    }
}
