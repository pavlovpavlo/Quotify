package com.kovhan.data.library.repository

import com.kovhan.core.models.quote.DailyQuote
import com.kovhan.data.library.local.daily.DailyQuoteDao
import com.kovhan.data.library.local.daily.DailySeenDao
import com.kovhan.data.library.local.daily.DailySeenEntity
import com.kovhan.data.library.local.daily.DailySelectionDao
import com.kovhan.data.library.local.daily.DailySelectionEntity
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toEntity
import com.kovhan.data.library.remote.DailyQuoteRemoteDataSource
import com.kovhan.domain.daily.DailyQuoteRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyQuoteRepositoryImpl @Inject constructor(
    private val remote: DailyQuoteRemoteDataSource,
    private val quoteDao: DailyQuoteDao,
    private val selectionDao: DailySelectionDao,
    private val seenDao: DailySeenDao,
) : DailyQuoteRepository {

    private val refreshMutex = Mutex()

    @Volatile
    private var refreshed = false

    override suspend fun prefetch() = ensurePoolCached()

    override suspend fun getDailyQuote(): DailyQuote? {
        ensurePoolCached()
        val today = LocalDate.now().toEpochDay()
        val selection = selectionDao.get()
        if (selection?.dismissedEpochDay == today) return null

        val pool = quoteDao.getAll().map { it.toDomain() }
        if (pool.isEmpty()) return null

        if (selection != null && selection.epochDay == today) {
            pool.firstOrNull { it.id == selection.quoteId }?.let { return it }
        }

        val chosen = pickUnseen(pool)
        seenDao.insert(DailySeenEntity(chosen.id))
        selectionDao.set(
            (selection ?: DailySelectionEntity()).copy(quoteId = chosen.id, epochDay = today),
        )
        return chosen
    }

    override suspend fun getCachedDailyQuote(): DailyQuote? {
        val today = LocalDate.now().toEpochDay()
        val selection = selectionDao.get() ?: return null
        if (selection.dismissedEpochDay == today) return null
        if (selection.epochDay != today) return null
        val quoteId = selection.quoteId ?: return null
        return quoteDao.getAll().firstOrNull { it.id == quoteId }?.toDomain()
    }

    override suspend fun dismissForToday() {
        val today = LocalDate.now().toEpochDay()
        val selection = selectionDao.get() ?: DailySelectionEntity()
        selectionDao.set(selection.copy(dismissedEpochDay = today))
    }

    /** Prefer a quote not shown before; once every quote has been seen, reset the history. */
    private suspend fun pickUnseen(pool: List<DailyQuote>): DailyQuote {
        val seen = seenDao.getAllIds().toSet()
        val unseen = pool.filterNot { it.id in seen }
        if (unseen.isNotEmpty()) return unseen.random()
        seenDao.clear()
        return pool.random()
    }

    private suspend fun ensurePoolCached() {
        if (refreshed) return
        refreshMutex.withLock {
            if (refreshed) return
            val remotePool = remote.getAll()
            if (remotePool.isNotEmpty()) {
                quoteDao.replaceAll(remotePool.map { it.toEntity() })
            }
            refreshed = true
        }
    }
}
