package com.kovhan.feature.subscription.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import com.kovhan.core.navigation.BottomSheetEntryBuilder
import com.kovhan.core.navigation.BottomSheetKey
import com.kovhan.core.navigation.ManageSubscriptionSheetKey
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.subscription.presentation.manage.navigation.ManageSubscriptionSheetEntry
import javax.inject.Inject

class SubscriptionBottomSheetEntryBuilder @Inject constructor() : BottomSheetEntryBuilder {

    override fun build(
        scope: EntryProviderScope<BottomSheetKey>,
        coordinator: NavigationCoordinator,
        paddingValues: PaddingValues,
    ) {
        scope.entry<ManageSubscriptionSheetKey> {
            ManageSubscriptionSheetEntry(coordinator = coordinator)
        }
    }
}
