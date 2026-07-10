package com.kovhan.domain.ai

import com.kovhan.core.models.AiError
import com.kovhan.core.models.Outcome

/**
 * Suggests thematic tags for a quote. Called on demand (never automatically).
 * A successful [Outcome] with an empty list means nothing could be suggested;
 * a [Outcome.Failure] distinguishes an offline device from other errors.
 */
interface TagSuggestionRepository {
    suspend fun suggest(quote: String): Outcome<List<String>, AiError>
}
