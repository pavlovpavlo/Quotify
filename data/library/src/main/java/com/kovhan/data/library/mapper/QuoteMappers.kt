package com.kovhan.data.library.mapper

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.quote.Quote
import com.kovhan.data.library.dto.QuoteDto

fun QuoteDto.toDomain() = Quote(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = normalizedCollectionId(),
    isFavourite = isFavouriteResolved(),
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
    page = page,
    createdAt = createdAt,
)

fun Quote.toDto() = QuoteDto(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = collectionId,
    isFavourite = isFavourite,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
    page = page,
    createdAt = createdAt,
)

internal fun QuoteDto.isFavouriteResolved(): Boolean =
    isFavourite || collectionId == SavedCollection.FAVOURITES_ID

internal fun QuoteDto.normalizedCollectionId(): String? =
    collectionId?.takeUnless { it == SavedCollection.FAVOURITES_ID }
