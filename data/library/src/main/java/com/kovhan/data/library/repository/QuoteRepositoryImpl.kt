package com.kovhan.data.library.repository

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.data.library.local.library.PendingEntityType
import com.kovhan.data.library.local.library.PendingOpType
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.library.QuoteDao
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toEntity
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.matches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepositoryImpl @Inject constructor(
    private val dao: QuoteDao,
    private val pendingDao: PendingOperationDao,
) : QuoteRepository {

    override suspend fun getAll(): List<Quote> =
        dao.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): Quote? =
        dao.getById(id)?.toDomain()

    override fun observeAll(): Flow<List<Quote>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeFiltered(filter: QuoteFilter): Flow<List<Quote>> =
        observeAll().map { quotes -> quotes.filter { it.matches(filter) } }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
        enqueue(id, PendingOpType.DELETE)
    }

    override suspend fun edit(quote: Quote) {
        dao.upsert(quote.toEntity())
        enqueue(quote.id, PendingOpType.UPSERT)
    }

    override suspend fun setInPushPlaylist(id: String, added: Boolean) {
        dao.setInPushPlaylist(id, added)
        enqueue(id, PendingOpType.UPSERT)
    }

    override suspend fun setInWidgetPlaylist(id: String, added: Boolean) {
        dao.setInWidgetPlaylist(id, added)
        enqueue(id, PendingOpType.UPSERT)
    }

    private suspend fun enqueue(id: String, opType: PendingOpType) {
        pendingDao.insert(
            PendingOperationEntity.of(
                entityType = PendingEntityType.QUOTE,
                entityId = id,
                opType = opType,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}
