package com.kovhan.data.library.repository

import app.cash.turbine.test
import com.kovhan.core.models.SavedBook
import com.kovhan.data.library.dto.SavedBookDto
import com.kovhan.data.library.remote.SavedBookRemoteDataSource
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

@DisplayName("SavedBookRepositoryImpl")
class SavedBookRepositoryImplTest {

    private lateinit var remote: SavedBookRemoteDataSource
    private lateinit var repository: SavedBookRepositoryImpl

    private val dto = SavedBookDto("b1", "The Stranger")
    private val domain = SavedBook("b1", "The Stranger")

    @BeforeEach
    fun setUp() {
        remote = mockk(relaxed = true)
        repository = SavedBookRepositoryImpl(remote)
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
        coEvery { remote.getById("b1") } returns dto

        assertEquals(domain, repository.getById("b1"))
    }

    @Test
    @DisplayName("getById returns null when the dto is missing")
    fun getByIdMissing() = runTest {
        coEvery { remote.getById("b1") } returns null

        assertNull(repository.getById("b1"))
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
        repository.deleteById("b1")

        coVerify { remote.deleteById("b1") }
    }
}
