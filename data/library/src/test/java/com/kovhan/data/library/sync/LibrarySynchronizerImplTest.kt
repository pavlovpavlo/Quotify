package com.kovhan.data.library.sync

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kovhan.data.library.local.library.CollectionDao
import com.kovhan.data.library.local.library.PendingEntityType
import com.kovhan.data.library.local.library.PendingOpType
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.library.QuoteDao
import com.kovhan.data.library.local.library.QuoteEntity
import com.kovhan.data.library.local.library.SavedAuthorDao
import com.kovhan.data.library.local.library.SavedBookDao
import com.kovhan.data.library.local.library.SavedTagDao
import com.kovhan.data.library.local.widget.PlaylistDao
import com.kovhan.data.library.remote.CollectionRemoteDataSource
import com.kovhan.data.library.remote.PlaylistRemoteDataSource
import com.kovhan.data.library.remote.QuoteRemoteDataSource
import com.kovhan.data.library.remote.SavedAuthorRemoteDataSource
import com.kovhan.data.library.remote.SavedBookRemoteDataSource
import com.kovhan.data.library.remote.SavedTagRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("LibrarySynchronizerImpl")
class LibrarySynchronizerImplTest {

    private val auth: FirebaseAuth = mockk()
    private val pendingDao: PendingOperationDao = mockk(relaxed = true)
    private val quoteDao: QuoteDao = mockk(relaxed = true)
    private val collectionDao: CollectionDao = mockk(relaxed = true)
    private val authorDao: SavedAuthorDao = mockk(relaxed = true)
    private val bookDao: SavedBookDao = mockk(relaxed = true)
    private val tagDao: SavedTagDao = mockk(relaxed = true)
    private val playlistDao: PlaylistDao = mockk(relaxed = true)
    private val quoteRemote: QuoteRemoteDataSource = mockk(relaxed = true)
    private val collectionRemote: CollectionRemoteDataSource = mockk(relaxed = true)
    private val authorRemote: SavedAuthorRemoteDataSource = mockk(relaxed = true)
    private val bookRemote: SavedBookRemoteDataSource = mockk(relaxed = true)
    private val tagRemote: SavedTagRemoteDataSource = mockk(relaxed = true)
    private val playlistRemote: PlaylistRemoteDataSource = mockk(relaxed = true)

    private lateinit var synchronizer: LibrarySynchronizerImpl

    private val quoteEntity = QuoteEntity(
        id = "q1", text = "t", authorId = null, bookId = null, collectionId = null,
        tagIds = emptyList(), inPushPlaylist = false, inWidgetPlaylist = false, sourceDailyId = null,
    )

    @BeforeEach
    fun setUp() {
        synchronizer = LibrarySynchronizerImpl(
            auth, pendingDao, quoteDao, collectionDao, authorDao, bookDao, tagDao, playlistDao,
            quoteRemote, collectionRemote, authorRemote, bookRemote, tagRemote, playlistRemote,
        )
    }

    private fun signedIn() {
        every { auth.currentUser } returns mockk<FirebaseUser>()
    }

    private fun upsertOp(id: String) = PendingOperationEntity.of(
        PendingEntityType.QUOTE, id, PendingOpType.UPSERT, createdAt = 1L,
    )

    private fun deleteOp(id: String) = PendingOperationEntity.of(
        PendingEntityType.QUOTE, id, PendingOpType.DELETE, createdAt = 2L,
    )

    @Test
    @DisplayName("syncPendingChanges returns false and does nothing when signed out")
    fun syncSignedOut() = runTest {
        every { auth.currentUser } returns null

        assertFalse(synchronizer.syncPendingChanges())
        coVerify(exactly = 0) { pendingDao.getAllOrdered() }
    }

    @Test
    @DisplayName("an UPSERT op pushes the current Room entity and is then removed")
    fun upsertDrains() = runTest {
        signedIn()
        val op = upsertOp("q1")
        coEvery { pendingDao.getAllOrdered() } returns listOf(op)
        coEvery { quoteDao.getById("q1") } returns quoteEntity

        assertTrue(synchronizer.syncPendingChanges())

        coVerify { quoteRemote.edit(match { it.id == "q1" }) }
        coVerify { pendingDao.deleteByKey(op.key) }
    }

    @Test
    @DisplayName("a DELETE op deletes remotely and is then removed")
    fun deleteDrains() = runTest {
        signedIn()
        val op = deleteOp("q1")
        coEvery { pendingDao.getAllOrdered() } returns listOf(op)

        assertTrue(synchronizer.syncPendingChanges())

        coVerify { quoteRemote.deleteById("q1") }
        coVerify { pendingDao.deleteByKey(op.key) }
    }

    @Test
    @DisplayName("a failing op is left in the queue and the result is not fully drained")
    fun failureLeavesQueue() = runTest {
        signedIn()
        val op = upsertOp("q1")
        coEvery { pendingDao.getAllOrdered() } returns listOf(op)
        coEvery { quoteDao.getById("q1") } returns quoteEntity
        coEvery { quoteRemote.edit(any()) } throws RuntimeException("network")

        assertFalse(synchronizer.syncPendingChanges())

        coVerify(exactly = 0) { pendingDao.deleteByKey(op.key) }
    }

    @Test
    @DisplayName("refreshFromRemote does nothing when signed out")
    fun refreshSignedOut() = runTest {
        every { auth.currentUser } returns null

        synchronizer.refreshFromRemote()

        coVerify(exactly = 0) { quoteRemote.getAll() }
    }

    @Test
    @DisplayName("refreshFromRemote replaces Room from the remote sources")
    fun refreshReplaces() = runTest {
        signedIn()

        synchronizer.refreshFromRemote()

        coVerify { quoteDao.replaceAll(any()) }
        coVerify { collectionDao.replaceAll(any()) }
        coVerify { authorDao.replaceAll(any()) }
        coVerify { bookDao.replaceAll(any()) }
        coVerify { tagDao.replaceAll(any()) }
    }
}
