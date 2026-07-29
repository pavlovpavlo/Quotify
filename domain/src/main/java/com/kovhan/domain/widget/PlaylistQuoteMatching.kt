package com.kovhan.domain.widget

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.widget.Playlist
import com.kovhan.core.models.widget.PlaylistSourceType

/** True when [quote] is pulled into this playlist by any of its sources. */
fun Playlist.matches(quote: Quote): Boolean = sources.any { source ->
    when (source.type) {
        PlaylistSourceType.QUOTE -> quote.id == source.refId
        PlaylistSourceType.FOLDER -> quote.collectionId == source.refId
        PlaylistSourceType.TAG -> source.refId in quote.tagIds
        PlaylistSourceType.BOOK -> quote.bookId == source.refId
        PlaylistSourceType.AUTHOR -> quote.authorId == source.refId
    }
}

/** Number of distinct [quotes] resolved into this playlist. */
fun Playlist.quoteCount(quotes: List<Quote>): Int = quotes.count { matches(it) }
