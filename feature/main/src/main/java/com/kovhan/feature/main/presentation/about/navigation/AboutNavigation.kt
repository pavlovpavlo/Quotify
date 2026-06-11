package com.kovhan.feature.main.presentation.about.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kovhan.core.ui.navigation.MainGraph
import com.kovhan.feature.main.presentation.about.AboutScreen

internal fun NavGraphBuilder.aboutScreen(
    navAction: AboutScreenNavAction,
    paddingValues: PaddingValues,
) {
    composable<MainGraph.AboutScreen> {
        AboutScreen(
            navAction = navAction,
            paddingValues = paddingValues,
        )
    }
}
