package com.kovhan.feature.auth.presentation.google

import android.content.Context
import com.kovhan.domain.auth.model.AuthError

sealed interface GoogleSignInOutcome {
    data class Success(val idToken: String) : GoogleSignInOutcome
    data class Failure(val error: AuthError) : GoogleSignInOutcome
}

interface GoogleSignInClient {
    suspend fun signIn(context: Context): GoogleSignInOutcome
}
