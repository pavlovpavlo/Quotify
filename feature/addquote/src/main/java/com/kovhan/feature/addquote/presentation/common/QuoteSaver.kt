package com.kovhan.feature.addquote.presentation.common

import com.kovhan.core.analytics.AnalyticsTracker
import com.kovhan.core.analytics.LimitReason
import com.kovhan.core.analytics.event.LimitReached
import com.kovhan.core.analytics.event.QuoteAdded
import com.kovhan.core.models.quote.AddQuoteAction
import com.kovhan.domain.library.use_case.quote.SaveQuoteToCollectionUseCase
import com.kovhan.domain.premium.use_case.CheckQuoteLimitUseCase
import com.kovhan.domain.quote.CheckAddQuoteActionUseCase
import com.kovhan.feature.addquote.presentation.analytics.toAnalytics
import com.kovhan.feature.addquote.presentation.details.mvi.QuoteDraft
import javax.inject.Inject

sealed interface QuoteSaveResult {
    /** Безкоштовний план вичерпав ліміт збережених цитат. */
    data object Blocked : QuoteSaveResult

    data class Saved(val action: AddQuoteAction) : QuoteSaveResult
}

/**
 * Єдина точка збереження чернетки в колекцію: ліміт, запис, аналітика й дія
 * після збереження. Спільна для шита вибору колекції та для входу з порожньої
 * папки, де колекція вже відома і шит не відкривається.
 */
class QuoteSaver @Inject constructor(
    private val saveQuoteToCollection: SaveQuoteToCollectionUseCase,
    private val checkQuoteLimit: CheckQuoteLimitUseCase,
    private val checkAddQuoteAction: CheckAddQuoteActionUseCase,
    private val analytics: AnalyticsTracker,
) {
    suspend fun save(
        draft: QuoteDraft,
        collectionId: String,
        generalName: String,
        widgetPlaced: Boolean,
    ): QuoteSaveResult {
        if (!checkQuoteLimit()) {
            analytics.track(LimitReached(LimitReason.QUOTES))
            return QuoteSaveResult.Blocked
        }

        saveQuoteToCollection(
            text = draft.text,
            authorName = draft.authorName,
            bookName = draft.bookName,
            tagNames = draft.tagNames,
            collectionId = collectionId,
            inWidgetPlaylist = draft.inWidgetPlaylist,
            inPushPlaylist = draft.inPushPlaylist,
            generalName = generalName,
            page = draft.page,
        )

        analytics.track(
            QuoteAdded(
                inputMethod = draft.inputMethod.toAnalytics(),
                textLength = draft.text.trim().length,
                hasAuthor = !draft.authorName.isNullOrBlank(),
                hasBook = !draft.bookName.isNullOrBlank(),
                tagsCount = draft.tagNames.size,
                widgetEnabled = draft.inWidgetPlaylist,
                pushEnabled = draft.inPushPlaylist,
            ),
        )

        return QuoteSaveResult.Saved(checkAddQuoteAction(widgetPlaced))
    }
}
