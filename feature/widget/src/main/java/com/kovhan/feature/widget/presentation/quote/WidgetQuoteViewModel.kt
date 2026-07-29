package com.kovhan.feature.widget.presentation.quote

import com.kovhan.core.navigation.QuoteEditDraft
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.author.ObserveSavedAuthorsUseCase
import com.kovhan.domain.library.use_case.book.ObserveSavedBooksUseCase
import com.kovhan.domain.library.use_case.quote.DeleteQuoteUseCase
import com.kovhan.domain.library.use_case.quote.GetQuoteByIdUseCase
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuoteByIdUseCase
import com.kovhan.domain.library.use_case.quote.UpsertQuoteUseCase
import com.kovhan.domain.library.use_case.tag.ObserveSavedTagsUseCase
import com.kovhan.domain.widget.use_case.content.HandleWidgetQuoteRemovalUseCase
import com.kovhan.feature.widget.presentation.quote.mvi.WidgetQuoteEffect
import com.kovhan.feature.widget.presentation.quote.mvi.WidgetQuoteIntent
import com.kovhan.feature.widget.presentation.quote.mvi.WidgetQuoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetQuoteViewModel @Inject constructor(
    private val observeEnrichedById: ObserveEnrichedQuoteByIdUseCase,
    observeSavedAuthors: ObserveSavedAuthorsUseCase,
    observeSavedBooks: ObserveSavedBooksUseCase,
    observeSavedTags: ObserveSavedTagsUseCase,
    private val getQuoteById: GetQuoteByIdUseCase,
    private val upsertQuote: UpsertQuoteUseCase,
    private val deleteQuote: DeleteQuoteUseCase,
    private val handleWidgetQuoteRemoval: HandleWidgetQuoteRemovalUseCase,
) : BaseViewModel<WidgetQuoteState, WidgetQuoteEffect>(WidgetQuoteState()),
    WidgetQuoteIntent {

    private var quoteId: String? = null
    private var generalName: String = ""
    private var bound = false

    private var authorOptions: List<String> = emptyList()
    private var bookOptions: List<String> = emptyList()
    private var tagPool: List<String> = emptyList()

    init {
        observeSavedAuthors()
            .onEach { authors -> authorOptions = authors.map { it.name } }
            .launchIn(viewModelScope)
        observeSavedBooks()
            .onEach { books -> bookOptions = books.map { it.name } }
            .launchIn(viewModelScope)
        observeSavedTags()
            .onEach { tags -> tagPool = tags.map { it.name } }
            .launchIn(viewModelScope)
    }

    fun bind(id: String, generalName: String) {
        if (bound) return
        bound = true
        quoteId = id
        this.generalName = generalName
        observeEnrichedById(id)
            .onEach { quote -> publishState { copy(isLoading = false, quote = quote) } }
            .launchIn(viewModelScope)
    }

    override fun onToggleMenu() = publishState { copy(menuVisible = !menuVisible) }

    override fun onDismissMenu() = publishState { copy(menuVisible = false) }

    override fun onEditClicked() {
        val quote = uiState.value.quote ?: return
        publishState { copy(menuVisible = false) }
        publishEffect(
            WidgetQuoteEffect.OpenEditSheet(
                draft = QuoteEditDraft(
                    quoteId = quote.id,
                    text = quote.text,
                    authorName = quote.author?.name.orEmpty(),
                    bookName = quote.book?.name.orEmpty(),
                    tags = quote.tags.map { it.name },
                    inWidgetPlaylist = quote.inWidgetPlaylist,
                    inPushPlaylist = quote.inPushPlaylist,
                ),
                authorOptions = authorOptions,
                bookOptions = bookOptions,
                tagPool = tagPool,
            ),
        )
    }

    fun onEditSaved(draft: QuoteEditDraft) {
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
            )
            publishEffect(WidgetQuoteEffect.Edited)
        }
    }

    override fun onDeleteClicked() {
        val id = quoteId ?: return
        publishState { copy(menuVisible = false) }
        publishEffect(WidgetQuoteEffect.OpenDeleteDialog(id))
    }

    fun onDeleteConfirmed() {
        val id = quoteId ?: return
        viewModelScope.launch {
            deleteQuote(id)
            handleWidgetQuoteRemoval(id)
            publishEffect(WidgetQuoteEffect.Deleted)
        }
    }
}
