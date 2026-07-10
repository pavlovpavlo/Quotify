package com.kovhan.data.library.repository

import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.data.library.local.library.CollectionDao
import com.kovhan.data.library.local.library.PendingEntityType
import com.kovhan.data.library.local.library.PendingOpType
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toEntity
import com.kovhan.domain.library.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepositoryImpl @Inject constructor(
    private val dao: CollectionDao,
    private val pendingDao: PendingOperationDao,
) : CollectionRepository {

    override suspend fun getAll(): List<SavedCollection> =
        dao.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): SavedCollection? =
        dao.getById(id)?.toDomain()

    override fun observeAll(): Flow<List<SavedCollection>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
        enqueue(id, PendingOpType.DELETE)
    }

    override suspend fun edit(item: SavedCollection) {
        dao.upsert(item.toEntity())
        enqueue(item.id, PendingOpType.UPSERT)
    }

    private suspend fun enqueue(id: String, opType: PendingOpType) {
        pendingDao.insert(
            PendingOperationEntity.of(
                entityType = PendingEntityType.COLLECTION,
                entityId = id,
                opType = opType,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}
