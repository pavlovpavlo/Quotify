package com.kovhan.domain.library

import com.kovhan.core.models.Quote
import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.QuotePlaylist

fun Quote.matches(filter: QuoteFilter): Boolean {
    val playlist = filter.playlist
    return (filter.authorId == null || authorId == filter.authorId) &&
        (filter.bookId == null || bookId == filter.bookId) &&
        (filter.tagId == null || filter.tagId in tagIds) &&
        (filter.collectionId == null || collectionId == filter.collectionId) &&
        (playlist == null || inPlaylist(playlist))
}

private fun Quote.inPlaylist(playlist: QuotePlaylist): Boolean =
    when (playlist) {
        QuotePlaylist.PUSH -> inPushPlaylist
        QuotePlaylist.WIDGET -> inWidgetPlaylist
    }
