package com.kovhan.core.models.quote

data class QuoteFilter(
    val authorId: String? = null,
    val bookId: String? = null,
    val tagId: String? = null,
    val collectionId: String? = null,
    val playlist: QuotePlaylist? = null,
)
