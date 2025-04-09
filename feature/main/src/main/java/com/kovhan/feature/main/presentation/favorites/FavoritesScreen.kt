package com.kovhan.feature.main.presentation.favorites

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
import com.kovhan.feature.main.presentation.favorites.mvi.FavoritesScreenIntent
import com.kovhan.feature.main.presentation.favorites.mvi.FavoritesScreenState
import com.kovhan.feature.main.presentation.favorites.navigation.FavoritesScreenNavAction

@Composable
fun FavoritesScreen(
    state: FavoritesScreenState,
    intent: FavoritesScreenIntent,
    navAction: FavoritesScreenNavAction,
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
                text = "Favorites Screen",
                modifier = Modifier.align(Alignment.Center),
                color = QuotifyMaterialTheme.colors.primary
            )
        }
    }
} 