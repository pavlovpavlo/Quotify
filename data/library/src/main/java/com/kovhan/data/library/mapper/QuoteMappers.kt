package com.kovhan.data.library.mapper

import com.kovhan.core.models.Quote
import com.kovhan.data.library.dto.QuoteDto

fun QuoteDto.toDomain() = Quote(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
)

fun Quote.toDto() = QuoteDto(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
)
