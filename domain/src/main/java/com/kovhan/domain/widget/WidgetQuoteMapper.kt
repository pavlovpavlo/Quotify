package com.kovhan.domain.widget

import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.models.widget.WidgetQuote

fun EnrichedQuote.toWidgetQuote() = WidgetQuote(
    id = id,
    text = text,
    authorName = author?.name,
    bookName = book?.name,
)
