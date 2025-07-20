package com.kovhan.feature.auth.presentation.login

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
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenIntent
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenState
import com.kovhan.feature.auth.presentation.login.navigation.LoginScreenNavAction

@Composable
fun LoginScreen(
    state: LoginScreenState,
    intent: LoginScreenIntent,
    navAction: LoginScreenNavAction,
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
                text = "Login Screen",
                modifier = Modifier.align(Alignment.Center),
                color = QuotifyMaterialTheme.colors.primary
            )
        }
    }
} 