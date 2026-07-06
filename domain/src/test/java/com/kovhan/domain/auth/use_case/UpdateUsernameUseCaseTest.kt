package com.kovhan.domain.auth.use_case

import com.kovhan.domain.auth.UserRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UpdateUsernameUseCase")
class UpdateUsernameUseCaseTest {

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val useCase = UpdateUsernameUseCase(userRepository)

    @ParameterizedTest(name = "normalizes \"{0}\" to \"{1}\"")
    @MethodSource("usernameCases")
    @DisplayName("trims whitespace and strips a leading @ before saving")
    fun normalizesUsername(input: String, expected: String) = runTest {
        useCase(input)

        coVerify { userRepository.setUsername(expected) }
    }

    fun usernameCases(): Stream<Arguments> = Stream.of(
        Arguments.of("john", "john"),
        Arguments.of("  john  ", "john"),
        Arguments.of("@john", "john"),
        Arguments.of("  @john  ", "john"),
        Arguments.of("@@john", "john"),
    )
}
