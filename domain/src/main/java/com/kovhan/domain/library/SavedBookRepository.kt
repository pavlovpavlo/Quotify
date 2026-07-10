package com.kovhan.domain.library

import com.kovhan.core.models.collections.SavedBook
import kotlinx.coroutines.flow.Flow

interface SavedBookRepository {
    suspend fun getAll(): List<SavedBook>

    suspend fun getById(id: String): SavedBook?

    fun observeAll(): Flow<List<SavedBook>>

    suspend fun deleteById(id: String)

    suspend fun edit(item: SavedBook)
}
