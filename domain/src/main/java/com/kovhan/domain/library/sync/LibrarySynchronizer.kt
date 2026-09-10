package com.kovhan.domain.library.sync

interface LibrarySynchronizer {

    /**
     * Replays queued offline mutations to the backend. A pending item is removed only
     * after it is successfully mirrored. Returns `true` when the queue is fully drained.
     */
    suspend fun syncPendingChanges(): Boolean

    /** Pulls the remote library into the local database (Room is the source of truth). */
    suspend fun refreshFromRemote()

    /**
     * Повний цикл: [syncPendingChanges], а далі [refreshFromRemote] — але лише якщо
     * чергу вдалося спорожнити, бо пул замінює таблиці цілком і затер би непроштовхані
     * зміни. [force] обходить внутрішній тротлінг (холодний старт тягне завжди,
     * повернення з фону — не частіше ніж раз на хвилину).
     */
    suspend fun sync(force: Boolean = false)

    /**
     * Wipes the local library (all five tables + the pending-sync queue). Room is NOT scoped
     * by uid, so this must run on sign-out and account deletion — otherwise the next user
     * (including a fresh anonymous guest) inherits the previous user's local data.
     */
    suspend fun clearLocal()

    /**
     * Deletes all of the current user's remote library documents (every `users/{uid}` library
     * subcollection). Best-effort; must run while still authenticated (before the auth account
     * is deleted) so Firestore rules permit the deletes.
     */
    suspend fun purgeRemote()
}
