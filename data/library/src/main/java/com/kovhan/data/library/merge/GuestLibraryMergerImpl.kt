package com.kovhan.data.library.merge

import com.kovhan.core.models.quote.Quote
import com.kovhan.core.models.collections.SavedAuthor
import com.kovhan.core.models.collections.SavedBook
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.data.library.local.library.PendingOperationDao
import com.kovhan.domain.library.CollectionRepository
import com.kovhan.domain.library.GuestLibraryMerger
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.SavedBookRepository
import com.kovhan.domain.library.SavedTagRepository
import com.kovhan.domain.library.model.LibrarySnapshot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuestLibraryMergerImpl @Inject constructor(
    private val quoteRepository: QuoteRepository,
    private val collectionRepository: CollectionRepository,
    private val authorRepository: SavedAuthorRepository,
    private val bookRepository: SavedBookRepository,
    private val tagRepository: SavedTagRepository,
    private val pendingDao: PendingOperationDao,
) : GuestLibraryMerger {

    override suspend fun snapshot(): LibrarySnapshot = LibrarySnapshot(
        quotes = quoteRepository.getAll(),
        collections = collectionRepository.getAll(),
        authors = authorRepository.getAll(),
        books = bookRepository.getAll(),
        tags = tagRepository.getAll(),
    )

    override suspend fun clearPending() {
        pendingDao.clear()
    }

    override suspend fun merge(snapshot: LibrarySnapshot) {
        val collectionRemap = mergeCollections(snapshot.collections)
        val authorRemap = mergeAuthors(snapshot.authors)
        val bookRemap = mergeBooks(snapshot.books)
        val tagRemap = mergeTags(snapshot.tags)
        mergeQuotes(snapshot.quotes, collectionRemap, authorRemap, bookRemap, tagRemap)
    }

    private suspend fun mergeCollections(guests: List<SavedCollection>): Map<String, String> {
        val target = collectionRepository.getAll()
        val byName = target.associateBy { norm(it.name) }
        val byId = target.associateBy { it.id }
        val remap = HashMap<String, String>()
        for (guest in guests) {
            if (guest.id == SavedCollection.GENERAL_ID || guest.id == SavedCollection.FAVOURITES_ID) {
                remap[guest.id] = guest.id
                if (!byId.containsKey(guest.id)) collectionRepository.edit(guest)
                continue
            }
            val existing = byName[norm(guest.name)]
            if (existing != null) {
                remap[guest.id] = existing.id
            } else {
                collectionRepository.edit(guest)
                remap[guest.id] = guest.id
            }
        }
        return remap
    }

    private suspend fun mergeAuthors(guests: List<SavedAuthor>): Map<String, String> {
        val byName = authorRepository.getAll().associateBy { norm(it.name) }
        val remap = HashMap<String, String>()
        for (guest in guests) {
            val existing = byName[norm(guest.name)]
            if (existing != null) {
                remap[guest.id] = existing.id
            } else {
                authorRepository.edit(guest)
                remap[guest.id] = guest.id
            }
        }
        return remap
    }

    private suspend fun mergeBooks(guests: List<SavedBook>): Map<String, String> {
        val byName = bookRepository.getAll().associateBy { norm(it.name) }
        val remap = HashMap<String, String>()
        for (guest in guests) {
            val existing = byName[norm(guest.name)]
            if (existing != null) {
                remap[guest.id] = existing.id
            } else {
                bookRepository.edit(guest)
                remap[guest.id] = guest.id
            }
        }
        return remap
    }

    private suspend fun mergeTags(guests: List<SavedTag>): Map<String, String> {
        val byName = tagRepository.getAll().associateBy { norm(it.name) }
        val remap = HashMap<String, String>()
        for (guest in guests) {
            val existing = byName[norm(guest.name)]
            if (existing != null) {
                remap[guest.id] = existing.id
            } else {
                tagRepository.edit(guest)
                remap[guest.id] = guest.id
            }
        }
        return remap
    }

    private suspend fun mergeQuotes(
        guests: List<Quote>,
        collectionRemap: Map<String, String>,
        authorRemap: Map<String, String>,
        bookRemap: Map<String, String>,
        tagRemap: Map<String, String>,
    ) {
        val target = quoteRepository.getAll().associateBy { it.id }
        for (guest in guests) {
            val remapped = guest.copy(
                authorId = guest.authorId?.let { authorRemap[it] ?: it },
                bookId = guest.bookId?.let { bookRemap[it] ?: it },
                collectionId = guest.collectionId?.let { collectionRemap[it] ?: it },
                tagIds = guest.tagIds.map { tagRemap[it] ?: it }.distinct(),
            )
            val existing = target[guest.id]
            if (existing == null) {
                quoteRepository.edit(remapped)
            } else {
                quoteRepository.edit(
                    existing.copy(
                        authorId = existing.authorId ?: remapped.authorId,
                        bookId = existing.bookId ?: remapped.bookId,
                        collectionId = existing.collectionId ?: remapped.collectionId,
                        tagIds = (existing.tagIds + remapped.tagIds).distinct(),
                        inPushPlaylist = existing.inPushPlaylist || remapped.inPushPlaylist,
                        inWidgetPlaylist = existing.inWidgetPlaylist || remapped.inWidgetPlaylist,
                    ),
                )
            }
        }
    }

    private fun norm(name: String): String = name.trim().lowercase()
}
