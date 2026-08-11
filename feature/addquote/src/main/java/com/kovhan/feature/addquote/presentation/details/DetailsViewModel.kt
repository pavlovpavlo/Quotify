package com.kovhan.feature.addquote.presentation.details

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.ui.view_model.BaseViewModel
import com.kovhan.domain.library.use_case.author.ObserveSavedAuthorsUseCase
import com.kovhan.domain.library.use_case.book.ObserveSavedBooksUseCase
import com.kovhan.domain.library.use_case.tag.ObserveSavedTagsUseCase
import com.kovhan.feature.addquote.presentation.details.mvi.DetailsEffect
import com.kovhan.feature.addquote.presentation.details.mvi.DetailsIntent
import com.kovhan.feature.addquote.presentation.details.mvi.DetailsState
import com.kovhan.feature.addquote.presentation.details.mvi.QuoteDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    observeSavedAuthors: ObserveSavedAuthorsUseCase,
    observeSavedBooks: ObserveSavedBooksUseCase,
    observeSavedTags: ObserveSavedTagsUseCase,
) : BaseViewModel<DetailsState, DetailsEffect>(DetailsState()), DetailsIntent {

    private var quoteInitialized = false

    init {
        viewModelScope.launch {
            observeSavedAuthors().collect { authors -> publishState { copy(authors = authors) } }
        }
        viewModelScope.launch {
            observeSavedBooks().collect { books -> publishState { copy(books = books) } }
        }
        viewModelScope.launch {
            observeSavedTags().collect { tags -> publishState { copy(tagPool = tags.map { it.name }) } }
        }
    }

    override fun onInitialQuote(text: String) {
        if (quoteInitialized) return
        quoteInitialized = true
        publishState { copy(quote = TextFieldValue(text, TextRange(text.length))) }
    }

    override fun onQuoteChanged(value: TextFieldValue) = publishState { copy(quote = value) }

    override fun onAuthorQueryChanged(value: String) = publishState { copy(authorQuery = value) }
    override fun onAuthorPicked(name: String) = publishState { copy(authorQuery = name) }
    override fun onBookQueryChanged(value: String) = publishState { copy(bookQuery = value) }
    override fun onBookPicked(name: String) = publishState { copy(bookQuery = name) }
    override fun onPageChanged(value: String) = publishState { copy(page = value) }

    override fun onOpenTagSheet() = publishEffect(DetailsEffect.OpenTagSheet)

    fun onTagSheetApplied(
        selectedTags: List<String>,
        aiTags: List<String>,
    ) = publishState {
        copy(selectedTags = selectedTags, aiTags = aiTags)
    }

    override fun onRemoveTag(tag: String) = publishState {
        copy(selectedTags = selectedTags.filterNot { it.equals(tag, ignoreCase = true) })
    }

    override fun onWidgetToggle(enabled: Boolean) = publishState { copy(inWidgetPlaylist = enabled) }
    override fun onPushToggle(enabled: Boolean) = publishState { copy(inPushPlaylist = enabled) }

    override fun onSaveClicked() {
        val state = uiState.value
        if (!state.canSave) return
        val draft = QuoteDraft(
            text = state.quote.text.trim(),
            authorName = state.authorQuery.trim().ifBlank { null },
            bookName = state.bookQuery.trim().ifBlank { null },
            tagNames = state.selectedTags,
            inWidgetPlaylist = state.inWidgetPlaylist,
            inPushPlaylist = state.inPushPlaylist,
            page = state.page.toIntOrNull(),
        )
        publishEffect(DetailsEffect.ProceedToSave(draft))
    }

    override fun onBackClicked() = publishEffect(DetailsEffect.Back)
    override fun onCloseClicked() = publishEffect(DetailsEffect.Close)
}
