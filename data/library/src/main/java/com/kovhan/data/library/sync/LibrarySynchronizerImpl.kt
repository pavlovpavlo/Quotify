package com.kovhan.data.library.sync

import com.google.firebase.auth.FirebaseAuth
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibrarySynchronizerImpl @Inject constructor(
    private val auth: FirebaseAuth,
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

    override suspend fun syncPendingChanges(): Boolean {
        if (auth.currentUser == null) return false
        var allDrained = true
        for (op in pendingDao.getAllOrdered()) {
            val pushed = runCatching { push(op) }.isSuccess
            if (pushed) pendingDao.deleteByKey(op.key) else allDrained = false
        }
        return allDrained
    }

    override suspend fun refreshFromRemote() {
        if (auth.currentUser == null) return
        runCatching { quoteDao.replaceAll(quoteRemote.getAll().map { it.toEntity() }) }
        runCatching { collectionDao.replaceAll(collectionRemote.getAll().map { it.toEntity() }) }
        runCatching { authorDao.replaceAll(authorRemote.getAll().map { it.toEntity() }) }
        runCatching { bookDao.replaceAll(bookRemote.getAll().map { it.toEntity() }) }
        runCatching { tagDao.replaceAll(tagRemote.getAll().map { it.toEntity() }) }
        runCatching {
            val dtos = playlistRemote.getAll()
            playlistDao.replaceAll(
                playlists = dtos.map { it.toPlaylistEntity() },
                sources = dtos.flatMap { it.toSourceEntities() },
            )
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
}
