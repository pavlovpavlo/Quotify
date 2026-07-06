package com.kovhan.data.library.repository

import com.kovhan.core.models.SavedTag
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toDto
import com.kovhan.data.library.remote.SavedTagRemoteDataSource
import com.kovhan.domain.library.SavedTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedTagRepositoryImpl @Inject constructor(
    private val remote: SavedTagRemoteDataSource,
) : SavedTagRepository {

    override suspend fun getAll(): List<SavedTag> =
        remote.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): SavedTag? =
        remote.getById(id)?.toDomain()

    override fun observeAll(): Flow<List<SavedTag>> =
        remote.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun deleteById(id: String) = remote.deleteById(id)

    override suspend fun edit(item: SavedTag) = remote.edit(item.toDto())
}
