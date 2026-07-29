package com.kovhan.data.library.repository

import com.kovhan.core.models.widget.Playlist
import com.kovhan.core.models.widget.PlaylistSource
import com.kovhan.data.library.local.library.PendingEntityType
import com.kovhan.data.library.local.library.PendingOpType
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.widget.PlaylistDao
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toEntity
import com.kovhan.data.library.mapper.toSourceEntities
import com.kovhan.domain.common.IdGenerator
import com.kovhan.domain.widget.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Offline-first store for widget playlists: Room is the source of truth, every
 * mutation is mirrored to Firestore through the pending-operation queue
 * ([com.kovhan.data.library.sync.LibrarySynchronizerImpl]). Mirrors
 * [CollectionRepositoryImpl].
 */
@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val dao: PlaylistDao,
    private val pendingDao: PendingOperationDao,
    private val idGenerator: IdGenerator,
) : PlaylistRepository {

    override fun observeAll(): Flow<List<Playlist>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getAll(): List<Playlist> =
        dao.getAllWithSources().map { it.toDomain() }

    override suspend fun getById(id: String): Playlist? =
        dao.getById(id)?.toDomain()

    override suspend fun create(
        name: String,
        sources: List<PlaylistSource>,
    ): String {
        val playlist = Playlist(
            id = idGenerator.generate(),
            name = name,
            sources = sources,
        )
        dao.upsertWithSources(playlist.toEntity(), playlist.toSourceEntities())
        enqueue(playlist.id, PendingOpType.UPSERT)
        return playlist.id
    }

    override suspend fun update(playlist: Playlist) {
        dao.upsertWithSources(playlist.toEntity(), playlist.toSourceEntities())
        enqueue(playlist.id, PendingOpType.UPSERT)
    }

    override suspend fun rename(id: String, name: String) {
        dao.rename(id, name)
        enqueue(id, PendingOpType.UPSERT)
    }

    override suspend fun delete(id: String) {
        dao.deleteById(id)
        enqueue(id, PendingOpType.DELETE)
    }

    private suspend fun enqueue(id: String, opType: PendingOpType) {
        pendingDao.insert(
            PendingOperationEntity.of(
                entityType = PendingEntityType.PLAYLIST,
                entityId = id,
                opType = opType,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}
