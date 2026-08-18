package com.kovhan.core.navigation

/** Shared result payload of the quote-edit sheet, reused across features. */
data class QuoteEditDraft(
    val quoteId: String,
    val text: String,
    val authorName: String,
    val bookName: String,
    val tags: List<String>,
    val aiTags: List<String> = emptyList(),
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
    val page: String = "",
)

data class MoveQuoteResult(
    val quoteId: String,
    val targetCollectionId: String,
)
