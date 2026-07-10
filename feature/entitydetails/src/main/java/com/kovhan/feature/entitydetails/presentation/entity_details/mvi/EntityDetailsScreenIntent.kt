package com.kovhan.feature.entitydetails.presentation.entity_details.mvi

import com.kovhan.core.models.EnrichedQuote
import com.kovhan.feature.entitydetails.presentation.entity_details.model.EntityQuoteDraft

/** Intents raised by the entity-details screen. */
interface EntityDetailsScreenIntent {
    fun onRenameRequested()
    fun onRenameConfirmed(name: String)
    fun onDeleteRequested()
    fun onDeleteConfirmed()
    fun onEditStyleRequested()
    fun onCollectionStyleConfirmed(iconId: String, tone: String)

    fun onEditQuoteRequested(quote: EnrichedQuote)
    fun onEditQuoteSaved(draft: EntityQuoteDraft)

    fun onMoveQuoteRequested(quoteId: String)
    fun onMoveQuoteConfirmed(quoteId: String, targetCollectionId: String)
    fun onRemoveQuoteRequested(quoteId: String)
    fun onRemoveQuoteConfirmed(quoteId: String)
}
