package com.kovhan.feature.addquote.presentation.details.mvi

import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.ui.UiState

enum class AiState { IDLE, LOADING, DONE }

data class DetailsState(
    val quote: TextFieldValue = TextFieldValue(),
    val authorQuery: String = "",
    val bookQuery: String = "",
    val authors: List<SavedAuthor> = emptyList(),
    val books: List<SavedBook> = emptyList(),
    val selectedTags: List<String> = emptyList(),
    val tagPool: List<String> = emptyList(),
    val aiTags: List<String> = emptyList(),
    val inWidgetPlaylist: Boolean = true,
    val inPushPlaylist: Boolean = false,
) : UiState {

    val canSave: Boolean
        get() = quote.text.isNotBlank()

    val filteredAuthorNames: List<String>
        get() = authors.map { it.name }.matching(authorQuery)

    val filteredBookNames: List<String>
        get() = books.map { it.name }.matching(bookQuery)
}

private fun List<String>.matching(query: String): List<String> {
    val q = query.trim()
    return if (q.isEmpty()) this else filter { it.contains(q, ignoreCase = true) }
}
