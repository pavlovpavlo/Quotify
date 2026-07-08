package com.kovhan.data.library.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kovhan.domain.ai.AiUsage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AI usage counters stored per user at `users/{uid}/ai_usage/{month}`. Each
 * month document holds a running [total] plus a `days` map of per-day counts,
 * both bumped atomically with [FieldValue.increment] so concurrent devices
 * stay consistent.
 */
@Singleton
class AiUsageRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private fun collection() = firestore.userSubcollection(auth, SUBCOLLECTION_AI_USAGE)

    suspend fun getUsage(monthKey: String, dayKey: String): AiUsage {
        val snapshot = collection()?.document(monthKey)?.get()?.await()
            ?: return AiUsage(todayCount = 0, monthCount = 0)
        val total = snapshot.getLong(FIELD_TOTAL)?.toInt() ?: 0
        val days = snapshot.get(FIELD_DAYS) as? Map<*, *>
        val today = (days?.get(dayKey) as? Number)?.toInt() ?: 0
        return AiUsage(todayCount = today, monthCount = total)
    }

    suspend fun recordRequest(monthKey: String, dayKey: String) {
        val document = collection()?.document(monthKey) ?: return
        document.set(
            mapOf(
                FIELD_MONTH to monthKey,
                FIELD_TOTAL to FieldValue.increment(1),
                FIELD_DAYS to mapOf(dayKey to FieldValue.increment(1)),
            ),
            SetOptions.merge(),
        ).await()
    }

    private companion object {
        const val FIELD_MONTH = "month"
        const val FIELD_TOTAL = "total"
        const val FIELD_DAYS = "days"
    }
}
