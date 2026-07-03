package com.kovhan.data.auth.repository

import com.kovhan.core.models.AuthUser
import com.kovhan.core.models.Outcome
import com.kovhan.data.auth.mapper.toAuthError
import com.kovhan.data.auth.remote.AuthRemoteDataSource
import com.kovhan.domain.auth.AuthRepository
import com.kovhan.domain.auth.UserRepository
import com.kovhan.domain.auth.model.AuthResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthRepository @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val userRepository: UserRepository,
    private val sessionWriter: UserSessionWriter,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): AuthResult<AuthUser> =
        runAuth { remote.signIn(email, password) }.cacheIfSuccess()

    override suspend fun signUp(
        email: String,
        password: String,
        displayName: String?,
    ): AuthResult<AuthUser> =
        runAuth { remote.signUp(email, password, displayName) }.cacheIfSuccess()

    override suspend fun sendPasswordReset(email: String): AuthResult<Unit> =
        runAuthUnit { remote.sendPasswordReset(email) }

    override suspend fun signInWithGoogle(idToken: String): AuthResult<AuthUser> =
        runAuth { remote.signInWithGoogle(idToken) }.cacheIfSuccess()

    override suspend fun updateDisplayName(displayName: String): AuthResult<AuthUser> =
        runAuth { remote.updateDisplayName(displayName) }.cacheIfSuccess()

    override suspend fun updateEmail(newEmail: String): AuthResult<Unit> =
        runAuthUnit { remote.updateEmail(newEmail) }

    override suspend fun reloadUser(): AuthResult<AuthUser> =
        runAuth { remote.reloadUser() }.cacheIfSuccess()

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
    ): AuthResult<Unit> =
        runAuthUnit { remote.changePassword(currentPassword, newPassword) }

    override suspend fun deleteAccount(): AuthResult<Unit> =
        runAuthUnit {
            remote.deleteAccount()
            userRepository.clear()
        }

    override fun currentUser(): AuthUser? = remote.currentUser()

    override fun signOut() = remote.signOut()

    private suspend fun AuthResult<AuthUser>.cacheIfSuccess(): AuthResult<AuthUser> {
        if (this is Outcome.Success) {
            sessionWriter.persist(data)
        }
        return this
    }

    private inline fun runAuth(block: () -> AuthUser): AuthResult<AuthUser> =
        try {
            Outcome.Success(block())
        } catch (t: Throwable) {
            Outcome.Failure(t.toAuthError())
        }

    private inline fun runAuthUnit(block: () -> Unit): AuthResult<Unit> =
        try {
            block()
            Outcome.Success(Unit)
        } catch (t: Throwable) {
            Outcome.Failure(t.toAuthError())
        }
}
