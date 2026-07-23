package com.kovhan.domain.ai


sealed interface AiAccess {
    data object Allowed : AiAccess
    data class Denied(val reason: AiDenialReason) : AiAccess
}

enum class AiDenialReason {
    NOT_REGISTERED,
    FREE_LIMIT_REACHED,
    DAILY_LIMIT_REACHED,
    MONTHLY_LIMIT_REACHED,
}
