package com.kovhan.feature.addquote.presentation.common

import androidx.annotation.StringRes
import com.kovhan.design.systems.R
import com.kovhan.domain.ai.AiDenialReason


data class AiGateContent(
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int,
    val isUpsell: Boolean,
)

fun AiDenialReason.toGateContent(): AiGateContent = when (this) {
    AiDenialReason.NOT_REGISTERED,
    AiDenialReason.FREE_LIMIT_REACHED,
    -> AiGateContent(
        titleRes = R.string.ai_gate_upsell_title,
        messageRes = R.string.ai_gate_upsell_message,
        isUpsell = true,
    )

    AiDenialReason.DAILY_LIMIT_REACHED -> AiGateContent(
        titleRes = R.string.ai_gate_daily_title,
        messageRes = R.string.ai_gate_daily_message,
        isUpsell = false,
    )

    AiDenialReason.MONTHLY_LIMIT_REACHED -> AiGateContent(
        titleRes = R.string.ai_gate_monthly_title,
        messageRes = R.string.ai_gate_monthly_message,
        isUpsell = false,
    )
}
