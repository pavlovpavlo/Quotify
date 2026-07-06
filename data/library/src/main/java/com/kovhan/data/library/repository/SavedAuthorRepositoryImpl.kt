package com.kovhan.data.library.repository

import com.kovhan.core.models.SavedAuthor
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toDto
import com.kovhan.data.library.remote.SavedAuthorRemoteDataSource
import com.kovhan.domain.library.SavedAuthorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedAuthorRepositoryImpl @Inject constructor(
    private val remote: SavedAuthorRemoteDataSource,
) : SavedAuthorRepository {

    override suspend fun getAll(): List<SavedAuthor> =
        remote.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): SavedAuthor? =
        remote.getById(id)?.toDomain()

    override fun observeAll(): Flow<List<SavedAuthor>> =
        remote.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun deleteById(id: String) = remote.deleteById(id)

    override suspend fun edit(item: SavedAuthor) = remote.edit(item.toDto())
}
