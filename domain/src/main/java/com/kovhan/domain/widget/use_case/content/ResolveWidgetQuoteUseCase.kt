package com.kovhan.domain.widget.use_case.content

import com.kovhan.core.models.widget.WidgetQuote
import com.kovhan.domain.daily.localizedAuthor
import com.kovhan.domain.daily.localizedBook
import com.kovhan.domain.daily.localizedText
import com.kovhan.domain.daily.use_case.GetCachedDailyQuoteUseCase
import com.kovhan.domain.library.use_case.quote.ObserveEnrichedQuoteByIdUseCase
import com.kovhan.domain.settings.use_case.GetLanguageUseCase
import com.kovhan.domain.widget.WidgetQuoteRef
import com.kovhan.domain.widget.toWidgetQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Turns a snapshot ref into the quote to render, resolving either namespace
 * ([WidgetQuoteRef]). Null when the ref no longer points at anything — the
 * caller then rotates on.
 */
class ResolveWidgetQuoteUseCase @Inject constructor(
    private val observeEnrichedById: ObserveEnrichedQuoteByIdUseCase,
    private val getCachedDailyQuote: GetCachedDailyQuoteUseCase,
    private val getLanguage: GetLanguageUseCase,
) {
    suspend operator fun invoke(ref: String): WidgetQuote? = observe(ref).first()

    fun observe(ref: String): Flow<WidgetQuote?> =
        if (WidgetQuoteRef.isDaily(ref)) {
            flow { emit(resolveDaily(ref)) }
        } else {
            observeEnrichedById(ref).map { it?.toWidgetQuote() }
        }

    private suspend fun resolveDaily(ref: String): WidgetQuote? {
        val daily = getCachedDailyQuote()?.takeIf { WidgetQuoteRef.daily(it.id) == ref } ?: return null
        val language = getLanguage().first()
        return WidgetQuote(
            id = ref,
            text = daily.localizedText(language),
            authorName = daily.localizedAuthor(language),
            bookName = daily.localizedBook(language),
        )
    }
}
