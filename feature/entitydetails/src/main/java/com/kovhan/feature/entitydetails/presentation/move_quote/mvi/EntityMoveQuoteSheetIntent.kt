package com.kovhan.feature.entitydetails.presentation.move_quote.mvi

interface EntityMoveQuoteSheetIntent {
    fun onSelectCollection(collectionId: String)
    fun onCreateCollectionRequested()
    fun onCollectionCreated(collectionId: String)
    fun onMoveRequested(targetCollectionId: String)
}
