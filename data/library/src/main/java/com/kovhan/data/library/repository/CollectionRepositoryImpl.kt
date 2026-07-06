package com.kovhan.data.library.repository

import com.kovhan.core.models.SavedCollection
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toDto
import com.kovhan.data.library.remote.CollectionRemoteDataSource
import com.kovhan.domain.library.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepositoryImpl @Inject constructor(
    private val remote: CollectionRemoteDataSource,
) : CollectionRepository {

    override suspend fun getAll(): List<SavedCollection> =
        remote.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): SavedCollection? =
        remote.getById(id)?.toDomain()

    override fun observeAll(): Flow<List<SavedCollection>> =
        remote.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun deleteById(id: String) = remote.deleteById(id)

    override suspend fun edit(item: SavedCollection) = remote.edit(item.toDto())
}
