package com.kovhan.feature.search.presentation.search

import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.core.navigation.QuoteEditDraft
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.author.ObserveSavedAuthorsUseCase
import com.kovhan.domain.library.use_case.book.ObserveSavedBooksUseCase
import com.kovhan.domain.library.use_case.collection.ObserveCollectionsUseCase
import com.kovhan.domain.library.use_case.quote.DeleteQuoteUseCase
import com.kovhan.domain.library.use_case.quote.GetQuoteByIdUseCase
import com.kovhan.domain.library.use_case.quote.MoveQuoteToCollectionUseCase
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuotesUseCase
import com.kovhan.domain.library.use_case.quote.UpsertQuoteUseCase
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    observeEnrichedQuotes: ObserveEnrichedQuotesUseCase,
    observeCollections: ObserveCollectionsUseCase,
    observeSavedBooks: ObserveSavedBooksUseCase,
    observeSavedAuthors: ObserveSavedAuthorsUseCase,
    observeSavedTags: ObserveSavedTagsUseCase,
    private val searchLibrary: SearchLibraryUseCase,
    private val getQuoteById: GetQuoteByIdUseCase,
    private val upsertQuote: UpsertQuoteUseCase,
    private val moveQuoteToCollection: MoveQuoteToCollectionUseCase,
    private val deleteQuote: DeleteQuoteUseCase,
) : BaseViewModel<SearchState, SearchEffect>(SearchState(isLoading = true)),
    SearchScreenAction {

    private var generalName: String = ""
    private var authorOptions: List<String> = emptyList()
    private var bookOptions: List<String> = emptyList()
    private var tagPool: List<String> = emptyList()

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
        library
            .onEach { snapshot ->
                authorOptions = snapshot.authors.map { it.name }
                bookOptions = snapshot.books.map { it.name }
                tagPool = snapshot.tags.map { it.name }
            }
            .launchIn(viewModelScope)

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

    fun bind(generalName: String) {
        this.generalName = generalName
    }

    override fun onQueryChange(query: String) = publishState { copy(query = query) }

    override fun onScopeChange(scope: SearchScope) = publishState { copy(scope = scope) }

    override fun onQuoteMenuToggled(quoteId: String) = publishState {
        copy(expandedQuoteMenuId = quoteId.takeIf { it != expandedQuoteMenuId })
    }

    override fun onQuoteMenuDismissed() = publishState { copy(expandedQuoteMenuId = null) }

    override fun onEditQuoteRequested(quote: EnrichedQuote) {
        publishState { copy(expandedQuoteMenuId = null) }
        publishEffect(
            SearchEffect.OpenQuoteEditor(
                draft = QuoteEditDraft(
                    quoteId = quote.id,
                    text = quote.text,
                    authorName = quote.author?.name.orEmpty(),
                    bookName = quote.book?.name.orEmpty(),
                    tags = quote.tags.map { it.name },
                    inWidgetPlaylist = quote.inWidgetPlaylist,
                    inPushPlaylist = quote.inPushPlaylist,
                    page = quote.page?.toString().orEmpty(),
                ),
                authorOptions = authorOptions,
                bookOptions = bookOptions,
                tagPool = tagPool,
            ),
        )
    }

    override fun onMoveQuoteRequested(quoteId: String) {
        publishState { copy(expandedQuoteMenuId = null) }
        viewModelScope.launch {
            publishEffect(
                SearchEffect.OpenMoveQuoteSheet(
                    quoteId = quoteId,
                    keepsFavourite = getQuoteById(quoteId)?.isFavourite == true,
                ),
            )
        }
    }

    override fun onDeleteQuoteRequested(quoteId: String) {
        publishState { copy(expandedQuoteMenuId = null) }
        publishEffect(SearchEffect.OpenDeleteQuoteDialog(quoteId))
    }

    fun onEditQuoteSaved(draft: QuoteEditDraft) {
        if (draft.text.isBlank()) return
        viewModelScope.launch {
            val existing = getQuoteById(draft.quoteId)
            upsertQuote(
                id = draft.quoteId,
                text = draft.text,
                authorName = draft.authorName.ifBlank { null },
                bookName = draft.bookName.ifBlank { null },
                tagNames = draft.tags,
                collectionId = existing?.collectionId,
                inWidgetPlaylist = draft.inWidgetPlaylist,
                inPushPlaylist = draft.inPushPlaylist,
                generalName = generalName,
                sourceDailyId = existing?.sourceDailyId,
                page = draft.page.toIntOrNull(),
                isFavourite = existing?.isFavourite == true,
            )
        }
    }

    fun onMoveQuoteConfirmed(quoteId: String, targetCollectionId: String) {
        viewModelScope.launch {
            moveQuoteToCollection(
                quoteId = quoteId,
                targetCollectionId = targetCollectionId,
                generalName = generalName,
            )
        }
    }

    fun onDeleteQuoteConfirmed(quoteId: String) {
        viewModelScope.launch { deleteQuote(quoteId) }
    }

    private data class LibrarySnapshot(
        val quotes: List<EnrichedQuote>,
        val folders: List<SavedCollection>,
        val books: List<SavedBook>,
        val authors: List<SavedAuthor>,
        val tags: List<SavedTag>,
    )
}
