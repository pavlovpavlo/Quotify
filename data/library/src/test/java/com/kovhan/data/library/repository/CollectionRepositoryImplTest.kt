package com.kovhan.data.library.repository

import app.cash.turbine.test
import com.kovhan.core.models.SavedCollection
import com.kovhan.data.library.dto.CollectionDto
import com.kovhan.data.library.remote.CollectionRemoteDataSource
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

@DisplayName("CollectionRepositoryImpl")
class CollectionRepositoryImplTest {

    private lateinit var remote: CollectionRemoteDataSource
    private lateinit var repository: CollectionRepositoryImpl

    private val dto = CollectionDto("c1", "Stoics", iconId = "scroll", iconColor = "112233")
    private val domain = SavedCollection("c1", "Stoics", iconId = "scroll", iconColor = "112233")

    @BeforeEach
    fun setUp() {
        remote = mockk(relaxed = true)
        repository = CollectionRepositoryImpl(remote)
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
        coEvery { remote.getById("c1") } returns dto

        assertEquals(domain, repository.getById("c1"))
    }

    @Test
    @DisplayName("getById returns null when the dto is missing")
    fun getByIdMissing() = runTest {
        coEvery { remote.getById("c1") } returns null

        assertNull(repository.getById("c1"))
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
        repository.deleteById("c1")

        coVerify { remote.deleteById("c1") }
    }
}
