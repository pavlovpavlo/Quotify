package com.kovhan.domain.library

import com.kovhan.core.models.SavedCollection
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    suspend fun getAll(): List<SavedCollection>

    suspend fun getById(id: String): SavedCollection?

    fun observeAll(): Flow<List<SavedCollection>>

    suspend fun deleteById(id: String)

    suspend fun edit(item: SavedCollection)
}
