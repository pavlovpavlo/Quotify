package com.kovhan.core.models

import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.core.models.quote.EnrichedQuote

data class LibrarySearchResults(
    val quotes: List<EnrichedQuote> = emptyList(),
    val folders: List<SavedCollection> = emptyList(),
    val books: List<SavedBook> = emptyList(),
    val authors: List<SavedAuthor> = emptyList(),
    val tags: List<SavedTag> = emptyList(),
)
