package com.kovhan.feature.subscription.presentation.manage.navigation

import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.subscription.presentation.manage.ManageSubscriptionSheet

@Composable
internal fun ManageSubscriptionSheetEntry(coordinator: NavigationCoordinator) {
    ManageSubscriptionSheet(onDismiss = coordinator::dismissBottomSheet)
}
