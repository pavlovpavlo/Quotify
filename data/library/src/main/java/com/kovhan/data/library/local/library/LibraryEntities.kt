package com.kovhan.data.library.local.library

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey val id: String,
    val text: String,
    val authorId: String?,
    val bookId: String?,
    val collectionId: String?,
    val tagIds: List<String>,
    val inPushPlaylist: Boolean,
    val inWidgetPlaylist: Boolean,
    val sourceDailyId: String?,
    val page: Int? = null,
    val isFavourite: Boolean = false,
    val createdAt: Long = 0L,
)

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val iconId: String,
    val iconColor: String,
)

@Entity(tableName = "saved_authors")
data class SavedAuthorEntity(
    @PrimaryKey val id: String,
    val name: String,
)

@Entity(tableName = "saved_books")
data class SavedBookEntity(
    @PrimaryKey val id: String,
    val name: String,
)

@Entity(tableName = "saved_tags")
data class SavedTagEntity(
    @PrimaryKey val id: String,
    val name: String,
)
