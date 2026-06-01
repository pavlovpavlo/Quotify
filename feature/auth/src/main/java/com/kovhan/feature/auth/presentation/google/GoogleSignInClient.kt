package com.kovhan.feature.auth.presentation.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.kovhan.domain.auth.AuthError
import timber.log.Timber

sealed interface GoogleSignInOutcome {
    data class Success(val idToken: String) : GoogleSignInOutcome
    data class Failure(val error: AuthError) : GoogleSignInOutcome
}

// Web client id is resolved at runtime so the module compiles before the Google provider
// is enabled in Firebase (which generates default_web_client_id).
class GoogleSignInClient(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(): GoogleSignInOutcome {
        val webClientId = webClientId()
        if (webClientId.isNullOrBlank()) {
            Timber.w("Google Sign-In: default_web_client_id is missing — enable Google provider in Firebase.")
            return GoogleSignInOutcome.Failure(AuthError.GoogleSignInFailed)
        }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val response = credentialManager.getCredential(context, request)
            val credential = response.credential
            val token = GoogleIdTokenCredential.createFrom(credential.data).idToken
            GoogleSignInOutcome.Success(token)
        } catch (e: GetCredentialCancellationException) {
            GoogleSignInOutcome.Failure(AuthError.GoogleSignInCancelled)
        } catch (e: NoCredentialException) {
            Timber.w(e, "Google Sign-In: no credentials available")
            GoogleSignInOutcome.Failure(AuthError.GoogleSignInFailed)
        } catch (e: GoogleIdTokenParsingException) {
            Timber.e(e, "Google Sign-In: failed to parse ID token")
            GoogleSignInOutcome.Failure(AuthError.GoogleSignInFailed)
        } catch (e: GetCredentialException) {
            Timber.e(e, "Google Sign-In: credential request failed")
            GoogleSignInOutcome.Failure(AuthError.GoogleSignInFailed)
        }
    }

    private fun webClientId(): String? {
        val resId = context.resources.getIdentifier(
            WEB_CLIENT_ID_RES,
            "string",
            context.packageName,
        )
        return if (resId != 0) context.getString(resId) else null
    }

    private companion object {
        const val WEB_CLIENT_ID_RES = "default_web_client_id"
    }
}
