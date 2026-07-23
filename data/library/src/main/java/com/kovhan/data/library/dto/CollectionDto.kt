package com.kovhan.data.library.dto

import com.kovhan.core.models.collections.SavedCollection

data class CollectionDto(
    val id: String = "",
    val name: String = "",
    val iconId: String = SavedCollection.DEFAULT_ICON_ID,
    val iconColor: String = SavedCollection.DEFAULT_ICON_COLOR,
)
