package com.kovhan.domain.library.model

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.models.SavedTag

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
)
