package com.kovhan.feature.main.presentation.about.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.kovhan.core.navigation.NavigationCoordinator
import com.kovhan.feature.main.presentation.about.AboutScreen

@Composable
internal fun AboutEntry(
    coordinator: NavigationCoordinator,
    paddingValues: PaddingValues,
) {
    AboutScreen(
        navAction = object : AboutScreenNavAction {
            override fun navigateBack() {
                coordinator.goBack()
            }
        },
        paddingValues = paddingValues,
    )
}
