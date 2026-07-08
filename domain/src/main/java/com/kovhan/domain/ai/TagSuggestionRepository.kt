package com.kovhan.domain.ai

/**
 * Suggests thematic tags for a quote. Backed by an on-device or remote model;
 * called on demand (never automatically) so it stays cheap. An empty list means
 * nothing could be suggested or the request failed.
 */
interface TagSuggestionRepository {
    suspend fun suggest(quote: String): List<String>
}
