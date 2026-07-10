package com.kovhan.core.models.quote

import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag

data class EnrichedQuote(
    val id: String,
    val text: String,
    val author: SavedAuthor?,
    val book: SavedBook?,
    val collection: SavedCollection? = null,
    val tags: List<SavedTag>,
    val inPushPlaylist: Boolean = false,
    val inWidgetPlaylist: Boolean = false,
)
