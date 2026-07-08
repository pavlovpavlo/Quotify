package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.Quote
import com.kovhan.core.models.SavedAuthor
import com.kovhan.core.models.SavedBook
import com.kovhan.core.models.SavedCollection
import com.kovhan.core.models.SavedTag
import com.kovhan.domain.common.IdGenerator
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
import javax.inject.Inject

class SaveQuoteToCollectionUseCase @Inject constructor(
    private val authorRepository: SavedAuthorRepository,
    private val bookRepository: SavedBookRepository,
    private val tagRepository: SavedTagRepository,
    private val editQuote: EditQuoteUseCase,
    private val idGenerator: IdGenerator,
) {
    suspend operator fun invoke(
        text: String,
        authorName: String?,
        bookName: String?,
        tagNames: List<String>,
        collectionId: String,
        inWidgetPlaylist: Boolean,
        inPushPlaylist: Boolean,
        generalName: String,
    ) {
        val authorId = resolveAuthorId(authorName)
        val bookId = resolveBookId(bookName)
        val tagIds = resolveTagIds(tagNames)

        editQuote(
            Quote(
                id = idGenerator.generate(),
                text = text.trim(),
                authorId = authorId,
                bookId = bookId,
                collectionId = collectionId.takeUnless { it == SavedCollection.GENERAL_ID },
                tagIds = tagIds,
                inWidgetPlaylist = inWidgetPlaylist,
                inPushPlaylist = inPushPlaylist,
            ),
            generalName = generalName,
        )
    }

    private suspend fun resolveAuthorId(name: String?): String? {
        val trimmed = name?.trim().takeUnless { it.isNullOrEmpty() } ?: return null
        authorRepository.getAll()
            .firstOrNull { it.name.equals(trimmed, ignoreCase = true) }
            ?.let { return it.id }

        val id = idGenerator.generate()
        authorRepository.edit(SavedAuthor(id = id, name = trimmed))
        return id
    }

    private suspend fun resolveBookId(name: String?): String? {
        val trimmed = name?.trim().takeUnless { it.isNullOrEmpty() } ?: return null
        bookRepository.getAll()
            .firstOrNull { it.name.equals(trimmed, ignoreCase = true) }
            ?.let { return it.id }

        val id = idGenerator.generate()
        bookRepository.edit(SavedBook(id = id, name = trimmed))
        return id
    }

    private suspend fun resolveTagIds(names: List<String>): List<String> {
        val existing = tagRepository.getAll()
        return names
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinctBy { it.lowercase() }
            .map { name ->
                existing.firstOrNull { it.name.equals(name, ignoreCase = true) }?.id
                    ?: createTag(name)
            }
    }

    private suspend fun createTag(name: String): String {
        val id = idGenerator.generate()
        tagRepository.edit(SavedTag(id = id, name = name))
        return id
    }
}
