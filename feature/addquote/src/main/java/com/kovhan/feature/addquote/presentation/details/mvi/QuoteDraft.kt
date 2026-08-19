package com.kovhan.feature.addquote.presentation.details.mvi

import com.kovhan.core.navigation.models.QuoteInputMethod
import kotlinx.serialization.Serializable

/**
 * Everything the user assembled on the details screen, ready to be persisted by
 * the next step (collection picker). Author/book are carried as names — they are
 * resolved to (or created as) entities when the quote is actually saved.
 */
@Serializable
data class QuoteDraft(
    val inputMethod: QuoteInputMethod = QuoteInputMethod.TEXT,
    val text: String,
    val authorName: String?,
    val bookName: String?,
    val tagNames: List<String>,
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
    val page: Int? = null,
)
