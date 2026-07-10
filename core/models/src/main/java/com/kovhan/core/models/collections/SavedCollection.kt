package com.kovhan.core.models.collections

data class SavedCollection(
    val id: String,
    val name: String,
    val iconId: String = DEFAULT_ICON_ID,
    val iconColor: String = DEFAULT_ICON_COLOR,
    val quoteCount: Int? = null,
) {
    companion object {
        const val FAVOURITES_ID = "favourite"
        const val GENERAL_ID = "general"
        const val DEFAULT_ICON_ID = "default"
        const val DEFAULT_ICON_COLOR = "terra"
    }
}
