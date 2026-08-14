package com.kovhan.domain.premium

enum class OfferTrigger {
    WELCOME,

    CANCELED,

    TENURE,
}

interface OfferPromptRepository {

    suspend fun firstLaunchAt(): Long

    suspend fun rememberFirstLaunch(timestamp: Long)

    suspend fun isShown(trigger: OfferTrigger): Boolean

    suspend fun markShown(trigger: OfferTrigger)
}
