package com.kovhan.domain.library.use_case.author

import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.SavedAuthor
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedAuthorRepository
import com.kovhan.domain.library.matches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveSavedAuthorsUseCase @Inject constructor(
    private val authorRepository: SavedAuthorRepository,
    private val quoteRepository: QuoteRepository,
) {
    operator fun invoke(withCount: Boolean = false): Flow<List<SavedAuthor>> =
        if (!withCount) {
            authorRepository.observeAll()
        } else {
            combine(
                authorRepository.observeAll(),
                quoteRepository.observeAll(),
            ) { authors, quotes ->
                authors.map { author ->
                    author.copy(quoteCount = quotes.count { it.matches(QuoteFilter(authorId = author.id)) })
                }
            }
        }
}
