package com.kovhan.core.models

data class EnrichedQuote(
    val id: String,
    val text: String,
    val author: SavedAuthor?,
    val book: SavedBook?,
    val tags: List<SavedTag>,
    val inPushPlaylist: Boolean = false,
    val inWidgetPlaylist: Boolean = false,
)
