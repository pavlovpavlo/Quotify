package com.kovhan.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.AuthResult
import com.kovhan.domain.auth.AuthUser
import com.kovhan.domain.auth.UserRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class FirebaseAuthRepository
    @Inject
    constructor(
        private val auth: FirebaseAuth,
        private val userRepository: UserRepository,
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
                auth.signInWithCredential(credential).await().requireUser()
            }.cacheIfSuccess()

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

        override fun currentUser(): AuthUser? = auth.currentUser?.toDomain()

        override fun signOut() = auth.signOut()

        private suspend fun AuthResult<AuthUser>.cacheIfSuccess(): AuthResult<AuthUser> {
            if (this is AuthResult.Success) userRepository.cacheUser(data)
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
            )
    }

private suspend fun <T> Task<T>.await(): T =
    suspendCancellableCoroutine<T> { cont ->
        addOnCompleteListener { task ->
            val exception = task.exception
            when {
                exception != null -> cont.resumeWith(Result.failure(exception))
                task.isCanceled -> cont.cancel()
                else ->
                    @Suppress("UNCHECKED_CAST")
                    cont.resume(task.result as T)
            }
        }
    }
