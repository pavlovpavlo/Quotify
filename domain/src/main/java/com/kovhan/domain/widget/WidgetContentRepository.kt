package com.kovhan.domain.widget

import kotlinx.coroutines.flow.Flow

/**
 * Persisted, offline-available content that feeds the home-screen widget:
 * - the resolved quote-id **snapshot** (what currently belongs on the widget),
 * - the **seen** set powering no-repeat-until-exhausted rotation,
 * - the single **current** quote id the widget is showing right now.
 *
 * Lives in Room so the widget process can read it without the app running.
 */
interface WidgetContentRepository {
    fun observeSnapshotIds(): Flow<List<String>>

    suspend fun getSnapshotIds(): List<String>

    /** Replaces the snapshot wholesale; prunes seen entries no longer present. */
    suspend fun replaceSnapshot(quoteIds: List<String>)

    suspend fun getSeenIds(): List<String>

    suspend fun markSeen(quoteId: String)

    suspend fun resetSeen()

    fun observeCurrentQuoteId(): Flow<String?>

    suspend fun getCurrentQuoteId(): String?

    suspend fun setCurrentQuoteId(quoteId: String?)
}
