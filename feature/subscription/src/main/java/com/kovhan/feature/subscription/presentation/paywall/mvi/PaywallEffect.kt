package com.kovhan.feature.subscription.presentation.paywall.mvi

import com.kovhan.core.ui.UiEffect

/**
 * Екран нікуди не навігує сам: успішна покупка перемикає його у стан
 * підтвердження, а закриття робить сам користувач.
 */
sealed class PaywallEffect : UiEffect
