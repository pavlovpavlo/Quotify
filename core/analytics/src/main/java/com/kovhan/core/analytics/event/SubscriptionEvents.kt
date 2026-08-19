package com.kovhan.core.analytics.event

import com.kovhan.core.analytics.AnalyticsEvent
import com.kovhan.core.analytics.AnalyticsParam
import com.kovhan.core.analytics.LimitReason
import com.kovhan.core.analytics.PaywallSource
import com.kovhan.core.analytics.PaywallType
import com.kovhan.core.analytics.PurchaseFailure
import com.kovhan.core.analytics.RestoreStatus

class SubscriptionOpened(source: PaywallSource, type: PaywallType) : AnalyticsEvent(
    name = "subscription_opened",
    params = mapOf(
        AnalyticsParam.REASON to source.value,
        AnalyticsParam.TYPE to type.value,
    ),
)

class SubscriptionClosed(source: PaywallSource, type: PaywallType) : AnalyticsEvent(
    name = "subscription_closed",
    params = mapOf(
        AnalyticsParam.REASON to source.value,
        AnalyticsParam.TYPE to type.value,
    ),
)

class SubscriptionPurchaseInitiated(
    source: PaywallSource,
    type: PaywallType,
    productId: String,
) : AnalyticsEvent(
    name = "subscription_purchase_initiated",
    params = mapOf(
        AnalyticsParam.REASON to source.value,
        AnalyticsParam.TYPE to type.value,
        AnalyticsParam.PRODUCT to productId,
    ),
)

class SubscriptionPurchaseCanceled(
    source: PaywallSource,
    type: PaywallType,
    productId: String,
) : AnalyticsEvent(
    name = "subscription_purchase_canceled",
    params = mapOf(
        AnalyticsParam.REASON to source.value,
        AnalyticsParam.TYPE to type.value,
        AnalyticsParam.PRODUCT to productId,
    ),
)

class SubscriptionPurchaseFinished(
    source: PaywallSource,
    type: PaywallType,
    productId: String,
) : AnalyticsEvent(
    name = "subscription_purchase_finished",
    params = mapOf(
        AnalyticsParam.REASON to source.value,
        AnalyticsParam.TYPE to type.value,
        AnalyticsParam.PRODUCT to productId,
    ),
)

class SubscriptionPurchaseFailed(
    source: PaywallSource,
    type: PaywallType,
    productId: String,
    failure: PurchaseFailure,
) : AnalyticsEvent(
    name = "subscription_purchase_failed",
    params = mapOf(
        AnalyticsParam.REASON to source.value,
        AnalyticsParam.TYPE to type.value,
        AnalyticsParam.PRODUCT to productId,
        AnalyticsParam.RESULT to failure.value,
    ),
)

class LimitReached(reason: LimitReason) : AnalyticsEvent(
    name = "limit_reached",
    params = mapOf(AnalyticsParam.REASON to reason.value),
)

class SubscriptionRestored(status: RestoreStatus, productId: String) : AnalyticsEvent(
    name = "subscription_restored",
    params = mapOf(
        AnalyticsParam.STATUS to status.value,
        AnalyticsParam.PRODUCT to productId,
    ),
)
