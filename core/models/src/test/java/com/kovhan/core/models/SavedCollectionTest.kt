package com.kovhan.core.models

import com.kovhan.core.models.collections.SavedCollection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("SavedCollection")
class SavedCollectionTest {

    @Test
    @DisplayName("defaults the icon id and color when they are omitted")
    fun appliesDefaults() {
        val collection = SavedCollection(id = "c1", name = "Favorites")

        assertEquals("default", collection.iconId)
        assertEquals("terra", collection.iconColor)
    }

    @Test
    @DisplayName("keeps the icon id and color when they are provided")
    fun keepsExplicitValues() {
        val collection = SavedCollection(
            id = "c1",
            name = "Favorites",
            iconId = "heart",
            iconColor = "FF0000",
        )

        assertEquals("heart", collection.iconId)
        assertEquals("FF0000", collection.iconColor)
    }

    @Test
    @DisplayName("exposes the documented default constants")
    fun exposesConstants() {
        assertEquals("default", SavedCollection.DEFAULT_ICON_ID)
        assertEquals("terra", SavedCollection.DEFAULT_ICON_COLOR)
    }
}
