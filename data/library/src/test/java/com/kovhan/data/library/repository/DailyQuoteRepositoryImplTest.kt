package com.kovhan.data.library.repository

import com.kovhan.data.library.dto.DailyQuoteDto
import com.kovhan.data.library.local.daily.DailyQuoteDao
import com.kovhan.data.library.local.daily.DailyQuoteEntity
import com.kovhan.data.library.local.daily.DailySeenDao
import com.kovhan.data.library.local.daily.DailySeenEntity
import com.kovhan.data.library.local.daily.DailySelectionDao
import com.kovhan.data.library.local.daily.DailySelectionEntity
import com.kovhan.data.library.remote.DailyQuoteRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("DailyQuoteRepositoryImpl")
class DailyQuoteRepositoryImplTest {

    private lateinit var remote: DailyQuoteRemoteDataSource
    private lateinit var quoteDao: DailyQuoteDao
    private lateinit var selectionDao: DailySelectionDao
    private lateinit var seenDao: DailySeenDao
    private lateinit var repository: DailyQuoteRepositoryImpl

    private val today = LocalDate.now().toEpochDay()
    private val q1 =
        DailyQuoteEntity("q1", "Stay hungry", "Будь голодним", "Jobs", "Джобс", "Speech", "Промова")
    private val q2 =
        DailyQuoteEntity("q2", "Know thyself", "Пізнай себе", "Socrates", "Сократ", null, null)

    @BeforeEach
    fun setUp() {
        remote = mockk(relaxed = true)
        quoteDao = mockk(relaxed = true)
        selectionDao = mockk(relaxed = true)
        seenDao = mockk(relaxed = true)
        repository = DailyQuoteRepositoryImpl(remote, quoteDao, selectionDao, seenDao)
    }

    @Test
    @DisplayName("prefetch replaces the cached pool when the remote returns quotes")
    fun prefetchReplaces() = runTest {
        coEvery { remote.getAll() } returns
            listOf(
                DailyQuoteDto("q1", "Stay hungry", "Будь голодним", "Jobs", "Джобс", "Speech", "Промова"),
            )

        repository.prefetch()

        coVerify { quoteDao.replaceAll(listOf(q1)) }
    }

    @Test
    @DisplayName("prefetch keeps the cached pool when the remote returns nothing")
    fun prefetchKeepsCacheWhenEmpty() = runTest {
        coEvery { remote.getAll() } returns emptyList()

        repository.prefetch()

        coVerify(exactly = 0) { quoteDao.replaceAll(any()) }
    }

    @Test
    @DisplayName("getDailyQuote returns null when the pool is empty")
    fun emptyPool() = runTest {
        coEvery { quoteDao.getAll() } returns emptyList()

        assertNull(repository.getDailyQuote())
    }

    @Test
    @DisplayName("getDailyQuote returns the stored quote when selected earlier today")
    fun sameDayReturnsStored() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1, q2)
        coEvery { selectionDao.get() } returns DailySelectionEntity(quoteId = "q2", epochDay = today)

        assertEquals("q2", repository.getDailyQuote()?.id)
        coVerify(exactly = 0) { selectionDao.set(any()) }
    }

    @Test
    @DisplayName("getDailyQuote re-rolls and persists when the stored day is not today")
    fun newDayReRolls() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns
            DailySelectionEntity(quoteId = "q2", epochDay = today - 1)

        val stored = slot<DailySelectionEntity>()
        coEvery { selectionDao.set(capture(stored)) } returns Unit

        assertEquals("q1", repository.getDailyQuote()?.id)
        assertEquals("q1", stored.captured.quoteId)
        assertEquals(today, stored.captured.epochDay)
    }

    @Test
    @DisplayName("getDailyQuote re-rolls when the stored quote is no longer in the pool")
    fun staleSelectionReRolls() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns
            DailySelectionEntity(quoteId = "gone", epochDay = today)

        assertEquals("q1", repository.getDailyQuote()?.id)
        coVerify { selectionDao.set(DailySelectionEntity(quoteId = "q1", epochDay = today)) }
    }

    @Test
    @DisplayName("getDailyQuote prefers a quote that has not been shown yet")
    fun prefersUnseenQuote() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1, q2)
        coEvery { selectionDao.get() } returns null
        coEvery { seenDao.getAllIds() } returns listOf("q1")

        assertEquals("q2", repository.getDailyQuote()?.id)
        coVerify { seenDao.insert(DailySeenEntity("q2")) }
    }

    @Test
    @DisplayName("getDailyQuote resets the seen history once every quote has been shown")
    fun resetsSeenWhenExhausted() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns null
        coEvery { seenDao.getAllIds() } returns listOf("q1")

        assertEquals("q1", repository.getDailyQuote()?.id)
        coVerify { seenDao.clear() }
    }

    @Test
    @DisplayName("getDailyQuote still picks from the cached pool when the remote is unreachable")
    fun offlineFallsBackToCachedPool() = runTest {
        coEvery { remote.getAll() } throws RuntimeException("offline")
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns null

        assertEquals("q1", repository.getDailyQuote()?.id)
    }

    @Test
    @DisplayName("ensureDailyQuote rolls a new quote for today without touching the network")
    fun ensurePicksOffline() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns
            DailySelectionEntity(quoteId = "q2", epochDay = today - 1)

        assertEquals("q1", repository.ensureDailyQuote()?.id)
        coVerify { selectionDao.set(DailySelectionEntity(quoteId = "q1", epochDay = today)) }
        coVerify(exactly = 0) { remote.getAll() }
    }

    @Test
    @DisplayName("ensureDailyQuote reuses today's selection")
    fun ensureReusesTodaySelection() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1, q2)
        coEvery { selectionDao.get() } returns DailySelectionEntity(quoteId = "q2", epochDay = today)

        assertEquals("q2", repository.ensureDailyQuote()?.id)
        coVerify(exactly = 0) { selectionDao.set(any()) }
    }

    @Test
    @DisplayName("getDailyQuote hides the library card once dismissed for today")
    fun dismissedHidesLibraryCard() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns
            DailySelectionEntity(quoteId = "q1", epochDay = today, dismissedEpochDay = today)

        assertNull(repository.getDailyQuote())
    }

    @Test
    @DisplayName("dismissing for today leaves the widget's quote intact")
    fun dismissedKeepsWidgetQuote() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns
            DailySelectionEntity(quoteId = "q1", epochDay = today, dismissedEpochDay = today)

        assertEquals("q1", repository.getCachedDailyQuote()?.id)
        assertEquals("q1", repository.ensureDailyQuote()?.id)
    }

    @Test
    @DisplayName("a dismissal from yesterday no longer hides today's card")
    fun yesterdaysDismissalExpires() = runTest {
        coEvery { quoteDao.getAll() } returns listOf(q1)
        coEvery { selectionDao.get() } returns
            DailySelectionEntity(quoteId = "q2", epochDay = today - 1, dismissedEpochDay = today - 1)

        assertEquals("q1", repository.getDailyQuote()?.id)
    }
}
