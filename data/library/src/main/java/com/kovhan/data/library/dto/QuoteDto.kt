package com.kovhan.data.library.dto

data class QuoteDto(
    val id: String = "",
    val text: String = "",
    val authorId: String? = null,
    val bookId: String? = null,
    val collectionId: String? = null,
    val tagIds: List<String> = emptyList(),
    val inPushPlaylist: Boolean = false,
    val inWidgetPlaylist: Boolean = false,
    val sourceDailyId: String? = null,
    val page: Int? = null,
    val isFavourite: Boolean = false,
)
