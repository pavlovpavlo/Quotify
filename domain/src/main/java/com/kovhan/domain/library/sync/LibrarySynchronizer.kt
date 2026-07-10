package com.kovhan.domain.library.sync

interface LibrarySynchronizer {

    /**
     * Replays queued offline mutations to the backend. A pending item is removed only
     * after it is successfully mirrored. Returns `true` when the queue is fully drained.
     */
    suspend fun syncPendingChanges(): Boolean

    /** Pulls the remote library into the local database (Room is the source of truth). */
    suspend fun refreshFromRemote()
}
