package com.kovhan.domain.library.use_case.tag

import com.kovhan.core.models.QuoteFilter
import com.kovhan.core.models.SavedTag
import com.kovhan.domain.library.QuoteRepository
import com.kovhan.domain.library.SavedTagRepository
import com.kovhan.domain.library.matches
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveSavedTagsUseCase @Inject constructor(
    private val tagRepository: SavedTagRepository,
    private val quoteRepository: QuoteRepository,
) {
    operator fun invoke(withCount: Boolean = false): Flow<List<SavedTag>> =
        if (!withCount) {
            tagRepository.observeAll()
        } else {
            combine(
                tagRepository.observeAll(),
                quoteRepository.observeAll(),
            ) { tags, quotes ->
                tags.map { tag ->
                    tag.copy(quoteCount = quotes.count { it.matches(QuoteFilter(tagId = tag.id)) })
                }
            }
        }
}
