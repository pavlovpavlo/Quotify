package com.kovhan.data.library.repository

import app.cash.turbine.test
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.library.SavedAuthorDao
import com.kovhan.data.library.local.library.SavedAuthorEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SavedAuthorRepositoryImpl")
class SavedAuthorRepositoryImplTest {

    private lateinit var dao: SavedAuthorDao
    private lateinit var pendingDao: PendingOperationDao
    private lateinit var repository: SavedAuthorRepositoryImpl

    private val entity = SavedAuthorEntity("a1", "Camus")
    private val domain = SavedAuthor("a1", "Camus")

    @BeforeEach
    fun setUp() {
        dao = mockk(relaxed = true)
        pendingDao = mockk(relaxed = true)
        repository = SavedAuthorRepositoryImpl(dao, pendingDao)
    }

    @Test
    @DisplayName("getAll maps the local entities into domain models")
    fun getAllMaps() = runTest {
        coEvery { dao.getAll() } returns listOf(entity)

        assertEquals(listOf(domain), repository.getAll())
    }

    @Test
    @DisplayName("getById maps a found entity into a domain model")
    fun getByIdMaps() = runTest {
        coEvery { dao.getById("a1") } returns entity

        assertEquals(domain, repository.getById("a1"))
    }

    @Test
    @DisplayName("getById returns null when the entity is missing")
    fun getByIdMissing() = runTest {
        coEvery { dao.getById("a1") } returns null

        assertNull(repository.getById("a1"))
    }

    @Test
    @DisplayName("observeAll maps each emitted list into domain models")
    fun observeAllMaps() = runTest {
        every { dao.observeAll() } returns flowOf(listOf(entity))

        repository.observeAll().test {
            assertEquals(listOf(domain), awaitItem())
            awaitComplete()
        }
    }

    @Test
    @DisplayName("edit writes to Room and enqueues an UPSERT pending op")
    fun editEnqueues() = runTest {
        val op = slot<PendingOperationEntity>()

        repository.edit(domain)

        coVerify { dao.upsert(entity) }
        coVerify { pendingDao.insert(capture(op)) }
        assertEquals("AUTHOR:a1", op.captured.key)
        assertEquals("UPSERT", op.captured.opType)
    }

    @Test
    @DisplayName("deleteById removes from Room and enqueues a DELETE pending op")
    fun deleteEnqueues() = runTest {
        val op = slot<PendingOperationEntity>()

        repository.deleteById("a1")

        coVerify { dao.deleteById("a1") }
        coVerify { pendingDao.insert(capture(op)) }
        assertEquals("DELETE", op.captured.opType)
    }
}
