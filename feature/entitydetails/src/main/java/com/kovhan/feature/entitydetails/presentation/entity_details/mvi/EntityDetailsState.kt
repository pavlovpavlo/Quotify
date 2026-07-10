package com.kovhan.feature.entitydetails.presentation.entity_details.mvi

import com.kovhan.core.models.quote.EnrichedQuote
import com.kovhan.core.models.collections.SavedCollection
import com.kovhan.core.navigation.EntityType
import com.kovhan.core.ui.UiState

data class EntityDetailsState(
    val isLoading: Boolean = false,
    val type: EntityType = EntityType.COLLECTION,
    val title: String = "",
    val quotes: List<EnrichedQuote> = emptyList(),
    val summary: EntityDetailsSummary = EntityDetailsSummary(),
    val menu: EntityDetailsMenu = EntityDetailsMenu(),
) : UiState {
    val displayTitle: String
        get() = if (type == EntityType.TAG && title.isNotBlank()) "#$title" else title
}

data class EntityDetailsSummary(
    val iconId: String = SavedCollection.DEFAULT_ICON_ID,
    val tone: String = SavedCollection.DEFAULT_ICON_COLOR,
    val quoteCount: Int = 0,
)

data class EntityDetailsMenu(
    val canRename: Boolean = false,
    val canEditStyle: Boolean = false,
    val canDelete: Boolean = false,
) {
    val isVisible: Boolean
        get() = canRename || canEditStyle || canDelete
}
