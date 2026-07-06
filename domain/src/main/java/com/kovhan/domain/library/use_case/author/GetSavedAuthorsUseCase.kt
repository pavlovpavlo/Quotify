package com.kovhan.domain.library.use_case.author

import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.SavedAuthor
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.matches
import javax.inject.Inject

class GetSavedAuthorsUseCase @Inject constructor(
    private val authorRepository: SavedAuthorRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(withCount: Boolean = false): List<SavedAuthor> {
        val authors = authorRepository.getAll()
        if (!withCount) return authors
        val quotes = quoteRepository.getAll()
        return authors.map { author ->
            author.copy(quoteCount = quotes.count { it.matches(QuoteFilter(authorId = author.id)) })
        }
    }
}
