package com.kovhan.data.library.local.widget

import androidx.room.Entity
import androidx.room.PrimaryKey

/** One quote id in the resolved widget snapshot; [position] preserves order. */
@Entity(tableName = "widget_quotes")
data class WidgetQuoteEntity(
    @PrimaryKey val quoteId: String,
    val position: Int,
)

/** A quote already shown in the current rotation cycle. */
@Entity(tableName = "widget_seen")
data class WidgetSeenEntity(
    @PrimaryKey val quoteId: String,
    val shownAt: Long,
)

/** Single-row table holding the quote the widget currently displays. */
@Entity(tableName = "widget_state")
data class WidgetStateEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val currentQuoteId: String?,
    val lastRotatedAt: Long,
) {
    companion object {
        const val SINGLE_ROW_ID = 0
    }
}
