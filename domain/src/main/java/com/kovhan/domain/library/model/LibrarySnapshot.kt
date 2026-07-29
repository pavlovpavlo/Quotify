package com.kovhan.domain.library.model

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.core.models.widget.Playlist

/**
 * An in-memory copy of the whole local library, captured before an auth switch that
 * changes the Firebase uid (and therefore the remote paths). Taken so a guest's data
 * can be merged into the account being signed into, before [LibrarySynchronizer.refreshFromRemote]
 * hard-replaces the local Room tables with the target account's remote content.
 */
data class LibrarySnapshot(
    val quotes: List<Quote>,
    val collections: List<SavedCollection>,
    val authors: List<SavedAuthor>,
    val books: List<SavedBook>,
    val tags: List<SavedTag>,
    val playlists: List<Playlist> = emptyList(),
)
