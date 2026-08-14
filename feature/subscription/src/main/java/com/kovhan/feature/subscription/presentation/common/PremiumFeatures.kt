package com.kovhan.feature.subscription.presentation.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kovhan.design.systems.R

/** Перелік переваг Premium — спільний для пейволу й екрана поточної підписки. */
internal data class PremiumFeature(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val textRes: Int,
)

internal val premiumFeatures = listOf(
    PremiumFeature(R.drawable.ic_offline, R.string.paywall_feature_offline),
    PremiumFeature(R.drawable.ic_quotes, R.string.paywall_feature_quotes),
    PremiumFeature(R.drawable.ic_pencil, R.string.paywall_feature_style),
    PremiumFeature(R.drawable.ic_sparkles, R.string.paywall_feature_ai),
)
