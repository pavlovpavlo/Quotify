package com.kovhan.domain.premium

/** Коли востаннє показували стартовий екран підписки — щоб не набридати щозапуску. */
interface PaywallPromptRepository {

    suspend fun lastShownAt(): Long

    suspend fun markShown(timestamp: Long)
}
