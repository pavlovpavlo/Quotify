package com.kovhan.feature.addquote.presentation.details.component.tageditor.navigation

import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.PaywallKey
import com.kovhan.core.navigation.TagSheetAiLimitDialogKey
import com.kovhan.feature.addquote.presentation.common.AiLimitDialog
import com.kovhan.feature.addquote.presentation.common.toDomain

@Composable
internal fun TagSheetAiLimitDialogEntry(
    key: TagSheetAiLimitDialogKey,
    coordinator: NavigationCoordinator,
) {
    AiLimitDialog(
        reason = key.reason.toDomain(),
        onDismiss = coordinator::dismissDialog,
        onReviewSubscriptions = {
            coordinator.dismissDialog()
            coordinator.navigate(PaywallKey)
        },
    )
}
