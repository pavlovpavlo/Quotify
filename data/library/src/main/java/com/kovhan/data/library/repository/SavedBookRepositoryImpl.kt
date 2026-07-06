package com.kovhan.data.library.repository

import com.kovhan.core.models.SavedBook
import com.kovhan.data.library.mapper.toDomain
import com.kovhan.data.library.mapper.toDto
import com.kovhan.data.library.remote.SavedBookRemoteDataSource
import com.kovhan.domain.library.SavedBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedBookRepositoryImpl @Inject constructor(
    private val remote: SavedBookRemoteDataSource,
) : SavedBookRepository {

    override suspend fun getAll(): List<SavedBook> =
        remote.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): SavedBook? =
        remote.getById(id)?.toDomain()

    override fun observeAll(): Flow<List<SavedBook>> =
        remote.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun deleteById(id: String) = remote.deleteById(id)

    override suspend fun edit(item: SavedBook) = remote.edit(item.toDto())
}
