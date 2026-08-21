package com.kovhan.core.models.widget

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WidgetSpecialCovers")
class WidgetSpecialCoversTest {

    @Test
    @DisplayName("unlocks one cover per finished survey")
    fun unlocksPerSurvey() {
        assertTrue(WidgetSpecialCovers.unlocked(0).isEmpty())
        assertEquals(listOf("special_1", "special_2", "special_3"), WidgetSpecialCovers.unlocked(3))
    }

    @Test
    @DisplayName("never hands out more covers than exist")
    fun capsAtTheEnd() {
        assertEquals(WidgetSpecialCovers.ALL, WidgetSpecialCovers.unlocked(99))
        assertTrue(WidgetSpecialCovers.unlocked(-1).isEmpty())
    }

    @Test
    @DisplayName("grants the cover matching the survey count")
    fun grantsMatchingCover() {
        assertEquals("special_1", WidgetSpecialCovers.rewardFor(1))
        assertEquals("special_10", WidgetSpecialCovers.rewardFor(10))
    }

    @Test
    @DisplayName("runs out once every cover is given away")
    fun runsOut() {
        assertNull(WidgetSpecialCovers.rewardFor(11))
        assertNull(WidgetSpecialCovers.rewardFor(0))
    }
}
