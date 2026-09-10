package com.kovhan.data.library.repository

import app.cash.turbine.test
import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.quote.QuotePlaylist
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.library.QuoteDao
import com.kovhan.data.library.local.library.QuoteEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("QuoteRepositoryImpl")
class QuoteRepositoryImplTest {

    private lateinit var dao: QuoteDao
    private lateinit var pendingDao: PendingOperationDao
    private lateinit var repository: QuoteRepositoryImpl

    // q1 is in BOTH playlists; q2 is in neither; q3 is widget-only.
    private val q1 = QuoteEntity(
        id = "q1",
        text = "camus push+widget",
        authorId = "a-camus",
        bookId = "b-stranger",
        collectionId = null,
        tagIds = listOf("t-absurd"),
        inPushPlaylist = true,
        inWidgetPlaylist = true,
        sourceDailyId = null,
    )
    private val q2 = QuoteEntity(
        id = "q2",
        text = "seneca none",
        authorId = "a-seneca",
        bookId = "b-letters",
        collectionId = "c-fav",
        tagIds = listOf("t-stoic"),
        inPushPlaylist = false,
        inWidgetPlaylist = false,
        sourceDailyId = null,
    )
    private val q3 = QuoteEntity(
        id = "q3",
        text = "camus widget",
        authorId = "a-camus",
        bookId = "b-myth",
        collectionId = null,
        tagIds = listOf("t-absurd", "t-stoic"),
        inPushPlaylist = false,
        inWidgetPlaylist = true,
        sourceDailyId = null,
    )

    @BeforeEach
    fun setUp() {
        dao = mockk(relaxed = true)
        pendingDao = mockk(relaxed = true)
        repository = QuoteRepositoryImpl(dao, pendingDao)
        every { dao.observeAll() } returns flowOf(listOf(q1, q2, q3))
    }

    @Nested
    @DisplayName("observeFiltered by playlist")
    inner class FilterByPlaylist {

        @Test
        @DisplayName("returns only quotes flagged for the push playlist")
        fun push() = runTest {
            repository.observeFiltered(QuoteFilter(playlist = QuotePlaylist.PUSH)).test {
                assertEquals(listOf("q1"), awaitItem().map { it.id })
                awaitComplete()
            }
        }

        @Test
        @DisplayName("returns a quote that is in both playlists for the widget playlist too")
        fun widgetIncludesDualMembership() = runTest {
            repository.observeFiltered(QuoteFilter(playlist = QuotePlaylist.WIDGET)).test {
                assertEquals(listOf("q1", "q3"), awaitItem().map { it.id })
                awaitComplete()
            }
        }
    }

    @Nested
    @DisplayName("observeFiltered")
    inner class ObserveFiltered {

        @Test
        @DisplayName("filters by collection")
        fun byCollection() = runTest {
            repository.observeFiltered(QuoteFilter(collectionId = "c-fav")).test {
                assertEquals(listOf("q2"), awaitItem().map { it.id })
                awaitComplete()
            }
        }

        @Test
        @DisplayName("returns everything for an empty filter")
        fun emptyFilter() = runTest {
            repository.observeFiltered(QuoteFilter()).test {
                assertEquals(listOf("q1", "q2", "q3"), awaitItem().map { it.id })
                awaitComplete()
            }
        }
    }

    @Nested
    @DisplayName("reads and writes")
    inner class ReadsAndWrites {

        @Test
        @DisplayName("getById maps a found entity")
        fun getByIdMaps() = runTest {
            coEvery { dao.getById("q1") } returns q1

            assertEquals("q1", repository.getById("q1")?.id)
        }

        @Test
        @DisplayName("getById returns null when missing")
        fun getByIdMissing() = runTest {
            coEvery { dao.getById("q1") } returns null

            assertNull(repository.getById("q1"))
        }

        @Test
        @DisplayName("edit writes to Room and enqueues an UPSERT pending op")
        fun editEnqueues() = runTest {
            val entity = slot<QuoteEntity>()
            val op = slot<PendingOperationEntity>()

            repository.edit(q1.toDomainQuote())

            coVerify { dao.upsert(capture(entity)) }
            assertEquals(q1.copy(createdAt = entity.captured.createdAt), entity.captured)
            coVerify { pendingDao.insert(capture(op)) }
            assertEquals("QUOTE:q1", op.captured.key)
            assertEquals("UPSERT", op.captured.opType)
        }

        @Test
        @DisplayName("edit stamps createdAt on a quote Room has never seen")
        fun editStampsCreatedAt() = runTest {
            val entity = slot<QuoteEntity>()
            coEvery { dao.getById("q1") } returns null
            val before = System.currentTimeMillis()

            repository.edit(q1.toDomainQuote())

            coVerify { dao.upsert(capture(entity)) }
            assertTrue(entity.captured.createdAt >= before)
        }

        @Test
        @DisplayName("edit keeps the stored createdAt so an edit does not reorder the list")
        fun editPreservesCreatedAt() = runTest {
            val entity = slot<QuoteEntity>()
            coEvery { dao.getById("q1") } returns q1.copy(createdAt = 1_700_000_000_000L)

            repository.edit(q1.toDomainQuote().copy(text = "edited"))

            coVerify { dao.upsert(capture(entity)) }
            assertEquals(1_700_000_000_000L, entity.captured.createdAt)
        }

        @Test
        @DisplayName("deleteById removes from Room and enqueues a DELETE pending op")
        fun deleteEnqueues() = runTest {
            val op = slot<PendingOperationEntity>()

            repository.deleteById("q1")

            coVerify { dao.deleteById("q1") }
            coVerify { pendingDao.insert(capture(op)) }
            assertEquals("DELETE", op.captured.opType)
        }

        @Test
        @DisplayName("setInPushPlaylist updates Room and enqueues an UPSERT pending op")
        fun pushEnqueues() = runTest {
            repository.setInPushPlaylist("q1", true)

            coVerify { dao.setInPushPlaylist("q1", true) }
            coVerify { pendingDao.insert(any()) }
        }
    }

    private fun QuoteEntity.toDomainQuote() = Quote(
        id = id,
        text = text,
        authorId = authorId,
        bookId = bookId,
        collectionId = collectionId,
        tagIds = tagIds,
        inPushPlaylist = inPushPlaylist,
        inWidgetPlaylist = inWidgetPlaylist,
        sourceDailyId = sourceDailyId,
    )
}
