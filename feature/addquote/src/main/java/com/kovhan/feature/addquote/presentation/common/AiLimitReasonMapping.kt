package com.kovhan.feature.addquote.presentation.common

import com.kovhan.core.navigation.AiLimitDialogReason
import com.kovhan.domain.ai.AiDenialReason

fun AiDenialReason.toNav(): AiLimitDialogReason = when (this) {
    AiDenialReason.NOT_REGISTERED -> AiLimitDialogReason.NOT_REGISTERED
    AiDenialReason.SUBSCRIPTION_REQUIRED -> AiLimitDialogReason.SUBSCRIPTION_REQUIRED
    AiDenialReason.FREE_DAILY_LIMIT_REACHED -> AiLimitDialogReason.FREE_DAILY_LIMIT_REACHED
    AiDenialReason.FREE_MONTHLY_LIMIT_REACHED -> AiLimitDialogReason.FREE_MONTHLY_LIMIT_REACHED
    AiDenialReason.DAILY_LIMIT_REACHED -> AiLimitDialogReason.DAILY_LIMIT_REACHED
    AiDenialReason.MONTHLY_LIMIT_REACHED -> AiLimitDialogReason.MONTHLY_LIMIT_REACHED
}

fun AiLimitDialogReason.toDomain(): AiDenialReason = when (this) {
    AiLimitDialogReason.NOT_REGISTERED -> AiDenialReason.NOT_REGISTERED
    AiLimitDialogReason.SUBSCRIPTION_REQUIRED -> AiDenialReason.SUBSCRIPTION_REQUIRED
    AiLimitDialogReason.FREE_DAILY_LIMIT_REACHED -> AiDenialReason.FREE_DAILY_LIMIT_REACHED
    AiLimitDialogReason.FREE_MONTHLY_LIMIT_REACHED -> AiDenialReason.FREE_MONTHLY_LIMIT_REACHED
    AiLimitDialogReason.DAILY_LIMIT_REACHED -> AiDenialReason.DAILY_LIMIT_REACHED
    AiLimitDialogReason.MONTHLY_LIMIT_REACHED -> AiDenialReason.MONTHLY_LIMIT_REACHED
}
