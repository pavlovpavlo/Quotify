package com.kovhan.feature.subscription.presentation.common

import com.kovhan.core.analytics.PaywallSource
import com.kovhan.core.navigation.models.PaywallOrigin

internal fun PaywallOrigin.toAnalytics(): PaywallSource = when (this) {
    PaywallOrigin.HOME -> PaywallSource.HOME
    PaywallOrigin.LIMIT -> PaywallSource.LIMIT
    PaywallOrigin.BANNER -> PaywallSource.BANNER
    PaywallOrigin.TENURE -> PaywallSource.TENURE
    PaywallOrigin.CANCEL -> PaywallSource.CANCEL
}
