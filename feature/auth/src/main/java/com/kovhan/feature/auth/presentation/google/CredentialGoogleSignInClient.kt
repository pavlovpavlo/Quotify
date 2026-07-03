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
import com.kovhan.domain.auth.model.AuthError
import timber.log.Timber
import javax.inject.Inject

class CredentialGoogleSignInClient @Inject constructor() : GoogleSignInClient {

    override suspend fun signIn(context: Context): GoogleSignInOutcome {
        val webClientId = context.webClientId()
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
            val response = CredentialManager.create(context).getCredential(context, request)
            val token = GoogleIdTokenCredential.createFrom(response.credential.data).idToken
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

    private fun Context.webClientId(): String? {
        val resId = resources.getIdentifier(WEB_CLIENT_ID_RES, "string", packageName)
        return if (resId != 0) getString(resId) else null
    }

    private companion object {
        const val WEB_CLIENT_ID_RES = "default_web_client_id"
    }
}
