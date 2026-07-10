package com.kovhan.domain.library

import com.kovhan.domain.library.model.LibrarySnapshot

/**
 * Merges a guest (anonymous) user's local library into the account being signed into.
 *
 * Entities are deduplicated by normalized name (`trim().lowercase()`) for collections,
 * tags, authors and books; the special `general`/`favourite` collections are matched by id.
 * Quotes are matched by [com.kovhan.core.models.Quote.id]; existing quotes are safely
 * merged (union of tags, OR of playlist flags, existing non-null refs kept).
 *
 * Writes go through the library repositories so the pending-sync queue is populated, after
 * which the caller runs [LibrarySynchronizer.syncPendingChanges] and a final refresh.
 */
interface GuestLibraryMerger {

    /** Captures the current local library before any auth switch. */
    suspend fun snapshot(): LibrarySnapshot

    /** Drops the offline pending queue; the [snapshot] already holds the effect of those ops. */
    suspend fun clearPending()

    /** Applies [snapshot] into the currently-signed-in account's local library. */
    suspend fun merge(snapshot: LibrarySnapshot)
}
