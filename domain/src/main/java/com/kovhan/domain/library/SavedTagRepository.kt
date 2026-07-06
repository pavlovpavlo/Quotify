package com.kovhan.domain.library

import com.kovhan.core.models.SavedTag
import kotlinx.coroutines.flow.Flow

interface SavedTagRepository {
    suspend fun getAll(): List<SavedTag>

    suspend fun getById(id: String): SavedTag?

    fun observeAll(): Flow<List<SavedTag>>

    suspend fun deleteById(id: String)

    suspend fun edit(item: SavedTag)
}
