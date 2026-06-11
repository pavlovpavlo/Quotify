package com.kovhan.domain.auth

import android.content.Context

sealed interface GoogleSignInOutcome {
    data class Success(val idToken: String) : GoogleSignInOutcome
    data class Failure(val error: AuthError) : GoogleSignInOutcome
}

interface GoogleSignInClient {
    suspend fun signIn(context: Context): GoogleSignInOutcome
}
