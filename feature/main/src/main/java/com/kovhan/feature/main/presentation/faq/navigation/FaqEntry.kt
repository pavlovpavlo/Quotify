package com.kovhan.feature.main.presentation.faq.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.faq.FaqScreen

@Composable
internal fun FaqEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    FaqScreen(
        navAction = object : FaqScreenNavAction {
            override fun navigateBack() {
                coordinator.goBack()
            }
        },
        paddingValues = paddingValues,
    )
}
