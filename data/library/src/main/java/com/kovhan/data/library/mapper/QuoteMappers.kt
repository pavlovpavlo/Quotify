package com.kovhan.data.library.mapper

import com.kovhan.core.models.quote.Quote
import com.kovhan.data.library.dto.QuoteDto

fun QuoteDto.toDomain() = Quote(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = collectionId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
    page = page,
)

fun Quote.toDto() = QuoteDto(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = collectionId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
    page = page,
)
