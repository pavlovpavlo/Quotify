package com.kovhan.data.auth.repository

import com.kovhan.core.models.AuthUser
import com.kovhan.core.models.Outcome
import com.kovhan.data.auth.remote.AuthRemoteDataSource
import com.kovhan.domain.auth.UserRepository
import com.kovhan.domain.auth.model.AuthError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("FirebaseAuthRepository")
class FirebaseAuthRepositoryTest {

    private lateinit var remote: AuthRemoteDataSource
    private lateinit var userRepository: UserRepository
    private lateinit var sessionWriter: UserSessionWriter
    private lateinit var repository: FirebaseAuthRepository

    private val user = AuthUser(
        uid = "uid-1",
        email = "user@example.com",
        displayName = "User",
        isEmailVerified = true,
    )

    @BeforeEach
    fun setUp() {
        remote = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        sessionWriter = mockk(relaxed = true)
        repository = FirebaseAuthRepository(remote, userRepository, sessionWriter)
    }

    @Nested
    @DisplayName("signIn")
    inner class SignIn {

        @Test
        @DisplayName("caches the session and returns Success on a valid login")
        fun success() = runTest {
            coEvery { remote.signIn("user@example.com", "secret12") } returns user

            val result = repository.signIn("user@example.com", "secret12")

            assertEquals(Outcome.Success(user), result)
            coVerify { sessionWriter.persist(user) }
        }

        @Test
        @DisplayName("maps the error and does not cache when the login fails")
        fun failure() = runTest {
            coEvery { remote.signIn(any(), any()) } throws RuntimeException("boom")

            val result = repository.signIn("user@example.com", "secret12")

            assertEquals(Outcome.Failure(AuthError.Unknown("boom")), result)
            coVerify(exactly = 0) { sessionWriter.persist(any()) }
        }
    }

    @Nested
    @DisplayName("deleteAccount")
    inner class DeleteAccount {

        @Test
        @DisplayName("deletes the remote account and clears the cached user")
        fun clearsUser() = runTest {
            val result = repository.deleteAccount()

            assertEquals(Outcome.Success(Unit), result)
            coVerify { remote.deleteAccount() }
            coVerify { userRepository.clear() }
        }
    }
}
