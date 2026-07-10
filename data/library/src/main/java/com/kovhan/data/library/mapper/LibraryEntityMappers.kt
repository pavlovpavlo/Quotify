package com.kovhan.data.library.mapper

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.models.SavedTag
import com.kovhan.data.library.dto.CollectionDto
import com.kovhan.data.library.dto.QuoteDto
import com.kovhan.data.library.dto.SavedAuthorDto
import com.kovhan.data.library.dto.SavedBookDto
import com.kovhan.data.library.dto.SavedTagDto
import com.kovhan.data.library.local.library.CollectionEntity
import com.kovhan.data.library.local.library.QuoteEntity
import com.kovhan.data.library.local.library.SavedAuthorEntity
import com.kovhan.data.library.local.library.SavedBookEntity
import com.kovhan.data.library.local.library.SavedTagEntity

// region Quote

fun QuoteEntity.toDomain() = Quote(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = collectionId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
)

fun Quote.toEntity() = QuoteEntity(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = collectionId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
)

fun QuoteEntity.toDto() = QuoteDto(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = collectionId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
)

fun QuoteDto.toEntity() = QuoteEntity(
    id = id,
    text = text,
    authorId = authorId,
    bookId = bookId,
    collectionId = collectionId,
    tagIds = tagIds,
    inPushPlaylist = inPushPlaylist,
    inWidgetPlaylist = inWidgetPlaylist,
    sourceDailyId = sourceDailyId,
)

// endregion

// region Collection

fun CollectionEntity.toDomain() = SavedCollection(
    id = id,
    name = name,
    iconId = iconId,
    iconColor = iconColor,
)

fun SavedCollection.toEntity() = CollectionEntity(
    id = id,
    name = name,
    iconId = iconId,
    iconColor = iconColor,
)

fun CollectionEntity.toDto() = CollectionDto(
    id = id,
    name = name,
    iconId = iconId,
    iconColor = iconColor,
)

fun CollectionDto.toEntity() = CollectionEntity(
    id = id,
    name = name,
    iconId = iconId,
    iconColor = iconColor,
)

// endregion

// region SavedAuthor / SavedBook / SavedTag

fun SavedAuthorEntity.toDomain() = SavedAuthor(id = id, name = name)

fun SavedAuthor.toEntity() = SavedAuthorEntity(id = id, name = name)

fun SavedAuthorEntity.toDto() = SavedAuthorDto(id = id, name = name)

fun SavedAuthorDto.toEntity() = SavedAuthorEntity(id = id, name = name)

fun SavedBookEntity.toDomain() = SavedBook(id = id, name = name)

fun SavedBook.toEntity() = SavedBookEntity(id = id, name = name)

fun SavedBookEntity.toDto() = SavedBookDto(id = id, name = name)

fun SavedBookDto.toEntity() = SavedBookEntity(id = id, name = name)

fun SavedTagEntity.toDomain() = SavedTag(id = id, name = name)

fun SavedTag.toEntity() = SavedTagEntity(id = id, name = name)

fun SavedTagEntity.toDto() = SavedTagDto(id = id, name = name)

fun SavedTagDto.toEntity() = SavedTagEntity(id = id, name = name)

// endregion
