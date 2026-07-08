package com.kovhan.feature.addquote.presentation.common

import com.kovhan.core.navigation.AiLimitDialogReason
import com.kovhan.domain.ai.AiDenialReason

fun AiDenialReason.toNav(): AiLimitDialogReason = when (this) {
    AiDenialReason.NOT_REGISTERED -> AiLimitDialogReason.NOT_REGISTERED
    AiDenialReason.FREE_LIMIT_REACHED -> AiLimitDialogReason.FREE_LIMIT_REACHED
    AiDenialReason.DAILY_LIMIT_REACHED -> AiLimitDialogReason.DAILY_LIMIT_REACHED
    AiDenialReason.MONTHLY_LIMIT_REACHED -> AiLimitDialogReason.MONTHLY_LIMIT_REACHED
}

fun AiLimitDialogReason.toDomain(): AiDenialReason = when (this) {
    AiLimitDialogReason.NOT_REGISTERED -> AiDenialReason.NOT_REGISTERED
    AiLimitDialogReason.FREE_LIMIT_REACHED -> AiDenialReason.FREE_LIMIT_REACHED
    AiLimitDialogReason.DAILY_LIMIT_REACHED -> AiDenialReason.DAILY_LIMIT_REACHED
    AiLimitDialogReason.MONTHLY_LIMIT_REACHED -> AiDenialReason.MONTHLY_LIMIT_REACHED
}
