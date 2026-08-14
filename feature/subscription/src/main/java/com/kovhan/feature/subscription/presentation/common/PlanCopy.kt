package com.kovhan.feature.subscription.presentation.common

import androidx.annotation.StringRes
import com.kovhan.design.systems.R
import com.kovhan.domain.billing.BillingProducts


internal data class PlanCopy(
    @param:StringRes val shortNameRes: Int?,
    @param:StringRes val fullNameRes: Int?,
    @param:StringRes val descriptionRes: Int?,
    @param:StringRes val periodRes: Int?,
    @param:StringRes val metaRes: Int?,
    val isPopular: Boolean,
)

internal fun planCopy(basePlanId: String): PlanCopy = when (basePlanId) {
    BillingProducts.BASE_PLAN_YEARLY -> PlanCopy(
        shortNameRes = R.string.paywall_plan_yearly,
        fullNameRes = R.string.paywall_plan_yearly_name,
        descriptionRes = R.string.paywall_plan_yearly_desc,
        periodRes = R.string.paywall_period_year,
        metaRes = R.string.paywall_plan_meta_yearly,
        isPopular = true,
    )

    BillingProducts.BASE_PLAN_MONTHLY -> PlanCopy(
        shortNameRes = R.string.paywall_plan_monthly,
        fullNameRes = R.string.paywall_plan_monthly_name,
        descriptionRes = R.string.paywall_plan_monthly_desc,
        periodRes = R.string.paywall_period_month,
        metaRes = R.string.paywall_plan_meta_monthly,
        isPopular = false,
    )

    BillingProducts.BASE_PLAN_WEEKLY -> PlanCopy(
        shortNameRes = R.string.paywall_plan_weekly,
        fullNameRes = R.string.paywall_plan_weekly_name,
        descriptionRes = R.string.paywall_plan_weekly_desc,
        periodRes = R.string.paywall_period_week,
        metaRes = R.string.paywall_plan_meta_weekly,
        isPopular = false,
    )

    else -> PlanCopy(null, null, null, null, null, isPopular = false)
}

internal val planDisplayOrder = listOf(
    BillingProducts.BASE_PLAN_YEARLY,
    BillingProducts.BASE_PLAN_MONTHLY,
    BillingProducts.BASE_PLAN_WEEKLY,
)
