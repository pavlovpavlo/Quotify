package com.kovhan.domain.auth.use_case

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("AuthValidator")
class AuthValidatorTest {

    private val validator = AuthValidator()

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("isEmailValid")
    inner class EmailValidation {

        @ParameterizedTest(name = "isEmailValid(\"{0}\") == {1}")
        @MethodSource("emailCases")
        fun validates(email: String, expected: Boolean) {
            assertEquals(expected, validator.isEmailValid(email))
        }

        fun emailCases(): Stream<Arguments> = Stream.of(
            Arguments.of("user@example.com", true),
            Arguments.of("a@b.co", true),
            Arguments.of("  user@example.com  ", true),
            Arguments.of("plainaddress", false),
            Arguments.of("user@", false),
            Arguments.of("user@domain", false),
            Arguments.of("@example.com", false),
            Arguments.of("", false),
            Arguments.of("   ", false),
        )
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("isNameValid")
    inner class NameValidation {

        @ParameterizedTest(name = "isNameValid(\"{0}\") == {1}")
        @MethodSource("nameCases")
        fun validates(name: String, expected: Boolean) {
            assertEquals(expected, validator.isNameValid(name))
        }

        fun nameCases(): Stream<Arguments> = Stream.of(
            Arguments.of("A", true),
            Arguments.of("  Pavlo  ", true),
            Arguments.of("", false),
            Arguments.of("   ", false),
        )
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("isPasswordValid")
    inner class PasswordValidation {

        @ParameterizedTest(name = "isPasswordValid(\"{0}\") == {1}")
        @MethodSource("passwordCases")
        fun validates(password: String, expected: Boolean) {
            assertEquals(expected, validator.isPasswordValid(password))
        }

        fun passwordCases(): Stream<Arguments> = Stream.of(
            Arguments.of("12345678", true),
            Arguments.of("123456789", true),
            Arguments.of("  12345678  ", true),
            Arguments.of("       8", false),
            Arguments.of("1234567", false),
            Arguments.of("  7  ", false),
            Arguments.of("        ", false),
        )
    }
}
