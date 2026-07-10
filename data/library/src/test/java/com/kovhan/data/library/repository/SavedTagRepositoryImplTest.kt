package com.kovhan.data.library.repository

import app.cash.turbine.test
import com.kovhan.core.models.SavedTag
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.data.library.local.library.PendingOperationEntity
import com.kovhan.data.library.local.library.SavedTagDao
import com.kovhan.data.library.local.library.SavedTagEntity
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

@DisplayName("SavedTagRepositoryImpl")
class SavedTagRepositoryImplTest {

    private lateinit var dao: SavedTagDao
    private lateinit var pendingDao: PendingOperationDao
    private lateinit var repository: SavedTagRepositoryImpl

    private val entity = SavedTagEntity("t1", "stoic")
    private val domain = SavedTag("t1", "stoic")

    @BeforeEach
    fun setUp() {
        dao = mockk(relaxed = true)
        pendingDao = mockk(relaxed = true)
        repository = SavedTagRepositoryImpl(dao, pendingDao)
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
        coEvery { dao.getById("t1") } returns entity

        assertEquals(domain, repository.getById("t1"))
    }

    @Test
    @DisplayName("getById returns null when the entity is missing")
    fun getByIdMissing() = runTest {
        coEvery { dao.getById("t1") } returns null

        assertNull(repository.getById("t1"))
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
        assertEquals("TAG:t1", op.captured.key)
        assertEquals("UPSERT", op.captured.opType)
    }

    @Test
    @DisplayName("deleteById removes from Room and enqueues a DELETE pending op")
    fun deleteEnqueues() = runTest {
        val op = slot<PendingOperationEntity>()

        repository.deleteById("t1")

        coVerify { dao.deleteById("t1") }
        coVerify { pendingDao.insert(capture(op)) }
        assertEquals("DELETE", op.captured.opType)
    }
}
