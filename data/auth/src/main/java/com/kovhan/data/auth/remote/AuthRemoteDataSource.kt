package com.kovhan.data.auth.remote

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.kovhan.core.models.AuthUser
import com.kovhan.data.auth.util.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth,
) {

    suspend fun signIn(email: String, password: String): AuthUser =
        auth.signInWithEmailAndPassword(email, password).await().requireUser().toDomain()

    suspend fun signUp(email: String, password: String, displayName: String?): AuthUser {
        val user = auth.createUserWithEmailAndPassword(email, password).await().requireUser()
        if (!displayName.isNullOrBlank()) {
            user.updateProfile(displayNameRequest(displayName)).await()
            user.reload().await()
        }
        return (auth.currentUser ?: user).toDomain()
    }

    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun signInWithGoogle(idToken: String): AuthUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val user = auth.signInWithCredential(credential).await().requireUser()
        return user.ensurePhotoFromProvider().toDomain()
    }

    suspend fun updateDisplayName(displayName: String): AuthUser {
        val user = requireSignedIn()
        user.updateProfile(displayNameRequest(displayName)).await()
        user.reload().await()
        return (auth.currentUser ?: user).toDomain()
    }

    suspend fun updateEmail(newEmail: String) {
        requireSignedIn().verifyBeforeUpdateEmail(newEmail).await()
    }

    suspend fun reloadUser(): AuthUser {
        val user = requireSignedIn()
        user.reload().await()
        return (auth.currentUser ?: user).toDomain()
    }

    suspend fun changePassword(currentPassword: String, newPassword: String) {
        val user = requireSignedIn()
        val email = user.email ?: throw IllegalStateException("User has no email credential")
        val credential = EmailAuthProvider.getCredential(email, currentPassword)
        user.reauthenticate(credential).await()
        user.updatePassword(newPassword).await()
    }

    suspend fun deleteAccount() {
        requireSignedIn().delete().await()
    }

    fun currentUser(): AuthUser? = auth.currentUser?.toDomain()

    fun currentUserId(): String? = auth.currentUser?.uid

    fun currentPhotoUrl(): String? = auth.currentUser?.photoUrl?.toString()

    fun signOut() = auth.signOut()

    private fun requireSignedIn(): FirebaseUser =
        auth.currentUser ?: throw IllegalStateException("No signed-in user")

    private suspend fun FirebaseUser.ensurePhotoFromProvider(): FirebaseUser {
        if (photoUrl != null) return this
        val providerPhoto = providerData
            .firstOrNull { it.providerId == GoogleAuthProvider.PROVIDER_ID && it.photoUrl != null }
            ?.photoUrl
            ?: return this
        updateProfile(UserProfileChangeRequest.Builder().setPhotoUri(providerPhoto).build()).await()
        reload().await()
        return auth.currentUser ?: this
    }

    private fun displayNameRequest(displayName: String): UserProfileChangeRequest =
        UserProfileChangeRequest.Builder().setDisplayName(displayName).build()

    private fun com.google.firebase.auth.AuthResult.requireUser(): FirebaseUser =
        user ?: throw IllegalStateException("Firebase returned a null user")

    private fun FirebaseUser.toDomain(): AuthUser =
        AuthUser(
            uid = uid,
            email = email,
            displayName = displayName,
            isEmailVerified = isEmailVerified,
            photoUrl = photoUrl?.toString(),
            isGoogleAccount = providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID },
        )
}
