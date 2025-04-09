package com.kovhan.feature.main.presentation.profile

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
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenIntent
import com.kovhan.feature.main.presentation.profile.mvi.ProfileScreenState
import com.kovhan.feature.main.presentation.profile.navigation.ProfileScreenNavAction

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    intent: ProfileScreenIntent,
    navAction: ProfileScreenNavAction,
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
                text = "Profile Screen",
                modifier = Modifier.align(Alignment.Center),
                color = QuotifyMaterialTheme.colors.primary
            )
        }
    }
} 