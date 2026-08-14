package com.kovhan.feature.addquote.presentation.common

import androidx.annotation.StringRes
import com.kovhan.design.systems.R
import com.kovhan.domain.ai.AiDenialReason

/**
 * [isUpsell] керує кнопкою в модалці: безкоштовному плану пропонуємо купівлю,
 * передплатнику, який вичерпав квоту, — купувати вже нічого.
 */
data class AiGateContent(
    @param:StringRes val titleRes: Int,
    @param:StringRes val messageRes: Int,
    val isUpsell: Boolean,
)

fun AiDenialReason.toGateContent(): AiGateContent = when (this) {
    AiDenialReason.NOT_REGISTERED,
    AiDenialReason.SUBSCRIPTION_REQUIRED,
    -> AiGateContent(
        titleRes = R.string.ai_gate_upsell_title,
        messageRes = R.string.ai_gate_upsell_message,
        isUpsell = true,
    )

    AiDenialReason.FREE_DAILY_LIMIT_REACHED -> AiGateContent(
        titleRes = R.string.ai_gate_free_daily_title,
        messageRes = R.string.ai_gate_free_daily_message,
        isUpsell = true,
    )

    AiDenialReason.FREE_MONTHLY_LIMIT_REACHED -> AiGateContent(
        titleRes = R.string.ai_gate_free_monthly_title,
        messageRes = R.string.ai_gate_free_monthly_message,
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
