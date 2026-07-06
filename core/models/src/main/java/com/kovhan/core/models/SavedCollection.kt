package com.kovhan.core.models

data class SavedCollection(
    val id: String,
    val name: String,
    val iconId: String = DEFAULT_ICON_ID,
    val iconColor: String = DEFAULT_ICON_COLOR,
) {
    companion object {
        const val DEFAULT_ICON_ID = "default"
        const val DEFAULT_ICON_COLOR = "E8E2D5"
    }
}
