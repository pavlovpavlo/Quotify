package com.kovhan.feature.subscription.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.navigation.EntryBuilder
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.OfferKey
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.SubscriptionKey
import com.kovhan.feature.subscription.presentation.current.navigation.CurrentSubscriptionEntry
import com.kovhan.feature.subscription.presentation.offer.navigation.OfferEntry
import com.kovhan.feature.subscription.presentation.paywall.navigation.PaywallEntry
import javax.inject.Inject

class SubscriptionEntryBuilder @Inject constructor() : EntryBuilder {

    override fun build(
        scope: EntryProviderScope<NavKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<PaywallKey> { key ->
            PaywallEntry(
                origin = key.origin,
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }

        scope.entry<SubscriptionKey> {
            CurrentSubscriptionEntry(coordinator = coordinator, paddingValues = paddingValues)
        }

        scope.entry<OfferKey> { key ->
            OfferEntry(
                origin = key.origin,
                coordinator = coordinator,
                paddingValues = paddingValues,
            )
        }
    }
}
