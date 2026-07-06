package com.kovhan.domain.library

import com.kovhan.core.models.SavedAuthor
import kotlinx.coroutines.flow.Flow

interface SavedAuthorRepository {
    suspend fun getAll(): List<SavedAuthor>

    suspend fun getById(id: String): SavedAuthor?

    fun observeAll(): Flow<List<SavedAuthor>>

    suspend fun deleteById(id: String)

    suspend fun edit(item: SavedAuthor)
}
