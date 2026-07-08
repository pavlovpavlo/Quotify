package com.kovhan.data.scan

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.kovhan.domain.ai.TagSuggestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import timber.log.Timber
import javax.inject.Inject

/**
 * [TagSuggestionRepository] backed by Gemini through Firebase AI Logic.
 *
 * The quote is sent to a text model that returns a short list of thematic tags
 * in the quote's own language (Ukrainian or English). The model is asked to
 * reply with a bare JSON array so the result is trivial to parse; anything else
 * is treated as "no suggestions". Needs the network — an empty list means the
 * request failed or produced nothing usable.
 */
class GeminiTagSuggestionRepository @Inject constructor() : TagSuggestionRepository {

    private val model by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(MODEL_NAME)
    }

    override suspend fun suggest(quote: String): List<String> = withContext(Dispatchers.IO) {
        val trimmed = quote.trim()
        if (trimmed.isEmpty()) return@withContext emptyList()
        try {
            val request = content { text(PROMPT + trimmed) }
            val raw = model.generateContent(request).text.orEmpty()
            parseTags(raw)
        } catch (t: Throwable) {
            Timber.e(t, "Gemini tag suggestion failed")
            emptyList()
        }
    }

    private fun parseTags(raw: String): List<String> {
        val json = raw.substringAfter('[', "").let { if (it.isEmpty()) "" else "[$it" }
            .substringBeforeLast(']', "").let { if (it.isEmpty()) "" else "$it]" }
        if (json.isEmpty()) return emptyList()
        return runCatching {
            val array = JSONArray(json)
            (0 until array.length())
                .map { array.optString(it).trim().trimStart('#').trim() }
                .filter { it.isNotEmpty() }
                .distinctBy { it.lowercase() }
                .take(MAX_TAGS)
        }.getOrElse { emptyList() }
    }

    private companion object {
        const val MODEL_NAME = "gemini-2.5-flash"
        const val MAX_TAGS = 8
        val PROMPT = """
            You suggest thematic tags for a quote so a reader can file it in a library.
            Read the quote below and return between 5 and 8 short tags that capture its
            themes, mood and topics. Each tag is one or two lowercase words, in the SAME
            language as the quote (Ukrainian or English). No hashtags, no explanations.
            Respond with ONLY a JSON array of strings, e.g. ["love","time","hope"].

            Quote:

        """.trimIndent()
    }
}
