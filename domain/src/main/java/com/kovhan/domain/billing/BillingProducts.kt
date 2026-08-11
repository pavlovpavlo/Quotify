package com.kovhan.domain.billing

/**
 * Ідентифікатори з Play Console. Product ID незмінний після створення товару,
 * base plan обирається через offerToken із [com.kovhan.core.models.billing.PremiumProduct].
 */
object BillingProducts {
    const val PREMIUM = "premium"

    const val BASE_PLAN_WEEKLY = "weekly"
    const val BASE_PLAN_MONTHLY = "monthly"
    const val BASE_PLAN_YEARLY = "yearly"
}
