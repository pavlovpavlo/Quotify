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
        const val FAVOURITES_ICON_ID = "heart"
    }
}

/**
 * Favourites is not user-styleable, so it always carries the heart icon —
 * including collections created before the icon was pinned, and any copy that
 * comes back from Firestore.
 */
fun SavedCollection.withPinnedIcon(): SavedCollection =
    if (id == SavedCollection.FAVOURITES_ID && iconId != SavedCollection.FAVOURITES_ICON_ID) {
        copy(iconId = SavedCollection.FAVOURITES_ICON_ID)
    } else {
        this
    }
