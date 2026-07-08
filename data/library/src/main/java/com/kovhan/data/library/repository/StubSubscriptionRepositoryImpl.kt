package com.kovhan.data.library.repository

import com.kovhan.domain.ai.SubscriptionRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Placeholder subscription source until billing is wired — nobody is subscribed,
 * so everyone gets the free AI quota.
 */
@Singleton
class StubSubscriptionRepositoryImpl @Inject constructor() : SubscriptionRepository {
    override suspend fun isSubscribed(): Boolean = false
}
