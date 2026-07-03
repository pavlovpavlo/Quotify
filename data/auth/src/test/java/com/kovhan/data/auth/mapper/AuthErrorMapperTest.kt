package com.kovhan.data.auth.mapper

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kovhan.domain.auth.model.AuthError
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Throwable.toAuthError")
class AuthErrorMapperTest {

    @Nested
    @DisplayName("typed Firebase exceptions")
    inner class TypedExceptions {

        @Test
        @DisplayName("maps network exception to Network")
        fun network() {
            assertEquals(AuthError.Network, mockk<FirebaseNetworkException>().toAuthError())
        }

        @Test
        @DisplayName("maps too-many-requests to TooManyRequests")
        fun tooManyRequests() {
            assertEquals(
                AuthError.TooManyRequests,
                mockk<FirebaseTooManyRequestsException>().toAuthError(),
            )
        }

        @Test
        @DisplayName("maps recent-login-required to RecentLoginRequired")
        fun recentLogin() {
            assertEquals(
                AuthError.RecentLoginRequired,
                mockk<FirebaseAuthRecentLoginRequiredException>().toAuthError(),
            )
        }

        @Test
        @DisplayName("maps weak-password to WeakPassword")
        fun weakPassword() {
            assertEquals(
                AuthError.WeakPassword,
                mockk<FirebaseAuthWeakPasswordException>().toAuthError(),
            )
        }

        @Test
        @DisplayName("maps user-collision to EmailAlreadyInUse")
        fun userCollision() {
            assertEquals(
                AuthError.EmailAlreadyInUse,
                mockk<FirebaseAuthUserCollisionException>().toAuthError(),
            )
        }
    }

    @Nested
    @DisplayName("invalid-user error codes")
    inner class InvalidUser {

        @Test
        @DisplayName("maps ERROR_USER_DISABLED to UserDisabled")
        fun disabled() {
            val ex = mockk<FirebaseAuthInvalidUserException> {
                every { errorCode } returns "ERROR_USER_DISABLED"
            }

            assertEquals(AuthError.UserDisabled, ex.toAuthError())
        }

        @Test
        @DisplayName("maps any other code to UserNotFound")
        fun notFound() {
            val ex = mockk<FirebaseAuthInvalidUserException> {
                every { errorCode } returns "ERROR_USER_NOT_FOUND"
            }

            assertEquals(AuthError.UserNotFound, ex.toAuthError())
        }
    }

    @Nested
    @DisplayName("invalid-credential error codes")
    inner class InvalidCredentials {

        @Test
        @DisplayName("maps ERROR_INVALID_EMAIL to InvalidEmail")
        fun invalidEmail() {
            val ex = mockk<FirebaseAuthInvalidCredentialsException> {
                every { errorCode } returns "ERROR_INVALID_EMAIL"
            }

            assertEquals(AuthError.InvalidEmail, ex.toAuthError())
        }

        @Test
        @DisplayName("maps any other code to InvalidCredentials")
        fun invalidCredentials() {
            val ex = mockk<FirebaseAuthInvalidCredentialsException> {
                every { errorCode } returns "ERROR_INVALID_CREDENTIAL"
            }

            assertEquals(AuthError.InvalidCredentials, ex.toAuthError())
        }
    }

    @Nested
    @DisplayName("message sniffing for collapsed credential errors")
    inner class MessageSniffing {

        @Test
        @DisplayName("detects INVALID_LOGIN_CREDENTIALS")
        fun invalidLoginCredentials() {
            assertEquals(
                AuthError.InvalidCredentials,
                RuntimeException("Failure: INVALID_LOGIN_CREDENTIALS reported").toAuthError(),
            )
        }

        @Test
        @DisplayName("detects WRONG_PASSWORD")
        fun wrongPassword() {
            assertEquals(
                AuthError.InvalidCredentials,
                RuntimeException("the WRONG_PASSWORD was used").toAuthError(),
            )
        }

        @Test
        @DisplayName("detects EMAIL_ALREADY_IN_USE")
        fun emailAlreadyInUse() {
            assertEquals(
                AuthError.EmailAlreadyInUse,
                RuntimeException("EMAIL_ALREADY_IN_USE").toAuthError(),
            )
        }

        @Test
        @DisplayName("detects USER_NOT_FOUND")
        fun userNotFound() {
            assertEquals(
                AuthError.UserNotFound,
                RuntimeException("USER_NOT_FOUND").toAuthError(),
            )
        }

        @Test
        @DisplayName("detects INVALID_EMAIL")
        fun invalidEmail() {
            assertEquals(
                AuthError.InvalidEmail,
                RuntimeException("INVALID_EMAIL").toAuthError(),
            )
        }

        @Test
        @DisplayName("detects NETWORK")
        fun network() {
            assertEquals(
                AuthError.Network,
                RuntimeException("a NETWORK error occurred").toAuthError(),
            )
        }
    }

    @Nested
    @DisplayName("unknown errors")
    inner class Unknown {

        @Test
        @DisplayName("falls back to Unknown with the original message")
        fun unknownWithMessage() {
            assertEquals(
                AuthError.Unknown("something unexpected"),
                RuntimeException("something unexpected").toAuthError(),
            )
        }

        @Test
        @DisplayName("falls back to Unknown with null message")
        fun unknownWithoutMessage() {
            assertEquals(AuthError.Unknown(null), RuntimeException().toAuthError())
        }
    }
}
