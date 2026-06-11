package com.kovhan.feature.auth.presentation.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.kovhan.domain.auth.GoogleSignInClient
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@Composable
internal fun rememberGoogleSignInClient(): GoogleSignInClient {
    val context = LocalContext.current
    return remember(context) {
        EntryPointAccessors
            .fromApplication(context.applicationContext, GoogleSignInEntryPoint::class.java)
            .googleSignInClient()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface GoogleSignInEntryPoint {
    fun googleSignInClient(): GoogleSignInClient
}
