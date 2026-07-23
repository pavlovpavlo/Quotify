package com.kovhan.data.library.mapper

import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.data.library.dto.CollectionDto
import com.kovhan.data.library.dto.SavedAuthorDto
import com.kovhan.data.library.dto.SavedBookDto
import com.kovhan.data.library.dto.SavedTagDto

fun SavedAuthorDto.toDomain() = SavedAuthor(id = id, name = name)

fun SavedAuthor.toDto() = SavedAuthorDto(id = id, name = name)

fun SavedTagDto.toDomain() = SavedTag(id = id, name = name)

fun SavedTag.toDto() = SavedTagDto(id = id, name = name)

fun SavedBookDto.toDomain() = SavedBook(id = id, name = name)

fun SavedBook.toDto() = SavedBookDto(id = id, name = name)

fun CollectionDto.toDomain() = SavedCollection(
    id = id,
    name = name,
    iconId = iconId,
    iconColor = iconColor,
)

fun SavedCollection.toDto() = CollectionDto(
    id = id,
    name = name,
    iconId = iconId,
    iconColor = iconColor,
)
