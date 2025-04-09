package com.kovhan.feature.main.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.feature.main.presentation.home.mvi.HomeScreenIntent
import com.kovhan.feature.main.presentation.home.mvi.HomeScreenState
import com.kovhan.feature.main.presentation.home.navigation.HomeScreenNavAction

@Composable
fun HomeScreen(
    state: HomeScreenState,
    intent: HomeScreenIntent,
    navAction: HomeScreenNavAction,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = QuotifyMaterialTheme.colors.primary
            )
        } else {
            Text(
                text = "Home Screen",
                modifier = Modifier.align(Alignment.Center),
                color = QuotifyMaterialTheme.colors.primary
            )
        }
    }
} 