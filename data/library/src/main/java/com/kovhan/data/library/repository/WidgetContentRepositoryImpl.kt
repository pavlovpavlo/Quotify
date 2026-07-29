package com.kovhan.data.library.repository

import com.kovhan.data.library.local.widget.WidgetContentDao
import com.kovhan.data.library.local.widget.WidgetQuoteEntity
import com.kovhan.data.library.local.widget.WidgetSeenEntity
import com.kovhan.data.library.local.widget.WidgetStateEntity
import com.kovhan.domain.widget.WidgetContentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetContentRepositoryImpl @Inject constructor(
    private val dao: WidgetContentDao,
) : WidgetContentRepository {

    override fun observeSnapshotIds(): Flow<List<String>> = dao.observeSnapshotIds()

    override suspend fun getSnapshotIds(): List<String> = dao.getSnapshotIds()

    override suspend fun replaceSnapshot(quoteIds: List<String>) {
        dao.replaceSnapshot(
            quoteIds.mapIndexed { index, id -> WidgetQuoteEntity(quoteId = id, position = index) },
        )
    }

    override suspend fun getSeenIds(): List<String> = dao.getSeenIds()

    override suspend fun markSeen(quoteId: String) {
        dao.insertSeen(WidgetSeenEntity(quoteId = quoteId, shownAt = System.currentTimeMillis()))
    }

    override suspend fun resetSeen() = dao.clearSeen()

    override fun observeCurrentQuoteId(): Flow<String?> =
        dao.observeState().map { it?.currentQuoteId }

    override suspend fun getCurrentQuoteId(): String? = dao.getState()?.currentQuoteId

    override suspend fun setCurrentQuoteId(quoteId: String?) {
        dao.setState(
            WidgetStateEntity(
                currentQuoteId = quoteId,
                lastRotatedAt = System.currentTimeMillis(),
            ),
        )
    }
}
