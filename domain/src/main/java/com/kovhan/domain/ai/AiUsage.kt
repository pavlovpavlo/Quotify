package com.kovhan.domain.ai

/** How many AI requests the current user has spent today and this month. */
data class AiUsage(
    val todayCount: Int,
    val monthCount: Int,
)
