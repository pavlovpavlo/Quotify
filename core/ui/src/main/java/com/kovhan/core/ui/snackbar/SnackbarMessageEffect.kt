package com.kovhan.core.ui.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow

@Composable
fun SnackbarMessageEffect(
    snackbar: Flow<SnackbarMessage>,
    hostState: SnackbarHostState,
) {
    val context = LocalContext.current
    LaunchedEffect(snackbar, hostState) {
        snackbar.collect { message ->
            hostState.currentSnackbarData?.dismiss()
            hostState.showSnackbar(
                QuotifySnackbarVisuals(
                    message = message.resolve(context),
                    type = message.type,
                ),
            )
        }
    }
}
