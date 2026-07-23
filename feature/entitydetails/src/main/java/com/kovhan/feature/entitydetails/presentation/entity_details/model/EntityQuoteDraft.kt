package com.kovhan.feature.entitydetails.presentation.entity_details.model

/** UI draft for the quote-edit sheet, shared by collection and entity details. */
data class EntityQuoteDraft(
    val quoteId: String,
    val text: String,
    val authorName: String,
    val bookName: String,
    val tags: List<String>,
    val aiTags: List<String> = emptyList(),
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
)
