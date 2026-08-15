package com.kovhan.domain.premium

enum class OfferTrigger {
    CANCELED,

    TENURE,
}

interface OfferPromptRepository {

    suspend fun freeSinceAt(): Long

    suspend fun rememberFreeSince(timestamp: Long)

    suspend fun isShown(trigger: OfferTrigger): Boolean

    suspend fun markShown(trigger: OfferTrigger)

    suspend fun clearShown(trigger: OfferTrigger)
}
