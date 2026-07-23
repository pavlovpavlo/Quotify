package com.kovhan.domain.ai.use_case

import com.kovhan.core.models.AiError
import com.kovhan.core.models.Outcome
import com.kovhan.domain.ai.TagSuggestionRepository
import javax.inject.Inject

class SuggestTagsUseCase @Inject constructor(
    private val repository: TagSuggestionRepository,
) {
    suspend operator fun invoke(quote: String): Outcome<List<String>, AiError> =
        repository.suggest(quote)
}
