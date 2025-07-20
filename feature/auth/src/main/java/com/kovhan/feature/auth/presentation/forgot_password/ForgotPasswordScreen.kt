package com.kovhan.feature.auth.presentation.forgot_password

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
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenIntent
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenState
import com.kovhan.feature.auth.presentation.forgot_password.navigation.ForgotPasswordScreenNavAction

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordScreenState,
    intent: ForgotPasswordScreenIntent,
    navAction: ForgotPasswordScreenNavAction,
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
                text = "Forgot Password Screen",
                modifier = Modifier.align(Alignment.Center),
                color = QuotifyMaterialTheme.colors.primary
            )
        }
    }
} 