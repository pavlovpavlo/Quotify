package com.kovhan.core.models

data class LibrarySearchResults(
    val quotes: List<EnrichedQuote> = emptyList(),
    val folders: List<SavedCollection> = emptyList(),
    val books: List<SavedBook> = emptyList(),
    val authors: List<SavedAuthor> = emptyList(),
    val tags: List<SavedTag> = emptyList(),
)
