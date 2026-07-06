package com.kovhan.data.library.repository

import app.cash.turbine.test
import com.kovhan.core.models.SavedTag
import com.kovhan.data.library.dto.SavedTagDto
import com.kovhan.data.library.remote.SavedTagRemoteDataSource
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
import org.junit.jupiter.api.Test

@DisplayName("SavedTagRepositoryImpl")
class SavedTagRepositoryImplTest {

    private lateinit var remote: SavedTagRemoteDataSource
    private lateinit var repository: SavedTagRepositoryImpl

    private val dto = SavedTagDto("t1", "Absurdism")
    private val domain = SavedTag("t1", "Absurdism")

    @BeforeEach
    fun setUp() {
        remote = mockk(relaxed = true)
        repository = SavedTagRepositoryImpl(remote)
    }

    @Test
    @DisplayName("getAll maps the remote dtos into domain models")
    fun getAllMaps() = runTest {
        coEvery { remote.getAll() } returns listOf(dto)

        assertEquals(listOf(domain), repository.getAll())
    }

    @Test
    @DisplayName("getById maps a found dto into a domain model")
    fun getByIdMaps() = runTest {
        coEvery { remote.getById("t1") } returns dto

        assertEquals(domain, repository.getById("t1"))
    }

    @Test
    @DisplayName("getById returns null when the dto is missing")
    fun getByIdMissing() = runTest {
        coEvery { remote.getById("t1") } returns null

        assertNull(repository.getById("t1"))
    }

    @Test
    @DisplayName("observeAll maps each emitted list into domain models")
    fun observeAllMaps() = runTest {
        every { remote.observeAll() } returns flowOf(listOf(dto))

        repository.observeAll().test {
            assertEquals(listOf(domain), awaitItem())
            awaitComplete()
        }
    }

    @Test
    @DisplayName("edit converts the domain model to a dto before writing")
    fun editConverts() = runTest {
        repository.edit(domain)

        coVerify { remote.edit(dto) }
    }

    @Test
    @DisplayName("deleteById forwards the id to the remote source")
    fun deleteForwards() = runTest {
        repository.deleteById("t1")

        coVerify { remote.deleteById("t1") }
    }
}
