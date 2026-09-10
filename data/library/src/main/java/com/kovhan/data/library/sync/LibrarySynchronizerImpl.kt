package com.kovhan.data.library.sync

import com.google.firebase.auth.FirebaseAuth
import com.kovhan.data.library.local.TransactionRunner
import com.kovhan.data.library.local.library.CollectionDao
import com.kovhan.data.library.local.library.PendingEntityType
import com.kovhan.data.library.local.library.PendingOpType
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.library.QuoteDao
import com.kovhan.data.library.local.library.SavedAuthorDao
import com.kovhan.data.library.local.library.SavedBookDao
import com.kovhan.data.library.local.library.SavedTagDao
import com.kovhan.data.library.local.widget.PlaylistDao
import com.kovhan.data.library.mapper.toDto
import com.kovhan.data.library.mapper.toEntity
import com.kovhan.data.library.mapper.toPlaylistEntity
import com.kovhan.data.library.mapper.toSourceEntities
import com.kovhan.data.library.remote.CollectionRemoteDataSource
import com.kovhan.data.library.remote.PlaylistRemoteDataSource
import com.kovhan.data.library.remote.QuoteRemoteDataSource
import com.kovhan.data.library.remote.SavedAuthorRemoteDataSource
import com.kovhan.data.library.remote.SavedBookRemoteDataSource
import com.kovhan.data.library.remote.SavedTagRemoteDataSource
import com.kovhan.domain.library.sync.LibrarySynchronizer
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibrarySynchronizerImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val transaction: TransactionRunner,
    private val pendingDao: PendingOperationDao,
    private val quoteDao: QuoteDao,
    private val collectionDao: CollectionDao,
    private val authorDao: SavedAuthorDao,
    private val bookDao: SavedBookDao,
    private val tagDao: SavedTagDao,
    private val playlistDao: PlaylistDao,
    private val quoteRemote: QuoteRemoteDataSource,
    private val collectionRemote: CollectionRemoteDataSource,
    private val authorRemote: SavedAuthorRemoteDataSource,
    private val bookRemote: SavedBookRemoteDataSource,
    private val tagRemote: SavedTagRemoteDataSource,
    private val playlistRemote: PlaylistRemoteDataSource,
) : LibrarySynchronizer {

    private val syncMutex = Mutex()
    private var lastSyncAt = 0L

    override suspend fun syncPendingChanges(): Boolean {
        if (auth.currentUser == null) return false
        var allDrained = true
        for (op in pendingDao.getAllOrdered()) {
            val pushed = runCatching { push(op) }.isSuccess
            if (pushed) pendingDao.deleteByKey(op.key) else allDrained = false
        }
        return allDrained
    }

    override suspend fun sync(force: Boolean) {
        if (auth.currentUser == null) return
        syncMutex.withLock {
            val now = System.currentTimeMillis()
            if (!force && now - lastSyncAt < MIN_SYNC_INTERVAL_MS) return
            val drained = runCatching { syncPendingChanges() }.getOrDefault(false)
            if (!drained) return
            runCatching { refreshFromRemote() }
                .onSuccess { lastSyncAt = System.currentTimeMillis() }
        }
    }

    override suspend fun refreshFromRemote() {
        if (auth.currentUser == null) return

        runCatching {
            val remote = quoteRemote.getAll()
            transaction {
                val pending = pendingIdsFor(PendingEntityType.QUOTE)
                val local = quoteDao.getAll()
                val knownCreatedAt = local.associate { it.id to it.createdAt }
                val incoming = remote
                    .map { it.toEntity() }
                    .map { entity ->
                        if (entity.createdAt > 0L) {
                            entity
                        } else {
                            entity.copy(createdAt = knownCreatedAt[entity.id] ?: 0L)
                        }
                    }
                quoteDao.replaceAll(pending.merge(incoming, local) { it.id })
            }
        }
        runCatching {
            applyRemote(
                type = PendingEntityType.COLLECTION,
                remote = collectionRemote.getAll().map { it.toEntity() },
                id = { it.id },
                local = collectionDao::getAll,
                replace = collectionDao::replaceAll,
            )
        }
        runCatching {
            applyRemote(
                type = PendingEntityType.AUTHOR,
                remote = authorRemote.getAll().map { it.toEntity() },
                id = { it.id },
                local = authorDao::getAll,
                replace = authorDao::replaceAll,
            )
        }
        runCatching {
            applyRemote(
                type = PendingEntityType.BOOK,
                remote = bookRemote.getAll().map { it.toEntity() },
                id = { it.id },
                local = bookDao::getAll,
                replace = bookDao::replaceAll,
            )
        }
        runCatching {
            applyRemote(
                type = PendingEntityType.TAG,
                remote = tagRemote.getAll().map { it.toEntity() },
                id = { it.id },
                local = tagDao::getAll,
                replace = tagDao::replaceAll,
            )
        }
        runCatching {
            val dtos = playlistRemote.getAll()
            transaction {
                val pending = pendingIdsFor(PendingEntityType.PLAYLIST)
                val kept = if (pending.isEmpty) {
                    emptyList()
                } else {
                    playlistDao.getAllWithSources().filter { it.playlist.id in pending.upserts }
                }
                val incoming = dtos.filterNot { it.id in pending.queued }
                playlistDao.replaceAll(
                    playlists = incoming.map { it.toPlaylistEntity() } + kept.map { it.playlist },
                    sources = incoming.flatMap { it.toSourceEntities() } +
                        kept.flatMap { it.sources },
                )
            }
        }
    }

    /**
     * Рядки, на які ще висить незакрита операція в черзі, — це локальна правда,
     * якої бекенд поки не бачив. Пул замінює таблицю цілком, тож без цього
     * фільтра цитата, збережена поки тривав мережевий запит, зникала: `replaceAll`
     * стирав рядок, а `push` потім не знаходив його і тихо викидав операцію.
     *
     * Читається всередині тієї ж транзакції, що й запис, тому вікно між
     * «подивились чергу» і «переписали таблицю» закрите.
     */
    private suspend fun <T> applyRemote(
        type: PendingEntityType,
        remote: List<T>,
        id: (T) -> String,
        local: suspend () -> List<T>,
        replace: suspend (List<T>) -> Unit,
    ) = transaction {
        val pending = pendingIdsFor(type)
        replace(pending.merge(remote, if (pending.isEmpty) emptyList() else local(), id))
    }

    private suspend fun pendingIdsFor(type: PendingEntityType): PendingIds {
        val ops = pendingDao.getAllOrdered().filter { it.entityType == type.name }
        return PendingIds(
            queued = ops.map { it.entityId }.toSet(),
            upserts = ops.filter { it.opType == PendingOpType.UPSERT.name }
                .map { it.entityId }
                .toSet(),
        )
    }

    private data class PendingIds(val queued: Set<String>, val upserts: Set<String>) {
        val isEmpty: Boolean get() = queued.isEmpty()

        fun <T> merge(incoming: List<T>, local: List<T>, id: (T) -> String): List<T> {
            if (isEmpty) return incoming
            return incoming.filterNot { id(it) in queued } + local.filter { id(it) in upserts }
        }
    }

    override suspend fun clearLocal() {
        runCatching { pendingDao.clear() }
        runCatching { quoteDao.clear() }
        runCatching { collectionDao.clear() }
        runCatching { authorDao.clear() }
        runCatching { bookDao.clear() }
        runCatching { tagDao.clear() }
        runCatching { playlistDao.clear() }
    }

    override suspend fun purgeRemote() {
        if (auth.currentUser == null) return
        runCatching { quoteRemote.getAll().forEach { quoteRemote.deleteById(it.id) } }
        runCatching { collectionRemote.getAll().forEach { collectionRemote.deleteById(it.id) } }
        runCatching { authorRemote.getAll().forEach { authorRemote.deleteById(it.id) } }
        runCatching { bookRemote.getAll().forEach { bookRemote.deleteById(it.id) } }
        runCatching { tagRemote.getAll().forEach { tagRemote.deleteById(it.id) } }
        runCatching { playlistRemote.getAll().forEach { playlistRemote.deleteById(it.id) } }
    }

    private suspend fun push(op: PendingOperationEntity) {
        val isDelete = op.opType == PendingOpType.DELETE.name
        when (PendingEntityType.valueOf(op.entityType)) {
            PendingEntityType.QUOTE ->
                if (isDelete) quoteRemote.deleteById(op.entityId)
                else quoteDao.getById(op.entityId)?.let { quoteRemote.edit(it.toDto()) }

            PendingEntityType.COLLECTION ->
                if (isDelete) collectionRemote.deleteById(op.entityId)
                else collectionDao.getById(op.entityId)?.let { collectionRemote.edit(it.toDto()) }

            PendingEntityType.AUTHOR ->
                if (isDelete) authorRemote.deleteById(op.entityId)
                else authorDao.getById(op.entityId)?.let { authorRemote.edit(it.toDto()) }

            PendingEntityType.BOOK ->
                if (isDelete) bookRemote.deleteById(op.entityId)
                else bookDao.getById(op.entityId)?.let { bookRemote.edit(it.toDto()) }

            PendingEntityType.TAG ->
                if (isDelete) tagRemote.deleteById(op.entityId)
                else tagDao.getById(op.entityId)?.let { tagRemote.edit(it.toDto()) }

            PendingEntityType.PLAYLIST ->
                if (isDelete) playlistRemote.deleteById(op.entityId)
                else playlistDao.getById(op.entityId)?.let { playlistRemote.edit(it.toDto()) }
        }
    }

    private companion object {
        const val MIN_SYNC_INTERVAL_MS = 60_000L
    }
}
