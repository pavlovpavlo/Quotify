package com.kovhan.domain.ai

sealed interface AiAccess {
    data object Allowed : AiAccess
    data class Denied(val reason: AiDenialReason) : AiAccess
}

/** AI-функції, що списують спільну квоту. */
enum class AiFeature {
    /** Розпізнавання тексту з фото. Безкоштовному плану доступне в межах ліміту. */
    SCAN,

    /** Підказки тегів. Тільки для передплатників. */
    TAGS,
}

/**
 * Причини відмови. Розділені на «безкоштовні» й «преміум» варіанти ліміту,
 * бо перші показують кнопку купівлі, а другі — лише повідомлення.
 */
enum class AiDenialReason {
    NOT_REGISTERED,
    SUBSCRIPTION_REQUIRED,
    FREE_DAILY_LIMIT_REACHED,
    FREE_MONTHLY_LIMIT_REACHED,
    DAILY_LIMIT_REACHED,
    MONTHLY_LIMIT_REACHED,
}
