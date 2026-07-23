package com.kovhan.domain.library.use_case.quote

import com.kovhan.core.models.collections.SavedCollection
import javax.inject.Inject

class SaveQuoteToCollectionUseCase @Inject constructor(
    private val upsertQuote: UpsertQuoteUseCase,
) {
    suspend operator fun invoke(
        text: String,
        authorName: String?,
        bookName: String?,
        tagNames: List<String>,
        collectionId: String,
        inWidgetPlaylist: Boolean,
        inPushPlaylist: Boolean,
        generalName: String,
    ) {
        upsertQuote(
            text = text,
            authorName = authorName,
            bookName = bookName,
            tagNames = tagNames,
            collectionId = collectionId.takeUnless { it == SavedCollection.GENERAL_ID },
            inWidgetPlaylist = inWidgetPlaylist,
            inPushPlaylist = inPushPlaylist,
            generalName = generalName,
        )
    }
}
