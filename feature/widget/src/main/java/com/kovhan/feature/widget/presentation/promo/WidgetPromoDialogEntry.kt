package com.kovhan.feature.widget.presentation.promo

import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.core.navigation.WidgetPromoKey
import com.kovhan.core.navigation.WidgetSettingsKey

@Composable
fun WidgetPromoDialogEntry(
    key: WidgetPromoKey,
    coordinator: NavigationCoordinator,
) {
    WidgetPromoDialog(
        onConfirm = {
            coordinator.dismissDialog()
            coordinator.navigate(WidgetSettingsKey)
        },
        onDismiss = coordinator::dismissDialog,
    )
}
