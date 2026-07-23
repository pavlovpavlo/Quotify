package com.kovhan.domain.ai

/**
 * Whether the current user has an active paid subscription, which raises the AI
 * usage limits. No billing is wired yet — the implementation returns false.
 */
interface SubscriptionRepository {
    suspend fun isSubscribed(): Boolean
}
