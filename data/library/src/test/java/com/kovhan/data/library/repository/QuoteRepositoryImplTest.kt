package com.kovhan.data.library.repository

import app.cash.turbine.test
import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.QuotePlaylist
import com.kovhan.data.library.dto.QuoteDto
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.remote.QuoteRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("QuoteRepositoryImpl")
class QuoteRepositoryImplTest {

    private lateinit var remote: QuoteRemoteDataSource
    private lateinit var repository: QuoteRepositoryImpl

    // q1 is in BOTH playlists; q2 is in neither; q3 is widget-only.
    private val q1 = QuoteDto(
        id = "q1",
        text = "camus push+widget",
        authorId = "a-camus",
        bookId = "b-stranger",
        tagIds = listOf("t-absurd"),
        inPushPlaylist = true,
        inWidgetPlaylist = true,
    )
    private val q2 = QuoteDto(
        id = "q2",
        text = "seneca none",
        authorId = "a-seneca",
        bookId = "b-letters",
        collectionId = "c-fav",
        tagIds = listOf("t-stoic"),
        inPushPlaylist = false,
        inWidgetPlaylist = false,
    )
    private val q3 = QuoteDto(
        id = "q3",
        text = "camus widget",
        authorId = "a-camus",
        bookId = "b-myth",
        tagIds = listOf("t-absurd", "t-stoic"),
        inPushPlaylist = false,
        inWidgetPlaylist = true,
    )

    @BeforeEach
    fun setUp() {
        remote = mockk(relaxed = true)
        repository = QuoteRepositoryImpl(remote)
        every { remote.observeAll() } returns flowOf(listOf(q1, q2, q3))
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
        @DisplayName("filters by author")
        fun byAuthor() = runTest {
            repository.observeFiltered(QuoteFilter(authorId = "a-camus")).test {
                assertEquals(listOf("q1", "q3"), awaitItem().map { it.id })
                awaitComplete()
            }
        }

        @Test
        @DisplayName("filters by tag membership")
        fun byTag() = runTest {
            repository.observeFiltered(QuoteFilter(tagId = "t-stoic")).test {
                assertEquals(listOf("q2", "q3"), awaitItem().map { it.id })
                awaitComplete()
            }
        }

        @Test
        @DisplayName("combines several criteria with AND")
        fun combinesCriteria() = runTest {
            repository.observeFiltered(
                QuoteFilter(authorId = "a-camus", playlist = QuotePlaylist.PUSH),
            ).test {
                assertEquals(listOf("q1"), awaitItem().map { it.id })
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
        @DisplayName("getById maps a found dto")
        fun getByIdMaps() = runTest {
            coEvery { remote.getById("q1") } returns q1

            assertEquals("q1", repository.getById("q1")?.id)
        }

        @Test
        @DisplayName("getById returns null when missing")
        fun getByIdMissing() = runTest {
            coEvery { remote.getById("q1") } returns null

            assertNull(repository.getById("q1"))
        }

        @Test
        @DisplayName("edit converts the quote to a dto before writing")
        fun editConverts() = runTest {
            repository.edit(q1.toDomain())

            coVerify { remote.edit(q1) }
        }

        @Test
        @DisplayName("setInPushPlaylist forwards to the remote source")
        fun pushForwards() = runTest {
            repository.setInPushPlaylist("q1", true)

            coVerify { remote.setInPushPlaylist("q1", true) }
        }

        @Test
        @DisplayName("setInWidgetPlaylist forwards to the remote source")
        fun widgetForwards() = runTest {
            repository.setInWidgetPlaylist("q1", false)

            coVerify { remote.setInWidgetPlaylist("q1", false) }
        }
    }
}
