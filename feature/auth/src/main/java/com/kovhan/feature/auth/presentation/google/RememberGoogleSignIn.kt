package com.kovhan.feature.auth.presentation.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.kovhan.domain.auth.AuthError
import kotlinx.coroutines.launch

@Composable
internal fun rememberGoogleSignIn(
    onStart: () -> Unit,
    onIdToken: (String) -> Unit,
    onError: (AuthError) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val client = remember(context) { GoogleSignInClient(context) }

    return {
        onStart()
        scope.launch {
            when (val outcome = client.signIn()) {
                is GoogleSignInOutcome.Success -> onIdToken(outcome.idToken)
                is GoogleSignInOutcome.Failure -> onError(outcome.error)
            }
        }
    }
}
