package com.kovhan.core.models.widget

/**
 * The fully-resolved quote the home-screen widget and its background rotation
 * task render — the same display richness as a list item (text + author + book),
 * decoupled from library-internal fields.
 */
data class WidgetQuote(
    val id: String,
    val text: String,
    val authorName: String?,
    val bookName: String?,
)
