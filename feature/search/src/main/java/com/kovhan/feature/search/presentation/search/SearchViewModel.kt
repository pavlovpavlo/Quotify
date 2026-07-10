package com.kovhan.feature.search.presentation.search

import com.kovhan.core.models.EnrichedQuote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.models.SavedTag
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.author.ObserveSavedAuthorsUseCase
import com.kovhan.domain.library.use_case.book.ObserveSavedBooksUseCase
import com.kovhan.domain.library.use_case.collection.ObserveCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuotesUseCase
import com.kovhan.domain.library.use_case.search.SearchLibraryUseCase
import com.kovhan.domain.library.use_case.tag.ObserveSavedTagsUseCase
import com.kovhan.feature.search.presentation.search.mvi.SearchEffect
import com.kovhan.feature.search.presentation.search.mvi.SearchScope
import com.kovhan.feature.search.presentation.search.mvi.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    observeEnrichedQuotes: ObserveEnrichedQuotesUseCase,
    observeCollections: ObserveCollectionsUseCase,
    observeSavedBooks: ObserveSavedBooksUseCase,
    observeSavedAuthors: ObserveSavedAuthorsUseCase,
    observeSavedTags: ObserveSavedTagsUseCase,
    private val searchLibrary: SearchLibraryUseCase,
) : BaseViewModel<SearchState, SearchEffect>(SearchState(isLoading = true)),
    SearchScreenAction {

    private val library = combine(
        observeEnrichedQuotes(),
        observeCollections(),
        observeSavedBooks(withCount = true),
        observeSavedAuthors(withCount = true),
        observeSavedTags(withCount = true),
    ) { quotes, folders, books, authors, tags ->
        LibrarySnapshot(quotes, folders, books, authors, tags)
    }

    private val query = uiState.map { it.query }.distinctUntilChanged()

    init {
        combine(library, query) { snapshot, query ->
            searchLibrary(
                query = query,
                quotes = snapshot.quotes,
                folders = snapshot.folders,
                books = snapshot.books,
                authors = snapshot.authors,
                tags = snapshot.tags,
            )
        }
            .onEach { results -> publishState { copy(results = results, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    override fun onQueryChange(query: String) = publishState { copy(query = query) }

    override fun onScopeChange(scope: SearchScope) = publishState { copy(scope = scope) }

    private data class LibrarySnapshot(
        val quotes: List<EnrichedQuote>,
        val folders: List<SavedCollection>,
        val books: List<SavedBook>,
        val authors: List<SavedAuthor>,
        val tags: List<SavedTag>,
    )
}
