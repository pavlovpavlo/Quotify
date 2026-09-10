package com.kovhan.core.models.quote

data class Quote(
    val id: String,
    val text: String,
    val authorId: String? = null,
    val bookId: String? = null,
    val collectionId: String? = null,
    val isFavourite: Boolean = false,
    val tagIds: List<String> = emptyList(),
    val inPushPlaylist: Boolean = false,
    val inWidgetPlaylist: Boolean = false,
    val sourceDailyId: String? = null,
    val page: Int? = null,
    val createdAt: Long = 0L,
)
