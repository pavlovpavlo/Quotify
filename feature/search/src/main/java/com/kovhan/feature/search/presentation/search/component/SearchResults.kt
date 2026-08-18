package com.kovhan.feature.search.presentation.search.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.feature.search.presentation.search.SearchScreenAction
import com.kovhan.feature.search.presentation.search.mvi.SearchScope
import com.kovhan.feature.search.presentation.search.navigation.SearchScreenNavAction

@Composable
internal fun SearchResults(
    scope: SearchScope,
    query: String,
    quotes: List<EnrichedQuote>,
    folders: List<SavedCollection>,
    books: List<SavedBook>,
    authors: List<SavedAuthor>,
    tags: List<SavedTag>,
    expandedQuoteMenuId: String?,
    action: SearchScreenAction,
    navAction: SearchScreenNavAction,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (scope) {
            SearchScope.QUOTES -> SearchQuoteResults(
                quotes = quotes,
                query = query,
                expandedMenuId = expandedQuoteMenuId,
                action = action,
            )
            SearchScope.FOLDERS -> SearchFolderResults(
                folders = folders,
                navAction = navAction,
            )
            SearchScope.BOOKS -> SearchBookResults(
                books = books,
                navAction = navAction,
            )
            SearchScope.AUTHORS -> SearchAuthorResults(
                authors = authors,
                navAction = navAction,
            )
            SearchScope.TAGS -> SearchTagResults(
                tags = tags,
                navAction = navAction,
            )
        }
    }
}
