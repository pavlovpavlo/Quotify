package com.kovhan.data.auth.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.kovhan.data.auth.mapper.toAuthError
import com.kovhan.data.auth.remote.FirestoreUserRepository
import com.kovhan.data.auth.util.await
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.AuthUser
import com.kovhan.domain.auth.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository
    @Inject
    constructor(
        private val auth: FirebaseAuth,
        private val userRepository: UserRepository,
        private val userProfileRepository: FirestoreUserRepository,
    ) : AuthRepository {
        override suspend fun signIn(
            email: String,
            password: String,
        ): AuthResult<AuthUser> =
            runAuth { auth.signInWithEmailAndPassword(email, password).await().requireUser() }
                .cacheIfSuccess()

        override suspend fun signUp(
            email: String,
            password: String,
            displayName: String?,
        ): AuthResult<AuthUser> =
            runAuth {
                val user = auth.createUserWithEmailAndPassword(email, password).await().requireUser()
                if (!displayName.isNullOrBlank()) {
                    val request =
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build()
                    user.updateProfile(request).await()
                    user.reload().await()
                }
                user
            }.cacheIfSuccess()

        override suspend fun sendPasswordReset(email: String): AuthResult<Unit> = runAuthUnit { auth.sendPasswordResetEmail(email).await() }

        override suspend fun signInWithGoogle(idToken: String): AuthResult<AuthUser> =
            runAuth {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val user = auth.signInWithCredential(credential).await().requireUser()
                user.ensurePhotoFromProvider()
            }.cacheIfSuccess()

        private suspend fun FirebaseUser.ensurePhotoFromProvider(): FirebaseUser {
            if (photoUrl != null) return this
            val providerPhoto = providerData
                .firstOrNull { it.providerId == GoogleAuthProvider.PROVIDER_ID && it.photoUrl != null }
                ?.photoUrl
                ?: return this
            val request =
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(providerPhoto)
                    .build()
            updateProfile(request).await()
            reload().await()
            return auth.currentUser ?: this
        }

        override suspend fun updateDisplayName(displayName: String): AuthResult<AuthUser> =
            runAuth {
                val user = auth.currentUser ?: throw IllegalStateException("No signed-in user")
                val request =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()
                user.updateProfile(request).await()
                user.reload().await()
                auth.currentUser ?: user
            }.cacheIfSuccess()

        override suspend fun updateEmail(newEmail: String): AuthResult<Unit> =
            runAuthUnit {
                val user = auth.currentUser ?: throw IllegalStateException("No signed-in user")
                user.verifyBeforeUpdateEmail(newEmail).await()
            }

        override suspend fun reloadUser(): AuthResult<AuthUser> =
            runAuth {
                val user = auth.currentUser ?: throw IllegalStateException("No signed-in user")
                user.reload().await()
                auth.currentUser ?: user
            }.cacheIfSuccess()

        override suspend fun changePassword(
            currentPassword: String,
            newPassword: String,
        ): AuthResult<Unit> =
            runAuthUnit {
                val user = auth.currentUser ?: throw IllegalStateException("No signed-in user")
                val email = user.email ?: throw IllegalStateException("User has no email credential")
                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                user.reauthenticate(credential).await()
                user.updatePassword(newPassword).await()
            }

        override suspend fun deleteAccount(): AuthResult<Unit> =
            runAuthUnit {
                val user = auth.currentUser ?: throw IllegalStateException("No signed-in user")
                user.delete().await()
                userRepository.clear()
            }

        override fun currentUser(): AuthUser? = auth.currentUser?.toDomain()

        override fun signOut() = auth.signOut()

        private suspend fun AuthResult<AuthUser>.cacheIfSuccess(): AuthResult<AuthUser> {
            if (this is AuthResult.Success) {
                val remote = runCatching { userProfileRepository.fetchProfile(data.uid) }.getOrNull()
                userRepository.cacheUser(
                    data.copy(
                        username = remote?.username ?: data.username,
                        photoUrl = remote?.photoUrl ?: data.photoUrl,
                    ),
                )
            }
            return this
        }

        private inline fun runAuth(block: () -> FirebaseUser): AuthResult<AuthUser> =
            try {
                AuthResult.Success(block().toDomain())
            } catch (t: Throwable) {
                AuthResult.Failure(t.toAuthError())
            }

        private inline fun runAuthUnit(block: () -> Unit): AuthResult<Unit> =
            try {
                block()
                AuthResult.Success(Unit)
            } catch (t: Throwable) {
                AuthResult.Failure(t.toAuthError())
            }

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
