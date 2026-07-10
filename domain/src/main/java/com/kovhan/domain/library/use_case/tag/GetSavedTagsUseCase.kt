package com.kovhan.domain.library.use_case.tag

import com.kovhan.core.models.quote.QuoteFilter
import com.kovhan.core.models.collections.SavedTag
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedTagRepository
import com.kovhan.domain.library.matches
import javax.inject.Inject

class GetSavedTagsUseCase @Inject constructor(
    private val tagRepository: SavedTagRepository,
    private val quoteRepository: QuoteRepository,
) {
    suspend operator fun invoke(withCount: Boolean = false): List<SavedTag> {
        val tags = tagRepository.getAll()
        if (!withCount) return tags
        val quotes = quoteRepository.getAll()
        return tags.map { tag ->
            tag.copy(quoteCount = quotes.count { it.matches(QuoteFilter(tagId = tag.id)) })
        }
    }
}
