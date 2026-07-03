package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.model.ValidationError
import io.mockk.mockk
import org.junit.jupiter.api.Test

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("ValidateAuthInputUseCase")
class ValidateAuthInputUseCaseTest {

    private lateinit var validator: AuthValidator
    private lateinit var useCase: ValidateAuthInputUseCase

    @BeforeEach
    fun setUp() {
        validator = mockk()
        useCase = ValidateAuthInputUseCase(validator)
    }

    @Nested
    @DisplayName("when name is provided")
    inner class NameValidation {

        @Test
        @DisplayName("returns EmptyName when name is invalid")
        fun returnsEmptyNameWhenNameIsInvalid() {
            every { validator.isNameValid("") } returns false

            val result = useCase(name = "")

            assertEquals(ValidationError.EmptyName, result)
            verify { validator.isNameValid("") }
        }

        @Test
        @DisplayName("continues validation when name is valid")
        fun continuesValidationWhenNameIsValid() {
            every { validator.isNameValid("Pavlo") } returns true
            every { validator.isEmailValid("wrong") } returns false

            val result = useCase(
                name = "Pavlo",
                email = "wrong"
            )

            assertEquals(ValidationError.InvalidEmail, result)
            verify { validator.isNameValid("Pavlo") }
            verify { validator.isEmailValid("wrong") }
        }
    }

    @Nested
    @DisplayName("when email is provided")
    inner class EmailValidation {

        @Test
        @DisplayName("returns InvalidEmail when email is invalid")
        fun returnsInvalidEmailWhenEmailIsInvalid() {
            every { validator.isEmailValid("test") } returns false

            val result = useCase(email = "test")

            assertEquals(ValidationError.InvalidEmail, result)
            verify { validator.isEmailValid("test") }
        }

        @Test
        @DisplayName("continues validation when email is valid")
        fun continuesValidationWhenEmailIsValid() {
            every { validator.isEmailValid("pavlo@gmail.com") } returns true
            every { validator.isPasswordValid("123") } returns false

            val result = useCase(
                email = "pavlo@gmail.com",
                password = "123"
            )

            assertEquals(ValidationError.ShortPassword, result)
            verify { validator.isEmailValid("pavlo@gmail.com") }
            verify { validator.isPasswordValid("123") }
        }
    }

    @Nested
    @DisplayName("when password is provided")
    inner class PasswordValidation {

        @Test
        @DisplayName("returns ShortPassword when password is invalid")
        fun returnsShortPasswordWhenPasswordIsInvalid() {
            every { validator.isPasswordValid("123") } returns false

            val result = useCase(password = "123")

            assertEquals(ValidationError.ShortPassword, result)
            verify { validator.isPasswordValid("123") }
        }
    }

    @Nested
    @DisplayName("when all provided fields are valid")
    inner class SuccessfulValidation {

        @Test
        @DisplayName("returns null")
        fun returnsNullWhenAllFieldsAreValid() {
            every { validator.isNameValid("Pavlo") } returns true
            every { validator.isEmailValid("pavlo@gmail.com") } returns true
            every { validator.isPasswordValid("12345678") } returns true

            val result = useCase(
                name = "Pavlo",
                email = "pavlo@gmail.com",
                password = "12345678"
            )

            assertNull(result)
            verify { validator.isNameValid("Pavlo") }
            verify { validator.isEmailValid("pavlo@gmail.com") }
            verify { validator.isPasswordValid("12345678") }
        }

        @Test
        @DisplayName("returns null when all fields are null")
        fun returnsNullWhenAllFieldsAreNull() {
            val result = useCase()

            assertNull(result)
        }
    }

    @Nested
    @DisplayName("validation priority")
    inner class ValidationPriority {

        @Test
        @DisplayName("returns EmptyName when name, email and password are invalid")
        fun returnsEmptyNameWhenAllFieldsAreInvalid() {
            every { validator.isNameValid("") } returns false

            val result = useCase(
                name = "",
                email = "wrong",
                password = "123"
            )

            assertEquals(ValidationError.EmptyName, result)
            verify { validator.isNameValid("") }
        }

        @Test
        @DisplayName("returns InvalidEmail when name is valid but email and password are invalid")
        fun returnsInvalidEmailWhenEmailAndPasswordAreInvalid() {
            every { validator.isNameValid("Pavlo") } returns true
            every { validator.isEmailValid("wrong") } returns false

            val result = useCase(
                name = "Pavlo",
                email = "wrong",
                password = "123"
            )

            assertEquals(ValidationError.InvalidEmail, result)
            verify { validator.isNameValid("Pavlo") }
            verify { validator.isEmailValid("wrong") }
        }

        @Test
        @DisplayName("returns ShortPassword when only password is invalid")
        fun returnsShortPasswordWhenOnlyPasswordIsInvalid() {
            every { validator.isNameValid("Pavlo") } returns true
            every { validator.isEmailValid("pavlo@gmail.com") } returns true
            every { validator.isPasswordValid("123") } returns false

            val result = useCase(
                name = "Pavlo",
                email = "pavlo@gmail.com",
                password = "123"
            )

            assertEquals(ValidationError.ShortPassword, result)
            verify { validator.isNameValid("Pavlo") }
            verify { validator.isEmailValid("pavlo@gmail.com") }
            verify { validator.isPasswordValid("123") }
        }
    }
}