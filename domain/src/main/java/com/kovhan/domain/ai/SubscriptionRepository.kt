package com.kovhan.domain.ai

import com.kovhan.core.models.billing.SubscriptionStatus

/**
 * Paid subscription status, which also raises the AI usage limits. Written
 * exclusively by the backend (Cloudflare Worker) into `users/{uid}.premium` —
 * the app only ever reads it.
 */
interface SubscriptionRepository {

    /** Online reads Firestore, offline falls back to the cached status. */
    suspend fun isSubscribed(): Boolean

    /** Full status for the subscription management UI. */
    suspend fun getStatus(): SubscriptionStatus

    /** Forces a Firestore read and refreshes the local cache. */
    suspend fun refresh(): SubscriptionStatus
}
