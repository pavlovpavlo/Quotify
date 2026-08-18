package com.kovhan.domain.library

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.quote.QuotePlaylist

fun Quote.matches(filter: QuoteFilter): Boolean {
    val playlist = filter.playlist
    return (filter.authorId == null || authorId == filter.authorId) &&
        (filter.bookId == null || bookId == filter.bookId) &&
        (filter.tagId == null || filter.tagId in tagIds) &&
        inCollection(filter.collectionId) &&
        (playlist == null || inPlaylist(playlist))
}

private fun Quote.inCollection(collectionId: String?): Boolean = when (collectionId) {
    null -> true
    SavedCollection.FAVOURITES_ID -> isFavourite
    else -> this.collectionId == collectionId
}

private fun Quote.inPlaylist(playlist: QuotePlaylist): Boolean =
    when (playlist) {
        QuotePlaylist.PUSH -> inPushPlaylist
        QuotePlaylist.WIDGET -> inWidgetPlaylist
    }
